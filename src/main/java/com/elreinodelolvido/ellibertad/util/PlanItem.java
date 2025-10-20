package com.elreinodelolvido.ellibertad.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 🎯 Item del plan de refactor con superpoderes TURBO
 */
public class PlanItem {
    final String className;
    final String packageName;
    final String descripcion;
    final LocalDateTime timestamp;

    public PlanItem(String className, String packageName, String descripcion, LocalDateTime timestamp) {
        this.className = Objects.requireNonNull(className).trim();
        this.packageName = Objects.requireNonNull(packageName).trim();
        this.descripcion = Objects.requireNonNull(descripcion).trim();
        this.timestamp = Objects.requireNonNull(timestamp);
    }

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * 🚨 Prioridad automática basada en descripción
     */
    public PrioridadRefactor getPrioridad() {
        return PrioridadRefactor.detectar(this.descripcion);
    }

    public String toMarkdown() {
        PrioridadRefactor prioridad = getPrioridad();
        return String.format("""
            #### %s %s
            - **📅 Registrado**: %s
            - **🎯 Mejora**: %s
            - **🚨 Prioridad**: %s
            - **📦 Paquete**: `%s`

            """,
            prioridad.getEmoji(),
            this.className,
            this.timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            this.descripcion,
            prioridad.getEmojiLabel(),
            this.packageName
        );
    }

    @Override
    public String toString() {
        return String.format("PlanItem[%s.%s - %s]", 
            packageName, className, 
            timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}