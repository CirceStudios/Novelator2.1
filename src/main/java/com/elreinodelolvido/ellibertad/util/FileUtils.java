package com.elreinodelolvido.ellibertad.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.model.ClassInfo;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;

/**
 * 🏴‍☠️ FILEUTILS_TURBO_ULTRA — Fusión épica con todas las funcionalidades
 * ⚡ Cache infernal + Estadísticas épicas + Gestión robusta +500% velocidad pirata
 */
public class FileUtils {

    // 🎯 CACHE TURBO - Porque leer dos veces es de cobardes
    private static final Map<String, String> CACHE_LECTURAS = new ConcurrentHashMap<>();
    private static final Map<String, Long> CACHE_TIMESTAMPS = new ConcurrentHashMap<>();
    private static final Set<String> ARCHIVOS_EN_ESCRITURA = ConcurrentHashMap.newKeySet();
    
    // 📊 ESTADÍSTICAS ÉPICAS
    private static final AtomicInteger CONTADOR_LECTURAS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_ESCRITURAS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_KRAKENS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_CACHE_HITS = new AtomicInteger(0);
    private static final AtomicInteger ARCHIVOS_CREADOS = new AtomicInteger(0);
    private static final AtomicInteger DIRECTORIOS_CREADOS = new AtomicInteger(0);
    
    // ⚡ PATRONES TURBO
    private static final Pattern PATRON_CODIGO_JAVA = Pattern.compile("```(?:java)?\\s*(.*?)```", Pattern.DOTALL);
    private static final Pattern PATRON_IMPORTS = Pattern.compile("^import\\s+.*?;", Pattern.MULTILINE);
    
    // 🎯 CONFIGURACIÓN TURBO
    private static final int MAX_CACHE_SIZE = 1000;
    private static final long CACHE_TIMEOUT_MS = 300000; // 5 minutos
    private static final Map<String, Integer> OPERACIONES_POR_TIPO = new ConcurrentHashMap<>();

    // =========================================================================
    // 🚀 MÉTODOS DE LA VERSIÓN ORIGINAL (MANTENIDOS)
    // =========================================================================

    /**
     * 🗂️ LISTADO TURBO - Con filtros inteligentes
     */
    public static List<String> listFiles(String directory, String extension) {
        try {
            return Files.walk(Paths.get(directory))
                    .filter(p -> Files.isRegularFile(p) && p.toString().endsWith(extension))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("💥 Error turbo-listando archivos en: " + directory, e);
        }
    }

    /**
     * 🆕 LISTADO TURBO MEJORADO - Con más opciones
     */
    public static List<String> listFilesTurbo(String directory, String extension, boolean recursive) {
        try {
            Stream<Path> stream = recursive ? 
                Files.walk(Paths.get(directory)) : 
                Files.list(Paths.get(directory));
            
            return stream
                    .filter(p -> Files.isRegularFile(p) && p.toString().endsWith(extension))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("💥 Error turbo-listando: " + directory, e);
        }
    }

