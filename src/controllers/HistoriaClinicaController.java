package controllers;

import domain.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.*;
import storage.DataStorage;
import utils.*;
import config.AppConfig;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class HistoriaClinicaController {

    @FXML
    private Label lblPacienteInfo;

    // Secciones del formulario
    @FXML
    private VBox seccionDatosPersonales;
    @FXML
    private VBox seccionAntecedentes;
    @FXML
    private VBox seccionMotivo;
    @FXML
    private VBox seccionSignos;
    @FXML
    private VBox seccionDiagnostico;

    // Datos Personales
    @FXML
    private DatePicker dateFechaNacimiento;
    @FXML
    private Label lblEdadCalculada;
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

    // Diagnóstico y Nivel de Riesgo
    @FXML
    private TextArea txtDiagnostico;
    @FXML
    private ComboBox<String> cmbNivelRiesgo;

    // Seguimiento Crónico
    @FXML
    private VBox seccionSeguimiento;
    @FXML
    private Label lblEstadoSeguimiento;
    @FXML
    private Button btnCrearSeguimiento;
    @FXML
    private Button btnVerSeguimiento;
    @FXML
    private VBox panelNuevoSeguimiento;
    @FXML
    private TextArea txtTratamiento;
    @FXML
    private TextField txtProximaCita;
    @FXML
    private TextArea txtNotasSeguimiento;

    private AuthService authService;
    private HistoriaClinicaService historiaService;
    private DataStorage storage;
    private Paciente paciente;
    private HistoriaClinica historiaClinica;
    private boolean esMedico;

    public HistoriaClinicaController() {
        this.authService = AuthService.getInstance();
        this.historiaService = HistoriaClinicaService.getInstance();
        this.storage = DataStorage.getInstance();
    }

    @FXML
    public void initialize() {
        configurarComboBoxes();
        configurarCalculoIMC();
        configurarCalculoEdad();
    }

    private void configurarComboBoxes() {
        cmbTipoSangre.getItems().addAll("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-");
        cmbEstadoCivil.getItems().addAll("Soltero", "Casado", "Divorciado", "Viudo", "Unión Libre");
        cmbNivelRiesgo.getItems().addAll("BAJO", "MEDIO", "ALTO");
    }

    private void configurarCalculoEdad() {
        dateFechaNacimiento.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                int edad = LocalDate.now().getYear() - newVal.getYear();
                lblEdadCalculada.setText(edad + " años");
            } else {
                lblEdadCalculada.setText("-- años");
            }
        });
    }

    private void configurarCalculoIMC() {
        txtPeso.textProperty().addListener((obs, old, newVal) -> calcularIMC());
        txtAltura.textProperty().addListener((obs, old, newVal) -> calcularIMC());
    }

    private void calcularIMC() {
        try {
            double peso = Double.parseDouble(txtPeso.getText());
            double altura = Double.parseDouble(txtAltura.getText());

            // Validar que la altura esté en metros (rango razonable: 0.5m a 3m)
            if (altura > 3.0) {
                lblIMC.setText("ERROR");
                lblIMC.setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold;");
                AlertUtils.mostrarAdvertencia("Altura Incorrecta",
                        "La altura debe ingresarse en METROS.\n\n" +
                                "Ejemplo: Si mide 170 cm, ingrese 1.70\n" +
                                "Su valor actual (" + String.format("%.2f", altura) + ") parece estar en centímetros.");
                return;
            }

            if (altura < 0.5) {
                lblIMC.setText("ERROR");
                lblIMC.setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold;");
                AlertUtils.mostrarAdvertencia("Altura Incorrecta",
                        "La altura debe ser mayor a 0.5 metros.\n\n" +
                                "Ingrese su altura en metros (ejemplo: 1.70)");
                return;
            }

            if (peso > 0 && altura > 0) {
                double imc = peso / (altura * altura);
                lblIMC.setText(String.format("%.1f", imc));

                // Cambiar color según el IMC
                if (imc < 18.5) {
                    lblIMC.setStyle("-fx-text-fill: #2196F3; -fx-font-weight: bold;"); // Bajo peso
                } else if (imc < 25) {
                    lblIMC.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;"); // Normal
                } else if (imc < 30) {
                    lblIMC.setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold;"); // Sobrepeso
                } else {
                    lblIMC.setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold;"); // Obesidad
                }
            }
        } catch (NumberFormatException e) {
            lblIMC.setText("--");
            lblIMC.setStyle("");
        }
    }

    public void cargarPaciente(Paciente paciente) {
        cargarPaciente(paciente, null);
    }

    public void cargarPaciente(Paciente paciente, String seccionMostrar) {
        this.paciente = paciente;
        this.esMedico = authService.getUsuarioActual() instanceof Medico;

        lblPacienteInfo.setText("Paciente: " + paciente.getNombreCompleto() +
                " - HC: " + paciente.getNumeroHistoriaClinica());

        historiaClinica = historiaService.obtenerHistoriaClinica(paciente.getId());
        cargarDatos();

        // Mostrar solo la sección específica si se indica
        if (seccionMostrar != null && !esMedico) {
            mostrarSoloSeccion(seccionMostrar);
        }
    }

    private void mostrarSoloSeccion(String seccion) {
        // Ocultar todas las secciones primero
        seccionDatosPersonales.setVisible(false);
        seccionDatosPersonales.setManaged(false);
        seccionAntecedentes.setVisible(false);
        seccionAntecedentes.setManaged(false);
        seccionMotivo.setVisible(false);
        seccionMotivo.setManaged(false);
        seccionSignos.setVisible(false);
        seccionSignos.setManaged(false);
        seccionDiagnostico.setVisible(false);
        seccionDiagnostico.setManaged(false);

        // Mostrar solo la sección solicitada
        switch (seccion) {
            case "datos":
                seccionDatosPersonales.setVisible(true);
                seccionDatosPersonales.setManaged(true);
                break;
            case "antecedentes":
                seccionAntecedentes.setVisible(true);
                seccionAntecedentes.setManaged(true);
                break;
            case "motivo":
                seccionMotivo.setVisible(true);
                seccionMotivo.setManaged(true);
                break;
            case "signos":
                seccionSignos.setVisible(true);
                seccionSignos.setManaged(true);
                break;
            case "diagnostico":
                seccionDiagnostico.setVisible(true);
                seccionDiagnostico.setManaged(true);
                break;
            default:
                // Mostrar todas si no se reconoce la sección
                mostrarTodasLasSecciones();
                break;
        }
    }

    private void mostrarTodasLasSecciones() {
        seccionDatosPersonales.setVisible(true);
        seccionDatosPersonales.setManaged(true);
        seccionAntecedentes.setVisible(true);
        seccionAntecedentes.setManaged(true);
        seccionMotivo.setVisible(true);
        seccionMotivo.setManaged(true);
        seccionSignos.setVisible(true);
        seccionSignos.setManaged(true);
        seccionDiagnostico.setVisible(true);
        seccionDiagnostico.setManaged(true);
    }

    private void cargarDatos() {
        // Cargar fecha de nacimiento
        if (paciente.getFechaNacimiento() != null) {
            dateFechaNacimiento.setValue(paciente.getFechaNacimiento());
        }

        // Cargar datos personales
        txtDireccion.setText(paciente.getDireccion() != null ? paciente.getDireccion() : "");
        txtCiudad.setText(paciente.getCiudad() != null ? paciente.getCiudad() : "");
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
            cmbTipoSangre.setValue(ultimos.getTipoSangre()); // Cargar tipo de sangre desde signos vitales
        }

        // Cargar diagnóstico y nivel de riesgo
        if (historiaClinica.getDiagnostico() != null) {
            txtDiagnostico.setText(historiaClinica.getDiagnostico());
        }
        if (historiaClinica.getNivelRiesgo() != null) {
            cmbNivelRiesgo.setValue(historiaClinica.getNivelRiesgo());
        }

        // Controlar permisos: solo médicos pueden editar diagnóstico y nivel de riesgo
        if (!esMedico) {
            txtDiagnostico.setEditable(false);
            txtDiagnostico.setDisable(true);
            cmbNivelRiesgo.setDisable(true);
            // Ocultar sección de seguimiento para pacientes
            seccionSeguimiento.setVisible(false);
            seccionSeguimiento.setManaged(false);
        } else {
            // Verificar si ya tiene seguimiento activo
            verificarSeguimientoExistente();
        }
    }

    @FXML
    private void guardarHistoria() {
        if (!validarCampos()) {
            return;
        }

        try {
            // Guardar fecha de nacimiento
            if (dateFechaNacimiento.getValue() != null) {
                paciente.setFechaNacimiento(dateFechaNacimiento.getValue());
            }

            // Guardar datos personales
            paciente.setDireccion(txtDireccion.getText());
            paciente.setCiudad(txtCiudad.getText());
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
                signos.setTipoSangre(cmbTipoSangre.getValue()); // Guardar tipo de sangre en signos vitales

                historiaService.agregarSignosVitales(paciente.getId(), signos);
            }

            // Guardar diagnóstico y nivel de riesgo
            if (!txtDiagnostico.getText().isEmpty()) {
                historiaClinica.setDiagnostico(txtDiagnostico.getText());
            }
            if (cmbNivelRiesgo.getValue() != null) {
                historiaClinica.setNivelRiesgo(cmbNivelRiesgo.getValue());
            }

            // Guardar datos del paciente actualizados
            storage.actualizarUsuario(paciente);

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
        // La validación es opcional, no obligatoria
        // Permitir guardar incluso con campos vacíos
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
            AlertUtils.mostrarError("Error", "No se pudo volver al dashboard");
        }
    }

    // Métodos para Seguimiento Crónico
    private void verificarSeguimientoExistente() {
        SeguimientoService seguimientoService = SeguimientoService.getInstance();
        Seguimiento seguimiento = seguimientoService.obtenerSeguimientoActivoPorPaciente(paciente.getId());

        if (seguimiento != null) {
            lblEstadoSeguimiento.setText("✓ Paciente con seguimiento crónico activo");
            lblEstadoSeguimiento.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
            btnCrearSeguimiento.setVisible(false);
            btnCrearSeguimiento.setManaged(false);
            btnVerSeguimiento.setVisible(true);
            btnVerSeguimiento.setManaged(true);
        } else {
            lblEstadoSeguimiento.setText("Sin seguimiento crónico");
            lblEstadoSeguimiento.setStyle("-fx-text-fill: #757575;");
            btnCrearSeguimiento.setVisible(true);
            btnCrearSeguimiento.setManaged(true);
            btnVerSeguimiento.setVisible(false);
            btnVerSeguimiento.setManaged(false);
        }
    }

    @FXML
    private void crearSeguimientoCronico() {
        // Verificar que tenga diagnóstico antes de crear seguimiento
        if (txtDiagnostico.getText().isEmpty()) {
            AlertUtils.mostrarAdvertencia("Diagnóstico Requerido",
                    "Debe ingresar un diagnóstico antes de crear un seguimiento crónico.");
            return;
        }

        // Mostrar panel para ingresar datos del seguimiento
        panelNuevoSeguimiento.setVisible(true);
        panelNuevoSeguimiento.setManaged(true);
        btnCrearSeguimiento.setDisable(true);

        // Pre-cargar el diagnóstico
        if (!txtDiagnostico.getText().isEmpty()) {
            txtNotasSeguimiento.setText("Diagnóstico: " + txtDiagnostico.getText());
        }
    }

    @FXML
    private void confirmarSeguimiento() {
        if (txtTratamiento.getText().isEmpty()) {
            AlertUtils.mostrarAdvertencia("Tratamiento Requerido",
                    "Debe especificar el tratamiento para el seguimiento crónico.");
            return;
        }

        try {
            SeguimientoService seguimientoService = SeguimientoService.getInstance();
            Medico medico = (Medico) authService.getUsuarioActual();

            Seguimiento seguimiento = new Seguimiento();
            seguimiento.setId(java.util.UUID.randomUUID().toString());
            seguimiento.setPacienteId(paciente.getId());
            seguimiento.setMedicoId(medico.getId());
            seguimiento.setDiagnostico(txtDiagnostico.getText());
            seguimiento.setTratamiento(txtTratamiento.getText());
            seguimiento.setProximaCita(txtProximaCita.getText());
            seguimiento.setNotas(txtNotasSeguimiento.getText());
            seguimiento.setEsCronico(true);
            seguimiento.setEstadoSeguimiento("activo");

            if (seguimientoService.crearSeguimiento(seguimiento)) {
                AlertUtils.mostrarExito("Éxito", "Seguimiento crónico creado exitosamente");
                panelNuevoSeguimiento.setVisible(false);
                panelNuevoSeguimiento.setManaged(false);
                verificarSeguimientoExistente();

                // Limpiar campos
                txtTratamiento.clear();
                txtProximaCita.clear();
                txtNotasSeguimiento.clear();
            } else {
                AlertUtils.mostrarError("Error", "No se pudo crear el seguimiento");
            }

        } catch (Exception e) {
            AlertUtils.mostrarError("Error", "Error al crear el seguimiento: " + e.getMessage());
        }
    }

    @FXML
    private void cancelarSeguimiento() {
        panelNuevoSeguimiento.setVisible(false);
        panelNuevoSeguimiento.setManaged(false);
        btnCrearSeguimiento.setDisable(false);
        txtTratamiento.clear();
        txtProximaCita.clear();
        txtNotasSeguimiento.clear();
    }

    @FXML
    private void verSeguimiento() {
        AlertUtils.mostrarInfo("Seguimiento Crónico",
                "Funcionalidad de ver histórico de seguimiento en desarrollo.\n\n" +
                        "Aquí se mostrará el historial completo de consultas y evolución del paciente.");
    }
}
