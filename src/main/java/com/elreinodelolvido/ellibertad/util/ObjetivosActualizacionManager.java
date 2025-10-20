package com.elreinodelolvido.ellibertad.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 🎯 OBJETIVOS_TURBO — Gestor épico de objetivos de actualización.
 * ⚡ Ahora con historial, categorías, prioridades y +500% de organización pirata.
 */
public class ObjetivosActualizacionManager {

    private static final String OBJETIVOS_DIR = "autogen-output/objetivos/";
    private static final String OBJETIVOS_ACTUALES = OBJETIVOS_DIR + "objetivos-actuales.md";
    private static final String HISTORIAL_OBJETIVOS = OBJETIVOS_DIR + "historial-objetivos.md";
    private static final String ESTADISTICAS_OBJETIVOS = OBJETIVOS_DIR + "estadisticas.json";
    
    private static final Scanner scanner = new Scanner(System.in);
    
    // 📊 ESTADÍSTICAS TURBO
    private static final AtomicInteger CONTADOR_OBJETIVOS = new AtomicInteger(0);
    private static final AtomicInteger CONTADOR_COMPLETADOS = new AtomicInteger(0);
    private static final Map<String, Integer> CATEGORIAS = new HashMap<>();

    static {
        // Inicializar categorías épicas
        CATEGORIAS.put("🏗️ ARQUITECTURA", 0);
        CATEGORIAS.put("⚡ PERFORMANCE", 0);
        CATEGORIAS.put("🔧 REFACTOR", 0);
        CATEGORIAS.put("🎨 UI/UX", 0);
        CATEGORIAS.put("🐛 BUGFIX", 0);
        CATEGORIAS.put("📈 FEATURE", 0);
        CATEGORIAS.put("🏴‍☠️ NARRATIVA", 0);
    }

    /**
     * 🎪 MENÚ TURBO DE OBJETIVOS
     */
    public static void mostrarMenu() {
        System.out.println("\n" + "🎯".repeat(50));
        System.out.println("           GESTOR TURBO DE OBJETIVOS PIRATAS");
        System.out.println("🎯".repeat(50));

        while (true) {
            System.out.println("\n🎯 ¿QUÉ DESEAS HACER, CAPITÁN?");
            System.out.println("1️⃣  📜 Ver objetivos actuales");
            System.out.println("2️⃣  📝 Agregar nuevo objetivo épico");
            System.out.println("3️⃣  🏷️  Gestionar categorías");
            System.out.println("4️⃣  ✅ Marcar objetivo como completado");
            System.out.println("5️⃣  📊 Ver estadísticas épicas");
            System.out.println("6️⃣  🗑️  Limpiar objetivos completados");
            System.out.println("7️⃣  📖 Ver historial de objetivos");
            System.out.println("0️⃣  ⚓ Volver al menú principal");
            System.out.print("👉 Tu orden: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": verObjetivosTurbo(); break;
                case "2": agregarObjetivoTurbo(); break;
                case "3": gestionarCategorias(); break;
                case "4": marcarCompletado(); break;
                case "5": mostrarEstadisticasEpicas(); break;
                case "6": limpiarObjetivosCompletados(); break;
                case "7": verHistorialObjetivos(); break;
                case "0": 
                    System.out.println("⚓ Volviendo al puente de mando...");
                    return;
                case "debug": mostrarDebugTurbo(); break;
                default: 
                    System.out.println("❌ Orden no reconocida, grumete. Intenta de nuevo.");
            }
        }
    }

    /**
     * 📜 VISUALIZACIÓN TURBO DE OBJETIVOS
     */
    private static void verObjetivosTurbo() {
        try {
            if (!Files.exists(Path.of(OBJETIVOS_ACTUALES))) {
                System.out.println("🫥 No hay objetivos en el mapa del tesoro.");
                return;
            }

            List<String> lineas = Files.readAllLines(Path.of(OBJETIVOS_ACTUALES));
            if (lineas.isEmpty() || (lineas.size() == 1 && lineas.get(0).trim().isEmpty())) {
                System.out.println("🫥 El cofre de objetivos está vacío.");
                return;
            }

            System.out.println("\n📜 MAPA DEL TESORO - OBJETIVOS ACTUALES");
            System.out.println("=".repeat(80));
            
            int contador = 1;
            for (String linea : lineas) {
                if (linea.trim().isEmpty() || linea.startsWith("#")) continue;
                
                if (linea.startsWith("- [x]")) {
                    System.out.printf("%2d. ✅ %s\n", contador++, linea.substring(5).trim());
                } else if (linea.startsWith("- [ ]")) {
                    System.out.printf("%2d. 🎯 %s\n", contador++, linea.substring(5).trim());
                } else {
                    System.out.printf("%2d. 📌 %s\n", contador++, linea.trim());
                }
            }
            
            System.out.println("=".repeat(80));
            System.out.println("📊 Total: " + (contador - 1) + " objetivos en el mapa");

        } catch (IOException e) {
            System.err.println("💥 Error leyendo el mapa del tesoro: " + e.getMessage());
        }
    }

