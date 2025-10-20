```java
package com.novelator.autogen.plan;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Clase base generada automáticamente desde el plan para la gestión de tareas.
 * Implementa un sistema robusto y thread-safe para la administración de tareas
 * con capacidades de seguimiento y estado.
 */
public class TaskManager {
    
    // Campos/atributos
    private final ConcurrentMap<String, Task> tasks;
    private final Object lock;
    
    /**
     * Representa una tarea individual con estado y metadatos.
     */
    public static class Task {
        private final String id;
        private final String description;
        private TaskStatus status;
        private final long createdAt;
        private long updatedAt;
        
        public Task(String description) {
            this.id = UUID.randomUUID().toString();
            this.description = description;
            this.status = TaskStatus.PENDING;
            this.createdAt = System.currentTimeMillis();
            this.updatedAt = this.createdAt;
        }
        
        // Getters
        public String getId() { return id; }
        public String getDescription() { return description; }
        public TaskStatus getStatus() { return status; }
        public long getCreatedAt() { return createdAt; }
        public long getUpdatedAt() { return updatedAt; }
        
        // Setters con actualización de timestamp
        public void setStatus(TaskStatus status) {
            this.status = status;
            this.updatedAt = System.currentTimeMillis();
        }
        
        @Override
        public String toString() {
            return String.format("Task{id='%s', description='%s', status=%s, created=%d, updated=%d}", 
                               id, description, status, createdAt, updatedAt);
        }
    }
    
    /**
     * Enumeración de estados posibles para una tarea.
     */
    public enum TaskStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        FAILED
    }
    
    // Constructores
    
    /**
     * Constructor por defecto que inicializa el gestor de tareas.
     */
    public TaskManager() {
        this.tasks = new ConcurrentHashMap<>();
        this.lock = new Object();
    }
    
    // Métodos implementados
    
    /**
     * Crea una nueva tarea con la descripción proporcionada.
     * 
     * @param description Descripción de la tarea
     * @return La tarea creada
     * @throws IllegalArgumentException si la descripción es nula o vacía
     */
    public Task createTask(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la tarea no puede ser nula o vacía");
        }
        
        Task task = new Task(description.trim());
        tasks.put(task.getId(), task);
        return task;
    }
    
    /**
     * Obtiene una tarea por su ID.
     * 
     * @param taskId ID de la tarea
     * @return La tarea encontrada o null si no existe
     */
    public Task getTask(String taskId) {
        return tasks.get(taskId);
    }
    
    /**
     * Actualiza el estado de una tarea específica.
     * 
     * @param taskId ID de la tarea
     * @param status Nuevo estado de la tarea
     * @return true si la actualización fue exitosa, false si la tarea no existe
     */
    public boolean updateTaskStatus(String taskId, TaskStatus status) {
        Task task = tasks.get(taskId);
        if (task != null) {
            task.setStatus(status);
            return true;
        }
        return false;
    }
    
    /**
     * Elimina una tarea del gestor.
     * 
     * @param taskId ID de la tarea a eliminar
     * @return true si la eliminación fue exitosa, false si la tarea no existe
     */
    public boolean deleteTask(String taskId) {
        return tasks.remove(taskId) != null;
    }
    
    /**
     * Obtiene todas las tareas existentes.
     * 
     * @return Lista de todas las tareas
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }
    
    /**
     * Obtiene las tareas filtradas por estado.
     * 
     * @param status Estado por el cual filtrar
     * @return Lista de tareas con el estado especificado
     */
    public List<Task> getTasksByStatus(TaskStatus status) {
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.getStatus() == status) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
    
    /**
     * Obtiene el número total de tareas gestionadas.
     * 
     * @return Cantidad de tareas
     */
    public int getTaskCount() {
        return tasks.size();
    }
    
    /**
     * Limpia todas las tareas completadas del gestor.
     * 
     * @return Número de tareas eliminadas
     */
    public int clearCompletedTasks() {
        int initialSize = tasks.size();
        tasks.entrySet().removeIf(entry -> entry.getValue().getStatus() == TaskStatus.COMPLETED);
        return initialSize - tasks.size();
    }
    
    /**
     * Verifica si existe una tarea con el ID especificado.
     * 
     * @param taskId ID de la tarea a verificar
     * @return true si la tarea existe, false en caso contrario
     */
    public boolean containsTask(String taskId) {
        return tasks.containsKey(taskId);
    }
}
```