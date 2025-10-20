package com.elreinodelolvido.ellibertad.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import com.elreinodelolvido.ellibertad.model.ClassInfo;

/**
 * ☠️ DICCIONARIO_TURBO — ¡LA LEGIÓN DE ALMAS PIRATAS MÁS ÉPICA DEL CARAJO!
 * 🏴‍☠️ Registro viviente de maldiciones, blasfemias y alias satánicos para tu código.
 * ⚡ AHORA CON +500% DE BLASFEMIAS POR SEGUNDO Y 0% NULLPOINTERS!
 */
public class DiccionarioDeTripulacion {

    private static final Map<String, String> NOMBRES_LEGENDARIOS = new ConcurrentHashMap<>();
    private static final Map<String, String> MALDICIONES_ASIGNADAS = new ConcurrentHashMap<>();
    private static final Set<String> ALMAS_CONDENADAS = Collections.newSetFromMap(new ConcurrentHashMap<>());
    
    // 🎯 CACHE TURBO - Porque preguntar dos veces es de cobardes
    private static final Map<String, String> CACHE_INFERNAL = new ConcurrentHashMap<>();

    // 🏴‍☠️ LEGIÓN DE BLASFEMIAS EPICAS
    private static final List<String> BLASFEMIAS_COSMICAS = Arrays.asList(
        "El Tuerto del Linter", "Madre Tormenta", "El Calderero del Backend",
        "La Bruma de la Cache", "El Vigía de los Commits", "El Escriba del Token",
        "La Sombra del Stacktrace", "La Voz del Compilador", "El Rumor del Bug Fantasma",
        "El Cazador de Referencias", "La Llama del Singleton", "El Navegante del Refactor",
        "La Veleta del Logging", "El Guardián del Puerto 8080", "El Kraken del Parsing",
        "La Rosa de los Tests", "El Carpintero del JSON", "El Silencio del Catch",
        "El Eco de los GET", "El Mapa del Path", "La Niebla del Merge",
        // 🚀 NUEVAS BLASFEMIAS TURBO
        "El Borracho del NullPointer", "La Maldición del StackOverflow", 
        "El Endemoniado del Infinite Loop", "La Furia del ConcurrentModification",
        "El Poseído del Memory Leak", "La Bruja del ClassCast", 
        "El Zombie del Deadlock", "El Fantasma del Garbage Collector",
        "El Demonio del Bytecode", "La Pesadilla del Reflection",
        "El Caos del Multithreading", "El Apocalipsis del OutOfMemory",
        "El Infierno del SQL Injection", "El Vacío del 404",
        "El Terror del 500", "La Ira del Timeout",
        "El Abismo del Buffer Overflow", "La Perdición del Race Condition"
    );

    // 👑 DIOSES Y DEMONIOS FIJOS - ¡LOS MÁS ÉPICOS!
    static {
        NOMBRES_LEGENDARIOS.put("AutogenMain", "⚡ CTHULHU - El Dios del Caos Codeado");
        NOMBRES_LEGENDARIOS.put("DeepSeekClient", "🔮 EL ORÁCULO SATÁNICO");
        NOMBRES_LEGENDARIOS.put("ResumenProyecto", "👑 LA REINA DEL APOCALIPSIS");
        NOMBRES_LEGENDARIOS.put("GeneradorClasesNuevas", "🧙 CIRCE - La Bruja del Bytecode");
        NOMBRES_LEGENDARIOS.put("RollbackManager", "⏳ CARONTE - El Barquero del Git Reset");
        NOMBRES_LEGENDARIOS.put("ValidadorFirmas", "💂 WINSTON CHURCHILL - El Defensor de las Firmas");
        NOMBRES_LEGENDARIOS.put("RevisorInteractivo", "🎭 BERTO ROMERO - El Comediante del Code Review");
        NOMBRES_LEGENDARIOS.put("JsonUtils", "🧽 BOB ESPONJA - El Absorbedor de JSONs");
        NOMBRES_LEGENDARIOS.put("DiffUtil", "⭐ PATRICIO - La Estrella de los Diffs");
        NOMBRES_LEGENDARIOS.put("AuditorSistema", "🗡️ YAMAMOTO - El Samurái del Logging");
        NOMBRES_LEGENDARIOS.put("FileUtils", "💻 MAC - El Dios de los Files");
        NOMBRES_LEGENDARIOS.put("ConsolaDeTripulacion", "🎮 NEO - El Elegido del Dialogo");
        NOMBRES_LEGENDARIOS.put("OraculoTecnico", "🔭 GALILEO - El Visionario Técnico");
        NOMBRES_LEGENDARIOS.put("PromptCache", "🌫️ LA BRUMA INFINITA");
        
        // ⚡ CARGA TURBO REPARADA - ¡SIN NULLPOINTERS!
        cargarMaldicionesPersistentes();
    }

    // 🎯 MÉTODO PRINCIPAL TURBO-EXTREME (SIN CAMBIOS)
    public static String nombrePirata(ClassInfo clase) {
        return nombrePirata(clase.getName());
    }

