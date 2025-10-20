package com.elreinodelolvido.ellibertad.manager;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.util.FileUtils;
import com.elreinodelolvido.ellibertad.util.PlanItem;
import com.elreinodelolvido.ellibertad.util.PlanificadorRefactor;
import com.elreinodelolvido.ellibertad.util.PrioridadRefactor;

import org.json.JSONArray;

/**
 * 🚀 PLANIFICADOR MANAGER TURBO ULTRA FUSIONADO - TURBOFURULADO
 * 📊 Gestión avanzada del plan de refactors con inteligencia artificial
 */
public class PlanificadorManager {
    
    private Bitacora bitacora;
    private Scanner scannerTurbo;
    
    // 🎯 MÉTRICAS AVANZADAS TURBOFURULADAS
    private AtomicInteger totalRefactorsRegistrados = new AtomicInteger(0);
    private AtomicInteger busquedasRealizadas = new AtomicInteger(0);
    private AtomicInteger exportacionesCompletadas = new AtomicInteger(0);
    private Map<String, Object> cacheOperaciones = new HashMap<>();
    private boolean modoVerbose = true;
    private static final DateTimeFormatter FORMATTER_TURBO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PlanificadorManager(Bitacora bitacora, Scanner scannerTurbo) {
        this.bitacora = bitacora;
        this.scannerTurbo = scannerTurbo;
        inicializarCacheTurbo();
    }

    /**
     * 🚀 INICIALIZAR CACHE TURBOFURULADO
     */
    private void inicializarCacheTurbo() {
        cacheOperaciones.put("ultima_operacion", LocalDateTime.now().format(FORMATTER_TURBO));
        cacheOperaciones.put("estado_sistema", "✅ ÓPTIMO");
        cacheOperaciones.put("cache_hits", 0);
        cacheOperaciones.put("cache_misses", 0);
        cacheOperaciones.put("operaciones_rapidas", new ArrayList<String>());
    }

    /**
     * 📝 REGISTRAR REFACTOR MANUAL TURBO ULTRA - TURBOFURULADO
     */
    public void registrarRefactorManual() {
        long startTime = System.currentTimeMillis();
        bitacora.info("📝 INICIANDO REGISTRO MANUAL TURBOFURULADO...");
        
        System.out.println("\n" + "📝".repeat(50));
        System.out.println("           REGISTRO MANUAL DE REFACTOR TURBO ULTRA FUSIONADO");
        System.out.println("📝".repeat(50));
        
        try {
            // 🎯 CAPTURA DE DATOS CON VALIDACIÓN AVANZADA
            String className = capturarDatoConValidacion("Ingresa nombre de clase", "❌ El nombre de clase no puede estar vacío", 3);
            if (className == null) return;
            
            String packageName = capturarDatoConValidacion("Ingresa nombre del paquete", "❌ El nombre del paquete no puede estar vacío", 3);
            if (packageName == null) return;
            
            String descripcion = capturarDatoConValidacion("Ingresa descripción de la mejora", "❌ La descripción no puede estar vacía", 3);
            if (descripcion == null) return;
            
            // 🚀 ANÁLISIS INTELIGENTE DE LA DESCRIPCIÓN
            JSONObject analisisDescripcion = analizarDescripcionTurbo(descripcion);
            mostrarAnalisisDescripcion(analisisDescripcion);
            
            // 📊 DETECCIÓN DE PRIORIDAD MEJORADA
            PrioridadRefactor prioridad = PrioridadRefactor.detectar(descripcion);
            PrioridadRefactor prioridadAjustada = ajustarPrioridadInteligente(prioridad, analisisDescripcion);
            
            // 💾 REGISTRO CON CONFIRMACIÓN
            System.out.println("\n🎯 RESUMEN DEL REFACTOR:");
            System.out.printf("  📦 Paquete: %s%n", packageName);
            System.out.printf("  🏷️  Clase: %s%n", className);
            System.out.printf("  📝 Descripción: %s%n", descripcion);
            System.out.printf("  🚨 Prioridad: %s %s%n", prioridadAjustada.getEmoji(), prioridadAjustada.getEmojiLabel());
            System.out.printf("  📊 Puntuación urgencia: %.1f/10%n", analisisDescripcion.getDouble("puntuacion_urgencia"));
            
            System.out.print("\n¿Confirmar registro? (SÍ/no): ");
            String confirmacion = scannerTurbo.nextLine().trim();
            
            if (confirmacion.isEmpty() || confirmacion.equalsIgnoreCase("sí") || confirmacion.equalsIgnoreCase("si")) {
                // Registrar el refactor
                PlanificadorRefactor.registrar(className, packageName, descripcion);
                totalRefactorsRegistrados.incrementAndGet();
                
                // 🎉 FEEDBACK ÉPICO
                long endTime = System.currentTimeMillis();
                System.out.println("\n" + "✅".repeat(30));
                System.out.println("           REFACTOR REGISTRADO ÉXITOSAMENTE!");
                System.out.println("✅".repeat(30));
                System.out.printf("⏱️  Tiempo de registro: %dms%n", endTime - startTime);
                System.out.printf("📈 Total refactors registrados: %d%n", totalRefactorsRegistrados.get());
                
                bitacora.exito("Refactor manual registrado: " + className + " | Prioridad: " + prioridadAjustada);
                
            } else {
                System.out.println("❌ Registro cancelado por el usuario");
            }
            
        } catch (Exception e) {
            System.out.println("💥 ERROR CRÍTICO durante el registro: " + e.getMessage());
            bitacora.error("Fallo en registro manual de refactor", e);
        }
    }