    /**
     * 📝 AGREGADO TURBO DE OBJETIVOS
     */
    private static void agregarObjetivoTurbo() {
        System.out.println("\n📝 CREANDO NUEVO OBJETIVO ÉPICO");
        System.out.println("─".repeat(40));

        // 🎯 DESCRIPCIÓN
        System.out.print("🎯 Descripción del objetivo: ");
        String descripcion = scanner.nextLine().trim();
        
        if (descripcion.isEmpty()) {
            System.out.println("❌ Un objetivo sin descripción es como un barco sin timón.");
            return;
        }

        // 🏷️ CATEGORÍA
        String categoria = seleccionarCategoria();
        
        // 🚨 PRIORIDAD
        String prioridad = seleccionarPrioridad();
        
        // ⏱️ ESTIMACIÓN
        System.out.print("⏱️ Estimación (ej: 2h, 1d, 30min): ");
        String estimacion = scanner.nextLine().trim();
        if (estimacion.isEmpty()) estimacion = "No estimado";

        // 🎪 CONSTRUIR OBJETIVO ÉPICO
        String objetivoFormateado = String.format("- [ ] **%s** %s | 🏷️ %s | 🚨 %s | ⏱️ %s | 📅 %s",
                prioridad, descripcion, categoria, prioridad, estimacion, 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd HH:mm")));

        // 💾 GUARDAR OBJETIVO
        try {
            FileUtils.ensureParentDirectoryExists(OBJETIVOS_ACTUALES);
            FileUtils.appendToFile(OBJETIVOS_ACTUALES, objetivoFormateado + "\n");
            
            // 📊 ACTUALIZAR ESTADÍSTICAS
            CONTADOR_OBJETIVOS.incrementAndGet();
            CATEGORIAS.put(categoria, CATEGORIAS.getOrDefault(categoria, 0) + 1);
            
            // 📖 REGISTRAR EN HISTORIAL
            registrarEnHistorial("AGREGADO", descripcion, categoria);
            
            System.out.println("✅ ¡OBJETIVO ÉPICO AGREGADO AL MAPA!");
            System.out.println("🎯 " + descripcion);
            System.out.println("🏷️ Categoría: " + categoria);
            System.out.println("🚨 Prioridad: " + prioridad);

        } catch (Exception e) {
            System.err.println("💥 Error guardando objetivo: " + e.getMessage());
        }
    }

    /**
     * 🏷️ SELECCIÓN TURBO DE CATEGORÍA
     */
    private static String seleccionarCategoria() {
        System.out.println("\n🏷️ SELECCIONA CATEGORÍA:");
        List<String> categorias = new ArrayList<>(CATEGORIAS.keySet());
        
        for (int i = 0; i < categorias.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, categorias.get(i));
        }
        System.out.printf("%d. ✏️ Crear nueva categoría\n", categorias.size() + 1);
        System.out.print("👉 Elige opción: ");

        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            if (opcion >= 1 && opcion <= categorias.size()) {
                return categorias.get(opcion - 1);
            } else if (opcion == categorias.size() + 1) {
                System.out.print("🎭 Nombre de nueva categoría: ");
                String nueva = scanner.nextLine().trim();
                if (!nueva.isEmpty()) {
                    String categoriaCompleta = "🎪 " + nueva.toUpperCase();
                    CATEGORIAS.put(categoriaCompleta, 0);
                    return categoriaCompleta;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Usando categoría por defecto.");
        }

        return "🎪 VARIOS";
    }

    /**
     * 🚨 SELECCIÓN TURBO DE PRIORIDAD
     */
    private static String seleccionarPrioridad() {
        System.out.println("\n🚨 SELECCIONA PRIORIDAD:");
        System.out.println("1. 🔴 ALTA - Crítico para la misión");
        System.out.println("2. 🟡 MEDIA - Importante pero no urgente");
        System.out.println("3. 🟢 BAJA - Mejora o optimización");
        System.out.print("👉 Elige prioridad (1-3): ");

        String opcion = scanner.nextLine().trim();
        return switch (opcion) {
            case "1" -> "🔴 ALTA";
            case "2" -> "🟡 MEDIA";
            case "3" -> "🟢 BAJA";
            default -> "⚪ NO DEFINIDA";
        };
    }

    /**
     * ✅ MARCADO TURBO DE COMPLETADO
     */
    private static void marcarCompletado() {
        try {
            List<String> objetivos = Files.readAllLines(Path.of(OBJETIVOS_ACTUALES));
            if (objetivos.isEmpty()) {
                System.out.println("🫥 No hay objetivos para completar.");
                return;
            }

            System.out.println("\n✅ MARCAR OBJETIVO COMO COMPLETADO");
            List<String> objetivosPendientes = new ArrayList<>();
            int index = 1;

            for (String objetivo : objetivos) {
                if (objetivo.startsWith("- [ ]")) {
                    System.out.printf("%2d. %s\n", index++, objetivo.substring(5).trim());
                    objetivosPendientes.add(objetivo);
                }
            }

            if (objetivosPendientes.isEmpty()) {
                System.out.println("🎉 ¡Todos los objetivos están completados!");
                return;
            }

            System.out.print("👉 Número del objetivo completado (0 para cancelar): ");
            try {
                int seleccion = Integer.parseInt(scanner.nextLine().trim());
                if (seleccion > 0 && seleccion <= objetivosPendientes.size()) {
                    String objetivoCompletado = objetivosPendientes.get(seleccion - 1);
                    String nuevoObjetivo = objetivoCompletado.replace("- [ ]", "- [x]") + " ✅ " + 
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
                    
                    // 🔄 REEMPLAZAR EN LISTA
                    objetivos.set(objetivos.indexOf(objetivoCompletado), nuevoObjetivo);
                    
                    // 💾 GUARDAR
                    Files.write(Path.of(OBJETIVOS_ACTUALES), objetivos);
                    
                    // 📊 ACTUALIZAR ESTADÍSTICAS
                    CONTADOR_COMPLETADOS.incrementAndGet();
                    
                    // 📖 REGISTRAR EN HISTORIAL
                    registrarEnHistorial("COMPLETADO", objetivoCompletado.substring(5).trim(), "");
                    
                    System.out.println("🎉 ¡OBJETIVO COMPLETADO! ¡La tripulación celebra!");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Selección inválida.");
            }

        } catch (IOException e) {
            System.err.println("💥 Error marcando objetivo: " + e.getMessage());
        }
    }

    /**
     * 🏷️ GESTIÓN TURBO DE CATEGORÍAS
     */
    private static void gestionarCategorias() {
        System.out.println("\n🏷️ GESTIÓN DE CATEGORÍAS ÉPICAS");
        System.out.println("─".repeat(40));
        
        CATEGORIAS.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> 
                    System.out.printf("%-20s: %2d objetivos\n", entry.getKey(), entry.getValue()));
        
        System.out.println("─".repeat(40));
        System.out.println("📊 Total categorías: " + CATEGORIAS.size());
    }

    /**
     * 📊 ESTADÍSTICAS TURBO ÉPICAS
     */
    private static void mostrarEstadisticasEpicas() {
        int totalObjetivos = CONTADOR_OBJETIVOS.get();
        int completados = CONTADOR_COMPLETADOS.get();
        double tasaCompletitud = totalObjetivos > 0 ? (double) completados / totalObjetivos * 100 : 0;
        
        System.out.println("\n📊 ESTADÍSTICAS ÉPICAS DE OBJETIVOS");
        System.out.println("⚡".repeat(50));
        System.out.printf("🎯 Objetivos creados: %d\n", totalObjetivos);
        System.out.printf("✅ Objetivos completados: %d\n", completados);
        System.out.printf("📈 Tasa de completitud: %.1f%%\n", tasaCompletitud);
        System.out.printf("🏷️ Categorías activas: %d\n", CATEGORIAS.size());
        
        // 🎯 CATEGORÍA MÁS POPULAR
        String categoriaPopular = CATEGORIAS.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey() + " (" + entry.getValue() + ")")
                .orElse("Ninguna");
        
        System.out.printf("🎪 Categoría más popular: %s\n", categoriaPopular);
        System.out.println("⚡".repeat(50));
        
        if (tasaCompletitud > 80) {
            System.out.println("🏆 ¡Rendimiento legendario, Capitán!");
        } else if (tasaCompletitud > 50) {
            System.out.println("⚡ ¡Buen progreso en la misión!");
        } else {
            System.out.println("💪 ¡Sigue navegando, la tripulación cree en ti!");
        }
    }

