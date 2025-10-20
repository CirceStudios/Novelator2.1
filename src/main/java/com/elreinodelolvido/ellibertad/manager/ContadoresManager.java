package com.elreinodelolvido.ellibertad.manager;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;
import org.json.JSONArray;

public class ContadoresManager {
    
    // 📊 ESTADÍSTICAS TURBO FUSIONADAS MEJORADAS
    private static final AtomicInteger CONTADOR_EJECUCIONES = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_CLASES_PROCESADAS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_KRAKENS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_INTEGRACIONES_EXITOSAS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_MEJORAS_ACTIVAS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_VERIFICACIONES = new AtomicInteger(0);
    
    // 🚀 CONTADORES AVANZADOS TURBOFURULADOS
    private static final AtomicInteger CONTADOR_LLAMADAS_API = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_REFACTORS_REGISTRADOS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_ANALISIS_EXITOSOS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_TIEMPO_TOTAL_MS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_TOKENS_UTILIZADOS = new AtomicInteger(0);
    
    // 🎯 HISTÓRICO Y TENDENCIAS
    private static final List<ContadoresSnapshot> HISTORICO_SNAPSHOTS = Collections.synchronizedList(new ArrayList<>());
    private static final Map<String, AtomicInteger> CONTADORES_PERSONALIZADOS = new ConcurrentHashMap<>();
    private static final int MAX_HISTORICO = 100; // Límite de snapshots en historial

    // 🔧 CONFIGURACIÓN
    private boolean modoVerbose = true;
    private boolean autoSnapshot = true;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 🏴‍☠️ CONSTRUCTOR TURBOFURULADO
     * @param contadorVerificaciones 
     * @param contadorMejorasActivas 
     * @param contadorIntegracionesExitosas 
     * @param contadorKrakens 
     * @param contadorClasesProcesadas 
     * @param contadorEjecuciones 
     */
    public ContadoresManager(AtomicInteger contadorEjecuciones, AtomicInteger contadorClasesProcesadas, AtomicInteger contadorKrakens, AtomicInteger contadorIntegracionesExitosas, AtomicInteger contadorMejorasActivas, AtomicInteger contadorVerificaciones) {
        inicializarContadoresPersonalizados();
        if (autoSnapshot) {
            tomarSnapshotAutomatico("INICIALIZACIÓN");
        }
    }

    /**
     * 🚀 INICIALIZAR CONTADORES PERSONALIZADOS
     */
    private void inicializarContadoresPersonalizados() {
        CONTADORES_PERSONALIZADOS.put("operaciones_file_utils", new AtomicInteger(0));
        CONTADORES_PERSONALIZADOS.put("escaneos_proyecto", new AtomicInteger(0));
        CONTADORES_PERSONALIZADOS.put("analisis_ia", new AtomicInteger(0));
        CONTADORES_PERSONALIZADOS.put("pdfs_generados", new AtomicInteger(0));
        CONTADORES_PERSONALIZADOS.put("cache_hits", new AtomicInteger(0));
        CONTADORES_PERSONALIZADOS.put("cache_misses", new AtomicInteger(0));
    }

    // =========================================================================
    // 📊 MÉTODOS DE INCREMENTO TURBOFURULADOS
    // =========================================================================

    /**
     * 📊 INCREMENTAR CONTADOR DE EJECUCIONES CON SNAPSHOT
     */
    public void incrementarEjecuciones() {
        CONTADOR_EJECUCIONES.incrementAndGet();
        if (autoSnapshot && CONTADOR_EJECUCIONES.get() % 5 == 0) {
            tomarSnapshotAutomatico("EJECUCIÓN_" + CONTADOR_EJECUCIONES.get());
        }
        if (modoVerbose) {
            System.out.println("🎯 Ejecución incrementada: " + CONTADOR_EJECUCIONES.get());
        }
    }

    public void incrementarEjecuciones(int cantidad) {
        CONTADOR_EJECUCIONES.addAndGet(cantidad);
        if (autoSnapshot) {
            tomarSnapshotAutomatico("EJECUCIONES_MULTIPLES_" + cantidad);
        }
    }

    /**
     * 📊 INCREMENTAR CONTADOR DE CLASES PROCESADAS
     */
    public void incrementarClasesProcesadas() {
        CONTADOR_CLASES_PROCESADAS.incrementAndGet();
        if (modoVerbose && CONTADOR_CLASES_PROCESADAS.get() % 10 == 0) {
            System.out.println("📦 Clases procesadas: " + CONTADOR_CLASES_PROCESADAS.get());
        }
    }

    public void incrementarClasesProcesadas(int cantidad) {
        CONTADOR_CLASES_PROCESADAS.addAndGet(cantidad);
    }

    /**
     * 📊 INCREMENTAR CONTADOR DE KRAKENS
     */
    public void incrementarKrakens() {
        CONTADOR_KRAKENS.incrementAndGet();
        if (modoVerbose) {
            System.out.println("💥 Kraken detectado! Total: " + CONTADOR_KRAKENS.get());
        }
    }

    public void incrementarKrakens(int cantidad) {
        CONTADOR_KRAKENS.addAndGet(cantidad);
    }

    /**
     * 📊 INCREMENTAR CONTADOR DE INTEGRACIONES EXITOSAS
     */
    public void incrementarIntegracionesExitosas() {
        CONTADOR_INTEGRACIONES_EXITOSAS.incrementAndGet();
        if (modoVerbose && CONTADOR_INTEGRACIONES_EXITOSAS.get() % 5 == 0) {
            System.out.println("✅ Integraciones exitosas: " + CONTADOR_INTEGRACIONES_EXITOSAS.get());
        }
    }

    public void incrementarIntegracionesExitosas(int cantidad) {
        CONTADOR_INTEGRACIONES_EXITOSAS.addAndGet(cantidad);
    }

