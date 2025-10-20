package com.elreinodelolvido.ellibertad.scanner;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 🏴‍☠️ ClassMetadata - Metadatos avanzados para el análisis de clases
 * ¡Ahora con +300% de información estratégica pirata! ⚓
 */
public class ClassMetadata {
    private String fullyQualifiedName;
    private LocalDateTime scanTime;
    private int complexityScore;
    private double maintainabilityIndex;
    private Set<String> dependencies;
    private Set<String> dependents;
    private Map<String, Object> qualityMetrics;
    private List<String> codeSmells;
    private String architecturalRole;
    private boolean needsRefactoring;
    private String refactoringPriority; // LOW, MEDIUM, HIGH, CRITICAL
    
 // 🎯 COHESION SCORE - MÉTRICA DE COHESIÓN AVANZADA
    private double cohesionScore;
    private double couplingScore;
    private List<String> designPatterns;
    private Map<String, Double> qualityIndicators = new HashMap<>();
    private List<String> architecturalSmells = new ArrayList<>();
    private Map<String, Object> securityMetrics = new HashMap<>();

    // 🏗️ Constructores
    public ClassMetadata() {
        this.dependencies = new HashSet<>();
        this.dependents = new HashSet<>();
        this.qualityMetrics = new HashMap<>();
        this.codeSmells = new ArrayList<>();
        this.scanTime = LocalDateTime.now();
    }

    public ClassMetadata(String fullyQualifiedName) {
        this();
        this.fullyQualifiedName = fullyQualifiedName;
    }

    // 🎯 Getters y Setters
    public String getFullyQualifiedName() { return fullyQualifiedName; }
    public void setFullyQualifiedName(String fullyQualifiedName) { this.fullyQualifiedName = fullyQualifiedName; }

    public LocalDateTime getScanTime() { return scanTime; }
    public void setScanTime(LocalDateTime scanTime) { this.scanTime = scanTime; }

    public int getComplexityScore() { return complexityScore; }
    public void setComplexityScore(int complexityScore) { this.complexityScore = complexityScore; }

    public double getMaintainabilityIndex() { return maintainabilityIndex; }
    public void setMaintainabilityIndex(double maintainabilityIndex) { this.maintainabilityIndex = maintainabilityIndex; }

    public Set<String> getDependencies() { return Collections.unmodifiableSet(dependencies); }
    public void setDependencies(Set<String> dependencies) { this.dependencies = new HashSet<>(dependencies); }

    public Set<String> getDependents() { return Collections.unmodifiableSet(dependents); }
    public void setDependents(Set<String> dependents) { this.dependents = new HashSet<>(dependents); }

    public Map<String, Object> getQualityMetrics() { return Collections.unmodifiableMap(qualityMetrics); }
    public void setQualityMetrics(Map<String, Object> qualityMetrics) { this.qualityMetrics = new HashMap<>(qualityMetrics); }

    public List<String> getCodeSmells() { return Collections.unmodifiableList(codeSmells); }
    public void setCodeSmells(List<String> codeSmells) { this.codeSmells = new ArrayList<>(codeSmells); }

    public String getArchitecturalRole() { return architecturalRole; }
    public void setArchitecturalRole(String architecturalRole) { this.architecturalRole = architecturalRole; }

    public boolean isNeedsRefactoring() { return needsRefactoring; }
    public void setNeedsRefactoring(boolean needsRefactoring) { this.needsRefactoring = needsRefactoring; }

    public String getRefactoringPriority() { return refactoringPriority; }
    public void setRefactoringPriority(String refactoringPriority) { this.refactoringPriority = refactoringPriority; }

    // 🚀 Métodos de utilidad
    public void addDependency(String dependency) {
        this.dependencies.add(dependency);
    }

    public void addDependent(String dependent) {
        this.dependents.add(dependent);
    }

    public void addCodeSmell(String smell) {
        this.codeSmells.add(smell);
    }

