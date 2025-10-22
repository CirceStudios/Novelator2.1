package com.elreinodelolvido.ellibertad.manager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.elreinodelolvido.ellibertad.AutogenTurboFusion;
import com.elreinodelolvido.ellibertad.api.OraculoDeLaLibertad;
import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.manager.TripulacionManager.PirataInfo;
import com.elreinodelolvido.ellibertad.memoria.SistemaMemoriaPirata;
import com.elreinodelolvido.ellibertad.model.ClassInfo;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;
import com.elreinodelolvido.ellibertad.util.FileUtils;

/**
 * 🏴‍☠️ WAR COUNCIL MANAGER - Epic debate between pirates
 * 🎯 Sailors discuss, remember previous sessions and reach consensus
 */
public class ConsejoDeGuerraManager {
    private final TripulacionManager tripulacionManager;
    private final Bitacora bitacora;
    private final Map<String, List<IntervencionPirata>> memoriaDebates;
    private DebateActual debateActual;
    private final Scanner inputScanner;
    
    // 🎯 STRUCTURE FOR DEBATE MEMORY
    public static class IntervencionPirata {
        private final String pirata;
        private final String rol;
        private final String intervencion;
        private final long timestamp;
        private final String temaDebate;
        private final int ronda;
        
        public IntervencionPirata(String pirata, String rol, String intervencion, String temaDebate, int ronda) {
            this.pirata = pirata;
            this.rol = rol;
            this.intervencion = intervencion;
            this.timestamp = System.currentTimeMillis();
            this.temaDebate = temaDebate;
            this.ronda = ronda;
        }
        
        // Getters
        public String getPirata() { return pirata; }
        public String getRol() { return rol; }
        public String getIntervencion() { return intervencion; }
        public long getTimestamp() { return timestamp; }
        public String getTemaDebate() { return temaDebate; }
        public int getRonda() { return ronda; }
        
        @Override
        public String toString() {
            return String.format("[Round %d] %s (%s): %s", ronda, pirata, rol, 
                intervencion.length() > 100 ? intervencion.substring(0, 100) + "..." : intervencion);
        }
    }
    
    private static class DebateActual {
        String preguntaOriginal;
        List<IntervencionPirata> intervenciones;
        Set<String> piratasQueHanIntervenido;
        int rondas;
        String temaPrincipal;
        
        DebateActual(String pregunta) {
            this.preguntaOriginal = pregunta;
            this.intervenciones = new ArrayList<>();
            this.piratasQueHanIntervenido = new HashSet<>();
            this.rondas = 0;
            this.temaPrincipal = extraerTemaPrincipal(pregunta);
            
        }
        
        private static String extraerTemaPrincipal(String pregunta) {
            // 🎯 Extract keywords to group similar debates
            String[] palabrasClave = {"performance", "optimization", "design", "architecture", 
                                    "code", "refactor", "bug", "error", "implementation", 
                                    "test", "quality", "security", "scalability"};
            for (String palabra : palabrasClave) {
                if (pregunta.toLowerCase().contains(palabra)) {
                    return palabra;
                }
            }
            return "general";
        }
        
        void agregarIntervencion(IntervencionPirata intervencion) {
            intervenciones.add(intervencion);
            piratasQueHanIntervenido.add(intervencion.getPirata());
        }
        
        boolean haIntervenido(String pirata) {
            return piratasQueHanIntervenido.contains(pirata);
        }
        
        String obtenerResumenDebate() {
            StringBuilder sb = new StringBuilder();
            sb.append("📜 CURRENT DEBATE SUMMARY:\n");
            sb.append("Question: ").append(preguntaOriginal).append("\n\n");
            sb.append("Interventions by round:\n");
            
            Map<Integer, List<IntervencionPirata>> porRonda = new HashMap<>();
            for (IntervencionPirata interv : intervenciones) {
                porRonda.computeIfAbsent(interv.getRonda(), k -> new ArrayList<>()).add(interv);
            }
            
            for (Map.Entry<Integer, List<IntervencionPirata>> entry : porRonda.entrySet()) {
                sb.append("\n🎯 ROUND ").append(entry.getKey()).append(":\n");
                for (IntervencionPirata interv : entry.getValue()) {
                    sb.append("  • ").append(interv.getPirata()).append(": ")
                      .append(interv.getIntervencion().substring(0, 
                          Math.min(80, interv.getIntervencion().length())))
                      .append("...\n");
                }
            }
            
            return sb.toString();
        }
    }
    
