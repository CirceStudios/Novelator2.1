package com.elreinodelolvido.ellibertad.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.elreinodelolvido.ellibertad.api.DeepSeekClient;
import com.elreinodelolvido.ellibertad.model.ClassInfo;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;

/**
 * 🏴‍☠️ NARRADOR_PIRATA_TURBO — Cronista épico del barco Libertad.
 * ⚡ Ahora con +500% de épica, arcos narrativos y registro de leyendas.
 */
public class NarradorPirata {

    private static final String OUTPUT_DIR = "autogen-output/narrativas/";
    private static final String NARRATIVA_ACTUAL = OUTPUT_DIR + "narrativa-final.md";
    private static final String LEYENDAS_DIR = OUTPUT_DIR + "leyendas/";
    private static final String ESTADISTICAS_NARRATIVAS = OUTPUT_DIR + "estadisticas-narrativas.json";
    
    private static final DeepSeekClient ORACULO = new DeepSeekClient();
    
    // 📊 ESTADÍSTICAS TURBO
    private static final AtomicInteger CONTADOR_NARRATIVAS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_LEYENDAS = new AtomicInteger(0);
    private static final Map<String, Integer> TEMAS_NARRATIVOS = new HashMap<>();

    static {
        // Inicializar temas épicos
        TEMAS_NARRATIVOS.put("⚔️ BATALLA_EPICA", 0);
        TEMAS_NARRATIVOS.put("🌊 TRAVESIA_OCEANO", 0);
        TEMAS_NARRATIVOS.put("🏝️ DESCUBRIMIENTO", 0);
        TEMAS_NARRATIVOS.put("🐙 ENFRENTAMIENTO_KRAKEN", 0);
        TEMAS_NARRATIVOS.put("🔮 PROFECIA_CUMPLIDA", 0);
        TEMAS_NARRATIVOS.put("⚓ MOTIN_RESUELTO", 0);
    }

    /**
     * 🎭 GENERACIÓN TURBO DE NARRATIVA ÉPICA
     */
    public static String generarNarrativaFinal() {
        long startTime = System.currentTimeMillis();
        CONTADOR_NARRATIVAS.incrementAndGet();
        
        System.out.println("\n📜 INVOCANDO AL CRONISTA TURBO DEL BARCO LIBERTAD...");

        try {
            // 🎯 1. RECOLECCIÓN TURBO DE INGREDIENTES NARRATIVOS
            Map<String, String> ingredientes = recolectarIngredientesNarrativos();
            
            // 🎪 2. CONSTRUCCIÓN TURBO DE PROMPT ÉPICO
            String prompt = construirPromptNarrativoTurbo(ingredientes);
            
            // 🔮 3. INVOCACIÓN TURBO AL ORÁCULO
            System.out.println("🔮 Consultando al Oráculo de los Mares...");
            String historia = ORACULO.enviarPromptNarrativo(prompt);

            if (historia == null || historia.isBlank()) {
                System.err.println("❌ El Oráculo guarda silencio... no hay narrativa.");
                return generarNarrativaDeRespaldo(ingredientes);
            }

            // ✨ 4. POST-PROCESAMIENTO TURBO
            historia = postProcesarNarrativa(historia, ingredientes);
            
            // 💾 5. GUARDADO TURBO Y REGISTRO
            guardarNarrativaCompleta(historia, ingredientes);
            
            // 📊 6. ACTUALIZAR ESTADÍSTICAS
            actualizarEstadisticasNarrativas(historia);
            
            long tiempo = System.currentTimeMillis() - startTime;
            System.out.println("✅ NARRATIVA TURBO GENERADA en " + tiempo + "ms");
            System.out.println("📖 Tamaño: " + historia.length() + " caracteres épicos");
            
            return historia;

        } catch (Exception e) {
            System.err.println("💥 FALLO EPICO EN LA CRÓNICA:");
            FileUtils.registrarKraken("NarradorPirata", e);
            return generarNarrativaDeEmergencia();
        }
    }

