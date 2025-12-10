package controllers;

import domain.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.AuthService;
import utils.AlertUtils;
import config.AppConfig;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button btnMedico;
    @FXML
    private Button btnPaciente;

    private AuthService authService;

    public LoginController() {
        this.authService = AuthService.getInstance();
    }

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

    @FXML
    private void loginMedico() {
        login(Usuario.TipoUsuario.MEDICO);
    }

    @FXML
    private void loginPaciente() {
        login(Usuario.TipoUsuario.PACIENTE);
    }

    private void login(Usuario.TipoUsuario tipo) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            mostrarError(AppConfig.MSG_CAMPOS_VACIOS);
            return;
        }

        if (authService.login(email, password, tipo)) {
            errorLabel.setVisible(false);

            try {
                String fxmlFile = tipo == Usuario.TipoUsuario.MEDICO ? "dashboardMedico.fxml"
                        : "dashboardPaciente.fxml";

                cambiarEscena(fxmlFile);
            } catch (IOException e) {
                AlertUtils.mostrarError("Error", "No se pudo cargar el dashboard: " + e.getMessage());
            }
        } else {
            mostrarError(AppConfig.MSG_LOGIN_FALLIDO);
        }
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
    }

    @FXML
    private void irARegistro() {
        AlertUtils.mostrarInfo("Información", "Funcionalidad de registro en desarrollo");
    }

    private void cambiarEscena(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/" + fxmlFile));
        Parent root = loader.load();

        Scene scene = new Scene(root, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/resources/css/styles.css").toExternalForm());

        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(AppConfig.APP_NAME + " - Dashboard");
        stage.centerOnScreen();
    }
}
