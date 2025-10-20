package com.elreinodelolvido.ellibertad.util;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 🏴‍☠️ OBSERVADOR_EXCEPCIONES_TURBO — ¡VIGILANTE ÉPICO DE LOS KRAKENS DEL CÓDIGO!
 * ⚡ Sistema de monitoreo en tiempo real que aprende de cada excepción
 * 🔮 Infundido con la sabiduría de la Bruja del Bytecode
 */
public class ObservadorExcepcionesTurbo {

    private static final ConcurrentHashMap<String, ExcepcionRegistro> REGISTRO_EXCEPCIONES = 
        new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<EventoExcepcion> COLA_EVENTOS = 
        new ConcurrentLinkedQueue<>();
    private static final ScheduledExecutorService OBSERVADOR = 
        Executors.newScheduledThreadPool(2);
    
    private static final AtomicInteger CONTADOR_TOTAL_EXCEPCIONES = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_EXCEPCIONES_CRITICAS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_EXCEPCIONES_RECURRENTES = new AtomicInteger(0);
    
    private static volatile boolean OBSERVADOR_ACTIVO = false;

    /**
     * 🚀 INICIAR OBSERVADOR TURBO
     */
    public static void iniciarObservador() {
        if (OBSERVADOR_ACTIVO) {
            System.out.println("⚠️ Observador ya está activo");
            return;
        }
        
        OBSERVADOR_ACTIVO = true;
        System.out.println("🔮 ACTIVANDO OBSERVADOR DE EXCEPCIONES TURBO...");
        
        // 🎯 TAREA DE MONITOREO EN TIEMPO REAL
        OBSERVADOR.scheduleAtFixedRate(() -> {
            try {
                procesarEventosPendientes();
                analizarPatronesCriticos();
                limpiarRegistrosAntiguos();
            } catch (Exception e) {
                System.err.println("💥 Error en observador: " + e.getMessage());
            }
        }, 0, 30, TimeUnit.SECONDS); // Cada 30 segundos
        
        // 🎯 TAREA DE REPORTES AUTOMÁTICOS
        OBSERVADOR.scheduleAtFixedRate(() -> {
            try {
                generarReporteAutomatico();
            } catch (Exception e) {
                System.err.println("💥 Error generando reporte: " + e.getMessage());
            }
        }, 1, 5, TimeUnit.MINUTES); // Cada 5 minutos
        
        System.out.println("✅ OBSERVADOR TURBO ACTIVADO - Vigilando krakens...");
    }

