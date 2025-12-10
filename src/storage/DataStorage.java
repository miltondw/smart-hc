package storage;

import domain.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import utils.LocalDateTimeAdapter;
import utils.LocalDateAdapter;
import utils.UsuarioAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;

public class DataStorage {
    private static DataStorage instance;
    private final String dataDirectory = "data/";
    private final Gson gson;

    private List<Usuario> usuarios;
    private List<HistoriaClinica> historiasClinicas;
    private List<Seguimiento> seguimientos;

    private DataStorage() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(Usuario.class, new UsuarioAdapter())
                .setPrettyPrinting()
                .create();
        inicializarDirectorios();
        cargarDatos();
        inicializarDatosPrueba();
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    private void inicializarDirectorios() {
        try {
            Files.createDirectories(Paths.get(dataDirectory));
        } catch (IOException e) {
            System.err.println("Error creando directorios: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        usuarios = cargarDesdeArchivo("usuarios.json", new TypeToken<List<Usuario>>() {
        }.getType());
        historiasClinicas = cargarDesdeArchivo("historias_clinicas.json", new TypeToken<List<HistoriaClinica>>() {
        }.getType());
        seguimientos = cargarDesdeArchivo("seguimientos.json", new TypeToken<List<Seguimiento>>() {
        }.getType());

        if (usuarios == null)
            usuarios = new ArrayList<>();
        if (historiasClinicas == null)
            historiasClinicas = new ArrayList<>();
        if (seguimientos == null)
            seguimientos = new ArrayList<>();

        // Limpiar nulos de las listas
        usuarios.removeIf(u -> u == null);
        historiasClinicas.removeIf(hc -> hc == null);
        seguimientos.removeIf(s -> s == null);
    }

    private <T> List<T> cargarDesdeArchivo(String nombreArchivo, Type tipo) {
        File archivo = new File(dataDirectory + nombreArchivo);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(archivo)) {
            return gson.fromJson(reader, tipo);
        } catch (IOException e) {
            System.err.println("Error leyendo " + nombreArchivo + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void guardarEnArchivo(String nombreArchivo, Object datos) {
        try (Writer writer = new FileWriter(dataDirectory + nombreArchivo)) {
            gson.toJson(datos, writer);
        } catch (IOException e) {
            System.err.println("Error guardando " + nombreArchivo + ": " + e.getMessage());
        }
    }

    // Métodos para Usuarios
    public Optional<Usuario> buscarUsuarioPorEmail(String email) {
        return usuarios.stream()
                .filter(u -> u != null && u.getEmail() != null)
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    public Optional<Usuario> buscarUsuarioPorId(String id) {
        return usuarios.stream()
                .filter(u -> u != null && u.getId() != null)
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public boolean guardarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        guardarEnArchivo("usuarios.json", usuarios);
        return true;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            if (u != null && u.getId() != null && u.getId().equals(usuario.getId())) {
                usuarios.set(i, usuario);
                guardarEnArchivo("usuarios.json", usuarios);
                return true;
            }
        }
        return false;
    }

    public List<Paciente> obtenerTodosLosPacientes() {
        return usuarios.stream()
                .filter(u -> u != null && u instanceof Paciente)
                .map(u -> (Paciente) u)
                .toList();
    }

    // Métodos para Historias Clínicas
    public Optional<HistoriaClinica> buscarHistoriaClinicaPorPaciente(String pacienteId) {
        return historiasClinicas.stream()
                .filter(hc -> hc.getPacienteId().equals(pacienteId))
                .findFirst();
    }

    public boolean guardarHistoriaClinica(HistoriaClinica historiaClinica) {
        historiasClinicas.add(historiaClinica);
        guardarEnArchivo("historias_clinicas.json", historiasClinicas);
        return true;
    }

    public boolean actualizarHistoriaClinica(HistoriaClinica historiaClinica) {
        for (int i = 0; i < historiasClinicas.size(); i++) {
            if (historiasClinicas.get(i).getId().equals(historiaClinica.getId())) {
                historiasClinicas.set(i, historiaClinica);
                guardarEnArchivo("historias_clinicas.json", historiasClinicas);
                return true;
            }
        }
        return false;
    }

    public List<HistoriaClinica> obtenerTodasLasHistoriasClinicas() {
        return new ArrayList<>(historiasClinicas);
    }

    // Métodos para Seguimientos
    public boolean guardarSeguimiento(Seguimiento seguimiento) {
        seguimientos.add(seguimiento);
        guardarEnArchivo("seguimientos.json", seguimientos);
        return true;
    }

    public boolean actualizarSeguimiento(Seguimiento seguimiento) {
        for (int i = 0; i < seguimientos.size(); i++) {
            if (seguimientos.get(i).getId().equals(seguimiento.getId())) {
                seguimientos.set(i, seguimiento);
                guardarEnArchivo("seguimientos.json", seguimientos);
                return true;
            }
        }
        return false;
    }

    public List<Seguimiento> obtenerTodosLosSeguimientos() {
        return new ArrayList<>(seguimientos);
    }

    // Inicializar datos de prueba
    private void inicializarDatosPrueba() {
        if (usuarios.isEmpty()) {
            // Crear médico de prueba
            Medico medico = new Medico("M001", "Dr. Carlos", "Ramírez",
                    "medico@smarthc.com", "medico123", "3001234567",
                    "MP-12345", "Medicina General");
            medico.setInstitucion("Hospital Central");
            guardarUsuario(medico);

            // Crear paciente de prueba
            Paciente paciente = new Paciente("P001", "María", "González",
                    "paciente@smarthc.com", "paciente123", "3009876543");
            paciente.setGenero("Femenino");
            paciente.setCiudad("Bogotá");
            guardarUsuario(paciente);
        }
    }
}
