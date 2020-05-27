package com.SAS.ClientApp;

import Presentaion.ClientAppApplication;
import com.SAS.ClientApp.Controllers.Vista.VistaNavigator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class DeYMCAApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(ClientAppApplication.class).run();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("DeYMCA");
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(createScene(loadMainPane()));
        //stage.show();
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    static class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage() {
            return ((Stage) getSource());
        }
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
        Scene scene = new Scene(mainPane);
        scene.getStylesheets().setAll(getClass().getResource("/css/vista.css").toExternalForm());
        return scene;
    }
}
