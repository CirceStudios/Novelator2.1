package com.elreinodelolvido.ellibertad.engine;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 🧼 Grumete Bitacora — Cronista tímido del navío Libertad.
 * Registra eventos del sistema en archivos por día, asegurando trazabilidad del viaje.
 * Ahora con +300% de robustez pirata! ⚓
 */
public class Bitacora {
    
    private static final Path BITACORA_DIR = Paths.get("autogen-output/logs");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMATO_COMPLETO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // 🚀 Thread-safe para operaciones concurrentes
    private static final List<String> eventos = new CopyOnWriteArrayList<>();
    private static final Set<String> archivosBitacora = new HashSet<>();
    
    // 🎯 Niveles de log
    public enum Nivel {
        DEBUG("🔍", "DEBUG"),
        INFO("ℹ️", "INFO"),
        AVISO("⚠️", "AVISO"),
        WARN("⚠️", "WARN"),
        ERROR("💥", "ERROR"),
        KRAKEN("🐙", "KRAKEN"),
        EXITO("✅", "EXITO"),
        TURBO("⚡", "TURBO");
        
        private final String emoji;
        private final String texto;
        
        Nivel(String emoji, String texto) {
            this.emoji = emoji;
            this.texto = texto;
        }

        public String getEmoji() { return emoji; }
        public String getTexto() { return texto; }
    }
    
    // 🔧 Configuración
    private static Nivel NIVEL_MINIMO = Nivel.INFO;
    private static boolean CONSOLA_ACTIVA = true;
    private static boolean ARCHIVO_ACTIVO = true;
    private static int MAX_EVENTOS_MEMORIA = 1000;

    /**
     * Registra un evento en la bitácora (memoria + disco).
     */
    public static void log(String mensaje) {
        log(Nivel.INFO, mensaje);
    }
    
    /**
     * Registra un evento con nivel específico.
     */
    public static void log(Nivel nivel, String mensaje) {
        if (nivel.ordinal() < NIVEL_MINIMO.ordinal()) {
            return; // Ignorar logs por debajo del nivel mínimo
        }
        
        String hora = LocalDateTime.now().format(FORMATO_HORA);
        String entrada = String.format("%s [%s] %s", nivel.getEmoji(), hora, mensaje);
        
        // 🚀 Gestión de memoria eficiente
        synchronized (eventos) {
            eventos.add(entrada);
            if (eventos.size() > MAX_EVENTOS_MEMORIA) {
                eventos.remove(0); // Mantener tamaño máximo
            }
        }
        
        // 📝 Salida a consola
        if (CONSOLA_ACTIVA) {
            System.out.println(entrada);
        }
        
        // 💾 Persistencia en archivo
        if (ARCHIVO_ACTIVO) {
            guardarEnArchivo(entrada);
        }
    }
    
    /**
     * 🚀 Métodos rápidos para niveles comunes
     */
    public static void info(String mensaje) {
        log(Nivel.INFO, mensaje);
    }
    
    public static void warn(String mensaje) {
        log(Nivel.WARN, mensaje);
    }
    
    public static void warn(String mensaje, Exception e) {
        log(Nivel.WARN, mensaje + " - " + e.getClass().getSimpleName() + ": " + e.getMessage());
        // Guardar stack trace en archivo de errores
        guardarErrorCompleto(mensaje, e);
    }
    
    public static void error(String mensaje) {
        log(Nivel.ERROR, mensaje);
    }
    
    public static void error(String mensaje, Exception e) {
        log(Nivel.ERROR, mensaje + " - " + e.getClass().getSimpleName() + ": " + e.getMessage());
        // Guardar stack trace en archivo de errores
        guardarErrorCompleto(mensaje, e);
    }
    
    public static void turbo(String mensaje) {
        log(Nivel.TURBO, "⚡ TURBO: " + mensaje);
    }
    
    public static void kraken(String mensaje) {
        log(Nivel.KRAKEN, "🐙 KRAKEN: " + mensaje);
    }
    
    public static void exito(String mensaje) {
        log(Nivel.EXITO, "✅ " + mensaje);
    }
    
    public static void debug(String mensaje) {
        log(Nivel.DEBUG, mensaje);
    }

