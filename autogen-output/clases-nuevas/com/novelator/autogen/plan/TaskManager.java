```java
package com.novelator.autogen.plan;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Clase base generada automáticamente desde el plan para la gestión de tareas.
 * Proporciona funcionalidades completas para crear, gestionar y rastrear tareas
 * de manera concurrente y segura.
 */
public class TaskManager {
    
    /**
     * Representa una tarea individual con identificador único y estado.
     */
    public static class Task {
        private final String id;
        private String description;
        private TaskStatus status;
        private long createdAt;
        private long updatedAt;
        
        /**
         * Constructor para crear una nueva tarea.
         * 
         * @param description Descripción de la tarea
         */
        public Task(String description) {
            this.id = UUID.randomUUID().toString();
            this.description = description;
            this.status = TaskStatus.PENDING;
            this.createdAt = System.currentTimeMillis();
            this.updatedAt = this.createdAt;
        }
        
        // Getters y setters
        public String getId() { return id; }
        public String getDescription() { return description; }
        public void setDescription(String description) { 
            this.description = description; 
            this.updatedAt = System.currentTimeMillis();
        }
        public TaskStatus getStatus() { return status; }
        public void setStatus(TaskStatus status) { 
            this.status = status; 
            this.updatedAt = System.currentTimeMillis();
        }
        public long getCreatedAt() { return createdAt; }
        public long getUpdatedAt() { return updatedAt; }
        
        @Override
        public String toString() {
            return String.format("Task{id='%s', description='%s', status=%s}", 
                               id, description, status);
        }
    }
    
    /**
     * Enumeración que representa los posibles estados de una tarea.
     */
    public enum TaskStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
    
    private final ConcurrentMap<String, Task> tasks;
    
    /**
     * Constructor por defecto que inicializa el gestor de tareas.
     */
    public TaskManager() {
        this.tasks = new ConcurrentHashMap<>();
    }
    
    /**
     * Crea una nueva tarea y la añade al gestor.
     * 
     * @param description Descripción de la tarea a crear
     * @return La tarea creada
     */
    public Task createTask(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la tarea no puede estar vacía");
        }
        
        Task task = new Task(description.trim());
        tasks.put(task.getId(), task);
        return task;
    }
    
    /**
     * Obtiene una tarea por su identificador único.
     * 
     * @param taskId Identificador de la tarea
     * @return La tarea encontrada o null si no existe
     */
    public Task getTask(String taskId) {
        return tasks.get(taskId);
    }
    
    /**
     * Actualiza el estado de una tarea específica.
     * 
     * @param taskId Identificador de la tarea
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
     * Actualiza la descripción de una tarea específica.
     * 
     * @param taskId Identificador de la tarea
     * @param description Nueva descripción de la tarea
     * @return true si la actualización fue exitosa, false si la tarea no existe
     */
    public boolean updateTaskDescription(String taskId, String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la tarea no puede estar vacía");
        }
        
        Task task = tasks.get(taskId);
        if (task != null) {
            task.setDescription(description.trim());
            return true;
        }
        return false;
    }
    
    /**
     * Elimina una tarea del gestor.
     * 
     * @param taskId Identificador de la tarea a eliminar
     * @return true si la eliminación fue exitosa, false si la tarea no existe
     */
    public boolean deleteTask(String taskId) {
        return tasks.remove(taskId) != null;
    }
    
    /**
     * Obtiene todas las tareas existentes en el gestor.
     * 
     * @return Lista de todas las tareas
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }
    
    /**
     * Obtiene las tareas filtradas por estado.
     * 
     * @param status Estado por el que filtrar
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
     * Obtiene el número total de tareas en el gestor.
     * 
     * @return Cantidad total de tareas
     */
    public int getTaskCount() {
        return tasks.size();
    }
    
    /**
     * Obtiene el número de tareas filtradas por estado.
     * 
     * @param status Estado por el que filtrar
     * @return Cantidad de tareas con el estado especificado
     */
    public int getTaskCountByStatus(TaskStatus status) {
        return getTasksByStatus(status).size();
    }
    
    /**
     * Limpia todas las tareas del gestor.
     */
    public void clearAllTasks() {
        tasks.clear();
    }
    
    /**
     * Verifica si existe una tarea con el identificador especificado.
     * 
     * @param taskId Identificador de la tarea
     * @return true si la tarea existe, false en caso contrario
     */
    public boolean containsTask(String taskId) {
        return tasks.containsKey(taskId);
    }
}
```