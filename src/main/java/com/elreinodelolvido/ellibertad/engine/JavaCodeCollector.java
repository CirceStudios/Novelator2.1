package com.elreinodelolvido.ellibertad.engine;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.elreinodelolvido.ellibertad.model.ClassCode;
import com.elreinodelolvido.ellibertad.model.ClassInfo;
import com.elreinodelolvido.ellibertad.model.FieldInfo;
import com.elreinodelolvido.ellibertad.model.MethodCode;
import com.elreinodelolvido.ellibertad.model.MethodInfo;
import com.elreinodelolvido.ellibertad.model.ParameterCode;
import com.elreinodelolvido.ellibertad.model.FieldCode;
import com.elreinodelolvido.ellibertad.scanner.EmergencyJavaParser;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ReferenceType;

/**
 * 🎯 RECOLECTOR DE CÓDIGO JAVA - Recopila todos los métodos de análisis y extrae código completo
 */
public class JavaCodeCollector {
    private final ProjectScanner scanner;
    private final Bitacora bitacora;

    public JavaCodeCollector(ProjectScanner scanner) {
        this.scanner = scanner;
        this.bitacora = new Bitacora();
    }

    public JavaCodeCollector(ProjectScanner scanner, Bitacora bitacora) {
        this.scanner = scanner;
        this.bitacora = bitacora;
    }