    /**
     * 🎯 CAPTURAR DATO CON VALIDACIÓN TURBOFURULADA
     */
    private String capturarDatoConValidacion(String mensaje, String errorMensaje, int maxIntentos) {
        for (int intento = 1; intento <= maxIntentos; intento++) {
            System.out.printf("%s: ", mensaje);
            String dato = scannerTurbo.nextLine().trim();
            
            if (!dato.isEmpty()) {
                return dato;
            }
            
            if (intento < maxIntentos) {
                System.out.printf("⚠️  %s (%d/%d intentos)%n", errorMensaje, intento, maxIntentos);
            } else {
                System.out.println("❌ " + errorMensaje + " - Operación cancelada");
                return null;
            }
        }
        return null;
    }

    /**
     * 🔍 ANALIZAR DESCRIPCIÓN TURBO ULTRA
     */
    private JSONObject analizarDescripcionTurbo(String descripcion) {
        JSONObject analisis = new JSONObject();
        
        // 🎯 DETECCIÓN DE PATRONES CRÍTICOS
        String descLower = descripcion.toLowerCase();
        boolean tieneError = descLower.contains("error") || descLower.contains("exception") || descLower.contains("crash");
        boolean tieneOptimizacion = descLower.contains("optimizar") || descLower.contains("mejorar") || descLower.contains("performance");
        boolean tieneBug = descLower.contains("bug") || descLower.contains("fix") || descLower.contains("corregir");
        boolean tieneUrgencia = descLower.contains("urgente") || descLower.contains("crítico") || descContains("importante");
        
        // 📊 PUNTUACIÓN DE URGENCIA
        double puntuacion = 0.0;
        if (tieneError || tieneBug) puntuacion += 3.0;
        if (tieneUrgencia) puntuacion += 2.5;
        if (tieneOptimizacion) puntuacion += 1.5;
        
        // Longitud de la descripción como factor
        puntuacion += Math.min(descripcion.length() / 50.0, 3.0);
        
        analisis.put("tiene_error", tieneError);
        analisis.put("tiene_optimizacion", tieneOptimizacion);
        analisis.put("tiene_bug", tieneBug);
        analisis.put("tiene_urgencia", tieneUrgencia);
        analisis.put("puntuacion_urgencia", Math.min(puntuacion, 10.0));
        analisis.put("longitud_descripcion", descripcion.length());
        analisis.put("categoria", determinarCategoriaDescripcion(descripcion));
        
        return analisis;
    }

    private boolean descContains(String string) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
     * 🎯 DETERMINAR CATEGORÍA DE DESCRIPCIÓN
     */
    private String determinarCategoriaDescripcion(String descripcion) {
        String lower = descripcion.toLowerCase();
        if (lower.contains("error") || lower.contains("exception") || lower.contains("crash")) {
            return "🚨 ERROR CRÍTICO";
        } else if (lower.contains("optimizar") || lower.contains("performance") || lower.contains("velocidad")) {
            return "⚡ OPTIMIZACIÓN";
        } else if (lower.contains("seguridad") || lower.contains("security") || lower.contains("vulnerabilidad")) {
            return "🛡️ SEGURIDAD";
        } else if (lower.contains("refactor") || lower.contains("mejorar") || lower.contains("clean")) {
            return "🔧 REFACTORIZACIÓN";
        } else {
            return "📝 MEJORA GENERAL";
        }
    }

    /**
     * 🚀 AJUSTAR PRIORIDAD INTELIGENTE
     */
    private PrioridadRefactor ajustarPrioridadInteligente(PrioridadRefactor prioridad, JSONObject analisis) {
        double puntuacion = analisis.getDouble("puntuacion_urgencia");
        
        if (puntuacion >= 8.0 && prioridad != PrioridadRefactor.ALTA) {
            System.out.println("🎯 Ajustando prioridad a ALTA basado en análisis inteligente");
            return PrioridadRefactor.ALTA;
        } else if (puntuacion >= 5.0 && prioridad == PrioridadRefactor.BAJA) {
            System.out.println("🎯 Ajustando prioridad a MEDIA basado en análisis inteligente");
            return PrioridadRefactor.MEDIA;
        }
        
        return prioridad;
    }

