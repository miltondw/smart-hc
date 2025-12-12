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
            // ============ CREAR MÉDICOS ============

            // Médico 1 - Medicina General
            Medico medico1 = new Medico("M001", "Dr. Carlos Eduardo", "Díaz Mendoza",
                    "carlos.diaz@smarthc.com", "medico123", "3012458796",
                    "MP-54321", "Medicina General");
            medico1.setInstitucion("Hospital San Juan de Dios - Ocaña");
            guardarUsuario(medico1);

            // Médico 2 - Pediatría
            Medico medico2 = new Medico("M002", "Dra. María Isabel", "Contreras Silva",
                    "maria.contreras@smarthc.com", "medico123", "3015647892",
                    "MP-65432", "Pediatría");
            medico2.setInstitucion("Clínica Norte - Ocaña");
            guardarUsuario(medico2);

            // Médico 3 - Cardiología
            Medico medico3 = new Medico("M003", "Dr. José Antonio", "Ramírez Cáceres",
                    "jose.ramirez@smarthc.com", "medico123", "3018765432",
                    "MP-78543", "Cardiología");
            medico3.setInstitucion("Hospital San Juan de Dios - Ocaña");
            guardarUsuario(medico3);

            // ============ CREAR PACIENTES ============

            // Paciente 1
            Paciente p1 = new Paciente("P001", "Ana Lucía", "Rojas Peña",
                    "ana.rojas@email.com", "paciente123", "3015678943");
            p1.setGenero("Femenino");
            p1.setCiudad("Ocaña");
            p1.setDireccion("Calle 10 # 15-42, Barrio La Primavera");
            p1.setFechaNacimiento(java.time.LocalDate.of(1995, 3, 15));
            p1.setEstadoCivil("Soltera");
            p1.setOcupacion("Docente");
            p1.setContactoEmergencia("Luis Alberto Rojas");
            p1.setTelefonoEmergencia("3008765432");
            guardarUsuario(p1);

            // Paciente 2
            Paciente p2 = new Paciente("P002", "Miguel Ángel", "Torres Gómez",
                    "miguel.torres@email.com", "paciente123", "3019874563");
            p2.setGenero("Masculino");
            p2.setCiudad("Ocaña");
            p2.setDireccion("Carrera 8 # 22-15, Centro");
            p2.setFechaNacimiento(java.time.LocalDate.of(1988, 7, 22));
            p2.setEstadoCivil("Casado");
            p2.setOcupacion("Comerciante");
            p2.setContactoEmergencia("Sandra Milena Gómez");
            p2.setTelefonoEmergencia("3016543287");
            guardarUsuario(p2);

            // Paciente 3
            Paciente p3 = new Paciente("P003", "Laura Sofía", "Martínez Duarte",
                    "laura.martinez@email.com", "paciente123", "3007896541");
            p3.setGenero("Femenino");
            p3.setCiudad("Ocaña");
            p3.setDireccion("Avenida Circunvalar # 5-78, Ciudadela Norte");
            p3.setFechaNacimiento(java.time.LocalDate.of(2002, 11, 8));
            p3.setEstadoCivil("Soltera");
            p3.setOcupacion("Estudiante");
            p3.setContactoEmergencia("Roberto Martínez");
            p3.setTelefonoEmergencia("3014567893");
            guardarUsuario(p3);

            // Paciente 4
            Paciente p4 = new Paciente("P004", "Juan Pablo", "Villamizar Ortiz",
                    "juan.villamizar@email.com", "paciente123", "3012345678");
            p4.setGenero("Masculino");
            p4.setCiudad("Ocaña");
            p4.setDireccion("Calle 15 # 12-34, Barrio San Agustín");
            p4.setFechaNacimiento(java.time.LocalDate.of(1975, 5, 18));
            p4.setEstadoCivil("Casado");
            p4.setOcupacion("Ingeniero");
            p4.setContactoEmergencia("Patricia Ortiz");
            p4.setTelefonoEmergencia("3019876543");
            guardarUsuario(p4);

            // Paciente 5
            Paciente p5 = new Paciente("P005", "Claudia Patricia", "Sánchez Ruiz",
                    "claudia.sanchez@email.com", "paciente123", "3015432187");
            p5.setGenero("Femenino");
            p5.setCiudad("Ocaña");
            p5.setDireccion("Transversal 3 # 8-56, Barrio El Carmen");
            p5.setFechaNacimiento(java.time.LocalDate.of(1990, 9, 25));
            p5.setEstadoCivil("Divorciada");
            p5.setOcupacion("Enfermera");
            p5.setContactoEmergencia("Martha Ruiz");
            p5.setTelefonoEmergencia("3007654321");
            guardarUsuario(p5);

            // Paciente 6
            Paciente p6 = new Paciente("P006", "Pedro Antonio", "Cárdenas López",
                    "pedro.cardenas@email.com", "paciente123", "3018765432");
            p6.setGenero("Masculino");
            p6.setCiudad("Ocaña");
            p6.setDireccion("Calle 20 # 18-90, Barrio La Esperanza");
            p6.setFechaNacimiento(java.time.LocalDate.of(1968, 2, 14));
            p6.setEstadoCivil("Casado");
            p6.setOcupacion("Agricultor");
            p6.setContactoEmergencia("Rosa López");
            p6.setTelefonoEmergencia("3012987654");
            guardarUsuario(p6);

            // Paciente 7
            Paciente p7 = new Paciente("P007", "Diana Carolina", "Peña Guerrero",
                    "diana.pena@email.com", "paciente123", "3016789543");
            p7.setGenero("Femenino");
            p7.setCiudad("Ocaña");
            p7.setDireccion("Carrera 11 # 14-23, Centro Histórico");
            p7.setFechaNacimiento(java.time.LocalDate.of(1998, 6, 30));
            p7.setEstadoCivil("Soltera");
            p7.setOcupacion("Diseñadora Gráfica");
            p7.setContactoEmergencia("Carlos Peña");
            p7.setTelefonoEmergencia("3015678921");
            guardarUsuario(p7);

            // Paciente 8
            Paciente p8 = new Paciente("P008", "Roberto Carlos", "Moreno Acosta",
                    "roberto.moreno@email.com", "paciente123", "3009876543");
            p8.setGenero("Masculino");
            p8.setCiudad("Ocaña");
            p8.setDireccion("Diagonal 7 # 16-45, Barrio Buenos Aires");
            p8.setFechaNacimiento(java.time.LocalDate.of(1983, 12, 5));
            p8.setEstadoCivil("Unión Libre");
            p8.setOcupacion("Mecánico");
            p8.setContactoEmergencia("Liliana Acosta");
            p8.setTelefonoEmergencia("3018765432");
            guardarUsuario(p8);

            // Paciente 9
            Paciente p9 = new Paciente("P009", "Sandra Milena", "Blanco Rivera",
                    "sandra.blanco@email.com", "paciente123", "3014567891");
            p9.setGenero("Femenino");
            p9.setCiudad("Ocaña");
            p9.setDireccion("Calle 18 # 9-67, Barrio Cristo Rey");
            p9.setFechaNacimiento(java.time.LocalDate.of(1992, 4, 17));
            p9.setEstadoCivil("Casada");
            p9.setOcupacion("Contadora");
            p9.setContactoEmergencia("Jorge Blanco");
            p9.setTelefonoEmergencia("3016789012");
            guardarUsuario(p9);

            // Paciente 10
            Paciente p10 = new Paciente("P010", "Andrés Felipe", "Castro Mendoza",
                    "andres.castro@email.com", "paciente123", "3017890123");
            p10.setGenero("Masculino");
            p10.setCiudad("Ocaña");
            p10.setDireccion("Carrera 15 # 25-34, Barrio La Florida");
            p10.setFechaNacimiento(java.time.LocalDate.of(2000, 8, 9));
            p10.setEstadoCivil("Soltero");
            p10.setOcupacion("Estudiante Universitario");
            p10.setContactoEmergencia("Gloria Mendoza");
            p10.setTelefonoEmergencia("3019012345");
            guardarUsuario(p10);

            // Paciente 11
            Paciente p11 = new Paciente("P011", "Martha Cecilia", "Vargas Suárez",
                    "martha.vargas@email.com", "paciente123", "3008901234");
            p11.setGenero("Femenino");
            p11.setCiudad("Ocaña");
            p11.setDireccion("Avenida Francisco Fernández de Contreras # 12-89");
            p11.setFechaNacimiento(java.time.LocalDate.of(1965, 1, 20));
            p11.setEstadoCivil("Viuda");
            p11.setOcupacion("Pensionada");
            p11.setContactoEmergencia("Felipe Vargas");
            p11.setTelefonoEmergencia("3015432198");
            guardarUsuario(p11);

            // Paciente 12
            Paciente p12 = new Paciente("P012", "Luis Eduardo", "Quintero Parra",
                    "luis.quintero@email.com", "paciente123", "3016543219");
            p12.setGenero("Masculino");
            p12.setCiudad("Ocaña");
            p12.setDireccion("Calle 12 # 7-45, Barrio San Francisco");
            p12.setFechaNacimiento(java.time.LocalDate.of(1979, 10, 11));
            p12.setEstadoCivil("Casado");
            p12.setOcupacion("Abogado");
            p12.setContactoEmergencia("Andrea Parra");
            p12.setTelefonoEmergencia("3017654321");
            guardarUsuario(p12);

            // Paciente 13
            Paciente p13 = new Paciente("P013", "Gabriela Andrea", "Ríos Téllez",
                    "gabriela.rios@email.com", "paciente123", "3019876501");
            p13.setGenero("Femenino");
            p13.setCiudad("Ocaña");
            p13.setDireccion("Transversal 9 # 19-23, Barrio Olaya Herrera");
            p13.setFechaNacimiento(java.time.LocalDate.of(1996, 3, 28));
            p13.setEstadoCivil("Soltera");
            p13.setOcupacion("Psicóloga");
            p13.setContactoEmergencia("Hernán Ríos");
            p13.setTelefonoEmergencia("3008765123");
            guardarUsuario(p13);

            // Paciente 14
            Paciente p14 = new Paciente("P014", "Oscar Hernando", "Silva Hernández",
                    "oscar.silva@email.com", "paciente123", "3015678234");
            p14.setGenero("Masculino");
            p14.setCiudad("Ocaña");
            p14.setDireccion("Diagonal 13 # 21-56, Urbanización Los Alpes");
            p14.setFechaNacimiento(java.time.LocalDate.of(1971, 7, 3));
            p14.setEstadoCivil("Divorciado");
            p14.setOcupacion("Arquitecto");
            p14.setContactoEmergencia("Julio Silva");
            p14.setTelefonoEmergencia("3012345098");
            guardarUsuario(p14);

            // Paciente 15
            Paciente p15 = new Paciente("P015", "Paola Andrea", "Jaimes Guerrero",
                    "paola.jaimes@email.com", "paciente123", "3018907654");
            p15.setGenero("Femenino");
            p15.setCiudad("Ocaña");
            p15.setDireccion("Calle 16 # 11-78, Barrio La Libertad");
            p15.setFechaNacimiento(java.time.LocalDate.of(1987, 9, 16));
            p15.setEstadoCivil("Casada");
            p15.setOcupacion("Odontóloga");
            p15.setContactoEmergencia("Ricardo Jaimes");
            p15.setTelefonoEmergencia("3017890456");
            guardarUsuario(p15);

            // Paciente 16
            Paciente p16 = new Paciente("P016", "Fernando José", "Gómez Navarro",
                    "fernando.gomez@email.com", "paciente123", "3009012345");
            p16.setGenero("Masculino");
            p16.setCiudad("Ocaña");
            p16.setDireccion("Carrera 6 # 13-90, Centro");
            p16.setFechaNacimiento(java.time.LocalDate.of(1993, 5, 7));
            p16.setEstadoCivil("Soltero");
            p16.setOcupacion("Periodista");
            p16.setContactoEmergencia("Elena Navarro");
            p16.setTelefonoEmergencia("3016789234");
            guardarUsuario(p16);

            // Paciente 17
            Paciente p17 = new Paciente("P017", "Yolanda Patricia", "Arévalo Méndez",
                    "yolanda.arevalo@email.com", "paciente123", "3014321987");
            p17.setGenero("Femenino");
            p17.setCiudad("Ocaña");
            p17.setDireccion("Avenida 4 # 17-45, Barrio San Martín");
            p17.setFechaNacimiento(java.time.LocalDate.of(1980, 11, 23));
            p17.setEstadoCivil("Unión Libre");
            p17.setOcupacion("Veterinaria");
            p17.setContactoEmergencia("Gustavo Méndez");
            p17.setTelefonoEmergencia("3019876234");
            guardarUsuario(p17);

            // Paciente 18
            Paciente p18 = new Paciente("P018", "Ricardo Andrés", "Balaguera Cruz",
                    "ricardo.balaguera@email.com", "paciente123", "3017654098");
            p18.setGenero("Masculino");
            p18.setCiudad("Ocaña");
            p18.setDireccion("Calle 9 # 14-12, Barrio La Victoria");
            p18.setFechaNacimiento(java.time.LocalDate.of(1985, 2, 28));
            p18.setEstadoCivil("Casado");
            p18.setOcupacion("Electricista");
            p18.setContactoEmergencia("Carolina Cruz");
            p18.setTelefonoEmergencia("3015432876");
            guardarUsuario(p18);

            // Paciente 19
            Paciente p19 = new Paciente("P019", "Beatriz Elena", "Castañeda Mora",
                    "beatriz.castaneda@email.com", "paciente123", "3008765098");
            p19.setGenero("Femenino");
            p19.setCiudad("Ocaña");
            p19.setDireccion("Transversal 5 # 20-34, Barrio Siete de Agosto");
            p19.setFechaNacimiento(java.time.LocalDate.of(1977, 6, 12));
            p19.setEstadoCivil("Casada");
            p19.setOcupacion("Administradora");
            p19.setContactoEmergencia("Álvaro Castañeda");
            p19.setTelefonoEmergencia("3012109876");
            guardarUsuario(p19);

            // Paciente 20
            Paciente p20 = new Paciente("P020", "Javier Enrique", "Portilla Sarmiento",
                    "javier.portilla@email.com", "paciente123", "3016098765");
            p20.setGenero("Masculino");
            p20.setCiudad("Ocaña");
            p20.setDireccion("Diagonal 11 # 8-23, Barrio Popular");
            p20.setFechaNacimiento(java.time.LocalDate.of(1991, 12, 31));
            p20.setEstadoCivil("Soltero");
            p20.setOcupacion("Chef");
            p20.setContactoEmergencia("Lucía Sarmiento");
            p20.setTelefonoEmergencia("3019087654");
            guardarUsuario(p20);
        }
    }
}
