package com.elreinodelolvido.ellibertad.debates;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.elreinodelolvido.ellibertad.api.OraculoTecnico;
import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.scanner.IntegradorForzado;
import com.elreinodelolvido.ellibertad.util.FileUtils;
import com.elreinodelolvido.ellibertad.util.GeneradorClasesNuevas;
import com.elreinodelolvido.ellibertad.util.ValidadorFirmas;

/**
 * 🧩 FUSOR DE DEBATES AUTÓNOMO
 * Fundidor de Voluntades – traduce los debates en planes de acción sobre el código.
 */
public class FusorDeDebatesAutonomo {

    private final OraculoTecnico oraculo;
    private final Bitacora bitacora;
    private final GeneradorClasesNuevas generador;
    private final IntegradorForzado integrador;
    private final ValidadorFirmas validador;

    // Configuración para mostrar por consola
    private boolean mostrarPorConsola = true;

    public FusorDeDebatesAutonomo(
            OraculoTecnico oraculo,
            Bitacora bitacora,
            GeneradorClasesNuevas generador,
            IntegradorForzado integrador,
            ValidadorFirmas validador) {
        this.oraculo = oraculo;
        this.bitacora = bitacora;
        this.generador = generador;
        this.integrador = integrador;
        this.validador = validador;
    }

