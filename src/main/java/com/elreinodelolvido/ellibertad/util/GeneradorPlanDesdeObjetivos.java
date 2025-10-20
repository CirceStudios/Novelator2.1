package com.elreinodelolvido.ellibertad.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.elreinodelolvido.ellibertad.api.DeepSeekClient;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;

/**
 * GeneradorPlanDesdeObjetivos — Genera plan funcional y estructurado a partir
 * de los objetivos definidos por el usuario y el estado del sistema, invocando a DeepSeek.
 */
public class GeneradorPlanDesdeObjetivos {

    private static final String PLAN_DIR = "software/";
    private static final String PLAN_MD = "PLAN_MD";
    private static final String PLAN_JSON = "PLAN_JSON";
    private static final String CLASSES_PLAN = "CLASSES_PLAN";

    public static void generar(String resumenGlobal, String objetivosTexto) {
        System.out.println("🧭 Generando plan funcional desde objetivos...");

        String prompt = construirPrompt(resumenGlobal, objetivosTexto);
        String respuesta = new DeepSeekClient().enviarPromptTecnico(prompt);

        if (respuesta == null || respuesta.isBlank()) {
            System.err.println("❌ No se pudo generar el plan desde los objetivos.");
            return;
        }

        Path planDir = Paths.get(PLAN_DIR);
        try {
            Files.createDirectories(planDir);

            writeToFileIfNotNull(planDir, PLAN_MD, extraerBloque(respuesta, PLAN_MD));
            writeToFileIfNotNull(planDir, PLAN_JSON, extraerBloque(respuesta, PLAN_JSON));
            writeToFileIfNotNull(planDir, CLASSES_PLAN, extraerBloque(respuesta, CLASSES_PLAN));

        } catch (Exception e) {
            System.err.println("💥 Error guardando los archivos de plan.");
            e.printStackTrace();
        }
    }

    private static void writeToFileIfNotNull(Path planDir, String fileName, String content) {
        if (content != null) {
            FileUtils.writeToFile(planDir.resolve(fileName).toString(), content);
            System.out.println("📄 " + fileName + " generado.");
        }
    }

    private static String construirPrompt(String resumen, String objetivos) {
        final int RESUMEN_TRUNC_LENGTH = 4000;
        final int OBJETIVOS_TRUNC_LENGTH = 2000;
        final int LISTADO_CLASES_TRUNC_LENGTH = 4000;
        final int PLAN_MD_TRUNC_LENGTH = 3000;
        final int PLAN_JSON_TRUNC_LENGTH = 3000;

        String resumenTrunc = trunc(resumen, RESUMEN_TRUNC_LENGTH);
        String objetivosTrunc = trunc(objetivos, OBJETIVOS_TRUNC_LENGTH);
        String listadoClases = null;
		try {
			listadoClases = trunc(ProjectScanner.generarResumenCompleto(), LISTADO_CLASES_TRUNC_LENGTH);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String planMd = trunc(FileUtils.readFileIfExists(PLAN_DIR + "plan.md"), PLAN_MD_TRUNC_LENGTH);
        String planJson = trunc(FileUtils.readFileIfExists(PLAN_DIR + "plan.json"), PLAN_JSON_TRUNC_LENGTH);

        return buildPrompt(resumenTrunc, objetivosTrunc, listadoClases, planMd, planJson);
    }

    private static String buildPrompt(String resumenTrunc, String objetivosTrunc, String listadoClases, String planMd, String planJson) {
        return
            "Eres un generador experto de estructuras de software modular en Java.\n" +
            "Tu misión es analizar el estado actual del sistema, los objetivos de evolución y el plan funcional propuesto,\n" +
            "y generar el código completo de las nuevas clases necesarias para cumplir la actualización.\n" +
            "=== RESUMEN TÉCNICO Y NARRATIVO DEL SISTEMA ===\n" +
            resumenTrunc + "\n" +
            "=== OBJETIVOS DEFINIDOS POR EL USUARIO ===\n" +
            objetivosTrunc + "\n" +
            "=== CLASES EXISTENTES EN EL PROYECTO ===\n" +
            listadoClases + "\n" +
            "=== PLAN FUNCIONAL (plan.md) ===\n" +
            (planMd.isEmpty() ? "(sin plan.md)" : planMd) + "\n" +
            "=== PLAN DETALLADO (plan.json) ===\n" +
            (planJson.isEmpty() ? "(sin plan.json)" : planJson) + "\n" +
            "=== INSTRUCCIONES ===\n" +
            "- Devuelve tres bloques marcados así: [PLAN_MD]...[/PLAN_MD], [PLAN_JSON]...[/PLAN_JSON], [CLASSES_PLAN]...[/CLASSES_PLAN]\n" +
            "- Crea únicamente clases nuevas (no repitas las existentes).\n" +
            "- Usa `package com.novelator.[dominio]` en cada clase.\n" +
            "- No expliques nada. Solo devuelve los bloques.\n";
    }

    private static String extraerBloque(String texto, String etiqueta) {
        String ini = "[" + etiqueta + "]";
        String fin = "[/" + etiqueta + "]";

        int start = texto.indexOf(ini);
        int end = texto.indexOf(fin);

        if (start == -1 || end == -1 || start >= end) return null;
        return texto.substring(start + ini.length(), end).trim();
    }

    private static String trunc(String input, int max) {
        if (input == null) return "";
        return input.length() > max ? input.substring(0, max - 20) + "\n[...TRUNCADO...]" : input;
    }
}