package com.elreinodelolvido.ellibertad.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 🏴‍☠️ ParameterInfo - Representa un parámetro de método
 */
public class ParameterInfo {
    private String name;
    private String type;
    private boolean isFinal;
    private Map<String, String> annotations;

    public ParameterInfo() {
        this.annotations = new HashMap<>();
    }

    public ParameterInfo(String name, String type) {
        this();
        this.name = name;
        this.type = type;
    }

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isFinal() { return isFinal; }
    public void setFinal(boolean isFinal) { this.isFinal = isFinal; }

    public Map<String, String> getAnnotations() { return Collections.unmodifiableMap(annotations); }
    public void setAnnotations(Map<String, String> annotations) { this.annotations = new HashMap<>(annotations); }

    public void addAnnotation(String name, String value) {
        this.annotations.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        // Anotaciones
        for (String annotation : annotations.keySet()) {
            sb.append("@").append(annotation).append(" ");
        }
        
        if (isFinal) sb.append("final ");
        sb.append(type).append(" ").append(name);
        
        return sb.toString();
    }

    /**
     * 🎯 OBTENER DECLARACIÓN COMPLETA DEL PARÁMETRO
     */
    public String getCompleteDeclaration() {
        StringBuilder sb = new StringBuilder();
        
        // Anotaciones
        if (!annotations.isEmpty()) {
            for (Map.Entry<String, String> annotation : annotations.entrySet()) {
                sb.append("@").append(annotation.getKey());
                if (annotation.getValue() != null && !annotation.getValue().isEmpty()) {
                    sb.append("(").append(annotation.getValue()).append(")");
                }
                sb.append(" ");
            }
        }
        
        // Modificador final
        if (isFinal) {
            sb.append("final ");
        }
        
        // Tipo y nombre
        sb.append(type).append(" ").append(name);
        
        return sb.toString();
    }

    /**
     * 🎯 OBTENER EL TIPO SIMPLE (sin package)
     */
    public String getSimpleType() {
        if (type == null) {
            return "";
        }
        
        // Para tipos genéricos, procesar la parte base
        if (type.contains("<")) {
            String baseType = type.substring(0, type.indexOf('<'));
            String genericPart = type.substring(type.indexOf('<'));
            return getSimpleName(baseType) + genericPart;
        }
        
        // Para arrays
        if (type.endsWith("[]")) {
            String baseType = type.substring(0, type.length() - 2);
            return getSimpleName(baseType) + "[]";
        }
        
        return getSimpleName(type);
    }

    /**
     * 🎯 OBTENER NOMBRE SIMPLE DEL TIPO (sin package)
     */
    private String getSimpleName(String fullTypeName) {
        if (fullTypeName == null) {
            return "";
        }
        
        int lastDot = fullTypeName.lastIndexOf('.');
        return lastDot >= 0 ? fullTypeName.substring(lastDot + 1) : fullTypeName;
    }

    /**
     * 🎯 VERIFICAR SI EL PARÁMETRO TIENE ANOTACIONES
     */
    public boolean hasAnnotations() {
        return annotations != null && !annotations.isEmpty();
    }

    /**
     * 🎯 VERIFICAR SI EL PARÁMETRO TIENE UNA ANOTACIÓN ESPECÍFICA
     */
    public boolean hasAnnotation(String annotationName) {
        if (annotationName == null || annotations == null) {
            return false;
        }
        
        return annotations.keySet().stream()
            .anyMatch(key -> 
                key.equals(annotationName) || 
                key.endsWith("." + annotationName) ||
                getSimpleAnnotationName(key).equals(annotationName)
            );
    }

    /**
     * 🎯 OBTENER NOMBRE SIMPLE DE ANOTACIÓN (sin package)
     */
    private String getSimpleAnnotationName(String fullAnnotationName) {
        if (fullAnnotationName == null) return "";
        int lastDot = fullAnnotationName.lastIndexOf('.');
        return lastDot >= 0 ? fullAnnotationName.substring(lastDot + 1) : fullAnnotationName;
    }

