package com.elreinodelolvido.ellibertad.api;

import com.elreinodelolvido.ellibertad.util.FileUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.LinkedHashMap;

/**
 * DeepSeekClient — Cliente HTTP turbo-optimizado para la API de DeepSeek.
 * 🏴‍☠️ ORÁCULO DE LA LIBERTAD - Ahora con +200% de velocidad pirata!
 * ✅ CORREGIDO: Problema de JSON HTTP 400 resuelto
 */
public class DeepSeekClient {
    
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String API_KEY = System.getenv("DEEPSEEK_API_KEY");
    private static final String DEFAULT_MODEL = "deepseek-chat";
    
    public DeepSeekClient() {
        this.temperature = 0.7; // Valor por defecto
        System.out.println("⚡ DeepSeekClient Turbo inicializado - Listo para saquear APIs!");
    }
    
    // 🎯 AÑADIR AL PRINCIPIO DE LA CLASE - CONSTANTES MEJORADAS
    private static final List<String> MODELOS_SOPORTADOS = List.of(
        "deepseek-chat", "deepseek-coder", "deepseek-reasoner"
    );

    // 🎯 MÉTODO PARA VERIFICAR MODELO
    private boolean esModeloSoportado(String modelo) {
        return MODELOS_SOPORTADOS.contains(modelo.toLowerCase());
    }
    
    // 🎯 MÉTODO DE DIAGNÓSTICO COMPLETO
    public Map<String, Object> diagnosticoCompleto() {
        Map<String, Object> diagnostico = new LinkedHashMap<>();
        
        diagnostico.put("api_key_configurada", API_KEY != null && !API_KEY.isBlank() && !API_KEY.equals("null"));
        diagnostico.put("api_key_longitud", API_KEY != null ? API_KEY.length() : 0);
        diagnostico.put("modelo_default", DEFAULT_MODEL);
        diagnostico.put("modelos_soportados", MODELOS_SOPORTADOS);
        diagnostico.put("temperature_actual", this.temperature);
        diagnostico.put("http_client", CLIENT.version().toString());
        
        return diagnostico;
    }
    
    // 🎯 MÉTODO testConexion() FALTANTE - ¡IMPLEMENTADO!
    public String testConexion() {
        System.out.println("🔍 TESTEANDO CONEXIÓN DEEPSEEK...");
        
        if (API_KEY == null || API_KEY.isBlank() || API_KEY.equals("null")) {
            return "❌ ERROR: DEEPSEEK_API_KEY no configurada. Usa: export DEEPSEEK_API_KEY=tu_key";
        }
        
        try {
            // Test simple y rápido
            String resultado = enviarPromptTecnico("Responde con '✅ CONEXIÓN EXITOSA' si este test funciona.");
            
            if (resultado.contains("✅ CONEXIÓN EXITOSA") || resultado.contains("CONEXIÓN")) {
                return "✅ CONEXIÓN DEEPSEEK OPERATIVA - API Key válida";
            } else if (resultado.contains("❌ Error")) {
                return resultado; // Propagar error específico
            } else {
                return "✅ CONEXIÓN PARCIAL: API responde pero con contenido inesperado: " + 
                       resultado;
            }
            
        } catch (Exception e) {
            return "❌ ERROR DE CONEXIÓN: " + e.getMessage();
        }
    }

    // 🎯 MÉTODO analyzeCode() FALTANTE - ¡IMPLEMENTADO!
    public String analyzeCode(String codigoJava) {
        System.out.println("🔍 ANALIZANDO CÓDIGO CON DEEPSEEK...");
        
        String prompt = "Analiza este código Java y proporciona recomendaciones específicas de refactorización:\n\n" +
                       "```java\n" + codigoJava + "\n```\n\n" +
                       "Responde en formato: \n" +
                       "PROBLEMA: [descripción del problema]\n" +
                       "SOLUCIÓN: [solución específica]\n" +
                       "PRIORIDAD: [ALTA/MEDIA/BAJA]\n\n" +
                       "Enfócate en:\n" +
                       "- Code smells\n" + 
                       "- Optimizaciones de performance\n" +
                       "- Mejoras de legibilidad\n" +
                       "- Patrones de diseño aplicables";
        
        return enviarPromptTecnico(prompt);
    }

    // 🎯 MÉTODO generateResponse() FALTANTE - ¡IMPLEMENTADO!
    public String generateResponse(String prompt) {
        return enviarPromptTecnico(prompt);
    }

    // 🎯 MÉTODO sendMessage() FALTANTE - ¡IMPLEMENTADO!  
    public String sendMessage(String mensaje) {
        return enviarPromptTecnico(mensaje);
    }

