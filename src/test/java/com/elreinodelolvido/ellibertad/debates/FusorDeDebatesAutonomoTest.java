package com.elreinodelolvido.ellibertad.debates;

import com.elreinodelolvido.ellibertad.cache.AnalisisCacheManager;
import com.elreinodelolvido.ellibertad.manager.AnalisisManager;
import com.elreinodelolvido.ellibertad.scanner.ClassMetadata;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FusorDeDebatesAutonomoTest {

    private AnalisisCacheManager cacheManager;
    private ProjectScanner projectScanner;
    private AnalisisManager analisisManager;
    private FusorDeDebatesAutonomo fusor;

    @BeforeEach
    void setUp() throws Exception {
        cacheManager = mock(AnalisisCacheManager.class);
        projectScanner = mock(ProjectScanner.class);
        analisisManager = mock(AnalisisManager.class);

        fusor = new FusorDeDebatesAutonomo();

        // Inyectar mocks en campos privados con reflection (simula @Inject)
        setPrivateField(fusor, "cacheManager", cacheManager);
        setPrivateField(fusor, "projectScanner", projectScanner);
        setPrivateField(fusor, "analisisManager", analisisManager);
    }

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void whenCacheEmpty_scannerRunsAndCachePopulated_andAnalisisInvoked() {
        String proyectoPath = "some/path";

        // cache initially empty
        when(cacheManager.isEmpty()).thenReturn(true);

        // scanner returns one class metadata
        ClassMetadata cm = mock(ClassMetadata.class);
        Map<String, ClassMetadata> scanResult = new HashMap<>();
        scanResult.put("A", cm);

        when(projectScanner.scanProjectTurbo(proyectoPath)).thenReturn(scanResult);

        // after putAll, getAll should return the scanResult (simulate behavior)
        when(cacheManager.getAll()).thenReturn(scanResult);

        // analisisManager returns empty metrics map for simplicity
        when(analisisManager.analisisRapidoConMetricas(anyList())).thenReturn(Collections.emptyMap());

        // Execute
        fusor.iniciarDebateAutonomo(proyectoPath);

        // Verifications
        verify(projectScanner, times(1)).scanProjectTurbo(proyectoPath);
        verify(cacheManager, times(1)).putAll(scanResult);

        // Capture argument passed to analisisRapidoConMetricas
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(analisisManager, times(1)).analisisRapidoConMetricas(captor.capture());
        List capturedContext = captor.getValue();
        assertNotNull(capturedContext);
        assertEquals(1, capturedContext.size(), "El contexto pasado al analisis debe contener 1 clase");
    }

    @Test
    void whenCacheNotEmpty_noScannerCall() {
        String proyectoPath = "ignored";

        // cache already has data
        ClassMetadata cm = mock(ClassMetadata.class);
        Map<String, ClassMetadata> cached = new HashMap<>();
        cached.put("B", cm);

        when(cacheManager.isEmpty()).thenReturn(false);
        when(cacheManager.getAll()).thenReturn(cached);
        when(analisisManager.analisisRapidoConMetricas(anyList())).thenReturn(Collections.emptyMap());

        // Execute
        fusor.iniciarDebateAutonomo(proyectoPath);

        // scanner must not be called
        verify(projectScanner, never()).scanProjectTurbo(anyString());
        // analysis must be called with the cached values
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(analisisManager, times(1)).analisisRapidoConMetricas(captor.capture());
        List captured = captor.getValue();
        assertEquals(1, captured.size(), "Debe usar el contexto de cache sin llamar al scanner");
    }
}