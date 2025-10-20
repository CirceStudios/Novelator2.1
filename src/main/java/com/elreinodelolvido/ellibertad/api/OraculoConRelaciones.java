package com.elreinodelolvido.ellibertad.api;

import java.util.HashSet;
import java.util.Set;

import com.elreinodelolvido.ellibertad.memoria.SistemaMemoriaPirata;

/**
 * 🔮 ORÁCULO MEJORADO CON GESTIÓN DE RELACIONES PIRATAS
 */
public class OraculoConRelaciones extends OraculoDeLaLibertad {
    
    private final SistemaMemoriaPirata memoria;
    
    public OraculoConRelaciones() {
        this.memoria = SistemaMemoriaPirata.obtenerInstancia();
    }
    
    @Override
    public String invocar(String prompt, String contexto, double temperatura) {
        // 🎯 MEJORAR EL PROMPT CON INFORMACIÓN DE RELACIONES
        String promptMejorado = enriquecerPromptConRelaciones(prompt, contexto);
        return super.invocar(promptMejorado, contexto, temperatura);
    }
    
    private String enriquecerPromptConRelaciones(String promptOriginal, String contexto) {
        // 🎯 IDENTIFICAR PIRATAS MENCIONADOS EN EL PROMPT
        Set<String> piratasMencionados = extraerPiratasDelPrompt(promptOriginal);
        
        if (piratasMencionados.isEmpty()) {
            return promptOriginal;
        }
        
        StringBuilder contextoRelaciones = new StringBuilder();
        contextoRelaciones.append("\n\n🧠 CONTEXTO DE RELACIONES PIRATAS:\n");
        
        for (String pirata : piratasMencionados) {
            memoria.obtenerMemoriaPirata(pirata).ifPresent(mem -> {
                contextoRelaciones.append("\n").append(pirata).append(":\n");
                
                // 🎯 MEJORES AMIGOS
                mem.obtenerMejorAmigo().ifPresent(amigo -> 
                    contextoRelaciones.append("  • Mejor amigo: ").append(amigo).append("\n"));
                
                // 🎯 HABILIDADES DESTACADAS
                contextoRelaciones.append("  • Habilidades: ")
                                 .append(String.join(", ", mem.getHabilidades())).append("\n");
                
                // 🎯 RECUERDOS RECIENTES RELEVANTES
                mem.getRecuerdos().stream()
                   .filter(r -> r.getImportancia() >= 7)
                   .limit(2)
                   .forEach(r -> contextoRelaciones.append("  • Recuerdo: ").append(r).append("\n"));
            });
        }
        
        return promptOriginal + contextoRelaciones.toString();
    }
    
    private Set<String> extraerPiratasDelPrompt(String prompt) {
        Set<String> piratas = new HashSet<>();
        // 🎯 BUSCAR NOMBRES DE PIRATAS CONOCIDOS EN EL PROMPT
        memoria.obtenerTodosLosPiratas().forEach(pirata -> {
            if (prompt.contains(pirata)) {
                piratas.add(pirata);
            }
        });
        return piratas;
    }
}
