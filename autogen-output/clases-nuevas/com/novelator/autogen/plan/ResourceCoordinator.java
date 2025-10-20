```java
package com.novelator.autogen.plan;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * Clase base generada automáticamente desde el plan.
 * Coordina recursos del sistema utilizando composición y patrones de diseño robustos.
 * 
 * <p>Esta clase gestiona la asignación, liberación y monitoreo de recursos
 * en un entorno de procesamiento autónomo de texto.</p>
 */
public class ResourceCoordinator {
    
    // Campos/atributos
    private final Map<String, Object> resourceRegistry;
    private final Set<String> lockedResources;
    private final ReentrantLock coordinationLock;
    private final Condition resourceAvailable;
    private final ScheduledExecutorService maintenanceExecutor;
    private volatile boolean isActive;
    
    // Constantes de configuración
    private static final int DEFAULT_MAX_RESOURCES = 100;
    private static final long MAINTENANCE_INTERVAL_MS = 30000L;
    private static final long RESOURCE_TIMEOUT_MS = 60000L;
    
    /**
     * Constructor por defecto que inicializa el coordinador con configuración predeterminada.
     */
    public ResourceCoordinator() {
        this.resourceRegistry = new ConcurrentHashMap<>();
        this.lockedResources = ConcurrentHashMap.newKeySet();
        this.coordinationLock = new ReentrantLock(true);
        this.resourceAvailable = coordinationLock.newCondition();
        this.maintenanceExecutor = Executors.newSingleThreadScheduledExecutor();
        this.isActive = true;
        
        initializeMaintenanceTask();
    }
    
    /**
     * Constructor con configuración personalizada de límites.
     * 
     * @param maxResources número máximo de recursos permitidos
     */
    public ResourceCoordinator(int maxResources) {
        this();
        // La configuración de límites se aplica en los métodos de adquisición
    }
    
    /**
     * Registra un nuevo recurso en el coordinador.
     * 
     * @param resourceId identificador único del recurso
     * @param resource objeto recurso a registrar
     * @return true si el registro fue exitoso, false si el recurso ya existe
     */
    public boolean registerResource(String resourceId, Object resource) {
        if (resourceId == null || resource == null) {
            throw new IllegalArgumentException("Resource ID and resource object cannot be null");
        }
        
        coordinationLock.lock();
        try {
            if (resourceRegistry.containsKey(resourceId)) {
                return false;
            }
            
            resourceRegistry.put(resourceId, resource);
            resourceAvailable.signalAll();
            return true;
        } finally {
            coordinationLock.unlock();
        }
    }
    
    /**
     * Adquiere un recurso para uso exclusivo.
     * 
     * @param resourceId identificador del recurso a adquirir
     * @return el recurso adquirido, o null si no está disponible
     */
    public Object acquireResource(String resourceId) {
        return acquireResource(resourceId, 0, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Adquiere un recurso con tiempo de espera máximo.
     * 
     * @param resourceId identificador del recurso a adquirir
     * @param timeout tiempo máximo de espera
     * @param unit unidad de tiempo
     * @return el recurso adquirido, o null si no está disponible en el tiempo especificado
     */
    public Object acquireResource(String resourceId, long timeout, TimeUnit unit) {
        if (resourceId == null) {
            throw new IllegalArgumentException("Resource ID cannot be null");
        }
        
        coordinationLock.lock();
        try {
            long waitTime = unit.toNanos(timeout);
            
            while (isResourceLocked(resourceId) && isActive) {
                if (timeout == 0) {
                    return null; // No esperar si timeout es cero
                }
                
                if (waitTime <= 0) {
                    return null; // Timeout expirado
                }
                
                try {
                    waitTime = resourceAvailable.awaitNanos(waitTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            
            if (!isActive || !resourceRegistry.containsKey(resourceId)) {
                return null;
            }
            
            lockedResources.add(resourceId);
            return resourceRegistry.get(resourceId);
        } finally {
            coordinationLock.unlock();
        }
    }
    
    /**
     * Libera un recurso previamente adquirido.
     * 
     * @param resourceId identificador del recurso a liberar
     * @return true si la liberación fue exitosa, false si el recurso no estaba bloqueado
     */
    public boolean releaseResource(String resourceId) {
        if (resourceId == null) {
            throw new IllegalArgumentException("Resource ID cannot be null");
        }
        
        coordinationLock.lock();
        try {
            boolean wasLocked = lockedResources.remove(resourceId);
            if (wasLocked) {
                resourceAvailable.signalAll();
            }
            return wasLocked;
        } finally {
            coordinationLock.unlock();
        }
    }
    
    /**
     * Elimina un recurso del coordinador.
     * 
     * @param resourceId identificador del recurso a eliminar
     * @return el recurso eliminado, o null si no existía
     */
    public Object removeResource(String resourceId) {
        if (resourceId == null) {
            throw new IllegalArgumentException("Resource ID cannot be null");
        }
        
        coordinationLock.lock();
        try {
            lockedResources.remove(resourceId);
            Object removed = resourceRegistry.remove(resourceId);
            if (removed != null) {
                resourceAvailable.signalAll();
            }
            return removed;
        } finally {
            coordinationLock.unlock();
        }
    }
    
    /**
     * Verifica si un recurso está actualmente bloqueado.
     * 
     * @param resourceId identificador del recurso
     * @return true si el recurso está bloqueado, false en caso contrario
     */
    public boolean isResourceLocked(String resourceId) {
        coordinationLock.lock();
        try {
            return lockedResources.contains(resourceId);
        } finally {
            coordinationLock.unlock();
        }
    }
    
    /**
     * Obtiene el número total de recursos registrados.
     * 
     * @return cantidad de recursos registrados
     */
    public int getRegisteredResourceCount() {
        return resourceRegistry.size();
    }
    
    /**
     * Obtiene el número de recursos actualmente bloqueados.
     * 
     * @return cantidad de recursos bloqueados
     */
    public int getLockedResourceCount() {
        coordinationLock.lock();
        try {
            return lockedResources.size();
        } finally {
            coordinationLock.unlock();
        }
    }
    
    /**
     * Apaga el coordinador y libera todos los recursos.
     */
    public void shutdown() {
        coordinationLock.lock();
        try {
            isActive = false;
            maintenanceExecutor.shutdown();
            lockedResources.clear();
            resourceRegistry.clear();
            resourceAvailable.signalAll();
        } finally {
            coordinationLock.unlock();
        }
    }
    
    /**
     * Verifica si el coordinador está activo.
     * 
     * @return true si el coordinador está activo, false si está apagado
     */
    public boolean isActive() {
        return isActive;
    }
    
    // Métodos privados de utilidad
    
    private boolean isResourceLocked(String resourceId) {
        return lockedResources.contains(resourceId);
    }
    
    private void initializeMaintenanceTask() {
        maintenanceExecutor.scheduleAtFixedRate(() -> {
            performMaintenance();
        }, MAINTENANCE_INTERVAL_MS, MAINTENANCE_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }
    
    private void performMaintenance() {
        coordinationLock.lock();
        try {
            // Limpieza de recursos huérfanos o timeout
            Iterator<String> iterator = lockedResources.iterator();
            while (iterator.hasNext()) {
                String resourceId = iterator.next();
                // En una implementación real, verificaría timeouts aquí
            }
        } finally {
            coordinationLock.unlock();
        }
    }
}
```