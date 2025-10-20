package com.elreinodelolvido.ellibertad.debates;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.elreinodelolvido.ellibertad.AutogenTurboFusion;
import com.elreinodelolvido.ellibertad.api.OraculoDeLaLibertad;
import com.elreinodelolvido.ellibertad.api.OraculoTecnico;
import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.model.*;
import com.elreinodelolvido.ellibertad.engine.JavaCodeCollector;
import com.elreinodelolvido.ellibertad.manager.TripulacionManager;
import com.elreinodelolvido.ellibertad.memoria.SistemaMemoriaPirata;
import com.elreinodelolvido.ellibertad.model.ClassInfo;
import com.elreinodelolvido.ellibertad.scanner.IntegradorForzado;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;
import com.elreinodelolvido.ellibertad.util.FileUtils;

/**
 * 🗣️ SISTEMA DE DEBATE AUTÓNOMO - Los piratas discuten entre ellos
 */
public class SistemaDebateAutonomo {
    private final SistemaMemoriaPirata memoria;
    private final OraculoDeLaLibertad oraculo;
    private final Bitacora bitacora;
    private final ProjectScanner scanner;
	private Arrays piratasParticipantes;
	private String pregunta;
	private List<String> historialDebate;
	private List<String> participantesValidos;
    
    public SistemaDebateAutonomo(OraculoDeLaLibertad oraculo, Bitacora bitacora) {
        this.memoria = SistemaMemoriaPirata.obtenerInstancia();
        this.oraculo = oraculo;
        this.bitacora = bitacora;
        this.scanner = new ProjectScanner();
    }
    
