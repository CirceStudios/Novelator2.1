package com.elreinodelolvido.ellibertad.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalisisCacheHealthControllerTest {

    private AnalisisCacheHealthController controller;
    private AnalisisCacheManager cacheManager;

    @BeforeEach
    void setUp() throws Exception {
        controller = new AnalisisCacheHealthController();
        cacheManager = mock(AnalisisCacheManager.class);
        // inject mock via reflection
        java.lang.reflect.Field f = controller.getClass().getDeclaredField("cacheManager");
        f.setAccessible(true);
        f.set(controller, cacheManager);
    }

    @Test
    void returnsHealthPayloadWithValues() {
        when(cacheManager.getLastUpdated()).thenReturn(12345L);
        when(cacheManager.isStale()).thenReturn(false);

        Response r = controller.health();
        assertEquals(200, r.getStatus());
        Object entity = r.getEntity();
        assertNotNull(entity);
        assertTrue(entity instanceof Map);
        Map<?,?> m = (Map<?,?>) entity;
        assertEquals(12345L, ((Number) m.get("lastUpdated")).longValue());
        assertEquals(false, m.get("stale"));
    }

    @Test
    void returnsStaleTrueWhenCacheStale() {
        when(cacheManager.getLastUpdated()).thenReturn(0L);
        when(cacheManager.isStale()).thenReturn(true);

        Response r = controller.health();
        assertEquals(200, r.getStatus());
        Map<?,?> m = (Map<?,?>) r.getEntity();
        assertEquals(true, m.get("stale"));
    }
}