    /**
     * 🚀 MÉTODO PRINCIPAL - Obtiene todo el código Java de una clase específica
     */
    public Optional<ClassCode> getCompleteJavaCode(String fullyQualifiedName) {
        try {
            bitacora.info("🔍 Buscando código completo para: " + fullyQualifiedName);
            
            // 🎯 BUSCAR LA CLASE EN EL SCANNER
            Optional<ClassInfo> classInfoOpt = scanner.getClassByName(fullyQualifiedName);
            if (classInfoOpt.isEmpty()) {
                bitacora.warn("❌ Clase no encontrada: " + fullyQualifiedName);
                return Optional.empty();
            }

            ClassInfo classInfo = classInfoOpt.get();
            return extractCompleteClassCode(classInfo);

        } catch (Exception e) {
            bitacora.error("💥 Error obteniendo código para " + fullyQualifiedName + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * 🎯 EXTRAER CÓDIGO COMPLETO DE LA CLASE
     */
    private Optional<ClassCode> extractCompleteClassCode(ClassInfo classInfo) {
        try {
            String sourcePath = classInfo.getSourcePath();
            File sourceFile = new File(sourcePath);
            
            if (!sourceFile.exists()) {
                bitacora.warn("📁 Archivo fuente no encontrado: " + sourcePath);
                return extractCodeFromMemory(classInfo);
            }

            // 🎯 ESTRATEGIA EN CASCADA PARA OBTENER CÓDIGO
            return executeCascadingCodeExtraction(classInfo, sourceFile);

        } catch (Exception e) {
            bitacora.error("💥 Error extrayendo código de: " + classInfo.getFullName());
            return Optional.empty();
        }
    }

    /**
     * 🎯 ESTRATEGIA EN CASCADA - Múltiples métodos de extracción
     */
    private Optional<ClassCode> executeCascadingCodeExtraction(ClassInfo classInfo, File sourceFile) {
        // 🎯 NIVEL 1: Extracción completa con JavaParser
        Optional<ClassCode> result = extractWithJavaParser(classInfo, sourceFile);
        if (result.isPresent() && isHighQualityExtraction(result.get())) {
            bitacora.debug("✅ Extracción nivel 1 exitosa: " + classInfo.getFullName());
            return result;
        }

        // 🎯 NIVEL 2: Extracción estructurada
        result = extractStructuredCode(classInfo, sourceFile);
        if (result.isPresent()) {
            bitacora.debug("✅ Extracción nivel 2 exitosa: " + classInfo.getFullName());
            return result;
        }

        // 🎯 NIVEL 3: Extracción por líneas
        result = extractCodeByLines(classInfo, sourceFile);
        if (result.isPresent()) {
            bitacora.debug("✅ Extracción nivel 3 exitosa: " + classInfo.getFullName());
            return result;
        }

        // 🎯 NIVEL 4: Reconstrucción desde metadatos
        result = reconstructCodeFromMetadata(classInfo);
        bitacora.debug((result.isPresent() ? "✅" : "❌") + " Extracción nivel 4: " + classInfo.getFullName());
        return result;
    }

    /**
     * 🎯 EXTRACCIÓN CON JAVAPARSER - Método más preciso
     */
    private Optional<ClassCode> extractWithJavaParser(ClassInfo classInfo, File sourceFile) {
        try {
            ParseResult<CompilationUnit> parseResult = EmergencyJavaParser.parseRobust(sourceFile);
            
            if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
                CompilationUnit unit = parseResult.getResult().get();
                return extractSpecificClassFromUnit(unit, classInfo, sourceFile);
            }
            
        } catch (Exception e) {
            bitacora.debug("⚠️ Falló extracción JavaParser para: " + classInfo.getFullName());
        }
        
        return Optional.empty();
    }

    /**
     * 🎯 EXTRAER CLASE ESPECÍFICA DEL COMPILATION UNIT
     */
    private Optional<ClassCode> extractSpecificClassFromUnit(CompilationUnit unit, ClassInfo classInfo, File sourceFile) {
        try {
            String targetClassName = classInfo.getName();
            String packageName = classInfo.getPackageName();

            // 🎯 BUSCAR LA DECLARACIÓN DE CLASE ESPECÍFICA
            Optional<ClassOrInterfaceDeclaration> classDecl = unit.findAll(ClassOrInterfaceDeclaration.class).stream()
                    .filter(clazz -> clazz.getNameAsString().equals(targetClassName))
                    .findFirst();

            if (classDecl.isPresent()) {
                ClassCode classCode = new ClassCode(classInfo.getFullName());
                classCode.setSourceFile(sourceFile);
                classCode.setPackageDeclaration(unit.getPackageDeclaration().map(pd -> pd.getNameAsString()).orElse(""));
                
                // 🎯 OBTENER CÓDIGO COMPLETO DE LA CLASE
                String completeCode = classDecl.get().toString();
                classCode.setClassCode(completeCode);
                
                // 🎯 EXTRAER IMPORTS
                List<String> imports = unit.getImports().stream()
                        .map(importDecl -> importDecl.toString().trim())
                        .collect(Collectors.toList());
                classCode.setImports(imports);
                
                // 🎯 EXTRAER COMENTARIOS DE CLASE
                classDecl.get().getComment().ifPresent(comment -> {
                    classCode.setClassComment(comment.getContent());
                });
                
                classDecl.get().getJavadoc().ifPresent(javadoc -> {
                    classCode.setJavadoc(javadoc.getDescription().toText());
                });

                // 🎯 ANALIZAR ESTRUCTURA INTERNA
                analyzeClassStructure(classDecl.get(), classCode);
                
                return Optional.of(classCode);
            }

        } catch (Exception e) {
            bitacora.error("💥 Error extrayendo clase específica: " + classInfo.getFullName());
        }
        
        return Optional.empty();
    }

    /**
     * 🎯 ANALIZAR ESTRUCTURA INTERNA DE LA CLASE
     */
    private void analyzeClassStructure(ClassOrInterfaceDeclaration classDecl, ClassCode classCode) {
        // 🎯 CAMPOS
        List<FieldCode> fields = classDecl.findAll(FieldDeclaration.class).stream()
                .map(this::extractFieldCode)
                .collect(Collectors.toList());
        classCode.setFields(fields);

        // 🎯 MÉTODOS
        List<MethodCode> methods = classDecl.findAll(MethodDeclaration.class).stream()
                .map(this::extractMethodCode)
                .collect(Collectors.toList());
        classCode.setMethods(methods);

        // 🎯 CONSTRUCTORES
        List<MethodCode> constructors = classDecl.findAll(ConstructorDeclaration.class).stream()
                .map(this::extractConstructorCode)
                .collect(Collectors.toList());
        classCode.setConstructors(constructors);

        // 🎯 CLASES INTERNAS
        List<ClassCode> innerClasses = classDecl.findAll(ClassOrInterfaceDeclaration.class).stream()
                .filter(inner -> !inner.equals(classDecl))
                .map(inner -> extractInnerClassCode(inner, classCode))
                .collect(Collectors.toList());
        classCode.setInnerClasses(innerClasses);

        // 🎯 ENUMS INTERNOS
        List<ClassCode> innerEnums = classDecl.findAll(EnumDeclaration.class).stream()
                .map(enumDecl -> extractEnumCode(enumDecl, classCode))
                .collect(Collectors.toList());
        classCode.setInnerEnums(innerEnums);
    }

    /**
     * 🎯 EXTRAER CÓDIGO DE CAMPO
     */
    private FieldCode extractFieldCode(FieldDeclaration fieldDecl) {
        FieldCode fieldCode = new FieldCode();
        
        if (!fieldDecl.getVariables().isEmpty()) {
            VariableDeclarator variable = fieldDecl.getVariables().get(0);
            fieldCode.setName(variable.getNameAsString());
            fieldCode.setType(fieldDecl.getElementType().asString());
            
            variable.getInitializer().ifPresent(init -> {
                fieldCode.setInitialValue(init.toString());
            });
        }
        
        // 🎯 MODIFICADORES
        fieldCode.setPublic(fieldDecl.isPublic());
        fieldCode.setPrivate(fieldDecl.isPrivate());
        fieldCode.setProtected(fieldDecl.isProtected());
        fieldCode.setStatic(fieldDecl.isStatic());
        fieldCode.setFinal(fieldDecl.isFinal());
        
        // 🎯 ANOTACIONES
        List<String> annotations = fieldDecl.getAnnotations().stream()
                .map(AnnotationExpr::toString)
                .collect(Collectors.toList());
        fieldCode.setAnnotations(annotations);
        
        // 🎯 COMENTARIOS
        fieldDecl.getComment().ifPresent(comment -> {
            fieldCode.setComment(comment.getContent());
        });
        
        return fieldCode;
    }

    /**
     * 🎯 EXTRAER CÓDIGO DE MÉTODO
     */
    private MethodCode extractMethodCode(MethodDeclaration methodDecl) {
        MethodCode methodCode = new MethodCode();
        methodCode.setName(methodDecl.getNameAsString());
        methodCode.setReturnType(methodDecl.getType().asString());
        methodCode.setSignature(methodDecl.getDeclarationAsString());
        
        // 🎯 CUERPO COMPLETO DEL MÉTODO
        methodDecl.getBody().ifPresent(body -> {
            methodCode.setBody(body.toString());
            methodCode.setLineCount(calculateMethodLines(body));
        });

        // 🎯 MODIFICADORES
        methodCode.setPublic(methodDecl.isPublic());
        methodCode.setPrivate(methodDecl.isPrivate());
        methodCode.setProtected(methodDecl.isProtected());
        methodCode.setStatic(methodDecl.isStatic());
        methodCode.setFinal(methodDecl.isFinal());
        methodCode.setAbstract(methodDecl.isAbstract());
        methodCode.setSynchronized(methodDecl.isSynchronized());

        // 🎯 PARÁMETROS
        List<ParameterCode> parameters = methodDecl.getParameters().stream()
                .map(this::extractParameterCode)
                .collect(Collectors.toList());
        methodCode.setParameters(parameters);

        // 🎯 EXCEPCIONES
        List<String> exceptions = methodDecl.getThrownExceptions().stream()
                .map(ReferenceType::asString)
                .collect(Collectors.toList());
        methodCode.setExceptions(exceptions);

        // 🎯 ANOTACIONES
        List<String> annotations = methodDecl.getAnnotations().stream()
                .map(AnnotationExpr::toString)
                .collect(Collectors.toList());
        methodCode.setAnnotations(annotations);

        // 🎯 COMENTARIOS Y JAVADOC
        methodDecl.getComment().ifPresent(comment -> {
            methodCode.setComment(comment.getContent());
        });
        
        methodDecl.getJavadoc().ifPresent(javadoc -> {
            methodCode.setJavadoc(javadoc.getDescription().toText());
        });

        return methodCode;
    }

    /**
     * 🎯 EXTRAER CÓDIGO DE CONSTRUCTOR
     */
    private MethodCode extractConstructorCode(ConstructorDeclaration constructorDecl) {
        MethodCode constructorCode = new MethodCode();
        constructorCode.setName(constructorDecl.getNameAsString());
        constructorCode.setReturnType("void");
        constructorCode.setSignature(constructorDecl.getDeclarationAsString());
        constructorCode.setConstructor(true);
        
        // 🎯 CUERPO
        constructorCode.setBody(constructorDecl.getBody().toString());
        
        // 🎯 MODIFICADORES
        constructorCode.setPublic(constructorDecl.isPublic());
        constructorCode.setPrivate(constructorDecl.isPrivate());
        constructorCode.setProtected(constructorDecl.isProtected());

        // 🎯 PARÁMETROS
        List<ParameterCode> parameters = constructorDecl.getParameters().stream()
                .map(this::extractParameterCode)
                .collect(Collectors.toList());
        constructorCode.setParameters(parameters);

        return constructorCode;
    }

    /**
     * 🎯 EXTRAER CÓDIGO DE PARÁMETRO
     */
    private ParameterCode extractParameterCode(com.github.javaparser.ast.body.Parameter param) {
        ParameterCode paramCode = new ParameterCode();
        paramCode.setName(param.getNameAsString());
        paramCode.setType(param.getType().asString());
        paramCode.setFinal(param.isFinal());
        paramCode.setVarArgs(param.isVarArgs());
        
        // 🎯 ANOTACIONES DE PARÁMETRO
        List<String> annotations = param.getAnnotations().stream()
                .map(AnnotationExpr::toString)
                .collect(Collectors.toList());
        paramCode.setAnnotations(annotations);
        
        return paramCode;
    }

    /**
     * 🎯 EXTRAER CÓDIGO DE CLASE INTERNA
     */
    private ClassCode extractInnerClassCode(ClassOrInterfaceDeclaration innerClass, ClassCode parentClass) {
        ClassCode innerClassCode = new ClassCode(parentClass.getFullName() + "$" + innerClass.getNameAsString());
        innerClassCode.setClassCode(innerClass.toString());
        innerClassCode.setInnerClass(true);
        
        // 🎯 TIPO DE CLASE INTERNA
        innerClassCode.setClassType(innerClass.isInterface() ? "INTERFACE" : "CLASS");
        
        return innerClassCode;
    }

    /**
     * 🎯 EXTRAER CÓDIGO DE ENUM INTERNO
     */
    private ClassCode extractEnumCode(EnumDeclaration enumDecl, ClassCode parentClass) {
        ClassCode enumCode = new ClassCode(parentClass.getFullName() + "$" + enumDecl.getNameAsString());
        enumCode.setClassCode(enumDecl.toString());
        enumCode.setClassType("ENUM");
        enumCode.setInnerClass(true);
        
        return enumCode;
    }

    /**
     * 🎯 EXTRACCIÓN ESTRUCTURADA - Cuando JavaParser falla
     */
    private Optional<ClassCode> extractStructuredCode(ClassInfo classInfo, File sourceFile) {
        try {
            String content = Files.readString(sourceFile.toPath(), StandardCharsets.UTF_8);
            ClassCode classCode = new ClassCode(classInfo.getFullName());
            classCode.setSourceFile(sourceFile);
            
            // 🎯 USAR REGEX PARA EXTRAER LA CLASE ESPECÍFICA
            String classPattern = String.format(
                "(public\\s+)?(class|interface|enum|record)\\s+%s\\s*[^{]*\\{[\\s\\S]*?\\n\\}",
                Pattern.quote(classInfo.getName())
            );
            
            Pattern pattern = Pattern.compile(classPattern);
            Matcher matcher = pattern.matcher(content);
            
            if (matcher.find()) {
                classCode.setClassCode(matcher.group(0));
                
                // 🎯 EXTRAER PACKAGE
                extractPackageFromContent(content, classCode);
                
                // 🎯 EXTRAER IMPORTS
                extractImportsFromContent(content, classCode);
                
                return Optional.of(classCode);
            }
            
        } catch (Exception e) {
            bitacora.debug("⚠️ Falló extracción estructurada para: " + classInfo.getFullName());
        }
        
        return Optional.empty();
    }

    /**
     * 🎯 EXTRACCIÓN POR LÍNEAS - Método de respaldo
     */
    private Optional<ClassCode> extractCodeByLines(ClassInfo classInfo, File sourceFile) {
        try {
            List<String> lines = Files.readAllLines(sourceFile.toPath(), StandardCharsets.UTF_8);
            ClassCode classCode = new ClassCode(classInfo.getFullName());
            
            // 🎯 BUSCAR INICIO Y FIN DE LA CLASE
            int classStart = -1;
            int classEnd = -1;
            int braceCount = 0;
            boolean inClass = false;
            
            String classDeclaration = String.format("(class|interface|enum|record)\\s+%s\\b", classInfo.getName());
            Pattern classPattern = Pattern.compile(classDeclaration);
            
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                Matcher matcher = classPattern.matcher(line);
                
                if (matcher.find() && classStart == -1) {
                    classStart = i;
                    inClass = true;
                    braceCount = 0;
                }
                
                if (inClass) {
                    braceCount += countBraces(line);
                    if (braceCount == 0 && classStart != -1) {
                        classEnd = i;
                        break;
                    }
                }
            }
            
            if (classStart != -1 && classEnd != -1) {
                List<String> classLines = lines.subList(classStart, classEnd + 1);
                String classCodeStr = String.join("\n", classLines);
                classCode.setClassCode(classCodeStr);
                return Optional.of(classCode);
            }
            
        } catch (Exception e) {
            bitacora.debug("⚠️ Falló extracción por líneas para: " + classInfo.getFullName());
        }
        
        return Optional.empty();
    }

    /**
     * 🎯 RECONSTRUIR CÓDIGO DESDE METADATOS - Último recurso
     */
    private Optional<ClassCode> reconstructCodeFromMetadata(ClassInfo classInfo) {
        try {
            ClassCode classCode = new ClassCode(classInfo.getFullName());
            StringBuilder codeBuilder = new StringBuilder();
            
            // 🎯 RECONSTRUIR DECLARACIÓN DE CLASE
            codeBuilder.append(reconstructClassDeclaration(classInfo));
            codeBuilder.append(" {\n\n");
            
            // 🎯 RECONSTRUIR CAMPOS
            classInfo.getFields().forEach(field -> {
                codeBuilder.append("    ").append(reconstructField(field)).append("\n");
            });
            
            if (!classInfo.getFields().isEmpty()) {
                codeBuilder.append("\n");
            }
            
            // 🎯 RECONSTRUIR MÉTODOS
            classInfo.getMethods().forEach(method -> {
                codeBuilder.append("    ").append(reconstructMethod(method)).append("\n\n");
            });
            
            codeBuilder.append("}");
            
            classCode.setClassCode(codeBuilder.toString());
            classCode.setReconstructed(true);
            
            return Optional.of(classCode);
            
        } catch (Exception e) {
            bitacora.debug("⚠️ Falló reconstrucción para: " + classInfo.getFullName());
            return Optional.empty();
        }
    }

    /**
     * 🎯 RECONSTRUIR DECLARACIÓN DE CLASE
     */
    private String reconstructClassDeclaration(ClassInfo classInfo) {
        StringBuilder declaration = new StringBuilder();
        
        if (classInfo.isPublic()) declaration.append("public ");
        if (classInfo.isAbstract()) declaration.append("abstract ");
        if (classInfo.isFinal()) declaration.append("final ");
        
        String type = classInfo.getType().toLowerCase();
        if ("interface".equals(type)) {
            declaration.append("interface ");
        } else if ("enum".equals(type)) {
            declaration.append("enum ");
        } else if ("record".equals(type)) {
            declaration.append("record ");
        } else {
            declaration.append("class ");
        }
        
        declaration.append(classInfo.getName());
        
        // 🎯 SUPERCLASE E INTERFACES (simplificado)
        if (classInfo.getSuperClass() != null && !classInfo.getSuperClass().isEmpty()) {
            declaration.append(" extends ").append(classInfo.getSuperClass());
        }
        
        if (!classInfo.getInterfaces().isEmpty()) {
            declaration.append(" implements ").append(String.join(", ", classInfo.getInterfaces()));
        }
        
        return declaration.toString();
    }

    /**
     * 🎯 RECONSTRUIR CAMPO
     */
    private String reconstructField(FieldInfo field) {
        StringBuilder fieldBuilder = new StringBuilder();
        
        // 🎯 ANOTACIONES
        field.getAnnotations().forEach((key, value) -> {
            fieldBuilder.append("@").append(key);
            if (value != null && !value.isEmpty()) {
                fieldBuilder.append("(").append(value).append(")");
            }
            fieldBuilder.append("\n    ");
        });
        
        // 🎯 MODIFICADORES
        if (field.isPublic()) fieldBuilder.append("public ");
        if (field.isPrivate()) fieldBuilder.append("private ");
        if (field.isProtected()) fieldBuilder.append("protected ");
        if (field.isStatic()) fieldBuilder.append("static ");
        if (field.isFinal()) fieldBuilder.append("final ");
        
        fieldBuilder.append(field.getType()).append(" ").append(field.getName());
        
        if (field.getInitialValue() != null && !field.getInitialValue().isEmpty()) {
            fieldBuilder.append(" = ").append(field.getInitialValue());
        }
        
        fieldBuilder.append(";");
        return fieldBuilder.toString();
    }

    /**
     * 🎯 RECONSTRUIR MÉTODO
     */
    private String reconstructMethod(MethodInfo method) {
        StringBuilder methodBuilder = new StringBuilder();
        
        // 🎯 ANOTACIONES
        method.getAnnotations().forEach((key, value) -> {
            methodBuilder.append("    @").append(key);
            if (value != null && !value.isEmpty()) {
                methodBuilder.append("(").append(value).append(")");
            }
            methodBuilder.append("\n");
        });
        
        methodBuilder.append("    ");
        
        // 🎯 MODIFICADORES
        if (method.isPublic()) methodBuilder.append("public ");
        if (method.isPrivate()) methodBuilder.append("private ");
        if (method.isProtected()) methodBuilder.append("protected ");
        if (method.isStatic()) methodBuilder.append("static ");
        if (method.isFinal()) methodBuilder.append("final ");
        if (method.isAbstract()) methodBuilder.append("abstract ");
        
        methodBuilder.append(method.getReturnType()).append(" ").append(method.getName());
        methodBuilder.append("(");
        
        // 🎯 PARÁMETROS
        List<String> params = method.getParameters().stream()
                .map(param -> {
                    String paramStr = "";
                    if (param.isFinal()) paramStr += "final ";
                    paramStr += param.getType() + " " + param.getName();
                    return paramStr;
                })
                .collect(Collectors.toList());
        
        methodBuilder.append(String.join(", ", params));
        methodBuilder.append(")");
        
        // 🎯 EXCEPCIONES
        if (!method.getExceptions().isEmpty()) {
            methodBuilder.append(" throws ").append(String.join(", ", method.getExceptions()));
        }
        
        // 🎯 CUERPO
        if (method.isAbstract()) {
            methodBuilder.append(";");
        } else {
            methodBuilder.append(" {\n        // método implementado\n    }");
        }
        
        return methodBuilder.toString();
    }

    /**
     * 🎯 MÉTODOS AUXILIARES
     */
    private int calculateMethodLines(BlockStmt body) {
        return body.toString().split("\n").length;
    }

    private int countBraces(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == '{') count++;
            if (c == '}') count--;
        }
        return count;
    }

    private void extractPackageFromContent(String content, ClassCode classCode) {
        Pattern packagePattern = Pattern.compile("package\\s+([^;]+);");
        Matcher matcher = packagePattern.matcher(content);
        if (matcher.find()) {
            classCode.setPackageDeclaration(matcher.group(1));
        }
    }

    private void extractImportsFromContent(String content, ClassCode classCode) {
        Pattern importPattern = Pattern.compile("import\\s+([^;]+);");
        Matcher matcher = importPattern.matcher(content);
        List<String> imports = new ArrayList<>();
        while (matcher.find()) {
            imports.add(matcher.group(0));
        }
        classCode.setImports(imports);
    }

    private boolean isHighQualityExtraction(ClassCode classCode) {
        return classCode.getClassCode() != null && 
               !classCode.getClassCode().trim().isEmpty() &&
               classCode.getClassCode().contains("class") &&
               classCode.getClassCode().contains("{") &&
               classCode.getClassCode().contains("}");
    }

    private Optional<ClassCode> extractCodeFromMemory(ClassInfo classInfo) {
        // 🎯 INTENTAR EXTRAER DE LOS METADATOS EN MEMORIA
        bitacora.warn("📁 Archivo fuente no disponible, reconstruyendo desde metadatos: " + classInfo.getFullName());
        return reconstructCodeFromMetadata(classInfo);
    }

    /**
     * 🎯 MÉTODO PARA OBTENER MÚLTIPLES CLASES
     */
    public Map<String, ClassCode> getMultipleClasses(Set<String> fullyQualifiedNames) {
        Map<String, ClassCode> results = new ConcurrentHashMap<>();
        
        fullyQualifiedNames.parallelStream().forEach(className -> {
            getCompleteJavaCode(className).ifPresent(classCode -> {
                results.put(className, classCode);
            });
        });
        
        return results;
    }

    /**
     * 🎯 MÉTODO PARA OBTENER TODAS LAS CLASES ESCANEADAS
     */
    public Map<String, ClassCode> getAllScannedClasses() {
        Map<String, ClassCode> allClasses = new ConcurrentHashMap<>();
        List<ClassInfo> classes = ProjectScanner.getClasses();
        
        classes.parallelStream().forEach(classInfo -> {
            getCompleteJavaCode(classInfo.getFullName()).ifPresent(classCode -> {
                allClasses.put(classInfo.getFullName(), classCode);
            });
        });
        
        return allClasses;
    }

    /**
     * 🎯 GENERAR REPORTE DE EXTRACCIÓN
     */
    public String generateExtractionReport() {
        List<ClassInfo> allClasses = ProjectScanner.getClasses();
        Map<String, ClassCode> extracted = getAllScannedClasses();
        
        StringBuilder report = new StringBuilder();
        report.append("📊 REPORTE DE EXTRACCIÓN DE CÓDIGO\n");
        report.append("================================\n\n");
        report.append("Clases totales escaneadas: ").append(allClasses.size()).append("\n");
        report.append("Clases extraídas exitosamente: ").append(extracted.size()).append("\n");
        report.append("Tasa de éxito: ").append(String.format("%.1f%%", 
            (double) extracted.size() / Math.max(1, allClasses.size()) * 100)).append("\n\n");
        
        report.append("CLASES EXTRAÍDAS:\n");
        extracted.forEach((name, classCode) -> {
            String quality = classCode.isReconstructed() ? "🔄 RECONSTRUIDA" : "✅ COMPLETA";
            report.append("  • ").append(name).append(" - ").append(quality).append("\n");
        });
        
        return report.toString();
    }
    
    
}
