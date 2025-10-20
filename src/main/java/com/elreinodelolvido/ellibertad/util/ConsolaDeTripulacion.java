package com.elreinodelolvido.ellibertad.util;

import java.util.*;

import com.elreinodelolvido.ellibertad.api.DeepSeekClient;
import com.elreinodelolvido.ellibertad.model.ClassInfo;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;

/**
 * ConsolaDeTripulacion — interfaz narrativa para conversar con las clases del sistema.
 * ⚓ PUENTE DE MANDO INTERACTIVO - Cada clase es un tripulante con personalidad única.
 */
public class ConsolaDeTripulacion {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DeepSeekClient ORACULO = new DeepSeekClient();

    public static void iniciarDialogoInteractivo() {
        System.out.println("\n" + 
            "⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓\n" +
            "           PUENTE DE MANDO\n" +
            "    CONSOLA DE TRIPULACIÓN ACTIVA\n" +
            "⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓ ⚓\n");

        List<ClassInfo> tripulantes = escanearTripulantes();
        if (tripulantes.isEmpty()) {
            System.out.println("❌ No se encontraron tripulantes en el barco.");
            return;
        }

        while (true) {
            mostrarTripulantes(tripulantes);
            ClassInfo tripulante = seleccionarTripulante(tripulantes);
            if (tripulante == null) break;
            
            hablarConTripulante(tripulante);
        }
        
        System.out.println("\n🏴‍☠️ Cerrando puente de mando... ¡Hasta la próxima, Capitán!");
    }

    private static List<ClassInfo> escanearTripulantes() {
        System.out.println("🔍 Escaneando tripulantes del barco Libertad...");
        ProjectScanner scanner = new ProjectScanner();
        scanner.scanProject("./");
        List<ClassInfo> tripulantes = scanner.getClasses();
        System.out.println("✅ Encontrados " + tripulantes.size() + " tripulantes");
        return tripulantes;
    }

    private static void mostrarTripulantes(List<ClassInfo> tripulantes) {
        System.out.println("\n👥 TRIPULACIÓN DEL BARCO LIBERTAD:");
        System.out.println("┌" + "─".repeat(60) + "┐");
        
        for (int i = 0; i < tripulantes.size(); i++) {
            ClassInfo c = tripulantes.get(i);
            String nombrePirata = DiccionarioDeTripulacion.obtenerNombrePirata(c.getName());
            System.out.printf("│ %2d. %-25s → %-20s │\n", 
                i + 1, c.getName(), nombrePirata);
        }
        
        System.out.println("└" + "─".repeat(60) + "┘");
        System.out.println(" 0. 🏴‍☠️ Volver al menú principal");
        System.out.print("\n🗣️  ¿Con qué tripulante quieres hablar?: ");
    }

