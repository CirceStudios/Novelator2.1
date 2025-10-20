```java
package com.novelator.autogen.plan;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Clase base generada automáticamente desde el plan.
 * Ejecuta planes de texto autónomos de manera asíncrona y gestiona
 * el ciclo de vida de las tareas planificadas.
 */
public class PlanExecutor {
    
    private final ExecutorService executorService;
    private volatile boolean isShutdown;
    
    /**
     * Constructor por defecto que inicializa el executor con un pool de hilos fijo.
     */
    public PlanExecutor() {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.isShutdown = false;
    }
    
    /**
     * Constructor que permite especificar el número de hilos del pool.
     * 
     * @param threadPoolSize número de hilos en el pool de ejecución
     * @throws IllegalArgumentException si threadPoolSize es menor o igual a 0
     */
    public PlanExecutor(int threadPoolSize) {
        if (threadPoolSize <= 0) {
            throw new IllegalArgumentException("El tamaño del pool de hilos debe ser mayor a 0");
        }
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.isShutdown = false;
    }
    
    /**
     * Ejecuta una tarea planificada de manera asíncrona.
     * 
     * @param taskSupplier proveedor de la tarea a ejecutar
     * @param <T> tipo del resultado de la tarea
     * @return CompletableFuture que representa la ejecución asíncrona
     * @throws IllegalStateException si el executor está apagado
     */
    public <T> CompletableFuture<T> executePlan(Supplier<T> taskSupplier) {
        if (isShutdown) {
            throw new IllegalStateException("PlanExecutor está apagado, no se pueden ejecutar nuevas tareas");
        }
        
        return CompletableFuture.supplyAsync(taskSupplier, executorService);
    }
    
    /**
     * Ejecuta una tarea Runnable de manera asíncrona.
     * 
     * @param task tarea a ejecutar
     * @return CompletableFuture que representa la ejecución asíncrona
     * @throws IllegalStateException si el executor está apagado
     */
    public CompletableFuture<Void> executeRunnablePlan(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("PlanExecutor está apagado, no se pueden ejecutar nuevas tareas");
        }
        
        return CompletableFuture.runAsync(task, executorService);
    }
    
    /**
     * Verifica si el executor está apagado.
     * 
     * @return true si el executor está apagado, false en caso contrario
     */
    public boolean isShutdown() {
        return isShutdown;
    }
    
    /**
     * Inicia el proceso de apagado del executor.
     * Las tareas en ejecución continuarán, pero no se aceptarán nuevas tareas.
     */
    public void shutdown() {
        if (!isShutdown) {
            executorService.shutdown();
            isShutdown = true;
        }
    }
    
    /**
     * Intenta detener todas las tareas en ejecución inmediatamente.
     * 
     * @return lista de tareas que estaban pendientes de ejecución
     */
    public void shutdownNow() {
        if (!isShutdown) {
            executorService.shutdownNow();
            isShutdown = true;
        }
    }
    
    /**
     * Verifica si todas las tareas han terminado después del apagado.
     * 
     * @return true si todas las tareas han terminado, false en caso contrario
     */
    public boolean isTerminated() {
        return executorService.isTerminated();
    }
}
```