    /**
     * 🌊 Punto de entrada: fusionar sabiduría colectiva en un plan de evolución.
     */
    public void fusionarSabiduriaColectiva() {
        bitacora.info("[🧩FUSOR] Iniciando fusión mejorada...");
        imprimirConsola("🚀 INICIANDO FUSIÓN DE DEBATES AUTÓNOMOS");
        imprimirConsola("==========================================");
        
        try {
            List<File> debates = FileUtils.listFilesTurbo("autogen-output/debates", ".md", false)
                    .stream().map(File::new).collect(Collectors.toList());
            
            if (debates.isEmpty()) {
                bitacora.warn("[🧩FUSOR] No hay debates para fusionar.");
                imprimirConsola("❌ No se encontraron debates para fusionar en: autogen-output/debates/");
                return;
            }

            imprimirConsola("📁 Debates encontrados: " + debates.size());
            for (File debate : debates) {
                imprimirConsola("   - " + debate.getName());
            }
            imprimirConsola("");

            List<String> conclusiones = new ArrayList<>();
            for (File debate : debates) {
                bitacora.info("[🧩FUSOR] Analizando " + debate.getName());
                imprimirConsola("🔍 Analizando debate: " + debate.getName());
                List<String> conclusionesDebate = analizarConclusionesDebate(debate);
                conclusiones.addAll(conclusionesDebate);
                imprimirConsola("   Conclusiones extraídas: " + conclusionesDebate.size());
            }

            if (conclusiones.isEmpty()) {
                bitacora.warn("[🧩FUSOR] No se extrajo ninguna conclusión técnica.");
                imprimirConsola("❌ No se pudieron extraer conclusiones técnicas de los debates.");
                return;
            }

            imprimirConsola("");
            imprimirConsola("📊 CONCLUSIONES EXTRAÍDAS:");
            imprimirConsola("----------------------------");
            for (int i = 0; i < conclusiones.size(); i++) {
                imprimirConsola((i + 1) + ". " + conclusiones.get(i));
            }
            imprimirConsola("");

            // 🎯 PROMPT MEJORADO - SOLO TEXTO
            imprimirConsola("🧠 CONSULTANDO AL ORÁCULO TÉCNICO...");
            String promptMejorado = construirPromptParaPlan(conclusiones);
            String respuesta = oraculo.invocar(promptMejorado, "fusor_plan_autonomo", 0.6);

            // 🎯 GUARDAR RESPUESTA CRUDA
            FileUtils.writeToFile("autogen-output/debug/respuesta-fusor-cruda.txt", respuesta);
            
            // 🎯 MOSTRAR RESPUESTA DEL ORÁCULO POR CONSOLA
            imprimirConsola("✅ RESPUESTA DEL ORÁCULO RECIBIDA");
            imprimirConsola("=================================");
            imprimirConsola(respuesta);
            imprimirConsola("=================================");
            imprimirConsola("");
            
            // 🎯 PROCESAR SOLO TEXTO (NO CÓDIGO)
            imprimirConsola("🔄 PROCESANDO RESPUESTA PARA GENERAR PLAN...");
            String planPath = generarPlanDesdeRespuestaTexto(respuesta);
            
            if (planPath != null && !planPath.contains("fallback")) {
                bitacora.info("[🧩FUSOR] Plan generado exitosamente: " + planPath);
                imprimirConsola("✅ PLAN GENERADO EXITOSAMENTE: " + planPath);
                
                // 🎯 MOSTRAR CONTENIDO DEL PLAN POR CONSOLA
                mostrarContenidoDelPlan(planPath);
                
                ejecutarIntegracion(planPath);
            } else {
                bitacora.warn("[🧩FUSOR] Usando plan fallback, integración limitada");
                imprimirConsola("⚠️  USANDO PLAN DE RESERVA (FALLBACK)");
            }
            
            registrarCronicaFusion(conclusiones);

        } catch (Exception e) {
            bitacora.error("[🧩FUSOR] 💥 Error en fusión", e);
            imprimirConsola("💥 ERROR DURANTE LA FUSIÓN: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 📄 MOSTRAR CONTENIDO DEL PLAN POR CONSOLA
     */
    private void mostrarContenidoDelPlan(String planPath) {
        try {
            imprimirConsola("");
            imprimirConsola("📋 CONTENIDO DEL PLAN GENERADO:");
            imprimirConsola("================================");
            
            String contenido = FileUtils.readFile(planPath);
            if (contenido != null) {
                imprimirConsola(contenido);
            } else {
                imprimirConsola("No se pudo leer el contenido del plan: " + planPath);
            }
            
            imprimirConsola("================================");
            imprimirConsola("");
            
        } catch (Exception e) {
            imprimirConsola("❌ Error mostrando contenido del plan: " + e.getMessage());
        }
    }

    /**
     * 🖨️ MÉTODO PARA IMPRIMIR EN CONSOLA
     */
    private void imprimirConsola(String mensaje) {
        if (mostrarPorConsola) {
            System.out.println(mensaje);
        }
    }

    /**
     * 🖨️ MÉTODO PARA IMPRIMIR SEPARADOR EN CONSOLA
     */
    private void imprimirSeparadorConsola() {
        if (mostrarPorConsola) {
            System.out.println("--------------------------------------------------");
        }
    }
    
    /**
     * 🎯 GENERAR PLAN DESDE RESPUESTA DE TEXTO - Ignora código Java
     */
    private String generarPlanDesdeRespuestaTexto(String respuesta) {
        try {
            imprimirConsola("🔧 Procesando respuesta para generar plan...");
            imprimirConsola("   Longitud de respuesta: " + (respuesta != null ? respuesta.length() + " caracteres" : "null"));
            
            if (respuesta == null || respuesta.trim().isEmpty()) {
                bitacora.warn("[🧩FUSOR] Respuesta vacía o nula");
                imprimirConsola("❌ Respuesta vacía o nula - generando plan de reserva");
                return generarPlanFallback();
            }
            
            // 🎯 EXTRAER SOLO TEXTO - ELIMINAR CÓDIGO JAVA
            imprimirConsola("   Eliminando código Java...");
            String textoSinCodigo = eliminarCodigoJava(respuesta);
            imprimirConsola("   Texto limpio: " + textoSinCodigo.length() + " caracteres");
            
            // 🎯 BUSCAR CONTENIDO ESTRUCTURADO EN TEXTO
            imprimirConsola("   Analizando estructura del texto...");
            if (contienePlanEstructurado(textoSinCodigo)) {
                imprimirConsola("   ✅ Texto con estructura detectada - generando plan estructurado");
                return guardarPlanTextoEstructurado(textoSinCodigo);
            } else {
                imprimirConsola("   ℹ️  Texto sin estructura clara - generando plan simple");
                return guardarPlanTextoSimple(textoSinCodigo);
            }
            
        } catch (Exception e) {
            bitacora.error("[🧩FUSOR] Error generando plan desde texto", e);
            imprimirConsola("💥 Error generando plan: " + e.getMessage());
            return generarPlanFallback();
        }
    }

    /**
     * 🚫 ELIMINAR CÓDIGO JAVA - Filtra bloques de código Java
     */
    private String eliminarCodigoJava(String contenido) {
        if (contenido == null) return "";
        
        imprimirConsola("   🚫 Filtrando código Java...");
        
        // 🎯 PATRONES PARA BLOQUES DE CÓDIGO JAVA
        String resultado = contenido;
        
        // 🎯 ELIMINAR BLOQUES DE CÓDIGO COMPLETOS
        resultado = resultado.replaceAll("```java\\s*[\\s\\S]*?```", "");
        resultado = resultado.replaceAll("```\\s*[\\s\\S]*?```", "");
        
        // 🎯 ELIMINAR LÍNEAS DE CÓDIGO SUELTAS
        String[] lineas = resultado.split("\n");
        StringBuilder textoLimpio = new StringBuilder();
        
        int lineasCodigoEliminadas = 0;
        int lineasTextoMantenidas = 0;
        
        for (String linea : lineas) {
            String lineaTrim = linea.trim();
            boolean esCodigo = false;
            
            // 🎯 DETECTAR SI ES LÍNEA DE CÓDIGO
            if (lineaTrim.startsWith("package ") || 
                lineaTrim.startsWith("import ") ||
                lineaTrim.startsWith("public class ") ||
                lineaTrim.startsWith("private ") ||
                lineaTrim.startsWith("public ") ||
                lineaTrim.startsWith("protected ") ||
                lineaTrim.startsWith("@") ||
                (lineaTrim.contains("{") && lineaTrim.contains("}") && 
                 (lineaTrim.contains("(") || lineaTrim.contains(")"))) ||
                lineaTrim.endsWith(";")) {
                esCodigo = true;
                lineasCodigoEliminadas++;
            }
            
            if (!esCodigo && !lineaTrim.isEmpty()) {
                textoLimpio.append(linea).append("\n");
                lineasTextoMantenidas++;
            }
        }
        
        imprimirConsola("   📊 Estadísticas filtrado: " + lineasCodigoEliminadas + " líneas código eliminadas, " + 
                       lineasTextoMantenidas + " líneas texto mantenidas");
        
        return textoLimpio.toString().trim();
    }

    /**
     * 🔍 DETECTAR SI CONTIENE PLAN ESTRUCTURADO
     */
    private boolean contienePlanEstructurado(String texto) {
        if (texto == null) return false;
        
        String[] indicadoresEstructura = {
            "FASE", "PASO", "OBJETIVO", "PRIORIDAD", "IMPLEMENTAR", 
            "DESARROLLAR", "CREAR", "MODIFICAR", "OPTIMIZAR"
        };
        
        String textoUpper = texto.toUpperCase();
        int contadorIndicadores = 0;
        
        for (String indicador : indicadoresEstructura) {
            if (textoUpper.contains(indicador)) {
                contadorIndicadores++;
            }
        }
        
        boolean tieneEstructura = contadorIndicadores >= 3; // Mínimo 3 indicadores de estructura
        imprimirConsola("   🔍 Indicadores de estructura encontrados: " + contadorIndicadores + "/" + indicadoresEstructura.length);
        
        return tieneEstructura;
    }

    /**
     * 💾 GUARDAR PLAN TEXTO ESTRUCTURADO
     */
    private String guardarPlanTextoEstructurado(String texto) {
        try {
            String planDir = "autogen-output/planes";
            FileUtils.crearDirectorioSiNoExiste(planDir);
            
            String timestamp = String.valueOf(System.currentTimeMillis());
            String planPath = planDir + "/plan_estructurado_" + timestamp + ".txt";
            
            String contenidoEstructurado = construirPlanEstructurado(texto);
            FileUtils.writeToFile(planPath, contenidoEstructurado);
            
            bitacora.info("[🧩FUSOR] Plan estructurado guardado: " + planPath);
            imprimirConsola("   💾 Plan estructurado guardado en: " + planPath);
            
            return planPath;
            
        } catch (Exception e) {
            bitacora.error("[🧩FUSOR] Error guardando plan estructurado", e);
            imprimirConsola("   ❌ Error guardando plan estructurado: " + e.getMessage());
            return guardarPlanTextoSimple(texto);
        }
    }

    /**
     * 📝 CONSTRUIR PLAN ESTRUCTURADO DESDE TEXTO
     */
    private String construirPlanEstructurado(String texto) {
        StringBuilder plan = new StringBuilder();
        
        plan.append("# 🏴‍☠️ PLAN DE ACTUALIZACIÓN - TEXTO ESTRUCTURADO\n\n");
        plan.append("**Generado**: ").append(new Date()).append("\n\n");
        plan.append("## 📋 ANÁLISIS DEL TEXTO\n\n");
        
        // 🎯 EXTRAER OBJETIVOS PRINCIPALES
        List<String> objetivos = extraerObjetivos(texto);
        if (!objetivos.isEmpty()) {
            plan.append("### 🎯 OBJETIVOS PRINCIPALES\n\n");
            for (String objetivo : objetivos) {
                plan.append("- ").append(objetivo).append("\n");
            }
            plan.append("\n");
        }
        
        // 🎯 EXTRAER ACCIONES CONCRETAS
        List<String> acciones = extraerAcciones(texto);
        if (!acciones.isEmpty()) {
            plan.append("### 🔧 ACCIONES IDENTIFICADAS\n\n");
            for (String accion : acciones) {
                plan.append("- ").append(accion).append("\n");
            }
            plan.append("\n");
        }
        
        // 🎯 CONTENIDO ORIGINAL (LIMPIADO)
        plan.append("## 📝 CONTENIDO ORIGINAL PROCESADO\n\n");
        plan.append(texto).append("\n\n");
        
        // 🎯 PLAN DE EJECUCIÓN
        plan.append("## 🚀 PLAN DE EJECUCIÓN\n\n");
        plan.append("1. **Análisis de requisitos** - Validar objetivos identificados\n");
        plan.append("2. **Diseño de solución** - Elaborar arquitectura basada en texto\n");
        plan.append("3. **Implementación** - Desarrollar según especificaciones\n");
        plan.append("4. **Pruebas** - Validar funcionalidad\n");
        plan.append("5. **Integración** - Incorporar al sistema\n\n");
        
        plan.append("**Nota**: Este plan se generó automáticamente desde análisis de texto.\n");
        
        return plan.toString();
    }

    /**
     * 🎯 EXTRAER OBJETIVOS DEL TEXTO
     */
    private List<String> extraerObjetivos(String texto) {
        List<String> objetivos = new ArrayList<>();
        String[] lineas = texto.split("\n");
        
        for (String linea : lineas) {
            String lineaLower = linea.toLowerCase();
            if (lineaLower.contains("objetivo") || 
                lineaLower.contains("objetivos") ||
                lineaLower.contains("meta") ||
                lineaLower.contains("propósito") ||
                lineaLower.contains("finalidad")) {
                
                String objetivoLimpio = linea.trim()
                    .replace("OBJETIVO:", "")
                    .replace("Objetivo:", "")
                    .replace("OBJETIVOS:", "")
                    .replace("Objetivos:", "")
                    .replace("META:", "")
                    .replace("Meta:", "")
                    .trim();
                
                if (!objetivoLimpio.isEmpty() && objetivoLimpio.length() > 10) {
                    objetivos.add(objetivoLimpio);
                }
            }
        }
        
        // 🎯 FALLBACK: USAR PRIMERAS LÍNEAS COMO OBJETIVOS
        if (objetivos.isEmpty() && lineas.length > 0) {
            for (int i = 0; i < Math.min(3, lineas.length); i++) {
                if (lineas[i].length() > 20) {
                    objetivos.add(lineas[i].trim());
                }
            }
        }
        
        imprimirConsola("   🎯 Objetivos extraídos: " + objetivos.size());
        return objetivos;
    }

    /**
     * 🔧 EXTRAER ACCIONES DEL TEXTO
     */
    private List<String> extraerAcciones(String texto) {
        List<String> acciones = new ArrayList<>();
        String[] lineas = texto.split("\n");
        
        String[] verbosAccion = {
            "implementar", "desarrollar", "crear", "modificar", "optimizar",
            "mejorar", "agregar", "eliminar", "cambiar", "actualizar",
            "integrar", "refactorizar", "revisar", "corregir", "solucionar"
        };
        
        for (String linea : lineas) {
            String lineaLower = linea.toLowerCase();
            for (String verbo : verbosAccion) {
                if (lineaLower.contains(verbo) && linea.length() > 15) {
                    String accionLimpia = linea.trim();
                    if (!acciones.contains(accionLimpia)) {
                        acciones.add(accionLimpia);
                    }
                    break;
                }
            }
        }
        
        imprimirConsola("   🔧 Acciones identificadas: " + acciones.size());
        return acciones;
    }

    /**
     * 💾 GUARDAR PLAN TEXTO SIMPLE
     */
    private String guardarPlanTextoSimple(String texto) {
        try {
            String planDir = "autogen-output/planes";
            FileUtils.crearDirectorioSiNoExiste(planDir);
            
            String timestamp = String.valueOf(System.currentTimeMillis());
            String planPath = planDir + "/plan_texto_" + timestamp + ".txt";
            
            String contenido = "# 📝 PLAN DE TEXTO AUTÓNOMO\n\n" +
                              "**Generado**: " + new Date() + "\n\n" +
                              "## 📋 CONTENIDO ANALIZADO:\n\n" + texto +
                              "\n\n## 💡 NOTA:\nEste plan se generó desde análisis textual sin procesamiento de código Java.";
            
            FileUtils.writeToFile(planPath, contenido);
            bitacora.info("[🧩FUSOR] Plan de texto simple guardado: " + planPath);
            imprimirConsola("   💾 Plan simple guardado en: " + planPath);
            
            return planPath;
            
        } catch (Exception e) {
            bitacora.error("[🧩FUSOR] Error guardando plan de texto simple", e);
            imprimirConsola("   ❌ Error guardando plan simple: " + e.getMessage());
            return generarPlanFallback();
        }
    }

    /**
     * 🆘 GENERAR PLAN FALLBACK - Cuando todo falla
     */
    private String generarPlanFallback() {
        try {
            String planDir = "autogen-output/planes";
            FileUtils.crearDirectorioSiNoExiste(planDir);
            
            String fallbackPath = planDir + "/plan_fallback_" + System.currentTimeMillis() + ".txt";
            
            String planFallback = """
                PLAN DE ACTUALIZACIÓN FALLBACK - TEXTO
                
                OBJETIVO: Implementar mejoras críticas detectadas en debates
                
                ACCIONES IDENTIFICADAS:
                - Optimizar sistema de procesamiento de texto
                - Implementar análisis semántico mejorado
                - Mejorar extracción de conclusiones técnicas
                - Refinar generación de planes basados en texto
                
                PASOS PRIORITARIOS:
                1. Analizar estructura del texto recibido
                2. Identificar patrones y objetivos
                3. Elaborar plan de acción textual
                4. Ejecutar integración basada en texto
                
                GENERADO: %s
                NOTA: Plan fallback activado - procesamiento solo de texto
                """.formatted(new Date());
            
            FileUtils.writeToFile(fallbackPath, planFallback);
            bitacora.warn("[🧩FUSOR] Usando plan fallback de texto: " + fallbackPath);
            imprimirConsola("   ⚠️  Plan de reserva generado: " + fallbackPath);
            
            return fallbackPath;
            
        } catch (Exception e) {
            bitacora.error("[🧩FUSOR] Error crítico en plan fallback", e);
            imprimirConsola("   💥 Error crítico generando plan de reserva");
            return "autogen-output/planes/plan_emergencia.txt";
        }
    }

    /**
     * 🎯 CONSTRUIR PROMPT ESPECÍFICO PARA TEXTO (NO CÓDIGO)
     */
    private String construirPromptParaPlan(List<String> conclusiones) {
        return """
            ANALIZA estas conclusiones técnicas de debates y genera un PLAN DE ACCIÓN en TEXTO DESCRIPTIVO:
            
            CONCLUSIONES:
            %s
            
            FORMATO REQUERIDO:
            - SOLO TEXTO DESCRIPTIVO, NO CÓDIGO JAVA
            - Describe objetivos, fases y acciones en lenguaje natural
            - Enfócate en la arquitectura y funcionalidad, no en implementación específica
            - Usa español claro y técnico
            - Estructura en secciones lógicas pero sin formato JSON estricto
            
            EJEMPLO DE FORMATO:
            OBJETIVO PRINCIPAL: [descripción del objetivo]
            
            FASES DE IMPLEMENTACIÓN:
            FASE 1: [nombre] - [descripción]
            FASE 2: [nombre] - [descripción]
            
            ACCIONES CONCRETAS:
            - [acción específica]
            - [acción específica]
            
            PRIORIDADES:
            [lista de prioridades]
            
            IMPORTANTE: NO incluyas código Java, solo texto descriptivo.
            """.formatted(String.join("\n", conclusiones));
    }

    /**
     * 📜 Lee un debate y extrae secciones con conclusiones o recomendaciones.
     */
    private List<String> analizarConclusionesDebate(File archivo) throws IOException {
        List<String> conclusiones = new ArrayList<>();
        String texto = FileUtils.readFile(archivo.getAbsolutePath());
        if (texto == null) return conclusiones;
        Arrays.stream(texto.split("\n"))
                .filter(l -> l.startsWith("📊") || l.startsWith("🎯") || l.startsWith("🔧") || l.startsWith("🚀"))
                .forEach(conclusiones::add);
        return conclusiones;
    }

    /**
     * ⚙️ Ejecuta el ciclo de integración: generación → integración → validación.
     */
    private void ejecutarIntegracion(String planPath) {
        if (planPath == null) return;
        try {
            imprimirConsola("");
            imprimirConsola("⚙️  INICIANDO INTEGRACIÓN DEL PLAN...");
            bitacora.info("[🧩FUSOR] Ejecutando integración de plan textual...");
            
            // El generador ahora trabajará con planes de texto en lugar de código
            generador.generarDesdePlan(planPath);
            imprimirConsola("   ✅ Generación desde plan completada");
            
            integrador.integrarTodo();
            imprimirConsola("   ✅ Integración completada");
            
            validador.validarFirmas();
            imprimirConsola("   ✅ Validación de firmas completada");
            
            bitacora.info("[🧩FUSOR] ✅ Integración de texto completada.");
            imprimirConsola("🎉 INTEGRACIÓN COMPLETADA EXITOSAMENTE");
            
        } catch (Exception e) {
            bitacora.error("[🧩FUSOR] 💥 Error durante integración textual", e);
            imprimirConsola("💥 ERROR DURANTE LA INTEGRACIÓN: " + e.getMessage());
        }
    }

    /**
     * 🏴‍☠️ Registra un capítulo en la bitácora pirata.
     */
    private void registrarCronicaFusion(List<String> conclusiones) {
        try {
            StringBuilder cap = new StringBuilder();
            cap.append("\n## ⚙️ Capítulo de la Fusión de Voluntades - TEXTO\n");
            cap.append("Fecha: ").append(new Date()).append("\n\n");
            cap.append("**Resumen de la Sabiduría Colectiva:**\n");
            for (String c : conclusiones) cap.append("- ").append(c).append("\n");
            cap.append("\n> El Libertad integró las voces de su tripulación en planes de texto puro.\n");
            FileUtils.appendToFile("bitacora-pirata.md", cap.toString());
            bitacora.info("[🧩FUSOR] Crónica textual registrada en bitacora-pirata.md");
            imprimirConsola("📖 Crónica registrada en bitacora-pirata.md");
        } catch (Exception e) {
            bitacora.error("[🧩FUSOR] Error escribiendo crónica", e);
            imprimirConsola("❌ Error registrando crónica: " + e.getMessage());
        }
    }

    /**
     * 🔧 CONFIGURAR VISUALIZACIÓN POR CONSOLA
     */
    public void setMostrarPorConsola(boolean mostrar) {
        this.mostrarPorConsola = mostrar;
    }
}