    public ConsejoDeGuerraManager(TripulacionManager tripulacionManager, Bitacora bitacora) {
        this.tripulacionManager = tripulacionManager;
        this.bitacora = bitacora;
        this.memoriaDebates = new ConcurrentHashMap<>();
        this.inputScanner = new Scanner(System.in);
        this.memoria = SistemaMemoriaPirata.obtenerInstancia();
        
        bitacora.info("🏴‍☠️ War Council initialized - Pirates will debate as a true team");
    }
    
    /**
     * 🎯 START WAR COUNCIL - Complete debate between pirates
     */
    public void iniciarConsejoDeGuerra() {
        mostrarBannerConsejo();
        
        while (true) {
            // 1. USER QUESTION
            String pregunta = solicitarPreguntaUsuario();
            if (pregunta == null || esComandoSalir(pregunta)) {
                break;
            }
            
            // 2. START DEBATE
            realizarDebateCompleto(pregunta);
            
            // 3. ASK TO CONTINUE
            if (!preguntarContinuar()) {
                break;
            }
        }
        
        finalizarConsejo();
    }
    
    /**
     * 🏴‍☠️ PERFORM COMPLETE DEBATE BETWEEN PIRATES
     */
    private void realizarDebateCompleto(String pregunta) {
        debateActual = new DebateActual(pregunta);
        int ronda = 1;
        boolean continuarDebate = true;
        
        bitacora.info("🎯 STARTING DEBATE: " + pregunta);
        System.out.println("\n🌊 STARTING PIRATE WAR COUNCIL...");
        System.out.println("❓ QUESTION: " + pregunta);
        System.out.println("=" .repeat(80));
        
        // 🎯 LOAD PREVIOUS MEMORY ABOUT THIS TOPIC
        String contextoMemoria = cargarContextoMemoria(debateActual.temaPrincipal);
        
        while (continuarDebate && ronda <= 5) { // Maximum 5 rounds
            System.out.println("\n🎯 DEBATE ROUND " + ronda);
            
            // A. SELECT PIRATE TO INTERVENE
            String pirataInterviniendo = seleccionarPirataParaRonda(pregunta, ronda, contextoMemoria);
            if (pirataInterviniendo == null) {
                System.out.println("🤐 The crew has nothing more to contribute this round");
                break;
            }
            
            // B. GET PIRATE INTERVENTION
            String intervencion = obtenerIntervencionPirata(pirataInterviniendo, pregunta, ronda, contextoMemoria);
            
            // C. REGISTER INTERVENTION
            TripulacionManager.PirataInfo pirata = tripulacionManager.getMapaTripulacion().get(pirataInterviniendo);
            IntervencionPirata interv = new IntervencionPirata(
                pirata != null ? pirata.getNombrePirata() : pirataInterviniendo,
                pirata != null ? pirata.getRolPirata() : "Sailor",
                intervencion,
                debateActual.temaPrincipal,
                ronda
            );
            
            debateActual.agregarIntervencion(interv);
            
            // D. SHOW INTERVENTION
            mostrarIntervencionPirata(interv);
            
            // E. ASK DEEPSEEK IF ANOTHER PIRATE SHOULD INTERVENE
            continuarDebate = deberiaContinuarDebate(pregunta, ronda, contextoMemoria);
            
            ronda++;
            debateActual.rondas = ronda;
            
            // Small dramatic pause
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        
        // 🎯 SAVE TO MEMORY
        guardarDebateEnMemoria();
        
        // 📊 SHOW FINAL SUMMARY
        mostrarResumenFinalDebate();
    }
    
    /**
     * 🎯 SELECT PIRATE FOR CURRENT ROUND
     */
    private String seleccionarPirataParaRonda(String pregunta, int ronda, String contextoMemoria) {
        try {
            List<ClassInfo> clasesDisponibles = obtenerClasesDisponibles();
            bitacora.info("🔍 DIAGNOSTIC - Available classes: " + clasesDisponibles.size());
            
            if (clasesDisponibles.isEmpty()) {
                bitacora.error("❌ No classes available for debate");
                return null;
            }
            
            // 🎯 DETAILED LOG OF AVAILABLE CLASSES
            bitacora.debug("📋 Classes available for round " + ronda + ":");
            clasesDisponibles.forEach(clase -> 
                bitacora.debug("   • " + clase.getFullName() + " [" + clase.getType() + "]")
            );
            
            // 🎯 BUILD LIST OF PIRATES WHO HAVEN'T INTERVENED
            List<ClassInfo> piratasNoIntervenidos = new ArrayList<>();
            for (ClassInfo clase : clasesDisponibles) {
                String nombreClase = clase.getFullName();
                if (!debateActual.haIntervenido(nombreClase)) {
                    piratasNoIntervenidos.add(clase);
                }
            }
            
            bitacora.debug("🎯 Pirates not intervened: " + piratasNoIntervenidos.size());
            
            // If all have intervened, allow repetitions
            if (piratasNoIntervenidos.isEmpty()) {
                piratasNoIntervenidos = clasesDisponibles;
                bitacora.debug("🔁 All have intervened, allowing repetitions");
            }
            
            // 🎯 PROMPT FOR SELECTION CONSIDERING MEMORY
            StringBuilder prompt = new StringBuilder();
            prompt.append("You are the captain in a Pirate War Council. Round ").append(ronda).append(".\n\n");
            
            if (!contextoMemoria.isEmpty()) {
                prompt.append("PREVIOUS DEBATES CONTEXT:\n").append(contextoMemoria).append("\n\n");
            }
            
            prompt.append("AVAILABLE PIRATES:\n");
            for (ClassInfo clase : piratasNoIntervenidos) {
                String nombreClase = clase.getFullName();
                TripulacionManager.PirataInfo pirata = tripulacionManager.getMapaTripulacion().get(nombreClase);
                if (pirata != null) {
                    prompt.append("- ").append(nombreClase).append(" (")
                          .append(pirata.getNombrePirata()).append(" - ").append(pirata.getRolPirata()).append(")\n");
                } else {
                    // 🎯 CREATE TEMPORARY PIRATE IF NOT IN MAP
                    String nombreSimple = extraerNombreSimple(nombreClase);
                    prompt.append("- ").append(nombreClase).append(" (")
                          .append(nombreSimple).append(" - Sailor)\n");
                }
            }
            
            prompt.append("\nCURRENT QUESTION: ").append(pregunta).append("\n\n");
            
            if (ronda > 1) {
                prompt.append("PREVIOUS INTERVENTIONS IN THIS DEBATE:\n")
                      .append(debateActual.obtenerResumenDebate()).append("\n\n");
            }
            
            prompt.append("Which pirate has the most valuable perspective to contribute this round?\n");
            prompt.append("Consider specialization, previous experiences and what new angle they can provide.\n");
            prompt.append("Return ONLY the full class name chosen.");
            
            // 🚀 USE ORACLE FOR SELECTION
            OraculoDeLaLibertad oraculo = new OraculoDeLaLibertad();
            String respuesta = oraculo.invocar(prompt.toString(), "war_council_selection", 0.4);
            
            bitacora.debug("🤖 Oracle response: " + respuesta);
            
            String pirataSeleccionado = extraerNombreClaseDeRespuesta(respuesta, piratasNoIntervenidos);
            
            if (pirataSeleccionado != null) {
                bitacora.info("🎯 Pirate selected for round " + ronda + ": " + pirataSeleccionado);
            } else {
                bitacora.warn("⚠️ Could not select pirate, using random selection");
                pirataSeleccionado = seleccionarPirataAleatorio(piratasNoIntervenidos);
            }
            
            return pirataSeleccionado;
            
        } catch (Exception e) {
            bitacora.error("💥 Error selecting pirate for council", e);
            return seleccionarPirataAleatorio(obtenerClasesDisponibles());
        }
    }
    
   
    
    /**
     * 🔄 DECIDE IF DEBATE SHOULD CONTINUE
     */
    private boolean deberiaContinuarDebate(String pregunta, int ronda, String contextoMemoria) {
        if (ronda >= 5) { // Maximum round limit
            return false;
        }
        
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("You are the moderator of the Pirate War Council.\n\n");
            prompt.append("CURRENT DEBATE - Round ").append(ronda).append(":\n");
            prompt.append("Question: ").append(pregunta).append("\n");
            prompt.append("Interventions so far:\n").append(debateActual.obtenerResumenDebate()).append("\n\n");
            
            if (!contextoMemoria.isEmpty()) {
                prompt.append("HISTORICAL CONTEXT:\n").append(contextoMemoria).append("\n\n");
            }
            
            prompt.append("Do you think ANOTHER pirate should intervene with a different perspective?\n");
            prompt.append("Consider:\n");
            prompt.append("- Are there unexplored angles?\n");
            prompt.append("- Are there contradictions to resolve?\n");
            prompt.append("- Is the discussion reaching consensus?\n\n");
            prompt.append("Respond ONLY with 'true' or 'false'.");
            
            OraculoDeLaLibertad oraculo = new OraculoDeLaLibertad();
            String respuesta = oraculo.invocar(prompt.toString(), "debate_continuation", 0.3);
            
            return "true".equalsIgnoreCase(respuesta.trim());
            
        } catch (Exception e) {
            bitacora.error("Error deciding debate continuation", e);
            // By default, continue if we haven't reached the limit
            return ronda < 3;
        }
    }
    
