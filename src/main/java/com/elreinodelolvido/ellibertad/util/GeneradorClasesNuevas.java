package com.elreinodelolvido.ellibertad.util;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.elreinodelolvido.ellibertad.api.DeepSeekClient;
import com.elreinodelolvido.ellibertad.api.OraculoTecnico;
import com.elreinodelolvido.ellibertad.engine.Bitacora;

/**
 * GeneradorClasesNuevas - Sistema de generación automática de clases
 * Con estadísticas completas y tracking de métricas
 */
public class GeneradorClasesNuevas {
    
    // 📊 ESTADÍSTICAS DE GENERACIÓN
    private static final AtomicInteger clasesGeneradasTotal = new AtomicInteger(0);
    private static final AtomicInteger clasesGeneradasSesion = new AtomicInteger(0);
    private static final AtomicInteger tokensUtilizados = new AtomicInteger(0);
    private static final AtomicInteger erroresGeneracion = new AtomicInteger(0);
    private static final List<String> clasesGeneradas = new ArrayList<>();
    private static final Map<String, Integer> generacionesPorTipo = new HashMap<>();
    
    // 📅 FECHA DE INICIO
    private static final Date fechaInicio = new Date();
    
    /**
     * 🎯 GETESTADISTICAS COMPLETO - ¡IMPLEMENTADO!
     */
    public static EstadisticasGenerador getEstadisticas() {
        return new EstadisticasGenerador(
            clasesGeneradasTotal.get(),
            clasesGeneradasSesion.get(),
            tokensUtilizados.get(),
            erroresGeneracion.get(),
            new ArrayList<>(clasesGeneradas),
            new HashMap<>(generacionesPorTipo),
            fechaInicio,
            new Date()
        );
    }
    
    /**
     * 🚀 GENERAR CLASES DESDE OBJETIVOS - MÉTODO PRINCIPAL
     */
    public static List<String> generarDesdeObjetivos(String resumenProyecto, String objetivos) {
        List<String> nuevasClases = new ArrayList<>();
        
        try {
            System.out.println("🧠 ACTIVANDO TURBO-GENERADOR DE CLASES...");
            
            // 🎯 CONSTRUIR PROMPT OPTIMIZADO
            String prompt = construirPromptGeneracion(resumenProyecto, objetivos);
            
            // 🚀 CONFIGURAR CLIENTE PARA GENERACIÓN LARGA
            DeepSeekClient client = new DeepSeekClient();
            client.setTemperature(0.2); // Baja temperatura para código consistente
            
            System.out.println("⚡ Enviando prompt épico a DeepSeek...");
            String respuesta = client.enviarPromptNarrativo(prompt);
            
            if (respuesta != null && !respuesta.trim().isEmpty()) {
                System.out.println("🚀 INICIANDO EXTRACCIÓN TURBO MEJORADA...");
                nuevasClases = extraerClasesDeRespuesta(respuesta);
                
                // 📊 ACTUALIZAR ESTADÍSTICAS
                actualizarEstadisticas(nuevasClases, respuesta.length());
                
            } else {
                System.err.println("❌ Respuesta vacía del generador");
                erroresGeneracion.incrementAndGet();
            }
            
        } catch (Exception e) {
            System.err.println("💥 Error en generación: " + e.getMessage());
            erroresGeneracion.incrementAndGet();
        }
        
        return nuevasClases;
    }
    
    /**
     * 🏗️ CONSTRUIR PROMPT OPTIMIZADO PARA GENERACIÓN
     */
    private static String construirPromptGeneracion(String resumenProyecto, String objetivos) {
        return """
            Eres un experto arquitecto Java. Genera clases COMPLETAS y FUNCIONALES basadas en estos requerimientos.
            
            === RESUMEN DEL PROYECTO ACTUAL ===
            %s
            
            === OBJETIVOS ESPECÍFICOS ===
            %s
            
            🎯 INSTRUCCIONES ESPECÍFICAS:
            1. Genera clases Java COMPLETAS y LISTAS PARA USAR
            2. Incluye package, imports, métodos implementados
            3. Usa nombres descriptivos y siguiendo convenciones Java
            4. Proporciona código que se integre con el proyecto existente
            5. Incluye comentarios Javadoc para métodos importantes
            6. Asegura que el código compile y sea funcional
            
            📝 FORMATO DE RESPUESTA:
            Para cada clase, usa este formato:
            
            ```java
            package com.novelator.autogen.[subpaquete];
            
            /**
             * Descripción de la clase
             */
            public class NombreClase {
                // Campos
                
                // Constructores
                
                // Métodos implementados completamente
            }
            ```
            
            Genera entre 2-4 clases que resuelvan los objetivos. Enfócate en UTILIDAD y FUNCIONALIDAD.
            """.formatted(resumenProyecto, objetivos);
    }
    
