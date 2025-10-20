package com.elreinodelolvido.ellibertad.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Scanner;

import com.elreinodelolvido.ellibertad.model.ClassInfo;

/**
 * RevisorInteractivo — Sistema de revisión y aprobación de refactors.
 * 🎯 Ahora con más opciones y mejor manejo de errores.
 */
public class RevisorInteractivo {

    private static final Scanner scanner = new Scanner(System.in);

    public static void revisar(ClassInfo classInfo) {
        String className = classInfo.getName();
        String packagePath = classInfo.getPackageName().replace('.', '/');
        String basePath = "autogen-output/refactor/" + packagePath + "/" + className;
        
        String refactorPath = basePath + ".refactor.java";
        String diffPath = basePath + ".diff.txt";
        String checkPath = basePath + ".check.txt";
        String originalPath = classInfo.getSourcePath();

        System.out.println("\n🎯 REVISIÓN INTERACTIVA: " + className);
        System.out.println("📁 Paquete: " + classInfo.getPackageName());

        // Verificar que existen los archivos necesarios
        if (!verificarArchivosRevisión(originalPath, refactorPath, diffPath)) {
            return;
        }

        // 1️⃣ Mostrar validación de firmas primero
        mostrarValidacionFirmas(checkPath);

        // 2️⃣ Mostrar comparación visual
        mostrarComparacionLadoALado(originalPath, refactorPath);

        // 3️⃣ Mostrar diff detallado
        mostrarDiffCompleto(diffPath);

        // 4️⃣ Tomar decisión
        tomarDecision(classInfo, refactorPath, originalPath);
    }

    /**
     * ✅ Verifica que existan todos los archivos necesarios para la revisión
     */
    private static boolean verificarArchivosRevisión(String originalPath, String refactorPath, String diffPath) {
        File original = new File(originalPath);
        File refactor = new File(refactorPath);
        File diff = new File(diffPath);

        if (!original.exists()) {
            System.err.println("❌ No se encuentra archivo original: " + originalPath);
            return false;
        }
        if (!refactor.exists()) {
            System.err.println("❌ No se encuentra archivo refactorizado: " + refactorPath);
            return false;
        }
        if (!diff.exists()) {
            System.err.println("❌ No se encuentra archivo diff: " + diffPath);
            return false;
        }

        return true;
    }

