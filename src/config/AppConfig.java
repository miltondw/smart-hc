package config;

public class AppConfig {

    // Información de la aplicación
    public static final String APP_NAME = "SMART HC";
    public static final String APP_FULL_NAME = "Historias Clínicas Inteligentes";
    public static final String APP_VERSION = "1.0.0";

    // Rutas de recursos
    public static final String FXML_PATH = "/resources/fxml/";
    public static final String CSS_PATH = "/resources/css/";
    public static final String DATA_PATH = "data/";

    // Dimensiones de ventanas
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 700;
    public static final int LOGIN_WIDTH = 400;
    public static final int LOGIN_HEIGHT = 500;

    // Colores del tema
    public static final String PRIMARY_COLOR = "#2196F3";
    public static final String SECONDARY_COLOR = "#03A9F4";
    public static final String SUCCESS_COLOR = "#4CAF50";
    public static final String WARNING_COLOR = "#FF9800";
    public static final String ERROR_COLOR = "#F44336";

    // Configuración de historia clínica
    public static final double PORCENTAJE_COMPLETADO_MIN = 80.0;

    // Mensajes del sistema
    public static final String MSG_LOGIN_EXITOSO = "Inicio de sesión exitoso";
    public static final String MSG_LOGIN_FALLIDO = "Email o contraseña incorrectos";
    public static final String MSG_CAMPOS_VACIOS = "Por favor complete todos los campos requeridos";
    public static final String MSG_GUARDADO_EXITOSO = "Información guardada correctamente";
    public static final String MSG_ERROR_GUARDADO = "Error al guardar la información";

    private AppConfig() {
        // Constructor privado para evitar instanciación
    }
}
