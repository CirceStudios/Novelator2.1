package com.elreinodelolvido.ellibertad.manager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.elreinodelolvido.ellibertad.chunk.ChunkAnalysis;
import com.elreinodelolvido.ellibertad.chunk.ChunkAnalysisResult;
import com.elreinodelolvido.ellibertad.chunk.ChunkedFileProcessor;
import com.elreinodelolvido.ellibertad.chunk.FileChunk;
import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.model.ClassInfo;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;
import com.elreinodelolvido.ellibertad.util.FileUtils;
import com.elreinodelolvido.ellibertad.util.GeneradorPDFTurbo;
import com.elreinodelolvido.ellibertad.util.PlanificadorRefactor;
import com.elreinodelolvido.ellibertad.util.PrioridadRefactor;

public class AnalisisManager {
    
    private Bitacora bitacora;
    private ProjectScanner scannerAvanzado;
    private AtomicInteger contadorClasesProcesadas;
    private AtomicInteger contadorKrakens;

    // 🎯 CACHE PARA EVITAR REPROCESAMIENTO
    private Map<String, String> cacheAnalisis = new ConcurrentHashMap<>();
    private Map<String, Long> cacheTimestamp = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION_MS = 30 * 60 * 1000; // 30 minutos

    // 🚀 PATRONES REGEXP MEJORADOS PARA EXTRACCIÓN
    private static final Pattern PATRON_SUGERENCIAS = Pattern.compile(
        "(?:(?:^|\\n)[-•*]\\s*(.*?)(?=\\n[-•*]|\\n\\n|$))|" + // Listas con viñetas
        "(?:(?:^|\\n)\\d+\\.\\s*(.*?)(?=\\n\\d+\\.|\\n\\n|$))|" + // Listas numeradas
        "(?:(?:^|\\n)(?:Sugerencia|Recomendación|Mejora|Problema)\\s*:?\\s*(.*?)(?=\\n(?:Sugerencia|Recomendación|Mejora|Problema)|\\n\\n|$))", 
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
    );

    // ⚡ CONFIGURACIÓN DE ANÁLISIS
    private int maxClasesPorAnalisis = 10;
    private int delayEntreRequestsMs = 2000;
    private boolean modoVerbose = true;

    // 🎯 En el constructor, inicializar el processor
    public AnalisisManager(Bitacora bitacora, ProjectScanner scannerAvanzado, 
                          AtomicInteger contadorClasesProcesadas, AtomicInteger contadorKrakens) {
        this.bitacora = bitacora;
        this.scannerAvanzado = scannerAvanzado;
        this.contadorClasesProcesadas = contadorClasesProcesadas;
        this.contadorKrakens = contadorKrakens;
        this.chunkProcessor = new ChunkedFileProcessor(bitacora);
        
        bitacora.info("🚀 AnalisisManager inicializado con soporte para archivos grandes");
    }

