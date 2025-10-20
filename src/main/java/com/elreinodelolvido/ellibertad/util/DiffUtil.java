package com.elreinodelolvido.ellibertad.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// 🎯 IMPORTS TURBO PARA LA LIBRERÍA DE DIFF
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;

/**
 * ⚡ TURBO-DIFF — ¡COMPARACIÓN DE CÓDIGO A VELOCIDAD DE RAYO!
 * 🏴‍☠️ Encuentra diferencias más rápido que un pirata encontrando oro.
 */
public class DiffUtil {

    // 🎯 CACHE TURBO - Porque leer dos veces es de cobardes
    private static final ConcurrentHashMap<String, List<String>> CACHE_ARCHIVOS = new ConcurrentHashMap<>();
    
    // 📊 ESTADÍSTICAS ÉPICAS
    private static final AtomicInteger CONTADOR_DIFFS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_ERRORES = new AtomicInteger(0);

    /**
     * ⚡ GENERAR DIFF TURBO-EXTREMO
     * Encuentra diferencias antes de que te des cuenta de que las necesitas
     */
    public static String generarDiff(String originalPath, String refactorPath) {
        try {
            String original = FileUtils.readFile(originalPath);
            String refactor = FileUtils.readFile(refactorPath);
            
            if (original == null || refactor == null) {
                return "❌ No se pudieron leer los archivos para comparar";
            }
            
            return generarDiffContenido(original, refactor, 
                Paths.get(originalPath).getFileName().toString(),
                Paths.get(refactorPath).getFileName().toString());
                
        } catch (Exception e) {
            return "💥 Error generando diff: " + e.getMessage();
        }
    }
    
    private static String generarDiffContenido(String original, String refactor, 
                                             String nombreOriginal, String nombreRefactor) {
        StringBuilder diff = new StringBuilder();
        diff.append("🏴‍☠️ COMPARACIÓN TURBO: ").append(nombreOriginal).append(" vs ").append(nombreRefactor).append("\n");
        diff.append("═".repeat(80)).append("\n\n");
        
        String[] lineasOriginal = original.split("\n");
        String[] lineasRefactor = refactor.split("\n");
        
        int maxLines = Math.max(lineasOriginal.length, lineasRefactor.length);
        int cambios = 0;
        
        for (int i = 0; i < maxLines; i++) {
            String lineaOriginal = i < lineasOriginal.length ? lineasOriginal[i] : "";
            String lineaRefactor = i < lineasRefactor.length ? lineasRefactor[i] : "";
            
            if (!lineaOriginal.equals(lineaRefactor)) {
                cambios++;
                diff.append("🎯 CAMBIO #").append(cambios).append("\n");
                diff.append("📍 Línea: ").append(i + 1).append("\n");
                
                if (lineaOriginal.isEmpty()) {
                    diff.append("🎪 Tipo: INSERT\n");
                    diff.append("➕ ").append(lineaRefactor).append("\n");
                } else if (lineaRefactor.isEmpty()) {
                    diff.append("🎪 Tipo: DELETE\n");
                    diff.append("➖ ").append(lineaOriginal).append("\n");
                } else {
                    diff.append("🎪 Tipo: CHANGE\n");
                    diff.append("🔹 ORIGINAL: ").append(lineaOriginal).append("\n");
                    diff.append("🔸 REFACTOR: ").append(lineaRefactor).append("\n");
                }
                diff.append("─".repeat(40)).append("\n");
            }
        }
        
        diff.append("\n📊 RESUMEN: ").append(cambios).append(" cambios detectados\n");
        diff.append("📏 Líneas original: ").append(lineasOriginal.length).append("\n");
        diff.append("📏 Líneas refactor: ").append(lineasRefactor.length).append("\n");
        
        return diff.toString();
    }


    /**
     * 🚀 CARGA TURBO CON CACHE INFERNAL
     */
    private static List<String> cargarLineasTurbo(String filePath) throws IOException {
        return CACHE_ARCHIVOS.computeIfAbsent(filePath, k -> {
            try {
                List<String> lineas = Files.readAllLines(Path.of(filePath));
                System.out.println("📁 Turbo-cargado: " + filePath + " (" + lineas.size() + " líneas)");
                return lineas;
            } catch (IOException e) {
                throw new RuntimeException("💥 Error turbo-cargando: " + filePath, e);
            }
        });
    }

