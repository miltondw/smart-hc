package controllers;

import domain.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.*;
import utils.*;
import config.AppConfig;

import java.io.IOException;

public class DashboardPacienteController {

    @FXML
    private Label lblPacienteNombre;
    @FXML
    private Label lblPorcentaje;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label lblNumeroHistoria;
    @FXML
    private Label lblEdad;
    @FXML
    private Label lblUltimaActualizacion;
    @FXML
    private Label lblAlertas;

    private AuthService authService;
    private HistoriaClinicaService historiaService;
    private Paciente pacienteActual;
    private HistoriaClinica historiaClinica;

    public DashboardPacienteController() {
        this.authService = AuthService.getInstance();
        this.historiaService = HistoriaClinicaService.getInstance();
    }

    @FXML
    public void initialize() {
        pacienteActual = authService.getPacienteActual();

        if (pacienteActual != null) {
            cargarInformacion();
        }
    }

    private void cargarInformacion() {
        lblPacienteNombre.setText(pacienteActual.getNombreCompleto());
        lblNumeroHistoria.setText(pacienteActual.getNumeroHistoriaClinica());
        lblEdad.setText(pacienteActual.getEdad() + " años");

        historiaClinica = historiaService.obtenerHistoriaClinica(pacienteActual.getId());

        // Calcular el porcentaje actualizado
        double porcentaje = historiaService.obtenerPorcentajeCompletado(pacienteActual.getId());
        lblPorcentaje.setText(String.format("%.0f%%", porcentaje));
        progressBar.setProgress(porcentaje / 100.0);

        String fecha = DateUtils.formatearFechaHora(historiaClinica.getFechaActualizacion());
        lblUltimaActualizacion.setText(fecha);

        actualizarAlertas(porcentaje);
    }

    private void actualizarAlertas(double porcentaje) {
        if (porcentaje < 50) {
            lblAlertas.setText("⚠ Tu historia clínica está incompleta. Complétala para una mejor atención.");
            lblAlertas.setStyle("-fx-text-fill: #FF9800;");
        } else if (porcentaje < 80) {
            lblAlertas.setText("ℹ Estás cerca de completar tu historia clínica. ¡Continúa!");
            lblAlertas.setStyle("-fx-text-fill: #2196F3;");
        } else {
            lblAlertas.setText("✓ Tu historia clínica está completa. ¡Excelente!");
            lblAlertas.setStyle("-fx-text-fill: #4CAF50;");
        }
    }

    @FXML
    private void completarDatosPersonales() {
        abrirHistoriaClinica("datos");
    }

    @FXML
    private void registrarAntecedentes() {
        abrirHistoriaClinica("antecedentes");
    }

    @FXML
    private void registrarMotivoConsulta() {
        abrirHistoriaClinica("motivo");
    }

    @FXML
    private void registrarSignosVitales() {
        abrirHistoriaClinica("signos");
    }

    private void abrirHistoriaClinica(String seccion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/historiaClinica.fxml"));
            Parent root = loader.load();

            HistoriaClinicaController controller = loader.getController();
            controller.cargarPaciente(pacienteActual, seccion);

            Scene scene = new Scene(root, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/resources/css/styles.css").toExternalForm());

            Stage stage = (Stage) lblPacienteNombre.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            AlertUtils.mostrarError("Error", "No se pudo abrir la historia clínica: " + e.getMessage());
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

            Stage stage = (Stage) lblPacienteNombre.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(AppConfig.APP_NAME + " - Login");
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtils.mostrarError("Error", "No se pudo volver al login: " + e.getMessage());
        }
    }
}