    /**
     * 🧠 LOAD MEMORY CONTEXT
     */
    private String cargarContextoMemoria(String tema) {
        List<IntervencionPirata> debatesPrevios = memoriaDebates.get(tema);
        if (debatesPrevios == null || debatesPrevios.isEmpty()) {
            return "";
        }
        
        StringBuilder contexto = new StringBuilder();
        contexto.append("📜 PREVIOUS DEBATES ABOUT '").append(tema).append("':\n\n");
        
        // Group by pirate and show their consistent positions
        Map<String, List<IntervencionPirata>> porPirata = new HashMap<>();
        for (IntervencionPirata interv : debatesPrevios) {
            porPirata.computeIfAbsent(interv.getPirata(), k -> new ArrayList<>()).add(interv);
        }
        
        for (Map.Entry<String, List<IntervencionPirata>> entry : porPirata.entrySet()) {
            contexto.append(entry.getKey()).append(" has previously said:\n");
            for (IntervencionPirata interv : entry.getValue()) {
                contexto.append("• ").append(interv.getIntervencion().substring(0, 
                    Math.min(100, interv.getIntervencion().length()))).append("...\n");
            }
            contexto.append("\n");
        }
        
        return contexto.toString();
    }
    
    /**
     * 💾 SAVE DEBATE TO MEMORY
     */
    private void guardarDebateEnMemoria() {
        String tema = debateActual.temaPrincipal;
        memoriaDebates.computeIfAbsent(tema, k -> new ArrayList<>())
                     .addAll(debateActual.intervenciones);
        
        bitacora.info("💾 Debate saved to memory - Topic: " + tema + 
                     " - Interventions: " + debateActual.intervenciones.size());
    }
    
