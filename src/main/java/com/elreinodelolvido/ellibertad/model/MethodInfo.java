package com.elreinodelolvido.ellibertad.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 🏴‍☠️ MethodInfo - Representa un método de una clase Java
 */
public class MethodInfo {
    private String name;
    private String returnType;
    private String signature;
    private String visibility; // PUBLIC, PRIVATE, PROTECTED, PACKAGE
    private boolean isStatic;
    private boolean isFinal;
    private boolean isAbstract;
    private boolean isSynchronized;
    private boolean isNative;
    private List<ParameterInfo> parameters;
    private List<String> exceptions;
    private Map<String, String> annotations;
    private List<String> modifiers;
    private int lineCount;
    private int complexity;
    
 // 🎯 CUERPO DEL MÉTODO CON ANÁLISIS SEMÁNTICO
    private String methodBody;
    private List<String> bodyStatements = new ArrayList<>();
    private Map<String, Integer> operationCounts = new HashMap<>();
    private Set<String> referencedFields = new HashSet<>();
    private Set<String> invokedMethods = new HashSet<>();
    private Set<String> localVariables = new HashSet<>();

    // 🏗️ Constructores
    public MethodInfo() {
        this.parameters = new ArrayList<>();
        this.exceptions = new ArrayList<>();
        this.annotations = new HashMap<>();
        this.modifiers = new ArrayList<>();
    }

    public MethodInfo(String name, String returnType) {
        this.name = name;
        this.returnType = returnType;
        this.visibility = "PUBLIC";
    }

    // 🎯 Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }

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

    public List<ParameterInfo> getParameters() { return Collections.unmodifiableList(parameters); }
    public void setParameters(List<ParameterInfo> parameters) { this.parameters = new ArrayList<>(parameters); }

    public List<String> getExceptions() { return Collections.unmodifiableList(exceptions); }
    public void setExceptions(List<String> exceptions) { this.exceptions = new ArrayList<>(exceptions); }

    public Map<String, String> getAnnotations() { return Collections.unmodifiableMap(annotations); }
    public void setAnnotations(Map<String, String> annotations) { this.annotations = new HashMap<>(annotations); }

    public List<String> getModifiers() { return Collections.unmodifiableList(modifiers); }
    public void setModifiers(List<String> modifiers) { this.modifiers = new ArrayList<>(modifiers); }

    public int getLineCount() { return lineCount; }
    public void setLineCount(int lineCount) { this.lineCount = lineCount; }

    public int getComplexity() { return complexity; }
    public void setComplexity(int complexity) { this.complexity = complexity; }

    // 🚀 Métodos de utilidad
    public void addParameter(ParameterInfo parameter) {
        this.parameters.add(parameter);
    }

    public void addException(String exception) {
        this.exceptions.add(exception);
    }

    public void addAnnotation(String name, String value) {
        this.annotations.put(name, value);
    }

    public void addModifier(String modifier) {
        this.modifiers.add(modifier);
    }

    public boolean isPublic() {
        return "PUBLIC".equals(visibility);
    }

    public boolean isPrivate() {
        return "PRIVATE".equals(visibility);
    }

    public boolean isProtected() {
        return "PROTECTED".equals(visibility);
    }

    public boolean isConstructor() {
        return returnType == null || returnType.isEmpty();
    }

    public boolean isGetter() {
        return name.startsWith("get") && !returnType.equals("void") && parameters.isEmpty();
    }

    public boolean isSetter() {
        return name.startsWith("set") && returnType.equals("void") && parameters.size() == 1;
    }

    public String getDeclaration() {
        StringBuilder sb = new StringBuilder();
        
        // Anotaciones
        for (String annotation : annotations.keySet()) {
            sb.append("@").append(annotation).append(" ");
        }
        
        // Modificadores
        if (!modifiers.isEmpty()) {
            sb.append(String.join(" ", modifiers)).append(" ");
        }
        
        // Visibilidad
        if (visibility != null) {
            sb.append(visibility.toLowerCase()).append(" ");
        }
        
        // static, final, etc.
        if (isStatic) sb.append("static ");
        if (isFinal) sb.append("final ");
        if (isAbstract) sb.append("abstract ");
        if (isSynchronized) sb.append("synchronized ");
        if (isNative) sb.append("native ");
        
        // Tipo de retorno y nombre
        if (isConstructor()) {
            sb.append(name);
        } else {
            sb.append(returnType).append(" ").append(name);
        }
        
        // Parámetros
        sb.append("(");
        for (int i = 0; i < parameters.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(parameters.get(i));
        }
        sb.append(")");
        
        // Excepciones
        if (!exceptions.isEmpty()) {
            sb.append(" throws ").append(String.join(", ", exceptions));
        }
        
        return sb.toString();
    }

    @Override
    public String toString() {
        String emoji = isConstructor() ? "🛠️" : 
                      isGetter() ? "📥" : 
                      isSetter() ? "📤" : "⚡";
        
        return String.format("%s %s %s(%d params)", 
            emoji, returnType != null ? returnType : "constructor", 
            name, parameters.size());
    }

    // 🚀 Métodos de visibilidad específicos
    public void setPublic(boolean isPublic) {
        if (isPublic) {
            this.visibility = "PUBLIC";
        } else if ("PUBLIC".equals(this.visibility)) {
            this.visibility = "PRIVATE"; // Fallback a private si se quita public
        }
    }

    public void setPrivate(boolean isPrivate) {
        if (isPrivate) {
            this.visibility = "PRIVATE";
        } else if ("PRIVATE".equals(this.visibility)) {
            this.visibility = "PACKAGE"; // Fallback a package si se quita private
        }
    }

    public void setProtected(boolean isProtected) {
        if (isProtected) {
            this.visibility = "PROTECTED";
        } else if ("PROTECTED".equals(this.visibility)) {
            this.visibility = "PACKAGE"; // Fallback a package si se quita protected
        }
    }

    /**
     * 🎯 VERIFICAR SI EL MÉTODO TIENE ERRORES
     */
    public boolean hasErrors() {
        // Verificar errores básicos de estructura
        if (name == null || name.trim().isEmpty()) {
            return true;
        }
        
        // Verificar nombre inválido
        if (!name.matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
            return true;
        }
        
        // Verificar visibilidad inválida
        if (visibility == null || !Arrays.asList("PUBLIC", "PRIVATE", "PROTECTED", "PACKAGE").contains(visibility)) {
            return true;
        }
        
        // Verificar conflictos de modificadores
        if (isAbstract && (isStatic || isFinal || isSynchronized || isNative)) {
            return true; // abstract no puede combinarse con static, final, synchronized o native
        }
        
        if (isFinal && isAbstract) {
            return true; // final y abstract son mutuamente excluyentes
        }
        
        if (isNative && (isAbstract || isSynchronized)) {
            return true; // native no puede ser abstract o synchronized
        }
        
        // Verificar tipo de retorno para constructores
        if (isConstructor() && returnType != null && !returnType.isEmpty()) {
            return true; // Los constructores no deben tener tipo de retorno
        }
        
        // Verificar tipo de retorno para métodos normales
        if (!isConstructor() && (returnType == null || returnType.trim().isEmpty())) {
            return true; // Los métodos deben tener tipo de retorno (void es válido)
        }
        
        // Verificar parámetros duplicados
        Set<String> paramNames = new HashSet<>();
        for (ParameterInfo param : parameters) {
            if (param != null && param.getName() != null) {
                if (!paramNames.add(param.getName())) {
                    return true; // Parámetro duplicado
                }
            }
        }
        
        // Verificar complejidad excesiva
        if (complexity > 50) {
            return true;
        }
        
        // Verificar método demasiado largo
        if (lineCount > 200) {
            return true;
        }
        
        return false;
    }

