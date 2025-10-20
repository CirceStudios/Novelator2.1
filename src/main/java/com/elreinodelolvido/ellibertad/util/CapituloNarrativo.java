package com.elreinodelolvido.ellibertad.util;



import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.UUID;

import com.elreinodelolvido.ellibertad.api.DeepSeekClient;

/**
 * CapituloNarrativo — controla la generación de historias encadenadas por ejecución.
 * Cada ejecución es un nuevo capítulo de la saga pirata del sistema.
 */
public class CapituloNarrativo {

    private static final String BITACORA_GLOBAL = "autogen-output/bitacora-pirata.md";
    private static final String CAPITULO_ACTUAL = "autogen-output/bitacora-pirata/narrativa-final.md";
    private static final String RESUMEN_CAPITULOS = "autogen-output/bitacora-pirata/capitulos.jsonl";

    public static void generarNuevoCapitulo(String objetivos, String resumen, String krakens) {
        try {
            System.out.println("\n📖 Forjando un nuevo capítulo de la leyenda...");

            String fecha = LocalDateTime.now().toString();
            String prompt = construirPromptEncadenado(objetivos, resumen, krakens);

            DeepSeekClient oraculo = new DeepSeekClient();
            String respuesta = oraculo.enviarPromptTecnico(prompt);
            String historia = respuesta; // 🎉 Así de simple

            // Guardar capítulo como markdown
            String id = UUID.randomUUID().toString().substring(0, 8);
            String archivoCapitulo = "autogen-output/bitacora-pirata/capitulo-" + id + ".md";
            FileUtils.writeToFile(archivoCapitulo, historia);
            FileUtils.appendToFile(BITACORA_GLOBAL, "\n\n" + historia);

            // Guardar resumen del capítulo
            String titulo = extraerTitulo(historia);
            FileUtils.appendToFile(RESUMEN_CAPITULOS, "{" +
                "\"id\":\"" + id + "\"," +
                "\"fecha\":\"" + fecha + "\"," +
                "\"titulo\":\"" + titulo.replaceAll("\"", "'") + "\"}\\n");

            // Mostrar
            System.out.println("✅ Capítulo generado y registrado: " + archivoCapitulo);
            System.out.println("📚 Título: " + titulo);

        } catch (Exception e) {
            System.err.println("💥 Error generando capítulo narrativo encadenado");
            e.printStackTrace();

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            FileUtils.registrarKraken("CapituloNarrativo", sw.toString());
        }
    }

    private static String construirPromptEncadenado(String objetivos, String resumen, String krakens) {
        String resumenHistorico = FileUtils.readFile(RESUMEN_CAPITULOS);
        return """
            🏴‍☠️ Eres Gustavo Adolfo Bécquer, cronista del navío *Libertad*. Esta historia debe continuar una saga pirata previa,
            construida a lo largo de múltiples capítulos.

            🌊 Cada ejecución del sistema AUTOGEN es una nueva travesía en el Reino del Olvido.
            Tu tarea es contar, con estilo literario y simbólico, lo que ha ocurrido en esta última ejecución,
            usando los eventos técnicos como semillas narrativas.

            === OBJETIVOS DE LA TRAVESÍA ===
            %s

            === TRIPULACIÓN Y SISTEMA ACTUAL ===
            %s

            === KRAKENS ENFRENTADOS ===
            %s

            === HISTORIA NARRADA HASTA AHORA ===
            %s

            🔮 Escribe solo la nueva sección que continúa esta historia, como si fuera el siguiente capítulo de una novela pirata.
            Introduce título y relato.
            No expliques lo que haces, solo narra.
            """.formatted(objetivos, resumen, krakens, resumenHistorico);
    }

    private static String extraerTitulo(String texto) {
        String[] lines = texto.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("#")) return line.replace("#", "").trim();
        }
        return "Sin título";
    }
}