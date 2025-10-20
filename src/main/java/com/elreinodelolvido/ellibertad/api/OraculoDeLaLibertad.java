package com.elreinodelolvido.ellibertad.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.util.FileUtils;

/**
 * 🧭 Grumete Bded3 — *El Oráculo de la Libertad*  
 * Fusión sagrada entre técnica y narrativa. Invoca modelos LLM para múltiples voces del barco.  
 * ¡Pronto hablará con el eco de GPT, DeepSeek, Claude o modelos locales!
 */
public class OraculoDeLaLibertad {

    private final DeepSeekClient deepSeek;
    private Bitacora bitacora = new Bitacora();

    public OraculoDeLaLibertad() {
        this.deepSeek = new DeepSeekClient();
        this.bitacora = bitacora;
        bitacora.setConsolaActiva(true);
        bitacora.setArchivoActivo(true);
    }

    /**
     * Envía un mensaje narrativo y lo registra en la bitácora narrativa.
     */
    public String invocarNarrador(String mensaje) {
        System.out.println("🎭 Invocando al Narrador Épico...");
        String response = deepSeek.enviarPromptNarrativo(mensaje);
        if (response != null) {
            registrarBitacoraNarrativa(mensaje, response, "narrativo");
            System.out.println("✅ Narración completada y registrada en bitácora.");
        } else {
            System.err.println("❌ Falló la invocación del narrador.");
        }
        return response;
    }

    /**
     * Método flexible para cualquier tipo de consulta
     */
    public String invocar(String mensaje, String tipo, double temperatura) {
        System.out.println("🔮 Invocando al Oráculo (" + tipo + ")...");
        String response = deepSeek.enviar(mensaje, "deepseek-chat", temperatura, 
            "Eres un asistente útil especializado en " + tipo + ".");
        
        if (response != null) {
            registrarBitacoraNarrativa(mensaje, response, tipo);
            System.out.println("✅ Invocación '" + tipo + "' completada.");
        } else {
            System.err.println("❌ Falló la invocación del tipo: " + tipo);
        }
        return response;
    }

    /**
     * Guarda en bitácora el intercambio con el Oráculo.
     */
    private void registrarBitacoraNarrativa(String prompt, String respuesta, String tipo) {
        try {
            String id = UUID.randomUUID().toString().substring(0, 8);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            // Asegurar que el directorio existe
            FileUtils.crearArchivoSiNoExiste("autogen-output/bitacora-oraculo.md", 
                "# 🧭 Bitácora del Oráculo de la Libertad\n\n*Registro de todas las consultas al oráculo*\n\n");

            String md = """

                ---
                ## 🧭 Intercambio con el Oráculo (%s)
                **ID**: `%s`  
                **Fecha**: %s  
                **Tipo**: %s
                ---

                ### 📤 Prompt enviado:
                ```text
                %s
                ```

                ### 📥 Respuesta recibida:
                ```text
                %s
                ```

                ---
                *Fin del intercambio*
                
                """.formatted(tipo.toUpperCase(), id, timestamp, tipo, 
                    prompt.trim(), 
                    respuesta != null ? respuesta.trim() : "NULL");

            FileUtils.appendToFile("autogen-output/bitacora-oraculo.md", md);
            
        } catch (Exception e) {
            System.err.println("💥 Error registrando en bitácora: " + e.getMessage());
        }
    }

    /**
     * Obtiene estadísticas de uso del oráculo
     */
    public void mostrarEstadisticas() {
        System.out.println("\n📊 Estadísticas del Oráculo de la Libertad:");
        System.out.println("🔮 Instanciado: " + this.getClass().getSimpleName());
        System.out.println("🚀 Cliente: DeepSeek API");
        System.out.println("📁 Bitácora: autogen-output/bitacora-oraculo.md");
    }

    // EN OraculoDeLaLibertad.java - VERSIÓN TURBOFULURADA:

