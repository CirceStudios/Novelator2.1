package com.elreinodelolvido.ellibertad.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 🎯 CLASE PARA REPRESENTAR CÓDIGO COMPLETO DE UNA CLASE
 */
public class ClassCode {
    private final String fullyQualifiedName;
    private String packageDeclaration;
    private String classCode;
    private List<String> imports;
    private File sourceFile;
    private String classComment;
    private String javadoc;
    private String classType;
    private boolean isInnerClass;
    private boolean isReconstructed;
    
    // 🎯 ESTRUCTURA DETALLADA
    private List<FieldCode> fields = new ArrayList<>();
    private List<MethodCode> methods = new ArrayList<>();
    private List<MethodCode> constructors = new ArrayList<>();
    private List<ClassCode> innerClasses = new ArrayList<>();
    private List<ClassCode> innerEnums = new ArrayList<>();

    public ClassCode(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public String getFullName() {
		return this.fullyQualifiedName;
	}

	// 🎯 GETTERS Y SETTERS
    public String getFullyQualifiedName() { return fullyQualifiedName; }
    public String getPackageDeclaration() { return packageDeclaration; }
    public void setPackageDeclaration(String packageDeclaration) { this.packageDeclaration = packageDeclaration; }
    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }
    public List<String> getImports() { return imports; }
    public void setImports(List<String> imports) { this.imports = imports; }
    public File getSourceFile() { return sourceFile; }
    public void setSourceFile(File sourceFile) { this.sourceFile = sourceFile; }
    public String getClassComment() { return classComment; }
    public void setClassComment(String classComment) { this.classComment = classComment; }
    public String getJavadoc() { return javadoc; }
    public void setJavadoc(String javadoc) { this.javadoc = javadoc; }
    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }
    public boolean isInnerClass() { return isInnerClass; }
    public void setInnerClass(boolean innerClass) { isInnerClass = innerClass; }
    public boolean isReconstructed() { return isReconstructed; }
    public void setReconstructed(boolean reconstructed) { isReconstructed = reconstructed; }
    public List<FieldCode> getFields() { return fields; }
    public void setFields(List<FieldCode> fields) { this.fields = fields; }
    public List<MethodCode> getMethods() { return methods; }
    public void setMethods(List<MethodCode> methods) { this.methods = methods; }
    public List<MethodCode> getConstructors() { return constructors; }
    public void setConstructors(List<MethodCode> constructors) { this.constructors = constructors; }
    public List<ClassCode> getInnerClasses() { return innerClasses; }
    public void setInnerClasses(List<ClassCode> innerClasses) { this.innerClasses = innerClasses; }
    public List<ClassCode> getInnerEnums() { return innerEnums; }
    public void setInnerEnums(List<ClassCode> innerEnums) { this.innerEnums = innerEnums; }

    /**
     * 🎯 OBTENER CÓDIGO COMPLETO CON IMPORTS Y PACKAGE
     */
    public String getCompleteFileCode() {
        StringBuilder completeCode = new StringBuilder();
        
        if (packageDeclaration != null && !packageDeclaration.isEmpty()) {
            completeCode.append("package ").append(packageDeclaration).append(";\n\n");
        }
        
        if (imports != null && !imports.isEmpty()) {
            imports.forEach(imp -> completeCode.append(imp).append("\n"));
            completeCode.append("\n");
        }
        
        if (classComment != null && !classComment.isEmpty()) {
            completeCode.append("/* ").append(classComment).append(" */\n");
        }
        
        if (javadoc != null && !javadoc.isEmpty()) {
            completeCode.append("/**\n * ").append(javadoc).append("\n */\n");
        }
        
        completeCode.append(classCode);
        return completeCode.toString();
    }

    @Override
    public String toString() {
        return String.format("ClassCode{name='%s', type='%s', reconstructed=%s}", 
            fullyQualifiedName, classType, isReconstructed);
    }
}
