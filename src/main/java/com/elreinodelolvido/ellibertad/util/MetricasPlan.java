package com.elreinodelolvido.ellibertad.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 📊 Contenedor de métricas avanzadas del plan
 */
public class MetricasPlan {
    private final int totalRefactors;
    private final int paquetesAfectados;
    private final Map<Object, Long> distribucionPrioridades;
    private final String paqueteMasAfectado;
    private final long refactorsConProblemas;

    public MetricasPlan(List<PlanItem> items) {
        this.totalRefactors = items.size();
        this.paquetesAfectados = (int) items.stream()
            .map(PlanItem::getPackageName)
            .distinct()
            .count();
        
        this.distribucionPrioridades = items.stream()
            .collect(Collectors.groupingBy(PlanItem::getPrioridad, Collectors.counting()));
        
        this.paqueteMasAfectado = items.stream()
            .collect(Collectors.groupingBy(PlanItem::getPackageName, Collectors.counting()))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(entry -> entry.getKey() + " (" + entry.getValue() + ")")
            .orElse("Ninguno");

        this.refactorsConProblemas = items.stream()
            .filter(item -> item.getDescripcion().contains("❌") || item.getDescripcion().contains("💥"))
            .count();
    }

    // Getters turbo...
    public int getTotalRefactors() { return totalRefactors; }
    public int getPaquetesAfectados() { return paquetesAfectados; }
    public Map<Object, Long> getDistribucionPrioridades() { return distribucionPrioridades; }
    public String getPaqueteMasAfectado() { return paqueteMasAfectado; }
    public long getRefactorsConProblemas() { return refactorsConProblemas; }

    @Override
    public String toString() {
        return String.format("MetricasTurbo[refactors=%d, paquetes=%d, problemas=%d]", 
            totalRefactors, paquetesAfectados, refactorsConProblemas);
    }
}
