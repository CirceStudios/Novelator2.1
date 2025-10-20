package com.elreinodelolvido.ellibertad.scanner;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.util.FileUtils;
import com.elreinodelolvido.ellibertad.util.RollbackManager;
import com.elreinodelolvido.ellibertad.util.ValidadorFirmas;

/**
 * 🏴‍☠️ IntegradorForzado — Ejecuta integración forzada de archivos refactorizados
 * ¡Ahora con +500% de robustez y estrategias de integración inteligentes! ⚓
 */
public class IntegradorForzado {
    private final Bitacora bitacora;
    private final AtomicInteger exitos = new AtomicInteger(0);
    private final AtomicInteger fallos = new AtomicInteger(0);
    private final Set<Path> archivosIntegrados = new HashSet<>();

    public IntegradorForzado(Bitacora bitacora) {
        this.bitacora = Objects.requireNonNull(bitacora, "La bitácora no puede ser nula");
    }

    /**
     * 🚀 Integración turbo de un archivo individual
     */
    @SuppressWarnings("static-access")
	public ResultadoIntegracion integrarArchivo(Path archivoGenerado, Path archivoDestino) {
        bitacora.info("⚓ Preparando integración forzada...");
        bitacora.debug("Origen: " + archivoGenerado);
        bitacora.debug("Destino: " + archivoDestino);

        if (!Files.exists(archivoGenerado)) {
            bitacora.error("❌ Archivo generado no existe: " + archivoGenerado);
            return ResultadoIntegracion.error("Archivo generado no existe");
        }

        if (!Files.exists(archivoDestino)) {
            bitacora.error("❌ Archivo destino no existe: " + archivoDestino);
            return ResultadoIntegracion.error("Archivo destino no existe");
        }

        Path backupPath = crearBackupPath(archivoDestino);

        try {
            // 1. 🛡️ Crear backup estratégico
            crearBackup(archivoDestino, backupPath);

            // 2. 🔍 Validación avanzada de firmas
            ResultadoValidacion validacion = validarFirmasAvanzado(archivoDestino, archivoGenerado);
            if (!validacion.esExitoso()) {
                bitacora.error("❌ Validación de firmas fallida: " + validacion.getMensaje());
                return ResultadoIntegracion.error("Validación fallida: " + validacion.getMensaje());
            }

            // 3. ⚡ Integración turbo
            integrarCodigo(archivoGenerado, archivoDestino);

            // 4. ✅ Verificación post-integracion
            if (verificarIntegracion(archivoDestino)) {
                archivosIntegrados.add(archivoDestino);
                exitos.incrementAndGet();
                bitacora.exito("✅ Integración turbo exitosa: " + archivoDestino.getFileName());
                return ResultadoIntegracion.exitoso(archivoDestino.toString());
            } else {
                throw new IOException("Verificación post-integracion falló");
            }

        } catch (Exception e) {
            fallos.incrementAndGet();
            bitacora.error("💥 Kraken durante integración: " + e.getMessage(), e);
            ejecutarRollbackEstrategico(archivoDestino, backupPath);
            return ResultadoIntegracion.error(e.getMessage());
        }
    }

    /**
     * 🎯 Integración por lotes inteligente
     */
    @SuppressWarnings("static-access")
	public ResultadoLote integrarTodos(List<Object> archivosGenerados, List<Object> archivosDestino) {
        if (archivosGenerados.size() != archivosDestino.size()) {
            bitacora.error("❌ Tamaños desiguales de listas. Generados: " + 
                archivosGenerados.size() + ", Destino: " + archivosDestino.size());
            return ResultadoLote.error("Tamaños desiguales");
        }

        bitacora.info("🚀 Iniciando integración por lotes turbo (" + archivosGenerados.size() + " archivos)");

        List<ResultadoIntegracion> resultados = new ArrayList<>();
        List<Path> archivosFallados = new ArrayList<>();

        for (int i = 0; i < archivosGenerados.size(); i++) {
            Path generado = (Path) archivosGenerados.get(i);
            Path destino = (Path) archivosDestino.get(i);

            ResultadoIntegracion resultado = integrarArchivo(generado, destino);
            resultados.add(resultado);

            if (!resultado.esExitoso()) {
                archivosFallados.add(destino);
            }
        }

        ResultadoLote resultadoLote = new ResultadoLote(resultados, archivosFallados);
        bitacora.info("📊 Resultado lote: " + resultadoLote);
        
        return resultadoLote;
    }