    public String invocarOraculoTecnico(String codigoFuente) {
        try {
            String nombreClase = extraerNombreClase(codigoFuente);
            
            // 🚀 PROMPT TURBOFULURADO - SOLICITANDO MÉTODOS NUEVOS Y MEJORAS
            String prompt = "Eres un arquitecto senior de Java y experto en patrones de diseño. " +
                           "Analiza esta clase Java y proporciona un análisis COMPLETO que incluya:\n\n" +
                           
                           "🎯 **ANÁLISIS DE CÓDIGO ACTUAL:**\n" +
                           "- Code smells detectados\n" +
                           "- Violaciones de principios SOLID\n" +
                           "- Problemas de diseño arquitectónico\n" +
                           "- Oportunidades de optimización\n\n" +
                           
                           "🚀 **PROPUESTAS DE NUEVOS MÉTODOS:**\n" +
                           "- 3-5 métodos nuevos que extiendan la funcionalidad\n" +
                           "- Métodos utilitarios que mejoren usabilidad\n" +
                           "- Métodos de validación y seguridad\n" +
                           "- Métodos de logging y monitoreo\n\n" +
                           
                           "🔧 **ACTUALIZACIONES Y MEJORAS:**\n" +
                           "- Refactorizaciones específicas\n" +
                           "- Implementación de patrones de diseño\n" +
                           "- Mejoras de performance\n" +
                           "- Actualización a mejores prácticas Java modernas\n\n" +
                           
                           "📝 **FORMATO DE RESPUESTA:**\n" +
                           "Usa este formato exacto:\n" +
                           "ANÁLISIS: [tu análisis aquí]\n" +
                           "NUEVOS_MÉTODOS: [lista de métodos propuestos]\n" +
                           "ACTUALIZACIONES: [lista de mejoras específicas]\n" +
                           "CÓDIGO_REFACTORIZADO: [código completo mejorado]\n\n" +
                           
                           "CLASE A ANALIZAR:\n```java\n" + codigoFuente + "\n```";
            
            DeepSeekClient cliente = new DeepSeekClient();
            String respuestaJson = cliente.analyzeCode(prompt);
            
            String respuesta = extraerContenidoDeRespuestaDeepSeek(respuestaJson);
            
            // 🎨 FORMATEAR RESPUESTA PARA MEJOR LEGIBILIDAD
            return formatearRespuestaTurbo(nombreClase, respuesta);
            
        } catch (Exception e) {
            return generarFallbackTurbo(codigoFuente, e);
        }
    }

    /**
     * 🎯 EXTRAER CONTENIDO REAL DE LA RESPUESTA JSON DE DEEPSEEK
     * Convierte la respuesta JSON de la API en texto legible
     */
    private String extraerContenidoDeRespuestaDeepSeek(String respuestaJson) {
        try {
            // Si la respuesta ya es texto plano, devolverla directamente
            if (respuestaJson == null || respuestaJson.trim().isEmpty()) {
                return "❌ Respuesta vacía de DeepSeek API";
            }
            
            // Intentar parsear como JSON
            if (respuestaJson.trim().startsWith("{") && respuestaJson.trim().endsWith("}")) {
                try {
                    JSONObject json = new JSONObject(respuestaJson);
                    
                    // Buscar el contenido en la estructura típica de DeepSeek
                    if (json.has("choices")) {
                        JSONArray choices = json.getJSONArray("choices");
                        if (choices.length() > 0) {
                            JSONObject firstChoice = choices.getJSONObject(0);
                            if (firstChoice.has("message")) {
                                JSONObject message = firstChoice.getJSONObject("message");
                                if (message.has("content")) {
                                    String contenido = message.getString("content");
                                    return contenido != null ? contenido.trim() : "Contenido vacío";
                                }
                            } else if (firstChoice.has("text")) {
                                // Formato alternativo
                                return firstChoice.getString("text").trim();
                            }
                        }
                    }
                    
                    // Si tiene "content" directamente
                    if (json.has("content")) {
                        return json.getString("content").trim();
                    }
                    
                    // Si tiene "text" directamente
                    if (json.has("text")) {
                        return json.getString("text").trim();
                    }
                    
                    // Si no encontramos la estructura esperada, mostrar la respuesta completa
                    bitacora.warn("Estructura JSON no reconocida, devolviendo respuesta completa");
                    return respuestaJson;
                    
                } catch (Exception jsonException) {
                    bitacora.warn("Error parseando JSON, devolviendo respuesta original: " + jsonException.getMessage());
                    return respuestaJson;
                }
            }
            
            // Si no es JSON, devolver la respuesta tal cual
            return respuestaJson;
            
        } catch (Exception e) {
            bitacora.error("Error en extraerContenidoDeRespuestaDeepSeek", e);
            return "❌ Error procesando respuesta DeepSeek: " + e.getMessage() + "\n\nRespuesta original:\n" + respuestaJson;
        }
    }

