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
import java.util.Optional;

public class SeguimientosCronicosController {

    @FXML
    private Label lblMedicoNombre;
    @FXML
    private Label lblTotalCronicos;
    @FXML
    private Label lblCitasProximas;
    @FXML
    private TableView<SeguimientoViewModel> tablaSeguimientos;
    @FXML
    private TableColumn<SeguimientoViewModel, String> colPaciente;
    @FXML
    private TableColumn<SeguimientoViewModel, String> colDiagnostico;
    @FXML
    private TableColumn<SeguimientoViewModel, String> colTratamiento;
    @FXML
    private TableColumn<SeguimientoViewModel, String> colProximaCita;
    @FXML
    private TableColumn<SeguimientoViewModel, String> colEstado;
    @FXML
    private TableColumn<SeguimientoViewModel, Void> colAcciones;

    private AuthService authService;
    private SeguimientoService seguimientoService;
    private DataStorage storage;
    private Medico medicoActual;

    public SeguimientosCronicosController() {
        this.authService = AuthService.getInstance();
        this.seguimientoService = SeguimientoService.getInstance();
        this.storage = DataStorage.getInstance();
    }

    @FXML
    public void initialize() {
        medicoActual = authService.getMedicoActual();

        if (medicoActual != null) {
            lblMedicoNombre.setText("Dr. " + medicoActual.getNombreCompleto());
            configurarTabla();
            cargarSeguimientos();
            actualizarEstadisticas();
        }
    }

    private void configurarTabla() {
        colPaciente.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombrePaciente()));

        colDiagnostico.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDiagnostico()));

        colTratamiento.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTratamiento()));

        colProximaCita.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProximaCita()));

        colEstado.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEstado()));

        // Agregar botones de acción
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnVer = new Button("Ver Historia");

            {
                btnVer.setOnAction(event -> {
                    SeguimientoViewModel seguimiento = getTableView().getItems().get(getIndex());
                    verHistoriaClinica(seguimiento.getPacienteId());
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

    private void cargarSeguimientos() {
        List<Seguimiento> seguimientos = seguimientoService.obtenerSeguimientosCronicos();
        List<SeguimientoViewModel> viewModels = seguimientos.stream()
                .map(this::crearViewModel)
                .filter(vm -> vm != null)
                .toList();

        tablaSeguimientos.setItems(FXCollections.observableArrayList(viewModels));
    }

    private SeguimientoViewModel crearViewModel(Seguimiento seguimiento) {
        Optional<Usuario> usuario = storage.buscarUsuarioPorId(seguimiento.getPacienteId());
        if (usuario.isEmpty() || !(usuario.get() instanceof Paciente)) {
            return null;
        }

        Paciente paciente = (Paciente) usuario.get();
        return new SeguimientoViewModel(
                paciente.getId(),
                paciente.getNombreCompleto(),
                seguimiento.getDiagnostico() != null ? seguimiento.getDiagnostico() : "Sin diagnóstico",
                seguimiento.getTratamiento() != null ? seguimiento.getTratamiento() : "Sin tratamiento",
                seguimiento.getProximaCita() != null ? seguimiento.getProximaCita() : "No programada",
                seguimiento.getEstadoSeguimiento() != null ? seguimiento.getEstadoSeguimiento().toUpperCase()
                        : "ACTIVO");
    }

    private void actualizarEstadisticas() {
        List<Seguimiento> seguimientos = seguimientoService.obtenerSeguimientosCronicos();
        lblTotalCronicos.setText(String.valueOf(seguimientos.size()));

        long citasProximas = seguimientos.stream()
                .filter(s -> s.getProximaCita() != null && !s.getProximaCita().isEmpty())
                .count();
        lblCitasProximas.setText(String.valueOf(citasProximas));
    }

    private void verHistoriaClinica(String pacienteId) {
        try {
            Optional<Usuario> usuario = storage.buscarUsuarioPorId(pacienteId);
            if (usuario.isEmpty() || !(usuario.get() instanceof Paciente)) {
                AlertUtils.mostrarError("Error", "No se encontró el paciente");
                return;
            }

            Paciente paciente = (Paciente) usuario.get();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/historiaClinica.fxml"));
            Parent root = loader.load();

            HistoriaClinicaController controller = loader.getController();
            controller.cargarPaciente(paciente);

            Scene scene = new Scene(root, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/resources/css/styles.css").toExternalForm());

            Stage stage = (Stage) tablaSeguimientos.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            AlertUtils.mostrarError("Error", "No se pudo cargar la historia clínica: " + e.getMessage());
        }
    }

    @FXML
    private void volver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/dashboardMedico.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/resources/css/styles.css").toExternalForm());

            Stage stage = (Stage) lblMedicoNombre.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            AlertUtils.mostrarError("Error", "No se pudo volver al dashboard");
        }
    }

    // Clase interna para el ViewModel
    public static class SeguimientoViewModel {
        private final String pacienteId;
        private final String nombrePaciente;
        private final String diagnostico;
        private final String tratamiento;
        private final String proximaCita;
        private final String estado;

        public SeguimientoViewModel(String pacienteId, String nombrePaciente, String diagnostico,
                String tratamiento, String proximaCita, String estado) {
            this.pacienteId = pacienteId;
            this.nombrePaciente = nombrePaciente;
            this.diagnostico = diagnostico;
            this.tratamiento = tratamiento;
            this.proximaCita = proximaCita;
            this.estado = estado;
        }

        public String getPacienteId() {
            return pacienteId;
        }

        public String getNombrePaciente() {
            return nombrePaciente;
        }

        public String getDiagnostico() {
            return diagnostico;
        }

        public String getTratamiento() {
            return tratamiento;
        }

        public String getProximaCita() {
            return proximaCita;
        }

        public String getEstado() {
            return estado;
        }
    }
}
