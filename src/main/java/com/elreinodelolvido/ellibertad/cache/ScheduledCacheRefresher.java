package com.elreinodelolvido.ellibertad.cache;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;

/**
 * Scheduled refresher that periodically calls AnalisisCacheService.refreshCache.
 * Configure proyectoPath, intervalMillis and enabled via setters or DI config.
 */
public class ScheduledCacheRefresher {

    @Inject
    private AnalisisCacheService cacheService;

    // configurable via setters or by DI config
    private String proyectoPath = "."; // default: repo root, override in config
    private long intervalMillis = 5 * 60 * 1000L; // default 5 minutes
    private boolean enabled = true;

    private ScheduledExecutorService scheduler;

    public ScheduledCacheRefresher() {}

    public void setProyectoPath(String proyectoPath) {
        this.proyectoPath = proyectoPath;
    }

    public void setIntervalMillis(long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @PostConstruct
    public void start() {
        if (!enabled) return;
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "cache-refresher");
            t.setDaemon(true);
            return t;
        });
        // initial delay 0 to try refresh at startup
        scheduler.scheduleAtFixedRate(this::refreshSafely, 0, intervalMillis, TimeUnit.MILLISECONDS);
    }

    private void refreshSafely() {
        try {
            cacheService.refreshCache(proyectoPath);
        } catch (Exception e) {
            // Best-effort logging for now
            System.err.println("ScheduledCacheRefresher failed: " + e.getMessage());
        }
    }

    @PreDestroy
    public void stop() {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}