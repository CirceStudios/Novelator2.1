package com.elreinodelolvido.ellibertad.chunk;

import java.util.*;

/**
 * 📝 RESULTADO DE ANÁLISIS DE CHUNK INDIVIDUAL - CON RESPUESTA IA
 */
public class ChunkAnalysis {
    private final String chunkId;
    private final int chunkNumber;
    private List<String> sugerencias;
    private boolean exito;
    private String error;
    private long tiempoProcesamiento;
    private String respuestaIA; // 🆕 NUEVO CAMPO
    
    public ChunkAnalysis(String chunkId, int chunkNumber) {
        this.chunkId = chunkId;
        this.chunkNumber = chunkNumber;
        this.sugerencias = new ArrayList<>();
        this.exito = false;
        this.tiempoProcesamiento = 0;
        this.respuestaIA = ""; // 🆕 INICIALIZAR
    }
    
    // 🎯 GETTERS Y SETTERS
    public String getChunkId() { return chunkId; }
    public int getChunkNumber() { return chunkNumber; }
    public List<String> getSugerencias() { return sugerencias; }
    public void setSugerencias(List<String> sugerencias) { this.sugerencias = sugerencias; }
    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public long getTiempoProcesamiento() { return tiempoProcesamiento; }
    public void setTiempoProcesamiento(long tiempo) { this.tiempoProcesamiento = tiempo; }
    public String getRespuestaIA() { return respuestaIA; } // 🆕 NUEVO GETTER
    public void setRespuestaIA(String respuestaIA) { this.respuestaIA = respuestaIA; } // 🆕 NUEVO SETTER
}
