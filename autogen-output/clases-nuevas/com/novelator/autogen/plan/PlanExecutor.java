```java
package com.novelator.autogen.plan;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Clase base generada automáticamente desde el plan.
 * Ejecuta planes de texto autónomos con capacidades de procesamiento asíncrono.
 */
public class PlanExecutor {
    
    private final ExecutorService executorService;
    private volatile boolean isRunning;
    private final Object lock = new Object();
    
    /**
     * Constructor por defecto que inicializa el executor con un pool de hilos fijo.
     */
    public PlanExecutor() {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.isRunning = false;
    }
    
    /**
     * Constructor que permite especificar el número de hilos del pool.
     * 
     * @param threadPoolSize número de hilos en el pool de ejecución
     */
    public PlanExecutor(int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.isRunning = false;
    }
    
    /**
     * Ejecuta una tarea de forma asíncrona y retorna un CompletableFuture con el resultado.
     * 
     * @param <T> tipo del resultado
     * @param taskSupplier proveedor de la tarea a ejecutar
     * @return CompletableFuture que contendrá el resultado de la ejecución
     */
    public <T> CompletableFuture<T> executeAsync(Supplier<T> taskSupplier) {
        synchronized (lock) {
            if (!isRunning) {
                throw new IllegalStateException("PlanExecutor no está en ejecución");
            }
        }
        
        return CompletableFuture.supplyAsync(taskSupplier, executorService);
    }
    
    /**
     * Ejecuta una tarea Runnable de forma asíncrona.
     * 
     * @param task tarea a ejecutar
     * @return CompletableFuture que se completará cuando termine la tarea
     */
    public CompletableFuture<Void> executeAsync(Runnable task) {
        synchronized (lock) {
            if (!isRunning) {
                throw new IllegalStateException("PlanExecutor no está en ejecución");
            }
        }
        
        return CompletableFuture.runAsync(task, executorService);
    }
    
    /**
     * Inicia el PlanExecutor, permitiendo la ejecución de tareas.
     */
    public void start() {
        synchronized (lock) {
            if (isRunning) {
                throw new IllegalStateException("PlanExecutor ya está en ejecución");
            }
            isRunning = true;
        }
    }
    
    /**
     * Detiene el PlanExecutor y libera los recursos del executor service.
     * No acepta nuevas tareas pero permite completar las existentes.
     */
    public void stop() {
        synchronized (lock) {
            if (!isRunning) {
                throw new IllegalStateException("PlanExecutor no está en ejecución");
            }
            isRunning = false;
        }
        executorService.shutdown();
    }
    
    /**
     * Detiene inmediatamente el PlanExecutor, interrumpiendo todas las tareas en ejecución.
     */
    public void stopImmediately() {
        synchronized (lock) {
            if (!isRunning) {
                throw new IllegalStateException("PlanExecutor no está en ejecución");
            }
            isRunning = false;
        }
        executorService.shutdownNow();
    }
    
    /**
     * Verifica si el PlanExecutor está en estado de ejecución.
     * 
     * @return true si está ejecutando, false en caso contrario
     */
    public boolean isRunning() {
        synchronized (lock) {
            return isRunning;
        }
    }
    
    /**
     * Ejecuta una tarea de procesamiento de texto específica del plan.
     * 
     * @param textContent contenido de texto a procesar
     * @return resultado del procesamiento del texto
     */
    public String processTextPlan(String textContent) {
        if (textContent == null || textContent.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido de texto no puede ser nulo o vacío");
        }
        
        // Implementación base para procesamiento de texto
        return "Procesado: " + textContent.trim() + " | Estado: " + 
               (isRunning ? "EJECUTANDO" : "DETENIDO");
    }
    
    /**
     * Ejecuta una tarea de análisis de contenido fallido.
     * 
     * @param analysisData datos de análisis a procesar
     * @return mensaje indicando el estado del análisis fallido
     */
    public String executeFailedAnalysis(String analysisData) {
        return "❌ Análisis fallido procesado: " + 
               (analysisData != null ? analysisData : "Sin datos") + 
               " | Timestamp: " + System.currentTimeMillis();
    }
}
```