    public static String nombrePirata(String nombreClase) {
        // ⚡ CACHE INFERNAL - ¡NADA DE PENSAR DOS VECES!
        return CACHE_INFERNAL.computeIfAbsent(nombreClase, k -> 
            NOMBRES_LEGENDARIOS.getOrDefault(k, 
                MALDICIONES_ASIGNADAS.getOrDefault(k, 
                    generarMaldicionEpica(k)
                )
            )
        );
    }

    // 🎪 GENERADOR DE MALDICIONES ÉPICAS (SIN CAMBIOS)
    private static String generarMaldicionEpica(String nombreClase) {
        // 🎲 SELECCIÓN SATÁNICA - Aleatoriedad cósmica
        String blasfemia = BLASFEMIAS_COSMICAS.get(
            ThreadLocalRandom.current().nextInt(BLASFEMIAS_COSMICAS.size())
        );
        
        // 🔥 INYECCIÓN DE CAOS - 30% de chance de turbo-modificación
        if (ThreadLocalRandom.current().nextDouble() < 0.3) {
            blasfemia = turboModificarBlasfemia(blasfemia);
        }
        
        // 💾 REGISTRO EN EL INFIERNO
        MALDICIONES_ASIGNADAS.put(nombreClase, blasfemia);
        ALMAS_CONDENADAS.add(blasfemia);
        
        // 💀 PERSISTENCIA SATÁNICA
        guardarMaldicionesEnElAbismo();
        
        System.out.println("👹 ¡NUEVA MALDICIÓN: " + nombreClase + " → " + blasfemia + "!");
        return blasfemia;
    }

    // 🎭 MODIFICADOR TURBO-DELIRANTE (SIN CAMBIOS)
    private static String turboModificarBlasfemia(String original) {
        String[] modificadores = {"Ultra", "Mega", "Hyper", "Super", "Omega", "Alpha", "Beta", "Gamma", "Delta"};
        String[] sufijos = {" del Apocalipsis", " Satánico", " Infernal", " Cósmico", " Divino", " Maldito"};
        
        String mod = modificadores[ThreadLocalRandom.current().nextInt(modificadores.length)];
        String suf = sufijos[ThreadLocalRandom.current().nextInt(sufijos.length)];
        
        return mod + " " + original + suf;
    }

    // 🗺️ MAPA COMPLETO DEL INFIERNO (SIN CAMBIOS)
    public static Map<String, String> getMapaTripulacion() {
        Map<String, String> infiernoCompleto = new LinkedHashMap<>(NOMBRES_LEGENDARIOS);
        infiernoCompleto.putAll(MALDICIONES_ASIGNADAS);
        return Collections.unmodifiableMap(infiernoCompleto);
    }

    // 📊 ESTADÍSTICAS SATÁNICAS (SIN CAMBIOS)
    public static void mostrarEstadisticasEpicas() {
        System.out.println("\n📊 ESTADÍSTICAS DEL INFIERNO:");
        System.out.println("👑 Dioses legendarios: " + NOMBRES_LEGENDARIOS.size());
        System.out.println("👹 Almas condenadas: " + MALDICIONES_ASIGNADAS.size());
        System.out.println("🌫️ Blasfemias disponibles: " + BLASFEMIAS_COSMICAS.size());
        System.out.println("⚡ Cache infernal: " + CACHE_INFERNAL.size());
        System.out.println("💀 Tasa de condenación: " + 
            String.format("%.1f%%", (double) MALDICIONES_ASIGNADAS.size() / 
            (NOMBRES_LEGENDARIOS.size() + MALDICIONES_ASIGNADAS.size()) * 100));
    }

    // 💾 PERSISTENCIA EN EL ABISMO - ¡REPARADA CONTRA NULLPOINTERS!
    private static final String RUTA_ABISMO = "autogen-output/tripulacion/maldiciones-eternas.json";