    // =========================================================================
    // 🎪 AUXILIARY AND VISUAL METHODS
    // =========================================================================
    
    private void mostrarBannerConsejo() {
        System.out.println("\n" + "⚔️".repeat(80));
        System.out.println("                  PIRATE WAR COUNCIL ACTIVATED!");
        System.out.println("🎯 Sailors will debate, remember and reach consensus");
        System.out.println("🧠 Previous debate memory: " + memoriaDebates.size() + " topics");
        System.out.println("⚔️".repeat(80));
    }
    
    private String solicitarPreguntaUsuario() {
        System.out.println("\n" + "🎯".repeat(60));
        System.out.println("🏴‍☠️  WAR COUNCIL - QUESTION FOR THE CREW");
        System.out.println("🎯".repeat(60));
        System.out.println("Write your question for the debate (or 'exit' to finish):");
        System.out.print("❓ > ");
        
        String pregunta = inputScanner.nextLine().trim();
        
        if (pregunta.isEmpty()) {
            System.out.println("⚠️  Question cannot be empty.");
            return null;
        }
        
        return pregunta;
    }
    
    private void mostrarIntervencionPirata(IntervencionPirata intervencion) {
        System.out.println("\n" + "🌊".repeat(80));
        System.out.println("🏴‍☠️  INTERVENES " + intervencion.getPirata().toUpperCase());
        System.out.println("📜 Round " + intervencion.getRonda() + " | Role: " + intervencion.getRol());
        System.out.println("🌊".repeat(80));
        System.out.println(intervencion.getIntervencion());
        System.out.println("⚓".repeat(80));
    }
    
    private void mostrarResumenFinalDebate() {
        System.out.println("\n" + "📜".repeat(80));
        System.out.println("                  WAR COUNCIL FINAL SUMMARY");
        System.out.println("📜".repeat(80));
        System.out.println("❓ Original question: " + debateActual.preguntaOriginal);
        System.out.println("🎯 Rounds completed: " + (debateActual.rondas - 1));
        System.out.println("👥 Pirates who intervened: " + debateActual.piratasQueHanIntervenido.size());
        System.out.println("💾 Topic saved in memory: '" + debateActual.temaPrincipal + "'");
        System.out.println("\n" + "🏆".repeat(40));
        System.out.println("           DEBATE SUCCESSFULLY CONCLUDED!");
        System.out.println("🏆".repeat(40));
    }
    
    private boolean preguntarContinuar() {
        System.out.println("\nDo you want to conduct another War Council? (y/n)");
        System.out.print("🎯 > ");
        String respuesta = inputScanner.nextLine().trim();
        return respuesta.equalsIgnoreCase("y") || respuesta.equalsIgnoreCase("yes");
    }
    
    private void finalizarConsejo() {
        System.out.println("\n" + "🌅".repeat(80));
        System.out.println("                  WAR COUNCIL FINISHED");
        System.out.println("🏴‍☠️ Pirates rest... but their wisdom remains in memory");
        System.out.println("📚 Debates saved: " + memoriaDebates.size() + " different topics");
        System.out.println("🌅".repeat(80));
        
        bitacora.info("🏴‍☠️ War Council finished - " + memoriaDebates.size() + " topics in memory");
    }
    
