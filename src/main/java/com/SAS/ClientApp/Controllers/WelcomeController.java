package com.SAS.ClientApp.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.SAS.ClientApp.Controllers.Vista.VistaNavigator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class WelcomeController implements Initializable {

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean initDone;
    private Pane mainPane;

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnSignin;

    @FXML
    private Button btnGuest;

    @FXML
    private Button btnSignup;


    @FXML private Stage stage;


    public void handleRegister(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        mainPane = null;
        try {
            mainPane = (Pane) loader.load(getClass().getResourceAsStream(VistaNavigator.REGISTER));
        } catch (IOException e) {
        }
        stage.close();
        Scene scene = new Scene(mainPane);
        stage.setTitle("DeYMCA");
        stage.setScene(scene);
        stage.show();
    }

    public void handleGuestLogin(ActionEvent actionEvent) {
        try {
            Node node = (Node) actionEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            //stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(VistaNavigator.MAIN));
            mainPane = null;
            try {
                mainPane = (Pane) loader.load();
            } catch (IOException e) {
            }
            // Get the Controller from the FXMLLoader
            MainController mainController = loader.getController();
            VistaNavigator.setMainController(loader.getController());
            // Set data in the controller
            mainController.setUserName("Guest");
            mainController.setUserRole("GUEST");
            mainController.setPersonalArea(true);
            stage.close();
            Scene scene = new Scene(mainPane);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){

        }
    }

    public void handleLogin(ActionEvent actionEvent) {
        String response;
        String userName = txtUsername.getText();
        String password = txtPassword.getText();
        if (userName.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Empty credentials");
        } else {
            response = sendLoginRequest();
            if (!response.equals("Failed")) {
                try {
                    JSONObject res = new JSONObject(response);
                    String role = res.get("role").toString();
                    String userID = res.get("user_id").toString();
                    Node node = (Node) actionEvent.getSource();
                    stage = (Stage) node.getScene().getWindow();
                    //stage.close();
                    Callable<Boolean> initDetails = () -> {
                        try {
                            return setPersonalArea(userName, role, userID);
                        } catch (Exception e) {
                            throw new IllegalStateException("task interrupted", e);
                        }
                    };
                    try {
                        Future<Boolean> future = executor.submit(initDetails);
                        new WelcomeController.HoldScreen().start();
                        initDone = future.get();

                    } catch (Exception e) {

                    }

                } catch (Exception e) {

                }
            }
        }
    }

    private String sendLoginRequest() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
            HttpPost request = new HttpPost(MainController.serverURL + "/users/login");
            JSONObject json = new JSONObject();
            json.put("username", txtUsername.getText());
            json.put("password", txtPassword.getText());
            //create the request
            StringEntity stringEntity = new StringEntity(json.toString());
            request.getRequestLine();
            request.setEntity(stringEntity);
            request.addHeader("Content-Type", "application/json");

            CloseableHttpResponse response = httpClient.execute(request);

            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                    if (result.equals("Failed")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Your credentials are incorrect.\nPlease try again.");
                        alert.show();
                    }
                }

            } finally {
                response.close();
            }
        } catch (Exception e) {
            return result;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                return result;
            }
        }
        return result;
    }

    private void loadStage(Stage stage, Pane mainPane){
        executor.shutdownNow();
        stage.close();
        Scene scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        // TODO
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go");
        }

         */
    }

    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }

    private boolean setPersonalArea(String username,  String role, String userID) {
        String response = sendDetailsRequest(username, role);
        if (!response.equals("Failed")) {
            // parsing JSON
            JSONObject details = new JSONObject(response);

            switch (role) {
                case "team_owner":
//                    // Get the Controller from the FXMLLoader
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(VistaNavigator.MAIN));
                    try {
                        mainPane = (Pane) loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Get the Controller from the FXMLLoader
                    MainController mainController = loader.getController();
                    VistaNavigator.setMainController(loader.getController());
                    // Set data in the controller
                    mainController.setPersonalDetails(details);
                    mainController.setUserName(username);
                    mainController.setUserRole(role);
                    mainController.setUserID(userID);
                    mainController.setNoNotificationsButton();

//                    PersonalAreaControllerTeamOwner controller = loader.getController();
//                    // Set data in the controller
//                    Boolean result = controller.init(details);

                    return true;

                default:
                    return true;
            }
        }
        return false;

    }

    private String sendDetailsRequest(String username, String role) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
            String url = String.format(MainController.serverURL + "/users/getUserDetails/%s/%s", username, role);
            HttpGet request = new HttpGet(url);
            //create the request
            CloseableHttpResponse response = httpClient.execute(request);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }

            } finally {
                response.close();
            }
        } catch (Exception e) {
            return result;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                return result;
            }
        }
        return result;
    }


    class HoldScreen extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (initDone) {
                            loadStage(stage, mainPane);
                        }
                    }
                });
            } catch (Exception e) {

            }
        }
    }
}