    private static void guardarMaldicionesEnElAbismo() {
        try {
            // 🛡️ VERIFICACIÓN CONTRA NULLS - ¡EL INFIERNO ES SEGURO!
            Path abismoEternal = obtenerAbismoSeguro();
            if (abismoEternal == null) {
                System.err.println("😈 EL ABISMO SE NEGÓ A EXISTIR - Maldiciones en memoria solamente");
                return;
            }
            
            Files.createDirectories(abismoEternal.getParent());
            
            // 🚀 JSON TURBO - Sin dependencias externas, puro caos
            String jsonInfernal = "{\n" +
                "  \"ultima_actualizacion\": \"" + new Date() + "\",\n" +
                "  " + "\"maldiciones_asignadas\": " + MALDICIONES_ASIGNADAS.toString() + "\n" +
                "}";
            Files.write(abismoEternal, jsonInfernal.getBytes(), 
                       StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            System.out.println("💾 Maldiciones guardadas en el abismo: " + MALDICIONES_ASIGNADAS.size());
        } catch (IOException e) {
            System.err.println("😈 EL INFIERNO ESTÁ LLENO: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("💥 CAOS CÓSMICO EN EL GUARDADO: " + e.getMessage());
        }
    }

    private static void cargarMaldicionesPersistentes() {
        try {
            // 🛡️ VERIFICACIÓN ROBUSTA CONTRA NULLS
            Path abismoEternal = obtenerAbismoSeguro();
            if (abismoEternal == null || !Files.exists(abismoEternal)) {
                System.out.println("🌪️ El abismo está vacío - comenzando con maldiciones frescas");
                return;
            }
            
            // 🎪 PARSEO MANUAL - ¡QUE SE JODAN LAS DEPENDENCIAS!
            String contenido = new String(Files.readAllBytes(abismoEternal));
            if (contenido.contains("\"maldiciones_asignadas\": {")) {
                int start = contenido.indexOf("{", contenido.indexOf("\"maldiciones_asignadas\":"));
                int end = contenido.lastIndexOf("}") + 1;
                String maldicionesJson = contenido.substring(start, end);
                
                // 🎯 PARSEO TURBO-DELIRANTE
                maldicionesJson = maldicionesJson.replace("{", "").replace("}", "");
                String[] entries = maldicionesJson.split(",");
                
                for (String entry : entries) {
                    String[] parts = entry.split("=");
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        MALDICIONES_ASIGNADAS.put(key, value);
                        ALMAS_CONDENADAS.add(value);
                    }
                }
            }
            System.out.println("👹 " + MALDICIONES_ASIGNADAS.size() + " maldiciones cargadas del abismo!");
        } catch (Exception e) {
            System.err.println("💥 EL INFIERNO SE CORROMPIÓ: " + e.getMessage());
            System.out.println("🔄 Continuando con maldiciones en memoria...");
        }
    }

    // 🛡️ MÉTODO NUEVO: OBTENER ABISMO SEGURO - ¡CERO NULLPOINTERS!
    private static Path obtenerAbismoSeguro() {
        try {
            // 🎯 MÚLTIPLES ESTRATEGIAS CONTRA NULLS
            Path abismo = Paths.get(RUTA_ABISMO);
            
            // Verificar que el Path es válido
            if (abismo == null) {
                System.err.println("⚠️  Paths.get() retornó null - usando estrategia de respaldo");
                return crearAbismoRespaldo();
            }
            
            // Verificar que el filesystem es válido
            if (abismo.getFileSystem() == null) {
                System.err.println("⚠️  Filesystem es null - usando estrategia de respaldo");
                return crearAbismoRespaldo();
            }
            
            return abismo;
            
        } catch (Exception e) {
            System.err.println("💥 Error catastrófico obteniendo abismo: " + e.getMessage());
            return crearAbismoRespaldo();
        }
    }
    
    // 🎯 ESTRATEGIA DE RESPLANDO - ¡NUNCA FALLARÁ!
    private static Path crearAbismoRespaldo() {
        try {
            // Usar el directorio de trabajo actual como respaldo
            Path directorioActual = Paths.get("").toAbsolutePath();
            Path abismoRespaldo = directorioActual.resolve("autogen-output/tripulacion/maldiciones-respaldo.json");
            
            System.out.println("🛡️  Usando abismo de respaldo: " + abismoRespaldo);
            return abismoRespaldo;
            
        } catch (Exception e) {
            System.err.println("💥 CRÍTICO: No se pudo crear abismo de respaldo");
            // En el peor caso, retornamos null pero manejamos elegantemente
            return null;
        }
    }

    // 🎪 MÉTODO EXTRA: GENERAR NOMBRE ALEATORIO PARA CUALQUIER COSA (SIN CAMBIOS)
    public static String maldecirAleatoriamente() {
        return BLASFEMIAS_COSMICAS.get(
            ThreadLocalRandom.current().nextInt(BLASFEMIAS_COSMICAS.size())
        );
    }

    public static String obtenerNombrePirata(String name) {
        return nombrePirata(name);
    }
    
    // 🆕 MÉTODO NUEVO: VERIFICAR SALUD DEL DICCIONARIO
    public static void verificarSaludInfernal() {
        System.out.println("\n🏥 DIAGNÓSTICO DEL INFIERNO:");
        System.out.println("✅ Nombres legendarios: " + NOMBRES_LEGENDARIOS.size());
        System.out.println("✅ Maldiciones asignadas: " + MALDICIONES_ASIGNADAS.size());
        System.out.println("✅ Cache infernal: " + CACHE_INFERNAL.size());
        System.out.println("✅ Blasfemias disponibles: " + BLASFEMIAS_COSMICAS.size());
        
        Path abismo = obtenerAbismoSeguro();
        if (abismo != null && Files.exists(abismo)) {
            System.out.println("✅ Abismo persistente: OPERATIVO");
        } else {
            System.out.println("⚠️  Abismo persistente: NO DISPONIBLE (modo memoria)");
        }
        
        System.out.println("🎯 Estado general: ¡INFIERNO OPERATIVO!");
    }
}