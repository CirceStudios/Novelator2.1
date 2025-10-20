package com.elreinodelolvido.ellibertad.memoria;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.manager.TripulacionManager;
import com.elreinodelolvido.ellibertad.model.ClassInfo;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;
import com.elreinodelolvido.ellibertad.util.FileUtils;

/**
 * 🧠 SISTEMA DE MEMORIA PIRATA - Cada pirata tiene sus recuerdos y relaciones
 * 🎯 Integrado con ProjectScanner para acceder a las clases reales del proyecto
 */
public class SistemaMemoriaPirata {
    
    private static SistemaMemoriaPirata instancia;
    private final Map<String, MemoriaPirata> memoriaPiratas;
    private final Map<String, RelacionPirata> relaciones;
    private final ProjectScanner scanner;
    private final Bitacora bitacora;
    
    // 🎯 SINGLETON PARA ACCESO GLOBAL
    public static SistemaMemoriaPirata obtenerInstancia() {
        if (instancia == null) {
            instancia = new SistemaMemoriaPirata();
        }
        return instancia;
    }
    
    private SistemaMemoriaPirata() {
        this.memoriaPiratas = new ConcurrentHashMap<>();
        this.relaciones = new ConcurrentHashMap<>();
        this.bitacora = new Bitacora(); // ✅ PRIMERO
        this.scanner = new ProjectScanner(bitacora); // ✅ CON BITACORA
        inicializarMemoriasBase();
    }

