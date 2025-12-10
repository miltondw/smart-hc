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
import java.util.Arrays;

public class HistoriaClinicaController {

    @FXML
    private Label lblPacienteInfo;

    // Datos Personales
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtCiudad;
    @FXML
    private ComboBox<String> cmbTipoSangre;
    @FXML
    private ComboBox<String> cmbEstadoCivil;
    @FXML
    private TextField txtOcupacion;
    @FXML
    private TextField txtContactoEmergencia;
    @FXML
    private TextField txtTelefonoEmergencia;

    // Antecedentes
    @FXML
    private TextArea txtEnfermedadesPersonales;
    @FXML
    private TextArea txtEnfermedadesFamiliares;
    @FXML
    private TextArea txtCirugias;
    @FXML
    private TextArea txtAlergias;
    @FXML
    private TextArea txtMedicamentos;

    // Motivo de Consulta
    @FXML
    private TextArea txtMotivoConsulta;
    @FXML
    private TextArea txtEnfermedadActual;

    // Signos Vitales
    @FXML
    private TextField txtTemperatura;
    @FXML
    private TextField txtPresionSistolica;
    @FXML
    private TextField txtPresionDiastolica;
    @FXML
    private TextField txtFrecuenciaCardiaca;
    @FXML
    private TextField txtSaturacionOxigeno;
    @FXML
    private TextField txtPeso;
    @FXML
    private TextField txtAltura;
    @FXML
    private Label lblIMC;

    private AuthService authService;
    private HistoriaClinicaService historiaService;
    private Paciente paciente;
    private HistoriaClinica historiaClinica;
    private boolean esMedico;

    public HistoriaClinicaController() {
        this.authService = AuthService.getInstance();
        this.historiaService = HistoriaClinicaService.getInstance();
    }

    @FXML
    public void initialize() {
        configurarComboBoxes();
        configurarCalculoIMC();
    }

    private void configurarComboBoxes() {
        cmbTipoSangre.getItems().addAll("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-");
        cmbEstadoCivil.getItems().addAll("Soltero", "Casado", "Divorciado", "Viudo", "Unión Libre");
    }

    private void configurarCalculoIMC() {
        txtPeso.textProperty().addListener((obs, old, newVal) -> calcularIMC());
        txtAltura.textProperty().addListener((obs, old, newVal) -> calcularIMC());
    }

    private void calcularIMC() {
        try {
            double peso = Double.parseDouble(txtPeso.getText());
            double altura = Double.parseDouble(txtAltura.getText());

            if (peso > 0 && altura > 0) {
                double imc = peso / (altura * altura);
                lblIMC.setText(String.format("%.1f", imc));

                // Cambiar color según el IMC
                if (imc < 18.5) {
                    lblIMC.setStyle("-fx-text-fill: #2196F3;");
                } else if (imc < 25) {
                    lblIMC.setStyle("-fx-text-fill: #4CAF50;");
                } else if (imc < 30) {
                    lblIMC.setStyle("-fx-text-fill: #FF9800;");
                } else {
                    lblIMC.setStyle("-fx-text-fill: #F44336;");
                }
            }
        } catch (NumberFormatException e) {
            lblIMC.setText("--");
        }
    }

    public void cargarPaciente(Paciente paciente) {
        this.paciente = paciente;
        this.esMedico = authService.getUsuarioActual() instanceof Medico;

        lblPacienteInfo.setText("Paciente: " + paciente.getNombreCompleto() +
                " - HC: " + paciente.getNumeroHistoriaClinica());

        historiaClinica = historiaService.obtenerHistoriaClinica(paciente.getId());
        cargarDatos();
    }

    private void cargarDatos() {
        // Cargar datos personales
        txtDireccion.setText(paciente.getDireccion() != null ? paciente.getDireccion() : "");
        txtCiudad.setText(paciente.getCiudad() != null ? paciente.getCiudad() : "");
        cmbTipoSangre.setValue(paciente.getTipoSangre());
        cmbEstadoCivil.setValue(paciente.getEstadoCivil());
        txtOcupacion.setText(paciente.getOcupacion() != null ? paciente.getOcupacion() : "");
        txtContactoEmergencia.setText(paciente.getContactoEmergencia() != null ? paciente.getContactoEmergencia() : "");
        txtTelefonoEmergencia.setText(paciente.getTelefonoEmergencia() != null ? paciente.getTelefonoEmergencia() : "");

        // Cargar antecedentes
        Antecedentes ant = historiaClinica.getAntecedentes();
        if (ant != null) {
            txtEnfermedadesPersonales.setText(String.join(", ", ant.getEnfermedadesPersonales()));
            txtEnfermedadesFamiliares.setText(String.join(", ", ant.getEnfermedadesFamiliares()));
            txtCirugias.setText(String.join(", ", ant.getCirugias()));
            txtAlergias.setText(String.join(", ", ant.getAlergias()));
            txtMedicamentos.setText(String.join(", ", ant.getMedicamentosActuales()));
        }

        // Cargar motivo de consulta
        txtMotivoConsulta
                .setText(historiaClinica.getMotivoConsulta() != null ? historiaClinica.getMotivoConsulta() : "");
        txtEnfermedadActual
                .setText(historiaClinica.getEnfermedadActual() != null ? historiaClinica.getEnfermedadActual() : "");

        // Cargar signos vitales (último registro)
        if (!historiaClinica.getSignosVitales().isEmpty()) {
            SignosVitales ultimos = historiaClinica.getSignosVitales()
                    .get(historiaClinica.getSignosVitales().size() - 1);
            txtTemperatura.setText(String.valueOf(ultimos.getTemperatura()));
            txtPresionSistolica.setText(String.valueOf(ultimos.getPresionSistolica()));
            txtPresionDiastolica.setText(String.valueOf(ultimos.getPresionDiastolica()));
            txtFrecuenciaCardiaca.setText(String.valueOf(ultimos.getFrecuenciaCardiaca()));
            txtSaturacionOxigeno.setText(String.valueOf(ultimos.getSaturacionOxigeno()));
            txtPeso.setText(String.valueOf(ultimos.getPeso()));
            txtAltura.setText(String.valueOf(ultimos.getAltura()));
        }
    }