    /**
     * 🔍 EXTRAER NOMBRE DE CLASE DEL CÓDIGO FUENTE
     * Busca el nombre de la clase en el código Java
     */
    private String extraerNombreClase(String codigoFuente) {
        try {
            if (codigoFuente == null || codigoFuente.trim().isEmpty()) {
                return "ClaseDesconocida";
            }
            
            String[] lineas = codigoFuente.split("\n");
            
            // Buscar patrones de declaración de clase
            for (String linea : lineas) {
                linea = linea.trim();
                
                // Patrón: public class NombreClase
                if (linea.startsWith("public class ") || linea.startsWith("class ")) {
                    String[] partes = linea.split("\\s+");
                    for (int i = 0; i < partes.length; i++) {
                        if (partes[i].equals("class") && i + 1 < partes.length) {
                            String nombreClase = partes[i + 1];
                            
                            // Limpiar caracteres extraños
                            nombreClase = nombreClase.replace("{", "")
                                                    .replace("}", "")
                                                    .replace(";", "")
                                                    .trim();
                            
                            if (!nombreClase.isEmpty()) {
                                return nombreClase;
                            }
                        }
                    }
                }
                
                // Patrón: public final class NombreClase
                if (linea.startsWith("public final class ")) {
                    String temp = linea.replace("public final class ", "").trim();
                    String nombreClase = temp.split("\\s+")[0].replace("{", "").trim();
                    if (!nombreClase.isEmpty()) {
                        return nombreClase;
                    }
                }
                
                // Patrón: public abstract class NombreClase
                if (linea.startsWith("public abstract class ")) {
                    String temp = linea.replace("public abstract class ", "").trim();
                    String nombreClase = temp.split("\\s+")[0].replace("{", "").trim();
                    if (!nombreClase.isEmpty()) {
                        return nombreClase;
                    }
                }
            }
            
            // Si no encontramos, buscar en comentarios o nombres de archivo
            return extraerNombreClaseFallback(codigoFuente);
            
        } catch (Exception e) {
            bitacora.error("Error en extraerNombreClase", e);
            return "ClaseDesconocida";
        }
    }

