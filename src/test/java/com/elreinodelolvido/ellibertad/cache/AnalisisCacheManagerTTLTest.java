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
    void testTTLBecomesStale() throws Exception {
        cacheManager.setTtlMillis(100);
        Map<String, ClassMetadata> entries = new HashMap<>();
        entries.put("X", mock(ClassMetadata.class));
        cacheManager.putAll(entries);

        assertFalse(cacheManager.isStale(), "Cache no debe estar stale inmediatamente después de putAll");

        // wait beyond TTL
        Thread.sleep(150);

        assertTrue(cacheManager.isStale(), "Cache debe considerarse stale tras superar el TTL");
    }
}