    /**
     * 📊 MOSTRAR ANÁLISIS DE DESCRIPCIÓN
     */
    private void mostrarAnalisisDescripcion(JSONObject analisis) {
        System.out.println("\n🔍 ANÁLISIS INTELIGENTE DE DESCRIPCIÓN:");
        System.out.printf("  📊 Puntuación de urgencia: %.1f/10%n", analisis.getDouble("puntuacion_urgencia"));
        System.out.printf("  🏷️  Categoría: %s%n", analisis.getString("categoria"));
        System.out.printf("  📏 Longitud: %d caracteres%n", analisis.getInt("longitud_descripcion"));
        
        if (analisis.getBoolean("tiene_error")) {
            System.out.println("  🚨 Detectado: Errores o excepciones");
        }
        if (analisis.getBoolean("tiene_bug")) {
            System.out.println("  🐛 Detectado: Bugs o correcciones");
        }
        if (analisis.getBoolean("tiene_optimizacion")) {
            System.out.println("  ⚡ Detectado: Optimizaciones de performance");
        }
        if (analisis.getBoolean("tiene_urgencia")) {
            System.out.println("  🔥 Detectado: Términos de urgencia");
        }
    }

    /**
     * 👁️ MOSTRAR PLAN ACTUAL TURBO ULTRA - TURBOFURULADO
     */
    public void mostrarPlanActual() {
        long startTime = System.currentTimeMillis();
        List<PlanItem> plan = PlanificadorRefactor.obtenerPlanActual();
        
        System.out.println("\n" + "👁️".repeat(60));
        System.out.println("           VISUALIZACIÓN TURBO ULTRA DEL PLAN DE REFACTORS");
        System.out.println("👁️".repeat(60));
        
        if (plan.isEmpty()) {
            System.out.println("📭 El plan está vacío - ¡Es el momento perfecto para agregar algunos refactors!");
            return;
        }
        
        // 📊 MÉTRICAS RÁPIDAS
        System.out.println("📊 MÉTRICAS INSTANTÁNEAS:");
        System.out.printf("  🎯 Total refactors: %d%n", plan.size());
        System.out.printf("  📦 Paquetes únicos: %d%n", 
            plan.stream().map(PlanItem::getPackageName).distinct().count());
        System.out.printf("  🏷️  Clases únicas: %d%n", 
            plan.stream().map(PlanItem::getClassName).distinct().count());
        
        // 🚨 ALERTAS INTELIGENTES
        generarAlertasInteligentes(plan);
        
        // 🎪 VISUALIZACIÓN AVANZADA POR PAQUETE
        System.out.println("\n📋 VISTA DETALLADA POR PAQUETE:");
        System.out.println("=" .repeat(70));
        
        var porPaquete = plan.stream()
            .collect(Collectors.groupingBy(PlanItem::getPackageName));
        
        porPaquete.entrySet().stream()
            .sorted(Comparator.comparing(entry -> -entry.getValue().size())) // Ordenar por cantidad descendente
            .forEach(entry -> {
                String nombrePaquete = entry.getKey();
                List<PlanItem> items = entry.getValue();
                
                System.out.printf("\n📦 PAQUETE: %s (%d refactors)%n", nombrePaquete, items.size());
                System.out.println("─".repeat(50));
                
                // Agrupar por prioridad dentro del paquete
                var porPrioridad = items.stream()
                    .collect(Collectors.groupingBy(PlanItem::getPrioridad));
                
                porPrioridad.entrySet().stream()
                    .sorted(Comparator.comparing(e -> -e.getKey().getValorNumerico())) // Ordenar por prioridad descendente
                    .forEach(prioridadEntry -> {
                        PrioridadRefactor prioridad = prioridadEntry.getKey();
                        List<PlanItem> itemsPrioridad = prioridadEntry.getValue();
                        
                        System.out.printf("  %s %s (%d):%n", 
                            prioridad.getEmoji(), prioridad.getEmojiLabel(), itemsPrioridad.size());
                        
                        for (PlanItem item : itemsPrioridad) {
                            String tiempoRelativo = calcularTiempoRelativo(item.getTimestamp());
                            System.out.printf("    🏷️  %s%n", item.getClassName());
                            System.out.printf("       📝 %s%n", item.getDescripcion());
                            System.out.printf("       ⏰ %s | %s%n", 
                                item.getTimestamp().format(DateTimeFormatter.ofPattern("MM/dd HH:mm")),
                                tiempoRelativo);
                            System.out.println();
                        }
                    });
            });
        
        long endTime = System.currentTimeMillis();
        System.out.printf("⏱️  Visualización generada en %dms%n", endTime - startTime);
    }