    // =========================================================================
    // 🔧 INTEGRATION METHODS WITH TRIPULACIONMANAGER
    // =========================================================================
    
    ProjectScanner scanner = new ProjectScanner();
    FileUtils file = new FileUtils();
    private Bitacora bitacoraTurbo;
    
    private List<ClassInfo> obtenerClasesDisponibles() {
        try {
            // 🎯 FIRST VERIFY IF THERE ARE SCANNED CLASSES
            List<ClassInfo> clases = ProjectScanner.getClasses();
            
            if (clases == null || clases.isEmpty()) {
                bitacora.warn("🔄 No classes scanned, executing automatic scan...");
                
                // 🆕 EXECUTE SCAN IF NO CLASSES
                
                
                // 🎯 TRY AGAIN
                clases = ProjectScanner.getClasses();
            }
            
            if (clases == null || clases.isEmpty()) {
                bitacora.warn("⚠️ ProjectScanner.getClasses() returned empty list");
                return crearClasesDesdeRolesPiratas();
            }
            
            bitacora.info("🎯 Classes available for debate: " + clases.size());
            return clases;
            
        } catch (Exception e) {
            bitacora.error("💥 Error in obtenerClasesDisponibles(): " + e.getMessage());
            return crearClasesDesdeRolesPiratas();
        }
    }

    private String extraerNombreClaseDeRespuesta(String respuesta, List<ClassInfo> clases) {
        if (respuesta == null || clases == null || clases.isEmpty()) {
            return null;
        }
        
        String respuestaLimpia = respuesta.trim();
        
        // 🎯 SEARCH IN AVAILABLE CLASSES LIST
        for (ClassInfo clase : clases) {
            String fullName = clase.getFullName();
            String simpleName = clase.getName();
            
            if (respuestaLimpia.equals(fullName) || 
                respuestaLimpia.equals(simpleName) ||
                respuestaLimpia.contains(fullName) ||
                respuestaLimpia.contains(simpleName)) {
                return fullName;
            }
        }
        
        // 🎯 SEARCH IN ROLES_PIRATAS AS FALLBACK
        for (String claseName : TripulacionManager.ROLES_PIRATAS.keySet()) {
            if (respuestaLimpia.contains(claseName)) {
                return "com.novelator.autogen." + claseName;
            }
        }
        
        bitacora.warn("🔍 Could not find class in response: " + respuestaLimpia);
        return null;
    }

    private String obtenerCodigoFuente(String nombreClase) {
        
        OraculoDeLaLibertad oraculo = new OraculoDeLaLibertad();
        TripulacionManager tripulacion = new TripulacionManager(scanner, oraculo, bitacora);
        PirataInfo pirata = new PirataInfo(nombreClase, nombreClase, nombreClase, nombreClase);
        
        try {
            // 🎯 CLEAN AND NORMALIZE CLASS NAME
            String normalizedName = normalizarNombreClase(nombreClase);
            
            // 🎯 SEARCH IN MULTIPLE LOCATIONS
            String[] posiblesPaths = {
                "src/main/java/" + normalizedName.replace('.', '/') + ".java",
                "src/test/java/" + normalizedName.replace('.', '/') + ".java",
                normalizedName.replace('.', '/') + ".java",
                "com/novelator/autogen/" + extraerNombreSimple(normalizedName) + ".java"
            };
            
            for (String path : posiblesPaths) {
                try {
                    String codigo = FileUtils.buscarCodigoJavaCompleto(path);
                    if (codigo != null && !codigo.trim().isEmpty()) {
                        return codigo;
                    }
                } catch (Exception e) {
                    // Continue with next path
                }
            }
            
            
            return tripulacion.simularRespuestaPirata(nombreClase, pirata);
            
        } catch (Exception e) {
            bitacora.error("💥 Error getting source code: " + nombreClase, e);
            String fail = "Epic Fail";
            return fail;
        }
    }

    private String normalizarNombreClase(String nombreClase) {
        if (nombreClase == null) return "";
        
        // 🎯 REMOVE REDUNDANT PACKAGE
        if (nombreClase.startsWith("com.novelator.autogen.com.novelator.autogen.")) {
            return nombreClase.replace("com.novelator.autogen.com.novelator.autogen.", "com.novelator.autogen.");
        }
        
        // 🎯 ADD DEFAULT PACKAGE IF NOT PRESENT
        if (!nombreClase.contains(".") && TripulacionManager.ROLES_PIRATAS.containsKey(nombreClase)) {
            return "com.novelator.autogen." + nombreClase;
        }
        
        return nombreClase;
    }

