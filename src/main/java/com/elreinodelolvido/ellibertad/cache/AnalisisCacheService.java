package com.elreinodelolvido.ellibertad.cache;

import com.elreinodelolvido.ellibertad.scanner.ClassMetadata;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;
import javax.inject.Inject;
import java.util.Map;

/**
 * Servicio encargado de refrescar la cache llamando al ProjectScanner.
 * Permite centralizar el refresh y exponerlo como endpoint si es necesario.
 */
public class AnalisisCacheService {

    @Inject
    private AnalisisCacheManager cacheManager;

    @Inject
    private ProjectScanner projectScanner;

    public AnalisisCacheService() {}

    /**
     * Fuerza un refresh sincrónico de la cache usando el scanner.
     * @param proyectoPath ruta del proyecto a escanear
     * @return true si se obtuvieron resultados y la cache fue actualizada
     */
    public boolean refreshCache(String proyectoPath) {
        try {
            Map<String, ClassMetadata> scanResult = projectScanner.scanProjectTurbo(proyectoPath);
            if (scanResult != null && !scanResult.isEmpty()) {
                cacheManager.putAll(scanResult);
                return true;
            }
        } catch (Exception e) {
            System.err.println("AnalisisCacheService.refreshCache failed: " + e.getMessage());
        }
        return false;
    }
}