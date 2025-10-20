package com.elreinodelolvido.ellibertad.api;

import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.util.FileUtils;
import com.google.gson.Gson;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UnknownFormatConversionException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 🧠 Oráculo Técnico TURBO ULTRA — Grumete profético del Libertad
 * 🚀 Lanza prompts al DeepSeek y devuelve sabiduría refactorizada
 * 🛡️ Versión blindada con logs, cache inteligente y validaciones épicas
 * ✅ REPARADO COMPLETO: Sin límites de tokens, timeouts extendidos, validación mejorada
 */
public class OraculoTecnico {

    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String API_KEY = System.getenv("DEEPSEEK_API_KEY");
    private static final String DEFAULT_MODEL = "deepseek-chat";
    
    // 🚀 TIMEOUTS EXTENDIDOS - PARA CÓDIGO COMPLETO
    private static final int CONNECT_TIMEOUT = 45000;  // 45 segundos
    private static final int READ_TIMEOUT = 90000;     // 90 segundos
    
    private final PromptCache cache;
    private final String modelo;
    private final double temperatura;
    private final Gson gson = new Gson();
	private Object bitacora;

    public OraculoTecnico(String modelo, double temperatura, double fallbackTemp) {
        this.modelo = modelo != null ? modelo : DEFAULT_MODEL;
        this.cache = new PromptCache(50);
        this.temperatura = temperatura;
    }

    /**
     * 🎯 MÉTODO DE DIAGNÓSTICO API - MEJORADO
     */
    public void diagnosticarAPI() {
        System.out.println("\n🔍 DIAGNÓSTICO ORÁCULO TÉCNICO:");
        System.out.println("📡 API Key: " + (API_KEY != null ? "✅ Configurada" : "❌ Faltante"));
        System.out.println("🌐 URL: " + API_URL);
        System.out.println("⏰ Timeouts: Connect=" + CONNECT_TIMEOUT + "ms, Read=" + READ_TIMEOUT + "ms");
        System.out.println("🤖 Modelo: " + modelo);
        System.out.println("🔥 Temperatura: " + temperatura);
        System.out.println("💾 Cache: " + (cache != null ? "✅ Activo" : "❌ Inactivo"));
        
        // Test de conexión simple
        try {
            String test = enviar("Responde 'OK' si funciona", 0.1, 
                "Responde siempre exactamente 'OK' sin explicaciones adicionales");
            boolean funciona = test != null && test.contains("OK");
            System.out.println("🧪 Test API: " + (funciona ? "✅ FUNCIONA" : "❌ FALLÓ"));
            if (test != null) {
                System.out.println("📝 Respuesta test: '" + test + "'");
            }
        } catch (Exception e) {
            System.out.println("💥 Error en test: " + e.getMessage());
        }
    }

    public String enviarPromptTecnico(String prompt) {
        log("🔧 Enviando prompt técnico (" + prompt.length() + " chars)...");
        return enviar(prompt, 0.3, "Eres un experto en refactorización de código Java, análisis de arquitectura, patrones de diseño y mejores prácticas. Sé preciso, conciso y orientado a producción.");
    }

    public String enviarPromptNarrativo(String prompt) {
        log("🎭 Enviando prompt narrativo (" + prompt.length() + " chars)...");
        return enviar(prompt, 0.9, "Eres un narrador épico de historias piratas, lleno de metáforas náuticas, humor y dramatismo literario. Usa lenguaje colorido pero mantén la coherencia.");
    }
    
