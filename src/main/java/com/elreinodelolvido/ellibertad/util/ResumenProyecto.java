package com.elreinodelolvido.ellibertad.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.elreinodelolvido.ellibertad.model.ClassInfo;
import com.elreinodelolvido.ellibertad.scanner.ProjectScanner;

/**
 * ResumenProyecto — genera resúmenes técnicos y narrativos del sistema actual.
 * 🏴‍☠️ VERSIÓN MEJORADA: Con más estadísticas y narrativa épica.
 */
public class ResumenProyecto {

    /**
     * 📊 Genera resumen técnico del proyecto
     */
    public static String generarResumen() {
        ProjectScanner scanner = new ProjectScanner();
        scanner.scanProject("./");
        List<ClassInfo> clases = scanner.getClasses();

        StringBuilder sb = new StringBuilder();
        sb.append("# 📊 RESUMEN TÉCNICO DEL PROYECTO\n\n");
        
        // Estadísticas generales
        Map<String, Long> clasesPorPaquete = clases.stream()
            .collect(Collectors.groupingBy(ClassInfo::getPackageName, Collectors.counting()));
        
        long clasesPublicas = clases.stream()
            .filter(c -> c.getName() != null && Character.isUpperCase(c.getName().charAt(0)))
            .count();

        sb.append("## 📈 ESTADÍSTICAS\n");
        sb.append("- **Total de clases**: ").append(clases.size()).append("\n");
        sb.append("- **Clases públicas**: ").append(clasesPublicas).append("\n");
        sb.append("- **Paquetes distintos**: ").append(clasesPorPaquete.size()).append("\n");
        
        // Paquetes más grandes
        sb.append("\n## 🗂️ PAQUETES PRINCIPALES\n");
        clasesPorPaquete.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> sb.append("- ").append(entry.getKey())
                              .append(": ").append(entry.getValue()).append(" clases\n"));

        sb.append("\n## 📋 LISTA COMPLETA DE CLASES\n");
        for (ClassInfo clase : clases) {
            sb.append("- `").append(clase.getName()).append("`")
              .append(" → ").append(clase.getPackageName()).append("\n");
        }

