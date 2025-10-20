package com.elreinodelolvido.ellibertad.model;

import java.util.*;

/**
 * 🏴‍☠️ ClassInfo - Representa una clase Java con toda su información estructural
 * ¡Ahora con +200% de detalles piratas! ⚓
 */
public class ClassInfo {
    private String name;
    private String packageName;
    private String sourcePath;
    private String type; // CLASS, INTERFACE, ENUM, ANNOTATION, RECORD
    private List<MethodInfo> methods;
    private List<FieldInfo> fields;
    private List<String> imports;
    private List<String> interfaces;
    private String superClass;
    private List<ClassInfo> innerClasses;
    private Map<String, String> annotations;
    private String sourceCode;
    private int lineCount;
    private double complexity;
    private boolean isPublic;
    private boolean isAbstract;
    private boolean isFinal;
    private boolean isStatic;
    private boolean isInterface;
	private boolean isStrictfp;

    // 🏗️ Constructores
    public ClassInfo() {
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.imports = new ArrayList<>();
        this.interfaces = new ArrayList<>();
        this.innerClasses = new ArrayList<>();
        this.annotations = new HashMap<>();
    }

    public ClassInfo(String name, String packageName, String sourcePath) {
        this();
        this.name = name;
        this.packageName = packageName;
        this.sourcePath = sourcePath;
        this.type = "CLASS";
    }

    // 🎯 Getters y Setters mejorados
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getSourcePath() { return sourcePath; }
    public void setSourcePath(String sourcePath) { this.sourcePath = sourcePath; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<MethodInfo> getMethods() { return Collections.unmodifiableList(methods); }
    public void setMethods(List<MethodInfo> methods) { this.methods = new ArrayList<>(methods); }

    public List<FieldInfo> getFields() { return Collections.unmodifiableList(fields); }
    public void setFields(List<FieldInfo> fields) { this.fields = new ArrayList<>(fields); }

    public List<String> getImports() { return Collections.unmodifiableList(imports); }
    public void setImports(List<String> imports) { this.imports = new ArrayList<>(imports); }

    public List<String> getInterfaces() { return Collections.unmodifiableList(interfaces); }
    public void setInterfaces(List<String> interfaces) { this.interfaces = new ArrayList<>(interfaces); }

    public String getSuperClass() { return superClass; }
    public void setSuperClass(String superClass) { this.superClass = superClass; }

    public List<ClassInfo> getInnerClasses() { return Collections.unmodifiableList(innerClasses); }
    public void setInnerClasses(List<ClassInfo> innerClasses) { this.innerClasses = new ArrayList<>(innerClasses); }

    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }

    public int getLineCount() { return lineCount; }
    public void setLineCount(int lineCount) { this.lineCount = lineCount; }

