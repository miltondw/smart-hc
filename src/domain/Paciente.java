package domain;

import java.time.LocalDate;

public class Paciente extends Usuario {
    private String numeroHistoriaClinica;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String ciudad;
    private String genero;
    private String estadoCivil;
    private String ocupacion;
    private String contactoEmergencia;
    private String telefonoEmergencia;

    public Paciente() {
        super();
        setTipo(TipoUsuario.PACIENTE);
    }

    public Paciente(String id, String nombre, String apellido, String email, String password, String telefono) {
        super(id, nombre, apellido, email, password, telefono, TipoUsuario.PACIENTE);
        this.numeroHistoriaClinica = "HC-" + id;
    }

    // Getters y Setters
    public String getNumeroHistoriaClinica() {
        return numeroHistoriaClinica;
    }

    public void setNumeroHistoriaClinica(String numeroHistoriaClinica) {
        this.numeroHistoriaClinica = numeroHistoriaClinica;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        if (fechaNacimiento == null)
            return 0;
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getContactoEmergencia() {
        return contactoEmergencia;
    }

    public void setContactoEmergencia(String contactoEmergencia) {
        this.contactoEmergencia = contactoEmergencia;
    }

    public String getTelefonoEmergencia() {
        return telefonoEmergencia;
    }

    public void setTelefonoEmergencia(String telefonoEmergencia) {
        this.telefonoEmergencia = telefonoEmergencia;
    }
}
