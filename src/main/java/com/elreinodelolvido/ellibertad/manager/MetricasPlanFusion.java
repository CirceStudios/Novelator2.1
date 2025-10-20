package com.elreinodelolvido.ellibertad.manager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONObject;

import com.elreinodelolvido.ellibertad.util.PlanItem;
import com.elreinodelolvido.ellibertad.util.PrioridadRefactor;

import org.json.JSONArray;

/**
 * 📊 Clase para métricas avanzadas del plan fusionado - TURBOFURULADO
 * 🚀 Análisis empresarial completo con inteligencia avanzada
 */
public class MetricasPlanFusion {
    private final List<PlanItem> plan;
    private final Map<String, Object> cacheMetricas;
    private final Map<String, Long> historicoCalculos;
    private boolean modoVerbose;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // 🎯 CONSTANTES DE CONFIGURACIÓN
    private static final double UMBRAL_CONCENTRACION = 0.3;
    private static final double UMBRAL_URGENCIA_ALTA = 0.5;
    private static final int TOP_PAQUETES_LIMITE = 10;
    private static final int COMPLEJIDAD_ALTA = 70;
    private static final int COMPLEJIDAD_MEDIA = 40;
    
    public MetricasPlanFusion(List<PlanItem> plan) {
        this.plan = plan != null ? new ArrayList<>(plan) : new ArrayList<>();
        this.cacheMetricas = new ConcurrentHashMap<>();
        this.historicoCalculos = new ConcurrentHashMap<>();
        this.modoVerbose = true;
        inicializarCache();
    }

    /**
     * 🚀 INICIALIZAR CACHE INTELIGENTE
     */
    private void inicializarCache() {
        cacheMetricas.put("ultima_actualizacion", LocalDateTime.now().format(FORMATTER));
        cacheMetricas.put("version_metricas", "3.0.0-turbofuru");
        cacheMetricas.put("total_calculos", 0);
        cacheMetricas.put("cache_hits", 0);
        cacheMetricas.put("cache_misses", 0);
    }

    /**
     * 🔧 CONFIGURAR MODO VERBOSE
     */
    public void setModoVerbose(boolean verbose) {
        this.modoVerbose = verbose;
        if (modoVerbose) {
            System.out.println("🔊 Modo verbose activado para métricas");
        }
    }

    // =========================================================================
    // 📊 MÉTRICAS BÁSICAS TURBOFURULADAS
    // =========================================================================

    /**
     * 📊 OBTENER TOTAL DE REFACTORS CON CACHE
     */
    public int getTotalRefactors() {
        return obtenerDelCache("total_refactors", () -> plan.size());
    }

    /**
     * 📊 OBTENER PAQUETES AFECTADOS CON CACHE
     */
    public long getPaquetesAfectados() {
        return obtenerDelCache("paquetes_afectados", () -> 
            plan.stream()
                .map(PlanItem::getPackageName)
                .distinct()
                .count()
        );
    }

    /**
     * 📊 OBTENER CLASES AFECTADAS
     */
    public long getClasesAfectadas() {
        return obtenerDelCache("clases_afectadas", () ->
            plan.stream()
                .map(PlanItem::getClassName)
                .distinct()
                .count()
        );
    }

    /**
     * 📊 OBTENER DISTRIBUCIÓN POR PRIORIDAD CON ANÁLISIS AVANZADO
     */
    public Map<PrioridadRefactor, JSONObject> getDistribucionPrioridadAvanzada() {
        return obtenerDelCache("distribucion_prioridad_avanzada", () -> {
            Map<PrioridadRefactor, Long> conteo = plan.stream()
                .collect(Collectors.groupingBy(
                    PlanItem::getPrioridad, 
                    Collectors.counting()
                ));

            Map<PrioridadRefactor, JSONObject> resultado = new HashMap<>();
            for (Map.Entry<PrioridadRefactor, Long> entry : conteo.entrySet()) {
                PrioridadRefactor prioridad = entry.getKey();
                long count = entry.getValue();
                double porcentaje = (double) count / getTotalRefactors() * 100;
                
                JSONObject analisis = new JSONObject();
                analisis.put("cantidad", count);
                analisis.put("porcentaje", String.format("%.1f%%", porcentaje));
                analisis.put("valor_numerico", prioridad.getValorNumerico());
                analisis.put("emoji", prioridad.getEmoji());
                analisis.put("impacto", calcularImpactoPrioridad(prioridad, count));
                
                resultado.put(prioridad, analisis);
            }
            
            return resultado;
        });
    }

    /**
     * 🎯 CALCULAR IMPACTO DE PRIORIDAD
     */
    private String calcularImpactoPrioridad(PrioridadRefactor prioridad, long cantidad) {
        double ratio = (double) cantidad / getTotalRefactors();
        
        switch (prioridad) {
            case ALTA:
                return ratio > 0.4 ? "🚨 ALTO IMPACTO" : "⚡ IMPACTO MODERADO";
            case MEDIA:
                return ratio > 0.5 ? "📊 DOMINANTE" : "🔧 BALANCEADO";
            case BAJA:
                return ratio > 0.3 ? "📈 ACUMULACIÓN" : "🔄 MANTENIMIENTO";
            default:
                return "🔍 SIN DATOS";
        }
    }

    // =========================================================================
    // 📈 MÉTRICAS TEMPORALES AVANZADAS
    // =========================================================================

