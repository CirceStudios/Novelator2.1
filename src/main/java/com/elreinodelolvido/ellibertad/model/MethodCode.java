package com.elreinodelolvido.ellibertad.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 🎯 CLASE PARA REPRESENTAR UN MÉTODO DE CÓDIGO JAVA
 */
public class MethodCode {
    private String name;
    private String returnType;
    private String signature;
    private String body;
    private int lineCount;
    private int complexity;
    private boolean isPublic;
    private boolean isPrivate;
    private boolean isProtected;
    private boolean isStatic;
    private boolean isFinal;
    private boolean isAbstract;
    private boolean isSynchronized;
    private boolean isNative;
    private boolean isStrictfp;
    private boolean isDefault;
    private boolean isConstructor;
    private boolean isOverride;
    private boolean isDeprecated;
    
    // 🎯 ESTRUCTURA DETALLADA
    private List<ParameterCode> parameters = new ArrayList<>();
    private List<String> exceptions = new ArrayList<>();
    private Map<String, String> exceptionsMetadata = new HashMap<>();
    private List<String> annotations = new ArrayList<>();
    private Map<String, Map<String, String>> annotationsWithParams = new HashMap<>();
    private List<String> typeParameters = new ArrayList<>();
    private Map<String, List<String>> typeParameterBounds = new HashMap<>();
    private List<String> javadocTags = new ArrayList<>();
    private String comment;
    private String javadoc;
    
    // 🎯 METADATAS ADICIONALES
    private Map<String, Object> metadata = new HashMap<>();

    public MethodCode() {
        // Constructor por defecto
    }

    public MethodCode(String name, String returnType) {
        this.name = name;
        this.returnType = returnType;
    }