        return sb.toString();
    }

    /**
     * 🏴‍☠️ Genera resumen narrativo épico estilo pirata
     */
    public static String generarResumenPirata() {
        ProjectScanner scanner = new ProjectScanner();
        scanner.scanProject("./");
        List<ClassInfo> clases = scanner.getClasses();

        StringBuilder historia = new StringBuilder();
        String fechaFormateada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        
        historia.append("## 📜 BITÁCORA OFICIAL DEL BARCO 'LIBERTAD'\n");
        historia.append("**Fecha de navegación:** ").append(fechaFormateada).append("\n");
        historia.append("**Capitán al mando:** Sistema AUTOGEN\n\n");

        // 🎭 INTRODUCCIÓN ÉPICA
        historia.append(generarIntroduccionEpica(clases.size()));

        // 👥 TRIPULACIÓN ACTUAL
        historia.append(generarTripulacion(clases));

        // 📊 ESTADÍSTICAS NARRATIVAS
        historia.append(generarEstadisticasNarrativas(clases));

        // 🗺️ ESTADO ACTUAL DEL VIAJE
        historia.append(generarEstadoViaje());

        // 🐙 KRAKENS ENFRENTADOS
        historia.append(generarReporteKrakens());

        // 🎯 PRÓXIMOS OBJETIVOS
        historia.append(generarProximosObjetivos());

        historia.append("\n---\n");
        historia.append("📚 *Fin de esta entrada de bitácora. ¡Que los vientos del código nos sean favorables!*\n");
        historia.append("🏴‍☠️ *— El Oráculo de la Libertad*\n");

        // Guardar en bitácora
        guardarEnBitacora(historia.toString());
        
        return historia.toString();
    }

    /**
     * 🎭 Genera introducción épica basada en el tamaño del proyecto
     */
    private static String generarIntroduccionEpica(int totalClases) {
        if (totalClases < 10) {
            return "⚓ **El barco 'Libertad' zarpa con una tripulación modesta pero valiente...**\n\n";
        } else if (totalClases < 30) {
            return "⚓ **El 'Libertad' navega con una tripulación experimentada por mares conocidos...**\n\n";
        } else {
            return "⚓ **El imponente 'Libertad', con su vasta tripulación, domina los mares del código...**\n\n";
        }
    }

    /**
     * 👥 Genera lista de tripulación con roles narrativos
     */
    private static String generarTripulacion(List<ClassInfo> clases) {
        StringBuilder tripulacion = new StringBuilder();
        tripulacion.append("## 👥 TRIPULACIÓN ACTUAL\n\n");

        // Agrupar por paquete (gremio pirata)
        Map<String, List<ClassInfo>> clasesPorPaquete = clases.stream()
            .collect(Collectors.groupingBy(ClassInfo::getPackageName));

        for (Map.Entry<String, List<ClassInfo>> gremio : clasesPorPaquete.entrySet()) {
            String nombreGremio = obtenerNombreGremio(gremio.getKey());
            tripulacion.append("### 🏴‍☠️ ").append(nombreGremio).append("\n");
            tripulacion.append("*Gremio especializado en: `").append(gremio.getKey()).append("`*\n\n");

            for (ClassInfo clase : gremio.getValue()) {
                String nombrePirata = DiccionarioDeTripulacion.nombrePirata(clase);
                String rol = asignarRolNarrativo(clase);
                
                tripulacion.append("- **").append(nombrePirata).append("**")
                          .append(" (`").append(clase.getName()).append("`)")
                          .append(" - ").append(rol).append("\n");
            }
            tripulacion.append("\n");
        }

        return tripulacion.toString();
    }

    /**
     * 📊 Genera estadísticas con narrativa pirata
     */
    private static String generarEstadisticasNarrativas(List<ClassInfo> clases) {
        long totalClases = clases.size();
        long paquetesUnicos = clases.stream().map(ClassInfo::getPackageName).distinct().count();
        
        // Clases por tipo (estimado por nombre)
        long servicios = clases.stream().filter(c -> c.getName().toLowerCase().contains("service")).count();
        long utilidades = clases.stream().filter(c -> c.getName().toLowerCase().contains("util")).count();
        long modelos = clases.stream().filter(c -> c.getName().toLowerCase().contains("model")).count();

        return String.format("""
            ## 📊 INVENTARIO DEL BOTÍN
            
            ¡La tripulación ha acumulado un tesoro de conocimientos!
            
            - 🗃️ **%.0f mapas del conocimiento** (clases totales)
            - 📦 **%.0f cofres especializados** (paquetes únicos)
            - ⚓ **%.0f navegantes** (servicios)
            - 🛠️ **%.0f artesanos** (utilidades)  
            - 🎭 **%.0f embajadores** (modelos)
            
            """, (double) totalClases, (double) paquetesUnicos, 
            (double) servicios, (double) utilidades, (double) modelos);
    }

    /**
     * 🗺️ Genera estado actual del viaje
     */
    private static String generarEstadoViaje() {
        return """
            ## 🗺️ ESTADO ACTUAL DEL VIAJE
            
            El barco 'Libertad' navega con determinación a través del:
            
            ### 🌊 **Mar de la Refactorización**
            - Análisis continuo de la tripulación existente
            - Mejoras constantes en las velas del código
            - Optimización de rutas de navegación (métodos)
            
            ### ⚡ **Corrientes de la Generación**
            - Nuevos tripulantes siendo reclutados automáticamente
            - Mapas del tesoro (planes) generados por el oráculo
            - Integración fluida de nuevas habilidades
            
            ### 🏝️ **Islas de la Estabilidad**
            - Sistema de rollback listo para tormentas (Caronte)
            - Bitácora actualizada en tiempo real
            - Auditorías periódicas de la integridad del barco
            
            """;
    }

    /**
     * 🐙 Genera reporte de krakens (errores) enfrentados
     */
    private static String generarReporteKrakens() {
        StringBuilder krakens = new StringBuilder();
        krakens.append("## 🐙 ENFRENTAMIENTOS CON KRAKENS\n\n");

        try {
            String contenidoKrakens = FileUtils.readFileIfExists("autogen-output/krakens/log-krakens.md");
            if (contenidoKrakens != null && !contenidoKrakens.trim().isEmpty()) {
                // Contar krakens por líneas que contengan "❌" o "💥"
                long totalKrakens = contenidoKrakens.lines()
                    .filter(line -> line.contains("❌") || line.contains("💥") || line.contains("Kraken"))
                    .count();
                
                if (totalKrakens > 0) {
                    krakens.append("**¡La tripulación ha enfrentado ").append(totalKrakens)
                           .append(" krakens en esta travesía!**\n\n");
                    krakens.append("Algunos de los encuentros más memorables:\n");
                    
                    // Tomar algunos ejemplos
                    contenidoKrakens.lines()
                        .filter(line -> line.contains("❌") || line.contains("💥"))
                        .limit(3)
                        .forEach(line -> krakens.append("- ").append(line).append("\n"));
                } else {
                    krakens.append("**¡Aguas tranquilas! No se han avistado krakens recientemente.**\n");
                    krakens.append("La tripulación navega con confianza y sin contratiempos.\n");
                }
            } else {
                krakens.append("**No hay registros de krakens en esta travesía.**\n");
                krakens.append("¡El barco navega en aguas pacíficas del código!\n");
            }
        } catch (Exception e) {
            krakens.append("⚠️ *Los registros de krakens están temporalmente indescifrables...*\n");
        }

        krakens.append("\n");
        return krakens.toString();
    }

    /**
     * 🎯 Genera sección de próximos objetivos
     */
    private static String generarProximosObjetivos() {
        return """
            ## 🎯 PRÓXIMOS OBJETIVOS
            
            El capitán ha establecido estas coordenadas para la navegación:
            
            - 🧭 **Seguir los mapas del tesoro** en `software/plan.md`
            - ⚡ **Generar nuevos tripulantes** según los objetivos
            - 🔄 **Revisar y aprobar** mejoras en la tripulación existente
            - 📖 **Actualizar esta bitácora** con cada nueva travesía
            - 🐙 **Enfrentar valientemente** cualquier kraken que aparezca
            
            ¡Rumbo a nuevas aventuras en el Reino del Olvido!
            """;
    }

    /**
     * 🏴‍☠️ Asigna nombres épicos a los paquetes
     */
    private static String obtenerNombreGremio(String nombrePaquete) {
        if (nombrePaquete.contains("api")) return "Consejo de los Oráculos";
        if (nombrePaquete.contains("util")) return "Gremio de los Artesanos";
        if (nombrePaquete.contains("model")) return "Círculo de los Embajadores";
        if (nombrePaquete.contains("scanner")) return "Orden de los Vigías";
        if (nombrePaquete.contains("tripulacion")) return "Hermandad de la Tripulación";
        return "Gremio de " + nombrePaquete.substring(nombrePaquete.lastIndexOf('.') + 1);
    }

    /**
     * 🎭 Asigna rol narrativo basado en características de la clase
     */
    private static String asignarRolNarrativo(ClassInfo clase) {
        String nombre = clase.getName().toLowerCase();
        
        if (nombre.contains("manager") || nombre.contains("controller")) {
            return "comanda una sección crucial del navío";
        } else if (nombre.contains("service")) {
            return "presta servicios esenciales a la tripulación";
        } else if (nombre.contains("util")) {
            return "forja herramientas y artefactos mágicos";
        } else if (nombre.contains("model") || nombre.contains("entity")) {
            return "representa conceptos fundamentales del reino";
        } else if (nombre.contains("api") || nombre.contains("client")) {
            return "comunica con entidades externas y oráculos";
        } else if (nombre.contains("scanner") || nombre.contains("auditor")) {
            return "vigila los mares en busca de peligros y oportunidades";
        } else {
            return "desempeña un rol vital en la tripulación";
        }
    }

    /**
     * 💾 Guarda la bitácora en archivo
     */
    private static void guardarEnBitacora(String contenido) {
        try {
            // Crear entrada con separador
            String entrada = "\n" + "=".repeat(80) + "\n" + contenido + "\n";
            FileUtils.appendToFile("autogen-output/bitacora-pirata.md", entrada);
            System.out.println("📜 Bitácora pirata actualizada en: autogen-output/bitacora-pirata.md");
        } catch (Exception e) {
            System.err.println("⚠️ Error guardando bitácora: " + e.getMessage());
        }
    }
}
