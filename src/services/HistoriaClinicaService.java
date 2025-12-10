package services;

import domain.*;
import storage.DataStorage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HistoriaClinicaService {
    private static HistoriaClinicaService instance;
    private DataStorage storage;

    private HistoriaClinicaService() {
        this.storage = DataStorage.getInstance();
    }

    public static HistoriaClinicaService getInstance() {
        if (instance == null) {
            instance = new HistoriaClinicaService();
        }
        return instance;
    }

    public HistoriaClinica obtenerHistoriaClinica(String pacienteId) {
        Optional<HistoriaClinica> hc = storage.buscarHistoriaClinicaPorPaciente(pacienteId);

        if (hc.isPresent()) {
            return hc.get();
        } else {
            // Crear nueva historia clínica si no existe
            HistoriaClinica nueva = new HistoriaClinica(UUID.randomUUID().toString(), pacienteId);
            storage.guardarHistoriaClinica(nueva);
            return nueva;
        }
    }

    public boolean actualizarHistoriaClinica(HistoriaClinica historiaClinica) {
        historiaClinica.calcularPorcentajeCompletado();
        return storage.actualizarHistoriaClinica(historiaClinica);
    }

    public boolean agregarSignosVitales(String pacienteId, SignosVitales signos) {
        HistoriaClinica hc = obtenerHistoriaClinica(pacienteId);
        signos.setId(UUID.randomUUID().toString());
        signos.setPacienteId(pacienteId);
        hc.agregarSignosVitales(signos);
        return actualizarHistoriaClinica(hc);
    }

    public boolean agregarAntecedente(String pacienteId, Antecedentes antecedentes) {
        HistoriaClinica hc = obtenerHistoriaClinica(pacienteId);
        hc.setAntecedentes(antecedentes);
        return actualizarHistoriaClinica(hc);
    }

    public boolean agregarMotivoConsulta(String pacienteId, String motivo, String enfermedad) {
        HistoriaClinica hc = obtenerHistoriaClinica(pacienteId);
        hc.setMotivoConsulta(motivo);
        hc.setEnfermedadActual(enfermedad);
        return actualizarHistoriaClinica(hc);
    }

    public List<HistoriaClinica> obtenerTodasLasHistorias() {
        return storage.obtenerTodasLasHistoriasClinicas();
    }

    public List<HistoriaClinica> obtenerHistoriasCompletadas() {
        return storage.obtenerTodasLasHistoriasClinicas().stream()
                .filter(HistoriaClinica::isCompletada)
                .toList();
    }

    public double obtenerPorcentajeCompletado(String pacienteId) {
        HistoriaClinica hc = obtenerHistoriaClinica(pacienteId);
        return hc.getPorcentajeCompletado();
    }
}