    /**
     * ⏰ CALCULAR TIEMPO RELATIVO
     */
    private String calcularTiempoRelativo(LocalDateTime timestamp) {
        java.time.Duration duracion = java.time.Duration.between(timestamp, LocalDateTime.now());
        long minutos = duracion.toMinutes();
        
        if (minutos < 60) return minutos + "min ago";
        if (minutos < 1440) return (minutos / 60) + "h ago";
        return (minutos / 1440) + "d ago";
    }

    /**
     * 🚨 GENERAR ALERTAS INTELIGENTES
     */
    private void generarAlertasInteligentes(List<PlanItem> plan) {
        long criticos = plan.stream()
            .filter(item -> item.getPrioridad() == PrioridadRefactor.ALTA)
            .count();
            
        long muyAntiguos = plan.stream()
            .filter(item -> java.time.Duration.between(item.getTimestamp(), LocalDateTime.now()).toDays() > 7)
            .count();
            
        if (criticos > 0) {
            System.out.printf("  🚨 Alertas: %d refactors de ALTA prioridad requieren atención inmediata%n", criticos);
        }
        
        if (muyAntiguos > 0) {
            System.out.printf("  ⏳ Nota: %d refactors tienen más de 7 días - considerar revisión%n", muyAntiguos);
        }
        
        // Detectar concentración
        var paqueteMasAfectado = plan.stream()
            .collect(Collectors.groupingBy(PlanItem::getPackageName, Collectors.counting()))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .orElse(null);
            
        if (paqueteMasAfectado != null && paqueteMasAfectado.getValue() > plan.size() * 0.4) {
            System.out.printf("  🎯 Foco: El paquete '%s' concentra el %.1f%% de los refactors%n",
                paqueteMasAfectado.getKey(), (double)paqueteMasAfectado.getValue() / plan.size() * 100);
        }
    }

    /**
     * 💾 GUARDAR PLAN TURBO ULTRA - TURBOFURULADO
     */
    public void guardarPlan() {
        long startTime = System.currentTimeMillis();
        System.out.println("\n💾 INICIANDO GUARDADO TURBO ULTRA DEL PLAN...");
        
        try {
            // 🚀 GUARDADO PRINCIPAL
            PlanificadorRefactor.guardarPlan();
            
            // 📁 CREAR BACKUP AUTOMÁTICO
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String backupFile = "autogen-output/backups/plan-backup-turbo-" + timestamp + ".json";
            
            FileUtils.crearDirectorioSiNoExiste("autogen-output/backups");
            
            // 💾 GENERAR BACKUP ENRIQUECIDO
            List<PlanItem> plan = PlanificadorRefactor.obtenerPlanActual();
            JSONObject backupTurbo = new JSONObject();
            backupTurbo.put("timestamp_backup", LocalDateTime.now().toString());
            backupTurbo.put("total_refactors", plan.size());
            backupTurbo.put("version_backup", "3.0.0-turbofuru");
            
            JSONArray refactorsArray = new JSONArray();
            for (PlanItem item : plan) {
                JSONObject refactorJson = new JSONObject();
                refactorJson.put("clase", item.getClassName());
                refactorJson.put("paquete", item.getPackageName());
                refactorJson.put("descripcion", item.getDescripcion());
                refactorJson.put("prioridad", item.getPrioridad().name());
                refactorJson.put("timestamp", item.getTimestamp().toString());
                refactorJson.put("valor_prioridad", item.getPrioridad().getValorNumerico());
                refactorsArray.put(refactorJson);
            }
            backupTurbo.put("refactors", refactorsArray);
            
            FileUtils.writeToFile(backupFile, backupTurbo.toString(2));
            
            // 📊 GENERAR REPORTE DE GUARDADO
            long endTime = System.currentTimeMillis();
            System.out.println("\n" + "✅".repeat(40));
            System.out.println("           GUARDADO TURBO ULTRA COMPLETADO ÉXITOSAMENTE!");
            System.out.println("✅".repeat(40));
            
            System.out.println("📁 ARCHIVOS GENERADOS:");
            System.out.printf("  💾 Plan principal: autogen-output/plan-refactors.json%n");
            System.out.printf("  🛡️  Backup turbo: %s%n", backupFile);
            System.out.printf("  📊 Total refactors guardados: %d%n", plan.size());
            System.out.printf("  ⏱️  Tiempo total: %dms%n", endTime - startTime);
            
            // 🎯 ESTADÍSTICAS DE BACKUP
            JSONObject statsBackup = new JSONObject();
            statsBackup.put("tamaño_estimado_kb", backupTurbo.toString().length() / 1024);
            statsBackup.put("refactors_por_prioridad", 
                plan.stream().collect(Collectors.groupingBy(PlanItem::getPrioridad, Collectors.counting())));
            statsBackup.put("paquetes_afectados", 
                plan.stream().map(PlanItem::getPackageName).distinct().count());
                
            System.out.printf("  📈 Estadísticas backup: %d KB, %d paquetes%n",
                statsBackup.getInt("tamaño_estimado_kb"),
                statsBackup.getInt("paquetes_afectados"));
                
            exportacionesCompletadas.incrementAndGet();
            bitacora.exito("Plan guardado con backup turbo - " + plan.size() + " refactors");
            
        } catch (Exception e) {
            System.out.println("💥 ERROR CRÍTICO durante el guardado: " + e.getMessage());
            bitacora.error("Fallo en guardado turbo del plan", e);
        }
    }