    /**
     * 🔧 EXTRAER CLASES DE LA RESPUESTA
     */
    private static List<String> extraerClasesDeRespuesta(String respuesta) {
        List<String> clasesExtraidas = new ArrayList<>();
        
        try {
            // 📝 DIVIDIR POR BLOQUES DE CÓDIGO
            String[] bloques = respuesta.split("```java");
            
            for (int i = 1; i < bloques.length; i++) { // Empezar desde 1 para saltar el primer segmento
                String bloque = bloques[i];
                int finCodigo = bloque.indexOf("```");
                if (finCodigo > 0) {
                    String codigoClase = bloque.substring(0, finCodigo).trim();
                    
                    if (esClaseJavaValida(codigoClase)) {
                        // 📦 EXTRAER NOMBRE DE CLASE
                        String nombreClase = extraerNombreClase(codigoClase);
                        if (nombreClase != null) {
                            clasesExtraidas.add(nombreClase);
                            clasesGeneradas.add(nombreClase);
                            
                            // 💾 GUARDAR ARCHIVO
                            guardarClaseEnArchivo(nombreClase, codigoClase);
                            
                            System.out.println("💾 TURBO-GUARDADO: autogen-output/clases-nuevas/" + 
                                             determinarRutaPaquete(codigoClase) + "/" + nombreClase + ".java");
                        }
                    }
                }
            }
            
            // 📊 ACTUALIZAR CONTADORES
            int nuevas = clasesExtraidas.size();
            clasesGeneradasTotal.addAndGet(nuevas);
            clasesGeneradasSesion.addAndGet(nuevas);
            
            System.out.println("✅ TURBO-GENERACIÓN COMPLETADA: " + nuevas + " clases");
            
        } catch (Exception e) {
            System.err.println("❌ Error extrayendo clases: " + e.getMessage());
            erroresGeneracion.incrementAndGet();
        }
        
        return clasesExtraidas;
    }
    
    /**
     * ✅ VALIDAR SI ES UNA CLASE JAVA VÁLIDA
     */
    private static boolean esClaseJavaValida(String codigo) {
        return codigo.contains("public class") || 
               codigo.contains("public interface") || 
               codigo.contains("public enum");
    }
    