    /**
     * 🎯 OBTENER EL TIPO DE RETORNO SIMPLE (sin package)
     */
    public String getSimpleReturnType() {
        if (returnType == null || returnType.isEmpty()) {
            return isConstructor() ? "constructor" : "void";
        }
        
        return getSimpleTypeName(returnType);
    }

    /**
     * 🎯 OBTENER LISTA DE TIPOS DE PARÁMETROS SIMPLES
     */
    public List<String> getSimpleParameterTypes() {
        List<String> simpleTypes = new ArrayList<>();
        for (ParameterInfo param : parameters) {
            if (param != null) {
                simpleTypes.add(param.getType());
            }
        }
        return simpleTypes;
    }

    /**
     * 🎯 OBTENER FIRMA COMPLETA DEL MÉTODO
     */
    public String getCompleteSignature() {
        StringBuilder sb = new StringBuilder();
        
        // Anotaciones
        if (!annotations.isEmpty()) {
            for (Map.Entry<String, String> annotation : annotations.entrySet()) {
                sb.append("@").append(annotation.getKey());
                if (annotation.getValue() != null && !annotation.getValue().isEmpty()) {
                    sb.append("(").append(annotation.getValue()).append(")");
                }
                sb.append("\n");
            }
        }
        
        // Modificadores de visibilidad
        sb.append(visibility.toLowerCase()).append(" ");
        
        // Otros modificadores
        if (isStatic) sb.append("static ");
        if (isFinal) sb.append("final ");
        if (isAbstract) sb.append("abstract ");
        if (isSynchronized) sb.append("synchronized ");
        if (isNative) sb.append("native ");
        
        // Tipo de retorno y nombre
        if (isConstructor()) {
            sb.append(name);
        } else {
            sb.append(returnType).append(" ").append(name);
        }
        
        // Parámetros
        sb.append("(");
        for (int i = 0; i < parameters.size(); i++) {
            if (i > 0) sb.append(", ");
            ParameterInfo param = parameters.get(i);
            if (param != null) {
                sb.append(param.getCompleteDeclaration());
            }
        }
        sb.append(")");
        
        // Excepciones
        if (!exceptions.isEmpty()) {
            sb.append(" throws ").append(String.join(", ", exceptions));
        }
        
        return sb.toString();
    }

    /**
     * 🎯 CALCULAR COMPLEJIDAD CICLOMÁTICA ESTIMADA
     */
    public int calculateCyclomaticComplexity() {
        int baseComplexity = 1; // Base complexity
        
        // Aumentar complejidad basado en características del método
        if (parameters.size() > 3) baseComplexity += 1;
        if (exceptions.size() > 2) baseComplexity += 1;
        if (isSynchronized) baseComplexity += 1;
        if (lineCount > 50) baseComplexity += 2;
        if (lineCount > 100) baseComplexity += 3;
        
        // Estimación basada en número de líneas (simplificada)
        baseComplexity += Math.max(0, (lineCount - 10) / 10);
        
        return Math.max(1, baseComplexity);
    }

    /**
     * 🎯 VERIFICAR SI ES UN MÉTODO BOOLEAN GETTER
     */
    public boolean isBooleanGetter() {
        return (name.startsWith("is") || name.startsWith("has")) && 
               (returnType.equals("boolean") || returnType.equals("Boolean")) && 
               parameters.isEmpty();
    }

    /**
     * 🎯 VERIFICAR SI EL MÉTODO ESTÁ SOBRECARGADO
     */
    public boolean isOverloaded(List<MethodInfo> allMethods) {
        if (allMethods == null) return false;
        
        long sameNameCount = allMethods.stream()
            .filter(m -> m != null && name.equals(m.getName()))
            .count();
        
        return sameNameCount > 1;
    }

    /**
     * 🎯 OBTENER NOMBRE SIMPLE DEL TIPO (sin package)
     */
    private String getSimpleTypeName(String fullTypeName) {
        if (fullTypeName == null) {
            return "";
        }
        
        // Para tipos genéricos, procesar la parte base
        if (fullTypeName.contains("<")) {
            String baseType = fullTypeName.substring(0, fullTypeName.indexOf('<'));
            String genericPart = fullTypeName.substring(fullTypeName.indexOf('<'));
            return getSimpleName(baseType) + genericPart;
        }
        
        // Para arrays
        if (fullTypeName.endsWith("[]")) {
            String baseType = fullTypeName.substring(0, fullTypeName.length() - 2);
            return getSimpleName(baseType) + "[]";
        }
        
        return getSimpleName(fullTypeName);
    }

    /**
     * 🎯 OBTENER NOMBRE SIMPLE (sin package)
     */
    private String getSimpleName(String fullName) {
        if (fullName == null) {
            return "";
        }
        
        int lastDot = fullName.lastIndexOf('.');
        return lastDot >= 0 ? fullName.substring(lastDot + 1) : fullName;
    }

    /**
     * 🎯 OBTENER INFORMACIÓN DE CALIDAD DEL MÉTODO
     */
    public Map<String, Object> getQualityInfo() {
        Map<String, Object> qualityInfo = new HashMap<>();
        
        qualityInfo.put("name", name);
        qualityInfo.put("returnType", getSimpleReturnType());
        qualityInfo.put("parameterCount", parameters.size());
        qualityInfo.put("lineCount", lineCount);
        qualityInfo.put("complexity", complexity);
        qualityInfo.put("cyclomaticComplexity", calculateCyclomaticComplexity());
        qualityInfo.put("isConstructor", isConstructor());
        qualityInfo.put("isGetter", isGetter());
        qualityInfo.put("isSetter", isSetter());
        qualityInfo.put("isBooleanGetter", isBooleanGetter());
        qualityInfo.put("visibility", visibility);
        qualityInfo.put("hasErrors", hasErrors());
        
        // Evaluación de calidad
        String quality = "GOOD";
        if (complexity > 20 || lineCount > 100 || parameters.size() > 5) {
            quality = "POOR";
        } else if (complexity > 10 || lineCount > 50 || parameters.size() > 3) {
            quality = "FAIR";
        }
        qualityInfo.put("qualityRating", quality);
        
        return qualityInfo;
    }

    public void setBody(String body) {
        this.methodBody = body;
        
        // 🎯 ANÁLISIS AUTOMÁTICO DEL CUERPO DEL MÉTODO
        if (body != null && !body.trim().isEmpty()) {
            analyzeMethodBody(body);
        }
    }

    public String getBody() {
        return methodBody;
    }

    public boolean hasBody() {
        return methodBody != null && !methodBody.trim().isEmpty();
    }

    // 🎯 STRICTFP - CÁLCULOS DE PUNTO FLOTANTE PRECISOS
    private boolean isStrictfp;

    public void setStrictfp(boolean strictfpEnabled) {
        this.isStrictfp = strictfpEnabled;
        if (strictfpEnabled) {
            addModifier("strictfp");
            addAnnotation("StrictFloatingPoint", "enabled");
        } else {
            this.modifiers.remove("strictfp");
            this.annotations.remove("StrictFloatingPoint");
        }
    }

    public boolean isStrictfp() {
        return isStrictfp;
    }

