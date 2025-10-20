package com.elreinodelolvido.ellibertad.cache;

import com.elreinodelolvido.ellibertad.scanner.ClassMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AnalisisCacheManagerTest {

    private AnalisisCacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheManager = new AnalisisCacheManager();
        cacheManager.invalidate();
    }

    @Test
    void testPutAllAndGetAll_andGet() {
        assertTrue(cacheManager.isEmpty(), "Cache debe empezar vacía");

        Map<String, ClassMetadata> entries = new HashMap<>();
        ClassMetadata cm = mock(ClassMetadata.class);
        entries.put("com.example.Foo", cm);

        cacheManager.putAll(entries);

        assertFalse(cacheManager.isEmpty(), "Cache no debe estar vacía tras putAll");
        assertEquals(1, cacheManager.getAll().size(), "Tamaño del cache debe ser 1");
        Optional<ClassMetadata> retrieved = cacheManager.get("com.example.Foo");
        assertTrue(retrieved.isPresent(), "Debe poder obtenerse el ClassMetadata insertado");
        assertSame(cm, retrieved.get(), "El objeto recuperado debe ser el mismo insertado");
    }

    @Test
    void testInvalidateResetsState() {
        Map<String, ClassMetadata> entries = new HashMap<>();
        entries.put("X", mock(ClassMetadata.class));
        cacheManager.putAll(entries);

        assertFalse(cacheManager.isEmpty());
        cacheManager.invalidate();
        assertTrue(cacheManager.isEmpty(), "invalidate debe vaciar el cache");
        assertEquals(0, cacheManager.getAll().size());
    }
}