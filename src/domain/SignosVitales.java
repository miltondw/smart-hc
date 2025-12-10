package domain;

import java.time.LocalDateTime;

public class SignosVitales {
    private String id;
    private String pacienteId;
    private LocalDateTime fechaRegistro;
    private double temperatura; // Celsius
    private int presionSistolica; // mmHg
    private int presionDiastolica; // mmHg
    private int frecuenciaCardiaca; // latidos por minuto
    private int frecuenciaRespiratoria; // respiraciones por minuto
    private double saturacionOxigeno; // porcentaje
    private double peso; // kg
    private double altura; // metros
    private String observaciones;

    public SignosVitales() {
        this.fechaRegistro = LocalDateTime.now();
    }

    public SignosVitales(String id, String pacienteId) {
        this();
        this.id = id;
        this.pacienteId = pacienteId;
    }

    public double calcularIMC() {
        if (peso > 0 && altura > 0) {
            return peso / (altura * altura);
        }
        return 0;
    }

    public String getPresionArterial() {
        return presionSistolica + "/" + presionDiastolica;
    }

    public boolean esNormal() {
        boolean tempNormal = temperatura >= 36.0 && temperatura <= 37.5;
        boolean presionNormal = presionSistolica >= 90 && presionSistolica <= 140 &&
                presionDiastolica >= 60 && presionDiastolica <= 90;
        boolean fcNormal = frecuenciaCardiaca >= 60 && frecuenciaCardiaca <= 100;
        boolean frNormal = frecuenciaRespiratoria >= 12 && frecuenciaRespiratoria <= 20;
        boolean satNormal = saturacionOxigeno >= 95;

        return tempNormal && presionNormal && fcNormal && frNormal && satNormal;
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

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public int getPresionSistolica() {
        return presionSistolica;
    }

    public void setPresionSistolica(int presionSistolica) {
        this.presionSistolica = presionSistolica;
    }

    public int getPresionDiastolica() {
        return presionDiastolica;
    }

    public void setPresionDiastolica(int presionDiastolica) {
        this.presionDiastolica = presionDiastolica;
    }

    public int getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(int frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public int getFrecuenciaRespiratoria() {
        return frecuenciaRespiratoria;
    }

    public void setFrecuenciaRespiratoria(int frecuenciaRespiratoria) {
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
    }

    public double getSaturacionOxigeno() {
        return saturacionOxigeno;
    }

    public void setSaturacionOxigeno(double saturacionOxigeno) {
        this.saturacionOxigeno = saturacionOxigeno;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
