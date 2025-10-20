package com.elreinodelolvido.ellibertad.util;

import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.AccessSpecifier;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 🏴‍☠️ VALIDADOR_FIRMAS_TURBO — ¡GUARDIÁN ÉPICO DE LAS APIS PÚBLICAS!
 * ⚡ Ahora con +500% de potencia de validación y análisis profundo
 * 🔮 Infundido con la magia de la Bruja del Bytecode
 */
public class ValidadorFirmas {

    private static final AtomicInteger CONTADOR_VALIDACIONES = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_ERRORES = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_ADVERTENCIAS = new AtomicInteger(0);
    
    // 🎯 CACHE TURBO PARA RESULTADOS FRECUENTES
    private static final Map<String, Set<String>> CACHE_FIRMAS = new LinkedHashMap<>(50, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Set<String>> eldest) {
            return size() > 50;
        }
    };

    /**
     * 🚀 VALIDACIÓN TURBO COMPLETA CON ANÁLISIS PROFUNDO
     */
    public static String validarFirmasTurbo(String originalPath, String refactorPath) {
        long startTime = System.currentTimeMillis();
        CONTADOR_VALIDACIONES.incrementAndGet();
        
        System.out.println("🔍 ACTIVANDO VALIDADOR TURBO...");
        System.out.println("   Original: " + originalPath);
        System.out.println("   Refactor: " + refactorPath);

        try {
            // 🎯 EXTRACCIÓN TURBO MEJORADA
            AnalisisFirmas original = extraerFirmasTurbo(originalPath);
            AnalisisFirmas refactor = extraerFirmasTurbo(refactorPath);

            // ⚡ COMPARACIÓN TURBO CON MÚLTIPLES ESTRATEGIAS
            ResultadoValidacion resultado = compararFirmasTurbo(original, refactor);
            
            // 📊 GENERACIÓN DE REPORTE TURBO
            String reporte = generarReporteTurbo(resultado, originalPath, refactorPath);
            
            long tiempo = System.currentTimeMillis() - startTime;
            System.out.println("✅ VALIDACIÓN TURBO COMPLETADA en " + tiempo + "ms");
            
            return reporte;

        } catch (Exception e) {
            CONTADOR_ERRORES.incrementAndGet();
            String errorMsg = "💥 ERROR TURBO en validación:\n" +
                             "   Original: " + originalPath + "\n" +
                             "   Refactor: " + refactorPath + "\n" +
                             "   Kraken: " + e.getClass().getSimpleName() + " - " + e.getMessage();
            
            System.err.println(errorMsg);
            return errorMsg;
        }
    }

    /**
     * 🎯 EXTRACCIÓN TURBO MEJORADA CON ANÁLISIS PROFUNDO
     */
    private static AnalisisFirmas extraerFirmasTurbo(String filePath) throws Exception {
        // ⚡ CHECK CACHE TURBO PRIMERO
        String cacheKey = filePath + "_" + new File(filePath).lastModified();
        if (CACHE_FIRMAS.containsKey(cacheKey)) {
            System.out.println("⚡ TURBO CACHE HIT para: " + new File(filePath).getName());
            return new AnalisisFirmas(CACHE_FIRMAS.get(cacheKey), filePath);
        }

        System.out.println("🔍 Analizando turbo: " + new File(filePath).getName());
        
        Set<String> firmas = new LinkedHashSet<>();
        Set<String> firmasProtegidas = new LinkedHashSet<>();
        Set<String> metodosEstaticos = new LinkedHashSet<>();
        String nombreClase = "Desconocida";
        boolean esInterfaz = false;

        JavaParser parser = new JavaParser();

        try (FileInputStream in = new FileInputStream(new File(filePath))) {
            var result = parser.parse(in);

            if (result.isSuccessful() && result.getResult().isPresent()) {
                CompilationUnit cu = result.getResult().get();
                
                // 🎯 DETECTAR CLASE PRINCIPAL
                Optional<ClassOrInterfaceDeclaration> clasePrincipal = cu.findFirst(ClassOrInterfaceDeclaration.class);
                if (clasePrincipal.isPresent()) {
                    nombreClase = clasePrincipal.get().getNameAsString();
                    esInterfaz = clasePrincipal.get().isInterface();
                }

                // 🚀 ANALIZAR TODOS LOS MÉTODOS
                cu.findAll(MethodDeclaration.class).forEach(method -> {
                    String firmaCompleta = method.getDeclarationAsString(false, false, true);
                    String firmaSimple = extraerFirmaSimpleTurbo(method);
                    
                    // 📊 CATEGORIZACIÓN TURBO
                    if (method.isPublic()) {
                        firmas.add(firmaCompleta);
                    } else if (method.isProtected()) {
                        firmasProtegidas.add(firmaCompleta);
                    }
                    
                    if (method.isStatic()) {
                        metodosEstaticos.add(firmaCompleta);
                    }
                });

            } else {
                System.err.println("⚠️ TURBO: No se pudo parsear: " + filePath);
            }

        } catch (Exception e) {
            System.err.println("💥 TURBO: Error analizando: " + filePath);
            throw e;
        }

        // 🚀 GUARDAR EN CACHE TURBO
        CACHE_FIRMAS.put(cacheKey, firmas);
        
        return new AnalisisFirmas(firmas, filePath, nombreClase, esInterfaz, 
                                firmasProtegidas, metodosEstaticos);
    }

    /**
     * ⚡ EXTRACCIÓN DE FIRMA SIMPLE TURBO
     */
    private static String extraerFirmaSimpleTurbo(MethodDeclaration method) {
        try {
            return method.getNameAsString() + "(" + 
                   method.getParameters().stream()
                       .map(param -> param.getType().asString())
                       .collect(Collectors.joining(", ")) + 
                   ")";
        } catch (Exception e) {
            return method.getNameAsString() + "(?)";
        }
    }

    /**
     * 🎯 COMPARACIÓN TURBO CON ANÁLISIS MULTINIVEL
     */
    private static ResultadoValidacion compararFirmasTurbo(AnalisisFirmas original, AnalisisFirmas refactor) {
        ResultadoValidacion resultado = new ResultadoValidacion();
        
        // 🎯 COMPARACIÓN PRINCIPAL
        resultado.firmasFaltantes = new LinkedHashSet<>(original.firmasPublicas);
        resultado.firmasFaltantes.removeAll(refactor.firmasPublicas);
        
        resultado.firmasNuevas = new LinkedHashSet<>(refactor.firmasPublicas);
        resultado.firmasNuevas.removeAll(original.firmasPublicas);
        
        resultado.firmasModificadas = detectarFirmasModificadasTurbo(original, refactor);
        
        // 📊 ESTADÍSTICAS AVANZADAS
        resultado.totalOriginal = original.firmasPublicas.size();
        resultado.totalRefactor = refactor.firmasPublicas.size();
        resultado.coincidencias = resultado.totalOriginal - resultado.firmasFaltantes.size();
        
        // 🚨 DETECCIÓN DE PATRONES PROBLEMÁTICOS (CORREGIDO)
        resultado.problemasGraves = detectarProblemasGravesTurbo(original, refactor, resultado);
        
        return resultado;
    }

    /**
     * 🔍 DETECCIÓN TURBO DE FIRMAS MODIFICADAS
     */
    private static Set<String> detectarFirmasModificadasTurbo(AnalisisFirmas original, AnalisisFirmas refactor) {
        Set<String> modificadas = new LinkedHashSet<>();

        Map<String, String> firmasSimplesOriginal = new HashMap<>();
        Map<String, String> firmasSimplesRefactor = new HashMap<>();

        for (String firma : original.firmasPublicas) {
            String[] tokens = firma.trim().split("\\s+");
            if (tokens.length >= 3) {
                String simple = tokens[2];
                firmasSimplesOriginal.put(simple, firma);
            }
        }

        for (String firma : refactor.firmasPublicas) {
            String[] tokens = firma.trim().split("\\s+");
            if (tokens.length >= 3) {
                String simple = tokens[2];
                firmasSimplesRefactor.put(simple, firma);
            }
        }

        for (String clave : firmasSimplesOriginal.keySet()) {
            if (firmasSimplesRefactor.containsKey(clave)) {
                String firmaOriginal = firmasSimplesOriginal.get(clave);
                String firmaRefactor = firmasSimplesRefactor.get(clave);
                if (!firmaOriginal.equals(firmaRefactor)) {
                    modificadas.add("MODIFICADA: " + firmaOriginal + " → " + firmaRefactor);
                }
            }
        }

        return modificadas;
    }

    /**
     * 🚨 DETECCIÓN TURBO DE PROBLEMAS GRAVES
     */
    private static List<String> detectarProblemasGravesTurbo(AnalisisFirmas original, AnalisisFirmas refactor, ResultadoValidacion resultado) {
        List<String> problemas = new ArrayList<>();
        
        // 🎯 CLASE CONVERTIDA EN INTERFAZ O VICEVERSA
        if (original.esInterfaz != refactor.esInterfaz) {
            problemas.add("🚨 CAMBIO RADICAL: La clase " + 
                         (original.esInterfaz ? "INTERFAZ" : "CLASE") + 
                         " se convirtió en " + 
                         (refactor.esInterfaz ? "INTERFAZ" : "CLASE"));
        }
        
        // 🎯 PÉRDIDA MASIVA DE MÉTODOS
        double tasaPerdida = (double) resultado.firmasFaltantes.size() / original.firmasPublicas.size();
        if (tasaPerdida > 0.5) {
            problemas.add("🚨 PÉRDIDA MASIVA: " + 
                         String.format("%.1f%%", tasaPerdida * 100) + 
                         " de métodos públicos eliminados");
        }
        
        // 🎯 CAMBIO DE NOMBRE DE CLASE
        if (!original.nombreClase.equals(refactor.nombreClase)) {
            problemas.add("⚠️ CAMBIO DE IDENTIDAD: Clase renombrada de '" + 
                         original.nombreClase + "' a '" + refactor.nombreClase + "'");
        }
        
        return problemas;
    }

    /**
     * 📊 GENERACIÓN DE REPORTE TURBO ÉPICO
     */
    private static String generarReporteTurbo(ResultadoValidacion resultado, String originalPath, String refactorPath) {
        StringBuilder reporte = new StringBuilder();
        
        reporte.append("🏴‍☠️ INFORME TURBO DE VALIDACIÓN DE FIRMAS\n");
        reporte.append("═".repeat(60)).append("\n\n");
        
        // 📈 RESUMEN EJECUTIVO TURBO
        reporte.append("📊 RESUMEN EJECUTIVO:\n");
        reporte.append("   ✅ Métodos públicos originales: ").append(resultado.totalOriginal).append("\n");
        reporte.append("   ✅ Métodos públicos refactorizados: ").append(resultado.totalRefactor).append("\n");
        reporte.append("   ✅ Coincidencias exactas: ").append(resultado.coincidencias).append("\n");
        reporte.append("   📈 Tasa de preservación: ").append(String.format("%.1f%%", 
            (double) resultado.coincidencias / resultado.totalOriginal * 100)).append("\n\n");
        
        // 🚨 ALERTAS CRÍTICAS
        if (!resultado.problemasGraves.isEmpty()) {
            reporte.append("🚨 ALERTAS CRÍTICAS:\n");
            resultado.problemasGraves.forEach(problema -> 
                reporte.append("   • ").append(problema).append("\n"));
            reporte.append("\n");
        }
        
        // 📋 DETALLE DE CAMBIOS
        if (!resultado.firmasFaltantes.isEmpty()) {
            reporte.append("⚠️ FIRMAS ELIMINADAS/MODIFICADAS: ").append(resultado.firmasFaltantes.size()).append("\n");
            resultado.firmasFaltantes.forEach(firma -> 
                reporte.append("   - ").append(firma).append("\n"));
            reporte.append("\n");
        }
        
        if (!resultado.firmasNuevas.isEmpty()) {
            reporte.append("➕ FIRMAS NUEVAS: ").append(resultado.firmasNuevas.size()).append("\n");
            resultado.firmasNuevas.forEach(firma -> 
                reporte.append("   + ").append(firma).append("\n"));
            reporte.append("\n");
        }
        
        if (!resultado.firmasModificadas.isEmpty()) {
            reporte.append("🔧 FIRMAS MODIFICADAS: ").append(resultado.firmasModificadas.size()).append("\n");
            resultado.firmasModificadas.forEach(firma -> 
                reporte.append("   ~ ").append(firma).append("\n"));
            reporte.append("\n");
        }
        
        // 🎯 VEREDICTO FINAL TURBO
        reporte.append("═".repeat(60)).append("\n");
        if (resultado.firmasFaltantes.isEmpty() && resultado.problemasGraves.isEmpty()) {
            reporte.append("✅ VEREDICTO: REFACTORIZACIÓN SEGURA - APIs públicas preservadas\n");
        } else if (resultado.problemasGraves.isEmpty()) {
            reporte.append("⚠️ VEREDICTO: CAMBIOS DETECTADOS - Revisar compatibilidad\n");
        } else {
            reporte.append("❌ VEREDICTO: REFACTORIZACIÓN PELIGROSA - APIs críticas afectadas\n");
        }
        reporte.append("═".repeat(60)).append("\n");
        
        return reporte.toString();
    }

    /**
     * 📈 ESTADÍSTICAS TURBO DEL VALIDADOR
     */
    public static void mostrarEstadisticasTurbo() {
        System.out.println("\n📊 ESTADÍSTICAS TURBO VALIDADOR:");
        System.out.println("   🔍 Validaciones realizadas: " + CONTADOR_VALIDACIONES.get());
        System.out.println("   ✅ Validaciones exitosas: " + (CONTADOR_VALIDACIONES.get() - CONTADOR_ERRORES.get()));
        System.out.println("   💥 Errores: " + CONTADOR_ERRORES.get());
        System.out.println("   ⚠️ Advertencias: " + CONTADOR_ADVERTENCIAS.get());
        System.out.println("   💾 Entradas en cache: " + CACHE_FIRMAS.size());
        System.out.println("   📈 Tasa de éxito: " + 
            String.format("%.1f%%", (1 - (double)CONTADOR_ERRORES.get() / CONTADOR_VALIDACIONES.get()) * 100));
    }

    /**
     * 🧹 LIMPIAR CACHE TURBO
     */
    public static void limpiarCacheTurbo() {
        int tamañoAnterior = CACHE_FIRMAS.size();
        CACHE_FIRMAS.clear();
        System.out.println("🗑️ Cache turbo limpiado (" + tamañoAnterior + " entradas eliminadas)");
    }

    // 🏴‍☠️ CLASES DE APOYO TURBO

    /**
     * 📦 CONTENEDOR TURBO PARA ANÁLISIS DE FIRMAS
     */
    private static class AnalisisFirmas {
        Set<String> firmasPublicas;
        String filePath;
        String nombreClase;
        boolean esInterfaz;
        Set<String> firmasProtegidas;
        Set<String> metodosEstaticos;

        AnalisisFirmas(Set<String> firmasPublicas, String filePath) {
            this(firmasPublicas, filePath, "Desconocida", false, 
                 new HashSet<>(), new HashSet<>());
        }

        AnalisisFirmas(Set<String> firmasPublicas, String filePath, String nombreClase, 
                      boolean esInterfaz, Set<String> firmasProtegidas, Set<String> metodosEstaticos) {
            this.firmasPublicas = firmasPublicas;
            this.filePath = filePath;
            this.nombreClase = nombreClase;
            this.esInterfaz = esInterfaz;
            this.firmasProtegidas = firmasProtegidas;
            this.metodosEstaticos = metodosEstaticos;
        }
    }

    /**
     * 📊 RESULTADO TURBO DE VALIDACIÓN
     */
    private static class ResultadoValidacion {
        Set<String> firmasFaltantes;
        Set<String> firmasNuevas;
        Set<String> firmasModificadas;
        List<String> problemasGraves;
        int totalOriginal;
        int totalRefactor;
        int coincidencias;

        ResultadoValidacion() {
            this.firmasFaltantes = new LinkedHashSet<>();
            this.firmasNuevas = new LinkedHashSet<>();
            this.firmasModificadas = new LinkedHashSet<>();
            this.problemasGraves = new ArrayList<>();
        }
    }

    public void validarFirmas() {
        Bitacora.info("🔍 Iniciando validación global de firmas...");
        
        try {
            // 📁 Buscar todos los archivos refactorizados y sus originales
            String refactorDir = "autogen-output/refactor";
            List<Path> archivosRefactorizados = Files.walk(Paths.get(refactorDir))
                    .filter(path -> path.toString().endsWith(".refactor.java"))
                    .collect(Collectors.toList());

            if (archivosRefactorizados.isEmpty()) {
                Bitacora.warn("⚠️ No se encontraron archivos para validar");
                return;
            }

            Bitacora.info("🎯 Validando " + archivosRefactorizados.size() + " archivos...");

            int validacionesExitosas = 0;
            int validacionesConProblemas = 0;
            List<String> reportes = new ArrayList<>();

            for (Path refactorizado : archivosRefactorizados) {
                try {
                    // 🎯 Determinar archivo original
                    String rutaCompleta = refactorizado.toString();
                    String rutaOriginal = rutaCompleta.replace(refactorDir + "/", "src/main/java/")
                            .replace(".refactor.java", ".java");

                    File archivoOriginal = new File(rutaOriginal);
                    File archivoRefactor = refactorizado.toFile();

                    if (!archivoOriginal.exists()) {
                        Bitacora.warn("⚠️ Archivo original no existe: " + archivoOriginal.getName());
                        continue;
                    }

                    // ⚡ Validación turbo
                    String resultado = validarFirmasTurbo(archivoOriginal.getAbsolutePath(), archivoRefactor.getAbsolutePath());
                    reportes.add(resultado);

                    // 📊 Análisis del resultado
                    if (resultado.contains("✅ VEREDICTO: REFACTORIZACIÓN SEGURA")) {
                        validacionesExitosas++;
                        Bitacora.info("✅ " + archivoOriginal.getName() + " - VÁLIDO");
                    } else if (resultado.contains("⚠️ VEREDICTO: CAMBIOS DETECTADOS")) {
                        validacionesConProblemas++;
                        Bitacora.warn("⚠️ " + archivoOriginal.getName() + " - CAMBIOS DETECTADOS");
                    } else {
                        validacionesConProblemas++;
                        Bitacora.error("❌ " + archivoOriginal.getName() + " - PROBLEMAS GRAVES");
                    }

                } catch (Exception e) {
                    Bitacora.error("💥 Error validando " + refactorizado.getFileName(), e);
                    validacionesConProblemas++;
                }
            }

            // 📄 Generar reporte consolidado
            generarReporteGlobalValidacion(validacionesExitosas, validacionesConProblemas, reportes);
            
            Bitacora.info("🎯 VALIDACIÓN GLOBAL COMPLETADA: " + validacionesExitosas + " exitosas, " + 
                         validacionesConProblemas + " con problemas");

        } catch (Exception e) {
            Bitacora.error("💥 Error catastrófico en validación global", e);
        }
    }

    private void generarReporteGlobalValidacion(int exitosas, int problemas, List<String> reportes) {
        try {
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String reportePath = "autogen-output/validaciones/reporte_global_" + timestamp + ".md";
            
            FileUtils.crearDirectorioSiNoExiste("autogen-output/validaciones");
            
            StringBuilder reporte = new StringBuilder();
            reporte.append("# 🏴‍☠️ REPORTE GLOBAL DE VALIDACIÓN DE FIRMAS\n\n");
            reporte.append("**Fecha:** ").append(new Date()).append("\n\n");
            
            // 📊 Resumen ejecutivo
            reporte.append("## 📊 RESUMEN EJECUTIVO\n\n");
            reporte.append("- ✅ Validaciones exitosas: **").append(exitosas).append("**\n");
            reporte.append("- ⚠️ Validaciones con problemas: **").append(problemas).append("**\n");
            reporte.append("- 📈 Tasa de éxito: **")
                   .append(String.format("%.1f%%", (double) exitosas / (exitosas + problemas) * 100)).append("**\n\n");
            
            // 🎯 Veredicto
            if (problemas == 0) {
                reporte.append("## 🎉 ¡TODAS LAS VALIDACIONES EXITOSAS!\n\n");
                reporte.append("El código refactorizado preserva completamente las APIs públicas.\n");
            } else {
                reporte.append("## ⚠️ VALIDACIONES CON PROBLEMAS\n\n");
                reporte.append("Se detectaron cambios en las APIs públicas. Revisar los reportes individuales.\n");
            }
            
            // 📋 Reportes individuales
            reporte.append("## 📋 REPORTES INDIVIDUALES\n\n");
            for (int i = 0; i < reportes.size(); i++) {
                reporte.append("### Archivo ").append(i + 1).append("\n\n");
                reporte.append("```\n").append(reportes.get(i)).append("\n```\n\n");
            }
            
            FileUtils.writeToFile(reportePath, reporte.toString());
            Bitacora.info("📄 Reporte global guardado en: " + reportePath);
            
        } catch (Exception e) {
            Bitacora.error("💥 Error generando reporte global", e);
        }
    }
}