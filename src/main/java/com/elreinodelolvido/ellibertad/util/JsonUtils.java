package com.elreinodelolvido.ellibertad.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.elreinodelolvido.ellibertad.model.ClassInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 🏴‍☠️ JSON_UTILS_TURBO — ¡SERIALIZACIÓN MÁS RÁPIDA QUE UN PIRATA ESCAPANDO!
 * ⚡ Convierte objetos a JSON y viceversa con poder turbo.
 */
public class JsonUtils {

    // 🎯 OBJECT_MAPER TURBO - Configurado para máxima velocidad
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules(); // ⚡ Para manejar tipos modernos

    // 🚀 CACHE TURBO - Porque serializar dos veces es de cobardes
    private static final Map<Object, String> CACHE_SERIALIZACION = new ConcurrentHashMap<>();
    private static final Map<String, Object> CACHE_DESERIALIZACION = new ConcurrentHashMap<>();
    
    // 📊 ESTADÍSTICAS ÉPICAS
    private static final java.util.concurrent.atomic.AtomicInteger CONTADOR_SERIALIZACIONES = 
        new java.util.concurrent.atomic.AtomicInteger(0);
    private static final java.util.concurrent.atomic.AtomicInteger CONTADOR_DESERIALIZACIONES = 
        new java.util.concurrent.atomic.AtomicInteger(0);


    /**
     * 🎯 SERIALIZACIÓN TURBO SIN INDENTACIÓN (para producción)
     */
    public static String toJsonCompact(Object object) {
        try {
            return OBJECT_MAPPER.writer().without(SerializationFeature.INDENT_OUTPUT)
                              .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("💥 Error serializando JSON compacto", e);
        }
    }

    /**
     * 📁 GUARDAR JSON A ARCHIVO TURBO
     */
    public static void saveToFile(Object object, String filePath) {
        try {
            String json = toJson(object);
            FileUtils.writeToFile(filePath, json);
            System.out.println("💾 JSON turbo-guardado en: " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("💥 Error guardando JSON a archivo: " + filePath, e);
        }
    }

    /**
     * 📖 LEER JSON DESDE ARCHIVO TURBO
     */
    public static <T> T loadFromFile(String filePath, Class<T> clazz) {
        try {
            String json = FileUtils.readFile(filePath);
            return fromJson(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("💥 Error cargando JSON desde archivo: " + filePath, e);
        }
    }

    /**
     * 🎪 CONVERTIR MAP A JSON TURBO
     */
    public static String mapToJson(Map<String, Object> map) {
        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("💥 Error convirtiendo Map a JSON", e);
        }
    }

    /**
     * 🎯 CONVERTIR JSON A MAP TURBO
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonToMap(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("💥 Error convirtiendo JSON a Map", e);
        }
    }

    /**
     * 🔍 VALIDAR JSON TURBO
     */
    public static boolean isValidJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return false;
        }
        try {
            OBJECT_MAPPER.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 🎨 PRETTIFY JSON TURBO (formatear JSON feo)
     */
    public static String prettifyJson(String uglyJson) {
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(uglyJson);
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (Exception e) {
            System.err.println("💥 Error formateando JSON: " + e.getMessage());
            return uglyJson; // 🏴‍☠️ Si falla, devolver el original
        }
    }

    /**
     * 🚀 SERIALIZACIÓN TURBO PARA DEBUG
     */
    public static String toDebugJson(Object object) {
        try {
            String json = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            // 🎯 LIMITAR TAMAÑO PARA DEBUG
            return json.length() > 5000 ? json.substring(0, 5000) + "\n... [JSON TRUNCADO]" : json;
        } catch (JsonProcessingException e) {
            return "💥 ERROR SERIALIZANDO: " + e.getMessage();
        }
    }

    /**
     * 📊 ESTADÍSTICAS TURBO-JSON
     */
    public static void mostrarEstadisticasEpicas() {
        System.out.println("\n📊 ESTADÍSTICAS TURBO-JSON:");
        System.out.println("⚡ Serializaciones: " + CONTADOR_SERIALIZACIONES.get());
        System.out.println("🔁 Deserializaciones: " + CONTADOR_DESERIALIZACIONES.get());
        System.out.println("💾 Cache serialización: " + CACHE_SERIALIZACION.size());
        System.out.println("💾 Cache deserialización: " + CACHE_DESERIALIZACION.size());
        System.out.println("🎯 Eficiencia cache: " + 
            String.format("%.1f%%", ((double)(CACHE_SERIALIZACION.size() + CACHE_DESERIALIZACION.size()) / 
            (CONTADOR_SERIALIZACIONES.get() + CONTADOR_DESERIALIZACIONES.get())) * 100));
    }

    /**
     * 🧹 LIMPIAR CACHES TURBO
     */
    public static void limpiarCaches() {
        int serializaciones = CACHE_SERIALIZACION.size();
        int deserializaciones = CACHE_DESERIALIZACION.size();
        
        CACHE_SERIALIZACION.clear();
        CACHE_DESERIALIZACION.clear();
        
        System.out.println("🗑️ Caches JSON turbo-limpios (" + serializaciones + " serial, " + 
                          deserializaciones + " deserial)");
    }

    /**
     * 🆕 CONVERTIR LISTA DE CLASS_INFO A JSON TURBO
     */
    public static String classInfoListToJson(List<ClassInfo> classes) {
        try {
            Map<String, Object> simplified = new LinkedHashMap<>();
            simplified.put("total_clases", classes.size());
            simplified.put("timestamp", new Date().toString());
            
            List<Map<String, String>> clasesData = new ArrayList<>();
            for (ClassInfo clase : classes) {
                Map<String, String> claseMap = new LinkedHashMap<>();
                claseMap.put("nombre", clase.getName());
                claseMap.put("paquete", clase.getPackageName());
                claseMap.put("ruta", clase.getSourcePath());
                clasesData.add(claseMap);
            }
            simplified.put("clases", clasesData);
            
            return toJson(simplified);
        } catch (Exception e) {
            return "{\"error\": \"💥 Error serializando ClassInfo: " + e.getMessage() + "\"}";
        }
    }

    /**
     * 🎪 MÉTODO TURBO: JSON CON TIMESTAMP AUTOMÁTICO
     */
    public static String toJsonWithTimestamp(Object object) {
        try {
            if (object instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) object;
                map.put("_timestamp", System.currentTimeMillis());
                map.put("_timestamp_readable", new Date().toString());
                return toJson(map);
            } else {
                Map<String, Object> wrapper = new LinkedHashMap<>();
                wrapper.put("data", object);
                wrapper.put("_timestamp", System.currentTimeMillis());
                wrapper.put("_timestamp_readable", new Date().toString());
                return toJson(wrapper);
            }
        } catch (Exception e) {
            return "{\"error\": \"💥 Error agregando timestamp: " + e.getMessage() + "\"}";
        }
    }
    
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

        public static String toJsonWithTimestamp(List<?> data) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("timestamp", LocalDateTime.now().toString());
            result.put("data", data);
            result.put("count", data.size());
            return GSON.toJson(result);
        }

        public static String toJson(Object obj) {
            return GSON.toJson(obj);
        }

        public static <T> T fromJson(String json, Class<T> classOfT) {
            return GSON.fromJson(json, classOfT);
        }
    }