    // 🎯 GETTERS Y SETTERS BÁSICOS
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }
    
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    
    public int getLineCount() { return lineCount; }
    public void setLineCount(int lineCount) { this.lineCount = lineCount; }
    
    public int getComplexity() { return complexity; }
    public void setComplexity(int complexity) { this.complexity = complexity; }

    // 🎯 GETTERS Y SETTERS DE MODIFICADORES
    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
    
    public boolean isPrivate() { return isPrivate; }
    public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
    
    public boolean isProtected() { return isProtected; }
    public void setProtected(boolean isProtected) { this.isProtected = isProtected; }
    
    public boolean isStatic() { return isStatic; }
    public void setStatic(boolean isStatic) { this.isStatic = isStatic; }
    
    public boolean isFinal() { return isFinal; }
    public void setFinal(boolean isFinal) { this.isFinal = isFinal; }
    
    public boolean isAbstract() { return isAbstract; }
    public void setAbstract(boolean isAbstract) { this.isAbstract = isAbstract; }
    
    public boolean isSynchronized() { return isSynchronized; }
    public void setSynchronized(boolean isSynchronized) { this.isSynchronized = isSynchronized; }
    
    public boolean isNative() { return isNative; }
    public void setNative(boolean isNative) { this.isNative = isNative; }
    
    public boolean isStrictfp() { return isStrictfp; }
    public void setStrictfp(boolean isStrictfp) { this.isStrictfp = isStrictfp; }
    
    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }
    
    public boolean isConstructor() { return isConstructor; }
    public void setConstructor(boolean isConstructor) { this.isConstructor = isConstructor; }
    
    public boolean isOverride() { return isOverride; }
    public void setOverride(boolean isOverride) { this.isOverride = isOverride; }
    
    public boolean isDeprecated() { return isDeprecated; }
    public void setDeprecated(boolean isDeprecated) { this.isDeprecated = isDeprecated; }

    // 🎯 GETTERS Y SETTERS DE COLECCIONES
    public List<ParameterCode> getParameters() { return parameters; }
    public void setParameters(List<ParameterCode> parameters) { this.parameters = parameters; }
    
    public List<String> getExceptions() { return exceptions; }
    public void setExceptions(List<String> exceptions) { this.exceptions = exceptions; }
    
    public Map<String, String> getExceptionsMetadata() { return exceptionsMetadata; }
    public void setExceptionsMetadata(Map<String, String> exceptionsMetadata) { this.exceptionsMetadata = exceptionsMetadata; }
    
    public List<String> getAnnotations() { return annotations; }
    public void setAnnotations(List<String> annotations) { this.annotations = annotations; }
    
    public Map<String, Map<String, String>> getAnnotationsWithParams() { return annotationsWithParams; }
    public void setAnnotationsWithParams(Map<String, Map<String, String>> annotationsWithParams) { this.annotationsWithParams = annotationsWithParams; }
    
    public List<String> getTypeParameters() { return typeParameters; }
    public void setTypeParameters(List<String> typeParameters) { this.typeParameters = typeParameters; }
    
    public Map<String, List<String>> getTypeParameterBounds() { return typeParameterBounds; }
    public void setTypeParameterBounds(Map<String, List<String>> typeParameterBounds) { this.typeParameterBounds = typeParameterBounds; }
    
    public List<String> getJavadocTags() { return javadocTags; }
    public void setJavadocTags(List<String> javadocTags) { this.javadocTags = javadocTags; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public String getJavadoc() { return javadoc; }
    public void setJavadoc(String javadoc) { this.javadoc = javadoc; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    // 🎯 MÉTODOS DE CONVENIENCIA - AGREGAR ELEMENTOS
    public void addParameter(ParameterCode parameter) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<>();
        }
        this.parameters.add(parameter);
    }

    public void addException(String exception) {
        if (this.exceptions == null) {
            this.exceptions = new ArrayList<>();
        }
        this.exceptions.add(exception);
    }

    public void addExceptionMetadata(String exception, String type) {
        if (this.exceptionsMetadata == null) {
            this.exceptionsMetadata = new HashMap<>();
        }
        this.exceptionsMetadata.put(exception, type);
    }

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
        Map<String, String> params = new HashMap<>();
        if (annotationValue != null && !annotationValue.isEmpty()) {
            params.put("value", annotationValue);
        }
        this.annotationsWithParams.put(annotationName, params);
    }

    public void addAnnotation(String annotationName, String annotationValue, Map<String, String> params) {
        addAnnotation(annotationName);
        if (this.annotationsWithParams == null) {
            this.annotationsWithParams = new HashMap<>();
        }
        this.annotationsWithParams.put(annotationName, params != null ? new HashMap<>(params) : new HashMap<>());
    }

    public void addTypeParameter(String typeParam) {
        if (this.typeParameters == null) {
            this.typeParameters = new ArrayList<>();
        }
        this.typeParameters.add(typeParam);
    }

    public void addTypeParameterBound(String typeParam, String bound) {
        if (this.typeParameterBounds == null) {
            this.typeParameterBounds = new HashMap<>();
        }
        this.typeParameterBounds.computeIfAbsent(typeParam, k -> new ArrayList<>()).add(bound);
    }

    public void addJavadocTag(String tagName, String tagContent) {
        if (this.javadocTags == null) {
            this.javadocTags = new ArrayList<>();
        }
        this.javadocTags.add("@" + tagName + " " + tagContent);
    }

    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    // 🎯 MÉTODOS DE VERIFICACIÓN
    public boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
    }

    public boolean hasExceptions() {
        return exceptions != null && !exceptions.isEmpty();
    }

    public boolean hasAnnotations() {
        return annotations != null && !annotations.isEmpty();
    }

    public boolean hasBody() {
        return body != null && !body.trim().isEmpty();
    }

    public boolean isVoid() {
        return "void".equals(returnType);
    }

    public boolean isGetter() {
        return name != null && 
               (name.startsWith("get") || name.startsWith("is")) &&
               parameters.isEmpty() &&
               !isVoid() &&
               !isConstructor;
    }

    public boolean isSetter() {
        return name != null && 
               name.startsWith("set") &&
               parameters.size() == 1 &&
               isVoid() &&
               !isConstructor;
    }

    // 🎯 MÉTODOS DE INFORMACIÓN
    public String getFullSignature() {
        if (signature != null) {
            return signature;
        }
        
        StringBuilder sb = new StringBuilder();
        
        // Modificadores
        if (isPublic) sb.append("public ");
        if (isPrivate) sb.append("private ");
        if (isProtected) sb.append("protected ");
        if (isStatic) sb.append("static ");
        if (isFinal) sb.append("final ");
        if (isAbstract) sb.append("abstract ");
        if (isSynchronized) sb.append("synchronized ");
        if (isNative) sb.append("native ");
        if (isStrictfp) sb.append("strictfp ");
        if (isDefault) sb.append("default ");
        
        // Tipo de retorno
        if (!isConstructor) {
            sb.append(returnType).append(" ");
        }
        
        // Nombre
        sb.append(name);
        
        // Parámetros
        sb.append("(");
        if (hasParameters()) {
            List<String> paramStrings = parameters.stream()
                .map(param -> {
                    StringBuilder paramSb = new StringBuilder();
                    if (param.isFinal()) paramSb.append("final ");
                    paramSb.append(param.getType()).append(" ").append(param.getName());
                    return paramSb.toString();
                })
                .collect(Collectors.toList());
            sb.append(String.join(", ", paramStrings));
        }
        sb.append(")");
        
        // Excepciones
        if (hasExceptions()) {
            sb.append(" throws ").append(String.join(", ", exceptions));
        }
        
        return sb.toString();
    }

    public int getParameterCount() {
        return parameters != null ? parameters.size() : 0;
    }

    public Optional<ParameterCode> getParameterByName(String paramName) {
        if (!hasParameters()) return Optional.empty();
        return parameters.stream()
                .filter(param -> paramName.equals(param.getName()))
                .findFirst();
    }

    public List<String> getParameterTypes() {
        if (!hasParameters()) return new ArrayList<>();
        return parameters.stream()
                .map(ParameterCode::getType)
                .collect(Collectors.toList());
    }

    public List<String> getParameterNames() {
        if (!hasParameters()) return new ArrayList<>();
        return parameters.stream()
                .map(ParameterCode::getName)
                .collect(Collectors.toList());
    }

    // 🎯 MÉTODOS DE TRANSFORMACIÓN
    public String toCodeString() {
        StringBuilder code = new StringBuilder();
        
        // Javadoc
        if (javadoc != null && !javadoc.isEmpty()) {
            code.append("/**\n * ").append(javadoc).append("\n */\n");
        }
        
        // Anotaciones
        if (hasAnnotations()) {
            for (String annotation : annotations) {
                code.append("@").append(annotation).append("\n");
            }
        }
        
        // Firma del método
        code.append(getFullSignature());
        
        // Cuerpo
        if (isAbstract() || isNative()) {
            code.append(";");
        } else {
            code.append(" {\n");
            if (body != null) {
                // Indentar el cuerpo
                String indentedBody = body.lines()
                        .map(line -> "    " + line)
                        .collect(Collectors.joining("\n"));
                code.append(indentedBody);
            } else {
                code.append("        // Implementación no disponible\n");
            }
            code.append("\n}");
        }
        
        return code.toString();
    }

    // 🎯 MÉTODOS DE COPIA
    public MethodCode copy() {
        MethodCode copy = new MethodCode();
        copy.name = this.name;
        copy.returnType = this.returnType;
        copy.signature = this.signature;
        copy.body = this.body;
        copy.lineCount = this.lineCount;
        copy.complexity = this.complexity;
        copy.isPublic = this.isPublic;
        copy.isPrivate = this.isPrivate;
        copy.isProtected = this.isProtected;
        copy.isStatic = this.isStatic;
        copy.isFinal = this.isFinal;
        copy.isAbstract = this.isAbstract;
        copy.isSynchronized = this.isSynchronized;
        copy.isNative = this.isNative;
        copy.isStrictfp = this.isStrictfp;
        copy.isDefault = this.isDefault;
        copy.isConstructor = this.isConstructor;
        copy.isOverride = this.isOverride;
        copy.isDeprecated = this.isDeprecated;
        
        // Copiar colecciones
        if (this.parameters != null) {
            copy.parameters = this.parameters.stream()
                    .map(ParameterCode::copy)
                    .collect(Collectors.toList());
        }
        if (this.exceptions != null) {
            copy.exceptions = new ArrayList<>(this.exceptions);
        }
        if (this.exceptionsMetadata != null) {
            copy.exceptionsMetadata = new HashMap<>(this.exceptionsMetadata);
        }
        if (this.annotations != null) {
            copy.annotations = new ArrayList<>(this.annotations);
        }
        if (this.annotationsWithParams != null) {
            copy.annotationsWithParams = new HashMap<>();
            this.annotationsWithParams.forEach((key, value) -> 
                copy.annotationsWithParams.put(key, new HashMap<>(value))
            );
        }
        if (this.typeParameters != null) {
            copy.typeParameters = new ArrayList<>(this.typeParameters);
        }
        if (this.typeParameterBounds != null) {
            copy.typeParameterBounds = new HashMap<>();
            this.typeParameterBounds.forEach((key, value) -> 
                copy.typeParameterBounds.put(key, new ArrayList<>(value))
            );
        }
        if (this.javadocTags != null) {
            copy.javadocTags = new ArrayList<>(this.javadocTags);
        }
        copy.comment = this.comment;
        copy.javadoc = this.javadoc;
        if (this.metadata != null) {
            copy.metadata = new HashMap<>(this.metadata);
        }
        
        return copy;
    }

    @Override
    public String toString() {
        return String.format(
            "MethodCode{name='%s', returnType='%s', parameters=%d, public=%s, static=%s}",
            name, returnType, getParameterCount(), isPublic, isStatic
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodCode that = (MethodCode) o;
        return Objects.equals(getFullSignature(), that.getFullSignature());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFullSignature());
    }
}