    @FXML
    private void guardarHistoria() {
        if (!validarCampos()) {
            return;
        }

        try {
            // Guardar datos personales
            paciente.setDireccion(txtDireccion.getText());
            paciente.setCiudad(txtCiudad.getText());
            paciente.setTipoSangre(cmbTipoSangre.getValue());
            paciente.setEstadoCivil(cmbEstadoCivil.getValue());
            paciente.setOcupacion(txtOcupacion.getText());
            paciente.setContactoEmergencia(txtContactoEmergencia.getText());
            paciente.setTelefonoEmergencia(txtTelefonoEmergencia.getText());

            // Guardar antecedentes
            Antecedentes antecedentes = new Antecedentes();
            if (!txtEnfermedadesPersonales.getText().isEmpty()) {
                antecedentes.setEnfermedadesPersonales(Arrays.asList(txtEnfermedadesPersonales.getText().split(",")));
            }
            if (!txtEnfermedadesFamiliares.getText().isEmpty()) {
                antecedentes.setEnfermedadesFamiliares(Arrays.asList(txtEnfermedadesFamiliares.getText().split(",")));
            }
            if (!txtCirugias.getText().isEmpty()) {
                antecedentes.setCirugias(Arrays.asList(txtCirugias.getText().split(",")));
            }
            if (!txtAlergias.getText().isEmpty()) {
                antecedentes.setAlergias(Arrays.asList(txtAlergias.getText().split(",")));
            }
            if (!txtMedicamentos.getText().isEmpty()) {
                antecedentes.setMedicamentosActuales(Arrays.asList(txtMedicamentos.getText().split(",")));
            }
            historiaClinica.setAntecedentes(antecedentes);

            // Guardar motivo de consulta
            historiaClinica.setMotivoConsulta(txtMotivoConsulta.getText());
            historiaClinica.setEnfermedadActual(txtEnfermedadActual.getText());

            // Guardar signos vitales
            if (!txtTemperatura.getText().isEmpty()) {
                SignosVitales signos = new SignosVitales();
                signos.setPacienteId(paciente.getId());
                signos.setTemperatura(Double.parseDouble(txtTemperatura.getText()));
                signos.setPresionSistolica(Integer.parseInt(txtPresionSistolica.getText()));
                signos.setPresionDiastolica(Integer.parseInt(txtPresionDiastolica.getText()));
                signos.setFrecuenciaCardiaca(Integer.parseInt(txtFrecuenciaCardiaca.getText()));
                signos.setSaturacionOxigeno(Double.parseDouble(txtSaturacionOxigeno.getText()));
                signos.setPeso(Double.parseDouble(txtPeso.getText()));
                signos.setAltura(Double.parseDouble(txtAltura.getText()));

                historiaService.agregarSignosVitales(paciente.getId(), signos);
            }

            if (historiaService.actualizarHistoriaClinica(historiaClinica)) {
                AlertUtils.mostrarExito("Éxito", AppConfig.MSG_GUARDADO_EXITOSO);
                volver();
            } else {
                AlertUtils.mostrarError("Error", AppConfig.MSG_ERROR_GUARDADO);
            }

        } catch (NumberFormatException e) {
            AlertUtils.mostrarError("Error", "Por favor verifique que los valores numéricos sean correctos");
        }
    }

    private boolean validarCampos() {
        if (txtDireccion.getText().isEmpty() || txtCiudad.getText().isEmpty()) {
            AlertUtils.mostrarAdvertencia("Campos Incompletos",
                    "Por favor complete al menos la dirección y ciudad");
            return false;
        }
        return true;
    }

    @FXML
    private void volver() {
        try {
            String fxmlFile = esMedico ? "dashboardMedico.fxml" : "dashboardPaciente.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/" + fxmlFile));
            Parent root = loader.load();

            Scene scene = new Scene(root, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/resources/css/styles.css").toExternalForm());

            Stage stage = (Stage) lblPacienteInfo.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            AlertUtils.mostrarError("Error", "No se pudo volver al dashboard: " + e.getMessage());
        }
    }
}