    /**
     * 🎯 VERIFICAR SI EL PARÁMETRO TIENE ERRORES
     */
    public boolean hasErrors() {
        // Verificar errores básicos de estructura
        if (name == null || name.trim().isEmpty()) {
            return true;
        }
        
        if (type == null || type.trim().isEmpty()) {
            return true;
        }
        
        // Verificar nombre inválido
        if (!name.matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$")) {
            return true;
        }
        
        // Verificar tipo inválido
        if (!isValidType(type)) {
            return true;
        }
        
        return false;
    }

    /**
     * 🎯 VALIDAR SI EL TIPO ES VÁLIDO
     */
    private boolean isValidType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return false;
        }
        
        // Tipos primitivos válidos
        String[] primitiveTypes = {"byte", "short", "int", "long", "float", "double", "boolean", "char", "void"};
        if (Arrays.asList(primitiveTypes).contains(type)) {
            return true;
        }
        
        // Tipos de array
        if (type.endsWith("[]")) {
            String baseType = type.substring(0, type.length() - 2);
            return isValidType(baseType);
        }
        
        // Tipos genéricos (simplificado)
        if (type.contains("<") && type.contains(">")) {
            // Para tipos genéricos, validamos la parte base
            String baseType = type.substring(0, type.indexOf('<'));
            return baseType.matches("^[a-zA-Z_$][a-zA-Z0-9_$]*(\\.[a-zA-Z_$][a-zA-Z0-9_$]*)*$");
        }
        
        // Tipos de clase normales (deben seguir el patrón de nombre Java)
        return type.matches("^[a-zA-Z_$][a-zA-Z0-9_$]*(\\.[a-zA-Z_$][a-zA-Z0-9_$]*)*$");
    }

    /**
     * 🎯 OBTENER INFORMACIÓN DE CALIDAD DEL PARÁMETRO
     */
    public Map<String, Object> getQualityInfo() {
        Map<String, Object> qualityInfo = new HashMap<>();
        
        qualityInfo.put("name", name);
        qualityInfo.put("type", type);
        qualityInfo.put("simpleType", getSimpleType());
        qualityInfo.put("isFinal", isFinal);
        qualityInfo.put("hasAnnotations", hasAnnotations());
        qualityInfo.put("annotationCount", annotations.size());
        qualityInfo.put("hasErrors", hasErrors());
        
        // Evaluación de calidad
        String quality = "GOOD";
        if (hasErrors()) {
            quality = "ERROR";
        } else if (type.length() > 50) { // Tipo muy largo
            quality = "POOR";
        } else if (annotations.size() > 3) { // Demasiadas anotaciones
            quality = "FAIR";
        }
        qualityInfo.put("qualityRating", quality);
        
        return qualityInfo;
    }

    /**
     * 🚀 MÉTODOS TURBO IMPLEMENTADOS - ANÁLISIS EXHAUSTIVO DE PARÁMETROS
     */

    // 🎯 VARIABLES DE INSTANCIA TURBO
    private boolean isVarArgs;
    private String description;
    private String defaultValue;
    private boolean isNullable;
    private boolean isNotNull;
    private String validationRules;
    private Map<String, Map<String, String>> annotationParameters = new HashMap<>();
    private Set<String> referencedTypes = new HashSet<>();
    private int parameterIndex;
    private String parameterRole; // INPUT, OUTPUT, BOTH, CONTEXT

    // 🎯 VARARGS - PARÁMETROS DE ARGUMENTOS VARIABLES
    public void setVarArgs(boolean isVarArgs) {
        this.isVarArgs = isVarArgs;
        
        // 🎯 AJUSTAR AUTOMÁTICAMENTE EL TIPO PARA VARARGS
        if (isVarArgs && type != null && !type.endsWith("...")) {
            // Convertir tipo[] a tipo...
            if (type.endsWith("[]")) {
                this.type = type.substring(0, type.length() - 2) + "...";
            } else {
                this.type = type + "...";
            }
            
            // 🎯 AGREGAR ANOTACIÓN DE METADATO
            addAnnotation("VarArgs", "true");
        }
    }

    public boolean isVarArgs() {
        return isVarArgs;
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
        
        // 🎯 ALMACENAR PARÁMETROS POR SEPARADO PARA ANÁLISIS
        if (parameters != null) {
            this.annotationParameters.put(name, new HashMap<>(parameters));
        }
        
        // 🎯 DETECCIÓN AUTOMÁTICA DE ANOTACIONES ESPECIALES
        detectSpecialParameterAnnotation(name, parameters);
    }

    // 🎯 DESCRIPCIÓN Y DOCUMENTACIÓN
    public void setDescription(String description) {
        this.description = description;
        
        // 🎯 EXTRACCIÓN AUTOMÁTICA DE METADATOS DE LA DESCRIPCIÓN
        if (description != null) {
            extractDescriptionMetadata(description);
        }
    }

    public String getDescription() {
        return description;
    }

    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }

    // 🎯 MÉTODOS DE ACCESO ADICIONALES TURBO
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        if (defaultValue != null) {
            addAnnotation("DefaultValue", "\"" + defaultValue + "\"");
        }
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setNullable(boolean isNullable) {
        this.isNullable = isNullable;
        if (isNullable) {
            addAnnotation("Nullable", "");
            this.isNotNull = false; // Mutuamente excluyentes
        }
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNotNull(boolean isNotNull) {
        this.isNotNull = isNotNull;
        if (isNotNull) {
            addAnnotation("NotNull", "");
            this.isNullable = false; // Mutuamente excluyentes
        }
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public void setValidationRules(String rules) {
        this.validationRules = rules;
        if (rules != null && !rules.trim().isEmpty()) {
            addAnnotation("Validation", "\"" + rules + "\"");
        }
    }

    public String getValidationRules() {
        return validationRules;
    }

    public void setParameterIndex(int index) {
        this.parameterIndex = index;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public void setParameterRole(String role) {
        this.parameterRole = role;
        if (role != null) {
            addAnnotation("ParameterRole", "\"" + role + "\"");
        }
    }

    public String getParameterRole() {
        return parameterRole;
    }

    // 🎯 MÉTODOS PRIVADOS DE ANÁLISIS TURBO

    /**
     * 🎯 DETECTAR ANOTACIONES ESPECIALES DE PARÁMETROS
     */
    private void detectSpecialParameterAnnotation(String annotationName, Map<String, String> parameters) {
        if (annotationName == null) return;
        
        String simpleName = getSimpleAnnotationName(annotationName);
        
        switch (simpleName) {
            case "NotNull":
            case "Nonnull":
                this.isNotNull = true;
                this.isNullable = false;
                break;
                
            case "Nullable":
                this.isNullable = true;
                this.isNotNull = false;
                break;
                
            case "Min":
            case "Max":
            case "Size":
            case "Pattern":
                if (this.validationRules == null) {
                    this.validationRules = "";
                }
                this.validationRules += simpleName + " ";
                if (parameters != null && parameters.containsKey("value")) {
                    this.validationRules += parameters.get("value") + "; ";
                }
                break;
                
            case "Autowired":
            case "Inject":
                setParameterRole("DEPENDENCY_INJECTION");
                break;
                
            case "RequestBody":
            case "RequestParam":
            case "PathVariable":
                setParameterRole("HTTP_INPUT");
                break;
                
            case "Valid":
                setParameterRole("VALIDATION_TARGET");
                break;
                
            case "DefaultValue":
                if (parameters != null && parameters.containsKey("value")) {
                    this.defaultValue = parameters.get("value").replace("\"", "");
                }
                break;
        }
    }

    /**
     * 🎯 EXTRAER METADATOS DE LA DESCRIPCIÓN
     */
    private void extractDescriptionMetadata(String description) {
        if (description == null) return;
        
        // 🎯 DETECTAR PATRONES EN LA DESCRIPCIÓN
        String lowerDesc = description.toLowerCase();
        
        if (lowerDesc.contains("optional") || lowerDesc.contains("opcional")) {
            addAnnotation("Optional", "true");
        }
        
        if (lowerDesc.contains("required") || lowerDesc.contains("requerido")) {
            addAnnotation("Required", "true");
            this.isNotNull = true;
        }
        
        if (lowerDesc.contains("default") || lowerDesc.contains("por defecto")) {
            extractDefaultValueFromDescription(description);
        }
        
        if (lowerDesc.contains("validation") || lowerDesc.contains("validación")) {
            extractValidationFromDescription(description);
        }
        
        // 🎯 DETECTAR TIPOS DE PARÁMETROS POR DESCRIPCIÓN
        detectParameterTypeFromDescription(description);
    }

    private void extractDefaultValueFromDescription(String description) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
            "(?i)default\\s*(?:value)?\\s*[:=]?\\s*([^.,;]+)"
        );
        java.util.regex.Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            this.defaultValue = matcher.group(1).trim();
            addAnnotation("DefaultValue", "\"" + this.defaultValue + "\"");
        }
    }

    private void extractValidationFromDescription(String description) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
            "(?i)(?:must|debe)\\s+be\\s+([^.,;]+)"
        );
        java.util.regex.Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            String validation = matcher.group(1).trim();
            if (this.validationRules == null) {
                this.validationRules = validation;
            } else {
                this.validationRules += "; " + validation;
            }
        }
    }

    private void detectParameterTypeFromDescription(String description) {
        String lowerDesc = description.toLowerCase();
        
        if (lowerDesc.contains("input") || lowerDesc.contains("entrada")) {
            setParameterRole("INPUT");
        } else if (lowerDesc.contains("output") || lowerDesc.contains("salida")) {
            setParameterRole("OUTPUT");
        } else if (lowerDesc.contains("context") || lowerDesc.contains("contexto")) {
            setParameterRole("CONTEXT");
        } else if (lowerDesc.contains("configuration") || lowerDesc.contains("configuración")) {
            setParameterRole("CONFIGURATION");
        } else if (lowerDesc.contains("callback") || lowerDesc.contains("retrollamada")) {
            setParameterRole("CALLBACK");
        }
    }

    /**
     * 🎯 ANÁLISIS DE TIPOS AVANZADO
     */
    public void analyzeTypeDependencies() {
        if (type == null) return;
        
        // 🎯 IDENTIFICAR TIPOS REFERENCIADOS
        identifyReferencedTypes(type);
        
        // 🎯 CLASIFICACIÓN DEL TIPO
        classifyParameterType();
    }

    private void identifyReferencedTypes(String typeString) {
        if (typeString == null) return;
        
        // 🎯 PARA TIPOS GENÉRICOS, EXTRAER TIPOS INTERNOS
        if (typeString.contains("<") && typeString.contains(">")) {
            String genericContent = typeString.substring(typeString.indexOf('<') + 1, typeString.lastIndexOf('>'));
            String[] genericTypes = genericContent.split(",");
            
            for (String genericType : genericTypes) {
                String cleanedType = genericType.trim();
                if (cleanedType.contains("<")) {
                    // Tipo genérico anidado - procesar recursivamente
                    identifyReferencedTypes(cleanedType);
                } else {
                    referencedTypes.add(getSimpleName(cleanedType));
                }
            }
        }
        
        // 🎯 AGREGAR EL TIPO PRINCIPAL
        String mainType = typeString;
        if (mainType.contains("<")) {
            mainType = mainType.substring(0, mainType.indexOf('<'));
        }
        if (mainType.endsWith("...")) {
            mainType = mainType.substring(0, mainType.length() - 3);
        }
        if (mainType.endsWith("[]")) {
            mainType = mainType.substring(0, mainType.length() - 2);
        }
        
        referencedTypes.add(getSimpleName(mainType));
    }

    private void classifyParameterType() {
        if (type == null) return;
        
        String simpleType = getSimpleType().toLowerCase();
        
        // 🎯 CLASIFICACIÓN POR CATEGORÍA
        if (isPrimitiveType(simpleType)) {
            addAnnotation("TypeCategory", "PRIMITIVE");
        } else if (isCollectionType(simpleType)) {
            addAnnotation("TypeCategory", "COLLECTION");
            setParameterRole("COLLECTION_INPUT");
        } else if (isFunctionalType(simpleType)) {
            addAnnotation("TypeCategory", "FUNCTIONAL");
            setParameterRole("CALLBACK");
        } else if (isBeanType(simpleType)) {
            addAnnotation("TypeCategory", "BEAN");
            setParameterRole("BEAN_INPUT");
        } else {
            addAnnotation("TypeCategory", "CUSTOM");
        }
        
        // 🎯 CLASIFICACIÓN POR PROPÓSITO
        if (simpleType.contains("request") || simpleType.contains("dto")) {
            setParameterRole("DATA_TRANSFER");
        } else if (simpleType.contains("config") || simpleType.contains("properties")) {
            setParameterRole("CONFIGURATION");
        } else if (simpleType.contains("context")) {
            setParameterRole("CONTEXT");
        }
    }

    private boolean isPrimitiveType(String type) {
        String[] primitives = {"byte", "short", "int", "long", "float", "double", "boolean", "char"};
        return Arrays.asList(primitives).contains(type);
    }

    private boolean isCollectionType(String type) {
        return type.contains("list") || type.contains("set") || type.contains("map") || 
               type.contains("collection") || type.contains("array") || type.endsWith("[]");
    }

    private boolean isFunctionalType(String type) {
        return type.contains("function") || type.contains("consumer") || 
               type.contains("supplier") || type.contains("predicate") ||
               type.contains("runnable") || type.contains("callable");
    }

    private boolean isBeanType(String type) {
        return !isPrimitiveType(type) && !isCollectionType(type) && !isFunctionalType(type) &&
               Character.isUpperCase(type.charAt(0)) && !type.contains(".");
    }

    // 🎯 MÉTODOS DE UTILIDAD AVANZADOS

    /**
     * 🎯 OBTENER DECLARACIÓN COMPLETA MEJORADA
     */
    public String getEnhancedDeclaration() {
        StringBuilder sb = new StringBuilder();
        
        // 🎯 ANOTACIONES CON PARÁMETROS
        if (!annotations.isEmpty()) {
            for (Map.Entry<String, String> annotation : annotations.entrySet()) {
                sb.append(annotation.getValue()).append(" ");
            }
        }
        
        // 🎯 MODIFICADORES
        if (isFinal) sb.append("final ");
        if (isVarArgs) sb.append("/* varargs */ ");
        
        // 🎯 TIPO Y NOMBRE
        sb.append(type).append(" ").append(name);
        
        // 🎯 VALOR POR DEFECTO (si existe)
        if (defaultValue != null) {
            sb.append(" = ").append(defaultValue);
        }
        
        return sb.toString();
    }

    /**
     * 🎯 OBTENER INFORMACIÓN DE ANOTACIONES DETALLADA
     */
    public Map<String, Object> getAnnotationDetails() {
        Map<String, Object> details = new HashMap<>();
        
        details.put("count", annotations.size());
        details.put("annotations", new HashMap<>(annotations));
        details.put("parameters", new HashMap<>(annotationParameters));
        
        // 🎯 ANÁLISIS DE ANOTACIONES
        List<String> validationAnnotations = new ArrayList<>();
        List<String> dependencyAnnotations = new ArrayList<>();
        List<String> metadataAnnotations = new ArrayList<>();
        
        for (String annotation : annotations.keySet()) {
            String simpleName = getSimpleAnnotationName(annotation);
            if (isValidationAnnotation(simpleName)) {
                validationAnnotations.add(annotation);
            } else if (isDependencyAnnotation(simpleName)) {
                dependencyAnnotations.add(annotation);
            } else {
                metadataAnnotations.add(annotation);
            }
        }
        
        details.put("validation_annotations", validationAnnotations);
        details.put("dependency_annotations", dependencyAnnotations);
        details.put("metadata_annotations", metadataAnnotations);
        
        return details;
    }

    private boolean isValidationAnnotation(String annotation) {
        String[] validationAnnotations = {"NotNull", "Nullable", "Min", "Max", "Size", "Pattern", "Email", "Valid"};
        return Arrays.asList(validationAnnotations).contains(annotation);
    }

    private boolean isDependencyAnnotation(String annotation) {
        String[] dependencyAnnotations = {"Autowired", "Inject", "Resource", "Value"};
        return Arrays.asList(dependencyAnnotations).contains(annotation);
    }

    /**
     * 🎯 GENERAR REPORTE DE CALIDAD COMPLETO
     */
    public Map<String, Object> generateQualityReport() {
        Map<String, Object> report = new LinkedHashMap<>();
        
        report.put("parameter_name", this.name);
        report.put("complete_declaration", getEnhancedDeclaration());
        report.put("parameter_index", this.parameterIndex);
        
        // 🎯 ANÁLISIS DE TIPO
        Map<String, Object> typeAnalysis = new HashMap<>();
        typeAnalysis.put("full_type", this.type);
        typeAnalysis.put("simple_type", getSimpleType());
        typeAnalysis.put("is_varargs", this.isVarArgs);
        typeAnalysis.put("is_final", this.isFinal);
        typeAnalysis.put("referenced_types", new ArrayList<>(this.referencedTypes));
        typeAnalysis.put("type_complexity", calculateTypeComplexity());
        report.put("type_analysis", typeAnalysis);
        
        // 🎯 ANÁLISIS DE ANOTACIONES
        report.put("annotation_analysis", getAnnotationDetails());
        
        // 🎯 METADATAS AVANZADAS
        Map<String, Object> advancedMetadata = new HashMap<>();
        advancedMetadata.put("description", this.description);
        advancedMetadata.put("default_value", this.defaultValue);
        advancedMetadata.put("is_nullable", this.isNullable);
        advancedMetadata.put("is_not_null", this.isNotNull);
        advancedMetadata.put("validation_rules", this.validationRules);
        advancedMetadata.put("parameter_role", this.parameterRole);
        advancedMetadata.put("has_description", hasDescription());
        advancedMetadata.put("has_default_value", this.defaultValue != null);
        advancedMetadata.put("has_validation", this.validationRules != null);
        report.put("advanced_metadata", advancedMetadata);
        
        // 🎯 EVALUACIÓN DE CALIDAD
        Map<String, Object> qualityAssessment = new HashMap<>();
        qualityAssessment.put("quality_rating", calculateQualityRating());
        qualityAssessment.put("documentation_score", calculateDocumentationScore());
        qualityAssessment.put("validation_score", calculateValidationScore());
        qualityAssessment.put("naming_score", calculateNamingScore());
        qualityAssessment.put("recommendations", generateParameterRecommendations());
        report.put("quality_assessment", qualityAssessment);
        
        return report;
    }

    private String calculateQualityRating() {
        if (hasErrors()) return "ERROR";
        
        int score = 100;
        
        // 🎯 PENALIZACIONES
        if (!hasDescription()) score -= 20;
        if (this.type.length() > 50) score -= 15;
        if (this.annotations.size() > 5) score -= 10;
        if (this.name.length() > 20) score -= 5;
        if (this.isVarArgs && this.parameterIndex > 0) score -= 10; // Varargs debe ser último
        
        // 🎯 BONIFICACIONES
        if (this.description != null && this.description.length() > 10) score += 10;
        if (this.isNotNull) score += 5;
        if (this.validationRules != null) score += 10;
        
        if (score >= 90) return "EXCELLENT";
        if (score >= 75) return "GOOD";
        if (score >= 60) return "FAIR";
        return "POOR";
    }

    private int calculateDocumentationScore() {
        int score = 0;
        
        if (hasDescription()) score += 50;
        if (this.description != null && this.description.length() > 20) score += 30;
        if (this.defaultValue != null) score += 10;
        if (this.validationRules != null) score += 10;
        
        return Math.min(100, score);
    }

    private int calculateValidationScore() {
        int score = 0;
        
        if (this.isNotNull) score += 40;
        if (this.validationRules != null) score += 30;
        
        // 🎯 CONTAR ANOTACIONES DE VALIDACIÓN
        long validationAnnotations = this.annotations.keySet().stream()
            .filter(ann -> isValidationAnnotation(getSimpleAnnotationName(ann)))
            .count();
        
        score += (int) (validationAnnotations * 10);
        
        return Math.min(100, score);
    }

    private int calculateNamingScore() {
        int score = 100;
        
        // 🎯 VERIFICAR CONVENCIONES DE NOMBRADO
        if (this.name == null || this.name.trim().isEmpty()) {
            return 0;
        }
        
        if (!Character.isLowerCase(this.name.charAt(0))) {
            score -= 30; // Debe empezar con minúscula
        }
        
        if (this.name.length() < 2) {
            score -= 20; // Nombre muy corto
        }
        
        if (this.name.length() > 25) {
            score -= 15; // Nombre muy largo
        }
        
        if (this.name.contains("_")) {
            score -= 10; // Preferible camelCase sobre snake_case
        }
        
        // 🎯 VERIFICAR NOMBRES POCO DESCRIPTIVOS
        String[] badNames = {"var", "obj", "temp", "data", "value", "arg", "param"};
        if (Arrays.asList(badNames).contains(this.name.toLowerCase())) {
            score -= 25;
        }
        
        return Math.max(0, score);
    }

    private int calculateTypeComplexity() {
        int complexity = 1;
        
        if (this.type.contains("<")) complexity += 2;
        if (this.type.contains("[]")) complexity += 1;
        if (this.isVarArgs) complexity += 1;
        if (this.type.length() > 30) complexity += 1;
        
        return complexity;
    }

    private List<String> generateParameterRecommendations() {
        List<String> recommendations = new ArrayList<>();
        
        if (!hasDescription()) {
            recommendations.add("Agregar descripción al parámetro");
        }
        
        if (this.name.length() < 2) {
            recommendations.add("Usar un nombre más descriptivo para el parámetro");
        }
        
        if (this.type.length() > 50) {
            recommendations.add("Considerar simplificar el tipo del parámetro");
        }
        
        if (this.isVarArgs && this.parameterIndex > 0) {
            recommendations.add("Los parámetros varargs deben ser los últimos en la lista");
        }
        
        if (this.annotations.size() > 5) {
            recommendations.add("Demasiadas anotaciones - considerar simplificar");
        }
        
        if (this.isNullable && this.isNotNull) {
            recommendations.add("Conflicto: parámetro marcado como nullable y not null");
        }
        
        if (Arrays.asList("var", "obj", "temp", "data").contains(this.name.toLowerCase())) {
            recommendations.add("Usar un nombre más significativo en lugar de '" + this.name + "'");
        }
        
        return recommendations;
    }

    // 🎯 MÉTODOS DE ACCESO ADICIONALES
    public Map<String, Map<String, String>> getAnnotationParameters() {
        return Collections.unmodifiableMap(annotationParameters);
    }

    public Set<String> getReferencedTypes() {
        return Collections.unmodifiableSet(referencedTypes);
    }

    public Map<String, String> getAnnotationParametersFor(String annotationName) {
        return annotationParameters.getOrDefault(annotationName, Collections.emptyMap());
    }

    public boolean hasValidation() {
        return validationRules != null || 
               annotations.keySet().stream()
                   .anyMatch(ann -> isValidationAnnotation(getSimpleAnnotationName(ann)));
    }

    public boolean isWellDocumented() {
        return hasDescription() && calculateDocumentationScore() >= 70;
    }

    public boolean isWellNamed() {
        return calculateNamingScore() >= 80;
    }
}