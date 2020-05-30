package com.SAS.Presentation.Controllers;

import com.SAS.Presentation.Controllers.Vista.VistaNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.IOException;


public class PersonalAreaControllerGeneral {

    @FXML
    private javafx.scene.control.TextField name;
    @FXML
    private javafx.scene.control.TextField username;
    @FXML
    private javafx.scene.control.TextField email;
    @FXML
    private javafx.scene.control.Button editbtn;
    @FXML
    private javafx.scene.control.Button savebtn;
    @FXML
    private javafx.scene.control.Button cancelbtn;
    @FXML
    private javafx.scene.control.Button registerbtn;
    @FXML
    private javafx.scene.control.SplitPane split;


    private String sname;
    private String semail;
    private static String current;
    private static String newTeam;


    /**
     * Event handler fired when the user requests a new vista.
     *
     * @param event the event that triggered the handler.
     */
    @FXML
    void nextPane(ActionEvent event) {
        VistaNavigator.loadVista(VistaNavigator.PERSONAL_TEAM_OWNER);
    }

    public boolean init(JSONObject details) {
        username.setText(details.get("Username").toString());
        name.setText(details.get("Name").toString());
        email.setText(details.get("Email").toString());
        this.sname = name.getText();
        this.semail = email.getText();
        this.current = MainController.username;
        return true;
    }

    public void editDetails(ActionEvent actionEvent) {
        split.setStyle("-fx-background-color: #f9ffd4");
        name.setEditable(true);
        email.setEditable(true);
        cancelbtn.setVisible(true);
        savebtn.setVisible(true);
    }

    public void saveDetails(ActionEvent actionEvent) {
        //call update in DB
        if (updateDetails().equals("OK")) {
            sname = name.getText();
            semail = email.getText();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Your details have been saved.");
            alert.setTitle("Details alert");
            alert.show();
            exitEdit();
        }

    }

    private String updateDetails() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
            HttpPost request = new HttpPost(MainController.serverURL + "/users/setUserDetails");

            JSONObject json = new JSONObject();
            json.put("Username", username.getText());
            json.put("Full name", name.getText());
            json.put("Email", email.getText());

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

    public void cancelDetails(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Your changes will not be saved.");
        alert.setTitle("Details alert");
        alert.show();
        exitEdit();
        name.setText(sname);
        email.setText(semail);
    }

    private void exitEdit() {
        split.setStyle("-fx-background-color: white");
        name.setEditable(false);
        email.setEditable(false);
        cancelbtn.setVisible(false);
        savebtn.setVisible(false);
    }

}
