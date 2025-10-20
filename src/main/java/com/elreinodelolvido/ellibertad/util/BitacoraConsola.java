package com.elreinodelolvido.ellibertad.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 🏴‍☠️ BITÁCORA DE CONSOLA TURBOFULURADA - Captura épica de toda la ejecución
 * 🔮 Captura TODO: System.out, System.err, logs personalizados y hasta los susurros del código
 */
public class BitacoraConsola {

    // 🎯 CONFIGURACIÓN ÉPICA
    private static PrintStream originalOut;
    private static PrintStream originalErr;
    private static PrintStream bitacoraStream;
    private static ByteArrayOutputStream bufferMemoria;

    private static final String OUTPUT_DIR = "autogen-output/bitacora";
    private static final String OUTPUT_FILE = OUTPUT_DIR + "/bitacora-consola.md";
    private static final String BACKUP_FILE = OUTPUT_DIR + "/backups/bitacora-backup-%s.md";

    // 📊 ESTADÍSTICAS TURBO
    private static final List<String> bufferLineas = new CopyOnWriteArrayList<>();
    private static long totalLineasCapturadas = 0;
    private static long totalBytesCapturados = 0;
    private static LocalDateTime inicioCaptura;
    private static boolean capturaActiva = false;

    // 🎨 FORMATO MARKDOWN ÉPICO
    private static final String HEADER_MARKDOWN = """
        # 🏴‍☠️ Bitácora Turbofulurada - Captura Épica de Consola
        
        > *Generado automáticamente por Autogen Turbo Fusión*
        
        ## 📋 Metadatos de la Ejecución
        
        | Propiedad | Valor |
        |-----------|-------|
        | **Inicio** | %s |
        | **Sistema** | Autogen Turbo Fusión |
        | **Modo** | Captura completa de consola |
        | **Estado** | %s |
        
        ## 📜 Registro de Actividades
        
        ```plaintext
        """;

    private static final String FOOTER_MARKDOWN = """
        ```
        
        ## 📊 Estadísticas de Captura
        
        | Métrica | Valor |
        |---------|-------|
        | **Líneas capturadas** | %d |
        | **Bytes capturados** | %d |
        | **Duración** | %s |
        | **Eficiencia** | %.1f%% |
        
        ---
        
        *¡La travesía turbofulurada ha concluido! 🏴‍☠️*
        """;

