package com.elreinodelolvido.ellibertad.cache;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Lightweight lifecycle registrar that configures and starts the ScheduledCacheRefresher
 * based on environment variables. Acts as a bridge to ensure the refresher is started
 * in environments where the DI container does not automatically call @PostConstruct on it.
 */
public class AppLifecycleRegistrar {

    @Inject
    private ScheduledCacheRefresher refresher;

    public AppLifecycleRegistrar() {}

    @PostConstruct
    public void init() {
        if (refresher == null) return;

        String enabled = System.getenv("CACHE_REFRESH_ENABLED");
        if (enabled != null) {
            refresher.setEnabled(Boolean.parseBoolean(enabled));
        }

        String proyectoPath = System.getenv("CACHE_REFRESH_PROJECT_PATH");
        if (proyectoPath != null && !proyectoPath.trim().isEmpty()) {
            refresher.setProyectoPath(proyectoPath);
        }

        String interval = System.getenv("CACHE_REFRESH_INTERVAL_MS");
        if (interval != null) {
            try {
                refresher.setIntervalMillis(Long.parseLong(interval));
            } catch (NumberFormatException ignored) {}
        }

        // Ensure refresher is started
        try {
            refresher.start();
        } catch (Exception e) {
            System.err.println("AppLifecycleRegistrar: failed to start refresher: " + e.getMessage());
        }
    }

    @PreDestroy
    public void shutdown() {
        if (refresher != null) {
            try {
                refresher.stop();
            } catch (Exception e) {
                System.err.println("AppLifecycleRegistrar: failed to stop refresher: " + e.getMessage());
            }
        }
    }
}