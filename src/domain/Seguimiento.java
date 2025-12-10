package domain;

import java.time.LocalDateTime;

public class Seguimiento {
    private String id;
    private String pacienteId;
    private String medicoId;
    private LocalDateTime fechaRegistro;
    private String diagnostico;
    private String tratamiento;
    private String evolucion;
    private String proximaCita;
    private boolean esCronico;
    private String estadoSeguimiento; // "activo", "completado", "suspendido"
    private String notas;

    public Seguimiento() {
        this.fechaRegistro = LocalDateTime.now();
        this.estadoSeguimiento = "activo";
    }

    public Seguimiento(String id, String pacienteId, String medicoId) {
        this();
        this.id = id;
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
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

    public String getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(String medicoId) {
        this.medicoId = medicoId;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getEvolucion() {
        return evolucion;
    }

    public void setEvolucion(String evolucion) {
        this.evolucion = evolucion;
    }

    public String getProximaCita() {
        return proximaCita;
    }

    public void setProximaCita(String proximaCita) {
        this.proximaCita = proximaCita;
    }

    public boolean isEsCronico() {
        return esCronico;
    }

    public void setEsCronico(boolean esCronico) {
        this.esCronico = esCronico;
    }

    public String getEstadoSeguimiento() {
        return estadoSeguimiento;
    }

    public void setEstadoSeguimiento(String estadoSeguimiento) {
        this.estadoSeguimiento = estadoSeguimiento;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