    /**
     * 🛑 DETENER OBSERVADOR TURBO
     */
    public static void detenerObservador() {
        if (!OBSERVADOR_ACTIVO) {
            return;
        }
        
        OBSERVADOR_ACTIVO = false;
        OBSERVADOR.shutdown();
        
        try {
            if (!OBSERVADOR.awaitTermination(10, TimeUnit.SECONDS)) {
                OBSERVADOR.shutdownNow();
            }
        } catch (InterruptedException e) {
            OBSERVADOR.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        generarReporteFinal();
        System.out.println("🛑 OBSERVADOR TURBO DETENIDO");
    }

    /**
     * 📝 REGISTRAR EXCEPCIÓN TURBO
     */
    /**
     * 📝 REGISTRAR EXCEPCIÓN TURBO
     */
    public static void registrarExcepcion(String modulo, String metodo, Throwable excepcion, String contexto) {
        CONTADOR_TOTAL_EXCEPCIONES.incrementAndGet();
        
        EventoExcepcion evento = new EventoExcepcion(
            modulo, metodo, excepcion, contexto, LocalDateTime.now()
        );
        
        COLA_EVENTOS.offer(evento);
        actualizarRegistroExcepciones(evento);
        
        // 🚨 ALERTA EN TIEMPO REAL PARA EXCEPCIONES CRÍTICAS
        if (esExcepcionCritica(excepcion)) {
            CONTADOR_EXCEPCIONES_CRITICAS.incrementAndGet();
            emitirAlertaCritica(evento);
        }
        
        System.out.println("🎯 Excepción registrada: " + modulo + "." + metodo + 
                          " - " + excepcion.getClass().getSimpleName());
    }

    /**
     * 🎯 REGISTRAR KRAKEN (alias para registrarExcepcion)
     */
    public static void registrarKraken(String ubicacion, Throwable kraken) {
        registrarExcepcion("SISTEMA", ubicacion, kraken, "KRAKEN_DETECTADO");
    }

    /**
     * 🎯 REGISTRAR KRAKEN (alias para registrarExcepcion)
     */
    public static void registrarKraken(String ubicacion, Exception kraken) {
        registrarExcepcion("SISTEMA", ubicacion, kraken, "KRAKEN_DETECTADO");
    }

    /**
     * ⚡ ACTUALIZAR REGISTRO DE EXCEPCIONES
     */
    private static void actualizarRegistroExcepciones(EventoExcepcion evento) {
        String clave = generarClaveExcepcion(evento);
        
        REGISTRO_EXCEPCIONES.compute(clave, (k, registroExistente) -> {
            if (registroExistente == null) {
                return new ExcepcionRegistro(evento);
            } else {
                registroExistente.incrementarOcurrencias();
                registroExistente.actualizarUltimaOcurrencia();
                
                // 🚨 DETECTAR RECURRENCIA
                if (registroExistente.getOcurrencias() >= 3) {
                    CONTADOR_EXCEPCIONES_RECURRENTES.incrementAndGet();
                    if (registroExistente.getOcurrencias() == 3) {
                        emitirAlertaRecurrente(registroExistente);
                    }
                }
                
                return registroExistente;
            }
        });
    }

    /**
     * 🔄 PROCESAR EVENTOS PENDIENTES
     */
    private static void procesarEventosPendientes() {
        int procesados = 0;
        EventoExcepcion evento;
        
        while ((evento = COLA_EVENTOS.poll()) != null && procesados < 100) {
            // 🎯 ANÁLISIS EN PROFUNDIDAD DE CADA EVENTO
            analizarCausaRaiz(evento);
            generarRecomendacion(evento);
            procesados++;
        }
        
        if (procesados > 0) {
            System.out.println("🔍 Observador procesó " + procesados + " eventos de excepción");
        }
    }

    /**
     * 🕵️ ANALIZAR CAUSA RAIZ
     */
    private static void analizarCausaRaiz(EventoExcepcion evento) {
        String causaRaiz = determinarCausaRaiz(evento);
        evento.setCausaRaiz(causaRaiz);
        
        // 🎯 APRENDIZAJE AUTOMÁTICO: Patrones comunes
        if (causaRaiz.contains("NULL_POINTER")) {
            aprenderPatronNullPointer(evento);
        } else if (causaRaiz.contains("FORMATO")) {
            aprenderPatronFormato(evento);
        } else if (causaRaiz.contains("ARCHIVO")) {
            aprenderPatronArchivo(evento);
        }
    }

    /**
     * 🎯 DETERMINAR CAUSA RAIZ
     */
    private static String determinarCausaRaiz(EventoExcepcion evento) {
        Throwable ex = evento.getExcepcion();  // Cambiado a Throwable
        String mensaje = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
        
        if (ex instanceof NullPointerException) return "NULL_POINTER";
        if (ex instanceof NumberFormatException) return "FORMATO_NUMERICO";
        if (ex instanceof java.nio.file.NoSuchFileException) return "ARCHIVO_NO_ENCONTRADO";
        if (ex instanceof java.io.IOException) return "ERROR_IO";
        if (ex instanceof IllegalArgumentException) return "ARGUMENTO_INVALIDO";
        if (ex instanceof IllegalStateException) return "ESTADO_INVALIDO";
        if (ex instanceof OutOfMemoryError) return "MEMORIA_INSUFICIENTE";
        if (ex instanceof StackOverflowError) return "DESBORDAMIENTO_PILA";
        if (mensaje.contains("timeout")) return "TIMEOUT";
        if (mensaje.contains("connection")) return "CONEXION";
        if (mensaje.contains("permission")) return "PERMISO";
        
        return "DESCONOCIDA";
    }

    /**
     * 💡 GENERAR RECOMENDACIÓN INTELIGENTE
     */
    private static void generarRecomendacion(EventoExcepcion evento) {
        String recomendacion = "";
        
        switch (evento.getCausaRaiz()) {
            case "NULL_POINTER":
                recomendacion = "🔧 Implementar validación de nulos en " + evento.getMetodo();
                break;
            case "FORMATO_NUMERICO":
                recomendacion = "🔧 Añadir try-catch para conversión numérica en " + evento.getModulo();
                break;
            case "ARCHIVO_NO_ENCONTRADO":
                recomendacion = "🔧 Verificar existencia de archivos antes de operar";
                break;
            case "TIMEOUT":
                recomendacion = "⏱️ Aumentar timeout o implementar reintentos";
                break;
            default:
                recomendacion = "🔍 Revisar lógica en " + evento.getModulo() + "." + evento.getMetodo();
        }
        
        evento.setRecomendacion(recomendacion);
    }

    /**
     * 🚨 ANALIZAR PATRONES CRÍTICOS
     */
    private static void analizarPatronesCriticos() {
        // 🎯 IDENTIFICAR EXCEPCIONES MÁS FRECUENTES
        List<ExcepcionRegistro> topExcepciones = REGISTRO_EXCEPCIONES.values().stream()
            .sorted((a, b) -> Integer.compare(b.getOcurrencias(), a.getOcurrencias()))
            .limit(5)
            .toList();
        
        if (!topExcepciones.isEmpty()) {
            System.out.println("📊 TOP EXCEPCIONES:");
            topExcepciones.forEach(reg -> {
                System.out.println("   • " + reg.getClave() + ": " + reg.getOcurrencias() + " ocurrencias");
            });
        }
        
        // 🚨 ALERTA POR AUMENTO SÚBITO DE EXCEPCIONES
        if (CONTADOR_TOTAL_EXCEPCIONES.get() > 10) {
            double tasaPorMinuto = calcularTasaExcepcionesPorMinuto();
            if (tasaPorMinuto > 2.0) {
                System.out.println("🚨 ALERTA: Alta tasa de excepciones (" + 
                                 String.format("%.1f", tasaPorMinuto) + "/minuto)");
            }
        }
    }

    /**
     * 📈 GENERAR REPORTE AUTOMÁTICO
     */
    private static void generarReporteAutomatico() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("# 🏴‍☠️ REPORTE DEL OBSERVADOR DE EXCEPCIONES TURBO\n\n");
        reporte.append("**Fecha:** ").append(LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");
        
        reporte.append("## 📊 ESTADÍSTICAS GLOBALES\n");
        reporte.append("- Total excepciones: ").append(CONTADOR_TOTAL_EXCEPCIONES.get()).append("\n");
        reporte.append("- Excepciones críticas: ").append(CONTADOR_EXCEPCIONES_CRITICAS.get()).append("\n");
        reporte.append("- Excepciones recurrentes: ").append(CONTADOR_EXCEPCIONES_RECURRENTES.get()).append("\n");
        reporte.append("- Tasa por minuto: ").append(String.format("%.1f", calcularTasaExcepcionesPorMinuto())).append("\n\n");
        
        reporte.append("## 🚨 EXCEPCIONES MÁS FRECUENTES\n");
        REGISTRO_EXCEPCIONES.values().stream()
            .sorted((a, b) -> Integer.compare(b.getOcurrencias(), a.getOcurrencias()))
            .limit(10)
            .forEach(reg -> {
                reporte.append("### ").append(reg.getClave()).append("\n");
                reporte.append("- Ocurrencias: ").append(reg.getOcurrencias()).append("\n");
                reporte.append("- Última vez: ").append(reg.getUltimaOcurrencia()).append("\n");
                reporte.append("- Recomendación: ").append(reg.getRecomendacion()).append("\n\n");
            });
        
        FileUtils.writeToFile("autogen-output/observador-excepciones/reporte-automatico.md", reporte.toString());
    }

    /**
     * 📊 GENERAR REPORTE FINAL
     */
    private static void generarReporteFinal() {
        generarReporteAutomatico();
        System.out.println("📄 Reporte final del observador guardado");
    }

    /**
     * 🧹 LIMPIAR REGISTROS ANTIGUOS
     */
    private static void limpiarRegistrosAntiguos() {
        LocalDateTime hace24Horas = LocalDateTime.now().minusHours(24);
        
        REGISTRO_EXCEPCIONES.entrySet().removeIf(entry -> 
            entry.getValue().getUltimaOcurrencia().isBefore(hace24Horas)
        );
    }

    // 🎯 MÉTODOS DE APRENDIZAJE AUTOMÁTICO

    private static void aprenderPatronNullPointer(EventoExcepcion evento) {
        System.out.println("🧠 Aprendiendo patrón NullPointer en: " + evento.getModulo());
    }

    private static void aprenderPatronFormato(EventoExcepcion evento) {
        System.out.println("🧠 Aprendiendo patrón Formato en: " + evento.getModulo());
    }

    private static void aprenderPatronArchivo(EventoExcepcion evento) {
        System.out.println("🧠 Aprendiendo patrón Archivo en: " + evento.getModulo());
    }

    // 🚨 MÉTODOS DE ALERTA

    private static void emitirAlertaCritica(EventoExcepcion evento) {
        String alerta = "🚨 ALERTA CRÍTICA: " + evento.getExcepcion().getClass().getSimpleName() +
                       " en " + evento.getModulo() + "." + evento.getMetodo();
        System.err.println(alerta);
        FileUtils.appendToFile("autogen-output/observador-excepciones/alertas-criticas.log", 
            LocalDateTime.now() + " - " + alerta + "\n");
    }

    private static void emitirAlertaRecurrente(ExcepcionRegistro registro) {
        String alerta = "🔄 EXCEPCIÓN RECURRENTE: " + registro.getClave() + 
                       " (" + registro.getOcurrencias() + " veces)";
        System.err.println(alerta);
        FileUtils.appendToFile("autogen-output/observador-excepciones/alertas-recurrentes.log", 
            LocalDateTime.now() + " - " + alerta + "\n");
    }

    // 🎯 MÉTODOS DE UTILIDAD

    private static boolean esExcepcionCritica(Throwable ex) {
        return ex instanceof NullPointerException ||
               ex instanceof OutOfMemoryError ||
               ex instanceof StackOverflowError ||
               ex instanceof VirtualMachineError ||
               ex instanceof LinkageError;
    }

    private static String generarClaveExcepcion(EventoExcepcion evento) {
        return evento.getModulo() + "." + evento.getMetodo() + ":" + 
               evento.getExcepcion().getClass().getSimpleName();
    }

    private static double calcularTasaExcepcionesPorMinuto() {
        return CONTADOR_TOTAL_EXCEPCIONES.get() / 5.0; // Asumiendo 5 minutos de ejecución
    }

    // 📊 MÉTODOS DE CONSULTA PÚBLICA

    public static Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("observador_activo", OBSERVADOR_ACTIVO);
        stats.put("total_excepciones", CONTADOR_TOTAL_EXCEPCIONES.get());
        stats.put("excepciones_criticas", CONTADOR_EXCEPCIONES_CRITICAS.get());
        stats.put("excepciones_recurrentes", CONTADOR_EXCEPCIONES_RECURRENTES.get());
        stats.put("registros_activos", REGISTRO_EXCEPCIONES.size());
        stats.put("eventos_pendientes", COLA_EVENTOS.size());
        return stats;
    }