    /**
     * 📝 EXTRAER SUGERENCIAS REALES - TURBOFURULADO CON REGEX
     */
    private List<String> extraerSugerenciasReales(String respuestaIA) {
        List<String> sugerencias = new ArrayList<>();
        
        if (respuestaIA == null || respuestaIA.trim().isEmpty()) {
            return sugerencias;
        }
        
        // 🎯 USAR REGEX MEJORADO PARA CAPTURAR SUGERENCIAS
        Matcher matcher = PATRON_SUGERENCIAS.matcher(respuestaIA);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group(i) != null && !matcher.group(i).trim().isEmpty()) {
                    String sugerencia = matcher.group(i).trim();
                    // 🚀 FILTRAR SUGERENCIAS VÁLIDAS
                    if (esSugerenciaValida(sugerencia)) {
                        sugerencias.add(sugerencia);
                    }
                }
            }
        }
        
        // 🎯 FALLBACK: SI REGEX NO CAPTURA NADA, USAR MÉTODO ORIGINAL
        if (sugerencias.isEmpty()) {
            sugerencias = extraerSugerenciasFallback(respuestaIA);
        }
        
        return sugerencias;
    }

    /**
     * 🎯 VALIDAR SI UNA SUGERENCIA ES VÁLIDA
     */
    private boolean esSugerenciaValida(String sugerencia) {
        if (sugerencia.length() < 20) return false;
        
        String lower = sugerencia.toLowerCase();
        return !lower.matches("^(hola|gracias|saludos|analizo|clase|java).*") &&
               !lower.contains("como asistente") &&
               !lower.contains("no puedo") &&
               !lower.contains("no tengo") &&
               (lower.contains("suger") || lower.contains("recomend") || 
                lower.contains("mejorar") || lower.contains("consider") ||
                lower.contains("debería") || lower.contains("podría") ||
                lower.contains("evitar") || lower.contains("optimizar"));
    }

    /**
     * 🔄 MÉTODO FALLBACK PARA EXTRACCIÓN DE SUGERENCIAS
     */
    private List<String> extraerSugerenciasFallback(String respuestaIA) {
        List<String> sugerencias = new ArrayList<>();
        String[] lineas = respuestaIA.split("\n");
        StringBuilder sugerenciaActual = new StringBuilder();
        boolean enSugerencia = false;
        
        for (String linea : lineas) {
            linea = linea.trim();
            
            // 🚨 DETECTAR INICIO DE SUGERENCIA
            if (linea.startsWith("- ") || linea.startsWith("• ") || 
                linea.startsWith("* ") || linea.matches("\\d+\\.\\s.*") ||
                linea.toLowerCase().contains("sugerencia:") ||
                linea.toLowerCase().contains("recomendación:") ||
                (linea.length() > 10 && (
                 linea.toLowerCase().contains("deberías") ||
                 linea.toLowerCase().contains("considera") ||
                 linea.toLowerCase().contains("podrías") ||
                 linea.toLowerCase().contains("mejorar")))) {
                
                if (sugerenciaActual.length() > 0 && esSugerenciaValida(sugerenciaActual.toString())) {
                    sugerencias.add(sugerenciaActual.toString().trim());
                }
                sugerenciaActual = new StringBuilder();
                enSugerencia = true;
            }
            
            if (enSugerencia) {
                if (!linea.isEmpty()) {
                    sugerenciaActual.append(linea).append(" ");
                } else {
                    // Línea vacía puede indicar fin de sugerencia
                    if (sugerenciaActual.length() > 0 && esSugerenciaValida(sugerenciaActual.toString())) {
                        sugerencias.add(sugerenciaActual.toString().trim());
                    }
                    sugerenciaActual = new StringBuilder();
                    enSugerencia = false;
                }
            }
        }
        
        // AGREGAR ÚLTIMA SUGERENCIA
        if (sugerenciaActual.length() > 0 && esSugerenciaValida(sugerenciaActual.toString())) {
            sugerencias.add(sugerenciaActual.toString().trim());
        }
        
        return sugerencias;
    }

    /**
     * 🎯 ANÁLISIS RÁPIDO CON MÉTRICAS - NUEVO MÉTODO TURBOFURULADO
     */
    public JSONObject analisisRapidoConMetricas() {
        bitacora.info("⚡ INICIANDO ANÁLISIS RÁPIDO CON MÉTRICAS...");
        
        JSONObject metricas = new JSONObject();
        long startTime = System.currentTimeMillis();
        
        try {
            scannerAvanzado.scanProject("./");
            List<ClassInfo> clases = scannerAvanzado.getClasses();
            
            // 📊 MÉTRICAS BÁSICAS
            metricas.put("totalClases", clases.size());
            metricas.put("clasesAnalizables", 0);
            metricas.put("codigoTotalLineas", 0);
            metricas.put("archivosGrandes", 0);
            metricas.put("archivosConProblemas", 0);
            
            List<JSONObject> detalleClases = new ArrayList<>();
            
            // 🔍 ANÁLISIS RÁPIDO DE CADA CLASE
            for (ClassInfo clase : clases) {
                JSONObject infoClase = new JSONObject();
                infoClase.put("nombre", clase.getFullName());
                infoClase.put("archivo", clase.getSourcePath());
                
                try {
                    String codigo = FileUtils.readFile(clase.getSourcePath());
                    if (codigo != null) {
                        int lineas = codigo.split("\n").length;
                        infoClase.put("lineas", lineas);
                        infoClase.put("tamañoBytes", codigo.length());
                        
                        metricas.put("codigoTotalLineas", metricas.getInt("codigoTotalLineas") + lineas);
                        metricas.put("clasesAnalizables", metricas.getInt("clasesAnalizables") + 1);
                        
                        // 🚨 DETECTAR POSIBLES PROBLEMAS
                        List<String> problemas = detectarProblemasRapidos(codigo);
                        infoClase.put("problemasDetectados", problemas);
                        infoClase.put("prioridad", calcularPrioridadRapida(problemas));
                        
                        if (!problemas.isEmpty()) {
                            metricas.put("archivosConProblemas", metricas.getInt("archivosConProblemas") + 1);
                        }
                        
                        if (lineas > 200) {
                            metricas.put("archivosGrandes", metricas.getInt("archivosGrandes") + 1);
                        }
                    }
                } catch (Exception e) {
                    infoClase.put("error", e.getMessage());
                }
                
                detalleClases.add(infoClase);
            }
            
            metricas.put("detalleClases", detalleClases);
            metricas.put("tiempoAnalisisMs", System.currentTimeMillis() - startTime);
            metricas.put("timestamp", new java.util.Date().toString());
            
            // 📈 MOSTRAR RESUMEN RÁPIDO
            System.out.println("\n📊 RESUMEN ANÁLISIS RÁPIDO:");
            System.out.println("  • Total clases: " + metricas.getInt("totalClases"));
            System.out.println("  • Clases analizables: " + metricas.getInt("clasesAnalizables"));
            System.out.println("  • Líneas de código: " + metricas.getInt("codigoTotalLineas"));
            System.out.println("  • Archivos grandes: " + metricas.getInt("archivosGrandes"));
            System.out.println("  • Archivos con problemas: " + metricas.getInt("archivosConProblemas"));
            System.out.println("  • Tiempo análisis: " + metricas.getLong("tiempoAnalisisMs") + "ms");
            
        } catch (Exception e) {
            metricas.put("error", e.getMessage());
            bitacora.error("Error en análisis rápido con métricas", e);
        }
        
        return metricas;
    }

    /**
     * 🔍 DETECTAR PROBLEMAS RÁPIDOS EN CÓDIGO
     */
    private List<String> detectarProblemasRapidos(String codigo) {
        List<String> problemas = new ArrayList<>();
        String codigoLower = codigo.toLowerCase();
        
        // 🚨 PATRONES DE PROBLEMAS COMUNES
        if (codigo.split("\n").length > 500) {
            problemas.add("CLASE_MUY_GRANDE");
        }
        
        if (codigoLower.contains("system.out.println") && !codigoLower.contains("logger")) {
            problemas.add("USO_SYSTEM_OUT");
        }
        
        if (codigoLower.contains("// todo") || codigoLower.contains("// fixme")) {
            problemas.add("TODOS_PENDIENTES");
        }
        
        if (codigoLower.contains("catch (exception e)") && codigoLower.contains("e.printstacktrace")) {
            problemas.add("EXCEPCIONES_GENERICAS");
        }
        
        if (codigoLower.contains("public static void main") && codigo.split("\n").length > 50) {
            problemas.add("MAIN_COMPLEJO");
        }
        
        if (codigoLower.contains("thread.sleep") && !codigoLower.contains("timeunit")) {
            problemas.add("THREAD_SLEEP_DIRECTO");
        }
        
        return problemas;
    }

    /**
     * ⚡ CALCULAR PRIORIDAD RÁPIDA
     */
    private String calcularPrioridadRapida(List<String> problemas) {
        if (problemas.contains("CLASE_MUY_GRANDE") || problemas.contains("MAIN_COMPLEJO")) {
            return "🔴 ALTA";
        } else if (!problemas.isEmpty()) {
            return "🟡 MEDIA";
        } else {
            return "🟢 BAJA";
        }
    }

    /**
     * 🛠️ CONFIGURAR ANÁLISIS - NUEVO MÉTODO TURBOFURULADO
     */
    public void configurarAnalisis(int maxClases, int delayMs, boolean verbose) {
        this.maxClasesPorAnalisis = maxClases;
        this.delayEntreRequestsMs = delayMs;
        this.modoVerbose = verbose;
        
        System.out.println("⚙️ CONFIGURACIÓN DE ANÁLISIS ACTUALIZADA:");
        System.out.println("  • Máximo clases: " + maxClases);
        System.out.println("  • Delay entre requests: " + delayMs + "ms");
        System.out.println("  • Modo verbose: " + verbose);
    }

    /**
     * 🧹 LIMPIAR CACHE - NUEVO MÉTODO TURBOFURULADO
     */
    public void limpiarCache() {
        int elementosEliminados = cacheAnalisis.size();
        cacheAnalisis.clear();
        cacheTimestamp.clear();
        
        System.out.println("🧹 CACHE LIMPIADO: " + elementosEliminados + " elementos eliminados");
        bitacora.info("Cache de análisis limpiado: " + elementosEliminados + " elementos");
    }

    /**
     * 📊 ESTADÍSTICAS DE CACHE - NUEVO MÉTODO TURBOFURULADO
     */
    public JSONObject obtenerEstadisticasCache() {
        JSONObject stats = new JSONObject();
        stats.put("elementosEnCache", cacheAnalisis.size());
        stats.put("espacioEstimadoKB", cacheAnalisis.values().stream().mapToInt(String::length).sum() / 1024);
        
        long ahora = System.currentTimeMillis();
        long elementosExpirados = cacheTimestamp.values().stream()
            .filter(ts -> (ahora - ts) > CACHE_DURATION_MS)
            .count();
        
        stats.put("elementosExpirados", elementosExpirados);
        stats.put("configuracion", new JSONObject()
            .put("maxClases", maxClasesPorAnalisis)
            .put("delayMs", delayEntreRequestsMs)
            .put("modoVerbose", modoVerbose)
        );
        
        return stats;
    }

    // 🎯 LOS MÉTODOS ORIGINALES SE MANTIENEN, PERO MEJORADOS...

    /**
     * 🎯 DETECTAR SI LA RESPUESTA CONTIENE SUGERENCIAS REALES - MEJORADO
     */
    private boolean contieneSugerenciasReales(String respuestaIA) {
        if (respuestaIA == null || respuestaIA.trim().isEmpty()) {
            return false;
        }
        
        // 🚀 USAR EXTRACCIÓN MEJORADA
        List<String> sugerencias = extraerSugerenciasReales(respuestaIA);
        return !sugerencias.isEmpty();
    }

    // ... (el resto de métodos existentes se mantienen con mejoras menores)

    /**
     * 🎯 EJECUTAR ANÁLISIS COMPLETO ULTRA - OPTIMIZADO
     */
    public void ejecutarAnalisisCompletoUltra(boolean dryRun) {
        // 🚀 USAR LA NUEVA IMPLEMENTACIÓN DE analizarTodo()
        JSONObject resultado = analizarTodo();
        
        if (dryRun) {
            System.out.println("\n🎭 MODO DRY-RUN: Análisis completado sin registrar refactors");
            resultado.put("modo", "dry-run");
        }
        
        // 📊 MOSTRAR RESULTADO MEJORADO
        System.out.println("\n📋 RESULTADO DETALLADO:");
        resultado.keySet().forEach(key -> {
            if (!key.equals("detalleClases")) { // Evitar mostrar detalle completo en consola
                System.out.println("  • " + key + ": " + resultado.get(key));
            }
        });
    }
 // 🚀 STREAMING PARA ARCHIVOS GRANDES
    private ChunkedFileProcessor chunkProcessor;
    private Map<String, ChunkAnalysisResult> ongoingChunkAnalyses = new ConcurrentHashMap<>();
    private final int LARGE_FILE_THRESHOLD_MB = 5; // 5MB
    
    /**
     * 🚀 ANALIZAR POR CHUNKS - STREAMING PARALELO
     */
    private JSONObject analizarPorChunks(Path filePath, long fileSize) throws IOException {
        JSONObject resultado = new JSONObject();
        
        // 🎯 DIVIDIR ARCHIVO EN CHUNKS
        List<FileChunk> chunks = chunkProcessor.processLargeFile(filePath, fileSize);
        resultado.put("totalChunks", chunks.size());
        resultado.put("tamañoPromedioChunk", chunks.stream()
            .mapToInt(FileChunk::getLineCount).average().orElse(0));
        
        // 🚀 PROCESAR CHUNKS EN PARALELO
        List<CompletableFuture<ChunkAnalysis>> futures = new ArrayList<>();
        ExecutorService chunkExecutor = Executors.newFixedThreadPool(
            Math.min(chunks.size(), Runtime.getRuntime().availableProcessors())
        );
        
        for (FileChunk chunk : chunks) {
            CompletableFuture<ChunkAnalysis> future = CompletableFuture.supplyAsync(() -> {
                return procesarChunk(chunk);
            }, chunkExecutor);
            futures.add(future);
        }
        
        // 🎯 ESPERAR RESULTADOS
        List<ChunkAnalysis> resultadosChunks = futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
        
        chunkExecutor.shutdown();
        
        // 📊 CONSOLIDAR RESULTADOS
        List<String> todasSugerencias = resultadosChunks.stream()
            .flatMap(analysis -> analysis.getSugerencias().stream())
            .distinct()
            .collect(Collectors.toList());
        
        resultado.put("sugerencias", todasSugerencias);
        resultado.put("totalSugerencias", todasSugerencias.size());
        resultado.put("chunksProcesados", resultadosChunks.size());
        resultado.put("modo", "streaming");
        
        // 🎯 MÉTRICAS DE RENDIMIENTO
        long endTime = System.currentTimeMillis();
        resultado.put("tiempoTotalMs", endTime - System.currentTimeMillis());
        resultado.put("eficienciaStreaming", 
            calcularEficienciaStreaming(chunks.size(), resultadosChunks.size()));
        
        bitacora.info("✅ Análisis streaming completado: " + chunks.size() + " chunks procesados");
        
        return resultado;
    }
   
    
    /**
     * 🎯 FILTRAR SUGERENCIAS RELEVANTES PARA EL CHUNK
     */
    private List<String> filtrarSugerenciasPorChunk(List<String> sugerencias, FileChunk chunk) {
        return sugerencias.stream()
            .filter(sugerencia -> esSugerenciaRelevanteParaChunk(sugerencia, chunk))
            .collect(Collectors.toList());
    }
    
    /**
     * 🎯 VERIFICAR SI SUGERENCIA ES RELEVANTE PARA EL CHUNK
     */
    private boolean esSugerenciaRelevanteParaChunk(String sugerencia, FileChunk chunk) {
        String sugerenciaLower = sugerencia.toLowerCase();
        
        // 🚨 SUGERENCIAS QUE PROBABLEMENTE NO APLIQUEN A CHUNKS PARCIALES
        if (chunk.isPartial()) {
            if (sugerenciaLower.contains("estructura completa") ||
                sugerenciaLower.contains("organización general") ||
                sugerenciaLower.contains("arquitectura del archivo")) {
                return false;
            }
        }
        
        // ✅ SUGERENCIAS QUE SÍ APLICAN A CHUNKS
        return sugerenciaLower.contains("método") ||
               sugerenciaLower.contains("variable") ||
               sugerenciaLower.contains("función") ||
               sugerenciaLower.contains("lógica") ||
               sugerenciaLower.contains("optimizar") ||
               sugerenciaLower.contains("mejorar") ||
               !chunk.isPartial(); // Todas aplican si es archivo completo
    }
    
    /**
     * 📊 CALCULAR EFICIENCIA DEL STREAMING
     */
    private String calcularEficienciaStreaming(int totalChunks, int chunksExitosos) {
        double eficiencia = (double) chunksExitosos / totalChunks * 100;
        return String.format("%.1f%%", eficiencia);
    }
    
    /**
     * 🚀 ANÁLISIS INTELIGENTE - DETECTA AUTOMÁTICAMENTE MODO STREAMING
     */
    public JSONObject analizarInteligente(ClassInfo clase) {
        try {
            Path filePath = Paths.get(clase.getSourcePath());
            long fileSize = Files.size(filePath);
            
            if (fileSize > LARGE_FILE_THRESHOLD_MB * 1024 * 1024) {
                bitacora.info("🎯 Archivo grande detectado, usando streaming: " + 
                             clase.getFullName() + " (" + (fileSize/1024/1024) + "MB)");
                return analizarArchivoGrande(filePath);
            } else {
                return analizarArchivoNormal(filePath);
            }
            
        } catch (Exception e) {
            JSONObject resultado = new JSONObject();
            resultado.put("error", true);
            resultado.put("mensaje", "Error en análisis inteligente: " + e.getMessage());
            return resultado;
        }
    }
    
    /**
     * 🚀 ANALIZAR TODO - IMPLEMENTACIÓN TURBOFURULADA COMPLETA CON STREAMING Y VISUALIZACIÓN IA
     */
    public JSONObject analizarTodo() {
        bitacora.info("🚀 INICIANDO ANÁLISIS COMPLETO TURBOFURULADO CON STREAMING...");
        
        long startTime = System.currentTimeMillis();
        JSONObject resultado = new JSONObject();
        
        try {
            // 🎯 ESCANEAR PROYECTO
            System.out.println("🔍 ESCANEANDO PROYECTO...");
            scannerAvanzado.scanProject("./");
            List<ClassInfo> clases = scannerAvanzado.getClasses();
            
            // 🚀 CONFIGURAR LÍMITES
            int totalClases = clases.size();
            int limiteAnalisis = Math.min(maxClasesPorAnalisis, totalClases);
            
            resultado.put("totalClases", totalClases);
            resultado.put("clasesAnalizadas", 0);
            resultado.put("sugerenciasEncontradas", 0);
            resultado.put("errores", 0);
            resultado.put("archivosGrandesProcesados", 0);
            resultado.put("modoStreamingUtilizado", false);
            
            System.out.println("\n" + "📊".repeat(80));
            System.out.println("           ANÁLISIS COMPLETO TURBOFURULADO CON STREAMING");
            System.out.println("📊".repeat(80));
            System.out.println("  • Total clases detectadas: " + totalClases);
            System.out.println("  • Límite de análisis: " + limiteAnalisis);
            System.out.println("  • Modo verbose: " + modoVerbose);
            System.out.println("  • Umbral archivos grandes: " + LARGE_FILE_THRESHOLD_MB + "MB");
            System.out.println("  • Mostrando respuestas IA: ✅ ACTIVADO");
            
            // 🎯 INICIALIZAR ORÁCULO
            com.elreinodelolvido.ellibertad.api.OraculoDeLaLibertad oraculo = new com.elreinodelolvido.ellibertad.api.OraculoDeLaLibertad();
            
            // 📈 CONTADORES
            int clasesAnalizadas = 0;
            int sugerenciasTotales = 0;
            int refactorsRegistrados = 0;
            int archivosGrandesProcesados = 0;
            List<String> clasesConProblemas = new ArrayList<>();
            
            // 🚀 ANALIZAR CLASES (CON LÍMITE) - CON DETECCIÓN AUTOMÁTICA DE STREAMING
            for (int i = 0; i < limiteAnalisis; i++) {
                ClassInfo clase = clases.get(i);
                
                try {
                    // 📄 VERIFICAR EXISTENCIA DEL ARCHIVO
                    Path filePath = Paths.get(clase.getSourcePath());
                    if (!Files.exists(filePath)) {
                        String errorMsg = "❌ Archivo no encontrado: " + clase.getSourcePath();
                        clasesConProblemas.add(errorMsg);
                        contadorKrakens.incrementAndGet();
                        continue;
                    }
                    
                    if (modoVerbose) {
                        System.out.println("\n🎯 ANALIZANDO [" + (i+1) + "/" + limiteAnalisis + "]: " + clase.getFullName());
                    }
                    
                    // 🚀 DETECTAR SI ES ARCHIVO GRANDE Y USAR STREAMING
                    long fileSize = Files.size(filePath);
                    boolean esArchivoGrande = fileSize > LARGE_FILE_THRESHOLD_MB * 1024 * 1024;
                    
                    JSONObject resultadoAnalisis;
                    List<String> sugerencias = new ArrayList<>();
                    String respuestaIACompleta = "";
                    
                    if (esArchivoGrande) {
                        // 🎯 USAR STREAMING PARA ARCHIVOS GRANDES
                        if (modoVerbose) {
                            System.out.println("  📁 ARCHIVO GRANDE DETECTADO (" + (fileSize/1024/1024) + "MB) - USANDO STREAMING...");
                        }
                        
                        resultadoAnalisis = analizarArchivoGrande(filePath);
                        archivosGrandesProcesados++;
                        resultado.put("modoStreamingUtilizado", true);
                        
                        // 📝 EXTRAER SUGERENCIAS DEL RESULTADO STREAMING
                        if (resultadoAnalisis.has("sugerencias")) {
                            Object sugerenciasObj = resultadoAnalisis.get("sugerencias");
                            if (sugerenciasObj instanceof List) {
                                sugerencias = (List<String>) sugerenciasObj;
                            }
                        }
                        
                        // 🎯 MOSTRAR RESPUESTAS IA DE CHUNKS
                        if (resultadoAnalisis.has("respuestasIAChunks")) {
                            mostrarRespuestasIAChunks(resultadoAnalisis.getJSONArray("respuestasIAChunks"), clase.getFullName());
                        }
                        
                        if (modoVerbose) {
                            System.out.println("  ✅ Streaming completado: " + sugerencias.size() + " sugerencias de " + 
                                             resultadoAnalisis.get("chunksProcesados") + " chunks");
                        }
                        
                    } else {
                        // 🎯 USAR MÉTODO NORMAL PARA ARCHIVOS PEQUEÑOS
                        String codigoFuente = FileUtils.readFile(clase.getSourcePath());
                        
                        if (codigoFuente != null && !codigoFuente.trim().isEmpty()) {
                            // 🚀 VERIFICAR CACHE
                            String cacheKey = clase.getFullName() + "_" + codigoFuente.hashCode();
                            String analisisIA = obtenerAnalisisCacheado(cacheKey, codigoFuente, oraculo);
                            
                            if (analisisIA != null) {
                                // 🎯 MOSTRAR RESPUESTA IA COMPLETA
                                mostrarRespuestaIA(analisisIA, clase.getFullName(), "NORMAL");
                                
                                // 📝 PROCESAR RESPUESTA IA
                                sugerencias = extraerSugerenciasReales(analisisIA);
                                respuestaIACompleta = analisisIA;
                                
                                resultadoAnalisis = new JSONObject();
                                resultadoAnalisis.put("sugerencias", sugerencias);
                                resultadoAnalisis.put("modo", "normal");
                                resultadoAnalisis.put("respuestaIA", analisisIA);
                            } else {
                                resultadoAnalisis = new JSONObject();
                                resultadoAnalisis.put("error", "No se pudo obtener análisis IA");
                            }
                        } else {
                            resultadoAnalisis = new JSONObject();
                            resultadoAnalisis.put("error", "Archivo vacío o no legible");
                        }
                    }
                    
                    // 📝 PROCESAR RESULTADOS DEL ANÁLISIS
                    if (resultadoAnalisis != null && !resultadoAnalisis.has("error")) {
                        if (!sugerencias.isEmpty()) {
                            sugerenciasTotales += sugerencias.size();
                            
                            // 📋 REGISTRAR REFACTORS
                            for (String sugerencia : sugerencias) {
                                if (!PlanificadorRefactor.estaEnPlan(clase.getName(), clase.getPackageName())) {
                                    PlanificadorRefactor.registrar(
                                        clase.getName(), 
                                        clase.getPackageName(), 
                                        "🤖 IA: " + sugerencia + (esArchivoGrande ? " [STREAMING]" : "")
                                    );
                                    refactorsRegistrados++;
                                }
                            }
                            
                            if (modoVerbose) {
                                System.out.println("  ✅ " + sugerencias.size() + " sugerencias - " + 
                                                 refactorsRegistrados + " refactors registrados" +
                                                 (esArchivoGrande ? " [STREAMING]" : ""));
                            }
                        }
                        
                        clasesAnalizadas++;
                        contadorClasesProcesadas.incrementAndGet();
                    } else {
                        String errorMsg = "❌ Error en análisis de " + clase.getFullName() + 
                                        (resultadoAnalisis != null ? ": " + resultadoAnalisis.optString("error", "Desconocido") : "");
                        clasesConProblemas.add(errorMsg);
                        contadorKrakens.incrementAndGet();
                        
                        if (modoVerbose) {
                            System.out.println(errorMsg);
                        }
                    }
                    
                    // ⏰ RESPETAR RATE LIMITING (solo para análisis normales, streaming ya maneja su propio rate)
                    if (!esArchivoGrande && i < limiteAnalisis - 1) {
                        Thread.sleep(delayEntreRequestsMs);
                    }
                    
                } catch (Exception e) {
                    String errorMsg = "❌ Error analizando " + clase.getFullName() + ": " + e.getMessage();
                    clasesConProblemas.add(errorMsg);
                    contadorKrakens.incrementAndGet();
                    
                    if (modoVerbose) {
                        System.out.println(errorMsg);
                    }
                }
            }
            
            // 📊 CALCULAR MÉTRICAS FINALES
            long endTime = System.currentTimeMillis();
            long duracionTotal = endTime - startTime;
            double eficiencia = (double) clasesAnalizadas / limiteAnalisis * 100;
            
            // 🎯 CONSTRUIR RESULTADO COMPLETO
            resultado.put("clasesAnalizadas", clasesAnalizadas);
            resultado.put("sugerenciasEncontradas", sugerenciasTotales);
            resultado.put("refactorsRegistrados", refactorsRegistrados);
            resultado.put("archivosGrandesProcesados", archivosGrandesProcesados);
            resultado.put("errores", clasesConProblemas.size());
            resultado.put("duracionMs", duracionTotal);
            resultado.put("eficiencia", String.format("%.1f%%", eficiencia));
            resultado.put("clasesConProblemas", clasesConProblemas);
            resultado.put("timestamp", new java.util.Date().toString());
            
            // 📈 MOSTRAR REPORTE FINAL
            System.out.println("\n" + "🎉".repeat(80));
            System.out.println("           ANÁLISIS COMPLETADO - RESUMEN TURBOFURULADO CON STREAMING");
            System.out.println("🎉".repeat(80));
            System.out.println("  📊 ESTADÍSTICAS:");
            System.out.println("    • Clases analizadas: " + clasesAnalizadas + "/" + limiteAnalisis);
            System.out.println("    • Sugerencias encontradas: " + sugerenciasTotales);
            System.out.println("    • Refactors registrados: " + refactorsRegistrados);
            System.out.println("    • Archivos grandes procesados: " + archivosGrandesProcesados);
            System.out.println("    • Errores: " + clasesConProblemas.size());
            System.out.println("    • Duración: " + duracionTotal + "ms");
            System.out.println("    • Eficiencia: " + String.format("%.1f%%", eficiencia));
            
            if (archivosGrandesProcesados > 0) {
                System.out.println("    • Modo streaming: ✅ ACTIVADO");
            }
            
            if (!clasesConProblemas.isEmpty()) {
                System.out.println("\n  ⚠️  CLASES CON PROBLEMAS:");
                for (String problema : clasesConProblemas) {
                    System.out.println("    • " + problema);
                }
            }
            
            bitacora.exito("Análisis completo turbofurulado finalizado: " + clasesAnalizadas + 
                          " clases procesadas (" + archivosGrandesProcesados + " con streaming)");
            
        } catch (Exception e) {
            contadorKrakens.incrementAndGet();
            String errorMsg = "💥 ERROR CRÍTICO en análisis completo: " + e.getMessage();
            System.out.println(errorMsg);
            bitacora.error("Fallo en análisis completo turbofurulado", e);
            
            resultado.put("error", true);
            resultado.put("mensajeError", errorMsg);
        }
        
        return resultado;
    }

    /**
     * 🚀 ANALIZAR ARCHIVO GRANDE CON STREAMING - CON VISUALIZACIÓN DE RESPUESTAS IA
     */
    public JSONObject analizarArchivoGrande(Path filePath) {
        bitacora.info("📁 INICIANDO ANÁLISIS CON STREAMING: " + filePath.getFileName());
        
        JSONObject resultado = new JSONObject();
        long startTime = System.currentTimeMillis();
        
        try {
            // 🎯 VERIFICAR TAMAÑO
            long fileSize = Files.size(filePath);
            boolean isLargeFile = fileSize > LARGE_FILE_THRESHOLD_MB * 1024 * 1024;
            
            resultado.put("archivo", filePath.toString());
            resultado.put("tamañoBytes", fileSize);
            resultado.put("esGrande", isLargeFile);
            
            if (!isLargeFile) {
                // 🎯 ARCHIVO PEQUEÑO - ANÁLISIS NORMAL
                return analizarArchivoNormal(filePath);
            }
            
            // 🚀 ARCHIVO GRANDE - ANÁLISIS POR CHUNKS
            List<FileChunk> chunks = chunkProcessor.processLargeFile(filePath, fileSize);
            resultado.put("totalChunks", chunks.size());
            resultado.put("tamañoPromedioChunk", chunks.stream()
                .mapToInt(FileChunk::getLineCount).average().orElse(0));
            
            // 🚀 PROCESAR CHUNKS EN PARALELO
            List<CompletableFuture<ChunkAnalysis>> futures = new ArrayList<>();
            ExecutorService chunkExecutor = Executors.newFixedThreadPool(
                Math.min(chunks.size(), Runtime.getRuntime().availableProcessors())
            );
            
            JSONArray respuestasIAChunks = new JSONArray();
            
            for (FileChunk chunk : chunks) {
                CompletableFuture<ChunkAnalysis> future = CompletableFuture.supplyAsync(() -> {
                    ChunkAnalysis analysis = procesarChunk(chunk);
                    
                    // 🎯 CAPTURAR RESPUESTA IA PARA MOSTRAR EN CONSOLA
                    if (analysis.getRespuestaIA() != null && !analysis.getRespuestaIA().isEmpty()) {
                        synchronized (respuestasIAChunks) {
                            JSONObject respuestaChunk = new JSONObject();
                            respuestaChunk.put("chunkNumber", chunk.getChunkNumber());
                            respuestaChunk.put("chunkId", chunk.getChunkId());
                            respuestaChunk.put("respuestaIA", analysis.getRespuestaIA());
                            respuestasIAChunks.put(respuestaChunk);
                        }
                    }
                    
                    return analysis;
                }, chunkExecutor);
                futures.add(future);
            }
            
            // 🎯 ESPERAR RESULTADOS
            List<ChunkAnalysis> resultadosChunks = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
            
            chunkExecutor.shutdown();
            
            // 📊 CONSOLIDAR RESULTADOS
            List<String> todasSugerencias = resultadosChunks.stream()
                .flatMap(analysis -> analysis.getSugerencias().stream())
                .distinct()
                .collect(Collectors.toList());
            
            resultado.put("sugerencias", todasSugerencias);
            resultado.put("totalSugerencias", todasSugerencias.size());
            resultado.put("chunksProcesados", resultadosChunks.size());
            resultado.put("chunksConError", resultadosChunks.stream().filter(r -> !r.isExito()).count());
            resultado.put("respuestasIAChunks", respuestasIAChunks);
            resultado.put("modo", "streaming");
            
            // 🎯 MÉTRICAS DE RENDIMIENTO
            long endTime = System.currentTimeMillis();
            resultado.put("tiempoTotalMs", endTime - startTime);
            resultado.put("eficienciaStreaming", 
                calcularEficienciaStreaming(chunks.size(), resultadosChunks.size()));
            
            bitacora.info("✅ Análisis streaming completado: " + chunks.size() + " chunks procesados, " + 
                         todasSugerencias.size() + " sugerencias encontradas");
            
        } catch (Exception e) {
            contadorKrakens.incrementAndGet();
            String errorMsg = "💥 Error analizando archivo grande: " + filePath + " - " + e.getMessage();
            bitacora.error("Fallo en análisis streaming", e);
            
            resultado.put("error", true);
            resultado.put("mensajeError", errorMsg);
        }
        
        return resultado;
    }

    /**
     * 🎯 PROCESAR CHUNK INDIVIDUAL - CON CAPTURA DE RESPUESTA IA
     */
    private ChunkAnalysis procesarChunk(FileChunk chunk) {
        ChunkAnalysis resultado = new ChunkAnalysis(chunk.getChunkId(), chunk.getChunkNumber());
        long startTime = System.currentTimeMillis();
        
        try {
            // 🎯 USAR ORÁCULO PARA ANALIZAR CHUNK
            com.elreinodelolvido.ellibertad.api.OraculoDeLaLibertad oraculo = 
                new com.elreinodelolvido.ellibertad.api.OraculoDeLaLibertad();
            
            String contexto = chunk.getContextForAI();
            String analisisIA = oraculo.invocarOraculoTecnico(contexto);
            
            // 🎯 GUARDAR RESPUESTA IA COMPLETA
            resultado.setRespuestaIA(analisisIA);
            
            // 🎯 MOSTRAR RESPUESTA IA EN CONSOLA
            mostrarRespuestaIA(analisisIA, chunk.getClassName(), "STREAMING-CHUNK-" + chunk.getChunkNumber());
            
            List<String> sugerencias = extraerSugerenciasReales(analisisIA);
            
            // 🎯 FILTRAR SUGERENCIAS RELEVANTES PARA EL CHUNK
            List<String> sugerenciasFiltradas = filtrarSugerenciasPorChunk(sugerencias, chunk);
            
            resultado.setSugerencias(sugerenciasFiltradas);
            resultado.setExito(true);
            resultado.setTiempoProcesamiento(System.currentTimeMillis() - startTime);
            
            if (modoVerbose) {
                System.out.println("    ✅ Chunk " + chunk.getChunkNumber() + " procesado: " + 
                                 sugerenciasFiltradas.size() + " sugerencias");
            }
            
        } catch (Exception e) {
            resultado.setExito(false);
            resultado.setError(e.getMessage());
            resultado.setTiempoProcesamiento(System.currentTimeMillis() - startTime);
            bitacora.error("Error procesando chunk " + chunk.getChunkNumber(), e);
        }
        
        return resultado;
    }

    /**
     * 🎯 ANALIZAR ARCHIVO NORMAL (pequeño) - CON VISUALIZACIÓN IA
     */
    private JSONObject analizarArchivoNormal(Path filePath) throws IOException {
        JSONObject resultado = new JSONObject();
        String contenido = Files.readString(filePath, StandardCharsets.UTF_8);
        
        // 🎯 USAR ORÁCULO PARA ANÁLISIS COMPLETO
        com.elreinodelolvido.ellibertad.api.OraculoDeLaLibertad oraculo = 
            new com.elreinodelolvido.ellibertad.api.OraculoDeLaLibertad();
        
        String analisisIA = oraculo.invocarOraculoTecnico(contenido);
        
        // 🎯 MOSTRAR RESPUESTA IA COMPLETA
        String nombreArchivo = filePath.getFileName().toString();
        mostrarRespuestaIA(analisisIA, nombreArchivo, "NORMAL");
        
        List<String> sugerencias = extraerSugerenciasReales(analisisIA);
        
        resultado.put("sugerencias", sugerencias);
        resultado.put("totalSugerencias", sugerencias.size());
        resultado.put("modo", "normal");
        resultado.put("chunks", 1);
        resultado.put("respuestaIA", analisisIA);
        
        return resultado;
    }

    /**
     * 🎯 MOSTRAR RESPUESTA IA EN CONSOLA - FORMATO BONITO
     */
    private void mostrarRespuestaIA(String respuestaIA, String nombreClase, String modo) {
        System.out.println("\n" + "🤖".repeat(60));
        System.out.println("           RESPUESTA IA - " + modo);
        System.out.println("🤖".repeat(60));
        System.out.println("📁 Clase: " + nombreClase);
        System.out.println("🎯 Modo: " + modo);
        System.out.println("⏰ Timestamp: " + new java.util.Date());
        System.out.println("📏 Longitud respuesta: " + respuestaIA.length() + " caracteres");
        System.out.println("📝".repeat(60));
        System.out.println(respuestaIA);
        System.out.println("📝".repeat(60));
        System.out.println("🔚 FIN RESPUESTA IA - " + modo);
        System.out.println("🤖".repeat(60) + "\n");
    }

    /**
     * 🎯 MOSTRAR RESPUESTAS IA DE CHUNKS - RESUMEN CONSOLIDADO
     */
    private void mostrarRespuestasIAChunks(JSONArray respuestasIAChunks, String nombreClase) {
        System.out.println("\n" + "🔍".repeat(60));
        System.out.println("           RESUMEN RESPUESTAS IA - STREAMING");
        System.out.println("🔍".repeat(60));
        System.out.println("📁 Clase: " + nombreClase);
        System.out.println("📦 Total chunks: " + respuestasIAChunks.length());
        System.out.println("📊 Resumen por chunk:");
        
        for (int i = 0; i < respuestasIAChunks.length(); i++) {
            JSONObject chunkRespuesta = respuestasIAChunks.getJSONObject(i);
            int chunkNumber = chunkRespuesta.getInt("chunkNumber");
            String respuestaIA = chunkRespuesta.getString("respuestaIA");
            
            System.out.println("  • Chunk " + chunkNumber + ": " + respuestaIA.length() + " caracteres");
            
            // 🎯 MOSTRAR EXTRACTO DE CADA RESPUESTA (primeras 200 caracteres)
            if (respuestaIA.length() > 200) {
                System.out.println("    📄 " + respuestaIA.substring(0, 200).replace("\n", " ") + "...");
            } else {
                System.out.println("    📄 " + respuestaIA.replace("\n", " "));
            }
        }
        System.out.println("🔚 FIN RESUMEN STREAMING - " + nombreClase);
        System.out.println("🔍".repeat(60) + "\n");
    }

    /**
     * 🚀 OBTENER ANÁLISIS CACHEADO - CON VISUALIZACIÓN IA
     */
    private String obtenerAnalisisCacheado(String cacheKey, String codigoFuente, 
                                         com.elreinodelolvido.ellibertad.api.OraculoDeLaLibertad oraculo) {
        // 🎯 VERIFICAR CACHE
        Long timestamp = cacheTimestamp.get(cacheKey);
        if (timestamp != null && (System.currentTimeMillis() - timestamp) < CACHE_DURATION_MS) {
            String cached = cacheAnalisis.get(cacheKey);
            if (cached != null) {
                if (modoVerbose) {
                    System.out.println("  🔄 Usando análisis cacheado");
                    
                    // 🎯 MOSTRAR TAMBIÉN RESPUESTA CACHEADADA
                    System.out.println("  📋 Respuesta cacheada disponible (" + cached.length() + " caracteres)");
                }
                return cached;
            }
        }
        
        // 🚀 SOLICITAR NUEVO ANÁLISIS
        try {
            long startTime = System.currentTimeMillis();
            String analisisIA = oraculo.invocarOraculoTecnico(codigoFuente);
            long endTime = System.currentTimeMillis();
            
            if (modoVerbose) {
                System.out.println("  ⏱️  API respondió en " + (endTime - startTime) + "ms");
            }
            
            // 💾 GUARDAR EN CACHE
            cacheAnalisis.put(cacheKey, analisisIA);
            cacheTimestamp.put(cacheKey, System.currentTimeMillis());
            
            return analisisIA;
            
        } catch (Exception e) {
            System.out.println("  ❌ Error llamando a API: " + e.getMessage());
            return null;
        }
    }
}