    public void sincronizarConProyecto() {
        try {
            System.out.println("🔄 Sincronizando memorias con proyecto real...");
            
            // 🎯 FORZAR ESCANEO COMPLETO
            System.out.println("📁 Ejecutando escaneo completo del proyecto...");
            scanner.scanProject("src"); // ✅ Escanear directorio src específicamente
            List<ClassInfo> clases = scanner.getClasses();
            
            System.out.println("✅ Clases encontradas: " + clases.size());
            
            // 🎯 ACTUALIZAR MEMORIAS CON CLASES REALES
            for (ClassInfo clase : clases) {
                String nombreClase = clase.getName();
                System.out.println("🔍 Procesando clase: " + nombreClase);
                
                // 🎯 BUSCAR PIRATA EXISTENTE O CREAR NUEVO
                boolean encontrado = false;
                for (MemoriaPirata memoria : memoriaPiratas.values()) {
                    if (memoria.getNombreClase().equals(nombreClase)) {
                        System.out.println("✅ Pirata encontrado: " + memoria.getNombre());
                        encontrado = true;
                        break;
                    }
                }
                
                if (!encontrado) {
                    String nuevoPirata = generarNombrePirata(nombreClase);
                    MemoriaPirata nuevaMemoria = new MemoriaPirata(nuevoPirata, nombreClase);
                    memoriaPiratas.put(nuevoPirata, nuevaMemoria);
                    System.out.println("🏴‍☠️ Nuevo pirata creado: " + nuevoPirata + " para " + nombreClase);
                }
            }
            
            System.out.println("✅ Sincronización completada. Total piratas: " + memoriaPiratas.size());
            
        } catch (Exception e) {
            System.err.println("💥 Error sincronizando con proyecto: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 🏴‍☠️ MEMORIA INDIVIDUAL DE CADA PIRATA
     */
    /**
     * 🏴‍☠️ MEMORIA INDIVIDUAL DE CADA PIRATA
     */
    public static class MemoriaPirata {
        private final String nombre;
        private String nombreClase;
        private final List<Recuerdo> recuerdos;
        private final Map<String, FragmentoCodigo> fragmentosCodigo;
        private final Set<String> habilidades;
        private final Map<String, Integer> relaciones; // pirata -> nivelConfianza (1-10)
        private Instant fechaUltimaActualizacion;
        private String personalidad;
        private final Map<String, Integer> interaccionesPositivas;
        private String rutaArchivo; // 🆕 CAMPO PARA LA RUTA DEL ARCHIVO
        
        public MemoriaPirata(String nombre, String nombreClase) {
            this.nombre = nombre;
            this.nombreClase = nombreClase;
            this.recuerdos = new ArrayList<>();
            this.fragmentosCodigo = new ConcurrentHashMap<>();
            this.habilidades = new HashSet<>();
            this.relaciones = new ConcurrentHashMap<>();
            this.interaccionesPositivas = new ConcurrentHashMap<>();
            this.fechaUltimaActualizacion = Instant.now();
            this.personalidad = "No definida";
            this.rutaArchivo = ""; // 🆕 INICIALIZAR RUTA VACÍA
            
            // 🎯 HABILIDADES BASE SEGÚN ROL
            inicializarHabilidadesBase();
            // 🆕 INFERIR RUTA BASADA EN EL NOMBRE DE CLASE
            inferirRutaArchivo();
        }
        
        /**
         * 🆕 INFERIR RUTA DE ARCHIVO BASADA EN EL NOMBRE DE CLASE
         */
        private void inferirRutaArchivo() {
            if (nombreClase == null || nombreClase.isEmpty()) {
                this.rutaArchivo = "";
                return;
            }
            
            try {
                // 🎯 CONVERTIR NOMBRE DE CLASE A RUTA DE ARCHIVO
                String rutaBase = "src/main/java/";
                String rutaClase = nombreClase.replace('.', '/') + ".java";
                this.rutaArchivo = rutaBase + rutaClase;
                
                // 🎯 VERIFICAR SI EXISTE EN DIFERENTES ESTRUCTURAS
                String[] estructurasAlternativas = {
                    "src/main/java/",
                    "src/",
                    "main/java/",
                    "app/src/main/java/",
                    ""
                };
                
                for (String estructura : estructurasAlternativas) {
                    String rutaAlternativa = estructura + rutaClase;
                    File archivo = new File(rutaAlternativa);
                    if (archivo.exists()) {
                        this.rutaArchivo = rutaAlternativa;
                        break;
                    }
                }
                
            } catch (Exception e) {
                this.rutaArchivo = "";
            }
        }
        
        /**
         * 🆕 OBTENER RUTA DEL ARCHIVO - IMPLEMENTACIÓN COMPLETA
         */
        public String getRutaArchivo() {
            // 🎯 SI YA TENEMOS RUTA Y EXISTE, DEVOLVERLA
            if (rutaArchivo != null && !rutaArchivo.isEmpty()) {
                File archivo = new File(rutaArchivo);
                if (archivo.exists()) {
                    return rutaArchivo;
                }
            }
            
            // 🎯 BUSCAR ARCHIVO EN ESTRUCTURAS COMUNES
            return buscarArchivoEnEstructuras();
        }
        
        /**
         * 🆕 BUSCAR ARCHIVO EN ESTRUCTURAS DE PROYECTO COMUNES
         */
        private String buscarArchivoEnEstructuras() {
            if (nombreClase == null || nombreClase.isEmpty()) {
                return "";
            }
            
            String[] estructuras = {
                "src/main/java/",
                "src/test/java/", 
                "src/",
                "main/java/",
                "test/java/",
                "app/src/main/java/",
                "app/src/test/java/",
                ""
            };
            
            String rutaClase = nombreClase.replace('.', '/') + ".java";
            
            for (String estructura : estructuras) {
                String rutaCompleta = estructura + rutaClase;
                File archivo = new File(rutaCompleta);
                if (archivo.exists() && archivo.isFile()) {
                    this.rutaArchivo = rutaCompleta; // 🎯 ACTUALIZAR PARA FUTURAS CONSULTAS
                    return rutaCompleta;
                }
            }
            
            // 🎯 BUSCAR RECURSIVAMENTE DESDE EL DIRECTORIO ACTUAL
            return buscarArchivoRecursivamente(rutaClase);
        }
        
        /**
         * 🆕 BUSCAR ARCHIVO RECURSIVAMENTE
         */
        private String buscarArchivoRecursivamente(String nombreArchivo) {
            try {
                Path directorioActual = Paths.get(".").toAbsolutePath().normalize();
                Optional<Path> archivoEncontrado = Files.walk(directorioActual)
                        .filter(path -> path.toString().endsWith(nombreArchivo))
                        .filter(path -> Files.isRegularFile(path))
                        .findFirst();
                
                if (archivoEncontrado.isPresent()) {
                    String ruta = archivoEncontrado.get().toString();
                    this.rutaArchivo = ruta; // 🎯 ACTUALIZAR PARA FUTURAS CONSULTAS
                    return ruta;
                }
            } catch (IOException e) {
                // 🎯 SILENCIAR ERROR, SIMPLEMENTE NO ENCONTRÓ
            }
            
            return "";
        }
        
        /**
         * 🆕 ESTABLECER RUTA DE ARCHIVO EXPLÍCITAMENTE
         */
        public void setRutaArchivo(String rutaArchivo) {
            this.rutaArchivo = rutaArchivo;
            this.fechaUltimaActualizacion = Instant.now();
            agregarEventoHistorial("Ruta actualizada", "Nueva ruta: " + rutaArchivo);
        }
        
        /**
         * 🆕 VERIFICAR SI LA RUTA DEL ARCHIVO ES VÁLIDA
         */
        public boolean tieneRutaArchivoValida() {
            if (rutaArchivo == null || rutaArchivo.isEmpty()) {
                return false;
            }
            File archivo = new File(rutaArchivo);
            return archivo.exists() && archivo.isFile();
        }
        
        /**
         * 🆕 ACTUALIZAR CLASE DEL PIRATA CON INFERENCIA DE RUTA
         */
        public void actualizarClase(String nuevaClase) {
            this.nombreClase = nuevaClase;
            this.fechaUltimaActualizacion = Instant.now();
            
            // 🎯 INFERIR NUEVA RUTA BASADA EN LA NUEVA CLASE
            inferirRutaArchivo();
            
            agregarEventoHistorial("Clase actualizada", 
                String.format("Nueva clase: %s | Ruta inferida: %s", nuevaClase, rutaArchivo));
        }

        /**
         * 🆕 AGREGAR HABILIDAD AL PIRATA
         */
        public void agregarHabilidad(String habilidad) {
            if (!habilidades.contains(habilidad)) {
                habilidades.add(habilidad);
                this.fechaUltimaActualizacion = Instant.now();
                agregarEventoHistorial("Habilidad aprendida", "Nueva habilidad: " + habilidad);
            }
        }

        /**
         * 🆕 ELIMINAR HABILIDAD DEL PIRATA
         */
        public void eliminarHabilidad(String habilidad) {
            if (habilidades.contains(habilidad)) {
                habilidades.remove(habilidad);
                this.fechaUltimaActualizacion = Instant.now();
                agregarEventoHistorial("Habilidad olvidada", "Habilidad removida: " + habilidad);
            }
        }

        /**
         * 🆕 VERIFICAR SI TIENE HABILIDAD
         */
        public boolean tieneHabilidad(String habilidad) {
            return habilidades.contains(habilidad);
        }

        /**
         * 🆕 ESTABLECER PERSONALIDAD
         */
        public void setPersonalidad(String personalidad) {
            this.personalidad = personalidad;
            this.fechaUltimaActualizacion = Instant.now();
            agregarEventoHistorial("Personalidad actualizada", "Nueva personalidad: " + personalidad);
        }
        
        /**
         * 🆕 OBTENER PERSONALIDAD
         */
        public String getPersonalidad() {
            return personalidad;
        }

        /**
         * 🆕 REGISTRAR INTERACCIÓN POSITIVA
         */
        public void registrarInteraccionPositiva(String otroPirata, String contexto) {
            String claveRelacion = nombre + "->" + otroPirata;
            interaccionesPositivas.put(claveRelacion, 
                interaccionesPositivas.getOrDefault(claveRelacion, 0) + 1);
            
            // 🎯 ACTUALIZAR RELACIÓN CON EL OTRO PIRATA
            relaciones.merge(otroPirata, 2, Integer::sum); // +2 puntos por interacción positiva
            
            this.fechaUltimaActualizacion = Instant.now();
            agregarEventoHistorial("Interacción positiva", 
                String.format("Con %s: %s | Confianza: %d", otroPirata, contexto, relaciones.getOrDefault(otroPirata, 0)));
        }
        
        /**
         * 🆕 REGISTRAR INTERACCIÓN NEGATIVA
         */
        public void registrarInteraccionNegativa(String otroPirata, String contexto) {
            // 🎯 DISMINUIR CONFIANZA PERO NO ELIMINAR COMPLETAMENTE
            relaciones.merge(otroPirata, -1, Integer::sum);
            
            this.fechaUltimaActualizacion = Instant.now();
            agregarEventoHistorial("Interacción negativa", 
                String.format("Con %s: %s | Confianza: %d", otroPirata, contexto, relaciones.getOrDefault(otroPirata, 0)));
        }
        
        /**
         * 🆕 OBTENER NIVEL DE CONFIANZA CON OTRO PIRATA
         */
        public int obtenerConfianza(String otroPirata) {
            return relaciones.getOrDefault(otroPirata, 0);
        }
        
        /**
         * 🆕 OBTENER PIRATAS CON ALTA CONFIANZA (≥5)
         */
        public List<String> obtenerAliados() {
            return relaciones.entrySet().stream()
                    .filter(entry -> entry.getValue() >= 5)
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
        
        private void inicializarHabilidadesBase() {
            // Cada pirata tiene habilidades según su clase/rol
            if (nombreClase.contains("Scanner")) {
                habilidades.addAll(Arrays.asList("exploracion", "analisis", "deteccion", "parseo", "escaneo"));
            } else if (nombreClase.contains("Manager")) {
                habilidades.addAll(Arrays.asList("gestion", "coordinacion", "estrategia", "liderazgo", "organizacion"));
            } else if (nombreClase.contains("Debug")) {
                habilidades.addAll(Arrays.asList("diagnostico", "reparacion", "optimizacion", "depuracion", "resolucion_problemas"));
            } else if (nombreClase.contains("API")) {
                habilidades.addAll(Arrays.asList("comunicacion", "integracion", "conexion", "protocolos", "web_services"));
            } else if (nombreClase.contains("Collector")) {
                habilidades.addAll(Arrays.asList("recoleccion", "extraccion", "procesamiento", "analisis_codigo", "mineria_datos"));
            }
            habilidades.add("programacion"); // Todos saben programar
            habilidades.add("java"); // Especialidad en Java
            habilidades.add("colaboracion"); // Trabajo en equipo
        }
        
        // 🔧 MÉTODOS PARA GESTIONAR RECUERDOS
        public void agregarRecuerdo(String contenido, String tipo, String... piratasInvolucrados) {
            Recuerdo recuerdo = new Recuerdo(contenido, tipo, piratasInvolucrados);
            recuerdos.add(recuerdo);
            
            // 🎯 ACTUALIZAR RELACIONES CON PIRATAS INVOLUCRADOS
            for (String pirata : piratasInvolucrados) {
                if (!pirata.equals(this.nombre)) {
                    relaciones.merge(pirata, 1, Integer::sum);
                }
            }
            
            this.fechaUltimaActualizacion = Instant.now();
        }
        
        /**
         * 🆕 AGREGAR RECUERDO CON METADATOS ADICIONALES
         */
        public void agregarRecuerdoDetallado(String contenido, String tipo, int importancia, String... tags) {
            Recuerdo recuerdo = new Recuerdo(contenido, tipo, importancia, tags);
            recuerdos.add(recuerdo);
            this.fechaUltimaActualizacion = Instant.now();
        }
        
        /**
         * 🆕 BUSCAR RECUERDOS POR TIPO O CONTENIDO
         */
        public List<Recuerdo> buscarRecuerdos(String consulta) {
            return recuerdos.stream()
                    .filter(recuerdo -> recuerdo.coincideCon(consulta))
                    .sorted((r1, r2) -> Integer.compare(r2.getImportancia(), r1.getImportancia()))
                    .collect(Collectors.toList());
        }
        
        public void agregarFragmentoCodigo(String identificador, String codigo, String proposito) {
            fragmentosCodigo.put(identificador, new FragmentoCodigo(codigo, proposito));
            this.fechaUltimaActualizacion = Instant.now();
            agregarEventoHistorial("Fragmento de código agregado", 
                String.format("ID: %s | Propósito: %s", identificador, proposito));
        }
        
        /**
         * 🆕 OBTENER FRAGMENTO DE CÓDIGO POR IDENTIFICADOR
         */
        public Optional<FragmentoCodigo> obtenerFragmentoCodigo(String identificador) {
            return Optional.ofNullable(fragmentosCodigo.get(identificador));
        }
        
        /**
         * 🆕 ELIMINAR FRAGMENTO DE CÓDIGO
         */
        public boolean eliminarFragmentoCodigo(String identificador) {
            if (fragmentosCodigo.containsKey(identificador)) {
                fragmentosCodigo.remove(identificador);
                this.fechaUltimaActualizacion = Instant.now();
                agregarEventoHistorial("Fragmento de código eliminado", "ID: " + identificador);
                return true;
            }
            return false;
        }
        
        public String obtenerContextoPersonalizado(String pregunta) {
            StringBuilder contexto = new StringBuilder();
            
            // 🎯 INFORMACIÓN BÁSICA DEL PIRATA
            contexto.append("🏴‍☠️ PIRATA: ").append(nombre).append("\n");
            contexto.append("📦 CLASE: ").append(nombreClase).append("\n");
            contexto.append("🎭 PERSONALIDAD: ").append(personalidad).append("\n");
            contexto.append("📅 ÚLTIMA ACTUALIZACIÓN: ").append(fechaUltimaActualizacion).append("\n\n");
            
            // 🎯 HABILIDADES
            contexto.append("💼 HABILIDADES: ").append(String.join(", ", habilidades)).append("\n\n");
            
            // 🎯 RECUERDOS RELEVANTES
            contexto.append("🧠 RECUERDOS RELEVANTES:\n");
            recuerdos.stream()
                    .filter(r -> r.esRelevantePara(pregunta))
                    .sorted((r1, r2) -> Integer.compare(r2.getImportancia(), r1.getImportancia()))
                    .limit(3)
                    .forEach(r -> contexto.append("• ").append(r).append("\n"));
            
            // 🔗 RELACIONES IMPORTANTES
            contexto.append("\n🤝 RELACIONES DESTACADAS:\n");
            relaciones.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(3)
                    .forEach(entry -> 
                        contexto.append("• ").append(entry.getKey())
                               .append(" (confianza: ").append(entry.getValue()).append(")\n"));
            
            // 💾 FRAGMENTOS DE CÓDIGO ÚTILES
            contexto.append("\n💾 FRAGMENTOS DE CÓDIGO DISPONIBLES:\n");
            fragmentosCodigo.values().stream()
                    .filter(f -> f.esRelevantePara(pregunta))
                    .limit(2)
                    .forEach(f -> contexto.append("• ").append(f).append("\n"));
            
            // 🎯 INFORMACIÓN DE ARCHIVO
            if (tieneRutaArchivoValida()) {
                contexto.append("\n📁 ARCHIVO FUENTE: ").append(rutaArchivo).append("\n");
            }
            
            return contexto.toString();
        }
        
        /**
         * 🆕 OBTENER RESUMEN EJECUTIVO DEL PIRATA
         */
        public String obtenerResumenEjecutivo() {
            return String.format(
                "🏴‍☠️ %s | 📦 %s | 💼 %d habilidades | 🧠 %d recuerdos | 🤝 %d relaciones | 📅 %s",
                nombre,
                nombreClase,
                habilidades.size(),
                recuerdos.size(),
                relaciones.size(),
                fechaUltimaActualizacion.toString().substring(0, 16)
            );
        }
        
        public Optional<String> obtenerMejorAmigo() {
            return relaciones.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey);
        }
        
        /**
         * 🆕 OBTENER ESTADÍSTICAS COMPLETAS
         */
        public Map<String, Object> obtenerEstadisticas() {
            Map<String, Object> stats = new HashMap<>();
            stats.put("nombre", nombre);
            stats.put("clase", nombreClase);
            stats.put("total_recuerdos", recuerdos.size());
            stats.put("total_fragmentos_codigo", fragmentosCodigo.size());
            stats.put("total_habilidades", habilidades.size());
            stats.put("total_relaciones", relaciones.size());
            stats.put("aliados", obtenerAliados().size());
            stats.put("fecha_ultima_actualizacion", fechaUltimaActualizacion);
            stats.put("ruta_archivo_valida", tieneRutaArchivoValida());
            
            // 🎯 ESTADÍSTICAS DE RECUERDOS POR TIPO
            Map<String, Long> recuerdosPorTipo = recuerdos.stream()
                    .collect(Collectors.groupingBy(Recuerdo::getTipo, Collectors.counting()));
            stats.put("recuerdos_por_tipo", recuerdosPorTipo);
            
            return stats;
        }
        
        // 🆕 MÉTODO AUXILIAR PARA AGREGAR EVENTOS AL HISTORIAL
        private void agregarEventoHistorial(String tipo, String descripcion) {
            agregarRecuerdo(descripcion, tipo, nombre);
        }
        
        // Getters
        public String getNombre() { return nombre; }
        public String getNombreClase() { return nombreClase; }
        public List<Recuerdo> getRecuerdos() { return Collections.unmodifiableList(recuerdos); }
        public Map<String, Integer> getRelaciones() { return Collections.unmodifiableMap(relaciones); }
        public Set<String> getHabilidades() { return Collections.unmodifiableSet(habilidades); }
        public Map<String, FragmentoCodigo> getFragmentosCodigo() { return Collections.unmodifiableMap(fragmentosCodigo); }
        public Instant getFechaUltimaActualizacion() { return fechaUltimaActualizacion; }

        /**
         * 🆕 ACTUALIZAR FECHA DE ACTUALIZACIÓN
         */
        public void actualizarTimestamp() {
            this.fechaUltimaActualizacion = Instant.now();
        }

        @Override
        public String toString() {
            return String.format(
                "MemoriaPirata{nombre='%s', clase='%s', habilidades=%d, recuerdos=%d, relaciones=%d}",
                nombre, nombreClase, habilidades.size(), recuerdos.size(), relaciones.size()
            );
        }
    }
    
    /**
     * 📝 RECUERDO INDIVIDUAL - Sistema de memoria avanzado para piratas
     */
    public static class Recuerdo {
        private final String contenido;
        private final String tipo; // "conversacion", "solucion", "error", "colaboracion", "aprendizaje", "descubrimiento"
        private final String[] piratasInvolucrados;
        private final long timestamp;
        private final int importancia; // 1-10
        private final String[] tags; // 🆕 TAGS PARA CATEGORIZACIÓN
        private final String contexto; // 🆕 CONTEXTO ADICIONAL
        private final Map<String, Object> metadatos; // 🆕 METADATOS EXTENSIBLES
        
        // 🎯 TIPOS DE RECUERDO PREDEFINIDOS
        public static final String TIPO_CONVERSACION = "conversacion";
        public static final String TIPO_SOLUCION = "solucion";
        public static final String TIPO_ERROR = "error";
        public static final String TIPO_COLABORACION = "colaboracion";
        public static final String TIPO_APRENDIZAJE = "aprendizaje";
        public static final String TIPO_DESCUBRIMIENTO = "descubrimiento";
        public static final String TIPO_EVENTO = "evento";
        public static final String TIPO_LOGRO = "logro";
        
        /**
         * 🏗️ CONSTRUCTOR BÁSICO
         */
        public Recuerdo(String contenido, String tipo, String... piratasInvolucrados) {
            this(contenido, tipo, 5, new String[0], "", piratasInvolucrados);
        }
        
        /**
         * 🏗️ CONSTRUCTOR CON IMPORTANCIA PERSONALIZADA
         */
        public Recuerdo(String contenido, String tipo, int importancia, String... tags) {
            this(contenido, tipo, importancia, tags, "", new String[0]);
        }
        
        /**
         * 🏗️ CONSTRUCTOR COMPLETO
         */
        public Recuerdo(String contenido, String tipo, int importancia, String[] tags, 
                       String contexto, String... piratasInvolucrados) {
            this.contenido = contenido;
            this.tipo = tipo != null ? tipo : TIPO_CONVERSACION;
            this.piratasInvolucrados = piratasInvolucrados != null ? piratasInvolucrados : new String[0];
            this.timestamp = System.currentTimeMillis();
            this.importancia = Math.max(1, Math.min(10, importancia)); // Asegurar rango 1-10
            this.tags = tags != null ? tags : new String[0];
            this.contexto = contexto != null ? contexto : "";
            this.metadatos = new HashMap<>();
            
            // 🎯 INICIALIZAR METADATOS POR DEFECTO
            inicializarMetadatos();
        }
        
        /**
         * 🎯 INICIALIZAR METADATOS POR DEFECTO
         */
        private void inicializarMetadatos() {
            metadatos.put("longitud_contenido", contenido.length());
            metadatos.put("cantidad_piratas", piratasInvolucrados.length);
            metadatos.put("cantidad_tags", tags.length);
            metadatos.put("tiene_contexto", !contexto.isEmpty());
            metadatos.put("fecha_creacion", Instant.ofEpochMilli(timestamp));
        }
        
        /**
         * 🎯 CALCULAR IMPORTANCIA AUTOMÁTICAMENTE (para constructores básicos)
         */
        private int calcularImportancia(String contenido, String tipo) {
            int base = 5;
            
            // 🎯 FACTOR POR TIPO
            switch (tipo) {
                case TIPO_SOLUCION:
                case TIPO_LOGRO:
                    base += 3;
                    break;
                case TIPO_ERROR:
                case TIPO_DESCUBRIMIENTO:
                    base += 2;
                    break;
                case TIPO_APRENDIZAJE:
                    base += 1;
                    break;
                case TIPO_CONVERSACION:
                    base += 0; // Neutral
                    break;
            }
            
            // 🎯 FACTOR POR LONGITUD DEL CONTENIDO
            if (contenido.length() > 500) base += 2;
            else if (contenido.length() > 200) base += 1;
            
            // 🎯 FACTOR POR CANTIDAD DE PIRATAS INVOLUCRADOS
            if (piratasInvolucrados.length > 3) base += 2;
            else if (piratasInvolucrados.length > 1) base += 1;
            
            return Math.min(Math.max(1, base), 10); // Asegurar rango 1-10
        }
        
        /**
         * 🎯 VERIFICAR SI ES RELEVANTE PARA UNA PREGUNTA
         */
        public boolean esRelevantePara(String pregunta) {
            if (pregunta == null || pregunta.trim().isEmpty()) {
                return false;
            }
            
            String preguntaLower = pregunta.toLowerCase();
            
            // 🎯 BUSCAR EN CONTENIDO
            if (contenido.toLowerCase().contains(preguntaLower)) {
                return true;
            }
            
            // 🎯 BUSCAR EN TAGS
            for (String tag : tags) {
                if (tag.toLowerCase().contains(preguntaLower)) {
                    return true;
                }
            }
            
            // 🎯 BUSCAR EN CONTEXTO
            if (contexto.toLowerCase().contains(preguntaLower)) {
                return true;
            }
            
            // 🎯 BUSCAR EN PIRATAS INVOLUCRADOS
            for (String pirata : piratasInvolucrados) {
                if (preguntaLower.contains(pirata.toLowerCase())) {
                    return true;
                }
            }
            
            // 🎯 BUSCAR EN TIPO
            if (tipo.toLowerCase().contains(preguntaLower)) {
                return true;
            }
            
            return false;
        }
        
        /**
         * 🆕 VERIFICAR SI COINCIDE CON UNA CONSULTA (búsqueda más flexible)
         */
        public boolean coincideCon(String consulta) {
            if (consulta == null || consulta.trim().isEmpty()) {
                return false;
            }
            
            String consultaLower = consulta.toLowerCase();
            
            // 🎯 BUSQUEDA POR PALABRAS CLAVE
            String[] palabras = consultaLower.split("\\s+");
            int coincidencias = 0;
            
            for (String palabra : palabras) {
                if (palabra.length() < 3) continue; // Ignorar palabras muy cortas
                
                if (contenido.toLowerCase().contains(palabra) ||
                    contexto.toLowerCase().contains(palabra) ||
                    tipo.toLowerCase().contains(palabra) ||
                    Arrays.stream(tags).anyMatch(tag -> tag.toLowerCase().contains(palabra)) ||
                    Arrays.stream(piratasInvolucrados).anyMatch(pirata -> pirata.toLowerCase().contains(palabra))) {
                    coincidencias++;
                }
            }
            
            // 🎯 CONSIDERAR RELEVANTE SI COINCIDE CON AL MENOS EL 30% DE LAS PALABRAS
            return coincidencias >= Math.ceil(palabras.length * 0.3);
        }
        
        /**
         * 🆕 OBTENER ANTIGÜEDAD EN MINUTOS
         */
        public long getAntiguedadMinutos() {
            return (System.currentTimeMillis() - timestamp) / (1000 * 60);
        }
        
        /**
         * 🆕 OBTENER ANTIGÜEDAD EN HORAS
         */
        public long getAntiguedadHoras() {
            return getAntiguedadMinutos() / 60;
        }
        
        /**
         * 🆕 OBTENER ANTIGÜEDAD EN DÍAS
         */
        public long getAntiguedadDias() {
            return getAntiguedadHoras() / 24;
        }
        
        /**
         * 🆕 VERIFICAR SI ES RECIENTE (menos de 1 hora)
         */
        public boolean esReciente() {
            return getAntiguedadMinutos() < 60;
        }
        
        /**
         * 🆕 VERIFICAR SI ES FRESCO (menos de 24 horas)
         */
        public boolean esFresco() {
            return getAntiguedadHoras() < 24;
        }
        
        /**
         * 🆕 AGREGAR METADATO PERSONALIZADO
         */
        public void agregarMetadato(String clave, Object valor) {
            metadatos.put(clave, valor);
        }
        
        /**
         * 🆕 OBTENER METADATO
         */
        public Optional<Object> obtenerMetadato(String clave) {
            return Optional.ofNullable(metadatos.get(clave));
        }
        
        /**
         * 🆕 OBTENER CONTENIDO RESUMIDO
         */
        public String getContenidoResumido(int maxLongitud) {
            if (contenido.length() <= maxLongitud) {
                return contenido;
            }
            return contenido.substring(0, maxLongitud - 3) + "...";
        }
        
        /**
         * 🆕 OBTENER FECHA LEGIBLE
         */
        public String getFechaLegible() {
            return Instant.ofEpochMilli(timestamp)
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        
        /**
         * 🆕 OBTENER DESCRIPCIÓN DETALLADA
         */
        public String getDescripcionDetallada() {
            StringBuilder sb = new StringBuilder();
            sb.append("📝 RECUERDO - ").append(tipo.toUpperCase()).append("\n");
            sb.append("📅 Fecha: ").append(getFechaLegible()).append("\n");
            sb.append("⭐ Importancia: ").append("★".repeat(importancia)).append(" (").append(importancia).append("/10)\n");
            sb.append("👥 Piratas: ").append(String.join(", ", piratasInvolucrados)).append("\n");
            
            if (tags.length > 0) {
                sb.append("🏷️ Tags: ").append(String.join(", ", tags)).append("\n");
            }
            
            if (!contexto.isEmpty()) {
                sb.append("📋 Contexto: ").append(contexto).append("\n");
            }
            
            sb.append("💭 Contenido: ").append(contenido);
            
            return sb.toString();
        }
        
        /**
         * 🆕 VERIFICAR SI TIENE TAG ESPECÍFICO
         */
        public boolean tieneTag(String tagBuscado) {
            return Arrays.stream(tags)
                    .anyMatch(tag -> tag.equalsIgnoreCase(tagBuscado));
        }
        
        /**
         * 🆕 VERIFICAR SI ESTÁ INVOLUCRADO UN PIRATA ESPECÍFICO
         */
        public boolean estaInvolucrado(String nombrePirata) {
            return Arrays.stream(piratasInvolucrados)
                    .anyMatch(pirata -> pirata.equalsIgnoreCase(nombrePirata));
        }
        
        /**
         * 🆕 OBTENER PUNTAJE DE RELEVANCIA PARA UNA CONSULTA
         */
        public int calcularPuntajeRelevancia(String consulta) {
            if (consulta == null || consulta.trim().isEmpty()) {
                return 0;
            }
            
            int puntaje = 0;
            String consultaLower = consulta.toLowerCase();
            
            // 🎯 PUNTAJE POR COINCIDENCIA EN CONTENIDO
            if (contenido.toLowerCase().contains(consultaLower)) {
                puntaje += 10;
            }
            
            // 🎯 PUNTAJE POR COINCIDENCIA EN TAGS
            for (String tag : tags) {
                if (tag.toLowerCase().contains(consultaLower)) {
                    puntaje += 5;
                }
            }
            
            // 🎯 PUNTAJE POR COINCIDENCIA EN TIPO
            if (tipo.toLowerCase().contains(consultaLower)) {
                puntaje += 3;
            }
            
            // 🎯 PUNTAJE POR IMPORTANCIA
            puntaje += importancia;
            
            // 🎯 BONUS POR RECIENCIA (más reciente = más relevante)
            if (esReciente()) {
                puntaje += 3;
            } else if (esFresco()) {
                puntaje += 1;
            }
            
            return puntaje;
        }
        
        /**
         * 🆕 CREAR COPIA DEL RECUERDO
         */
        public Recuerdo copiar() {
            Recuerdo copia = new Recuerdo(
                contenido,
                tipo,
                importancia,
                Arrays.copyOf(tags, tags.length),
                contexto,
                Arrays.copyOf(piratasInvolucrados, piratasInvolucrados.length)
            );
            
            // 🎯 COPIAR METADATOS
            copia.metadatos.putAll(this.metadatos);
            
            return copia;
        }
        
        @Override
        public String toString() {
            String contenidoResumido = getContenidoResumido(80);
            String piratasStr = piratasInvolucrados.length > 0 ? 
                " | 👥 " + String.join(", ", piratasInvolucrados) : "";
            String tagsStr = tags.length > 0 ? 
                " | 🏷️ " + String.join(", ", tags) : "";
            
            return String.format("[%s] %s%s%s", 
                tipo, 
                contenidoResumido,
                piratasStr,
                tagsStr);
        }
        
        /**
         * 🆕 REPRESENTACIÓN JSON PARA PERSISTENCIA
         */
        public String toJson() {
            try {
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("contenido", contenido);
                jsonMap.put("tipo", tipo);
                jsonMap.put("piratasInvolucrados", Arrays.asList(piratasInvolucrados));
                jsonMap.put("timestamp", timestamp);
                jsonMap.put("importancia", importancia);
                jsonMap.put("tags", Arrays.asList(tags));
                jsonMap.put("contexto", contexto);
                jsonMap.put("metadatos", metadatos);
                
                // 🎯 USAR JACKSON O SIMILAR EN PRODUCCIÓN
                return String.format(
                    "{\"contenido\":\"%s\",\"tipo\":\"%s\",\"importancia\":%d,\"timestamp\":%d}",
                    contenido.replace("\"", "\\\""),
                    tipo,
                    importancia,
                    timestamp
                );
            } catch (Exception e) {
                return "{}";
            }
        }
        
        // 🎯 GETTERS COMPLETOS
        public String getContenido() { return contenido; }
        public String getTipo() { return tipo; }
        public String[] getPiratasInvolucrados() { return Arrays.copyOf(piratasInvolucrados, piratasInvolucrados.length); }
        public long getTimestamp() { return timestamp; }
        public int getImportancia() { return importancia; }
        public String[] getTags() { return Arrays.copyOf(tags, tags.length); }
        public String getContexto() { return contexto; }
        public Map<String, Object> getMetadatos() { return Collections.unmodifiableMap(metadatos); }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Recuerdo recuerdo = (Recuerdo) o;
            return timestamp == recuerdo.timestamp && 
                   Objects.equals(contenido, recuerdo.contenido) && 
                   Objects.equals(tipo, recuerdo.tipo);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(contenido, tipo, timestamp);
        }
    }
    
    /**
     * 💾 FRAGMENTO DE CÓDIGO COMPARTIBLE
     */
    public static class FragmentoCodigo {
        private final String codigo;
        private final String proposito;
        private final long timestamp;
        private final String pirataOrigen;
        
        public FragmentoCodigo(String codigo, String proposito) {
            this.codigo = codigo;
            this.proposito = proposito;
            this.timestamp = System.currentTimeMillis();
            this.pirataOrigen = "Desconocido";
        }
        
        public FragmentoCodigo(String codigo, String proposito, String pirataOrigen) {
            this.codigo = codigo;
            this.proposito = proposito;
            this.timestamp = System.currentTimeMillis();
            this.pirataOrigen = pirataOrigen;
        }
        
        public boolean esRelevantePara(String pregunta) {
            return proposito.toLowerCase().contains(pregunta.toLowerCase()) ||
                   codigo.toLowerCase().contains(pregunta.toLowerCase());
        }
        
        @Override
        public String toString() {
            return String.format("%s: %s", 
                proposito,
                codigo.length() > 30 ? codigo.substring(0, 27) + "..." : codigo);
        }
        
        // Getters
        public String getCodigo() { return codigo; }
        public String getProposito() { return proposito; }
        public long getTimestamp() { return timestamp; }
        public String getPirataOrigen() { return pirataOrigen; }
    }
    
    /**
     * 🔗 RELACIÓN ENTRE DOS PIRATAS
     */
    public static class RelacionPirata {
        private final String pirata1;
        private final String pirata2;
        private int nivelConfianza; // 1-10
        private final List<String> interacciones;
        private String tipoRelacion; // "amistad", "colaboracion", "rivalidad", "mentoria"
        
        public RelacionPirata(String pirata1, String pirata2) {
            this.pirata1 = pirata1;
            this.pirata2 = pirata2;
            this.nivelConfianza = 1;
            this.interacciones = new ArrayList<>();
            this.tipoRelacion = "colaboracion";
        }
        
        public void registrarInteraccion(String descripcion, int cambioConfianza) {
            interacciones.add(descripcion);
            this.nivelConfianza = Math.max(1, Math.min(10, this.nivelConfianza + cambioConfianza));
            actualizarTipoRelacion();
        }
        
        private void actualizarTipoRelacion() {
            if (nivelConfianza >= 8) tipoRelacion = "amistad";
            else if (nivelConfianza >= 6) tipoRelacion = "colaboracion";
            else if (nivelConfianza >= 4) tipoRelacion = "conocido";
            else tipoRelacion = "rivalidad";
        }
        
        public boolean involucra(String pirata) {
            return pirata1.equals(pirata) || pirata2.equals(pirata);
        }
        
        // Getters
        public String getPirata1() { return pirata1; }
        public String getPirata2() { return pirata2; }
        public int getNivelConfianza() { return nivelConfianza; }
        public List<String> getInteracciones() { return Collections.unmodifiableList(interacciones); }
        public String getTipoRelacion() { return tipoRelacion; }
    }

    // =========================================================================
    // 🎯 MÉTODOS PRINCIPALES DEL SISTEMA
    // =========================================================================

    /**
     * 🎪 INICIALIZAR MEMORIAS BASE DESDE TRIPULACIÓN
     */
    private void inicializarMemoriasBase() {
        // 🎯 USAR ROLES_PIRATAS PARA INICIALIZAR
        for (Map.Entry<String, String[]> entry : TripulacionManager.ROLES_PIRATAS.entrySet()) {
            String nombreClase = entry.getKey();
            String[] datosPirata = entry.getValue();
            String nombrePirata = datosPirata[1];
            
            MemoriaPirata memoria = new MemoriaPirata(nombrePirata, nombreClase);
            memoriaPiratas.put(nombrePirata, memoria);
        }
        System.out.println("🏴‍☠️ Memorias pirata inicializadas: " + memoriaPiratas.size());
    }

    /**
     * 🔄 REGISTRAR INTERACCIÓN ENTRE PIRATAS
     */
    public void registrarInteraccion(String pirataPrincipal, String[] otrosPiratas, String contexto, String contenido) {
        try {
            // 🎯 REGISTRAR EN MEMORIA DEL PIRATA PRINCIPAL
            MemoriaPirata memoria = memoriaPiratas.get(pirataPrincipal);
            if (memoria != null) {
                memoria.agregarRecuerdo(contenido, "conversacion", otrosPiratas);
            }
            
            // 🎯 ACTUALIZAR RELACIONES
            for (String otroPirata : otrosPiratas) {
                if (!otroPirata.equals(pirataPrincipal)) {
                    String claveRelacion = generarClaveRelacion(pirataPrincipal, otroPirata);
                    RelacionPirata relacion = relaciones.computeIfAbsent(claveRelacion, 
                        k -> new RelacionPirata(pirataPrincipal, otroPirata));
                    
                    relacion.registrarInteraccion(contexto + ": " + contenido, 1);
                }
            }
            
            System.out.println("💬 Interacción registrada: " + pirataPrincipal + " con " + 
                             Arrays.toString(otrosPiratas));
                             
        } catch (Exception e) {
            System.err.println("💥 Error registrando interacción: " + e.getMessage());
        }
    }

    /**
     * 🧠 OBTENER MEMORIA DE UN PIRATA
     */
    public Optional<MemoriaPirata> obtenerMemoriaPirata(String nombrePirata) {
        return Optional.ofNullable(memoriaPiratas.get(nombrePirata));
    }

    /**
     * 📊 OBTENER TODOS LOS PIRATAS
     */
    public Set<String> obtenerTodosLosPiratas() {
        return Collections.unmodifiableSet(memoriaPiratas.keySet());
    }

    /**
     * 🔗 OBTENER RELACIONES DESTACADAS
     */
    public Map<RelacionPirata, Integer> obtenerRelacionesDestacadas() {
        Map<RelacionPirata, Integer> relacionesDestacadas = new HashMap<>();
        
        relaciones.values().stream()
            .filter(rel -> rel.getNivelConfianza() >= 5)
            .forEach(rel -> {
                relacionesDestacadas.put(rel, rel.getNivelConfianza());
            });
            
        return relacionesDestacadas;
    }

    /**
     * 💾 OBTENER FRAGMENTOS DESTACADOS
     */
    public List<FragmentoCodigo> obtenerFragmentosDestacados() {
        return memoriaPiratas.values().stream()
            .flatMap(memoria -> memoria.getFragmentosCodigo().values().stream())
            .sorted((f1, f2) -> Long.compare(f2.getTimestamp(), f1.getTimestamp()))
            .limit(10)
            .collect(Collectors.toList());
    }

    /**
     * 🎯 REGISTRAR DEBATE COMPLETO
     */
    public void registrarDebateCompleto(String pregunta, List<String> historialDebate, String[] participantes) {
        try {
            String contexto = "Debate sobre: " + pregunta;
            
            for (String intervencion : historialDebate) {
                // Extraer pirata de la intervención (formato: "Pirata: texto")
                if (intervencion.contains(":")) {
                    String pirata = intervencion.substring(0, intervencion.indexOf(":")).trim();
                    String contenido = intervencion.substring(intervencion.indexOf(":") + 1).trim();
                    
                    // Crear array de otros participantes
                    String[] otros = Arrays.stream(participantes)
                        .filter(p -> !p.equals(pirata))
                        .toArray(String[]::new);
                    
                    if (otros.length > 0) {
                        registrarInteraccion(pirata, otros, contexto, contenido);
                    }
                }
            }
            
            // 🎯 GUARDAR EN BITÁCORA
            guardarDebateEnBitacora(pregunta, historialDebate, participantes);
            
        } catch (Exception e) {
            System.err.println("💥 Error registrando debate completo: " + e.getMessage());
        }
    }

    /**
     * 🔧 AGREGAR FRAGMENTO DE CÓDIGO A PIRATA
     */
    public void agregarFragmentoCodigoPirata(String nombrePirata, String identificador, String codigo, String proposito) {
        MemoriaPirata memoria = memoriaPiratas.get(nombrePirata);
        if (memoria != null) {
            memoria.agregarFragmentoCodigo(identificador, codigo, proposito);
            System.out.println("💾 Fragmento de código agregado a " + nombrePirata + ": " + proposito);
        }
    }

    /**
     * 🆕 REGISTRAR NUEVO PIRATA EN LA MEMORIA
     */
    public void registrarNuevoPirata(String nombrePirata, String nombreClase) {
        try {
            bitacora.info("🏴‍☠️ Registrando nuevo pirata: " + nombrePirata + " -> " + nombreClase);
            
            // 🎯 VERIFICAR SI EL PIRATA YA EXISTE
            if (memoriaPiratas.containsKey(nombrePirata)) {
                bitacora.debug("🔄 Pirata ya existe, actualizando: " + nombrePirata);
                MemoriaPirata existente = memoriaPiratas.get(nombrePirata);
                existente.actualizarClase(nombreClase);
                return;
            }
            
            // 🎯 CREAR NUEVA MEMORIA PARA EL PIRATA
            MemoriaPirata nuevoPirata = new MemoriaPirata(nombrePirata, nombreClase);
            
            // 🎯 CONFIGURAR DATOS BÁSICOS BASADOS EN EL NOMBRE
            configurarPirataPorNombre(nuevoPirata, nombrePirata);
            
            // 🎯 AGREGAR A LA MEMORIA
            memoriaPiratas.put(nombrePirata, nuevoPirata);
            
            // 🎯 GUARDAR EN ARCHIVO
            guardarMemoriaPirata(nuevoPirata);
            
            bitacora.exito("✅ Nuevo pirata registrado: " + nombrePirata + " (" + nombreClase + ")");
            
        } catch (Exception e) {
            bitacora.error("💥 Error registrando nuevo pirata: " + nombrePirata, e);
        }
    }

    /**
     * 🎯 CONFIGURAR PIRATA POR NOMBRE
     */
    private void configurarPirataPorNombre(MemoriaPirata pirata, String nombrePirata) {
        // 🎯 CONFIGURAR ESPECIALIDAD BASADA EN EL NOMBRE
        if (nombrePirata.contains("Barbanegra") || nombrePirata.contains("Turbo")) {
            pirata.agregarHabilidad("Navegación avanzada");
            pirata.agregarHabilidad("Optimización de rendimiento");
            pirata.agregarHabilidad("Estrategias ofensivas");
            pirata.setPersonalidad("Agresivo pero estratégico");
        } 
        else if (nombrePirata.contains("Ojo de Halcón") || nombrePirata.contains("Halcón")) {
            pirata.agregarHabilidad("Análisis detallado");
            pirata.agregarHabilidad("Detección de problemas");
            pirata.agregarHabilidad("Visión a largo plazo");
            pirata.setPersonalidad("Observador y meticuloso");
        }
        else if (nombrePirata.contains("Sable Afilado")) {
            pirata.agregarHabilidad("Código limpio");
            pirata.agregarHabilidad("Refactorización");
            pirata.agregarHabilidad("Buenas prácticas");
            pirata.setPersonalidad("Perfeccionista y detallista");
        }
        else if (nombrePirata.contains("Mano de Papel")) {
            pirata.agregarHabilidad("Documentación");
            pirata.agregarHabilidad("Planificación");
            pirata.agregarHabilidad("Coordinación");
            pirata.setPersonalidad("Organizado y metódico");
        }
        else if (nombrePirata.contains("Rumbo Certero")) {
            pirata.agregarHabilidad("Navegación");
            pirata.agregarHabilidad("Planificación de rutas");
            pirata.agregarHabilidad("Toma de decisiones");
            pirata.setPersonalidad("Decisivo y confiable");
        }
        else if (nombrePirata.contains("Viejo Trueno")) {
            pirata.agregarHabilidad("Experiencia");
            pirata.agregarHabilidad("Sabiduría práctica");
            pirata.agregarHabilidad("Mentoría");
            pirata.setPersonalidad("Sabio y experimentado");
        }
        else if (nombrePirata.contains("Mente Brillante")) {
            pirata.agregarHabilidad("Estrategia avanzada");
            pirata.agregarHabilidad("Resolución de problemas complejos");
            pirata.agregarHabilidad("Innovación");
            pirata.setPersonalidad("Inteligente y creativo");
        }
        else if (nombrePirata.contains("Oráculo")) {
            pirata.agregarHabilidad("Predicción");
            pirata.agregarHabilidad("Análisis de tendencias");
            pirata.agregarHabilidad("Visión futura");
            pirata.setPersonalidad("Misterioso y sabio");
        }
        else {
            // 🎯 CONFIGURACIÓN POR DEFECTO
            pirata.agregarHabilidad("Adaptabilidad");
            pirata.agregarHabilidad("Trabajo en equipo");
            pirata.agregarHabilidad("Resolución de problemas");
            pirata.setPersonalidad("Equilibrado y colaborativo");
        }
        
        // 🎯 CONFIGURAR RELACIONES INICIALES
        configurarRelacionesIniciales(pirata, nombrePirata);
    }

    /**
     * 🔗 CONFIGURAR RELACIONES INICIALES
     */
    private void configurarRelacionesIniciales(MemoriaPirata nuevoPirata, String nombrePirata) {
        // 🎯 AGREGAR ALGUNAS RELACIONES BÁSICAS CON PIRATAS EXISTENTES
        for (String pirataExistente : memoriaPiratas.keySet()) {
            if (!pirataExistente.equals(nombrePirata)) {
                // 🎯 RELACIONES POSITIVAS CON PIRATAS COMPLEMENTARIOS
                if ((nombrePirata.contains("Barbanegra") && pirataExistente.contains("Halcón")) ||
                    (nombrePirata.contains("Halcón") && pirataExistente.contains("Barbanegra")) ||
                    (nombrePirata.contains("Sable") && pirataExistente.contains("Mano")) ||
                    (nombrePirata.contains("Mano") && pirataExistente.contains("Sable"))) {
                    nuevoPirata.registrarInteraccionPositiva(pirataExistente, "Relación complementaria natural");
                }
            }
        }
    }

    /**
     * 💾 GUARDAR MEMORIA DEL PIRATA EN ARCHIVO
     */
    private void guardarMemoriaPirata(MemoriaPirata pirata) {
        try {
            String directorio = "autogen-output/memoria/piratas/";
            FileUtils.crearDirectorioSiNoExiste(directorio);
            
            String archivo = directorio + pirata.getNombre().replace(" ", "_") + ".json";
            
            // 🎯 CREAR REPRESENTACIÓN JSON SIMPLIFICADA
            String json = "// Memoria Pirata: " + pirata.getNombre() + "\n" +
                         "Nombre: " + pirata.getNombre() + "\n" +
                         "Clase: " + pirata.getNombreClase() + "\n" +
                         "Personalidad: " + pirata.getPersonalidad() + "\n" +
                         "Habilidades: " + String.join(", ", pirata.getHabilidades()) + "\n" +
                         "Fecha Registro: " + Instant.now().toString() + "\n";
            
            FileUtils.writeToFile(archivo, json);
            bitacora.debug("💾 Memoria guardada: " + archivo);
            
        } catch (Exception e) {
            bitacora.error("💥 Error guardando memoria del pirata: " + pirata.getNombre(), e);
        }
    }

    /**
     * 🎯 INFERIR CLASE DESDE CÓDIGO - VERSIÓN MEJORADA
     */
    public String inferirClaseDesdeCodigo(String codigo, String nombrePirata) {
        try {
            bitacora.info("🔮 Intentando inferir clase para: " + nombrePirata);
            
            // 🎯 BUSCAR DECLARACIÓN DE CLASE EN EL CÓDIGO
            if (codigo.contains("public class")) {
                int start = codigo.indexOf("public class") + 12;
                int end = codigo.indexOf("{", start);
                if (end > start) {
                    String className = codigo.substring(start, end).trim().split("[\\s<{]")[0];
                    bitacora.info("✅ Clase inferida desde 'public class': " + className);
                    return className;
                }
            }
            
            if (codigo.contains("class ")) {
                int start = codigo.indexOf("class ") + 6;
                int end = codigo.indexOf("{", start);
                if (end > start) {
                    String className = codigo.substring(start, end).trim().split("[\\s<{]")[0];
                    bitacora.info("✅ Clase inferida desde 'class': " + className);
                    return className;
                }
            }
            
            // 🎯 BUSCAR PATRONES DE NOMBRE DE ARCHIVO EN EL CÓDIGO
            if (codigo.contains("package ")) {
                int packageStart = codigo.indexOf("package ") + 8;
                int packageEnd = codigo.indexOf(";", packageStart);
                if (packageEnd > packageStart) {
                    String packageName = codigo.substring(packageStart, packageEnd).trim();
                    // 🎯 CONSTRUIR NOMBRE DE CLASE BASADO EN PACKAGE Y NOMBRE PIRATA
                    String inferredClass = packageName + "." + 
                        nombrePirata.replace(" ", "").replace("de", "").replace("el", "");
                    bitacora.info("🎯 Clase inferida desde package: " + inferredClass);
                    return inferredClass;
                }
            }
            
            // 🎯 FALLBACK: GENERAR NOMBRE BASADO EN EL PIRATA
            String fallbackClass = "com.novelator.autogen.piratas." + 
                nombrePirata.replace(" ", "").replace("de", "").replace("el", "");
            bitacora.info("🔄 Usando clase fallback: " + fallbackClass);
            return fallbackClass;
            
        } catch (Exception e) {
            bitacora.error("💥 Error inferiendo clase para: " + nombrePirata, e);
            return "com.novelator.autogen.piratas." + nombrePirata.replace(" ", "");
        }
    }

    // =========================================================================
    // 🛠️ MÉTODOS AUXILIARES
    // =========================================================================

    private String generarClaveRelacion(String pirata1, String pirata2) {
        // 🎯 GENERAR CLAVE ÚNICA ORDENADA PARA LA RELACIÓN
        String[] piratas = {pirata1, pirata2};
        Arrays.sort(piratas);
        return piratas[0] + "::" + piratas[1];
    }

    private String generarNombrePirata(String nombreClase) {
        // 🎯 GENERAR NOMBRE PIRATA BASADO EN EL NOMBRE DE CLASE
        String[] nombresPirata = {"Rayo", "Tormenta", "Ciclón", "Marea", "Vendaval", "Abismo", "Corsario", "Bucanero"};
        String[] titulos = {"Veloz", "Bravo", "Salvaje", "Astuto", "Sagaz", "Intrépido", "Audaz"};
        
        Random random = new Random();
        String nombre = nombresPirata[random.nextInt(nombresPirata.length)];
        String titulo = titulos[random.nextInt(titulos.length)];
        
        return nombre + " " + titulo;
    }

    private void guardarDebateEnBitacora(String pregunta, List<String> historial, String[] participantes) {
        try {
            StringBuilder bitacora = new StringBuilder();
            bitacora.append("\n## 🏴‍☠️ DEBATE REGISTRADO\n");
            bitacora.append("**Fecha**: ").append(new Date()).append("\n");
            bitacora.append("**Pregunta**: ").append(pregunta).append("\n");
            bitacora.append("**Participantes**: ").append(String.join(", ", participantes)).append("\n\n");
            
            bitacora.append("### 📜 HISTORIAL DEL DEBATE\n");
            for (int i = 0; i < historial.size(); i++) {
                bitacora.append(i + 1).append(". ").append(historial.get(i)).append("\n\n");
            }
            bitacora.append("---\n");
            
            FileUtils.appendToFile("autogen-output/memoria-pirata/debates.md", bitacora.toString());
            
        } catch (Exception e) {
            System.err.println("💥 Error guardando debate en bitácora: " + e.getMessage());
        }
    }

    // =========================================================================
    // 📊 MÉTODOS DE VISUALIZACIÓN PARA EL CAPITÁN
    // =========================================================================

    /**
     * 👑 MOSTRAR ESTADO COMPLETO PARA EL CAPITÁN
     */
    public void mostrarEstadoCompleto() {
        System.out.println("\n" + "👑".repeat(80));
        System.out.println("           INFORME DEL CAPITÁN - SISTEMA DE MEMORIA PIRATA");
        System.out.println("👑".repeat(80));
        
        // 🎯 ESTADO GENERAL
        System.out.println("\n📊 ESTADO GENERAL:");
        System.out.println("  • Piratas registrados: " + memoriaPiratas.size());
        System.out.println("  • Relaciones activas: " + relaciones.size());
        System.out.println("  • Fragmentos de código: " + 
            memoriaPiratas.values().stream()
                .mapToInt(m -> m.getFragmentosCodigo().size())
                .sum());
        
        // 🎯 PIRATAS MÁS ACTIVOS
        System.out.println("\n🏆 PIRATAS MÁS ACTIVOS:");
        memoriaPiratas.entrySet().stream()
            .sorted((e1, e2) -> Integer.compare(e2.getValue().getRecuerdos().size(), 
                                              e1.getValue().getRecuerdos().size()))
            .limit(5)
            .forEach(entry -> {
                MemoriaPirata mem = entry.getValue();
                System.out.printf("  • %-15s: %d recuerdos, %d relaciones, %d habilidades%n",
                    mem.getNombre(), 
                    mem.getRecuerdos().size(),
                    mem.getRelaciones().size(),
                    mem.getHabilidades().size());
            });
        
        // 🎯 MEJORES AMIGOS
        System.out.println("\n🤝 MEJORES AMIGOS:");
        relaciones.values().stream()
            .filter(rel -> rel.getTipoRelacion().equals("amistad"))
            .sorted((r1, r2) -> Integer.compare(r2.getNivelConfianza(), r1.getNivelConfianza()))
            .limit(3)
            .forEach(rel -> {
                System.out.printf("  • %s ↔ %s (confianza: %d)%n",
                    rel.getPirata1(), rel.getPirata2(), rel.getNivelConfianza());
            });
        
        // 🎯 FRAGMENTOS RECIENTES
        System.out.println("\n💾 FRAGMENTOS DE CÓDIGO RECIENTES:");
        obtenerFragmentosDestacados().stream()
            .limit(3)
            .forEach(frag -> {
                System.out.printf("  • %s: %s%n", 
                    frag.getProposito(),
                    frag.getCodigo().length() > 40 ? 
                        frag.getCodigo().substring(0, 37) + "..." : frag.getCodigo());
            });
    }

    /**
     * 🔍 MOSTRAR DETALLES DE UN PIRATA ESPECÍFICO
     */
    public void mostrarDetallesPirata(String nombrePirata) {
        MemoriaPirata memoria = memoriaPiratas.get(nombrePirata);
        if (memoria == null) {
            System.out.println("❌ Pirata no encontrado: " + nombrePirata);
            return;
        }
        
        System.out.println("\n" + "🏴‍☠️".repeat(60));
        System.out.println("           DETALLES DE " + nombrePirata.toUpperCase());
        System.out.println("🏴‍☠️".repeat(60));
        
        System.out.println("📜 Clase: " + memoria.getNombreClase());
        System.out.println("🎯 Habilidades: " + String.join(", ", memoria.getHabilidades()));
        System.out.println("🧠 Recuerdos: " + memoria.getRecuerdos().size());
        System.out.println("🔗 Relaciones: " + memoria.getRelaciones().size());
        System.out.println("💾 Fragmentos: " + memoria.getFragmentosCodigo().size());
        
        // 🎯 MEJOR AMIGO
        memoria.obtenerMejorAmigo().ifPresent(amigo -> {
            System.out.println("🤝 Mejor amigo: " + amigo);
        });
        
        // 🎯 RECUERDOS RECIENTES
        System.out.println("\n📅 RECUERDOS RECIENTES:");
        memoria.getRecuerdos().stream()
            .sorted((r1, r2) -> Long.compare(r2.getTimestamp(), r1.getTimestamp()))
            .limit(3)
            .forEach(rec -> {
                System.out.println("  • " + rec);
            });
        
        // 🎯 RELACIONES DESTACADAS
        System.out.println("\n🔗 RELACIONES DESTACADAS:");
        memoria.getRelaciones().entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(3)
            .forEach(entry -> {
                System.out.println("  • " + entry.getKey() + " (confianza: " + entry.getValue() + ")");
            });
    }

    /**
     * 🧹 LIMPIAR MEMORIAS ANTIGUAS
     */
    public void limpiarMemoriasAntiguas() {
        long ahora = System.currentTimeMillis();
        long UMBRAL_ANTIGUEDAD = 30 * 24 * 60 * 60 * 1000L; // 30 días
        
        int recuerdosEliminados = 0;
        int fragmentosEliminados = 0;
        
        for (MemoriaPirata memoria : memoriaPiratas.values()) {
            // Eliminar recuerdos antiguos
            recuerdosEliminados += memoria.getRecuerdos().removeIf(
                recuerdo -> (ahora - recuerdo.getTimestamp()) > UMBRAL_ANTIGUEDAD
            ) ? 1 : 0;
            
            // Eliminar fragmentos antiguos
            fragmentosEliminados += memoria.getFragmentosCodigo().entrySet().removeIf(
                entry -> (ahora - entry.getValue().getTimestamp()) > UMBRAL_ANTIGUEDAD
            ) ? 1 : 0;
        }
        
        System.out.println("🧹 Memoria limpiada: " + recuerdosEliminados + 
                         " recuerdos y " + fragmentosEliminados + " fragmentos eliminados");
    }
}