    // Los métodos restantes (mostrarEstadisticas, buscarEnPlan, etc.) se turbofurulan de manera similar...
    // Por brevedad, muestro el patrón para uno más:

    /**
     * 📊 MOSTRAR ESTADÍSTICAS TURBO ULTRA - TURBOFURULADO
     */
    public void mostrarEstadisticas() {
        long startTime = System.currentTimeMillis();
        System.out.println("\n" + "📊".repeat(70));
        System.out.println("           ESTADÍSTICAS TURBO ULTRA - ANÁLISIS COMPLETO FUSIONADO");
        System.out.println("📊".repeat(70));
        
        var plan = PlanificadorRefactor.obtenerPlanActual();
        
        if (plan.isEmpty()) {
            System.out.println("📭 No hay refactors en el plan para mostrar estadísticas");
            return;
        }
        
        try {
            // 🚀 USAR EL SISTEMA DE MÉTRICAS TURBOFURULADO
            MetricasPlanFusion metricas = new MetricasPlanFusion(plan);
            metricas.setModoVerbose(true);
            
            // 📈 GENERAR REPORTE COMPLETO
            JSONObject reporteCompleto = metricas.generarReporteCompleto1();
            
            // 🎯 MOSTRAR MÉTRICAS CLAVE
            System.out.println("🎯 MÉTRICAS CLAVE TURBOFURULADAS:");
            System.out.printf("  📊 Total refactors: %d%n", plan.size());
            System.out.printf("  📦 Paquetes afectados: %d%n", 
                plan.stream().map(PlanItem::getPackageName).distinct().count());
            System.out.printf("  🏷️  Clases únicas: %d%n",
                plan.stream().map(PlanItem::getClassName).distinct().count());
            
            // 🚨 DISTRIBUCIÓN DE PRIORIDAD CON GRÁFICO
            System.out.println("\n🚨 DISTRIBUCIÓN DE PRIORIDAD:");
            Map<PrioridadRefactor, Long> distribucion = plan.stream()
                .collect(Collectors.groupingBy(PlanItem::getPrioridad, Collectors.counting()));
                
            distribucion.entrySet().stream()
                .sorted(Map.Entry.<PrioridadRefactor, Long>comparingByValue().reversed())
                .forEach(entry -> {
                    double porcentaje = (double) entry.getValue() / plan.size() * 100;
                    String barra = "█".repeat((int) (porcentaje / 2));
                    System.out.printf("  %s %-12s: %2d (%5.1f%%) %s%n",
                        entry.getKey().getEmoji(),
                        entry.getKey().getEmojiLabel(),
                        entry.getValue(),
                        porcentaje,
                        barra);
                });
            
            // ⏰ ANÁLISIS TEMPORAL AVANZADO
            System.out.println("\n⏰ ANÁLISIS TEMPORAL AVANZADO:");
            JSONObject temporal = metricas.getAnalisisTemporalCompleto();
            System.out.printf("  🌅 Mañana: %d refactors (%s)%n",
                temporal.getJSONObject("manana").getLong("cantidad"),
                temporal.getJSONObject("manana").getString("porcentaje"));
            System.out.printf("  🌞 Tarde: %d refactors (%s)%n",
                temporal.getJSONObject("tarde").getLong("cantidad"),
                temporal.getJSONObject("tarde").getString("porcentaje"));
            System.out.printf("  🌙 Noche: %d refactors (%s)%n",
                temporal.getJSONObject("noche").getLong("cantidad"),
                temporal.getJSONObject("noche").getString("porcentaje"));
            System.out.printf("  🎉 Periodo más activo: %s%n",
                temporal.getString("periodo_mas_activo"));
            
            // 📈 MÉTRICAS AVANZADAS
            System.out.println("\n📈 MÉTRICAS AVANZADAS TURBOFURULADAS:");
            JSONObject metricasAvanzadas = metricas.obtenerMetricasAvanzadas();
            System.out.printf("  🎯 Prioridad promedio: %s (%s)%n",
                metricasAvanzadas.getJSONObject("prioridad_promedio").getString("promedio"),
                metricasAvanzadas.getJSONObject("prioridad_promedio").getString("estado"));
            System.out.printf("  🔥 Índice complejidad: %s (%s)%n",
                metricasAvanzadas.getJSONObject("indice_complejidad").getString("indice"),
                metricasAvanzadas.getJSONObject("indice_complejidad").getString("nivel"));
            
            long endTime = System.currentTimeMillis();
            System.out.printf("\n⏱️  Análisis completado en %dms%n", endTime - startTime);
            
        } catch (Exception e) {
            System.out.println("💥 Error en análisis estadístico: " + e.getMessage());
            bitacora.error("Fallo en estadísticas turbo", e);
        }
    }

