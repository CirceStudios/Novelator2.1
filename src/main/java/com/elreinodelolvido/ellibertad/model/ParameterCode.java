package com.elreinodelolvido.ellibertad.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 🎯 CLASE PARA REPRESENTAR UN PARÁMETRO DE MÉTODO
 */
public class ParameterCode {
    private String name;
    private String type;
    private boolean isFinal;
    private boolean isVarArgs;
    private List<String> annotations = new ArrayList<>();
    private Map<String, String> annotationsWithParams = new HashMap<>();

    public ParameterCode() {
        // Constructor por defecto
    }

    public ParameterCode(String name, String type) {
        this.name = name;
        this.type = type;
    }

    // 🎯 GETTERS Y SETTERS
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public boolean isFinal() { return isFinal; }
    public void setFinal(boolean isFinal) { this.isFinal = isFinal; }
    
    public boolean isVarArgs() { return isVarArgs; }
    public void setVarArgs(boolean isVarArgs) { this.isVarArgs = isVarArgs; }
    
    public List<String> getAnnotations() { return annotations; }
    public void setAnnotations(List<String> annotations) { this.annotations = annotations; }
    
    public Map<String, String> getAnnotationsWithParams() { return annotationsWithParams; }
    public void setAnnotationsWithParams(Map<String, String> annotationsWithParams) { this.annotationsWithParams = annotationsWithParams; }

    // 🎯 MÉTODOS DE CONVENIENCIA
    public void addAnnotation(String annotation) {
        if (this.annotations == null) {
            this.annotations = new ArrayList<>();
        }
        this.annotations.add(annotation);
    }

    public void addAnnotation(String annotationName, String annotationValue) {
        addAnnotation(annotationName);
        if (this.annotationsWithParams == null) {
            this.annotationsWithParams = new HashMap<>();
        }
        this.annotationsWithParams.put(annotationName, annotationValue);
    }

    public boolean hasAnnotations() {
        return annotations != null && !annotations.isEmpty();
    }

    public String getFullDeclaration() {
        StringBuilder sb = new StringBuilder();
        
        // Anotaciones
        if (hasAnnotations()) {
            for (String annotation : annotations) {
                sb.append("@").append(annotation).append(" ");
            }
        }
        
        // Modificador final
        if (isFinal) {
            sb.append("final ");
        }
        
        // Tipo
        sb.append(type);
        
        // Varargs
        if (isVarArgs) {
            sb.append("...");
        }
        
        // Nombre
        sb.append(" ").append(name);
        
        return sb.toString();
    }

    public ParameterCode copy() {
        ParameterCode copy = new ParameterCode();
        copy.name = this.name;
        copy.type = this.type;
        copy.isFinal = this.isFinal;
        copy.isVarArgs = this.isVarArgs;
        if (this.annotations != null) {
            copy.annotations = new ArrayList<>(this.annotations);
        }
        if (this.annotationsWithParams != null) {
            copy.annotationsWithParams = new HashMap<>(this.annotationsWithParams);
        }
        return copy;
    }

    @Override
    public String toString() {
        return String.format(
            "ParameterCode{name='%s', type='%s', final=%s, varArgs=%s}",
            name, type, isFinal, isVarArgs
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterCode that = (ParameterCode) o;
        return isFinal == that.isFinal && 
               isVarArgs == that.isVarArgs && 
               Objects.equals(name, that.name) && 
               Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, isFinal, isVarArgs);
    }
}