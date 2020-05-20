package Controllers;

import Controllers.Vista.VistaNavigator;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;


/**
 * Main controller class for the entire layout.
 */
public class MainController {

    protected static final String serverURL = "http://localhost:8080";

    /**
     * Holder of a switchable vista.
     */
    @FXML private BorderPane vistaHolder;
    @FXML private JFXButton personalArea;
    @FXML private JFXButton teams;
    @FXML private JFXButton leagues;
    @FXML private JFXButton games;
    @FXML private JFXButton exit;


    /**
     * Replaces the vista displayed in the vista holder with a new vista.
     *
     * @param node the vista node to be swapped in.
     */
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
