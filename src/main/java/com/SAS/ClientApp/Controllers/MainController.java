package com.SAS.ClientApp.Controllers;

import com.SAS.ClientApp.Controllers.Vista.VistaNavigator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Service
public class MainController {

    @Autowired
    public static final String serverURL = "http://localhost:8080";
    private JSONObject personalDetails;
    private String userRole;
    private List<String> notifications = new LinkedList<>();

    public static String userID;

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
    @FXML
    private javafx.scene.control.Button notificationBtn;


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

    public JSONObject getPersonalDetails() {
        return this.personalDetails;
    }

    public void setPersonalDetails(JSONObject personalDetails) {
        this.personalDetails = personalDetails;
    }

    public void setUserID(String userID) {
        MainController.userID = userID;
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
        Pane pane = null;
        switch (userRole){
            case "team_owner":
                FXMLLoader loader = new FXMLLoader(getClass().getResource(VistaNavigator.PERSONAL_TEAM_OWNER));
                try {
                    pane = (Pane) loader.load();
                } catch (IOException e) {
                }
                PersonalAreaControllerTeamOwner controller = loader.getController();
                //Set data in the controller
                controller.init(personalDetails, userName.getText());
                try {
                    //Node node = loader.load();
                    VistaNavigator.loadVista(pane);
                }catch (Exception e){

                }
                //TODO: other roles
        }
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

    public void addNotification(String message) {
        notificationBtn.setStyle("-fx-background-image: url('/images/notification.jpeg')");
        this.notifications.add(message);
    }

    public void showNotifications(ActionEvent actionEvent) {
        notificationBtn.setStyle("-fx-background-image: url('/images/noNotification.jpeg')");
        this.notifications.add("bla");
        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList (notifications);
        list.setItems(items);

        Stage notificationsStage = new Stage();
        notificationsStage.setTitle("Notifications");

        VBox vbox = new VBox(list);
        Scene scene = new Scene(vbox, 350, 250);
        notificationsStage.setScene(scene);
        notificationsStage.show();
    }
}
