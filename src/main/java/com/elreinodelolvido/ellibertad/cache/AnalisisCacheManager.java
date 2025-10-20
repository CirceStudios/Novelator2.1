package com.elreinodelolvido.ellibertad.cache;

import com.elreinodelolvido.ellibertad.scanner.ClassMetadata;
import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.nio.file.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AnalisisCacheManager: cache central simple para compartir resultados de escaneo/analisis.
 * - Thread-safe
 * - Persistencia opcional a disco (autogen-output/cache/analisis-cache.json)
 */
public class AnalisisCacheManager {
    private final ConcurrentMap<String, ClassMetadata> classMap = new ConcurrentHashMap<>();
    private final AtomicLong lastUpdated = new AtomicLong(0);
    private final ObjectMapper mapper = new ObjectMapper();
    private final Path persistence = Paths.get("autogen-output/cache/analisis-cache.json");

    public boolean isEmpty() {
        return classMap.isEmpty();
    }

    public void putAll(Map<String, ClassMetadata> entries) {
        if (entries == null || entries.isEmpty()) return;
        classMap.putAll(entries);
        lastUpdated.set(System.currentTimeMillis());
    }

    public Map<String, ClassMetadata> getAll() {
        return Collections.unmodifiableMap(new HashMap<>(classMap));
    }

    public Optional<ClassMetadata> get(String key) {
        return Optional.ofNullable(classMap.get(key));
    }

    public long getLastUpdated() {
        return lastUpdated.get();
    }

    public void invalidate() {
        classMap.clear();
        lastUpdated.set(0);
    }

    // Simple persistence (best-effort). Call from shutdown hook or after major updates.
    public void persistToDisk() {
        try {
            Files.createDirectories(persistence.getParent());
            mapper.writeValue(persistence.toFile(), classMap);
        } catch (IOException e) {
            // Prefer logging via existing logging infra
            System.err.println("AnalisisCacheManager.persistToDisk failed: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromDisk() {
        try {
            if (!Files.exists(persistence)) return;
            Map<String, ClassMetadata> loaded = mapper.readValue(persistence.toFile(), Map.class);
            if (loaded != null) {
                classMap.putAll(loaded);
                lastUpdated.set(System.currentTimeMillis());
            }
        } catch (Exception e) {
            System.err.println("AnalisisCacheManager.loadFromDisk failed: " + e.getMessage());
        }
    }
}