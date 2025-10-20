package com.elreinodelolvido.ellibertad.util;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 🎨 GENERADOR DE PDFS TURBOFULURADOS - CON FUENTES REPARADAS
 */
public class GeneradorPDFTurbo {

    // 🎨 PALETA DE COLORES ÉPICA
    private static final Color COLOR_PRIMARIO = new DeviceRgb(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new DeviceRgb(230, 126, 34);
    private static final Color COLOR_EXITO = new DeviceRgb(46, 204, 113);
    private static final Color COLOR_ERROR = new DeviceRgb(231, 76, 60);
    private static final Color COLOR_ADVERTENCIA = new DeviceRgb(241, 196, 15);
    private static final Color COLOR_FONDO_CODIGO = new DeviceRgb(40, 44, 52);

    // 🆕 FUENTES PDF CONFIGURADAS
    private static PdfFont fuenteNormal;
    private static PdfFont fuenteNegrita;
    private static PdfFont fuenteItalica;

    static {
        try {
            // 🛠️ INICIALIZAR FUENTES AL CARGAR LA CLASE
            fuenteNormal = PdfFontFactory.createFont();
            fuenteNegrita = PdfFontFactory.createFont();
            fuenteItalica = PdfFontFactory.createFont();
        } catch (Exception e) {
            System.err.println("⚠️ Error inicializando fuentes PDF: " + e.getMessage());
        }
    }

    /**
     * 🎯 GENERAR PDF COMPLETO DESDE CONTENIDO DE CONSOLA - VERSIÓN REPARADA
     */
    public static void generarPDFDesdeConsola(String contenidoConsola, String titulo, String rutaSalida) {
        try {
            FileUtils.crearDirectorioSiNoExiste("autogen-output/reportes");
            
            PdfWriter writer = new PdfWriter(rutaSalida);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // 🛠️ CONFIGURAR FUENTES POR DEFECTO EN EL DOCUMENTO
            if (fuenteNormal != null) {
                document.setFont(fuenteNormal);
            }

            // 🏴‍☠️ ENCABEZADO ÉPICO
            agregarEncabezadoEpico(document, titulo);
            
            // 📊 RESUMEN EJECUTIVO
            agregarResumenEjecutivo(document, contenidoConsola);
            
            // 📝 CONTENIDO PRINCIPAL FORMATEADO
            agregarContenidoFormateado(document, contenidoConsola);
            
            // 🎯 MÉTRICAS Y ESTADÍSTICAS
            agregarMetricasAutomaticas(document, contenidoConsola);
            
            // 🔮 RECOMENDACIONES INTELIGENTES TURBOFULURADAS
            agregarRecomendacionesInteligentes(document, contenidoConsola);
            
            document.close();
            
            System.out.println("🎨 PDF generado exitosamente: " + rutaSalida);
            
        } catch (Exception e) {
            System.err.println("💥 Error generando PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 🏴‍☠️ ENCABEZADO ÉPICO CON FUENTES REPARADAS
     */
    private static void agregarEncabezadoEpico(Document document, String titulo) {
        try {
            // TÍTULO PRINCIPAL - REPARADO: Sin setFontFamily
            Paragraph header = new Paragraph()
                .add(new Text("AUTOGEN TURBO FUSIÓN\n")
                    .setFontColor(COLOR_PRIMARIO)
                    .setBold()
                    .setFontSize(20))
                .add(new Text(titulo + "\n")
                    .setFontColor(COLOR_SECUNDARIO)
                    .setItalic()
                    .setFontSize(14))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
            
            document.add(header);
            
            // INFORMACIÓN DE EJECUCIÓN
            String fechaEjecucion = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            Paragraph info = new Paragraph()
                .add("Ejecutado el: " + fechaEjecucion + " | ")
                .add(new Text("🏴‍☠️ SISTEMA OPERATIVO AL 100%")
                    .setFontColor(COLOR_EXITO))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setMarginBottom(30);
            
            document.add(info);
            
        } catch (Exception e) {
            System.err.println("⚠️ Error en encabezado PDF: " + e.getMessage());
        }
    }

    /**
     * 📊 RESUMEN EJECUTIVO AUTOMÁTICO - REPARADO
     */
    private static void agregarResumenEjecutivo(Document document, String contenido) {
        try {
            Paragraph tituloSeccion = new Paragraph()
                .add(new Text("📊 RESUMEN EJECUTIVO")
                    .setBold()
                    .setFontColor(COLOR_PRIMARIO)
                    .setFontSize(16))
                .setMarginBottom(15);
            
            document.add(tituloSeccion);
            
            // 📈 CALCULAR MÉTRICAS AUTOMÁTICAMENTE
            int totalLineas = contenido.split("\n").length;
            int clasesAnalizadas = contarOcurrencias(contenido, "ANÁLISIS") + 
                                 contarOcurrencias(contenido, "CLASE") +
                                 contarOcurrencias(contenido, "ClassInfo");
            int errores = contarOcurrencias(contenido, "❌") + 
                         contarOcurrencias(contenido, "💥") +
                         contarOcurrencias(contenido, "ERROR");
            int exitos = contarOcurrencias(contenido, "✅") + 
                        contarOcurrencias(contenido, "ÉXITO");
            int advertencias = contarOcurrencias(contenido, "⚠️") + 
                             contarOcurrencias(contenido, "ADVERTENCIA");
            
            // CREAR TABLA DE RESUMEN
            float[] anchosColumnas = {200f, 200f};
            Table tablaResumen = new Table(anchosColumnas);
            
            tablaResumen.addCell(crearCelda("Métrica", true, COLOR_PRIMARIO));
            tablaResumen.addCell(crearCelda("Valor", true, COLOR_PRIMARIO));
            
            agregarFilaTabla(tablaResumen, "Total de líneas ejecutadas", String.valueOf(totalLineas));
            agregarFilaTabla(tablaResumen, "Clases analizadas", String.valueOf(clasesAnalizadas));
            agregarFilaTabla(tablaResumen, "Operaciones exitosas", String.valueOf(exitos), COLOR_EXITO);
            agregarFilaTabla(tablaResumen, "Errores detectados", String.valueOf(errores), 
                           errores > 0 ? COLOR_ERROR : COLOR_EXITO);
            agregarFilaTabla(tablaResumen, "Advertencias", String.valueOf(advertencias), 
                           advertencias > 0 ? COLOR_ADVERTENCIA : null);
            
            document.add(tablaResumen);
            document.add(new Paragraph("\n"));
            
        } catch (Exception e) {
            System.err.println("⚠️ Error en resumen ejecutivo: " + e.getMessage());
        }
    }

    /**
     * 📝 CONTENIDO FORMATEADO CON FUENTES REPARADAS
     */
    private static void agregarContenidoFormateado(Document document, String contenido) {
        try {
            Paragraph tituloSeccion = new Paragraph()
                .add(new Text("📝 DETALLE DE EJECUCIÓN")
                    .setBold()
                    .setFontColor(COLOR_PRIMARIO)
                    .setFontSize(16))
                .setMarginBottom(15);
            
            document.add(tituloSeccion);
            
            // DIVIDIR Y FORMATEAR CONTENIDO
            String[] lineas = contenido.split("\n");
            
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) {
                    document.add(new Paragraph(" "));
                    continue;
                }
                
                // DETECTAR BLOQUES DE CÓDIGO
                if (linea.trim().startsWith("```") || linea.contains("public class") || 
                    linea.contains("private") || linea.contains("public void")) {
                    agregarLineaCodigo(document, linea);
                } else {
                    agregarLineaFormateada(document, linea);
                }
            }
            
        } catch (Exception e) {
            System.err.println("⚠️ Error formateando contenido: " + e.getMessage());
        }
    }

    /**
     * 🎨 AGREGAR LÍNEA DE CÓDIGO FORMATEADA - REPARADA: Sin setFontFamily
     */
    private static void agregarLineaCodigo(Document document, String linea) {
        try {
            Paragraph parrafo = new Paragraph(linea)
                .setFontColor(DeviceRgb.WHITE)
                .setBackgroundColor(COLOR_FONDO_CODIGO)
                .setFontSize(8)
                .setPadding(5)
                .setMargin(2);
            
            document.add(parrafo);
            
        } catch (Exception e) {
            System.err.println("⚠️ Error en línea código: " + e.getMessage());
        }
    }

    /**
     * 📈 AGREGAR MÉTRICAS AUTOMÁTICAS - REPARADO
     */
    private static void agregarMetricasAutomaticas(Document document, String contenido) {
        try {
            Paragraph tituloSeccion = new Paragraph()
                .add(new Text("📈 MÉTRICAS DE CALIDAD")
                    .setBold()
                    .setFontColor(COLOR_PRIMARIO)
                    .setFontSize(16))
                .setMarginBottom(15);
            
            document.add(tituloSeccion);
            
            // CALCULAR MÉTRICAS AVANZADAS
            double eficiencia = calcularEficiencia(contenido);
            int sugerencias = contarOcurrencias(contenido, "SOLUCIÓN:") + 
                             contarOcurrencias(contenido, "SUGERENCIA:") +
                             contarOcurrencias(contenido, "RECOMENDACIÓN:");
            int problemasCriticos = contarOcurrencias(contenido, "PRIORIDAD: ALTA") + 
                                   contarOcurrencias(contenido, "CRÍTICO");
            
            // TABLA DE MÉTRICAS
            float[] anchos = {250f, 150f};
            Table tablaMetricas = new Table(anchos);
            
            tablaMetricas.addCell(crearCelda("Métrica de Calidad", true, COLOR_SECUNDARIO));
            tablaMetricas.addCell(crearCelda("Valor", true, COLOR_SECUNDARIO));
            
            agregarFilaTabla(tablaMetricas, "Eficiencia del sistema", 
                           String.format("%.1f%%", eficiencia), 
                           eficiencia > 80 ? COLOR_EXITO : eficiencia > 60 ? COLOR_ADVERTENCIA : COLOR_ERROR);
            
            agregarFilaTabla(tablaMetricas, "Sugerencias generadas", 
                           String.valueOf(sugerencias),
                           sugerencias > 0 ? COLOR_EXITO : null);
            
            agregarFilaTabla(tablaMetricas, "Problemas críticos", 
                           String.valueOf(problemasCriticos),
                           problemasCriticos == 0 ? COLOR_EXITO : COLOR_ERROR);
            
            agregarFilaTabla(tablaMetricas, "Tasa de refactorización", 
                           calcularTasaRefactor(contenido), null);
            
            document.add(tablaMetricas);
            
        } catch (Exception e) {
            System.err.println("⚠️ Error en métricas: " + e.getMessage());
        }
    }

    /**
     * 🔮 RECOMENDACIONES INTELIGENTES TURBOFULURADAS - VERSIÓN REPARADA
     */
    private static void agregarRecomendacionesInteligentes(Document document, String contenido) {
        try {
            Paragraph tituloSeccion = new Paragraph()
                .add(new Text("🔮 RECOMENDACIONES INTELIGENTES TURBOFULURADAS")
                    .setBold()
                    .setFontColor(COLOR_PRIMARIO)
                    .setFontSize(16))
                .setMarginBottom(15);
            
            document.add(tituloSeccion);
            
            Map<String, List<String>> recomendacionesClasificadas = extraerRecomendacionesInteligentes(contenido);
            
            if (recomendacionesClasificadas.isEmpty() || todasLasListasVacias(recomendacionesClasificadas)) {
                document.add(new Paragraph("✅ Sistema operando de manera óptima. No se requieren acciones inmediatas.")
                    .setFontColor(COLOR_EXITO)
                    .setItalic());
            } else {
                // 🚨 RECOMENDACIONES CRÍTICAS
                if (!recomendacionesClasificadas.get("criticas").isEmpty()) {
                    document.add(new Paragraph("🚨 ACCIONES CRÍTICAS - Requieren atención inmediata:")
                        .setBold()
                        .setFontColor(COLOR_ERROR)
                        .setMarginTop(10)
                        .setMarginBottom(5));
                    
                    com.itextpdf.layout.element.List listaCriticas = new com.itextpdf.layout.element.List()
                        .setSymbolIndent(15)
                        .setListSymbol("💥");
                    
                    for (String recomendacion : recomendacionesClasificadas.get("criticas")) {
                        ListItem item = new ListItem(recomendacion);
                        item.setFontColor(COLOR_ERROR);
                        listaCriticas.add(item);
                    }
                    document.add(listaCriticas);
                }
                
                // ⚡ RECOMENDACIONES IMPORTANTES
                if (!recomendacionesClasificadas.get("importantes").isEmpty()) {
                    document.add(new Paragraph("⚡ MEJORAS RECOMENDADAS - Planificar próximos sprints:")
                        .setBold()
                        .setFontColor(COLOR_ADVERTENCIA)
                        .setMarginTop(10)
                        .setMarginBottom(5));
                    
                    com.itextpdf.layout.element.List listaImportantes = new com.itextpdf.layout.element.List()
                        .setSymbolIndent(15)
                        .setListSymbol("⚡");
                    
                    for (String recomendacion : recomendacionesClasificadas.get("importantes")) {
                        ListItem item = new ListItem(recomendacion);
                        item.setFontColor(COLOR_ADVERTENCIA);
                        listaImportantes.add(item);
                    }
                    document.add(listaImportantes);
                }
                
                // 💡 RECOMENDACIONES SUGERIDAS
                if (!recomendacionesClasificadas.get("sugerencias").isEmpty()) {
                    document.add(new Paragraph("💡 OPTIMIZACIONES SUGERIDAS - Mejoras a considerar:")
                        .setBold()
                        .setFontColor(COLOR_PRIMARIO)
                        .setMarginTop(10)
                        .setMarginBottom(5));
                    
                    com.itextpdf.layout.element.List listaSugerencias = new com.itextpdf.layout.element.List()
                        .setSymbolIndent(15)
                        .setListSymbol("💡");
                    
                    for (String recomendacion : recomendacionesClasificadas.get("sugerencias")) {
                        ListItem item = new ListItem(recomendacion);
                        item.setFontColor(COLOR_PRIMARIO);
                        listaSugerencias.add(item);
                    }
                    document.add(listaSugerencias);
                }
                
                // 📈 RESUMEN ESTADÍSTICO
                agregarResumenEstadisticoRecomendaciones(document, recomendacionesClasificadas);
            }
            
            // 🏴‍☠️ FIRMA ÉPICA MEJORADA
            Paragraph firma = new Paragraph("\n\n— Generado automáticamente por el Oráculo Turbofulurado 🏴‍☠️")
                .setTextAlignment(TextAlignment.RIGHT)
                .setItalic()
                .setFontSize(9)
                .setFontColor(new DeviceRgb(80, 80, 80));
            
            document.add(firma);
            
        } catch (Exception e) {
            System.err.println("⚠️ Error en recomendaciones inteligentes: " + e.getMessage());
            document.add(new Paragraph("🔧 El sistema de recomendaciones inteligentes no está disponible temporalmente.")
                .setFontColor(COLOR_ADVERTENCIA));
        }
    }

    // =========================================================================
    // 🛠️ MÉTODOS AUXILIARES REPARADOS - SIN setFontFamily
    // =========================================================================

    private static Cell crearCelda(String texto, boolean esHeader, Color color) {
        Paragraph parrafo = new Paragraph(texto);
        if (esHeader) {
            parrafo.setBold();
        }
        
        Cell celda = new Cell().add(parrafo);
        if (esHeader && color != null) {
            celda.setBackgroundColor(color);
            parrafo.setFontColor(DeviceRgb.WHITE);
        }
        return celda;
    }

    private static void agregarFilaTabla(Table tabla, String metrico, String valor) {
        agregarFilaTabla(tabla, metrico, valor, null);
    }

    private static void agregarFilaTabla(Table tabla, String metrico, String valor, Color color) {
        tabla.addCell(crearCelda(metrico, false, null));
        
        Cell celdaValor = crearCelda(valor, false, null);
        if (color != null) {
            Paragraph parrafoValor = (Paragraph) celdaValor.getChildren().get(0);
            parrafoValor.setFontColor(color);
        }
        tabla.addCell(celdaValor);
    }

    private static void agregarLineaFormateada(Document document, String linea) {
        Paragraph parrafo = new Paragraph(linea);
        
        // DETECTAR TIPO DE LÍNEA POR EMOJIS/PATRONES
        if (linea.contains("❌") || linea.contains("💥") || linea.toLowerCase().contains("error")) {
            parrafo.setFontColor(COLOR_ERROR);
        } else if (linea.contains("✅") || linea.toLowerCase().contains("éxito")) {
            parrafo.setFontColor(COLOR_EXITO);
        } else if (linea.contains("⚠️") || linea.toLowerCase().contains("advertencia")) {
            parrafo.setFontColor(COLOR_ADVERTENCIA);
        } else if (linea.contains("🎯") || linea.contains("🚀")) {
            parrafo.setBold();
        } else if (linea.matches(".*[=⏱️📊🔍].*")) {
            parrafo.setItalic();
        }
        
        document.add(parrafo);
    }

    private static int contarOcurrencias(String texto, String patron) {
        if (texto == null || patron == null) return 0;
        return texto.split(Pattern.quote(patron), -1).length - 1;
    }

    private static double calcularEficiencia(String contenido) {
        int exitos = contarOcurrencias(contenido, "✅") + 
                    contarOcurrencias(contenido, "ÉXITO");
        int errores = contarOcurrencias(contenido, "❌") + 
                     contarOcurrencias(contenido, "ERROR");
        int total = exitos + errores;
        return total > 0 ? (double) exitos / total * 100 : 100.0;
    }

    private static String calcularTasaRefactor(String contenido) {
        int refactors = contarOcurrencias(contenido, "REFACTOR") + 
                       contarOcurrencias(contenido, "refactor");
        int clases = contarOcurrencias(contenido, "CLASE") + 
                    contarOcurrencias(contenido, "ClassInfo");
        return clases > 0 ? String.format("%.1f%%", (double) refactors / clases * 100) : "0%";
    }

    // =========================================================================
    // 🔮 MÉTODOS DE RECOMENDACIONES INTELIGENTES (MANTENIDOS IGUAL)
    // =========================================================================

    private static Map<String, List<String>> extraerRecomendacionesInteligentes(String contenido) {
        Map<String, List<String>> clasificadas = new HashMap<>();
        clasificadas.put("criticas", new ArrayList<>());
        clasificadas.put("importantes", new ArrayList<>());
        clasificadas.put("sugerencias", new ArrayList<>());
        
        if (contenido == null || contenido.trim().isEmpty()) {
            return clasificadas;
        }
        
        String[] lineas = contenido.split("\n");
        boolean enSeccionRecomendaciones = false;
        StringBuilder bufferRecomendacion = new StringBuilder();
        
        for (String linea : lineas) {
            String lineaTrim = linea.trim();
            
            if (lineaTrim.matches(".*(RECOMENDACIONES|SUGERENCIAS|MEJORAS|ACCIONES|PLAN).*") || 
                lineaTrim.contains("🎯") || lineaTrim.contains("💡") || lineaTrim.contains("⚡")) {
                enSeccionRecomendaciones = true;
                continue;
            }
            
            if (enSeccionRecomendaciones && 
                lineaTrim.matches(".*(ANÁLISIS|RESUMEN|CONCLUS|METRICAS|ESTADÍSTICAS|FIRMA).*")) {
                enSeccionRecomendaciones = false;
                procesarBufferRecomendacion(bufferRecomendacion, clasificadas);
            }
            
            if (enSeccionRecomendaciones) {
                if (lineaTrim.matches("^[\\-•*▶]\\s+.+") || 
                    lineaTrim.matches("^\\d+\\.\\s+.+") ||
                    lineaTrim.matches("^[🎯💡⚡🚨🔧📝🎨🔮].+")) {
                    
                    procesarBufferRecomendacion(bufferRecomendacion, clasificadas);
                    bufferRecomendacion.append(lineaTrim);
                } 
                else if (bufferRecomendacion.length() > 0 && !lineaTrim.isEmpty()) {
                    bufferRecomendacion.append(" ").append(lineaTrim);
                }
            }
            
            if (lineaTrim.matches(".*(ERROR|EXCEPCIÓN|FALLO|CRASH|NULL|EXCEPTION).*")) {
                clasificadas.get("criticas").add("Detectado: " + extraerFraseRelevante(lineaTrim));
            }
        }
        
        procesarBufferRecomendacion(bufferRecomendacion, clasificadas);
        
        return clasificadas;
    }

    private static void procesarBufferRecomendacion(StringBuilder buffer, Map<String, List<String>> clasificadas) {
        if (buffer.length() == 0) return;
        
        String recomendacion = buffer.toString().trim();
        
        recomendacion = recomendacion
            .replaceAll("^[\\-•*▶\\d\\.\\s]+", "")
            .replaceAll("^[🎯💡⚡🚨🔧📝🎨🔮\\s]+", "")
            .trim();
        
        if (recomendacion.length() < 10) {
            buffer.setLength(0);
            return;
        }
        
        String prioridad = determinarPrioridadRecomendacion(recomendacion);
        clasificadas.get(prioridad).add(recomendacion);
        
        buffer.setLength(0);
    }

    private static String determinarPrioridadRecomendacion(String recomendacion) {
        String lower = recomendacion.toLowerCase();
        
        if (lower.matches(".*\\b(error|exception|crash|fail|null|broken|critical|urgent|security|vulnerability|memory leak|deadlock)\\b.*") ||
            lower.contains("💥") || lower.contains("🚨") || lower.contains("❌")) {
            return "criticas";
        }
        
        if (lower.matches(".*\\b(improve|optimize|refactor|performance|speed|memory|complexity|duplicate|smell|technical debt)\\b.*") ||
            lower.contains("⚡") || lower.contains("⚠️") || lower.contains("🔧")) {
            return "importantes";
        }
        
        if (lower.matches(".*\\b(suggest|consider|could|might|optional|enhancement|cleanup|documentation|comment)\\b.*") ||
            lower.contains("💡") || lower.contains("🎯") || lower.contains("📝")) {
            return "sugerencias";
        }
        
        if (recomendacion.length() > 100 && 
            (lower.contains("debería") || lower.contains("recomiendo") || lower.contains("mejorar"))) {
            return "importantes";
        }
        
        return "sugerencias";
    }

    private static void agregarResumenEstadisticoRecomendaciones(Document document, Map<String, List<String>> recomendaciones) {
        int totalCriticas = recomendaciones.get("criticas").size();
        int totalImportantes = recomendaciones.get("importantes").size();
        int totalSugerencias = recomendaciones.get("sugerencias").size();
        int total = totalCriticas + totalImportantes + totalSugerencias;
        
        if (total == 0) return;
        
        Paragraph resumen = new Paragraph()
            .add(new Text("📊 RESUMEN ESTADÍSTICO: ")
                .setBold())
            .add(new Text(total + " recomendaciones identificadas ("))
            .add(new Text(totalCriticas + " críticas")
                .setFontColor(COLOR_ERROR))
            .add(new Text(", "))
            .add(new Text(totalImportantes + " importantes")
                .setFontColor(COLOR_ADVERTENCIA))
            .add(new Text(", "))
            .add(new Text(totalSugerencias + " sugerencias")
                .setFontColor(COLOR_PRIMARIO))
            .add(new Text(")"))
            .setMarginTop(15)
            .setFontSize(10);
        
        document.add(resumen);
    }

    private static String extraerFraseRelevante(String linea) {
        if (linea.length() > 80) {
            return linea.substring(0, 77) + "...";
        }
        return linea;
    }

    private static boolean todasLasListasVacias(Map<String, List<String>> map) {
        return map.values().stream().allMatch(List::isEmpty);
    }

    // =========================================================================
    // 📄 MÉTODOS DE PDF INDIVIDUALES (MANTENIDOS IGUAL)
    // =========================================================================

    public static void generarPDFAnalisisIndividual(String nombreClase, String analisisTurbo, String recomendaciones) {
        try {
            BitacoraConsola.log("🎨 Generando PDF individual para: " + nombreClase);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nombreArchivo = "analisis_" + nombreClase.replace(".", "_").replace("$", "_") + "_" + timestamp + ".pdf";
            String rutaPDF = "autogen-output/reportes/individuales/" + nombreArchivo;
            
            FileUtils.crearDirectorioSiNoExiste("autogen-output/reportes/individuales");
            
            String contenidoEpico = construirContenidoAnalisisIndividual(nombreClase, analisisTurbo, recomendaciones);
            generarPDFDesdeConsola(contenidoEpico, "Análisis Individual - " + nombreClase, rutaPDF);
            
            System.out.println("🎨 PDF individual generado: " + rutaPDF);
            BitacoraConsola.logExito("PDF individual creado para " + nombreClase + " - " + nombreArchivo);
            
        } catch (Exception e) {
            System.err.println("💥 Error generando PDF individual: " + e.getMessage());
            BitacoraConsola.logError("Fallo generando PDF individual para " + nombreClase);
        }
    }

    public static String construirContenidoAnalisisIndividual(String nombreClase, String analisis, String recomendaciones) {
        StringBuilder contenido = new StringBuilder();
        
        contenido.append("🏴‍☠️ ANÁLISIS TURBOFULURADO INDIVIDUAL\n");
        contenido.append("=".repeat(60)).append("\n\n");
        
        contenido.append("📋 INFORMACIÓN DE LA CLASE ANALIZADA\n");
        contenido.append("-".repeat(40)).append("\n");
        contenido.append("• Clase: ").append(nombreClase).append("\n");
        contenido.append("• Fecha análisis: ").append(LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        contenido.append("• Tipo: Análisis individual profundo\n");
        contenido.append("• Sistema: Autogen Turbo Fusión 🏴‍☠️\n\n");
        
        contenido.append("📊 METADATAS DEL ANÁLISIS\n");
        contenido.append("-".repeat(30)).append("\n");
        contenido.append("• Longitud del análisis: ").append(analisis != null ? analisis.length() : 0).append(" caracteres\n");
        contenido.append("• Nivel de detalle: ").append(calcularNivelDetalle(analisis)).append("\n");
        contenido.append("• Urgencia detectada: ").append(calcularUrgenciaDesdeAnalisis(analisis)).append("\n");
        contenido.append("• Sugerencias extraídas: ").append(contarSugerencias(recomendaciones)).append("\n\n");
        
        contenido.append("🤖 ANÁLISIS TURBOFULURADO COMPLETO\n");
        contenido.append("-".repeat(40)).append("\n");
        contenido.append(analisis != null ? analisis : "[ANÁLISIS NO DISPONIBLE]").append("\n\n");
        
        contenido.append("🎯 RECOMENDACIONES ESPECÍFICAS\n");
        contenido.append("-".repeat(35)).append("\n");
        
        if (recomendaciones != null && !recomendaciones.trim().isEmpty()) {
            String[] recs = recomendaciones.split("\n");
            for (int i = 0; i < recs.length; i++) {
                if (!recs[i].trim().isEmpty()) {
                    String emoji = obtenerEmojiPrioridad(recs[i]);
                    contenido.append(emoji).append(" ").append(i + 1).append(". ").append(recs[i].trim()).append("\n");
                }
            }
        } else {
            contenido.append("✅ No se detectaron recomendaciones específicas. El código parece estar bien estructurado.\n");
        }
        
        contenido.append("\n");
        
        contenido.append("⚡ PLAN DE ACCIÓN RECOMENDADO\n");
        contenido.append("-".repeat(35)).append("\n");
        contenido.append(generarPlanAccion(analisis, recomendaciones)).append("\n\n");
        
        contenido.append("📈 MÉTRICAS DE CALIDAD INFERIDAS\n");
        contenido.append("-".repeat(35)).append("\n");
        contenido.append(generarMetricasCalidad(analisis)).append("\n\n");
        
        contenido.append("🔮 PRÓXIMOS PASOS SUGERIDOS\n");
        contenido.append("-".repeat(30)).append("\n");
        contenido.append(generarProximosPasos(nombreClase, analisis)).append("\n\n");
        
        contenido.append("🏁".repeat(60)).append("\n");
        contenido.append("¡ANÁLISIS INDIVIDUAL COMPLETADO! 🏴‍☠️\n");
        contenido.append("Generado automáticamente por Autogen Turbo Fusión\n");
        contenido.append("Timestamp: ").append(LocalDateTime.now()).append("\n");
        
        return contenido.toString();
    }

    // =========================================================================
    // 🛠️ MÉTODOS AUXILIARES TURBOFULURADOS (MANTENIDOS IGUAL)
    // =========================================================================

    private static String calcularNivelDetalle(String analisis) {
        if (analisis == null) return "🟡 MEDIO";
        
        int longitud = analisis.length();
        int parrafos = analisis.split("\n\n").length;
        
        if (longitud > 2000 || parrafos > 10) {
            return "🔴 ALTO (Análisis exhaustivo)";
        } else if (longitud > 1000 || parrafos > 5) {
            return "🟡 MEDIO (Análisis detallado)";
        } else {
            return "🟢 BAJO (Análisis conciso)";
        }
    }

    private static String calcularUrgenciaDesdeAnalisis(String analisis) {
        if (analisis == null) return "🟢 BAJA";
        
        String analisisLower = analisis.toLowerCase();
        
        if (analisisLower.contains("crítico") || analisisLower.contains("urgente") || 
            analisisLower.contains("error grave") || analisisLower.contains("security risk") ||
            analisisLower.contains("vulnerabilidad") || analisis.contains("💥")) {
            return "🔴 ALTA (Acción inmediata requerida)";
        } else if (analisisLower.contains("importante") || analisisLower.contains("debería") ||
                   analisisLower.contains("recomiendo") || analisisLower.contains("mejorar") ||
                   analisis.contains("⚠️")) {
            return "🟡 MEDIA (Planificar acción)";
        } else {
            return "🟢 BAJA (Mejora opcional)";
        }
    }

    private static int contarSugerencias(String recomendaciones) {
        if (recomendaciones == null || recomendaciones.trim().isEmpty()) return 0;
        return recomendaciones.split("\n").length;
    }

    private static String obtenerEmojiPrioridad(String recomendacion) {
        if (recomendacion == null) return "💡";
        
        String recLower = recomendacion.toLowerCase();
        
        if (recLower.contains("crítico") || recLower.contains("urgente") || recLower.contains("error")) {
            return "🚨";
        } else if (recLower.contains("importante") || recLower.contains("debería") || recLower.contains("mejorar")) {
            return "⚡";
        } else if (recLower.contains("optimizar") || recLower.contains("rendimiento")) {
            return "🎯";
        } else if (recLower.contains("considerar") || recLower.contains("sugerencia")) {
            return "💡";
        } else {
            return "🔹";
        }
    }

    private static String generarPlanAccion(String analisis, String recomendaciones) {
        List<String> acciones = new ArrayList<>();
        
        if (analisis != null) {
            if (analisis.toLowerCase().contains("refactor")) {
                acciones.add("• 🔄 **Refactorizar** el código según las sugerencias");
            }
            if (analisis.toLowerCase().contains("performance") || analisis.toLowerCase().contains("rendimiento")) {
                acciones.add("• ⚡ **Optimizar** el rendimiento identificado");
            }
            if (analisis.toLowerCase().contains("seguridad") || analisis.toLowerCase().contains("security")) {
                acciones.add("• 🛡️ **Revisar** aspectos de seguridad");
            }
            if (analisis.toLowerCase().contains("test") || analisis.toLowerCase().contains("prueba")) {
                acciones.add("• 🧪 **Implementar** pruebas unitarias");
            }
            if (analisis.toLowerCase().contains("documentación") || analisis.toLowerCase().contains("documentation")) {
                acciones.add("• 📝 **Mejorar** documentación del código");
            }
        }
        
        if (acciones.isEmpty()) {
            acciones.add("• 🔍 **Revisar** el análisis completo de IA");
            acciones.add("• 📋 **Priorizar** las recomendaciones según impacto");
            acciones.add("• 🎯 **Implementar** mejoras de forma incremental");
        }
        
        return String.join("\n", acciones);
    }

    private static String generarMetricasCalidad(String analisis) {
        if (analisis == null) return "No se pudieron calcular métricas";
        
        List<String> metricas = new ArrayList<>();
        
        if (analisis.toLowerCase().contains("complex") || analisis.toLowerCase().contains("complej")) {
            metricas.add("• 🧠 **Complejidad**: Puede ser alta - considerar simplificación");
        } else {
            metricas.add("• 🧠 **Complejidad**: Aparentemente manejable");
        }
        
        if (analisis.toLowerCase().contains("duplicate") || analisis.toLowerCase().contains("duplicad")) {
            metricas.add("• 🔄 **Duplicación**: Se detectó código duplicado");
        } else {
            metricas.add("• 🔄 **Duplicación**: No se menciona duplicación");
        }
        
        if (analisis.toLowerCase().contains("maintain") || analisis.toLowerCase().contains("manten")) {
            metricas.add("• 🛠️ **Mantenibilidad**: Puede necesitar mejoras");
        } else {
            metricas.add("• 🛠️ **Mantenibilidad**: Aparentemente buena");
        }
        
        if (analisis.toLowerCase().contains("readability") || analisis.toLowerCase().contains("legibilidad")) {
            metricas.add("• 📖 **Legibilidad**: Podría mejorarse");
        } else {
            metricas.add("• 📖 **Legibilidad**: Aparentemente aceptable");
        }
        
        return String.join("\n", metricas);
    }

    private static String generarProximosPasos(String nombreClase, String analisis) {
        List<String> pasos = new ArrayList<>();
        
        pasos.add("1. 📋 **Revisar** este análisis completo");
        pasos.add("2. 🎯 **Priorizar** las recomendaciones según impacto");
        pasos.add("3. 🔄 **Implementar** cambios de forma incremental");
        pasos.add("4. 🧪 **Probar** los cambios después de cada modificación");
        pasos.add("5. 📝 **Documentar** las mejoras realizadas");
        
        if (nombreClase.toLowerCase().contains("service")) {
            pasos.add("6. ⚡ **Optimizar** lógica de negocio si es necesario");
        } else if (nombreClase.toLowerCase().contains("controller")) {
            pasos.add("6. 🛡️ **Validar** manejo de errores y seguridad");
        } else if (nombreClase.toLowerCase().contains("repository")) {
            pasos.add("6. 🗄️ **Revisar** consultas y acceso a datos");
        } else if (nombreClase.toLowerCase().contains("util") || nombreClase.toLowerCase().contains("helper")) {
            pasos.add("6. 🔧 **Verificar** reutilización y eficiencia");
        }
        
        return String.join("\n", pasos);
    }

    public static void generarPDFDesdeAnalisisIA(String nombreClase, String respuestaIA) {
        try {
            List<String> sugerencias = extraerSugerenciasDeRespuestaIA(respuestaIA);
            String recomendacionesStr = String.join("\n", sugerencias);
            generarPDFAnalisisIndividual(nombreClase, respuestaIA, recomendacionesStr);
            
        } catch (Exception e) {
            System.err.println("💥 Error en generación automática desde IA: " + e.getMessage());
        }
    }

    private static List<String> extraerSugerenciasDeRespuestaIA(String respuestaIA) {
        List<String> sugerencias = new ArrayList<>();
        
        if (respuestaIA == null) return sugerencias;
        
        String[] lineas = respuestaIA.split("\n");
        StringBuilder sugerenciaActual = new StringBuilder();
        
        for (String linea : lineas) {
            linea = linea.trim();
            
            if (linea.startsWith("- ") || linea.startsWith("• ") || 
                linea.startsWith("* ") || linea.matches("\\d+\\.\\s.*") ||
                linea.toLowerCase().contains("sugerencia:") ||
                linea.toLowerCase().contains("recomendación:") ||
                linea.toLowerCase().contains("deberías") ||
                linea.toLowerCase().contains("considera") ||
                linea.toLowerCase().contains("podrías")) {
                
                if (sugerenciaActual.length() > 0) {
                    sugerencias.add(sugerenciaActual.toString());
                    sugerenciaActual = new StringBuilder();
                }
                
                sugerenciaActual.append(linea);
            } 
            else if (sugerenciaActual.length() > 0 && !linea.isEmpty()) {
                sugerenciaActual.append(" ").append(linea);
            }
        }
        
        if (sugerenciaActual.length() > 0) {
            sugerencias.add(sugerenciaActual.toString());
        }
        
        return sugerencias.stream()
            .map(s -> s.replace("Sugerencia:", "").replace("Recomendación:", "").trim())
            .filter(s -> s.length() > 10)
            .filter(s -> !s.toLowerCase().contains("hola") && !s.toLowerCase().contains("gracias"))
            .collect(java.util.stream.Collectors.toList());
    }

    public static void generarPDFEjecucionCompleta(String contenidoConsola) {
        try {
            String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String rutaPDF = "autogen-output/reportes/ejecucion_completa_" + timestamp + ".pdf";
            
            generarPDFDesdeConsola(contenidoConsola, 
                "Ejecución Completa del Sistema", rutaPDF);
            
        } catch (Exception e) {
            System.err.println("💥 Error generando PDF de ejecución: " + e.getMessage());
        }
    }
}