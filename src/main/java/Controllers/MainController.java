package Controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Main controller class for the entire layout.
 */
public class MainController {

    protected static final String serverURL = "http://localhost:8080";

    /** Holder of a switchable vista. */
    @FXML
    private StackPane vistaHolder;

    /**
     * Replaces the vista displayed in the vista holder with a new vista.
     *
     * @param node the vista node to be swapped in.
     */
    public void setVista(Node node) {
        vistaHolder.getChildren().setAll(node);
    }

}