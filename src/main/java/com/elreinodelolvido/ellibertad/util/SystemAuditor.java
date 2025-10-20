package com.elreinodelolvido.ellibertad.util;

import java.io.IOException;
import java.util.List;

public class SystemAuditor {

    private static final String COMPILATION_COMMAND = "javac -d ./bin $(find ./src -name '*.java')";
    private static final String AUTOGEN_OUTPUT_PATH = "autogen-output/nuevas/";
    private static final String JAVA_EXTENSION = ".java";
    private static final String PACKAGE_PREFIX = "com.novelator.";

    /**
     * Punto de entrada principal para la auditoría final en NOVELATOR.
     */
    public static void ejecutarAuditoriaFinal() {
        executeFinalAudit();
    }

    private static void executeFinalAudit() {
        System.out.println("\n🧪 Checking system integrity after update...");

        boolean isCompilationSuccessful = testCompilation();
        boolean areClassesLoaded = testClassesLoaded();

        if (isCompilationSuccessful && areClassesLoaded) {
            System.out.println("✅ System verified. Everything seems in order, captain.");
        } else {
            System.out.println("⚠️ Problems detected during the final audit.");
        }
    }

    private static boolean testCompilation() {
        System.out.println("🔧 Running compilation with 'javac'...");
        try {
            Process process = Runtime.getRuntime().exec(COMPILATION_COMMAND);
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Compilation error: " + e.getMessage());
            return false;
        }
    }

    private static boolean testClassesLoaded() {
        System.out.println("📦 Validating load of generated classes...");
        try {
            List<String> newClasses = FileUtils.listFiles(AUTOGEN_OUTPUT_PATH, JAVA_EXTENSION);
            if (newClasses.isEmpty()) {
                System.out.println("ℹ️ No new classes to validate load.");
                return true;
            }

            for (String path : newClasses) {
                String className = extractClassName(path);
                try {
                    Class.forName(className);
                    System.out.println("✅ Class loaded: " + className);
                } catch (ClassNotFoundException e) {
                    System.out.println("❌ Could not load: " + className);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error validating new classes: " + e.getMessage());
            return false;
        }
    }

    private static String extractClassName(String filePath) {
        String name = filePath
                .replace(AUTOGEN_OUTPUT_PATH, "")
                .replace(JAVA_EXTENSION, "")
                .replace("/", ".")
                .replace("\\", ".");
        return PACKAGE_PREFIX + name;
    }
}