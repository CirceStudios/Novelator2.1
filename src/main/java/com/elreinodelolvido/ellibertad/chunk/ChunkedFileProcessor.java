package com.elreinodelolvido.ellibertad.chunk;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

import com.elreinodelolvido.ellibertad.engine.Bitacora;

/**
 * 🚀 PROCESADOR DE ARCHIVOS GRANDES POR CHUNKS - STREAMING OPTIMIZADO
 * Maneja archivos de cualquier tamaño dividiéndolos en chunks procesables
 */
public class ChunkedFileProcessor {
    private final Bitacora bitacora;
    private final int CHUNK_SIZE_LINES = 500; // Líneas por chunk
    private final int MAX_CHUNK_SIZE_BYTES = 2 * 1024 * 1024; // 2MB máximo por chunk
    private final Pattern CLASS_PATTERN = Pattern.compile(
        "^(public\\s+)?(class|interface|enum|record|@interface)\\s+(\\w+)"
    );
    
    public ChunkedFileProcessor(Bitacora bitacora) {
        this.bitacora = bitacora;
    }
    
    /**
     * 🎯 ANALIZAR ARCHIVO GRANDE POR CHUNKS - STREAMING
     */
    public List<FileChunk> processLargeFile(Path filePath, long fileSize) throws IOException {
        List<FileChunk> chunks = new ArrayList<>();
        
        if (fileSize <= MAX_CHUNK_SIZE_BYTES * 2) {
            // Archivo no tan grande, procesar completo
            chunks.add(processSingleChunk(filePath));
            return chunks;
        }
        
        bitacora.info("📁 Procesando archivo grande por streaming: " + filePath.getFileName() + 
                     " (" + (fileSize / 1024 / 1024) + "MB)");
        
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            StringBuilder currentChunk = new StringBuilder();
            int lineCount = 0;
            int chunkNumber = 1;
            boolean inMultiLineComment = false;
            String currentClassName = extractMainClassName(filePath);
            
            while ((line = reader.readLine()) != null) {
                // 🎯 MANEJO DE COMENTARIOS MULTILÍNEA
                line = handleMultiLineComments(line, inMultiLineComment);
                inMultiLineComment = line.contains("/*") && !line.contains("*/");
                
                currentChunk.append(line).append("\n");
                lineCount++;
                
                // 🎯 CREAR CHUNK CUANDO SE ALCANCE EL LÍMITE
                if (lineCount >= CHUNK_SIZE_LINES || 
                    currentChunk.length() >= MAX_CHUNK_SIZE_BYTES) {
                    
                    if (currentChunk.length() > 0) {
                        FileChunk chunk = createFileChunk(
                            filePath, currentChunk.toString(), chunkNumber++, 
                            currentClassName, lineCount
                        );
                        chunks.add(chunk);
                        
                        // 🎯 RESET PARA SIGUIENTE CHUNK
                        currentChunk = new StringBuilder();
                        lineCount = 0;
                    }
                }
            }
            
            // 🎯 AGREGAR ÚLTIMO CHUNK
            if (currentChunk.length() > 0) {
                FileChunk chunk = createFileChunk(
                    filePath, currentChunk.toString(), chunkNumber, 
                    currentClassName, lineCount
                );
                chunks.add(chunk);
            }
        }
        
        bitacora.info("✅ Archivo dividido en " + chunks.size() + " chunks: " + filePath.getFileName());
        return chunks;
    }
    
    /**
     * 🎯 PROCESAR CHUNK ÚNICO (para archivos pequeños)
     */
    private FileChunk processSingleChunk(Path filePath) throws IOException {
        String content = Files.readString(filePath, StandardCharsets.UTF_8);
        String className = extractMainClassName(filePath);
        
        return new FileChunk(
            filePath.toString(),
            className,
            content,
            1, // chunkNumber
            content.split("\n").length, // totalLines
            false // isPartial
        );
    }
    
    /**
     * 🎯 MANEJAR COMENTARIOS MULTILÍNEA
     */
    private String handleMultiLineComments(String line, boolean inMultiLineComment) {
        if (inMultiLineComment) {
            if (line.contains("*/")) {
                return line.substring(line.indexOf("*/") + 2);
            }
            return ""; // Omitir líneas dentro de comentarios
        }
        return line;
    }
    
    /**
     * 🎯 EXTRAER NOMBRE DE CLASE PRINCIPAL
     */
    private String extractMainClassName(Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (CLASS_PATTERN.matcher(line).find()) {
                    // Extraer nombre de clase
                    String[] parts = line.split("\\s+");
                    for (int i = 0; i < parts.length; i++) {
                        if (parts[i].equals("class") || parts[i].equals("interface") || 
                            parts[i].equals("enum") || parts[i].equals("record")) {
                            if (i + 1 < parts.length) {
                                return parts[i + 1].split("[<{]")[0]; // Sin genéricos
                            }
                        }
                    }
                }
                // Limitar búsqueda a primeras 50 líneas
                if (reader.readLine() != null && reader.readLine().contains("class")) break;
            }
        } catch (Exception e) {
            bitacora.debug("⚠️ No se pudo extraer nombre de clase: " + filePath.getFileName());
        }
        return filePath.getFileName().toString().replace(".java", "");
    }
    
    /**
     * 🎯 CREAR CHUNK DE ARCHIVO
     */
    private FileChunk createFileChunk(Path filePath, String content, int chunkNumber, 
                                    String className, int lineCount) {
        return new FileChunk(
            filePath.toString(),
            className,
            content,
            chunkNumber,
            lineCount,
            true // isPartial para chunks de archivos grandes
        );
    }
}