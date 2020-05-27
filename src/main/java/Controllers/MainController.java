package Controllers;

import Controllers.Vista.VistaNavigator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import view.Controller;

public class MainController {

    public static final String serverURL = "http://localhost:8080";

    private String userRole;

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

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public void setUserName(String userName) {
        this.userName.setText(userName);
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
        System.exit(0);
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
