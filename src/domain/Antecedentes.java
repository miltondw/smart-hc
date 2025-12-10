package domain;

import java.util.ArrayList;
import java.util.List;

public class Antecedentes {
    private List<String> enfermedadesPersonales;
    private List<String> enfermedadesFamiliares;
    private List<String> cirugias;
    private List<String> alergias;
    private List<String> medicamentosActuales;
    private List<String> vacunas;
    private String habitosAlcohol;
    private String habitosTabaco;
    private String habitosEjercicio;
    private String habitosAlimentacion;

    public Antecedentes() {
        this.enfermedadesPersonales = new ArrayList<>();
        this.enfermedadesFamiliares = new ArrayList<>();
        this.cirugias = new ArrayList<>();
        this.alergias = new ArrayList<>();
        this.medicamentosActuales = new ArrayList<>();
        this.vacunas = new ArrayList<>();
    }

    public boolean isCompleto() {
        return !enfermedadesPersonales.isEmpty() ||
                !enfermedadesFamiliares.isEmpty() ||
                !cirugias.isEmpty() ||
                !alergias.isEmpty() ||
                !medicamentosActuales.isEmpty();
    }

    // Getters y Setters
    public List<String> getEnfermedadesPersonales() {
        return enfermedadesPersonales;
    }

    public void setEnfermedadesPersonales(List<String> enfermedadesPersonales) {
        this.enfermedadesPersonales = enfermedadesPersonales;
    }

    public void agregarEnfermedadPersonal(String enfermedad) {
        this.enfermedadesPersonales.add(enfermedad);
    }

    public List<String> getEnfermedadesFamiliares() {
        return enfermedadesFamiliares;
    }

    public void setEnfermedadesFamiliares(List<String> enfermedadesFamiliares) {
        this.enfermedadesFamiliares = enfermedadesFamiliares;
    }

    public void agregarEnfermedadFamiliar(String enfermedad) {
        this.enfermedadesFamiliares.add(enfermedad);
    }

    public List<String> getCirugias() {
        return cirugias;
    }

    public void setCirugias(List<String> cirugias) {
        this.cirugias = cirugias;
    }

    public void agregarCirugia(String cirugia) {
        this.cirugias.add(cirugia);
    }

    public List<String> getAlergias() {
        return alergias;
    }

    public void setAlergias(List<String> alergias) {
        this.alergias = alergias;
    }

    public void agregarAlergia(String alergia) {
        this.alergias.add(alergia);
    }

    public List<String> getMedicamentosActuales() {
        return medicamentosActuales;
    }

    public void setMedicamentosActuales(List<String> medicamentosActuales) {
        this.medicamentosActuales = medicamentosActuales;
    }

    public void agregarMedicamento(String medicamento) {
        this.medicamentosActuales.add(medicamento);
    }

    public List<String> getVacunas() {
        return vacunas;
    }

    public void setVacunas(List<String> vacunas) {
        this.vacunas = vacunas;
    }

    public void agregarVacuna(String vacuna) {
        this.vacunas.add(vacuna);
    }

    public String getHabitosAlcohol() {
        return habitosAlcohol;
    }

    public void setHabitosAlcohol(String habitosAlcohol) {
        this.habitosAlcohol = habitosAlcohol;
    }

    public String getHabitosTabaco() {
        return habitosTabaco;
    }

    public void setHabitosTabaco(String habitosTabaco) {
        this.habitosTabaco = habitosTabaco;
    }

    public String getHabitosEjercicio() {
        return habitosEjercicio;
    }

    public void setHabitosEjercicio(String habitosEjercicio) {
        this.habitosEjercicio = habitosEjercicio;
    }

    public String getHabitosAlimentacion() {
        return habitosAlimentacion;
    }

    public void setHabitosAlimentacion(String habitosAlimentacion) {
        this.habitosAlimentacion = habitosAlimentacion;
    }
}