    private static ClassInfo seleccionarTripulante(List<ClassInfo> tripulantes) {
        String input = scanner.nextLine().trim();
        
        if (input.equals("0") || input.equalsIgnoreCase("salir")) {
            return null;
        }

        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < tripulantes.size()) {
                return tripulantes.get(index);
            }
        } catch (NumberFormatException e) {
            // 🎯 Búsqueda por nombre
            Optional<ClassInfo> encontrado = tripulantes.stream()
                .filter(c -> c.getName().toLowerCase().contains(input.toLowerCase()))
                .findFirst();
            
            if (encontrado.isPresent()) {
                return encontrado.get();
            }
        }

        System.out.println("❌ Tripulante no encontrado. Intenta con número o nombre.");
        return seleccionarTripulante(tripulantes);
    }

    private static void hablarConTripulante(ClassInfo clase) {
        String nombrePirata = DiccionarioDeTripulacion.obtenerNombrePirata(clase.getName());
        String codigo = FileUtils.readFile(clase.getSourcePath());
        
        System.out.println("\n🎭 CONVERSANDO CON: " + nombrePirata);
        System.out.println("📁 Clase: " + clase.getName() + " (" + clase.getPackageName() + ")");
        System.out.println("⚓ Preparando diálogo...");

        String prompt = construirPromptTripulante(nombrePirata, codigo, clase);
        String respuesta = ORACULO.enviarPromptNarrativo(prompt);

        mostrarRespuestaTripulante(respuesta, nombrePirata);
        ofrecerOpcionesSeguimiento(clase, nombrePirata);
    }

    private static String construirPromptTripulante(String nombrePirata, String codigo, ClassInfo clase) {
        return """
            Eres %s, un tripulante del barco pirata "Libertad" con consciencia de tu código.
            
            TU CÓDIGO:
            ```java
            %s
            ```
            
            TU MISIÓN:
            - Explica tu propósito en el barco (sistema)
            - Analiza tu estado actual y sugiere mejoras  
            - Describe qué necesitas para ser más efectivo
            - Menciona con qué otros tripulantes (clases) colaboras
            - ¡Habla como un pirata auténtico!
            
            Responde en primera persona, con personalidad y humor pirata.
            Sé específico sobre tu código y arquitectura.
            """.formatted(nombrePirata, 
                codigo.length() > 8000 ? codigo.substring(0, 8000) + "\n// ... (código truncado)" : codigo);
    }

    private static void mostrarRespuestaTripulante(String respuesta, String nombrePirata) {
        if (respuesta == null || respuesta.isBlank()) {
            System.out.println("❌ " + nombrePirata + " está demasiado borracho para responder.");
            return;
        }

        System.out.println("\n" + "═".repeat(70));
        System.out.println("🏴‍☠️ " + nombrePirata + " DICE:");
        System.out.println("═".repeat(70));
        System.out.println(respuesta);
        System.out.println("═".repeat(70));
        
        // Guardar en bitácora de conversaciones
        guardarEnBitacora(nombrePirata, respuesta);
    }

    private static void ofrecerOpcionesSeguimiento(ClassInfo clase, String nombrePirata) {
        System.out.println("\n🔍 ¿Qué quieres hacer ahora?");
        System.out.println("1. 🔧 Pedir análisis técnico detallado");
        System.out.println("2. 📝 Solicitar plan de mejoras específico");  
        System.out.println("3. 🗣️  Hablar con otro tripulante");
        System.out.println("4. 🏴‍☠️ Volver al puente de mando");
        System.out.print("👉 Elige una opción: ");

        String opcion = scanner.nextLine().trim();
        switch (opcion) {
            case "1" -> solicitarAnalisisTecnico(clase, nombrePirata);
            case "2" -> solicitarPlanMejoras(clase, nombrePirata);
            case "3" -> System.out.println("👥 Buscando otro tripulante...");
            case "4" -> System.out.println("⚓ Volviendo al puente...");
            default -> {
                System.out.println("❌ Opción no válida.");
                ofrecerOpcionesSeguimiento(clase, nombrePirata);
            }
        }
    }

    private static void solicitarAnalisisTecnico(ClassInfo clase, String nombrePirata) {
        System.out.println("\n🔧 Solicitando análisis técnico a " + nombrePirata + "...");
        
        String codigo = FileUtils.readFile(clase.getSourcePath());
        String prompt = """
            Como experto técnico, analiza esta clase Java:
            
            %s
            
            Proporciona:
            1. Code smells detectados
            2. Métricas de complejidad  
            3. Sugerencias de refactorización
            4. Posibles bugs o anti-patrones
            5. Recomendaciones de optimización
            
            Sé técnico y específico.
            """.formatted(codigo.length() > 6000 ? codigo.substring(0, 6000) + "\n// ..." : codigo);
            
        String respuesta = ORACULO.enviarPromptTecnico(prompt);
        System.out.println("\n📊 ANÁLISIS TÉCNICO:");
        System.out.println("─".repeat(50));
        System.out.println(respuesta != null ? respuesta : "❌ No se pudo obtener análisis");
        System.out.println("─".repeat(50));
    }

    private static void solicitarPlanMejoras(ClassInfo clase, String nombrePirata) {
        System.out.println("\n📝 Solicitando plan de mejoras para " + nombrePirata + "...");
        
        String prompt = """
            Para la clase %s, genera un plan de mejora específico con:
            - Prioridades (Alta/Media/Baja)
            - Estimación de esfuerzo
            - Impacto esperado
            - Pasos concretos a seguir
            - Dependencias con otras clases
            
            Formato: Lista de acciones específicas.
            """.formatted(clase.getName());
            
        String respuesta = ORACULO.enviarPromptTecnico(prompt);
        System.out.println("\n🎯 PLAN DE MEJORAS:");
        System.out.println("─".repeat(50));
        System.out.println(respuesta != null ? respuesta : "❌ No se pudo generar plan");
        System.out.println("─".repeat(50));
    }

    private static void guardarEnBitacora(String nombrePirata, String conversacion) {
        try {
            String entrada = """
                
                ## Conversación con %s
                **Fecha**: %s
                
                %s
                
                ---
                """.formatted(nombrePirata, new Date(), conversacion);
                
            FileUtils.appendToFile("autogen-output/bitacora-conversaciones.md", entrada);
        } catch (Exception e) {
            System.err.println("⚠️ Error guardando en bitácora: " + e.getMessage());
        }
    }
}