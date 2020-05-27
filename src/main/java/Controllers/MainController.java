package Controllers;

import Controllers.Vista.VistaNavigator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import view.Controller;

public class MainController {

    public static final String serverURL = "http://localhost:8080";

    private String userRole;

    @FXML
    private JFXTextField userName;
    @FXML
    private BorderPane vistaHolder;
    @FXML
    private JFXButton personalArea;
    @FXML
    private JFXButton teams;
    @FXML
    private JFXButton leagues;
    @FXML
    private JFXButton games;
    @FXML
    private JFXButton exit;

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public void setUserName(String userName) {
        this.userName.setText(userName);
    }

    public void setPersonalArea(boolean isDisable) {
        personalArea.setDisable(isDisable);
    }

    public String getUserRole() {
        return userRole;
    }

    public String getUserName() {
        return userName.getText();
    }

    public void setVista(Node node) {
        vistaHolder.getChildren().setAll(node);
    }

    @FXML
    private void exit(ActionEvent event) {
        sendExit(userName);
        System.exit(0);
    }

    private void sendExit(JFXTextField userName) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpPost request = new HttpPost(MainController.serverURL + "/users/exit");

            JSONObject json = new JSONObject();
            json.put("username", userName.getText());

            //create the request
            StringEntity stringEntity = new StringEntity(json.toString());
            request.getRequestLine();
            request.setEntity(stringEntity);
            request.addHeader("Content-Type", "application/json");

            CloseableHttpResponse response = httpClient.execute(request);
        }
        catch (Exception e) {

        }
    }

    @FXML
    public void showPersonalArea(ActionEvent actionEvent) {
        VistaNavigator.loadVista(VistaNavigator.PERSONAL);
    }

    @FXML
    public void showTeams(ActionEvent actionEvent) {
        VistaNavigator.loadVista(VistaNavigator.TEAMS);
    }

    @FXML
    public void showLeagues(ActionEvent actionEvent) {
        VistaNavigator.loadVista(VistaNavigator.LEAGUES);
    }

    @FXML
    public void showGames(ActionEvent actionEvent) {
        VistaNavigator.loadVista(VistaNavigator.GAMES);
    }

}