    public void addQualityMetric(String metric, Object value) {
        this.qualityMetrics.put(metric, value);
    }

    public boolean hasCodeSmells() {
        return !codeSmells.isEmpty();
    }

    public boolean isHighPriority() {
        return "HIGH".equals(refactoringPriority) || "CRITICAL".equals(refactoringPriority);
    }

    public int getDependencyCount() {
        return dependencies.size();
    }

    public int getDependentCount() {
        return dependents.size();
    }

    public double calculateStability() {
        int fanOut = getDependencyCount();
        int fanIn = getDependentCount();
        return fanIn + fanOut == 0 ? 1.0 : (double) fanOut / (fanIn + fanOut);
    }

    @Override
    public String toString() {
        return String.format(
            "🏴‍☠️ %s | 🔗 Deps: %d | 📊 Compl: %d | 🎯 Prior: %s | 💩 Smells: %d",
            fullyQualifiedName,
            getDependencyCount(),
            complexityScore,
            refactoringPriority,
            codeSmells.size()
        );
    }

    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("📋 METADATOS DETALLADOS\n");
        sb.append("=====================\n");
        sb.append("🏷️  Clase: ").append(fullyQualifiedName).append("\n");
        sb.append("📅 Escaneada: ").append(scanTime).append("\n");
        sb.append("🎯 Rol Arquitectónico: ").append(architecturalRole != null ? architecturalRole : "No definido").append("\n");
        sb.append("📊 Métricas de Calidad:\n");
        
        qualityMetrics.forEach((key, value) -> {
            sb.append("   • ").append(key).append(": ").append(value).append("\n");
        });
        
        sb.append("🔗 Dependencias: ").append(dependencies.size()).append("\n");
        if (!dependencies.isEmpty()) {
            dependencies.forEach(dep -> sb.append("   • ").append(dep).append("\n"));
        }
        
        sb.append("🎯 Dependientes: ").append(dependents.size()).append("\n");
        if (!dependents.isEmpty()) {
            dependents.forEach(dep -> sb.append("   • ").append(dep).append("\n"));
        }
        
        sb.append("💩 Code Smells: ").append(codeSmells.size()).append("\n");
        if (!codeSmells.isEmpty()) {
            codeSmells.forEach(smell -> sb.append("   • ").append(smell).append("\n"));
        }
        
        sb.append("🔄 Necesita Refactor: ").append(needsRefactoring ? "✅ SÍ" : "❌ NO").append("\n");
        sb.append("🎪 Prioridad: ").append(refactoringPriority != null ? refactoringPriority : "NO DEFINIDA").append("\n");
        
