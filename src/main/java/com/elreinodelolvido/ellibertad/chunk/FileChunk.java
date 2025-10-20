package com.elreinodelolvido.ellibertad.chunk;

/**
 * 📦 MODELO DE CHUNK DE ARCHIVO PARA STREAMING
 */
public class FileChunk {
    private final String filePath;
    private final String className;
    private final String content;
    private final int chunkNumber;
    private final int lineCount;
    private final boolean isPartial;
    private final String chunkId;
    
    public FileChunk(String filePath, String className, String content, 
                    int chunkNumber, int lineCount, boolean isPartial) {
        this.filePath = filePath;
        this.className = className;
        this.content = content;
        this.chunkNumber = chunkNumber;
        this.lineCount = lineCount;
        this.isPartial = isPartial;
        this.chunkId = generateChunkId(filePath, chunkNumber);
    }
    
    private String generateChunkId(String filePath, int chunkNumber) {
        return filePath.hashCode() + "_chunk_" + chunkNumber;
    }
    
    // 🎯 GETTERS
    public String getFilePath() { return filePath; }
    public String getClassName() { return className; }
    public String getContent() { return content; }
    public int getChunkNumber() { return chunkNumber; }
    public int getLineCount() { return lineCount; }
    public boolean isPartial() { return isPartial; }
    public String getChunkId() { return chunkId; }
    
    /**
     * 🎯 OBTENER CONTEXTO PARA IA (información adicional)
     */
    public String getContextForAI() {
        StringBuilder context = new StringBuilder();
        context.append("📋 CONTEXTO DEL CHUNK:\n");
        context.append("• Archivo: ").append(filePath).append("\n");
        context.append("• Clase: ").append(className).append("\n");
        context.append("• Chunk: ").append(chunkNumber).append("/").append("N").append("\n");
        context.append("• Líneas: ").append(lineCount).append("\n");
        context.append("• Tipo: ").append(isPartial ? "Partial" : "Complete").append("\n");
        context.append("\n📝 CONTENIDO:\n");
        context.append(content);
        return context.toString();
    }
    
    @Override
    public String toString() {
        return String.format("FileChunk{file=%s, chunk=%d, lines=%d, partial=%s}", 
            filePath, chunkNumber, lineCount, isPartial);
    }
}
