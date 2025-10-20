package com.elreinodelolvido.ellibertad.util;

public enum PrioridadRefactor {
    ALTA("🚨", "🚨 ALTA - Crítico", 3),
    MEDIA("🟡", "🟡 MEDIA - Mejora", 2), 
    BAJA("🟢", "🟢 BAJA - Mantenimiento", 1);

    private final String emoji;
    private final String emojiLabel;
    private final int valorNumerico;

    PrioridadRefactor(String emoji, String emojiLabel, int valorNumerico) {
        this.emoji = emoji;
        this.emojiLabel = emojiLabel;
        this.valorNumerico = valorNumerico;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getEmojiLabel() {
        return emojiLabel;
    }

    public int getValorNumerico() {
        return valorNumerico;
    }

    /**
     * 🧠 Detección turbo-inteligente de prioridad
     */
    public static PrioridadRefactor detectar(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return BAJA;
        }
        
        String lower = descripcion.toLowerCase().trim();

        if (lower.contains("error") || lower.contains("❌") || lower.contains("💥") || 
            lower.contains("exception") || lower.contains("fail") || lower.contains("broken") ||
            lower.contains("crash") || lower.contains("no funciona") || lower.contains("bug") ||
            lower.contains("fix") || lower.contains("corregir") || lower.contains("urgent")) {
            return ALTA;
        } else if (lower.contains("mejorar") || lower.contains("optimizar") || 
                   lower.contains("performance") || lower.contains("lento") ||
                   lower.contains("refactor") || lower.contains("clean") ||
                   lower.contains("code smell") || lower.contains("technical debt") ||
                   lower.contains("mejor práctica") || lower.contains("patrón")) {
            return MEDIA;
        } else {
            return BAJA;
        }
    }
}