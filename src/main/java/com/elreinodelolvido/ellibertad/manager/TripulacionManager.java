package com.elreinodelolvido.ellibertad.manager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.elreinodelolvido.ellibertad.api.OraculoDeLaLibertad;
import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.manager.ConsejoDeGuerraManager.IntervencionPirata;
import com.elreinodelolvido.ellibertad.memoria.SistemaMemoriaPirata;
import com.elreinodelolvido.ellibertad.memoria.SistemaMemoriaPirata.MemoriaPirata;
import com.elreinodelolvido.ellibertad.model.ClassInfo;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;
import com.elreinodelolvido.ellibertad.util.FileUtils;

/**
 * 🏴‍☠️ TRIPULACIÓN MANAGER - El Capitán que coordina la tripulación pirata
 * 🎯 Gestiona las preguntas y respuestas entre las clases del proyecto
 */
public class TripulacionManager {
    
    private final ProjectScanner scanner;
    private final OraculoDeLaLibertad oraculo;
    private final Bitacora bitacora;
    private final Map<String, PirataInfo> mapaTripulacion;
    private boolean sesionActiva;
    private final Scanner inputScanner;
	private SistemaMemoriaPirata memoria;
	private String rutaArchivo;
    
    // 🎪 MAPA DE TRIPULACIÓN PREDEFINIDO
    public static final Map<String, String[]> ROLES_PIRATAS = Map.of(
        "ProjectScanner", new String[]{"Vigía", "Ojo de Halcón", "Escudriña los mares del código en busca de tesoros ocultos"},
        "AutogenTurboFusion", new String[]{"Capitán", "Barbanegra Turbo", "Comanda el barco con poder y velocidad implacables"},
        "OraculoDeLaLibertad", new String[]{"Adivino", "El Oráculo", "Ve el futuro del código en las estrellas y runas ancestrales"},
        "DebugManager", new String[]{"Cirujano", "Sable Afilado", "Cura las heridas del código con precisión mortal"},
        "APIManager", new String[]{"Navegante", "Brujo de los Mares", "Domina los vientos y corrientes de las APIs lejanas"},
        "ReporteManager", new String[]{"Cartógrafo", "Mano de Papel", "Traza mapas detallados de las tierras conquistadas"},
        "SistemaManager", new String[]{"Contramaestre", "Viejo Trueno", "Mantiene el barco en orden y disciplina férrea"},
        "AnalisisManager", new String[]{"Estratega", "Mente Brillante", "Planifica las batallas con sabiduría ancestral"},
        "PlanificadorManager", new String[]{"Timonel", "Rumbo Certero", "Dirige el barco hacia destinos gloriosos"}
    );
    
    public TripulacionManager(ProjectScanner scanner, OraculoDeLaLibertad oraculo, Bitacora bitacora) {
        this.scanner = scanner;
        this.oraculo = (oraculo != null) ? oraculo : new OraculoDeLaLibertad(); // ✅ Fallback
        this.bitacora = (bitacora != null) ? bitacora : new Bitacora(); // ✅ Fallback
        this.mapaTripulacion = new ConcurrentHashMap<>();
        this.sesionActiva = false;
        this.inputScanner = new Scanner(System.in);
        
        // ✅ INICIALIZAR MEMORIA
        this.memoria = SistemaMemoriaPirata.obtenerInstancia();
        
        inicializarTripulacion();
    }
    
    /**
     * 🏴‍☠️ INICIAR SESIÓN CON LA TRIPULACIÓN
     */
    public void iniciarSesionTripulacion() {
        mostrarBannerInicio();
        sesionActiva = true;
        
        bitacora.exito("🏴‍☠️ SESIÓN DE TRIPULACIÓN INICIADA");
        
        while (sesionActiva) {
            ejecutarTurnoPregunta();
        }
        
        finalizarSesion();
    }
    
    /**
     * 🎯 EJECUTAR TURNO DE PREGUNTA
     */
    private void ejecutarTurnoPregunta() {
        try {
            // 1. SOLICITAR PREGUNTA AL USUARIO
            String pregunta = solicitarPreguntaUsuario();
            if (pregunta == null || pregunta.trim().isEmpty()) {
                return;
            }
            
            if (esComandoSalir(pregunta)) {
                sesionActiva = false;
                return;
            }
            
            bitacora.info("🎯 PREGUNTA RECIBIDA: " + pregunta);
            
            // 2. OBTENER CLASES DISPONIBLES
            List<ClassInfo> clasesDisponibles = obtenerClasesDisponibles();
            if (clasesDisponibles.isEmpty()) {
                System.out.println("❌ No hay tripulación disponible. Ejecuta scanProject primero.");
                return;
            }
            
            // 3. SELECCIONAR PIRATA PARA RESPONDER
            String claseElegida = seleccionarPirataParaPregunta(pregunta, clasesDisponibles);
            if (claseElegida == null) {
                System.out.println("❌ La tripulación no pudo decidir quién debe responder.");
                return;
            }
            
            // 4. OBTENER INFORMACIÓN DEL PIRATA
            PirataInfo pirata = mapaTripulacion.get(claseElegida);
            if (pirata == null) {
                pirata = crearPirataGenerico(claseElegida);
            }
            
            // 5. SIMULAR RESPUESTA DEL PIRATA
            String respuesta = simularRespuestaPirata(pregunta, pirata);
            
            // 6. MOSTRAR RESPUESTA
            mostrarRespuestaPirata(pirata, respuesta);
            
            // 7. REGISTRAR EN BITÁCORA
            registrarIntercambioBitacora(pregunta, pirata, respuesta);
            
        } catch (Exception e) {
            System.err.println("💥 Error en turno de pregunta: " + e.getMessage());
            bitacora.error("Error en TripulacionManager: " + e.getMessage());
        }
    }
    
    /**
     * 🗣️ SOLICITAR PREGUNTA AL USUARIO
     */
    private String solicitarPreguntaUsuario() {
        System.out.println("\n" + "⚓".repeat(60));
        System.out.println("🏴‍☠️  CONSULTA A LA TRIPULACIÓN");
        System.out.println("⚓".repeat(60));
        System.out.println("Escribe tu pregunta para la tripulación (o 'salir' para terminar):");
        System.out.print("🎯 > ");
        
        String pregunta = inputScanner.nextLine().trim();
        
        if (pregunta.isEmpty()) {
            System.out.println("⚠️  La pregunta no puede estar vacía.");
            return null;
        }
        
        return pregunta;
    }
    