    private String extraerNombreSimple(String fullClassName) {
        if (fullClassName == null) return "";
        int lastDot = fullClassName.lastIndexOf('.');
        return lastDot > 0 ? fullClassName.substring(lastDot + 1) : fullClassName;
    }

    private String extraerPackage(String fullClassName) {
        if (fullClassName == null) return "com.novelator.autogen";
        int lastDot = fullClassName.lastIndexOf('.');
        return lastDot > 0 ? fullClassName.substring(0, lastDot) : "com.novelator.autogen";
    }

    private String seleccionarPirataAleatorio(List<ClassInfo> clases) {
        // Simple random selection logic
        if (clases == null || clases.isEmpty()) {
            return null;
        }
        Random random = new Random();
        ClassInfo claseAleatoria = clases.get(random.nextInt(clases.size()));
        return claseAleatoria.getName();
    }
    
    private boolean esComandoSalir(String input) {
        return input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit") || 
               input.equalsIgnoreCase("bye");
    }
    
    // 🎯 ADDITIONAL PUBLIC METHODS
    
    /**
     * 📊 SHOW MEMORY STATISTICS
     */
    public void mostrarEstadisticasMemoria() {
        System.out.println("\n🧠 WAR COUNCIL MEMORY STATISTICS:");
        System.out.println("Topics debated: " + memoriaDebates.size());
        
        for (Map.Entry<String, List<IntervencionPirata>> entry : memoriaDebates.entrySet()) {
            System.out.println("  • " + entry.getKey() + ": " + entry.getValue().size() + " interventions");
        }
    }
    
    /**
     * 🧹 CLEAR DEBATE MEMORY
     */
    public void limpiarMemoria() {
        int totalIntervenciones = memoriaDebates.values().stream()
                .mapToInt(List::size)
                .sum();
        memoriaDebates.clear();
        System.out.println("🧹 Memory cleared: " + totalIntervenciones + " interventions deleted");
        bitacora.info("War Council memory cleared");
    }
    
    private List<ClassInfo> crearClasesDesdeRolesPiratas() {
        List<ClassInfo> clases = new ArrayList<>();
        
        for (String className : TripulacionManager.ROLES_PIRATAS.keySet()) {
            ClassInfo classInfo = new ClassInfo();
            classInfo.setName(className);
            classInfo.setPackageName("com.novelator.autogen");
            classInfo.setFullName("com.novelator.autogen." + className);
            classInfo.setType("CLASS");
            
            // 🎯 ADD BASIC METADATA
            classInfo.addAnnotation("Source", "ROLES_PIRATAS");
            classInfo.addAnnotation("Rescue", "true");
            
            clases.add(classInfo);
        }
        
        bitacora.info("🛡️ Classes created from ROLES_PIRATAS: " + clases.size());
        return clases;
    }
    
    // In ConsejoDeGuerraManager, add:
    private final SistemaMemoriaPirata memoria;

    /**
     * 🎯 GET IMPROVED PIRATE INTERVENTION (with memory)
     */
    private String obtenerIntervencionPirata(String nombreClase, String pregunta, int ronda, String contextoMemoria) {
        try {
            // 🎯 TRY TO USE MEMORY SYSTEM FIRST
            Optional<String> nombrePirataOpt = encontrarPirataPorClase(nombreClase);
            
            if (nombrePirataOpt.isPresent()) {
                String nombrePirata = nombrePirataOpt.get();
                
                // 🎯 GET PIRATE'S PERSONAL CONTEXT FROM MEMORY
                String contextoPersonal = "";
                try {
                    SistemaMemoriaPirata memoria = SistemaMemoriaPirata.obtenerInstancia();
                    contextoPersonal = memoria.obtenerMemoriaPirata(nombrePirata)
                            .map(m -> m.obtenerContextoPersonalizado(pregunta))
                            .orElse("");
                } catch (Exception e) {
                    bitacora.debug("🧠 Memory not available, continuing without personal context");
                }
                
                // 🎯 BUILD IMPROVED PROMPT WITH MEMORY
                String prompt = construirPromptIntervencionConMemoria(
                    nombrePirata, pregunta, ronda, contextoMemoria, contextoPersonal);
                
                // 🎯 INVOKE ORACLE
                OraculoDeLaLibertad oraculo = new OraculoDeLaLibertad();
                String intervencion = oraculo.invocar(prompt, "council_intervention_memory", 0.7);
                
                // 🎯 REGISTER IN MEMORY IF AVAILABLE
                try {
                    SistemaMemoriaPirata memoria = SistemaMemoriaPirata.obtenerInstancia();
                    // Here you could register the intervention in the pirate's memory
                } catch (Exception e) {
                    // Ignore memory errors
                }
                
                bitacora.info("🎯 Intervention with memory generated for: " + nombrePirata);
                return intervencion;
                
            } else {
                // 🎯 FALLBACK: USE ORIGINAL METHOD WITHOUT MEMORY
                bitacora.debug("🔍 Pirate not found, using original method for: " + nombreClase);
                return obtenerIntervencionPirataOriginal(nombreClase, pregunta, ronda, contextoMemoria);
            }
            
        } catch (Exception e) {
            bitacora.error("💥 Error in improved pirate intervention: " + nombreClase, e);
            // 🎯 EMERGENCY FALLBACK
            return obtenerIntervencionPirataOriginal(nombreClase, pregunta, ronda, contextoMemoria);
        }
    }

