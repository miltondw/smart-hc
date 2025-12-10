package controllers;

import domain.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.*;
import storage.DataStorage;
import utils.AlertUtils;
import config.AppConfig;

import java.io.IOException;
import java.util.List;

public class DashboardMedicoController {

    @FXML
    private Label lblMedicoNombre;
    @FXML
    private Label lblTotalPacientes;
    @FXML
    private Label lblHistoriasCompletas;
    @FXML
    private Label lblSeguimientosActivos;
    @FXML
    private TableView<Paciente> tablaPacientes;
    @FXML
    private TableColumn<Paciente, String> colNombre;
    @FXML
    private TableColumn<Paciente, String> colHistoriaClinica;
    @FXML
    private TableColumn<Paciente, Integer> colEdad;
    @FXML
    private TableColumn<Paciente, String> colCiudad;
    // @FXML
    // private TableColumn<Paciente, Double> colPorcentaje;
    @FXML
    private TableColumn<Paciente, Void> colAcciones;

    private AuthService authService;
    private HistoriaClinicaService historiaService;
    private SeguimientoService seguimientoService;
    private DataStorage storage;
    private Medico medicoActual;

    public DashboardMedicoController() {
        this.authService = AuthService.getInstance();
        this.historiaService = HistoriaClinicaService.getInstance();
        this.seguimientoService = SeguimientoService.getInstance();
        this.storage = DataStorage.getInstance();
    }

    @FXML
    public void initialize() {
        medicoActual = authService.getMedicoActual();

        if (medicoActual != null) {
            lblMedicoNombre.setText("Dr. " + medicoActual.getNombreCompleto());
            cargarEstadisticas();
            configurarTabla();
            cargarPacientes();
        }
    }

    private void cargarEstadisticas() {
        List<Paciente> pacientes = storage.obtenerTodosLosPacientes();
        List<HistoriaClinica> historias = historiaService.obtenerHistoriasCompletadas();
        int seguimientos = seguimientoService.contarSeguimientosActivosPorMedico(medicoActual.getId());

        lblTotalPacientes.setText(String.valueOf(pacientes.size()));
        lblHistoriasCompletas.setText(String.valueOf(historias.size()));
        lblSeguimientosActivos.setText(String.valueOf(seguimientos));
    }

    private void configurarTabla() {
        colNombre.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombreCompleto()));

        colHistoriaClinica.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getNumeroHistoriaClinica()));

        colEdad.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getEdad()));

        colCiudad.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCiudad()));

        // colPorcentaje.setCellValueFactory(cellData -> {
        // double porcentaje =
        // historiaService.obtenerPorcentajeCompletado(cellData.getValue().getId());
        // return new javafx.beans.property.SimpleObjectProperty<>(porcentaje);
        // });

        // Agregar botón de acción
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnVer = new Button("Ver Historia");

            {
                btnVer.setOnAction(event -> {
                    Paciente paciente = getTableView().getItems().get(getIndex());
                    verHistoriaClinica(paciente);
                });
                btnVer.getStyleClass().add("btn-table-action");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnVer);
            }
        });
    }

    private void cargarPacientes() {
        List<Paciente> pacientes = storage.obtenerTodosLosPacientes();
        tablaPacientes.setItems(FXCollections.observableArrayList(pacientes));
    }

    @FXML
    private void verPacientes() {
        cargarPacientes();
    }

    @FXML
    private void verHistorias() {
        List<HistoriaClinica> historias = historiaService.obtenerTodasLasHistorias();
        AlertUtils.mostrarInfo("Historias Clínicas",
                "Total de historias clínicas: " + historias.size());
    }

    @FXML
    private void verSeguimientos() {
        List<Seguimiento> seguimientos = seguimientoService.obtenerSeguimientosCronicos();
        AlertUtils.mostrarInfo("Seguimientos Crónicos",
                "Pacientes con seguimiento activo: " + seguimientos.size());
    }

    private void verHistoriaClinica(Paciente paciente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/historiaClinica.fxml"));
            Parent root = loader.load();

            HistoriaClinicaController controller = loader.getController();
            controller.cargarPaciente(paciente);

            Scene scene = new Scene(root, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/resources/css/styles.css").toExternalForm());

            Stage stage = (Stage) tablaPacientes.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            AlertUtils.mostrarError("Error", "No se pudo cargar la historia clínica: " + e.getMessage());
        }
    }

    @FXML
    private void cerrarSesion() {
        if (AlertUtils.mostrarConfirmacion("Cerrar Sesión", "¿Está seguro que desea cerrar sesión?")) {
            authService.logout();
            volverALogin();
        }
    }

    private void volverALogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, AppConfig.LOGIN_WIDTH, AppConfig.LOGIN_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/resources/css/styles.css").toExternalForm());

            Stage stage = (Stage) lblMedicoNombre.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(AppConfig.APP_NAME + " - Login");
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtils.mostrarError("Error", "No se pudo volver al login: " + e.getMessage());
        }
    }
}
