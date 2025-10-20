package com.elreinodelolvido.ellibertad.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.ws.rs.core.Response;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalisisCacheControllerTest {

    private AnalisisCacheController controller;
    private AnalisisCacheService cacheService;

    @BeforeEach
    void setUp() {
        controller = new AnalisisCacheController();
        cacheService = mock(AnalisisCacheService.class);
        // inyectar campo por reflection (simula @Inject)
        try {
            java.lang.reflect.Field f = controller.getClass().getDeclaredField("cacheService");
            f.setAccessible(true);
            f.set(controller, cacheService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void returnsBadRequestWhenNoProjectPath() {
        Response r = controller.refreshCache(null);
        assertEquals(400, r.getStatus());
    }

    @Test
    void returnsOkWhenServiceRefreshed() {
        when(cacheService.refreshCache("p")).thenReturn(true);
        Response r = controller.refreshCache("p");
        assertEquals(200, r.getStatus());
    }

    @Test
    void returnsAcceptedWhenNoData() {
        when(cacheService.refreshCache("p")).thenReturn(false);
        Response r = controller.refreshCache("p");
        assertEquals(202, r.getStatus());
    }
}