    /**
     * 🔍 FIND PIRATE BY CLASS - Using ProjectScanner
     */
    private Optional<String> encontrarPirataPorClase(String nombreClase) {
        try {
            // 🎯 NORMALIZE CLASS NAME
            String nombreNormalizado = normalizarNombreClase(nombreClase);
            
            // 🎯 SEARCH IN CREW MAP
            Map<String, TripulacionManager.PirataInfo> mapaTripulacion = tripulacionManager.getMapaTripulacion();
            
            // 1. Search by full name
            for (Map.Entry<String, TripulacionManager.PirataInfo> entry : mapaTripulacion.entrySet()) {
                if (entry.getKey().equals(nombreNormalizado) || 
                    entry.getKey().equals(nombreClase)) {
                    return Optional.of(entry.getValue().getNombrePirata());
                }
            }
            
            // 2. Search by simple name (without package)
            String nombreSimple = extraerNombreSimple(nombreClase);
            for (Map.Entry<String, TripulacionManager.PirataInfo> entry : mapaTripulacion.entrySet()) {
                String claveSimple = extraerNombreSimple(entry.getKey());
                if (claveSimple.equals(nombreSimple)) {
                    return Optional.of(entry.getValue().getNombrePirata());
                }
            }
            
            // 3. Search in ROLES_PIRATAS as fallback
            for (String claseKey : TripulacionManager.ROLES_PIRATAS.keySet()) {
                if (claseKey.equals(nombreSimple) || nombreClase.contains(claseKey)) {
                    String[] datosPirata = TripulacionManager.ROLES_PIRATAS.get(claseKey);
                    return Optional.of(datosPirata[1]); // Pirate name
                }
            }
            
            bitacora.debug("🔍 No pirate found for class: " + nombreClase);
            return Optional.empty();
            
        } catch (Exception e) {
            bitacora.error("💥 Error finding pirate by class: " + nombreClase, e);
            return Optional.empty();
        }
    }

    /**
     * 🎯 BUILD INTERVENTION PROMPT WITH MEMORY
     */
    private String construirPromptIntervencionConMemoria(String nombrePirata, String pregunta, int ronda, 
                                                       String contextoMemoria, String contextoPersonal) {
        StringBuilder prompt = new StringBuilder();
        
        // 🎯 PIRATE IDENTITY
        prompt.append("You are pirate ").append(nombrePirata);
        
        // 🎯 GET COMPLETE PIRATE INFORMATION
        TripulacionManager.PirataInfo pirata = tripulacionManager.getMapaTripulacion().values().stream()
                .filter(p -> p.getNombrePirata().equals(nombrePirata))
                .findFirst()
                .orElse(null);
        
        if (pirata != null) {
            prompt.append(", the ").append(pirata.getRolPirata())
                  .append(" of the ship. ").append(pirata.getDescripcionRol()).append("\n\n");
        } else {
            prompt.append(", brave member of the pirate crew.\n\n");
        }
        
        prompt.append("YOU ARE PARTICIPATING IN A PIRATE WAR COUNCIL - ROUND ").append(ronda).append("\n\n");
        
        // 🎯 GLOBAL MEMORY CONTEXT
        if (!contextoMemoria.isEmpty()) {
            prompt.append("🧠 HISTORICAL DEBATE CONTEXT:\n")
                  .append(contextoMemoria).append("\n\n");
        }
        
        // 🎯 PIRATE'S PERSONAL CONTEXT
        if (!contextoPersonal.isEmpty()) {
            prompt.append("📜 YOUR PERSONAL EXPERIENCE AND MEMORIES:\n")
                  .append(contextoPersonal).append("\n\n");
        }
        
        // 🎯 PIRATE'S SOURCE CODE
        if (pirata != null) {
            String codigoFuente = obtenerCodigoFuente(pirata.getNombreClase());
            prompt.append("💾 YOUR CURRENT SOURCE CODE:\n```java\n")
                  .append(codigoFuente).append("\n```\n\n");
        }
        
        prompt.append("🎯 MAIN DEBATE QUESTION:\n")
              .append(pregunta).append("\n\n");
        
        // 🎯 SPECIFIC INSTRUCTIONS BY ROUND
        prompt.append("INSTRUCTIONS FOR THIS INTERVENTION (Round ").append(ronda).append("):\n");
        
        if (ronda == 1) {
            prompt.append("• Present your initial perspective as ").append(pirata != null ? pirata.getRolPirata() : "expert").append("\n");
            prompt.append("• Analyze the problem from your unique specialty\n");
            prompt.append("• Establish your clear position on the topic\n");
        } else {
            prompt.append("• Respond directly to previous interventions\n");
            prompt.append("• Refute or support arguments with technical evidence\n");
            prompt.append("• Look for key consensus or disagreement points\n");
        }
        
        prompt.append("• Maintain your pirate personality but be technically precise\n");
        prompt.append("• Propose practical solutions based on your code\n");
        prompt.append("• Consider your relationships with other pirates in the debate\n\n");
        
        prompt.append("📝 REQUIRED RESPONSE FORMAT:\n");
        prompt.append("🎯 [Your perspective as pirate - maintain role and personality]\n");
        prompt.append("🔧 [Specific technical analysis from your specialty]\n");  
        prompt.append("💡 [Practical suggestions based on your current code]\n");
        prompt.append("🤝 [How you would collaborate with other pirates to implement]\n");
        prompt.append("🏴‍☠️ [Epic pirate conclusion - call to action]\n");
        
        return prompt.toString();
    }