    /**
     * 🆘 MÉTODO FALLBACK PARA EXTRAER NOMBRE DE CLASE
     * Usa métodos alternativos cuando el principal falla
     */
    private String extraerNombreClaseFallback(String codigoFuente) {
        try {
            // Buscar cualquier línea que contenga "class " seguido de texto
            String[] lineas = codigoFuente.split("\n");
            for (String linea : lineas) {
                if (linea.contains(" class ")) {
                    int classIndex = linea.indexOf(" class ");
                    String afterClass = linea.substring(classIndex + 7).trim();
                    String[] palabras = afterClass.split("\\s+");
                    if (palabras.length > 0) {
                        String posibleNombre = palabras[0].replace("{", "").replace("}", "").trim();
                        if (posibleNombre.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                            return posibleNombre;
                        }
                    }
                }
            }
            
            // Buscar en comentarios de package
            for (String linea : lineas) {
                if (linea.trim().startsWith("package ")) {
                    String packageLine = linea.trim();
                    // Extraer el último segmento del package como posible nombre de clase
                    String[] partes = packageLine.split("\\.");
                    if (partes.length > 0) {
                        String ultimaParte = partes[partes.length - 1]
                            .replace(";", "")
                            .replace("{", "")
                            .trim();
                        if (!ultimaParte.isEmpty()) {
                            return ultimaParte;
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            bitacora.error("Error en extraerNombreClaseFallback", e);
        }
        
        return "ClaseSinNombre";
    }

    // 🎨 MÉTODO PARA FORMATEAR LA RESPUESTA DE MANERA ÉPICA
    private String formatearRespuestaTurbo(String nombreClase, String respuesta) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("🏴‍☠️ **ANÁLISIS TURBOFULURADO DE: ").append(nombreClase).append("**\n\n");
        
        // 🎯 SEPARAR LAS SECCIONES DE LA RESPUESTA
        if (respuesta.contains("ANÁLISIS:")) {
            String analisis = extraerSeccion(respuesta, "ANÁLISIS:", "NUEVOS_MÉTODOS:");
            sb.append("🔍 **ANÁLISIS DETALLADO:**\n").append(analisis).append("\n\n");
        }
        
        if (respuesta.contains("NUEVOS_MÉTODOS:")) {
            String nuevosMetodos = extraerSeccion(respuesta, "NUEVOS_MÉTODOS:", "ACTUALIZACIONES:");
            sb.append("🚀 **MÉTODOS NUEVOS PROPUESTOS:**\n").append(nuevosMetodos).append("\n\n");
        }
        
        if (respuesta.contains("ACTUALIZACIONES:")) {
            String actualizaciones = extraerSeccion(respuesta, "ACTUALIZACIONES:", "CÓDIGO_REFACTORIZADO:");
            sb.append("🔧 **ACTUALIZACIONES RECOMENDADAS:**\n").append(actualizaciones).append("\n\n");
        }
        
        if (respuesta.contains("CÓDIGO_REFACTORIZADO:")) {
            String codigoRefactorizado = respuesta.substring(respuesta.indexOf("CÓDIGO_REFACTORIZADO:") + "CÓDIGO_REFACTORIZADO:".length());
            sb.append("💎 **CÓDIGO TURBOFULURADO:**\n```java\n").append(codigoRefactorizado.trim()).append("\n```");
        } else {
            sb.append(respuesta);
        }
        
        return sb.toString();
    }

    // 📝 MÉTODO AUXILIAR PARA EXTRAER SECCIONES
    private String extraerSeccion(String texto, String inicio, String fin) {
        int startIdx = texto.indexOf(inicio) + inicio.length();
        int endIdx = texto.indexOf(fin, startIdx);
        if (endIdx == -1) endIdx = texto.length();
        return texto.substring(startIdx, endIdx).trim();
    }

    // 🆘 FALLBACK TURBO-MEJORADO
    private String generarFallbackTurbo(String codigoFuente, Exception e) {
        String nombreClase = extraerNombreClase(codigoFuente);
        
        return "🏴‍☠️ **ANÁLISIS TURBOFULURADO DE: " + nombreClase + "**\n\n" +
               "🔍 **ANÁLISIS DETALLADO:**\n" +
               "❌ Error de conexión con DeepSeek: " + e.getMessage() + "\n\n" +
               "🚀 **MÉTODOS NUEVOS PROPUESTOS (FALLBACK):**\n" +
               "• `public void validarIntegridad()` - Validación completa del estado\n" +
               "• `public Map<String, Object> obtenerMetricas()` - Métricas de performance\n" +
               "• `public void ejecutarBackupAutomatico()` - Sistema de respaldo\n" +
               "• `public boolean verificarDependencias()` - Validación de dependencias\n\n" +
               "🔧 **ACTUALIZACIONES RECOMENDADAS:**\n" +
               "• Implementar manejo robusto de excepciones\n" +
               "• Agregar logging estructurado\n" +
               "• Mejorar validación de parámetros\n" +
               "• Implementar patron Observer para eventos\n\n" +
               "💎 **SISTEMA OPERATIVO - EJECUTA 'deepseek-fix' PARA REPARAR CONEXIÓN**";
    }
    
}