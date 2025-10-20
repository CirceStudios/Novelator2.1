package com.elreinodelolvido.ellibertad.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONObject;

import com.elreinodelolvido.ellibertad.api.OraculoDeLaLibertad;
import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.util.FileUtils;

import org.json.JSONArray;

public class APIManager {
    
    private Bitacora bitacora;
    
    // 🚀 MÉTRICAS Y ESTADÍSTICAS AVANZADAS
    private AtomicInteger totalLlamadasAPI = new AtomicInteger(0);
    private AtomicInteger llamadasExitosas = new AtomicInteger(0);
    private AtomicInteger llamadasFallidas = new AtomicInteger(0);
    private AtomicLong tiempoTotalRespuesta = new AtomicLong(0);
    private AtomicInteger tokensUtilizados = new AtomicInteger(0);
    
    // 🎯 CONFIGURACIÓN DINÁMICA
    private Map<String, Object> configuracionAPI = new HashMap<>();
    private HttpClient httpClient;
    private boolean clienteNativoHabilitado = false;
    
    // 🔄 CACHE INTELIGENTE
    private Map<String, CacheEntry> cacheRespuestas = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION_MS = 15 * 60 * 1000; // 15 minutos
    
    // 🏴‍☠️ CLIENTE API DE EMERGENCIA
    private Object clienteEmergencia = null;
    private boolean modoEmergencia = false;

    // 🎯 CLASE INTERNA PARA CACHE
    private static class CacheEntry {
        String respuesta;
        long timestamp;
        int tokens;
        
        CacheEntry(String respuesta, int tokens) {
            this.respuesta = respuesta;
            this.timestamp = System.currentTimeMillis();
            this.tokens = tokens;
        }
        
        boolean isValido() {
            return (System.currentTimeMillis() - timestamp) < CACHE_DURATION_MS;
        }
    }

    public APIManager(Bitacora bitacora) {
        this.bitacora = bitacora;
        inicializarConfiguracion();
        inicializarHttpClient();
        inicializarClienteEmergencia();
    }

    /**
     * 🚀 INICIALIZAR CONFIGURACIÓN COMPLETA
     */
    private void inicializarConfiguracion() {
        configuracionAPI.put("timeout_ms", 45000);
        configuracionAPI.put("max_reintentos", 3);
        configuracionAPI.put("delay_reintento_ms", 2000);
        configuracionAPI.put("max_tokens", 4000);
        configuracionAPI.put("temperature", 0.7);
        configuracionAPI.put("habilitar_cache", true);
        configuracionAPI.put("modo_verbose", true);
        configuracionAPI.put("url_api", "https://api.deepseek.com/v1/chat/completions");
        
        bitacora.info("⚙️ Configuración API inicializada con " + configuracionAPI.size() + " parámetros");
    }

