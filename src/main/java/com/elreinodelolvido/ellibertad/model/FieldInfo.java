package com.elreinodelolvido.ellibertad.model;

import java.util.*;

/**
 * 🏴‍☠️ FieldInfo - Representa un campo/atributo de una clase Java
 */
public class FieldInfo {
    private String name;
    private String type;
    private String visibility; // PUBLIC, PRIVATE, PROTECTED, PACKAGE
    private boolean isStatic;
    private boolean isFinal;
    private boolean isTransient;
    private boolean isVolatile;
    private String initialValue;
    private Map<String, String> annotations;
    private List<String> modifiers;

    // 🏗️ Constructores
    public FieldInfo() {
        this.annotations = new HashMap<>();
        this.modifiers = new ArrayList<>();
    }

    public FieldInfo(String name, String type) {
        this();
        this.name = name;
        this.type = type;
        this.visibility = "PRIVATE";
    }

    // 🎯 Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }

    public boolean isStatic() { return isStatic; }
    public void setStatic(boolean isStatic) { this.isStatic = isStatic; }

    public boolean isFinal() { return isFinal; }
    public void setFinal(boolean isFinal) { this.isFinal = isFinal; }

    public boolean isTransient() { return isTransient; }
    public void setTransient(boolean isTransient) { this.isTransient = isTransient; }

    public boolean isVolatile() { return isVolatile; }
    public void setVolatile(boolean isVolatile) { this.isVolatile = isVolatile; }

    public String getInitialValue() { return initialValue; }
    public void setInitialValue(String initialValue) { this.initialValue = initialValue; }

    public Map<String, String> getAnnotations() { return Collections.unmodifiableMap(annotations); }
    public void setAnnotations(Map<String, String> annotations) { this.annotations = new HashMap<>(annotations); }

    public List<String> getModifiers() { return Collections.unmodifiableList(modifiers); }
    public void setModifiers(List<String> modifiers) { this.modifiers = new ArrayList<>(modifiers); }

    // 🚀 Métodos de utilidad
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
        if (isTransient) sb.append("transient ");
        if (isVolatile) sb.append("volatile ");
        
        // Tipo y nombre
        sb.append(type).append(" ").append(name);
        
        // Valor inicial
        if (initialValue != null) {
            sb.append(" = ").append(initialValue);
        }
        