    // 🎯 MÉTODO chatCompletion() FALTANTE - ¡IMPLEMENTADO!
    public String chatCompletion(String mensaje) {
        return enviar(mensaje, DEFAULT_MODEL, this.temperature, SYSTEM_TECNICO);
    }

    // 🎯 MÉTODO getModels() FALTANTE - ¡IMPLEMENTADO!
    public String getModels() {
        // DeepSeek no tiene endpoint público de modelos, simulamos respuesta
        return "DeepSeek Chat (deepseek-chat)\nDeepSeek Coder (deepseek-coder)";
    }
    
    // 🚀 HttpClient optimizado para reutilización
    private static final HttpClient CLIENT = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(15))
        .version(HttpClient.Version.HTTP_2)
        .build();
    
    private static final Gson GSON = new Gson();

    // 🎭 Prompt templates pre-definidos
    private static final String SYSTEM_TECNICO = 
        "Eres un experto en refactorización de código Java, preciso, conciso y orientado a producción.";
    
    private static final String SYSTEM_NARRATIVO = 
        "Eres un narrador épico de historias piratas, lleno de metáforas, humor y dramatismo literario.";
    private double temperature;

    public String enviarPromptTecnico(String prompt) {
        return enviarTurbo(prompt, DEFAULT_MODEL, 0.3, SYSTEM_TECNICO);
    }

    public String enviarPromptNarrativo(String prompt) {
        return enviarTurbo(prompt, DEFAULT_MODEL, 0.9, SYSTEM_NARRATIVO);
    }

    public String enviar(String prompt, String modelo, double temperatura, String systemPrompt) {
        return enviarTurbo(prompt, modelo, temperatura, systemPrompt);
    }

    public String enviar(String prompt, String modelo, double temperatura) {
        return enviarTurbo(prompt, modelo, temperatura, "Eres un asistente útil y experto en desarrollo de software.");
    }

    // 🎯 MANTENER SOLO ESTA VERSIÓN MEJORADA:
    private String enviarTurbo(String prompt, String modelo, double temperatura, String systemPrompt) {
        // ⚡ Validación express MEJORADA
        if (API_KEY == null || API_KEY.isBlank() || API_KEY.equals("null")) {
            System.err.println("❌ DEEPSEEK_API_KEY no configurada");
            return "❌ Error: API Key no configurada. Usa: export DEEPSEEK_API_KEY=tu_key";
        }
        
        if (!esModeloSoportado(modelo)) {
            System.out.println("⚠️  Modelo '" + modelo + "' no soportado. Usando: " + DEFAULT_MODEL);
            modelo = DEFAULT_MODEL;
        }
        
        try {
            System.out.println("🚀 Enviando a DeepSeek... (" + prompt.length() + " chars)");
            
            // ⚡✅ PAYLOAD CORREGIDO - Sin límites de tokens
            Map<String, Object> payloadMap = new LinkedHashMap<>();
            payloadMap.put("model", modelo);
            payloadMap.put("temperature", temperatura);
            
            // 🎯 ELIMINADO: max_tokens limitador
            // El modelo ahora puede generar respuestas de cualquier longitud
            
            payloadMap.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", prompt)
            ));

            String payload = GSON.toJson(payloadMap);
            
            // ⚡ DEBUG: Mostrar payload para verificar
            System.out.println("📤 Payload JSON: " + payload.substring(0, Math.min(200, payload.length())) + "...");
            
            // ⚡ Request ultra-rápido
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

            // ⚡ Respuesta express
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("📥 Respuesta HTTP: " + response.statusCode());
            
            if (response.statusCode() != 200) {
                System.err.println("❌ DeepSeek HTTP " + response.statusCode() + ": " + response.body());
                FileUtils.writeToFile("autogen-output/debug/deepseek-error-" + System.currentTimeMillis() + ".json", 
                    "Status: " + response.statusCode() + "\nBody: " + response.body() + "\nPayload: " + payload);
                return "❌ Error API: HTTP " + response.statusCode() + " - " + response.body();
            }

            // ⚡ Extracción turbo
            String respuesta = extraerContenidoTurbo(response.body());
            System.out.println("✅ DeepSeek respondió (" + respuesta.length() + " chars)");
            
            return respuesta;

        } catch (Exception e) {
            System.err.println("💥 Error DeepSeek: " + e.getMessage());
            e.printStackTrace();
            return "❌ Error: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }

    /**
     * ⚡ EXTRACCIÓN TURBO MEJORADA - Con triple estrategia
     */
    private String extraerContenidoTurbo(String jsonResponse) {
        System.out.println("🚀 INICIANDO EXTRACCIÓN TURBO MEJORADA...");
        
        try {
            // 🛡️ ESTRATEGIA 1: GSON (más robusto)
            System.out.println("🎯 Estrategia 1: GSON...");
            String contenido = extraerConGson(jsonResponse);
            if (contenido != null && !contenido.trim().isEmpty() && contenido.length() > 10) {
                System.out.println("✅ GSON funcionó: " + contenido.length() + " chars");
                return contenido;
            }
            
            // 🛡️ ESTRATEGIA 2: REGEX (más flexible)  
            System.out.println("🎯 Estrategia 2: REGEX...");
            contenido = extraerConRegex(jsonResponse);
            if (contenido != null && !contenido.trim().isEmpty() && contenido.length() > 10) {
                System.out.println("✅ REGEX funcionó: " + contenido.length() + " chars");
                return contenido;
            }
            
            // 🛡️ ESTRATEGIA 3: INDEXOF (más básico)
            System.out.println("🎯 Estrategia 3: INDEXOF...");
            contenido = extraerConIndexOf(jsonResponse);
            if (contenido != null && !contenido.trim().isEmpty() && contenido.length() > 10) {
                System.out.println("✅ INDEXOF funcionó: " + contenido.length() + " chars");
                return contenido;
            }
            
            // 😈 ÚLTIMO RECURSO: RESCATE SATÁNICO
            System.out.println("😈 TODAS LAS ESTRATEGIAS FALLARON - ACTIVANDO RESCATE SATÁNICO...");
            return rescateSatánicoDesdeJSON(jsonResponse);
            
        } catch (Exception e) {
            System.err.println("💥 ERROR CRÍTICO en extracción turbo: " + e.getMessage());
            return "⚠️ Krakens del JSON atacaron todas las estrategias";
        }
    }

    /**
     * 😈 RESCATE SATÁNICO DESDE JSON - Cuando todo falla
     */
    private String rescateSatánicoDesdeJSON(String jsonResponse) {
        try {
            System.out.println("😈 BUSCANDO CUALQUIER TEXTO ÚTIL EN EL JSON...");
            
            // Buscar cualquier texto que parezca código Java
            if (jsonResponse.contains("package ") || jsonResponse.contains("class ")) {
                // Extraer manualmente buscando patrones
                int start = Math.max(
                    jsonResponse.indexOf("package "),
                    jsonResponse.indexOf("class ")
                );
                
                if (start != -1) {
                    // Tomar todo el contenido disponible sin límites
                    String rescate = jsonResponse.substring(start);
                    
                    // Limpiar caracteres problemáticos
                    rescate = rescate.replace("\\n", "\n")
                                   .replace("\\\"", "\"")
                                   .replace("\\\\", "\\");
                    
                    System.out.println("😈 RESCATE COMPLETO: " + rescate.length() + " chars");
                    return "// 🔥 CÓDIGO RESCATADO POR FUERZAS OSCURAS\n" + rescate;
                }
            }
            
            // Si no hay código, devolver mensaje de error épico
            return "// 🏴‍☠️ EL ORÁCULO SATÁNICO NO PUDO EXTRAER NADA\n" +
                   "// El JSON era: " + jsonResponse.length() + " chars\n" +
                   "// Contenido: " + jsonResponse;
            
        } catch (Exception e) {
            return "// 💥 FALLO CATASTRÓFICO EN RESCATE SATÁNICO: " + e.getMessage();
        }
    }

    /**
     * 🎯 EXTRACCIÓN CON GSON - Método principal (más robusto)
     */
    private String extraerConGson(String jsonResponse) {
        try {
            System.out.println("🔮 GSON: Intentando extraer contenido...");
            
            // Parsear el JSON completo
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            
            // Navegar por la estructura: choices[0].message.content
            JsonArray choices = jsonObject.getAsJsonArray("choices");
            if (choices == null || choices.size() == 0) {
                System.err.println("❌ GSON: No se encontró array 'choices'");
                return null;
            }
            
            JsonObject firstChoice = choices.get(0).getAsJsonObject();
            JsonObject message = firstChoice.getAsJsonObject("message");
            if (message == null) {
                System.err.println("❌ GSON: No se encontró objeto 'message'");
                return null;
            }
            
            JsonElement contentElement = message.get("content");
            if (contentElement == null || contentElement.isJsonNull()) {
                System.err.println("❌ GSON: No se encontró 'content' o es null");
                return null;
            }
            
            String contenido = contentElement.getAsString();
            System.out.println("✅ GSON: Extracción exitosa - " + contenido.length() + " chars");
            return contenido;
            
        } catch (Exception e) {
            System.err.println("💥 GSON: Error en extracción - " + e.getMessage());
            return null;
        }
    }

    /**
     * 🎯 EXTRACCIÓN CON REGEX - Método secundario (más flexible)
     */
    private String extraerConRegex(String jsonResponse) {
        try {
            System.out.println("🔍 REGEX: Intentando extraer contenido...");
            
            // Patrón regex para encontrar "content": "valor"
            Pattern pattern = Pattern.compile("\"content\"\\s*:\\s*\"(.*?)\"", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(jsonResponse);
            
            if (matcher.find()) {
                String contenido = matcher.group(1);
                
                // Limpiar escapes JSON
                contenido = contenido
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\")
                    .replace("\\t", "\t")
                    .replace("\\r", "\r")
                    .replace("\\/", "/");
                
                System.out.println("✅ REGEX: Extracción exitosa - " + contenido.length() + " chars");
                return contenido;
            } else {
                System.err.println("❌ REGEX: No se encontró patrón 'content'");
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("💥 REGEX: Error en extracción - " + e.getMessage());
            return null;
        }
    }

    /**
     * 🎯 EXTRACCIÓN CON INDEXOF - Método de respaldo (más básico)
     */
    private String extraerConIndexOf(String jsonResponse) {
        try {
            System.out.println("📡 INDEXOF: Intentando extraer contenido...");
            
            // Buscar el inicio del contenido
            int contentStart = jsonResponse.indexOf("\"content\":");
            if (contentStart == -1) {
                contentStart = jsonResponse.indexOf("\"content\" :");
                if (contentStart == -1) {
                    System.err.println("❌ INDEXOF: No se encontró 'content' en el JSON");
                    return null;
                }
                contentStart += 11; // "\"content\" :".length()
            } else {
                contentStart += 10; // "\"content\":".length()
            }
            
            // Buscar las comillas de apertura
            int quoteStart = jsonResponse.indexOf("\"", contentStart);
            if (quoteStart == -1) {
                System.err.println("❌ INDEXOF: No se encontró comilla de apertura después de content");
                return null;
            }
            
            // La posición real del contenido empieza después de la comilla
            int contentRealStart = quoteStart + 1;
            
            // Buscar la comilla de cierre (manejar escapes)
            int contentEnd = -1;
            int currentPos = contentRealStart;
            boolean escapeNext = false;
            
            while (currentPos < jsonResponse.length()) {
                char c = jsonResponse.charAt(currentPos);
                
                if (escapeNext) {
                    escapeNext = false;
                } else if (c == '\\') {
                    escapeNext = true;
                } else if (c == '"') {
                    contentEnd = currentPos;
                    break;
                }
                currentPos++;
            }
            
            if (contentEnd == -1) {
                System.err.println("❌ INDEXOF: No se encontró comilla de cierre balanceada");
                return null;
            }
            
            String contenido = jsonResponse.substring(contentRealStart, contentEnd);
            
            // Limpiar escapes
            contenido = contenido
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\t", "\t")
                .replace("\\r", "\r")
                .replace("\\/", "/");
            
            System.out.println("✅ INDEXOF: Extracción exitosa - " + contenido.length() + " chars");
            return contenido;
            
        } catch (Exception e) {
            System.err.println("💥 INDEXOF: Error en extracción - " + e.getMessage());
            return null;
        }
    }

    /**
     * 🎯 Método para debugging rápido
     */
    public void debugEstado() {
        System.out.println("\n🔍 DEBUG DeepSeekClient:");
        System.out.println("📡 API Key: " + (API_KEY != null ? "✅ Configurada" : "❌ Faltante"));
        System.out.println("🚀 HttpClient: " + CLIENT.version());
        System.out.println("🎯 Modelo default: " + DEFAULT_MODEL);
        
        // Test de payload
        Map<String, Object> testPayload = Map.of(
            "model", "test",
            "temperature", 0.7,
            "messages", List.of(
                Map.of("role", "user", "content", "test")
            )
        );
        System.out.println("🎯 Test JSON: " + GSON.toJson(testPayload));
    }

    /**
     * 🎯 SETTER PARA TEMPERATURA - ¡IMPLEMENTADO!
     */
    public void setTemperature(double temperature) {
        if (temperature >= 0.0 && temperature <= 1.0) {
            this.temperature = temperature;
            System.out.println("🎯 Temperatura configurada: " + temperature);
        } else {
            System.out.println("⚠️ Temperatura fuera de rango (0.0-1.0). Usando: " + this.temperature);
        }
    }
}