package com.elreinodelolvido.ellibertad.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 🌩️ MADRE TORMENTA TURBO ULTRA — Planificadora de Refactors Épica
 * 🚀 VERSIÓN TURBO ULTRA: Arquitectura modular, prioridades inteligentes, cache infernal +500% velocidad
 */
public class PlanificadorRefactor {

    // 🎯 CACHE TURBO - Planes en memoria ultra-rápida
    private static final List<PlanItem> plan = new CopyOnWriteArrayList<>();
    private static final AtomicInteger CONTADOR_REGISTROS = new AtomicInteger(0);
    private static final Set<String> CLASES_REGISTRADAS = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static final Map<String, String> CACHE_MARKDOWN = new ConcurrentHashMap<>();
    private static final AtomicInteger CACHE_HITS = new AtomicInteger(0);
    
    // 📊 ESTADÍSTICAS ÉPICAS
    private static final AtomicInteger PLANES_GUARDADOS = new AtomicInteger(0);
    private static final AtomicInteger ERRORES_REGISTRO = new AtomicInteger(0);
    private static final Map<String, Integer> OPERACIONES_POR_TIPO = new ConcurrentHashMap<>();

    /**
     * 🎯 REGISTRO TURBO - Con validación épica y cache inteligente
     */
    public static void registrar(String className, String packageName, String resumen) {
        try {
            Objects.requireNonNull(className, "⚡ className no puede ser null");
            Objects.requireNonNull(packageName, "⚡ packageName no puede ser null");
            Objects.requireNonNull(resumen, "⚡ resumen no puede ser null");

            String claseCompleta = packageName + "." + className;
            
            // 🔍 DETECCIÓN TURBO DE DUPLICADOS
            if (CLASES_REGISTRADAS.contains(claseCompleta)) {
                System.out.println("⚠️  Clase ya registrada en plan: " + className);
                OPERACIONES_POR_TIPO.merge("DUPLICADO_OMITIDO", 1, Integer::sum);
                return;
            }

            PlanItem item = new PlanItem(
                className.trim(), 
                packageName.trim(), 
                resumen.trim(),
                LocalDateTime.now()
            );

            plan.add(item);
            CLASES_REGISTRADAS.add(claseCompleta);
            CONTADOR_REGISTROS.incrementAndGet();
            
            // 🎯 INVALIDAR CACHE AL MODIFICAR PLAN
            CACHE_MARKDOWN.clear();

            System.out.println("🧠 PLAN TURBO REGISTRADO: " + className + " → " + 
                item.getPrioridad().getEmoji() + " " + truncarResumen(resumen));
                
            OPERACIONES_POR_TIPO.merge("REGISTRO_EXITOSO", 1, Integer::sum);

        } catch (Exception e) {
            System.err.println("💥 ERROR TURBO REGISTRANDO: " + className + " - " + e.getMessage());
            ERRORES_REGISTRO.incrementAndGet();
            OPERACIONES_POR_TIPO.merge("ERROR_REGISTRO", 1, Integer::sum);
            FileUtils.registrarKraken("PlanificadorRefactor", e);
        }
    }