    /**
     * 🎯 RECOLECCIÓN TURBO DE INGREDIENTES NARRATIVOS
     */
    private static Map<String, String> recolectarIngredientesNarrativos() {
        Map<String, String> ingredientes = new LinkedHashMap<>();
        
        try {
            // 📜 OBJETIVOS
            ingredientes.put("objetivos", FileUtils.readFileIfExists("autogen-output/objetivos.md"));
            if (ingredientes.get("objetivos").isEmpty()) {
                ingredientes.put("objetivos", "# Objetivos de la Travesía\n\n- Navegar hacia nuevas funcionalidades\n- Reclutar tripulantes valientes\n- Enfrentar krakens del código");
            }
            
            // 👥 TRIPULACIÓN
            ingredientes.put("tripulacion", generarResumenTripulacionEpico());
            
            // 🐙 KRAKENS
            ingredientes.put("krakens", FileUtils.readFileIfExists("autogen-output/krakens/log-krakens.md"));
            
            // 📊 ESTADÍSTICAS
            ingredientes.put("estadisticas", generarEstadisticasNarrativas());
            
            // 🏴‍☠️ EVENTOS RECIENTES
            ingredientes.put("eventos", detectarEventosRecientes());
            
            // 🎭 PERSONAJES DESTACADOS
            ingredientes.put("personajes", identificarPersonajesDestacados());
            
            System.out.println("🎯 Ingredientes narrativos recolectados: " + ingredientes.size() + " elementos");

        } catch (Exception e) {
            System.err.println("⚠️ Error recolectando ingredientes, usando valores por defecto");
            ingredientes.put("objetivos", "Objetivos no disponibles");
            ingredientes.put("tripulacion", "Tripulación en formación");
            ingredientes.put("krakens", "Aguas tranquilas");
        }
        
        return ingredientes;
    }

    /**
     * 👥 GENERACIÓN TURBO DE RESUMEN DE TRIPULACIÓN
     */
    private static String generarResumenTripulacionEpico() {
        try {
            ProjectScanner scanner = new ProjectScanner();
            scanner.scanProject("./");
            List<ClassInfo> clases = scanner.getClasses();
            
            StringBuilder tripulacion = new StringBuilder();
            tripulacion.append("## 👥 GRANDES ALMAS DEL BARCO LIBERTAD\n\n");
            
            // 🎯 AGRUPAR POR GREMIOS (PAQUETES)
            Map<String, List<ClassInfo>> porGremio = new TreeMap<>();
            for (ClassInfo clase : clases) {
                porGremio.computeIfAbsent(clase.getPackageName(), k -> new ArrayList<>()).add(clase);
            }
            
            for (Map.Entry<String, List<ClassInfo>> gremio : porGremio.entrySet()) {
                String nombreGremio = obtenerNombreGremioEpico(gremio.getKey());
                tripulacion.append("### ").append(nombreGremio).append("\n");
                tripulacion.append("*Especialistas en las artes de `").append(gremio.getKey()).append("`*\n\n");
                
                for (ClassInfo clase : gremio.getValue()) {
                    String nombrePirata = DiccionarioDeTripulacion.nombrePirata(clase);
                    String rol = asignarRolEpico(clase);
                    String especialidad = determinarEspecialidad(clase.getName());
                    
                    tripulacion.append("- **").append(nombrePirata).append("**")
                              .append(" - ").append(rol)
                              .append(" | *").append(especialidad).append("*\n");
                }
                tripulacion.append("\n");
            }
            
            tripulacion.append("📊 **Total de almas a bordo**: ").append(clases.size()).append("\n");
            return tripulacion.toString();
            
        } catch (Exception e) {
            return "La tripulación se prepara en silencio para la próxima travesía...";
        }
    }

