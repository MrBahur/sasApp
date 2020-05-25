package Controllers;

import Controllers.Vista.VistaNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class registerController {

    @FXML private javafx.scene.control.TextField userNameField;
    @FXML private javafx.scene.control.TextField passwordField;
    @FXML private javafx.scene.control.TextField fullNameField;
    @FXML private javafx.scene.control.TextField emailField;
    @FXML private javafx.scene.control.ComboBox occupation;


    public void handleSubmit(ActionEvent actionEvent) {
        register();
        showWelcomeScreen(actionEvent);
    }

    private void showWelcomeScreen(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        Pane mainPane = null;
        try {
            mainPane = (Pane) loader.load(getClass().getResourceAsStream(VistaNavigator.WELCOME));
        } catch (IOException e) {
        }
        stage.close();
        Scene scene = new Scene(mainPane);
        stage.setTitle("DeYMCA");
        stage.setScene(scene);
        stage.show();
    }


    public void register() {

        //check that all fields are full

        if (fullNameField.getText().isEmpty() || userNameField.getText().isEmpty() || passwordField.getText().isEmpty() || emailField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing input");
            alert.setContentText("One or more of the registration fields are empty. Please make sure you fill in all the required details");
            alert.show();
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpPost request = new HttpPost(MainController.serverURL + "/users/register");

                JSONObject json = new JSONObject();
                json.put("username", userNameField.getText());
                json.put("password", passwordField.getText());
                json.put("full name", fullNameField.getText());
                json.put("email", emailField.getText());
                json.put("occupation", ((String)occupation.getValue()).replace(' ','_'));
                //create the request
                StringEntity stringEntity = new StringEntity(json.toString());
                request.getRequestLine();
                request.setEntity(stringEntity);
                request.addHeader("Content-Type", "application/json");
                CloseableHttpResponse response = httpClient.execute(request);
                try {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String result = EntityUtils.toString(entity);
                        if (result.equals("Created")) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setContentText("Your account was created successfully.");
                            alert.show();
                            //move to login
                            //VistaNavigator.loadVista(VistaNavigator.LOGIN);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Your details are invalid.\nPlease try again.");
                            alert.show();
                        }
                    }

                } finally {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