    /**
     * 🧹 LIMPIEZA TURBO DE OBJETIVOS COMPLETADOS
     */
    private static void limpiarObjetivosCompletados() {
        try {
            if (!Files.exists(Path.of(OBJETIVOS_ACTUALES))) {
                System.out.println("🫥 No hay objetivos que limpiar.");
                return;
            }

            List<String> objetivos = Files.readAllLines(Path.of(OBJETIVOS_ACTUALES));
            List<String> objetivosPendientes = objetivos.stream()
                    .filter(obj -> !obj.startsWith("- [x]"))
                    .collect(Collectors.toList());

            if (objetivosPendientes.size() == objetivos.size()) {
                System.out.println("🧹 No hay objetivos completados para limpiar.");
                return;
            }

            int eliminados = objetivos.size() - objetivosPendientes.size();
            Files.write(Path.of(OBJETIVOS_ACTUALES), objetivosPendientes);
            
            System.out.println("🧹 " + eliminados + " objetivos completados eliminados del mapa.");
            System.out.println("📋 " + objetivosPendientes.size() + " objetivos pendientes mantienen el rumbo.");

        } catch (IOException e) {
            System.err.println("💥 Error limpiando objetivos: " + e.getMessage());
        }
    }

    /**
     * 📖 VISUALIZACIÓN TURBO DE HISTORIAL
     */
    private static void verHistorialObjetivos() {
        try {
            if (!Files.exists(Path.of(HISTORIAL_OBJETIVOS))) {
                System.out.println("📜 El historial de objetivos está vacío.");
                return;
            }

            System.out.println("\n📖 HISTORIAL ÉPICO DE OBJETIVOS");
            System.out.println("=".repeat(80));
            Files.lines(Path.of(HISTORIAL_OBJETIVOS))
                    .forEach(System.out::println);
            System.out.println("=".repeat(80));

        } catch (IOException e) {
            System.err.println("💥 Error leyendo historial: " + e.getMessage());
        }
    }

