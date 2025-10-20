package com.elreinodelolvido.ellibertad.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * KrakensAnalyzer — analiza los logs de errores (krakens) y genera
 * un informe técnico y simbólico en formato Markdown.
 */
public class KrakensAnalyzer {

    private static final Path LOG_PATH = Path.of("autogen-output/krakens/log-krakens.md");
    private static final Path OUT_PATH = Path.of("autogen-output/krakens/informe-krakens.md");

    /**
     * Analiza el log de krakens y devuelve un informe Markdown completo.
     */
    public static String generarInforme() {
    	
    	Path logPath = Path.of("autogen-output/krakens/log-krakens.md");
    	
    	if (!Files.exists(logPath)) {
            System.out.println("⚠️ No se encontró el archivo log-krakens.md. No se registraron krakens.");
            return "🟢 No se encontraron krakens durante esta travesía.\n";
        }

        try {
            List<String> lineas = Files.readAllLines(LOG_PATH);
            Map<String, List<String>> clasesPorTipo = new HashMap<>();
            Map<String, Integer> conteo = new HashMap<>();

            Pattern patronTipo = Pattern.compile("💀|🌪️|🐉");
            Pattern patronClase = Pattern.compile("Clase:\\s+(\\S+)");

            for (String linea : lineas) {
                Matcher matcherTipo = patronTipo.matcher(linea);
                Matcher matcherClase = patronClase.matcher(linea);

                String tipo = matcherTipo.find() ? matcherTipo.group() : null;
                String clase = matcherClase.find() ? matcherClase.group(1) : null;

                if (tipo != null && clase != null) {
                    clasesPorTipo.computeIfAbsent(tipo, k -> new ArrayList<>()).add(clase);
                    conteo.put(tipo, conteo.getOrDefault(tipo, 0) + 1);
                }
            }

            StringBuilder informe = new StringBuilder();
            informe.append("# 🧜‍♂️ Informe de Krakens enfrentados\n\n");

            informe.append("## 📊 Resumen por tipo de Kraken\n");
            informe.append("- 💀 Fantasmas: ").append(conteo.getOrDefault("💀", 0)).append("\n");
            informe.append("- 🌪️ Tormentas: ").append(conteo.getOrDefault("🌪️", 0)).append("\n");
            informe.append("- 🐉 Leviatanes: ").append(conteo.getOrDefault("🐉", 0)).append("\n\n");

            for (Map.Entry<String, List<String>> entry : clasesPorTipo.entrySet()) {
                String tipo = entry.getKey();
                List<String> clases = entry.getValue();
                informe.append("### ").append(tipo).append(" ").append(nombreTipo(tipo)).append("\n");
                for (String clase : clases) {
                    informe.append("- ").append(clase).append("\n");
                }
                informe.append("\n");
            }

            return informe.toString();

        } catch (IOException e) {
            System.err.println("💥 Error generando informe de krakens: " + e.getMessage());
            return "⚠️ Error generando informe de krakens: " + e.getMessage() + "\n";
        }
    }

    /**
     * Genera el archivo Markdown a partir del análisis de krakens.
     */
    public static void generarInformeMarkdown() {
        String informe = generarInforme();
        FileUtils.writeToFile(OUT_PATH.toString(), informe);
        System.out.println("📜 Informe de krakens generado: " + OUT_PATH);
    }

    private static String nombreTipo(String simbolo) {
        return switch (simbolo) {
            case "💀" -> "Fantasmas";
            case "🌪️" -> "Tormentas";
            case "🐉" -> "Leviatanes";
            default -> "Desconocido";
        };
    }
}