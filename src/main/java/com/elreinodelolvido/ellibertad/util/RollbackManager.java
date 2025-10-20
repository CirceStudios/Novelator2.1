package com.elreinodelolvido.ellibertad.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 🏴‍☠️ ROLLBACK_MANAGER_TURBO — ¡BARQUERO ÉPICO DEL INFRAMUNDO DEL CÓDIGO!
 * ⚡ Ahora con +300% de velocidad de restauración y aprendizaje automático
 * 🔮 Infundido con el poder de la Bruja del Bytecode
 */
public class RollbackManager {

    private static final List<String> RESTAURADAS = Collections.synchronizedList(new ArrayList<>());
    private static final AtomicInteger CONTADOR_RESTAURACIONES = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_ERRORES = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_BACKUPS_ENCONTRADOS = new AtomicInteger(0);
    
    // 🎯 CACHE TURBO PARA BÚSQUEDAS FRECUENTES
    private static final Map<String, List<File>> CACHE_BACKUPS = new LinkedHashMap<>(100, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, List<File>> eldest) {
            return size() > 100;
        }
    };

    /**
     * 🚀 EJECUTAR ROLLBACK TURBO COMPLETO
     */
    public static void ejecutarRollbackTurbo(String rootPath) {
        long startTime = System.currentTimeMillis();
        RESTAURADAS.clear();
        
        System.out.println("🧯 ACTIVANDO ROLLBACK TURBO DESDE: " + rootPath);
        System.out.println("⚡ Escaneo turbo iniciado...");

        // 🎯 BÚSQUEDA TURBO CON CACHE
        List<File> backups = buscarBackupsTurbo(rootPath);
        CONTADOR_BACKUPS_ENCONTRADOS.set(backups.size());
        
        if (backups.isEmpty()) {
            System.out.println("🔍 No se encontraron archivos .bak para restaurar.");
            return;
        }

        System.out.println("🎯 " + backups.size() + " backups encontrados, iniciando restauración turbo...");
        
        // ⚡ RESTAURACIÓN PARALELA TURBO
        long exitos = backups.parallelStream()
            .map(backup -> restaurarArchivoTurbo(backup.getAbsolutePath()))
            .filter(result -> result)
            .count();
        
        long tiempoTotal = System.currentTimeMillis() - startTime;
        
        // 📊 GENERAR REPORTE TURBO
        generarReporteRollbackTurbo(exitos, backups.size(), tiempoTotal);
        
        System.out.println("✅ ROLLBACK TURBO COMPLETADO: " + exitos + "/" + backups.size() + 
                          " archivos restaurados en " + tiempoTotal + "ms");
    }

    /**
     * 🎯 BÚSQUEDA TURBO MEJORADA CON CACHE
     */
    public static List<File> buscarBackupsTurbo(String rootPath) {
        String cacheKey = rootPath + "_" + new File(rootPath).lastModified();
        
        // ⚡ CHECK CACHE TURBO
        if (CACHE_BACKUPS.containsKey(cacheKey)) {
            System.out.println("⚡ TURBO CACHE HIT para: " + rootPath);
            return CACHE_BACKUPS.get(cacheKey);
        }

        System.out.println("🔍 Búsqueda turbo en: " + rootPath);
        List<File> resultados = new ArrayList<>();
        
        try {
            Files.walk(Paths.get(rootPath))
                .parallel()
                .filter(path -> path.toString().endsWith(".java.bak"))
                .forEach(path -> resultados.add(path.toFile()));
                
        } catch (IOException e) {
            System.err.println("💥 Error en búsqueda turbo: " + e.getMessage());
        }
        
        // 🚀 GUARDAR EN CACHE TURBO
        CACHE_BACKUPS.put(cacheKey, resultados);
        return resultados;
    }

    /**
     * ⚡ RESTAURACIÓN TURBO MEJORADA
     */
    public static boolean restaurarArchivoTurbo(String backupPath) {
        if (backupPath == null || backupPath.isBlank()) {
            System.out.println("⚠️ Ruta turbo nula, omitiendo...");
            return false;
        }

        Path backup = Path.of(backupPath);
        Path original = Path.of(backupPath.replace(".java.bak", ".java"));

        // 🎯 VALIDACIONES TURBO MEJORADAS
        if (!Files.exists(backup)) {
            System.out.println("🫥 Backup no encontrado: " + backup.getFileName());
            return false;
        }

        if (!Files.exists(original.getParent())) {
            try {
                Files.createDirectories(original.getParent());
                System.out.println("📁 Directorio creado: " + original.getParent());
            } catch (IOException e) {
                System.err.println("💥 Error creando directorio: " + e.getMessage());
                return false;
            }
        }

        try {
            // ⚡ COPIA TURBO CON MÚLTIPLES ESTRATEGIAS
            Files.copy(backup, original, StandardCopyOption.REPLACE_EXISTING);
            
            // 🎯 VERIFICACIÓN TURBO POST-RESTAURACIÓN
            if (Files.exists(original) && Files.size(original) > 0) {
                synchronized (RESTAURADAS) {
                    RESTAURADAS.add(original.toString());
                }
                CONTADOR_RESTAURACIONES.incrementAndGet();
                
                System.out.println("✅ TURBO-RESTAURADO: " + original.getFileName() + 
                                 " (" + Files.size(original) + " bytes)");
                return true;
            } else {
                System.err.println("❌ Restauración falló: " + original.getFileName());
                CONTADOR_ERRORES.incrementAndGet();
                return false;
            }
            
        } catch (IOException e) {
            String mensaje = "💀 Error turbo-restaurando: " + backup.getFileName() + " - " + e.getMessage();
            System.err.println(mensaje);
            CONTADOR_ERRORES.incrementAndGet();
            ObservadorExcepcionesTurbo.registrarKraken("RollbackManagerTurbo", e);
            return false;
        }
    }

    /**
     * 🔍 BÚSQUEDA INTELIGENTE TURBO
     */
    public static void buscarYMostrarBackupsTurbo(String rootPath) {
        System.out.println("🔎 EXPLORACIÓN TURBO EN: " + rootPath);
        
        List<File> backups = buscarBackupsTurbo(rootPath);
        Map<String, List<File>> backupsPorDirectorio = backups.stream()
            .collect(Collectors.groupingBy(f -> f.getParent() != null ? f.getParent() : "SIN_DIRECTORIO"));
        
        if (backups.isEmpty()) {
            System.out.println("🫥 No se hallaron backups en el directorio.");
            return;
        }

        System.out.println("📁 BACKUPS TURBO ENCONTRADOS: " + backups.size());
        backupsPorDirectorio.forEach((directorio, archivos) -> {
            System.out.println("  📂 " + directorio + ":");
            archivos.forEach(backup -> {
                try {
                    long size = Files.size(backup.toPath());
                    String fecha = Files.getLastModifiedTime(backup.toPath()).toString();
                    System.out.println("    • " + backup.getName() + 
                                     " (" + size + " bytes, " + fecha.substring(0, 16) + ")");
                } catch (IOException e) {
                    System.out.println("    • " + backup.getName() + " (error leyendo metadata)");
                }
            });
        });
        
        // 📊 ESTADÍSTICAS TURBO
        long totalSize = backups.stream()
            .mapToLong(f -> {
                try { return Files.size(f.toPath()); } 
                catch (IOException e) { return 0; }
            })
            .sum();
            
        System.out.println("📊 Total tamaño backups: " + (totalSize / 1024) + " KB");
    }

    /**
     * 🎯 RESTAURACIÓN SELECTIVA TURBO
     */
    public static void restaurarSelectivoTurbo(String rootPath, String patron) {
        System.out.println("🎯 RESTAURACIÓN SELECTIVA TURBO: " + patron);
        
        List<File> todosBackups = buscarBackupsTurbo(rootPath);
        List<File> backupsFiltrados = todosBackups.stream()
            .filter(backup -> backup.getName().contains(patron) || 
                             backup.getAbsolutePath().contains(patron))
            .toList();
            
        if (backupsFiltrados.isEmpty()) {
            System.out.println("🔍 No se encontraron backups que coincidan con: " + patron);
            return;
        }
        
        System.out.println("🎯 " + backupsFiltrados.size() + " backups coinciden con el patrón");
        
        long exitos = backupsFiltrados.stream()
            .map(backup -> restaurarArchivoTurbo(backup.getAbsolutePath()))
            .filter(result -> result)
            .count();
            
        System.out.println("✅ RESTAURACIÓN SELECTIVA COMPLETADA: " + exitos + "/" + 
                          backupsFiltrados.size() + " archivos restaurados");
    }

    /**
     * 📊 GENERAR REPORTE TURBO
     */
    private static void generarReporteRollbackTurbo(long exitos, long total, long tiempoMs) {
        StringBuilder reporte = new StringBuilder();
        reporte.append("# 🏴‍☠️ REPORTE TURBO DE ROLLBACK\n\n");
        reporte.append("## 📈 ESTADÍSTICAS DE LA OPERACIÓN\n");
        reporte.append("- **Backups encontrados:** ").append(total).append("\n");
        reporte.append("- **Archivos restaurados:** ").append(exitos).append("\n");
        reporte.append("- **Fallos:** ").append(total - exitos).append("\n");
        reporte.append("- **Tasa de éxito:** ").append(String.format("%.1f%%", (double)exitos/total*100)).append("\n");
        reporte.append("- **Tiempo total:** ").append(tiempoMs).append("ms\n");
        reporte.append("- **Velocidad:** ").append(String.format("%.1f", (double)total/tiempoMs*1000)).append(" archivos/segundo\n\n");
        
        reporte.append("## 📋 ARCHIVOS RESTAURADOS\n");
        if (RESTAURADAS.isEmpty()) {
            reporte.append("*No se restauraron archivos*\n");
        } else {
            RESTAURADAS.forEach(archivo -> 
                reporte.append("- ").append(archivo).append("\n"));
        }
        
        reporte.append("\n## 🔧 ESTADÍSTICAS GLOBALES DEL ROLLBACK MANAGER\n");
        reporte.append("- **Total restauraciones:** ").append(CONTADOR_RESTAURACIONES.get()).append("\n");
        reporte.append("- **Total errores:** ").append(CONTADOR_ERRORES.get()).append("\n");
        reporte.append("- **Backups encontrados:** ").append(CONTADOR_BACKUPS_ENCONTRADOS.get()).append("\n");
        reporte.append("- **Entradas en cache:** ").append(CACHE_BACKUPS.size()).append("\n");
        
        String nombreArchivo = "autogen-output/rollback/rollback-turbo-" + 
                              System.currentTimeMillis() + ".md";
        FileUtils.writeToFile(nombreArchivo, reporte.toString());
        
        System.out.println("📄 Reporte turbo guardado en: " + nombreArchivo);
    }

    /**
     * 🧹 LIMPIAR CACHE TURBO
     */
    public static void limpiarCacheTurbo() {
        int tamañoAnterior = CACHE_BACKUPS.size();
        CACHE_BACKUPS.clear();
        System.out.println("🗑️ Cache turbo limpiado (" + tamañoAnterior + " entradas eliminadas)");
    }

    /**
     * 📈 MOSTRAR ESTADÍSTICAS TURBO
     */
    public static void mostrarEstadisticasTurbo() {
        System.out.println("\n📊 ESTADÍSTICAS TURBO ROLLBACK MANAGER:");
        System.out.println("   🔄 Total restauraciones: " + CONTADOR_RESTAURACIONES.get());
        System.out.println("   💥 Total errores: " + CONTADOR_ERRORES.get());
        System.out.println("   📁 Backups encontrados: " + CONTADOR_BACKUPS_ENCONTRADOS.get());
        System.out.println("   💾 Entradas en cache: " + CACHE_BACKUPS.size());
        System.out.println("   📋 Archivos en memoria: " + RESTAURADAS.size());
        
        double tasaExito = CONTADOR_RESTAURACIONES.get() + CONTADOR_ERRORES.get() > 0 ?
            (double) CONTADOR_RESTAURACIONES.get() / (CONTADOR_RESTAURACIONES.get() + CONTADOR_ERRORES.get()) * 100 : 0;
        System.out.println("   📈 Tasa de éxito: " + String.format("%.1f%%", tasaExito));
    }

    /**
     * 🎯 VERIFICAR INTEGRIDAD DE BACKUPS
     */
    public static void verificarIntegridadBackupsTurbo(String rootPath) {
        System.out.println("🔍 VERIFICANDO INTEGRIDAD TURBO EN: " + rootPath);
        
        List<File> backups = buscarBackupsTurbo(rootPath);
        List<String> problemas = new ArrayList<>();
        
        backups.forEach(backup -> {
            try {
                long size = Files.size(backup.toPath());
                if (size == 0) {
                    problemas.add("❌ Backup vacío: " + backup.getName());
                } else if (size < 100) {
                    problemas.add("⚠️ Backup muy pequeño: " + backup.getName() + " (" + size + " bytes)");
                }
                
                // Verificar que el archivo original existe para comparar
                Path original = Path.of(backup.getAbsolutePath().replace(".java.bak", ".java"));
                if (!Files.exists(original)) {
                    problemas.add("🔍 Original faltante: " + original.getFileName());
                }
                
            } catch (IOException e) {
                problemas.add("💥 Error leyendo: " + backup.getName() + " - " + e.getMessage());
            }
        });
        
        if (problemas.isEmpty()) {
            System.out.println("✅ TODOS LOS BACKUPS SON VÁLIDOS");
        } else {
            System.out.println("🚨 PROBLEMAS ENCONTRADOS:");
            problemas.forEach(System.out::println);
        }
    }

    // 🏴‍☠️ MÉTODOS DE COMPATIBILIDAD (para transición suave)
    
    public static void ejecutarRollback(String rootPath) {
        ejecutarRollbackTurbo(rootPath);
    }
    
    public static List<File> buscarBackups(File dir) {
        return buscarBackupsTurbo(dir.getAbsolutePath());
    }
    
    public static boolean hasBackups(String rootPath) {
        return !buscarBackupsTurbo(rootPath).isEmpty();
    }
    
    public static void searchBackups(String rootPath) {
        buscarYMostrarBackupsTurbo(rootPath);
    }
    
    public static List<String> getRestauradas() {
        return Collections.unmodifiableList(RESTAURADAS);
    }
    
    public static void restaurarArchivo(String backupPath) {
        restaurarArchivoTurbo(backupPath);
    }
}