    /**
     * 🔄 Integración basada en ClassInfo
     */
    @SuppressWarnings("static-access")
	public static ResultadoIntegracion integrar(String className, String packagePath, String sourcePath) {
        Bitacora bitacora = new Bitacora();
        IntegradorForzado integrador = new IntegradorForzado(bitacora);

        String destinoPath = "src/main/java/" + packagePath.replace('.', '/') + "/" + className + ".java";
        String refactorPath = "autogen-output/refactor/" + packagePath.replace('.', '/') + "/" + className + ".refactor.java";
        String checkPath = "autogen-output/refactor/" + packagePath.replace('.', '/') + "/" + className + ".check.txt";

        bitacora.info("⚓ Integrando clase: " + className + " desde " + refactorPath);

        try {
            // Validación de firmas con registro
            String resultadoValidacion = ValidadorFirmas.validarFirmasTurbo(destinoPath, refactorPath);
            FileUtils.writeToFile(checkPath, resultadoValidacion);
            
            if (!resultadoValidacion.contains("✅")) {
                bitacora.error("❌ Firmas inválidas detectadas");
                FileUtils.writeToFile(checkPath + ".error", resultadoValidacion);
                return ResultadoIntegracion.error("Firmas inválidas");
            }

            // Ejecutar integración
            return integrador.integrarArchivo(Paths.get(refactorPath), Paths.get(destinoPath));

        } catch (Exception e) {
            bitacora.error("💥 Error catastrófico integrando " + className, e);
            RollbackManager.restaurarArchivo(destinoPath);
            return ResultadoIntegracion.error("Error: " + e.getMessage());
        }
    }