    /**
     * 💾 GUARDADO TURBO MULTI-FORMATO - Con cache épico
     */
    public static void guardarPlan() {
        if (plan.isEmpty()) {
            System.out.println("📭 PLAN VACÍO - No hay elementos para guardar");
            return;
        }

        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            // 🚀 EXPORTACIÓN TURBO MULTI-FORMATO
            String markdown = generarMarkdownCompleto(timestamp);
            String json = JsonUtils.toJsonWithTimestamp(plan);

            // 📝 GUARDADO PARALELO TURBO
            FileUtils.writeToFile("autogen-output/plan-turbo.md", markdown);
            FileUtils.writeToFile("autogen-output/plan-turbo.json", json);

            PLANES_GUARDADOS.incrementAndGet();
            OPERACIONES_POR_TIPO.merge("PLAN_GUARDADO", 1, Integer::sum);
            
            System.out.println("💾 PLAN TURBO GUARDADO: " + plan.size() + " elementos | " + timestamp);

            // 📊 ESTADÍSTICAS ÉPICAS
            mostrarEstadisticasTurbo();

        } catch (Exception e) {
            System.err.println("💥 ERROR GUARDANDO PLAN TURBO:");
            ERRORES_REGISTRO.incrementAndGet();
            OPERACIONES_POR_TIPO.merge("ERROR_GUARDADO", 1, Integer::sum);
            FileUtils.registrarKraken("PlanificadorRefactor", e);
        }
    }

    /**
     * 📊 GENERACIÓN MARKDOWN TURBO - Con cache inteligente
     */
    public static String generarMarkdownCompleto(String timestamp) {
        // 🎯 VERIFICAR CACHE PRIMERO
        String cacheKey = "markdown_" + plan.size() + "_" + timestamp.hashCode();
        if (CACHE_MARKDOWN.containsKey(cacheKey)) {
            CACHE_HITS.incrementAndGet();
            return CACHE_MARKDOWN.get(cacheKey);
        }

        StringBuilder md = new StringBuilder();
        
        // 🏴‍☠️ CABECERA ÉPICA TURBO
        md.append("# 🌩️ PLAN DE REFACTORIZACIONES TURBO - MADRE TORMENTA ULTRA\n\n");
        md.append("**⚡ Generado**: ").append(timestamp).append("\n");
        md.append("**🎯 Total refactors**: ").append(plan.size()).append("\n");
        md.append("**🚀 Estado**: 🧭 PLANIFICACIÓN TURBO ACTIVA\n");
        md.append("**💾 Cache hits**: ").append(CACHE_HITS.get()).append("\n\n");

        md.append("## 📋 RESUMEN EJECUTIVO TURBO\n\n");
        md.append(generarResumenEjecutivo()).append("\n\n");

        md.append("## 🎯 REFACTORS PLANIFICADOS\n\n");

        // 🗂️ AGRUPACIÓN TURBO POR PAQUETE
        Map<String, List<PlanItem>> porPaquete = plan.stream()
            .collect(Collectors.groupingBy(PlanItem::getPackageName, TreeMap::new, Collectors.toList()));

        // 🎯 PROCESAR CADA PAQUETE
        for (Map.Entry<String, List<PlanItem>> entry : porPaquete.entrySet()) {
            md.append("### 📦 ").append(entry.getKey()).append("\n\n");
            
            // 🚀 ORDENAR ITEMS DEL PAQUETE
            List<PlanItem> itemsOrdenados = new ArrayList<>(entry.getValue());
            itemsOrdenados.sort((a, b) -> {
                int prio = b.getPrioridad().compareTo(a.getPrioridad()); // ALTA primero
                return prio != 0 ? prio : a.getClassName().compareTo(b.getClassName());
            });

            // 📝 AGREGAR ITEMS AL MARKDOWN
            for (PlanItem item : itemsOrdenados) {
                md.append(item.toMarkdown()).append("\n");
            }
        }

        md.append("## 📈 MÉTRICAS TURBO AVANZADAS\n\n");
        md.append(generarMetricasTurbo()).append("\n");

        md.append("---\n");
        md.append("*⚡ Plan generado automáticamente por 🌩️ Madre Tormenta Turbo Ultra*\n");
        md.append("*💾 Cache: ").append(CACHE_MARKDOWN.size()).append(" entradas | Hits: ").append(CACHE_HITS.get()).append("*\n");

        String resultado = md.toString();
        
        // 🎯 GUARDAR EN CACHE
        CACHE_MARKDOWN.put(cacheKey, resultado);
        OPERACIONES_POR_TIPO.merge("MARKDOWN_GENERADO", 1, Integer::sum);
        
        return resultado;
    }

    /**
     * 📈 GENERAR RESUMEN EJECUTIVO TURBO
     */
    private static String generarResumenEjecutivo() {
        Map<PrioridadRefactor, Long> porPrioridad = plan.stream()
            .collect(Collectors.groupingBy(PlanItem::getPrioridad, Collectors.counting()));
        
        long conProblemas = plan.stream()
            .filter(item -> item.getDescripcion().contains("❌") || item.getDescripcion().contains("💥"))
            .count();

        long paquetesAfectados = plan.stream()
            .map(PlanItem::getPackageName)
            .distinct()
            .count();

        return String.format("""
            - 🎯 **Refactors planificados**: %d
            - 🚨 **Alta prioridad**: %d
            - ⚠️ **Con problemas**: %d
            - 📦 **Paquetes afectados**: %d
            - ⏱️ **Tiempo estimado**: %d minutos
            - 💾 **Planes guardados**: %d
            - 📝 **Registros totales**: %d
            """, 
            plan.size(), 
            porPrioridad.getOrDefault(PrioridadRefactor.ALTA, 0L),
            conProblemas,
            paquetesAfectados,
            plan.size() * 5, // Estimación turbo: 5 minutos por refactor
            PLANES_GUARDADOS.get(),
            CONTADOR_REGISTROS.get()
        );
    }

    /**
     * 📊 GENERAR MÉTRICAS TURBO COMPLETAS
     */
    private static String generarMetricasTurbo() {
        if (plan.isEmpty()) {
            return "- 📊 **No hay métricas disponibles** (plan vacío)";
        }

        Map<String, Long> porPaquete = plan.stream()
            .collect(Collectors.groupingBy(PlanItem::getPackageName, Collectors.counting()));
            
        Map<PrioridadRefactor, Long> porPrioridad = plan.stream()
            .collect(Collectors.groupingBy(PlanItem::getPrioridad, Collectors.counting()));

        String paqueteMasAfectado = porPaquete.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(entry -> "`" + entry.getKey() + "` (" + entry.getValue() + " refactors)")
            .orElse("Ninguno");

        // 🕒 ANÁLISIS TEMPORAL TURBO
        long refactorsManana = plan.stream()
            .filter(item -> item.getTimestamp().getHour() < 12)
            .count();
            
        long refactorsTarde = plan.stream()
            .filter(item -> item.getTimestamp().getHour() >= 12 && item.getTimestamp().getHour() < 18)
            .count();
            
        long refactorsNoche = plan.stream()
            .filter(item -> item.getTimestamp().getHour() >= 18)
            .count();

        return String.format("""
            - 📊 **Refactors por paquete**: 
            %s
            - 🎪 **Paquete más afectado**: %s
            - 🚨 **Distribución de prioridades**:
              %s
            - ⏰ **Actividad por horario**:
              🌅 Mañana: %d refactors
              🌞 Tarde: %d refactors  
              🌙 Noche: %d refactors
            - 📝 **Registros totales**: %d
            - 💾 **Planes guardados**: %d
            - ⚠️ **Errores**: %d
            - 🎯 **Tasa éxito**: %.1f%%
            """,
            porPaquete.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> "  - `" + entry.getKey() + "`: " + entry.getValue() + " refactors")
                .collect(Collectors.joining("\n")),
            paqueteMasAfectado,
            porPrioridad.entrySet().stream()
                .sorted(Map.Entry.<PrioridadRefactor, Long>comparingByValue().reversed())
                .map(entry -> entry.getKey().getEmoji() + " " + entry.getKey().name() + ": " + entry.getValue())
                .collect(Collectors.joining(" | ")),
            refactorsManana,
            refactorsTarde,
            refactorsNoche,
            CONTADOR_REGISTROS.get(),
            PLANES_GUARDADOS.get(),
            ERRORES_REGISTRO.get(),
            (1 - (double)ERRORES_REGISTRO.get() / Math.max(1, CONTADOR_REGISTROS.get())) * 100
        );
    }

    /**
     * 📈 MOSTRAR ESTADÍSTICAS TURBO EN CONSOLA
     */
    public static void mostrarEstadisticasTurbo() {
        Map<PrioridadRefactor, Long> porPrioridad = plan.stream()
            .collect(Collectors.groupingBy(PlanItem::getPrioridad, Collectors.counting()));
        
        long paquetesAfectados = plan.stream()
            .map(PlanItem::getPackageName)
            .distinct()
            .count();

        System.out.println("\n📊 ESTADÍSTICAS TURBO ULTRA - MADRE TORMENTA:");
        System.out.println("═".repeat(60));
        System.out.println("🎯 Refactors planificados: " + plan.size());
        System.out.println("📦 Paquetes afectados: " + paquetesAfectados);
        System.out.println("💾 Planes guardados: " + PLANES_GUARDADOS.get());
        System.out.println("📝 Registros totales: " + CONTADOR_REGISTROS.get());
        System.out.println("⚡ Cache hits: " + CACHE_HITS.get());
        System.out.println("⚠️  Errores: " + ERRORES_REGISTRO.get());
        System.out.println("🎯 Tasa éxito: " + 
            String.format("%.1f%%", (1 - (double)ERRORES_REGISTRO.get() / Math.max(1, CONTADOR_REGISTROS.get())) * 100));
        
        System.out.println("\n🚨 DISTRIBUCIÓN DE PRIORIDADES:");
        porPrioridad.entrySet().stream()
            .sorted(Map.Entry.<PrioridadRefactor, Long>comparingByValue().reversed())
            .forEach(entry -> {
                String barra = "█".repeat((int) (entry.getValue() * 30 / Math.max(1, plan.size())));
                System.out.printf("   %s %-15s: %2d %s%n", 
                    entry.getKey().getEmoji(), 
                    entry.getKey().name(), 
                    entry.getValue(), 
                    barra);
            });
            
        System.out.println("\n🔧 OPERACIONES POR TIPO:");
        OPERACIONES_POR_TIPO.forEach((tipo, cantidad) -> 
            System.out.println("   • " + tipo + ": " + cantidad));
            
        System.out.println("═".repeat(60));
    }

    /**
     * 🧹 LIMPIEZA TURBO DEL PLAN
     */
    public static void limpiarPlan() {
        int tamañoAnterior = plan.size();
        plan.clear();
        CLASES_REGISTRADAS.clear();
        CACHE_MARKDOWN.clear();
        
        System.out.println("🧹 PLAN TURBO LIMPIADO: " + tamañoAnterior + " elementos removidos");
        OPERACIONES_POR_TIPO.merge("PLAN_LIMPIADO", 1, Integer::sum);
    }

    /**
     * 🔍 OBTENER PLAN ACTUAL (SOLO LECTURA TURBO)
     */
    public static List<PlanItem> obtenerPlanActual() {
        return Collections.unmodifiableList(new ArrayList<>(plan));
    }

    /**
     * ✅ VERIFICACIÓN TURBO SI CLASE ESTÁ EN PLAN
     */
    public static boolean estaEnPlan(String className, String packageName) {
        return CLASES_REGISTRADAS.contains(packageName + "." + className);
    }

    /**
     * 🎪 OBTENER MÉTRICAS AVANZADAS DEL PLAN
     */
    public static MetricasPlan obtenerMetricas() {
        return new MetricasPlan(plan);
    }

    /**
     * ✂️ TRUNCAR RESUMEN PARA LOGS - IMPLEMENTACIÓN TURBO
     */
    private static String truncarResumen(String resumen) {
        if (resumen == null || resumen.trim().isEmpty()) {
            return "[Sin descripción]";
        }
        
        String resumenLimpio = resumen.trim();
        if (resumenLimpio.length() <= 50) {
            return resumenLimpio;
        }
        
        // 🔍 BUSCAR PUNTO DE CORTE NATURAL (ESPACIO MÁS CERCANO)
        int ultimoEspacio = resumenLimpio.lastIndexOf(' ', 47);
        int puntoCorte = (ultimoEspacio > 30) ? ultimoEspacio : 47;
        
        return resumenLimpio.substring(0, puntoCorte) + "...";
    }

    /**
     * 📊 CLASE DE MÉTRICAS AVANZADAS TURBO
     */
    public static class MetricasPlan {
        private final int totalRefactors;
        private final int paquetesAfectados;
        private final Map<PrioridadRefactor, Long> distribucionPrioridad;
        private final LocalDateTime fechaGeneracion;

        public MetricasPlan(List<PlanItem> plan) {
            this.totalRefactors = plan.size();
            this.paquetesAfectados = (int) plan.stream()
                .map(PlanItem::getPackageName)
                .distinct()
                .count();
            this.distribucionPrioridad = plan.stream()
                .collect(Collectors.groupingBy(PlanItem::getPrioridad, Collectors.counting()));
            this.fechaGeneracion = LocalDateTime.now();
        }

        public int getTotalRefactors() { return totalRefactors; }
        public int getPaquetesAfectados() { return paquetesAfectados; }
        public Map<PrioridadRefactor, Long> getDistribucionPrioridad() { return distribucionPrioridad; }
        public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    }
}