    /**
     * 📊 INCREMENTAR CONTADOR DE MEJORAS ACTIVAS
     */
    public void incrementarMejorasActivas() {
        CONTADOR_MEJORAS_ACTIVAS.incrementAndGet();
    }

    public void incrementarMejorasActivas(int cantidad) {
        CONTADOR_MEJORAS_ACTIVAS.addAndGet(cantidad);
    }

    /**
     * 📊 INCREMENTAR CONTADOR DE VERIFICACIONES
     */
    public void incrementarVerificaciones() {
        CONTADOR_VERIFICACIONES.incrementAndGet();
    }

    public void incrementarVerificaciones(int cantidad) {
        CONTADOR_VERIFICACIONES.addAndGet(cantidad);
    }

    // =========================================================================
    // 🚀 CONTADORES AVANZADOS TURBOFURULADOS
    // =========================================================================

    /**
     * 📊 INCREMENTAR LLAMADAS API
     */
    public void incrementarLlamadasAPI() {
        CONTADOR_LLAMADAS_API.incrementAndGet();
    }

    public void incrementarLlamadasAPI(int cantidad) {
        CONTADOR_LLAMADAS_API.addAndGet(cantidad);
    }

    /**
     * 📊 INCREMENTAR REFACTORS REGISTRADOS
     */
    public void incrementarRefactorsRegistrados() {
        CONTADOR_REFACTORS_REGISTRADOS.incrementAndGet();
    }

    public void incrementarRefactorsRegistrados(int cantidad) {
        CONTADOR_REFACTORS_REGISTRADOS.addAndGet(cantidad);
    }

    /**
     * 📊 INCREMENTAR ANÁLISIS EXITOSOS
     */
    public void incrementarAnalisisExitosos() {
        CONTADOR_ANALISIS_EXITOSOS.incrementAndGet();
    }

    public void incrementarAnalisisExitosos(int cantidad) {
        CONTADOR_ANALISIS_EXITOSOS.addAndGet(cantidad);
    }

    /**
     * 📊 AGREGAR TIEMPO DE EJECUCIÓN
     */
    public void agregarTiempoEjecucion(long tiempoMs) {
        CONTADOR_TIEMPO_TOTAL_MS.addAndGet((int) tiempoMs);
    }

    /**
     * 📊 AGREGAR TOKENS UTILIZADOS
     */
    public void agregarTokensUtilizados(int tokens) {
        CONTADOR_TOKENS_UTILIZADOS.addAndGet(tokens);
    }

