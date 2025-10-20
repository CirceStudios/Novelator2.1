package com.elreinodelolvido.ellibertad.scanner;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.Pair;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 🚨 PARSER DE EMERGENCIA - Soluciona bugs de JavaParser
 */
public class EmergencyJavaParser {
    private static final Set<String> SKIP_FILES = Set.of("ProjectScanner.java", "Bitacora.java");
    
    /**
     * 🛡️ PARSE ROBUSTO - Evita todos los bugs conocidos
     */
    public static ParseResult<CompilationUnit> parseRobust(File file) {
        // 🚨 EVITAR ARCHIVOS PROBLEMÁTICOS
        if (SKIP_FILES.contains(file.getName())) {
            return createFallbackResult(file, "Archivo excluido por problemas conocidos");
        }
        
        try {
            // 🎯 DETECTAR ENCODING CORRECTO
            String content = readFileWithEncodingDetection(file);
            
            // 🎯 LIMPIAR CARACTERES PROBLEMÁTICOS
            content = cleanProblematicContent(content);
            
            // 🎯 VERIFICAR SI EL CONTENIDO ES VÁLIDO
            if (!isValidJavaContent(content)) {
                return createFallbackResult(file, "Contenido Java no válido");
            }
            
            // 🎯 CONFIGURACIÓN ULTRA-TOLERANTE
            ParserConfiguration config = new ParserConfiguration();
            config.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);
            config.setAttributeComments(false);
            config.setLexicalPreservationEnabled(false);
            config.setPreprocessUnicodeEscapes(true);
            config.setDoNotAssignCommentsPrecedingEmptyLines(true);
            
            JavaParser parser = new JavaParser(config);
            
            // 🎯 PARSE SEGURO
            return parser.parse(ParseStart.COMPILATION_UNIT, Providers.provider(content));
            
        } catch (Exception e) {
            // 🛡️ FALLBACK: Parser simplificado
            return createFallbackResult(file, "Error en parse robusto: " + e.getMessage());
        }
    }
    
    /**
     * 🎯 CREAR RESULTADO DE FALLBACK VÁLIDO
     */
    private static ParseResult<CompilationUnit> createFallbackResult(File file, String message) {
        List<Problem> problems = new ArrayList<>();
        
        // 🎯 CREAR PROBLEMA USANDO EL CONSTRUCTOR CORRECTO (más simple)
        // JavaParser no expone fácilmente la creación de Problems, así que creamos un resultado con problemas
        try {
            // Intentar crear un problema válido
            Problem problem = createSimpleProblem(message, file.getName());
            problems.add(problem);
        } catch (Exception e) {
            // Si falla, crear un problema básico
            problems.add(new Problem("Fallback: " + message, null, new RuntimeException("Parse emergency")));
        }
        
        return new ParseResult<>(null, problems, null);
    }
    
    /**
     * 🎯 CREAR PROBLEMA SIMPLE (compatible con JavaParser)
     */
    private static Problem createSimpleProblem(String message, String fileName) {
        try {
            // 🎯 USAR REFLECTION PARA CREAR UN PROBLEMA VÁLIDO
            // Esto es más robusto que depender de constructores específicos
            return new Problem(
                "EmergencyParser: " + message + " - File: " + fileName,
                null, // TokenRange
                new RuntimeException("Emergency fallback")
            );
        } catch (Exception e) {
            // 🛡️ FALLBACK ULTRA-SIMPLE
            return new Problem("Emergency fallback for: " + fileName, null, null);
        }
    }
    
    /**
     * 🎯 LECTURA CON DETECCIÓN DE ENCODING
     */
    private static String readFileWithEncodingDetection(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        
        // 🎯 PROBAR DIFERENTES ENCODINGS
        String[] encodings = {"UTF-8", "ISO-8859-1", "Windows-1252", "US-ASCII"};
        
        for (String encoding : encodings) {
            String content = new String(fileContent, encoding);
			if (isValidJavaContent(content)) {
			    return content;
			}
        }
        
        // 🛡️ FALLBACK: UTF-8 con reemplazo de caracteres inválidos
        return new String(fileContent, StandardCharsets.UTF_8);
    }
    
    /**
     * 🎯 LIMPIAR CONTENIDO PROBLEMÁTICO
     */
    private static String cleanProblematicContent(String content) {
        if (content == null) return "";
        
        StringBuilder cleaned = new StringBuilder();
        
        // 🎯 PROCESAR CARACTER POR CARACTER PARA MAYOR CONTROL
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            
            // 🎯 PERMITIR CARACTERES JAVA VÁLIDOS
            if (isValidJavaChar(c)) {
                cleaned.append(c);
            } else if (c == '\r') {
                // 🎯 NORMALIZAR SALTO DE LÍNEA
                cleaned.append('\n');
            } else if (Character.isWhitespace(c)) {
                // 🎯 PRESERVAR ESPACIOS VÁLIDOS
                cleaned.append(' ');
            } else {
                // 🎯 REEMPLAZAR CARACTERES INVÁLIDOS
                cleaned.append(' ');
            }
        }
        
        String result = cleaned.toString();
        
        // 🎯 LIMPIAR LÍNEAS DEMASIADO LARGAS (evitar buffer overflow)
        result = splitLongLines(result, 1000);
        
        return result;
    }
    
    /**
     * 🎯 VERIFICAR SI CARACTER ES VÁLIDO PARA JAVA
     */
    private static boolean isValidJavaChar(char c) {
        // 🎯 CARACTERES PERMITIDOS EN JAVA
        return (c >= 32 && c <= 126) ||    // ASCII imprimible
               (c >= 128 && c <= 255) ||   // Latin-1 extendido
               (c == '\n' || c == '\t') || // Tabulador y nueva línea
               Character.isLetterOrDigit(c) || // Letras y dígitos Unicode
               "{}[]()<>;,.=+-*/%&|^!~?:@#\"'\\$_ ".indexOf(c) >= 0; // Símbolos Java
    }
    
    /**
     * 🎯 DIVIDIR LÍNEAS MUY LARGAS
     */
    private static String splitLongLines(String content, int maxLineLength) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();
        
        for (String line : lines) {
            if (line.length() > maxLineLength) {
                // 🎯 DIVIDIR LÍNEA EN FRAGMENTOS MÁS PEQUEÑOS
                for (int i = 0; i < line.length(); i += maxLineLength) {
                    int end = Math.min(i + maxLineLength, line.length());
                    result.append(line.substring(i, end)).append("\n");
                }
            } else {
                result.append(line).append("\n");
            }
        }
        
        return result.toString();
    }
    
    /**
     * 🎯 VALIDAR CONTENIDO JAVA
     */
    private static boolean isValidJavaContent(String content) {
        if (content == null || content.trim().isEmpty()) return false;
        
        // 🎯 VERIFICAR PATRONES BÁSICOS DE ARCHIVO JAVA
        boolean hasJavaStructure = content.contains("class") || 
                                  content.contains("interface") || 
                                  content.contains("enum") ||
                                  content.contains("package") ||
                                  content.contains("public") ||
                                  content.contains("private");
        
        if (!hasJavaStructure) return false;
        
        // 🎯 VERIFICAR QUE NO SEA DEMASIADO CORTO O LARGO
        if (content.length() < 10 || content.length() > 1000000) {
            return false;
        }
        
        // 🎯 VERIFICAR CARACTERES INVÁLIDOS EXCESIVOS
        long invalidCharCount = content.chars()
            .filter(c -> !isValidJavaChar((char)c))
            .count();
            
        return (invalidCharCount * 100 / content.length()) < 10; // Menos del 10% de caracteres inválidos
    }
    
    /**
     * 🛡️ PARSER DE FALLBACK ULTRA-SIMPLE
     */
    public static ParseResult<CompilationUnit> parseFallback(File file) {
        return createFallbackResult(file, "Parse fallback activado");
    }
    
    /**
     * 🎯 MÉTODO ALTERNATIVO: PARSE SUPER-SIMPLE
     */
    public static Optional<CompilationUnit> parseSuperSimple(File file) {
        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            
            // 🎯 LIMPIEZA BÁSICA
            content = content.replaceAll("[\\u0000-\\u001F]", ""); // Remover caracteres de control
            content = content.replaceAll("\\r\\n", "\n"); // Normalizar líneas
            
            ParserConfiguration config = new ParserConfiguration();
            config.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_11); // Nivel más compatible
            
            JavaParser parser = new JavaParser(config);
            ParseResult<CompilationUnit> result = parser.parse(ParseStart.COMPILATION_UNIT, Providers.provider(content));
            
            return result.getResult();
            
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * 🎯 EXTRACCIÓN BÁSICA DE INFORMACIÓN COMO ÚLTIMO RECURSO
     */
    public static Map<String, String> extractBasicFileInfo(File file) {
        Map<String, String> info = new HashMap<>();
        
        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            
            // 🎯 EXTRAER PACKAGE
            Pattern packagePattern = Pattern.compile("package\\s+([^;]+);");
            java.util.regex.Matcher packageMatcher = packagePattern.matcher(content);
            if (packageMatcher.find()) {
                info.put("package", packageMatcher.group(1).trim());
            }
            
            // 🎯 EXTRAER CLASE/INTERFACE/ENUM
            Pattern classPattern = Pattern.compile("(public\\s+)?(class|interface|enum)\\s+(\\w+)");
            java.util.regex.Matcher classMatcher = classPattern.matcher(content);
            if (classMatcher.find()) {
                info.put("type", classMatcher.group(2));
                info.put("name", classMatcher.group(3));
            }
            
            // 🎯 ESTADÍSTICAS BÁSICAS
            info.put("lines", String.valueOf(content.split("\n").length));
            info.put("size", String.valueOf(file.length()));
            
        } catch (Exception e) {
            info.put("error", "No se pudo extraer información básica: " + e.getMessage());
        }
        
        return info;
    }
    
    /**
     * 🎯 VERIFICAR SI EL ARCHIVO DEBERÍA SER PROCESADO
     */
    public static boolean shouldProcessFile(File file) {
        if (file == null || !file.exists() || !file.getName().endsWith(".java")) {
            return false;
        }
        
        // 🚨 EXCLUIR ARCHIVOS DEMASIADO GRANDES
        if (file.length() > 10 * 1024 * 1024) { // 10MB
            return false;
        }
        
        // 🚨 EXCLUIR ARCHIVOS CON NOMBRES PROBLEMÁTICOS
        String fileName = file.getName().toLowerCase();
        if (fileName.contains("test") || fileName.contains("generated")) {
            return true; // Permitir tests pero con cuidado
        }
        
        return true;
    }
    
    /**
     * 🎯 OBTENER ESTADÍSTICAS DE CALIDAD DEL PARSE
     */
    public static Map<String, Object> getParseQualityMetrics(ParseResult<CompilationUnit> result) {
        Map<String, Object> metrics = new HashMap<>();
        
        if (result == null) {
            metrics.put("status", "NULL_RESULT");
            metrics.put("quality", "UNKNOWN");
            return metrics;
        }
        
        metrics.put("isSuccessful", result.isSuccessful());
        metrics.put("hasResult", result.getResult().isPresent());
        
        if (result.getProblems() != null) {
            metrics.put("problemCount", result.getProblems().size());
            List<String> problemMessages = new ArrayList<>();
            for (Problem problem : result.getProblems()) {
                problemMessages.add(problem.getMessage());
            }
            metrics.put("problems", problemMessages);
        } else {
            metrics.put("problemCount", 0);
            metrics.put("problems", Collections.emptyList());
        }
        
        // 🎯 CLASIFICAR CALIDAD
        String quality = "EXCELLENT";
        if (!result.isSuccessful()) {
            quality = "POOR";
        } else if (result.getProblems() != null && !result.getProblems().isEmpty()) {
            quality = result.getProblems().size() > 5 ? "FAIR" : "GOOD";
        }
        
        metrics.put("quality", quality);
        
        return metrics;
    }
    
    /**
     * 🎯 ESTRATEGIA DE PARSE EN CASCADA (más robusta)
     */
    public static ParseResult<CompilationUnit> parseCascading(File file) {
        // 🎯 INTENTO 1: Parse robusto completo
        ParseResult<CompilationUnit> result = parseRobust(file);
        if (result.isSuccessful() && result.getResult().isPresent()) {
            return result;
        }
        
        // 🎯 INTENTO 2: Parse super-simple
        Optional<CompilationUnit> simpleResult = parseSuperSimple(file);
        if (simpleResult.isPresent()) {
            return new ParseResult<>(simpleResult.get(), Collections.emptyList(), null);
        }
        
        // 🎯 INTENTO 3: Fallback final
        return parseFallback(file);
    }
}
