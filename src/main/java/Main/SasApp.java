package Main;

import Controllers.Vista.VistaNavigator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Main.Main application class.
 */
public class SasApp extends Application {

    public static Boolean isSplashLoaded = false;



    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("DeYMCA");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(createScene(loadMainPane()));
        stage.show();
    }

    /**
     * Loads the main fxml layout.
     * Sets up the vista switching Controllers.VistaNavigator.Controllers.VistaNavigator.
     * Loads the first vista into the fxml layout.
     *
     * @return the loaded pane.
     * @throws IOException if the pane could not be loaded.
     */
    private Pane loadMainPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane mainPane = (Pane) loader.load(getClass().getResourceAsStream(VistaNavigator.ANIMATION));
        //MainController mainController = loader.getController();
        //VistaNavigator.setMainController(mainController);
        return mainPane;
    }

    /**
     * Creates the main application scene.
     *
     * @param mainPane the main application layout.
     * @return the created scene.
     */
    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(
                mainPane
        );
        scene.getStylesheets().setAll(
                getClass().getResource("/css/vista.css").toExternalForm()
        );

        return scene;
    }

    /*
    public static void main(String[] args) {
        //SpringApplication.run(SasApp.class, args);
        //launch(args);
    }
    */
}