    /**
     * 📊 INCREMENTAR CONTADOR PERSONALIZADO
     */
    public void incrementarContadorPersonalizado(String nombre) {
        CONTADORES_PERSONALIZADOS.computeIfAbsent(nombre, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void incrementarContadorPersonalizado(String nombre, int cantidad) {
        CONTADORES_PERSONALIZADOS.computeIfAbsent(nombre, k -> new AtomicInteger(0)).addAndGet(cantidad);
    }

    // =========================================================================
    // 📊 GETTERS TURBOFURULADOS
    // =========================================================================

    public static int getContadorEjecuciones() {
        return CONTADOR_EJECUCIONES.get();
    }

    public static int getContadorClasesProcesadas() {
        return CONTADOR_CLASES_PROCESADAS.get();
    }

    public static int getContadorKrakens() {
        return CONTADOR_KRAKENS.get();
    }

    public static int getContadorIntegracionesExitosas() {
        return CONTADOR_INTEGRACIONES_EXITOSAS.get();
    }

    public static int getContadorMejorasActivas() {
        return CONTADOR_MEJORAS_ACTIVAS.get();
    }

    public static int getContadorVerificaciones() {
        return CONTADOR_VERIFICACIONES.get();
    }

    public static int getContadorLlamadasAPI() {
        return CONTADOR_LLAMADAS_API.get();
    }

    public static int getContadorRefactorsRegistrados() {
        return CONTADOR_REFACTORS_REGISTRADOS.get();
    }

    public static int getContadorAnalisisExitosos() {
        return CONTADOR_ANALISIS_EXITOSOS.get();
    }

    public static long getTiempoTotalEjecucion() {
        return CONTADOR_TIEMPO_TOTAL_MS.get();
    }

    public static int getTokensUtilizados() {
        return CONTADOR_TOKENS_UTILIZADOS.get();
    }

    public int getContadorPersonalizado(String nombre) {
        AtomicInteger contador = CONTADORES_PERSONALIZADOS.get(nombre);
        return contador != null ? contador.get() : 0;
    }

    public Map<String, Integer> getTodosContadoresPersonalizados() {
        Map<String, Integer> resultado = new ConcurrentHashMap<>();
        CONTADORES_PERSONALIZADOS.forEach((k, v) -> resultado.put(k, v.get()));
        return resultado;
    }

    // =========================================================================
    // 🎯 SETTERS Y CONFIGURACIÓN
    // =========================================================================

    public void setContadorMejorasActivas(int valor) {
        CONTADOR_MEJORAS_ACTIVAS.set(valor);
    }

    public void setContadorEjecuciones(int valor) {
        CONTADOR_EJECUCIONES.set(valor);
    }

    public void setContadorClasesProcesadas(int valor) {
        CONTADOR_CLASES_PROCESADAS.set(valor);
    }

    public void setContadorKrakens(int valor) {
        CONTADOR_KRAKENS.set(valor);
    }

    public void setContadorIntegracionesExitosas(int valor) {
        CONTADOR_INTEGRACIONES_EXITOSAS.set(valor);
    }

    public void setContadorVerificaciones(int valor) {
        CONTADOR_VERIFICACIONES.set(valor);
    }

    public void setModoVerbose(boolean verbose) {
        this.modoVerbose = verbose;
    }

    public void setAutoSnapshot(boolean autoSnapshot) {
        this.autoSnapshot = autoSnapshot;
    }

    // =========================================================================
    // 🔄 OPERACIONES AVANZADAS TURBOFURULADAS
    // =========================================================================

    /**
     * 📊 REINICIAR TODOS LOS CONTADORES
     */
    public void reiniciarContadores() {
        CONTADOR_EJECUCIONES.set(0);
        CONTADOR_CLASES_PROCESADAS.set(0);
        CONTADOR_KRAKENS.set(0);
        CONTADOR_INTEGRACIONES_EXITOSAS.set(0);
        CONTADOR_MEJORAS_ACTIVAS.set(0);
        CONTADOR_VERIFICACIONES.set(0);
        CONTADOR_LLAMADAS_API.set(0);
        CONTADOR_REFACTORS_REGISTRADOS.set(0);
        CONTADOR_ANALISIS_EXITOSOS.set(0);
        CONTADOR_TIEMPO_TOTAL_MS.set(0);
        CONTADOR_TOKENS_UTILIZADOS.set(0);
        
        CONTADORES_PERSONALIZADOS.forEach((k, v) -> v.set(0));
        HISTORICO_SNAPSHOTS.clear();
        
        tomarSnapshotAutomatico("REINICIO_COMPLETO");
        
        if (modoVerbose) {
            System.out.println("🔄 TODOS LOS CONTADORES REINICIADOS");
        }
    }

    /**
     * 📊 REINICIAR CONTADORES SELECTIVOS
     */
    public void reiniciarContadoresSelectivos(boolean ejecuciones, boolean clases, boolean krakens, 
                                            boolean integraciones, boolean mejoras, boolean verificaciones) {
        if (ejecuciones) CONTADOR_EJECUCIONES.set(0);
        if (clases) CONTADOR_CLASES_PROCESADAS.set(0);
        if (krakens) CONTADOR_KRAKENS.set(0);
        if (integraciones) CONTADOR_INTEGRACIONES_EXITOSAS.set(0);
        if (mejoras) CONTADOR_MEJORAS_ACTIVAS.set(0);
        if (verificaciones) CONTADOR_VERIFICACIONES.set(0);
        
        tomarSnapshotAutomatico("REINICIO_SELECTIVO");
    }

    // =========================================================================
    // 📈 MÉTRICAS Y CÁLCULOS AVANZADOS TURBOFURULADOS
    // =========================================================================

    /**
     * 📊 CALCULAR EFICIENCIA DEL SISTEMA
     */
    public double calcularEficienciaSistema() {
        if (getContadorClasesProcesadas() == 0) {
            return 0.0;
        }
        return (1 - (double)getContadorKrakens() / getContadorClasesProcesadas()) * 100;
    }

    /**
     * 📊 CALCULAR TASA DE ÉXITO
     */
    public double calcularTasaExito() {
        if (getContadorEjecuciones() == 0) {
            return 0.0;
        }
        return (double)getContadorIntegracionesExitosas() / getContadorEjecuciones() * 100;
    }

    /**
     * 📊 CALCULAR DENSIDAD DE MEJORAS
     */
    public double calcularDensidadMejoras() {
        if (getContadorEjecuciones() == 0) {
            return 0.0;
        }
        return (double)getContadorMejorasActivas() / getContadorEjecuciones();
    }

    /**
     * 📊 CALCULAR TIEMPO PROMEDIO POR EJECUCIÓN
     */
    public double calcularTiempoPromedioPorEjecucion() {
        if (getContadorEjecuciones() == 0) {
            return 0.0;
        }
        return (double)getTiempoTotalEjecucion() / getContadorEjecuciones();
    }

    /**
     * 📊 CALCULAR EFICIENCIA DE API
     */
    public double calcularEficienciaAPI() {
        if (getContadorLlamadasAPI() == 0) {
            return 0.0;
        }
        return (double)getContadorAnalisisExitosos() / getContadorLlamadasAPI() * 100;
    }

    /**
     * 📊 CALCULAR PRODUCTIVIDAD DEL SISTEMA
     */
    public double calcularProductividad() {
        if (getTiempoTotalEjecucion() == 0) {
            return 0.0;
        }
        return (double)getContadorClasesProcesadas() / (getTiempoTotalEjecucion() / 1000.0); // Clases por segundo
    }

    /**
     * 📊 OBTENER RESUMEN RÁPIDO TURBOFURULADO
     */
    public String obtenerResumenRapido() {
        double eficiencia = calcularEficienciaSistema();
        double tasaExito = calcularTasaExito();
        double densidad = calcularDensidadMejoras();
        double eficienciaAPI = calcularEficienciaAPI();
        double productividad = calcularProductividad();
        
        return String.format(
            "📊 RESUMEN TURBO: Exec=%d, Clases=%d, Éxito=%.1f%%, Efic=%.1f%%, API=%.1f%%, Prod=%.2f cls/s",
            getContadorEjecuciones(),
            getContadorClasesProcesadas(),
            tasaExito,
            eficiencia,
            eficienciaAPI,
            productividad
        );
    }

    // =========================================================================
    // 📊 MÉTODOS DE VISUALIZACIÓN TURBOFURULADOS
    // =========================================================================

    /**
     * 📊 MOSTRAR ESTADÍSTICAS COMPLETAS TURBOFURULADAS
     */
    public void mostrarEstadisticasCompletas() {
        System.out.println("\n" + "📊".repeat(80));
        System.out.println("           ESTADÍSTICAS TURBOFURULADAS - ANÁLISIS COMPLETO DEL SISTEMA");
        System.out.println("📊".repeat(80));
        
        System.out.println("🎯 EJECUCIÓN DEL SISTEMA:");
        System.out.println("  • Ejecuciones completadas: " + getContadorEjecuciones());
        System.out.println("  • Clases procesadas: " + getContadorClasesProcesadas());
        System.out.println("  • Verificaciones realizadas: " + getContadorVerificaciones());
        System.out.println("  • Tiempo total ejecución: " + getTiempoTotalEjecucion() + "ms");
        
        System.out.println("\n⚡ RENDIMIENTO Y CALIDAD:");
        System.out.println("  • Integraciones exitosas: " + getContadorIntegracionesExitosas());
        System.out.println("  • Krakens encontrados: " + getContadorKrakens());
        System.out.println("  • Mejoras activas: " + getContadorMejorasActivas());
        System.out.println("  • Refactors registrados: " + getContadorRefactorsRegistrados());
        
        System.out.println("\n🔗 USO DE API:");
        System.out.println("  • Llamadas API: " + getContadorLlamadasAPI());
        System.out.println("  • Análisis exitosos: " + getContadorAnalisisExitosos());
        System.out.println("  • Tokens utilizados: " + getTokensUtilizados());
        
        System.out.println("\n📈 MÉTRICAS AVANZADAS:");
        System.out.println("  • Eficiencia del sistema: " + String.format("%.1f%%", calcularEficienciaSistema()));
        System.out.println("  • Tasa de éxito: " + String.format("%.1f%%", calcularTasaExito()));
        System.out.println("  • Densidad de mejoras: " + String.format("%.2f", calcularDensidadMejoras()));
        System.out.println("  • Eficiencia API: " + String.format("%.1f%%", calcularEficienciaAPI()));
        System.out.println("  • Productividad: " + String.format("%.2f cls/s", calcularProductividad()));
        System.out.println("  • Tiempo promedio/ejecución: " + String.format("%.0fms", calcularTiempoPromedioPorEjecucion()));
        
        // Evaluación del sistema
        mostrarEvaluacionSistema();
    }

    /**
     * 📊 MOSTRAR EVALUACIÓN DEL SISTEMA
     */
    private void mostrarEvaluacionSistema() {
        double eficiencia = calcularEficienciaSistema();
        String evaluacion;
        String emoji;
        
        if (eficiencia >= 95) {
            evaluacion = "🏆 EXCELENTE";
            emoji = "🎯";
        } else if (eficiencia >= 85) {
            evaluacion = "✅ ÓPTIMO";
            emoji = "⚡";
        } else if (eficiencia >= 70) {
            evaluacion = "⚠️  ACEPTABLE";
            emoji = "🔧";
        } else if (eficiencia >= 50) {
            evaluacion = "🔴 REQUIERE MEJORAS";
            emoji = "💥";
        } else {
            evaluacion = "💀 CRÍTICO";
            emoji = "🚨";
        }
        
        System.out.println("\n🎯 EVALUACIÓN DEL SISTEMA:");
        System.out.println("  • Estado: " + evaluacion + " " + emoji);
        System.out.println("  • Nivel de confianza: " + String.format("%.0f/100", eficiencia));
        
        if (eficiencia < 70) {
            System.out.println("  • Recomendación: Revisar configuración y logs del sistema");
        }
    }

    /**
     * 📊 MOSTRAR ESTADÍSTICAS AVANZADAS ULTRA TURBOFURULADAS
     */
    public void mostrarEstadisticasAvanzadasUltra() {
        double eficiencia = calcularEficienciaSistema();
        double tasaExito = calcularTasaExito();
        double eficienciaAPI = calcularEficienciaAPI();
        double productividad = calcularProductividad();
        
        System.out.println("\n" + "🚀".repeat(80));
        System.out.println("           ESTADÍSTICAS AVANZADAS ULTRA TURBOFURULADAS");
        System.out.println("🚀".repeat(80));
        
        System.out.println("📈 MÉTRICAS CLAVE:");
        System.out.println("  • Eficiencia del sistema: " + String.format("%.1f%%", eficiencia));
        System.out.println("  • Tasa de éxito operativo: " + String.format("%.1f%%", tasaExito));
        System.out.println("  • Eficiencia API: " + String.format("%.1f%%", eficienciaAPI));
        System.out.println("  • Productividad: " + String.format("%.2f clases/segundo", productividad));
        System.out.println("  • Densidad de mejoras: " + String.format("%.2f", calcularDensidadMejoras()));
        
        System.out.println("\n🎯 INDICADORES DE CALIDAD:");
        System.out.println("  • Ratio éxito/error: " + String.format("%.2f:1", 
            (double)getContadorIntegracionesExitosas() / Math.max(1, getContadorKrakens())));
        System.out.println("  • Efectividad análisis: " + String.format("%.1f%%", 
            (double)getContadorAnalisisExitosos() / Math.max(1, getContadorLlamadasAPI()) * 100));
        System.out.println("  • Velocidad procesamiento: " + String.format("%.0fms/clase", 
            (double)getTiempoTotalEjecucion() / Math.max(1, getContadorClasesProcesadas())));
    }

    /**
     * 📊 MOSTRAR ESTADÍSTICAS FINALES ÉPICAS TURBOFURULADAS
     */
    public void mostrarEstadisticasFinalesEpicas() {
        System.out.println("\n" + "🎊".repeat(100));
        System.out.println("           ESTADÍSTICAS FINALES TURBO FUSIÓN ULTRA - SISTEMA COMPLETO TURBOFURULADO");
        System.out.println("🎊".repeat(100));
        
        double eficiencia = calcularEficienciaSistema();
        double productividad = calcularProductividad();
        
        System.out.println("📊 RESUMEN FINAL TURBOFURULADO:");
        System.out.println("  🎯 Sesiones completadas: " + getContadorEjecuciones());
        System.out.println("  📦 Clases procesadas: " + getContadorClasesProcesadas());
        System.out.println("  ✅ Integraciones exitosas: " + getContadorIntegracionesExitosas());
        System.out.println("  💥 Krakens derrotados: " + getContadorKrakens());
        System.out.println("  🔮 Mejoras épicas activas: " + getContadorMejorasActivas());
        System.out.println("  🔍 Verificaciones realizadas: " + getContadorVerificaciones());
        System.out.println("  🔗 Llamadas API: " + getContadorLlamadasAPI());
        System.out.println("  📝 Refactors registrados: " + getContadorRefactorsRegistrados());
        System.out.println("  ⏱️  Tiempo total: " + getTiempoTotalEjecucion() + "ms");
        System.out.println("  🧠 Tokens utilizados: " + getTokensUtilizados());
        
        System.out.println("\n📈 MÉTRICAS DE EXCELENCIA:");
        System.out.println("  • Eficiencia del sistema: " + String.format("%.1f%%", eficiencia));
        System.out.println("  • Productividad: " + String.format("%.2f clases/segundo", productividad));
        System.out.println("  • Tasa de éxito: " + String.format("%.1f%%", calcularTasaExito()));
        System.out.println("  • Eficiencia API: " + String.format("%.1f%%", calcularEficienciaAPI()));
        
        // Evaluación final
        String evaluacionFinal = eficiencia >= 90 ? "🏆 ÉPICO" : 
                               eficiencia >= 75 ? "✅ ÓPTIMO" : 
                               eficiencia >= 60 ? "⚠️  SATISFACTORIO" : "🔴 MEJORABLE";
        
        System.out.println("\n🎯 EVALUACIÓN FINAL: " + evaluacionFinal);
        System.out.println("\n🏴‍☠️ ¡LA FUSIÓN TURBO ULTRA TURBOFURULADA HA TERMINADO SU TRAVESÍA ÉPICA!");
        System.out.println("🎊".repeat(100));
    }

    // =========================================================================
    // 📋 REPORTES Y EXPORTACIÓN TURBOFURULADOS
    // =========================================================================

    /**
     * 📊 GENERAR REPORTE DETALLADO TURBOFURULADO
     */
    public void generarReporteDetallado() {
        System.out.println("\n" + "📈".repeat(90));
        System.out.println("           REPORTE DETALLADO TURBOFURULADO - ANÁLISIS COMPLETO DEL SISTEMA");
        System.out.println("📈".repeat(90));
        
        mostrarEstadisticasCompletas();
        
        System.out.println("\n📋 ANÁLISIS POR CATEGORÍA:");
        System.out.println("  🎯 Ejecución: " + getContadorEjecuciones() + " sesiones completadas");
        System.out.println("  🔍 Procesamiento: " + getContadorClasesProcesadas() + " clases analizadas");
        System.out.println("  ⚡ Integración: " + getContadorIntegracionesExitosas() + " operaciones exitosas");
        System.out.println("  💥 Errores: " + getContadorKrakens() + " krakens enfrentados");
        System.out.println("  🔮 Mejoras: " + getContadorMejorasActivas() + " mejoras activas");
        System.out.println("  ✅ Verificaciones: " + getContadorVerificaciones() + " verificaciones realizadas");
        System.out.println("  🔗 API: " + getContadorLlamadasAPI() + " llamadas, " + getContadorAnalisisExitosos() + " exitosas");
        System.out.println("  📝 Refactors: " + getContadorRefactorsRegistrados() + " registrados");
        
        System.out.println("\n🎯 INDICADORES CLAVE TURBOFURULADOS:");
        System.out.println("  • Eficiencia: " + String.format("%.1f%%", calcularEficienciaSistema()));
        System.out.println("  • Tasa de éxito: " + String.format("%.1f%%", calcularTasaExito()));
        System.out.println("  • Densidad mejoras: " + String.format("%.2f", calcularDensidadMejoras()));
        System.out.println("  • Eficiencia API: " + String.format("%.1f%%", calcularEficienciaAPI()));
        System.out.println("  • Productividad: " + String.format("%.2f cls/s", calcularProductividad()));
        System.out.println("  • Velocidad: " + String.format("%.0fms/clase", 
            (double)getTiempoTotalEjecucion() / Math.max(1, getContadorClasesProcesadas())));
        
        // Mostrar contadores personalizados si existen
        if (!CONTADORES_PERSONALIZADOS.isEmpty()) {
            System.out.println("\n🔧 CONTADORES PERSONALIZADOS:");
            CONTADORES_PERSONALIZADOS.forEach((nombre, contador) -> {
                if (contador.get() > 0) {
                    System.out.println("  • " + nombre + ": " + contador.get());
                }
            });
        }
        
        mostrarEvaluacionSistema();
        System.out.println("📈".repeat(90));
    }

    /**
     * 📊 EXPORTAR DATOS EN FORMATO CSV TURBOFURULADO
     */
    public String exportarDatosCSV() {
        return String.format(
            "Timestamp,Ejecuciones,ClasesProcesadas,IntegracionesExitosas,Krakens,MejorasActivas,Verificaciones,LlamadasAPI,AnalisisExitosos,RefactorsRegistrados,TiempoTotalMs,TokensUtilizados,Eficiencia,TasaExito,EficienciaAPI,Productividad%n" +
            "%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%.2f,%.2f,%.2f,%.2f",
            LocalDateTime.now().format(FORMATTER),
            getContadorEjecuciones(),
            getContadorClasesProcesadas(),
            getContadorIntegracionesExitosas(),
            getContadorKrakens(),
            getContadorMejorasActivas(),
            getContadorVerificaciones(),
            getContadorLlamadasAPI(),
            getContadorAnalisisExitosos(),
            getContadorRefactorsRegistrados(),
            getTiempoTotalEjecucion(),
            getTokensUtilizados(),
            calcularEficienciaSistema(),
            calcularTasaExito(),
            calcularEficienciaAPI(),
            calcularProductividad()
        );
    }

    /**
     * 📊 EXPORTAR DATOS EN FORMATO JSON TURBOFURULADO
     */
    public String exportarDatosJSON() {
        JSONObject json = new JSONObject();
        
        // Datos básicos
        json.put("timestamp", LocalDateTime.now().format(FORMATTER));
        json.put("ejecuciones", getContadorEjecuciones());
        json.put("clasesProcesadas", getContadorClasesProcesadas());
        json.put("integracionesExitosas", getContadorIntegracionesExitosas());
        json.put("krakens", getContadorKrakens());
        json.put("mejorasActivas", getContadorMejorasActivas());
        json.put("verificaciones", getContadorVerificaciones());
        
        // Datos avanzados
        json.put("llamadasAPI", getContadorLlamadasAPI());
        json.put("analisisExitosos", getContadorAnalisisExitosos());
        json.put("refactorsRegistrados", getContadorRefactorsRegistrados());
        json.put("tiempoTotalMs", getTiempoTotalEjecucion());
        json.put("tokensUtilizados", getTokensUtilizados());
        
        // Métricas calculadas
        json.put("eficiencia", String.format("%.2f", calcularEficienciaSistema()));
        json.put("tasaExito", String.format("%.2f", calcularTasaExito()));
        json.put("eficienciaAPI", String.format("%.2f", calcularEficienciaAPI()));
        json.put("productividad", String.format("%.2f", calcularProductividad()));
        json.put("tiempoPromedioEjecucion", String.format("%.2f", calcularTiempoPromedioPorEjecucion()));
        
        // Contadores personalizados
        JSONObject personalizados = new JSONObject();
        CONTADORES_PERSONALIZADOS.forEach((k, v) -> personalizados.put(k, v.get()));
        json.put("contadoresPersonalizados", personalizados);
        
        // Snapshot actual
        json.put("snapshot", crearSnapshot().toJSON());
        
        return json.toString(4); // Indentación de 4 espacios
    }

    /**
     * 📊 GUARDAR REPORTE EN ARCHIVO
     */
    public void guardarReporteArchivo(String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("REPORTE TURBOFURULADO - " + LocalDateTime.now().format(FORMATTER));
            writer.println("=".repeat(80));
            writer.println(exportarDatosCSV());
            writer.println("\n" + exportarDatosJSON());
            System.out.println("✅ Reporte guardado en: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("❌ Error guardando reporte: " + e.getMessage());
        }
    }

    // =========================================================================
    // 🕐 SNAPSHOTS E HISTÓRICO TURBOFURULADOS
    // =========================================================================

    /**
     * 📊 CREAR SNAPSHOT DE CONTADORES
     */
    public ContadoresSnapshot crearSnapshot() {
        return new ContadoresSnapshot(
            getContadorEjecuciones(),
            getContadorClasesProcesadas(),
            getContadorKrakens(),
            getContadorIntegracionesExitosas(),
            getContadorMejorasActivas(),
            getContadorVerificaciones(),
            getContadorLlamadasAPI(),
            getContadorAnalisisExitosos(),
            getContadorRefactorsRegistrados(),
            getTiempoTotalEjecucion(),
            getTokensUtilizados(),
            new ConcurrentHashMap<>(getTodosContadoresPersonalizados())
        );
    }

    /**
     * 📊 TOMAR SNAPSHOT AUTOMÁTICO
     */
    private void tomarSnapshotAutomatico(String motivo) {
        ContadoresSnapshot snapshot = crearSnapshot();
        snapshot.setMotivo(motivo);
        HISTORICO_SNAPSHOTS.add(snapshot);
        
        // Limitar el tamaño del histórico
        if (HISTORICO_SNAPSHOTS.size() > MAX_HISTORICO) {
            HISTORICO_SNAPSHOTS.remove(0);
        }
        
        if (modoVerbose) {
            System.out.println("📸 Snapshot tomado: " + motivo);
        }
    }

    /**
     * 📊 TOMAR SNAPSHOT MANUAL
     */
    public void tomarSnapshot(String motivo) {
        tomarSnapshotAutomatico(motivo);
    }

    /**
     * 📊 RESTAURAR DESDE SNAPSHOT
     */
    public void restaurarDesdeSnapshot(ContadoresSnapshot snapshot) {
        CONTADOR_EJECUCIONES.set(snapshot.ejecuciones);
        CONTADOR_CLASES_PROCESADAS.set(snapshot.clasesProcesadas);
        CONTADOR_KRAKENS.set(snapshot.krakens);
        CONTADOR_INTEGRACIONES_EXITOSAS.set(snapshot.integracionesExitosas);
        CONTADOR_MEJORAS_ACTIVAS.set(snapshot.mejorasActivas);
        CONTADOR_VERIFICACIONES.set(snapshot.verificaciones);
        CONTADOR_LLAMADAS_API.set(snapshot.llamadasAPI);
        CONTADOR_ANALISIS_EXITOSOS.set(snapshot.analisisExitosos);
        CONTADOR_REFACTORS_REGISTRADOS.set(snapshot.refactorsRegistrados);
        CONTADOR_TIEMPO_TOTAL_MS.set((int) snapshot.tiempoTotalMs);
        CONTADOR_TOKENS_UTILIZADOS.set(snapshot.tokensUtilizados);
        
        // Restaurar contadores personalizados
        snapshot.contadoresPersonalizados.forEach((k, v) -> 
            CONTADORES_PERSONALIZADOS.computeIfAbsent(k, key -> new AtomicInteger(0)).set(v)
        );
        
        tomarSnapshotAutomatico("RESTAURADO_DESDE_SNAPSHOT");
    }

    /**
     * 📊 OBTENER HISTÓRICO DE SNAPSHOTS
     */
    public List<ContadoresSnapshot> getHistoricoSnapshots() {
        return new ArrayList<>(HISTORICO_SNAPSHOTS);
    }

    /**
     * 📊 MOSTRAR TENDENCIAS DEL HISTÓRICO
     */
    public void mostrarTendencias() {
        if (HISTORICO_SNAPSHOTS.size() < 2) {
            System.out.println("📊 No hay suficientes snapshots para mostrar tendencias");
            return;
        }

        ContadoresSnapshot primero = HISTORICO_SNAPSHOTS.get(0);
        ContadoresSnapshot ultimo = HISTORICO_SNAPSHOTS.get(HISTORICO_SNAPSHOTS.size() - 1);

        System.out.println("\n📈 TENDENCIAS DEL SISTEMA:");
        System.out.println("  • Ejecuciones: " + primero.ejecuciones + " → " + ultimo.ejecuciones + " (" + (ultimo.ejecuciones - primero.ejecuciones) + ")");
        System.out.println("  • Clases procesadas: " + primero.clasesProcesadas + " → " + ultimo.clasesProcesadas + " (" + (ultimo.clasesProcesadas - primero.clasesProcesadas) + ")");
        System.out.println("  • Integraciones exitosas: " + primero.integracionesExitosas + " → " + ultimo.integracionesExitosas + " (" + (ultimo.integracionesExitosas - primero.integracionesExitosas) + ")");
        System.out.println("  • Krakens: " + primero.krakens + " → " + ultimo.krakens + " (" + (ultimo.krakens - primero.krakens) + ")");
        System.out.println("  • Llamadas API: " + primero.llamadasAPI + " → " + ultimo.llamadasAPI + " (" + (ultimo.llamadasAPI - primero.llamadasAPI) + ")");
        
        // Calcular eficiencia inicial vs final
        double eficienciaInicial = primero.calcularEficiencia();
        double eficienciaFinal = ultimo.calcularEficiencia();
        System.out.println("  • Eficiencia: " + String.format("%.1f%%", eficienciaInicial) + " → " + 
                          String.format("%.1f%%", eficienciaFinal) + " (" + 
                          String.format("%+.1f%%", eficienciaFinal - eficienciaInicial) + ")");
    }

    /**
     * 📊 COMPARAR CON SNAPSHOT ANTERIOR
     */
    public String compararConSnapshot(ContadoresSnapshot anterior) {
        ContadoresSnapshot actual = crearSnapshot();
        
        StringBuilder comparacion = new StringBuilder();
        comparacion.append("\n📊 COMPARACIÓN CON SNAPSHOT ANTERIOR:\n");
        
        comparacion.append(String.format("  • Ejecuciones: %d → %d (%+d)%n", 
            anterior.ejecuciones, actual.ejecuciones, actual.ejecuciones - anterior.ejecuciones));
        
        comparacion.append(String.format("  • Clases procesadas: %d → %d (%+d)%n", 
            anterior.clasesProcesadas, actual.clasesProcesadas, actual.clasesProcesadas - anterior.clasesProcesadas));
        
        comparacion.append(String.format("  • Integraciones exitosas: %d → %d (%+d)%n", 
            anterior.integracionesExitosas, actual.integracionesExitosas, actual.integracionesExitosas - anterior.integracionesExitosas));
        
        comparacion.append(String.format("  • Krakens: %d → %d (%+d)%n", 
            anterior.krakens, actual.krakens, actual.krakens - anterior.krakens));
        
        comparacion.append(String.format("  • Mejoras activas: %d → %d (%+d)%n", 
            anterior.mejorasActivas, actual.mejorasActivas, actual.mejorasActivas - anterior.mejorasActivas));
        
        comparacion.append(String.format("  • Verificaciones: %d → %d (%+d)%n", 
            anterior.verificaciones, actual.verificaciones, actual.verificaciones - anterior.verificaciones));
        
        comparacion.append(String.format("  • Llamadas API: %d → %d (%+d)%n", 
            anterior.llamadasAPI, actual.llamadasAPI, actual.llamadasAPI - anterior.llamadasAPI));
        
        comparacion.append(String.format("  • Análisis exitosos: %d → %d (%+d)%n", 
            anterior.analisisExitosos, actual.analisisExitosos, actual.analisisExitosos - anterior.analisisExitosos));
        
        // Comparar eficiencia
        double eficienciaAnterior = anterior.calcularEficiencia();
        double eficienciaActual = actual.calcularEficiencia();
        comparacion.append(String.format("  • Eficiencia: %.1f%% → %.1f%% (%+.1f%%)%n", 
            eficienciaAnterior, eficienciaActual, eficienciaActual - eficienciaAnterior));
        
        return comparacion.toString();
    }

    // =========================================================================
    // 📊 CLASE SNAPSHOT TURBOFURULADA
    // =========================================================================

    /**
     * 📊 CLASE INTERNA PARA SNAPSHOT DE CONTADORES TURBOFURULADA
     */
    public static class ContadoresSnapshot {
        public final int ejecuciones;
        public final int clasesProcesadas;
        public final int krakens;
        public final int integracionesExitosas;
        public final int mejorasActivas;
        public final int verificaciones;
        public final int llamadasAPI;
        public final int analisisExitosos;
        public final int refactorsRegistrados;
        public final long tiempoTotalMs;
        public final int tokensUtilizados;
        public final Map<String, Integer> contadoresPersonalizados;
        public final LocalDateTime timestamp;
        private String motivo;

        public ContadoresSnapshot(int ejecuciones, int clasesProcesadas, int krakens, 
                                int integracionesExitosas, int mejorasActivas, int verificaciones) {
            this(ejecuciones, clasesProcesadas, krakens, integracionesExitosas, mejorasActivas, 
                 verificaciones, 0, 0, 0, 0, 0, new ConcurrentHashMap<>());
        }

        public ContadoresSnapshot(int ejecuciones, int clasesProcesadas, int krakens,
                                int integracionesExitosas, int mejorasActivas, int verificaciones,
                                int llamadasAPI, int analisisExitosos, int refactorsRegistrados,
                                long tiempoTotalMs, int tokensUtilizados, 
                                Map<String, Integer> contadoresPersonalizados) {
            this.ejecuciones = ejecuciones;
            this.clasesProcesadas = clasesProcesadas;
            this.krakens = krakens;
            this.integracionesExitosas = integracionesExitosas;
            this.mejorasActivas = mejorasActivas;
            this.verificaciones = verificaciones;
            this.llamadasAPI = llamadasAPI;
            this.analisisExitosos = analisisExitosos;
            this.refactorsRegistrados = refactorsRegistrados;
            this.tiempoTotalMs = tiempoTotalMs;
            this.tokensUtilizados = tokensUtilizados;
            this.contadoresPersonalizados = new ConcurrentHashMap<>(contadoresPersonalizados);
            this.timestamp = LocalDateTime.now();
            this.motivo = "MANUAL";
        }

        public void setMotivo(String motivo) {
            this.motivo = motivo;
        }

        public String getMotivo() {
            return motivo;
        }

        public double calcularEficiencia() {
            if (clasesProcesadas == 0) return 0.0;
            return (1 - (double) krakens / clasesProcesadas) * 100;
        }

        public JSONObject toJSON() {
            JSONObject json = new JSONObject();
            json.put("timestamp", timestamp.format(FORMATTER));
            json.put("motivo", motivo);
            json.put("ejecuciones", ejecuciones);
            json.put("clasesProcesadas", clasesProcesadas);
            json.put("krakens", krakens);
            json.put("integracionesExitosas", integracionesExitosas);
            json.put("mejorasActivas", mejorasActivas);
            json.put("verificaciones", verificaciones);
            json.put("llamadasAPI", llamadasAPI);
            json.put("analisisExitosos", analisisExitosos);
            json.put("refactorsRegistrados", refactorsRegistrados);
            json.put("tiempoTotalMs", tiempoTotalMs);
            json.put("tokensUtilizados", tokensUtilizados);
            json.put("contadoresPersonalizados", new JSONObject(contadoresPersonalizados));
            json.put("eficiencia", String.format("%.2f", calcularEficiencia()));
            return json;
        }

        @Override
        public String toString() {
            return String.format(
                "Snapshot[%s] Ejecuciones=%d, Clases=%d, Krakens=%d, Integraciones=%d, API=%d, Eficiencia=%.1f%%",
                motivo, ejecuciones, clasesProcesadas, krakens, integracionesExitosas, llamadasAPI, calcularEficiencia()
            );
        }
    }

    /**
     * 📊 OBTENER ÚLTIMO SNAPSHOT
     */
    public ContadoresSnapshot getUltimoSnapshot() {
        if (HISTORICO_SNAPSHOTS.isEmpty()) {
            return crearSnapshot();
        }
        return HISTORICO_SNAPSHOTS.get(HISTORICO_SNAPSHOTS.size() - 1);
    }

    /**
     * 📊 OBTENER SNAPSHOT MÁS EXITOSO
     */
    public ContadoresSnapshot getSnapshotMasExitoso() {
        return HISTORICO_SNAPSHOTS.stream()
            .max(Comparator.comparingDouble(ContadoresSnapshot::calcularEficiencia))
            .orElse(getUltimoSnapshot());
    }

    // =========================================================================
    // 🎯 MÉTODOS DE UTILIDAD TURBOFURULADOS
    // =========================================================================

    /**
     * 📊 VERIFICAR ESTADO DEL SISTEMA
     */
    public String verificarEstadoSistema() {
        double eficiencia = calcularEficienciaSistema();
        double tasaExito = calcularTasaExito();
        
        if (eficiencia >= 90 && tasaExito >= 80) {
            return "🏆 SISTEMA ÓPTIMO";
        } else if (eficiencia >= 75 && tasaExito >= 60) {
            return "✅ SISTEMA ESTABLE";
        } else if (eficiencia >= 50 && tasaExito >= 40) {
            return "⚠️  SISTEMA ACEPTABLE";
        } else {
            return "🔴 SISTEMA REQUIERE ATENCIÓN";
        }
    }

    /**
     * 📊 GENERAR RECOMENDACIONES
     */
    public List<String> generarRecomendaciones() {
        List<String> recomendaciones = new ArrayList<>();
        double eficiencia = calcularEficienciaSistema();
        double tasaExito = calcularTasaExito();
        double eficienciaAPI = calcularEficienciaAPI();

        if (eficiencia < 70) {
            recomendaciones.add("🔧 Revisar logs de errores para reducir krakens");
        }
        
        if (tasaExito < 60) {
            recomendaciones.add("⚡ Optimizar proceso de integración del sistema");
        }
        
        if (eficienciaAPI < 80) {
            recomendaciones.add("🔗 Mejorar manejo de llamadas a API externas");
        }
        
        if (getContadorKrakens() > getContadorIntegracionesExitosas()) {
            recomendaciones.add("💥 Implementar mejor manejo de excepciones");
        }
        
        if (getTiempoTotalEjecucion() > 60000 && getContadorClasesProcesadas() < 10) {
            recomendaciones.add("⏱️  Optimizar rendimiento del análisis");
        }

        if (recomendaciones.isEmpty()) {
            recomendaciones.add("🎯 El sistema funciona de manera óptima, mantener configuración actual");
        }

        return recomendaciones;
    }

    /**
     * 📊 MOSTRAR PANEL DE CONTROL TURBOFURULADO
     */
    public void mostrarPanelControl() {
        System.out.println("\n" + "🎛️ ".repeat(25));
        System.out.println("           PANEL DE CONTROL TURBOFURULADO - MONITOREO EN TIEMPO REAL");
        System.out.println("🎛️ ".repeat(25));
        
        System.out.println(obtenerResumenRapido());
        System.out.println("🎯 Estado: " + verificarEstadoSistema());
        
        System.out.println("\n📈 MÉTRICAS EN TIEMPO REAL:");
        System.out.println("  • Eficiencia: " + String.format("%.1f%%", calcularEficienciaSistema()));
        System.out.println("  • Tasa éxito: " + String.format("%.1f%%", calcularTasaExito()));
        System.out.println("  • Productividad: " + String.format("%.2f cls/s", calcularProductividad()));
        System.out.println("  • Snapshots: " + HISTORICO_SNAPSHOTS.size() + " en historial");
        
        System.out.println("\n💡 RECOMENDACIONES:");
        generarRecomendaciones().forEach(rec -> System.out.println("  • " + rec));
        
        System.out.println("🎛️ ".repeat(25));
    }
}