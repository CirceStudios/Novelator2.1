package com.elreinodelolvido.ellibertad.tripulacion;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;

import com.elreinodelolvido.ellibertad.engine.Bitacora;
import com.elreinodelolvido.ellibertad.util.FileUtils;
import com.elreinodelolvido.ellibertad.util.RollbackManager;
import com.elreinodelolvido.ellibertad.util.ValidadorFirmas;

/**
 * Vigía de los Commits — Integrador forzado de clases refactorizadas.
 * Valida firmas, guarda backup, y reemplaza el código original con precisión quirúrgica.
 * Si se detecta un Kraken, activa a Caronte para rollback inmediato.
 */
public class VigiaDeLosCommits {

    private final Bitacora bitacora;

    public VigiaDeLosCommits(Bitacora bitacora) {
        this.bitacora = Objects.requireNonNull(bitacora, "bitacora");
    }

    public void integrarArchivo(Path archivoGenerado, Path archivoDestino) {
        bitacora.log("📦 Integrando clase: " + archivoDestino.getFileName());

        try {
            // Validar firmas antes de sobreescribir
            String firmas = ValidadorFirmas.validarFirmasTurbo(archivoDestino.toString(), archivoGenerado.toString());
            FileUtils.writeToFile(archivoDestino + ".check.txt", firmas);

            if (!firmas.contains("✅")) {
                throw new KrakenException("❌ Firma inválida detectada. Aborting integration.");
            }

            // Guardar backup
            Path backup = archivoDestino.resolveSibling(archivoDestino.getFileName() + ".bak");
            Files.copy(archivoDestino, backup, StandardCopyOption.REPLACE_EXISTING);
            bitacora.log("🪣 Backup creado: " + backup.getFileName());

            // Integrar archivo
            Files.copy(archivoGenerado, archivoDestino, StandardCopyOption.REPLACE_EXISTING);
            bitacora.log("✅ Integración completada: " + archivoDestino.getFileName());

        } catch (KrakenException e) {
            bitacora.log("🐙 Kraken detectado en integración: " + e.getMessage());
            RollbackManager.restaurarArchivo(archivoDestino.toString());
        } catch (IOException e) {
            bitacora.log("💥 Error I/O al integrar archivo: " + archivoDestino.getFileName());
            FileUtils.atraparKraken("VigiaDeLosCommits", e);
        }
    }

    /**
     * Integra múltiples archivos generados sobre sus destinos.
     */
    public void integrarTodos(List<Path> archivosGenerados, List<Path> archivosDestino) {
        if (archivosGenerados.size() != archivosDestino.size()) {
            bitacora.log("⚠️ Número desigual de archivos generados/destino");
            return;
        }

        bitacora.log("🚀 Iniciando integración múltiple (" + archivosGenerados.size() + " clases)");

        for (int i = 0; i < archivosGenerados.size(); i++) {
            Path generado = archivosGenerados.get(i);
            Path destino = archivosDestino.get(i);

            bitacora.log("🔧 Integrando [" + (i + 1) + "/" + archivosGenerados.size() + "]: " + destino.getFileName());
            integrarArchivo(generado, destino);
        }

        bitacora.log("✅ Lote de integración completado.");
    }

    /**
     * Verifica si un archivo ya ha sido integrado correctamente.
     */
    public boolean estaIntegrado(Path archivoGenerado, Path archivoDestino) {
        try {
            if (!Files.exists(archivoDestino)) return false;

            byte[] original = Files.readAllBytes(archivoGenerado);
            byte[] destino = Files.readAllBytes(archivoDestino);

            return java.util.Arrays.equals(original, destino);
        } catch (IOException e) {
            bitacora.log("❓ No se pudo verificar integración: " + archivoDestino.getFileName());
            return false;
        }
    }

    /**
     * Excepción simbólica cuando un Kraken lógico impide la integración.
     */
    public static class KrakenException extends RuntimeException {
        public KrakenException(String mensaje) {
            super(mensaje);
        }
    }
}