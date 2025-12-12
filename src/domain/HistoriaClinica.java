package domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoriaClinica {
    private String id;
    private String pacienteId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Antecedentes antecedentes;
    private String motivoConsulta;
    private String enfermedadActual;
    private String diagnostico;
    private String nivelRiesgo; // BAJO, MEDIO, ALTO
    private List<SignosVitales> signosVitales;
    private List<String> adjuntos;
    private List<Seguimiento> seguimientos;
    private double porcentajeCompletado;
    private boolean completada;

    public HistoriaClinica() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.antecedentes = new Antecedentes();
        this.signosVitales = new ArrayList<>();
        this.adjuntos = new ArrayList<>();
        this.seguimientos = new ArrayList<>();
        this.completada = false;
        this.porcentajeCompletado = 0.0;
    }

    public HistoriaClinica(String id, String pacienteId) {
        this();
        this.id = id;
        this.pacienteId = pacienteId;
    }

    public void calcularPorcentajeCompletado() {
        int camposTotal = 10;
        int camposCompletos = 0;

        if (antecedentes != null && antecedentes.isCompleto())
            camposCompletos++;
        if (motivoConsulta != null && !motivoConsulta.isEmpty())
            camposCompletos++;
        if (enfermedadActual != null && !enfermedadActual.isEmpty())
            camposCompletos++;
        if (!signosVitales.isEmpty())
            camposCompletos += 3;
        if (!adjuntos.isEmpty())
            camposCompletos++;
        if (!seguimientos.isEmpty())
            camposCompletos++;

        this.porcentajeCompletado = (camposCompletos * 100.0) / camposTotal;
        this.completada = porcentajeCompletado >= 80;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(String pacienteId) {
        this.pacienteId = pacienteId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Antecedentes getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(Antecedentes antecedentes) {
        this.antecedentes = antecedentes;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
        this.fechaActualizacion = LocalDateTime.now();
        calcularPorcentajeCompletado();
    }

    public String getEnfermedadActual() {
        return enfermedadActual;
    }

    public void setEnfermedadActual(String enfermedadActual) {
        this.enfermedadActual = enfermedadActual;
        this.fechaActualizacion = LocalDateTime.now();
        calcularPorcentajeCompletado();
    }

    public List<SignosVitales> getSignosVitales() {
        return signosVitales;
    }

    public void setSignosVitales(List<SignosVitales> signosVitales) {
        this.signosVitales = signosVitales;
    }

    public void agregarSignosVitales(SignosVitales signos) {
        this.signosVitales.add(signos);
        this.fechaActualizacion = LocalDateTime.now();
        calcularPorcentajeCompletado();
    }

    public List<String> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<String> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public void agregarAdjunto(String adjunto) {
        this.adjuntos.add(adjunto);
        this.fechaActualizacion = LocalDateTime.now();
        calcularPorcentajeCompletado();
    }

    public List<Seguimiento> getSeguimientos() {
        return seguimientos;
    }

    public void setSeguimientos(List<Seguimiento> seguimientos) {
        this.seguimientos = seguimientos;
    }

    public void agregarSeguimiento(Seguimiento seguimiento) {
        this.seguimientos.add(seguimiento);
        this.fechaActualizacion = LocalDateTime.now();
    }

    public double getPorcentajeCompletado() {
        return porcentajeCompletado;
    }

    public void setPorcentajeCompletado(double porcentajeCompletado) {
        this.porcentajeCompletado = porcentajeCompletado;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public String getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(String nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
        this.fechaActualizacion = LocalDateTime.now();
    }
}