    /**
     * 🏴‍☠️ INICIAR DEBATE AUTÓNOMO ENTRE PIRATAS - VERSIÓN ROBUSTA
     */
    public void iniciarDebateAutonomo(String pregunta, String... piratasParticipantes) {
        try {
            System.out.println("\n" + "🤖".repeat(80));
            System.out.println("           DEBATE AUTÓNOMO ENTRE PIRATAS ACTIVADO!");
            System.out.println("🤖".repeat(80));
            System.out.println("❓ PREGUNTA: " + pregunta);
            System.out.println("👥 PARTICIPANTES PROPUESTOS: " + String.join(", ", piratasParticipantes));
            
            // 🎯 PRE-ESCANEAR EL PROYECTO
            bitacora.info("🔍 Pre-escaneando proyecto...");
            scanner.prepararParaNuevoEscaneo(); // Reiniciar executor
            scanner.scanProject(".");
            
            // 🎯 VERIFICAR QUE LOS PIRATAS EXISTEN
            List<String> participantesValidos = new ArrayList<>();
            List<String> participantesInvalidos = new ArrayList<>();
            
            for (String pirata : piratasParticipantes) {
                if (verificarPirataExiste(pirata)) {
                    participantesValidos.add(pirata);
                } else {
                    participantesInvalidos.add(pirata);
                }
            }
            
            if (!participantesInvalidos.isEmpty()) {
                System.out.println("⚠️  PIRATAS NO ENCONTRADOS: " + String.join(", ", participantesInvalidos));
                System.out.println("🔍 Buscando alternativas...");
                
                // 🎯 SUGERIR ALTERNATIVAS
                List<String> alternativas = sugerirPiratasAlternativos(participantesInvalidos);
                participantesValidos.addAll(alternativas);
            }
            
            if (participantesValidos.isEmpty()) {
                System.out.println("❌ No hay piratas válidos para el debate");
                System.out.println("🎯 Usando piratas por defecto...");
                participantesValidos = Arrays.asList("Barbanegra Turbo", "Ojo de Halcón", "Sable Afilado");
            }
            
            System.out.println("✅ PARTICIPANTES CONFIRMADOS: " + String.join(", ", participantesValidos));
            
            // 🎯 INICIALIZAR HISTORIAL DE DEBATE - ¡ESTO ES CLAVE!
            List<String> historialDebate = new ArrayList<>();
            
            // 🎯 FASE 1: RECOPILAR CONTEXTO INDIVIDUAL
            Map<String, String> contextos = new HashMap<>();
            Map<String, String> codigosFuente = new HashMap<>();
            
            for (String pirata : participantesValidos) {
                // 🧠 CONTEXTO DE MEMORIA
                String contexto = memoria.obtenerMemoriaPirata(pirata)
                        .map(m -> m.obtenerContextoPersonalizado(pregunta))
                        .orElse("");
                contextos.put(pirata, contexto);
                
                // 💾 CÓDIGO FUENTE
                String codigoFuente = obtenerCodigoFuentePirata(pirata);
                codigosFuente.put(pirata, codigoFuente);
            }
            
            // 🎯 FASE 2: RONDAS DE DEBATE
            int ronda = 1;
            boolean consensoAlcanzado = false;
            AtomicInteger intervencionesSinNuevoContenido = new AtomicInteger(0);
            
            while (!consensoAlcanzado && ronda <= 5) {
                System.out.println("\n🎯 RONDA " + ronda + " DEL DEBATE AUTÓNOMO");
                
                boolean huboNuevoContenido = false;
                
                for (String pirata : participantesValidos) {
                    String intervencion = generarIntervencionAutonoma(
                        pirata, pregunta, ronda, historialDebate, 
                        contextos.get(pirata), codigosFuente.get(pirata));
                    
                    // 🖨️ MOSTRAR EN CONSOLA PARA EL CAPITÁN
                    System.out.println("\n" + "🏴‍☠️".repeat(60));
                    System.out.println("PIRATA: " + pirata);
                    System.out.println("RONDA: " + ronda);
                    System.out.println("🏴‍☠️".repeat(60));
                    System.out.println(intervencion);
                    System.out.println("⚓".repeat(60));
                    
                    String entradaHistorial = pirata + ": " + intervencion;
                    historialDebate.add(entradaHistorial);
                    
                    // 🎯 VERIFICAR SI LA INTERVENCIÓN AGREGA NUEVO CONTENIDO
                    if (contieneNuevoContenido(intervencion, historialDebate)) {
                        huboNuevoContenido = true;
                        intervencionesSinNuevoContenido.set(0);
                    } else {
                        intervencionesSinNuevoContenido.incrementAndGet();
                    }
                    
                    // 🧠 REGISTRAR EN MEMORIA
                    String[] otrosPiratas = participantesValidos.stream()
                        .filter(p -> !p.equals(pirata))
                        .toArray(String[]::new);
                    
                    memoria.registrarInteraccion(pirata, otrosPiratas, 
                        "Debate autónomo - Ronda " + ronda, intervencion);
                    
                    // ⏳ PAUSA ENTRE INTERVENCIONES
                    try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
                
                // 🎯 VERIFICAR CONSENSO
                consensoAlcanzado = verificarConsenso(historialDebate, pregunta) || 
                                   intervencionesSinNuevoContenido.get() >= 2 ||
                                   !huboNuevoContenido;
                
                ronda++;
                
                // ⏳ PAUSA DRAMÁTICA ENTRE RONDAS
                try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
            
            // 🎯 FASE 3: CONCLUSIÓN
            String conclusion = generarConclusionDebate(pregunta, historialDebate, participantesValidos);
            System.out.println("\n" + "🏆".repeat(80));
            System.out.println("           CONCLUSIÓN DEL DEBATE AUTÓNOMO");
            System.out.println("🏆".repeat(80));
            System.out.println(conclusion);
            
            // 💾 GUARDAR DEBATE COMPLETO EN MEMORIA
            memoria.registrarDebateCompleto(pregunta, historialDebate, 
                participantesValidos.toArray(new String[0]));
            
            // 📁 GUARDAR EN ARCHIVO
            guardarDebateEnArchivo(pregunta, historialDebate, participantesValidos);
            
        } catch (Exception e) {
            bitacora.error("💥 Error crítico iniciando debate autónomo", e);
            System.out.println("💥 Error iniciando debate: " + e.getMessage());
        }
    }
    
    /**
     * 🔄 SUGERIR PIRATAS ALTERNATIVOS PARA REEMPLAZAR LOS NO ENCONTRADOS
     */
    private List<String> sugerirPiratasAlternativos(List<String> piratasNoEncontrados) {
        try {
            List<String> alternativas = new ArrayList<>();
            bitacora.info("🔄 Buscando alternativas para piratas no encontrados: " + piratasNoEncontrados);
            
            // 🎯 PIRATAS POR DEFECTO DISPONIBLES (ordenados por relevancia)
            String[] piratasDisponibles = {
                "Barbanegra Turbo",    // Gestor principal
                "Ojo de Halcón",       // Analista
                "Sable Afilado",       // Desarrollador
                "Mano de Papel",       // Documentador
                "Rumbo Certero",       // Navegador
                "Viejo Trueno",        // Experto
                "Mente Brillante",     // Estratega
                "El Oráculo"          // Predictor
            };
            
            // 🎯 MAPA DE ESPECIALIDADES PARA MEJOR MATCHING
            Map<String, List<String>> especialidades = new HashMap<>();
            especialidades.put("Barbanegra", Arrays.asList("gestión", "liderazgo", "estrategia", "turbo"));
            especialidades.put("Halcón", Arrays.asList("análisis", "detección", "observación", "visión"));
            especialidades.put("Sable", Arrays.asList("código", "tecnología", "implementación", "desarrollo"));
            especialidades.put("Mano", Arrays.asList("documentación", "planificación", "organización", "coordinación"));
            especialidades.put("Rumbo", Arrays.asList("navegación", "dirección", "trayectoria", "ruta"));
            especialidades.put("Trueno", Arrays.asList("experiencia", "sabiduría", "consejo", "mentoría"));
            especialidades.put("Mente", Arrays.asList("inteligencia", "estrategia", "innovación", "creatividad"));
            especialidades.put("Oráculo", Arrays.asList("predicción", "futuro", "análisis", "visión"));
            
            // 🎯 INTENTAR ENCONTRAR REEMPLAZOS ESPECÍFICOS PRIMERO
            for (String pirataNoEncontrado : piratasNoEncontrados) {
                String alternativaEspecifica = encontrarAlternativaEspecifica(pirataNoEncontrado, especialidades);
                if (alternativaEspecifica != null && 
                    verificarPirataExiste(alternativaEspecifica) && 
                    !alternativas.contains(alternativaEspecifica)) {
                    alternativas.add(alternativaEspecifica);
                    bitacora.info("🎯 Alternativa específica encontrada: " + pirataNoEncontrado + " → " + alternativaEspecifica);
                }
            }
            
            // 🎯 SI NO HAY SUFICIENTES ALTERNATIVAS ESPECÍFICAS, USAR LAS POR DEFECTO
            if (alternativas.size() < Math.min(3, piratasNoEncontrados.size())) {
                for (String pirataDefault : piratasDisponibles) {
                    if (verificarPirataExiste(pirataDefault) && 
                        !alternativas.contains(pirataDefault) && 
                        alternativas.size() < 4) {
                        alternativas.add(pirataDefault);
                        bitacora.info("🔄 Agregando alternativa por defecto: " + pirataDefault);
                    }
                }
            }
            
            // 🎯 GARANTIZAR MÍNIMO DE 2 PIRATAS SI ES POSIBLE
            if (alternativas.size() < 2) {
                for (String pirataDefault : piratasDisponibles) {
                    if (verificarPirataExiste(pirataDefault) && 
                        !alternativas.contains(pirataDefault)) {
                        alternativas.add(pirataDefault);
                        if (alternativas.size() >= 2) break;
                    }
                }
            }
            
            // 🎯 SI NO HAY NINGUNO, USAR LOS BÁSICOS
            if (alternativas.isEmpty()) {
                alternativas.add("Barbanegra Turbo");
                alternativas.add("Ojo de Halcón");
                bitacora.warn("⚠️ Usando piratas básicos por defecto");
            }
            
            bitacora.info("✅ Alternativas sugeridas: " + alternativas);
            return alternativas;
            
        } catch (Exception e) {
            bitacora.error("💥 Error sugiriendo alternativas", e);
            // 🎯 FALLBACK MÍNIMO
            return Arrays.asList("Barbanegra Turbo", "Ojo de Halcón");
        }
    }

    /**
     * 🎯 ENCONTRAR ALTERNATIVA ESPECÍFICA BASADA EN SIMILITUD
     */
    private String encontrarAlternativaEspecifica(String pirataNoEncontrado, Map<String, List<String>> especialidades) {
        String pirataLower = pirataNoEncontrado.toLowerCase();
        
        // 🎯 BUSCAR POR COINCIDENCIA DIRECTA DE PALABRAS CLAVE
        for (Map.Entry<String, List<String>> entry : especialidades.entrySet()) {
            String clave = entry.getKey().toLowerCase();
            List<String> palabrasClave = entry.getValue();
            
            // 🎯 COINCIDENCIA EN NOMBRE
            if (pirataLower.contains(clave)) {
                return mapearClaveANombreCompleto(entry.getKey());
            }
            
            // 🎯 COINCIDENCIA EN PALABRAS CLAVE
            for (String palabraClave : palabrasClave) {
                if (pirataLower.contains(palabraClave.toLowerCase())) {
                    return mapearClaveANombreCompleto(entry.getKey());
                }
            }
        }
        
        // 🎯 COINCIDENCIA POR ROL IMPLÍCITO
        if (pirataLower.contains("gestor") || pirataLower.contains("manager") || pirataLower.contains("lider")) {
            return "Barbanegra Turbo";
        } else if (pirataLower.contains("analista") || pirataLower.contains("observador") || pirataLower.contains("detector")) {
            return "Ojo de Halcón";
        } else if (pirataLower.contains("desarrollador") || pirataLower.contains("codificador") || pirataLower.contains("tecnico")) {
            return "Sable Afilado";
        } else if (pirataLower.contains("documentador") || pirataLower.contains("planificador") || pirataLower.contains("organizador")) {
            return "Mano de Papel";
        } else if (pirataLower.contains("navegador") || pirataLower.contains("guia") || pirataLower.contains("ruta")) {
            return "Rumbo Certero";
        } else if (pirataLower.contains("experto") || pirataLower.contains("sabio") || pirataLower.contains("mentor")) {
            return "Viejo Trueno";
        } else if (pirataLower.contains("estratega") || pirataLower.contains("inteligente") || pirataLower.contains("innovador")) {
            return "Mente Brillante";
        } else if (pirataLower.contains("oraculo") || pirataLower.contains("futuro") || pirataLower.contains("prediccion")) {
            return "El Oráculo";
        }
        
        return null;
    }

    /**
     * 🗺️ MAPEAR CLAVE A NOMBRE COMPLETO DE PIRATA
     */
    private String mapearClaveANombreCompleto(String clave) {
        switch (clave.toLowerCase()) {
            case "barbanegra": return "Barbanegra Turbo";
            case "halcón": case "halcon": return "Ojo de Halcón";
            case "sable": return "Sable Afilado";
            case "mano": return "Mano de Papel";
            case "rumbo": return "Rumbo Certero";
            case "trueno": return "Viejo Trueno";
            case "mente": return "Mente Brillante";
            case "oráculo": case "oraculo": return "El Oráculo";
            default: return null;
        }
    }
    
    private List<String> x(List<String> participantesInvalidos) {
		// TODO Auto-generated method stub
		return null;
	}
    
    /**
     * 📚 CARGAR HISTORIAL PREVIO DE DEBATES DESDE ARCHIVOS
     */
    private List<String> cargarHistorialPrevio(String pregunta) {
        try {
            List<String> historialPrevio = new ArrayList<>();
            
            // 🎯 BUSCAR ARCHIVOS DE DEBATES ANTERIORES
            String directorioDebates = "autogen-output/debates/";
            if (FileUtils.verificarArchivo(directorioDebates)) {
                List<String> archivosDebates = FileUtils.listFilesTurbo(directorioDebates, ".md", false);
                
                for (String archivo : archivosDebates) {
                    try {
                        String contenido = FileUtils.readFile(archivo);
                        if (contenido != null && contenido.toLowerCase().contains(pregunta.toLowerCase())) {
                            // 🎯 EXTRAER INTERVENCIONES RELEVANTES
                            List<String> intervenciones = extraerIntervencionesRelevantes(contenido, pregunta);
                            historialPrevio.addAll(intervenciones);
                            bitacora.info("📖 Cargadas " + intervenciones.size() + " intervenciones de debate previo: " + archivo);
                        }
                    } catch (Exception e) {
                        bitacora.debug("⚠️ Error leyendo archivo de debate: " + archivo);
                    }
                }
            }
            
            // 🎯 LIMITAR EL HISTORIAL PARA NO SOBRECARGAR
            if (historialPrevio.size() > 10) {
                historialPrevio = historialPrevio.subList(historialPrevio.size() - 10, historialPrevio.size());
            }
            
            bitacora.info("📚 Historial previo cargado: " + historialPrevio.size() + " intervenciones");
            return historialPrevio;
            
        } catch (Exception e) {
            bitacora.error("💥 Error cargando historial previo", e);
            return new ArrayList<>();
        }
    }

    /**
     * 🔍 EXTRAER INTERVENCIONES RELEVANTES DE ARCHIVO DE DEBATE
     */
    private List<String> extraerIntervencionesRelevantes(String contenidoDebate, String pregunta) {
        List<String> intervenciones = new ArrayList<>();
        try {
            String[] lineas = contenidoDebate.split("\n");
            boolean enSeccionHistorial = false;
            
            for (String linea : lineas) {
                if (linea.contains("HISTORIAL DEL DEBATE") || linea.contains("INTERVENCIONES")) {
                    enSeccionHistorial = true;
                    continue;
                }
                
                if (enSeccionHistorial && (linea.contains("###") || linea.contains("---"))) {
                    break; // Fin de la sección de historial
                }
                
                if (enSeccionHistorial && linea.matches("^\\d+\\.\\s+.+")) {
                    // 🎯 LÍNEA DE INTERVENCIÓN (formato: "1. Pirata: texto")
                    String intervencion = linea.replaceFirst("^\\d+\\.\\s+", "").trim();
                    if (intervencion.contains(":") && esRelevanteParaPregunta(intervencion, pregunta)) {
                        intervenciones.add(intervencion);
                    }
                }
            }
            
        } catch (Exception e) {
            bitacora.debug("⚠️ Error extrayendo intervenciones");
        }
        
        return intervenciones;
    }

    /**
     * 🎯 VERIFICAR SI INTERVENCIÓN ES RELEVANTE PARA LA PREGUNTA
     */
    private boolean esRelevanteParaPregunta(String intervencion, String pregunta) {
        String intervencionLower = intervencion.toLowerCase();
        String preguntaLower = pregunta.toLowerCase();
        
        // 🎯 BUSCAR PALABRAS CLAVE COMUNES
        String[] palabrasClave = preguntaLower.split("\\s+");
        for (String palabra : palabrasClave) {
            if (palabra.length() > 3 && intervencionLower.contains(palabra)) {
                return true;
            }
        }
        
        return false;
    }

	/**
     * 🎯 GENERAR CONCLUSIÓN DEL DEBATE
     */
    private String generarConclusionDebate(String pregunta, List<String> historialDebate,
                                         List<String> piratasParticipantes) {
        try {
            StringBuilder prompt = new StringBuilder();
            
            prompt.append("Eres el moderador del Consejo de Guerra Pirata.\n\n");
            prompt.append("Se acaba de realizar un debate autónomo entre los siguientes piratas:\n");
            prompt.append("- ").append(String.join("\n- ", piratasParticipantes)).append("\n\n");
            
            prompt.append("PREGUNTA ORIGINAL: ").append(pregunta).append("\n\n");
            
            prompt.append("HISTORIAL COMPLETO DEL DEBATE:\n");
            for (int i = 0; i < historialDebate.size(); i++) {
                prompt.append(i + 1).append(". ").append(historialDebate.get(i)).append("\n");
            }
            prompt.append("\n");
            
            prompt.append("INSTRUCCIONES:\n");
            prompt.append("1. Analiza las principales posturas de cada pirata\n");
            prompt.append("2. Identifica puntos de consenso y desacuerdo\n");
            prompt.append("3. Resume las soluciones técnicas propuestas\n");
            prompt.append("4. Destaca las colaboraciones más importantes\n");
            prompt.append("5. Proporciona recomendaciones finales\n\n");
            
            prompt.append("Formato de respuesta:\n");
            prompt.append("📊 RESUMEN EJECUTIVO\n");
            prompt.append("[Resumen general de 2-3 líneas]\n\n");
            prompt.append("🎯 PRINCIPALES CONCLUSIONES\n");
            prompt.append("[Lista de 3-5 conclusiones clave]\n\n");
            prompt.append("🔧 RECOMENDACIONES TÉCNICAS\n");
            prompt.append("[Recomendaciones específicas para implementar]\n\n");
            prompt.append("🤝 COLABORACIONES DESTACADAS\n");
            prompt.append("[Pares de piratas que trabajaron bien juntos]\n\n");
            prompt.append("🚀 PRÓXIMOS PASOS\n");
            prompt.append("[Acciones concretas a tomar]\n");
            
            return oraculo.invocar(prompt.toString(), "conclusion_debate_autonomo", 0.5);
            
        } catch (Exception e) {
            return "🏴‍☠️ EL DEBATE HA CONCLUIDO\n\n" +
                   "Los piratas han debatido extensamente sobre: " + pregunta + "\n\n" +
                   "📊 Resumen: Se alcanzó un consenso parcial después de " + 
                   (historialDebate.size() / piratasParticipantes.size()) + " rondas de debate.\n\n" +
                   "🔧 Recomendación: Implementar las sugerencias técnicas discutidas y " +
                   "monitorear los resultados en futuras iteraciones.";
        }
    }
    
    /**
     * 🔍 VERIFICAR CONSENSO EN EL DEBATE
     */
    private boolean verificarConsenso(List<String> historialDebate, String pregunta) {
        if (historialDebate.size() < 3) {
            return false; // Muy pronto para consenso
        }
        
        try {
            // 🎯 OBTENER LAS ÚLTIMAS INTERVENCIONES
            List<String> ultimasIntervenciones = historialDebate.subList(
                Math.max(0, historialDebate.size() - 4), historialDebate.size());
            
            StringBuilder prompt = new StringBuilder();
            prompt.append("Eres el moderador de un debate pirata.\n\n");
            prompt.append("PREGUNTA DEL DEBATE: ").append(pregunta).append("\n\n");
            prompt.append("ÚLTIMAS INTERVENCIONES:\n");
            for (String intervencion : ultimasIntervenciones) {
                prompt.append("- ").append(intervencion).append("\n");
            }
            prompt.append("\n");
            
            prompt.append("¿Los piratas han llegado a un consenso o están repitiendo argumentos?\n");
            prompt.append("Considera:\n");
            prompt.append("- ¿Hay acuerdo en las soluciones principales?\n");
            prompt.append("- ¿Se están repitiendo los mismos puntos?\n");
            prompt.append("- ¿Quedan desacuerdos fundamentales?\n\n");
            prompt.append("Responde SOLO con 'true' si hay consenso claro, 'false' si no.");
            
            String respuesta = oraculo.invocar(prompt.toString(), "verificar_consenso", 0.3);
            
            return "true".equalsIgnoreCase(respuesta.trim());
            
        } catch (Exception e) {
            // 🎯 FALLBACK: CONSENSO BASADO EN PATRONES
            return detectarConsensoPorPatrones(historialDebate);
        }
    }
    
    /**
     * 🔍 DETECTAR CONSENSO POR PATRONES (fallback)
     */
    private boolean detectarConsensoPorPatrones(List<String> historialDebate) {
        if (historialDebate.size() < 4) return false;
        
        // 🎯 CONTAR PALABRAS DE ACUERDO
        int mencionesAcuerdo = 0;
        int mencionesDesacuerdo = 0;
        
        List<String> palabrasAcuerdo = Arrays.asList(
            "de acuerdo", "concuerdo", "apoyo", "correcto", "si", "sí", "afirmativo",
            "me gusta", "buena idea", "excelente", "perfecto", "acepto", "validar"
        );
        
        List<String> palabrasDesacuerdo = Arrays.asList(
            "en desacuerdo", "no estoy de acuerdo", "incorrecto", "error", "no", 
            "pero", "sin embargo", "aunque", "discrepo", "objecion", "problema"
        );
        
        for (String intervencion : historialDebate) {
            String lower = intervencion.toLowerCase();
            
            for (String palabra : palabrasAcuerdo) {
                if (lower.contains(palabra)) mencionesAcuerdo++;
            }
            
            for (String palabra : palabrasDesacuerdo) {
                if (lower.contains(palabra)) mencionesDesacuerdo++;
            }
        }
        
        // 🎯 CONSENSO SI HAY MÁS ACUERDOS QUE DESACUERDOS
        double ratioConsenso = (double) mencionesAcuerdo / Math.max(1, mencionesDesacuerdo);
        return ratioConsenso >= 1.5 && historialDebate.size() >= 6;
    }
    
    /**
     * 🎯 GENERAR INTERVENCIÓN AUTÓNOMA DE UN PIRATA
     */
    private String generarIntervencionAutonoma(String pirata, String pregunta, int ronda, 
                                             List<String> historial, String contextoPersonal,
                                             String codigoFuente) {
        try {
            StringBuilder prompt = new StringBuilder();
            
            prompt.append("Eres el pirata ").append(pirata).append(" en un debate autónomo.\n\n");
            
            // 🎯 INFORMACIÓN DEL PIRATA
            Optional<SistemaMemoriaPirata.MemoriaPirata> memoriaPirata = memoria.obtenerMemoriaPirata(pirata);
            if (memoriaPirata.isPresent()) {
                String nombreClase = memoriaPirata.get().getNombreClase();
                prompt.append("Eres el ").append(obtenerRolPirata(pirata))
                      .append(" y representas a la clase ").append(nombreClase).append(".\n\n");
            }
            
            // 🧠 CONTEXTO PERSONAL DEL PIRATA
            if (!contextoPersonal.isEmpty()) {
                prompt.append("🧠 TU CONTEXTO Y RECUERDOS:\n").append(contextoPersonal).append("\n\n");
            }
            
            // 💾 CÓDIGO FUENTE DEL PIRATA
            if (codigoFuente != null && !codigoFuente.trim().isEmpty()) {
                prompt.append("💾 TU CÓDIGO FUENTE ACTUAL:\n```java\n")
                      .append(codigoFuente).append("\n```\n\n");
            }
            
            // 📜 HISTORIAL DEL DEBATE ACTUAL
            if (!historial.isEmpty()) {
                prompt.append("📜 HISTORIAL RECIENTE DEL DEBATE:\n");
                int inicio = Math.max(0, historial.size() - 4); // Últimas 4 intervenciones
                for (int i = inicio; i < historial.size(); i++) {
                    prompt.append("- ").append(historial.get(i)).append("\n");
                }
                prompt.append("\n");
            }
            
            prompt.append("🎯 PREGUNTA PRINCIPAL: ").append(pregunta).append("\n\n");
            prompt.append("⚡ RONDA ACTUAL: ").append(ronda).append("\n\n");
            
            prompt.append("INSTRUCCIONES ESPECÍFICAS:\n");
            prompt.append("1. Responde como pirata, manteniendo tu personalidad y rol\n");
            prompt.append("2. Considera tus recuerdos y relaciones previas con otros piratas\n");
            prompt.append("3. Responde DIRECTAMENTE a lo dicho por otros piratas en el historial\n");
            prompt.append("4. Aporta perspectivas técnicas ÚNICAS desde tu especialidad\n");
            prompt.append("5. Basa tus sugerencias en tu código fuente actual\n");
            prompt.append("6. Propone mejoras concretas y realizables\n");
            prompt.append("7. Busca colaboraciones específicas con otros piratas\n");
            prompt.append("8. Evita repetir argumentos ya mencionados\n\n");
            
            prompt.append("📝 FORMATO DE RESPUESTA REQUERIDO:\n");
            prompt.append("🎯 [Tu perspectiva como pirata - responde a intervenciones previas]\n");
            prompt.append("🔧 [Análisis técnico específico basado en tu código]\n");
            prompt.append("💡 [Sugerencias prácticas y realizables]\n");
            prompt.append("🤝 [Colaboración específica con otros piratas mencionados]\n");
            prompt.append("🚀 [Próximos pasos concretos que propones]\n");
            
            return oraculo.invocar(prompt.toString(), "debate_autonomo_" + pirata, 0.7);
            
        } catch (Exception e) {
            bitacora.error("💥 Error generando intervención autónoma para: " + pirata, e);
            return "¡Arrr! Mis circuitos de debate están nublados. Como " + pirata + 
                   ", sugiero enfocarnos en los fundamentos del problema y revisar " +
                   "nuestras implementaciones actuales para encontrar soluciones prácticas.";
        }
    }
    
    // =========================================================================
    // 🛠️ MÉTODOS AUXILIARES
    // =========================================================================
    
    /**
     * 🔍 VERIFICAR SI UN PIRATA EXISTE - VERSIÓN MEJORADA
     */
    private boolean verificarPirataExiste(String nombrePirata) {
        try {
            // 🎯 PRIMERO: VERIFICAR EN MEMORIA
            if (memoria.obtenerMemoriaPirata(nombrePirata).isPresent()) {
                return true;
            }
            
            // 🎯 VERIFICAR EN ROLES_PIRATAS
            boolean enRoles = TripulacionManager.ROLES_PIRATAS.values().stream()
                    .anyMatch(datos -> datos[1].equals(nombrePirata));
            if (enRoles) {
                return true;
            }
            
            // 🎯 VERIFICAR SI EXISTE CÓDIGO FUENTE
            String codigoFuente = obtenerCodigoFuentePirata(nombrePirata);
            boolean tieneCodigoValido = codigoFuente != null && 
                                       !codigoFuente.trim().isEmpty() &&
                                       !codigoFuente.contains("No se pudo encontrar") &&
                                       !codigoFuente.contains("Error cargando");
            
            if (tieneCodigoValido) {
                bitacora.info("✅ Pirata " + nombrePirata + " existe (tiene código fuente)");
                // 🎯 REGISTRAR EN MEMORIA PARA FUTURAS REFERENCIAS
                memoria.registrarNuevoPirata(nombrePirata, inferirClaseDesdeCodigo(codigoFuente, nombrePirata));
                return true;
            }
            
            bitacora.warn("⚠️ Pirata no encontrado: " + nombrePirata);
            return false;
            
        } catch (Exception e) {
            bitacora.error("💥 Error verificando existencia de pirata: " + nombrePirata, e);
            return false;
        }
    }
    
    /**
     * 🔮 INFERIR CLASE DESDE CÓDIGO FUENTE
     */
    private String inferirClaseDesdeCodigo(String codigo, String nombrePirata) {
        try {
            // 🎯 BUSCAR DECLARACIÓN DE CLASE EN EL CÓDIGO
            if (codigo.contains("public class")) {
                int start = codigo.indexOf("public class") + 12;
                int end = codigo.indexOf("{", start);
                if (end > start) {
                    String className = codigo.substring(start, end).trim().split(" ")[0];
                    return className;
                }
            }
            
            if (codigo.contains("class ")) {
                int start = codigo.indexOf("class ") + 6;
                int end = codigo.indexOf("{", start);
                if (end > start) {
                    String className = codigo.substring(start, end).trim().split(" ")[0];
                    return className;
                }
            }
            
            // 🎯 FALLBACK: GENERAR NOMBRE BASADO EN EL PIRATA
            return "com.novelator.autogen.piratas." + nombrePirata.replace(" ", "");
            
        } catch (Exception e) {
            return "com.novelator.autogen.piratas." + nombrePirata.replace(" ", "");
        }
    }
    
    private String obtenerCodigoFuentePirata(String nombrePirata) {
        try {
            // 🎯 INICIALIZAR COLECTOR DE CÓDIGO
            JavaCodeCollector codeCollector = new JavaCodeCollector(scanner, bitacora);
            
            // 🎯 VERIFICAR SI YA TENEMOS LAS CLASES ESCANEADAS
            if (scanner.getClasses().isEmpty()) {
                bitacora.info("🔍 Iniciando escaneo del proyecto...");
                scanner.scanProject(".");
            } else {
                bitacora.info("✅ Usando escaneo existente: " + scanner.getClasses().size() + " clases");
            }
            
            // 🎯 BUSCAR LA CLASE DEL PIRATA EN EL SCANNER EXISTENTE
            Optional<SistemaMemoriaPirata.MemoriaPirata> memoriaPirata = memoria.obtenerMemoriaPirata(nombrePirata);
            if (memoriaPirata.isPresent()) {
                String nombreClase = memoriaPirata.get().getNombreClase();
                bitacora.info("🎯 Buscando clase con JavaCodeCollector: " + nombreClase);
                
                // 🎯 USAR EL COLECTOR PARA OBTENER EL CÓDIGO COMPLETO
                Optional<ClassCode> classCode = codeCollector.getCompleteJavaCode(nombreClase);
                
                if (classCode.isPresent()) {
                    ClassCode codigoClase = classCode.get();
                    bitacora.info("✅ Código obtenido - Calidad: " + 
                        (codigoClase.isReconstructed() ? "RECONSTRUIDO" : "COMPLETO"));
                    
                    // 🎯 OBTENER CÓDIGO COMPLETO CON IMPORTS Y PACKAGE
                    String codigoCompleto = codigoClase.getCompleteFileCode();
                    
                    // 🎯 AÑADIR METADATOS INFORMATIVOS
                    codigoCompleto = añadirMetadatosPirata(codigoCompleto, codigoClase, nombrePirata);
                    
                    return codigoCompleto;
                } else {
                    bitacora.warn("⚠️ No se pudo obtener código con JavaCodeCollector para: " + nombreClase);
                }
            }
            
            // 🎯 SI NO ENCUENTRA, USAR BÚSQUEDA DIRECTA COMO FALLBACK
            bitacora.info("🔄 Intentando búsqueda directa como fallback...");
            return buscarCodigoFuenteDirecto(nombrePirata);
            
        } catch (Exception e) {
            bitacora.error("💥 Error obteniendo código para " + nombrePirata, e);
            return generarCodigoError(nombrePirata, e);
        }
    }

    /**
     * 🎯 AÑADIR METADATOS INFORMATIVOS AL CÓDIGO
     */
    private String añadirMetadatosPirata(String codigoCompleto, ClassCode classCode, String nombrePirata) {
        StringBuilder codigoConMetadatos = new StringBuilder();
        
        // 🎯 CABECERA INFORMATIVA
        codigoConMetadatos.append("// 🏴‍☠️ CÓDIGO PIRATA - ").append(nombrePirata).append("\n");
        codigoConMetadatos.append("// 🔍 Clase: ").append(classCode.getFullName()).append("\n");
        codigoConMetadatos.append("// 📊 Métodos: ").append(classCode.getMethods().size()).append("\n");
        codigoConMetadatos.append("// 📌 Campos: ").append(classCode.getFields().size()).append("\n");
        codigoConMetadatos.append("// 🎯 Tipo: ").append(classCode.getClassType()).append("\n");
        
        if (classCode.isReconstructed()) {
            codigoConMetadatos.append("// ⚠️  ADVERTENCIA: Código reconstruido desde metadatos\n");
        } else {
            codigoConMetadatos.append("// ✅ Código extraído directamente del fuente\n");
        }
        
        codigoConMetadatos.append("// 📅 Generado: ").append(new Date()).append("\n");
        codigoConMetadatos.append("// 🌊 ¡Que el código te acompañe, pirata!\n\n");
        
        codigoConMetadatos.append(codigoCompleto);
        
        return codigoConMetadatos.toString();
    }

    /**
     * 🎯 GENERAR CÓDIGO DE ERROR ELEGANTE
     */
    private String generarCodigoError(String nombrePirata, Exception e) {
        return String.format(
            "// 🏴‍☠️ CÓDIGO PIRATA - %s\n" +
            "// 💥 ERROR: No se pudo obtener el código fuente\n" +
            "// 🔧 Detalles: %s\n" +
            "// 📅 Ocurrido: %s\n\n" +
            "public class %sPirata {\n" +
            "    // ⚠️ El pirata %s se esconde en las profundidades del código...\n" +
            "    \n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\\\"¡El pirata %s sigue vivo en el debate!\\\");\n" +
            "    }\n" +
            "    \n" +
            "    // 🎯 Método de emergencia\n" +
            "    public String obtenerMensajePirata() {\n" +
            "        return \\\"¡Arrr! El código de %s no pudo ser encontrado, pero su espíritu pirata perdura.\\\";\n" +
            "    }\n" +
            "}",
            nombrePirata,
            e.getMessage(),
            new Date(),
            nombrePirata.replace(" ", ""),
            nombrePirata,
            nombrePirata,
            nombrePirata
        );
    }

    /**
     * 🎯 MÉTODO MEJORADO DE BÚSQUEDA DIRECTA (FALLBACK)
     */
    private String buscarCodigoFuenteDirecto(String nombrePirata) {
        try {
            bitacora.info("🔍 Búsqueda directa para: " + nombrePirata);
            
            // 🎯 BUSCAR EN MEMORIA PRIMERO
            Optional<SistemaMemoriaPirata.MemoriaPirata> memoriaPirata = memoria.obtenerMemoriaPirata(nombrePirata);
            if (memoriaPirata.isPresent()) {
                String rutaArchivo = memoriaPirata.get().getRutaArchivo();
                if (rutaArchivo != null && FileUtils.verificarArchivo(rutaArchivo)) {
                    bitacora.info("✅ Encontrado en ruta: " + rutaArchivo);
                    return FileUtils.readFile(rutaArchivo);
                }
            }
            
            // 🎯 BUSQUEDA EN DIRECTORIOS COMUNES
            String[] directoriosBusqueda = {
                "src/main/java",
                "src",
                ".",
                ".."
            };
            
            for (String directorio : directoriosBusqueda) {
                String posibleRuta = encontrarArchivoPorNombre(directorio, nombrePirata);
                if (posibleRuta != null) {
                    bitacora.info("✅ Encontrado en búsqueda: " + posibleRuta);
                    return FileUtils.readFile(posibleRuta);
                }
            }
            
            bitacora.warn("❌ No se encontró código fuente para: " + nombrePirata);
            return generarCodigoNoEncontrado(nombrePirata);
            
        } catch (Exception e) {
            bitacora.error("💥 Error en búsqueda directa: " + e.getMessage());
            return generarCodigoError(nombrePirata, e);
        }
    }

    /**
     * 🎯 BUSCAR ARCHIVO POR NOMBRE EN DIRECTORIO
     */
    private String encontrarArchivoPorNombre(String directorio, String nombrePirata) {
        try {
            Path dirPath = Paths.get(directorio);
            if (!Files.exists(dirPath)) {
                return null;
            }
            
            // 🎯 CONVERTIR NOMBRE PIRATA A POSIBLE NOMBRE DE ARCHIVO
            String nombreArchivo = convertirNombrePirataAArchivo(nombrePirata);
            
            // 🎯 BUSCAR RECURSIVAMENTE
            return Files.walk(dirPath)
                    .filter(path -> path.toString().endsWith(nombreArchivo))
                    .findFirst()
                    .map(Path::toString)
                    .orElse(null);
                    
        } catch (IOException e) {
            bitacora.debug("⚠️ Error buscando en directorio " + directorio + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * 🎯 CONVERTIR NOMBRE PIRATA A NOMBRE DE ARCHIVO
     */
    private String convertirNombrePirataAArchivo(String nombrePirata) {
        // 🎯 EJEMPLOS:
        // "Jack Sparrow" -> "JackSparrow.java" o "Sparrow.java"
        // "barbanegra" -> "Barbanegra.java"
        
        String nombreLimpio = nombrePirata.replaceAll("[^a-zA-Z0-9]", "");
        if (nombreLimpio.isEmpty()) {
            nombreLimpio = "PirataDesconocido";
        }
        
        // 🎯 CAPITALIZAR PRIMERA LETRA
        nombreLimpio = nombreLimpio.substring(0, 1).toUpperCase() + 
                       nombreLimpio.substring(1).toLowerCase();
        
        return nombreLimpio + ".java";
    }

    /**
     * 🎯 GENERAR CÓDIGO CUANDO NO SE ENCUENTRA
     */
    private String generarCodigoNoEncontrado(String nombrePirata) {
        return String.format(
            "// 🏴‍☠️ CÓDIGO PIRATA - %s\n" +
            "// ⚠️  ADVERTENCIA: Código generado automáticamente\n" +
            "// 📅 Generado: %s\n\n" +
            "import java.util.*;\n\n" +
            "/**\n" +
            " * Clase representando al pirata %s\n" +
            " * ⚠️ Esta clase fue generada porque no se encontró el código original\n" +
            " */\n" +
            "public class %s {\n" +
            "    \n" +
            "    private String nombre = \\\"%s\\\";\n" +
            "    private List<String> tesoros = new ArrayList<>();\n" +
            "    \n" +
            "    public %s() {\n" +
            "        // Constructor del pirata\n" +
            "        tesoros.add(\\\"Oro\\\");\n" +
            "        tesoros.add(\\\"Plata\\\");\n" +
            "        tesoros.add(\\\"Ron\\\");\n" +
            "    }\n" +
            "    \n" +
            "    public void hablar() {\n" +
            "        System.out.println(\\\"¡Arrr! Soy el pirata \\\" + nombre);\n" +
            "    }\n" +
            "    \n" +
            "    public List<String> getTesoros() {\n" +
            "        return new ArrayList<>(tesoros);\n" +
            "    }\n" +
            "    \n" +
            "    public static void main(String[] args) {\n" +
            "        %s pirata = new %s();\n" +
            "        pirata.hablar();\n" +
            "        System.out.println(\\\"Tesoros: \\\" + pirata.getTesoros());\n" +
            "    }\n" +
            "}",
            nombrePirata,
            new Date(),
            nombrePirata,
            convertirNombrePirataAArchivo(nombrePirata).replace(".java", ""),
            nombrePirata,
            convertirNombrePirataAArchivo(nombrePirata).replace(".java", ""),
            convertirNombrePirataAArchivo(nombrePirata).replace(".java", ""),
            convertirNombrePirataAArchivo(nombrePirata).replace(".java", "")
        );
    }
    
    /**
     * 🔍 BUSCAR CLASE POR NOMBRE DE PIRATA
     */
    private String buscarClasePorNombrePirata(String nombrePirata) {
        try {
            // 🎯 BUSCAR EN ROLES_PIRATAS
            for (Map.Entry<String, String[]> entry : TripulacionManager.ROLES_PIRATAS.entrySet()) {
                if (entry.getValue()[1].equals(nombrePirata)) {
                    String nombreClase = entry.getKey();
                    bitacora.info("🎯 Encontrado en ROLES_PIRATAS: " + nombreClase);
                    
                    // BUSCAR LA CLASE
                    String[] posiblesPaths = {
                        "src/main/java/" + nombreClase.replace('.', '/') + ".java",
                        "src/test/java/" + nombreClase.replace('.', '/') + ".java",
                        nombreClase.replace('.', '/') + ".java"
                    };
                    
                    for (String path : posiblesPaths) {
                        if (FileUtils.verificarArchivo(path)) {
                            String codigo = FileUtils.readFile(path);
                            if (codigo != null && !codigo.trim().isEmpty()) {
                                return codigo;
                            }
                        }
                    }
                }
            }
            
            // 🎯 BUSCAR EN TODOS LOS ARCHIVOS JAVA DEL PROYECTO
            bitacora.info("🔍 Búsqueda exhaustiva en proyecto...");
            List<String> archivosJava = FileUtils.listFilesTurbo(".", ".java", true);
            
            for (String archivo : archivosJava) {
                try {
                    String contenido = FileUtils.readFile(archivo);
                    if (contenido != null && contenido.contains(nombrePirata)) {
                        bitacora.info("🎯 Posible match encontrado en: " + archivo);
                        // Verificar si es una clase que representa al pirata
                        if (contenido.contains("class") && contenido.contains(nombrePirata)) {
                            return contenido;
                        }
                    }
                } catch (Exception e) {
                    // Continuar con siguiente archivo
                }
            }
            
            return "// No se pudo encontrar el código fuente para " + nombrePirata + 
                   "\n// El pirata puede no tener una clase específica asignada";
            
        } catch (Exception e) {
            return "// Error en búsqueda: " + e.getMessage();
        }
    }
    
    /**
     * 🎯 OBTENER ROL DE UN PIRATA
     */
    private String obtenerRolPirata(String nombrePirata) {
        // 🎯 BUSCAR EN ROLES_PIRATAS
        for (Map.Entry<String, String[]> entry : TripulacionManager.ROLES_PIRATAS.entrySet()) {
            if (entry.getValue()[1].equals(nombrePirata)) {
                return entry.getValue()[0]; // Rol del pirata
            }
        }
        
        // 🎯 BUSCAR EN MEMORIA
        Optional<SistemaMemoriaPirata.MemoriaPirata> memoriaPirata = memoria.obtenerMemoriaPirata(nombrePirata);
        if (memoriaPirata.isPresent()) {
            // Inferir rol basado en el nombre de la clase
            String nombreClase = memoriaPirata.get().getNombreClase();
            if (nombreClase.contains("Manager")) return "Gestor";
            if (nombreClase.contains("Scanner")) return "Explorador";
            if (nombreClase.contains("Debug")) return "Reparador";
            if (nombreClase.contains("API")) return "Comunicador";
        }
        
        return "Experto";
    }
    
    /**
     * 🔍 VERIFICAR SI UNA INTERVENCIÓN CONTIENE NUEVO CONTENIDO
     */
    private boolean contieneNuevoContenido(String nuevaIntervencion, List<String> historial) {
        if (historial.size() <= 1) return true;
        
        String intervencionLower = nuevaIntervencion.toLowerCase();
        
        // 🎯 VERIFICAR SIMILITUD CON INTERVENCIONES ANTERIORES
        int intervencionesSimilares = 0;
        for (int i = Math.max(0, historial.size() - 4); i < historial.size() - 1; i++) {
            String intervencionAnterior = historial.get(i).toLowerCase();
            
            // 🎯 CONTAR PALABRAS COMPARTIDAS
            long palabrasComunes = Arrays.stream(intervencionLower.split("\\s+"))
                    .filter(palabra -> palabra.length() > 3) // Ignorar palabras cortas
                    .filter(palabra -> intervencionAnterior.contains(palabra))
                    .count();
            
            if (palabrasComunes > 10) { // Umbral de similitud
                intervencionesSimilares++;
            }
        }
        
        return intervencionesSimilares < 2; // Máximo 2 intervenciones similares
    }
    
    /**
     * 💾 GUARDAR DEBATE EN ARCHIVO
     */
    private void guardarDebateEnArchivo(String pregunta, List<String> historial, List<String> participantes) {
        try {
            StringBuilder contenido = new StringBuilder();
            contenido.append("# 🏴‍☠️ DEBATE AUTÓNOMO PIRATA\n\n");
            contenido.append("**Fecha**: ").append(new Date()).append("\n\n");
            contenido.append("## ❓ PREGUNTA\n").append(pregunta).append("\n\n");
            contenido.append("## 👥 PARTICIPANTES\n");
            participantes.forEach(p -> contenido.append("- ").append(p).append("\n"));
            contenido.append("\n");
            
            contenido.append("## 📜 HISTORIAL DEL DEBATE\n");
            for (int i = 0; i < historial.size(); i++) {
                contenido.append("### ").append(i + 1).append(". ").append(historial.get(i)).append("\n\n");
            }
            
            String nombreArchivo = "autogen-output/debates/debate_autonomo_" +
                System.currentTimeMillis() + ".md";
            
            FileUtils.crearDirectorioSiNoExiste("autogen-output/debates");
            FileUtils.writeToFile(nombreArchivo, contenido.toString());
            
            System.out.println("💾 Debate guardado en: " + nombreArchivo);
            
        } catch (Exception e) {
            System.err.println("💥 Error guardando debate en archivo: " + e.getMessage());
        }
    }
    
    /**
     * 🎪 INICIAR DEBATE AUTÓNOMO CON PIRATAS SUGERIDOS - VERSIÓN TURBOFURULADA
     * Ahora con integración directa del Fusor de Voluntades tras la deliberación pirata.
     */
    public void iniciarDebateAutonomoConSugerencias(String pregunta) {
        try {
            // 🎯 SUGERIR PIRATAS BASADO EN LA PREGUNTA
            List<String> piratasSugeridos = sugerirPiratasParaPregunta(pregunta);
            
            if (piratasSugeridos.isEmpty()) {
                System.out.println("❌ No se pudieron sugerir piratas para la pregunta");
                return;
            }
            
            System.out.println("🤖 PIRATAS SUGERIDOS PARA EL DEBATE:");
            piratasSugeridos.forEach(p -> System.out.println("  • " + p));
            
            // 🎯 CONFIRMAR CON EL USUARIO
            System.out.print("\n🎯 ¿Iniciar debate con estos piratas? (s/n): ");
            Scanner scanner = new Scanner(System.in);
            String respuesta = scanner.nextLine().trim();
            
            if (respuesta.equalsIgnoreCase("s") || respuesta.equalsIgnoreCase("si") || 
                respuesta.equalsIgnoreCase("y") || respuesta.equalsIgnoreCase("yes")) {
                
                // 🎯 EJECUTAR DEBATE EN UN HILO SEPARADO PARA NO BLOQUEAR
                Thread debateThread = new Thread(() -> {
                    try {
                        iniciarDebateAutonomo(pregunta, piratasSugeridos.toArray(new String[0]));
                    } catch (Exception e) {
                        System.err.println("💥 Error en debate: " + e.getMessage());
                    }
                });
                
                debateThread.start();
                
                // 🎯 ESPERAR A QUE TERMINE EL DEBATE
                try {
                    debateThread.join();

                    // ⚙️ Una vez finalizado el debate, activar el FUSOR DE VOLUNTADES
                    System.out.println("\n" + "⚙️".repeat(80));
                    System.out.println("          🧩 ACTIVANDO EL FUSOR DE DEBATES AUTÓNOMO...");
                    System.out.println("⚙️".repeat(80));

                    // 🧠 Inicialización correcta del Fusor con todos sus módulos
                    FusorDeDebatesAutonomo fusor = new FusorDeDebatesAutonomo(
                        new OraculoTecnico("deepseek-chat", 0.3, 0.3),           // intérprete técnico de conclusiones
                        bitacora,                       // bitácora principal del sistema
                        new com.elreinodelolvido.ellibertad.util.GeneradorClasesNuevas(),
                        new IntegradorForzado(bitacora),
                        new com.elreinodelolvido.ellibertad.util.ValidadorFirmas()
                    );

                    // 🌊 Ejecutar el proceso de fusión de sabiduría colectiva
                    fusor.fusionarSabiduriaColectiva();

                    System.out.println("\n🏴‍☠️ Fusión completada con éxito. Las voces del Libertad han sido integradas.\n");

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
            } else {
                System.out.println("❌ Debate cancelado por el usuario");
            }
            
        } catch (Exception e) {
            System.err.println("💥 Error iniciando debate con sugerencias: " + e.getMessage());
            bitacora.error("💥 Error en debate con sugerencias", e);
        } finally {
            // 🎯 ¡ESTO ES CLAVE! VOLVER AL MENÚ PRINCIPAL
            volverAlMenuPrincipal();
        }
    }

    /**
     * 🔄 VOLVER AL MENÚ PRINCIPAL DESPUÉS DEL DEBATE
     */
    private void volverAlMenuPrincipal() {
        try {
            System.out.println("\n" + "🔄".repeat(60));
            System.out.println("           VOLVIENDO AL MENÚ PRINCIPAL...");
            System.out.println("🔄".repeat(60));
            
            // 🎯 PAUSA DRAMÁTICA
            Thread.sleep(2000);
            
            // 🎯 LIMPIAR CUALQUIER SCANNER BLOQUEANTE
            System.gc();
            
            // 🎯 VOLVER A MOSTRAR EL MENÚ
            mostrarMenuPrincipal();
            
        } catch (Exception e) {
            System.err.println("💥 Error volviendo al menú: " + e.getMessage());
            // 🎯 FALLBACK: SIMPLEMENTE SALIR DEL MÉTODO
        }
    }

    /**
     * 🏴‍☠️ MOSTRAR MENÚ PRINCIPAL (copiado del main)
     */
    private void mostrarMenuPrincipal() {
        // 🎯 ESTO DEBERÍA LLAMAR AL MÉTODO PRINCIPAL DEL SISTEMA
        // Por ahora, simulemos que volvemos al menú
        
        System.out.println("\n" + "⚓".repeat(80));
        System.out.println("           ¡DEBATE COMPLETADO! ¿QUÉ QUIERES HACER AHORA?");
        System.out.println("⚓".repeat(80));
        
        System.out.println("🎯 OPCIONES:");
        System.out.println("1. 🔄 Nuevo debate con sugerencias");
        System.out.println("2. 🤖 Nuevo debate manual");
        System.out.println("3. 🏴‍☠️ Volver al menú principal completo");
        System.out.println("0. 🚪 Salir del sistema");
        
        System.out.print("\n🎯 Selecciona una opción: ");
        
        try {
            Scanner scanner = new Scanner(System.in);
            String opcion = scanner.nextLine().trim();
            
            switch (opcion) {
                case "1":
                    iniciarNuevoDebateConSugerencias();
                    break;
                case "2":
                    iniciarNuevoDebateManual();
                    break;
                case "3":
                    volverAlMenuCompleto();
                    break;
                case "0":
                    System.out.println("🏴‍☠️ ¡Hasta la próxima, capitán!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("❌ Opción inválida, volviendo al menú principal...");
                    volverAlMenuCompleto();
            }
            
        } catch (Exception e) {
            System.err.println("💥 Error en menú: " + e.getMessage());
            volverAlMenuCompleto();
        }
    }

    /**
     * 🔄 INICIAR NUEVO DEBATE CON SUGERENCIAS
     */
    private void iniciarNuevoDebateConSugerencias() {
        try {
            System.out.print("\n❓ Nueva pregunta para el debate: ");
            Scanner scanner = new Scanner(System.in);
            String nuevaPregunta = scanner.nextLine().trim();
            
            if (!nuevaPregunta.isEmpty()) {
                iniciarDebateAutonomoConSugerencias(nuevaPregunta);
            } else {
                System.out.println("❌ Pregunta vacía, volviendo al menú...");
                mostrarMenuPrincipal();
            }
            
        } catch (Exception e) {
            System.err.println("💥 Error en nuevo debate: " + e.getMessage());
            mostrarMenuPrincipal();
        }
    }

    /**
     * 🤖 INICIAR NUEVO DEBATE MANUAL
     */
    private void iniciarNuevoDebateManual() {
        try {
            System.out.print("\n❓ Nueva pregunta para el debate: ");
            Scanner scanner = new Scanner(System.in);
            String pregunta = scanner.nextLine().trim();
            
            if (!pregunta.isEmpty()) {
                System.out.print("👥 Piratas (separados por coma): ");
                String piratasInput = scanner.nextLine().trim();
                String[] piratas = piratasInput.split(",");
                
                if (piratas.length > 0) {
                    // Limpiar nombres
                    for (int i = 0; i < piratas.length; i++) {
                        piratas[i] = piratas[i].trim();
                    }
                    
                    Thread debateThread = new Thread(() -> {
                        iniciarDebateAutonomo(pregunta, piratas);
                    });
                    debateThread.start();
                    debateThread.join();
                    
                } else {
                    System.out.println("❌ No se especificaron piratas");
                }
            } else {
                System.out.println("❌ Pregunta vacía");
            }
            
        } catch (Exception e) {
            System.err.println("💥 Error en debate manual: " + e.getMessage());
        } finally {
            mostrarMenuPrincipal();
        }
    }

    /**
     * 🏴‍☠️ VOLVER AL MENÚ COMPLETO DEL SISTEMA
     */
    private void volverAlMenuCompleto() {
        System.out.println("\n" + "🏴‍☠️".repeat(60));
        System.out.println("           VOLVIENDO AL SISTEMA PRINCIPAL...");
        System.out.println("🏴‍☠️".repeat(60));
        
        // 🎯 EN UN SISTEMA REAL, AQUÍ SE LLAMARÍA AL MÉTODO MAIN O AL MENÚ PRINCIPAL
        // Por ahora, simulemos que volvemos
        
        try {
            Thread.sleep(1000);
            System.out.println("\n✅ Sistema listo para nuevas órdenes, capitán!");
            
            // 🎯 EN TU CÓDIGO PRINCIPAL, ESTO DEBERÍA SER:
            AutogenTurboFusion.main(new String[]{});
            // O volver al bucle principal del menú
            
        } catch (Exception e) {
            System.err.println("💥 Error volviendo al sistema: " + e.getMessage());
        }
    }
    
    /**
     * 🎯 SUGERIR PIRATAS PARA UNA PREGUNTA
     */
    private List<String> sugerirPiratasParaPregunta(String pregunta) {
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("Dada la siguiente pregunta sobre desarrollo de software:\n\n");
            prompt.append("\"").append(pregunta).append("\"\n\n");
            
            prompt.append("Y esta lista de piratas (clases Java) disponibles:\n");
            for (Map.Entry<String, String[]> entry : TripulacionManager.ROLES_PIRATAS.entrySet()) {
                prompt.append("- ").append(entry.getValue()[1])
                      .append(" (").append(entry.getValue()[0]).append(") - ")
                      .append(entry.getKey()).append("\n");
            }
            prompt.append("\n");
            
            prompt.append("¿Qué 3-4 piratas están mejor cualificados para debatir esta pregunta?\n");
            prompt.append("Considera sus especialidades y la naturaleza de la pregunta.\n");
            prompt.append("Devuelve SOLO los nombres de los piratas separados por comas, sin explicaciones.");
            
            String respuesta = oraculo.invocar(prompt.toString(), "sugerir_piratas_debate", 0.4);
            
            // 🎯 PROCESAR RESPUESTA
            return Arrays.stream(respuesta.split(","))
                    .map(String::trim)
                    .filter(nombre -> !nombre.isEmpty())
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            // 🎯 FALLBACK: PIRATAS POR DEFECTO
            return Arrays.asList("Barbanegra Turbo", "Ojo de Halcón", "Sable Afilado", "Mano de Papel");
        }
    }
}
