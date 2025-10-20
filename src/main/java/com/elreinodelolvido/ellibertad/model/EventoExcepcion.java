package com.elreinodelolvido.ellibertad.model;

import java.time.LocalDateTime;

public class EventoExcepcion {
    private final String modulo;
    private final String metodo;
    private final Throwable excepcion;  // Cambiado de Exception a Throwable
    private final String contexto;
    private final LocalDateTime timestamp;
    private String causaRaiz;
    private String recomendacion;

    public EventoExcepcion(String modulo, String metodo, Throwable excepcion, 
                          String contexto, LocalDateTime timestamp) {
        this.modulo = modulo;
        this.metodo = metodo;
        this.excepcion = excepcion;
        this.contexto = contexto;
        this.timestamp = timestamp;
    }

    // Getters y setters...
    public String getModulo() { return modulo; }
    public String getMetodo() { return metodo; }
    public Throwable getExcepcion() { return excepcion; }  // Cambiado aquí también
    public String getContexto() { return contexto; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getCausaRaiz() { return causaRaiz; }
    public void setCausaRaiz(String causaRaiz) { this.causaRaiz = causaRaiz; }
    public String getRecomendacion() { return recomendacion; }
    public void setRecomendacion(String recomendacion) { this.recomendacion = recomendacion; }
}
