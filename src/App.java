import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import config.AppConfig;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/login.fxml"));
            Parent root = loader.load();

            // Crear la escena
            Scene scene = new Scene(root, AppConfig.LOGIN_WIDTH, AppConfig.LOGIN_HEIGHT);

            // Cargar estilos CSS
            scene.getStylesheets().add(getClass().getResource("/resources/css/styles.css").toExternalForm());

            // Configurar el stage
            primaryStage.setTitle(AppConfig.APP_NAME + " - " + AppConfig.APP_FULL_NAME);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