    public double getComplexity() { return complexity; }
    public void setComplexity(double complexity) { this.complexity = complexity; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public boolean isAbstract() { return isAbstract; }
    public void setAbstract(boolean isAbstract) { this.isAbstract = isAbstract; }

    public boolean isFinal() { return isFinal; }
    public void setFinal(boolean isFinal) { this.isFinal = isFinal; }

    public boolean isStatic() { return isStatic; }
    public void setStatic(boolean isStatic) { this.isStatic = isStatic; }

    public boolean isInterface() { return isInterface; }
    public void setInterface(boolean isInterface) { this.isInterface = isInterface; }
    

    // 🚀 Métodos de utilidad
    public void addMethod(MethodInfo method) {
        if (this.methods == null) {
            this.methods = new ArrayList<>();
        }
        this.methods.add(method);
    }

    public void addField(FieldInfo field) {
        if (this.fields == null) {
            this.fields = new ArrayList<>();
        }
        this.fields.add(field);
    }

    public void addImport(String importStatement) {
        if (this.imports == null) {
            this.imports = new ArrayList<>();
        }
        this.imports.add(importStatement);
    }

    public void addInterface(String interfaceName) {
        if (this.interfaces == null) {
            this.interfaces = new ArrayList<>();
        }
        this.interfaces.add(interfaceName);
    }

    public void addInnerClass(ClassInfo innerClass) {
        if (this.innerClasses == null) {
            this.innerClasses = new ArrayList<>();
        }
        this.innerClasses.add(innerClass);
    }

    public String getSimpleName() {
        return name;
    }

    public boolean hasMethods() {
        return methods != null && !methods.isEmpty();
    }

    public boolean hasFields() {
        return fields != null && !fields.isEmpty();
    }

    public boolean hasInnerClasses() {
        return innerClasses != null && !innerClasses.isEmpty();
    }

    public boolean hasImports() {
        return imports != null && !imports.isEmpty();
    }

    public boolean hasInterfaces() {
        return interfaces != null && !interfaces.isEmpty();
    }

    public boolean hasAnnotations() {
        return annotations != null && !annotations.isEmpty();
    }

    public int getMethodCount() {
        return methods != null ? methods.size() : 0;
    }

    public int getFieldCount() {
        return fields != null ? fields.size() : 0;
    }

    public int getInnerClassCount() {
        return innerClasses != null ? innerClasses.size() : 0;
    }

    public Optional<MethodInfo> findMethodByName(String methodName) {
        if (methods == null) return Optional.empty();
        return methods.stream()
                .filter(m -> m != null && methodName.equals(m.getName()))
                .findFirst();
    }

    public List<MethodInfo> getPublicMethods() {
        if (methods == null) return Collections.emptyList();
        return methods.stream()
                .filter(m -> m != null && m.isPublic())
                .toList();
    }

    public List<MethodInfo> getPrivateMethods() {
        if (methods == null) return Collections.emptyList();
        return methods.stream()
                .filter(m -> m != null && m.isPrivate())
                .toList();
    }

    public List<FieldInfo> getPublicFields() {
        if (fields == null) return Collections.emptyList();
        return fields.stream()
                .filter(f -> f != null && f.isPublic())
                .toList();
    }

    public List<FieldInfo> getStaticFields() {
        if (fields == null) return Collections.emptyList();
        return fields.stream()
                .filter(f -> f != null && f.isStatic())
                .toList();
    }

    public Optional<FieldInfo> findFieldByName(String fieldName) {
        if (fields == null) return Optional.empty();
        return fields.stream()
                .filter(f -> f != null && fieldName.equals(f.getName()))
                .findFirst();
    }

    // 📊 Métricas de calidad
    public double calculateCohesion() {
        if (fields == null || methods == null || fields.isEmpty() || methods.isEmpty()) {
            return 1.0;
        }
        
        int sharedFields = 0;
        for (MethodInfo method : methods) {
            if (method != null) {
                sharedFields += method.getReferencedFields().size();
            }
        }
        
        return (double) sharedFields / (fields.size() * methods.size());
    }

    public boolean isDataClass() {
        if (methods == null) return false;
        return methods.stream()
                .filter(Objects::nonNull)
                .allMatch(m -> m.isGetter() || m.isSetter() || m.isConstructor());
    }

    public boolean isUtilityClass() {
        if (methods == null || fields == null) return false;
        return methods.stream()
                .filter(Objects::nonNull)
                .allMatch(MethodInfo::isStatic) && 
               fields.stream()
                .filter(Objects::nonNull)
                .allMatch(FieldInfo::isStatic) &&
               !isAbstract();
    }

    public boolean isEntityClass() {
        return hasAnnotations() && 
               annotations.keySet().stream()
                   .anyMatch(ann -> ann.contains("Entity") || ann.contains("Table"));
    }

    public boolean isServiceClass() {
        return hasAnnotations() && 
               annotations.keySet().stream()
                   .anyMatch(ann -> ann.contains("Service") || ann.contains("Component"));
    }

    public boolean isControllerClass() {
        return hasAnnotations() && 
               annotations.keySet().stream()
                   .anyMatch(ann -> ann.contains("Controller") || ann.contains("RestController"));
    }

    public String getArchitecturalRole() {
        if (isEntityClass()) return "ENTITY";
        if (isServiceClass()) return "SERVICE";
        if (isControllerClass()) return "CONTROLLER";
        if (isUtilityClass()) return "UTILITY";
        if (isDataClass()) return "DATA";
        if (isInterface()) return "CONTRACT";
        return "BUSINESS";
    }

    // 🔍 Métodos de análisis
    public List<String> findCodeSmells() {
        List<String> smells = new ArrayList<>();
        
        // Clase demasiado grande
        if (getMethodCount() > 20) {
            smells.add("Clase demasiado grande (" + getMethodCount() + " métodos)");
        }
        
        // Demasiados campos
        if (getFieldCount() > 15) {
            smells.add("Demasiados campos (" + getFieldCount() + " campos)");
        }
        
        // Baja cohesión
        if (calculateCohesion() < 0.3) {
            smells.add("Baja cohesión (" + String.format("%.2f", calculateCohesion()) + ")");
        }
        
        // Métodos largos (análisis básico)
        if (methods != null) {
            methods.stream()
                .filter(Objects::nonNull)
                .filter(m -> m.getLineCount() > 50)
                .forEach(m -> smells.add("Método demasiado largo: " + m.getName()));
        }
        
        return smells;
    }

    public Map<String, Object> getQualityMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("total_methods", getMethodCount());
        metrics.put("total_fields", getFieldCount());
        metrics.put("total_inner_classes", getInnerClassCount());
        metrics.put("cohesion", calculateCohesion());
        metrics.put("complexity", complexity);
        metrics.put("line_count", lineCount);
        metrics.put("is_data_class", isDataClass());
        metrics.put("is_utility_class", isUtilityClass());
        metrics.put("architectural_role", getArchitecturalRole());
        metrics.put("code_smells", findCodeSmells().size());
        
        return metrics;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        // Emoji según el tipo
        String emoji = switch(type.toUpperCase()) {
            case "INTERFACE" -> "📜";
            case "ENUM" -> "🎯";
            case "ANNOTATION" -> "🏷️";
            case "RECORD" -> "💾";
            default -> "📦";
        };
        
        // Icono de visibilidad
        String visIcon = isPublic ? "🌐" : "🔐";
        
        sb.append(emoji).append(visIcon).append(" ").append(type.toUpperCase()).append(": ")
          .append(packageName).append(".").append(name).append("\n");
        sb.append("📁 Path: ").append(sourcePath).append("\n");
        
        if (superClass != null && !superClass.equals("Object") && !superClass.isEmpty()) {
            sb.append("📐 Extiende: ").append(superClass).append("\n");
        }
        
        if (hasInterfaces()) {
            sb.append("🔌 Implementa: ").append(String.join(", ", interfaces)).append("\n");
        }
        
        if (hasAnnotations()) {
            sb.append("🏷️ Anotaciones: ").append(annotations.keySet()).append("\n");
        }
        
        sb.append("📊 Métricas: ").append(lineCount).append(" líneas, ")
          .append(String.format("%.2f", complexity)).append(" complejidad, ")
          .append(getMethodCount()).append(" métodos, ")
          .append(getFieldCount()).append(" campos, ")
          .append(getInnerClassCount()).append(" clases internas\n");

        // Rol arquitectónico
        sb.append("🎯 Rol: ").append(getArchitecturalRole()).append("\n");

        // Code smells si existen
        List<String> smells = findCodeSmells();
        if (!smells.isEmpty()) {
            sb.append("⚠️  Code smells: ").append(smells.size()).append("\n");
            for (String smell : smells) {
                sb.append("   • ").append(smell).append("\n");
            }
        }

        if (hasFields()) {
            sb.append("  • Campos (").append(fields.size()).append("):\n");
            for (FieldInfo f : fields) {
                if (f != null) {
                    sb.append("    - ").append(f).append("\n");
                }
            }
        }

        if (hasMethods()) {
            sb.append("  • Métodos (").append(methods.size()).append("):\n");
            for (MethodInfo m : methods) {
                if (m != null) {
                    sb.append("    - ").append(m).append("\n");
                }
            }
        }

        if (hasInnerClasses()) {
            sb.append("  • Clases Internas (").append(innerClasses.size()).append("):\n");
            for (ClassInfo inner : innerClasses) {
                if (inner != null) {
                    sb.append("    - ").append(inner.getSimpleName()).append("\n");
                }
            }
        }

        return sb.toString();
    }

