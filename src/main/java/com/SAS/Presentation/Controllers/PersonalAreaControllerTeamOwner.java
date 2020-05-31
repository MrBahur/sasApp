package com.SAS.Presentation.Controllers;

import com.SAS.Presentation.Controllers.Vista.VistaNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.Tab;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.IOException;


public class PersonalAreaControllerTeamOwner {

    @FXML private javafx.scene.control.TextField name;
    @FXML private javafx.scene.control.TextField username;
    @FXML private javafx.scene.control.TextField email;
    @FXML private javafx.scene.control.TextField teamName;
    @FXML private javafx.scene.control.TextField nominatedBy;
    @FXML private javafx.scene.control.Button editbtn;
    @FXML private javafx.scene.control.Button savebtn;
    @FXML private javafx.scene.control.Button cancelbtn;
    @FXML private javafx.scene.control.Button registerbtn;
    @FXML private javafx.scene.layout.AnchorPane personal;
    @FXML private javafx.scene.layout.AnchorPane role;
    @FXML private javafx.scene.control.SplitPane split;
    @FXML private javafx.scene.control.TextField newTeamName;
    @FXML private Tab tabPersonal;
    @FXML private Tab tabActions;


    private String sname;
    private String semail;
    private static String currentOwner;
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

    public boolean init(JSONObject details, String currentOwner){
        username.setText(details.get("Username").toString());
        name.setText(details.get("Name").toString());
        email.setText(details.get("Email").toString());
        if (details.has("Team name")){
            teamName.setText(details.get("Team name").toString());

        }
        if (details.has("Nominated by")) {
            nominatedBy.setText(details.get("Nominated by").toString());
        }
        this.sname= name.getText();
        this.semail = email.getText();
        this.currentOwner = currentOwner;
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
            sname= name.getText();
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

    private void exitEdit(){
        split.setStyle("-fx-background-color: white");
        name.setEditable(false);
        email.setEditable(false);
        cancelbtn.setVisible(false);
        savebtn.setVisible(false);
    }

    public void registerTeam(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(VistaNavigator.class.getResource(VistaNavigator.OPENTEAM));
        try {
            Parent root = (Parent) loader.load();
            Scene newScene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setTitle("Team registration");
            newStage.setMaxHeight(640);
            newStage.setMaxWidth(620);
            newStage.setScene(newScene);
            newStage.show();
        }catch(Exception e){

        }
    }


    public void checkTeamStatus(ActionEvent actionEvent) {

        //check in DB
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            if (newTeam==null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Registration status");
                alert.setContentText("Please open a new registration request first");
                alert.show();
                return;
            }

            String url = String.format(MainController.serverURL + "/team/%s/teamStatus", newTeam.replace(" ", "%20"));
            HttpGet request = new HttpGet(url);
            //create the request
            CloseableHttpResponse response = httpClient.execute(request);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Registration status");
                    alert.setHeaderText("Registration status");
                    alert.setContentText("Your new team's status: " + result);
                    alert.show();
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




    public void registerRequest(ActionEvent actionEvent) {
        //Send to DB
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpPost request = new HttpPost(MainController.serverURL + "/team/registerTeam");

            JSONObject json = new JSONObject();
            json.put("teamName", newTeamName.getText());
            json.put("teamOwner", currentOwner);
            newTeam = newTeamName.getText();

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
                    if (result.equals("success")) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Your request to register the team was sent to the Association. You may follow the request status to check if registration was completed");
                        alert.show();
                        Node node = (Node) actionEvent.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        stage.close();

                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Your team name is invalid or your permissions are not sufficient.");
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
