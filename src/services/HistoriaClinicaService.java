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

    public HistoriaClinica obtenerHistoriaPorPaciente(String pacienteId) {
        Optional<HistoriaClinica> hc = storage.buscarHistoriaClinicaPorPaciente(pacienteId);
        return hc.orElse(null);
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
        Optional<Usuario> usuario = storage.buscarUsuarioPorId(pacienteId);

        if (usuario.isEmpty() || !(usuario.get() instanceof Paciente)) {
            return hc.getPorcentajeCompletado();
        }

        Paciente paciente = (Paciente) usuario.get();

        int camposTotal = 10; // Total de campos a evaluar
        int camposCompletos = 0;

        // Datos personales del paciente (3 campos)
        if (paciente.getFechaNacimiento() != null) {
            camposCompletos++;
        }

        if (paciente.getDireccion() != null && !paciente.getDireccion().isEmpty()) {
            camposCompletos++;
        }

        if (paciente.getContactoEmergencia() != null && !paciente.getContactoEmergencia().isEmpty()) {
            camposCompletos++;
        }

        // Antecedentes (2 campos)
        Antecedentes ant = hc.getAntecedentes();
        if (ant != null && ant.isCompleto()) {
            camposCompletos += 2;
        }

        // Motivo de consulta (2 campos)
        if (hc.getMotivoConsulta() != null && !hc.getMotivoConsulta().isEmpty()) {
            camposCompletos++;
        }
        if (hc.getEnfermedadActual() != null && !hc.getEnfermedadActual().isEmpty()) {
            camposCompletos++;
        }

        // Signos vitales (4 campos: tipo sangre + peso + altura + valores vitales
        // básicos)
        if (!hc.getSignosVitales().isEmpty()) {
            SignosVitales ultimos = hc.getSignosVitales().get(hc.getSignosVitales().size() - 1);

            // Tipo de sangre
            if (ultimos.getTipoSangre() != null && !ultimos.getTipoSangre().isEmpty()) {
                camposCompletos++;
                System.out.println("✓ Tipo de sangre: " + ultimos.getTipoSangre());
            } else {
                System.out.println("✗ Tipo de sangre vacío");
            }

            // Peso y altura
            if (ultimos.getPeso() > 0 && ultimos.getAltura() > 0) {
                camposCompletos++;
                System.out.println("✓ Peso y altura registrados");
            } else {
                System.out.println("✗ Peso o altura no registrados");
            }

            // Signos vitales básicos
            if (ultimos.getTemperatura() > 0 && ultimos.getPresionSistolica() > 0) {
                camposCompletos++;
            }
        }

        double porcentaje = (camposCompletos * 100.0) / camposTotal;
        hc.setPorcentajeCompletado(porcentaje);
        hc.setCompletada(porcentaje >= 80);

        return porcentaje;
    }
}
