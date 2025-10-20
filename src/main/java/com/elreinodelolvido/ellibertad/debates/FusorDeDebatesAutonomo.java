package com.elreinodelolvido.ellibertad.debates;

import com.elreinodelolvido.ellibertad.cache.AnalisisCacheManager;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;
import com.elreinodelolvido.ellibertad.scanner.ClassMetadata;
import com.elreinodelolvido.ellibertad.manager.AnalisisManager;
import java.util.*;
import javax.inject.Inject;

/**
 * Cambios mínimos para asegurar que cada debate arranque con datos frescos.
 * - Si cache vacía o stale, ejecuta scanProjectTurbo() y pone resultados en cache.
 * - Llama a analisisRapidoConMetricas() para enriquecer contexto.
 */
public class FusorDeDebatesAutonomo {

    @Inject
    private AnalisisCacheManager cacheManager;

    @Inject
    private ProjectScanner projectScanner;

    @Inject
    private AnalisisManager analisisManager;

    // Punto de entrada del debate (simplificado)
    public void iniciarDebateAutonomo(String proyectoPath) {
        // 1) Si no hay datos en cache o la cache está stale, hacemos un escaneo previo (bloqueante, breve)
        if (cacheManager.isEmpty() || cacheManager.isStale()) {
            Map<String, ClassMetadata> scanResult = projectScanner.scanProjectTurbo(proyectoPath);
            if (scanResult != null && !scanResult.isEmpty()) {
                cacheManager.putAll(scanResult);
            }
        }

        // 2) Obtener contexto del cache
        List<ClassMetadata> contexto = new ArrayList<>(cacheManager.getAll().values());

        // 3) Ejecutar análisis rápido (métricas) para enriquecer debate
        Map<String, Object> metricas = analisisManager.analisisRapidoConMetricas(contexto);

        // 4) Iniciar debate pasando datos frescos y métricas
        iniciarDebateAutonomoConDatos(contexto, metricas);
    }

    // Método ya existente que hace el trabajo del debate; aquí lo llamamos con contexto
    private void iniciarDebateAutonomoConDatos(List<ClassMetadata> clases, Map<String, Object> metricas) {
        // --- implementación actual del debate ---
        // usar 'clases' y 'metricas' para seleccionar piratas, preparar prompts y ejecutar rondas.
    }
}