package com.elreinodelolvido.ellibertad.cache;

import com.elreinodelolvido.ellibertad.scanner.ClassMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AnalisisCacheManagerTTLTest {

    private AnalisisCacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheManager = new AnalisisCacheManager();
        cacheManager.invalidate();
    }

    @Test
    void testTTLDeterministicUsingSetLastUpdated() {
        // TTL 100 ms
        cacheManager.setTtlMillis(100);
        Map<String, ClassMetadata> entries = new HashMap<>();
        entries.put("X", mock(ClassMetadata.class));
        cacheManager.putAll(entries);

        // Fresh immediately after putAll
        cacheManager.setLastUpdatedMillis(System.currentTimeMillis());
        assertFalse(cacheManager.isStale(), "Cache no debe estar stale inmediatamente después de putAll");

        // Make the lastUpdated appear in the past beyond the TTL
        long past = System.currentTimeMillis() - 200L;
        cacheManager.setLastUpdatedMillis(past);
        assertTrue(cacheManager.isStale(), "Cache debe considerarse stale tras simular tiempo pasado mayor al TTL");
    }
}