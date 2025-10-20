package com.elreinodelolvido.ellibertad.api;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.elreinodelolvido.ellibertad.util.FileUtils;

/**
 * PromptCache — La Bruma de la Cache 🌫️
 * Guarda en memoria los últimos prompts y sus respuestas para evitar redundancias.
 * Actúa como un velo que recuerda lo dicho en la niebla del pasado.
 */
public class PromptCache {

    private final int capacidad;
    private final Map<String, CacheEntry> cache;
    private int hits = 0;
    private int misses = 0;

    // Entrada de cache con timestamp y metadata
    private static class CacheEntry {
        final String respuesta;
        final long timestamp;
        final String tipo; // "tecnico", "narrativo", etc.
        
        CacheEntry(String respuesta, String tipo) {
            this.respuesta = respuesta;
            this.tipo = tipo;
            this.timestamp = System.currentTimeMillis();
        }
        
        boolean esExpirado(long tiempoExpiracionMs) {
            return (System.currentTimeMillis() - timestamp) > tiempoExpiracionMs;
        }
    }

    public PromptCache(int capacidad) {
        if (capacidad <= 0) throw new IllegalArgumentException("Capacidad debe ser > 0");
        this.capacidad = capacidad;
        this.cache = new LinkedHashMap<>(capacidad, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, CacheEntry> eldest) {
                return size() > PromptCache.this.capacidad;
            }
        };
    }

    public void guardar(String prompt, String respuesta) {
        guardar(prompt, respuesta, "general");
    }

    public void guardar(String prompt, String respuesta, String tipo) {
        Objects.requireNonNull(prompt, "prompt no puede ser null");
        Objects.requireNonNull(respuesta, "respuesta no puede ser null");
        
        String clave = normalizarClave(prompt);
        cache.put(clave, new CacheEntry(respuesta, tipo != null ? tipo : "general"));
        
        System.out.println("💾 Guardado en cache: '" + truncarTexto(clave, 50) + "'");
    }

    public String obtener(String prompt) {
        String clave = normalizarClave(prompt);
        CacheEntry entrada = cache.get(clave);
        
        if (entrada != null) {
            hits++;
            System.out.println("🎯 HIT en cache: '" + truncarTexto(clave, 50) + "'");
            return entrada.respuesta;
        } else {
            misses++;
            System.out.println("❌ MISS en cache: '" + truncarTexto(clave, 50) + "'");
            return null;
        }
    }

    public boolean contiene(String prompt) {
        return cache.containsKey(normalizarClave(prompt));
    }

    /**
     * Obtiene respuesta si existe, sino retorna null
     */
    public String obtenerSiExiste(String prompt) {
        return obtener(prompt);
    }

    /**
     * Limpia entradas expiradas (mayores a X milisegundos)
     */
    public int limpiarExpirados(long tiempoExpiracionMs) {
        int eliminados = 0;
        var iterator = cache.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (entry.getValue().esExpirado(tiempoExpiracionMs)) {
                iterator.remove();
                eliminados++;
            }
        }
        if (eliminados > 0) {
            System.out.println("🧹 Eliminados " + eliminados + " entradas expiradas del cache");
        }
        return eliminados;
    }

    public void limpiar() {
        int tamañoAnterior = cache.size();
        cache.clear();
        System.out.println("🗑️ Cache limpiado (" + tamañoAnterior + " entradas eliminadas)");
    }

    public int tamano() {
        return cache.size();
    }

    public int getHits() {
        return hits;
    }

    public int getMisses() {
        return misses;
    }

    public double getHitRate() {
        int total = hits + misses;
        return total > 0 ? (double) hits / total : 0.0;
    }

    public Map<String, String> obtenerTodo() {
        Map<String, String> resultado = new LinkedHashMap<>();
        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            resultado.put(entry.getKey(), entry.getValue().respuesta);
        }
        return resultado;
    }

    /**
     * Obtiene estadísticas detalladas del cache
     */
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("capacidad", capacidad);
        stats.put("entradas_actuales", tamano());
        stats.put("hits", hits);
        stats.put("misses", misses);
        stats.put("hit_rate", String.format("%.2f%%", getHitRate() * 100));
        stats.put("uso", String.format("%.1f%%", (double) tamano() / capacidad * 100));
        
        // Contar por tipos
        Map<String, Integer> porTipo = new LinkedHashMap<>();
        for (CacheEntry entry : cache.values()) {
            porTipo.merge(entry.tipo, 1, Integer::sum);
        }
        stats.put("entradas_por_tipo", porTipo);
        
        return stats;
    }

    /**
     * Guarda el estado del cache a disco (útil para debugging)
     */
    public void guardarEstado() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("# 🌫️ Estado del PromptCache\n\n");
            sb.append("## Estadísticas\n");
            Map<String, Object> stats = obtenerEstadisticas();
            for (Map.Entry<String, Object> entry : stats.entrySet()) {
                sb.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            
            sb.append("\n## Entradas en Cache\n");
            for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
                sb.append("### '").append(truncarTexto(entry.getKey(), 100)).append("'\n");
                sb.append("- Tipo: ").append(entry.getValue().tipo).append("\n");
                sb.append("- Tamaño respuesta: ").append(entry.getValue().respuesta.length()).append(" chars\n");
                sb.append("- Timestamp: ").append(entry.getValue().timestamp).append("\n\n");
            }
            
            FileUtils.writeToFile("autogen-output/cache/estado-cache.md", sb.toString());
            System.out.println("💾 Estado del cache guardado en autogen-output/cache/estado-cache.md");
            
        } catch (Exception e) {
            System.err.println("⚠️ Error guardando estado del cache: " + e.getMessage());
        }
    }

    private String normalizarClave(String prompt) {
        return prompt.trim().replaceAll("\\s+", " ");
    }

    private String truncarTexto(String texto, int maxLength) {
        if (texto == null || texto.length() <= maxLength) return texto;
        return texto.substring(0, maxLength) + "...";
    }

    @Override
    public String toString() {
        return String.format("🌫️ PromptCache[%d/%d, hitRate=%.1f%%, hits=%d, misses=%d]", 
            tamano(), capacidad, getHitRate() * 100, hits, misses);
    }
}