    /**
     * 💾 Guardado robusto en archivo
     */
    private static void guardarEnArchivo(String entrada) {
        try {
            Files.createDirectories(BITACORA_DIR);
            String archivoDelDia = "bitacora-" + FORMATO_DIA.format(LocalDateTime.now()) + ".log";
            Path archivoPath = BITACORA_DIR.resolve(archivoDelDia);
            
            String entradaCompleta = LocalDateTime.now().format(FORMATO_COMPLETO) + " " + entrada + "\n";
            Files.writeString(archivoPath, entradaCompleta, 
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            
            archivosBitacora.add(archivoDelDia);
            
        } catch (IOException e) {
            // Fallback silencioso para no romper el flujo principal
            System.err.println("⚠️ Error escribiendo en bitácora: " + e.getMessage());
        }
    }
    
    /**
     * 📋 Guardado de errores completos con stack trace
     */
    private static void guardarErrorCompleto(String mensaje, Exception e) {
        try {
            Files.createDirectories(BITACORA_DIR);
            Path archivoErrores = BITACORA_DIR.resolve("errores-detallados.log");
            
            String stackTrace = getStackTrace(e);
            String errorCompleto = String.format(
                "=== ERROR [%s] ===\nMensaje: %s\nExcepción: %s\nStack Trace:\n%s\n\n",
                LocalDateTime.now().format(FORMATO_COMPLETO),
                mensaje,
                e.getClass().getName(),
                stackTrace
            );
            
            Files.writeString(archivoErrores, errorCompleto, 
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                
        } catch (IOException ioEx) {
            System.err.println("💥 Error crítico guardando error: " + ioEx.getMessage());
        }
    }
    
    /**
     * 📊 Métodos de consulta y análisis
     */
    public static List<String> getEventos() {
        return new ArrayList<>(eventos);
    }
    
    public static List<String> getEventos(Nivel nivel) {
        return eventos.stream()
            .filter(evento -> evento.contains(nivel.getEmoji()))
            .collect(Collectors.toList());
    }
    
    public static int getTotalEventos() {
        return eventos.size();
    }
    
    public static Map<Nivel, Integer> getEstadisticasNiveles() {
        Map<Nivel, Integer> stats = new EnumMap<>(Nivel.class);
        for (Nivel nivel : Nivel.values()) {
            stats.put(nivel, 0);
        }
        
        for (String evento : eventos) {
            for (Nivel nivel : Nivel.values()) {
                if (evento.contains(nivel.getEmoji())) {
                    stats.put(nivel, stats.get(nivel) + 1);
                    break;
                }
            }
        }
        
        return stats;
    }
    
    /**
     * 🧹 Métodos de mantenimiento
     */
    public static void limpiarMemoria() {
        eventos.clear();
        info("🧹 Bitácora de memoria limpiada.");
    }
    
    public static void limpiarArchivosAntiguos(int diasRetencion) {
        try {
            LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasRetencion);
            String patronArchivo = "bitacora-*.log";
            
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(BITACORA_DIR, patronArchivo)) {
                for (Path archivo : stream) {
                    String nombreArchivo = archivo.getFileName().toString();
                    String fechaStr = nombreArchivo.replace("bitacora-", "").replace(".log", "");
                    
                    try {
                        LocalDateTime fechaArchivo = LocalDateTime.parse(fechaStr + "T00:00:00");
                        if (fechaArchivo.isBefore(fechaLimite)) {
                            Files.delete(archivo);
                            info("🗑️ Archivo antiguo eliminado: " + nombreArchivo);
                        }
                    } catch (Exception e) {
                        debug("No se pudo procesar archivo: " + nombreArchivo);
                    }
                }
            }
        } catch (IOException e) {
            error("Error limpiando archivos antiguos", e);
        }
    }
    
    /**
     * ⚙️ Métodos de configuración
     */
    public static void setNivelMinimo(Nivel nivel) {
        NIVEL_MINIMO = nivel;
        info("🎚️ Nivel mínimo de log cambiado a: " + nivel.getTexto());
    }
    
    public static void setConsolaActiva(boolean activa) {
        CONSOLA_ACTIVA = activa;
        debug("Consola " + (activa ? "activada" : "desactivada"));
    }
    
    public static void setArchivoActivo(boolean activo) {
        ARCHIVO_ACTIVO = activo;
        debug("Archivo " + (activo ? "activado" : "desactivado"));
    }
    
    public static void setMaxEventosMemoria(int max) {
        MAX_EVENTOS_MEMORIA = max;
        debug("Límite de memoria establecido a: " + max + " eventos");
    }
    
    public static String generarReporte() {
        Map<Nivel, Integer> stats = getEstadisticasNiveles();
        StringBuilder reporte = new StringBuilder();
        
        reporte.append("📊 REPORTE DE BITÁCORA\n");
        reporte.append("=====================\n");
        reporte.append("Total eventos: ").append(getTotalEventos()).append("\n");
        reporte.append("Nivel mínimo: ").append(NIVEL_MINIMO.getTexto()).append("\n");
        reporte.append("\nEstadísticas por nivel:\n");
        
        for (Map.Entry<Nivel, Integer> entry : stats.entrySet()) {
            if (entry.getValue() > 0) {
                // ✅ CORREGIDO: Usar formato válido
                String linea = String.format("  %-6s: %d eventos", 
                    entry.getKey().getTexto(), entry.getValue());
                reporte.append(linea).append("\n");
            }
        }
        
        reporte.append("\nÚltimos 5 eventos:\n");
        int inicio = Math.max(0, eventos.size() - 5);
        for (int i = inicio; i < eventos.size(); i++) {
            reporte.append("  ").append(eventos.get(i)).append("\n");
        }
        
        return reporte.toString();
    }
    
    /**
     * 🛠️ Utilidades
     */
    private static String getStackTrace(Exception e) {
        if (e == null) return "No stack trace available";
        java.io.StringWriter sw = new java.io.StringWriter();
        e.printStackTrace(new java.io.PrintWriter(sw));
        return sw.toString();
    }
    
    /**
     * 🏁 Inicialización estática
     */
    static {
        // Crear directorio al cargar la clase
        try {
            Files.createDirectories(BITACORA_DIR);
            info("⚓ Bitácora del Navío Libertad inicializada");
            info("📁 Directorio de logs: " + BITACORA_DIR.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("💥 ERROR CRÍTICO: No se pudo crear directorio de bitácoras: " + e.getMessage());
        }
    }

	public static boolean setModoTurbo(boolean b) {
		return true;
	}
}