    // 🛠️ Métodos de implementación
    private Path crearBackupPath(Path archivoDestino) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return archivoDestino.resolveSibling(archivoDestino.getFileName() + ".backup_" + timestamp);
    }

    @SuppressWarnings("static-access")
	private void crearBackup(Path origen, Path backup) throws IOException {
        Files.copy(origen, backup, StandardCopyOption.REPLACE_EXISTING);
        bitacora.debug("🧳 Backup creado: " + backup.getFileName());
    }

    @SuppressWarnings("static-access")
	private ResultadoValidacion validarFirmasAvanzado(Path original, Path refactorizado) {
        try {
            String validacion = ValidadorFirmas.validarFirmasTurbo(original.toString(), refactorizado.toString());
            
            if (validacion.contains("✅")) {
                return ResultadoValidacion.exitoso(validacion);
            } else if (validacion.contains("⚠️")) {
                bitacora.info("⚠️ Validación con advertencias: " + validacion);
                return ResultadoValidacion.conAdvertencias(validacion);
            } else {
                return ResultadoValidacion.fallido(validacion);
            }
        } catch (Exception e) {
            return ResultadoValidacion.fallido("Error en validación: " + e.getMessage());
        }
    }

    private void integrarCodigo(Path generado, Path destino) throws IOException {
        // Leer contenido y aplicar transformaciones si es necesario
        String contenido = Files.readString(generado);
        
        // Aquí podrías aplicar transformaciones adicionales
        Files.writeString(destino, contenido, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private boolean verificarIntegracion(Path archivo) {
        try {
            return Files.exists(archivo) && Files.size(archivo) > 0;
        } catch (IOException e) {
            return false;
        }
    }

    @SuppressWarnings("static-access")
	private void ejecutarRollbackEstrategico(Path destino, Path backup) {
        try {
            if (Files.exists(backup)) {
                Files.copy(backup, destino, StandardCopyOption.REPLACE_EXISTING);
                bitacora.info("🛟 Rollback estratégico ejecutado desde: " + backup.getFileName());
                
                // Limpiar backup después de rollback exitoso
                Files.deleteIfExists(backup);
            } else {
                bitacora.error("❌ Backup no disponible para rollback: " + backup);
            }
        } catch (IOException e) {
            bitacora.error("💥 Fallo catastrófico durante rollback: " + e.getMessage());
        }
    }

    // 📊 Métricas y estadísticas
    public EstadisticasIntegracion getEstadisticas() {
        return new EstadisticasIntegracion(
            exitos.get(),
            fallos.get(),
            archivosIntegrados.size()
        );
    }

    @SuppressWarnings("static-access")
	public void limpiarRegistros() {
        archivosIntegrados.clear();
        exitos.set(0);
        fallos.set(0);
        bitacora.debug("🧹 Registros de integración limpiados");
    }

    // 🎯 Clases de resultados
    public static class ResultadoIntegracion {
        private final boolean exitoso;
        private final String mensaje;
        private final String archivo;

        private ResultadoIntegracion(boolean exitoso, String mensaje, String archivo) {
            this.exitoso = exitoso;
            this.mensaje = mensaje;
            this.archivo = archivo;
        }

        public static ResultadoIntegracion exitoso(String archivo) {
            return new ResultadoIntegracion(true, "Integración exitosa", archivo);
        }

        public static ResultadoIntegracion error(String mensaje) {
            return new ResultadoIntegracion(false, mensaje, null);
        }

        public boolean esExitoso() { return exitoso; }
        public String getMensaje() { return mensaje; }
        public String getArchivo() { return archivo; }
    }

    public static class ResultadoLote {
        private final List<ResultadoIntegracion> resultados;
        private final List<Path> fallados;

        public ResultadoLote(List<ResultadoIntegracion> resultados, List<Path> fallados) {
            this.resultados = resultados;
            this.fallados = fallados;
        }

        public int getExitos() {
            return (int) resultados.stream().filter(ResultadoIntegracion::esExitoso).count();
        }

        public int getFallos() {
            return resultados.size() - getExitos();
        }

        public double getTasaExito() {
            return resultados.isEmpty() ? 0.0 : (double) getExitos() / resultados.size();
        }

        public List<Path> getFallados() { return fallados; }

        @Override
        public String toString() {
            return String.format("🎯 Lote: %d/%d exitos (%.1f%%)", 
                getExitos(), resultados.size(), getTasaExito() * 100);
        }

        public static ResultadoLote error(String mensaje) {
            return new ResultadoLote(Collections.emptyList(), Collections.emptyList());
        }
    }

    public static class ResultadoValidacion {
        private final boolean exitoso;
        private final boolean advertencias;
        private final String mensaje;

        private ResultadoValidacion(boolean exitoso, boolean advertencias, String mensaje) {
            this.exitoso = exitoso;
            this.advertencias = advertencias;
            this.mensaje = mensaje;
        }

        public static ResultadoValidacion exitoso(String mensaje) {
            return new ResultadoValidacion(true, false, mensaje);
        }

        public static ResultadoValidacion conAdvertencias(String mensaje) {
            return new ResultadoValidacion(true, true, mensaje);
        }

        public static ResultadoValidacion fallido(String mensaje) {
            return new ResultadoValidacion(false, false, mensaje);
        }

        public boolean esExitoso() { return exitoso; }
        public boolean tieneAdvertencias() { return advertencias; }
        public String getMensaje() { return mensaje; }
    }

    public static class EstadisticasIntegracion {
        private final int exitos;
        private final int fallos;
        private final int archivosIntegrados;

        public EstadisticasIntegracion(int exitos, int fallos, int archivosIntegrados) {
            this.exitos = exitos;
            this.fallos = fallos;
            this.archivosIntegrados = archivosIntegrados;
        }

        public int getExitos() { return exitos; }
        public int getFallos() { return fallos; }
        public int getTotal() { return exitos + fallos; }
        public int getArchivosIntegrados() { return archivosIntegrados; }
        public double getTasaExito() { 
            return getTotal() == 0 ? 0.0 : (double) exitos / getTotal(); 
        }

        @Override
        public String toString() {
            return String.format("📊 Estadísticas: %d/%d exitos (%.1f%%)", 
                exitos, getTotal(), getTasaExito() * 100);
        }
    }

    public void integrarTodo() {
        bitacora.info("🚀 Iniciando integración completa turbo...");
        
        try {
            // 📁 Buscar todos los archivos refactorizados
            String refactorDir = "autogen-output/refactor";
            List<Path> archivosRefactorizados = Files.walk(Paths.get(refactorDir))
                    .filter(path -> path.toString().endsWith(".refactor.java"))
                    .collect(Collectors.toList());

            if (archivosRefactorizados.isEmpty()) {
                bitacora.warn("⚠️ No se encontraron archivos refactorizados en " + refactorDir);
                return;
            }

            bitacora.info("📦 Encontrados " + archivosRefactorizados.size() + " archivos para integrar");

            List<ResultadoIntegracion> resultados = new ArrayList<>();
            List<Path> fallados = new ArrayList<>();

            // ⚡ Integración turbo por lotes
            for (Path refactorizado : archivosRefactorizados) {
                try {
                    // 🎯 Determinar ruta destino
                    String rutaRelativa = refactorDir + "/";
                    String rutaCompleta = refactorizado.toString();
                    String rutaDestino = rutaCompleta.replace(refactorDir + "/", "src/main/java/")
                            .replace(".refactor.java", ".java");

                    Path destino = Paths.get(rutaDestino);
                    
                    bitacora.debug("🔀 Integrando: " + refactorizado.getFileName() + " → " + destino.getFileName());
                    
                    ResultadoIntegracion resultado = integrarArchivo(refactorizado, destino);
                    resultados.add(resultado);
                    
                    if (!resultado.esExitoso()) {
                        fallados.add(refactorizado);
                        bitacora.error("❌ Falló integración de: " + refactorizado.getFileName());
                    }

                } catch (Exception e) {
                    bitacora.error("💥 Error procesando " + refactorizado.getFileName(), e);
                    fallados.add(refactorizado);
                    resultados.add(ResultadoIntegracion.error(e.getMessage()));
                }
            }

            // 📊 Reporte final
            int exitos = (int) resultados.stream().filter(ResultadoIntegracion::esExitoso).count();
            int total = resultados.size();
            
            bitacora.info("🎯 RESULTADO INTEGRACIÓN: " + exitos + "/" + total + " exitos");
            
            if (!fallados.isEmpty()) {
                bitacora.warn("⚠️ Archivos con problemas: " + fallados.size());
                for (Path fallado : fallados) {
                    bitacora.warn("   - " + fallado.getFileName());
                }
            }

            // 🧹 Limpiar archivos temporales exitosos
            limpiarArchivosTemporales(resultados);

        } catch (Exception e) {
            bitacora.error("💥 Error catastrófico en integración completa", e);
        }
    }

    private void limpiarArchivosTemporales(List<ResultadoIntegracion> resultados) {
        int limpiados = 0;
        for (ResultadoIntegracion resultado : resultados) {
            if (resultado.esExitoso() && resultado.getArchivo() != null) {
                try {
                    String rutaRefactor = resultado.getArchivo().replace("src/main/java/", "autogen-output/refactor/")
                            .replace(".java", ".refactor.java");
                    Files.deleteIfExists(Paths.get(rutaRefactor));
                    limpiados++;
                } catch (IOException e) {
                    bitacora.debug("⚠️ No se pudo limpiar archivo temporal: " + e.getMessage());
                }
            }
        }
        if (limpiados > 0) {
            bitacora.info("🧹 Archivos temporales limpiados: " + limpiados);
        }
    }
}