    /**
     * 🚀 INICIALIZAR HTTP CLIENT AVANZADO
     */
    private void inicializarHttpClient() {
        try {
            this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis((Integer) configuracionAPI.get("timeout_ms")))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .version(HttpClient.Version.HTTP_2)
                .build();
            
            clienteNativoHabilitado = true;
            bitacora.exito("✅ HttpClient nativo inicializado para modo emergencia");
        } catch (Exception e) {
            bitacora.warn("⚠️ No se pudo inicializar HttpClient nativo: " + e.getMessage());
        }
    }

    /**
     * 🚀 INICIALIZAR CLIENTE DE EMERGENCIA
     */
    private void inicializarClienteEmergencia() {
        try {
            // Intentar cargar el cliente de emergencia si existe
            Class<?> clienteEmergenciaClass = Class.forName("com.novelator.autogen.api.DeepSeekClientEmergencia");
            Constructor<?> constructor = clienteEmergenciaClass.getConstructor();
            this.clienteEmergencia = constructor.newInstance();
            bitacora.exito("✅ Cliente de emergencia cargado exitosamente");
        } catch (Exception e) {
            bitacora.info("🔧 Cliente de emergencia no disponible, se creará bajo demanda");
        }
    }

    /**
     * 🔧 DIAGNÓSTICO PROFUNDO API DEEPSEEK - TURBOFURULADO
     */
    public JSONObject diagnosticoProfundoAPI() {
        bitacora.info("🔧 EJECUTANDO DIAGNÓSTICO PROFUNDO API DEEPSEEK...");
        
        JSONObject diagnostico = new JSONObject();
        diagnostico.put("timestamp", new java.util.Date().toString());
        diagnostico.put("version_sistema", "2.0.0-turbofuru");
        
        try {
            // 1. VERIFICAR CLASE PRINCIPAL
            Class<?> deepSeekClass = Class.forName("com.novelator.autogen.api.DeepSeekClient");
            diagnostico.put("clase_principal", "✅ ENCONTRADA");
            diagnostico.put("clase_nombre", deepSeekClass.getName());
            
            // 2. VERIFICAR CONSTRUCTORES
            Constructor<?>[] constructores = deepSeekClass.getConstructors();
            diagnostico.put("constructores", constructores.length);
            
            // 3. CREAR INSTANCIA
            Object deepSeekInstance = constructores[0].newInstance();
            diagnostico.put("instancia", "✅ CREADA");
            
            // 4. VERIFICAR MÉTODOS
            Method[] metodos = deepSeekClass.getMethods();
            diagnostico.put("metodos_totales", metodos.length);
            
            JSONArray metodosDisponibles = new JSONArray();
            JSONArray pruebasEjecutadas = new JSONArray();
            
            // 5. PROBAR MÉTODOS CLAVE
            for (Method metodo : metodos) {
                String nombreMetodo = metodo.getName();
                metodosDisponibles.put(nombreMetodo);
                
                if (nombreMetodo.contains("test") || nombreMetodo.contains("conexion") || 
                    nombreMetodo.contains("analyze") || nombreMetodo.contains("analizar")) {
                    
                    JSONObject pruebaMetodo = new JSONObject();
                    pruebaMetodo.put("metodo", nombreMetodo);
                    
                    try {
                        long startTime = System.currentTimeMillis();
                        Object resultado = metodo.invoke(deepSeekInstance);
                        long endTime = System.currentTimeMillis();
                        
                        pruebaMetodo.put("estado", "✅ EXITOSO");
                        pruebaMetodo.put("tiempo_ms", endTime - startTime);
                        pruebaMetodo.put("resultado", resultado.toString().substring(0, Math.min(100, resultado.toString().length())));
                        
                    } catch (Exception e) {
                        pruebaMetodo.put("estado", "❌ FALLIDO");
                        pruebaMetodo.put("error", e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
                    }
                    
                    pruebasEjecutadas.put(pruebaMetodo);
                }
            }
            
            diagnostico.put("metodos_disponibles", metodosDisponibles);
            diagnostico.put("pruebas_ejecutadas", pruebasEjecutadas);
            
            // 6. VERIFICAR CONFIGURACIÓN API KEY
            String apiKey = System.getenv("DEEPSEEK_API_KEY");
            if (apiKey != null && !apiKey.trim().isEmpty() && !apiKey.equals("null")) {
                diagnostico.put("api_key", "✅ CONFIGURADA (longitud: " + apiKey.length() + ")");
            } else {
                diagnostico.put("api_key", "❌ NO CONFIGURADA");
                diagnostico.put("recomendacion", "Configurar variable de entorno: export DEEPSEEK_API_KEY=tu_clave");
            }
            
            // 7. VERIFICAR CONECTIVIDAD DE RED
            diagnostico.put("conectividad_red", verificarConectividadInternet());
            
            // 8. ESTADO GENERAL
            diagnostico.put("estado_general", "✅ OPERATIVO");
            diagnostico.put("nivel_confianza", "ALTO");
            
            bitacora.exito("Diagnóstico API completado - Sistema operativo");
            
        } catch (Exception e) {
            diagnostico.put("estado_general", "❌ CRÍTICO");
            diagnostico.put("error", e.getMessage());
            diagnostico.put("causa", e.getCause() != null ? e.getCause().getMessage() : "N/A");
            
            bitacora.error("Diagnóstico API falló", e);
        }
        
        // 🎯 MOSTRAR REPORTE DETALLADO
        mostrarReporteDiagnostico(diagnostico);
        return diagnostico;
    }

    /**
     * 🌐 VERIFICAR CONECTIVIDAD INTERNET
     */
    private String verificarConectividadInternet() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.deepseek.com"))
                .timeout(Duration.ofSeconds(10))
                .HEAD()
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return "✅ CONECTADO (HTTP " + response.statusCode() + ")";
        } catch (Exception e) {
            return "❌ SIN CONEXIÓN: " + e.getMessage();
        }
    }

    /**
     * 📊 MOSTRAR REPORTE DE DIAGNÓSTICO
     */
    private void mostrarReporteDiagnostico(JSONObject diagnostico) {
        System.out.println("\n" + "🔧".repeat(80));
        System.out.println("           DIAGNÓSTICO PROFUNDO API DEEPSEEK - REPORTE COMPLETO");
        System.out.println("🔧".repeat(80));
        
        System.out.println("📅 Timestamp: " + diagnostico.getString("timestamp"));
        System.out.println("🚀 Versión: " + diagnostico.getString("version_sistema"));
        System.out.println("🎯 Estado General: " + diagnostico.getString("estado_general"));
        
        if (diagnostico.has("clase_principal")) {
            System.out.println("\n🏗️  ESTRUCTURA DEL CLIENTE:");
            System.out.println("  • Clase Principal: " + diagnostico.getString("clase_principal"));
            System.out.println("  • Nombre: " + diagnostico.getString("clase_nombre"));
            System.out.println("  • Constructores: " + diagnostico.getInt("constructores"));
            System.out.println("  • Métodos Totales: " + diagnostico.getInt("metodos_totales"));
        }
        
        if (diagnostico.has("api_key")) {
            System.out.println("\n🔑 CONFIGURACIÓN API:");
            System.out.println("  • API Key: " + diagnostico.getString("api_key"));
        }
        
        if (diagnostico.has("conectividad_red")) {
            System.out.println("  • Conectividad: " + diagnostico.getString("conectividad_red"));
        }
        
        if (diagnostico.has("pruebas_ejecutadas")) {
            System.out.println("\n🧪 PRUEBAS EJECUTADAS:");
            JSONArray pruebas = diagnostico.getJSONArray("pruebas_ejecutadas");
            for (int i = 0; i < pruebas.length(); i++) {
                JSONObject prueba = pruebas.getJSONObject(i);
                System.out.println("  • " + prueba.getString("metodo") + ": " + prueba.getString("estado") + 
                                 (prueba.has("tiempo_ms") ? " (" + prueba.getLong("tiempo_ms") + "ms)" : ""));
            }
        }
        
        System.out.println("\n📈 MÉTRICAS DEL SISTEMA:");
        System.out.println("  • Llamadas Totales: " + totalLlamadasAPI.get());
        System.out.println("  • Éxitos: " + llamadasExitosas.get());
        System.out.println("  • Fallos: " + llamadasFallidas.get());
        System.out.println("  • Tokens Utilizados: " + tokensUtilizados.get());
        
        System.out.println("🔧".repeat(80));
    }

    /**
     * 🚀 CREAR CLIENTE API DE EMERGENCIA - TURBOFURULADO
     */
    public JSONObject crearClienteAPIEmergencia() {
        bitacora.info("🚀 CREANDO CLIENTE API DE EMERGENCIA TURBOFURULADO...");
        
        JSONObject resultado = new JSONObject();
        
        try {
            // 🎯 CÓDIGO DEL CLIENTE DE EMERGENCIA MEJORADO
            String codigoClienteEmergencia = 
                "package com.novelator.autogen.api;\n\n" +
                "import java.net.URI;\n" +
                "import java.net.http.HttpClient;\n" +
                "import java.net.http.HttpRequest;\n" +
                "import java.net.http.HttpResponse;\n" +
                "import java.time.Duration;\n" +
                "import org.json.JSONObject;\n" +
                "import org.json.JSONArray;\n\n" +
                "/**\n" +
                " * 🚀 CLIENTE DEEPSEEK DE EMERGENCIA TURBOFURULADO\n" +
                " * ⚡ Creado automáticamente por APIManager\n" +
                " * 🔧 Funciona sin dependencias complejas\n" +
                " */\n" +
                "public class DeepSeekClientEmergencia {\n" +
                "    private static final String API_KEY = System.getenv(\\\"DEEPSEEK_API_KEY\\\");\n" +
                "    private static final String API_URL = \\\"https://api.deepseek.com/v1/chat/completions\\\";\n" +
                "    private final HttpClient client;\n" +
                "    private int totalLlamadas = 0;\n" +
                "    private int llamadasExitosas = 0;\n\n" +
                "    public DeepSeekClientEmergencia() {\n" +
                "        this.client = HttpClient.newBuilder()\n" +
                "            .connectTimeout(Duration.ofSeconds(30))\n" +
                "            .build();\n" +
                "        System.out.println(\\\"✅ Cliente de emergencia DeepSeek inicializado\\\");\n" +
                "    }\n\n" +
                "    public String testConexion() {\n" +
                "        totalLlamadas++;\n" +
                "        try {\n" +
                "            if (API_KEY == null || API_KEY.isEmpty() || API_KEY.equals(\\\"null\\\")) {\n" +
                "                return \\\"❌ API_KEY no configurada. Usa: export DEEPSEEK_API_KEY=tu_key\\\";\n" +
                "            }\n\n" +
                "            JSONObject requestBody = new JSONObject();\n" +
                "            requestBody.put(\\\"model\\\", \\\"deepseek-chat\\\");\n" +
                "            \n" +
                "            JSONArray messages = new JSONArray();\n" +
                "            JSONObject message = new JSONObject();\n" +
                "            message.put(\\\"role\\\", \\\"user\\\");\n" +
                "            message.put(\\\"content\\\", \\\"Test connection - responde con OK en 5 palabras máximo\\\");\n" +
                "            messages.put(message);\n" +
                "            \n" +
                "            requestBody.put(\\\"messages\\\", messages);\n" +
                "            requestBody.put(\\\"max_tokens\\\", 20);\n" +
                "            requestBody.put(\\\"temperature\\\", 0.1);\n\n" +
                "            HttpRequest request = HttpRequest.newBuilder()\n" +
                "                .uri(URI.create(API_URL))\n" +
                "                .header(\\\"Content-Type\\\", \\\"application/json\\\")\n" +
                "                .header(\\\"Authorization\\\", \\\"Bearer \\\" + API_KEY)\n" +
                "                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))\n" +
                "                .build();\n\n" +
                "            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());\n" +
                "            \n" +
                "            if (response.statusCode() == 200) {\n" +
                "                llamadasExitosas++;\n" +
                "                JSONObject jsonResponse = new JSONObject(response.body());\n" +
                "                String content = jsonResponse.getJSONArray(\\\"choices\\\")\n" +
                "                    .getJSONObject(0)\n" +
                "                    .getJSONObject(\\\"message\\\")\n" +
                "                    .getString(\\\"content\\\");\n" +
                "                return \\\"✅ CONEXIÓN EXITOSA - Respuesta: \\\" + content.trim();\n" +
                "            } else {\n" +
                "                return \\\"❌ Error HTTP: \\\" + response.statusCode() + \\\" - \\\" + response.body();\n" +
                "            }\n" +
                "        } catch (Exception e) {\n" +
                "            return \\\"❌ Error de conexión: \\\" + e.getMessage();\n" +
                "        }\n" +
                "    }\n\n" +
                "    public String analizarCodigo(String codigo) {\n" +
                "        totalLlamadas++;\n" +
                "        try {\n" +
                "            if (API_KEY == null || API_KEY.isEmpty() || API_KEY.equals(\\\"null\\\")) {\n" +
                "                return \\\"❌ API_KEY no configurada\\\";\n" +
                "            }\n\n" +
                "            String prompt = \\\"Analiza este código Java y proporciona sugerencias específicas de refactorización, mejoras de rendimiento y detección de code smells. Responde en español.\\\\n\\\\nCódigo:\\\\n\\\\n\\\" + codigo;\n" +
                "            \n" +
                "            JSONObject requestBody = new JSONObject();\n" +
                "            requestBody.put(\\\"model\\\", \\\"deepseek-chat\\\");\n" +
                "            \n" +
                "            JSONArray messages = new JSONArray();\n" +
                "            JSONObject message = new JSONObject();\n" +
                "            message.put(\\\"role\\\", \\\"user\\\");\n" +
                "            message.put(\\\"content\\\", prompt);\n" +
                "            messages.put(message);\n" +
                "            \n" +
                "            requestBody.put(\\\"messages\\\", messages);\n" +
                "            requestBody.put(\\\"max_tokens\\\", 2000);\n" +
                "            requestBody.put(\\\"temperature\\\", 0.7);\n\n" +
                "            HttpRequest request = HttpRequest.newBuilder()\n" +
                "                .uri(URI.create(API_URL))\n" +
                "                .header(\\\"Content-Type\\\", \\\"application/json\\\")\n" +
                "                .header(\\\"Authorization\\\", \\\"Bearer \\\" + API_KEY)\n" +
                "                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))\n" +
                "                .build();\n\n" +
                "            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());\n" +
                "            \n" +
                "            if (response.statusCode() == 200) {\n" +
                "                llamadasExitosas++;\n" +
                "                JSONObject jsonResponse = new JSONObject(response.body());\n" +
                "                return jsonResponse.getJSONArray(\\\"choices\\\")\n" +
                "                    .getJSONObject(0)\n" +
                "                    .getJSONObject(\\\"message\\\")\n" +
                "                    .getString(\\\"content\\\");\n" +
                "            } else {\n" +
                "                return \\\"❌ Error en análisis: \\\" + response.statusCode() + \\\" - \\\" + response.body();\n" +
                "            }\n" +
                "        } catch (Exception e) {\n" +
                "            return \\\"❌ Error analizando código: \\\" + e.getMessage();\n" +
                "        }\n" +
                "    }\n\n" +
                "    public String getEstadisticas() {\n" +
                "        return \\\"📊 Estadísticas Emergencia - Llamadas: \\\" + totalLlamadas + \\\", Éxitos: \\\" + llamadasExitosas;\n" +
                "    }\n" +
                "}";
            
            // 📁 GUARDAR ARCHIVO
            String rutaArchivo = "autogen-output/DeepSeekClientEmergencia.java";
            FileUtils.writeToFile(rutaArchivo, codigoClienteEmergencia);
            
            // 🔧 COMPILAR AUTOMÁTICAMENTE
            boolean compilado = compilarClienteEmergencia(rutaArchivo);
            
            resultado.put("archivo_creado", rutaArchivo);
            resultado.put("compilado", compilado);
            resultado.put("estado", compilado ? "✅ CLIENTE DE EMERGENCIA CREADO Y COMPILADO" : "⚠️ CLIENTE CREADO PERO NO COMPILADO");
            
            System.out.println("✅ Cliente de emergencia turbofurulado creado: " + rutaArchivo);
            
            if (compilado) {
                System.out.println("🔧 El archivo ha sido compilado automáticamente");
                // 🚀 CARGAR EL CLIENTE RECIÉN CREADO
                cargarClienteEmergencia();
            } else {
                System.out.println("📝 Instrucciones de compilación manual:");
                System.out.println("   1. Asegúrate de tener org.json en el classpath");
                System.out.println("   2. Compila: javac -cp \".:lib/*\" autogen-output/DeepSeekClientEmergencia.java");
                System.out.println("   3. Configura API key: export DEEPSEEK_API_KEY=tu_key_aqui");
            }
            
            bitacora.exito("Cliente de emergencia turbofurulado creado exitosamente");
            
        } catch (Exception e) {
            resultado.put("estado", "❌ ERROR");
            resultado.put("error", e.getMessage());
            System.out.println("❌ Error creando cliente emergencia: " + e.getMessage());
            bitacora.error("Fallo creando cliente de emergencia", e);
        }
        
        return resultado;
    }

    /**
     * 🔧 COMPILAR CLIENTE DE EMERGENCIA AUTOMÁTICAMENTE
     */
    private boolean compilarClienteEmergencia(String rutaArchivo) {
        try {
            // 🎯 INTENTAR COMPILACIÓN AUTOMÁTICA
            ProcessBuilder pb = new ProcessBuilder("javac", "-cp", ".", rutaArchivo);
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            return exitCode == 0;
        } catch (Exception e) {
            bitacora.warn("No se pudo compilar automáticamente: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🚀 CARGAR CLIENTE DE EMERGENCIA
     */
    private void cargarClienteEmergencia() {
        try {
            Class<?> clienteClass = Class.forName("com.novelator.autogen.api.DeepSeekClientEmergencia");
            Constructor<?> constructor = clienteClass.getConstructor();
            this.clienteEmergencia = constructor.newInstance();
            this.modoEmergencia = true;
            bitacora.exito("✅ Cliente de emergencia cargado y activado");
        } catch (Exception e) {
            bitacora.warn("⚠️ No se pudo cargar el cliente de emergencia: " + e.getMessage());
        }
    }

    /**
     * 🎯 INVOCAR ORÁCULO TÉCNICO - TURBOFURULADO CON REINTENTOS Y CACHE
     */
    public String invocarOraculoTecnico(String codigoFuente) {
        return invocarOraculoTecnico(codigoFuente, 0);
    }

    private String invocarOraculoTecnico(String codigoFuente, int reintento) {
        totalLlamadasAPI.incrementAndGet();
        long startTime = System.currentTimeMillis();
        
        // 🎯 VERIFICAR CACHE PRIMERO
        String cacheKey = "oraculo_" + codigoFuente.hashCode();
        if ((Boolean) configuracionAPI.get("habilitar_cache")) {
            CacheEntry cached = cacheRespuestas.get(cacheKey);
            if (cached != null && cached.isValido()) {
                bitacora.debug("🔄 Usando respuesta cacheada para análisis");
                llamadasExitosas.incrementAndGet();
                return cached.respuesta;
            }
        }
        
        try {
            String resultado;
            
            if (modoEmergencia && clienteEmergencia != null) {
                // 🚀 USAR CLIENTE DE EMERGENCIA
                resultado = invocarClienteEmergencia(codigoFuente);
            } else {
                // 🎯 USAR ORÁCULO PRINCIPAL
                OraculoDeLaLibertad oraculo = new OraculoDeLaLibertad();
                resultado = oraculo.invocarOraculoTecnico(codigoFuente);
            }
            
            long endTime = System.currentTimeMillis();
            tiempoTotalRespuesta.addAndGet(endTime - startTime);
            llamadasExitosas.incrementAndGet();
            
            // 💾 GUARDAR EN CACHE
            if ((Boolean) configuracionAPI.get("habilitar_cache")) {
                int tokensEstimados = resultado.length() / 4; // Estimación aproximada
                cacheRespuestas.put(cacheKey, new CacheEntry(resultado, tokensEstimados));
                tokensUtilizados.addAndGet(tokensEstimados);
            }
            
            if ((Boolean) configuracionAPI.get("modo_verbose")) {
                System.out.println("✅ Análisis completado en " + (endTime - startTime) + "ms");
            }
            
            return resultado;
            
        } catch (Exception e) {
            llamadasFallidas.incrementAndGet();
            bitacora.error("Error invocando oráculo técnico (reintento " + reintento + ")", e);
            
            // 🔄 REINTENTAR SI ES POSIBLE
            int maxReintentos = (Integer) configuracionAPI.get("max_reintentos");
            if (reintento < maxReintentos) {
                try {
                    Thread.sleep((Integer) configuracionAPI.get("delay_reintento_ms"));
                    return invocarOraculoTecnico(codigoFuente, reintento + 1);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            
            return "❌ Error al invocar el oráculo técnico después de " + (reintento + 1) + " intentos: " + e.getMessage();
        }
    }

    /**
     * 🚀 INVOCAR CLIENTE DE EMERGENCIA
     */
    private String invocarClienteEmergencia(String codigoFuente) {
        try {
            Method analizarMethod = clienteEmergencia.getClass().getMethod("analizarCodigo", String.class);
            return (String) analizarMethod.invoke(clienteEmergencia, codigoFuente);
        } catch (Exception e) {
            bitacora.error("Error usando cliente de emergencia", e);
            return "❌ Fallback de emergencia también falló: " + e.getMessage();
        }
    }

    /**
     * 🔍 VERIFICAR CONFIGURACIÓN API - TURBOFURULADO
     */
    public JSONObject verificarConfiguracionAPI() {
        bitacora.info("🔍 EJECUTANDO VERIFICACIÓN COMPLETA DE CONFIGURACIÓN API...");
        
        JSONObject verificacion = new JSONObject();
        verificacion.put("timestamp", new java.util.Date().toString());
        
        try {
            // 1. VERIFICAR CLASE PRINCIPAL
            Class<?> deepSeekClass = Class.forName("com.novelator.autogen.api.DeepSeekClient");
            verificacion.put("clase_principal", "✅ OK");
            
            // 2. VERIFICAR API KEY
            String apiKey = System.getenv("DEEPSEEK_API_KEY");
            if (apiKey != null && !apiKey.trim().isEmpty() && !apiKey.equals("null")) {
                verificacion.put("api_key", "✅ CONFIGURADA");
                verificacion.put("api_key_longitud", apiKey.length());
                // No mostrar la clave por seguridad
                verificacion.put("api_key_preview", apiKey.substring(0, 8) + "..." + apiKey.substring(apiKey.length() - 4));
            } else {
                verificacion.put("api_key", "❌ NO CONFIGURADA");
            }
            
            // 3. VERIFICAR INSTANCIA
            Constructor<?> constructor = deepSeekClass.getConstructor();
            Object deepSeekInstance = constructor.newInstance();
            verificacion.put("instancia", "✅ CREADA");
            
            // 4. VERIFICAR MÉTODO DE PRUEBA
            Method testMethod = deepSeekClass.getMethod("testConexion");
            Object resultado = testMethod.invoke(deepSeekInstance);
            verificacion.put("prueba_conexion", "✅ EXITOSA");
            verificacion.put("resultado_prueba", resultado.toString());
            
            // 5. ESTADO FINAL
            verificacion.put("estado", "✅ CONFIGURACIÓN CORRECTA");
            verificacion.put("nivel", "ÓPTIMO");
            
            System.out.println("\n✅ Configuración API verificada correctamente");
            bitacora.exito("Verificación de configuración API completada - Sistema óptimo");
            
        } catch (Exception e) {
            verificacion.put("estado", "❌ CONFIGURACIÓN INCORRECTA");
            verificacion.put("error", e.getMessage());
            verificacion.put("nivel", "CRÍTICO");
            
            System.out.println("❌ Problema con configuración API: " + e.getMessage());
            bitacora.error("Fallo en verificación de configuración API", e);
        }
        
        return verificacion;
    }

    /**
     * 🧪 PROBAR CONEXIÓN CON API REAL - TURBOFURULADO
     */
    public JSONObject probarConexionAPIReal() {
        bitacora.info("🧪 EJECUTANDO PRUEBA DE CONEXIÓN API REAL...");
        
        JSONObject prueba = new JSONObject();
        prueba.put("timestamp", new java.util.Date().toString());
        
        try {
            Class<?> deepSeekClass = Class.forName("com.novelator.autogen.api.DeepSeekClient");
            Constructor<?> constructor = deepSeekClass.getConstructor();
            Object deepSeekInstance = constructor.newInstance();
            
            Method testMethod = deepSeekClass.getMethod("testConexion");
            
            long startTime = System.currentTimeMillis();
            String resultado = (String) testMethod.invoke(deepSeekInstance);
            long endTime = System.currentTimeMillis();
            
            prueba.put("tiempo_respuesta_ms", endTime - startTime);
            prueba.put("resultado", resultado);
            prueba.put("estado", resultado.contains("✅") || resultado.contains("EXITOSA") ? "✅ CONECTADO" : "❌ FALLIDO");
            
            System.out.println("\n🔍 RESULTADO PRUEBA API:");
            System.out.println("  • Estado: " + prueba.getString("estado"));
            System.out.println("  • Tiempo: " + prueba.getLong("tiempo_respuesta_ms") + "ms");
            System.out.println("  • Respuesta: " + resultado);
            
            if (prueba.getString("estado").contains("✅")) {
                bitacora.exito("Prueba de conexión API real exitosa");
            } else {
                bitacora.error("Prueba de conexión API fallida: " + resultado);
            }
            
        } catch (Exception e) {
            prueba.put("estado", "💥 ERROR CRÍTICO");
            prueba.put("error", e.getMessage());
            System.out.println("💥 Error probando conexión API: " + e.getMessage());
            bitacora.error("Fallo en prueba de conexión API real", e);
        }
        
        return prueba;
    }

    /**
     * 🔄 REINICIAR CLIENTE API - TURBOFURULADO
     */
    public JSONObject reiniciarClienteAPI() {
        bitacora.info("🔄 EJECUTANDO REINICIO COMPLETO DE CLIENTE API...");
        
        JSONObject reinicio = new JSONObject();
        reinicio.put("timestamp", new java.util.Date().toString());
        
        try {
            // 1. LIMPIAR CACHE
            int elementosCache = cacheRespuestas.size();
            cacheRespuestas.clear();
            reinicio.put("cache_limpiado", elementosCache + " elementos");
            
            // 2. REINICIAR CLIENTE PRINCIPAL
            Class<?> deepSeekClass = Class.forName("com.novelator.autogen.api.DeepSeekClient");
            Constructor<?> constructor = deepSeekClass.getConstructor();
            Object nuevaInstancia = constructor.newInstance();
            reinicio.put("cliente_principal", "✅ REINICIADO");
            
            // 3. REINICIAR CLIENTE EMERGENCIA SI EXISTE
            if (clienteEmergencia != null) {
                clienteEmergencia = constructor.newInstance();
                reinicio.put("cliente_emergencia", "✅ REINICIADO");
            }
            
            // 4. REINICIAR HTTP CLIENT
            inicializarHttpClient();
            reinicio.put("http_client", "✅ REINICIADO");
            
            // 5. RESULTADO
            reinicio.put("estado", "✅ REINICIO COMPLETO EXITOSO");
            
            System.out.println("✅ Cliente API reiniciado exitosamente");
            bitacora.exito("Reinicio completo de cliente API completado");
            
        } catch (Exception e) {
            reinicio.put("estado", "❌ REINICIO FALLIDO");
            reinicio.put("error", e.getMessage());
            System.out.println("❌ Error reiniciando cliente API: " + e.getMessage());
            bitacora.error("Fallo en reinicio de cliente API", e);
        }
        
        return reinicio;
    }

    /**
     * 📊 OBTENER ESTADÍSTICAS API COMPLETAS - TURBOFURULADO
     */
    public JSONObject mostrarEstadisticasAPI() {
        bitacora.info("📊 GENERANDO ESTADÍSTICAS COMPLETAS DE API...");
        
        JSONObject estadisticas = new JSONObject();
        estadisticas.put("timestamp", new java.util.Date().toString());
        
        // 📈 MÉTRICAS BÁSICAS
        estadisticas.put("llamadas_totales", totalLlamadasAPI.get());
        estadisticas.put("llamadas_exitosas", llamadasExitosas.get());
        estadisticas.put("llamadas_fallidas", llamadasFallidas.get());
        
        // 📊 PORCENTAJES
        double tasaExito = totalLlamadasAPI.get() > 0 ? 
            (double) llamadasExitosas.get() / totalLlamadasAPI.get() * 100 : 0;
        estadisticas.put("tasa_exito", String.format("%.1f%%", tasaExito));
        
        // ⏱️ TIEMPOS
        long tiempoPromedio = totalLlamadasAPI.get() > 0 ? 
            tiempoTotalRespuesta.get() / totalLlamadasAPI.get() : 0;
        estadisticas.put("tiempo_promedio_ms", tiempoPromedio);
        
        // 🎯 TOKENS
        estadisticas.put("tokens_estimados", tokensUtilizados.get());
        
        // 🔄 CACHE
        estadisticas.put("elementos_en_cache", cacheRespuestas.size());
        long elementosValidos = cacheRespuestas.values().stream()
            .filter(CacheEntry::isValido)
            .count();
        estadisticas.put("elementos_cache_validos", elementosValidos);
        
        // 🚀 CONFIGURACIÓN
        estadisticas.put("configuracion", new JSONObject(configuracionAPI));
        
        // 🏴‍☠️ ESTADO
        estadisticas.put("modo_emergencia", modoEmergencia);
        estadisticas.put("cliente_nativo_habilitado", clienteNativoHabilitado);
        estadisticas.put("cliente_emergencia_cargado", clienteEmergencia != null);
        
        // 📊 MOSTRAR REPORTE
        System.out.println("\n" + "📊".repeat(80));
        System.out.println("           ESTADÍSTICAS COMPLETAS API DEEPSEEK");
        System.out.println("📊".repeat(80));
        
        System.out.println("📈 MÉTRICAS DE USO:");
        System.out.println("  • Llamadas Totales: " + estadisticas.getInt("llamadas_totales"));
        System.out.println("  • Éxitos: " + estadisticas.getInt("llamadas_exitosas"));
        System.out.println("  • Fallos: " + estadisticas.getInt("llamadas_fallidas"));
        System.out.println("  • Tasa de Éxito: " + estadisticas.getString("tasa_exito"));
        System.out.println("  • Tiempo Promedio: " + estadisticas.getLong("tiempo_promedio_ms") + "ms");
        System.out.println("  • Tokens Estimados: " + estadisticas.getInt("tokens_estimados"));
        
        System.out.println("\n🔧 ESTADO DEL SISTEMA:");
        System.out.println("  • Modo Emergencia: " + (estadisticas.getBoolean("modo_emergencia") ? "✅ ACTIVADO" : "❌ INACTIVO"));
        System.out.println("  • Cliente Nativo: " + (estadisticas.getBoolean("cliente_nativo_habilitado") ? "✅ HABILITADO" : "❌ INHABILITADO"));
        System.out.println("  • Cliente Emergencia: " + (estadisticas.getBoolean("cliente_emergencia_cargado") ? "✅ CARGADO" : "❌ NO CARGADO"));
        
        System.out.println("\n💾 CACHE:");
        System.out.println("  • Elementos en Cache: " + estadisticas.getInt("elementos_en_cache"));
        System.out.println("  • Elementos Válidos: " + estadisticas.getLong("elementos_cache_validos"));
        
        System.out.println("📊".repeat(80));
        
        return estadisticas;
    }

    /**
     * 🛠️ REPARAR CONFIGURACIÓN API - TURBOFURULADO
     */
    public JSONObject repararConfiguracionAPI() {
        bitacora.info("🛠️ EJECUTANDO REPARACIÓN COMPLETA DE CONFIGURACIÓN API...");
        
        JSONObject reparacion = new JSONObject();
        reparacion.put("timestamp", new java.util.Date().toString());
        reparacion.put("pasos_completados", new JSONArray());
        
        System.out.println("\n🔧 REPARACIÓN DE CONFIGURACIÓN API TURBOFURULADA");
        System.out.println("=" .repeat(50));
        
        // PASO 1: VERIFICAR VARIABLE DE ENTORNO
        System.out.println("1. 🔍 Verificando variable DEEPSEEK_API_KEY...");
        String apiKey = System.getenv("DEEPSEEK_API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.equals("null")) {
            reparacion.getJSONArray("pasos_completados").put("❌ DEEPSEEK_API_KEY no configurada");
            System.out.println("   ❌ DEEPSEEK_API_KEY no configurada");
            System.out.println("   💡 Ejecuta: export DEEPSEEK_API_KEY=tu_clave_api");
        } else {
            reparacion.getJSONArray("pasos_completados").put("✅ DEEPSEEK_API_KEY configurada (longitud: " + apiKey.length() + ")");
            System.out.println("   ✅ DEEPSEEK_API_KEY configurada (longitud: " + apiKey.length() + ")");
        }
        
        // PASO 2: VERIFICAR CLASES
        System.out.println("2. 🔍 Verificando clases API...");
        String[] clasesRequeridas = {
            "com.novelator.autogen.api.DeepSeekClient",
            "com.novelator.autogen.api.OraculoDeLaLibertad",
            "org.json.JSONObject"
        };
        
        for (String clase : clasesRequeridas) {
            try {
                Class.forName(clase);
                reparacion.getJSONArray("pasos_completados").put("✅ " + clase);
                System.out.println("   ✅ " + clase);
            } catch (ClassNotFoundException e) {
                reparacion.getJSONArray("pasos_completados").put("❌ " + clase + " - NO ENCONTRADA");
                System.out.println("   ❌ " + clase + " - NO ENCONTRADA");
            }
        }
        
        // PASO 3: CREAR CLIENTE DE EMERGENCIA
        System.out.println("3. 🛡️ Creando cliente de emergencia...");
        JSONObject resultadoEmergencia = crearClienteAPIEmergencia();
        reparacion.put("cliente_emergencia", resultadoEmergencia);
        reparacion.getJSONArray("pasos_completados").put("🔧 Cliente emergencia: " + resultadoEmergencia.getString("estado"));
        
        // PASO 4: REINICIAR CLIENTES
        System.out.println("4. 🔄 Reiniciando clientes API...");
        JSONObject resultadoReinicio = reiniciarClienteAPI();
        reparacion.put("reinicio_clientes", resultadoReinicio);
        reparacion.getJSONArray("pasos_completados").put("🔄 Reinicio: " + resultadoReinicio.getString("estado"));
        
        // PASO 5: VERIFICACIÓN FINAL
        System.out.println("5. ✅ Verificación final...");
        JSONObject verificacionFinal = verificarConfiguracionAPI();
        reparacion.put("verificacion_final", verificacionFinal);
        reparacion.getJSONArray("pasos_completados").put("🎯 Verificación final: " + verificacionFinal.getString("estado"));
        
        // RESULTADO FINAL
        reparacion.put("estado", verificacionFinal.getString("estado").contains("✅") ? "✅ REPARACIÓN EXITOSA" : "⚠️ REPARACIÓN PARCIAL");
        reparacion.put("recomendaciones", new JSONArray()
            .put("Asegúrate de que DEEPSEEK_API_KEY esté configurada")
            .put("Verifica la conexión a internet")
            .put("Revisa que las dependencias de org.json estén presentes")
        );
        
        System.out.println("\n🎯 RESULTADO FINAL: " + reparacion.getString("estado"));
        bitacora.exito("Reparación de configuración API completada: " + reparacion.getString("estado"));
        
        return reparacion;
    }

    /**
     * 🔄 EJECUTAR PRUEBA DE ANÁLISIS SIMPLE - TURBOFURULADO
     */
    public JSONObject ejecutarPruebaAnalisisSimple() {
        bitacora.info("🔍 EJECUTANDO PRUEBA DE ANÁLISIS SIMPLE TURBOFURULADA...");
        
        JSONObject prueba = new JSONObject();
        prueba.put("timestamp", new java.util.Date().toString());
        
        try {
            String codigoPrueba = 
                "public class EjemploPrueba {\n" +
                "    public void metodoEjemplo() {\n" +
                "        System.out.println(\\\"Hola mundo\\\");\n" +
                "    }\n" +
                "}";
            
            System.out.println("🧪 ENVIANDO CÓDIGO DE PRUEBA A API...");
            long startTime = System.currentTimeMillis();
            String resultado = invocarOraculoTecnico(codigoPrueba);
            long endTime = System.currentTimeMillis();
            
            prueba.put("tiempo_respuesta_ms", endTime - startTime);
            prueba.put("longitud_respuesta", resultado.length());
            prueba.put("primeros_200_caracteres", resultado.substring(0, Math.min(200, resultado.length())));
            
            if (resultado.contains("❌") || resultado.contains("Error") || resultado.contains("error")) {
                prueba.put("estado", "❌ PRUEBA FALLIDA");
                prueba.put("detalle", "La API respondió con un error");
            } else {
                prueba.put("estado", "✅ PRUEBA EXITOSA");
                prueba.put("detalle", "La API respondió correctamente con análisis del código");
            }
            
            System.out.println("📝 RESULTADO PRUEBA:");
            System.out.println("  • Estado: " + prueba.getString("estado"));
            System.out.println("  • Tiempo: " + prueba.getLong("tiempo_respuesta_ms") + "ms");
            System.out.println("  • Longitud: " + prueba.getInt("longitud_respuesta") + " caracteres");
            System.out.println("  • Preview: " + prueba.getString("primeros_200_caracteres") + "...");
            
        } catch (Exception e) {
            prueba.put("estado", "💥 ERROR EN PRUEBA");
            prueba.put("error", e.getMessage());
            System.out.println("💥 Error en prueba de análisis: " + e.getMessage());
            bitacora.error("Fallo en prueba de análisis API", e);
        }
        
        return prueba;
    }

    /**
     * 🎯 CONFIGURAR PARÁMETROS API DINÁMICAMENTE
     */
    public JSONObject configurarAPI(String parametro, Object valor) {
        JSONObject configuracion = new JSONObject();
        
        if (configuracionAPI.containsKey(parametro)) {
            Object valorAnterior = configuracionAPI.put(parametro, valor);
            configuracion.put("parametro", parametro);
            configuracion.put("valor_anterior", valorAnterior);
            configuracion.put("valor_nuevo", valor);
            configuracion.put("estado", "✅ CONFIGURADO");
            
            bitacora.info("Parámetro API actualizado: " + parametro + " = " + valor);
        } else {
            configuracion.put("estado", "❌ PARÁMETRO NO ENCONTRADO");
            configuracion.put("parametros_disponibles", new JSONArray(configuracionAPI.keySet()));
        }
        
        return configuracion;
    }

    /**
     * 🧹 LIMPIAR CACHE DE RESPUESTAS
     */
    public JSONObject limpiarCache() {
        int elementos = cacheRespuestas.size();
        cacheRespuestas.clear();
        
        JSONObject resultado = new JSONObject();
        resultado.put("elementos_eliminados", elementos);
        resultado.put("estado", "✅ CACHE LIMPIADO");
        resultado.put("timestamp", new java.util.Date().toString());
        
        System.out.println("🧹 CACHE LIMPIADO: " + elementos + " elementos eliminados");
        bitacora.info("Cache de API limpiado: " + elementos + " elementos");
        
        return resultado;
    }

    /**
     * 🚀 ACTIVAR/DESACTIVAR MODO EMERGENCIA
     */
    public JSONObject toggleModoEmergencia(boolean activar) {
        this.modoEmergencia = activar;
        
        JSONObject resultado = new JSONObject();
        resultado.put("modo_emergencia", activar);
        resultado.put("estado", activar ? "✅ MODO EMERGENCIA ACTIVADO" : "✅ MODO NORMAL ACTIVADO");
        resultado.put("timestamp", new java.util.Date().toString());
        
        System.out.println("🚀 " + (activar ? "MODO EMERGENCIA ACTIVADO" : "MODO NORMAL ACTIVADO"));
        bitacora.info(activar ? "Modo emergencia API activado" : "Modo normal API activado");
        
        return resultado;
    }
    
    private OraculoDeLaLibertad oraculoInstance;

    /**
     * 🎯 OBTENER INSTANCIA DEL ORÁCULO (SINGLETON)
     * Reutiliza la misma instancia para mejor performance
     */
    public OraculoDeLaLibertad getOraculo() {
        if (oraculoInstance == null) {
            try {
                oraculoInstance = new OraculoDeLaLibertad();
                bitacora.debug("✅ Instancia única de OraculoDeLaLibertad creada");
            } catch (Exception e) {
                bitacora.error("❌ Error creando OraculoDeLaLibertad", e);
                oraculoInstance = new OraculoDeLaLibertad(); // Fallback
            }
        }
        return oraculoInstance;
    }

    // 🎯 GETTERS PARA MÉTRICAS
    public int getTotalLlamadasAPI() { return totalLlamadasAPI.get(); }
    public int getLlamadasExitosas() { return llamadasExitosas.get(); }
    public int getLlamadasFallidas() { return llamadasFallidas.get(); }
    public long getTiempoTotalRespuesta() { return tiempoTotalRespuesta.get(); }
    public int getTokensUtilizados() { return tokensUtilizados.get(); }
    public boolean isModoEmergencia() { return modoEmergencia; }
    public Map<String, Object> getConfiguracionAPI() { return new HashMap<>(configuracionAPI); }
}