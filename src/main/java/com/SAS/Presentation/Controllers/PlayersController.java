package com.SAS.Presentation.Controllers;

import com.SAS.Presentation.Controllers.Vista.VistaNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class PlayersController {

    /**
     * Event handler fired when the user requests a new vista.
     *
     * @param event the event that triggered the handler.
     */
    @FXML
    void nextPane(ActionEvent event) {
        VistaNavigator.loadVista(VistaNavigator.PLAYERS);
    }
}