    private List<ClassInfo> obtenerClasesDisponibles() {
        try {
            List<ClassInfo> clases = new ArrayList<>();
            
            // ✅ VERIFICAR SI EL SCANNER TIENE CLASES
            if (scanner != null) {
                // Intentar acceder a las clases mediante reflexión segura
                try {
                    // Buscar método que devuelva clases
                    for (java.lang.reflect.Method method : scanner.getClass().getMethods()) {
                        if (method.getReturnType().equals(List.class) && 
                            method.getParameterCount() == 0) {
                            Object result = method.invoke(scanner);
                            if (result instanceof List) {
                                clases = (List<ClassInfo>) result;
                                if (!clases.isEmpty()) break;
                            }
                        }
                    }
                } catch (Exception e) {
                    bitacora.debug("Reflexión falló: " + e.getMessage());
                }
            }
            
            // ✅ FALLBACK: USAR CLASES HARDCODEADAS DE ROLES_PIRATAS
            if (clases.isEmpty()) {
                bitacora.info("Usando clases hardcodeadas de ROLES_PIRATAS");
                for (String className : ROLES_PIRATAS.keySet()) {
                    ClassInfo classInfo = new ClassInfo();
                    classInfo.setName(className);
                    classInfo.setPackageName("com.elreinodelolvido.ellibertad");
                    classInfo.setFullName("com.elreinodelolvido.ellibertad." + className);
                    clases.add(classInfo);
                }
            }
            
            bitacora.info("Clases disponibles: " + clases.size());
            return clases;
            
        } catch (Exception e) {
            bitacora.error("Error crítico obteniendo clases: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    /**
     * 🎯 SELECCIONAR PIRATA PARA PREGUNTA
     */
    private String seleccionarPirataParaPregunta(String pregunta, List<ClassInfo> clases) {
        try {
            // Construir lista de nombres de clases
            StringBuilder listaClases = new StringBuilder();
            for (ClassInfo clase : clases) {
                listaClases.append("- ").append(clase.getFullName()).append("\n");
            }
            
            // Prompt para selección de clase
            String promptSeleccion = 
                "Eres el capitán de un barco pirata. Tienes esta tripulación (clases Java):\n\n" +
                listaClases.toString() + "\n" +
                "La pregunta de tu tripulante es: " + pregunta + "\n\n" +
                "¿Qué miembro de la tripulación (clase) está mejor cualificado para responder?\n" +
                "Considera:\n" +
                "1. La especialidad de cada clase\n" + 
                "2. La naturaleza de la pregunta\n" +
                "3. Las capacidades técnicas de cada clase\n\n" +
                "Devuelve SOLO el nombre completo de la clase elegida, sin explicaciones adicionales.";
            
            // Usar el oráculo para seleccionar
            String respuesta = oraculo.invocar(promptSeleccion, "seleccion_tripulacion", 0.3);
            
            if (respuesta == null || respuesta.trim().isEmpty()) {
                return seleccionarPirataAleatorio(clases);
            }
            
            // Extraer nombre de clase de la respuesta
            String claseElegida = extraerNombreClaseDeRespuesta(respuesta, clases);
            
            if (claseElegida != null) {
                bitacora.info("🎯 PIRATA SELECCIONADO: " + claseElegida);
                return claseElegida;
            }
            
            // Fallback: selección aleatoria
            return seleccionarPirataAleatorio(clases);
            
        } catch (Exception e) {
            bitacora.error("Error seleccionando pirata: " + e.getMessage());
            return seleccionarPirataAleatorio(clases);
        }
    }
    
    String simularRespuestaPirata(String pregunta, PirataInfo pirata) {
        try {
            // ✅ OBTENER CONTEXTO COMPLETO
            String codigoFuente = obtenerCodigoFuente(pirata.getNombreClase());
            String contextoMemoria = obtenerContextoMemoriaPirata(pirata.getNombrePirata(), pregunta);
            
            // ✅ CONSTRUIR PROMPT MEJORADO
            String prompt = construirPromptCompleto(pregunta, pirata, codigoFuente, contextoMemoria);
            
            return oraculo.invocar(prompt, "respuesta_pirata_mejorada", 0.7);
            
        } catch (Exception e) {
            bitacora.error("Error en respuesta pirata: " + e.getMessage());
            return generarRespuestaFallback(pirata, pregunta);
        }
    }

    private String construirPromptCompleto(String pregunta, PirataInfo pirata, String codigoFuente, String contextoMemoria) {
        StringBuilder prompt = new StringBuilder();
        
        // 🎯 SECCIÓN 1: IDENTIDAD PIRATA COMPLETA
        prompt.append("🏴‍☠️ CONSULTA PIRATA - IDENTIDAD COMPLETA 🏴‍☠️\n\n");
        prompt.append("👤 INFORMACIÓN DEL PIRATA:\n");
        prompt.append("═══════════════════════════════════════════\n");
        prompt.append("• Clase Java: ").append(pirata.getNombreClase()).append("\n");
        prompt.append("• Rol en la tripulación: ").append(pirata.getRolPirata()).append("\n");
        prompt.append("• Nombre Pirata: ").append(pirata.getNombrePirata()).append("\n");
        prompt.append("• Especialidad: ").append(pirata.getDescripcionRol()).append("\n");
        prompt.append("═══════════════════════════════════════════\n\n");
        
        // 🧠 SECCIÓN 2: MEMORIA Y CONTEXTO HISTÓRICO
        prompt.append("🧠 MEMORIA Y EXPERIENCIAS DEL PIRATA:\n");
        prompt.append("═══════════════════════════════════════════\n");
        if (contextoMemoria != null && !contextoMemoria.trim().isEmpty()) {
            prompt.append(contextoMemoria).append("\n");
        } else {
            prompt.append("• Este pirata tiene un historial fresco y está listo para nuevas aventuras\n");
            prompt.append("• Sin recuerdos específicos previos registrados\n");
            prompt.append("• Potencial ilimitado para crear nuevas leyendas\n");
        }
        prompt.append("═══════════════════════════════════════════\n\n");
        
        // 💾 SECCIÓN 3: CÓDIGO FUENTE COMPLETO
        prompt.append("💾 CÓDIGO FUENTE COMPLETO - ARSENAL TÉCNICO:\n");
        prompt.append("═══════════════════════════════════════════\n");
        prompt.append("```java\n");
        
        if (codigoFuente != null && !codigoFuente.trim().isEmpty()) {
            prompt.append(codigoFuente).append("\n");
        } else {
            prompt.append("// ¡Por todos los océanos! El código fuente no está disponible temporalmente.\n");
            prompt.append("// Pero el espíritu pirata y conocimiento técnico permanecen firmes.\n");
            prompt.append("// El pirata ").append(pirata.getNombrePirata()).append(" está listo para la acción.\n");
        }
        
        prompt.append("```\n");
        prompt.append("═══════════════════════════════════════════\n\n");
        
        // ❓ SECCIÓN 4: PREGUNTA PRINCIPAL
        prompt.append("🎯 PREGUNTA DEL CAPITÁN - MISIÓN ACTUAL:\n");
        prompt.append("═══════════════════════════════════════════\n");
        prompt.append("« ").append(pregunta).append(" »\n");
        prompt.append("═══════════════════════════════════════════\n\n");
        
        // 📜 SECCIÓN 5: INSTRUCCIONES DETALLADAS
        prompt.append("""
            📜 INSTRUCCIONES DETALLADAS DE RESPUESTA:
            ═══════════════════════════════════════════
            
            RESPUESTA COMO PIRATA TÉCNICO - FORMATO ESTRICTO:
            
            🌊 PERSPECTIVA PIRATA (3-5 líneas)
            • Responde manteniendo tu personalidad de pirata
            • Usa metáforas náuticas y lenguaje épico
            • Establece tu tono y carácter único
            • Conecta tu rol pirata con la pregunta técnica
            
            ⚙️ ANÁLISIS TÉCNICO DETALLADO (5-10 líneas)
            • Examina tu código fuente completo línea por línea
            • Identifica métodos, propiedades y funcionalidades relevantes
            • Explica cómo tu implementación actual se relaciona con la pregunta
            • Señala fortalezas y debilidades técnicas específicas
            • Usa ejemplos concretos de tu código
            
            🚀 MEJORAS TÉCNICAS ESPECÍFICAS (5-8 líneas)
            • Propone mejoras basadas en tu análisis del código
            • Incluye ejemplos de código mejorado si es apropiado
            • Sé técnicamente preciso y realista
            • Considera patrones de diseño y mejores prácticas
            
            🔥 REFACTORS CON TEMÁTICA PIRATA (4-6 líneas)
            • Sugiere refactorizaciones creativas
            • Usa nombres piratas memorables para métodos/clases
            • Ejemplos: navegarACodigoTurbo(), buscarTesoroDePatrones()
            • Mantén la coherencia técnica mientras aplicas la temática
            
            🤝 COLABORACIONES ESTRATÉGICAS (3-5 líneas)
            • Identifica otros piratas/clases que podrían ayudarte
            • Basado en tu memoria y relaciones existentes
            • Propone alianzas específicas y beneficios mutuos
            • Explica cómo la colaboración resolvería el problema
            
            💡 PLAN DE ACCIÓN INMEDIATO (4-6 líneas)
            • Lista acciones concretas y realizables
            • Prioriza por impacto y facilidad de implementación
            • Sé específico, medible y con plazos implícitos
            • Incluye métricas de éxito si es posible
            
            CRITERIOS DE EVALUACIÓN:
            ✓ Coherencia entre personalidad pirata y análisis técnico
            ✓ Profundidad del análisis del código fuente
            ✓ Calidad y realismo de las mejoras propuestas
            ✓ Creatividad en los nombres y metáforas piratas
            ✓ Utilidad práctica de las recomendaciones
            ✓ Claridad y estructura de la respuesta
            
            ¡Tu código fuente completo está disponible arriba - úsalo exhaustivamente!
            """);
        
        // 🏆 SECCIÓN 6: CIERRE ÉPICO
        prompt.append("\n\n🏴‍☠️ CIERRE ÉPICO - LLAMADO A LA ACCIÓN:\n");
        prompt.append("═══════════════════════════════════════════\n");
        prompt.append("¡QUE LOS MARES DEL CÓDIGO TE OBEDEZCAN, VALIENTE ").append(pirata.getNombrePirata()).append("!\n");
        prompt.append("EL DESTINO TÉCNICO DEL BARCO ESTÁ EN TUS MANOS.\n");
        prompt.append("RESPONDE CON LA SABIDURÍA DE TUS EXPERIENCIAS \n");
        prompt.append("Y EL CORAJE DE TU ESPÍRITU PIRATA. ¡ARRR! 🏴‍☠️\n");
        prompt.append("═══════════════════════════════════════════\n");
        
        return prompt.toString();
    }

	private String obtenerContextoMemoriaPirata(String nombrePirata, String pregunta) {
        try {
            Optional<MemoriaPirata> memoriaOpt = memoria.obtenerMemoriaPirata(nombrePirata);
            if (memoriaOpt.isPresent()) {
                return memoriaOpt.get().obtenerContextoPersonalizado(pregunta);
            }
            
            // ✅ CREAR MEMORIA SI NO EXISTE
            String nombreClase = encontrarClasePorPirata(nombrePirata);
            if (nombreClase != null) {
                memoria.registrarNuevoPirata(nombrePirata, nombreClase);
                return "🧠 Nueva memoria creada para " + nombrePirata;
            }
            
            return "🧠 Sin historial previo disponible";
            
        } catch (Exception e) {
            return "🧠 Memoria temporalmente no disponible";
        }
    }

    private String encontrarClasePorPirata(String nombrePirata) {
        for (Map.Entry<String, String[]> entry : ROLES_PIRATAS.entrySet()) {
            if (entry.getValue()[1].equals(nombrePirata)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    private String obtenerCodigoFuente(String nombreClase) {
        try {
            // ✅ BÚSQUEDA ROBUSTA EN MÚLTIPLES UBICACIONES
            String[] posiblesPaths = {
                "src/main/java/" + nombreClase.replace('.', '/') + ".java",
                "src/test/java/" + nombreClase.replace('.', '/') + ".java", 
                nombreClase.replace('.', '/') + ".java",
                "src/" + nombreClase.replace('.', '/') + ".java",
                "./" + nombreClase.replace('.', '/') + ".java"
            };
            
            for (String path : posiblesPaths) {
                if (FileUtils.verificarArchivo(path)) {
                    String codigo = FileUtils.readFile(path);
                    if (codigo != null && !codigo.trim().isEmpty()) {
                        bitacora.info("✅ Código encontrado en: " + path);
                        return codigo;
                    }
                }
            }
            
            // ✅ FALLBACK: CÓDIGO DE EJEMPLO BASADO EN EL ROL
            return generarCodigoEjemplo(nombreClase);
            
        } catch (Exception e) {
            bitacora.error("Error obteniendo código fuente: " + e.getMessage());
            return generarCodigoEjemplo(nombreClase);
        }
    }

    private String generarCodigoEjemplo(String nombreClase) {
        // ✅ GENERAR CÓDIGO DE EJEMPLO RELEVANTE
        String[] datosPirata = ROLES_PIRATAS.get(nombreClase);
        if (datosPirata != null) {
            return """
                // 🏴‍☠️ CÓDIGO DE %s - %s
                // %s
                
                public class %s {
                    // Este pirata se especializa en %s
                    // Sus responsabilidades incluyen funcionalidades específicas del sistema
                    
                    public void ejecutarTareaPrincipal() {
                        // Implementación específica del rol pirata
                        System.out.println("¡%s en acción!");
                    }
                    
                    // Métodos adicionales según el rol...
                }
                """.formatted(
                    datosPirata[1], datosPirata[0], datosPirata[2],
                    nombreClase, datosPirata[0].toLowerCase(), datosPirata[1]
                );
        }
        
        // ✅ FALLBACK GENÉRICO
        return """
            // 🏴‍☠️ CÓDIGO DE %s
            // Información detallada no disponible temporalmente
            
            public class %s {
                // Clase responsable de funcionalidades específicas del sistema
                // El pirata asociado tiene conocimientos especializados
                
                public void demostrarHabilidades() {
                    System.out.println("¡%s listo para la acción!");
                }
            }
            """.formatted(nombreClase, nombreClase, nombreClase);
    }
    
    /**
     * 🎯 BUSCAR ARCHIVO RECURSIVAMENTE EN TODAS LAS CARPETAS
     */
    private String buscarArchivoJava(String nombreArchivo) {
        try {
            Path directorioActual = Paths.get(".").toAbsolutePath().normalize();
            bitacora.debug("🔍 Buscando recursivamente desde: " + directorioActual);
            
            // 🎯 CONFIGURAR EXCLUSIONES PARA EVITAR BUCLE INFINITO
            Set<String> excludedDirs = Set.of(
                "target", "build", ".git", "node_modules", 
                "bin", "out", "dist", ".idea", ".vscode",
                "autogen-output" // Excluir nuestra propia salida
            );
            
            Optional<Path> archivoEncontrado = Files.walk(directorioActual, Integer.MAX_VALUE)
                    .filter(path -> {
                        // 🎯 EXCLUIR DIRECTORIOS NO DESEADOS
                        String nombreDir = path.getFileName() != null ? 
                            path.getFileName().toString() : "";
                        if (excludedDirs.contains(nombreDir)) {
                            return false;
                        }
                        
                        // 🎯 VERIFICAR SI ES EL ARCHIVO QUE BUSCAMOS
                        return path.toString().endsWith(nombreArchivo) && 
                               Files.isRegularFile(path);
                    })
                    .findFirst();
            
            if (archivoEncontrado.isPresent()) {
                String ruta = archivoEncontrado.get().toString();
                bitacora.debug("✅ Archivo encontrado recursivamente: " + ruta);
                this.rutaArchivo = ruta; // 🎯 ACTUALIZAR PARA FUTURAS CONSULTAS
                return ruta;
            } else {
                bitacora.debug("❌ Archivo no encontrado recursivamente: " + nombreArchivo);
            }
            
        } catch (IOException e) {
            bitacora.error("💥 Error en búsqueda recursiva: " + e.getMessage());
        }
        
        return "";
    }

    /**
     * 🎯 BUSCAR ARCHIVO EN ESTRUCTURAS DE PROYECTO COMUNES - MEJORADO
     */
    private String buscarArchivoEnEstructuras(String nombrePirata) {
        try {
            bitacora.debug("🔍 Búsqueda en estructuras para: " + nombrePirata);
            
            // 🎯 CONVERTIR NOMBRE PIRATA A POSIBLE NOMBRE DE ARCHIVO
            String nombreArchivo = convertirNombrePirataAArchivo(nombrePirata);
            
            // 🎯 ESTRUCTURAS DE PROYECTO MÁS COMPLETAS
            String[] estructuras = {
                "src/main/java/",
                "src/test/java/", 
                "src/",
                "main/java/",
                "test/java/",
                "app/src/main/java/",
                "app/src/test/java/",
                "source/",
                "sources/",
                "java/",
                "" // 🎯 BUSCAR DESDE LA RAIZ TAMBIÉN
            };
            
            // 🎯 BUSCAR EN CADA ESTRUCTURA
            for (String estructura : estructuras) {
                String rutaCompleta = estructura + nombreArchivo;
                File archivo = new File(rutaCompleta);
                
                if (archivo.exists() && archivo.isFile()) {
                    bitacora.debug("✅ Encontrado en estructura: " + rutaCompleta);
                    this.rutaArchivo = rutaCompleta;
                    return rutaCompleta;
                }
                
                // 🎯 BUSCAR RECURSIVAMENTE EN CADA ESTRUCTURA
                if (!estructura.isEmpty()) {
                    File directorioEstructura = new File(estructura);
                    if (directorioEstructura.exists() && directorioEstructura.isDirectory()) {
                        String rutaRecursiva = buscarEnDirectorioRecursivo(directorioEstructura, nombreArchivo);
                        if (!rutaRecursiva.isEmpty()) {
                            bitacora.debug("✅ Encontrado recursivamente en estructura: " + rutaRecursiva);
                            this.rutaArchivo = rutaRecursiva;
                            return rutaRecursiva;
                        }
                    }
                }
            }
            
            // 🎯 BUSCAR RECURSIVAMENTE DESDE EL DIRECTORIO ACTUAL
            String rutaRecursiva = buscarArchivoJava(nombreArchivo);
            if (!rutaRecursiva.isEmpty()) {
                return rutaRecursiva;
            }
            
            bitacora.debug("❌ No encontrado en estructuras: " + nombreArchivo);
            
        } catch (Exception e) {
            bitacora.error("💥 Error en búsqueda de estructuras: " + e.getMessage());
        }
        
        return "";
    }

    /**
     * 🎯 BUSCAR EN DIRECTORIO ESPECÍFICO DE FORMA RECURSIVA
     */
    private String buscarEnDirectorioRecursivo(File directorio, String nombreArchivo) {
        try {
            if (!directorio.exists() || !directorio.isDirectory()) {
                return "";
            }
            
            // 🎯 EXCLUSIONES PARA EVITAR BUCLE INFINITO
            Set<String> excludedDirs = Set.of(
                "target", "build", ".git", "node_modules", 
                "bin", "out", "dist", ".idea", ".vscode",
                "autogen-output"
            );
            
            // 🎯 BUSCAR RECURSIVAMENTE
            return Files.walk(directorio.toPath(), Integer.MAX_VALUE)
                    .filter(path -> {
                        String nombreDir = path.getFileName() != null ? 
                            path.getFileName().toString() : "";
                        
                        // 🎯 EXCLUIR DIRECTORIOS NO DESEADOS
                        if (excludedDirs.contains(nombreDir)) {
                            return false;
                        }
                        
                        // 🎯 VERIFICAR SI ES EL ARCHIVO QUE BUSCAMOS
                        return path.toString().endsWith(nombreArchivo) && 
                               Files.isRegularFile(path);
                    })
                    .findFirst()
                    .map(Path::toString)
                    .orElse("");
            
        } catch (IOException e) {
            bitacora.debug("⚠️ Error buscando en directorio " + directorio + ": " + e.getMessage());
            return "";
        }
    }

    /**
     * 🎯 CONVERTIR NOMBRE PIRATA A NOMBRE DE ARCHIVO - MEJORADO
     */
    private String convertirNombrePirataAArchivo(String nombrePirata) {
        if (nombrePirata == null || nombrePirata.trim().isEmpty()) {
            return "PirataDesconocido.java";
        }
        
        // 🎯 LIMPIAR Y FORMATEAR EL NOMBRE
        String nombreLimpio = nombrePirata
            .replaceAll("[^a-zA-Z0-9\\s]", "") // Mantener solo letras, números y espacios
            .replaceAll("\\s+", " ") // Unificar espacios múltiples
            .trim();
        
        if (nombreLimpio.isEmpty()) {
            return "PirataDesconocido.java";
        }
        
        // 🎯 SEPARAR POR ESPACIOS Y CAPITALIZAR
        String[] palabras = nombreLimpio.split("\\s+");
        StringBuilder nombreArchivo = new StringBuilder();
        
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                // 🎯 CAPITALIZAR PRIMERA LETRA, MINÚSCULAS EL RESTO
                String palabraFormateada = palabra.substring(0, 1).toUpperCase() + 
                                         palabra.substring(1).toLowerCase();
                nombreArchivo.append(palabraFormateada);
            }
        }
        
        // 🎯 VARIACIONES POSIBLES
        String[] variaciones = {
            nombreArchivo.toString() + ".java",
            nombreArchivo.toString() + "Pirata.java",
            nombreArchivo.toString() + "Class.java",
            // 🎯 PARA NOMBRES CON ROLES COMO "Rumbo Certero (Timonel)"
            nombreLimpio.replaceAll("[^a-zA-Z0-9]", "").replaceAll("\\s+", "") + ".java"
        };
        
        // 🎯 VERIFICAR SI ALGUNA VARIACIÓN EXISTE
        for (String variacion : variaciones) {
            // Verificar si existe en alguna estructura común
            if (existeArchivoEnAlgunaEstructura(variacion)) {
                return variacion;
            }
        }
        
        // 🎯 FALLBACK: USAR LA PRIMERA VARIACIÓN
        return variaciones[0];
    }

    /**
     * 🎯 VERIFICAR SI UN ARCHIVO EXISTE EN ALGUNA ESTRUCTURA COMÚN
     */
    private boolean existeArchivoEnAlgunaEstructura(String nombreArchivo) {
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
        
        for (String estructura : estructuras) {
            String rutaCompleta = estructura + nombreArchivo;
            File archivo = new File(rutaCompleta);
            if (archivo.exists() && archivo.isFile()) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 🎯 MÉTODO PRINCIPAL MEJORADO DE BÚSQUEDA DIRECTA
     */
    private String buscarCodigoFuenteDirecto(String nombrePirata) {
        try {
            bitacora.info("🔍 Búsqueda directa turbo para: " + nombrePirata);
            
            // 🎯 BUSCAR EN MEMORIA PRIMERO
            Optional<SistemaMemoriaPirata.MemoriaPirata> memoriaPirata = memoria.obtenerMemoriaPirata(nombrePirata);
            if (memoriaPirata.isPresent()) {
                String rutaArchivo = memoriaPirata.get().getRutaArchivo();
                if (rutaArchivo != null && !rutaArchivo.isEmpty() && FileUtils.verificarArchivo(rutaArchivo)) {
                    bitacora.info("✅ Encontrado en ruta de memoria: " + rutaArchivo);
                    return FileUtils.readFile(rutaArchivo);
                }
            }
            
            // 🎯 BUSQUEDA EN ESTRUCTURAS COMUNES MEJORADA
            String rutaEncontrada = buscarArchivoEnEstructuras(nombrePirata);
            if (!rutaEncontrada.isEmpty()) {
                bitacora.info("✅ Encontrado en búsqueda estructurada: " + rutaEncontrada);
                return FileUtils.readFile(rutaEncontrada);
            }
            
            // 🎯 BUSQUEDA POR NOMBRE DE CLASE (si está disponible)
            if (memoriaPirata.isPresent()) {
                String nombreClase = memoriaPirata.get().getNombreClase();
                if (nombreClase != null && !nombreClase.isEmpty()) {
                    String rutaPorClase = buscarPorNombreClase(nombreClase);
                    if (!rutaPorClase.isEmpty()) {
                        bitacora.info("✅ Encontrado por nombre de clase: " + rutaPorClase);
                        return FileUtils.readFile(rutaPorClase);
                    }
                }
            }
            
            bitacora.warn("❌ No se encontró código fuente para: " + nombrePirata);
            return null;
            
        } catch (Exception e) {
            bitacora.error("💥 Error en búsqueda directa turbo: " + e.getMessage());
            return null;
        }
    }

    /**
     * 🎯 BUSCAR POR NOMBRE DE CLASE COMPLETO
     */
    private String buscarPorNombreClase(String nombreClase) {
        try {
            if (nombreClase == null || nombreClase.isEmpty()) {
                return "";
            }
            
            // 🎯 CONVERTIR NOMBRE DE CLASE A RUTA DE ARCHIVO
            String rutaClase = nombreClase.replace('.', '/') + ".java";
            
            // 🎯 BUSCAR EN ESTRUCTURAS COMUNES
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
            
            for (String estructura : estructuras) {
                String rutaCompleta = estructura + rutaClase;
                File archivo = new File(rutaCompleta);
                if (archivo.exists() && archivo.isFile()) {
                    return rutaCompleta;
                }
            }
            
            // 🎯 BUSCAR RECURSIVAMENTE
            return buscarArchivoJava(rutaClase);
            
        } catch (Exception e) {
            bitacora.debug("⚠️ Error buscando por clase " + nombreClase + ": " + e.getMessage());
            return "";
        }
    }
    
    /**
     * 🎭 MOSTRAR RESPUESTA DEL PIRATA
     */
    private void mostrarRespuestaPirata(PirataInfo pirata, String respuesta) {
        System.out.println("\n" + "🌊".repeat(80));
        System.out.println("🏴‍☠️  RESPUESTA DE " + pirata.getNombrePirata().toUpperCase());
        System.out.println("📜 Rol: " + pirata.getRolPirata() + " | Clase: " + pirata.getNombreClase());
        System.out.println("🌊".repeat(80));
        
        if (respuesta != null) {
            System.out.println(respuesta);
        } else {
            System.out.println("❌ El pirata no pudo responder en este momento.");
        }
        
        System.out.println("\n" + "⚓".repeat(80));
    }
    
    // =========================================================================
    // 🛠️ MÉTODOS AUXILIARES
    // =========================================================================
    
    /**
     * 🎪 INICIALIZAR TRIPULACIÓN
     */
    private void inicializarTripulacion() {
        for (Map.Entry<String, String[]> entry : ROLES_PIRATAS.entrySet()) {
            String[] datosPirata = entry.getValue();
            mapaTripulacion.put(entry.getKey(), 
                new PirataInfo(entry.getKey(), datosPirata[0], datosPirata[1], datosPirata[2]));
        }
        bitacora.info("🏴‍☠️ Tripulación inicializada con " + mapaTripulacion.size() + " piratas");
    }
    
    /**
     * 🎲 SELECCIONAR PIRATA ALEATORIO
     */
    private String seleccionarPirataAleatorio(List<ClassInfo> clases) {
        if (clases.isEmpty()) return null;
        Random random = new Random();
        ClassInfo claseAleatoria = clases.get(random.nextInt(clases.size()));
        return claseAleatoria.getFullName();
    }
    
    /**
     * 🔍 EXTRAER NOMBRE DE CLASE DE RESPUESTA
     */
    private String extraerNombreClaseDeRespuesta(String respuesta, List<ClassInfo> clases) {
        String respuestaLimpia = respuesta.trim();
        
        // Buscar coincidencia exacta
        for (ClassInfo clase : clases) {
            if (respuestaLimpia.contains(clase.getFullName())) {
                return clase.getFullName();
            }
        }
        
        // Buscar por nombre simple
        for (ClassInfo clase : clases) {
            if (respuestaLimpia.contains(clase.getName())) {
                return clase.getFullName();
            }
        }
        
        return null;
    }
    
    /**
     * 🆘 GENERAR RESPUESTA FALLBACK
     */
    private String generarRespuestaFallback(PirataInfo pirata, String pregunta) {
        return "🎯 ¡Arrr, " + pirata.getNombrePirata() + " al habla! \n" +
               "Como " + pirata.getRolPirata().toLowerCase() + " de este barco, escucho tu pregunta: '" + pregunta + "'\n\n" +
               "🔧 Mi análisis técnico está temporalmente nublado...\n\n" +
               "⚡ Sugiero revisar mis cañones (métodos) y velas (propiedades)\n\n" +
               "🏴‍☠️ ¡Necesito un buen viento (conexión API) para darte mejores respuestas!";
    }
    
    /**
     * 📝 REGISTRAR INTERCAMBIO EN BITÁCORA
     */
    private void registrarIntercambioBitacora(String pregunta, PirataInfo pirata, String respuesta) {
        try {
            String id = UUID.randomUUID().toString().substring(0, 8);
            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            String md = """

                ---
                ## 🏴‍☠️ Consulta a la Tripulación (%s)
                **ID**: `%s`  
                **Fecha**: %s  
                **Pirata**: %s (%s)
                **Clase**: %s
                ---

                ### ❓ Pregunta:
                ```
                %s
                ```

                ### 🎯 Respuesta:
                ```
                %s
                ```

                ---
                *Fin del intercambio*
                
                """.formatted(pirata.getRolPirata(), id, timestamp, 
                    pirata.getNombrePirata(), pirata.getRolPirata(),
                    pirata.getNombreClase());

            FileUtils.crearArchivoSiNoExiste("autogen-output/bitacora-tripulacion.md", 
                "# 🏴‍☠️ Bitácora de la Tripulación\n\n*Registro de todas las consultas a la tripulación*\n\n");
            
            FileUtils.appendToFile("autogen-output/bitacora-tripulacion.md", md);
            
        } catch (Exception e) {
            System.err.println("💥 Error registrando en bitácora: " + e.getMessage());
        }
    }
    
    /**
     * 🚪 VERIFICAR COMANDO DE SALIDA
     */
    private boolean esComandoSalir(String input) {
        return input.equalsIgnoreCase("salir") || 
               input.equalsIgnoreCase("exit") || 
               input.equalsIgnoreCase("quit") ||
               input.equalsIgnoreCase("adios");
    }
   
    
    /**
     * 📦 EXTRAER PACKAGE DEL NOMBRE COMPLETO
     */
    private String extraerPackage(String fullClassName) {
        if (fullClassName == null) return "";
        int lastDot = fullClassName.lastIndexOf('.');
        return lastDot > 0 ? fullClassName.substring(0, lastDot) : "";
    }
    
    /**
     * 🏴‍☠️ CREAR PIRATA GENÉRICO PARA CLASES NO MAPEADAS
     */
    private PirataInfo crearPirataGenerico(String nombreClase) {
        String[] rolesGenericos = {
            "Grumete", "Marinero", "Artillero", "Cocinero", "Tesorero"
        };
        String[] nombresGenericos = {
            "Rayo Veloz", "Diente de Sable", "Ciclón", "Marea Brava", "Vendaval"
        };
        
        Random random = new Random();
        String rol = rolesGenericos[random.nextInt(rolesGenericos.length)];
        String nombre = nombresGenericos[random.nextInt(nombresGenericos.length)];
        String descripcion = "Valiente miembro de la tripulación especializado en " + nombreClase;
        
        PirataInfo pirata = new PirataInfo(nombreClase, rol, nombre, descripcion);
        mapaTripulacion.put(nombreClase, pirata);
        
        return pirata;
    }
    
    /**
     * 🎪 MOSTRAR BANNER DE INICIO
     */
    private void mostrarBannerInicio() {
        System.out.println("\n" + "🏴‍☠️".repeat(80));
        System.out.println("                  ¡TRIPULACIÓN PIRATA ACTIVADA!");
        System.out.println("🌊 Cada clase de tu proyecto es un pirata con habilidades únicas");
        System.out.println("🎯 Haz preguntas y deja que la tripulación te guíe");
        System.out.println("🏴‍☠️".repeat(80));
        System.out.println("\nMiembros de la tripulación disponibles:");
        
        mapaTripulacion.values().forEach(pirata -> {
            System.out.printf("  • %s (%s) - %s%n", 
                pirata.getNombrePirata(), pirata.getRolPirata(), pirata.getNombreClase());
        });
        
        System.out.println("\n" + "⚓".repeat(80));
    }
    
    /**
     * 📋 FINALIZAR SESIÓN
     */
    private void finalizarSesion() {
        System.out.println("\n" + "🌅".repeat(80));
        System.out.println("                  SESIÓN DE TRIPULACIÓN FINALIZADA");
        System.out.println("🏴‍☠️ La tripulación descansa... hasta la próxima aventura!");
        System.out.println("📜 Bitácora guardada en: autogen-output/bitacora-tripulacion.md");
        System.out.println("🌅".repeat(80));
        
        bitacora.exito("🏴‍☠️ SESIÓN DE TRIPULACIÓN FINALIZADA");
        inputScanner.close();
    }
    
    // =========================================================================
    // 🎪 CLASE INTERNA PIRATA INFO
    // =========================================================================
    
    /**
     * 🏴‍☠️ INFORMACIÓN DE UN PIRATA/CLASE
     */
    public static class PirataInfo {
        private final String nombreClase;
        private final String rolPirata;
        private final String nombrePirata;
        private final String descripcionRol;
        
        public PirataInfo(String nombreClase, String rolPirata, String nombrePirata, String descripcionRol) {
            this.nombreClase = nombreClase;
            this.rolPirata = rolPirata;
            this.nombrePirata = nombrePirata;
            this.descripcionRol = descripcionRol;
        }
        
        // Getters
        public String getNombreClase() { return nombreClase; }
        public String getRolPirata() { return rolPirata; }
        public String getNombrePirata() { return nombrePirata; }
        public String getDescripcionRol() { return descripcionRol; }
        
        @Override
        public String toString() {
            return String.format("%s (%s) - %s", nombrePirata, rolPirata, nombreClase);
        }
    }
    
    // =========================================================================
    // 🔧 MÉTODOS PÚBLICOS ADICIONALES
    // =========================================================================
    
    /**
     * 📊 MOSTRAR ESTADO DE LA TRIPULACIÓN
     */
    public void mostrarEstadoTripulacion() {
        System.out.println("\n🏴‍☠️ ESTADO DE LA TRIPULACIÓN:");
        System.out.println("Sesión activa: " + (sesionActiva ? "✅" : "❌"));
        System.out.println("Piratas registrados: " + mapaTripulacion.size());
        
        mapaTripulacion.values().forEach(pirata -> {
            System.out.printf("  🏴‍☠️ %-15s → %-12s (%s)%n",
                pirata.getNombrePirata(), pirata.getRolPirata(), pirata.getNombreClase());
        });
    }
    
    /**
     * 🔄 AGREGAR PIRATA PERSONALIZADO
     */
    public void agregarPirata(String nombreClase, String rol, String nombrePirata, String descripcion) {
        PirataInfo nuevoPirata = new PirataInfo(nombreClase, rol, nombrePirata, descripcion);
        mapaTripulacion.put(nombreClase, nuevoPirata);
        bitacora.info("🏴‍☠️ Nuevo pirata agregado: " + nuevoPirata);
    }
    
    /**
     * 🚪 FINALIZAR SESIÓN MANUALMENTE
     */
    public void finalizarSesionManualmente() {
        this.sesionActiva = false;
        finalizarSesion();
    }

 // 🔧 CORREGIR EL MÉTODO EN TRIPULACIONMANAGER
    public Map<String, PirataInfo> getMapaTripulacion() {
        return this.mapaTripulacion;
    }
    
    /**
     * 🔄 MÉTODOS DE INTEGRACIÓN PARA TRIPULACIONMANAGER
     */
    public class IntegracionMemoriaTripulacion {
        
        /**
         * 🧠 ACTUALIZAR TRIPULACIONMANAGER CON MEMORIA
         */
        public static void actualizarTripulacionConMemoria(TripulacionManager tripulacionManager) {
            // 🎯 INYECTAR MEMORIA EN CADA INTERACCIÓN
            SistemaMemoriaPirata memoria = SistemaMemoriaPirata.obtenerInstancia();
            
            // 🔄 MODIFICAR EL MÉTODO DE RESPUESTA PIRATA PARA USAR MEMORIA
            // (Esto requeriría refactorizar simularRespuestaPirata)
        }
        
        /**
         * 🖨️ SISTEMA DE VISUALIZACIÓN PARA EL CAPITÁN
         */
        public static class VisualizadorCapitan {
            
            public static void mostrarEstadoCompletoTripulacion() {
                SistemaMemoriaPirata memoria = SistemaMemoriaPirata.obtenerInstancia();
                
                System.out.println("\n" + "👑".repeat(80));
                System.out.println("           INFORME DEL CAPITÁN - ESTADO COMPLETO DE LA TRIPULACIÓN");
                System.out.println("👑".repeat(80));
                
                // 🎯 ESTADO DE MEMORIAS INDIVIDUALES
                System.out.println("\n🧠 ESTADO DE MEMORIAS INDIVIDUALES:");
                memoria.obtenerTodosLosPiratas().forEach(pirata -> {
                    MemoriaPirata mem = memoria.obtenerMemoriaPirata(pirata).orElse(null);
                    if (mem != null) {
                        System.out.printf("  • %-20s: %d recuerdos, %d relaciones, %d habilidades%n",
                            pirata, mem.getRecuerdos().size(), mem.getRelaciones().size(), mem.getHabilidades().size());
                        
                        // 🎯 MEJOR AMIGO
                        mem.obtenerMejorAmigo().ifPresent(amigo -> 
                            System.out.printf("      🤝 Mejor amigo: %s%n", amigo));
                    }
                });
                
                // 🔗 RED DE RELACIONES
                System.out.println("\n🔗 RED DE RELACIONES DE LA TRIPULACIÓN:");
                memoria.obtenerRelacionesDestacadas().forEach((relacion, fuerza) -> {
                    System.out.printf("  • %s ↔ %s (fuerza: %d)%n", 
                        relacion.getPirata1(), relacion.getPirata2(), fuerza);
                });
                
                // 💾 FRAGMENTOS DE CÓDIGO COMPARTIDOS
                System.out.println("\n💾 FRAGMENTOS DE CÓDIGO COMPARTIDOS:");
                memoria.obtenerFragmentosDestacados().forEach(fragmento -> {
                    System.out.printf("  • %s: %s%n", 
                        fragmento.getPirataOrigen(), fragmento.getProposito());
                });
            }
            
            public static void mostrarDebateEnTiempoReal(String pregunta, List<String> intervenciones) {
                System.out.println("\n" + "🎙️".repeat(80));
                System.out.println("           DEBATE EN TIEMPO REAL - CAPITÁN OBSERVANDO");
                System.out.println("🎙️".repeat(80));
                System.out.println("❓ PREGUNTA: " + pregunta);
                System.out.println("📊 INTERVENCIONES: " + intervenciones.size());
                
                intervenciones.forEach(intervencion -> {
                    System.out.println("\n" + "💬".repeat(40));
                    System.out.println(intervencion);
                    System.out.println("💬".repeat(40));
                });
            }
        }


		private SistemaMemoriaPirata memoria;


        // 🆕 MODIFICAR EL MÉTODO DE RESPUESTA PARA USAR MEMORIA
        private String simularRespuestaPirata(String pregunta, PirataInfo pirata) {
            try {
                // 🎯 OBTENER CONTEXTO DE MEMORIA DEL PIRATA
                String contextoMemoria = memoria.obtenerMemoriaPirata(pirata.getNombrePirata())
                        .map(m -> m.obtenerContextoPersonalizado(pregunta))
                        .orElse("");
                
                // 🎯 MEJORAR EL PROMPT CON MEMORIA
                String promptMejorado = construirPromptConMemoria(pregunta, pirata, contextoMemoria);
                
                return oraculo.invocar(promptMejorado, "respuesta_pirata_con_memoria", 0.7);
                
            } catch (Exception e) {
                // Fallback al método original
                return simularRespuestaPirataOriginal(pregunta, pirata);
            }
    }


        private String simularRespuestaPirataOriginal(String pregunta, PirataInfo pirata) {
            // Reutilizar la lógica del método principal de TripulacionManager
            try {
                String codigoFuente = obtenerCodigoFuente(pirata.getNombreClase());
                
                String promptPirata = 
                    "Eres la clase " + pirata.getNombreClase() + 
                    " con el rol de " + pirata.getRolPirata() + 
                    " y nombre pirata '" + pirata.getNombrePirata() + "'.\n\n" +
                    "Tu descripción: " + pirata.getDescripcionRol() + "\n\n" +
                    "Tu código fuente es:\n```java\n" + codigoFuente + "\n```\n\n" +
                    "Pregunta: " + pregunta + "\n\n" +
                    "Responde en formato pirata pero técnicamente útil.";
                
                OraculoDeLaLibertad oraculo = new OraculoDeLaLibertad();
                return oraculo.invocar(promptPirata, "respuesta_pirata_fallback", 0.7);
                
            } catch (Exception e) {
                return "¡Arrr! Mis circuitos están nublados. Como " + pirata.getNombrePirata() + 
                       ", no puedo responder ahora. Revisa mis cañones (logs) para más detalles.";
            }
        }


		private String construirPromptConMemoria(String pregunta, PirataInfo pirata, String contextoMemoria) {
		    return String.format(
		        "Pirata: %s (%s)\nContexto: %s\nPregunta: %s\n\nResponde como pirata:",
		        pirata.getNombrePirata(), 
		        pirata.getRolPirata(),
		        contextoMemoria,
		        pregunta
		    );
		}
		
		private String obtenerCodigoFuentePirata(String nombrePirata, String nombreClase) {
		    try {
		        // 🎯 ESTRATEGIA 1: USAR SCANNER EXISTENTE
		        if (scanner != null) {
		            // Forzar escaneo si es necesario
		            if (scanner.getClasses().isEmpty()) {
		                bitacora.info("🔍 Ejecutando escaneo rápido para: " + nombreClase);
		                scanner.scanProjectTurbo("."); // Escanear directorio actual
		            }
		            
		            // Buscar en el classMap del scanner
		            Optional<ClassInfo> classInfo = scanner.getClassByName(nombreClase);
		            if (classInfo.isPresent()) {
		                String sourcePath = classInfo.get().getSourcePath();
		                if (sourcePath != null) {
		                    File archivo = new File(sourcePath);
		                    if (archivo.exists()) {
		                        String codigo = Files.readString(archivo.toPath(), StandardCharsets.UTF_8);
		                        if (!codigo.trim().isEmpty()) {
		                            bitacora.info("✅ Código encontrado via scanner: " + nombreClase);
		                            return codigo;
		                        }
		                    }
		                }
		            }
		        }
		        
		        // 🎯 ESTRATEGIA 2: BÚSQUEDA DIRECTA INTELIGENTE
		        String codigoDirecto = buscarCodigoFuenteDirecto(nombreClase);
		        if (codigoDirecto != null && !codigoDirecto.contains("No se pudo encontrar")) {
		            return codigoDirecto;
		        }
		        
		        // 🎯 ESTRATEGIA 3: GENERAR CÓDIGO BASADO EN ROL
		        return (nombrePirata + nombreClase);
		        
		    } catch (Exception e) {
		        bitacora.error("💥 Error obteniendo código para " + nombrePirata + ": " + e.getMessage());
		        return (nombrePirata + nombreClase);
		    }
		}

		/**
		 * 🎯 BÚSQUEDA DIRECTA MEJORADA
		 */
		private String buscarCodigoFuenteDirecto(String nombreClase) {
		    // 🎯 CONVERTIR NOMBRE DE CLASE A RUTAS POSIBLES
		    String rutaClase = nombreClase.replace('.', '/') + ".java";
		    
		    // 🎯 LISTA COMPLETA DE UBICACIONES POSIBLES
		    String[] ubicaciones = {
		        "src/main/java/" + rutaClase,
		        "src/test/java/" + rutaClase, 
		        "main/java/" + rutaClase,
		        "test/java/" + rutaClase,
		        "src/" + rutaClase,
		        "./" + rutaClase,
		        "../" + rutaClase,
		        nombreClase.replace('.', '/') + ".java" // Ruta absoluta desde raíz
		    };
		    
		    for (String ubicacion : ubicaciones) {
		        try {
		            File archivo = new File(ubicacion);
		            if (archivo.exists() && archivo.isFile()) {
		                String contenido = Files.readString(archivo.toPath(), StandardCharsets.UTF_8);
		                if (!contenido.trim().isEmpty()) {
		                    bitacora.info("✅ Código encontrado en: " + ubicacion);
		                    return contenido;
		                }
		            }
		        } catch (Exception e) {
		            // Continuar con siguiente ubicación
		        }
		    }
		    
		    return null;
		}
    }   
}