    private String enviar(String prompt, double temp, String systemPrompt) {
        // 🚀 TURBOMODE: Cache check primero
        String cacheKey = prompt + "|" + temp + "|" + systemPrompt.hashCode();
        String cached = cache != null ? cache.obtener(cacheKey) : null;
        if (cached != null) {
            log("⚡ TURBO HIT! Respuesta desde cache (" + cached.length() + " chars)");
            return cached;
        }

        // 🔄 RETRY MECHANISM MEJORADO
        int maxRetries = 3;
        int[] retryDelays = {3000, 6000, 10000};
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                log("🔄 Intento " + attempt + "/" + maxRetries + " - Prompt: " + prompt.length() + " chars");
                
                if (attempt > 1) {
                    int delay = retryDelays[attempt - 2];
                    log("⏳ Esperando " + delay + "ms antes de reintentar...");
                    Thread.sleep(delay);
                }
                
                // 🚀 Validación express
                if (API_KEY == null || API_KEY.isBlank()) {
                    throw new IllegalStateException("❌ DEEPSEEK_API_KEY missing");
                }

                // ✅ PAYLOAD SIN LÍMITES DE TOKENS - ELIMINADO MAX_TOKENS
                Map<String, Object> payloadMap = new LinkedHashMap<>();
                payloadMap.put("model", modelo);
                payloadMap.put("temperature", temp);
                // 🚀 ELIMINADO: max_tokens - El modelo puede generar respuestas de cualquier longitud
                payloadMap.put("messages", List.of(
                    Map.of("role", "system", "content", systemPrompt),
                    Map.of("role", "user", "content", prompt)
                ));

                String payload = gson.toJson(payloadMap);
                log("📤 Payload JSON: " + payload.length() + " chars");

                // 🚀 CONEXIÓN CON TIMEOUTS EXTENDIDOS
                HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setDoOutput(true);

                // 🚀 Envío
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload.getBytes(StandardCharsets.UTF_8));
                }

                // 🚀 Respuesta
                int responseCode = conn.getResponseCode();
                log("📡 HTTP Response: " + responseCode);
                
                if (responseCode == 429) { // Rate limit
                    log("🚫 Rate limit alcanzado, reintentando...");
                    continue;
                }
                
                if (responseCode != 200) {
                    String errorBody = mostrarErrorBody(conn);
                    log("❌ DeepSeek HTTP " + responseCode + " - Body length: " + errorBody.length() + " chars");
                    
                    if (responseCode >= 500) { // Server error, retry
                        continue;
                    }
                    
                    return "❌ Error API: HTTP " + responseCode;
                }

                // ✅ LECTURA ROBUSTA MEJORADA
                String json = leerRespuesta(conn);
                if (json == null || json.trim().isEmpty() || json.length() < 10) {
                    log("⚠️ Respuesta JSON muy corta o vacía");
                    continue;
                }

                log("📥 Respuesta JSON: " + json.length() + " chars");
                String respuesta = extraerRespuesta(json);

                // ✅ VALIDACIÓN MEJORADA DE CÓDIGO COMPLETO
                if (respuesta == null || respuesta.trim().isEmpty()) {
                    log("⚠️ Respuesta extraída vacía o nula");
                    continue;
                }
                
                // 🎯 VERIFICAR ESTRUCTURA COMPLETA DEL CÓDIGO
                boolean tienePackage = respuesta.contains("package ");
                boolean tieneClass = respuesta.contains("class ");
                boolean tieneLlaveApertura = respuesta.contains("{");
                boolean tieneLlaveCierre = respuesta.contains("}");
                int totalLlaves = contarLlaves(respuesta);
                boolean llavesBalanceadas = totalLlaves > 0 && totalLlaves % 2 == 0;
                
                boolean codigoCompleto = tienePackage && tieneClass && 
                                       tieneLlaveApertura && tieneLlaveCierre && 
                                       llavesBalanceadas;

                if (!codigoCompleto) {
                    log("⚠️ Código incompleto - Estructura faltante:");
                    log("   Package: " + tienePackage + ", Class: " + tieneClass);
                    log("   Llaves { : " + tieneLlaveApertura + ", } : " + tieneLlaveCierre);
                    log("   Total llaves: " + totalLlaves + ", Balanceadas: " + llavesBalanceadas);
                    log("   Longitud respuesta: " + respuesta.length() + " chars");
                    continue;
                }

                if (respuesta.startsWith("⚠️") || respuesta.startsWith("❌")) {
                    log("⚠️ Respuesta con error: " + respuesta);
                    continue;
                }

                log("✅ TURBO RESPONSE COMPLETO! " + respuesta.length() + " chars");
                
                // 🎯 DEBUG: Mostrar preview de respuesta
                if (respuesta.length() > 150) {
                    log("🔍 Preview respuesta: " + respuesta.substring(0, 150) + "...");
                } else {
                    log("🔍 Respuesta completa: " + respuesta);
                }

                // 🚀 Cache inmediato
                if (cache != null && !respuesta.startsWith("❌") && !respuesta.startsWith("⚠️")) {
                    cache.guardar(cacheKey, respuesta, systemPrompt.contains("narrador") ? "narrativo" : "tecnico");
                }

                return respuesta;

            } catch (UnknownFormatConversionException e) {
                error("💥 ERROR DE FORMATO: " + e.getMessage());
                if (attempt == maxRetries) {
                    return "❌ Error de formato interno";
                }
            } catch (Exception e) {
                error("💥 Intento " + attempt + " fallido: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                if (attempt == maxRetries) {
                    return "❌ Oráculo falló después de " + maxRetries + " intentos";
                }
            }
        }
        
        return "❌ Todos los intentos fallaron";
    }

    /**
     * 🎯 CONTAR LLAVES PARA VERIFICAR CÓDIGO BALANCEADO
     */
    private int contarLlaves(String codigo) {
        if (codigo == null) return 0;
        int count = 0;
        for (char c : codigo.toCharArray()) {
            if (c == '{' || c == '}') count++;
        }
        return count;
    }

    private String extraerRespuesta(String json) {
        try {
            log("🎯 INICIANDO EXTRACCIÓN MEJORADA");
            log("📦 JSON recibido: " + json.length() + " caracteres");
            
            // 🚀 GUARDAR PARA DEBUG
            FileUtils.writeToFile("autogen-output/debug/deepseek-raw-response.json", json);
            
            // 🎯 ESTRATEGIA 1: Formato Markdown con JSON (NUEVO - PRIORITARIO)
            String contenidoMarkdown = extraerDeMarkdownConJson(json);
            if (contenidoMarkdown != null && contenidoMarkdown.length() > 100) {
                log("✅ Estrategia Markdown exitosa: " + contenidoMarkdown.length() + " chars");
                
                // 🎯 VERIFICAR SI ES SOLO JSON O MARKDOWN COMPLETO
                if (contenidoMarkdown.trim().startsWith("{")) {
                    log("📊 Contenido es JSON puro");
                    return contenidoMarkdown;
                } else {
                    log("📝 Contenido es Markdown con JSON");
                    return contenidoMarkdown; // Devolver todo el markdown
                }
            }
            
            // 🎯 ESTRATEGIA 2: Gson (existente)
            try {
                Map<String, Object> jsonMap = gson.fromJson(json, Map.class);
                String contenidoGson = extraerContenidoConGson(jsonMap);
                if (contenidoGson != null && contenidoGson.length() > 10) {
                    log("✅ Estrategia Gson exitosa: " + contenidoGson.length() + " chars");
                    return contenidoGson;
                }
            } catch (Exception e) {
                log("⚠️ Estrategia Gson falló: " + e.getMessage());
            }
            
            // 🎯 ESTRATEGIA 3: Choices (existente)
            String contenidoChoices = extraerDeChoices(json);
            if (contenidoChoices != null && contenidoChoices.length() > 10) {
                log("✅ Estrategia Choices exitosa: " + contenidoChoices.length() + " chars");
                return contenidoChoices;
            }
            
            // 🎯 ESTRATEGIA 4: Búsqueda directa (existente)
            String contenidoDirecto = extraerContenidoDirecto(json);
            if (contenidoDirecto != null && contenidoDirecto.length() > 10) {
                log("✅ Estrategia directa exitosa: " + contenidoDirecto.length() + " chars");
                return contenidoDirecto;
            }
            
            // ❌ SI TODO FALLA
            log("❌ Todas las estrategias fallaron");
            return "⚠️ No se pudo extraer contenido válido";
            
        } catch (Exception e) {
            error("💥 Error en extraerRespuesta: " + e.getMessage());
            return "❌ Error: " + e.getMessage();
        }
    }

    /**
     * 🎯 ESTRATEGIA 1: Extraer usando Gson con estructura completa
     */
    private String extraerContenidoConGson(Map<String, Object> jsonMap) {
        try {
            // 🎯 Estructura: choices[0].message.content
            if (jsonMap.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) jsonMap.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    if (firstChoice.containsKey("message")) {
                        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                        if (message.containsKey("content")) {
                            String content = (String) message.get("content");
                            return content != null ? content.trim() : null;
                        }
                    }
                }
            }
            
            // 🎯 Estructura alternativa: data[0].content
            if (jsonMap.containsKey("data")) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) jsonMap.get("data");
                if (data != null && !data.isEmpty()) {
                    Map<String, Object> firstData = data.get(0);
                    if (firstData.containsKey("content")) {
                        String content = (String) firstData.get("content");
                        return content != null ? content.trim() : null;
                    }
                }
            }
            
            // 🎯 Estructura simple: content directo
            if (jsonMap.containsKey("content")) {
                String content = (String) jsonMap.get("content");
                return content != null ? content.trim() : null;
            }
            
        } catch (Exception e) {
            log("⚠️ Error en extracción Gson: " + e.getMessage());
        }
        return null;
    }

    /**
     * 🎯 ESTRATEGIA 2: Extraer de estructura choices con búsqueda de texto
     */
    private String extraerDeChoices(String json) {
        try {
            // Buscar el patrón: "content": "texto aquí"
            // Manejar múltiples variaciones de formato
            String[] patterns = {
                "\"content\":\"(.*?)\"",
                "\"content\": \"(.*?)\"",
                "'content':'([^']*)'",
                "'content': '([^']*)'"
            };
            
            for (String pattern : patterns) {
                java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern, java.util.regex.Pattern.DOTALL);
                java.util.regex.Matcher matcher = regex.matcher(json);
                
                if (matcher.find()) {
                    String content = matcher.group(1);
                    if (content != null && !content.trim().isEmpty()) {
                        // 🎯 DESESCAPAR CARACTERES
                        content = content
                            .replace("\\n", "\n")
                            .replace("\\r", "\r")
                            .replace("\\t", "\t")
                            .replace("\\\"", "\"")
                            .replace("\\\\", "\\")
                            .replace("\\/", "/")
                            .replace("\\b", "\b")
                            .replace("\\f", "\f");
                        
                        return content.trim();
                    }
                }
            }
            
        } catch (Exception e) {
            log("⚠️ Error en extracción Choices: " + e.getMessage());
        }
        return null;
    }

    /**
     * 🎯 ESTRATEGIA 3: Búsqueda directa con múltiples enfoques
     */
    private String extraerContenidoDirecto(String json) {
        try {
            // 🎯 ENFOQUE A: Buscar después de "content": hasta el siguiente "
            int contentIndex = json.indexOf("\"content\":");
            if (contentIndex == -1) {
                contentIndex = json.indexOf("'content':");
            }
            
            if (contentIndex != -1) {
                // Avanzar más allá de la clave
                int valueStart = json.indexOf(":", contentIndex) + 1;
                
                // Buscar el inicio del valor (puede ser comillas o espacio)
                while (valueStart < json.length() && 
                      (json.charAt(valueStart) == ' ' || json.charAt(valueStart) == '\n' || json.charAt(valueStart) == '\t')) {
                    valueStart++;
                }
                
                // Determinar el delimitador (comilla simple o doble)
                char quoteChar = json.charAt(valueStart);
                if (quoteChar == '"' || quoteChar == '\'') {
                    valueStart++; // Saltar la comilla de apertura
                    int valueEnd = json.indexOf(quoteChar, valueStart);
                    
                    if (valueEnd > valueStart) {
                        String content = json.substring(valueStart, valueEnd);
                        
                        // 🎯 DESESCAPAR
                        content = desescaparJson(content);
                        
                        if (content.length() > 5) { // Mínimo razonable
                            return content;
                        }
                    }
                }
            }
            
            // 🎯 ENFOQUE B: Buscar cualquier texto entre comillas después de content
            String[] contentMarkers = {"\"content\"", "'content'"};
            for (String marker : contentMarkers) {
                int markerPos = json.indexOf(marker);
                if (markerPos != -1) {
                    int valueStart = markerPos + marker.length();
                    // Buscar los dos puntos siguientes
                    int colonPos = json.indexOf(":", valueStart);
                    if (colonPos != -1) {
                        // Buscar la primera comilla después de los dos puntos
                        int quotePos = json.indexOf("\"", colonPos);
                        if (quotePos == -1) quotePos = json.indexOf("'", colonPos);
                        
                        if (quotePos != -1) {
                            int quoteEnd = json.indexOf(json.charAt(quotePos), quotePos + 1);
                            if (quoteEnd > quotePos) {
                                String content = json.substring(quotePos + 1, quoteEnd);
                                content = desescaparJson(content);
                                if (content.length() > 5) {
                                    return content;
                                }
                            }
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            log("⚠️ Error en extracción directa: " + e.getMessage());
        }
        return null;
    }

    /**
     * 🛠️ DESESCAPAR CADENAS JSON
     */
    private String desescaparJson(String text) {
        if (text == null) return "";
        
        return text
            .replace("\\\\", "\\")  // Primero las barras invertidas
            .replace("\\n", "\n")
            .replace("\\r", "\r") 
            .replace("\\t", "\t")
            .replace("\\\"", "\"")
            .replace("\\'", "'")
            .replace("\\/", "/")
            .replace("\\b", "\b")
            .replace("\\f", "\f")
            .replace("\\u0026", "&")  // Unicode común
            .replace("\\u003c", "<")
            .replace("\\u003e", ">");
    }
    
    public String invocarTecnico(String codigoOriginal) {
        try {
            // 🛡️ SANITIZAR PRIMERO - Eliminar formatos problemáticos
            String codigoSanitizado = sanitizarCodigo(codigoOriginal);
            
            // 🎯 USAR CÓDIGO COMPLETO - SIN TRUNCAMIENTO
            String codigoParaAnalizar = codigoSanitizado;
            
            // 🚀 PROMPT MEJORADO - ÉNFASIS EN COMPLETITUD
            String prompt = construirPromptTecnico(codigoParaAnalizar);
            return enviarPromptTecnico(prompt);
            
        } catch (Exception e) {
            error("Error en invocarTecnico: " + e.getMessage());
            return "Error procesando código";
        }
    }

    private String sanitizarCodigo(String codigo) {
        if (codigo == null || codigo.isEmpty()) {
            return codigo;
        }
        
        String sanitizado = codigo
            .replace("%V", "PERCENT_V")
            .replace("%v", "PERCENT_v")
            .replace("%%V", "%%PERCENT_V")
            .replace("%%v", "%%PERCENT_v");
        
        boolean huboCambios = !sanitizado.equals(codigo);
        
        if (huboCambios) {
            sanitizado = "// 🔧 CÓDIGO SANITIZADO: Formatos %V/%v reemplazados por seguridad\n" + 
                        "// ⚠️ Esto evita errores de String.format durante el procesamiento\n" + 
                        sanitizado;
            log("🛡️ Código sanitizado: formatos %V/%v reemplazados");
        }
        
        return sanitizado;
    }

    private String construirPromptTecnico(String codigo) {
        return "REFACTORIZACIÓN COMPLETA - GENERA CÓDIGO JAVA 100% VÁLIDO Y COMPLETO\n\n" +
               "REQUISITOS ESTRICTOS (NO OMITIR NADA):\n" +
               "1. ✅ MANTENER EXACTAMENTE el mismo package y nombre de clase\n" +
               "2. ✅ INCLUIR TODOS los imports necesarios\n" +  
               "3. ✅ GENERAR código COMPLETO y COMPILABLE (con todas las llaves {})\n" +
               "4. ✅ CONSERVAR TODA la funcionalidad original\n" +
               "5. ✅ MEJORAR estructura, nombres, eficiencia\n" +
               "6. ✅ NO DEJAR MÉTODOS INCOMPLETOS\n" +
               "7. ✅ CERRAR TODAS LAS LLAVES {} CORRECTAMENTE\n\n" +
               "IMPORTANTE: El código DEBE compilar. No omitir métodos, no dejar llaves sin cerrar.\n\n" +
               "FORMATO OBLIGATORIO:\n" +
               "```java\n" +
               "[PACKAGE COMPLETO]\n" +
               "[IMPORTS COMPLETOS]\n" +
               "[CLASE COMPLETA CON TODOS LOS MÉTODOS Y LLAVES]\n" +
               "```\n\n" +
               "EJEMPLO DE RESPUESTA CORRECTA:\n" +
               "```java\n" +
               "package com.ejemplo.util;\n" +
               "import java.util.*;\n\n" +
               "public class MiClase {\n" +
               "    private final String nombre;\n\n" +
               "    public MiClase(String nombre) {\n" +
               "        this.nombre = nombre;\n" +
               "    }\n\n" +
               "    public void ejecutar() {\n" +
               "        // implementación completa\n" +
               "    }\n" +
               "}\n" +
               "```\n\n" +
               "CLASE ORIGINAL:\n" +
               "```java\n" +
               codigo + "\n" +
               "```\n\n" +
               "COMIENZA EL CÓDIGO REFACTORIZADO COMPLETO:";
    }

    public String analisisRapido(String codigo) {
        String prompt = "Análisis rápido de código Java. Responde en máximo 10 líneas:\n\n" +
                       codigo + "\n\n" +
                       "Analiza: estructura, problemas potenciales, sugerencias rápidas.";
        
        return enviar(prompt, 0.2, "Eres un analista de código Java rápido y directo. Responde solo con los puntos más importantes.");
    }

    /**
     * 📖 LEER RESPUESTA MEJORADO - Con más información de debug
     */
    private String leerRespuesta(HttpURLConnection conn) {
        try {
            // 🎯 LEER TODOS LOS DATOS DISPONIBLES
            java.io.InputStream inputStream = conn.getInputStream();
            java.io.ByteArrayOutputStream result = new java.io.ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            
            String json = result.toString(StandardCharsets.UTF_8.name());
            
            if (json == null || json.trim().isEmpty() || json.length() < 10) {
                error("⚠️ Respuesta JSON muy corta: " + (json != null ? json.length() : "null"));
                
                // 🎯 INTENTAR LEER EL ERROR STREAM SI HAY PROBLEMA
                if (conn.getErrorStream() != null) {
                    java.io.InputStream errorStream = conn.getErrorStream();
                    java.io.ByteArrayOutputStream errorResult = new java.io.ByteArrayOutputStream();
                    while ((length = errorStream.read(buffer)) != -1) {
                        errorResult.write(buffer, 0, length);
                    }
                    String errorBody = errorResult.toString(StandardCharsets.UTF_8.name());
                    log("📋 Error stream: " + errorBody);
                }
                
                return null;
            }
            
            log("📥 JSON crudo leído: " + json.length() + " caracteres");
         // 🚀 VERSIÓN TURBO: MOSTRAR JSON COMPLETO
            log("📋 JSON COMPLETO (" + json.length() + " chars):");
            log("═".repeat(80));
            log(json);  // ¡ESTO MUESTRA EL JSON COMPLETO!
            log("═".repeat(80));
            
            return json;
            
        } catch (Exception e) {
            error("❌ Error leyendo respuesta: " + e.getMessage());
            
            // 🎯 INTENTAR LEER ERROR STREAM EN CASO DE EXCEPCIÓN
            try {
                if (conn.getErrorStream() != null) {
                    java.util.Scanner errorScanner = new java.util.Scanner(conn.getErrorStream(), StandardCharsets.UTF_8);
                    if (errorScanner.hasNext()) {
                        String errorBody = errorScanner.useDelimiter("\\A").next();
                        log("📋 Error stream en excepción: " + errorBody);
                    }
                    errorScanner.close();
                }
            } catch (Exception ex) {
                // Ignorar errores secundarios
            }
            
            return null;
        }
    }
    
    /**
     * 🎯 ESTRATEGIA 4: Extraer contenido de formato Markdown con bloques de código
     */
    private String extraerDeMarkdownConJson(String json) {
        try {
            log("🔍 Buscando formato Markdown con JSON...");
            
            // 🎯 PATRONES PARA BLOQUES DE CÓDIGO MARKDOWN
            String[] codeBlockPatterns = {
                "```json\\s*(.*?)\\s*```",  // ```json {content} ```
                "```\\s*(.*?)\\s*```",      // ``` {content} ```
                "`{3,}.*?\\n(.*?)`{3,}"     // Múltiples backticks
            };
            
            for (String pattern : codeBlockPatterns) {
                java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern, java.util.regex.Pattern.DOTALL);
                java.util.regex.Matcher matcher = regex.matcher(json);
                
                if (matcher.find()) {
                    String codeContent = matcher.group(1);
                    log("✅ Encontrado bloque de código: " + codeContent.length() + " chars");
                    
                    // 🎯 VERIFICAR SI ES JSON VÁLIDO
                    if (codeContent.trim().startsWith("{") && codeContent.contains("plan_actualizacion")) {
                        log("🎯 JSON detectado en bloque de código");
                        return codeContent.trim();
                    }
                }
            }
            
            // 🎯 SI NO HAY BLOQUES DE CÓDIGO, BUSCAR EL CONTENIDO MARKDOWN COMPLETO
            if (json.contains("# ") && json.contains("## ")) {
                log("📝 Formato Markdown detectado, extrayendo contenido completo");
                return json; // Devolver todo el markdown
            }
            
        } catch (Exception e) {
            log("⚠️ Error en extracción Markdown: " + e.getMessage());
        }
        return null;
    }

    private String mostrarErrorBody(HttpURLConnection conn) {
        String errorBody = "No error body";
        try {
            Scanner errorScanner = new Scanner(conn.getErrorStream(), StandardCharsets.UTF_8);
            if (errorScanner.hasNext()) {
                errorBody = errorScanner.useDelimiter("\\A").next();
                error("❌ Error Body: " + errorBody);
            }
            errorScanner.close();
        } catch (Exception e) {
            // Ignorar
        }
        return errorBody;
    }

    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
    
    // Método para obtener estadísticas del cache
    public void mostrarEstadisticasCache() {
        System.out.println("\n📊 Estadísticas del Cache:");
        Map<String, Object> stats = cache.obtenerEstadisticas();
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }

    private void log(String msg) {
        System.out.println("[Oráculo Técnico] " + msg);
    }

    private void error(String msg) {
        System.err.println("[Oráculo Técnico] " + msg);
    }

    public String invocar(String prompt, String contexto, double temperatura) {
        Bitacora.info("[Oráculo] Invocando con contexto: " + contexto + ", temp: " + temperatura);
        
        try {
            // 🎯 Determinar system prompt basado en contexto
            String systemPrompt;
            switch (contexto) {
                case "fusor_plan_autonomo":
                    systemPrompt = "Eres un arquitecto de software experto en análisis de debates técnicos y generación de planes de desarrollo. " +
                                 "Genera planes concretos, factibles y bien estructurados con pasos específicos de implementación.";
                    break;
                case "refactor_tecnico":
                    systemPrompt = "Eres un ingeniero de software senior especializado en refactorización Java. " +
                                 "Mantén las APIs públicas, mejora la estructura interna y asegura la compatibilidad.";
                    break;
                case "analisis_rapido":
                    systemPrompt = "Eres un analista técnico conciso y directo. Proporciona insights rápidos y prácticos.";
                    break;
                default:
                    systemPrompt = "Eres un asistente técnico experto en desarrollo Java, arquitectura de software y mejores prácticas.";
            }

            // 🚀 Usar el método enviar existente
            String respuesta = enviar(prompt, temperatura, systemPrompt);
            
            // 📊 Log de métricas
            if (respuesta != null && !respuesta.startsWith("❌")) {
                Bitacora.info("[Oráculo] ✅ Respuesta exitosa: " + respuesta.length() + " caracteres");
            } else {
                Bitacora.warn("[Oráculo] ⚠️ Respuesta con problemas: " + 
                             (respuesta != null ? respuesta.substring(0, Math.min(100, respuesta.length())) : "null"));
            }
            
            return respuesta;
            
        } catch (Exception e) {
            Bitacora.error("[Oráculo] 💥 Error en invocación: " + e.getMessage(), e);
            return "❌ Error del Oráculo: " + e.getMessage();
        }
    }
}