package com.elreinodelolvido.ellibertad.scanner;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.comments.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.Modifier;
import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.model.ClassInfo;
import com.elreinodelolvido.ellibertad.model.FieldInfo;
import com.elreinodelolvido.ellibertad.model.MethodInfo;
import com.elreinodelolvido.ellibertad.model.ParameterInfo;
import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.*;

/**
 * 🚀 TURBO SCANNER 9000 - Escáner de proyectos Java con overdrive
 * ¡Analiza hasta el último bytecode con precisión cuántica! ⚡
 * ✅ TODOS LOS MÉTODOS ORIGINALES PRESERVADOS + TURBO MEJORAS
 */
public class ProjectScanner {
    private final static Map<String, ClassInfo> classMap = new ConcurrentHashMap<>();
    private final static Map<String, ClassMetadata> metadataMap = new ConcurrentHashMap<>();
    private final JavaParser parser;
    private final Bitacora bitacora;
    private final static Set<String> scannedPackages = ConcurrentHashMap.newKeySet();
    private final static List<String> scanErrors = Collections.synchronizedList(new ArrayList<>());
    
    // 🆕 CONFIGURACIÓN TURBO
    private ExecutorService turboExecutor;
    private final int MAX_CONCURRENT_PARSERS = Runtime.getRuntime().availableProcessors() * 2;
    private final Set<String> excludedDirs = Set.of("target", "build", ".git", "node_modules", "test", "test-resources");
    private final AtomicInteger filesProcessed = new AtomicInteger(0);
    private final AtomicInteger rescueOperations = new AtomicInteger(0);
    
 // 🎯 MÉTODOS DE ACCESO - DEBEN SER NO-STATIC
    public static List<ClassInfo> getClasses() {
        return new ArrayList<>(classMap.values());
    }

    public Optional<ClassInfo> getClassByName(String fullyQualifiedName) {
        return Optional.ofNullable(classMap.get(fullyQualifiedName));
    }

    public static List<ClassMetadata> getClassMetadata() {
        return new ArrayList<>(metadataMap.values());
    }

    public List<String> getScanErrors() {
        return new ArrayList<>(scanErrors);
    }

    public Set<String> getScannedPackages() {
        return new HashSet<>(scannedPackages);
    }

    // 🏗️ Constructores TURBO
    public ProjectScanner() {
        this(new Bitacora());
    }
    
 // 🆕 REEMPLAZAR en el constructor:
    public ProjectScanner(Bitacora bitacora) {
        this.bitacora = Objects.requireNonNull(bitacora, "🚨 Bitacora no puede ser null!");
        
        // 🆕 USAR PARSER ROBUSTO
        this.parser = createRobustParser();
        this.turboExecutor = Executors.newFixedThreadPool(MAX_CONCURRENT_PARSERS);
        
        bitacora.info("⚡ TURBO SCANNER INICIALIZADO - " + MAX_CONCURRENT_PARSERS + " hilos disponibles");
    }

    /**
     * 🚀 ESCANEO TURBO - VERSIÓN MÁXIMA VELOCIDAD
     */
    public void scanProject(String rootPath) {
        scanProjectTurbo(rootPath);
    }

    /**
     * 🌪️ ESCANEO TURBO MEJORADO - PARALELIZACIÓN MASIVA
     */
    public void scanProjectTurbo(String rootPath) {
        long startTime = System.nanoTime();
        bitacora.info("🎯 INICIANDO ESCANEO TURBO: " + rootPath);
        
        Path rootDir = Paths.get(rootPath);
        if (!Files.exists(rootDir)) {
            bitacora.error("❌ Directorio raíz no existe: " + rootPath);
            return;
        }

        // 🧹 LIMPIEZA COMPLETA TURBO
        resetScannerState();
        
        try {
            // 🎯 FASE 1: ESCANEO PARALELO MASIVO
            bitacora.info("🎯 FASE 1: Escaneo paralelo turbo...");
            List<Path> javaFiles = encontrarArchivosJavaTurbo(rootDir);
            bitacora.info("📁 Archivos Java encontrados: " + javaFiles.size());
            
            // 🚀 PROCESAMIENTO PARALELO
            procesarArchivosParalelo(javaFiles);
            
            // 🎯 DIAGNÓSTICO INTERMEDIO
            diagnosticClassMapCheck();
                
            // 🎯 FASE 2: ANÁLISIS AVANZADO
            bitacora.info("🎯 FASE 2: Análisis avanzado...");
            analizarDependenciasTurbo();
            calcularMetricasAvanzadas();
            detectarPatronesArquitectonicos();
            
        } catch (IOException e) {
            bitacora.error("💥 Error en escaneo turbo: " + e.getMessage());
        } finally {
            turboExecutor.shutdown();
            try {
                if (!turboExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                    turboExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                turboExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        long tiempoTotal = (System.nanoTime() - startTime) / 1_000_000;
        generarReporteTurbo(tiempoTotal);
        
        // 🎯 DIAGNÓSTICO FINAL
        diagnosticClassMapCheck();
    }

    /**
     * 🆕 ENCONTRAR ARCHIVOS JAVA TURBO - WalkTree optimizado
     */
    private List<Path> encontrarArchivosJavaTurbo(Path rootDir) throws IOException {
        List<Path> javaFiles = Collections.synchronizedList(new ArrayList<>());
        
        Files.walkFileTree(rootDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                String dirName = dir.getFileName().toString();
                if (excludedDirs.contains(dirName)) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }
            
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().endsWith(".java")) {
                    javaFiles.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        
        return javaFiles;
    }

    /**
     * 🚀 PROCESAR ARCHIVOS EN PARALELO
     */

    /**
     * 🛡️ PROCESAR ARCHIVO DE FORMA SEGURA Y AISLADA
     */
    private void processFileSafely(Path filePath) {
        String fileName = filePath.getFileName().toString();
        
        try {
            // 🎯 VERIFICACIÓN EXTRA DE SEGURIDAD
            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                bitacora.debug("⏭️ Archivo no válido: " + fileName);
                return;
            }
            
            // 🎯 LIMITAR TAMAÑO DE ARCHIVO
            long fileSize = Files.size(filePath);
            if (fileSize > 5 * 1024 * 1024) { // 5MB máximo
                bitacora.warn("📏 Archivo muy grande, procesamiento básico: " + fileName + " (" + fileSize + " bytes)");
                extractBasicInfoWithRegex(filePath.toFile(), readFileContent(filePath.toFile()));
                return;
            }
            
            // 🎯 PROCESAMIENTO PRINCIPAL
            parseJavaFileTurbo(filePath.toFile());
            
        } catch (IOException e) {
            bitacora.error("📁 Error de E/S procesando: " + fileName + " - " + e.getMessage());
        } catch (OutOfMemoryError e) {
            bitacora.error("💾 OUT OF MEMORY en: " + fileName + " - Reiniciando parser");
            // 🛡️ LIMPIAR MEMORIA Y REINTENTAR CON MÉTODO MÁS SIMPLE
            System.gc();
            extractBasicInfoWithRegex(filePath.toFile(), readFileContent(filePath.toFile()));
        } catch (Exception e) {
            bitacora.error("⚡ Error inesperado en: " + fileName + " - " + e.getClass().getSimpleName());
            emergencyRescue(filePath.toFile(), e);
        }
    }

    /**
     * 🚀 PARSE TURBO MEJORADO - Procesamiento robusto con múltiples capas de defensa
     */
    private void parseJavaFileTurbo(File file) {
        String fileName = file.getName();
        filesProcessed.incrementAndGet();
        
        // 🎯 VERIFICACIONES INICIALES DE SEGURIDAD
        if (!isValidJavaFile(file)) {
            bitacora.debug("⏭️ Archivo no válido omitido: " + fileName);
            return;
        }
        
        bitacora.debug("🔍 Iniciando procesamiento turbo: " + fileName);
        
        try {
            // 🎯 ESTRATEGIA EN CASCADA DE 4 NIVELES
            ParseResult<CompilationUnit> result = executeCascadingParseStrategy(file);
            
            // 🎯 ANALIZAR RESULTADO Y TOMAR DECISIÓN
            processParseResult(result, file);
            
        } catch (OutOfMemoryError e) {
            // 🚨 MANEJO ESPECIAL DE OUT OF MEMORY
            //TODO handleOutOfMemoryError(file, e);
        } catch (Exception e) {
            // 🛡️ CAPTURA DE CUALQUIER OTRO ERROR
            handleGeneralError(file, e);
        } finally {
            // 🧹 LIMPIEZA PREVENTIVA
            performSafetyCleanup();
        }
    }

    /**
     * 🎯 VERIFICAR SI EL ARCHIVO ES VÁLIDO PARA PROCESAMIENTO
     */
    private boolean isValidJavaFile(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        
        if (!file.getName().toLowerCase().endsWith(".java")) {
            return false;
        }
        
        // 🚨 EXCLUIR ARCHIVOS DEMASIADO GRANDES
        if (file.length() > 10 * 1024 * 1024) { // 10MB
            bitacora.warn("📏 Archivo demasiado grande: " + file.getName() + " (" + file.length() + " bytes)");
            return false;
        }
        
        // 🚨 EXCLUIR ARCHIVOS DEL PROPIO SCANNER (para evitar recursión)
        if (file.getName().contains("ProjectScanner") || 
            file.getName().contains("EmergencyJavaParser")) {
            bitacora.debug("🔄 Omitting self-referential file: " + file.getName());
            return false;
        }
        
        return true;
    }

    /**
     * 🎯 ESTRATEGIA EN CASCADA DE 4 NIVELES DE PARSE
     */
    private ParseResult<CompilationUnit> executeCascadingParseStrategy(File file) {
        String fileName = file.getName();
        
        // 🎯 NIVEL 1: PARSE ROBUSTO CON LIMPIEZA AVANZADA
        bitacora.debug("  🛡️ Nivel 1: Parse robusto...");
        ParseResult<CompilationUnit> result = EmergencyJavaParser.parseRobust(file);
        
        if (isHighQualityParse(result)) {
            bitacora.debug("  ✅ Nivel 1 exitoso - Calidad: ALTA");
            return result;
        }
        
        // 🎯 NIVEL 2: PARSE SUPER-SIMPLE (menos tolerante a errores)
        bitacora.debug("  🔄 Nivel 2: Parse simple...");
        Optional<CompilationUnit> simpleResult = EmergencyJavaParser.parseSuperSimple(file);
        if (simpleResult.isPresent()) {
            bitacora.debug("  ✅ Nivel 2 exitoso");
            return new ParseResult<>(simpleResult.get(), Collections.emptyList(), null);
        }
        
        // 🎯 NIVEL 3: PARSE CON CONFIGURACIÓN ALTERNATIVA
        bitacora.debug("  ⚡ Nivel 3: Parse alternativo...");
        result = executeAlternativeParse(file);
        if (result.isSuccessful() && result.getResult().isPresent()) {
            bitacora.debug("  ✅ Nivel 3 exitoso");
            return result;
        }
        
        // 🎯 NIVEL 4: FALLBACK CONTROLADO
        bitacora.debug("  🛟 Nivel 4: Fallback de emergencia...");
        return EmergencyJavaParser.parseFallback(file);
    }

    /**
     * 🎯 EJECUTAR PARSE CON CONFIGURACIÓN ALTERNATIVA
     */
    private ParseResult<CompilationUnit> executeAlternativeParse(File file) {
        try {
            // 🎯 CONFIGURACIÓN ALTERNATIVA MÁS PERMISIVA
            ParserConfiguration altConfig = new ParserConfiguration();
            altConfig.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_11);
            altConfig.setAttributeComments(false);
            altConfig.setLexicalPreservationEnabled(false);
            altConfig.setPreprocessUnicodeEscapes(true);
            
            JavaParser altParser = new JavaParser(altConfig);
            String content = readFileWithFallbackEncoding(file);
            
            // 🎯 LIMPIEZA AGGRESIVA PERO CONSERVADORA
            content = aggressiveButSafeContentCleanup(content);
            
            return altParser.parse(ParseStart.COMPILATION_UNIT, Providers.provider(content));
            
        } catch (Exception e) {
            return EmergencyJavaParser.parseFallback(file);
        }
    }

    /**
     * 🎯 LECTURA DE ARCHIVO CON DETECCIÓN DE ENCODING MEJORADA
     */
    private String readFileWithFallbackEncoding(File file) {
        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            
            // 🎯 PROBAR MÚLTIPLES ENCODINGS EN ORDEN DE PROBABILIDAD
            String[] encodings = {"UTF-8", "ISO-8859-1", "Windows-1252", "US-ASCII"};
            
            for (String encoding : encodings) {
                try {
                    String content = new String(fileBytes, encoding);
                    if (isLikelyValidJava(content)) {
                        return content;
                    }
                } catch (Exception e) {
                    // Continuar con siguiente encoding
                }
            }
            
            // 🛡️ ULTIMO RECURSO: UTF-8 con reemplazo
            return new String(fileBytes, StandardCharsets.UTF_8);
            
        } catch (IOException e) {
            return "// Error reading file: " + e.getMessage();
        }
    }