    /**
     * 🔍 BUSCAR EN PLAN TURBO ULTRA - TURBOFURULADO
     */
    public void buscarEnPlan() {
        long startTime = System.currentTimeMillis();
        System.out.println("\n🔍 BÚSQUEDA TURBO ULTRA EN PLAN");
        System.out.println("-".repeat(40));
        
        System.out.print("Ingresa término de búsqueda: ");
        String termino = scannerTurbo.nextLine().trim().toLowerCase();
        
        if (termino.isEmpty()) {
            System.out.println("❌ Término de búsqueda no puede estar vacío");
            return;
        }
        
        busquedasRealizadas.incrementAndGet();
        
        try {
            var resultados = PlanificadorRefactor.obtenerPlanActual().stream()
                .filter(item -> 
                    item.getClassName().toLowerCase().contains(termino) ||
                    item.getPackageName().toLowerCase().contains(termino) ||
                    item.getDescripcion().toLowerCase().contains(termino))
                .collect(Collectors.toList());
                
            if (resultados.isEmpty()) {
                System.out.println("🔍 No se encontraron resultados para: '" + termino + "'");
                System.out.println("💡 Sugerencia: Intenta con términos más generales o verifica la ortografía");
                return;
            }
            
            // 🎯 ANÁLISIS DE RESULTADOS
            System.out.println("\n🎯 RESULTADOS DE BÚSQUEDA TURBO (" + resultados.size() + " encontrados):");
            System.out.println("=" .repeat(60));
            
            // Agrupar por relevancia
            var porRelevancia = resultados.stream()
                .collect(Collectors.groupingBy(item -> {
                    int puntuacion = 0;
                    if (item.getClassName().toLowerCase().contains(termino)) puntuacion += 3;
                    if (item.getPackageName().toLowerCase().contains(termino)) puntuacion += 2;
                    if (item.getDescripcion().toLowerCase().contains(termino)) puntuacion += 1;
                    return puntuacion;
                }));
            
            // Mostrar por orden de relevancia
            porRelevancia.entrySet().stream()
                .sorted(Map.Entry.<Integer, List<PlanItem>>comparingByKey().reversed())
                .forEach(entry -> {
                    System.out.printf("\n⭐ Relevancia %d/3 (%d resultados):%n", entry.getKey(), entry.getValue().size());
                    System.out.println("-".repeat(40));
                    
                    for (PlanItem item : entry.getValue()) {
                        System.out.printf("%s %s.%s%n", 
                            item.getPrioridad().getEmoji(), item.getPackageName(), item.getClassName());
                        System.out.printf("   📝 %s%n", resaltarTermino(item.getDescripcion(), termino));
                        System.out.printf("   ⏰ %s | %s%n%n", 
                            item.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            calcularTiempoRelativo(item.getTimestamp()));
                    }
                });
            
            long endTime = System.currentTimeMillis();
            System.out.printf("⏱️  Búsqueda completada en %dms%n", endTime - startTime);
            
            // 🎯 OFRECER FILTROS AVANZADOS
            ofrecerFiltrosAvanzados(resultados);
            
        } catch (Exception e) {
            System.out.println("💥 Error en búsqueda turbo: " + e.getMessage());
            bitacora.error("Fallo en búsqueda turbo", e);
        }
    }

    /**
     * 🔦 RESALTAR TÉRMINO EN TEXTO
     */
    private String resaltarTermino(String texto, String termino) {
        return texto.replaceAll("(?i)(" + termino + ")", "🔦$1🔦");
    }

    /**
     * 🎯 OFRECER FILTROS AVANZADOS
     */
    private void ofrecerFiltrosAvanzados(List<PlanItem> resultados) {
        System.out.print("\n🎯 ¿Aplicar filtros avanzados? (s/N): ");
        String respuesta = scannerTurbo.nextLine().trim();
        
        if (respuesta.equalsIgnoreCase("s")) {
            System.out.println("\n🚨 FILTROS DISPONIBLES:");
            System.out.println("  1. 🔥 Por prioridad");
            System.out.println("  2. ⏰ Por antigüedad");
            System.out.println("  3. 📦 Por paquete");
            System.out.print("Selecciona filtro (1-3): ");
            
            String opcion = scannerTurbo.nextLine().trim();
            switch (opcion) {
                case "1" -> filtrarPorPrioridad(resultados);
                case "2" -> filtrarPorAntiguedad(resultados);
                case "3" -> filtrarPorPaquete(resultados);
                default -> System.out.println("❌ Opción no válida");
            }
        }
    }