    /**
     * 📝 REGISTRO TURBO EN HISTORIAL
     */
    private static void registrarEnHistorial(String accion, String descripcion, String categoria) {
        String entrada = String.format("- **%s** | %s | %s | %s\n",
                accion,
                descripcion.length() > 50 ? descripcion.substring(0, 47) + "..." : descripcion,
                categoria,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        FileUtils.appendToFile(HISTORIAL_OBJETIVOS, entrada);
    }

    /**
     * 🔧 DEBUG TURBO
     */
    private static void mostrarDebugTurbo() {
        System.out.println("\n🔧 DEBUG TURBO - OBJETIVOS");
        System.out.println("📁 OBJETIVOS_ACTUALES: " + OBJETIVOS_ACTUALES);
        System.out.println("📊 CONTADOR_OBJETIVOS: " + CONTADOR_OBJETIVOS.get());
        System.out.println("✅ CONTADOR_COMPLETADOS: " + CONTADOR_COMPLETADOS.get());
        System.out.println("🏷️ CATEGORIAS: " + CATEGORIAS.size());
        System.out.println("💾 DIR_EXISTS: " + Files.exists(Path.of(OBJETIVOS_DIR)));
    }

    /**
     * 🎯 OBTENCIÓN TURBO DE OBJETIVOS ACTUALES
     */
    public static String getObjetivosActuales() {
        try {
            if (Files.exists(Path.of(OBJETIVOS_ACTUALES))) {
                return Files.readString(Path.of(OBJETIVOS_ACTUALES));
            }
        } catch (IOException e) {
            System.err.println("⚠️ Error leyendo objetivos actuales: " + e.getMessage());
        }
        return "# 🎯 Objetivos de la Travesía\n\n- *No hay objetivos definidos actualmente*";
    }

    /**
     * 🆕 CARGA TURBO DE ESTADÍSTICAS AL INICIAR
     */
    public static void cargarEstadisticas() {
        try {
            if (Files.exists(Path.of(OBJETIVOS_ACTUALES))) {
                List<String> objetivos = Files.readAllLines(Path.of(OBJETIVOS_ACTUALES));
                long total = objetivos.stream().filter(line -> line.startsWith("- [")).count();
                long completados = objetivos.stream().filter(line -> line.startsWith("- [x]")).count();
                
                CONTADOR_OBJETIVOS.set((int) total);
                CONTADOR_COMPLETADOS.set((int) completados);
                
                System.out.println("📊 Estadísticas de objetivos cargadas: " + total + " objetivos");
            }
        } catch (IOException e) {
            System.out.println("⚠️ No se pudieron cargar estadísticas de objetivos previos");
        }
    }
}
