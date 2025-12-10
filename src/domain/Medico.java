package domain;

import java.util.ArrayList;
import java.util.List;

public class Medico extends Usuario {
    private String matriculaProfesional;
    private String especialidad;
    private String institucion;
    private List<String> horarioAtencion;
    private String consultorio;

    public Medico() {
        super();
        setTipo(TipoUsuario.MEDICO);
        this.horarioAtencion = new ArrayList<>();
    }

    public Medico(String id, String nombre, String apellido, String email, String password, String telefono,
            String matriculaProfesional, String especialidad) {
        super(id, nombre, apellido, email, password, telefono, TipoUsuario.MEDICO);
        this.matriculaProfesional = matriculaProfesional;
        this.especialidad = especialidad;
        this.horarioAtencion = new ArrayList<>();
    }

    // Getters y Setters
    public String getMatriculaProfesional() {
        return matriculaProfesional;
    }

    public void setMatriculaProfesional(String matriculaProfesional) {
        this.matriculaProfesional = matriculaProfesional;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public List<String> getHorarioAtencion() {
        return horarioAtencion;
    }

    public void setHorarioAtencion(List<String> horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }

    public String getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(String consultorio) {
        this.consultorio = consultorio;
    }
}