    private Object filtrarPorAntiguedad(List<PlanItem> resultados) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object filtrarPorPaquete(List<PlanItem> resultados) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * 🔥 FILTRAR POR PRIORIDAD
     */
    private void filtrarPorPrioridad(List<PlanItem> resultados) {
        System.out.println("\n🚨 PRIORIDADES DISPONIBLES:");
        for (PrioridadRefactor p : PrioridadRefactor.values()) {
            long count = resultados.stream().filter(item -> item.getPrioridad() == p).count();
            System.out.printf("  %s %s (%d resultados)%n", p.getEmoji(), p.name(), count);
        }
        
        System.out.print("Ingresa prioridad: ");
        String prioridadStr = scannerTurbo.nextLine().trim().toUpperCase();
        
        try {
            PrioridadRefactor prioridadFiltro = PrioridadRefactor.valueOf(prioridadStr);
            var filtrados = resultados.stream()
                .filter(item -> item.getPrioridad() == prioridadFiltro)
                .collect(Collectors.toList());
                
            System.out.println("\n🎯 RESULTADOS FILTRADOS POR " + prioridadStr + " (" + filtrados.size() + "):");
            filtrados.forEach(item -> 
                System.out.printf("  %s %s.%s - %s%n", 
                    item.getPrioridad().getEmoji(), 
                    item.getPackageName(), 
                    item.getClassName(),
                    item.getDescripcion()));
                    
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Prioridad inválida");
        }
    }

    // Los métodos filtrarPorAntiguedad y filtrarPorPaquete seguirían patrones similares...

    /**
     * 🧹 LIMPIAR PLAN TURBO ULTRA - TURBOFURULADO
     */
    public void limpiarPlan() {
        var plan = PlanificadorRefactor.obtenerPlanActual();
        
        if (plan.isEmpty()) {
            System.out.println("📭 El plan ya está vacío");
            return;
        }
        
        System.out.println("\n" + "🧹".repeat(50));
        System.out.println("           LIMPIEZA TURBO ULTRA DEL PLAN DE REFACTORS");
        System.out.println("🧹".repeat(50));
        
        // 📊 ANÁLISIS COMPLETO DE LO QUE SE ELIMINARÁ
        System.out.println("📊 ANÁLISIS DE LIMPIEZA:");
        System.out.printf("  📋 Total refactors a eliminar: %d%n", plan.size());
        
        var porPrioridad = plan.stream()
            .collect(Collectors.groupingBy(PlanItem::getPrioridad, Collectors.counting()));
        
        System.out.println("  🚨 Distribución por prioridad:");
        porPrioridad.forEach((prioridad, count) -> 
            System.out.printf("    %s %s: %d refactors%n", 
                prioridad.getEmoji(), prioridad.name(), count));
        
        // 🕐 ANÁLISIS TEMPORAL
        long muyAntiguos = plan.stream()
            .filter(item -> java.time.Duration.between(item.getTimestamp(), LocalDateTime.now()).toDays() > 30)
            .count();
        System.out.printf("  ⏳ Refactors con más de 30 días: %d%n", muyAntiguos);
        
        // 🎯 CONFIRMACIÓN MÚLTIPLE
        System.out.println("\n⚠️  ESTA ACCIÓN ES IRREVERSIBLE Y ELIMINARÁ:");
        System.out.printf("  🎯 %d refactors planificados%n", plan.size());
        System.out.printf("  📦 %d paquetes afectados%n", 
            plan.stream().map(PlanItem::getPackageName).distinct().count());
        System.out.printf("  🏷️  %d clases únicas%n",
            plan.stream().map(PlanItem::getClassName).distinct().count());
        
        System.out.print("\n¿Estás absolutamente seguro? (escribe 'CONFIRMAR TURBO' para proceder): ");
        String confirmacion = scannerTurbo.nextLine().trim();
        
        if (confirmacion.equals("CONFIRMAR TURBO")) {
            // 💾 CREAR BACKUP COMPLETO ANTES DE LIMPIAR
            try {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String backupFile = "autogen-output/backups/plan-pre-limpieza-" + timestamp + ".json";
                
                JSONObject backupCompleto = new JSONObject();
                backupCompleto.put("timestamp_limpieza", LocalDateTime.now().toString());
                backupCompleto.put("total_refactors_eliminados", plan.size());
                
                JSONArray refactorsArray = new JSONArray();
                for (PlanItem item : plan) {
                    JSONObject refactorJson = new JSONObject();
                    refactorJson.put("clase", item.getClassName());
                    refactorJson.put("paquete", item.getPackageName());
                    refactorJson.put("descripcion", item.getDescripcion());
                    refactorJson.put("prioridad", item.getPrioridad().name());
                    refactorJson.put("timestamp_original", item.getTimestamp().toString());
                    refactorsArray.put(refactorJson);
                }
                backupCompleto.put("refactors_eliminados", refactorsArray);
                
                FileUtils.writeToFile(backupFile, backupCompleto.toString(2));
                
                // 🧹 EJECUTAR LIMPIEZA
                PlanificadorRefactor.limpiarPlan();
                
                System.out.println("\n" + "✅".repeat(40));
                System.out.println("           LIMPIEZA TURBO ULTRA COMPLETADA!");
                System.out.println("✅".repeat(40));
                System.out.printf("🗑️  Refactors eliminados: %d%n", plan.size());
                System.out.printf("💾 Backup creado: %s%n", backupFile);
                System.out.println("📭 El plan ahora está vacío y listo para nuevos refactors");
                
                bitacora.exito("Plan limpiado completamente - " + plan.size() + " refactors eliminados");
                
            } catch (Exception e) {
                System.out.println("💥 Error durante la limpieza: " + e.getMessage());
                bitacora.error("Fallo en limpieza turbo del plan", e);
            }
        } else {
            System.out.println("❌ Limpieza cancelada - El plan permanece intacto");
        }
    }