        return sb.toString();
    }

    public boolean isConstant() {
        return isStatic && isFinal && type != null && 
               (type.equals("String") || type.matches("^(int|long|double|float|boolean|char)$"));
    }

    @Override
    public String toString() {
        String emoji = isConstant() ? "🔒" : "📌";
        return String.format("%s %s %s %s", emoji, visibility.toLowerCase(), type, name);
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
     * 🎯 VERIFICAR SI EL CAMPO TIENE ERRORES
     */
    public boolean hasErrors() {
        // Verificar errores básicos de estructura
        if (name == null || name.trim().isEmpty()) {
            return true;
        }
        
        if (type == null || type.trim().isEmpty()) {
            return true;
        }
        
        // Verificar visibilidad inválida
        if (visibility == null || !Arrays.asList("PUBLIC", "PRIVATE", "PROTECTED", "PACKAGE").contains(visibility)) {
            return true;
        }
        
        // Verificar conflictos de modificadores
        if (isFinal && isVolatile) {
            return true; // final y volatile son mutuamente excluyentes
        }
        
        if (isTransient && isVolatile) {
            return true; // transient y volatile son mutuamente excluyentes
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
     * 🎯 OBTENER EL NOMBRE SIMPLE DE UN TIPO (sin package)
     */
    private String getSimpleName(String fullTypeName) {
        if (fullTypeName == null) {
            return "";
        }
        
        int lastDot = fullTypeName.lastIndexOf('.');
        return lastDot >= 0 ? fullTypeName.substring(lastDot + 1) : fullTypeName;
    }

    /**
     * 🎯 OBTENER UNA REPRESENTACIÓN COMPLETA DEL CAMPO
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
                sb.append("\n");
            }
        }
        
        // Modificadores de visibilidad
        sb.append(visibility.toLowerCase()).append(" ");
        
        // Otros modificadores
        if (isStatic) sb.append("static ");
        if (isFinal) sb.append("final ");
        if (isTransient) sb.append("transient ");
        if (isVolatile) sb.append("volatile ");
        
        // Tipo y nombre
        sb.append(type).append(" ").append(name);
        
        // Valor inicial
        if (initialValue != null && !initialValue.isEmpty()) {
            sb.append(" = ").append(initialValue);
        }
        
        return sb.toString();
    }

    /**
     * 🚀 MÉTODOS TURBO IMPLEMENTADOS - ANÁLISIS EXHAUSTIVO DE CAMPOS
     */

    // 🎯 VARIABLES DE INSTANCIA TURBO
    private String fieldComment;
    private String javadoc;
    private Map<String, Map<String, String>> annotationParameters = new HashMap<>();
    private Set<String> referencedTypes = new HashSet<>();
    private String fieldCategory; // CONSTANT, DEPENDENCY, STATE, CACHE, CONFIG
    private boolean isLazy;
    private boolean isAutowired;
    private boolean isInject;
    private String validationRules;
    private String defaultValue;
    private boolean isNullable;
    private boolean isNotNull;
    private int fieldOrder;
    private String accessorType; // DIRECT, GETTER_SETTER, BUILDER

    // 🎯 COMENTARIOS Y DOCUMENTACIÓN AVANZADA
    public void setComment(String comment) {
        this.fieldComment = comment;
        
        // 🎯 ANÁLISIS AUTOMÁTICO DEL COMENTARIO
        if (comment != null) {
            extractCommentMetadata(comment);
        }
    }

    public String getComment() {
        return fieldComment;
    }

    public boolean hasComment() {
        return fieldComment != null && !fieldComment.trim().isEmpty();
    }

    public void setJavadoc(String javadocContent) {
        this.javadoc = javadocContent;
        
        // 🎯 PROCESAMIENTO AUTOMÁTICO DE JAVADOC
        if (javadocContent != null) {
            parseJavadocContent(javadocContent);
        }
    }

    public String getJavadoc() {
        return javadoc;
    }

    public boolean hasJavadoc() {
        return javadoc != null && !javadoc.trim().isEmpty();
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
        detectSpecialFieldAnnotation(name, parameters);
    }

    // 🎯 MÉTODOS DE ACCESO ADICIONALES TURBO
    public void setFieldCategory(String category) {
        this.fieldCategory = category;
        addAnnotation("FieldCategory", "\"" + category + "\"");
    }

    public String getFieldCategory() {
        return fieldCategory;
    }

    public void setLazy(boolean isLazy) {
        this.isLazy = isLazy;
        if (isLazy) {
            addAnnotation("Lazy", "true");
        }
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setAutowired(boolean isAutowired) {
        this.isAutowired = isAutowired;
        if (isAutowired) {
            addAnnotation("Autowired", "");
            this.fieldCategory = "DEPENDENCY";
        }
    }

    public boolean isAutowired() {
        return isAutowired;
    }

    public void setInject(boolean isInject) {
        this.isInject = isInject;
        if (isInject) {
            addAnnotation("Inject", "");
            this.fieldCategory = "DEPENDENCY";
        }
    }

    public boolean isInject() {
        return isInject;
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
            this.isNotNull = false;
        }
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNotNull(boolean isNotNull) {
        this.isNotNull = isNotNull;
        if (isNotNull) {
            addAnnotation("NotNull", "");
            this.isNullable = false;
        }
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public void setFieldOrder(int order) {
        this.fieldOrder = order;
    }

    public int getFieldOrder() {
        return fieldOrder;
    }

    public void setAccessorType(String accessorType) {
        this.accessorType = accessorType;
    }

    public String getAccessorType() {
        return accessorType;
    }

    // 🎯 MÉTODOS PRIVADOS DE ANÁLISIS TURBO

    /**
     * 🎯 EXTRAER METADATOS DEL COMENTARIO
     */
    private void extractCommentMetadata(String comment) {
        if (comment == null) return;
        
        String lowerComment = comment.toLowerCase();
        
        // 🎯 DETECTAR PATRONES EN COMENTARIOS
        if (lowerComment.contains("lazy") || lowerComment.contains("perezos")) {
            this.isLazy = true;
        }
        
        if (lowerComment.contains("required") || lowerComment.contains("requerid")) {
            this.isNotNull = true;
        }
        
        if (lowerComment.contains("optional") || lowerComment.contains("opcional")) {
            this.isNullable = true;
        }
        
        if (lowerComment.contains("default") || lowerComment.contains("por defecto")) {
            extractDefaultValueFromComment(comment);
        }
        
        if (lowerComment.contains("validation") || lowerComment.contains("validación")) {
            extractValidationFromComment(comment);
        }
        
        // 🎯 DETECTAR CATEGORÍA DEL CAMPO
        detectFieldCategoryFromComment(comment);
    }

    private void extractDefaultValueFromComment(String comment) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
            "(?i)default\\s*(?:value)?\\s*[:=]?\\s*([^.,;]+)"
        );
        java.util.regex.Matcher matcher = pattern.matcher(comment);
        if (matcher.find()) {
            this.defaultValue = matcher.group(1).trim();
        }
    }

    private void extractValidationFromComment(String comment) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
            "(?i)(?:must|debe)\\s+be\\s+([^.,;]+)"
        );
        java.util.regex.Matcher matcher = pattern.matcher(comment);
        if (matcher.find()) {
            this.validationRules = matcher.group(1).trim();
        }
    }

    private void detectFieldCategoryFromComment(String comment) {
        String lowerComment = comment.toLowerCase();
        
        if (lowerComment.contains("constant") || lowerComment.contains("constante")) {
            this.fieldCategory = "CONSTANT";
        } else if (lowerComment.contains("dependency") || lowerComment.contains("dependencia")) {
            this.fieldCategory = "DEPENDENCY";
        } else if (lowerComment.contains("state") || lowerComment.contains("estado")) {
            this.fieldCategory = "STATE";
        } else if (lowerComment.contains("cache")) {
            this.fieldCategory = "CACHE";
        } else if (lowerComment.contains("config") || lowerComment.contains("configuración")) {
            this.fieldCategory = "CONFIG";
        }
    }

    /**
     * 🎯 PROCESAR CONTENIDO JAVADOC
     */
    private void parseJavadocContent(String javadoc) {
        if (javadoc == null) return;
        
        String[] lines = javadoc.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            
            // 🎯 PROCESAR TAGS JAVADOC ESPECÍFICOS
            if (trimmed.startsWith("@")) {
                String[] parts = trimmed.split("\\s+", 2);
                String tagName = parts[0];
                String tagContent = parts.length > 1 ? parts[1] : "";
                
                processJavadocTag(tagName, tagContent);
            }
        }
    }

    private void processJavadocTag(String tag, String content) {
        switch (tag.toLowerCase()) {
            case "@value":
                if (this.defaultValue == null) {
                    this.defaultValue = content.trim();
                }
                break;
                
            case "@since":
                addAnnotation("Since", "\"" + content.trim() + "\"");
                break;
                
            case "@deprecated":
                addAnnotation("Deprecated", "");
                break;
                
            case "@see":
                addAnnotation("SeeReference", "\"" + content.trim() + "\"");
                break;
        }
    }

    /**
     * 🎯 DETECTAR ANOTACIONES ESPECIALES DE CAMPOS
     */
    private void detectSpecialFieldAnnotation(String annotationName, Map<String, String> parameters) {
        if (annotationName == null) return;
        
        String simpleName = getSimpleAnnotationName(annotationName);
        
        switch (simpleName) {
            case "Autowired":
            case "Inject":
            case "Resource":
                this.isAutowired = true;
                this.fieldCategory = "DEPENDENCY";
                break;
                
            case "Value":
                this.fieldCategory = "CONFIG";
                if (parameters != null && parameters.containsKey("value")) {
                    this.defaultValue = parameters.get("value").replace("${", "").replace("}", "");
                }
                break;
                
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
                
            case "Lazy":
                this.isLazy = true;
                break;
                
            case "Transient":
                this.isTransient = true;
                break;
                
            case "Volatile":
                this.isVolatile = true;
                break;
                
            case "Id":
            case "GeneratedValue":
                this.fieldCategory = "ENTITY_ID";
                break;
                
            case "Column":
                this.fieldCategory = "ENTITY_FIELD";
                break;
                
            case "OneToMany":
            case "ManyToOne":
            case "OneToOne":
            case "ManyToMany":
                this.fieldCategory = "ENTITY_RELATIONSHIP";
                break;
        }
    }

    /**
     * 🎯 ANÁLISIS DE TIPOS AVANZADO
     */
    public void analyzeTypeDependencies() {
        if (type == null) return;
        
        // 🎯 IDENTIFICAR TIPOS REFERENCIADOS
        identifyReferencedTypes(type);
        
        // 🎯 CLASIFICACIÓN AUTOMÁTICA DEL CAMPO
        autoClassifyField();
        
        // 🎯 DETECTAR TIPO DE ACCESO
        detectAccessorType();
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
        if (mainType.endsWith("[]")) {
            mainType = mainType.substring(0, mainType.length() - 2);
        }
        
        referencedTypes.add(getSimpleName(mainType));
    }

    private void autoClassifyField() {
        if (this.fieldCategory != null) return; // Ya clasificado
        
        // 🎯 CLASIFICACIÓN BASADA EN CARACTERÍSTICAS
        if (isConstant()) {
            this.fieldCategory = "CONSTANT";
        } else if (isAutowired || isInject) {
            this.fieldCategory = "DEPENDENCY";
        } else if (isStatic) {
            this.fieldCategory = "STATIC_UTILITY";
        } else if (isFinal) {
            this.fieldCategory = "IMMUTABLE_STATE";
        } else if (isTransient) {
            this.fieldCategory = "TRANSIENT_STATE";
        } else if (isVolatile) {
            this.fieldCategory = "CONCURRENT_STATE";
        } else if (type != null && type.toLowerCase().contains("cache")) {
            this.fieldCategory = "CACHE";
        } else if (type != null && (type.toLowerCase().contains("config") || type.toLowerCase().contains("properties"))) {
            this.fieldCategory = "CONFIG";
        } else {
            this.fieldCategory = "STATE";
        }
    }

    private void detectAccessorType() {
        if (isPublic()) {
            this.accessorType = "DIRECT";
        } else if (isPrivate() || isProtected()) {
            this.accessorType = "GETTER_SETTER";
        } else {
            this.accessorType = "PACKAGE_DIRECT";
        }
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
                sb.append(annotation.getValue()).append("\n");
            }
        }
        
        // 🎯 MODIFICADORES
        sb.append(visibility.toLowerCase()).append(" ");
        if (isStatic) sb.append("static ");
        if (isFinal) sb.append("final ");
        if (isTransient) sb.append("transient ");
        if (isVolatile) sb.append("volatile ");
        
        // 🎯 TIPO Y NOMBRE
        sb.append(type).append(" ").append(name);
        
        // 🎯 VALOR INICIAL
        if (initialValue != null) {
            sb.append(" = ").append(initialValue);
        } else if (defaultValue != null) {
            sb.append(" // default: ").append(defaultValue);
        }
        
        // 🎯 METADATOS ADICIONALES
        if (fieldCategory != null) {
            sb.append(" // ").append(fieldCategory);
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
        
        // 🎯 ANÁLISIS DE ANOTACIONES POR CATEGORÍA
        List<String> validationAnnotations = new ArrayList<>();
        List<String> dependencyAnnotations = new ArrayList<>();
        List<String> persistenceAnnotations = new ArrayList<>();
        List<String> metadataAnnotations = new ArrayList<>();
        
        for (String annotation : annotations.keySet()) {
            String simpleName = getSimpleAnnotationName(annotation);
            if (isValidationAnnotation(simpleName)) {
                validationAnnotations.add(annotation);
            } else if (isDependencyAnnotation(simpleName)) {
                dependencyAnnotations.add(annotation);
            } else if (isPersistenceAnnotation(simpleName)) {
                persistenceAnnotations.add(annotation);
            } else {
                metadataAnnotations.add(annotation);
            }
        }
        
        details.put("validation_annotations", validationAnnotations);
        details.put("dependency_annotations", dependencyAnnotations);
        details.put("persistence_annotations", persistenceAnnotations);
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

    private boolean isPersistenceAnnotation(String annotation) {
        String[] persistenceAnnotations = {"Id", "GeneratedValue", "Column", "OneToMany", "ManyToOne", "OneToOne", "ManyToMany"};
        return Arrays.asList(persistenceAnnotations).contains(annotation);
    }

    /**
     * 🎯 GENERAR REPORTE DE CALIDAD COMPLETO
     */
    public Map<String, Object> generateQualityReport() {
        Map<String, Object> report = new LinkedHashMap<>();
        
        report.put("field_name", this.name);
        report.put("complete_declaration", getEnhancedDeclaration());
        report.put("field_order", this.fieldOrder);
        
        // 🎯 ANÁLISIS DE TIPO
        Map<String, Object> typeAnalysis = new HashMap<>();
        typeAnalysis.put("full_type", this.type);
        typeAnalysis.put("simple_type", getSimpleType());
        typeAnalysis.put("referenced_types", new ArrayList<>(this.referencedTypes));
        typeAnalysis.put("type_complexity", calculateTypeComplexity());
        report.put("type_analysis", typeAnalysis);
        
        // 🎯 ANÁLISIS DE MODIFICADORES
        Map<String, Object> modifierAnalysis = new HashMap<>();
        modifierAnalysis.put("visibility", this.visibility);
        modifierAnalysis.put("is_static", this.isStatic);
        modifierAnalysis.put("is_final", this.isFinal);
        modifierAnalysis.put("is_transient", this.isTransient);
        modifierAnalysis.put("is_volatile", this.isVolatile);
        modifierAnalysis.put("is_constant", isConstant());
        modifierAnalysis.put("accessor_type", this.accessorType);
        report.put("modifier_analysis", modifierAnalysis);
        
        // 🎯 ANÁLISIS DE ANOTACIONES
        report.put("annotation_analysis", getAnnotationDetails());
        
        // 🎯 METADATAS AVANZADAS
        Map<String, Object> advancedMetadata = new HashMap<>();
        advancedMetadata.put("field_category", this.fieldCategory);
        advancedMetadata.put("initial_value", this.initialValue);
        advancedMetadata.put("default_value", this.defaultValue);
        advancedMetadata.put("is_nullable", this.isNullable);
        advancedMetadata.put("is_not_null", this.isNotNull);
        advancedMetadata.put("validation_rules", this.validationRules);
        advancedMetadata.put("is_lazy", this.isLazy);
        advancedMetadata.put("is_autowired", this.isAutowired);
        advancedMetadata.put("is_inject", this.isInject);
        advancedMetadata.put("has_comment", hasComment());
        advancedMetadata.put("has_javadoc", hasJavadoc());
        report.put("advanced_metadata", advancedMetadata);
        
        // 🎯 EVALUACIÓN DE CALIDAD
        Map<String, Object> qualityAssessment = new HashMap<>();
        qualityAssessment.put("quality_rating", calculateQualityRating());
        qualityAssessment.put("encapsulation_score", calculateEncapsulationScore());
        qualityAssessment.put("documentation_score", calculateDocumentationScore());
        qualityAssessment.put("naming_score", calculateNamingScore());
        qualityAssessment.put("recommendations", generateFieldRecommendations());
        report.put("quality_assessment", qualityAssessment);
        
        return report;
    }

    private String calculateQualityRating() {
        if (hasErrors()) return "ERROR";
        
        int score = 100;
        
        // 🎯 PENALIZACIONES
        if (isPublic() && !isFinal() && !isConstant()) score -= 30; // Campo público mutable
        if (!hasComment() && !isConstant()) score -= 20;
        if (this.type.length() > 50) score -= 15;
        if (this.annotations.size() > 8) score -= 10;
        if (this.name.length() > 25) score -= 5;
        
        // 🎯 BONIFICACIONES
        if (isPrivate()) score += 15;
        if (isFinal()) score += 10;
        if (hasComment() && this.fieldComment.length() > 10) score += 10;
        if (this.validationRules != null) score += 10;
        if (isConstant()) score += 20;
        
        if (score >= 90) return "EXCELLENT";
        if (score >= 75) return "GOOD";
        if (score >= 60) return "FAIR";
        return "POOR";
    }

    private int calculateEncapsulationScore() {
        int score = 100;
        
        if (isPublic()) score -= 40;
        if (isProtected()) score -= 20;
        if (!isPrivate() && !isFinal()) score -= 30;
        if (isPublic() && !isFinal()) score -= 50;
        
        return Math.max(0, score);
    }

    private int calculateDocumentationScore() {
        int score = 0;
        
        if (hasComment()) score += 40;
        if (hasJavadoc()) score += 30;
        if (this.fieldComment != null && this.fieldComment.length() > 20) score += 20;
        if (this.defaultValue != null) score += 10;
        
        return Math.min(100, score);
    }

    private int calculateNamingScore() {
        int score = 100;
        
        if (this.name == null || this.name.trim().isEmpty()) {
            return 0;
        }
        
        // 🎯 VERIFICAR CONVENCIONES DE NOMBRADO
        if (isConstant()) {
            // CONSTANTES deben ser UPPER_CASE
            if (!this.name.equals(this.name.toUpperCase()) || !this.name.contains("_")) {
                score -= 40;
            }
        } else {
            // Campos normales deben ser camelCase
            if (!Character.isLowerCase(this.name.charAt(0))) {
                score -= 30;
            }
        }
        
        if (this.name.length() < 2) {
            score -= 20;
        }
        
        if (this.name.length() > 25) {
            score -= 15;
        }
        
        if (this.name.contains("_") && !isConstant()) {
            score -= 10; // Preferible camelCase sobre snake_case para campos no constantes
        }
        
        // 🎯 VERIFICAR NOMBRES POCO DESCRIPTIVOS
        String[] badNames = {"var", "obj", "temp", "data", "value", "field"};
        if (Arrays.asList(badNames).contains(this.name.toLowerCase())) {
            score -= 25;
        }
        
        return Math.max(0, score);
    }

    private int calculateTypeComplexity() {
        int complexity = 1;
        
        if (this.type.contains("<")) complexity += 2;
        if (this.type.contains("[]")) complexity += 1;
        if (this.type.length() > 30) complexity += 1;
        if (this.referencedTypes.size() > 3) complexity += 1;
        
        return complexity;
    }

    private List<String> generateFieldRecommendations() {
        List<String> recommendations = new ArrayList<>();
        
        if (isPublic() && !isFinal()) {
            recommendations.add("Hacer el campo privado y proporcionar acceso a través de getters/setters");
        }
        
        if (!hasComment() && !isConstant()) {
            recommendations.add("Agregar documentación al campo");
        }
        
        if (this.name.length() < 2) {
            recommendations.add("Usar un nombre más descriptivo para el campo");
        }
        
        if (isConstant() && (!this.name.equals(this.name.toUpperCase()) || !this.name.contains("_"))) {
            recommendations.add("Usar convención UPPER_CASE_WITH_UNDERSCORES para constantes");
        }
        
        if (!isConstant() && Character.isUpperCase(this.name.charAt(0))) {
            recommendations.add("Usar convención camelCase para campos no constantes");
        }
        
        if (this.type.length() > 50) {
            recommendations.add("Considerar simplificar el tipo del campo");
        }
        
        if (this.annotations.size() > 8) {
            recommendations.add("Demasiadas anotaciones - considerar simplificar");
        }
        
        if (this.isNullable && this.isNotNull) {
            recommendations.add("Conflicto: campo marcado como nullable y not null");
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
        return hasComment() && calculateDocumentationScore() >= 70;
    }

    public boolean isWellNamed() {
        return calculateNamingScore() >= 80;
    }

    public boolean isWellEncapsulated() {
        return calculateEncapsulationScore() >= 80;
    }

    /**
     * 🎯 OBTENER NOMBRE SIMPLE DE ANOTACIÓN (sin package)
     */
    private String getSimpleAnnotationName(String fullAnnotationName) {
        if (fullAnnotationName == null) return "";
        int lastDot = fullAnnotationName.lastIndexOf('.');
        return lastDot >= 0 ? fullAnnotationName.substring(lastDot + 1) : fullAnnotationName;
    }
}