    // 🎯 DEFAULT METHODS - MÉTODOS DEFAULT EN INTERFACES
    private boolean isDefault;

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
        if (isDefault) {
            addModifier("default");
            this.isAbstract = false; // Los métodos default no pueden ser abstract
        } else {
            this.modifiers.remove("default");
        }
    }

    public boolean isDefault() {
        return isDefault;
    }

    // 🎯 TYPE PARAMETERS - SISTEMA DE GENÉRICOS AVANZADO
    private List<String> typeParameters = new ArrayList<>();
    private Map<String, List<String>> typeParameterBounds = new HashMap<>();

    public void addTypeParameter(String typeParam) {
        if (this.typeParameters == null) {
            this.typeParameters = new ArrayList<>();
        }
        if (typeParam != null && !typeParam.trim().isEmpty()) {
            this.typeParameters.add(typeParam.trim());
        }
    }

    public void addTypeParameterBound(String typeParam, String bound) {
        if (this.typeParameterBounds == null) {
            this.typeParameterBounds = new HashMap<>();
        }
        if (typeParam != null && bound != null) {
            this.typeParameterBounds.computeIfAbsent(typeParam, k -> new ArrayList<>()).add(bound);
        }
    }

    public List<String> getTypeParameters() {
        return typeParameters != null ? 
            Collections.unmodifiableList(typeParameters) : 
            Collections.emptyList();
    }

    public List<String> getTypeParameterBounds(String typeParam) {
        if (typeParameterBounds == null || typeParam == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(
            typeParameterBounds.getOrDefault(typeParam, Collections.emptyList())
        );
    }

    public boolean hasTypeParameters() {
        return typeParameters != null && !typeParameters.isEmpty();
    }

    public String getTypeParametersDeclaration() {
        if (!hasTypeParameters()) return "";
        
        List<String> paramsWithBounds = new ArrayList<>();
        for (String param : typeParameters) {
            List<String> bounds = getTypeParameterBounds(param);
            if (bounds.isEmpty()) {
                paramsWithBounds.add(param);
            } else {
                paramsWithBounds.add(param + " extends " + String.join(" & ", bounds));
            }
        }
        
        return "<" + String.join(", ", paramsWithBounds) + ">";
    }

    // 🎯 COMENTARIOS Y DOCUMENTACIÓN AVANZADA
    private String methodComment;
    private String javadoc;
    private List<String> todoComments = new ArrayList<>();
    private String author;
    private String version;
    private String since;

    public void setComment(String comment) {
        this.methodComment = comment;
        if (comment != null) {
            extractCommentMetadata(comment);
        }
    }

    public String getComment() {
        return methodComment;
    }

    public void setJavadoc(String javadocContent) {
        this.javadoc = javadocContent;
        if (javadocContent != null) {
            parseJavadocContent(javadocContent);
        }
    }

    public String getJavadoc() {
        return javadoc;
    }

    // 🎯 ANOTACIONES AVANZADAS CON PARÁMETROS
    public void addAnnotation(String name, String value, Map<String, String> parameters) {
        if (name == null) return;
        
        // 🎯 CONSTRUIR REPRESENTACIÓN COMPLETA DE LA ANOTACIÓN
        StringBuilder annotationBuilder = new StringBuilder();
        annotationBuilder.append("@").append(name);
        
        if (parameters != null && !parameters.isEmpty()) {
            annotationBuilder.append("(");
            List<String> paramPairs = new ArrayList<>();
            parameters.forEach((k, v) -> paramPairs.add(k + " = " + v));
            annotationBuilder.append(String.join(", ", paramPairs));
            annotationBuilder.append(")");
        } else if (value != null && !value.isEmpty()) {
            annotationBuilder.append("(").append(value).append(")");
        }
        
        this.annotations.put(name, annotationBuilder.toString());
        
        // 🎯 DETECCIÓN AUTOMÁTICA DE ANOTACIONES ESPECIALES
        detectSpecialAnnotation(name, parameters);
    }

    // 🎯 CONSTRUCTOR FLAG - IDENTIFICACIÓN PRECISA
    private boolean isConstructor;

    public void setConstructor(boolean isConstructor) {
        this.isConstructor = isConstructor;
        if (isConstructor) {
            this.returnType = null; // Los constructores no tienen tipo de retorno
            addAnnotation("Constructor", "true");
        }
    }

    // 🎯 METADATAS DE EXCEPCIONES AVANZADAS
    private Map<String, String> exceptionMetadata = new HashMap<>();
    private Map<String, List<String>> exceptionHandling = new HashMap<>();

    public void addExceptionMetadata(String exception, String metadata) {
        if (exception != null && metadata != null) {
            this.exceptionMetadata.put(exception, metadata);
        }
    }

    public void addExceptionHandling(String exception, List<String> handlingStrategies) {
        if (exception != null && handlingStrategies != null) {
            this.exceptionHandling.put(exception, new ArrayList<>(handlingStrategies));
        }
    }

    // 🎯 JAVADOC TAGS AVANZADOS
    public void addJavadocTag(String name, String content) {
        if (name != null && content != null) {
            this.javadocTags.put(name, content.trim());
            
            // 🎯 PROCESAR TAGS ESPECIALES
            switch (name.toLowerCase()) {
                case "@author":
                    this.author = content.trim();
                    break;
                case "@version":
                    this.version = content.trim();
                    break;
                case "@since":
                    this.since = content.trim();
                    break;
                case "@param":
                    processParamTag(content);
                    break;
                case "@return":
                    processReturnTag(content);
                    break;
                case "@throws":
                case "@exception":
                    processThrowsTag(content);
                    break;
            }
        }
    }

    // 🎯 OVERRIDE Y DEPRECATED - METADATAS ESPECIALES
    private boolean isOverride;
    private boolean isDeprecated;
    private String deprecationReason;
    private String deprecatedSince;
	private boolean isPrivate;

    public void setOverride(boolean isOverride) {
        this.isOverride = isOverride;
        if (isOverride) {
            addAnnotation("Override", "");
        }
    }

    public void setDeprecated(boolean isDeprecated) {
        this.isDeprecated = isDeprecated;
        if (isDeprecated) {
            addAnnotation("Deprecated", "");
            // 🎯 BUSCAR RAZÓN DE DEPRECACIÓN EN JAVADOC
            if (javadocTags.containsKey("@deprecated")) {
                this.deprecationReason = javadocTags.get("@deprecated");
            }
        }
    }

    public void setDeprecationInfo(String reason, String since) {
        this.isDeprecated = true;
        this.deprecationReason = reason;
        this.deprecatedSince = since;
        
        Map<String, String> deprecationParams = new HashMap<>();
        if (reason != null) deprecationParams.put("reason", "\"" + reason + "\"");
        if (since != null) deprecationParams.put("since", "\"" + since + "\"");
        
        addAnnotation("Deprecated", "", deprecationParams);
    }

    // 🎯 MÉTODOS PRIVADOS DE ANÁLISIS TURBO

    /**
     * 🎯 ANALIZAR CUERPO DEL MÉTODO PARA EXTRACCIÓN DE METADATAS
     */
    private void analyzeMethodBody(String body) {
        // 🎯 CONTAR OPERACIONES
        countOperations(body);
        
        // 🎯 IDENTIFICAR CAMPOS REFERENCIADOS
        identifyReferencedFields(body);
        
        // 🎯 IDENTIFICAR MÉTODOS INVOCADOS
        identifyInvokedMethods(body);
        
        // 🎯 IDENTIFICAR VARIABLES LOCALES
        identifyLocalVariables(body);
        
        // 🎯 CALCULAR COMPLEJIDAD AVANZADA
        this.complexity = calculateAdvancedComplexity(body);
        
        // 🎯 ESTIMAR LÍNEAS DE CÓDIGO
        this.lineCount = estimateLineCount(body);
    }

    private void countOperations(String body) {
        // 🎯 CONTAR TIPOS DE OPERACIONES
        String[] operationPatterns = {
            "if\\s*\\(", "for\\s*\\(", "while\\s*\\(", "switch\\s*\\(", 
            "try\\s*\\{", "catch\\s*\\(", "return\\s+", "throw\\s+",
            "new\\s+", "instanceof", "\\+\\+", "--", "\\+=", "-=", "\\*=", "/="
        };
        
        for (String pattern : operationPatterns) {
            java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher matcher = regex.matcher(body);
            int count = 0;
            while (matcher.find()) count++;
            operationCounts.put(pattern, count);
        }
    }

    private void identifyReferencedFields(String body) {
        // 🎯 PATRÓN SIMPLIFICADO PARA IDENTIFICAR CAMPOS (this.field o field)
        java.util.regex.Pattern fieldPattern = java.util.regex.Pattern.compile(
            "(?:this\\.)?(\\b[a-zA-Z_$][a-zA-Z0-9_$]*\\b)(?=\\s*[;=,)\\]])"
        );
        java.util.regex.Matcher matcher = fieldPattern.matcher(body);
        
        while (matcher.find()) {
            String fieldName = matcher.group(1);
            // Excluir palabras clave y métodos
            if (!isJavaKeyword(fieldName) && !fieldName.matches("^[a-z].*")) {
                referencedFields.add(fieldName);
            }
        }
    }

    private void identifyInvokedMethods(String body) {
        // 🎯 PATRÓN PARA IDENTIFICAR LLAMADAS A MÉTODOS
        java.util.regex.Pattern methodPattern = java.util.regex.Pattern.compile(
            "\\b([a-zA-Z_$][a-zA-Z0-9_$]*)\\s*\\("
        );
        java.util.regex.Matcher matcher = methodPattern.matcher(body);
        
        while (matcher.find()) {
            String methodName = matcher.group(1);
            if (!isJavaKeyword(methodName) && !methodName.equals("if") && 
                !methodName.equals("for") && !methodName.equals("while") &&
                !methodName.equals("switch") && !methodName.equals("catch")) {
                invokedMethods.add(methodName);
            }
        }
    }

    private void identifyLocalVariables(String body) {
        // 🎯 PATRÓN SIMPLIFICADO PARA VARIABLES LOCALES
        java.util.regex.Pattern varPattern = java.util.regex.Pattern.compile(
            "\\b(?:final\\s+)?([a-zA-Z_$][a-zA-Z0-9_$<>\\[\\]]+)\\s+([a-zA-Z_$][a-zA-Z0-9_$]*)\\s*[=;]"
        );
        java.util.regex.Matcher matcher = varPattern.matcher(body);
        
        while (matcher.find()) {
            localVariables.add(matcher.group(2));
        }
    }

    private int calculateAdvancedComplexity(String body) {
        int complexity = 1; // Complejidad base
        
        // 🎯 FACTORES DE COMPLEJIDAD
        complexity += operationCounts.getOrDefault("if\\s*\\(", 0);
        complexity += operationCounts.getOrDefault("for\\s*\\(", 0);
        complexity += operationCounts.getOrDefault("while\\s*\\(", 0);
        complexity += operationCounts.getOrDefault("switch\\s*\\(", 0);
        complexity += operationCounts.getOrDefault("catch\\s*\\(", 0);
        complexity += operationCounts.getOrDefault("throw\\s+", 0);
        
        // 🎯 COMPLEJIDAD POR OPERADORES LÓGICOS
        complexity += countOccurrences(body, "&&");
        complexity += countOccurrences(body, "\\|\\|");
        
        // 🎯 COMPLEJIDAD POR ANIDAMIENTO (estimación)
        complexity += countOccurrences(body, "\\{") / 2;
        
        return Math.max(1, complexity);
    }

    private int estimateLineCount(String body) {
        return (int) body.chars().filter(ch -> ch == '\n').count() + 1;
    }

    private int countOccurrences(String text, String pattern) {
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(text);
        int count = 0;
        while (matcher.find()) count++;
        return count;
    }

    private boolean isJavaKeyword(String word) {
        String[] keywords = {
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", 
            "char", "class", "const", "continue", "default", "do", "double", 
            "else", "enum", "extends", "final", "finally", "float", "for", 
            "goto", "if", "implements", "import", "instanceof", "int", 
            "interface", "long", "native", "new", "package", "private", 
            "protected", "public", "return", "short", "static", "strictfp", 
            "super", "switch", "synchronized", "this", "throw", "throws", 
            "transient", "try", "void", "volatile", "while"
        };
        return Arrays.asList(keywords).contains(word);
    }

    /**
     * 🎯 PROCESAMIENTO DE COMENTARIOS Y JAVADOC
     */
    private void extractCommentMetadata(String comment) {
        if (comment == null) return;
        
        // 🎯 DETECTAR COMENTARIOS TODO
        if (comment.toUpperCase().contains("TODO")) {
            extractTODOComments(comment);
        }
        
        // 🎯 DETECTAR COMENTARIOS FIXME
        if (comment.toUpperCase().contains("FIXME")) {
            todoComments.add("FIXME: " + extractNote(comment, "FIXME"));
        }
    }

    private void parseJavadocContent(String javadoc) {
        if (javadoc == null) return;
        
        String[] lines = javadoc.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            
            // 🎯 PROCESAR TAGS JAVADOC
            if (trimmed.startsWith("@")) {
                String[] parts = trimmed.split("\\s+", 2);
                String tagName = parts[0];
                String tagContent = parts.length > 1 ? parts[1] : "";
                
                javadocTags.put(tagName, tagContent);
                
                // 🎯 PROCESAMIENTO ESPECÍFICO POR TAG
                processSpecificJavadocTag(tagName, tagContent);
            }
        }
    }

    private void processSpecificJavadocTag(String tag, String content) {
        switch (tag.toLowerCase()) {
            case "@param":
                processParamTag(content);
                break;
            case "@return":
                processReturnTag(content);
                break;
            case "@throws":
            case "@exception":
                processThrowsTag(content);
                break;
            case "@see":
                processSeeTag(content);
                break;
        }
    }

    private void extractTODOComments(String comment) {
        java.util.regex.Pattern todoPattern = java.util.regex.Pattern.compile(
            "TODO[:\\.]?\\s*(.+)", java.util.regex.Pattern.CASE_INSENSITIVE
        );
        java.util.regex.Matcher matcher = todoPattern.matcher(comment);
        if (matcher.find()) {
            todoComments.add("TODO: " + matcher.group(1).trim());
        }
    }

    private String extractNote(String comment, String noteType) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
            noteType + "[:\\.]?\\s*(.+)", java.util.regex.Pattern.CASE_INSENSITIVE
        );
        java.util.regex.Matcher matcher = pattern.matcher(comment);
        return matcher.find() ? matcher.group(1).trim() : comment;
    }

    /**
     * 🎯 DETECCIÓN DE ANOTACIONES ESPECIALES
     */
    private void detectSpecialAnnotation(String annotationName, Map<String, String> parameters) {
        if (annotationName == null) return;
        
        String simpleName = getSimpleAnnotationName(annotationName);
        
        switch (simpleName) {
            case "Override":
                this.isOverride = true;
                break;
            case "Deprecated":
                this.isDeprecated = true;
                if (parameters != null && parameters.containsKey("since")) {
                    this.deprecatedSince = parameters.get("since").replace("\"", "");
                }
                break;
            case "Test":
                addAnnotation("TestFramework", "JUnit");
                break;
            case "Autowired":
            case "Inject":
                addAnnotation("DependencyInjection", "enabled");
                break;
            case "Transactional":
                addAnnotation("TransactionManagement", "enabled");
                break;
        }
    }

    private String getSimpleAnnotationName(String fullName) {
        if (fullName == null) return "";
        int lastDot = fullName.lastIndexOf('.');
        return lastDot >= 0 ? fullName.substring(lastDot + 1) : fullName;
    }

    // 🎯 MÉTODOS DE ACCESO ADICIONALES TURBO
    public Set<String> getReferencedFields() {
        return Collections.unmodifiableSet(referencedFields);
    }

    public Set<String> getInvokedMethods() {
        return Collections.unmodifiableSet(invokedMethods);
    }

    public Set<String> getLocalVariables() {
        return Collections.unmodifiableSet(localVariables);
    }

    public Map<String, Integer> getOperationCounts() {
        return Collections.unmodifiableMap(operationCounts);
    }

    public List<String> getTodoComments() {
        return Collections.unmodifiableList(todoComments);
    }

    public boolean hasTodoComments() {
        return !todoComments.isEmpty();
    }

    public boolean isOverride() {
        return isOverride;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public String getDeprecationReason() {
        return deprecationReason;
    }

    public String getDeprecatedSince() {
        return deprecatedSince;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    public String getSince() {
        return since;
    }

    /**
     * 🎯 GENERAR REPORTE DE CALIDAD COMPLETO DEL MÉTODO
     */
    public Map<String, Object> generateQualityReport() {
        Map<String, Object> report = new LinkedHashMap<>();
        
        report.put("method_name", this.name);
        report.put("complete_signature", getCompleteSignature());
        report.put("is_constructor", this.isConstructor);
        report.put("visibility", this.visibility);
        
        // 🎯 MÉTRICAS DE COMPLEJIDAD
        Map<String, Object> complexityMetrics = new HashMap<>();
        complexityMetrics.put("cyclomatic_complexity", this.complexity);
        complexityMetrics.put("line_count", this.lineCount);
        complexityMetrics.put("parameter_count", this.parameters.size());
        complexityMetrics.put("operation_counts", new HashMap<>(this.operationCounts));
        report.put("complexity_metrics", complexityMetrics);
        
        // 🎯 ANÁLISIS DE DEPENDENCIAS INTERNAS
        Map<String, Object> dependencyAnalysis = new HashMap<>();
        dependencyAnalysis.put("referenced_fields", new ArrayList<>(this.referencedFields));
        dependencyAnalysis.put("invoked_methods", new ArrayList<>(this.invokedMethods));
        dependencyAnalysis.put("local_variables", new ArrayList<>(this.localVariables));
        report.put("dependency_analysis", dependencyAnalysis);
        
        // 🎯 METADATAS AVANZADAS
        Map<String, Object> advancedMetadata = new HashMap<>();
        advancedMetadata.put("annotations_count", this.annotations.size());
        advancedMetadata.put("exceptions_count", this.exceptions.size());
        advancedMetadata.put("type_parameters_count", this.typeParameters.size());
        advancedMetadata.put("has_javadoc", this.javadoc != null);
        advancedMetadata.put("has_todo_comments", hasTodoComments());
        advancedMetadata.put("is_override", this.isOverride);
        advancedMetadata.put("is_deprecated", this.isDeprecated);
        advancedMetadata.put("is_default_method", this.isDefault);
        advancedMetadata.put("is_strictfp", this.isStrictfp);
        report.put("advanced_metadata", advancedMetadata);
        
        // 🎯 EVALUACIÓN DE CALIDAD
        Map<String, Object> qualityAssessment = new HashMap<>();
        qualityAssessment.put("quality_rating", calculateQualityRating());
        qualityAssessment.put("maintainability_score", calculateMaintainabilityScore());
        qualityAssessment.put("testability_score", calculateTestabilityScore());
        qualityAssessment.put("recommendations", generateMethodRecommendations());
        report.put("quality_assessment", qualityAssessment);
        
        return report;
    }

    private String calculateQualityRating() {
        if (this.complexity > 25 || this.lineCount > 100) return "POOR";
        if (this.complexity > 15 || this.lineCount > 50) return "FAIR";
        if (this.complexity > 10 || this.lineCount > 30) return "GOOD";
        return "EXCELLENT";
    }

    private double calculateMaintainabilityScore() {
        double score = 100.0;
        score -= this.complexity * 2.0;
        score -= Math.max(0, this.lineCount - 20) * 0.5;
        score -= this.parameters.size() * 3.0;
        score -= this.operationCounts.values().stream().mapToInt(Integer::intValue).sum() * 0.5;
        
        // 🎯 BONIFICACIONES POR BUENAS PRÁCTICAS
        if (this.javadoc != null) score += 5.0;
        if (this.annotations.containsKey("Override")) score += 3.0;
        if (this.parameters.size() <= 3) score += 5.0;
        
        return Math.max(0, Math.min(100, score));
    }

    private double calculateTestabilityScore() {
        double score = 100.0;
        score -= this.complexity * 1.5;
        score -= this.parameters.size() * 4.0;
        score -= this.exceptions.size() * 5.0;
        
        // 🎯 FACTORES QUE AFECTAN LA TESTABILIDAD
        if (this.isStatic) score -= 10.0;
        if (this.isPrivate) score -= 15.0;
        if (this.referencedFields.size() > 5) score -= 10.0;
        
        return Math.max(0, Math.min(100, score));
    }

    private List<String> generateMethodRecommendations() {
        List<String> recommendations = new ArrayList<>();
        
        if (this.complexity > 15) {
            recommendations.add("Considerar dividir el método - complejidad ciclomática alta");
        }
        
        if (this.lineCount > 50) {
            recommendations.add("Método demasiado largo - considerar extraer funcionalidades");
        }
        
        if (this.parameters.size() > 5) {
            recommendations.add("Demasiados parámetros - considerar usar objeto parámetro");
        }
        
        if (this.exceptions.size() > 3) {
            recommendations.add("Demasiadas excepciones - considerar excepción personalizada");
        }
        
        if (this.isDeprecated && this.deprecationReason == null) {
            recommendations.add("Agregar razón de deprecación en javadoc");
        }
        
        if (this.javadoc == null && !this.isOverride) {
            recommendations.add("Agregar documentación javadoc al método");
        }
        
        return recommendations;
    }

		/**
		 * 🚀 AGREGAR TAG JAVADOC AVANZADO - PROCESAMIENTO INTELIGENTE
		 */
		private Map<String, String> javadocTags = new HashMap<>();
		private Map<String, List<String>> javadocTagValues = new HashMap<>();
		private Set<String> processedTags = new HashSet<>();

		public void addJavadocTag(Optional<String> tagName, String content) {
		    if (tagName == null || !tagName.isPresent() || tagName.get().trim().isEmpty()) {
		        return;
		    }
		    
		    String name = tagName.get().trim();
		    String cleanedContent = content != null ? content.trim() : "";
		    
		    // 🎯 ALMACENAR TAG BÁSICO
		    this.javadocTags.put(name, cleanedContent);
		    
		    // 🎯 PROCESAMIENTO ESPECÍFICO POR TIPO DE TAG
		    processJavadocTagByType(name, cleanedContent);
		    
		    // 🎯 ALMACENAR MÚLTIPLES VALORES PARA TAGS REPETIDOS
		    this.javadocTagValues.computeIfAbsent(name, k -> new ArrayList<>()).add(cleanedContent);
		    
		    // 🎯 MARCAR COMO PROCESADO
		    this.processedTags.add(name);
		    
		    // 🎯 LOG DE DEBUG
		    System.out.println("🔖 Javadoc tag agregado: @" + name + " -> " + 
		                      (cleanedContent.length() > 50 ? cleanedContent.substring(0, 50) + "..." : cleanedContent));
		}

		/**
		 * 🎯 PROCESAR TAG JAVADOC POR TIPO ESPECÍFICO
		 */
		private void processJavadocTagByType(String tagName, String content) {
		    if (tagName == null) return;
		    
		    String lowerTag = tagName.toLowerCase();
		    
		    switch (lowerTag) {
		        case "@param":
		            processParamTag(content);
		            break;
		            
		        case "@return":
		        case "@returns":
		            processReturnTag(content);
		            break;
		            
		        case "@throws":
		        case "@exception":
		            processThrowsTag(content);
		            break;
		            
		        case "@see":
		            processSeeTag(content);
		            break;
		            
		        case "@author":
		            processAuthorTag(content);
		            break;
		            
		        case "@version":
		            processVersionTag(content);
		            break;
		            
		        case "@since":
		            processSinceTag(content);
		            break;
		            
		        case "@deprecated":
		            processDeprecatedTag(content);
		            break;
		            
		        case "@serial":
		        case "@serialfield":
		        case "@serialdata":
		            processSerialTag(tagName, content);
		            break;
		            
		        case "@implnote":
		        case "@implspec":
		            
		        case "@apiNote":
		        case "@implSpec":
		        case "@implNote":
		            processJava9PlusTag(tagName, content);
		            break;
		            
		        case "@hidden":
		            processHiddenTag(content);
		            break;
		            
		        case "@uses":
		            processUsesTag(content);
		            break;
		            
		        case "@provides":
		            processProvidesTag(content);
		            break;
		            
		        default:
		            processCustomTag(tagName, content);
		            break;
		    }
		}

		/**
		 * 🎯 PROCESAR TAG @param
		 */
		private void processParamTag(String content) {
		    if (content == null || content.trim().isEmpty()) return;
		    
		    // 🎯 FORMATO: @param parameterName description
		    String[] parts = content.split("\\s+", 2);
		    if (parts.length >= 2) {
		        String paramName = parts[0].trim();
		        String description = parts[1].trim();
		        
		        // 🎯 AGREGAR ANOTACIÓN ESPECÍFICA DEL PARÁMETRO
		        addAnnotation("ParamDocumentation", "\"" + paramName + ": " + description + "\"");
		        
		        // 🎯 ALMACENAR EN METADATOS ESPECÍFICOS
		        this.javadocTagValues.computeIfAbsent("@param_" + paramName, k -> new ArrayList<>())
		                            .add(description);
		        
		        System.out.println("📝 Documentación de parámetro: " + paramName + " -> " + description);
		    }
		}

		/**
		 * 🎯 PROCESAR TAG @return
		 */
		private void processReturnTag(String content) {
		    if (content == null) return;
		    
		    String description = content.trim();
		    
		    // 🎯 AGREGAR ANOTACIÓN DE RETORNO
		    addAnnotation("ReturnDocumentation", "\"" + description + "\"");
		    
		    // 🎯 EXTRACCIÓN DE TIPO DE RETORNO IMPLÍCITO
		    extractReturnTypeFromDescription(description);
		    
		    System.out.println("🔄 Documentación de retorno: " + description);
		}

		private void extractReturnTypeFromDescription(String description) {
		    if (description == null) return;
		    
		    String lowerDesc = description.toLowerCase();
		    
		    // 🎯 DETECTAR TIPOS DE RETORNO COMUNES EN DESCRIPCIONES
		    if (lowerDesc.contains("true") || lowerDesc.contains("false") || lowerDesc.contains("boolean")) {
		        addAnnotation("InferredReturnType", "boolean");
		    } else if (lowerDesc.contains("number") || lowerDesc.contains("integer") || lowerDesc.contains("int")) {
		        addAnnotation("InferredReturnType", "int");
		    } else if (lowerDesc.contains("string") || lowerDesc.contains("text")) {
		        addAnnotation("InferredReturnType", "String");
		    } else if (lowerDesc.contains("list") || lowerDesc.contains("array") || lowerDesc.contains("collection")) {
		        addAnnotation("InferredReturnType", "List");
		    } else if (lowerDesc.contains("map") || lowerDesc.contains("dictionary")) {
		        addAnnotation("InferredReturnType", "Map");
		    }
		}

		/**
		 * 🎯 PROCESAR TAG @throws / @exception
		 */
		private void processThrowsTag(String content) {
		    if (content == null || content.trim().isEmpty()) return;
		    
		    // 🎯 FORMATO: @throws ExceptionType description
		    String[] parts = content.split("\\s+", 2);
		    if (parts.length >= 2) {
		        String exceptionType = parts[0].trim();
		        String description = parts[1].trim();
		        
		        // 🎯 AGREGAR METADATOS DE EXCEPCIÓN
		        addAnnotation("ThrowsDocumentation", "\"" + exceptionType + ": " + description + "\"");
		        
		        // 🎯 CLASIFICAR EXCEPCIÓN
		        classifyException(exceptionType, description);
		        
		        System.out.println("⚠️ Documentación de excepción: " + exceptionType + " -> " + description);
		    }
		}

		private void classifyException(String exceptionType, String description) {
		    if (exceptionType == null) return;
		    
		    String lowerType = exceptionType.toLowerCase();
		    String lowerDesc = description.toLowerCase();
		    
		    // 🎯 DETECTAR TIPO DE EXCEPCIÓN
		    if (lowerType.contains("runtime") || lowerDesc.contains("unchecked")) {
		        addAnnotation("ExceptionCategory", "UNCHECKED");
		    } else if (lowerType.contains("error")) {
		        addAnnotation("ExceptionCategory", "ERROR");
		    } else {
		        addAnnotation("ExceptionCategory", "CHECKED");
		    }
		    
		    // 🎯 DETECTAR CONTEXTO DE EXCEPCIÓN
		    if (lowerDesc.contains("invalid") || lowerDesc.contains("invalid")) {
		        addAnnotation("ExceptionContext", "VALIDATION");
		    } else if (lowerDesc.contains("not found") || lowerDesc.contains("no encontrado")) {
		        addAnnotation("ExceptionContext", "NOT_FOUND");
		    } else if (lowerDesc.contains("null")) {
		        addAnnotation("ExceptionContext", "NULL_POINTER");
		    } else if (lowerDesc.contains("io") || lowerDesc.contains("file")) {
		        addAnnotation("ExceptionContext", "IO_OPERATION");
		    } else if (lowerDesc.contains("network") || lowerDesc.contains("connection")) {
		        addAnnotation("ExceptionContext", "NETWORK");
		    }
		}

		/**
		 * 🎯 PROCESAR TAG @see
		 */
		private void processSeeTag(String content) {
		    if (content == null) return;
		    
		    String reference = content.trim();
		    
		    // 🎯 AGREGAR REFERENCIA
		    addAnnotation("SeeReference", "\"" + reference + "\"");
		    
		    // 🎯 CLASIFICAR TIPO DE REFERENCIA
		    classifySeeReference(reference);
		    
		    System.out.println("👁️ Referencia @see: " + reference);
		}

		private void classifySeeReference(String reference) {
		    if (reference == null) return;
		    
		    // 🎯 DETECTAR TIPO DE REFERENCIA
		    if (reference.contains("#")) {
		        addAnnotation("ReferenceType", "METHOD_REFERENCE");
		    } else if (reference.contains("http://") || reference.contains("https://")) {
		        addAnnotation("ReferenceType", "URL_REFERENCE");
		    } else if (reference.contains("(") && reference.contains(")")) {
		        addAnnotation("ReferenceType", "CONSTRUCTOR_REFERENCE");
		    } else if (reference.contains("<") && reference.contains(">")) {
		        addAnnotation("ReferenceType", "GENERIC_REFERENCE");
		    } else {
		        addAnnotation("ReferenceType", "CLASS_REFERENCE");
		    }
		}

		/**
		 * 🎯 PROCESAR TAG @author
		 */
		private void processAuthorTag(String content) {
		    if (content == null) return;
		    
		    String author = content.trim();
		    
		    // 🎯 AGREGAR INFORMACIÓN DE AUTOR
		    addAnnotation("Author", "\"" + author + "\"");
		    
		    // 🎯 EXTRACCIÓN DE METADATOS DEL AUTOR
		    extractAuthorMetadata(author);
		    
		    System.out.println("👤 Autor: " + author);
		}

		private void extractAuthorMetadata(String author) {
		    if (author == null) return;
		    
		    // 🎯 DETECTAR FORMATOS DE AUTOR COMUNES
		    if (author.contains("<") && author.contains(">")) {
		        // Formato: Nombre <email>
		        int emailStart = author.indexOf('<');
		        int emailEnd = author.indexOf('>');
		        if (emailStart >= 0 && emailEnd > emailStart) {
		            String name = author.substring(0, emailStart).trim();
		            String email = author.substring(emailStart + 1, emailEnd).trim();
		            
		            addAnnotation("AuthorName", "\"" + name + "\"");
		            addAnnotation("AuthorEmail", "\"" + email + "\"");
		        }
		    } else if (author.contains("@")) {
		        // Probablemente solo email
		        addAnnotation("AuthorEmail", "\"" + author + "\"");
		    } else {
		        // Solo nombre
		        addAnnotation("AuthorName", "\"" + author + "\"");
		    }
		}

		/**
		 * 🎯 PROCESAR TAG @version
		 */
		private void processVersionTag(String content) {
		    if (content == null) return;
		    
		    String version = content.trim();
		    
		    // 🎯 AGREGAR INFORMACIÓN DE VERSIÓN
		    addAnnotation("Version", "\"" + version + "\"");
		    
		    // 🎯 ANÁLISIS SEMÁNTICO DE VERSIÓN
		    analyzeVersion(version);
		    
		    System.out.println("🔢 Versión: " + version);
		}

		private void analyzeVersion(String version) {
		    if (version == null) return;
		    
		    // 🎯 DETECTAR FORMATOS DE VERSIÓN
		    java.util.regex.Pattern semverPattern = java.util.regex.Pattern.compile(
		        "(\\d+)\\.(\\d+)\\.(\\d+)(?:-([a-zA-Z0-9.-]+))?(?:\\+([a-zA-Z0-9.-]+))?"
		    );
		    java.util.regex.Matcher matcher = semverPattern.matcher(version);
		    
		    if (matcher.matches()) {
		        addAnnotation("VersionFormat", "SEMVER");
		        addAnnotation("MajorVersion", matcher.group(1));
		        addAnnotation("MinorVersion", matcher.group(2));
		        addAnnotation("PatchVersion", matcher.group(3));
		        
		        if (matcher.group(4) != null) {
		            addAnnotation("PreRelease", "\"" + matcher.group(4) + "\"");
		        }
		        if (matcher.group(5) != null) {
		            addAnnotation("BuildMetadata", "\"" + matcher.group(5) + "\"");
		        }
		    } else if (version.matches("\\d+\\.\\d+")) {
		        addAnnotation("VersionFormat", "MAJOR_MINOR");
		    } else if (version.matches("\\d+")) {
		        addAnnotation("VersionFormat", "MAJOR_ONLY");
		    } else {
		        addAnnotation("VersionFormat", "CUSTOM");
		    }
		}

		/**
		 * 🎯 PROCESAR TAG @since
		 */
		private void processSinceTag(String content) {
		    if (content == null) return;
		    
		    String since = content.trim();
		    
		    // 🎯 AGREGAR INFORMACIÓN "SINCE"
		    addAnnotation("Since", "\"" + since + "\"");
		    
		    // 🎯 ANÁLISIS DE FECHA/VERSIÓN
		    analyzeSinceValue(since);
		    
		    System.out.println("📅 Since: " + since);
		}

		private void analyzeSinceValue(String since) {
		    if (since == null) return;
		    
		    // 🎯 DETECTAR FORMATOS COMUNES
		    if (since.matches("\\d+\\.\\d+(\\.\\d+)?")) {
		        addAnnotation("SinceType", "VERSION");
		    } else if (since.matches("\\d{4}-\\d{2}-\\d{2}")) {
		        addAnnotation("SinceType", "DATE");
		    } else if (since.matches("JDK\\s*\\d+")) {
		        addAnnotation("SinceType", "JDK_VERSION");
		    } else {
		        addAnnotation("SinceType", "TEXT");
		    }
		}

		/**
		 * 🎯 PROCESAR TAG @deprecated
		 */
		private void processDeprecatedTag(String content) {
		    if (content == null) return;
		    
		    String reason = content.trim();
		    
		    // 🎯 MARCAR COMO DEPRECADO
		    addAnnotation("Deprecated", "true");
		    addAnnotation("DeprecationReason", "\"" + reason + "\"");
		    
		    // 🎯 EXTRACCIÓN DE INFORMACIÓN DE DEPRECACIÓN
		    extractDeprecationInfo(reason);
		    
		    System.out.println("🗑️ Deprecated: " + reason);
		}

		private void extractDeprecationInfo(String reason) {
		    if (reason == null) return;
		    
		    String lowerReason = reason.toLowerCase();
		    
		    // 🎯 DETECTAR RAZONES COMUNES DE DEPRECACIÓN
		    if (lowerReason.contains("use") && lowerReason.contains("instead")) {
		        extractReplacementFromReason(reason);
		    }
		    
		    if (lowerReason.contains("since")) {
		        extractDeprecationVersion(reason);
		    }
		    
		    if (lowerReason.contains("will be removed") || lowerReason.contains("será eliminado")) {
		        addAnnotation("RemovalPlanned", "true");
		    }
		}

		private void extractReplacementFromReason(String reason) {
		    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
		        "(?i)use\\s+([^\\s,]+)\\s+instead"
		    );
		    java.util.regex.Matcher matcher = pattern.matcher(reason);
		    if (matcher.find()) {
		        addAnnotation("Replacement", "\"" + matcher.group(1) + "\"");
		    }
		}

		private void extractDeprecationVersion(String reason) {
		    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
		        "(?i)since\\s+(\\d+\\.\\d+(?:\\.\\d+)?)"
		    );
		    java.util.regex.Matcher matcher = pattern.matcher(reason);
		    if (matcher.find()) {
		        addAnnotation("DeprecatedSince", "\"" + matcher.group(1) + "\"");
		    }
		}

		/**
		 * 🎯 PROCESAR TAGS DE SERIALIZACIÓN
		 */
		private void processSerialTag(String tagName, String content) {
		    if (content == null) return;
		    
		    addAnnotation("SerializationInfo", "\"" + tagName + ": " + content + "\"");
		    System.out.println("💾 Tag de serialización: " + tagName + " -> " + content);
		}

		/**
		 * 🎯 PROCESAR TAGS DE IMPLEMENTACIÓN
		 */
		private void processImplementationTag(String tagName, String content) {
		    if (content == null) return;
		    
		    addAnnotation("ImplementationNote", "\"" + content + "\"");
		    addAnnotation("InternalDocumentation", "true");
		    System.out.println("🔧 Nota de implementación: " + content);
		}

		/**
		 * 🎯 PROCESAR TAGS JAVA 9+
		 */
		private void processJava9PlusTag(String tagName, String content) {
		    if (content == null) return;
		    
		    addAnnotation("Java9PlusTag", "\"" + tagName + ": " + content + "\"");
		    
		    switch (tagName) {
		        case "@apiNote":
		            addAnnotation("APINote", "\"" + content + "\"");
		            break;
		        case "@implSpec":
		            addAnnotation("ImplementationSpec", "\"" + content + "\"");
		            break;
		        case "@implNote":
		            addAnnotation("ImplementationDetail", "\"" + content + "\"");
		            break;
		    }
		    
		    System.out.println("☕ Tag Java 9+: " + tagName + " -> " + content);
		}

		/**
		 * 🎯 PROCESAR TAG @hidden
		 */
		private void processHiddenTag(String content) {
		    if (content == null) return;
		    
		    addAnnotation("Hidden", "true");
		    addAnnotation("InternalAPI", "true");
		    System.out.println("👻 Elemento oculto: " + content);
		}

		/**
		 * 🎯 PROCESAR TAGS @uses Y @provides
		 */
		private void processUsesTag(String content) {
		    if (content == null) return;
		    
		    addAnnotation("UsesService", "\"" + content + "\"");
		    System.out.println("🔗 Usa servicio: " + content);
		}

		private void processProvidesTag(String content) {
		    if (content == null) return;
		    
		    addAnnotation("ProvidesService", "\"" + content + "\"");
		    System.out.println("🏗️ Provee servicio: " + content);
		}

		/**
		 * 🎯 PROCESAR TAGS PERSONALIZADOS
		 */
		private void processCustomTag(String tagName, String content) {
		    if (content == null) return;
		    
		    addAnnotation("CustomJavadocTag", "\"" + tagName + ": " + content + "\"");
		    
		    // 🎯 DETECTAR TAGS PERSONALIZADOS COMUNES
		    if (tagName.startsWith("@todo") || tagName.equals("@TODO")) {
		        addAnnotation("TODO", "\"" + content + "\"");
		    } else if (tagName.startsWith("@note") || tagName.equals("@NOTE")) {
		        addAnnotation("Note", "\"" + content + "\"");
		    } else if (tagName.startsWith("@warning") || tagName.equals("@WARNING")) {
		        addAnnotation("Warning", "\"" + content + "\"");
		    }
		    
		    System.out.println("🎯 Tag personalizado: " + tagName + " -> " + content);
		}

		// 🎯 MÉTODOS DE ACCESO ADICIONALES
		public Map<String, String> getJavadocTags() {
		    return Collections.unmodifiableMap(javadocTags);
		}

		public Map<String, List<String>> getJavadocTagValues() {
		    return Collections.unmodifiableMap(javadocTagValues);
		}

		public Set<String> getProcessedTags() {
		    return Collections.unmodifiableSet(processedTags);
		}

		public boolean hasJavadocTag(String tagName) {
		    return javadocTags.containsKey(tagName) || processedTags.contains(tagName);
		}

		public List<String> getJavadocTagValues(String tagName) {
		    return javadocTagValues.getOrDefault(tagName, Collections.emptyList());
		}

		public String getFirstJavadocTagValue(String tagName) {
		    List<String> values = getJavadocTagValues(tagName);
		    return values.isEmpty() ? null : values.get(0);
		}

		/**
		 * 🎯 GENERAR REPORTE DE JAVADOC COMPLETO
		 */
		public Map<String, Object> generateJavadocReport() {
		    Map<String, Object> report = new LinkedHashMap<>();
		    
		    report.put("total_tags", javadocTags.size());
		    report.put("processed_tags", new ArrayList<>(processedTags));
		    report.put("all_tags", new HashMap<>(javadocTags));
		    report.put("tag_values", new HashMap<>(javadocTagValues));
		    
		    // 🎯 ANÁLISIS DE COMPLETITUD
		    Map<String, Object> completeness = new HashMap<>();
		    completeness.put("has_params", hasParamDocumentation());
		    completeness.put("has_return", hasReturnDocumentation());
		    completeness.put("has_throws", hasThrowsDocumentation());
		    completeness.put("has_author", hasAuthor());
		    completeness.put("has_version", hasVersion());
		    completeness.put("has_since", hasSince());
		    completeness.put("completeness_score", calculateJavadocCompleteness());
		    report.put("completeness_analysis", completeness);
		    
		    // 🎯 METADATOS EXTRAÍDOS
		    Map<String, Object> extractedMetadata = new HashMap<>();
		    extractedMetadata.put("authors", extractAllAuthors());
		    extractedMetadata.put("versions", extractAllVersions());
		    extractedMetadata.put("exceptions", extractAllExceptions());
		    extractedMetadata.put("see_references", extractAllSeeReferences());
		    extractedMetadata.put("custom_tags", extractCustomTags());
		    report.put("extracted_metadata", extractedMetadata);
		    
		    return report;
		}

		private boolean hasParamDocumentation() {
		    return javadocTags.keySet().stream().anyMatch(tag -> tag.toLowerCase().equals("@param"));
		}

		private boolean hasReturnDocumentation() {
		    return javadocTags.keySet().stream().anyMatch(tag -> 
		        tag.toLowerCase().equals("@return") || tag.toLowerCase().equals("@returns"));
		}

		private boolean hasThrowsDocumentation() {
		    return javadocTags.keySet().stream().anyMatch(tag -> 
		        tag.toLowerCase().equals("@throws") || tag.toLowerCase().equals("@exception"));
		}

		private boolean hasAuthor() {
		    return javadocTags.containsKey("@author");
		}

		private boolean hasVersion() {
		    return javadocTags.containsKey("@version");
		}

		private boolean hasSince() {
		    return javadocTags.containsKey("@since");
		}

		private int calculateJavadocCompleteness() {
		    int score = 0;
		    if (hasParamDocumentation()) score += 30;
		    if (hasReturnDocumentation()) score += 30;
		    if (hasThrowsDocumentation()) score += 20;
		    if (hasAuthor()) score += 10;
		    if (hasVersion()) score += 5;
		    if (hasSince()) score += 5;
		    return score;
		}

		private List<String> extractAllAuthors() {
		    return getJavadocTagValues("@author");
		}

		private List<String> extractAllVersions() {
		    return getJavadocTagValues("@version");
		}

		private List<String> extractAllExceptions() {
		    List<String> exceptions = new ArrayList<>();
		    exceptions.addAll(getJavadocTagValues("@throws"));
		    exceptions.addAll(getJavadocTagValues("@exception"));
		    return exceptions;
		}

		private List<String> extractAllSeeReferences() {
		    return getJavadocTagValues("@see");
		}

		private List<String> extractCustomTags() {
		    return javadocTags.keySet().stream()
		        .filter(tag -> !tag.matches("(@param|@return|@throws|@exception|@see|@author|@version|@since|@deprecated)"))
		        .collect(Collectors.toList());
		
	}
	
}