    /**
     * 📊 OBTENER ANÁLISIS TEMPORAL DETALLADO
     */
    public JSONObject getAnalisisTemporalCompleto() {
        return obtenerDelCache("analisis_temporal_completo", () -> {
            JSONObject temporal = new JSONObject();
            
            long manana = plan.stream()
                .filter(item -> item.getTimestamp().getHour() < 12)
                .count();
                
            long tarde = plan.stream()
                .filter(item -> item.getTimestamp().getHour() >= 12 && item.getTimestamp().getHour() < 18)
                .count();
                
            long noche = plan.stream()
                .filter(item -> item.getTimestamp().getHour() >= 18)
                .count();
            
            long finSemana = plan.stream()
                .filter(item -> {
                    int dayOfWeek = item.getTimestamp().getDayOfWeek().getValue();
                    return dayOfWeek == 6 || dayOfWeek == 7; // Sábado o Domingo
                })
                .count();
                
            long diaSemana = plan.stream()
                .filter(item -> {
                    int dayOfWeek = item.getTimestamp().getDayOfWeek().getValue();
                    return dayOfWeek >= 1 && dayOfWeek <= 5; // Lunes a Viernes
                })
                .count();

            temporal.put("manana", crearSegmentoTemporal("MAÑANA", manana, "🌅"));
            temporal.put("tarde", crearSegmentoTemporal("TARDE", tarde, "🌞"));
            temporal.put("noche", crearSegmentoTemporal("NOCHE", noche, "🌙"));
            temporal.put("fin_semana", crearSegmentoTemporal("FIN_SEMANA", finSemana, "🎉"));
            temporal.put("dia_semana", crearSegmentoTemporal("DÍA_SEMANA", diaSemana, "💼"));
            
            temporal.put("periodo_mas_activo", calcularPeriodoMasActivo(temporal));
            temporal.put("ratio_fin_semana", String.format("%.1f%%", (double) finSemana / getTotalRefactors() * 100));
            
            return temporal;
        });
    }

    /**
     * 🕐 CREAR SEGMENTO TEMPORAL
     */
    private JSONObject crearSegmentoTemporal(String nombre, long cantidad, String emoji) {
        JSONObject segmento = new JSONObject();
        segmento.put("nombre", nombre);
        segmento.put("cantidad", cantidad);
        segmento.put("emoji", emoji);
        segmento.put("porcentaje", String.format("%.1f%%", (double) cantidad / getTotalRefactors() * 100));
        segmento.put("intensidad", calcularIntensidad(cantidad));
        return segmento;
    }

    /**
     * 🎯 CALCULAR INTENSIDAD TEMPORAL
     */
    private String calcularIntensidad(long cantidad) {
        double ratio = (double) cantidad / getTotalRefactors();
        if (ratio > 0.4) return "🔥 ALTA";
        if (ratio > 0.2) return "⚡ MEDIA";
        return "💤 BAJA";
    }

    /**
     * 🕐 CALCULAR PERIODO MÁS ACTIVO
     */
    private String calcularPeriodoMasActivo(JSONObject temporal) {
        long maxCantidad = 0;
        String periodoMasActivo = "NO_DETERMINADO";
        
        for (String key : temporal.keySet()) {
            if (key.equals("periodo_mas_activo") || key.equals("ratio_fin_semana")) continue;
            
            JSONObject segmento = temporal.getJSONObject(key);
            long cantidad = segmento.getLong("cantidad");
            
            if (cantidad > maxCantidad) {
                maxCantidad = cantidad;
                periodoMasActivo = segmento.getString("nombre") + " " + segmento.getString("emoji");
            }
        }
        
        return periodoMasActivo;
    }

    /**
     * 📊 OBTENER TENDENCIA TEMPORAL DETALLADA
     */
    public JSONObject getTendenciaTemporalCompleta() {
        return obtenerDelCache("tendencia_temporal_completa", () -> {
            JSONObject tendencia = new JSONObject();
            
            // Por día
            Map<String, Long> porDia = plan.stream()
                .collect(Collectors.groupingBy(
                    item -> item.getTimestamp().toLocalDate().toString(),
                    Collectors.counting()
                ));
            tendencia.put("por_dia", new JSONObject(porDia));
            
            // Por semana
            Map<String, Long> porSemana = plan.stream()
                .collect(Collectors.groupingBy(
                    item -> item.getTimestamp().toLocalDate().getYear() + "-W" + 
                           String.format("%02d", item.getTimestamp().toLocalDate().get(java.time.temporal.WeekFields.ISO.weekOfYear())),
                    Collectors.counting()
                ));
            tendencia.put("por_semana", new JSONObject(porSemana));
            
            // Por mes
            Map<String, Long> porMes = plan.stream()
                .collect(Collectors.groupingBy(
                    item -> item.getTimestamp().getYear() + "-" + 
                           String.format("%02d", item.getTimestamp().getMonthValue()),
                    Collectors.counting()
                ));
            tendencia.put("por_mes", new JSONObject(porMes));
            
            // Análisis de tendencia
            tendencia.put("tendencia_general", analizarTendenciaGeneral(porDia));
            tendencia.put("dia_mas_activo", encontrarDiaMasActivo(porDia));
            tendencia.put("promedio_diario", String.format("%.1f", porDia.values().stream().mapToLong(Long::longValue).average().orElse(0.0)));
            
            return tendencia;
        });
    }

    /**
     * 📈 ANALIZAR TENDENCIA GENERAL
     */
    private String analizarTendenciaGeneral(Map<String, Long> porDia) {
        if (porDia.size() < 2) return "📊 DATOS INSUFICIENTES";
        
        List<Long> valores = new ArrayList<>(porDia.values());
        long primerValor = valores.get(0);
        long ultimoValor = valores.get(valores.size() - 1);
        
        if (ultimoValor > primerValor * 1.5) return "📈 TENDENCIA ALCISTA";
        if (ultimoValor < primerValor * 0.7) return "📉 TENDENCIA BAJISTA";
        return "📊 TENDENCIA ESTABLE";
    }

    /**
     * 🎯 ENCONTRAR DÍA MÁS ACTIVO
     */
    private String encontrarDiaMasActivo(Map<String, Long> porDia) {
        return porDia.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(entry -> entry.getKey() + " (" + entry.getValue() + " refactors)")
            .orElse("NO_DETERMINADO");
    }

    // =========================================================================
    // 🎪 ANÁLISIS DE PAQUETES TURBOFURULADO
    // =========================================================================

