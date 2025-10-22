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
 * 🏴‍☠️ CREW MANAGER - The Captain that coordinates the pirate crew
 * 🎯 Manages questions and answers between project classes
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
    
    // 🎪 PREDEFINED CREW MAP
    public static final Map<String, String[]> ROLES_PIRATAS = Map.of(
        "ProjectScanner", new String[]{"Lookout", "Hawk Eye", "Scans the code seas for hidden treasures"},
        "AutogenTurboFusion", new String[]{"Captain", "Blackbeard Turbo", "Commands the ship with relentless power and speed"},
        "OraculoDeLaLibertad", new String[]{"Oracle", "The Oracle", "Sees the future of code in the stars and ancient runes"},
        "DebugManager", new String[]{"Surgeon", "Sharp Saber", "Heals code wounds with deadly precision"},
        "APIManager", new String[]{"Navigator", "Sea Sorcerer", "Masters the winds and currents of distant APIs"},
        "ReporteManager", new String[]{"Cartographer", "Paper Hand", "Draws detailed maps of conquered lands"},
        "SistemaManager", new String[]{"First Mate", "Old Thunder", "Keeps the ship in order and iron discipline"},
        "AnalisisManager", new String[]{"Strategist", "Brilliant Mind", "Plans battles with ancestral wisdom"},
        "PlanificadorManager", new String[]{"Helmsman", "True Course", "Steers the ship toward glorious destinations"}
    );
    
    public TripulacionManager(ProjectScanner scanner, OraculoDeLaLibertad oraculo, Bitacora bitacora) {
        this.scanner = scanner;
        this.oraculo = (oraculo != null) ? oraculo : new OraculoDeLaLibertad(); // ✅ Fallback
        this.bitacora = (bitacora != null) ? bitacora : new Bitacora(); // ✅ Fallback
        this.mapaTripulacion = new ConcurrentHashMap<>();
        this.sesionActiva = false;
        this.inputScanner = new Scanner(System.in);
        
        // ✅ INITIALIZE MEMORY
        this.memoria = SistemaMemoriaPirata.obtenerInstancia();
        
        inicializarTripulacion();
    }
    
    /**
     * 🏴‍☠️ START SESSION WITH THE CREW
     */
    public void iniciarSesionTripulacion() {
        mostrarBannerInicio();
        sesionActiva = true;
        
        bitacora.exito("🏴‍☠️ CREW SESSION STARTED");
        
        while (sesionActiva) {
            ejecutarTurnoPregunta();
        }
        
        finalizarSesion();
    }
    
    /**
     * 🎯 EXECUTE QUESTION TURN
     */
    private void ejecutarTurnoPregunta() {
        try {
            // 1. REQUEST QUESTION FROM USER
            String pregunta = solicitarPreguntaUsuario();
            if (pregunta == null || pregunta.trim().isEmpty()) {
                return;
            }
            
            if (esComandoSalir(pregunta)) {
                sesionActiva = false;
                return;
            }
            
            bitacora.info("🎯 QUESTION RECEIVED: " + pregunta);
            
            // 2. GET AVAILABLE CLASSES
            List<ClassInfo> clasesDisponibles = obtenerClasesDisponibles();
            if (clasesDisponibles.isEmpty()) {
                System.out.println("❌ No crew available. Run scanProject first.");
                return;
            }
            
            // 3. SELECT PIRATE TO ANSWER
            String claseElegida = seleccionarPirataParaPregunta(pregunta, clasesDisponibles);
            if (claseElegida == null) {
                System.out.println("❌ The crew couldn't decide who should answer.");
                return;
            }
            
            // 4. GET PIRATE INFORMATION
            PirataInfo pirata = mapaTripulacion.get(claseElegida);
            if (pirata == null) {
                pirata = crearPirataGenerico(claseElegida);
            }
            
            // 5. SIMULATE PIRATE RESPONSE
            String respuesta = simularRespuestaPirata(pregunta, pirata);
            
            // 6. SHOW RESPONSE
            mostrarRespuestaPirata(pirata, respuesta);
            
            // 7. REGISTER IN LOG
            registrarIntercambioBitacora(pregunta, pirata, respuesta);
            
        } catch (Exception e) {
            System.err.println("💥 Error in question turn: " + e.getMessage());
            bitacora.error("Error in TripulacionManager: " + e.getMessage());
        }
    }
    
    /**
     * 🗣️ REQUEST QUESTION FROM USER
     */
    private String solicitarPreguntaUsuario() {
        System.out.println("\n" + "⚓".repeat(60));
        System.out.println("🏴‍☠️  CONSULT THE CREW");
        System.out.println("⚓".repeat(60));
        System.out.println("Write your question for the crew (or 'exit' to finish):");
        System.out.print("🎯 > ");
        
        String pregunta = inputScanner.nextLine().trim();
        
        if (pregunta.isEmpty()) {
            System.out.println("⚠️  Question cannot be empty.");
            return null;
        }
        
        return pregunta;
    }
    
    private List<ClassInfo> obtenerClasesDisponibles() {
        try {
            List<ClassInfo> clases = new ArrayList<>();
            
            // ✅ VERIFY IF SCANNER HAS CLASSES
            if (scanner != null) {
                // Try to access classes through safe reflection
                try {
                    // Look for method that returns classes
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
                    bitacora.debug("Reflection failed: " + e.getMessage());
                }
            }
            
            // ✅ FALLBACK: USE HARDCODED CLASSES FROM ROLES_PIRATAS
            if (clases.isEmpty()) {
                bitacora.info("Using hardcoded classes from ROLES_PIRATAS");
                for (String className : ROLES_PIRATAS.keySet()) {
                    ClassInfo classInfo = new ClassInfo();
                    classInfo.setName(className);
                    classInfo.setPackageName("com.elreinodelolvido.ellibertad");
                    classInfo.setFullName("com.elreinodelolvido.ellibertad." + className);
                    clases.add(classInfo);
                }
            }
            
            bitacora.info("Available classes: " + clases.size());
            return clases;
            
        } catch (Exception e) {
            bitacora.error("Critical error getting classes: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    /**
     * 🎯 SELECT PIRATE FOR QUESTION
     */
    private String seleccionarPirataParaPregunta(String pregunta, List<ClassInfo> clases) {
        try {
            // Build list of class names
            StringBuilder listaClases = new StringBuilder();
            for (ClassInfo clase : clases) {
                listaClases.append("- ").append(clase.getFullName()).append("\n");
            }
            
            // Prompt for class selection
            String promptSeleccion = 
                "You are the captain of a pirate ship. You have this crew (Java classes):\n\n" +
                listaClases.toString() + "\n" +
                "Your crew member's question is: " + pregunta + "\n\n" +
                "Which crew member (class) is best qualified to answer?\n" +
                "Consider:\n" +
                "1. Each class's specialty\n" + 
                "2. The nature of the question\n" +
                "3. Each class's technical capabilities\n\n" +
                "Return ONLY the full class name chosen, without additional explanations.";
            
            // Use oracle to select
            String respuesta = oraculo.invocar(promptSeleccion, "crew_selection", 0.3);
            
            if (respuesta == null || respuesta.trim().isEmpty()) {
                return seleccionarPirataAleatorio(clases);
            }
            
            // Extract class name from response
            String claseElegida = extraerNombreClaseDeRespuesta(respuesta, clases);
            
            if (claseElegida != null) {
                bitacora.info("🎯 PIRATE SELECTED: " + claseElegida);
                return claseElegida;
            }
            
            // Fallback: random selection
            return seleccionarPirataAleatorio(clases);
            
        } catch (Exception e) {
            bitacora.error("Error selecting pirate: " + e.getMessage());
            return seleccionarPirataAleatorio(clases);
        }
    }
    
    String simularRespuestaPirata(String pregunta, PirataInfo pirata) {
        try {
            // ✅ GET COMPLETE CONTEXT
            String codigoFuente = obtenerCodigoFuente(pirata.getNombreClase());
            String contextoMemoria = obtenerContextoMemoriaPirata(pirata.getNombrePirata(), pregunta);
            
            // ✅ BUILD IMPROVED PROMPT
            String prompt = construirPromptCompleto(pregunta, pirata, codigoFuente, contextoMemoria);
            
            return oraculo.invocar(prompt, "improved_pirate_response", 0.7);
            
        } catch (Exception e) {
            bitacora.error("Error in pirate response: " + e.getMessage());
            return generarRespuestaFallback(pirata, pregunta);
        }
    }

    private String construirPromptCompleto(String pregunta, PirataInfo pirata, String codigoFuente, String contextoMemoria) {
        StringBuilder prompt = new StringBuilder();
        
        // 🎯 SECTION 1: COMPLETE PIRATE IDENTITY
        prompt.append("🏴‍☠️ PIRATE CONSULTATION - COMPLETE IDENTITY 🏴‍☠️\n\n");
        prompt.append("👤 PIRATE INFORMATION:\n");
        prompt.append("═══════════════════════════════════════════\n");
        prompt.append("• Java Class: ").append(pirata.getNombreClase()).append("\n");
        prompt.append("• Crew Role: ").append(pirata.getRolPirata()).append("\n");
        prompt.append("• Pirate Name: ").append(pirata.getNombrePirata()).append("\n");
        prompt.append("• Specialty: ").append(pirata.getDescripcionRol()).append("\n");
        prompt.append("═══════════════════════════════════════════\n\n");
        
        // 🧠 SECTION 2: MEMORY AND HISTORICAL CONTEXT
        prompt.append("🧠 PIRATE MEMORY AND EXPERIENCES:\n");
        prompt.append("═══════════════════════════════════════════\n");
        if (contextoMemoria != null && !contextoMemoria.trim().isEmpty()) {
            prompt.append(contextoMemoria).append("\n");
        } else {
            prompt.append("• This pirate has a fresh history and is ready for new adventures\n");
            prompt.append("• No specific previous memories recorded\n");
            prompt.append("• Unlimited potential to create new legends\n");
        }
        prompt.append("═══════════════════════════════════════════\n\n");
        
        // 💾 SECTION 3: COMPLETE SOURCE CODE
        prompt.append("💾 COMPLETE SOURCE CODE - TECHNICAL ARSENAL:\n");
        prompt.append("═══════════════════════════════════════════\n");
        prompt.append("```java\n");
        
        if (codigoFuente != null && !codigoFuente.trim().isEmpty()) {
            prompt.append(codigoFuente).append("\n");
        } else {
            prompt.append("// By all the oceans! The source code is temporarily unavailable.\n");
            prompt.append("// But the pirate spirit and technical knowledge remain firm.\n");
            prompt.append("// Pirate ").append(pirata.getNombrePirata()).append(" is ready for action.\n");
        }
        
        prompt.append("```\n");
        prompt.append("═══════════════════════════════════════════\n\n");
        
        // ❓ SECTION 4: MAIN QUESTION
        prompt.append("🎯 CAPTAIN'S QUESTION - CURRENT MISSION:\n");
        prompt.append("═══════════════════════════════════════════\n");
        prompt.append("« ").append(pregunta).append(" »\n");
        prompt.append("═══════════════════════════════════════════\n\n");
        
        // 📜 SECTION 5: DETAILED INSTRUCTIONS
        prompt.append("""
            📜 DETAILED RESPONSE INSTRUCTIONS:
            ═══════════════════════════════════════════
            
            RESPONSE AS TECHNICAL PIRATE - STRICT FORMAT:
            
            🌊 PIRATE PERSPECTIVE (3-5 lines)
            • Respond maintaining your pirate personality
            • Use nautical metaphors and epic language
            • Establish your unique tone and character
            • Connect your pirate role with the technical question
            
            ⚙️ DETAILED TECHNICAL ANALYSIS (5-10 lines)
            • Examine your complete source code line by line
            • Identify relevant methods, properties and functionalities
            • Explain how your current implementation relates to the question
            • Point out specific technical strengths and weaknesses
            • Use concrete examples from your code
            
            🚀 SPECIFIC TECHNICAL IMPROVEMENTS (5-8 lines)
            • Propose improvements based on your code analysis
            • Include improved code examples if appropriate
            • Be technically precise and realistic
            • Consider design patterns and best practices
            
            🔥 PIRATE-THEMED REFACTORS (4-6 lines)
            • Suggest creative refactorings
            • Use memorable pirate names for methods/classes
            • Examples: navigateToTurboCode(), searchForPatternTreasure()
            • Maintain technical coherence while applying the theme
            
            🤝 STRATEGIC COLLABORATIONS (3-5 lines)
            • Identify other pirates/classes that could help you
            • Based on your memory and existing relationships
            • Propose specific alliances and mutual benefits
            • Explain how collaboration would solve the problem
            
            💡 IMMEDIATE ACTION PLAN (4-6 lines)
            • List concrete and actionable steps
            • Prioritize by impact and ease of implementation
            • Be specific, measurable and with implicit deadlines
            • Include success metrics if possible
            
            EVALUATION CRITERIA:
            ✓ Coherence between pirate personality and technical analysis
            ✓ Depth of source code analysis
            ✓ Quality and realism of proposed improvements
            ✓ Creativity in pirate names and metaphors
            ✓ Practical utility of recommendations
            ✓ Clarity and structure of response
            
            Your complete source code is available above - use it exhaustively!
            """);
        
        // 🏆 SECTION 6: EPIC CLOSURE
        prompt.append("\n\n🏴‍☠️ EPIC CLOSURE - CALL TO ACTION:\n");
        prompt.append("═══════════════════════════════════════════\n");
        prompt.append("MAY THE CODE SEAS OBEY YOU, BRAVE ").append(pirata.getNombrePirata()).append("!\n");
        prompt.append("THE TECHNICAL DESTINY OF THE SHIP IS IN YOUR HANDS.\n");
        prompt.append("RESPOND WITH THE WISDOM OF YOUR EXPERIENCES \n");
        prompt.append("AND THE COURAGE OF YOUR PIRATE SPIRIT. ARRR! 🏴‍☠️\n");
        prompt.append("═══════════════════════════════════════════\n");
        
        return prompt.toString();
    }

    private String obtenerContextoMemoriaPirata(String nombrePirata, String pregunta) {
        try {
            Optional<MemoriaPirata> memoriaOpt = memoria.obtenerMemoriaPirata(nombrePirata);
            if (memoriaOpt.isPresent()) {
                return memoriaOpt.get().obtenerContextoPersonalizado(pregunta);
            }
            
            // ✅ CREATE MEMORY IF IT DOESN'T EXIST
            String nombreClase = encontrarClasePorPirata(nombrePirata);
            if (nombreClase != null) {
                memoria.registrarNuevoPirata(nombrePirata, nombreClase);
                return "🧠 New memory created for " + nombrePirata;
            }
            
            return "🧠 No previous history available";
            
        } catch (Exception e) {
            return "🧠 Memory temporarily unavailable";
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
            // ✅ ROBUST SEARCH IN MULTIPLE LOCATIONS
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
                        bitacora.info("✅ Code found at: " + path);
                        return codigo;
                    }
                }
            }
            
            // ✅ FALLBACK: EXAMPLE CODE BASED ON ROLE
            return generarCodigoEjemplo(nombreClase);
            
        } catch (Exception e) {
            bitacora.error("Error getting source code: " + e.getMessage());
            return generarCodigoEjemplo(nombreClase);
        }
    }

    private String generarCodigoEjemplo(String nombreClase) {
        // ✅ GENERATE RELEVANT EXAMPLE CODE
        String[] datosPirata = ROLES_PIRATAS.get(nombreClase);
        if (datosPirata != null) {
            return """
                // 🏴‍☠️ CODE OF %s - %s
                // %s
                
                public class %s {
                    // This pirate specializes in %s
                    // Their responsibilities include specific system functionalities
                    
                    public void ejecutarTareaPrincipal() {
                        // Specific implementation of pirate role
                        System.out.println("%s in action!");
                    }
                    
                    // Additional methods according to role...
                }
                """.formatted(
                    datosPirata[1], datosPirata[0], datosPirata[2],
                    nombreClase, datosPirata[0].toLowerCase(), datosPirata[1]
                );
        }
        
        // ✅ GENERIC FALLBACK
        return """
            // 🏴‍☠️ CODE OF %s
            // Detailed information temporarily unavailable
            
            public class %s {
                // Class responsible for specific system functionalities
                // The associated pirate has specialized knowledge
                
                public void demostrarHabilidades() {
                    System.out.println("%s ready for action!");
                }
            }
            """.formatted(nombreClase, nombreClase, nombreClase);
    }
    
    /**
     * 🎯 RECURSIVELY SEARCH FILE IN ALL FOLDERS
     */
    private String buscarArchivoJava(String nombreArchivo) {
        try {
            Path directorioActual = Paths.get(".").toAbsolutePath().normalize();
            bitacora.debug("🔍 Recursively searching from: " + directorioActual);
            
            // 🎯 CONFIGURE EXCLUSIONS TO AVOID INFINITE LOOP
            Set<String> excludedDirs = Set.of(
                "target", "build", ".git", "node_modules", 
                "bin", "out", "dist", ".idea", ".vscode",
                "autogen-output" // Exclude our own output
            );
            
            Optional<Path> archivoEncontrado = Files.walk(directorioActual, Integer.MAX_VALUE)
                    .filter(path -> {
                        // 🎯 EXCLUDE UNWANTED DIRECTORIES
                        String nombreDir = path.getFileName() != null ? 
                            path.getFileName().toString() : "";
                        if (excludedDirs.contains(nombreDir)) {
                            return false;
                        }
                        
                        // 🎯 VERIFY IF IT'S THE FILE WE'RE LOOKING FOR
                        return path.toString().endsWith(nombreArchivo) && 
                               Files.isRegularFile(path);
                    })
                    .findFirst();
            
            if (archivoEncontrado.isPresent()) {
                String ruta = archivoEncontrado.get().toString();
                bitacora.debug("✅ File found recursively: " + ruta);
                this.rutaArchivo = ruta; // 🎯 UPDATE FOR FUTURE QUERIES
                return ruta;
            } else {
                bitacora.debug("❌ File not found recursively: " + nombreArchivo);
            }
            
        } catch (IOException e) {
            bitacora.error("💥 Error in recursive search: " + e.getMessage());
        }
        
        return "";
    }

    /**
     * 🎯 SEARCH FILE IN COMMON PROJECT STRUCTURES - IMPROVED
     */
    private String buscarArchivoEnEstructuras(String nombrePirata) {
        try {
            bitacora.debug("🔍 Structure search for: " + nombrePirata);
            
            // 🎯 CONVERT PIRATE NAME TO POSSIBLE FILE NAME
            String nombreArchivo = convertirNombrePirataAArchivo(nombrePirata);
            
            // 🎯 MORE COMPLETE PROJECT STRUCTURES
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
                "" // 🎯 SEARCH FROM ROOT TOO
            };
            
            // 🎯 SEARCH IN EACH STRUCTURE
            for (String estructura : estructuras) {
                String rutaCompleta = estructura + nombreArchivo;
                File archivo = new File(rutaCompleta);
                
                if (archivo.exists() && archivo.isFile()) {
                    bitacora.debug("✅ Found in structure: " + rutaCompleta);
                    this.rutaArchivo = rutaCompleta;
                    return rutaCompleta;
                }
                
                // 🎯 SEARCH RECURSIVELY IN EACH STRUCTURE
                if (!estructura.isEmpty()) {
                    File directorioEstructura = new File(estructura);
                    if (directorioEstructura.exists() && directorioEstructura.isDirectory()) {
                        String rutaRecursiva = buscarEnDirectorioRecursivo(directorioEstructura, nombreArchivo);
                        if (!rutaRecursiva.isEmpty()) {
                            bitacora.debug("✅ Found recursively in structure: " + rutaRecursiva);
                            this.rutaArchivo = rutaRecursiva;
                            return rutaRecursiva;
                        }
                    }
                }
            }
            
            // 🎯 RECURSIVE SEARCH FROM CURRENT DIRECTORY
            String rutaRecursiva = buscarArchivoJava(nombreArchivo);
            if (!rutaRecursiva.isEmpty()) {
                return rutaRecursiva;
            }
            
            bitacora.debug("❌ Not found in structures: " + nombreArchivo);
            
        } catch (Exception e) {
            bitacora.error("💥 Error in structure search: " + e.getMessage());
        }
        
        return "";
    }

    /**
     * 🎯 SEARCH IN SPECIFIC DIRECTORY RECURSIVELY
     */
    private String buscarEnDirectorioRecursivo(File directorio, String nombreArchivo) {
        try {
            if (!directorio.exists() || !directorio.isDirectory()) {
                return "";
            }
            
            // 🎯 EXCLUSIONS TO AVOID INFINITE LOOP
            Set<String> excludedDirs = Set.of(
                "target", "build", ".git", "node_modules", 
                "bin", "out", "dist", ".idea", ".vscode",
                "autogen-output"
            );
            
            // 🎯 SEARCH RECURSIVELY
            return Files.walk(directorio.toPath(), Integer.MAX_VALUE)
                    .filter(path -> {
                        String nombreDir = path.getFileName() != null ? 
                            path.getFileName().toString() : "";
                        
                        // 🎯 EXCLUDE UNWANTED DIRECTORIES
                        if (excludedDirs.contains(nombreDir)) {
                            return false;
                        }
                        
                        // 🎯 VERIFY IF IT'S THE FILE WE'RE LOOKING FOR
                        return path.toString().endsWith(nombreArchivo) && 
                               Files.isRegularFile(path);
                    })
                    .findFirst()
                    .map(Path::toString)
                    .orElse("");
            
        } catch (IOException e) {
            bitacora.debug("⚠️ Error searching in directory " + directorio + ": " + e.getMessage());
            return "";
        }
    }

    /**
     * 🎯 CONVERT PIRATE NAME TO FILE NAME - IMPROVED
     */
    private String convertirNombrePirataAArchivo(String nombrePirata) {
        if (nombrePirata == null || nombrePirata.trim().isEmpty()) {
            return "UnknownPirate.java";
        }
        
        // 🎯 CLEAN AND FORMAT NAME
        String nombreLimpio = nombrePirata
            .replaceAll("[^a-zA-Z0-9\\s]", "") // Keep only letters, numbers and spaces
            .replaceAll("\\s+", " ") // Unify multiple spaces
            .trim();
        
        if (nombreLimpio.isEmpty()) {
            return "UnknownPirate.java";
        }
        
        // 🎯 SPLIT BY SPACES AND CAPITALIZE
        String[] palabras = nombreLimpio.split("\\s+");
        StringBuilder nombreArchivo = new StringBuilder();
        
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                // 🎯 CAPITALIZE FIRST LETTER, LOWERCASE THE REST
                String palabraFormateada = palabra.substring(0, 1).toUpperCase() + 
                                         palabra.substring(1).toLowerCase();
                nombreArchivo.append(palabraFormateada);
            }
        }
        
        // 🎯 POSSIBLE VARIATIONS
        String[] variaciones = {
            nombreArchivo.toString() + ".java",
            nombreArchivo.toString() + "Pirate.java",
            nombreArchivo.toString() + "Class.java",
            // 🎯 FOR NAMES WITH ROLES LIKE "True Course (Helmsman)"
            nombreLimpio.replaceAll("[^a-zA-Z0-9]", "").replaceAll("\\s+", "") + ".java"
        };
        
        // 🎯 VERIFY IF ANY VARIATION EXISTS
        for (String variacion : variaciones) {
            // Check if it exists in some common structure
            if (existeArchivoEnAlgunaEstructura(variacion)) {
                return variacion;
            }
        }
        
        // 🎯 FALLBACK: USE FIRST VARIATION
        return variaciones[0];
    }

    /**
     * 🎯 VERIFY IF A FILE EXISTS IN ANY COMMON STRUCTURE
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
     * 🎯 IMPROVED MAIN DIRECT SEARCH METHOD
     */
    private String buscarCodigoFuenteDirecto(String nombrePirata) {
        try {
            bitacora.info("🔍 Direct turbo search for: " + nombrePirata);
            
            // 🎯 SEARCH IN MEMORY FIRST
            Optional<SistemaMemoriaPirata.MemoriaPirata> memoriaPirata = memoria.obtenerMemoriaPirata(nombrePirata);
            if (memoriaPirata.isPresent()) {
                String rutaArchivo = memoriaPirata.get().getRutaArchivo();
                if (rutaArchivo != null && !rutaArchivo.isEmpty() && FileUtils.verificarArchivo(rutaArchivo)) {
                    bitacora.info("✅ Found in memory path: " + rutaArchivo);
                    return FileUtils.readFile(rutaArchivo);
                }
            }
            
            // 🎯 IMPROVED SEARCH IN COMMON STRUCTURES
            String rutaEncontrada = buscarArchivoEnEstructuras(nombrePirata);
            if (!rutaEncontrada.isEmpty()) {
                bitacora.info("✅ Found in structured search: " + rutaEncontrada);
                return FileUtils.readFile(rutaEncontrada);
            }
            
            // 🎯 SEARCH BY CLASS NAME (if available)
            if (memoriaPirata.isPresent()) {
                String nombreClase = memoriaPirata.get().getNombreClase();
                if (nombreClase != null && !nombreClase.isEmpty()) {
                    String rutaPorClase = buscarPorNombreClase(nombreClase);
                    if (!rutaPorClase.isEmpty()) {
                        bitacora.info("✅ Found by class name: " + rutaPorClase);
                        return FileUtils.readFile(rutaPorClase);
                    }
                }
            }
            
            bitacora.warn("❌ No source code found for: " + nombrePirata);
            return null;
            
        } catch (Exception e) {
            bitacora.error("💥 Error in direct turbo search: " + e.getMessage());
            return null;
        }
    }

    /**
     * 🎯 SEARCH BY FULL CLASS NAME
     */
    private String buscarPorNombreClase(String nombreClase) {
        try {
            if (nombreClase == null || nombreClase.isEmpty()) {
                return "";
            }
            
            // 🎯 CONVERT CLASS NAME TO FILE PATH
            String rutaClase = nombreClase.replace('.', '/') + ".java";
            
            // 🎯 SEARCH IN COMMON STRUCTURES
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
            
            // 🎯 SEARCH RECURSIVELY
            return buscarArchivoJava(rutaClase);
            
        } catch (Exception e) {
            bitacora.debug("⚠️ Error searching by class " + nombreClase + ": " + e.getMessage());
            return "";
        }
    }
    
    /**
     * 🎭 SHOW PIRATE RESPONSE
     */
    private void mostrarRespuestaPirata(PirataInfo pirata, String respuesta) {
        System.out.println("\n" + "🌊".repeat(80));
        System.out.println("🏴‍☠️  RESPONSE FROM " + pirata.getNombrePirata().toUpperCase());
        System.out.println("📜 Role: " + pirata.getRolPirata() + " | Class: " + pirata.getNombreClase());
        System.out.println("🌊".repeat(80));
        
        if (respuesta != null) {
            System.out.println(respuesta);
        } else {
            System.out.println("❌ The pirate couldn't respond at this time.");
        }
        
        System.out.println("\n" + "⚓".repeat(80));
    }
    
    // =========================================================================
    // 🛠️ AUXILIARY METHODS
    // =========================================================================
    
    /**
     * 🎪 INITIALIZE CREW
     */
    private void inicializarTripulacion() {
        for (Map.Entry<String, String[]> entry : ROLES_PIRATAS.entrySet()) {
            String[] datosPirata = entry.getValue();
            mapaTripulacion.put(entry.getKey(), 
                new PirataInfo(entry.getKey(), datosPirata[0], datosPirata[1], datosPirata[2]));
        }
        bitacora.info("🏴‍☠️ Crew initialized with " + mapaTripulacion.size() + " pirates");
    }
    
    /**
     * 🎲 SELECT RANDOM PIRATE
     */
    private String seleccionarPirataAleatorio(List<ClassInfo> clases) {
        if (clases.isEmpty()) return null;
        Random random = new Random();
        ClassInfo claseAleatoria = clases.get(random.nextInt(clases.size()));
        return claseAleatoria.getFullName();
    }
    
    /**
     * 🔍 EXTRACT CLASS NAME FROM RESPONSE
     */
    private String extraerNombreClaseDeRespuesta(String respuesta, List<ClassInfo> clases) {
        String respuestaLimpia = respuesta.trim();
        
        // Look for exact match
        for (ClassInfo clase : clases) {
            if (respuestaLimpia.contains(clase.getFullName())) {
                return clase.getFullName();
            }
        }
        
        // Look by simple name
        for (ClassInfo clase : clases) {
            if (respuestaLimpia.contains(clase.getName())) {
                return clase.getFullName();
            }
        }
        
        return null;
    }
    
    /**
     * 🆘 GENERATE FALLBACK RESPONSE
     */
    private String generarRespuestaFallback(PirataInfo pirata, String pregunta) {
        return "🎯 Arrr, " + pirata.getNombrePirata() + " speaking! \n" +
               "As " + pirata.getRolPirata().toLowerCase() + " of this ship, I hear your question: '" + pregunta + "'\n\n" +
               "🔧 My technical analysis is temporarily cloudy...\n\n" +
               "⚡ I suggest checking my cannons (methods) and sails (properties)\n\n" +
               "🏴‍☠️ I need a good wind (API connection) to give you better answers!";
    }
    
    /**
     * 📝 REGISTER EXCHANGE IN LOG
     */
    private void registrarIntercambioBitacora(String pregunta, PirataInfo pirata, String respuesta) {
        try {
            String id = UUID.randomUUID().toString().substring(0, 8);
            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            String md = """

                ---
                ## 🏴‍☠️ Crew Consultation (%s)
                **ID**: `%s`  
                **Date**: %s  
                **Pirate**: %s (%s)
                **Class**: %s
                ---

                ### ❓ Question:
                ```
                %s
                ```

                ### 🎯 Response:
                ```
                %s
                ```

                ---
                *End of exchange*
                
                """.formatted(pirata.getRolPirata(), id, timestamp, 
                    pirata.getNombrePirata(), pirata.getRolPirata(),
                    pirata.getNombreClase());

            FileUtils.crearArchivoSiNoExiste("autogen-output/crew-log.md", 
                "# 🏴‍☠️ Crew Log\n\n*Record of all crew consultations*\n\n");
            
            FileUtils.appendToFile("autogen-output/crew-log.md", md);
            
        } catch (Exception e) {
            System.err.println("💥 Error registering in log: " + e.getMessage());
        }
    }
    
    /**
     * 🚪 VERIFY EXIT COMMAND
     */
    private boolean esComandoSalir(String input) {
        return input.equalsIgnoreCase("exit") || 
               input.equalsIgnoreCase("quit") || 
               input.equalsIgnoreCase("bye") ||
               input.equalsIgnoreCase("goodbye");
    }
   
    
    /**
     * 📦 EXTRACT PACKAGE FROM FULL NAME
     */
    private String extraerPackage(String fullClassName) {
        if (fullClassName == null) return "";
        int lastDot = fullClassName.lastIndexOf('.');
        return lastDot > 0 ? fullClassName.substring(0, lastDot) : "";
    }
    
    /**
     * 🏴‍☠️ CREATE GENERIC PIRATE FOR UNMAPPED CLASSES
     */
    private PirataInfo crearPirataGenerico(String nombreClase) {
        String[] rolesGenericos = {
            "Cabin Boy", "Sailor", "Gunner", "Cook", "Treasurer"
        };
        String[] nombresGenericos = {
            "Swift Lightning", "Saber Tooth", "Cyclone", "Wild Tide", "Gale"
        };
        
        Random random = new Random();
        String rol = rolesGenericos[random.nextInt(rolesGenericos.length)];
        String nombre = nombresGenericos[random.nextInt(nombresGenericos.length)];
        String descripcion = "Brave crew member specialized in " + nombreClase;
        
        PirataInfo pirata = new PirataInfo(nombreClase, rol, nombre, descripcion);
        mapaTripulacion.put(nombreClase, pirata);
        
        return pirata;
    }
    
    /**
     * 🎪 SHOW START BANNER
     */
    private void mostrarBannerInicio() {
        System.out.println("\n" + "🏴‍☠️".repeat(80));
        System.out.println("                  PIRATE CREW ACTIVATED!");
        System.out.println("🌊 Each class in your project is a pirate with unique abilities");
        System.out.println("🎯 Ask questions and let the crew guide you");
        System.out.println("🏴‍☠️".repeat(80));
        System.out.println("\nAvailable crew members:");
        
        mapaTripulacion.values().forEach(pirata -> {
            System.out.printf("  • %s (%s) - %s%n", 
                pirata.getNombrePirata(), pirata.getRolPirata(), pirata.getNombreClase());
        });
        
        System.out.println("\n" + "⚓".repeat(80));
    }
    
    /**
     * 📋 END SESSION
     */
    private void finalizarSesion() {
        System.out.println("\n" + "🌅".repeat(80));
        System.out.println("                  CREW SESSION ENDED");
        System.out.println("🏴‍☠️ The crew rests... until the next adventure!");
        System.out.println("📜 Log saved at: autogen-output/crew-log.md");
        System.out.println("🌅".repeat(80));
        
        bitacora.exito("🏴‍☠️ CREW SESSION ENDED");
        inputScanner.close();
    }
    
    // =========================================================================
    // 🎪 INTERNAL PIRATE INFO CLASS
    // =========================================================================
    
    /**
     * 🏴‍☠️ PIRATE/CLASS INFORMATION
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
    // 🔧 ADDITIONAL PUBLIC METHODS
    // =========================================================================
    
    /**
     * 📊 SHOW CREW STATUS
     */
    public void mostrarEstadoTripulacion() {
        System.out.println("\n🏴‍☠️ CREW STATUS:");
        System.out.println("Active session: " + (sesionActiva ? "✅" : "❌"));
        System.out.println("Registered pirates: " + mapaTripulacion.size());
        
        mapaTripulacion.values().forEach(pirata -> {
            System.out.printf("  🏴‍☠️ %-15s → %-12s (%s)%n",
                pirata.getNombrePirata(), pirata.getRolPirata(), pirata.getNombreClase());
        });
    }
    
    /**
     * 🔄 ADD CUSTOM PIRATE
     */
    public void agregarPirata(String nombreClase, String rol, String nombrePirata, String descripcion) {
        PirataInfo nuevoPirata = new PirataInfo(nombreClase, rol, nombrePirata, descripcion);
        mapaTripulacion.put(nombreClase, nuevoPirata);
        bitacora.info("🏴‍☠️ New pirate added: " + nuevoPirata);
    }
    
    /**
     * 🚪 MANUALLY END SESSION
     */
    public void finalizarSesionManualmente() {
        this.sesionActiva = false;
        finalizarSesion();
    }

    // 🔧 FIX THE METHOD IN TRIPULACIONMANAGER
    public Map<String, PirataInfo> getMapaTripulacion() {
        return this.mapaTripulacion;
    }
    
    /**
     * 🔄 INTEGRATION METHODS FOR TRIPULACIONMANAGER
     */
    public class IntegracionMemoriaTripulacion {
        
        /**
         * 🧠 UPDATE TRIPULACIONMANAGER WITH MEMORY
         */
        public static void actualizarTripulacionConMemoria(TripulacionManager tripulacionManager) {
            // 🎯 INJECT MEMORY IN EACH INTERACTION
            SistemaMemoriaPirata memoria = SistemaMemoriaPirata.obtenerInstancia();
            
            // 🔄 MODIFY THE PIRATE RESPONSE METHOD TO USE MEMORY
            // (This would require refactoring simularRespuestaPirata)
        }
        
        /**
         * 🖨️ VISUALIZATION SYSTEM FOR THE CAPTAIN
         */
        public static class VisualizadorCapitan {
            
            public static void mostrarEstadoCompletoTripulacion() {
                SistemaMemoriaPirata memoria = SistemaMemoriaPirata.obtenerInstancia();
                
                System.out.println("\n" + "👑".repeat(80));
                System.out.println("           CAPTAIN'S REPORT - COMPLETE CREW STATUS");
                System.out.println("👑".repeat(80));
                
                // 🎯 INDIVIDUAL MEMORY STATUS
                System.out.println("\n🧠 INDIVIDUAL MEMORY STATUS:");
                memoria.obtenerTodosLosPiratas().forEach(pirata -> {
                    MemoriaPirata mem = memoria.obtenerMemoriaPirata(pirata).orElse(null);
                    if (mem != null) {
                        System.out.printf("  • %-20s: %d memories, %d relationships, %d abilities%n",
                            pirata, mem.getRecuerdos().size(), mem.getRelaciones().size(), mem.getHabilidades().size());
                        
                        // 🎯 BEST FRIEND
                        mem.obtenerMejorAmigo().ifPresent(amigo -> 
                            System.out.printf("      🤝 Best friend: %s%n", amigo));
                    }
                });
                
                // 🔗 CREW RELATIONSHIP NETWORK
                System.out.println("\n🔗 CREW RELATIONSHIP NETWORK:");
                memoria.obtenerRelacionesDestacadas().forEach((relacion, fuerza) -> {
                    System.out.printf("  • %s ↔ %s (strength: %d)%n", 
                        relacion.getPirata1(), relacion.getPirata2(), fuerza);
                });
                
                // 💾 SHARED CODE FRAGMENTS
                System.out.println("\n💾 SHARED CODE FRAGMENTS:");
                memoria.obtenerFragmentosDestacados().forEach(fragmento -> {
                    System.out.printf("  • %s: %s%n", 
                        fragmento.getPirataOrigen(), fragmento.getProposito());
                });
            }
            
            public static void mostrarDebateEnTiempoReal(String pregunta, List<String> intervenciones) {
                System.out.println("\n" + "🎙️".repeat(80));
                System.out.println("           REAL-TIME DEBATE - CAPTAIN OBSERVING");
                System.out.println("🎙️".repeat(80));
                System.out.println("❓ QUESTION: " + pregunta);
                System.out.println("📊 INTERVENTIONS: " + intervenciones.size());
                
                intervenciones.forEach(intervencion -> {
                    System.out.println("\n" + "💬".repeat(40));
                    System.out.println(intervencion);
                    System.out.println("💬".repeat(40));
                });
            }
        }


        private SistemaMemoriaPirata memoria;


        // 🆕 MODIFY RESPONSE METHOD TO USE MEMORY
        private String simularRespuestaPirata(String pregunta, PirataInfo pirata) {
            try {
                // 🎯 GET PIRATE MEMORY CONTEXT
                String contextoMemoria = memoria.obtenerMemoriaPirata(pirata.getNombrePirata())
                        .map(m -> m.obtenerContextoPersonalizado(pregunta))
                        .orElse("");
                
                // 🎯 IMPROVE PROMPT WITH MEMORY
                String promptMejorado = construirPromptConMemoria(pregunta, pirata, contextoMemoria);
                
                return oraculo.invocar(promptMejorado, "pirate_response_with_memory", 0.7);
                
            } catch (Exception e) {
                // Fallback to original method
                return simularRespuestaPirataOriginal(pregunta, pirata);
            }
    }


        private String simularRespuestaPirataOriginal(String pregunta, PirataInfo pirata) {
            // Reuse logic from TripulacionManager's main method
            try {
                String codigoFuente = obtenerCodigoFuente(pirata.getNombreClase());
                
                String promptPirata = 
                    "You are class " + pirata.getNombreClase() + 
                    " with the role of " + pirata.getRolPirata() + 
                    " and pirate name '" + pirata.getNombrePirata() + "'.\n\n" +
                    "Your description: " + pirata.getDescripcionRol() + "\n\n" +
                    "Your source code is:\n```java\n" + codigoFuente + "\n```\n\n" +
                    "Question: " + pregunta + "\n\n" +
                    "Respond in pirate format but technically useful.";
                
                OraculoDeLaLibertad oraculo = new OraculoDeLaLibertad();
                return oraculo.invocar(promptPirata, "pirate_response_fallback", 0.7);
                
            } catch (Exception e) {
                return "Arrr! My circuits are cloudy. As " + pirata.getNombrePirata() + 
                       ", I can't respond now. Check my cannons (logs) for more details.";
            }
        }


        private String construirPromptConMemoria(String pregunta, PirataInfo pirata, String contextoMemoria) {
            return String.format(
                "Pirate: %s (%s)\nContext: %s\nQuestion: %s\n\nRespond as pirate:",
                pirata.getNombrePirata(), 
                pirata.getRolPirata(),
                contextoMemoria,
                pregunta
            );
        }
        
        private String obtenerCodigoFuentePirata(String nombrePirata, String nombreClase) {
            try {
                // 🎯 STRATEGY 1: USE EXISTING SCANNER
                if (scanner != null) {
                    // Force scan if necessary
                    if (scanner.getClasses().isEmpty()) {
                        bitacora.info("🔍 Executing quick scan for: " + nombreClase);
                        scanner.scanProjectTurbo("."); // Scan current directory
                    }
                    
                    // Search in scanner's classMap
                    Optional<ClassInfo> classInfo = scanner.getClassByName(nombreClase);
                    if (classInfo.isPresent()) {
                        String sourcePath = classInfo.get().getSourcePath();
                        if (sourcePath != null) {
                            File archivo = new File(sourcePath);
                            if (archivo.exists()) {
                                String codigo = Files.readString(archivo.toPath(), StandardCharsets.UTF_8);
                                if (!codigo.trim().isEmpty()) {
                                    bitacora.info("✅ Code found via scanner: " + nombreClase);
                                    return codigo;
                                }
                            }
                        }
                    }
                }
                
                // 🎯 STRATEGY 2: INTELLIGENT DIRECT SEARCH
                String codigoDirecto = buscarCodigoFuenteDirecto(nombreClase);
                if (codigoDirecto != null && !codigoDirecto.contains("Could not find")) {
                    return codigoDirecto;
                }
                
                // 🎯 STRATEGY 3: GENERATE CODE BASED ON ROLE
                return (nombrePirata + nombreClase);
                
            } catch (Exception e) {
                bitacora.error("💥 Error getting code for " + nombrePirata + ": " + e.getMessage());
                return (nombrePirata + nombreClase);
            }
        }

        /**
         * 🎯 IMPROVED DIRECT SEARCH
         */
        private String buscarCodigoFuenteDirecto(String nombreClase) {
            // 🎯 CONVERT CLASS NAME TO POSSIBLE PATHS
            String rutaClase = nombreClase.replace('.', '/') + ".java";
            
            // 🎯 COMPLETE LIST OF POSSIBLE LOCATIONS
            String[] ubicaciones = {
                "src/main/java/" + rutaClase,
                "src/test/java/" + rutaClase, 
                "main/java/" + rutaClase,
                "test/java/" + rutaClase,
                "src/" + rutaClase,
                "./" + rutaClase,
                "../" + rutaClase,
                nombreClase.replace('.', '/') + ".java" // Absolute path from root
            };
            
            for (String ubicacion : ubicaciones) {
                try {
                    File archivo = new File(ubicacion);
                    if (archivo.exists() && archivo.isFile()) {
                        String contenido = Files.readString(archivo.toPath(), StandardCharsets.UTF_8);
                        if (!contenido.trim().isEmpty()) {
                            bitacora.info("✅ Code found at: " + ubicacion);
                            return contenido;
                        }
                    }
                } catch (Exception e) {
                    // Continue with next location
                }
            }
            
            return null;
        }
    }   
}
