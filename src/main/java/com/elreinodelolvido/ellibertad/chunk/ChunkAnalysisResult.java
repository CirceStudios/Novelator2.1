package com.elreinodelolvido.ellibertad.chunk;

import java.util.*;

/**
 * 📊 RESULTADO DE ANÁLISIS POR CHUNKS
 */
public class ChunkAnalysisResult {
    private final String filePath;
    private final String className;
    private final List<String> suggestions;
    private final Map<Integer, List<String>> chunkSuggestions;
    private final int totalChunks;
    private final int processedChunks;
    private final boolean analysisComplete;
    
    public ChunkAnalysisResult(String filePath, String className, int totalChunks) {
        this.filePath = filePath;
        this.className = className;
        this.suggestions = new ArrayList<>();
        this.chunkSuggestions = new HashMap<>();
        this.totalChunks = totalChunks;
        this.processedChunks = 0;
        this.analysisComplete = false;
    }
    
    /**
     * 🎯 AGREGAR SUGERENCIAS DE CHUNK
     */
    public void addChunkSuggestions(int chunkNumber, List<String> chunkSuggestions) {
        this.chunkSuggestions.put(chunkNumber, new ArrayList<>(chunkSuggestions));
        this.suggestions.addAll(chunkSuggestions);
    }
    
    /**
     * 🎯 MARCAR CHUNK PROCESADO
     */
    public void markChunkProcessed() {
        // Lógica de conteo
    }
    
    /**
     * 🎯 VERIFICAR SI ANÁLISIS ESTÁ COMPLETO
     */
    public boolean isAnalysisComplete() {
        return processedChunks >= totalChunks;
    }
    
    /**
     * 🎯 OBTENER SUGERENCIAS CONSISTENTES (eliminar duplicados)
     */
    public List<String> getConsolidatedSuggestions() {
        return suggestions.stream()
            .distinct()
            .sorted()
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    // 🎯 GETTERS
    public String getFilePath() { return filePath; }
    public String getClassName() { return className; }
    public List<String> getSuggestions() { return getConsolidatedSuggestions(); }
    public Map<Integer, List<String>> getChunkSuggestions() { return chunkSuggestions; }
    public int getTotalChunks() { return totalChunks; }
    public int getProcessedChunks() { return processedChunks; }
    public boolean isComplete() { return analysisComplete; }
}