    /**
     * 🔍 EXTRAER NOMBRE DE CLASE DEL CÓDIGO
     */
    private static String extraerNombreClase(String codigo) {
        try {
            // Buscar "public class Nombre" o "public interface Nombre"
            String[] lineas = codigo.split("\n");
            for (String linea : lineas) {
                if (linea.trim().startsWith("public class") || 
                    linea.trim().startsWith("public interface") ||
                    linea.trim().startsWith("public enum")) {
                    
                    String[] partes = linea.trim().split("\\s+");
                    for (int i = 0; i < partes.length; i++) {
                        if (partes[i].equals("class") || 
                            partes[i].equals("interface") || 
                            partes[i].equals("enum")) {
                            
                            if (i + 1 < partes.length) {
                                String nombre = partes[i + 1];
                                // Limpiar nombre (puede tener { o extends)
                                nombre = nombre.replace("{", "").trim();
                                if (nombre.contains(" ")) {
                                    nombre = nombre.split(" ")[0];
                                }
                                if (nombre.contains("<")) {
                                    nombre = nombre.split("<")[0];
                                }
                                return nombre.trim();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error extrayendo nombre de clase: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * 📂 DETERMINAR RUTA DE PAQUETE DESDE EL CÓDIGO
     */
    private static String determinarRutaPaquete(String codigo) {
        try {
            String[] lineas = codigo.split("\n");
            for (String linea : lineas) {
                if (linea.trim().startsWith("package")) {
                    String paquete = linea.trim().substring(8).replace(";", "").trim();
                    return paquete.replace('.', '/');
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error determinando paquete: " + e.getMessage());
        }
        return "com/novelator/autogen/generadas";
    }
    
    /**
     * 💾 GUARDAR CLASE EN ARCHIVO
     */
    private static void guardarClaseEnArchivo(String nombreClase, String codigo) {
        try {
            String rutaPaquete = determinarRutaPaquete(codigo);
            String rutaCompleta = "autogen-output/clases-nuevas/" + rutaPaquete + "/" + nombreClase + ".java";
            
            FileUtils.crearDirectorioSiNoExiste("autogen-output/clases-nuevas/" + rutaPaquete);
            FileUtils.writeToFile(rutaCompleta, codigo);
            
        } catch (Exception e) {
            System.err.println("❌ Error guardando clase " + nombreClase + ": " + e.getMessage());
        }
    }
    
    /**
     * 📊 ACTUALIZAR ESTADÍSTICAS
     */
    private static void actualizarEstadisticas(List<String> nuevasClases, int longitudRespuesta) {
        // Estimar tokens (aproximado: 1 token ≈ 4 caracteres en inglés)
        int tokensEstimados = longitudRespuesta / 4;
        tokensUtilizados.addAndGet(tokensEstimados);
        
        // Actualizar por tipo
        for (String clase : nuevasClases) {
            String tipo = determinarTipoClase(clase);
            generacionesPorTipo.put(tipo, generacionesPorTipo.getOrDefault(tipo, 0) + 1);
        }
    }
    
    /**
     * 🏷️ DETERMINAR TIPO DE CLASE
     */
    private static String determinarTipoClase(String nombreClase) {
        if (nombreClase.toLowerCase().contains("manager") || nombreClase.toLowerCase().contains("controller")) {
            return "Manager";
        } else if (nombreClase.toLowerCase().contains("service")) {
            return "Service";
        } else if (nombreClase.toLowerCase().contains("util") || nombreClase.toLowerCase().contains("helper")) {
            return "Utility";
        } else if (nombreClase.toLowerCase().contains("model") || nombreClase.toLowerCase().contains("entity")) {
            return "Model";
        } else if (nombreClase.toLowerCase().contains("config")) {
            return "Configuration";
        } else {
            return "Other";
        }
    }
    
    /**
     * 📈 MOSTRAR ESTADÍSTICAS TURBO
     */
    public static void mostrarEstadisticasTurbo() {
        EstadisticasGenerador stats = getEstadisticas();
        
        System.out.println("\n📊 ESTADÍSTICAS TURBO-GENERADOR:");
        System.out.println("🏴‍☠️ Clases generadas en sesión: " + stats.getClasesGeneradasSesion());
        System.out.println("🎯 Clases únicas: " + stats.getClasesUnicas().size());
        System.out.println("📅 Total histórico: " + stats.getClasesGeneradasTotal());
        System.out.println("💎 Tokens utilizados: " + stats.getTokensUtilizados());
        System.out.println("🚨 Errores: " + stats.getErroresGeneracion());
        System.out.println("⏱️ Tiempo activo: " + stats.getTiempoActivo() + " minutos");
        
        System.out.println("\n📦 DISTRIBUCIÓN POR TIPO:");
        stats.getGeneracionesPorTipo().forEach((tipo, cantidad) -> {
            System.out.println("  • " + tipo + ": " + cantidad);
        });
    }
    
    /**
     * 📊 CLASE DE ESTADÍSTICAS
     */
    public static class EstadisticasGenerador {
        private final int clasesGeneradasTotal;
        private final int clasesGeneradasSesion;
        private final int tokensUtilizados;
        private final int erroresGeneracion;
        private final List<String> clasesUnicas;
        private final Map<String, Integer> generacionesPorTipo;
        private final Date fechaInicio;
        private final Date fechaConsulta;
        
        public EstadisticasGenerador(int total, int sesion, int tokens, int errores,
                                   List<String> unicas, Map<String, Integer> porTipo,
                                   Date inicio, Date consulta) {
            this.clasesGeneradasTotal = total;
            this.clasesGeneradasSesion = sesion;
            this.tokensUtilizados = tokens;
            this.erroresGeneracion = errores;
            this.clasesUnicas = unicas;
            this.generacionesPorTipo = porTipo;
            this.fechaInicio = inicio;
            this.fechaConsulta = consulta;
        }
        
        // Getters
        public int getClasesGeneradasTotal() { return clasesGeneradasTotal; }
        public int getClasesGeneradasSesion() { return clasesGeneradasSesion; }
        public int getTokensUtilizados() { return tokensUtilizados; }
        public int getErroresGeneracion() { return erroresGeneracion; }
        public List<String> getClasesUnicas() { return new ArrayList<>(clasesUnicas); }
        public Map<String, Integer> getGeneracionesPorTipo() { return new HashMap<>(generacionesPorTipo); }
        public long getTiempoActivo() { 
            return (fechaConsulta.getTime() - fechaInicio.getTime()) / (1000 * 60); 
        }
    }

	private Bitacora bitacora;

    public void generarDesdePlan(String planPath) {
        Bitacora.info("🧠 Iniciando generación desde plan: " + planPath);
        
        try {
            // 📖 Leer y parsear plan
            String contenidoPlan = FileUtils.readFile(planPath);
            if (contenidoPlan == null || contenidoPlan.trim().isEmpty()) {
                Bitacora.error("❌ Plan vacío o no encontrado: " + planPath);
                return;
            }

            // 🎯 Extraer especificaciones técnicas del plan
            EspecificacionesPlan especs = parsearEspecificacionesPlan(contenidoPlan);
            
            Bitacora.info("🎯 Plan parseado: " + especs.clasesRequeridas.size() + " clases requeridas");

            // 🚀 Generar cada clase requerida
            List<String> clasesGeneradas = new ArrayList<>();
            
            for (RequerimientoClase req : especs.clasesRequeridas) {
                try {
                    Bitacora.info("🔨 Generando clase: " + req.nombre);
                    
                    String codigoClase = generarClaseDesdeRequerimiento(req, especs.contextoGeneral);
                    if (codigoClase != null && !codigoClase.trim().isEmpty()) {
                        guardarClaseGenerada(req, codigoClase);
                        clasesGeneradas.add(req.nombre);
                        Bitacora.info("✅ Clase generada: " + req.nombre);
                    } else {
                        Bitacora.warn("⚠️ Clase vacía generada para: " + req.nombre);
                    }
                    
                } catch (Exception e) {
                    Bitacora.error("💥 Error generando clase " + req.nombre, e);
                }
            }

            // 📊 Reporte final
            generarReporteGeneracionPlan(clasesGeneradas, especs, planPath);
            
            Bitacora.info("🎉 GENERACIÓN DESDE PLAN COMPLETADA: " + clasesGeneradas.size() + 
                         "/" + especs.clasesRequeridas.size() + " clases generadas");

        } catch (Exception e) {
            Bitacora.error("💥 Error catastrófico generando desde plan", e);
        }
    }

    private EspecificacionesPlan parsearEspecificacionesPlan(String contenidoPlan) {
        EspecificacionesPlan especs = new EspecificacionesPlan();
        especs.contextoGeneral = contenidoPlan;
        especs.clasesRequeridas = new ArrayList<>();

        try {
            // 🎯 Extraer nombres de clases del plan (búsqueda simple)
            String[] lineas = contenidoPlan.split("\n");
            for (String linea : lineas) {
                linea = linea.trim();
                
                // Buscar patrones comunes en planes
                if (linea.matches(".*[Cc]lass\\s+[A-Z][a-zA-Z0-9_]*.*") || 
                    linea.matches(".*[Cc]rear\\s+[A-Z][a-zA-Z0-9_]*.*") ||
                    linea.matches(".*[Nn]ueva\\s+[Cc]lase\\s+[A-Z][a-zA-Z0-9_]*.*")) {
                    
                    // Extraer nombre de clase aproximado
                    String[] palabras = linea.split("\\s+");
                    for (int i = 0; i < palabras.length; i++) {
                        String palabra = palabras[i];
                        if ((palabra.equals("class") || palabra.equals("Class") || 
                             palabra.equals("Crear") || palabra.equals("crear") ||
                             palabra.equals("clase") || palabra.equals("Clase")) && 
                            i + 1 < palabras.length) {
                            
                            String nombreClase = palabras[i + 1].replaceAll("[^a-zA-Z0-9_]", "");
                            if (!nombreClase.isEmpty() && Character.isUpperCase(nombreClase.charAt(0))) {
                                RequerimientoClase req = new RequerimientoClase();
                                req.nombre = nombreClase;
                                req.descripcion = "Generada desde plan: " + linea;
                                req.paquete = determinarPaqueteDesdeContexto(contenidoPlan);
                                especs.clasesRequeridas.add(req);
                                break;
                            }
                        }
                    }
                }
            }

            // Si no se encontraron clases específicas, crear algunas genéricas
            if (especs.clasesRequeridas.isEmpty()) {
                Bitacora.info("🔍 No se detectaron clases específicas en el plan, generando clases base...");
                
                String[] clasesBase = {"PlanExecutor", "TaskManager", "ResourceCoordinator"};
                for (String nombreClase : clasesBase) {
                    RequerimientoClase req = new RequerimientoClase();
                    req.nombre = nombreClase;
                    req.descripcion = "Clase base generada automáticamente desde el plan";
                    req.paquete = "com.novelator.autogen.plan";
                    especs.clasesRequeridas.add(req);
                }
            }

        } catch (Exception e) {
            Bitacora.error("💥 Error parseando especificaciones del plan", e);
        }

        return especs;
    }

    private String determinarPaqueteDesdeContexto(String contexto) {
        if (contexto.contains("debate") || contexto.contains("fusor")) {
            return "com.novelator.autogen.debates";
        } else if (contexto.contains("plan") || contexto.contains("ejecutor")) {
            return "com.novelator.autogen.plan";
        } else if (contexto.contains("util") || contexto.contains("herramienta")) {
            return "com.novelator.autogen.util";
        } else {
            return "com.novelator.autogen.generated";
        }
    }

    private String generarClaseDesdeRequerimiento(RequerimientoClase req, String contexto) {
        try {
            // 🚀 Construir prompt específico para el Oráculo
            String prompt = construirPromptGeneracionClase(req, contexto);
            
            // 📡 Usar Oráculo Técnico para generar el código
            // Nota: Asumiendo que tenemos acceso al Oráculo a través de inyección de dependencias
            // En una implementación real, esto se manejaría mediante DI
            OraculoTecnico oraculo = new OraculoTecnico("deepseek-chat", 0.3, 0.3);
            return oraculo.invocar(prompt, "generacion_clase", 0.3);
            
        } catch (Exception e) {
            Bitacora.error("💥 Error generando clase desde requerimiento: " + req.nombre, e);
            return generarClaseFallback(req);
        }
    }

    private String construirPromptGeneracionClase(RequerimientoClase req, String contexto) {
        return String.format("""
            Genera una clase Java COMPLETA y FUNCIONAL con estos requerimientos:
            
            NOMBRE CLASE: %s
            PAQUETE: %s
            DESCRIPCIÓN: %s
            CONTEXTO DEL PLAN: %s
            
            REQUISITOS TÉCNICOS:
            1. Clase Java 100%% válida y compilable
            2. Package correcto e imports necesarios
            3. Métodos implementados completamente
            4. Comentarios Javadoc para la clase y métodos principales
            5. Estructura limpia y profesional
            6. Preferir composición sobre herencia
            
            FORMATO EXACTO:
            ```java
            package [paquete_completo];
            
            [imports_necesarios]
            
            /**
             * [Descripción de la clase]
             */
            public class %s {
                // Campos/atributos
                
                // Constructores
                
                // Métodos implementados
            }
            ```
            
            Genera SOLO el código Java, sin explicaciones adicionales.
            """, req.nombre, req.paquete, req.descripcion, 
            contexto.substring(0, Math.min(500, contexto.length())), req.nombre);
    }

    private String generarClaseFallback(RequerimientoClase req) {
        // 🛡️ Generación de fallback para cuando el Oráculo falle
        return String.format("""
            package %s;
            
            /**
             * %s
             * 
             * <p>Clase generada automáticamente desde plan de desarrollo.
             * Implementación base - requiere completar según especificaciones.
             */
            public class %s {
                
                private final String nombre;
                
                /**
                 * Constructor principal
                 */
                public %s() {
                    this.nombre = "%s";
                }
                
                /**
                 * Ejecutar funcionalidad principal
                 */
                public void ejecutar() {
                    // TODO: Implementar según especificaciones del plan
                    System.out.println("Ejecutando: " + nombre);
                }
                
                /**
                 * Obtener nombre de la clase
                 */
                public String getNombre() {
                    return nombre;
                }
                
                // Método main para pruebas básicas
                public static void main(String[] args) {
                    %s instancia = new %s();
                    instancia.ejecutar();
                }
            }
            """, req.paquete, req.descripcion, req.nombre, 
            req.nombre, req.nombre, req.nombre, req.nombre);
    }

    private void guardarClaseGenerada(RequerimientoClase req, String codigoClase) {
        try {
            String rutaPaquete = req.paquete.replace('.', '/');
            String rutaCompleta = "autogen-output/clases-nuevas/" + rutaPaquete + "/" + req.nombre + ".java";
            
            FileUtils.crearDirectorioSiNoExiste("autogen-output/clases-nuevas/" + rutaPaquete);
            FileUtils.writeToFile(rutaCompleta, codigoClase);
            
            Bitacora.debug("💾 Clase guardada: " + rutaCompleta);
            
        } catch (Exception e) {
            Bitacora.error("💥 Error guardando clase " + req.nombre, e);
        }
    }

    private void generarReporteGeneracionPlan(List<String> clasesGeneradas, EspecificacionesPlan especs, String planPath) {
        try {
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String reportePath = "autogen-output/generacion/reporte_plan_" + timestamp + ".md";
            
            FileUtils.crearDirectorioSiNoExiste("autogen-output/generacion");
            
            StringBuilder reporte = new StringBuilder();
            reporte.append("# 📋 REPORTE DE GENERACIÓN DESDE PLAN\n\n");
            reporte.append("**Plan origen:** ").append(planPath).append("\n");
            reporte.append("**Fecha generación:** ").append(new Date()).append("\n\n");
            
            reporte.append("## 📊 RESULTADOS\n\n");
            reporte.append("- 📁 Clases requeridas: **").append(especs.clasesRequeridas.size()).append("**\n");
            reporte.append("- ✅ Clases generadas: **").append(clasesGeneradas.size()).append("**\n");
            reporte.append("- 📈 Tasa de éxito: **")
                   .append(String.format("%.1f%%", (double) clasesGeneradas.size() / especs.clasesRequeridas.size() * 100))
                   .append("**\n\n");
            
            reporte.append("## 🗂️ CLASES GENERADAS\n\n");
            for (String clase : clasesGeneradas) {
                reporte.append("- `").append(clase).append("`\n");
            }
            
            if (clasesGeneradas.size() < especs.clasesRequeridas.size()) {
                reporte.append("\n## ⚠️ CLASES NO GENERADAS\n\n");
                for (RequerimientoClase req : especs.clasesRequeridas) {
                    if (!clasesGeneradas.contains(req.nombre)) {
                        reporte.append("- `").append(req.nombre).append("` - ").append(req.descripcion).append("\n");
                    }
                }
            }
            
            FileUtils.writeToFile(reportePath, reporte.toString());
            Bitacora.info("📄 Reporte de generación guardado: " + reportePath);
            
        } catch (Exception e) {
            Bitacora.error("💥 Error generando reporte de generación", e);
        }
    }

    // 🏴‍☠️ Clases de apoyo para la generación desde plan
    private static class EspecificacionesPlan {
        String contextoGeneral;
        List<RequerimientoClase> clasesRequeridas;
    }

    private static class RequerimientoClase {
        String nombre;
        String descripcion;
        String paquete;
        // Podrían añadirse más campos como métodos requeridos, interfaces, etc.
    }
}