    /**
     * 🏴‍☠️ GET ORIGINAL PIRATE INTERVENTION (without memory)
     */
    private String obtenerIntervencionPirataOriginal(String nombreClase, String pregunta, int ronda, String contextoMemoria) {
        try {
            // 🎯 GET BASIC PIRATE INFORMATION
            Optional<String> nombrePirataOpt = encontrarPirataPorClase(nombreClase);
            String nombrePirata = nombrePirataOpt.orElse("Brave Sailor");
            
            TripulacionManager.PirataInfo pirata = tripulacionManager.getMapaTripulacion().values().stream()
                    .filter(p -> p.getNombrePirata().equals(nombrePirata))
                    .findFirst()
                    .orElse(null);
            
            // 🎯 GET SOURCE CODE
            String codigoFuente = obtenerCodigoFuente(nombreClase);
            
            // 🎯 BUILD BASIC PROMPT
            StringBuilder prompt = new StringBuilder();
            prompt.append("You are ").append(nombrePirata);
            
            if (pirata != null) {
                prompt.append(", the ").append(pirata.getRolPirata())
                      .append(". ").append(pirata.getDescripcionRol());
            }
            
            prompt.append("\n\nYOU ARE IN ROUND ").append(ronda).append(" OF A PIRATE WAR COUNCIL.\n\n");
            
            if (!contextoMemoria.isEmpty()) {
                prompt.append("DEBATE CONTEXT:\n").append(contextoMemoria).append("\n\n");
            }
            
            prompt.append("YOUR CURRENT SOURCE CODE:\n```java\n")
                  .append(codigoFuente).append("\n```\n\n");
            
            prompt.append("MAIN QUESTION: ").append(pregunta).append("\n\n");
            
            prompt.append("INSTRUCTIONS:\n");
            prompt.append("1. Respond as pirate maintaining your personality\n");
            prompt.append("2. Analyze the problem from your role and specialty\n");
            prompt.append("3. Propose improvements based on your current code\n");
            prompt.append("4. Be technically precise but with pirate style\n\n");
            
            prompt.append("Response format:\n");
            prompt.append("🎯 [Your perspective as pirate]\n");
            prompt.append("🔧 [Specific technical analysis]\n");
            prompt.append("💡 [Practical suggestions]\n");
            prompt.append("🏴‍☠️ [Epic pirate conclusion]\n");
            
            // 🎯 INVOKE ORACLE
            OraculoDeLaLibertad oraculo = new OraculoDeLaLibertad();
            return oraculo.invocar(prompt.toString(), "council_intervention_original", 0.7);
            
        } catch (Exception e) {
            bitacora.error("💥 Error in original pirate intervention: " + nombreClase, e);
            return "Arrr! My code cannons are temporarily silenced. " +
                   "As expert in " + nombreClase + ", I suggest reviewing system fundamentals " +
                   "and considering strategic refactorings.";
        }
    }
}