    /**
     * 🎯 LIMPIEZA AGGRESIVA PERO SEGURA DEL CONTENIDO
     */
    private String aggressiveButSafeContentCleanup(String content) {
        if (content == null) return "";
        
        StringBuilder cleaned = new StringBuilder();
        
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            
            // 🎯 PERMITIR SOLO CARACTERES JAVA SEGUROS
            if (isSafeJavaChar(c)) {
                cleaned.append(c);
            } else if (c == '\r') {
                // 🎯 NORMALIZAR NEWLINES
                cleaned.append('\n');
            } else if (Character.isWhitespace(c)) {
                // 🎯 PRESERVAR ESPACIOS VÁLIDOS
                cleaned.append(' ');
            }
            // 🎯 IGNORAR CARACTERES PELIGROSOS O INVÁLIDOS
        }
        
        String result = cleaned.toString();
        
        // 🎯 ELIMINAR LÍNEAS DEMASIADO LARGAS (causa común de buffer overflow)
        result = splitAndLimitLines(result, 500); // Límite más conservador
        
        return result;
    }

    /**
     * 🎯 VERIFICAR SI CARACTER ES SEGURO PARA JAVA
     */
    private boolean isSafeJavaChar(char c) {
        // 🎯 CARACTERES EXPLÍCITAMENTE PERMITIDOS
        return (c >= 32 && c <= 126) ||    // ASCII básico
               (c == '\n' || c == '\t') || // Whitespace esencial
               (c >= 128 && c <= 255) ||   // Latin-1 extendido (con cuidado)
               Character.isLetterOrDigit(c) || // Letras/dígitos Unicode
               "{}[]()<>;,.=+-*/%&|^!~?:@#\"'\\$_ ".indexOf(c) >= 0; // Símbolos Java
    }

    /**
     * 🎯 DIVIDIR Y LIMITAR LÍNEAS LARGAS
     */
    private String splitAndLimitLines(String content, int maxLineLength) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();
        
        for (String line : lines) {
            if (line.length() > maxLineLength) {
                // 🎯 DIVIDIR SIN ROMPER PALABRAS CUANDO SEA POSIBLE
                int splitPoint = findSafeSplitPoint(line, maxLineLength);
                result.append(line.substring(0, splitPoint)).append("\n");
                result.append(line.substring(splitPoint)).append("\n");
            } else {
                result.append(line).append("\n");
            }
        }
        
        return result.toString();
    }

    /**
     * 🎯 ENCONTRAR PUNTO SEGURO PARA DIVIDIR LÍNEA
     */
    private int findSafeSplitPoint(String line, int maxLength) {
        // 🎯 BUSCAR ESPACIO O PUNTO Y COMA CERCA DEL LÍMITE
        for (int i = maxLength - 1; i > maxLength - 50 && i >= 0; i--) {
            if (i < line.length() && (line.charAt(i) == ' ' || line.charAt(i) == ';' || line.charAt(i) == '.')) {
                return i + 1; // Incluir el carácter de división
            }
        }
        
        // 🎯 FALLBACK: DIVIDIR EN EL LÍMITE EXACTO
        return Math.min(maxLength, line.length());
    }

    /**
     * 🎯 VERIFICAR SI EL PARSE ES DE ALTA CALIDAD
     */
    private boolean isHighQualityParse(ParseResult<CompilationUnit> result) {
        if (result == null || !result.isSuccessful() || !result.getResult().isPresent()) {
            return false;
        }
        
        if (result.getProblems() != null) {
            // 🎯 PERMITIR HASTA 3 PROBLEMAS MENORES
            return result.getProblems().size() <= 3;
        }
        
        return true;
    }

    /**
     * 🎯 PROCESAR RESULTADO DEL PARSE Y TOMAR DECISIÓN
     */
    private void processParseResult(ParseResult<CompilationUnit> result, File file) {
        String fileName = file.getName();
        
        if (result.getResult().isPresent()) {
            // 🎯 PARSE EXITOSO - PROCESAR NORMALMENTE
            CompilationUnit unit = result.getResult().get();
            procesarUnidadCompilacionTurbo(unit, file);
            
            // 🎯 REGISTRAR ÉXITO CON DETALLES
            int problemCount = result.getProblems() != null ? result.getProblems().size() : 0;
            if (problemCount == 0) {
                bitacora.debug("✅ Parse perfecto: " + fileName);
            } else {
                bitacora.debug("✅ Parse exitoso con " + problemCount + " advertencias: " + fileName);
            }
            
        } else {
            // 🎯 PARSE FALLIDO - USAR RESCATE
            bitacora.warn("🔄 Parse fallido, activando rescate: " + fileName);
            
            // 🎯 INTENTAR EXTRACCIÓN BÁSICA
            Map<String, String> basicInfo = EmergencyJavaParser.extractBasicFileInfo(file);
            if (!basicInfo.containsKey("error")) {
                createRescueClassInfo(file, basicInfo);
                bitacora.debug("🛡️ Rescate básico exitoso: " + fileName);
            } else {
                // 🎯 RESCATE DE EMERGENCIA MÍNIMO
                emergencyRescue(file, new RuntimeException("Parse fallido y rescate básico falló"));
                bitacora.debug("🆘 Rescate de emergencia: " + fileName);
            }
        }
    }

    /**
     * 🎯 CREAR INFORMACIÓN DE CLASE DESDE DATOS BÁSICOS (rescate)
     */
    private void createRescueClassInfo(File file, Map<String, String> basicInfo) {
        try {
            String className = basicInfo.getOrDefault("name", file.getName().replace(".java", ""));
            String packageName = basicInfo.getOrDefault("package", "");
            String type = basicInfo.getOrDefault("type", "CLASS");
            
            String fqcn = packageName.isEmpty() ? className : packageName + "." + className;
            
            if (!classMap.containsKey(fqcn)) {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setName(className);
                classInfo.setPackageName(packageName);
                classInfo.setSourcePath(file.getAbsolutePath());
                classInfo.setType(type.toUpperCase());
                
                // 🎯 METADATAS DE RESCATE
                classInfo.addAnnotation("RescateBasico", "true");
                classInfo.addAnnotation("ParseOriginal", "Fallido");
                classInfo.addAnnotation("Lineas", basicInfo.getOrDefault("lines", "0"));
                classInfo.addAnnotation("Tamaño", basicInfo.getOrDefault("size", "0"));
                
                classMap.put(fqcn, classInfo);
            }
            
        } catch (Exception e) {
            bitacora.error("💥 Error en rescate básico: " + file.getName());
        }
    }

    /**
     * 🚨 MANEJAR ERROR OUT OF MEMORY
     */
    private void handleOutOfMemoryError(File file, Exception e) {
        bitacora.error("💾 CRÍTICO: Out of Memory en: " + file.getName());
        
        // 🎯 LIMPIEZA AGGRESIVA DE MEMORIA
        System.gc();
        
        // 🎯 RESCATE MÍNIMO
        emergencyRescue(file, e);
        
        // 🎯 PAUSA PREVENTIVA
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 🆘 RESCATE DE EMERGENCIA - Crear información básica cuando todo falla
     */
    private void emergencyRescue(File file, Exception error) {
        try {
            String fileName = file.getName();
            String className = fileName.replace(".java", "");
            String packageName = extractPackageFromPathEnhanced(file);
            
            String fqcn = packageName.isEmpty() ? className : packageName + "." + className;
            
            if (!classMap.containsKey(fqcn)) {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setName(className);
                classInfo.setPackageName(packageName);
                classInfo.setSourcePath(file.getAbsolutePath());
                classInfo.setType("CLASS");
                
                // 🎯 METADATAS DE EMERGENCIA
                classInfo.addAnnotation("EmergencyRescue", "true");
                classInfo.addAnnotation("ErrorType", error.getClass().getSimpleName());
                classInfo.addAnnotation("ErrorMessage", error.getMessage() != null ? error.getMessage() : "Unknown");
                classInfo.addAnnotation("FileSize", String.valueOf(file.length()));
                classInfo.addAnnotation("RescueTimestamp", String.valueOf(System.currentTimeMillis()));
                
                classMap.put(fqcn, classInfo);
                bitacora.debug("🆘 RESCATE DE EMERGENCIA: " + fqcn);
            }
            
        } catch (Exception rescueEx) {
            bitacora.error("💥 FALLO CRÍTICO EN RESCATE DE EMERGENCIA: " + file.getName());
        }
    }

    /**
     * 🛡️ MANEJAR ERROR GENERAL
     */
    private void handleGeneralError(File file, Exception e) {
        bitacora.error("⚡ Error procesando " + file.getName() + ": " + 
                      e.getClass().getSimpleName() + " - " + e.getMessage());
        
        // 🎯 RESCATE DE EMERGENCIA
        emergencyRescue(file, e);
    }

    /**
     * 🧹 LIMPIEZA DE SEGURIDAD PREVENTIVA
     */
    private void performSafetyCleanup() {
        // 🎯 LIMPIEZA PERIÓDICA CADA 50 ARCHIVOS
        if (filesProcessed.get() % 50 == 0) {
            System.gc();
            bitacora.debug("🧹 Limpieza de memoria preventiva realizada");
        }
    }

    /**
     * 🎯 VERIFICAR SI CONTENIDO PARECE SER JAVA VÁLIDO
     */
    private boolean isLikelyValidJava(String content) {
        if (content == null || content.trim().isEmpty()) return false;
        
        // 🎯 PATRONES CLAVE DE ARCHIVOS JAVA
        boolean hasJavaKeywords = content.contains("class") || 
                                 content.contains("interface") || 
                                 content.contains("enum") ||
                                 content.contains("package") ||
                                 content.contains("import");
        
        boolean hasReasonableLength = content.length() >= 50 && content.length() <= 100000;
        
        return hasJavaKeywords && hasReasonableLength;
    }

	/**
     * 📦 CREAR LOTES DE ARCHIVOS PARA PROCESAMIENTO
     */
    private List<List<Path>> createBatches(List<Path> files, int batchSize) {
        List<List<Path>> batches = new ArrayList<>();
        
        for (int i = 0; i < files.size(); i += batchSize) {
            int end = Math.min(i + batchSize, files.size());
            batches.add(new ArrayList<>(files.subList(i, end)));
        }
        
        return batches;
    }

    /**
     * 🎯 ALTERNATIVA: PROCESAMIENTO PARALELO POR GRUPOS PEQUEÑOS
     */
    private void procesarArchivosParaleloOptimizado(List<Path> javaFiles) {
        bitacora.info("⚡ PARALELISMO OPTIMIZADO - " + javaFiles.size() + " archivos");
        
        // 🎯 ESTRATEGIA: Semáforo para limitar concurrencia real
        Semaphore semaphore = new Semaphore(MAX_CONCURRENT_PARSERS);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        for (Path filePath : javaFiles) {
            // 🎯 ESPERAR SEMÁFORO PARA CONTROLAR CONCURRENCIA
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    processFileSafely(filePath);
                } finally {
                    // 🎯 LIBERAR SEMÁFORO SIEMPRE
                    semaphore.release();
                }
            }, turboExecutor);
            
            futures.add(future);
        }
        
        // 🎯 ESPERAR COMPLETACIÓN CON MANEJO DE ERRORES
        try {
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
            );
            
            // 🎯 TIMEOUT CON FALLBACK
            allFutures.get(3, TimeUnit.MINUTES);
            
        } catch (TimeoutException e) {
            bitacora.warn("⏰ Timeout en paralelismo, continuando con archivos restantes...");
        } catch (Exception e) {
            bitacora.error("💥 Error en procesamiento paralelo: " + e.getMessage());
        }
    }

    /**
     * 🎯 MÉTODO HÍBRIDO: PARALELO CON FALLBACK SECUENCIAL
     */
    private void procesarArchivosHibrido(List<Path> javaFiles) {
        if (javaFiles.size() <= 10) {
            // 🎯 POCOS ARCHIVOS: PROCESAR SECUENCIALMENTE
            bitacora.info("🔧 MODO SECUENCIAL - Pocos archivos (" + javaFiles.size() + ")");
            procesarArchivosSecuencial(javaFiles);
        } else {
            // 🎯 MUCHOS ARCHIVOS: USAR PARALELISMO SEGURO
            bitacora.info("⚡ MODO PARALELO SEGURO - Muchos archivos (" + javaFiles.size() + ")");
            procesarArchivosParaleloOptimizado(javaFiles);
        }
    }

    /**
     * 🔧 PROCESAMIENTO SECUENCIAL SIMPLE (para casos especiales)
     */
    private void procesarArchivosSecuencial(List<Path> javaFiles) {
        int processed = 0;
        int errors = 0;
        
        for (Path filePath : javaFiles) {
            try {
                processFileSafely(filePath);
                processed++;
            } catch (Exception e) {
                errors++;
                bitacora.error("💥 Error secuencial en: " + filePath.getFileName());
            }
            
            // 🎯 LOG DE PROGRESO CADA 10 ARCHIVOS
            if (processed % 10 == 0) {
                bitacora.debug("📊 Progreso: " + processed + "/" + javaFiles.size() + " procesados, " + errors + " errores");
            }
        }
        
        bitacora.info("✅ Procesamiento secuencial completado: " + processed + " ok, " + errors + " errores");
    }

    /**
     * 🎯 MÉTODO PRINCIPAL MEJORADO - CON DETECCIÓN AUTOMÁTICA
     */
    private void procesarArchivosParalelo(List<Path> javaFiles) {
        // 🎯 DETECTAR MODO ÓPTIMO BASADO EN CARACTERÍSTICAS DEL SISTEMA
        if (deberiaUsarModoSecuencial(javaFiles)) {
            procesarArchivosSecuencial(javaFiles);
        } else {
            procesarArchivosParaleloOptimizado(javaFiles);
        }
    }

    /**
     * 🧠 DECIDIR MODO DE PROCESAMIENTO INTELIGENTEMENTE
     */
    private boolean deberiaUsarModoSecuencial(List<Path> javaFiles) {
        // 🎯 CRITERIOS PARA USAR MODO SECUENCIAL:
        
        // 1. Pocos archivos
        if (javaFiles.size() <= 5) {
            return true;
        }
        
        // 2. Sistema con pocos recursos
        long maxMemory = Runtime.getRuntime().maxMemory();
        if (maxMemory < 512 * 1024 * 1024) { // Menos de 512MB
            return true;
        }
        
        // 3. Demasiados errores previos
        if (scanErrors.size() > javaFiles.size() * 0.5) { // Más del 50% de errores
            bitacora.warn("⚠️ Muchos errores previos, usando modo secuencial");
            return true;
        }
        
        // 4. Por defecto, usar paralelo
        return false;
    }

	/**
     * 🧹 RESET COMPLETO DEL ESTADO
     */
    private void resetScannerState() {
        classMap.clear();
        metadataMap.clear();
        scannedPackages.clear();
        scanErrors.clear();
        filesProcessed.set(0);
        rescueOperations.set(0);
    }


    /**
     * 🛡️ MANEJO DE ERRORES ROBUSTO
     */
    private void manejarErrorParse(String fileName, String error, File file) {
        String errorMsg = error + " en: " + fileName;
        scanErrors.add(errorMsg);
        bitacora.debug("⚠️ " + errorMsg);
        
        rescueOperations.incrementAndGet();
        intentarRescateAvanzado(file, error);
    }

    /**
     * 🆕 RESCATE AVANZADO CON ANÁLISIS DE BYTECODE
     */
    private void intentarRescateAvanzado(File file, String errorOriginal) {
        try {
            String fileName = file.getName();
            String className = fileName.replace(".java", "");
            String packageName = extraerPackageDesdeRutaAvanzado(file);
            String fqcn = packageName.isEmpty() ? className : packageName + "." + className;

            if (!classMap.containsKey(fqcn)) {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setName(className);
                classInfo.setPackageName(packageName);
                classInfo.setSourcePath(file.getAbsolutePath());
                classInfo.setType("CLASS_RESCUED");
                
                // 🎪 METADATAS DE RESCATE AVANZADAS
                classInfo.addAnnotation("RescateTurbo", "true");
                classInfo.addAnnotation("ErrorOriginal", errorOriginal);
                classInfo.addAnnotation("TimestampRescate", String.valueOf(System.currentTimeMillis()));
                classInfo.addAnnotation("TamañoArchivo", String.valueOf(file.length()));
                
                // 🆕 INTENTAR EXTRAER MÁS INFORMACIÓN DEL ARCHIVO
                extraerInfoBasicaArchivo(file, classInfo);
                
                classMap.put(fqcn, classInfo);
                bitacora.debug("🛡️ CLASE RESCATADA TURBO: " + fqcn);
            }
            
        } catch (Exception rescueEx) {
            bitacora.debug("💥 FALLO EN RESCATE TURBO: " + file.getName());
        }
    }

    /**
     * 🆕 EXTRAER INFORMACIÓN BÁSICA DEL ARCHIVO
     */
    private void extraerInfoBasicaArchivo(File file, ClassInfo classInfo) {
        try {
            // Leer primeras líneas para detectar package y class
            List<String> lines = Files.readAllLines(file.toPath(), java.nio.charset.StandardCharsets.UTF_8);
            
            for (String line : lines.subList(0, Math.min(20, lines.size()))) {
                String trimmed = line.trim();
                
                if (trimmed.startsWith("package ")) {
                    String pkg = trimmed.substring(8, trimmed.indexOf(';')).trim();
                    if (!pkg.isEmpty()) {
                        classInfo.setPackageName(pkg);
                    }
                }
                
                if (trimmed.startsWith("public class ") || trimmed.startsWith("class ") ||
                    trimmed.startsWith("public interface ") || trimmed.startsWith("interface ") ||
                    trimmed.startsWith("public enum ") || trimmed.startsWith("enum ") ||
                    trimmed.startsWith("public @interface ") || trimmed.startsWith("@interface ")) {
                    
                    // Intentar extraer el nombre de la clase
                    String[] parts = trimmed.split("\\s+");
                    for (int i = 0; i < parts.length; i++) {
                        if (parts[i].equals("class") || parts[i].equals("interface") || 
                            parts[i].equals("enum") || parts[i].equals("@interface")) {
                            if (i + 1 < parts.length) {
                                String name = parts[i + 1].split("[<{]")[0]; // Eliminar genéricos
                                if (!name.isEmpty()) {
                                    classInfo.setName(name);
                                }
                            }
                        }
                    }
                    break;
                }
            }
            
            classInfo.addAnnotation("LineasTotales", String.valueOf(lines.size()));
            
        } catch (Exception e) {
            // Ignorar errores en análisis básico
        }
    }

    /**
     * 🆕 EXTRAER PACKAGE AVANZADO
     */
    private String extraerPackageDesdeRutaAvanzado(File file) {
        try {
            Path filePath = file.toPath().toAbsolutePath();
            String pathStr = filePath.toString();
            
            // Patrones comunes de estructura de proyectos
            String[] patterns = {
                "src/main/java/",
                "src/test/java/", 
                "src/",
                "main/java/",
                "test/java/"
            };
            
            for (String pattern : patterns) {
                int patternIndex = pathStr.indexOf(pattern);
                if (patternIndex != -1) {
                    int start = patternIndex + pattern.length();
                    int end = pathStr.lastIndexOf(File.separator);
                    if (start < end) {
                        String packagePath = pathStr.substring(start, end);
                        return packagePath.replace(File.separator, ".");
                    }
                }
            }
            
            // Fallback: estructura de directorios convencional
            return extraerPackageDesdeEstructuraDirectorios(filePath);
            
        } catch (Exception e) {
            return "";
        }
    }

    private String extraerPackageDesdeEstructuraDirectorios(Path filePath) {
        List<String> packageParts = new ArrayList<>();
        Path current = filePath.getParent();
        
        while (current != null) {
            String dirName = current.getFileName().toString();
            if (dirName.equals("java") || dirName.equals("src")) {
                break;
            }
            packageParts.add(0, dirName);
            current = current.getParent();
        }
        
        return String.join(".", packageParts);
    }

    /**
     * 🚀 PROCESAR UNIDAD DE COMPILACIÓN MEJORADO - Con verificación robusta
     */
    private void procesarUnidadCompilacionTurbo(CompilationUnit unit, File file) {
        if (unit == null) {
            bitacora.warn("⚠️ Unidad de compilación nula para: " + file.getName());
            return;
        }
        
        try {
            String packageName = unit.getPackageDeclaration()
                    .map(pd -> pd.getNameAsString())
                    .orElse("");
                    
            scannedPackages.add(packageName);

            // 🎯 PROCESAR TODOS LOS TIPOS CON VERIFICACIÓN
            processAllTypesWithVerification(unit, packageName, file);
            
        } catch (Exception e) {
            bitacora.error("💥 Error en procesarUnidadCompilacionTurbo: " + file.getName() + " - " + e.getMessage());
            // 🛡️ INTENTAR RESCATE
            emergencyRescue(file, e);
        }
    }

    /**
     * 🎯 PROCESAR TODOS LOS TIPOS CON VERIFICACIÓN ROBUSTA
     */
    private void processAllTypesWithVerification(CompilationUnit unit, String packageName, File file) {
        int processedCount = 0;
        
        // 🎯 PROCESAR CLASES E INTERFACES
        List<ClassOrInterfaceDeclaration> classes = unit.findAll(ClassOrInterfaceDeclaration.class);
        for (ClassOrInterfaceDeclaration clazz : classes) {
            if (procesarClaseOInterfazVerificado(clazz, packageName, file, unit)) {
                processedCount++;
            }
        }
        
        // 🎯 PROCESAR ENUMS
        List<EnumDeclaration> enums = unit.findAll(EnumDeclaration.class);
        for (EnumDeclaration enumDecl : enums) {
            if (procesarEnumVerificado(enumDecl, packageName, file, unit)) {
                processedCount++;
            }
        }
        
        // 🎯 PROCESAR ANOTACIONES
        List<AnnotationDeclaration> annotations = unit.findAll(AnnotationDeclaration.class);
        for (AnnotationDeclaration annotation : annotations) {
            if (procesarAnotacionVerificada(annotation, packageName, file, unit)) {
                processedCount++;
            }
        }
        
        // 🎯 PROCESAR RECORDS
        List<RecordDeclaration> records = unit.findAll(RecordDeclaration.class);
        for (RecordDeclaration record : records) {
            if (procesarRecordVerificado(record, packageName, file, unit)) {
                processedCount++;
            }
        }
        
        bitacora.debug("📊 Procesados " + processedCount + " tipos en: " + file.getName());
    }

    /**
     * 🎯 PROCESAR ENUM CON VERIFICACIÓN ROBUSTA
     */
    private boolean procesarEnumVerificado(EnumDeclaration enumDecl, String packageName, File file, CompilationUnit unit) {
        try {
            String enumName = enumDecl.getNameAsString();
            String fqcn = packageName.isEmpty() ? enumName : packageName + "." + enumName;

            // 🎯 VERIFICAR SI YA EXISTE
            if (classMap.containsKey(fqcn)) {
                bitacora.debug("🔍 Enum duplicado omitido: " + fqcn);
                return false;
            }

            // 🎯 CREAR CLASSINFO PARA ENUM
            ClassInfo classInfo = crearClassInfoBasico(enumName, packageName, file, "ENUM");
            
            // 🎯 CONFIGURAR PROPIEDADES ESPECÍFICAS DE ENUM
            classInfo.setPublic(enumDecl.isPublic());
            classInfo.setFinal(true); // Los enums son finales por defecto

            // 🎯 PROCESAR CONSTANTES DEL ENUM
            procesarConstantesEnum(enumDecl, classInfo);
            
            // 🎯 PROCESAR MÉTODOS DEL ENUM
            procesarMetodosEnum(enumDecl, classInfo);

            // 🎯 AGREGAR AL MAPA
            classMap.put(fqcn, classInfo);
            bitacora.debug("✅ ENUM registrado: " + fqcn);
            
            return true;
            
        } catch (Exception e) {
            bitacora.error("💥 Error procesando enum: " + enumDecl.getNameAsString() + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * 🎯 PROCESAR CONSTANTES DE ENUM
     */
    private void procesarConstantesEnum(EnumDeclaration enumDecl, ClassInfo classInfo) {
        enumDecl.getEntries().forEach(entry -> {
            FieldInfo constant = new FieldInfo();
            constant.setName(entry.getNameAsString());
            constant.setType(enumDecl.getNameAsString()); // Tipo es el nombre del enum
            constant.setPublic(true);
            constant.setStatic(true);
            constant.setFinal(true);
            
            // 🎯 PROCESAR ANOTACIONES DE LA CONSTANTE
            entry.getAnnotations().forEach(ann -> {
                constant.addAnnotation(ann.getNameAsString(), ann.toString());
            });
            
            classInfo.addField(constant);
        });
    }

    /**
     * 🎯 PROCESAR MÉTODOS DE ENUM
     */
    private void procesarMetodosEnum(EnumDeclaration enumDecl, ClassInfo classInfo) {
        enumDecl.findAll(MethodDeclaration.class).forEach(method -> {
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setName(method.getNameAsString());
            methodInfo.setReturnType(method.getType().asString());
            methodInfo.setSignature(method.getDeclarationAsString());
            
            // 🎯 CONFIGURAR MODIFICADORES
            methodInfo.setPublic(method.isPublic());
            methodInfo.setPrivate(method.isPrivate());
            methodInfo.setProtected(method.isProtected());
            methodInfo.setStatic(method.isStatic());
            methodInfo.setFinal(method.isFinal());
            methodInfo.setAbstract(method.isAbstract());
            
            // 🎯 PROCESAR PARÁMETROS
            method.getParameters().forEach(param -> {
                ParameterInfo paramInfo = new ParameterInfo();
                paramInfo.setName(param.getNameAsString());
                paramInfo.setType(param.getType().asString());
                paramInfo.setFinal(param.isFinal());
                methodInfo.addParameter(paramInfo);
            });
            
            classInfo.addMethod(methodInfo);
        });
    }

    /**
     * 🎯 PROCESAR ANOTACIÓN CON VERIFICACIÓN ROBUSTA
     */
    private boolean procesarAnotacionVerificada(AnnotationDeclaration annotation, String packageName, File file, CompilationUnit unit) {
        try {
            String annotationName = annotation.getNameAsString();
            String fqcn = packageName.isEmpty() ? annotationName : packageName + "." + annotationName;

            // 🎯 VERIFICAR SI YA EXISTE
            if (classMap.containsKey(fqcn)) {
                bitacora.debug("🔍 Anotación duplicada omitida: " + fqcn);
                return false;
            }

            // 🎯 CREAR CLASSINFO PARA ANOTACIÓN
            ClassInfo classInfo = crearClassInfoBasico(annotationName, packageName, file, "ANNOTATION");
            
            // 🎯 CONFIGURAR PROPIEDADES ESPECÍFICAS DE ANOTACIÓN
            classInfo.setPublic(annotation.isPublic());
            classInfo.setInterface(true); // Las anotaciones son interfaces especiales

            // 🎯 PROCESAR ELEMENTOS DE ANOTACIÓN (métodos)
            procesarElementosAnotacion(annotation, classInfo);

            // 🎯 AGREGAR AL MAPA
            classMap.put(fqcn, classInfo);
            bitacora.debug("✅ ANNOTATION registrada: " + fqcn);
            
            return true;
            
        } catch (Exception e) {
            bitacora.error("💥 Error procesando anotación: " + annotation.getNameAsString() + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * 🎯 PROCESAR ELEMENTOS DE ANOTACIÓN (métodos)
     */
    private void procesarElementosAnotacion(AnnotationDeclaration annotation, ClassInfo classInfo) {
        annotation.findAll(MethodDeclaration.class).forEach(method -> {
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setName(method.getNameAsString());
            methodInfo.setReturnType(method.getType().asString());
            methodInfo.setSignature(method.getDeclarationAsString());
            methodInfo.setPublic(true); // Los métodos de anotación son públicos
            methodInfo.setAbstract(true); // Son abstractos por defecto
            
            // 🎯 DETECTAR VALOR POR DEFECTO - FORMA CORRECTA
            method.getAnnotationByName("Default").ifPresent(defaultAnnotation -> {
                // El valor por defecto está en los atributos de la anotación @Default
                defaultAnnotation.getChildNodes().stream()
                    .filter(node -> node instanceof NameExpr || node instanceof StringLiteralExpr || node instanceof FieldAccessExpr)
                    .findFirst()
                    .ifPresent(defaultValue -> {
                        methodInfo.addAnnotation("DefaultValue", defaultValue.toString());
                    });
            });
            
            // ALTERNATIVA: Buscar en todas las anotaciones del método
            method.getAnnotations().forEach(ann -> {
                if (ann.getNameAsString().equals("Default")) {
                    // Procesar el valor por defecto
                    if (ann.getChildNodes().size() > 1) {
                        String defaultValue = ann.getChildNodes().get(1).toString();
                        methodInfo.addAnnotation("DefaultValue", defaultValue);
                    }
                }
            });
            
            classInfo.addMethod(methodInfo);
        });
    }

    /**
     * 🎯 PROCESAR RECORD CON VERIFICACIÓN ROBUSTA
     */
    private boolean procesarRecordVerificado(RecordDeclaration record, String packageName, File file, CompilationUnit unit) {
        try {
            String recordName = record.getNameAsString();
            String fqcn = packageName.isEmpty() ? recordName : packageName + "." + recordName;

            // 🎯 VERIFICAR SI YA EXISTE
            if (classMap.containsKey(fqcn)) {
                bitacora.debug("🔍 Record duplicado omitido: " + fqcn);
                return false;
            }

            // 🎯 CREAR CLASSINFO PARA RECORD
            ClassInfo classInfo = crearClassInfoBasico(recordName, packageName, file, "RECORD");
            
            // 🎯 CONFIGURAR PROPIEDADES ESPECÍFICAS DE RECORD
            classInfo.setPublic(record.isPublic());
            classInfo.setFinal(true); // Los records son finales

            // 🎯 PROCESAR COMPONENTES DEL RECORD (campos automáticos)
            procesarComponentesRecord(record, classInfo);
            
            // 🎯 PROCESAR MÉTODOS DEL RECORD
            procesarMetodosRecord(record, classInfo);

            // 🎯 AGREGAR AL MAPA
            classMap.put(fqcn, classInfo);
            bitacora.debug("✅ RECORD registrado: " + fqcn);
            
            return true;
            
        } catch (Exception e) {
            bitacora.error("💥 Error procesando record: " + record.getNameAsString() + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * 🎯 PROCESAR COMPONENTES DE RECORD
     */
    private void procesarComponentesRecord(RecordDeclaration record, ClassInfo classInfo) {
        record.getParameters().forEach(param -> {
            FieldInfo field = new FieldInfo();
            field.setName(param.getNameAsString());
            field.setType(param.getType().asString());
            field.setPrivate(true); // Los componentes del record son privados finales
            field.setFinal(true);
            
            // 🎯 PROCESAR ANOTACIONES DEL COMPONENTE
            param.getAnnotations().forEach(ann -> {
                field.addAnnotation(ann.getNameAsString(), ann.toString());
            });
            
            classInfo.addField(field);
        });
    }

    /**
     * 🎯 PROCESAR MÉTODOS DE RECORD
     */
    private void procesarMetodosRecord(RecordDeclaration record, ClassInfo classInfo) {
        record.findAll(MethodDeclaration.class).forEach(method -> {
            // 🎯 OMITIR MÉTODOS GENERADOS AUTOMÁTICAMENTE (equals, hashCode, toString)
            if (isAutoGeneratedRecordMethod(method.getNameAsString())) {
                return;
            }
            
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setName(method.getNameAsString());
            methodInfo.setReturnType(method.getType().asString());
            methodInfo.setSignature(method.getDeclarationAsString());
            
            // 🎯 CONFIGURAR MODIFICADORES
            methodInfo.setPublic(method.isPublic());
            methodInfo.setPrivate(method.isPrivate());
            methodInfo.setProtected(method.isProtected());
            methodInfo.setStatic(method.isStatic());
            methodInfo.setFinal(method.isFinal());
            
            // 🎯 PROCESAR PARÁMETROS
            method.getParameters().forEach(param -> {
                ParameterInfo paramInfo = new ParameterInfo();
                paramInfo.setName(param.getNameAsString());
                paramInfo.setType(param.getType().asString());
                paramInfo.setFinal(param.isFinal());
                methodInfo.addParameter(paramInfo);
            });
            
            classInfo.addMethod(methodInfo);
        });
    }

    /**
     * 🎯 VERIFICAR SI ES MÉTODO GENERADO AUTOMÁTICAMENTE EN RECORD
     */
    private boolean isAutoGeneratedRecordMethod(String methodName) {
        return methodName.equals("equals") || 
               methodName.equals("hashCode") || 
               methodName.equals("toString") ||
               methodName.equals("component") ||
               methodName.startsWith("get");
    }

    /**
     * 🎯 CREAR CLASSINFO BÁSICO (método auxiliar reutilizable)
     */
    private ClassInfo crearClassInfoBasico(String name, String packageName, File file, String type) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setName(name);
        classInfo.setPackageName(packageName);
        classInfo.setSourcePath(file.getAbsolutePath());
        classInfo.setType(type);
        return classInfo;
    }

	/**
     * 🎯 PROCESAR CLASE/INTERFAZ CON VERIFICACIÓN
     */
    private boolean procesarClaseOInterfazVerificado(ClassOrInterfaceDeclaration clazz, String packageName, 
                                                   File file, CompilationUnit unit) {
        try {
            String className = clazz.getNameAsString();
            boolean isInterface = clazz.isInterface();
            boolean isInner = clazz.isNestedType();
            String type = isInner ? "INNER_CLASS" : (isInterface ? "INTERFACE" : "CLASS");
            String fqcn = packageName.isEmpty() ? className : packageName + "." + className;

            // 🎯 VERIFICAR SI YA EXISTE
            if (classMap.containsKey(fqcn)) {
                bitacora.debug("🔍 Clase duplicada omitida: " + fqcn);
                return false;
            }

            // 🎯 CREAR CLASSINFO
            ClassInfo classInfo = crearClassInfoBasico(className, packageName, file, type);
            
            // 🎯 CONFIGURAR PROPIEDADES BÁSICAS
            classInfo.setPublic(clazz.isPublic());
            classInfo.setAbstract(clazz.isAbstract());
            classInfo.setFinal(clazz.isFinal());
            classInfo.setStatic(clazz.isStatic());
            classInfo.setInterface(isInterface);

            // 🎯 AGREGAR AL MAPA
            classMap.put(fqcn, classInfo);
            bitacora.debug("✅ " + type + " registrada: " + fqcn);
            
            return true;
            
        } catch (Exception e) {
            bitacora.error("💥 Error procesando clase: " + clazz.getNameAsString() + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * 🎯 PROCESAMIENTO COMPLETO TURBO
     */
    private void procesarUnidadCompilacionCompletaTurbo(CompilationUnit unit, File file) {
        String packageName = unit.getPackageDeclaration()
                .map(pd -> pd.getNameAsString())
                .orElse("");

        scannedPackages.add(packageName);

        // 🆕 PROCESAR IMPORTS DETALLADOS
        List<String> imports = unit.getImports().stream()
                .map(importDecl -> {
                    if (importDecl.isAsterisk()) {
                        return importDecl.getNameAsString() + ".*";
                    } else if (importDecl.isStatic()) {
                        return "static " + importDecl.getNameAsString();
                    } else {
                        return importDecl.getNameAsString();
                    }
                })
                .collect(Collectors.toList());

        // 🆕 PROCESAR COMENTARIOS A NIVEL DE ARCHIVO
        unit.getComment().ifPresent(comment -> {
            // Los comentarios del archivo se pueden procesar aquí
        });

        // Procesar todos los tipos en paralelo
        List<Runnable> processingTasks = new ArrayList<>();
        
        processingTasks.add(() -> unit.findAll(ClassOrInterfaceDeclaration.class).forEach(clazz -> {
            procesarClaseOInterfazTurbo(clazz, packageName, file, unit, imports);
        }));

        processingTasks.add(() -> unit.findAll(EnumDeclaration.class).forEach(enumDecl -> {
            procesarEnumTurbo(enumDecl, packageName, file, unit, imports);
        }));

        processingTasks.add(() -> unit.findAll(AnnotationDeclaration.class).forEach(annotation -> {
            procesarAnotacionTurbo(annotation, packageName, file, unit, imports);
        }));

        processingTasks.add(() -> unit.findAll(RecordDeclaration.class).forEach(record -> {
            procesarRecordTurbo(record, packageName, file, unit, imports);
        }));

        // 🆕 PROCESAR MÉTODOS Y CAMPOS A NIVEL DE ARCHIVO (static imports)
        processingTasks.add(() -> procesarMiembrosNivelArchivo(unit, packageName, file, imports));

        // Ejecutar procesamiento en paralelo
        processingTasks.parallelStream().forEach(Runnable::run);
    }

    /**
     * 🆕 PROCESAR CLASE/INTERFAZ TURBO - CON ANÁLISIS COMPLETO
     */
    private void procesarClaseOInterfazTurbo(ClassOrInterfaceDeclaration clazz, String packageName, 
                                           File file, CompilationUnit unit, List<String> imports) {
        String className = clazz.getNameAsString();
        boolean isInterface = clazz.isInterface();
        boolean isInner = clazz.isNestedType();
        String type = isInner ? "inner" : (isInterface ? "interface" : "class");
        String fqcn = packageName.isEmpty() ? className : packageName + "." + className;

        if (classMap.containsKey(fqcn)) {
            return;
        }

        ClassInfo classInfo = crearClassInfoBasico(className, packageName, file, type);
        
        // 🆕 INFORMACIÓN COMPLETA DE LA CLASE
        imports.forEach(classInfo::addImport);
        
        // 🆕 MODIFICADORES DETALLADOS
        classInfo.setPublic(clazz.isPublic());
        classInfo.setAbstract(clazz.isAbstract());
        classInfo.setFinal(clazz.isFinal());
        classInfo.setStatic(clazz.isStatic());
        classInfo.setStrictfp(clazz.isStrictfp());
        
        // 🆕 TIPO PARÁMETROS (GENÉRICOS)
        clazz.getTypeParameters().forEach(typeParam -> {
            classInfo.addTypeParameter(typeParam.getNameAsString());
        });

        // 🆕 COMENTARIOS DE LA CLASE
        clazz.getComment().ifPresent(comment -> {
            classInfo.setClassComment(comment.getContent());
        });

        // 🆕 JAVADOC
        clazz.getJavadoc().ifPresent(javadoc -> {
            classInfo.setJavadoc(javadoc.getDescription().toText());
        });

        // Procesamiento completo
        procesarCamposTurbo(clazz, classInfo);
        procesarMetodosTurbo(clazz, classInfo);
        procesarConstructoresTurbo(clazz, classInfo);
        procesarAnotacionesTurbo(clazz, classInfo);
        procesarHerenciaTurbo(clazz, classInfo);
        procesarClasesInternasTurbo(clazz, classInfo, packageName, file, imports);

        classMap.put(fqcn, classInfo);
        crearMetadataAvanzada(classInfo, clazz);
    }

    private void procesarClasesInternasTurbo(ClassOrInterfaceDeclaration clazz, ClassInfo classInfo, String packageName,
			File file, List<String> imports) {
		// TODO Auto-generated method stub
		
	}

	/**
     * 🆕 PROCESAR CAMPOS TURBO - ANÁLISIS EXHAUSTIVO
     */
    private void procesarCamposTurbo(ClassOrInterfaceDeclaration clazz, ClassInfo classInfo) {
        clazz.findAll(FieldDeclaration.class).forEach(field -> {
            field.getVariables().forEach(var -> {
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setName(var.getNameAsString());
                fieldInfo.setType(field.getElementType().asString());
                
                // 🆕 VALOR INICIAL
                var.getInitializer().ifPresent(init -> {
                    fieldInfo.setInitialValue(init.toString());
                });

                // 🆕 MODIFICADORES COMPLETOS
                fieldInfo.setPublic(field.isPublic());
                fieldInfo.setPrivate(field.isPrivate());
                fieldInfo.setProtected(field.isProtected());
                fieldInfo.setStatic(field.isStatic());
                fieldInfo.setFinal(field.isFinal());
                fieldInfo.setTransient(field.isTransient());
                fieldInfo.setVolatile(field.isVolatile());

                // 🆕 COMENTARIOS DEL CAMPO
                field.getComment().ifPresent(comment -> {
                    fieldInfo.setComment(comment.getContent());
                });

                // 🆕 PROCESAR ANOTACIONES COMPLETAS
                field.getAnnotations().forEach(ann -> {
                    Map<String, String> annotationParams = new HashMap<>();
                    if (ann.isNormalAnnotationExpr()) {
                        ann.asNormalAnnotationExpr().getPairs().forEach(pair -> {
                            annotationParams.put(pair.getNameAsString(), pair.getValue().toString());
                        });
                    }
                    fieldInfo.addAnnotation(ann.getNameAsString(), ann.toString(), annotationParams);
                });

                classInfo.addField(fieldInfo);
            });
        });
    }

    /**
     * 🆕 PROCESAR MÉTODOS TURBO - ANÁLISIS DETALLADO
     */
    private void procesarMetodosTurbo(ClassOrInterfaceDeclaration clazz, ClassInfo classInfo) {
        clazz.findAll(MethodDeclaration.class).forEach(method -> {
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setName(method.getNameAsString());
            methodInfo.setReturnType(method.getType().asString());
            methodInfo.setSignature(method.getDeclarationAsString());
            
            // 🆕 CUERPO DEL MÉTODO
            method.getBody().ifPresent(body -> {
                methodInfo.setBody(body.toString());
                methodInfo.setLineCount(body.getAllContainedComments().size() + 1); // Estimación
            });

            // 🆕 MODIFICADORES COMPLETOS
            methodInfo.setPublic(method.isPublic());
            methodInfo.setPrivate(method.isPrivate());
            methodInfo.setProtected(method.isProtected());
            methodInfo.setStatic(method.isStatic());
            methodInfo.setFinal(method.isFinal());
            methodInfo.setAbstract(method.isAbstract());
            methodInfo.setSynchronized(method.isSynchronized());
            methodInfo.setNative(method.isNative());
            methodInfo.setStrictfp(method.isStrictfp());
            methodInfo.setDefault(method.isDefault());

            // 🆕 TIPO PARÁMETROS (GENÉRICOS DEL MÉTODO)
            method.getTypeParameters().forEach(typeParam -> {
                methodInfo.addTypeParameter(typeParam.getNameAsString());
            });

            // 🆕 PROCESAR PARÁMETROS DETALLADOS
            method.getParameters().forEach(param -> {
                ParameterInfo paramInfo = new ParameterInfo();
                paramInfo.setName(param.getNameAsString());
                paramInfo.setType(param.getType().asString());
                paramInfo.setFinal(param.isFinal());
                paramInfo.setVarArgs(param.isVarArgs());
                
                // 🆕 ANOTACIONES DE PARÁMETROS
                param.getAnnotations().forEach(ann -> {
                    paramInfo.addAnnotation(ann.getNameAsString(), ann.toString());
                });

                methodInfo.addParameter(paramInfo);
            });
            
            // 🆕 EXCEPCIONES
            method.getThrownExceptions().forEach(exception -> {
                methodInfo.addException(exception.asString());
            });

            // 🆕 COMENTARIOS Y JAVADOC
            method.getComment().ifPresent(comment -> {
                methodInfo.setComment(comment.getContent());
            });
            
            method.getJavadoc().ifPresent(javadoc -> {
                methodInfo.setJavadoc(javadoc.getDescription().toText());
            });

            // 🆕 ANOTACIONES COMPLETAS DEL MÉTODO
            method.getAnnotations().forEach(ann -> {
                Map<String, String> annotationParams = new HashMap<>();
                if (ann.isNormalAnnotationExpr()) {
                    ann.asNormalAnnotationExpr().getPairs().forEach(pair -> {
                        annotationParams.put(pair.getNameAsString(), pair.getValue().toString());
                    });
                }
                methodInfo.addAnnotation(ann.getNameAsString(), ann.toString(), annotationParams);
            });

            classInfo.addMethod(methodInfo);
        });
    }

    /**
     * 🆕 PROCESAR CONSTRUCTORES
     */
    private void procesarConstructoresTurbo(ClassOrInterfaceDeclaration clazz, ClassInfo classInfo) {
        clazz.findAll(ConstructorDeclaration.class).forEach(constructor -> {
            MethodInfo constructorInfo = new MethodInfo();
            constructorInfo.setName(constructor.getNameAsString());
            constructorInfo.setReturnType("void"); // Los constructores no tienen return type explícito
            constructorInfo.setSignature(constructor.getDeclarationAsString());
            constructorInfo.setConstructor(true);

            // 🆕 MODIFICADORES
            constructorInfo.setPublic(constructor.isPublic());
            constructorInfo.setPrivate(constructor.isPrivate());
            constructorInfo.setProtected(constructor.isProtected());

            // 🆕 PARÁMETROS
            constructor.getParameters().forEach(param -> {
                ParameterInfo paramInfo = new ParameterInfo();
                paramInfo.setName(param.getNameAsString());
                paramInfo.setType(param.getType().asString());
                paramInfo.setFinal(param.isFinal());
                paramInfo.setVarArgs(param.isVarArgs());
                
                param.getAnnotations().forEach(ann -> {
                    paramInfo.addAnnotation(ann.getNameAsString(), ann.toString());
                });

                constructorInfo.addParameter(paramInfo);
            });

            // 🆕 EXCEPCIONES
            constructor.getThrownExceptions().forEach(exception -> {
                constructorInfo.addException(exception.asString());
            });

            classInfo.addMethod(constructorInfo);
        });
    }

    /**
     * 🆕 PROCESAR HERENCIA TURBO
     */
    private void procesarHerenciaTurbo(ClassOrInterfaceDeclaration clazz, ClassInfo classInfo) {
        // Superclase con genéricos
        clazz.getExtendedTypes().forEach(extended -> {
            classInfo.setSuperClass(extended.toString()); // Incluye genéricos
        });
        
        // Interfaces con genéricos
        clazz.getImplementedTypes().forEach(implemented -> {
            classInfo.addInterface(implemented.toString()); // Incluye genéricos
        });
    }

    /**
     * 🆕 PROCESAR ANOTACIONES TURBO
     */
    private void procesarAnotacionesTurbo(BodyDeclaration<?> declaration, ClassInfo classInfo) {
        declaration.getAnnotations().forEach(ann -> {
            Map<String, String> annotationParams = new HashMap<>();
            if (ann.isNormalAnnotationExpr()) {
                ann.asNormalAnnotationExpr().getPairs().forEach(pair -> {
                    annotationParams.put(pair.getNameAsString(), pair.getValue().toString());
                });
            } else if (ann.isSingleMemberAnnotationExpr()) {
                annotationParams.put("value", ann.asSingleMemberAnnotationExpr().getMemberValue().toString());
            }
            classInfo.addAnnotation(ann.getNameAsString(), ann.toString(), annotationParams);
        });
    }

    // ... (MÉTODOS SIMILARES TURBO PARA ENUMS, RECORDS, ANOTACIONES)

    /**
     * 🆕 PROCESAR MIEMBROS A NIVEL DE ARCHIVO
     */
    private void procesarMiembrosNivelArchivo(CompilationUnit unit, String packageName, File file, List<String> imports) {
        // Procesar campos y métodos a nivel de archivo (static imports, etc.)
        unit.findAll(FieldDeclaration.class).stream()
            .filter(field -> field.getParentNode().isPresent() && field.getParentNode().get() instanceof CompilationUnit)
            .forEach(field -> {
                // Estos son campos a nivel de archivo (raro en Java, pero posible)
            });

        unit.findAll(MethodDeclaration.class).stream()
            .filter(method -> method.getParentNode().isPresent() && method.getParentNode().get() instanceof CompilationUnit)
            .forEach(method -> {
                // Métodos a nivel de archivo
            });
    }

    /**
     * 🆕 METADATA AVANZADA
     */
    private void crearMetadataAvanzada(ClassInfo classInfo, ClassOrInterfaceDeclaration clazz) {
        ClassMetadata metadata = new ClassMetadata(classInfo.getFullName());
        
        // 🆕 MÉTRICAS AVANZADAS
        metadata.setComplexityScore(calcularComplejidadAvanzada(clazz));
        metadata.setMaintainabilityIndex(calcularMantenibilidadAvanzada(classInfo));
        metadata.setCohesionScore(calcularCohesion(clazz));
        metadata.setCouplingScore(calcularAcoplamiento(classInfo));
        
        // 🆕 DETECCIÓN DE PATRONES
        detectarPatronesDiseño(classInfo, clazz, metadata);
        detectarCodeSmellsAvanzados(classInfo, clazz, metadata);
        
        metadataMap.put(classInfo.getFullName(), metadata);
    }

    /**
     * 🆕 ANÁLISIS DE DEPENDENCIAS TURBO
     */
    private void analizarDependenciasTurbo() {
        classMap.values().parallelStream().forEach(classInfo -> {
            ClassMetadata metadata = metadataMap.get(classInfo.getFullName());
            if (metadata != null) {
                // Análisis completo de dependencias
                analizarDependenciasCompletas(classInfo, metadata);
            }
        });
    }

    /**
     * 🆕 GENERAR REPORTE TURBO
     */
    private void generarReporteTurbo(long tiempoTotal) {
        Map<String, Integer> stats = getStatistics();
        double tasaExito = filesProcessed.get() > 0 ? 
            (double) (filesProcessed.get() - scanErrors.size()) / filesProcessed.get() * 100 : 0;
        
        bitacora.exito(String.format(
            "🎉 ESCANEO TURBO COMPLETADO: %d clases, %d archivos procesados, %d rescates | " +
            "%.1f%% éxito en %dms", 
            classMap.size(), filesProcessed.get(), rescueOperations.get(),
            tasaExito, tiempoTotal
        ));
        
        // 🆕 REPORTE DETALLADO
        if (!scanErrors.isEmpty()) {
            bitacora.info("📋 RESUMEN TURBO:");
            bitacora.info("  • Archivos con error: " + scanErrors.size());
            bitacora.info("  • Operaciones de rescate: " + rescueOperations.get());
            bitacora.info("  • Tasa de rescate: " + 
                String.format("%.1f%%", (double) rescueOperations.get() / Math.max(1, scanErrors.size()) * 100));
        }
    }

    // ... (LOS MÉTODOS ORIGINALES SE MANTIENEN, SOLO SE AÑADEN VERSIONES TURBO)

    /**
     * 🆕 MÉTRICAS AVANZADAS
     */
    private int calcularComplejidadAvanzada(ClassOrInterfaceDeclaration clazz) {
        int complexity = 0;
        
        // Complejidad ciclomática de métodos
        complexity += clazz.findAll(MethodDeclaration.class).stream()
                .mapToInt(method -> calcularComplejidadCiclomatica(method))
                .sum();
        
        // Complejidad estructural
        complexity += clazz.findAll(FieldDeclaration.class).size() * 2;
        complexity += clazz.findAll(ClassOrInterfaceDeclaration.class).size() * 3; // Clases internas
        
        return complexity;
    }

    private double calcularMantenibilidadAvanzada(ClassInfo classInfo) {
        double baseMaintainability = 100.0;
        baseMaintainability -= classInfo.getMethodCount() * 0.8;
        baseMaintainability -= classInfo.getFieldCount() * 0.3;
        baseMaintainability -= classInfo.getInnerClassCount() * 1.5;
        return Math.max(0, baseMaintainability);
    }

    private double calcularCohesion(ClassOrInterfaceDeclaration clazz) {
        // Métrica LCOM (Lack of Cohesion in Methods) simplificada
        int methodCount = clazz.findAll(MethodDeclaration.class).size();
        int fieldCount = clazz.findAll(FieldDeclaration.class).size();
        
        if (methodCount == 0 || fieldCount == 0) return 1.0;
        
        // Cálculo simplificado de cohesión
        return Math.min(1.0, (double) methodCount / fieldCount);
    }

    private int calcularAcoplamiento(ClassInfo classInfo) {
        return classInfo.getImports().size() + 
               (classInfo.getSuperClass() != null ? 1 : 0) + 
               classInfo.getInterfaces().size();
    }

    /**
     * 🆕 DETECCIÓN DE PATRONES DE DISEÑO
     */
    private void detectarPatronesDiseño(ClassInfo classInfo, ClassOrInterfaceDeclaration clazz, ClassMetadata metadata) {
        List<String> patrones = new ArrayList<>();
        
        // Detectar Singleton
        if (esProbableSingleton(clazz)) {
            patrones.add("Singleton");
        }
        
        // Detectar Factory
        if (esProbableFactory(clazz)) {
            patrones.add("Factory");
        }
        
        // Detectar Builder
        if (esProbableBuilder(clazz)) {
            patrones.add("Builder");
        }
        
        metadata.setDesignPatterns(patrones);
    }

    private boolean esProbableSingleton(ClassOrInterfaceDeclaration clazz) {
        // Un singleton típico tiene un campo static de su propio tipo
        boolean tieneCampoStaticInstancia = clazz.findAll(FieldDeclaration.class).stream()
                .anyMatch(field -> field.isStatic() && 
                                  field.getElementType().asString().equals(clazz.getNameAsString()));
        
        boolean tieneConstructorPrivado = clazz.findAll(ConstructorDeclaration.class).stream()
                .anyMatch(constructor -> constructor.isPrivate());
        
        return tieneCampoStaticInstancia && tieneConstructorPrivado;
    }

    private boolean esProbableFactory(ClassOrInterfaceDeclaration clazz) {
        String className = clazz.getNameAsString();
        boolean nombreSugiereFactory = className.toLowerCase().contains("factory");
        
        boolean tieneMetodosCreacionales = clazz.findAll(MethodDeclaration.class).stream()
                .anyMatch(method -> method.isStatic() && 
                                   method.getType().asString().contains("create"));
        
        return nombreSugiereFactory || tieneMetodosCreacionales;
    }

    private boolean esProbableBuilder(ClassOrInterfaceDeclaration clazz) {
        String className = clazz.getNameAsString();
        boolean nombreSugiereBuilder = className.toLowerCase().contains("builder");
        
        boolean tieneMetodosFluent = clazz.findAll(MethodDeclaration.class).stream()
                .anyMatch(method -> method.getType().asString().equals(clazz.getNameAsString()));
        
        return nombreSugiereBuilder || tieneMetodosFluent;
    }

    /**
     * 🆕 DETECCIÓN DE CODE SMELLS AVANZADOS
     */
    private void detectarCodeSmellsAvanzados(ClassInfo classInfo, ClassOrInterfaceDeclaration clazz, ClassMetadata metadata) {
        List<String> smells = new ArrayList<>();

        // God Class
        if (classInfo.getMethodCount() > 30 || classInfo.getFieldCount() > 20) {
            smells.add("God Class - Demasiada responsabilidad");
        }

        // Feature Envy
        detectarFeatureEnvy(clazz, smells);
        
        // Long Method
        clazz.findAll(MethodDeclaration.class).forEach(method -> {
            if (method.getBody().isPresent()) {
                int statementCount = method.getBody().get().getStatements().size();
                if (statementCount > 50) {
                    smells.add("Long Method: " + method.getNameAsString() + " (" + statementCount + " statements)");
                }
            }
        });

        // Duplicated Code (simplificado)
        detectarCodigoDuplicado(clazz, smells);

        metadata.setCodeSmells(smells);
        metadata.setNeedsRefactoring(!smells.isEmpty());
        metadata.setRefactoringPriority(calcularPrioridadRefactoring(smells.size(), classInfo));
    }

    private void detectarFeatureEnvy(ClassOrInterfaceDeclaration clazz, List<String> smells) {
        clazz.findAll(MethodDeclaration.class).forEach(method -> {
            Set<String> externalTypes = new HashSet<>();
            Set<String> internalTypes = new HashSet<>();
            
            // Análisis simplificado de feature envy
            method.findAll(MethodCallExpr.class).forEach(call -> {
                call.getScope().ifPresent(scope -> {
                    String scopeType = scope.toString();
                    if (!scopeType.equals("this")) {
                        externalTypes.add(scopeType);
                    } else {
                        internalTypes.add(scopeType);
                    }
                });
            });
            
            if (externalTypes.size() > internalTypes.size() * 2) {
                smells.add("Feature Envy: " + method.getNameAsString());
            }
        });
    }

    private void detectarCodigoDuplicado(ClassOrInterfaceDeclaration clazz, List<String> smells) {
        // Detección básica de código duplicado por similitud de nombres de métodos
        Map<String, Long> methodNamePatterns = clazz.findAll(MethodDeclaration.class).stream()
                .map(method -> method.getNameAsString().toLowerCase())
                .collect(Collectors.groupingBy(name -> name, Collectors.counting()));
        
        methodNamePatterns.entrySet().stream()
                .filter(entry -> entry.getValue() > 3)
                .forEach(entry -> {
                    smells.add("Posible código duplicado: patrón '" + entry.getKey() + "' repetido " + entry.getValue() + " veces");
                });
    }

    private String calcularPrioridadRefactoring(int smellCount, ClassInfo classInfo) {
        if (smellCount > 10) return "CRITICAL";
        if (smellCount > 5) return "HIGH";
        if (smellCount > 2) return "MEDIUM";
        if (smellCount > 0) return "LOW";
        return "NONE";
    }

    /**
     * 🆕 DETECTAR PATRONES ARQUITECTÓNICOS
     */
    private void detectarPatronesArquitectonicos() {
        // Análisis de la estructura general del proyecto
        detectarCapasArquitectonicas();
        detectarPatronesInversiónDependencias();
    }

    private void detectarCapasArquitectonicas() {
        Map<String, Long> packagePatterns = scannedPackages.stream()
                .collect(Collectors.groupingBy(
                        pkg -> {
                            if (pkg.contains(".controller.") || pkg.contains(".web.")) return "CONTROLLER";
                            if (pkg.contains(".service.") || pkg.contains(".business.")) return "SERVICE";
                            if (pkg.contains(".repository.") || pkg.contains(".dao.")) return "REPOSITORY";
                            if (pkg.contains(".model.") || pkg.contains(".entity.")) return "MODEL";
                            return "OTHER";
                        },
                        Collectors.counting()
                ));
        
        bitacora.info("🏗️  Patrones arquitectónicos detectados: " + packagePatterns);
    }

    private void detectarPatronesInversiónDependencias() {
        long interfacesCount = getClassesByType("interface").size();
        long classesCount = getClassesByType("class").size();
        
        double inversionRatio = (double) interfacesCount / Math.max(1, classesCount);
        if (inversionRatio > 0.3) {
            bitacora.info("🎯 Alta tasa de inversión de dependencias detectada: " + String.format("%.2f", inversionRatio));
        }
    }

    // ... (TODOS LOS MÉTODOS ORIGINALES SE MANTIENEN EXACTAMENTE IGUALES)

    // 📊 MÉTODOS DE ANÁLISIS ORIGINALES - PRESERVADOS
    public static Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total_classes", classMap.size());
        stats.put("total_interfaces", getClassesByType("interface").size());
        stats.put("total_enums", getClassesByType("enum").size());
        stats.put("total_annotations", getClassesByType("annotation").size());
        stats.put("total_records", getClassesByType("record").size());
        stats.put("scan_errors", scanErrors.size());
        stats.put("scanned_packages", scannedPackages.size());
        
        int totalMethods = classMap.values().stream()
                .mapToInt(ClassInfo::getMethodCount)
                .sum();
        int totalFields = classMap.values().stream()
                .mapToInt(ClassInfo::getFieldCount)
                .sum();
        
        stats.put("total_methods", totalMethods);
        stats.put("total_fields", totalFields);
        
        return stats;
    }

    public static String generarResumenCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("📊 RESUMEN COMPLETO DEL PROYECTO\n");
        sb.append("================================\n\n");
        
        Map<String, Integer> stats = getStatistics();
        sb.append("ESTADÍSTICAS GENERALES:\n");
        stats.forEach((key, value) -> {
            sb.append("  • ").append(key).append(": ").append(value).append("\n");
        });
        sb.append("\n");
        
        sb.append("CLASES DETECTADAS:\n");
        getClasses().forEach(cls -> {
            String emoji = switch(cls.getType().toUpperCase()) {
                case "INTERFACE" -> "📜";
                case "ENUM" -> "🎯";
                case "ANNOTATION" -> "🏷️";
                case "RECORD" -> "💾";
                case "INNER-CLASS", "INNER-INTERFACE" -> "🔗";
                default -> "📦";
            };
            
            sb.append(emoji).append(" ").append(cls.getType().toUpperCase())
              .append(": ").append(cls.getFullName()).append("\n");
              
            Optional<ClassMetadata> metadata = getMetadataForClass(cls.getFullName());
            metadata.ifPresent(md -> {
                if (md.hasCodeSmells()) {
                    sb.append("   ⚠️  Code smells: ").append(md.getCodeSmells().size())
                      .append(", Prioridad: ").append(md.getRefactoringPriority()).append("\n");
                }
            });

            if (cls.hasFields()) {
                sb.append("   📌 Campos: ").append(cls.getFieldCount()).append("\n");
            }

            if (cls.hasMethods()) {
                sb.append("   ⚡ Métodos: ").append(cls.getMethodCount()).append("\n");
            }

            sb.append("\n");
        });

        List<ClassMetadata> needsRefactor = getClassMetadata().stream()
                .filter(ClassMetadata::isNeedsRefactoring)
                .filter(ClassMetadata::isHighPriority)
                .collect(Collectors.toList());
                
        if (!needsRefactor.isEmpty()) {
            sb.append("🚨 CLASES QUE NECESITAN REFACTORIZACIÓN URGENTE:\n");
            needsRefactor.forEach(md -> {
                sb.append("  • ").append(md.getFullyQualifiedName())
                  .append(" (").append(md.getCodeSmells().size()).append(" smells)\n");
            });
        }

        return sb.toString();
    }

    public String analisisRapido() {
        StringBuilder sb = new StringBuilder();
        sb.append("⚡ ANÁLISIS RÁPIDO DEL PROYECTO\n");
        sb.append("==============================\n");
        
        getStatistics().forEach((key, value) -> {
            sb.append("  ").append(key).append(": ").append(value).append("\n");
        });
        
        long classesConProblemas = getClassMetadata().stream()
                .filter(ClassMetadata::isNeedsRefactoring)
                .count();
                
        sb.append("  classes_con_problemas: ").append(classesConProblemas).append("\n");
        sb.append("  tasa_problemas: ").append(String.format("%.1f%%", 
            (double) classesConProblemas / Math.max(1, classMap.size()) * 100)).append("\n");
            
        return sb.toString();
    }

    public static Optional<ClassMetadata> getMetadataForClass(String fullyQualifiedName) {
        try {
            if (fullyQualifiedName == null || fullyQualifiedName.trim().isEmpty()) {
                return Optional.empty();
            }
            
            if (metadataMap != null && metadataMap.containsKey(fullyQualifiedName)) {
                ClassMetadata metadata = metadataMap.get(fullyQualifiedName);
                return Optional.ofNullable(metadata);
            }
            
            if (metadataMap != null) {
                Optional<String> matchingKey = metadataMap.keySet().stream()
                    .filter(key -> key.equalsIgnoreCase(fullyQualifiedName))
                    .findFirst();
                    
                if (matchingKey.isPresent()) {
                    return Optional.ofNullable(metadataMap.get(matchingKey.get()));
                }
            }
            
            return Optional.empty();
            
        } catch (Exception e) {
            System.err.println("⚠️ Error obteniendo metadata para: " + fullyQualifiedName);
            System.err.println("   Detalles: " + e.getMessage());
            return Optional.empty();
        }
    }

    public static List<ClassInfo> getClassesByType(String type) {
        try {
            if (type == null || type.trim().isEmpty()) {
                return Collections.emptyList();
            }
            
            if (classMap != null && !classMap.isEmpty()) {
                return classMap.values().stream()
                    .filter(Objects::nonNull)
                    .filter(cls -> {
                        try {
                            String clsType = cls.getType();
                            return clsType != null && type.equalsIgnoreCase(clsType);
                        } catch (Exception ex) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
            }
            
            return Collections.emptyList();
            
        } catch (Exception e) {
            System.err.println("⚠️ Error obteniendo clases por tipo: " + type);
            System.err.println("   Detalles: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // 🆕 MÉTODOS AUXILIARES TURBO
    private void intentarRegistroMinimoTurbo(CompilationUnit unit, File file, Exception error) {
        try {
            String packageName = unit.getPackageDeclaration()
                    .map(pd -> pd.getNameAsString())
                    .orElse("");
            
            Optional<String> nombreClase = unit.findAll(ClassOrInterfaceDeclaration.class)
                    .stream()
                    .findFirst()
                    .map(clazz -> clazz.getNameAsString());
            
            String className = nombreClase.orElse("ClaseNoProcesada_" + file.getName().replace(".java", ""));
            String fqcn = packageName.isEmpty() ? className : packageName + "." + className;
            
            if (!classMap.containsKey(fqcn)) {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setName(className);
                classInfo.setPackageName(packageName);
                classInfo.setSourcePath(file.getAbsolutePath());
                classInfo.setType("CLASS");
                
                classInfo.addAnnotation("RescateTurbo", "true");
                classInfo.addAnnotation("ErrorOriginal", error.getClass().getSimpleName());
                
                classMap.put(fqcn, classInfo);
                bitacora.debug("🛡️ CLASE RESCATADA TURBO: " + fqcn);
            }
            
        } catch (Exception rescueEx) {
            bitacora.debug("💥 FALLO CRÍTICO EN RESCATE TURBO: " + file.getName());
        }
    }

    private void analizarDependenciasCompletas(ClassInfo classInfo, ClassMetadata metadata) {
        if (classInfo.getImports() != null) {
            classInfo.getImports().forEach(metadata::addDependency);
        }
        
        if (classInfo.getSuperClass() != null) {
            metadata.addDependency(classInfo.getSuperClass());
        }
        
        if (classInfo.getInterfaces() != null) {
            classInfo.getInterfaces().forEach(metadata::addDependency);
        }
    }

    private void calcularMetricasAvanzadas() {
        metadataMap.values().forEach(metadata -> {
            metadata.addQualityMetric("stability", metadata.calculateStability());
            metadata.addQualityMetric("dependency_count", metadata.getDependencyCount());
            metadata.addQualityMetric("dependent_count", metadata.getDependentCount());
            metadata.addQualityMetric("abstractness", calcularAbstractness(metadata));
        });
    }

    private double calcularAbstractness(ClassMetadata metadata) {
        // Calcular qué tan abstracta es una clase basado en métodos abstractos vs concretos
        Optional<ClassInfo> classInfo = getClassByName(metadata.getFullyQualifiedName());
        if (classInfo.isPresent()) {
            long abstractMethods = classInfo.get().getMethods().stream()
                    .filter(MethodInfo::isAbstract)
                    .count();
            long totalMethods = classInfo.get().getMethodCount();
            return totalMethods > 0 ? (double) abstractMethods / totalMethods : 0.0;
        }
        return 0.0;
    }

    // ... (MÉTODOS ORIGINALES PARA PROCESAR ENUMS, RECORDS, ANOTACIONES - VERSIÓN TURBO)

    private void procesarEnumTurbo(EnumDeclaration enumDecl, String packageName, File file, CompilationUnit unit, List<String> imports) {
        String enumName = enumDecl.getNameAsString();
        String fqcn = packageName.isEmpty() ? enumName : packageName + "." + enumName;

        if (classMap.containsKey(fqcn)) return;

        ClassInfo classInfo = crearClassInfoBasico(enumName, packageName, file, "enum");
        imports.forEach(classInfo::addImport);
        
        classInfo.setPublic(enumDecl.isPublic());
        classInfo.setFinal(true);

        // Procesar constantes del enum
        enumDecl.getEntries().forEach(entry -> {
            FieldInfo constant = new FieldInfo();
            constant.setName(entry.getNameAsString());
            constant.setType(enumName);
            constant.setPublic(true);
            constant.setStatic(true);
            constant.setFinal(true);
            
            // 🆕 ANOTACIONES EN CONSTANTES ENUM
            entry.getAnnotations().forEach(ann -> {
                constant.addAnnotation(ann.getNameAsString(), ann.toString());
            });
            
            classInfo.addField(constant);
        });

        procesarMetodosTurbo(enumDecl, classInfo);
        classMap.put(fqcn, classInfo);
    }

    /**
     * ⚡ PROCESAR MÉTODOS TURBO PARA ENUMS - ANÁLISIS EXHAUSTIVO
     */
    private void procesarMetodosTurbo(EnumDeclaration enumDecl, ClassInfo classInfo) {
        if (enumDecl == null || classInfo == null) return;
        
        enumDecl.findAll(MethodDeclaration.class).forEach(method -> {
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setName(method.getNameAsString());
            methodInfo.setReturnType(method.getType().asString());
            methodInfo.setSignature(method.getDeclarationAsString());
            
            // 🎯 CUERPO DEL MÉTODO CON ANÁLISIS DE COMPLEJIDAD
            method.getBody().ifPresent(body -> {
                methodInfo.setBody(body.toString());
                methodInfo.setLineCount(calcularLineasMetodo(body));
                methodInfo.setComplexity(calcularComplejidadCiclomatica(method));
            });

            // 🎯 MODIFICADORES COMPLETOS CON DETECCIÓN DE PATRONES
            methodInfo.setPublic(method.isPublic());
            methodInfo.setPrivate(method.isPrivate());
            methodInfo.setProtected(method.isProtected());
            methodInfo.setStatic(method.isStatic());
            methodInfo.setFinal(method.isFinal());
            methodInfo.setAbstract(method.isAbstract());
            methodInfo.setSynchronized(method.isSynchronized());
            methodInfo.setNative(method.isNative());
            methodInfo.setStrictfp(method.isStrictfp());
            methodInfo.setDefault(method.isDefault());

            // 🎯 DETECCIÓN DE TIPO DE MÉTODO EN ENUM
            if (esMetodoValoresEnum(method)) {
                methodInfo.addAnnotation("EnumSpecialMethod", "values");
            }
            if (esMetodoValueOfEnum(method)) {
                methodInfo.addAnnotation("EnumSpecialMethod", "valueOf");
            }

            // 🎯 TIPO PARÁMETROS (GENÉRICOS DEL MÉTODO)
            method.getTypeParameters().forEach(typeParam -> {
                methodInfo.addTypeParameter(typeParam.getNameAsString());
                // 🎯 BOUNDS DE TYPE PARAMETERS
                typeParam.getTypeBound().forEach(bound -> {
                    methodInfo.addTypeParameterBound(typeParam.getNameAsString(), bound.asString());
                });
            });

            // 🎯 PROCESAR PARÁMETROS CON ANÁLISIS DETALLADO
            method.getParameters().forEach(param -> {
                ParameterInfo paramInfo = new ParameterInfo();
                paramInfo.setName(param.getNameAsString());
                paramInfo.setType(param.getType().asString());
                paramInfo.setFinal(param.isFinal());
                paramInfo.setVarArgs(param.isVarArgs());
                
                // 🎯 ANOTACIONES DE PARÁMETROS CON VALORES
                param.getAnnotations().forEach(ann -> {
                    Map<String, String> annotationParams = extraerParametrosAnotacion(ann);
                    paramInfo.addAnnotation(ann.getNameAsString(), ann.toString(), annotationParams);
                });

                methodInfo.addParameter(paramInfo);
            });
            
            // 🎯 EXCEPCIONES CON ANÁLISIS DE JERARQUÍA
            method.getThrownExceptions().forEach(exception -> {
                methodInfo.addException(exception.asString());
                // 🎯 DETECTAR SI ES CHECKED O UNCHECKED EXCEPTION
                if (esUncheckedException(exception.asString())) {
                    methodInfo.addExceptionMetadata(exception.asString(), "UNCHECKED");
                } else {
                    methodInfo.addExceptionMetadata(exception.asString(), "CHECKED");
                }
            });

            // 🎯 COMENTARIOS Y JAVADOC CON EXTRACCIÓN DE METADATOS
            method.getComment().ifPresent(comment -> {
                methodInfo.setComment(comment.getContent());
                // 🎯 DETECTAR PATRONES EN COMENTARIOS
                if (contienePatronTODO(comment.getContent())) {
                    methodInfo.addAnnotation("TODO", extraerTODO(comment.getContent()));
                }
            });
            
            method.getJavadoc().ifPresent(javadoc -> {
                methodInfo.setJavadoc(javadoc.getDescription().toText());
                // 🎯 EXTRAER TAGS JAVADOC ESPECÍFICOS
                javadoc.getBlockTags().forEach(tag -> {
                    methodInfo.addJavadocTag(tag.getName(), tag.getContent().toText());
                });
            });

            // 🎯 ANOTACIONES COMPLETAS DEL MÉTODO CON PARSING AVANZADO
            method.getAnnotations().forEach(ann -> {
                Map<String, String> annotationParams = extraerParametrosAnotacion(ann);
                methodInfo.addAnnotation(ann.getNameAsString(), ann.toString(), annotationParams);
                
                // 🎯 DETECCIÓN DE ANOTACIONES ESPECIALES
                if (esAnotacionOverride(ann)) {
                    methodInfo.setOverride(true);
                }
                if (esAnotacionDeprecated(ann)) {
                    methodInfo.setDeprecated(true);
                }
            });

            // 🎯 DETECCIÓN DE PATRONES EN MÉTODOS DE ENUM
            if (esMetodoConstructorEnum(method, enumDecl)) {
                methodInfo.addAnnotation("EnumRole", "CONSTRUCTOR");
            }
            if (esMetodoAccesorEnum(method)) {
                methodInfo.addAnnotation("EnumRole", "ACCESOR");
            }
            if (esMetodoNegocioEnum(method)) {
                methodInfo.addAnnotation("EnumRole", "BUSINESS_LOGIC");
            }

            classInfo.addMethod(methodInfo);
            bitacora.debug("⚡ Método de enum procesado: " + enumDecl.getNameAsString() + "." + method.getNameAsString());
        });
    }

    /**
     * 🎯 MÉTODOS AUXILIARES TURBO PARA PROCESAMIENTO DE ENUMS
     */

    // 🎯 DETECTAR MÉTODOS ESPECIALES DE ENUM
    private boolean esMetodoValoresEnum(MethodDeclaration method) {
        return "values".equals(method.getNameAsString()) && 
               method.isStatic() && 
               method.isPublic() &&
               method.getType().toString().contains("[]");
    }

    private boolean esMetodoValueOfEnum(MethodDeclaration method) {
        return "valueOf".equals(method.getNameAsString()) && 
               method.isStatic() && 
               method.isPublic() &&
               method.getParameters().size() == 1 &&
               method.getParameters().get(0).getType().asString().equals("String");
    }

    private boolean esMetodoConstructorEnum(MethodDeclaration method, EnumDeclaration enumDecl) {
        return method.getNameAsString().equals(enumDecl.getNameAsString()) &&
               !method.isStatic();
    }

    private boolean esMetodoAccesorEnum(MethodDeclaration method) {
        String methodName = method.getNameAsString();
        return (methodName.startsWith("get") || methodName.startsWith("is")) &&
               !method.getType().asString().equals("void") &&
               method.getParameters().isEmpty();
    }

    private boolean esMetodoNegocioEnum(MethodDeclaration method) {
        return method.getBody().isPresent() &&
               method.getBody().get().getStatements().size() > 3 &&
               !method.isAbstract();
    }

    // 🎯 ANÁLISIS DE EXCEPCIONES
    private boolean esUncheckedException(String exceptionType) {
        String[] uncheckedExceptions = {
            "RuntimeException", "NullPointerException", "IllegalArgumentException",
            "IllegalStateException", "UnsupportedOperationException", "IndexOutOfBoundsException"
        };
        return Arrays.stream(uncheckedExceptions)
                     .anyMatch(exceptionType::contains);
    }

    // 🎯 EXTRACCIÓN DE PARÁMETROS DE ANOTACIONES
    private Map<String, String> extraerParametrosAnotacion(AnnotationExpr ann) {
        Map<String, String> params = new HashMap<>();
        
        if (ann.isNormalAnnotationExpr()) {
            ann.asNormalAnnotationExpr().getPairs().forEach(pair -> {
                params.put(pair.getNameAsString(), pair.getValue().toString());
            });
        } else if (ann.isSingleMemberAnnotationExpr()) {
            params.put("value", ann.asSingleMemberAnnotationExpr().getMemberValue().toString());
        } else if (ann.isMarkerAnnotationExpr()) {
            params.put("marker", "true");
        }
        
        return params;
    }

    // 🎯 DETECCIÓN DE ANOTACIONES ESPECIALES
    private boolean esAnotacionOverride(AnnotationExpr ann) {
        return "Override".equals(ann.getNameAsString()) ||
               "java.lang.Override".equals(ann.getNameAsString());
    }

    private boolean esAnotacionDeprecated(AnnotationExpr ann) {
        return "Deprecated".equals(ann.getNameAsString()) ||
               "java.lang.Deprecated".equals(ann.getNameAsString());
    }

    // 🎯 ANÁLISIS DE COMENTARIOS
    private boolean contienePatronTODO(String comment) {
        return comment != null && comment.toUpperCase().contains("TODO");
    }

    private String extraerTODO(String comment) {
        if (comment == null) return "";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("TODO[:\\.]?\\s*(.+)", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = pattern.matcher(comment);
        return matcher.find() ? matcher.group(1).trim() : comment;
    }

    // 🎯 CÁLCULO DE MÉTRICAS
    private int calcularLineasMetodo(BlockStmt body) {
        if (body == null) return 0;
        String bodyString = body.toString();
        return (int) bodyString.chars().filter(ch -> ch == '\n').count() + 1;
    }

    private int calcularComplejidadCiclomatica(MethodDeclaration method) {
        int complexity = 1; // Complejidad base
        
        // 🎯 CONTAR DECISIONES
        complexity += method.findAll(IfStmt.class).size();
        complexity += method.findAll(ForStmt.class).size();
        complexity += method.findAll(WhileStmt.class).size();
        complexity += method.findAll(DoStmt.class).size();
        complexity += method.findAll(SwitchEntry.class).size();
        complexity += method.findAll(CatchClause.class).size();
        complexity += method.findAll(ConditionalExpr.class).size();
        
        // 🎯 CONTAR OPERADORES LÓGICOS
        complexity += method.findAll(BinaryExpr.class).stream()
                .filter(expr -> expr.getOperator() == BinaryExpr.Operator.AND ||
                               expr.getOperator() == BinaryExpr.Operator.OR)
                .count();
        
        return complexity;
    }

	private void procesarRecordTurbo(RecordDeclaration record, String packageName, File file, CompilationUnit unit, List<String> imports) {
        String recordName = record.getNameAsString();
        String fqcn = packageName.isEmpty() ? recordName : packageName + "." + recordName;

        ClassInfo classInfo = crearClassInfoBasico(recordName, packageName, file, "record");
        imports.forEach(classInfo::addImport);
        
        classInfo.setPublic(record.isPublic());
        classInfo.setFinal(true);

        // Procesar componentes del record
        record.getParameters().forEach(param -> {
            FieldInfo field = new FieldInfo();
            field.setName(param.getNameAsString());
            field.setType(param.getType().asString());
            field.setPrivate(true);
            field.setFinal(true);
            
            // 🆕 ANOTACIONES EN COMPONENTES RECORD
            param.getAnnotations().forEach(ann -> {
                field.addAnnotation(ann.getNameAsString(), ann.toString());
            });
            
            classInfo.addField(field);
        });

        classMap.put(fqcn, classInfo);
    }

    private void procesarAnotacionTurbo(AnnotationDeclaration annotation, String packageName, File file, CompilationUnit unit, List<String> imports) {
        String annotationName = annotation.getNameAsString();
        String fqcn = packageName.isEmpty() ? annotationName : packageName + "." + annotationName;

        ClassInfo classInfo = crearClassInfoBasico(annotationName, packageName, file, "annotation");
        imports.forEach(classInfo::addImport);
        
        classInfo.setPublic(annotation.isPublic());
        
        classMap.put(fqcn, classInfo);
    }

    // 🆕 DESTRUCTOR TURBO
    @Override
    protected void finalize() throws Throwable {
        if (turboExecutor != null && !turboExecutor.isShutdown()) {
            turboExecutor.shutdownNow();
        }
        super.finalize();
    }
    
 // 🆕 NUEVO MÉTODO - Crear parser robusto
    private JavaParser createRobustParser() {
        ParserConfiguration config = new ParserConfiguration();
        
        // Configuración más tolerante
        config.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);
        config.setAttributeComments(true);
        config.setLexicalPreservationEnabled(true);
        config.setPreprocessUnicodeEscapes(true);
        config.setDoNotAssignCommentsPrecedingEmptyLines(true);
        
        return new JavaParser(config);
    }


    // 🆕 MÉTODO AUXILIAR PARA LEER ARCHIVO
    private String readFileContent(File file) {
        try {
            return Files.readString(file.toPath(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }
    

    private String extractPackageFromPath(File file) {
        return extractPackageFromPathEnhanced(file);
    }

    /**
     * 🎯 CONVERTIR RUTA DE DIRECTORIO A NOMBRE DE PACKAGE
     */
    private String convertPathToPackage(String path) {
        if (path == null || path.trim().isEmpty()) {
            return "";
        }
        
        // 🎯 LIMPIAR Y CONVERTIR
        String cleanPath = path.replace(File.separator, ".")
                              .replace("/", ".")
                              .replace("\\", ".")
                              .replace("..", ".");
        
        // 🎯 ELIMINAR PUNTOS DUPLICADOS Y AL INICIO/FINAL
        cleanPath = cleanPath.replaceAll("\\.{2,}", ".");
        cleanPath = cleanPath.replaceAll("^\\.+", "").replaceAll("\\.+$", "");
        
        return cleanPath;
    }

    /**
     * 🎯 EXTRAER DESDE ESTRUCTURA DE DIRECTORIOS
     */
    private String extractFromDirectoryStructure(Path filePath) {
        List<String> packageParts = new ArrayList<>();
        Path current = filePath.getParent();
        boolean foundSourceDir = false;
        
        // 🎯 NAVEGAR HACIA ARRIBA HASTA ENCONTRAR DIRECTORIO FUENTE
        while (current != null && current.getFileName() != null) {
            String dirName = current.getFileName().toString();
            
            // 🎯 DETENER SI ENCONTRAMOS DIRECTORIO FUENTE
            if (isSourceDirectory(dirName)) {
                foundSourceDir = true;
                break;
            }
            
            // 🎯 DETENER SI ENCONTRAMOS DIRECTORIO RAÍZ DEL PROYECTO
            if (isProjectRootDirectory(dirName, current)) {
                break;
            }
            
            // 🎯 AGREGAR PARTE DEL PACKAGE (en orden inverso)
            if (!isExcludedDirectory(dirName)) {
                packageParts.add(0, dirName);
            }
            
            current = current.getParent();
        }
        
        // 🎯 SI NO ENCONTRAMOS DIRECTORIO FUENTE, USAR ESTRUCTURA COMPLETA
        if (!foundSourceDir && packageParts.size() > 1) {
            // Eliminar el primer elemento si parece ser el directorio del proyecto
            if (isLikelyProjectDir(packageParts.get(0))) {
                packageParts.remove(0);
            }
        }
        
        return packageParts.isEmpty() ? "" : String.join(".", packageParts);
    }

    /**
     * 🎯 VERIFICAR SI ES DIRECTORIO DE CÓDIGO FUENTE
     */
    private boolean isSourceDirectory(String dirName) {
        String[] sourceDirs = {"src", "main", "java", "test", "source", "sources"};
        return Arrays.asList(sourceDirs).contains(dirName.toLowerCase());
    }

    /**
     * 🎯 VERIFICAR SI ES DIRECTORIO RAÍZ DEL PROYECTO
     */
    private boolean isProjectRootDirectory(String dirName, Path path) {
        // 🎯 INDICADORES DE DIRECTORIO RAÍZ
        String[] rootIndicators = {".git", "pom.xml", "build.gradle", "build.xml", "package.json"};
        
        // Verificar si el directorio contiene archivos de proyecto
        try {
            if (Files.isDirectory(path)) {
                return Arrays.stream(rootIndicators)
                    .anyMatch(indicator -> Files.exists(path.resolve(indicator)));
            }
        } catch (Exception e) {
            // Ignorar errores de acceso
        }
        
        return false;
    }

    /**
     * 🎯 VERIFICAR SI ES DIRECTORIO EXCLUIDO
     */
    private boolean isExcludedDirectory(String dirName) {
        String[] excludedDirs = {"target", "build", ".git", "node_modules", "bin", "out", "dist", ".idea", ".vscode"};
        return Arrays.asList(excludedDirs).contains(dirName.toLowerCase());
    }

    /**
     * 🎯 VERIFICAR SI PARECE SER DIRECTORIO DE PROYECTO
     */
    private boolean isLikelyProjectDir(String dirName) {
        // 🎯 PATRONES COMUNES DE NOMBRES DE PROYECTOS
        String lowerName = dirName.toLowerCase();
        return lowerName.contains("project") || 
               lowerName.contains("app") || 
               lowerName.contains("application") ||
               lowerName.matches(".*[vV]\\d+(\\.\\d+)*") || // Versiones: v1.0, v2.3.1
               dirName.equals("src") ||
               dirName.equals("main");
    }

    /**
     * 🎯 MÉTODO ALTERNATIVO: ANALIZAR CONTENIDO DEL ARCHIVO
     */
    private String extractPackageFromFileContent(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            
            for (String line : lines) {
                String trimmed = line.trim();
                // 🎯 BUSCAR DECLARACIÓN DE PACKAGE
                if (trimmed.startsWith("package ")) {
                    int start = "package ".length();
                    int end = trimmed.indexOf(';');
                    if (end > start) {
                        return trimmed.substring(start, end).trim();
                    }
                }
                
                // 🎯 LIMITAR BÚSQUEDA A LAS PRIMERAS 20 LÍNEAS
                if (lines.indexOf(line) > 20) {
                    break;
                }
            }
        } catch (Exception e) {
            // Ignorar errores de lectura
        }
        
        return "";
    }

    /**
     * 🎯 MÉTODO PRINCIPAL MEJORADO CON MÚLTIPLES ESTRATEGIAS
     */
    private String extractPackageFromPathEnhanced(File file) {
        if (file == null) return "";
        
        // 🎯 ESTRATEGIA 1: DEL CONTENIDO DEL ARCHIVO (MÁS PRECISA)
        String packageFromContent = extractPackageFromFileContent(file);
        if (!packageFromContent.isEmpty()) {
            return packageFromContent;
        }
        
        // 🎯 ESTRATEGIA 2: DE LA RUTA DEL ARCHIVO
        String packageFromPath = extractPackageFromPath(file);
        if (!packageFromPath.isEmpty()) {
            return packageFromPath;
        }
        
        // 🎯 ESTRATEGIA 3: PREDICCIÓN INTELIGENTE
        return predictPackageFromFileName(file);
    }

    /**
     * 🎯 PREDECIR PACKAGE BASADO EN NOMBRE DE ARCHIVO
     */
    private String predictPackageFromFileName(File file) {
        String fileName = file.getName().replace(".java", "");
        
        // 🎯 PATRONES COMUNES
        if (fileName.toLowerCase().contains("test")) {
            return "test";
        } else if (fileName.toLowerCase().contains("util") || fileName.toLowerCase().contains("utils")) {
            return "util";
        } else if (fileName.toLowerCase().contains("model")) {
            return "model";
        } else if (fileName.toLowerCase().contains("service")) {
            return "service";
        } else if (fileName.toLowerCase().contains("controller")) {
            return "controller";
        } else if (fileName.toLowerCase().contains("entity")) {
            return "entity";
        } else if (fileName.toLowerCase().contains("config")) {
            return "config";
        }
        
        return "default";
    }

	// 🆕 MÉTODO DE FALLBACK
    private void parseWithFallback(File file) {
        try {
            bitacora.debug("🔄 Intentando parse normal: " + file.getName());
            parseJavaFileTurbo(file);
        } catch (Exception e) {
            bitacora.warn("🔄 Usando parser de fallback para: " + file.getName());
            parseJavaFileSimple(file);
        }
    }

    // 🆕 PARSER SIMPLIFICADO COMO FALLBACK
    private void parseJavaFileSimple(File file) {
        try {
            String content = Files.readString(file.toPath(), java.nio.charset.StandardCharsets.UTF_8);
            extractBasicInfoWithRegex(file, content);
            bitacora.debug("🛡️ Rescate básico exitoso: " + file.getName());
        } catch (Exception e) {
            bitacora.error("💥 Fallback también falló: " + file.getName() + " - " + e.getMessage());
        }
    }

    // 🆕 EXTRACCIÓN BÁSICA POR REGEX
    private void extractBasicInfoWithRegex(File file, String content) {
        try {
            String fileName = file.getName();
            String className = fileName.replace(".java", "");
            
            // 🎯 EXTRAER PACKAGE
            String packageName = "";
            java.util.regex.Pattern packagePattern = java.util.regex.Pattern.compile("package\\s+([^;]+);");
            java.util.regex.Matcher packageMatcher = packagePattern.matcher(content);
            if (packageMatcher.find()) {
                packageName = packageMatcher.group(1).trim();
            }
            
            // 🎯 EXTRAER CLASE/INTERFACE/ENUM
            String type = "CLASS";
            java.util.regex.Pattern classPattern = java.util.regex.Pattern.compile(
                "(public\\s+)?(class|interface|enum)\\s+" + java.util.regex.Pattern.quote(className)
            );
            java.util.regex.Matcher classMatcher = classPattern.matcher(content);
            if (classMatcher.find()) {
                String foundType = classMatcher.group(2);
                type = foundType.toUpperCase();
            }
            
            String fqcn = packageName.isEmpty() ? className : packageName + "." + className;
            
            if (!classMap.containsKey(fqcn)) {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setName(className);
                classInfo.setPackageName(packageName);
                classInfo.setSourcePath(file.getAbsolutePath());
                classInfo.setType(type);
                
                // 🎯 MARCAR COMO RESCATADO
                classInfo.addAnnotation("RescateRegex", "true");
                classInfo.addAnnotation("ParseOriginal", "Fallido");
                
                classMap.put(fqcn, classInfo);
                bitacora.debug("🛡️ CLASE RESCATADA POR REGEX: " + fqcn);
            }
            
        } catch (Exception e) {
            bitacora.error("💥 Error en rescate regex: " + file.getName());
        }}
        
        /**
         * 🔍 MÉTODO DE DIAGNÓSTICO - Verificar estado del classMap
         */
        private void diagnosticClassMapCheck() {
            bitacora.info("🔍 DIAGNÓSTICO CLASSMAP:");
            bitacora.info("   • Tamaño classMap: " + classMap.size());
            bitacora.info("   • Claves en classMap: " + classMap.keySet());
            
            if (!classMap.isEmpty()) {
                bitacora.info("   • Primeras 5 clases:");
                classMap.entrySet().stream()
                    .limit(5)
                    .forEach(entry -> 
                        bitacora.info("     - " + entry.getKey() + " [" + entry.getValue().getType() + "]")
                    );
            } else {
                bitacora.info("   • ❌ classMap VACÍO - esto es el problema!");
            }
    }

		public Set<String> getClassMap() {
			// TODO Auto-generated method stub
			return classMap.keySet();
		}
		
		/**
		 * 🏴‍☠️ REINICIAR EXECUTOR PARA NUEVOS ESCANEOS
		 * ¡Evita que los hilos se amotinen después del primer viaje!
		 */
		public void prepararParaNuevoEscaneo() {
		    // 🔄 CERRAR EXECUTOR VIEJO SI EXISTE
		    if (turboExecutor != null && !turboExecutor.isShutdown()) {
		        turboExecutor.shutdownNow();
		        try {
		            if (!turboExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
		                bitacora.warn("⏰ Timeout cerrando executor viejo, continuando...");
		            }
		        } catch (InterruptedException e) {
		            Thread.currentThread().interrupt();
		        }
		    }
		    
		    // 🚀 CREAR NUEVO EXECUTOR
		    this.turboExecutor = Executors.newFixedThreadPool(MAX_CONCURRENT_PARSERS);
		    bitacora.info("🏴‍☠️ ThreadPool recreado - Listo para nueva aventura pirata");
		}
}