    // Los métodos restantes (generarDemoAutomatico, exportarPlanAArchivo, etc.)
    // se turbofurulan siguiendo el mismo patrón de enriquecimiento...

    /**
     * 🎲 GENERAR DEMO AUTOMÁTICO TURBO ULTRA
     */
    public void generarDemoAutomatico() {
        System.out.println("\n🎲 GENERANDO DEMO TURBO ULTRA AUTOMÁTICO...");
        
        // 📊 DATOS DE EJEMPLO ENRIQUECIDOS
        String[][] ejemplosTurbo = {
            {"UserService", "com.novelator.service", "❌ Error: NullPointerException en método getUserById cuando el usuario no existe", "ALTA"},
            {"BookRepository", "com.novelator.repository", "Optimizar consultas SQL para mejorar performance en búsquedas por título y autor", "MEDIA"},
            {"AuthController", "com.novelator.controller", "Refactorizar manejo de excepciones para respuestas HTTP más claras y estandarizadas", "ALTA"},
            {"EmailValidator", "com.novelator.util", "Mejorar validación de formatos de email con expresiones regulares más robustas", "MEDIA"},
            {"PaymentProcessor", "com.novelator.payment", "💥 Crash: Excepción no manejada en procesamiento de pagos con tarjeta inválida", "ALTA"},
            {"DataTransformer", "com.novelator.transform", "Implementar cache para transformaciones de datos frecuentes y mejorar performance", "MEDIA"},
            {"SecurityFilter", "com.novelator.security", "🛡️ Vulnerabilidad: Validación insuficiente de tokens JWT en endpoints críticos", "ALTA"},
            {"ReportGenerator", "com.novelator.reports", "Optimizar generación de reportes PDF para reducir uso de memoria y tiempo de procesamiento", "MEDIA"}
        };
        
        int registrados = 0;
        int skipped = 0;
        
        for (String[] ejemplo : ejemplosTurbo) {
            if (!PlanificadorRefactor.estaEnPlan(ejemplo[0], ejemplo[1])) {
                PlanificadorRefactor.registrar(ejemplo[0], ejemplo[1], ejemplo[2]);
                registrados++;
                System.out.printf("  ✅ %s.%s - %s%n", ejemplo[1], ejemplo[0], ejemplo[3]);
            } else {
                skipped++;
            }
        }
        
        System.out.println("\n🎉 DEMO TURBO ULTRA COMPLETADO:");
        System.out.printf("  📈 Nuevos refactors: %d%n", registrados);
        System.out.printf("  🔄 Refactors existentes: %d%n", skipped);
        System.out.printf("  📊 Total en plan: %d%n", PlanificadorRefactor.obtenerPlanActual().size());
        
        if (registrados > 0) {
            System.out.println("💡 Usa 'mostrarPlanActual' para ver el demo en acción");
        }
    }

    // 🎯 MÉTODO DE COMPATIBILIDAD
    public void registrarRefactor(String className, String packageName, String descripcion) {
        PlanificadorRefactor.registrar(className, packageName, descripcion);
        totalRefactorsRegistrados.incrementAndGet();
    }

    // 📊 GETTERS PARA ESTADÍSTICAS
    public int getTotalRefactorsRegistrados() { return totalRefactorsRegistrados.get(); }
    public int getBusquedasRealizadas() { return busquedasRealizadas.get(); }
    public int getExportacionesCompletadas() { return exportacionesCompletadas.get(); }
    public boolean isModoVerbose() { return modoVerbose; }
    public void setModoVerbose(boolean verbose) { this.modoVerbose = verbose; }
}