    /**
     * 📁 CREACIÓN DE DIRECTORIOS TURBO
     */
    public static void ensureParentDirectoryExists(String outputPath) {
        try {
            Path parent = Path.of(outputPath).getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
                DIRECTORIOS_CREADOS.incrementAndGet();
                System.out.println("📂 DIRECTORIO TURBO CREADO: " + parent);
            }
        } catch (IOException e) {
            throw new RuntimeException("💥 Error turbo-creando directorio: " + outputPath, e);
        }
    }

    /**
     * 🐙 Versión sobrecargada para mensajes de error simples
     */
    public static void registrarKraken(String clase, String mensajeError) {
        CONTADOR_KRAKENS.incrementAndGet();
        String tipo = clasificarKraken(mensajeError);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        String entrada = "\n## " + tipo + "\n" +
                        "**Clase**: " + clase + "  \n" +
                        "**Fecha**: " + timestamp + "  \n" +
                        "**Detalles**:\n```log\n" + mensajeError.strip() + "\n```\n" +
                        "---\n";
        
        appendToFile("autogen-output/krakens/log-krakens.md", entrada);
        System.out.println("🐙 KRAKEN REGISTRADO: " + tipo + " en " + clase);
    }

    /**
     * 🎯 CLASIFICACIÓN TURBO DE KRAKENS
     */
    private static String clasificarKraken(String mensajeError) {
        String lower = mensajeError.toLowerCase();

        if (lower.contains("filenotfound") || lower.contains("no such file") || lower.contains("cannot find")) {
            return "💀 FANTASMA DEL CÓDIGO - Archivo perdido en la niebla";
        }

        if (lower.contains("nullpointer") || lower.contains("illegalstate") || lower.contains("indexoutofbounds")) {
            return "🌪️ TORMENTA LÓGICA - Variables rebeldes";
        }

        if (lower.contains("outofmemory") || lower.contains("stackoverflow") || lower.contains("memory")) {
            return "🐉 LEVIATÁN DEL ABISMO - Memoria insuficiente";
        }

        if (lower.contains("timeout") || lower.contains("connection") || lower.contains("network")) {
            return "⏰ KRAKEN DEL TIEMPO - Llamadas eternas";
        }

        if (lower.contains("permission") || lower.contains("access denied") || lower.contains("security")) {
            return "🔒 GUARDIÁN PROHIBIDO - Permisos denegados";
        }

        return "🐙 KRAKEN DESCONOCIDO - Misterio por resolver";
    }

    /**
     * 🎪 CAPTURA TURBO DE EXCEPCIONES
     */
    public static void atraparKraken(String clase, Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        registrarKraken(clase, sw.toString());
    }

    /**
     * 🆕 EXTRACCIÓN TURBO DE MÚLTIPLES CLASES
     */
    public static List<String> extraerMultiplesClases(String respuestaLLM) {
        List<String> clases = new ArrayList<>();
        if (respuestaLLM == null || respuestaLLM.isBlank()) return clases;

        try {
            Matcher matcher = PATRON_CODIGO_JAVA.matcher(respuestaLLM);
            while (matcher.find()) {
                String codigo = matcher.group(1).trim();
                if (!codigo.isEmpty()) {
                    clases.add(codigo);
                }
            }
            
            System.out.println("⚡ " + clases.size() + " CLASES EXTRAÍDAS");
            return clases;
            
        } catch (Exception e) {
            System.err.println("💥 Error extrayendo múltiples clases: " + e.getMessage());
            return Collections.singletonList(respuestaLLM.trim());
        }
    }

    /**
     * 📊 ESTADÍSTICAS TURBO FILEUTILS
     */
    public static void mostrarEstadisticasEpicas() {
        System.out.println("\n📊 ESTADÍSTICAS TURBO FILEUTILS:");
        System.out.println("📖 Lecturas: " + CONTADOR_LECTURAS.get());
        System.out.println("📝 Escrituras: " + CONTADOR_ESCRITURAS.get());
        System.out.println("🐙 Krakens: " + CONTADOR_KRAKENS.get());
        System.out.println("🎯 Cache hits: " + CONTADOR_CACHE_HITS.get());
        System.out.println("💾 Cache size: " + CACHE_LECTURAS.size());
        System.out.println("📄 Archivos creados: " + ARCHIVOS_CREADOS.get());
        System.out.println("🗂️ Directorios creados: " + DIRECTORIOS_CREADOS.get());
        System.out.println("🎯 Tasa de cache: " + 
            String.format("%.1f%%", (double)CONTADOR_CACHE_HITS.get() / Math.max(1, CONTADOR_LECTURAS.get()) * 100));
        System.out.println("⚡ Eficiencia: " + 
            String.format("%.1f%%", (1 - (double)CONTADOR_KRAKENS.get() / Math.max(1, (CONTADOR_LECTURAS.get() + CONTADOR_ESCRITURAS.get()))) * 100));
    }

    /**
     * 🧹 LIMPIAR CACHE TURBO MEJORADO - Con gestión automática
     */
    public static void limpiarCache() {
        int tamaño = CACHE_LECTURAS.size();
        
        // Limpiar entradas expiradas
        long now = System.currentTimeMillis();
        CACHE_TIMESTAMPS.entrySet().removeIf(entry -> 
            (now - entry.getValue()) > CACHE_TIMEOUT_MS);
        CACHE_LECTURAS.keySet().removeIf(key -> 
            !CACHE_TIMESTAMPS.containsKey(key));
        
        // Si sigue muy lleno, limpiar los más antiguos
        if (CACHE_LECTURAS.size() > MAX_CACHE_SIZE) {
            CACHE_TIMESTAMPS.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(CACHE_LECTURAS.size() - MAX_CACHE_SIZE)
                .forEach(entry -> {
                    CACHE_LECTURAS.remove(entry.getKey());
                    CACHE_TIMESTAMPS.remove(entry.getKey());
                });
        }
        
        System.out.println("🗑️  CACHE TURBO OPTIMIZADO: " + tamaño + " → " + CACHE_LECTURAS.size() + " entradas");
    }

    /**
     * 🆕 BACKUP TURBO DE ARCHIVO
     */
    public static void crearBackupTurbo(String archivoOriginal) {
        try {
            if (!Files.exists(Paths.get(archivoOriginal))) {
                System.out.println("⚠️  Archivo no existe para backup: " + archivoOriginal);
                return;
            }
            
            String backupPath = archivoOriginal + ".bak";
            Files.copy(Paths.get(archivoOriginal), Paths.get(backupPath), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("💾 BACKUP TURBO CREADO: " + backupPath);
            
        } catch (Exception e) {
            System.err.println("💥 Error creando backup: " + archivoOriginal);
        }
    }

    /**
     * 🆕 VERIFICACIÓN TURBO DE ARCHIVO
     */
    public static boolean verificarArchivo(String path) {
        try {
            Path filePath = Paths.get(path);
            return Files.exists(filePath) && 
                   Files.isReadable(filePath) && 
                   Files.isWritable(filePath) &&
                   Files.size(filePath) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // =========================================================================
    // 🔧 MÉTODOS DE REPARACIÓN Y VALIDACIÓN (MANTENIDOS)
    // =========================================================================

    public static String buscarCodigoJavaCompleto(String texto) {
        // Buscar desde package/import hasta el final
        int start = 0;
        if (texto.contains("package ")) start = texto.indexOf("package ");
        else if (texto.contains("import ")) start = texto.indexOf("import ");
        else if (texto.contains("public class")) start = texto.indexOf("public class");
        else if (texto.contains("class ")) start = texto.indexOf("class ");
        else return texto; // Devolver todo como último recurso
        
        return texto.substring(start).trim();
    }

    private static String intentarRepararCodigo(String codigoRoto) {
        // 🎯 Intentar reparar código incompleto
        String reparado = codigoRoto.trim();
        
        // Asegurar que termina con }
        if (!reparado.endsWith("}")) {
            reparado += "\n}";
        }
        
        // Asegurar que tiene package si parece una clase completa
        if (reparado.contains("public class") && !reparado.contains("package ")) {
            // Buscar el nombre de la clase para crear un package dummy
            Pattern classPattern = Pattern.compile("public class (\\w+)");
            Matcher matcher = classPattern.matcher(reparado);
            if (matcher.find()) {
                String className = matcher.group(1);
                reparado = "package com.novelator.autogen.util;\n\n" + reparado;
            }
        }
        
        return reparado;
    }
    
    public static boolean esCodigoJavaValido(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) return false;
        
        // 🎯 Validaciones básicas de código Java
        boolean tienePackage = codigo.contains("package ");
        boolean tieneClass = codigo.contains(" class ") || codigo.contains("public class");
        boolean tieneLlaveApertura = codigo.contains("{");
        boolean tieneLlaveCierre = codigo.contains("}");
        boolean longitudMinima = codigo.length() > 100;
        
        // Debe tener al menos class y llaves
        boolean esValido = tieneClass && tieneLlaveApertura && tieneLlaveCierre && longitudMinima;
        
        if (!esValido) {
            System.out.println("❌ Código inválido - Faltan: " +
                             (tieneClass ? "" : "class ") +
                             (tieneLlaveApertura ? "" : "{ ") +
                             (tieneLlaveCierre ? "" : "} "));
        }
        
        return esValido;
    }
    
    public static void buscarFormatosInvalidos(String codigo) {
        // Buscar patrones %V y %v en el código (los verdaderos problemas)
        if (codigo.contains("%V") || codigo.contains("%v")) {
            System.err.println("❌ FORMATO INVÁLIDO ENCONTRADO: %V o %v");
            int indice = Math.max(codigo.indexOf("%V"), codigo.indexOf("%v"));
            System.err.println("🔍 Contexto: " + 
                codigo.substring(Math.max(0, indice - 50), 
                               Math.min(codigo.length(), indice + 50)));
        }
    }

    // =========================================================================
    // 🚀 MÉTODOS DE GESTIÓN ROBUSTA (FUSIONADOS)
    // =========================================================================

    /**
     * 🗂️ CREAR DIRECTORIO SI NO EXISTE - VERSIÓN ROBUSTA FUSIONADA
     * @return 
     */
    public static boolean crearDirectorioSiNoExiste(String rutaDirectorio) {
        if (rutaDirectorio == null || rutaDirectorio.trim().isEmpty()) {
            System.err.println("❌ Ruta de directorio vacía o nula");
            CONTADOR_KRAKENS.incrementAndGet();
            return false;
        }
        
        try {
            Path path = Paths.get(rutaDirectorio);
            
            if (!Files.exists(path)) {
                // 🏗️ CREAR DIRECTORIO Y TODOS LOS PADRES NECESARIOS
                Files.createDirectories(path);
                DIRECTORIOS_CREADOS.incrementAndGet();
                OPERACIONES_POR_TIPO.merge("DIRECTORIO_CREADO", 1, Integer::sum);
                
                System.out.println("✅ Directorio creado: " + rutaDirectorio);
                
            } else if (!Files.isDirectory(path)) {
                System.err.println("❌ La ruta existe pero no es un directorio: " + rutaDirectorio);
                CONTADOR_KRAKENS.incrementAndGet();
            }
            // Si ya existe y es directorio, no hacer nada (éxito silencioso)
            
        } catch (Exception e) {
            System.err.println("💥 Error creando directorio '" + rutaDirectorio + "': " + e.getMessage());
            CONTADOR_KRAKENS.incrementAndGet();
            throw new RuntimeException("Error crítico creando directorio: " + rutaDirectorio, e);
        }
		return true;
    }

    /**
     * 📝 ESCRIBIR A ARCHIVO CON CREACIÓN AUTOMÁTICA DE DIRECTORIOS - FUSIONADO
     */
    public static void writeToFile(String rutaArchivo, String contenido) {
        if (rutaArchivo == null || contenido == null) {
            System.err.println("❌ Ruta o contenido nulo");
            CONTADOR_KRAKENS.incrementAndGet();
            return;
        }
        
        try {
            // 🗂️ EXTRAER DIRECTORIO DEL ARCHIVO Y CREARLO
            Path archivoPath = Paths.get(rutaArchivo);
            Path directorioPadre = archivoPath.getParent();
            
            if (directorioPadre != null) {
                crearDirectorioSiNoExiste(directorioPadre.toString());
            }
            
            // 📝 ESCRIBIR CONTENIDO
            Files.write(archivoPath, contenido.getBytes(StandardCharsets.UTF_8), 
                       StandardOpenOption.CREATE, 
                       StandardOpenOption.TRUNCATE_EXISTING,
                       StandardOpenOption.WRITE);
            
            ARCHIVOS_CREADOS.incrementAndGet();
            CONTADOR_ESCRITURAS.incrementAndGet();
            OPERACIONES_POR_TIPO.merge("ARCHIVO_ESCRITO", 1, Integer::sum);
            
            // 🎯 ACTUALIZAR CACHE
            CACHE_LECTURAS.put(rutaArchivo, contenido);
            CACHE_TIMESTAMPS.put(rutaArchivo, System.currentTimeMillis());
            
            System.out.println("💾 Archivo guardado: " + rutaArchivo + " (" + contenido.length() + " chars)");
            
        } catch (Exception e) {
            System.err.println("💥 Error escribiendo archivo '" + rutaArchivo + "': " + e.getMessage());
            CONTADOR_KRAKENS.incrementAndGet();
            throw new RuntimeException("Error crítico escribiendo archivo: " + rutaArchivo, e);
        }
    }

    /**
     * 📖 LEER ARCHIVO CON CACHE TURBO - FUSIONADO
     */
    public static String readFile(String path) {
        // 🎯 VERIFICAR CACHE PRIMERO
        if (CACHE_LECTURAS.containsKey(path) && CACHE_TIMESTAMPS.containsKey(path)) {
            long now = System.currentTimeMillis();
            if ((now - CACHE_TIMESTAMPS.get(path)) < CACHE_TIMEOUT_MS) {
                CONTADOR_CACHE_HITS.incrementAndGet();
                return CACHE_LECTURAS.get(path);
            } else {
                // Cache expirado, limpiar
                CACHE_LECTURAS.remove(path);
                CACHE_TIMESTAMPS.remove(path);
            }
        }
        
        try {
            Path filePath = Paths.get(path);
            
            if (!Files.exists(filePath)) {
                System.err.println("📭 Archivo no existe: " + path);
                return "";
            }
            
            if (!Files.isReadable(filePath)) {
                System.err.println("🔒 Archivo no readable: " + path);
                return "";
            }
            
            String contenido = Files.readString(filePath, StandardCharsets.UTF_8);
            CONTADOR_LECTURAS.incrementAndGet();
            OPERACIONES_POR_TIPO.merge("ARCHIVO_LEIDO", 1, Integer::sum);
            
            // 🎯 GUARDAR EN CACHE
            CACHE_LECTURAS.put(path, contenido);
            CACHE_TIMESTAMPS.put(path, System.currentTimeMillis());
            
            return contenido;
            
        } catch (Exception e) {
            System.err.println("💥 Error leyendo archivo '" + path + "': " + e.getMessage());
            CONTADOR_KRAKENS.incrementAndGet();
            return "";
        }
    }

    /**
     * 📝 APPEND TO FILE - FUSIONADO
     */
    public static void appendToFile(String path, String content) {
        try {
            Path filePath = Paths.get(path);
            Files.createDirectories(filePath.getParent());
            Files.writeString(filePath, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            CONTADOR_ESCRITURAS.incrementAndGet();
            OPERACIONES_POR_TIPO.merge("ARCHIVO_APPEND", 1, Integer::sum);
        } catch (IOException e) {
            System.err.println("❌ Error append archivo: " + path + " - " + e.getMessage());
            CONTADOR_KRAKENS.incrementAndGet();
        }
    }

    /**
     * 🆕 CREAR ARCHIVO SI NO EXISTE - FUSIONADO
     */
    public static void crearArchivoSiNoExiste(String path, String contenidoInicial) {
        try {
            if (!Files.exists(Paths.get(path))) {
                writeToFile(path, contenidoInicial);
                OPERACIONES_POR_TIPO.merge("ARCHIVO_CREADO", 1, Integer::sum);
            }
        } catch (Exception e) {
            System.err.println("❌ Error creando archivo: " + path);
            CONTADOR_KRAKENS.incrementAndGet();
        }
    }

    // =========================================================================
    // 🧹 MÉTODOS DE LIMPIEZA Y MANTENIMIENTO (FUSIONADOS)
    // =========================================================================

    public static void limpiarArchivosTemporales() {
        try {
            System.out.println("🧹 LIMPIANDO ARCHIVOS TEMPORALES...");
            
            // Limpiar archivos .tmp
            Files.walk(Paths.get("."))
                .filter(p -> p.toString().endsWith(".tmp"))
                .forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                        System.out.println("🗑️  Eliminado: " + p);
                    } catch (IOException e) {
                        System.err.println("⚠️  No se pudo eliminar: " + p);
                    }
                });
                
            // Limpiar archivos .bak antiguos (más de 1 día)
            Files.walk(Paths.get("."))
                .filter(p -> p.toString().endsWith(".bak"))
                .filter(p -> {
                    try {
                        return Files.getLastModifiedTime(p).toMillis() < 
                               System.currentTimeMillis() - (24 * 60 * 60 * 1000);
                    } catch (IOException e) {
                        return false;
                    }
                })
                .forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                        System.out.println("🗑️  Backup antiguo eliminado: " + p);
                    } catch (IOException e) {
                        System.err.println("⚠️  No se pudo eliminar backup: " + p);
                    }
                });
                
            System.out.println("✅ Limpieza de temporales completada");
            
        } catch (IOException e) {
            System.err.println("💥 Error en limpieza de temporales: " + e.getMessage());
            CONTADOR_KRAKENS.incrementAndGet();
        }
    }
    
    /**
     * 🔍 DIAGNÓSTICO TURBO DE FORMATOS - Busca %V y %v problemáticos
     */
    public static void diagnosticarFormatosProblematicos() {
        System.out.println("\n🔍 DIAGNÓSTICO DE FORMATOS PROBLEMÁTICOS EN FILEUTILS");
        
        try {
            // Buscar en todos los archivos Java del proyecto
            List<String> archivosJava = listFilesTurbo(".", ".java", true);
            int problemasEncontrados = 0;
            
            for (String archivo : archivosJava) {
                if (archivo.contains("FileUtils.java")) continue; // Saltarse a sí mismo
                
                String contenido = readFile(archivo);
                if (contenido.contains("%V") || contenido.contains("%v")) {
                    System.err.println("🚨 POSIBLE PROBLEMA EN: " + archivo);
                    problemasEncontrados++;
                    
                    // Mostrar contexto del problema
                    String[] lineas = contenido.split("\n");
                    for (int i = 0; i < lineas.length; i++) {
                        if (lineas[i].contains("%V") || lineas[i].contains("%v")) {
                            System.err.println("   Línea " + (i+1) + ": " + lineas[i].trim());
                        }
                    }
                }
            }
            
            if (problemasEncontrados == 0) {
                System.out.println("✅ No se encontraron formatos problemáticos %V/%v");
            } else {
                System.out.println("🎯 Total problemas encontrados: " + problemasEncontrados);
            }
            
        } catch (Exception e) {
            System.err.println("💥 Error en diagnóstico: " + e.getMessage());
            CONTADOR_KRAKENS.incrementAndGet();
        }
    }

    // =========================================================================
    // 🎯 MÉTODOS ESPECIALIZADOS FUSIONADOS
    // =========================================================================

    public static String readFileIfExists(String path) {
        try {
            if (Files.exists(Paths.get(path))) {
                return readFile(path);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 🎯 MEJORA EN FileUtils.extraerBloqueDeCodigo() - FUSIONADO
     */
    public static String extraerBloqueDeCodigo(String respuestaLLM) {
        if (respuestaLLM == null || respuestaLLM.trim().isEmpty()) {
            return "// ❌ Respuesta vacía del oráculo";
        }
        
        try {
            // 🚀 ESTRATEGIA 1: Buscar bloque de código entre ```java ... ```
            int startJava = respuestaLLM.indexOf("```java");
            if (startJava != -1) {
                startJava += 7; // Longitud de "```java"
                int endJava = respuestaLLM.indexOf("```", startJava);
                if (endJava != -1) {
                    String codigo = respuestaLLM.substring(startJava, endJava).trim();
                    if (codigo.length() > 100) {
                        System.out.println("✅ Código extraído de bloque java: " + codigo.length() + " chars");
                        return codigo;
                    }
                }
            }
            
            // 🚀 ESTRATEGIA 2: Buscar cualquier bloque de código entre ```
            int startBlock = respuestaLLM.indexOf("```");
            if (startBlock != -1) {
                startBlock += 3;
                int endBlock = respuestaLLM.indexOf("```", startBlock);
                if (endBlock != -1) {
                    String codigo = respuestaLLM.substring(startBlock, endBlock).trim();
                    // Verificar que parece código Java
                    if (codigo.contains("package ") || codigo.contains("class ") || codigo.contains("public ")) {
                        System.out.println("✅ Código extraído de bloque genérico: " + codigo.length() + " chars");
                        return codigo;
                    }
                }
            }
            
            // 🚀 ESTRATEGIA 3: Buscar código que empiece con package o class
            if (respuestaLLM.contains("package ") || respuestaLLM.contains("class ")) {
                // Extraer desde el primer package o class hasta el final
                int start = Math.max(
                    respuestaLLM.indexOf("package "),
                    respuestaLLM.indexOf("class ")
                );
                if (start != -1) {
                    String codigo = respuestaLLM.substring(start).trim();
                    System.out.println("✅ Código extraído por patrón: " + codigo.length() + " chars");
                    return codigo;
                }
            }
            
            // 🎯 ÚLTIMO RECURSO: Devolver la respuesta completa
            System.out.println("⚠️  No se pudo extraer código claro, usando respuesta completa");
            return respuestaLLM;
            
        } catch (Exception e) {
            System.err.println("💥 Error extrayendo código: " + e.getMessage());
            return "// ❌ Error extrayendo código del oráculo";
        }
    }

    // =========================================================================
    // 📊 MÉTODOS DE ESTADÍSTICAS COMPLETAS FUSIONADOS
    // =========================================================================

    /**
     * 📊 OBTENER ESTADÍSTICAS COMPLETAS DE FILEUTILS
     */
    public static EstadisticasFileUtils getEstadisticas() {
        return new EstadisticasFileUtils(
            ARCHIVOS_CREADOS.get(),
            DIRECTORIOS_CREADOS.get(),
            CONTADOR_KRAKENS.get(),
            new HashMap<>(OPERACIONES_POR_TIPO),
            CONTADOR_LECTURAS.get(),
            CONTADOR_ESCRITURAS.get(),
            CONTADOR_CACHE_HITS.get(),
            CACHE_LECTURAS.size()
        );
    }

    /**
     * 📈 MOSTRAR ESTADÍSTICAS COMPLETAS DE FILEUTILS
     */
    public static void mostrarEstadisticas() {
        EstadisticasFileUtils stats = getEstadisticas();
        
        System.out.println("\n📊 ESTADÍSTICAS FILEUTILS TURBO ULTRA:");
        System.out.println("📄 Archivos creados: " + stats.getArchivosCreados());
        System.out.println("🗂️ Directorios creados: " + stats.getDirectoriosCreados());
        System.out.println("🚨 Errores: " + stats.getErrores());
        System.out.println("📖 Lecturas: " + stats.getLecturas());
        System.out.println("📝 Escrituras: " + stats.getEscrituras());
        System.out.println("🎯 Cache hits: " + stats.getCacheHits());
        System.out.println("💾 Cache size: " + stats.getCacheSize());
        
        System.out.println("\n🔧 OPERACIONES POR TIPO:");
        stats.getOperacionesPorTipo().forEach((tipo, cantidad) -> {
            System.out.println("  • " + tipo + ": " + cantidad);
        });
    }

    /**
     * 📊 CLASE DE ESTADÍSTICAS PARA FILEUTILS
     */
    public static class EstadisticasFileUtils {
        private final int archivosCreados;
        private final int directoriosCreados;
        private final int errores;
        private final Map<String, Integer> operacionesPorTipo;
        private final int lecturas;
        private final int escrituras;
        private final int cacheHits;
        private final int cacheSize;
        
        public EstadisticasFileUtils(int archivos, int directorios, int errores, 
                                   Map<String, Integer> operaciones, int lecturas,
                                   int escrituras, int cacheHits, int cacheSize) {
            this.archivosCreados = archivos;
            this.directoriosCreados = directorios;
            this.errores = errores;
            this.operacionesPorTipo = operaciones;
            this.lecturas = lecturas;
            this.escrituras = escrituras;
            this.cacheHits = cacheHits;
            this.cacheSize = cacheSize;
        }
        
        public int getArchivosCreados() { return archivosCreados; }
        public int getDirectoriosCreados() { return directoriosCreados; }
        public int getErrores() { return errores; }
        public Map<String, Integer> getOperacionesPorTipo() { return new HashMap<>(operacionesPorTipo); }
        public int getLecturas() { return lecturas; }
        public int getEscrituras() { return escrituras; }
        public int getCacheHits() { return cacheHits; }
        public int getCacheSize() { return cacheSize; }
    }

    /**
     * 🐙 REGISTRAR KRAKEN CON EXCEPCIÓN
     */
    public static void registrarKraken(String clase, Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        registrarKraken(clase, sw.toString());
        
        // 🎯 REGISTRO ADICIONAL ESPECÍFICO PARA KRAKENS
        String krakenInfo = String.format(
            "🐙 KRAKEN MANUAL [%s] en %s: %s",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            clase,
            e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()
        );
        
        System.out.println(krakenInfo);
        
        // 📝 GUARDAR EN LOG ESPECÍFICO DE KRAKENS
        FileUtils.appendToFile("autogen-output/krakens/registro-manual.log", 
            krakenInfo + "\n" + getStackTrace(e) + "\n---\n");
    }

    /**
     * 🛠️ MÉTODO AUXILIAR PARA OBTENER STACK TRACE
     */
    private static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * 🆕 MÉTODO PARA REGISTRAR EXCEPCIÓN (IMPLEMENTACIÓN FALTANTE)
     */
    private static void registrarExcepcion(String contexto, String clase, Exception e, String tipo) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String stackTrace = getStackTrace(e);
        
        String entrada = String.format(
            "\n## %s\n**Contexto**: %s\n**Clase**: %s\n**Fecha**: %s\n**Tipo**: %s\n**Detalles**:\n```log\n%s\n```\n---\n",
            tipo, contexto, clase, timestamp, e.getClass().getSimpleName(), stackTrace
        );
        
        appendToFile("autogen-output/excepciones/log-excepciones.md", entrada);
        System.out.println("📝 Excepción registrada: " + tipo + " en " + clase);
    }

	public static void eliminarDirectorio(String string) {
		// TODO Auto-generated method stub
		
	}
	// =========================================================================
    // 🌀 NUEVOS MÉTODOS DIMENSIONALES TURBOFURULADOS
    // =========================================================================

    /**
     * 🌀 CLONAR REALIDAD ACTUAL - Para el Portal Dimensional
     */
    public static void copyDimensionalReality(Path sourceReality, Path targetReality) throws IOException {
        long startTime = System.nanoTime();
        bitacoraDimensional("🌀 CLONANDO REALIDAD: " + sourceReality + " → " + targetReality);
        
        try {
            // 🎯 VERIFICACIÓN DIMENSIONAL AVANZADA
            if (!Files.exists(sourceReality)) {
                throw new DimensionalException("❌ Realidad fuente no existe: " + sourceReality);
            }
            
            if (Files.exists(targetReality)) {
                bitacoraDimensional("⚠️  Realidad destino ya existe, limpiando...");
                eliminarDirectorioTurbo(targetReality.toString());
            }
            
            // 🚀 CLONACIÓN PARALELA TURBO
            clonarRealidadParalela(sourceReality, targetReality);
            
            long tiempo = (System.nanoTime() - startTime) / 1_000_000;
            bitacoraDimensional("✅ REALIDAD CLONADA en " + tiempo + "ms: " + targetReality);
            
        } catch (Exception e) {
            throw new DimensionalException("💥 Falla dimensional clonando realidad: " + e.getMessage(), e);
        }
    }

    /**
     * 🚀 CLONACIÓN PARALELA DE REALIDAD - Máxima velocidad
     */
    private static void clonarRealidadParalela(Path source, Path target) throws IOException {
        List<Path> archivos = Files.walk(source)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
        
        ExecutorService executor = Executors.newFixedThreadPool(8);
        List<Future<?>> futures = new ArrayList<>();
        
        for (Path archivo : archivos) {
            futures.add(executor.submit(() -> {
                try {
                    Path relativo = source.relativize(archivo);
                    Path destino = target.resolve(relativo);
                    
                    // 🗂️ CREAR DIRECTORIO PADRE SI NO EXISTE
                    ensureParentDirectoryExists(destino.toString());
                    
                    // 📝 COPIAR ARCHIVO CON VERIFICACIÓN
                    Files.copy(archivo, destino, StandardCopyOption.REPLACE_EXISTING);
                    
                } catch (Exception e) {
                    throw new RuntimeException("💥 Error copiando " + archivo, e);
                }
            }));
        }
        
        // 🎯 ESPERAR COMPLETACIÓN
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                executor.shutdownNow();
                throw new IOException("💥 Falla en clonación paralela", e);
            }
        }
        
        executor.shutdown();
    }

    /**
     * 🗑️ ELIMINAR DIRECTORIO TURBO - Para limpieza dimensional
     */
    public static void eliminarDirectorioTurbo(String directorio) {
        try {
            Path path = Paths.get(directorio);
            if (!Files.exists(path)) {
                return;
            }
            
            // 🚀 ELIMINACIÓN RECURSIVA PARALELA
            Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                    } catch (IOException e) {
                        System.err.println("⚠️  No se pudo eliminar: " + p + " - " + e.getMessage());
                    }
                });
                
            bitacoraDimensional("🗑️  Realidad eliminada: " + directorio);
            
        } catch (IOException e) {
            throw new DimensionalException("💥 Error eliminando realidad: " + directorio, e);
        }
    }

    /**
     * 🔍 OBTENER ARCHIVOS RECURSIVOS
     */
    private static Set<Path> obtenerArchivosRecursivos(Path directorio) throws IOException {
        if (!Files.exists(directorio)) {
            return Collections.emptySet();
        }
        
        return Files.walk(directorio)
                .filter(Files::isRegularFile)
                .collect(Collectors.toSet());
    }

    /**
     * 🆕 DETECTAR ARCHIVOS NUEVOS Y ELIMINADOS
     */
    private static void detectarArchivosNuevosYEliminados(Path realidadA, Path realidadB, 
                                                         Set<Path> archivosA, Set<Path> archivosB,
                                                         Map<String, Difference> diferencias) {
        // 🎯 ARCHIVOS NUEVOS EN REALIDAD B
        for (Path archivoB : archivosB) {
            Path relativoB = realidadB.relativize(archivoB);
            Path correspondienteA = realidadA.resolve(relativoB);
            
            if (!archivosA.contains(correspondienteA)) {
                diferencias.put(relativoB.toString(), 
                    new Difference(DifferenceType.NUEVO_ARCHIVO, "Archivo creado en nueva realidad"));
            }
        }
        
        // 🎯 ARCHIVOS ELIMINADOS EN REALIDAD B
        for (Path archivoA : archivosA) {
            Path relativoA = realidadA.relativize(archivoA);
            Path correspondienteB = realidadB.resolve(relativoA);
            
            if (!archivosB.contains(correspondienteB)) {
                diferencias.put(relativoA.toString(), 
                    new Difference(DifferenceType.ARCHIVO_ELIMINADO, "Archivo eliminado en nueva realidad"));
            }
        }
    }

    /**
     * 🔄 COMPARAR ARCHIVOS COMUNES
     */
    private static void compararArchivosComunes(Path realidadA, Path realidadB, 
                                               Set<Path> archivosA, Set<Path> archivosB,
                                               Map<String, Difference> diferencias) {
        // 🎯 ENCONTRAR ARCHIVOS COMUNES
        Set<Path> archivosComunes = archivosA.stream()
                .filter(archivoA -> {
                    Path relativo = realidadA.relativize(archivoA);
                    return archivosB.contains(realidadB.resolve(relativo));
                })
                .collect(Collectors.toSet());
        
        // 🎯 COMPARAR CONTENIDO EN PARALELO
        archivosComunes.parallelStream().forEach(archivoA -> {
            try {
                Path relativo = realidadA.relativize(archivoA);
                Path archivoB = realidadB.resolve(relativo);
                
                if (!Files.exists(archivoB)) {
                    return;
                }
                
                // 🎯 COMPARAR CONTENIDO
                String contenidoA = Files.readString(archivoA);
                String contenidoB = Files.readString(archivoB);
                
                if (!contenidoA.equals(contenidoB)) {
                    List<String> diffLines = calcularDiff(contenidoA, contenidoB);
                    diferencias.put(relativo.toString(), 
                        new Difference(DifferenceType.CONTENIDO_MODIFICADO, 
                                     "Archivo modificado", diffLines));
                }
                
            } catch (IOException e) {
                diferencias.put(archivoA.toString(), 
                    new Difference(DifferenceType.ERROR_COMPARACION, 
                                 "Error comparando: " + e.getMessage()));
            }
        });
    }

    /**
     * 📝 CALCULAR DIFF ENTRE CONTENIDOS
     */
    private static List<String> calcularDiff(String contenidoA, String contenidoB) {
        List<String> diffLines = new ArrayList<>();
        String[] lineasA = contenidoA.split("\n");
        String[] lineasB = contenidoB.split("\n");
        
        int maxLines = Math.max(lineasA.length, lineasB.length);
        
        for (int i = 0; i < maxLines; i++) {
            String lineaA = i < lineasA.length ? lineasA[i] : "";
            String lineaB = i < lineasB.length ? lineasB[i] : "";
            
            if (!lineaA.equals(lineaB)) {
                diffLines.add(String.format("@@ Línea %d @@", i + 1));
                diffLines.add("- " + lineaA);
                diffLines.add("+ " + lineaB);
            }
        }
        
        return diffLines;
    }

    /**
     * 💾 GUARDAR METADATOS DIMENSIONALES
     */
    public static void guardarMetadatosDimensionales(Path realidad, DimensionalMetadata metadata) {
        try {
            Path metadataPath = realidad.resolve(".dimensional-metadata.json");
            String json = convertirMetadataAJson(metadata);
            writeToFile(metadataPath.toString(), json);
            
            bitacoraDimensional("💾 Metadatos dimensionales guardados: " + metadataPath);
            
        } catch (Exception e) {
            throw new DimensionalException("💥 Error guardando metadatos dimensionales", e);
        }
    }

    /**
     * 📖 CARGAR METADATOS DIMENSIONALES
     */
    public static DimensionalMetadata cargarMetadatosDimensionales(Path realidad) {
        try {
            Path metadataPath = realidad.resolve(".dimensional-metadata.json");
            if (!Files.exists(metadataPath)) {
                return null;
            }
            
            String json = readFile(metadataPath.toString());
            return convertirJsonAMetadata(json);
            
        } catch (Exception e) {
            throw new DimensionalException("💥 Error cargando metadatos dimensionales", e);
        }
    }

    /**
     * 🌀 BITÁCORA DIMENSIONAL ESPECIALIZADA
     */
    private static void bitacoraDimensional(String mensaje) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String logEntry = String.format("[🌀DIMENSIONAL %s] %s", timestamp, mensaje);
        
        System.out.println(logEntry);
        appendToFile("autogen-output/dimensional/bitacora-dimensional.md", logEntry + "\n");
    }

    // =========================================================================
    // 🏴‍☠️ CLASES DE APOYO DIMENSIONALES
    // =========================================================================

    /**
     * 📊 DIFERENCIA ENTRE REALIDADES
     */
    public static class Difference {
        private final DifferenceType tipo;
        private final String descripcion;
        private final List<String> detalles;
        
        public Difference(DifferenceType tipo, String descripcion) {
            this(tipo, descripcion, new ArrayList<>());
        }
        
        public Difference(DifferenceType tipo, String descripcion, List<String> detalles) {
            this.tipo = tipo;
            this.descripcion = descripcion;
            this.detalles = detalles;
        }
        
        // Getters
        public DifferenceType getTipo() { return tipo; }
        public String getDescripcion() { return descripcion; }
        public List<String> getDetalles() { return detalles; }
    }

    /**
     * 🎯 TIPOS DE DIFERENCIA
     */
    public enum DifferenceType {
        NUEVO_ARCHIVO,
        ARCHIVO_ELIMINADO, 
        CONTENIDO_MODIFICADO,
        ERROR_COMPARACION
    }

    /**
     * 💥 EXCEPCIÓN DIMENSIONAL
     */
    public static class DimensionalException extends RuntimeException {
        public DimensionalException(String message) {
            super(message);
        }
        
        public DimensionalException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * 📦 METADATOS DIMENSIONALES
     */
    public static class DimensionalMetadata {
        private final String realityId;
        private final String timestamp;
        private final String sourceReality;
        private final Map<String, Object> properties;
        
        public DimensionalMetadata(String realityId, String sourceReality) {
            this.realityId = realityId;
            this.sourceReality = sourceReality;
            this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            this.properties = new HashMap<>();
        }
        
        public void setProperty(String key, Object value) {
            properties.put(key, value);
        }
        
        public Object getProperty(String key) {
            return properties.get(key);
        }
        
        // Getters
        public String getRealityId() { return realityId; }
        public String getTimestamp() { return timestamp; }
        public String getSourceReality() { return sourceReality; }
        public Map<String, Object> getProperties() { return properties; }
    }

    // =========================================================================
    // 🧩 MÉTODOS DE CONVERSIÓN JSON
    // =========================================================================

    private static String convertirMetadataAJson(DimensionalMetadata metadata) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("realityId", metadata.getRealityId());
        jsonMap.put("timestamp", metadata.getTimestamp());
        jsonMap.put("sourceReality", metadata.getSourceReality());
        jsonMap.put("properties", metadata.getProperties());
        
        try {
            return new com.google.gson.Gson().toJson(jsonMap);
        } catch (Exception e) {
            // Fallback simple
            return String.format("{\"realityId\":\"%s\",\"timestamp\":\"%s\",\"sourceReality\":\"%s\"}",
                              metadata.getRealityId(), metadata.getTimestamp(), metadata.getSourceReality());
        }
    }

    private static DimensionalMetadata convertirJsonAMetadata(String json) {
        try {
            Map<?, ?> map = new com.google.gson.Gson().fromJson(json, Map.class);
            String realityId = (String) map.get("realityId");
            String sourceReality = (String) map.get("sourceReality");
            
            DimensionalMetadata metadata = new DimensionalMetadata(realityId, sourceReality);
            
            // Cargar propiedades
            Map<?, ?> properties = (Map<?, ?>) map.get("properties");
            if (properties != null) {
                properties.forEach((key, value) -> 
                    metadata.setProperty(key.toString(), value));
            }
            
            return metadata;
            
        } catch (Exception e) {
            throw new DimensionalException("💥 Error parseando JSON dimensional", e);
        }
    }
    
}