    /**
     * 🚀 INICIAR CAPTURA TURBOFULURADA - Redirige TODO hacia la bitácora épica
     */
    public static void iniciarCaptura() {
        try {
            // 🎯 PREPARAR EL TERRENO
            FileUtils.crearDirectorioSiNoExiste(OUTPUT_DIR);
            FileUtils.crearDirectorioSiNoExiste(OUTPUT_DIR + "/backups");
            
            // 📝 CREAR BACKUP DE EJECUCIÓN ANTERIOR (si existe)
            crearBackupBitacoraAnterior();

            // 💾 GUARDAR STREAMS ORIGINALES
            originalOut = System.out;
            originalErr = System.err;

            // 🧠 BUFFER EN MEMORIA PARA CAPTURA RÁPIDA
            bufferMemoria = new ByteArrayOutputStream(1024 * 1024); // 1MB inicial

            // 🌊 STREAM COMBINADO TURBO (archivo + memoria + consola original)
            OutputStream archivo = new FileOutputStream(OUTPUT_FILE, false);
            OutputStream combinadoTurbo = new OutputStreamTurbofulurado(
                archivo, 
                bufferMemoria, 
                originalOut
            );

            bitacoraStream = new PrintStream(combinadoTurbo, true, StandardCharsets.UTF_8);

            // 🎯 REDIRIGIR SALIDAS DEL SISTEMA
            System.setOut(bitacoraStream);
            System.setErr(bitacoraStream);

            // 📊 INICIALIZAR ESTADÍSTICAS
            inicioCaptura = LocalDateTime.now();
            capturaActiva = true;
            totalLineasCapturadas = 0;
            totalBytesCapturados = 0;
            bufferLineas.clear();

            // 🏴‍☠️ ENCABEZADO ÉPICO
            String timestamp = inicioCaptura.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String header = String.format(HEADER_MARKDOWN, timestamp, "🟢 CAPTURA ACTIVA");
            
            try {
                Files.write(Paths.get(OUTPUT_FILE), header.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                // Si falla, lo escribimos through del stream
                bitacoraStream.print(header);
            }

            // 🔊 MENSAJE DE INICIO TURBO
            System.out.println("\n" + "🚀".repeat(60));
            System.out.println("           CAPTURA TURBOFULURADA INICIADA - " + timestamp);
            System.out.println("🚀".repeat(60));
            System.out.println("🎯 Redirigiendo System.out y System.err a la bitácora épica");
            System.out.println("📁 Archivo: " + new File(OUTPUT_FILE).getAbsolutePath());
            System.out.println("💾 Buffer: " + (bufferMemoria.size() / 1024) + " KB iniciales");
            System.out.println("🔮 Estado: TODO será capturado para la posteridad");
            System.out.println("🚀".repeat(60) + "\n");

        } catch (Exception e) {
            // 🆘 FALLO GRACIOSO - NO IMPACTAR LA EJECUCIÓN PRINCIPAL
            if (originalOut != null) {
                originalOut.println("⚠️ [BITÁCORA] No se pudo iniciar la captura turbofulurada: " + e.getMessage());
                originalOut.println("💡 El sistema continuará sin captura de consola");
            }
        }
    }

    /**
     * 🛑 RESTAURAR SALIDA Y FINALIZAR BITÁCORA ÉPICAMENTE
     */
    public static void restaurarSalida() {
        if (!capturaActiva) return;

        try {
            capturaActiva = false;
            LocalDateTime finCaptura = LocalDateTime.now();
            
            // ⏱️ CALCULAR DURACIÓN
            java.time.Duration duracion = java.time.Duration.between(inicioCaptura, finCaptura);
            String duracionFormateada = formatDuracion(duracion);
            
            // 📊 CALCULAR EFICIENCIA
            double eficiencia = totalLineasCapturadas > 0 ? 
                (1.0 - (double) contarErrores() / totalLineasCapturadas) * 100 : 100.0;

            // 🏁 FOOTER ÉPICO
            String footer = String.format(FOOTER_MARKDOWN, 
                totalLineasCapturadas, totalBytesCapturados, duracionFormateada, eficiencia);
            
            // 📝 ESCRIBIR FOOTER
            try (FileWriter fw = new FileWriter(OUTPUT_FILE, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(footer);
                bw.flush();
            }

            // 🔊 MENSAJE DE CIERRE
            System.out.println("\n" + "🏁".repeat(60));
            System.out.println("           CAPTURA TURBOFULURADA FINALIZADA");
            System.out.println("🏁".repeat(60));
            System.out.println("📊 Resumen épico:");
            System.out.println("   • Líneas capturadas: " + totalLineasCapturadas);
            System.out.println("   • Bytes procesados: " + totalBytesCapturados);
            System.out.println("   • Duración: " + duracionFormateada);
            System.out.println("   • Eficiencia: " + String.format("%.1f%%", eficiencia));
            System.out.println("📁 Bitácora guardada en: " + OUTPUT_FILE);
            System.out.println("🏴‍☠️ ¡La historia ha sido escrita!");
            System.out.println("🏁".repeat(60));

        } catch (Exception e) {
            // 🆘 SI FALLA EL CIERRE, AL MENOS RESTAURAR SALIDAS
            if (originalOut != null) {
                originalOut.println("⚠️ [BITÁCORA] Error en cierre épico: " + e.getMessage());
            }
        } finally {
            // 🔄 RESTAURAR SALIDAS ORIGINALES
            if (originalOut != null) System.setOut(originalOut);
            if (originalErr != null) System.setErr(originalErr);
            
            // 🧹 LIMPIAR RECURSOS
            if (bitacoraStream != null) {
                bitacoraStream.close();
                bitacoraStream = null;
            }
        }
    }

    /**
     * 📝 LOG TURBOFULURADO - Mensajes épicos con formato y timestamp
     */
    public static void log(String mensaje) {
        log(mensaje, "🔮");
    }

    public static void log(String mensaje, String emoji) {
        if (!capturaActiva) {
            // 📤 SI NO HAY CAPTURA, USAR CONSOLA ORIGINAL
            if (originalOut != null) {
                originalOut.println(emoji + " [LOG] " + mensaje);
            }
            return;
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        String lineaFormateada = String.format("[%s] %s %s", timestamp, emoji, mensaje);
        
        // 📝 AGREGAR AL BUFFER Y ENVIAR A BITÁCORA
        bufferLineas.add(lineaFormateada);
        totalLineasCapturadas++;
        totalBytesCapturados += lineaFormateada.getBytes(StandardCharsets.UTF_8).length;
        
        System.out.println(lineaFormateada);
    }

    /**
     * 🎯 LOGS ESPECIALIZADOS TURBOFULURADOS
     */
    public static void logExito(String mensaje) {
        log(mensaje, "✅");
    }

    public static void logError(String mensaje) {
        log(mensaje, "❌");
    }

    public static void logAdvertencia(String mensaje) {
        log(mensaje, "⚠️");
    }

    public static void logTurbo(String mensaje) {
        log(mensaje, "🚀");
    }

    public static void logDebug(String mensaje) {
        log(mensaje, "🐛");
    }

    /**
     * 📖 OBTENER CONTENIDO CAPTURADO - Para generación de PDFs épicos
     */
    public static String obtenerContenidoCapturado() {
        try {
            if (capturaActiva && bufferMemoria != null) {
                // 📦 SI LA CAPTURA ESTÁ ACTIVA, USAR BUFFER EN MEMORIA
                return bufferMemoria.toString(StandardCharsets.UTF_8.name());
            } else {
                // 📁 SI NO, LEER DEL ARCHIVO
                Path rutaBitacora = Paths.get(OUTPUT_FILE);
                if (Files.exists(rutaBitacora)) {
                    return new String(Files.readAllBytes(rutaBitacora), StandardCharsets.UTF_8);
                }
            }
        } catch (Exception e) {
            logError("No se pudo obtener contenido capturado: " + e.getMessage());
        }
        
        return "# 📜 Bitácora no disponible\n\nNo se pudo cargar el contenido de la bitácora.";
    }

    /**
     * 📊 OBTENER ESTADÍSTICAS TURBOFULURADAS
     */
    public static String obtenerEstadisticas() {
        if (!capturaActiva) return "📭 Captura no activa";
        
        LocalDateTime ahora = LocalDateTime.now();
        java.time.Duration duracion = java.time.Duration.between(inicioCaptura, ahora);
        
        return String.format(
            "📊 ESTADÍSTICAS BITÁCORA TURBO:\n" +
            "  • Líneas capturadas: %d\n" +
            "  • Bytes procesados: %d\n" +
            "  • Tiempo activa: %s\n" +
            "  • Buffer memoria: %d KB\n" +
            "  • Estado: %s",
            totalLineasCapturadas,
            totalBytesCapturados,
            formatDuracion(duracion),
            bufferMemoria != null ? bufferMemoria.size() / 1024 : 0,
            capturaActiva ? "🟢 ACTIVA" : "🔴 INACTIVA"
        );
    }

    /**
     * 🎯 OBTENER ÚLTIMAS LÍNEAS PARA MONITOREO EN TIEMPO REAL
     */
    public static List<String> obtenerUltimasLineas(int cantidad) {
        int desde = Math.max(0, bufferLineas.size() - cantidad);
        return new ArrayList<>(bufferLineas.subList(desde, bufferLineas.size()));
    }

    /**
     * 🔄 REINICIAR CAPTURA (útil para sesiones largas)
     */
    public static void reiniciarCaptura() {
        if (capturaActiva) {
            restaurarSalida();
        }
        // Pequeña pausa para asegurar que todo se cerró
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        iniciarCaptura();
    }

    // =========================================================================
    // 🛠️ MÉTODOS PRIVADOS TURBOFULURADOS
    // =========================================================================

    /**
     * 💾 STREAM TURBOFULURADO - Escribe en múltiples destinos simultáneamente
     */
    private static class OutputStreamTurbofulurado extends OutputStream {
        private final OutputStream archivo;
        private final OutputStream memoria;
        private final PrintStream consola;
        private final ByteArrayOutputStream lineaBuffer = new ByteArrayOutputStream();
        private boolean ultimoFueCR = false;

        public OutputStreamTurbofulurado(OutputStream archivo, OutputStream memoria, PrintStream consola) {
            this.archivo = archivo;
            this.memoria = memoria;
            this.consola = consola;
        }

        @Override
        public void write(int b) throws IOException {
            // 📝 CAPTURAR CARÁCTER
            char c = (char) b;
            
            // 🔄 MANEJO DE SALTO DE LÍNEA
            if (c == '\n') {
                procesarLineaCompleta();
            } else if (c == '\r') {
                ultimoFueCR = true;
                procesarLineaCompleta();
            } else {
                if (ultimoFueCR) {
                    ultimoFueCR = false;
                    procesarLineaCompleta();
                }
                lineaBuffer.write(b);
            }

            // 📤 ESCRIBIR A TODOS LOS DESTINOS
            archivo.write(b);
            memoria.write(b);
            consola.write(b);
        }

        private void procesarLineaCompleta() {
            if (lineaBuffer.size() > 0) {
                String linea = lineaBuffer.toString(StandardCharsets.UTF_8);
                bufferLineas.add(linea);
                totalLineasCapturadas++;
                totalBytesCapturados += linea.getBytes(StandardCharsets.UTF_8).length;
                lineaBuffer.reset();
            }
            ultimoFueCR = false;
        }

        @Override
        public void flush() throws IOException {
            archivo.flush();
            memoria.flush();
            consola.flush();
        }

        @Override
        public void close() throws IOException {
            // PROCESAR ÚLTIMA LÍNEA SI QUEDÓ PENDIENTE
            if (lineaBuffer.size() > 0) {
                procesarLineaCompleta();
            }
            archivo.close();
            memoria.close();
            // NO cerrar consola original
        }
    }

    /**
     * ⏱️ FORMATEAR DURACIÓN EN FORMATO ÉPICO
     */
    private static String formatDuracion(java.time.Duration duracion) {
        long horas = duracion.toHours();
        long minutos = duracion.toMinutesPart();
        long segundos = duracion.toSecondsPart();
        long milis = duracion.toMillisPart();
        
        if (horas > 0) {
            return String.format("%d h %d m %d s", horas, minutos, segundos);
        } else if (minutos > 0) {
            return String.format("%d m %d s", minutos, segundos);
        } else {
            return String.format("%d.%03d s", segundos, milis);
        }
    }

    /**
     * 📦 CREAR BACKUP DE BITÁCORA ANTERIOR
     */
    private static void crearBackupBitacoraAnterior() {
        try {
            File archivoAnterior = new File(OUTPUT_FILE);
            if (archivoAnterior.exists()) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
                String rutaBackup = String.format(BACKUP_FILE, timestamp);
                
                Files.copy(archivoAnterior.toPath(), Paths.get(rutaBackup));
                logTurbo("Backup de bitácora anterior creado: " + rutaBackup);
            }
        } catch (Exception e) {
            // No crítico, solo continuar
        }
    }

    /**
     * ❌ CONTAR ERRORES EN EL BUFFER
     */
    private static int contarErrores() {
        return (int) bufferLineas.stream()
            .filter(linea -> linea.contains("❌") || 
                           linea.toLowerCase().contains("error") || 
                           linea.contains("💥"))
            .count();
    }

    /**
     * 🎯 VERIFICAR ESTADO DE CAPTURA
     */
    public static boolean isCapturaActiva() {
        return capturaActiva;
    }

    /**
     * 📈 OBTENER MÉTRICAS EN TIEMPO REAL
     */
    public static Map<String, Object> obtenerMetricas() {
        Map<String, Object> metricas = new HashMap<>();
        metricas.put("capturaActiva", capturaActiva);
        metricas.put("totalLineas", totalLineasCapturadas);
        metricas.put("totalBytes", totalBytesCapturados);
        metricas.put("tamanoBuffer", bufferLineas.size());
        metricas.put("inicioCaptura", inicioCaptura);
        metricas.put("erroresDetectados", contarErrores());
        
        if (bufferMemoria != null) {
            metricas.put("memoriaUsadaKB", bufferMemoria.size() / 1024);
        }
        
        return metricas;
    }
}