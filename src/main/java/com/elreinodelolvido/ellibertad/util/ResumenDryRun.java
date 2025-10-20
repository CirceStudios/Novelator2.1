package com.elreinodelolvido.ellibertad.util;

/**
 * 📊 CLASE PARA RESUMEN DRY-RUN - CON GETTERS
 */
public class ResumenDryRun {
    private String className;
    private String packageName;
    private boolean firmasOk;
    private String resumen;

    ResumenDryRun(String className, String packageName, boolean firmasOk, String resumen) {
        this.className = className;
        this.packageName = packageName;
        this.firmasOk = firmasOk;
        this.resumen = resumen;
    }
    
    // 🎯 GETTERS PARA ACCESO EXTERNO
    public String getClassName() { return className; }
    public String getPackageName() { return packageName; }
    public boolean isFirmasOk() { return firmasOk; }
    public String getResumen() { return resumen; }
    
    // 🎯 MÉTODO PARA REPRESENTACIÓN EN STRING
    @Override
    public String toString() {
        return String.format("ResumenDryRun[%s.%s, firmasOk=%s, resumen=%s]", 
            packageName, className, firmasOk, 
            resumen.length() > 50 ? resumen.substring(0, 47) + "..." : resumen);
    }
}