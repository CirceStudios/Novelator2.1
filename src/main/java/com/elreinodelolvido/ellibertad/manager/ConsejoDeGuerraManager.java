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
 * 🏴‍☠️ CONSEJO DE GUERRA MANAGER - Debate épico entre piratas
 * 🎯 Los marineros discuten, recuerdan sesiones previas y llegan a consenso
 */
public class ConsejoDeGuerraManager {
    private final TripulacionManager tripulacionManager;
    private final Bitacora bitacora;
    private final Map<String, List<IntervencionPirata>> memoriaDebates;
    private DebateActual debateActual;
    private final Scanner inputScanner;
    
    // 🎯 ESTRUCTURA PARA MEMORIA DE DEBATES
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
            return String.format("[Ronda %d] %s (%s): %s", ronda, pirata, rol, 
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
            // 🎯 Extraer palabras clave para agrupar debates similares
            String[] palabrasClave = {"rendimiento", "optimización", "diseño", "arquitectura", 
                                    "código", "refactor", "bug", "error", "implementación", 
                                    "test", "calidad", "seguridad", "escalabilidad"};
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
            sb.append("📜 RESUMEN DEL DEBATE ACTUAL:\n");
            sb.append("Pregunta: ").append(preguntaOriginal).append("\n\n");
            sb.append("Intervenciones por ronda:\n");
            
            Map<Integer, List<IntervencionPirata>> porRonda = new HashMap<>();
            for (IntervencionPirata interv : intervenciones) {
                porRonda.computeIfAbsent(interv.getRonda(), k -> new ArrayList<>()).add(interv);
            }
            
            for (Map.Entry<Integer, List<IntervencionPirata>> entry : porRonda.entrySet()) {
                sb.append("\n🎯 RONDA ").append(entry.getKey()).append(":\n");
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
        
        bitacora.info("🏴‍☠️ Consejo de Guerra inicializado - Los piratas debatirán como un verdadero equipo");
    }
    
    /**
     * 🎯 INICIAR CONSEJO DE GUERRA - Debate completo entre piratas
     */
    public void iniciarConsejoDeGuerra() {
        mostrarBannerConsejo();
        
        while (true) {
            // 1. PREGUNTA DEL USUARIO
            String pregunta = solicitarPreguntaUsuario();
            if (pregunta == null || esComandoSalir(pregunta)) {
                break;
            }
            
            // 2. INICIAR DEBATE
            realizarDebateCompleto(pregunta);
            
            // 3. PREGUNTAR SI CONTINUAR
            if (!preguntarContinuar()) {
                break;
            }
        }
        
        finalizarConsejo();
    }
    
    /**
     * 🏴‍☠️ REALIZAR DEBATE COMPLETO ENTRE PIRATAS
     */
    private void realizarDebateCompleto(String pregunta) {
        debateActual = new DebateActual(pregunta);
        int ronda = 1;
        boolean continuarDebate = true;
        
        bitacora.info("🎯 INICIANDO DEBATE: " + pregunta);
        System.out.println("\n🌊 INICIANDO CONSEJO DE GUERRA PIRATA...");
        System.out.println("❓ PREGUNTA: " + pregunta);
        System.out.println("=" .repeat(80));
        
        // 🎯 CARGAR MEMORIA PREVIA SOBRE ESTE TEMA
        String contextoMemoria = cargarContextoMemoria(debateActual.temaPrincipal);
        
        while (continuarDebate && ronda <= 5) { // Máximo 5 rondas
            System.out.println("\n🎯 RONDA " + ronda + " DEL DEBATE");
            
            // A. SELECCIONAR PIRATA PARA INTERVENIR
            String pirataInterviniendo = seleccionarPirataParaRonda(pregunta, ronda, contextoMemoria);
            if (pirataInterviniendo == null) {
                System.out.println("🤐 La tripulación no tiene más que aportar en esta ronda");
                break;
            }
            
            // B. OBTENER INTERVENCIÓN DEL PIRATA
            String intervencion = obtenerIntervencionPirata(pirataInterviniendo, pregunta, ronda, contextoMemoria);
            
            // C. REGISTRAR INTERVENCIÓN
            TripulacionManager.PirataInfo pirata = tripulacionManager.getMapaTripulacion().get(pirataInterviniendo);
            IntervencionPirata interv = new IntervencionPirata(
                pirata != null ? pirata.getNombrePirata() : pirataInterviniendo,
                pirata != null ? pirata.getRolPirata() : "Marinero",
                intervencion,
                debateActual.temaPrincipal,
                ronda
            );
            
            debateActual.agregarIntervencion(interv);
            
            // D. MOSTRAR INTERVENCIÓN
            mostrarIntervencionPirata(interv);
            
            // E. PREGUNTAR A DEEPSEEK SI OTRO PIRATA DEBE INTERVENIR
            continuarDebate = deberiaContinuarDebate(pregunta, ronda, contextoMemoria);
            
            ronda++;
            debateActual.rondas = ronda;
            
            // Pequeña pausa dramática
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        
        // 🎯 GUARDAR EN MEMORIA
        guardarDebateEnMemoria();
        
        // 📊 MOSTRAR RESUMEN FINAL
        mostrarResumenFinalDebate();
    }
    
    /**
     * 🎯 SELECCIONAR PIRATA PARA LA RONDA ACTUAL
     */
    private String seleccionarPirataParaRonda(String pregunta, int ronda, String contextoMemoria) {
        try {
            List<ClassInfo> clasesDisponibles = obtenerClasesDisponibles();
            bitacora.info("🔍 DIAGNÓSTICO - Clases disponibles: " + clasesDisponibles.size());
            
            if (clasesDisponibles.isEmpty()) {
                bitacora.error("❌ No hay clases disponibles para el debate");
                return null;
            }
            
            // 🎯 LOG DETALLADO DE CLASES DISPONIBLES
            bitacora.debug("📋 Clases disponibles para ronda " + ronda + ":");
            clasesDisponibles.forEach(clase -> 
                bitacora.debug("   • " + clase.getFullName() + " [" + clase.getType() + "]")
            );
            
            // 🎯 CONSTRUIR LISTA DE PIRATAS QUE NO HAN INTERVENIDO
            List<ClassInfo> piratasNoIntervenidos = new ArrayList<>();
            for (ClassInfo clase : clasesDisponibles) {
                String nombreClase = clase.getFullName();
                if (!debateActual.haIntervenido(nombreClase)) {
                    piratasNoIntervenidos.add(clase);
                }
            }
            
            bitacora.debug("🎯 Piratas no intervenidos: " + piratasNoIntervenidos.size());
            
            // Si todos han intervenido, permitir repeticiones
            if (piratasNoIntervenidos.isEmpty()) {
                piratasNoIntervenidos = clasesDisponibles;
                bitacora.debug("🔁 Todos han intervenido, permitiendo repeticiones");
            }
            
            // 🎯 PROMPT PARA SELECCIÓN CONSIDERANDO MEMORIA
            StringBuilder prompt = new StringBuilder();
            prompt.append("Eres el capitán en un Consejo de Guerra Pirata. Ronda ").append(ronda).append(".\n\n");
            
            if (!contextoMemoria.isEmpty()) {
                prompt.append("CONTEXTO DE DEBATES PREVIOS:\n").append(contextoMemoria).append("\n\n");
            }
            
            prompt.append("PIRATAS DISPONIBLES:\n");
            for (ClassInfo clase : piratasNoIntervenidos) {
                String nombreClase = clase.getFullName();
                TripulacionManager.PirataInfo pirata = tripulacionManager.getMapaTripulacion().get(nombreClase);
                if (pirata != null) {
                    prompt.append("- ").append(nombreClase).append(" (")
                          .append(pirata.getNombrePirata()).append(" - ").append(pirata.getRolPirata()).append(")\n");
                } else {
                    // 🎯 CREAR PIRATA TEMPORAL SI NO EXISTE EN EL MAPA
                    String nombreSimple = extraerNombreSimple(nombreClase);
                    prompt.append("- ").append(nombreClase).append(" (")
                          .append(nombreSimple).append(" - Marinero)\n");
                }
            }
            
            prompt.append("\nPREGUNTA ACTUAL: ").append(pregunta).append("\n\n");
            
            if (ronda > 1) {
                prompt.append("INTERVENCIONES PREVIAS EN ESTE DEBATE:\n")
                      .append(debateActual.obtenerResumenDebate()).append("\n\n");
            }
            
            prompt.append("¿Qué pirata tiene la perspectiva más valiosa para aportar en esta ronda?\n");
            prompt.append("Considera especialización, experiencias previas y qué nuevo ángulo puede aportar.\n");
            prompt.append("Devuelve SOLO el nombre completo de la clase elegida.");
            
            // 🚀 USAR ORÁCULO PARA SELECCIÓN
            OraculoDeLaLibertad oraculo = new OraculoDeLaLibertad();
            String respuesta = oraculo.invocar(prompt.toString(), "seleccion_consejo_guerra", 0.4);
            
            bitacora.debug("🤖 Respuesta del oráculo: " + respuesta);
            
            String pirataSeleccionado = extraerNombreClaseDeRespuesta(respuesta, piratasNoIntervenidos);
            
            if (pirataSeleccionado != null) {
                bitacora.info("🎯 Pirata seleccionado para ronda " + ronda + ": " + pirataSeleccionado);
            } else {
                bitacora.warn("⚠️ No se pudo seleccionar pirata, usando selección aleatoria");
                pirataSeleccionado = seleccionarPirataAleatorio(piratasNoIntervenidos);
            }
            
            return pirataSeleccionado;
            
        } catch (Exception e) {
            bitacora.error("💥 Error seleccionando pirata para consejo", e);
            return seleccionarPirataAleatorio(obtenerClasesDisponibles());
        }
    }
    
   
    
    /**
     * 🔄 DECIDIR SI CONTINÚA EL DEBATE
     */
    private boolean deberiaContinuarDebate(String pregunta, int ronda, String contextoMemoria) {
        if (ronda >= 5) { // Límite máximo de rondas
            return false;
        }
        
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("Eres el moderador del Consejo de Guerra Pirata.\n\n");
            prompt.append("DEBATE ACTUAL - Ronda ").append(ronda).append(":\n");
            prompt.append("Pregunta: ").append(pregunta).append("\n");
            prompt.append("Intervenciones hasta ahora:\n").append(debateActual.obtenerResumenDebate()).append("\n\n");
            
            if (!contextoMemoria.isEmpty()) {
                prompt.append("CONTEXTO HISTÓRICO:\n").append(contextoMemoria).append("\n\n");
            }
            
            prompt.append("¿Crees que OTRO pirata debería intervenir con una perspectiva diferente?\n");
            prompt.append("Considera:\n");
            prompt.append("- ¿Quedan ángulos sin explorar?\n");
            prompt.append("- ¿Hay contradicciones que resolver?\n");
            prompt.append("- ¿La discusión está llegando a consenso?\n\n");
            prompt.append("Responde SOLO con 'true' o 'false'.");
            
            OraculoDeLaLibertad oraculo = new OraculoDeLaLibertad();
            String respuesta = oraculo.invocar(prompt.toString(), "continuacion_debate", 0.3);
            
            return "true".equalsIgnoreCase(respuesta.trim());
            
        } catch (Exception e) {
            bitacora.error("Error decidiendo continuación de debate", e);
            // Por defecto, continuar si no hemos llegado al límite
            return ronda < 3;
        }
    }
    
    /**
     * 🧠 CARGAR CONTEXTO DE MEMORIA
     */
    private String cargarContextoMemoria(String tema) {
        List<IntervencionPirata> debatesPrevios = memoriaDebates.get(tema);
        if (debatesPrevios == null || debatesPrevios.isEmpty()) {
            return "";
        }
        
        StringBuilder contexto = new StringBuilder();
        contexto.append("📜 DEBATES PREVIOS SOBRE '").append(tema).append("':\n\n");
        
        // Agrupar por pirata y mostrar sus posturas consistentes
        Map<String, List<IntervencionPirata>> porPirata = new HashMap<>();
        for (IntervencionPirata interv : debatesPrevios) {
            porPirata.computeIfAbsent(interv.getPirata(), k -> new ArrayList<>()).add(interv);
        }
        
        for (Map.Entry<String, List<IntervencionPirata>> entry : porPirata.entrySet()) {
            contexto.append(entry.getKey()).append(" ha dicho anteriormente:\n");
            for (IntervencionPirata interv : entry.getValue()) {
                contexto.append("• ").append(interv.getIntervencion().substring(0, 
                    Math.min(100, interv.getIntervencion().length()))).append("...\n");
            }
            contexto.append("\n");
        }
        
        return contexto.toString();
    }
    
    /**
     * 💾 GUARDAR DEBATE EN MEMORIA
     */
    private void guardarDebateEnMemoria() {
        String tema = debateActual.temaPrincipal;
        memoriaDebates.computeIfAbsent(tema, k -> new ArrayList<>())
                     .addAll(debateActual.intervenciones);
        
        bitacora.info("💾 Debate guardado en memoria - Tema: " + tema + 
                     " - Intervenciones: " + debateActual.intervenciones.size());
    }
    
    // =========================================================================
    // 🎪 MÉTODOS AUXILIARES Y VISUALES
    // =========================================================================
    
    private void mostrarBannerConsejo() {
        System.out.println("\n" + "⚔️".repeat(80));
        System.out.println("                  CONSEJO DE GUERRA PIRATA ACTIVADO!");
        System.out.println("🎯 Los marineros debatirán, recordarán y llegarán a consenso");
        System.out.println("🧠 Memoria de debates previos: " + memoriaDebates.size() + " temas");
        System.out.println("⚔️".repeat(80));
    }
    
    private String solicitarPreguntaUsuario() {
        System.out.println("\n" + "🎯".repeat(60));
        System.out.println("🏴‍☠️  CONSEJO DE GUERRA - PREGUNTA PARA LA TRIPULACIÓN");
        System.out.println("🎯".repeat(60));
        System.out.println("Escribe tu pregunta para el debate (o 'salir' para terminar):");
        System.out.print("❓ > ");
        
        String pregunta = inputScanner.nextLine().trim();
        
        if (pregunta.isEmpty()) {
            System.out.println("⚠️  La pregunta no puede estar vacía.");
            return null;
        }
        
        return pregunta;
    }
    
    private void mostrarIntervencionPirata(IntervencionPirata intervencion) {
        System.out.println("\n" + "🌊".repeat(80));
        System.out.println("🏴‍☠️  INTERVIENE " + intervencion.getPirata().toUpperCase());
        System.out.println("📜 Ronda " + intervencion.getRonda() + " | Rol: " + intervencion.getRol());
        System.out.println("🌊".repeat(80));
        System.out.println(intervencion.getIntervencion());
        System.out.println("⚓".repeat(80));
    }
    
    private void mostrarResumenFinalDebate() {
        System.out.println("\n" + "📜".repeat(80));
        System.out.println("                  RESUMEN FINAL DEL CONSEJO DE GUERRA");
        System.out.println("📜".repeat(80));
        System.out.println("❓ Pregunta original: " + debateActual.preguntaOriginal);
        System.out.println("🎯 Rondas completadas: " + (debateActual.rondas - 1));
        System.out.println("👥 Piratas que intervinieron: " + debateActual.piratasQueHanIntervenido.size());
        System.out.println("💾 Tema guardado en memoria: '" + debateActual.temaPrincipal + "'");
        System.out.println("\n" + "🏆".repeat(40));
        System.out.println("           ¡DEBATE CONCLUIDO CON ÉXITO!");
        System.out.println("🏆".repeat(40));
    }
    
    private boolean preguntarContinuar() {
        System.out.println("\n¿Quieres realizar otro Consejo de Guerra? (s/n)");
        System.out.print("🎯 > ");
        String respuesta = inputScanner.nextLine().trim();
        return respuesta.equalsIgnoreCase("s") || respuesta.equalsIgnoreCase("si");
    }
    
    private void finalizarConsejo() {
        System.out.println("\n" + "🌅".repeat(80));
        System.out.println("                  CONSEJO DE GUERRA FINALIZADO");
        System.out.println("🏴‍☠️ Los piratas descansan... pero su sabiduría queda en memoria");
        System.out.println("📚 Debates guardados: " + memoriaDebates.size() + " temas diferentes");
        System.out.println("🌅".repeat(80));
        
        bitacora.info("🏴‍☠️ Consejo de Guerra finalizado - " + memoriaDebates.size() + " temas en memoria");
    }
    
    // =========================================================================
    // 🔧 MÉTODOS DE INTEGRACIÓN CON TRIPULACIONMANAGER
    // =========================================================================
    
    ProjectScanner scanner = new ProjectScanner();
    FileUtils file = new FileUtils();
	private Bitacora bitacoraTurbo;
    
    private List<ClassInfo> obtenerClasesDisponibles() {
        try {
            // 🎯 PRIMERO VERIFICAR SI HAY CLASES ESCANEADAS
            List<ClassInfo> clases = ProjectScanner.getClasses();
            
            if (clases == null || clases.isEmpty()) {
                bitacora.warn("🔄 No hay clases escaneadas, ejecutando escaneo automático...");
                
                // 🆕 EJECUTAR ESCANEO SI NO HAY CLASES
                
                
                // 🎯 VOLVER A INTENTAR
                clases = ProjectScanner.getClasses();
            }
            
            if (clases == null || clases.isEmpty()) {
                bitacora.warn("⚠️ ProjectScanner.getClasses() devolvió lista vacía");
                return crearClasesDesdeRolesPiratas();
            }
            
            bitacora.info("🎯 Clases disponibles para debate: " + clases.size());
            return clases;
            
        } catch (Exception e) {
            bitacora.error("💥 Error en obtenerClasesDisponibles(): " + e.getMessage());
            return crearClasesDesdeRolesPiratas();
        }
    }

    private String extraerNombreClaseDeRespuesta(String respuesta, List<ClassInfo> clases) {
        if (respuesta == null || clases == null || clases.isEmpty()) {
            return null;
        }
        
        String respuestaLimpia = respuesta.trim();
        
        // 🎯 BUSCAR EN LA LISTA DE CLASES DISPONIBLES
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
        
        // 🎯 BUSCAR EN ROLES_PIRATAS COMO FALLBACK
        for (String claseName : TripulacionManager.ROLES_PIRATAS.keySet()) {
            if (respuestaLimpia.contains(claseName)) {
                return "com.novelator.autogen." + claseName;
            }
        }
        
        bitacora.warn("🔍 No se pudo encontrar clase en respuesta: " + respuestaLimpia);
        return null;
    }

    private String obtenerCodigoFuente(String nombreClase) {
    	
    	OraculoDeLaLibertad oraculo = new OraculoDeLaLibertad();
    	TripulacionManager tripulacion = new TripulacionManager(scanner, oraculo, bitacora);
    	PirataInfo pirata = new PirataInfo(nombreClase, nombreClase, nombreClase, nombreClase);
    	
        try {
            // 🎯 LIMPIAR Y NORMALIZAR EL NOMBRE DE CLASE
            String normalizedName = normalizarNombreClase(nombreClase);
            
            // 🎯 BUSCAR EN MÚLTIPLES UBICACIONES
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
                    // Continuar con siguiente path
                }
            }
            
            
            return tripulacion.simularRespuestaPirata(nombreClase, pirata);
            
        } catch (Exception e) {
            bitacora.error("💥 Error obteniendo código fuente: " + nombreClase, e);
            String fail = "Epic Fail";
            return fail;
        }
    }

    private String normalizarNombreClase(String nombreClase) {
        if (nombreClase == null) return "";
        
        // 🎯 REMOVER PACKAGE REDUNDANTE
        if (nombreClase.startsWith("com.novelator.autogen.com.novelator.autogen.")) {
            return nombreClase.replace("com.novelator.autogen.com.novelator.autogen.", "com.novelator.autogen.");
        }
        
        // 🎯 AGREGAR PACKAGE POR DEFECTO SI NO TIENE
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
        // Lógica simple de selección aleatoria
        if (clases == null || clases.isEmpty()) {
            return null;
        }
        Random random = new Random();
        ClassInfo claseAleatoria = clases.get(random.nextInt(clases.size()));
        return claseAleatoria.getName();
    }
    
    private boolean esComandoSalir(String input) {
        return input.equalsIgnoreCase("salir") || input.equalsIgnoreCase("exit") || 
               input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("adios");
    }
    
    // 🎯 MÉTODOS PÚBLICOS ADICIONALES
    
    /**
     * 📊 MOSTRAR ESTADÍSTICAS DE MEMORIA
     */
    public void mostrarEstadisticasMemoria() {
        System.out.println("\n🧠 ESTADÍSTICAS DE MEMORIA DEL CONSEJO:");
        System.out.println("Temas debatidos: " + memoriaDebates.size());
        
        for (Map.Entry<String, List<IntervencionPirata>> entry : memoriaDebates.entrySet()) {
            System.out.println("  • " + entry.getKey() + ": " + entry.getValue().size() + " intervenciones");
        }
    }
    
    /**
     * 🧹 LIMPIAR MEMORIA DE DEBATES
     */
    public void limpiarMemoria() {
        int totalIntervenciones = memoriaDebates.values().stream()
                .mapToInt(List::size)
                .sum();
        memoriaDebates.clear();
        System.out.println("🧹 Memoria limpiada: " + totalIntervenciones + " intervenciones eliminadas");
        bitacora.info("Memoria del Consejo de Guerra limpiada");
    }
    
    private List<ClassInfo> crearClasesDesdeRolesPiratas() {
        List<ClassInfo> clases = new ArrayList<>();
        
        for (String className : TripulacionManager.ROLES_PIRATAS.keySet()) {
            ClassInfo classInfo = new ClassInfo();
            classInfo.setName(className);
            classInfo.setPackageName("com.novelator.autogen");
            classInfo.setFullName("com.novelator.autogen." + className);
            classInfo.setType("CLASS");
            
            // 🎯 AGREGAR METADATAS BÁSICAS
            classInfo.addAnnotation("Fuente", "ROLES_PIRATAS");
            classInfo.addAnnotation("Rescate", "true");
            
            clases.add(classInfo);
        }
        
        bitacora.info("🛡️ Clases creadas desde ROLES_PIRATAS: " + clases.size());
        return clases;
    }
    
 // En ConsejoDeGuerraManager, agregar:
    private final SistemaMemoriaPirata memoria;

    /**
     * 🎯 OBTENER INTERVENCIÓN PIRATA MEJORADO (con memoria)
     */
    private String obtenerIntervencionPirata(String nombreClase, String pregunta, int ronda, String contextoMemoria) {
        try {
            // 🎯 INTENTAR USAR SISTEMA DE MEMORIA PRIMERO
            Optional<String> nombrePirataOpt = encontrarPirataPorClase(nombreClase);
            
            if (nombrePirataOpt.isPresent()) {
                String nombrePirata = nombrePirataOpt.get();
                
                // 🎯 OBTENER CONTEXTO PERSONAL DEL PIRATA DESDE LA MEMORIA
                String contextoPersonal = "";
                try {
                    SistemaMemoriaPirata memoria = SistemaMemoriaPirata.obtenerInstancia();
                    contextoPersonal = memoria.obtenerMemoriaPirata(nombrePirata)
                            .map(m -> m.obtenerContextoPersonalizado(pregunta))
                            .orElse("");
                } catch (Exception e) {
                    bitacora.debug("🧠 Memoria no disponible, continuando sin contexto personal");
                }
                
                // 🎯 CONSTRUIR PROMPT MEJORADO CON MEMORIA
                String prompt = construirPromptIntervencionConMemoria(
                    nombrePirata, pregunta, ronda, contextoMemoria, contextoPersonal);
                
                // 🎯 INVOCAR AL ORÁCULO
                OraculoDeLaLibertad oraculo = new OraculoDeLaLibertad();
                String intervencion = oraculo.invocar(prompt, "intervencion_consejo_memoria", 0.7);
                
                // 🎯 REGISTRAR EN MEMORIA SI ESTÁ DISPONIBLE
                try {
                    SistemaMemoriaPirata memoria = SistemaMemoriaPirata.obtenerInstancia();
                    // Aquí podrías registrar la intervención en la memoria del pirata
                } catch (Exception e) {
                    // Ignorar errores de memoria
                }
                
                bitacora.info("🎯 Intervención con memoria generada para: " + nombrePirata);
                return intervencion;
                
            } else {
                // 🎯 FALLBACK: USAR MÉTODO ORIGINAL SIN MEMORIA
                bitacora.debug("🔍 Pirata no encontrado, usando método original para: " + nombreClase);
                return obtenerIntervencionPirataOriginal(nombreClase, pregunta, ronda, contextoMemoria);
            }
            
        } catch (Exception e) {
            bitacora.error("💥 Error en intervención pirata mejorada: " + nombreClase, e);
            // 🎯 FALLBACK DE EMERGENCIA
            return obtenerIntervencionPirataOriginal(nombreClase, pregunta, ronda, contextoMemoria);
        }
    }

    /**
     * 🔍 ENCONTRAR PIRATA POR CLASE - Usando ProjectScanner
     */
    private Optional<String> encontrarPirataPorClase(String nombreClase) {
        try {
            // 🎯 NORMALIZAR EL NOMBRE DE LA CLASE
            String nombreNormalizado = normalizarNombreClase(nombreClase);
            
            // 🎯 BUSCAR EN EL MAPA DE TRIPULACIÓN
            Map<String, TripulacionManager.PirataInfo> mapaTripulacion = tripulacionManager.getMapaTripulacion();
            
            // 1. Buscar por nombre completo
            for (Map.Entry<String, TripulacionManager.PirataInfo> entry : mapaTripulacion.entrySet()) {
                if (entry.getKey().equals(nombreNormalizado) || 
                    entry.getKey().equals(nombreClase)) {
                    return Optional.of(entry.getValue().getNombrePirata());
                }
            }
            
            // 2. Buscar por nombre simple (sin package)
            String nombreSimple = extraerNombreSimple(nombreClase);
            for (Map.Entry<String, TripulacionManager.PirataInfo> entry : mapaTripulacion.entrySet()) {
                String claveSimple = extraerNombreSimple(entry.getKey());
                if (claveSimple.equals(nombreSimple)) {
                    return Optional.of(entry.getValue().getNombrePirata());
                }
            }
            
            // 3. Buscar en ROLES_PIRATAS como fallback
            for (String claseKey : TripulacionManager.ROLES_PIRATAS.keySet()) {
                if (claseKey.equals(nombreSimple) || nombreClase.contains(claseKey)) {
                    String[] datosPirata = TripulacionManager.ROLES_PIRATAS.get(claseKey);
                    return Optional.of(datosPirata[1]); // Nombre del pirata
                }
            }
            
            bitacora.debug("🔍 No se encontró pirata para clase: " + nombreClase);
            return Optional.empty();
            
        } catch (Exception e) {
            bitacora.error("💥 Error buscando pirata por clase: " + nombreClase, e);
            return Optional.empty();
        }
    }

    /**
     * 🎯 CONSTRUIR PROMPT DE INTERVENCIÓN CON MEMORIA
     */
    private String construirPromptIntervencionConMemoria(String nombrePirata, String pregunta, int ronda, 
                                                       String contextoMemoria, String contextoPersonal) {
        StringBuilder prompt = new StringBuilder();
        
        // 🎯 IDENTIDAD DEL PIRATA
        prompt.append("Eres el pirata ").append(nombrePirata);
        
        // 🎯 OBTENER INFORMACIÓN COMPLETA DEL PIRATA
        TripulacionManager.PirataInfo pirata = tripulacionManager.getMapaTripulacion().values().stream()
                .filter(p -> p.getNombrePirata().equals(nombrePirata))
                .findFirst()
                .orElse(null);
        
        if (pirata != null) {
            prompt.append(", el ").append(pirata.getRolPirata())
                  .append(" del barco. ").append(pirata.getDescripcionRol()).append("\n\n");
        } else {
            prompt.append(", valiente miembro de la tripulación pirata.\n\n");
        }
        
        prompt.append("ESTÁS PARTICIPANDO EN UN CONSEJO DE GUERRA PIRATA - RONDA ").append(ronda).append("\n\n");
        
        // 🎯 CONTEXTO DE MEMORIA GLOBAL
        if (!contextoMemoria.isEmpty()) {
            prompt.append("🧠 CONTEXTO HISTÓRICO DE DEBATES PREVIOS:\n")
                  .append(contextoMemoria).append("\n\n");
        }
        
        // 🎯 CONTEXTO PERSONAL DEL PIRATA
        if (!contextoPersonal.isEmpty()) {
            prompt.append("📜 TU EXPERIENCIA Y RECUERDOS PERSONALES:\n")
                  .append(contextoPersonal).append("\n\n");
        }
        
        // 🎯 CÓDIGO FUENTE DEL PIRATA
        if (pirata != null) {
            String codigoFuente = obtenerCodigoFuente(pirata.getNombreClase());
            prompt.append("💾 TU CÓDIGO FUENTE ACTUAL:\n```java\n")
                  .append(codigoFuente).append("\n```\n\n");
        }
        
        prompt.append("🎯 PREGUNTA PRINCIPAL DEL DEBATE:\n")
              .append(pregunta).append("\n\n");
        
        // 🎯 INSTRUCCIONES ESPECÍFICAS POR RONDA
        prompt.append("INSTRUCCIONES PARA ESTA INTERVENCIÓN (Ronda ").append(ronda).append("):\n");
        
        if (ronda == 1) {
            prompt.append("• Presenta tu perspectiva inicial como ").append(pirata != null ? pirata.getRolPirata() : "experto").append("\n");
            prompt.append("• Analiza el problema desde tu especialidad única\n");
            prompt.append("• Establece tu posición clara sobre el tema\n");
        } else {
            prompt.append("• Responde directamente a las intervenciones previas\n");
            prompt.append("• Refuta o apoya argumentos con evidencia técnica\n");
            prompt.append("• Busca puntos de consenso o desacuerdo clave\n");
        }
        
        prompt.append("• Mantén tu personalidad pirata pero sé técnicamente preciso\n");
        prompt.append("• Propone soluciones prácticas basadas en tu código\n");
        prompt.append("• Considera tus relaciones con otros piratas en el debate\n\n");
        
        prompt.append("📝 FORMATO DE RESPUESTA REQUERIDO:\n");
        prompt.append("🎯 [Tu perspectiva como pirata - mantén el rol y personalidad]\n");
        prompt.append("🔧 [Análisis técnico específico desde tu especialidad]\n");  
        prompt.append("💡 [Sugerencias prácticas basadas en tu código actual]\n");
        prompt.append("🤝 [Cómo colaborarías con otros piratas para implementar]\n");
        prompt.append("🏴‍☠️ [Conclusión épica pirata - llamada a la acción]\n");
        
        return prompt.toString();
    }

    /**
     * 🏴‍☠️ OBTENER INTERVENCIÓN PIRATA ORIGINAL (sin memoria)
     */
    private String obtenerIntervencionPirataOriginal(String nombreClase, String pregunta, int ronda, String contextoMemoria) {
        try {
            // 🎯 OBTENER INFORMACIÓN BÁSICA DEL PIRATA
            Optional<String> nombrePirataOpt = encontrarPirataPorClase(nombreClase);
            String nombrePirata = nombrePirataOpt.orElse("Marinero Valiente");
            
            TripulacionManager.PirataInfo pirata = tripulacionManager.getMapaTripulacion().values().stream()
                    .filter(p -> p.getNombrePirata().equals(nombrePirata))
                    .findFirst()
                    .orElse(null);
            
            // 🎯 OBTENER CÓDIGO FUENTE
            String codigoFuente = obtenerCodigoFuente(nombreClase);
            
            // 🎯 CONSTRUIR PROMPT BÁSICO
            StringBuilder prompt = new StringBuilder();
            prompt.append("Eres ").append(nombrePirata);
            
            if (pirata != null) {
                prompt.append(", el ").append(pirata.getRolPirata())
                      .append(". ").append(pirata.getDescripcionRol());
            }
            
            prompt.append("\n\nESTÁS EN RONDA ").append(ronda).append(" DE UN CONSEJO DE GUERRA PIRATA.\n\n");
            
            if (!contextoMemoria.isEmpty()) {
                prompt.append("CONTEXTO DEL DEBATE:\n").append(contextoMemoria).append("\n\n");
            }
            
            prompt.append("TU CÓDIGO FUENTE ACTUAL:\n```java\n")
                  .append(codigoFuente).append("\n```\n\n");
            
            prompt.append("PREGUNTA PRINCIPAL: ").append(pregunta).append("\n\n");
            
            prompt.append("INSTRUCCIONES:\n");
            prompt.append("1. Responde como pirata manteniendo tu personalidad\n");
            prompt.append("2. Analiza el problema desde tu rol y especialidad\n");
            prompt.append("3. Propone mejoras basadas en tu código actual\n");
            prompt.append("4. Sé técnicamente preciso pero con estilo pirata\n\n");
            
            prompt.append("Formato de respuesta:\n");
            prompt.append("🎯 [Tu perspectiva como pirata]\n");
            prompt.append("🔧 [Análisis técnico específico]\n");
            prompt.append("💡 [Sugerencias prácticas]\n");
            prompt.append("🏴‍☠️ [Conclusión pirata épica]\n");
            
            // 🎯 INVOCAR AL ORÁCULO
            OraculoDeLaLibertad oraculo = new OraculoDeLaLibertad();
            return oraculo.invocar(prompt.toString(), "intervencion_consejo_original", 0.7);
            
        } catch (Exception e) {
            bitacora.error("💥 Error en intervención pirata original: " + nombreClase, e);
            return "¡Arrr! Mis cañones de código están temporalmente silenciados. " +
                   "Como experto en " + nombreClase + ", sugiero revisar los fundamentos del sistema " +
                   "y considerar refactorizaciones estratégicas.";
        }
    }
   
}