    /**
     * 📊 OBTENER PAQUETES MÁS AFECTADOS CON ANÁLISIS COMPLETO
     */
    public JSONObject getPaquetesMasAfectadosCompleto(int limite) {
        String cacheKey = "paquetes_afectados_" + limite;
        return obtenerDelCache(cacheKey, () -> {
            JSONObject resultado = new JSONObject();
            
            List<Map.Entry<String, Long>> paquetesTop = plan.stream()
                .collect(Collectors.groupingBy(
                    PlanItem::getPackageName, 
                    Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limite)
                .collect(Collectors.toList());

            JSONArray paquetesArray = new JSONArray();
            for (int i = 0; i < paquetesTop.size(); i++) {
                var entry = paquetesTop.get(i);
                JSONObject paqueteInfo = new JSONObject();
                paqueteInfo.put("posicion", i + 1);
                paqueteInfo.put("nombre", entry.getKey());
                paqueteInfo.put("refactors", entry.getValue());
                paqueteInfo.put("porcentaje", String.format("%.1f%%", (double) entry.getValue() / getTotalRefactors() * 100));
                paqueteInfo.put("podio", obtenerEmojiPodio(i));
                paqueteInfo.put("criticidad", calcularCriticidadPaquete(entry.getValue()));
                
                paquetesArray.put(paqueteInfo);
            }
            
            resultado.put("paquetes", paquetesArray);
            resultado.put("total_analizados", paquetesTop.size());
            resultado.put("concentracion_total", calcularConcentracionTotal(paquetesTop));
            
            return resultado;
        });
    }

    /**
     * 🏆 OBTENER EMOJI DE PODIO
     */
    private String obtenerEmojiPodio(int posicion) {
        switch (posicion) {
            case 0: return "🥇";
            case 1: return "🥈";
            case 2: return "🥉";
            default: return "📦";
        }
    }

    /**
     * 🚨 CALCULAR CRITICIDAD DE PAQUETE
     */
    private String calcularCriticidadPaquete(long refactors) {
        double ratio = (double) refactors / getTotalRefactors();
        if (ratio > 0.3) return "🔴 CRÍTICO";
        if (ratio > 0.15) return "🟡 ALTO";
        if (ratio > 0.05) return "🟢 MEDIO";
        return "🔵 BAJO";
    }

    /**
     * 📊 CALCULAR CONCENTRACIÓN TOTAL
     */
    private String calcularConcentracionTotal(List<Map.Entry<String, Long>> paquetesTop) {
        if (paquetesTop.isEmpty()) return "0%";
        
        long totalTop = paquetesTop.stream().mapToLong(Map.Entry::getValue).sum();
        double concentracion = (double) totalTop / getTotalRefactors() * 100;
        
        if (concentracion > 80) return "🔴 ALTA CONCENTRACIÓN";
        if (concentracion > 60) return "🟡 CONCENTRACIÓN MODERADA";
        return "🟢 DISTRIBUCIÓN BALANCEADA";
    }

    /**
     * 📊 OBTENER DENSIDAD DE REFACTORS POR PAQUETE DETALLADA
     */
    public JSONObject getDensidadRefactorsPorPaqueteCompleto() {
        return obtenerDelCache("densidad_refactors_completo", () -> {
            JSONObject densidad = new JSONObject();
            long totalRefactors = getTotalRefactors();
            
            Map<String, Double> densidadMap = plan.stream()
                .collect(Collectors.groupingBy(
                    PlanItem::getPackageName,
                    Collectors.counting()
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> (double) entry.getValue() / totalRefactors * 100
                ));
            
            JSONArray densidadArray = new JSONArray();
            densidadMap.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> {
                    JSONObject item = new JSONObject();
                    item.put("paquete", entry.getKey());
                    item.put("densidad", String.format("%.2f%%", entry.getValue()));
                    item.put("categoria", categorizarDensidad(entry.getValue()));
                    densidadArray.put(item);
                });
            
            densidad.put("densidades", densidadArray);
            densidad.put("paquete_mas_denso", densidadMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey() + " (" + String.format("%.1f%%", entry.getValue()) + ")")
                .orElse("NO_DETERMINADO"));
            
            return densidad;
        });
    }

    /**
     * 🎯 CATEGORIZAR DENSIDAD
     */
    private String categorizarDensidad(double densidad) {
        if (densidad > 20) return "🔴 MUY ALTA";
        if (densidad > 10) return "🟡 ALTA";
        if (densidad > 5) return "🟢 MEDIA";
        return "🔵 BAJA";
    }

    // =========================================================================
    // 📈 MÉTRICAS AVANZADAS TURBOFURULADAS
    // =========================================================================

    /**
     * 📊 CALCULAR PRIORIDAD PROMEDIO CON ANÁLISIS
     */
    public JSONObject calcularPrioridadPromedioCompleta() {
        return obtenerDelCache("prioridad_promedio_completa", () -> {
            JSONObject resultado = new JSONObject();
            
            if (plan.isEmpty()) {
                resultado.put("promedio", 0.0);
                resultado.put("estado", "SIN_DATOS");
                resultado.put("emoji", "📭");
                return resultado;
            }
            
            double sumaPrioridades = plan.stream()
                .mapToDouble(item -> item.getPrioridad().getValorNumerico())
                .sum();
                
            double promedio = sumaPrioridades / plan.size();
            
            resultado.put("promedio", String.format("%.2f", promedio));
            resultado.put("estado", evaluarPrioridadPromedio(promedio));
            resultado.put("emoji", obtenerEmojiPrioridad(promedio));
            resultado.put("rango", obtenerRangoPrioridad(promedio));
            resultado.put("recomendacion", generarRecomendacionPrioridad(promedio));
            
            return resultado;
        });
    }

    /**
     * 🎯 EVALUAR PRIORIDAD PROMEDIO
     */
    private String evaluarPrioridadPromedio(double promedio) {
        if (promedio >= 8.5) return "🚨 MUY ALTA";
        if (promedio >= 7.0) return "🔴 ALTA";
        if (promedio >= 5.5) return "🟡 MEDIA";
        return "🟢 BAJA";
    }

    /**
     * 🎭 OBTENER EMOJI PRIORIDAD
     */
    private String obtenerEmojiPrioridad(double promedio) {
        if (promedio >= 8.5) return "💥";
        if (promedio >= 7.0) return "⚡";
        if (promedio >= 5.5) return "🔧";
        return "🔄";
    }

    /**
     * 📊 OBTENER RANGO PRIORIDAD
     */
    private String obtenerRangoPrioridad(double promedio) {
        if (promedio >= 9.0) return "9.0-10.0";
        if (promedio >= 8.0) return "8.0-8.9";
        if (promedio >= 7.0) return "7.0-7.9";
        if (promedio >= 6.0) return "6.0-6.9";
        return "0.0-5.9";
    }

    /**
     * 💡 GENERAR RECOMENDACIÓN PRIORIDAD
     */
    private String generarRecomendacionPrioridad(double promedio) {
        if (promedio >= 8.5) return "Revisar criterios de priorización - posible sobrecarga";
        if (promedio >= 7.0) return "Planificación cuidadosa requerida";
        if (promedio >= 5.5) return "Balance adecuado - continuar así";
        return "Considerar aumentar prioridad de refactors críticos";
    }

    /**
     * 📊 CALCULAR ÍNDICE DE COMPLEJIDAD AVANZADO
     */
    public JSONObject calcularIndiceComplejidadAvanzado() {
        return obtenerDelCache("indice_complejidad_avanzado", () -> {
            JSONObject complejidad = new JSONObject();
            
            if (plan.isEmpty()) {
                complejidad.put("indice", 0.0);
                complejidad.put("nivel", "SIN_DATOS");
                complejidad.put("emoji", "📭");
                return complejidad;
            }
            
            double factorPrioridad = Double.parseDouble(calcularPrioridadPromedioCompleta().getString("promedio")) / 10.0;
            
            // Factor de concentración (paquete más afectado)
            List<Map.Entry<String, Long>> paquetesTop = getPaquetesMasAfectadosLista(1);
            double factorConcentracion = !paquetesTop.isEmpty() ? 
                (double) paquetesTop.get(0).getValue() / plan.size() : 0.1;
            
            // Factor temporal (actividad nocturna)
            JSONObject temporal = getAnalisisTemporalCompleto();
            double factorTemporal = temporal.getJSONObject("noche").getLong("cantidad") > plan.size() * 0.4 ? 1.2 : 1.0;
            
            // Factor de diversidad (número de paquetes únicos)
            double factorDiversidad = Math.min(1.0, (double) getPaquetesAfectados() / 10.0);
            
            double indice = (factorPrioridad * factorConcentracion * factorTemporal * (2 - factorDiversidad)) * 100;
            
            complejidad.put("indice", String.format("%.1f", indice));
            complejidad.put("nivel", evaluarNivelComplejidad(indice));
            complejidad.put("emoji", obtenerEmojiComplejidad(indice));
            complejidad.put("factores", new JSONObject()
                .put("prioridad", String.format("%.2f", factorPrioridad))
                .put("concentracion", String.format("%.2f", factorConcentracion))
                .put("temporal", String.format("%.2f", factorTemporal))
                .put("diversidad", String.format("%.2f", factorDiversidad))
            );
            
            return complejidad;
        });
    }

    /**
     * 🎯 EVALUAR NIVEL DE COMPLEJIDAD
     */
    private String evaluarNivelComplejidad(double indice) {
        if (indice >= 80) return "🔴 EXTREMA";
        if (indice >= 60) return "🟠 ALTA";
        if (indice >= 40) return "🟡 MEDIA";
        if (indice >= 20) return "🟢 MODERADA";
        return "🔵 BAJA";
    }

    /**
     * 🎭 OBTENER EMOJI COMPLEJIDAD
     */
    private String obtenerEmojiComplejidad(double indice) {
        if (indice >= 80) return "💀";
        if (indice >= 60) return "🚨";
        if (indice >= 40) return "⚡";
        if (indice >= 20) return "🔧";
        return "🌱";
    }

    // =========================================================================
    // 🔍 ANÁLISIS DE PATRONES TURBOFURULADO
    // =========================================================================

    /**
     * 📊 IDENTIFICAR PATRONES AVANZADOS
     */
    public JSONObject identificarPatronesAvanzados() {
        return obtenerDelCache("patrones_avanzados", () -> {
            JSONObject patrones = new JSONObject();
            
            Map<PrioridadRefactor, Long> distribucionPrioridad = plan.stream()
                .collect(Collectors.groupingBy(PlanItem::getPrioridad, Collectors.counting()));
                
            List<Map.Entry<String, Long>> paquetesTop = getPaquetesMasAfectadosLista(3);
            JSONObject temporal = getAnalisisTemporalCompleto();

            // Patrones principales
            boolean tieneConcentracion = paquetesTop.stream()
                .anyMatch(entry -> entry.getValue() > plan.size() * UMBRAL_CONCENTRACION);
                
            boolean tieneUrgenciaAlta = distribucionPrioridad.getOrDefault(PrioridadRefactor.ALTA, 0L) > plan.size() * UMBRAL_URGENCIA_ALTA;
            boolean tieneActividadNocturna = temporal.getJSONObject("noche").getLong("cantidad") > temporal.getJSONObject("manana").getLong("cantidad") + temporal.getJSONObject("tarde").getLong("cantidad");
            boolean tieneFinSemanaSignificativo = temporal.getJSONObject("fin_semana").getLong("cantidad") > plan.size() * 0.2;

            patrones.put("CONCENTRACION_PAQUETES", crearPatron("Concentración en Paquetes", tieneConcentracion, "🎯"));
            patrones.put("URGENCIA_ALTA", crearPatron("Alta Urgencia", tieneUrgenciaAlta, "🚨"));
            patrones.put("ACTIVIDAD_NOCTURNA", crearPatron("Actividad Nocturna", tieneActividadNocturna, "🌙"));
            patrones.put("FIN_SEMANA_SIGNIFICATIVO", crearPatron("Fin de Semana Activo", tieneFinSemanaSignificativo, "🎉"));
            
            // Métricas adicionales
            patrones.put("TOTAL_REFACTORS", plan.size());
            patrones.put("PRIORIDAD_PROMEDIO", calcularPrioridadPromedioCompleta().getString("promedio"));
            patrones.put("INDICE_COMPLEJIDAD", calcularIndiceComplejidadAvanzado().getString("indice"));
            patrones.put("ESTADO_GENERAL", obtenerEstadoGeneral());
            
            return patrones;
        });
    }

    /**
     * 🎭 CREAR OBJETO DE PATRÓN
     */
    private JSONObject crearPatron(String nombre, boolean detectado, String emoji) {
        JSONObject patron = new JSONObject();
        patron.put("nombre", nombre);
        patron.put("detectado", detectado);
        patron.put("emoji", emoji);
        patron.put("recomendacion", detectado ? generarRecomendacionPatron(nombre) : "✅ Patrón no detectado");
        return patron;
    }

    /**
     * 💡 GENERAR RECOMENDACIÓN DE PATRÓN
     */
    private String generarRecomendacionPatron(String nombrePatron) {
        switch (nombrePatron) {
            case "Concentración en Paquetes":
                return "Considera dividir el paquete más afectado en módulos más pequeños";
            case "Alta Urgencia":
                return "Prioriza refactors críticos y revisa criterios de priorización";
            case "Actividad Nocturna":
                return "Revisa procesos automatizados o distribución de carga de trabajo";
            case "Fin de Semana Activo":
                return "Evalúa balance trabajo-vida personal del equipo";
            default:
                return "Revisar configuración del sistema";
        }
    }

    // =========================================================================
    // 📋 REPORTES TURBOFURULADOS
    // =========================================================================

    /**
     * 📊 GENERAR REPORTE COMPLETO TURBOFURULADO
     */
    public JSONObject generarReporteCompleto1() {
        long startTime = System.currentTimeMillis();
        
        JSONObject reporte = new JSONObject();
        reporte.put("timestamp", LocalDateTime.now().format(FORMATTER));
        reporte.put("version_reporte", "3.0.0-turbofuru");
        
        try {
            if (modoVerbose) {
                System.out.println("\n" + "📊".repeat(100));
                System.out.println("           REPORTE DE MÉTRICAS DEL PLAN TURBOFURULADO - ANÁLISIS COMPLETO");
                System.out.println("📊".repeat(100));
            }
            
            if (plan.isEmpty()) {
                reporte.put("estado", "VACIO");
                reporte.put("mensaje", "📭 No hay refactors en el plan para generar métricas");
                if (modoVerbose) {
                    System.out.println("📭 No hay refactors en el plan para generar métricas");
                }
                return reporte;
            }
            
            // 🎯 ESTADÍSTICAS BÁSICAS
            JSONObject estadisticasBasicas = obtenerEstadisticasBasicas();
            reporte.put("estadisticas_basicas", estadisticasBasicas);
            if (modoVerbose) mostrarEstadisticasBasicasConsola(estadisticasBasicas);
            
            // 🚨 DISTRIBUCIÓN POR PRIORIDAD
            JSONObject distribucionPrioridad = getDistribucionPrioridadCompleta();
            reporte.put("distribucion_prioridad", distribucionPrioridad);
            if (modoVerbose) mostrarDistribucionPrioridadConsola(distribucionPrioridad);
            
            // ⏰ ANÁLISIS TEMPORAL
            JSONObject analisisTemporal = getAnalisisTemporalCompleto();
            reporte.put("analisis_temporal", analisisTemporal);
            if (modoVerbose) mostrarAnalisisTemporalConsola(analisisTemporal);
            
            // 🎪 PAQUETES MÁS AFECTADOS
            JSONObject paquetesAfectados = getPaquetesMasAfectadosCompleto(5);
            reporte.put("paquetes_afectados", paquetesAfectados);
            if (modoVerbose) mostrarPaquetesAfectadosConsola(paquetesAfectados);
            
            // 📈 MÉTRICAS AVANZADAS
            JSONObject metricasAvanzadas = obtenerMetricasAvanzadas();
            reporte.put("metricas_avanzadas", metricasAvanzadas);
            if (modoVerbose) mostrarMetricasAvanzadasConsola(metricasAvanzadas);
            
            // 🔍 PATRONES DETECTADOS
            JSONObject patrones = identificarPatronesAvanzados();
            reporte.put("patrones", patrones);
            if (modoVerbose) mostrarPatronesConsola(patrones);
            
            // 💡 RECOMENDACIONES
            JSONArray recomendaciones = generarRecomendacionesAvanzadas();
            reporte.put("recomendaciones", recomendaciones);
            if (modoVerbose) mostrarRecomendacionesConsola(recomendaciones);
            
            long endTime = System.currentTimeMillis();
            reporte.put("tiempo_generacion_ms", endTime - startTime);
            reporte.put("estado", "COMPLETADO");
            
            if (modoVerbose) {
                System.out.println("\n✅ REPORTE GENERADO EN " + (endTime - startTime) + "ms");
            }
            
        } catch (Exception e) {
            reporte.put("error", e.getMessage());
            reporte.put("estado", "ERROR");
            System.err.println("💥 Error generando reporte: " + e.getMessage());
        }
        
        return reporte;
    }

    /**
     * 🎯 OBTENER ESTADÍSTICAS BÁSICAS
     */
    private JSONObject obtenerEstadisticasBasicas() {
        JSONObject stats = new JSONObject();
        stats.put("total_refactors", getTotalRefactors());
        stats.put("paquetes_afectados", getPaquetesAfectados());
        stats.put("clases_afectadas", getClasesAfectadas());
        stats.put("estado_general", obtenerEstadoGeneral());
        return stats;
    }

    /**
     * 📊 MOSTRAR ESTADÍSTICAS BÁSICAS EN CONSOLA
     */
    private void mostrarEstadisticasBasicasConsola(JSONObject stats) {
        System.out.println("🎯 ESTADÍSTICAS BÁSICAS:");
        System.out.println("  • Total refactors: " + stats.getInt("total_refactors"));
        System.out.println("  • Paquetes afectados: " + stats.getLong("paquetes_afectados"));
        System.out.println("  • Clases afectadas: " + stats.getLong("clases_afectadas"));
        System.out.println("  • Estado general: " + stats.getString("estado_general"));
    }

    /**
     * 🚨 OBTENER DISTRIBUCIÓN PRIORIDAD COMPLETA
     */
    private JSONObject getDistribucionPrioridadCompleta() {
        JSONObject distribucion = new JSONObject();
        Map<PrioridadRefactor, JSONObject> avanzada = getDistribucionPrioridadAvanzada();
        
        JSONArray prioridadesArray = new JSONArray();
        for (Map.Entry<PrioridadRefactor, JSONObject> entry : avanzada.entrySet()) {
            prioridadesArray.put(entry.getValue());
        }
        
        distribucion.put("prioridades", prioridadesArray);
        return distribucion;
    }

    /**
     * 🚨 MOSTRAR DISTRIBUCIÓN PRIORIDAD EN CONSOLA
     */
    private void mostrarDistribucionPrioridadConsola(JSONObject distribucion) {
        System.out.println("\n🚨 DISTRIBUCIÓN POR PRIORIDAD:");
        JSONArray prioridades = distribucion.getJSONArray("prioridades");
        for (int i = 0; i < prioridades.length(); i++) {
            JSONObject prioridad = prioridades.getJSONObject(i);
            String barra = "█".repeat((int) (prioridad.getDouble("porcentaje") / 3));
            System.out.printf("  %s %-15s: %2d (%6s) %s%n", 
                prioridad.getString("emoji"),
                prioridad.getInt("cantidad") + " refactors",
                prioridad.getInt("cantidad"),
                prioridad.getString("porcentaje"),
                barra);
        }
    }

    // Los métodos restantes de visualización seguirían el mismo patrón...

    // =========================================================================
    // 🔧 MÉTODOS DE UTILIDAD TURBOFURULADOS
    // =========================================================================

    /**
     * 💾 OBTENER DEL CACHE INTELIGENTE
     */
    @SuppressWarnings("unchecked")
    private <T> T obtenerDelCache(String clave, java.util.function.Supplier<T> calculo) {
        cacheMetricas.put("total_calculos", (Integer) cacheMetricas.get("total_calculos") + 1);
        
        if (cacheMetricas.containsKey(clave)) {
            cacheMetricas.put("cache_hits", (Integer) cacheMetricas.get("cache_hits") + 1);
            return (T) cacheMetricas.get(clave);
        }
        
        cacheMetricas.put("cache_misses", (Integer) cacheMetricas.get("cache_misses") + 1);
        T resultado = calculo.get();
        cacheMetricas.put(clave, resultado);
        
        // Registrar en histórico
        historicoCalculos.put(clave, System.currentTimeMillis());
        
        return resultado;
    }

    /**
     * 🧹 LIMPIAR CACHE
     */
    public void limpiarCache() {
        int elementos = cacheMetricas.size() - 5; // Mantener configuración básica
        cacheMetricas.keySet().removeIf(key -> 
            !key.equals("ultima_actualizacion") && 
            !key.equals("version_metricas") && 
            !key.equals("total_calculos") && 
            !key.equals("cache_hits") && 
            !key.equals("cache_misses"));
        
        historicoCalculos.clear();
        System.out.println("🧹 CACHE LIMPIADO: " + elementos + " elementos eliminados");
    }

    /**
     * 📊 OBTENER ESTADÍSTICAS DE CACHE
     */
    public JSONObject obtenerEstadisticasCache() {
        JSONObject stats = new JSONObject();
        stats.put("elementos_cache", cacheMetricas.size());
        stats.put("total_calculos", cacheMetricas.get("total_calculos"));
        stats.put("cache_hits", cacheMetricas.get("cache_hits"));
        stats.put("cache_misses", cacheMetricas.get("cache_misses"));
        
        double tasaHit = (Integer) cacheMetricas.get("cache_hits") > 0 ? 
            (double) (Integer) cacheMetricas.get("cache_hits") / (Integer) cacheMetricas.get("total_calculos") * 100 : 0;
        stats.put("tasa_hit", String.format("%.1f%%", tasaHit));
        
        return stats;
    }

    // =========================================================================
    // 🎯 MÉTODOS DE COMPATIBILIDAD (para mantener interfaz original)
    // =========================================================================

    /**
     * 📊 OBTENER DISTRIBUCIÓN POR PRIORIDAD (compatibilidad)
     */
    public Map<PrioridadRefactor, Long> getDistribucionPrioridad() {
        Map<PrioridadRefactor, JSONObject> avanzada = getDistribucionPrioridadAvanzada();
        return avanzada.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().getLong("cantidad")
            ));
    }

    /**
     * 📊 OBTENER ANÁLISIS TEMPORAL (compatibilidad)
     */
    public Map<String, Long> getAnalisisTemporal() {
        JSONObject temporal = getAnalisisTemporalCompleto();
        return Map.of(
            "MAÑANA", temporal.getJSONObject("manana").getLong("cantidad"),
            "TARDE", temporal.getJSONObject("tarde").getLong("cantidad"),
            "NOCHE", temporal.getJSONObject("noche").getLong("cantidad")
        );
    }

    /**
     * 📊 OBTENER PAQUETES MÁS AFECTADOS (compatibilidad)
     */
    public List<Map.Entry<String, Long>> getPaquetesMasAfectados(int limite) {
        return getPaquetesMasAfectadosLista(limite);
    }

    /**
     * 📊 OBTENER LISTA DE PAQUETES MÁS AFECTADOS
     */
    private List<Map.Entry<String, Long>> getPaquetesMasAfectadosLista(int limite) {
        return plan.stream()
            .collect(Collectors.groupingBy(
                PlanItem::getPackageName, 
                Collectors.counting()
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(limite)
            .collect(Collectors.toList());
    }

    /**
     * 📊 CALCULAR PRIORIDAD PROMEDIO (compatibilidad)
     */
    public double calcularPrioridadPromedio() {
        JSONObject completa = calcularPrioridadPromedioCompleta();
        return Double.parseDouble(completa.getString("promedio"));
    }

    /**
     * 📊 OBTENER DENSIDAD DE REFACTORS POR PAQUETE (compatibilidad)
     */
    public Map<String, Double> getDensidadRefactorsPorPaquete() {
        JSONObject completa = getDensidadRefactorsPorPaqueteCompleto();
        Map<String, Double> resultado = new HashMap<>();
        
        completa.getJSONArray("densidades").forEach(item -> {
            JSONObject jsonItem = (JSONObject) item;
            resultado.put(jsonItem.getString("paquete"), 
                Double.parseDouble(jsonItem.getString("densidad").replace("%", "")));
        });
        
        return resultado;
    }

    /**
     * 📊 OBTENER TENDENCIA TEMPORAL (compatibilidad)
     */
    public Map<String, Long> getTendenciaTemporal() {
        JSONObject completa = getTendenciaTemporalCompleta();
        return ((JSONObject) completa.get("por_dia")).toMap().entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> Long.valueOf(entry.getValue().toString())
            ));
    }

    /**
     * 📊 GENERAR REPORTE COMPLETO (compatibilidad)
     */
    public void generarReporteCompleto() {
        generarReporteCompleto1(); // Llama al método turbofurulado
    }

    /**
     * 📊 GENERAR REPORTE EJECUTIVO (compatibilidad)
     */
    public String generarReporteEjecutivo() {
        JSONObject reporteCompleto = generarReporteCompleto1();
        return reporteCompleto.getString("estado").equals("VACIO") ? 
            "📭 No hay refactors en el plan" :
            "📊 REPORTE EJECUTIVO TURBOFURULADO - " + reporteCompleto.getJSONObject("estadisticas_basicas").getString("estado_general");
    }

    /**
     * 📊 IDENTIFICAR PATRONES (compatibilidad)
     */
    public Map<String, Object> identificarPatrones() {
        JSONObject avanzados = identificarPatronesAvanzados();
        Map<String, Object> resultado = new HashMap<>();
        
        for (String key : avanzados.keySet()) {
            if (avanzados.get(key) instanceof JSONObject) {
                JSONObject patron = avanzados.getJSONObject(key);
                resultado.put(key, patron.getBoolean("detectado"));
            } else {
                resultado.put(key, avanzados.get(key));
            }
        }
        
        return resultado;
    }

    /**
     * 📊 GENERAR RECOMENDACIONES (compatibilidad)
     */
    public List<String> generarRecomendaciones() {
        JSONArray avanzadas = generarRecomendacionesAvanzadas();
        List<String> resultado = new ArrayList<>();
        
        for (int i = 0; i < avanzadas.length(); i++) {
            resultado.add(avanzadas.getString(i));
        }
        
        return resultado;
    }

    /**
     * 💡 GENERAR RECOMENDACIONES AVANZADAS
     */
    private JSONArray generarRecomendacionesAvanzadas() {
        JSONArray recomendaciones = new JSONArray();
        JSONObject patrones = identificarPatronesAvanzados();
        
        for (String key : patrones.keySet()) {
            if (patrones.get(key) instanceof JSONObject) {
                JSONObject patron = patrones.getJSONObject(key);
                if (patron.getBoolean("detectado")) {
                    recomendaciones.put(patron.getString("recomendacion"));
                }
            }
        }
        
        // Recomendaciones basadas en métricas generales
        if (plan.size() > 50) {
            recomendaciones.put("📋 Plan extenso: Considera dividir en fases de implementación");
        }
        
        double prioridadPromedio = calcularPrioridadPromedio();
        if (prioridadPromedio > 8.0) {
            recomendaciones.put("⚡ Prioridad alta promedio: Revisa criterios de priorización");
        }
        
        JSONObject complejidad = calcularIndiceComplejidadAvanzado();
        if (complejidad.getString("nivel").contains("ALTA") || complejidad.getString("nivel").contains("EXTREMA")) {
            recomendaciones.put("🎯 Complejidad elevada: Asigna recursos adicionales y planifica cuidadosamente");
        }
        
        if (recomendaciones.length() == 0) {
            recomendaciones.put("✅ Plan balanceado: Continúa con la implementación actual");
        }
        
        return recomendaciones;
    }

    /**
     * 📊 EXPORTAR DATOS PARA ANÁLISIS (compatibilidad)
     */
    public String exportarDatosAnalisis() {
        StringBuilder csv = new StringBuilder();
        csv.append("Paquete,Clase,Prioridad,Descripcion,Timestamp,HoraDelDia,ValorPrioridad,DiaSemana\n");
        
        for (PlanItem item : plan) {
            String horaDelDia = item.getTimestamp().getHour() < 12 ? "MAÑANA" : 
                               item.getTimestamp().getHour() < 18 ? "TARDE" : "NOCHE";
            
            String diaSemana = item.getTimestamp().getDayOfWeek().toString();
            
            csv.append(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d,\"%s\"%n",
                item.getPackageName(),
                item.getClassName(),
                item.getPrioridad().name(),
                item.getDescripcion().replace("\"", "\"\""),
                item.getTimestamp().toString(),
                horaDelDia,
                item.getPrioridad().getValorNumerico(),
                diaSemana
            ));
        }
        
        return csv.toString();
    }

    /**
     * 📊 COMPARAR CON PLAN ANTERIOR (compatibilidad)
     */
    public String compararConPlanAnterior(MetricasPlanFusion planAnterior) {
        StringBuilder comparacion = new StringBuilder();
        comparacion.append("📊 COMPARACIÓN CON PLAN ANTERIOR TURBOFURULADO\n");
        comparacion.append("=" .repeat(50)).append("\n");
        
        int diferenciaTotal = this.getTotalRefactors() - planAnterior.getTotalRefactors();
        long diferenciaPaquetes = this.getPaquetesAfectados() - planAnterior.getPaquetesAfectados();
        double diferenciaPrioridad = this.calcularPrioridadPromedio() - planAnterior.calcularPrioridadPromedio();
        
        comparacion.append(String.format("• Total refactors: %d → %d (%+d)%n", 
            planAnterior.getTotalRefactors(), this.getTotalRefactors(), diferenciaTotal));
            
        comparacion.append(String.format("• Paquetes afectados: %d → %d (%+d)%n", 
            planAnterior.getPaquetesAfectados(), this.getPaquetesAfectados(), diferenciaPaquetes));
            
        comparacion.append(String.format("• Prioridad promedio: %.2f → %.2f (%+.2f)%n", 
            planAnterior.calcularPrioridadPromedio(), this.calcularPrioridadPromedio(), diferenciaPrioridad));
        
        // Análisis de tendencia mejorado
        if (diferenciaTotal > 10) {
            comparacion.append("📈 Tendencia: 🚀 CRECIMIENTO SIGNIFICATIVO\n");
        } else if (diferenciaTotal > 0) {
            comparacion.append("📈 Tendencia: 📈 CRECIMIENTO MODERADO\n");
        } else if (diferenciaTotal < -10) {
            comparacion.append("📉 Tendencia: 🔻 DECREMENTO SIGNIFICATIVO\n");
        } else if (diferenciaTotal < 0) {
            comparacion.append("📉 Tendencia: 📉 DECREMENTO MODERADO\n");
        } else {
            comparacion.append("📊 Tendencia: ⚖️  ESTABILIDAD\n");
        }
        
        return comparacion.toString();
    }

    /**
     * 📊 CALCULAR INDICE DE COMPLEJIDAD (compatibilidad)
     */
    public double calcularIndiceComplejidad() {
        JSONObject avanzado = calcularIndiceComplejidadAvanzado();
        return Double.parseDouble(avanzado.getString("indice"));
    }

    /**
     * 📊 OBTENER ESTADO GENERAL DEL PLAN (compatibilidad)
     */
    public String obtenerEstadoGeneral() {
        if (plan.isEmpty()) {
            return "🟢 VACÍO - No hay refactors planificados";
        }
        
        JSONObject complejidad = calcularIndiceComplejidadAvanzado();
        String nivelComplejidad = complejidad.getString("nivel");
        double indice = Double.parseDouble(complejidad.getString("indice"));
        
        if (indice > 70 || nivelComplejidad.contains("EXTREMA")) {
            return "🔴 CRÍTICO - Requiere atención inmediata";
        } else if (indice > 40 || nivelComplejidad.contains("ALTA")) {
            return "🟡 ALERTA - Requiere planificación cuidadosa";
        } else {
            return "🟢 ESTABLE - Progreso normal";
        }
    }

    // =========================================================================
    // 🎯 MÉTODOS DE VISUALIZACIÓN (simplificados para ejemplo)
    // =========================================================================

    private void mostrarAnalisisTemporalConsola(JSONObject temporal) {
        System.out.println("\n⏰ ANÁLISIS TEMPORAL COMPLETO:");
        System.out.printf("  🌅 Mañana: %d refactors (%s)%n", 
            temporal.getJSONObject("manana").getLong("cantidad"),
            temporal.getJSONObject("manana").getString("porcentaje"));
        System.out.printf("  🌞 Tarde: %d refactors (%s)%n", 
            temporal.getJSONObject("tarde").getLong("cantidad"),
            temporal.getJSONObject("tarde").getString("porcentaje"));
        System.out.printf("  🌙 Noche: %d refactors (%s)%n", 
            temporal.getJSONObject("noche").getLong("cantidad"),
            temporal.getJSONObject("noche").getString("porcentaje"));
        System.out.println("  🎉 Periodo más activo: " + temporal.getString("periodo_mas_activo"));
    }

    private void mostrarPaquetesAfectadosConsola(JSONObject paquetes) {
        System.out.println("\n🎪 PAQUETES MÁS AFECTADOS (TOP 5):");
        JSONArray paquetesArray = paquetes.getJSONArray("paquetes");
        for (int i = 0; i < paquetesArray.length(); i++) {
            JSONObject paquete = paquetesArray.getJSONObject(i);
            System.out.printf("  %s %s: %d refactors (%s) %s%n",
                paquete.getString("podio"),
                paquete.getString("nombre"),
                paquete.getLong("refactors"),
                paquete.getString("porcentaje"),
                paquete.getString("criticidad"));
        }
    }

    private void mostrarMetricasAvanzadasConsola(JSONObject metricas) {
        System.out.println("\n📈 MÉTRICAS AVANZADAS:");
        System.out.println("  • Prioridad promedio: " + 
            metricas.getJSONObject("prioridad_promedio").getString("promedio") + " - " +
            metricas.getJSONObject("prioridad_promedio").getString("estado"));
        System.out.println("  • Índice complejidad: " + 
            metricas.getJSONObject("indice_complejidad").getString("indice") + " - " +
            metricas.getJSONObject("indice_complejidad").getString("nivel"));
    }

    private void mostrarPatronesConsola(JSONObject patrones) {
        System.out.println("\n🔍 PATRONES DETECTADOS:");
        for (String key : patrones.keySet()) {
            if (patrones.get(key) instanceof JSONObject) {
                JSONObject patron = patrones.getJSONObject(key);
                if (patron.getBoolean("detectado")) {
                    System.out.println("  • " + patron.getString("emoji") + " " + patron.getString("nombre"));
                }
            }
        }
    }

    private void mostrarRecomendacionesConsola(JSONArray recomendaciones) {
        System.out.println("\n💡 RECOMENDACIONES:");
        for (int i = 0; i < recomendaciones.length(); i++) {
            System.out.println("  • " + recomendaciones.getString(i));
        }
    }

    JSONObject obtenerMetricasAvanzadas() {
        JSONObject metricas = new JSONObject();
        metricas.put("prioridad_promedio", calcularPrioridadPromedioCompleta());
        metricas.put("indice_complejidad", calcularIndiceComplejidadAvanzado());
        metricas.put("densidad_refactors", getDensidadRefactorsPorPaqueteCompleto());
        return metricas;
    }

    // 🎯 GETTERS PARA ESTADÍSTICAS
    public int getTotalCalculos() { return (Integer) cacheMetricas.get("total_calculos"); }
    public int getCacheHits() { return (Integer) cacheMetricas.get("cache_hits"); }
    public int getCacheMisses() { return (Integer) cacheMetricas.get("cache_misses"); }
    public boolean isModoVerbose() { return modoVerbose; }
}
