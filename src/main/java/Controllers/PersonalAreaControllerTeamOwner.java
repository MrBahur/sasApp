package Controllers;

import Controllers.Vista.VistaNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.json.JSONObject;

public class PersonalAreaControllerTeamOwner {

    @FXML private javafx.scene.control.TextField name;
    @FXML private javafx.scene.control.TextField username;
    @FXML private javafx.scene.control.TextField email;
    @FXML private javafx.scene.control.TextField teamName;
    @FXML private javafx.scene.control.TextField nominatedBy;
    @FXML private javafx.scene.control.Button editbtn;
    @FXML private javafx.scene.control.Button savebtn;
    @FXML private javafx.scene.control.Button cancelbtn;
    @FXML private javafx.scene.layout.AnchorPane personal;
    @FXML private javafx.scene.layout.AnchorPane role;
    @FXML private javafx.scene.control.SplitPane split;

    private String sname;
    private String semail;


    /**
     * Event handler fired when the user requests a new vista.
     *
     * @param event the event that triggered the handler.
     */
    @FXML
    void nextPane(ActionEvent event) {
        VistaNavigator.loadVista(VistaNavigator.PERSONAL_TEAM_OWNER);
    }

    public void init(JSONObject details){
        username.setText(details.get("Username").toString());
        name.setText(details.get("Name").toString());
        email.setText(details.get("Email").toString());

    }

    public void editDetails(ActionEvent actionEvent) {
        split.setStyle("-fx-background-color: #f9ffd4");
        name.setEditable(true);
        email.setEditable(true);
        cancelbtn.setVisible(true);
        savebtn.setVisible(true);
    }

    public void saveDetails(ActionEvent actionEvent) {
        //TODO update details in DB
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Your details have been saved.");
        alert.setTitle("Details alert");
        alert.show();
        exitEdit();

    }

    public void cancelDetails(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Your changes will not be saved.");
        alert.setTitle("Details alert");
        alert.show();
        exitEdit();
        name.setText(sname);
        email.setText(semail);
    }

    private void exitEdit(){
        split.setStyle("-fx-background-color: white");
        name.setEditable(false);
        email.setEditable(false);
        cancelbtn.setVisible(false);
        savebtn.setVisible(false);
    }
}
