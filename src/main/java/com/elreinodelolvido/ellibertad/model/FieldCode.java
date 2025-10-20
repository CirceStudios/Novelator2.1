package com.elreinodelolvido.ellibertad.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 🎯 CLASES PARA ESTRUCTURA DETALLADA
 */
public class FieldCode {
    private String name;
    private String type;
    private String initialValue;
    private boolean isPublic;
    private boolean isPrivate;
    private boolean isProtected;
    private boolean isStatic;
    private boolean isFinal;
    private List<String> annotations = new ArrayList<>();
    private String comment;

    // 🎯 GETTERS Y SETTERS
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getInitialValue() { return initialValue; }
    public void setInitialValue(String initialValue) { this.initialValue = initialValue; }
    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean aPublic) { isPublic = aPublic; }
    public boolean isPrivate() { return isPrivate; }
    public void setPrivate(boolean aPrivate) { isPrivate = aPrivate; }
    public boolean isProtected() { return isProtected; }
    public void setProtected(boolean aProtected) { isProtected = aProtected; }
    public boolean isStatic() { return isStatic; }
    public void setStatic(boolean aStatic) { isStatic = aStatic; }
    public boolean isFinal() { return isFinal; }
    public void setFinal(boolean aFinal) { isFinal = aFinal; }
    public List<String> getAnnotations() { return annotations; }
    public void setAnnotations(List<String> annotations) { this.annotations = annotations; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}