    /**
     * ✅ Muestra validación de firmas (si existe)
     */
    private static void mostrarValidacionFirmas(String checkPath) {
        try {
            if (new File(checkPath).exists()) {
                String validacion = Files.readString(Path.of(checkPath));
                System.out.println("\n🔍 VALIDACIÓN DE FIRMAS:");
                System.out.println(validacion.contains("✅") ? "✅ FIRMAS VÁLIDAS" : "⚠️ PROBLEMAS EN FIRMAS");
                if (validacion.contains("❌") || validacion.contains("⚠️")) {
                    System.out.println(validacion);
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error mostrando validación: " + e.getMessage());
        }
    }

    /**
     * 🎨 Comparación lado a lado mejorada con resaltado
     */
    private static void mostrarComparacionLadoALado(String originalPath, String refactorPath) {
        try {
            List<String> originalLines = Files.readAllLines(Path.of(originalPath));
            List<String> refactorLines = Files.readAllLines(Path.of(refactorPath));

            int maxLines = Math.max(originalLines.size(), refactorLines.size());
            int width = 55;

            System.out.println("\n📊 COMPARACIÓN LADO A LADO");
            System.out.println("╔" + "═".repeat(width) + "╦" + "═".repeat(width) + "╗");
            System.out.printf("║ %-53s ║ %-53s ║\n", "ORIGINAL", "REFACTOR");
            System.out.println("╠" + "═".repeat(width) + "╬" + "═".repeat(width) + "╣");

            int cambiosDetectados = 0;
            for (int i = 0; i < maxLines; i++) {
                String left = i < originalLines.size() ? originalLines.get(i) : "";
                String right = i < refactorLines.size() ? refactorLines.get(i) : "";
                
                boolean esCambio = !left.equals(right) && !left.trim().isEmpty() && !right.trim().isEmpty();
                String indicador = esCambio ? "🔁" : "  ";
                
                if (esCambio) cambiosDetectados++;

                System.out.printf("║ %s %-52s ║ %-53s ║\n", 
                    indicador, trunc(left, 52), trunc(right, 53));
            }

            System.out.println("╚" + "═".repeat(width) + "╩" + "═".repeat(width) + "╝");
            System.out.println("📈 " + cambiosDetectados + " líneas con cambios detectadas");

        } catch (Exception e) {
            System.err.println("⚠️ Error en comparación: " + e.getMessage());
        }
    }

    /**
     * 📄 Muestra el diff completo con formato mejorado
     */
    private static void mostrarDiffCompleto(String diffPath) {
        try {
            String diff = Files.readString(Path.of(diffPath));
            
            System.out.println("\n🔧 DIFF DETALLADO:");
            System.out.println("─".repeat(80));
            
            // Mostrar solo las partes con cambios (no el diff completo si es muy largo)
            String[] lineas = diff.split("\n");
            int lineasMostradas = 0;
            boolean mostrandoCambio = false;
            
            for (String linea : lineas) {
                if (linea.startsWith("🎯") || linea.startsWith("📍") || linea.startsWith("🎪")) {
                    System.out.println(linea);
                    mostrandoCambio = true;
                    lineasMostradas++;
                } else if ((linea.startsWith("-") || linea.startsWith("+")) && mostrandoCambio) {
                    System.out.println(linea);
                    lineasMostradas++;
                } else if (linea.trim().isEmpty() && mostrandoCambio) {
                    System.out.println();
                    mostrandoCambio = false;
                }
                
                // Limitar salida para no saturar consola
                if (lineasMostradas > 50) {
                    System.out.println("... (diff truncado, ver archivo completo para más detalles)");
                    break;
                }
            }
            
            System.out.println("─".repeat(80));

        } catch (Exception e) {
            System.err.println("⚠️ Error mostrando diff: " + e.getMessage());
        }
    }

    /**
     * 🎯 Sistema de decisión mejorado con más opciones
     */
    private static void tomarDecision(ClassInfo classInfo, String refactorPath, String originalPath) {
        String className = classInfo.getName();
        
        while (true) {
            System.out.println("\n🎯 ¿QUÉ DESEAS HACER CON " + className + "?");
            System.out.println("1. ✅ Aprobar e integrar refactor");
            System.out.println("2. ❌ Rechazar cambios");
            System.out.println("3. 📝 Ver archivo refactor completo");
            System.out.println("4. 📖 Ver archivo original completo"); 
            System.out.println("5. ⏭️  Saltar esta clase");
            System.out.print("👉 Elige una opción (1-5): ");

            String opcion = scanner.nextLine().trim();
            
            switch (opcion) {
                case "1":
                    aprobar(classInfo);
                    return;
                case "2":
                    rechazar(className);
                    return;
                case "3":
                    mostrarArchivoCompleto(refactorPath, "REFACTOR");
                    break;
                case "4":
                    mostrarArchivoCompleto(originalPath, "ORIGINAL");
                    break;
                case "5":
                    System.out.println("⏭️ Saltando clase: " + className);
                    return;
                default:
                    System.out.println("❌ Opción no válida. Intenta de nuevo.");
            }
        }
    }

    /**
     * 📖 Muestra archivo completo
     */
    private static void mostrarArchivoCompleto(String filePath, String tipo) {
        try {
            List<String> lineas = Files.readAllLines(Path.of(filePath));
            System.out.println("\n📄 " + tipo + " - " + filePath);
            System.out.println("═".repeat(80));
            
            for (int i = 0; i < lineas.size(); i++) {
                System.out.printf("%3d │ %s\n", i + 1, lineas.get(i));
            }
            
            System.out.println("═".repeat(80));
            System.out.println("📊 Total líneas: " + lineas.size());
            
        } catch (Exception e) {
            System.err.println("❌ Error mostrando archivo: " + e.getMessage());
        }
    }

    /**
     * ✅ Aprobación mejorada con más verificaciones
     */
    private static void aprobar(ClassInfo classInfo) {
        String className = classInfo.getName();
        String packagePath = classInfo.getPackageName().replace('.', '/');
        String refactorPath = "autogen-output/refactor/" + packagePath + "/" + className + ".refactor.java";
        String destino = "autogen-output/aprobados/" + packagePath + "/" + className + ".java";

        try {
            // 1️⃣ Verificar que el refactor existe
            if (!new File(refactorPath).exists()) {
                System.err.println("❌ No se encuentra el archivo refactorizado: " + refactorPath);
                return;
            }

            // 2️⃣ Copiar a carpeta aprobados
            File destFile = new File(destino);
            destFile.getParentFile().mkdirs();
            Files.copy(Path.of(refactorPath), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ Clase aprobada y copiada a: " + destino);

            // 3️⃣ Intentar integración (si existe el integrador)
            intentarIntegracion(classInfo);

            // 4️⃣ Registrar en bitácora
            FileUtils.appendToFile("autogen-output/bitacora-aprobaciones.md", 
                "- ✅ " + className + " - " + new java.util.Date() + "\n");

        } catch (Exception e) {
            System.err.println("❌ Error aprobando clase: " + e.getMessage());
        }
    }

    /**
     * 🔄 Intenta integrar la clase (con fallback si no existe el integrador)
     */
    private static void intentarIntegracion(ClassInfo classInfo) {
        try {
            // Intentar usar IntegradorForzado si existe
            Class<?> integradorClass = Class.forName("com.novelator.autogen.scanner.IntegradorForzado");
            var method = integradorClass.getMethod("integrar", String.class, String.class, String.class);
            method.invoke(null, classInfo.getName(), classInfo.getPackageName().replace('.', '/'), classInfo.getSourcePath());
            System.out.println("🔧 Integración automática completada");
        } catch (ClassNotFoundException e) {
            System.out.println("ℹ️ IntegradorForzado no disponible - Clase guardada en aprobados");
        } catch (Exception e) {
            System.err.println("⚠️ Error en integración automática: " + e.getMessage());
            System.out.println("💾 Clase guardada en aprobados para integración manual");
        }
    }

    /**
     * ❌ Rechazo mejorado con opción de comentario
     */
    private static void rechazar(String className) {
        System.out.print("📝 ¿Razón del rechazo? (opcional): ");
        String razon = scanner.nextLine().trim();
        
        String registro = "- ❌ " + className + " - " + new java.util.Date();
        if (!razon.isEmpty()) {
            registro += " - Razón: " + razon;
        }
        
        try {
            FileUtils.appendToFile("autogen-output/bitacora-rechazos.md", registro + "\n");
            System.out.println("🚫 Clase rechazada y registrada");
        } catch (Exception e) {
            System.err.println("❌ Error registrando rechazo: " + e.getMessage());
        }
    }

    private static String trunc(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max - 3) + "..." : s;
    }
}