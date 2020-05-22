package Controllers;

import Controllers.Vista.VistaNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TeamsController {

    /**
     * Event handler fired when the user requests a new vista.
     *
     * @param event the event that triggered the handler.
     */
    @FXML
    void nextPane(ActionEvent event) {
        VistaNavigator.loadVista(VistaNavigator.TEAMS);
    }
}