        return sb.toString();
    }

    public String getClassName() {
        if (fullyQualifiedName == null) {
            return "";
        }
        int lastDot = fullyQualifiedName.lastIndexOf('.');
        return lastDot >= 0 ? fullyQualifiedName.substring(lastDot + 1) : fullyQualifiedName;
    }

    public void setCohesionScore(double cohesionScore) {
        this.cohesionScore = cohesionScore;
        // 🎯 CLASIFICACIÓN AUTOMÁTICA DE COHESIÓN
        String cohesionLevel = "BAJA";
        if (cohesionScore >= 0.8) cohesionLevel = "EXCELENTE";
        else if (cohesionScore >= 0.6) cohesionLevel = "ALTA";
        else if (cohesionScore >= 0.4) cohesionLevel = "MEDIA";
        
        this.qualityMetrics.put("cohesion_level", cohesionLevel);
        this.qualityMetrics.put("cohesion_score", cohesionScore);
        
        // 🎯 DETECCIÓN DE PROBLEMAS DE COHESIÓN
        if (cohesionScore < 0.3) {
            this.architecturalSmells.add("Cohesión extremadamente baja - posible God Class");
            this.needsRefactoring = true;
        }
    }

    public double getCohesionScore() {
        return cohesionScore;
    }

    // 🎯 COUPLING SCORE - MÉTRICA DE ACOPLAMIENTO AVANZADO
    public void setCouplingScore(int couplingScore) {
        this.couplingScore = couplingScore;
        this.qualityMetrics.put("coupling_score", couplingScore);
        
        // 🎯 CLASIFICACIÓN AUTOMÁTICA DE ACOPLAMIENTO
        String couplingLevel = "NORMAL";
        if (couplingScore > 15) couplingLevel = "MUY ALTO";
        else if (couplingScore > 10) couplingLevel = "ALTO";
        else if (couplingScore < 3) couplingLevel = "MUY BAJO";
        
        this.qualityMetrics.put("coupling_level", couplingLevel);
        
        // 🎯 DETECCIÓN DE ACOPLAMIENTO EXCESIVO
        if (couplingScore > 20) {
            this.architecturalSmells.add("Acoplamiento excesivo - posible dependencia cíclica");
            this.addCodeSmell("Demasiadas dependencias externas (" + couplingScore + ")");
            if (!"CRITICAL".equals(this.refactoringPriority)) {
                this.refactoringPriority = "HIGH";
            }
        }
    }

    public double getCouplingScore() {
        return couplingScore;
    }

    // 🎯 DESIGN PATTERNS - DETECCIÓN DE PATRONES ARQUITECTÓNICOS
    public void setDesignPatterns(List<String> patterns) {
        this.designPatterns = patterns != null ? new ArrayList<>(patterns) : new ArrayList<>();
        this.qualityMetrics.put("design_patterns", new ArrayList<>(this.designPatterns));
        this.qualityMetrics.put("patterns_count", this.designPatterns.size());
        
        // 🎯 ANÁLISIS DE MADUREZ ARQUITECTÓNICA
        if (!this.designPatterns.isEmpty()) {
            this.qualityMetrics.put("architectural_maturity", 
                this.designPatterns.size() >= 3 ? "ALTA" : 
                this.designPatterns.size() >= 1 ? "MEDIA" : "BAJA");
        }
        
        // 🎯 IMPACTO EN MANTENIBILIDAD
        if (this.designPatterns.contains("Singleton") || 
            this.designPatterns.contains("Factory") || 
            this.designPatterns.contains("Strategy")) {
            this.maintainabilityIndex += 5.0; // Patrones beneficiosos
        }
    }

    public List<String> getDesignPatterns() {
        return designPatterns != null ? 
            Collections.unmodifiableList(designPatterns) : 
            Collections.emptyList();
    }

    public void addDesignPattern(String pattern) {
        if (this.designPatterns == null) {
            this.designPatterns = new ArrayList<>();
        }
        if (!this.designPatterns.contains(pattern)) {
            this.designPatterns.add(pattern);
            setDesignPatterns(this.designPatterns); // Actualizar métricas
        }
    }

    public boolean usesDesignPattern(String pattern) {
        return designPatterns != null && designPatterns.contains(pattern);
    }

    // 🎯 MÉTRICAS DE CALIDAD AVANZADAS
    public void calculateAdvancedMetrics() {
        // 🎯 ÍNDICE DE MADUREZ TÉCNICA
        double maturityIndex = calculateTechnicalMaturityIndex();
        this.qualityMetrics.put("technical_maturity", maturityIndex);
        
        // 🎯 PUNTAJE DE CALIDAD GENERAL
        double qualityScore = calculateOverallQualityScore();
        this.qualityMetrics.put("overall_quality_score", qualityScore);
        
        // 🎯 RIESGO TÉCNICO
        double technicalRisk = calculateTechnicalRisk();
        this.qualityMetrics.put("technical_risk", technicalRisk);
        this.qualityMetrics.put("risk_level", 
            technicalRisk > 0.7 ? "ALTO" : 
            technicalRisk > 0.4 ? "MEDIO" : "BAJO");
    }

    private double calculateTechnicalMaturityIndex() {
        double score = 0.0;
        
        // 🎯 FACTORES DE MADUREZ
        if (this.designPatterns != null) score += this.designPatterns.size() * 0.1;
        if (this.cohesionScore > 0.6) score += 0.2;
        if (this.couplingScore < 10) score += 0.2;
        if (this.maintainabilityIndex > 70) score += 0.2;
        if (this.complexityScore < 20) score += 0.2;
        if (this.codeSmells.size() < 3) score += 0.1;
        
        return Math.min(1.0, score);
    }

    private double calculateOverallQualityScore() {
        double score = 100.0;
        
        // 🎯 PENALIZACIONES POR PROBLEMAS
        score -= this.codeSmells.size() * 2.5;
        score -= Math.max(0, this.complexityScore - 10) * 1.5;
        score -= Math.max(0, this.couplingScore - 5) * 2.0;
        score -= (1.0 - this.cohesionScore) * 25.0;
        score -= Math.max(0, 70 - this.maintainabilityIndex) * 0.5;
        
        // 🎯 BONIFICACIONES POR BUENAS PRÁCTICAS
        if (this.designPatterns != null) score += this.designPatterns.size() * 3.0;
        if (this.cohesionScore > 0.7) score += 5.0;
        if (this.couplingScore < 5) score += 5.0;
        
        return Math.max(0, Math.min(100, score));
    }

    private double calculateTechnicalRisk() {
        double risk = 0.0;
        
        // 🎯 FACTORES DE RIESGO
        if (this.complexityScore > 30) risk += 0.3;
        if (this.couplingScore > 15) risk += 0.3;
        if (this.cohesionScore < 0.3) risk += 0.2;
        if (this.maintainabilityIndex < 50) risk += 0.2;
        if (this.codeSmells.size() > 5) risk += 0.2;
        if ("CRITICAL".equals(this.refactoringPriority)) risk += 0.3;
        
        return Math.min(1.0, risk);
    }

    // 🎯 ANÁLISIS DE IMPACTO Y ESTABILIDAD
    public void calculateImpactMetrics() {
        // 🎯 ESTABILIDAD (I = Fan-out / (Fan-in + Fan-out))
        double instability = calculateStability();
        this.qualityMetrics.put("instability", instability);
        this.qualityMetrics.put("stability_category", 
            instability < 0.3 ? "ESTABLE" : 
            instability > 0.7 ? "INESTABLE" : "BALANCEADO");
        
        // 🎯 ABSTRACTNESS (A = abstract_elements / total_elements)
        double abstractness = calculateAbstractness();
        this.qualityMetrics.put("abstractness", abstractness);
        
        // 🎯 DISTANCIA DEL EJE PRINCIPAL (D = |A + I - 1|)
        double distanceFromMainSequence = Math.abs(abstractness + instability - 1);
        this.qualityMetrics.put("distance_from_main_sequence", distanceFromMainSequence);
        this.qualityMetrics.put("architectural_zone", 
            distanceFromMainSequence < 0.1 ? "ZONA IDEAL" :
            distanceFromMainSequence < 0.3 ? "ZONA ACEPTABLE" : "ZONA DE RIESGO");
    }

    private double calculateAbstractness() {
        // 🎯 SIMULACIÓN DE CÁLCULO DE ABSTRACTNESS
        // En implementación real, necesitarías datos de métodos/classes abstractas
        double abstractness = 0.0;
        
        if (this.fullyQualifiedName != null) {
            if (this.fullyQualifiedName.contains("Abstract") || 
                this.fullyQualifiedName.contains("Interface") ||
                this.architecturalRole != null && this.architecturalRole.equals("CONTRACT")) {
                abstractness = 0.8;
            } else if (this.qualityMetrics.containsKey("abstract_methods_count")) {
                int abstractMethods = (int) this.qualityMetrics.get("abstract_methods_count");
                int totalMethods = (int) this.qualityMetrics.getOrDefault("total_methods", 1);
                abstractness = (double) abstractMethods / totalMethods;
            }
        }
        
        return abstractness;
    }

    // 🎯 ANÁLISIS DE SEGURIDAD
    public void analyzeSecurityConcerns() {
        this.securityMetrics.clear();
        
        // 🎯 DETECCIÓN DE PATRONES DE SEGURIDAD
        if (this.fullyQualifiedName != null) {
            String className = this.fullyQualifiedName.toLowerCase();
            
            // 🎯 DETECCIÓN DE COMPONENTES CRÍTICOS
            if (className.contains("password") || className.contains("credential") || 
                className.contains("token") || className.contains("auth")) {
                this.securityMetrics.put("security_sensitive", true);
                this.securityMetrics.put("sensitivity_level", "HIGH");
            }
            
            if (className.contains("controller") || className.contains("endpoint") || 
                className.contains("api")) {
                this.securityMetrics.put("entry_point", true);
            }
            
            if (className.contains("service") || className.contains("business")) {
                this.securityMetrics.put("business_logic", true);
            }
            
            if (className.contains("repository") || className.contains("dao")) {
                this.securityMetrics.put("data_access", true);
            }
        }
        
        // 🎯 ANÁLISIS DE VULNERABILIDADES POTENCIALES
        List<String> potentialRisks = new ArrayList<>();
        
        if (this.couplingScore > 20) {
            potentialRisks.add("Alto acoplamiento - posible punto único de fallo");
        }
        
        if (this.complexityScore > 40) {
            potentialRisks.add("Alta complejidad - difícil de auditar");
        }
        
        if (this.dependencies.size() > 15) {
            potentialRisks.add("Muchas dependencias - superficie de ataque ampliada");
        }
        
        if (!potentialRisks.isEmpty()) {
            this.securityMetrics.put("potential_risks", potentialRisks);
            this.securityMetrics.put("security_risk_level", 
                potentialRisks.size() > 3 ? "HIGH" : 
                potentialRisks.size() > 1 ? "MEDIUM" : "LOW");
        }
    }

    // 🎯 MÉTRICAS DE EVOLUCIÓN
    public void calculateEvolutionMetrics() {
        // 🎯 TENDENCIA DE CAMBIOS (simulado - en realidad necesitarías histórico)
        double changeProbability = calculateChangeProbability();
        this.qualityMetrics.put("change_probability", changeProbability);
        this.qualityMetrics.put("evolution_stability", 
            changeProbability < 0.3 ? "ESTABLE" :
            changeProbability > 0.7 ? "VOLATIL" : "EVOLUCIONANDO");
    }

    private double calculateChangeProbability() {
        double probability = 0.0;
        
        // 🎯 FACTORES QUE INDICAN PROBABILIDAD DE CAMBIO
        if (this.architecturalRole != null && 
            ("CONTROLLER".equals(this.architecturalRole) || "SERVICE".equals(this.architecturalRole))) {
            probability += 0.4;
        }
        
        if (this.couplingScore > 10) probability += 0.3;
        if (this.dependents.size() > 5) probability += 0.2;
        if (this.complexityScore > 25) probability += 0.1;
        
        return Math.min(1.0, probability);
    }

    // 🎯 REPORTES AVANZADOS
    public Map<String, Object> generateComprehensiveReport() {
        Map<String, Object> report = new LinkedHashMap<>();
        
        report.put("class_name", this.fullyQualifiedName);
        report.put("scan_timestamp", this.scanTime);
        report.put("architectural_role", this.architecturalRole);
        
        // 🎯 MÉTRICAS PRINCIPALES
        Map<String, Object> coreMetrics = new HashMap<>();
        coreMetrics.put("maintainability_index", this.maintainabilityIndex);
        coreMetrics.put("complexity_score", this.complexityScore);
        coreMetrics.put("cohesion_score", this.cohesionScore);
        coreMetrics.put("coupling_score", this.couplingScore);
        coreMetrics.put("instability", calculateStability());
        coreMetrics.put("abstractness", calculateAbstractness());
        report.put("core_metrics", coreMetrics);
        
        // 🎯 ANÁLISIS DE CALIDAD
        Map<String, Object> qualityAnalysis = new HashMap<>();
        qualityAnalysis.put("overall_quality_score", calculateOverallQualityScore());
        qualityAnalysis.put("technical_maturity", calculateTechnicalMaturityIndex());
        qualityAnalysis.put("technical_risk", calculateTechnicalRisk());
        qualityAnalysis.put("refactoring_priority", this.refactoringPriority);
        qualityAnalysis.put("needs_refactoring", this.needsRefactoring);
        report.put("quality_analysis", qualityAnalysis);
        
        // 🎯 PATRONES Y SMELLS
        Map<String, Object> patternsAndSmells = new HashMap<>();
        patternsAndSmells.put("design_patterns", this.designPatterns != null ? this.designPatterns : Collections.emptyList());
        patternsAndSmells.put("code_smells", this.codeSmells);
        patternsAndSmells.put("architectural_smells", this.architecturalSmells);
        patternsAndSmells.put("security_concerns", this.securityMetrics);
        report.put("patterns_and_smells", patternsAndSmells);
        
        // 🎯 DEPENDENCIAS
        Map<String, Object> dependencyAnalysis = new HashMap<>();
        dependencyAnalysis.put("dependencies_count", this.dependencies.size());
        dependencyAnalysis.put("dependents_count", this.dependents.size());
        dependencyAnalysis.put("critical_dependencies", findCriticalDependencies());
        report.put("dependency_analysis", dependencyAnalysis);
        
        // 🎯 RECOMENDACIONES
        report.put("recommendations", generateRecommendations());
        
        return report;
    }

    private List<String> findCriticalDependencies() {
        List<String> critical = new ArrayList<>();
        
        if (this.dependencies != null) {
            for (String dependency : this.dependencies) {
                if (dependency.contains("framework") || 
                    dependency.contains("core") ||
                    dependency.contains("util") ||
                    dependency.startsWith("java.") ||
                    dependency.startsWith("javax.")) {
                    critical.add(dependency);
                }
            }
        }
        
        return critical;
    }

    private List<String> generateRecommendations() {
        List<String> recommendations = new ArrayList<>();
        
        if (this.cohesionScore < 0.4) {
            recommendations.add("Considerar dividir la clase - cohesión muy baja");
        }
        
        if (this.couplingScore > 15) {
            recommendations.add("Reducir dependencias - acoplamiento muy alto");
        }
        
        if (this.complexityScore > 30) {
            recommendations.add("Simplificar lógica - complejidad ciclomática elevada");
        }
        
        if (this.maintainabilityIndex < 50) {
            recommendations.add("Refactorizar urgentemente - mantenibilidad crítica");
        }
        
        if (this.codeSmells.size() > 5) {
            recommendations.add("Eliminar code smells prioritarios");
        }
        
        if (this.designPatterns == null || this.designPatterns.isEmpty()) {
            recommendations.add("Considerar aplicar patrones de diseño apropiados");
        }
        
        return recommendations;
    }

    // 🎯 MÉTODOS DE ACCESO ADICIONALES
    public Map<String, Object> getSecurityMetrics() {
        return Collections.unmodifiableMap(securityMetrics);
    }

    public List<String> getArchitecturalSmells() {
        return Collections.unmodifiableList(architecturalSmells);
    }

    public void addArchitecturalSmell(String smell) {
        this.architecturalSmells.add(smell);
    }

    public boolean hasSecurityConcerns() {
        return !this.securityMetrics.isEmpty();
    }

    public boolean isArchitecturallySound() {
        return this.architecturalSmells.isEmpty() && 
               this.cohesionScore > 0.5 && 
               this.couplingScore < 10;
    }
}