    public String toShortString() {
        return String.format("%s %s.%s [%s métodos, %s campos]", 
            getType().toUpperCase(), 
            packageName, 
            name, 
            getMethodCount(), 
            getFieldCount());
    }

    public String toJson() {
        // Representación JSON básica para debugging
        return String.format(
            "{\"name\":\"%s\",\"package\":\"%s\",\"type\":\"%s\",\"methods\":%d,\"fields\":%d,\"role\":\"%s\"}",
            name, packageName, type, getMethodCount(), getFieldCount(), getArchitecturalRole()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassInfo classInfo = (ClassInfo) o;
        return Objects.equals(getFullName(), classInfo.getFullName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFullName());
    }

    // 🏭 Métodos factory
    public static ClassInfo createClass(String name, String packageName, String sourcePath) {
        return new ClassInfo(name, packageName, sourcePath);
    }

    public static ClassInfo createInterface(String name, String packageName, String sourcePath) {
        ClassInfo info = new ClassInfo(name, packageName, sourcePath);
        info.setType("INTERFACE");
        info.setInterface(true);
        return info;
    }

    public static ClassInfo createEnum(String name, String packageName, String sourcePath) {
        ClassInfo info = new ClassInfo(name, packageName, sourcePath);
        info.setType("ENUM");
        info.setFinal(true);
        return info;
    }
        
        // ... otros campos y métodos existentes ...

        /**
         * 🎯 Verifica si la clase tiene una anotación específica
         */
        public boolean hasAnnotation(String annotationName) {
            if (annotationName == null || annotationName.trim().isEmpty() || this.annotations == null) {
                return false;
            }
            
            // Buscar por nombre exacto o nombre simple
            return this.annotations.keySet().stream()
                    .anyMatch(key -> 
                        key.equals(annotationName) || 
                        key.endsWith("." + annotationName) ||
                        getSimpleAnnotationName(key).equals(annotationName)
                    );
        }
        
        /**
         * 🎯 Obtener el nombre simple de una anotación (sin package)
         */
        private String getSimpleAnnotationName(String fullAnnotationName) {
            if (fullAnnotationName == null) return "";
            int lastDot = fullAnnotationName.lastIndexOf('.');
            return lastDot >= 0 ? fullAnnotationName.substring(lastDot + 1) : fullAnnotationName;
        }
        
        /**
         * 🎯 Agregar una anotación (si no existe este método)
         */
        public void addAnnotation(String name, String value) {
            if (this.annotations == null) {
                this.annotations = new HashMap<>();
            }
            this.annotations.put(name, value);
        }
        
        /**
         * 🎯 Obtener todas las anotaciones (si no existe este método)
         */
        public Map<String, String> getAnnotations() {
            if (this.annotations == null) {
                this.annotations = new HashMap<>();
            }
            return new HashMap<>(this.annotations);
        }

        /**
         * 🎯 OBTENER MODIFICADORES DE LA CLASE
         */
        public String getModifiers() {
            List<String> modifiers = new ArrayList<>();
            
            if (isPublic) modifiers.add("public");
            if (isAbstract) modifiers.add("abstract");
            if (isFinal) modifiers.add("final");
            if (isStatic) modifiers.add("static");
            if (isInterface) modifiers.add("interface");
            
            return String.join(" ", modifiers);
        }

        /**
         * 🎯 VERIFICAR SI LA CLASE TIENE ERRORES
         */
        public boolean hasErrors() {
            // Verificar errores básicos de estructura
            if (name == null || name.trim().isEmpty()) {
                return true;
            }
            
            if (packageName == null || packageName.trim().isEmpty()) {
                return true;
            }
            
            if (sourcePath == null || sourcePath.trim().isEmpty()) {
                return true;
            }
            
            // Verificar métodos con problemas
            if (methods != null) {
                for (MethodInfo method : methods) {
                    if (method != null && method.hasErrors()) {
                        return true;
                    }
                }
            }
            
            // Verificar campos con problemas
            if (fields != null) {
                for (FieldInfo field : fields) {
                    if (field != null && field.hasErrors()) {
                        return true;
                    }
                }
            }
            
            // Verificar complejidad excesiva
            if (complexity > 50.0) {
                return true;
            }
            
            // Verificar si es una clase demasiado grande
            if (lineCount > 1000 || getMethodCount() > 50 || getFieldCount() > 30) {
                return true;
            }
            
            return false;
        }

        /**
         * 🚀 MÉTODOS TURBO IMPLEMENTADOS - VERSIÓN BESTIAL
         */

        // 🎯 STRICTFP - PARA CÁLCULOS DE PUNTO FLOTANTE DE ALTA PRECISIÓN
        public void setStrictfp(boolean strictfpEnabled) {
            // 🆕 Marcar la clase como strictfp - cálculos consistentes en todas las plataformas
            if (this.annotations == null) {
                this.annotations = new HashMap<>();
            }
            if (strictfpEnabled) {
                this.annotations.put("Strictfp", "true");
                // 🎯 También almacenar como propiedad específica
                this.isStrictfp = strictfpEnabled; // Necesitarías agregar este campo
            } else {
                this.annotations.remove("Strictfp");
            }
        }

        // 🎯 TYPE PARAMETERS - GENÉRICOS CON ANÁLISIS COMPLETO
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

        public void addTypeParameterWithBounds(String typeParam, List<String> bounds) {
            addTypeParameter(typeParam);
            if (this.typeParameterBounds == null) {
                this.typeParameterBounds = new HashMap<>();
            }
            if (bounds != null && !bounds.isEmpty()) {
                this.typeParameterBounds.put(typeParam, new ArrayList<>(bounds));
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

        public String getTypeParametersAsString() {
            if (!hasTypeParameters()) return "";
            return "<" + String.join(", ", typeParameters) + ">";
        }

        // 🎯 CLASS COMMENT - COMENTARIOS CON ANÁLISIS SEMÁNTICO
        private String classComment;
        private String javadoc;
        private List<String> javadocTags = new ArrayList<>();
        private Map<String, String> javadocParams = new HashMap<>();
        private String author;
        private String version;
        private String since;
        private List<String> seeReferences = new ArrayList<>();
		private String fullName;

        public void setClassComment(String comment) {
            this.classComment = comment != null ? comment.trim() : null;
            // 🎯 EXTRACCIÓN AUTOMÁTICA DE METADATOS DEL COMENTARIO
            if (comment != null) {
                extractMetadataFromComment(comment);
            }
        }

        public String getClassComment() {
            return classComment;
        }

        public boolean hasClassComment() {
            return classComment != null && !classComment.isEmpty();
        }

        // 🎯 JAVADOC - PROCESAMIENTO AVANZADO
        public void setJavadoc(String javadocContent) {
            this.javadoc = javadocContent != null ? javadocContent.trim() : null;
            if (javadocContent != null) {
                parseJavadoc(javadocContent);
            }
        }

        public String getJavadoc() {
            return javadoc;
        }

        public boolean hasJavadoc() {
            return javadoc != null && !javadoc.isEmpty();
        }

        public List<String> getJavadocTags() {
            return javadocTags != null ? 
                Collections.unmodifiableList(javadocTags) : 
                Collections.emptyList();
        }

        public Map<String, String> getJavadocParams() {
            return javadocParams != null ? 
                new HashMap<>(javadocParams) : 
                Collections.emptyMap();
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

        public List<String> getSeeReferences() {
            return seeReferences != null ? 
                Collections.unmodifiableList(seeReferences) : 
                Collections.emptyList();
        }

        // 🎯 MÉTODOS PRIVADOS DE PROCESAMIENTO DE COMENTARIOS
        private void extractMetadataFromComment(String comment) {
            if (comment == null) return;
            
            // 🎯 DETECCIÓN DE PATRONES EN COMENTARIOS
            String[] lines = comment.split("\n");
            for (String line : lines) {
                String trimmed = line.trim();
                
                // Detectar autor
                if (trimmed.toLowerCase().contains("author:") || trimmed.toLowerCase().contains("@author")) {
                    this.author = extractValue(trimmed, "author");
                }
                
                // Detectar versión
                if (trimmed.toLowerCase().contains("version:") || trimmed.toLowerCase().contains("@version")) {
                    this.version = extractValue(trimmed, "version");
                }
                
                // Detectar since
                if (trimmed.toLowerCase().contains("since:") || trimmed.toLowerCase().contains("@since")) {
                    this.since = extractValue(trimmed, "since");
                }
                
                // Detectar referencias see
                if (trimmed.toLowerCase().contains("see:") || trimmed.toLowerCase().contains("@see")) {
                    String seeRef = extractValue(trimmed, "see");
                    if (seeRef != null && !seeRef.isEmpty()) {
                        if (this.seeReferences == null) {
                            this.seeReferences = new ArrayList<>();
                        }
                        this.seeReferences.add(seeRef);
                    }
                }
            }
        }

        private void parseJavadoc(String javadocContent) {
            if (javadocContent == null) return;
            
            if (this.javadocTags == null) {
                this.javadocTags = new ArrayList<>();
            }
            if (this.javadocParams == null) {
                this.javadocParams = new HashMap<>();
            }
            
            // 🎯 EXTRACCIÓN DE TAGS JAVADOC
            String[] lines = javadocContent.split("\n");
            for (String line : lines) {
                String trimmed = line.trim();
                
                // Detectar tags @param
                if (trimmed.startsWith("@param")) {
                    String paramInfo = trimmed.substring(6).trim();
                    String[] parts = paramInfo.split("\\s+", 2);
                    if (parts.length >= 2) {
                        javadocParams.put(parts[0], parts[1]);
                    }
                }
                
                // Detectar otros tags
                else if (trimmed.startsWith("@")) {
                    String tag = trimmed.split("\\s+")[0];
                    if (!javadocTags.contains(tag)) {
                        javadocTags.add(tag);
                    }
                    
                    // Extraer valores específicos de tags conocidos
                    if (trimmed.startsWith("@author")) {
                        this.author = extractValue(trimmed, "author");
                    } else if (trimmed.startsWith("@version")) {
                        this.version = extractValue(trimmed, "version");
                    } else if (trimmed.startsWith("@since")) {
                        this.since = extractValue(trimmed, "since");
                    } else if (trimmed.startsWith("@see")) {
                        String seeRef = extractValue(trimmed, "see");
                        if (seeRef != null && !seeRef.isEmpty()) {
                            if (this.seeReferences == null) {
                                this.seeReferences = new ArrayList<>();
                            }
                            this.seeReferences.add(seeRef);
                        }
                    }
                }
            }
        }

        private String extractValue(String line, String key) {
            if (line == null || key == null) return null;
            
            // 🎯 MÚLTIPLES PATRONES DE EXTRACCIÓN
            String[] patterns = {
                "@" + key + "\\s+(.+)",
                key + ":\\s*(.+)",
                key + "\\s*=\\s*\"([^\"]+)\"",
                key + "\\s*=\\s*'([^']+)'",
                key + "\\s*=\\s*([^\\s]+)"
            };
            
            for (String pattern : patterns) {
                java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern, java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Matcher matcher = regex.matcher(line);
                if (matcher.find()) {
                    return matcher.group(1).trim();
                }
            }
            
            return null;
        }

        // 🎯 MÉTODOS ADICIONALES TURBO PARA ANOTACIONES
        public void addAnnotation(String name, String value, Map<String, String> parameters) {
            if (this.annotations == null) {
                this.annotations = new HashMap<>();
            }
            
            // 🎯 FORMATO AVANZADO DE ANOTACIÓN CON PARÁMETROS
            if (parameters != null && !parameters.isEmpty()) {
                StringBuilder annotationBuilder = new StringBuilder();
                annotationBuilder.append("@").append(name);
                
                if (!parameters.isEmpty()) {
                    annotationBuilder.append("(");
                    List<String> paramPairs = new ArrayList<>();
                    parameters.forEach((k, v) -> paramPairs.add(k + " = " + v));
                    annotationBuilder.append(String.join(", ", paramPairs));
                    annotationBuilder.append(")");
                }
                
                this.annotations.put(name, annotationBuilder.toString());
            } else {
                this.annotations.put(name, value != null ? value : "@" + name);
            }
        }

        public Map<String, String> getAnnotationParameters(String annotationName) {
            Map<String, String> params = new HashMap<>();
            
            if (this.annotations == null || annotationName == null) {
                return params;
            }
            
            String annotationValue = this.annotations.get(annotationName);
            if (annotationValue != null && annotationValue.contains("(")) {
                // 🎯 PARSING AVANZADO DE PARÁMETROS DE ANOTACIÓN
                try {
                    String paramsStr = annotationValue.substring(
                        annotationValue.indexOf("(") + 1, 
                        annotationValue.lastIndexOf(")")
                    );
                    
                    String[] paramPairs = paramsStr.split(",");
                    for (String pair : paramPairs) {
                        String[] keyValue = pair.split("=", 2);
                        if (keyValue.length == 2) {
                            params.put(keyValue[0].trim(), keyValue[1].trim());
                        }
                    }
                } catch (Exception e) {
                    // 🛡️ MANEJO ELEGANTE DE ERRORES DE PARSING
                    System.err.println("⚠️ Error parsing annotation parameters: " + annotationName);
                }
            }
            
            return params;
        }

        // 🎯 MÉTRICAS AVANZADAS DE CALIDAD
        public double calculateMaintainabilityIndex() {
            if (lineCount == 0 || complexity == 0) return 100.0;
            
            // 🎯 FÓRMULA AVANZADA DE MANTENIBILIDAD
            double halsteadVolume = calculateHalsteadVolume();
            double cyclomaticComplexity = complexity;
            double linesOfCode = lineCount;
            
            double maintainabilityIndex = 171.0 - 5.2 * Math.log(halsteadVolume) 
                - 0.23 * cyclomaticComplexity - 16.2 * Math.log(linesOfCode);
            
            return Math.max(0, Math.min(100, maintainabilityIndex));
        }

        private double calculateHalsteadVolume() {
            // 🎯 MÉTRICAS DE HALSTEAD SIMPLIFICADAS
            int operators = getMethodCount() * 2 + getFieldCount();
            int operands = getFieldCount() + lineCount / 10;
            
            if (operators == 0 || operands == 0) return 1.0;
            
            return (operators + operands) * Math.log(operators + operands) / Math.log(2);
        }

        public String getMaintainabilityLevel() {
            double mi = calculateMaintainabilityIndex();
            if (mi >= 85) return "EXCELLENT";
            if (mi >= 65) return "GOOD";
            if (mi >= 45) return "MODERATE";
            return "DIFFICULT";
        }

        // 🎯 DETECCIÓN DE PATRONES AVANZADOS
        public boolean isSingleton() {
            if (!isFinal || !hasMethods() || !hasFields()) return false;
            
            // 🎯 PATRÓN SINGLETON: instancia estática + constructor privado
            boolean hasStaticInstance = getFields().stream()
                .anyMatch(field -> field.isStatic() && field.getType().equals(this.name));
            
            boolean hasPrivateConstructor = getMethods().stream()
                .anyMatch(method -> method.isConstructor() && method.isPrivate());
            
            return hasStaticInstance && hasPrivateConstructor;
        }

        public boolean isBuilder() {
            if (!hasMethods()) return false;
            
            // 🎯 PATRÓN BUILDER: métodos que retornan la misma instancia
            long fluentMethods = getMethods().stream()
                .filter(method -> method.getReturnType().equals(this.name) && 
                                 !method.isConstructor() && 
                                 !method.isStatic())
                .count();
            
            return fluentMethods >= 3; // Mínimo 3 métodos fluidos
        }

        public boolean isFactory() {
            String className = this.name.toLowerCase();
            
            // 🎯 PATRÓN FACTORY: nombre + métodos estáticos de creación
            boolean nameSuggestsFactory = className.contains("factory") || 
                                         className.contains("creator") || 
                                         className.contains("builder");
            
            boolean hasStaticCreationMethods = getMethods().stream()
                .anyMatch(method -> method.isStatic() && 
                                   (method.getName().startsWith("create") || 
                                    method.getName().startsWith("new")));
            
            return nameSuggestsFactory || hasStaticCreationMethods;
        }

        // 🎯 SERIALIZACIÓN AVANZADA
        public Map<String, Object> toDetailedMap() {
            Map<String, Object> map = new LinkedHashMap<>();
            
            map.put("name", this.name);
            map.put("package", this.packageName);
            map.put("fullName", getFullName());
            map.put("type", this.type);
            map.put("sourcePath", this.sourcePath);
            map.put("modifiers", getModifiers());
            map.put("architecturalRole", getArchitecturalRole());
            
            // 🎯 METADATAS AVANZADAS
            map.put("typeParameters", getTypeParameters());
            map.put("superClass", this.superClass);
            map.put("interfaces", this.interfaces);
            map.put("annotations", this.annotations != null ? new HashMap<>(this.annotations) : Collections.emptyMap());
            
            // 🎯 MÉTRICAS
            map.put("lineCount", this.lineCount);
            map.put("complexity", this.complexity);
            map.put("maintainabilityIndex", calculateMaintainabilityIndex());
            map.put("maintainabilityLevel", getMaintainabilityLevel());
            map.put("cohesion", calculateCohesion());
            
            // 🎯 CONTENIDO
            map.put("methodCount", getMethodCount());
            map.put("fieldCount", getFieldCount());
            map.put("innerClassCount", getInnerClassCount());
            
            // 🎯 PATRONES DETECTADOS
            map.put("isSingleton", isSingleton());
            map.put("isBuilder", isBuilder());
            map.put("isFactory", isFactory());
            map.put("isDataClass", isDataClass());
            map.put("isUtilityClass", isUtilityClass());
            
            // 🎯 COMENTARIOS
            map.put("hasJavadoc", hasJavadoc());
            map.put("hasClassComment", hasClassComment());
            map.put("author", this.author);
            map.put("version", this.version);
            map.put("since", this.since);
            
            return map;
        }

        public String toFormattedString() {
            StringBuilder sb = new StringBuilder();
            
            sb.append("🏷️ ").append(getFullName()).append("\n");
            sb.append("📊 TIPO: ").append(type).append(" | ").append(getModifiers()).append("\n");
            sb.append("🎯 ROL: ").append(getArchitecturalRole()).append("\n");
            
            if (hasTypeParameters()) {
                sb.append("🔧 GENÉRICOS: ").append(getTypeParametersAsString()).append("\n");
            }
            
            sb.append("📈 MÉTRICAS: ").append(lineCount).append(" líneas | ")
              .append(String.format("%.2f", complexity)).append(" complejidad | ")
              .append(String.format("%.1f", calculateMaintainabilityIndex())).append(" MI (")
              .append(getMaintainabilityLevel()).append(")\n");
            
            sb.append("📦 CONTENIDO: ").append(getMethodCount()).append(" métodos | ")
              .append(getFieldCount()).append(" campos | ")
              .append(getInnerClassCount()).append(" clases internas\n");
            
            // 🎯 PATRONES DETECTADOS
            List<String> patterns = new ArrayList<>();
            if (isSingleton()) patterns.add("Singleton");
            if (isBuilder()) patterns.add("Builder");
            if (isFactory()) patterns.add("Factory");
            if (!patterns.isEmpty()) {
                sb.append("🎭 PATRONES: ").append(String.join(", ", patterns)).append("\n");
            }
            
            return sb.toString();
        }

     // 🆕 AGREGAR ESTE MÉTODO
        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
        
        public String getFullName() {
            if (fullName != null && !fullName.isEmpty()) {
                return fullName;
            }
            // Fallback: construir desde package + name
            if (packageName != null && !packageName.isEmpty() && name != null && !name.isEmpty()) {
                return packageName + "." + name;
            }
            return name != null ? name : "";
        }
    }