    /**
     * 🎪 CONSTRUCCIÓN TURBO DE PROMPT NARRATIVO
     */
    private static String construirPromptNarrativoTurbo(Map<String, String> ingredientes) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        return """
            🏴‍☠️ ERES EL CRONISTA OFICIAL DEL BARCO *LIBERTAD* 🏴‍☠️
            
            # 🎭 TU MISIÓN EPICA:
            Escribe la crónica definitiva de la última travesía del barco Libertad por los mares del Reino del Olvido.
            
            # 🎨 ESTILO LITERARIO REQUERIDO:
            - **Voz épica y dramática**, como un viejo lobo de mar contando leyendas
            - **Simbología rica**: cada elemento técnico tiene significado narrativo
            - **Coherencia interna**: la historia debe fluir lógicamente
            - **Emoción auténtica**: celebra triunfos, lamenta pérdidas, inspira esperanza
            - **Longitud**: 800-1500 palabras, dividida en capítulos naturales
            
            # 🌊 ESTRUCTURA SUGERIDA:
            1. **Prólogo**: El llamado a la aventura
            2. **Preparación**: La tripulación se reúne
            3. **Travesía**: Navegación y descubrimientos  
            4. **Conflicto**: Enfrentamiento con krakens
            5. **Clímax**: Momento decisivo de la misión
            6. **Resolución**: Consecuencias y aprendizajes
            7. **Epílogo**: Mirada al futuro
            
            # 📜 INGREDIENTES PARA TU CRÓNICA:
            
            ## 🎯 OBJETIVOS DE LA TRAVESÍA
            %s
            
            ## 👥 TRIPULACIÓN ÉPICA
            %s
            
            ## 🐙 KRAKENS ENFRENTADOS
            %s
            
            ## 📊 ESTADÍSTICAS DEL VIAJE
            %s
            
            ## ⚡ EVENTOS RECIENTES
            %s
            
            ## 🎭 PERSONAJES DESTACADOS
            %s
            
            # 🚀 INSTRUCCIONES FINALES:
            - **NO uses markdown** en tu respuesta, solo texto narrativo puro
            - **NO expliques** lo que vas a hacer, simplemente **CUENTA LA HISTORIA**
            - **Incluye diálogos** naturales entre los personajes cuando sea apropiado
            - **Crea tensión dramática** y **momentos de revelación**
            - **Termina con una reflexión** sobre el significado del viaje
            
            ⚓ **Fecha de la crónica**: %s
            🧭 **Cronista**: El Oráculo de los Mares
            
            ¡Que los vientos narrativos te sean favorables!
            """.formatted(
                ingredientes.get("objetivos"),
                ingredientes.get("tripulacion"),
                ingredientes.get("krakens"),
                ingredientes.get("estadisticas"),
                ingredientes.get("eventos"),
                ingredientes.get("personajes"),
                timestamp
            );
    }

    /**
     * ✨ POST-PROCESAMIENTO TURBO DE NARRATIVA
     */
    private static String postProcesarNarrativa(String historia, Map<String, String> ingredientes) {
        // 🎨 MEJORAS ESTÉTICAS
        historia = historia.replace("```", "")  // Remover posibles markdown
                          .replace("**", "**")  // Mantener negritas si existen
                          .trim();
        
        // 📝 AGREGAR ENCABEZADO ÉPICO
        String encabezado = generarEncabezadoEpico(ingredientes);
        historia = encabezado + "\n\n" + historia;
        
        // 🎯 DETECTAR Y REGISTRAR TEMA PRINCIPAL
        String temaPrincipal = detectarTemaPrincipal(historia);
        TEMAS_NARRATIVOS.put(temaPrincipal, TEMAS_NARRATIVOS.getOrDefault(temaPrincipal, 0) + 1);
        
        System.out.println("🎭 Tema narrativo detectado: " + temaPrincipal);
        
        return historia;
    }

    /**
     * 💾 GUARDADO TURBO DE NARRATIVA COMPLETA
     */
    private static void guardarNarrativaCompleta(String historia, Map<String, String> ingredientes) {
        try {
            FileUtils.ensureParentDirectoryExists(NARRATIVA_ACTUAL);
            
            // 📄 GUARDAR NARRATIVA PRINCIPAL
            FileUtils.writeToFile(NARRATIVA_ACTUAL, historia);
            
            // 📖 AGREGAR A BITÁCORA MAESTRA
            FileUtils.appendToFile("autogen-output/bitacora-pirata.md", 
                "\n\n" + "📜".repeat(40) + "\n" + historia + "\n" + "📜".repeat(40) + "\n");
            
            // 🏴‍☠️ CREAR LEYENDA INDIVIDUAL
            crearLeyendaIndividual(historia, ingredientes);
            
            System.out.println("💾 Narrativa guardada en: " + NARRATIVA_ACTUAL);
            System.out.println("📚 Leyenda archivada en el gran tomo pirata");
            
        } catch (Exception e) {
            System.err.println("⚠️ Error guardando narrativa: " + e.getMessage());
        }
    }

    /**
     * 🏴‍☠️ CREACIÓN DE LEYENDA INDIVIDUAL
     */
    private static void crearLeyendaIndividual(String historia, Map<String, String> ingredientes) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String leyendaPath = LEYENDAS_DIR + "leyenda-" + timestamp + ".md";
            
            String leyendaCompleta = """
                # 🏴‍☠️ LEYENDA DEL BARCO LIBERTAD
                **Fecha**: %s
                **Cronista**: Narrador Pirata Turbo
                
                ## 📜 LA CRÓNICA
                %s
                
                ## 🎯 INGREDIENTES ORIGINALES
                <details>
                <summary>Ver ingredientes de la narrativa</summary>
                
                ### Objetivos
                %s
                
                ### Tripulación
                %s
                
                ### Krakens
                %s
                </details>
                
                ---
                *Leyenda preservada para la eternidad pirata*
                """.formatted(
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    historia,
                    ingredientes.get("objetivos"),
                    ingredientes.get("tripulacion"),
                    ingredientes.get("krakens")
                );
            
            FileUtils.writeToFile(leyendaPath, leyendaCompleta);
            CONTADOR_LEYENDAS.incrementAndGet();
            
        } catch (Exception e) {
            System.err.println("⚠️ Error creando leyenda individual");
        }
    }

    /**
     * 🆕 GENERACIÓN DE NARRATIVA DE RESPALDO
     */
    private static String generarNarrativaDeRespaldo(Map<String, String> ingredientes) {
        return """
            # 🌊 CRÓNICA DEL BARCO LIBERTAD
            **Fecha**: %s
            
            En los mares del Reino del Olvido, el barco Libertad mantiene su rumbo...
            
            ## 👥 LA TRIPULACIÓN
            %s
            
            ## 🎯 EL VIAJE CONTINÚA
            Los objetivos siguen guiando nuestra travesía a través de aguas desconocidas.
            
            ## 🐙 AGUAS TRANQUILAS
            Por ahora, los krakens respetan nuestro espacio, permitiéndonos avanzar.
            
            ⚓ *El viaje continúa, y cada nueva línea de código es un remo que nos acerca a nuestro destino.*
            """.formatted(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                ingredientes.get("tripulacion")
            );
    }

    /**
     * 🆕 GENERACIÓN DE NARRATIVA DE EMERGENCIA
     */
    private static String generarNarrativaDeEmergencia() {
        return """
            # ⚡ CRÓNICA DE EMERGENCIA
            **Fecha**: %s
            
            🏴‍☠️ ¡ATENCIÓN, TRIPULACIÓN!
            
            Una tormenta inesperada ha golpeado al cronista oficial. Los registros narrativos 
            se han visto comprometidos por krakens técnicos de gran poder.
            
            ## 🎯 MISIÓN DE RESCATE
            La tripulación del Libertad debe continuar su travesía mientras el cronista 
            se recupera de este contratiempo.
            
            🔧 *Los ingenieros trabajan para restaurar la capacidad narrativa...*
            
            ⚓ **El barco sigue su curso, porque la aventura nunca espera.**
            """.formatted(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    // 🔧 MÉTODOS AUXILIARES TURBO

    private static String obtenerNombreGremioEpico(String nombrePaquete) {
        if (nombrePaquete.contains("api")) return "🧙 CONSEJO DE LOS ORÁCULOS";
        if (nombrePaquete.contains("util")) return "⚒️ GREMIO DE ARTESANOS";
        if (nombrePaquete.contains("model")) return "🎭 CÍRCULO DE EMBAJADORES";
        if (nombrePaquete.contains("scanner")) return "👁️ ORDEN DE VIGÍAS";
        if (nombrePaquete.contains("tripulacion")) return "👥 HERMANDAD DE LA TRIPULACIÓN";
        return "🏴‍☠️ GREMIO DE " + nombrePaquete.substring(nombrePaquete.lastIndexOf('.') + 1).toUpperCase();
    }

    private static String asignarRolEpico(ClassInfo clase) {
        String nombre = clase.getName().toLowerCase();
        if (nombre.contains("manager") || nombre.contains("controller")) return "comanda una sección del navío";
        if (nombre.contains("service")) return "ofrece servicios esenciales a la tripulación";
        if (nombre.contains("util")) return "forja herramientas y artefactos mágicos";
        if (nombre.contains("model") || nombre.contains("entity")) return "encarna conceptos fundamentales";
        if (nombre.contains("api") || nombre.contains("client")) return "comunica con entidades distantes";
        return "desempeña un rol vital en la tripulación";
    }

    private static String determinarEspecialidad(String nombreClase) {
        if (nombreClase.contains("DeepSeek")) return "Oráculo de los Mares";
        if (nombreClase.contains("FileUtils")) return "Guardían de los Archivos";
        if (nombreClase.contains("Scanner")) return "Cartógrafo de Costas";
        if (nombreClase.contains("Auditor")) return "Inspector del Navío";
        return "Especialista en " + nombreClase;
    }

    private static String generarEstadisticasNarrativas() {
        return "Estadísticas en desarrollo...";
    }

    private static String detectarEventosRecientes() {
        return "Eventos recientes en análisis...";
    }

    private static String identificarPersonajesDestacados() {
        return "Personajes destacados en evaluación...";
    }

    private static String generarEncabezadoEpico(Map<String, String> ingredientes) {
        return """
            # 🏴‍☠️ CRÓNICA OFICIAL DEL BARCO LIBERTAD
            ## 🌊 Travesía por los Mares del Reino del Olvido
            
            *"Cada línea de código es una ola, cada clase un tripulante, 
            y cada bug un kraken a enfrentar con valor."*
            
            ---
            """;
    }

    private static String detectarTemaPrincipal(String historia) {
        if (historia.toLowerCase().contains("batalla") || historia.contains("⚔️")) return "⚔️ BATALLA_EPICA";
        if (historia.toLowerCase().contains("kraken") || historia.contains("🐙")) return "🐙 ENFRENTAMIENTO_KRAKEN";
        if (historia.toLowerCase().contains("descubrimiento") || historia.contains("🏝️")) return "🏝️ DESCUBRIMIENTO";
        return "🌊 TRAVESIA_OCEANO";
    }

    private static void actualizarEstadisticasNarrativas(String historia) {
        // Implementación para estadísticas detalladas
    }

    /**
     * 📊 MOSTRAR ESTADÍSTICAS ÉPICAS DEL NARRADOR
     */
    public static void mostrarEstadisticasEpicas() {
        System.out.println("\n📊 ESTADÍSTICAS DEL NARRADOR PIRATA TURBO");
        System.out.println("📜".repeat(50));
        System.out.println("🎭 Narrativas generadas: " + CONTADOR_NARRATIVAS.get());
        System.out.println("🏴‍☠️ Leyendas archivadas: " + CONTADOR_LEYENDAS.get());
        System.out.println("🎪 Temas narrativos únicos: " + TEMAS_NARRATIVOS.size());
        
        System.out.println("\n🎯 TEMAS MÁS POPULARES:");
        TEMAS_NARRATIVOS.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(3)
            .forEach(entry -> System.out.println("  " + entry.getKey() + ": " + entry.getValue()));
        
        System.out.println("📜".repeat(50));
    }
}