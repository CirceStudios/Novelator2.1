package com.elreinodelolvido.ellibertad.util;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 📄 Exportador de planes en formato Markdown para 🌩️ Madre Tormenta TURBO
 */
public class PlanMarkdownExporter {

    /**
     * 🔥 Exporta el plan de refactors en formato Markdown completo
     */
    public static String exportar(List<PlanItem> items, String timestamp) {
        StringBuilder sb = new StringBuilder();

        // 🎨 Cabecera épica
        sb.append(generarCabecera(timestamp, items.size()));

        // 📋 Resumen ejecutivo
        sb.append("## 📋 Resumen Ejecutivo\n\n");
        sb.append(generarResumenEjecutivo(items)).append("\n\n");

        // 🎯 Refactors planificados
        sb.append("## 🎯 Refactors Planificados\n\n");
        sb.append(generarRefactorsPorPaquete(items));

        // 📈 Métricas avanzadas
        sb.append("## 📈 Métricas del Plan\n\n");
        sb.append(generarMetricasAvanzadas(items));

        // 🏁 Footer épico
        sb.append("\n---\n");
        sb.append("*⚡ Generado automáticamente por 🌩️ Madre Tormenta TURBO*\n");

        return sb.toString();
    }

    private static String generarCabecera(String timestamp, int totalItems) {
        return String.format("""
            # 🌩️ PLAN DE REFACTORIZACIONES - MADRE TORMENTA TURBO

            **Generado**: %s  
            **Total de refactors planificados**: %d  
            **Estado**: 🧭 Pendiente de ejecución  
            **Versión**: ⚡ TURBO EDITION

            """, timestamp, totalItems);
    }

    private static String generarResumenEjecutivo(List<PlanItem> items) {
        Map<Object, Long> porPrioridad = items.stream()
            .collect(Collectors.groupingBy(PlanItem::getPrioridad, Collectors.counting()));

        long conProblemas = items.stream()
            .filter(item -> item.getDescripcion().contains("❌") || item.getDescripcion().contains("💥"))
            .count();

        long paquetesAfectados = items.stream()
            .map(PlanItem::getPackageName)
            .distinct()
            .count();

        return String.format("""
            - 🎯 **Refactors planificados**: %d
            - 🚨 **Alta prioridad**: %d
            - 🟡 **Prioridad media**: %d  
            - 🟢 **Prioridad baja**: %d
            - ⚠️ **Con problemas críticos**: %d
            - 📦 **Paquetes afectados**: %d
            - ⏱️ **Tiempo estimado**: %d minutos aprox.
            """,
            items.size(),
            porPrioridad.getOrDefault(PrioridadRefactor.ALTA, 0L),
            porPrioridad.getOrDefault(PrioridadRefactor.MEDIA, 0L),
            porPrioridad.getOrDefault(PrioridadRefactor.BAJA, 0L),
            conProblemas,
            paquetesAfectados,
            items.size() * 5
        );
    }

    private static String generarRefactorsPorPaquete(List<PlanItem> items) {
        Map<String, List<PlanItem>> porPaquete = items.stream()
            .collect(Collectors.groupingBy(
                PlanItem::getPackageName,
                TreeMap::new,
                Collectors.toList()
            ));

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<PlanItem>> entry : porPaquete.entrySet()) {
            sb.append("### 📦 ").append(entry.getKey()).append("\n\n");
            for (PlanItem item : entry.getValue()) {
                sb.append(item.toMarkdown());
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private static String generarMetricasAvanzadas(List<PlanItem> items) {
        Map<String, Long> porPaquete = items.stream()
            .collect(Collectors.groupingBy(PlanItem::getPackageName, Collectors.counting()));

        String paqueteMasAfectado = porPaquete.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(entry -> "`" + entry.getKey() + "` (" + entry.getValue() + " refactors)")
            .orElse("Ninguno");

        long refactorsManana = items.stream()
            .filter(item -> item.getTimestamp().getHour() < 12)
            .count();

        String listaPaquetes = porPaquete.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .map(entry -> "  - `" + entry.getKey() + "`: " + entry.getValue() + " refactors")
            .collect(Collectors.joining("\n"));

        return String.format("""
            - 📊 **Refactors por paquete**: 
            %s
            - 🎪 **Paquete más afectado**: %s
            - 🌅 **Refactors en horario matutino**: %d
            - 📝 **Registros totales**: %d
            """,
            listaPaquetes,
            paqueteMasAfectado,
            refactorsManana,
            items.size()
        );
    }
}