    /**
     * 🎪 CONSTRUYE UN DIFF TAN ÉPICO QUE LLORARÁS
     */
    private static String construirDiffEpico(Patch<String> patch, String originalPath, String refactorPath) {
        StringBuilder diff = new StringBuilder();
        
        // 🎭 ENCABEZADO ÉPICO
        diff.append("⚡ TURBO-DIFF REPORT\n");
        diff.append("📊 ").append(patch.getDeltas().size()).append(" cambios detectados\n");
        diff.append("📁 Original: ").append(originalPath).append("\n");
        diff.append("🔧 Refactor: ").append(refactorPath).append("\n");
        diff.append("=".repeat(60)).append("\n\n");

        // 🎯 PROCESAR DELTAS CON ESTILO
        AtomicInteger contadorCambios = new AtomicInteger(1);
        
        for (AbstractDelta<String> delta : patch.getDeltas()) {
            diff.append("🎯 CAMBIO #").append(contadorCambios.getAndIncrement()).append("\n");
            diff.append("📍 Posición: Línea ").append(delta.getSource().getPosition()).append("\n");
            diff.append("🎪 Tipo: ").append(delta.getType().toString().toUpperCase()).append("\n");
            diff.append("-".repeat(40)).append("\n");

            // 📝 LÍNEAS ORIGINALES (ROJO)
            if (!delta.getSource().getLines().isEmpty()) {
                diff.append("❌ ORIGINAL:\n");
                delta.getSource().getLines().forEach(line -> 
                    diff.append("   - ").append(escaparLinea(line)).append("\n"));
            }

            // 📝 LÍNEAS NUEVAS (VERDE)  
            if (!delta.getTarget().getLines().isEmpty()) {
                diff.append("✅ REFACTOR:\n");
                delta.getTarget().getLines().forEach(line -> 
                    diff.append("   + ").append(escaparLinea(line)).append("\n"));
            }

            diff.append("\n");
        }

        // 📊 RESUMEN ÉPICO
        diff.append("🎊 RESUMEN TURBO-DIFF:\n");
        diff.append("✨ Total cambios: ").append(patch.getDeltas().size()).append("\n");
        diff.append("🏁 Fin del reporte épico\n");

        return diff.toString();
    }

    /**
     * 🎨 ESCAPE ÉPICO PARA LÍNEAS PROBLEMÁTICAS
     */
    private static String escaparLinea(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            return "[LÍNEA VACÍA]";
        }
        // 🚀 Escape básico para caracteres raros
        return linea.replace("\t", "    ")
                   .replace("\r", "")
                   .replace("\0", "\\\\0");
    }

    /**
     * 📊 ESTADÍSTICAS TURBO-DIFF
     */
    public static void mostrarEstadisticasEpicas() {
        System.out.println("\n📊 ESTADÍSTICAS TURBO-DIFF:");
        System.out.println("⚡ Diffs realizados: " + CONTADOR_DIFFS.get());
        System.out.println("💥 Errores: " + CONTADOR_ERRORES.get());
        System.out.println("💾 Archivos en cache: " + CACHE_ARCHIVOS.size());
        System.out.println("🎯 Tasa de éxito: " + 
            String.format("%.1f%%", (1 - (double)CONTADOR_ERRORES.get() / CONTADOR_DIFFS.get()) * 100));
    }

    /**
     * 🧹 LIMPIAR CACHE TURBO
     */
    public static void limpiarCache() {
        int tamaño = CACHE_ARCHIVOS.size();
        CACHE_ARCHIVOS.clear();
        System.out.println("🗑️ Cache turbo-diff limpiado (" + tamaño + " archivos)");
    }

    /**
     * 🚀 COMPARACIÓN RÁPIDA SIN CACHE (para valientes)
     */
    public static String diffExpress(String originalPath, String refactorPath) {
        try {
            List<String> original = Files.readAllLines(Path.of(originalPath));
            List<String> refactor = Files.readAllLines(Path.of(refactorPath));
            
            Patch<String> patch = DiffUtils.diff(original, refactor);
            
            return patch.getDeltas().isEmpty() 
                ? "// ✅ IDÉNTICO" 
                : "// 🔄 " + patch.getDeltas().size() + " cambios detectados";
                
        } catch (Exception e) {
            return "// ❌ Error express: " + e.getMessage();
        }
    }
    
    /**
     * 🆕 MÉTODO TURBO: GENERAR DIFF ENTRE STRINGS DIRECTAMENTE
     */
    public static String generarDiffDesdeStrings(String contenidoOriginal, String contenidoRefactor, String nombreOriginal, String nombreRefactor) {
        try {
            List<String> original = List.of(contenidoOriginal.split("\n"));
            List<String> refactor = List.of(contenidoRefactor.split("\n"));
            
            Patch<String> patch = DiffUtils.diff(original, refactor);
            
            if (patch.getDeltas().isEmpty()) {
                return "// 🎉 ¡CONTENIDO IDÉNTICO!";
            }
            
            StringBuilder diff = new StringBuilder();
            diff.append("⚡ TURBO-DIFF ENTRE STRINGS\n");
            diff.append("📊 ").append(patch.getDeltas().size()).append(" cambios\n");
            diff.append("📁 ").append(nombreOriginal).append(" vs ").append(nombreRefactor).append("\n");
            diff.append("=".repeat(50)).append("\n");
            
            for (AbstractDelta<String> delta : patch.getDeltas()) {
                diff.append("📍 Línea ").append(delta.getSource().getPosition()).append(": ")
                   .append(delta.getType()).append("\n");
                
                delta.getSource().getLines().forEach(line -> diff.append("- ").append(line).append("\n"));
                delta.getTarget().getLines().forEach(line -> diff.append("+ ").append(line).append("\n"));
                diff.append("\n");
            }
            
            return diff.toString();
            
        } catch (Exception e) {
            return "// 💥 Error en diff de strings: " + e.getMessage();
        }
    }
}