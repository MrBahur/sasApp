package com.SAS.ClientApp.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.*;
import com.SAS.ClientApp.Controllers.Vista.VistaNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class AnimationController implements Initializable {

    @FXML private StackPane rootPane;
    private static String systemStatus;
    private ExecutorService executor;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        executor = Executors.newSingleThreadExecutor();
        Callable<String> systemCheck = () -> {
            try {
                return isSystemInit();
            } catch (Exception e) {
                throw new IllegalStateException("task interrupted", e);
            }
        };

        try {
            Future<String> future = executor.submit(systemCheck);
            //new SplashScreen().start();
            new HoldScreen().start();
            systemStatus = future.get();
            //executor.shutdownNow();

        } catch (Exception e) {

        }
        //new SplashScreen().start();
    }

    public void loadInitWindow() {
        executor.shutdownNow();
        FXMLLoader loader = new FXMLLoader();
        Pane mainPane = null;
        try {
            mainPane = (Pane) loader.load(getClass().getResourceAsStream(VistaNavigator.WIZARDVIEW));
        } catch (IOException e) {
        }
        //WizardController wizardController = loader.getController();
        //VistaNavigator.setMainController(wizardController);
        Scene scene = new Scene(mainPane);
        Stage stage = new Stage();
        stage.setTitle("System Setup");
        stage.setScene(scene);
        stage.show();

        rootPane.getScene().getWindow().hide();
    }


    public void loadWelcomeWindow() {
        executor.shutdownNow();
        FXMLLoader loader = new FXMLLoader();
        Pane mainPane = null;
        try {
            mainPane = (Pane) loader.load(getClass().getResourceAsStream(VistaNavigator.WELCOME));
        } catch (IOException e) {
        }
      //  MainController mainController = loader.getController();
      //  VistaNavigator.setMainController(mainController);
        Scene scene = new Scene(mainPane);
        Stage stage = new Stage();
        stage.setTitle("DeYMCA");
        stage.setScene(scene);
        stage.show();

        rootPane.getScene().getWindow().hide();
    }

    public String isSystemInit() {
        String res = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request;
        CloseableHttpResponse response = null;
        try {
            request = new HttpGet(MainController.serverURL + "/system/system_check");
            request.getRequestLine();
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                res = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return "bad";
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (Exception e) {
                return "bad";
            }
            return res;
        }
    }

    class HoldScreen extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (systemStatus.equals("true")) {
                            loadWelcomeWindow();
                        }
                        else if(systemStatus.equals("bad")){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("There was a problem connecting to our servers. \n" +
                                    "Please call our IT service or try again later");

                            if(alert.showAndWait().filter(ButtonType.OK::equals).isPresent()){
                                System.exit(0);
                            }
                            System.exit(0);
                        }
                        else {
                            loadInitWindow();
                        }
                    }
                });
            } catch (Exception e) {

            }
        }
    }

}