    public static void mostrarEstado() {
        System.out.println("\n🔮 ESTADO DEL OBSERVADOR TURBO:");
        Map<String, Object> stats = obtenerEstadisticas();
        stats.forEach((key, value) -> {
            System.out.println("   " + key + ": " + value);
        });
    }

    // 🏴‍☠️ CLASES DE APOYO TURBO

    private static class EventoExcepcion {
        private final String modulo;
        private final String metodo;
        private final Exception excepcion;
        private final String contexto;
        private final LocalDateTime timestamp;
        private String causaRaiz;
        private String recomendacion;

        public EventoExcepcion(String modulo, String metodo, Throwable excepcion2, 
                              String contexto, LocalDateTime timestamp) {
            this.modulo = modulo;
            this.metodo = metodo;
            this.excepcion = (Exception) excepcion2;
            this.contexto = contexto;
            this.timestamp = timestamp;
        }

        // Getters y setters...
        public String getModulo() { return modulo; }
        public String getMetodo() { return metodo; }
        public Exception getExcepcion() { return excepcion; }
        public String getContexto() { return contexto; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getCausaRaiz() { return causaRaiz; }
        public void setCausaRaiz(String causaRaiz) { this.causaRaiz = causaRaiz; }
        public String getRecomendacion() { return recomendacion; }
        public void setRecomendacion(String recomendacion) { this.recomendacion = recomendacion; }
    }

    private static class ExcepcionRegistro {
        private final String clave;
        private int ocurrencias;
        private LocalDateTime primeraOcurrencia;
        private LocalDateTime ultimaOcurrencia;
        private String recomendacion;

        public ExcepcionRegistro(EventoExcepcion evento) {
            this.clave = generarClaveExcepcion(evento);
            this.ocurrencias = 1;
            this.primeraOcurrencia = evento.getTimestamp();
            this.ultimaOcurrencia = evento.getTimestamp();
            this.recomendacion = evento.getRecomendacion();
        }

        public void incrementarOcurrencias() {
            this.ocurrencias++;
        }

        public void actualizarUltimaOcurrencia() {
            this.ultimaOcurrencia = LocalDateTime.now();
        }

        // Getters...
        public String getClave() { return clave; }
        public int getOcurrencias() { return ocurrencias; }
        public LocalDateTime getPrimeraOcurrencia() { return primeraOcurrencia; }
        public LocalDateTime getUltimaOcurrencia() { return ultimaOcurrencia; }
        public String getRecomendacion() { return recomendacion; }
    }

    public static int getKrakensCount() {
        return CONTADOR_TOTAL_EXCEPCIONES.get();
    }
}

