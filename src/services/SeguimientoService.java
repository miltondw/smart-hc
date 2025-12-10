package services;

import domain.*;
import storage.DataStorage;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SeguimientoService {
    private static SeguimientoService instance;
    private DataStorage storage;

    private SeguimientoService() {
        this.storage = DataStorage.getInstance();
    }

    public static SeguimientoService getInstance() {
        if (instance == null) {
            instance = new SeguimientoService();
        }
        return instance;
    }

    public Seguimiento crearSeguimiento(String pacienteId, String medicoId) {
        Seguimiento seguimiento = new Seguimiento(UUID.randomUUID().toString(), pacienteId, medicoId);
        storage.guardarSeguimiento(seguimiento);
        return seguimiento;
    }

    public boolean actualizarSeguimiento(Seguimiento seguimiento) {
        return storage.actualizarSeguimiento(seguimiento);
    }

    public List<Seguimiento> obtenerSeguimientosPorPaciente(String pacienteId) {
        return storage.obtenerTodosLosSeguimientos().stream()
                .filter(s -> s.getPacienteId().equals(pacienteId))
                .collect(Collectors.toList());
    }

    public List<Seguimiento> obtenerSeguimientosPorMedico(String medicoId) {
        return storage.obtenerTodosLosSeguimientos().stream()
                .filter(s -> s.getMedicoId().equals(medicoId))
                .collect(Collectors.toList());
    }

    public List<Seguimiento> obtenerSeguimientosActivos() {
        return storage.obtenerTodosLosSeguimientos().stream()
                .filter(s -> "activo".equals(s.getEstadoSeguimiento()))
                .collect(Collectors.toList());
    }

    public List<Seguimiento> obtenerSeguimientosCronicos() {
        return storage.obtenerTodosLosSeguimientos().stream()
                .filter(Seguimiento::isEsCronico)
                .filter(s -> "activo".equals(s.getEstadoSeguimiento()))
                .collect(Collectors.toList());
    }

    public boolean completarSeguimiento(String seguimientoId) {
        List<Seguimiento> seguimientos = storage.obtenerTodosLosSeguimientos();
        for (Seguimiento seg : seguimientos) {
            if (seg.getId().equals(seguimientoId)) {
                seg.setEstadoSeguimiento("completado");
                return storage.actualizarSeguimiento(seg);
            }
        }
        return false;
    }

    public int contarSeguimientosActivosPorMedico(String medicoId) {
        return (int) storage.obtenerTodosLosSeguimientos().stream()
                .filter(s -> s.getMedicoId().equals(medicoId))
                .filter(s -> "activo".equals(s.getEstadoSeguimiento()))
                .count();
    }
}
