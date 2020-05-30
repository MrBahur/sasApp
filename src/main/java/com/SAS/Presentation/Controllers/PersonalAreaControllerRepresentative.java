package com.SAS.Presentation.Controllers;

import com.SAS.Presentation.Controllers.Vista.VistaNavigator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;


public class PersonalAreaControllerRepresentative {

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
    private javafx.scene.layout.AnchorPane personal;
    @FXML
    private javafx.scene.layout.AnchorPane role;
    @FXML
    private javafx.scene.control.SplitPane split;

    private String sname;
    private String semail;
    private static String currentRepresentative;


    /**
     * Event handler fired when the user requests a new vista.
     *
     * @param event the event that triggered the handler.
     */
    @FXML
    void nextPane(ActionEvent event) {
        VistaNavigator.loadVista(VistaNavigator.PERSONAL_REPRESENTATIVE);
    }

    public boolean init(JSONObject details) {
        username.setText(details.get("Username").toString());
        name.setText(details.get("Name").toString());
        email.setText(details.get("Email").toString());
        this.sname = name.getText();
        this.semail = email.getText();
        this.currentRepresentative = MainController.userRole;
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


    public void confirmTeam(ActionEvent actionEvent) {
        //check in DB
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {

            String url = String.format(MainController.serverURL + "/team/getUnregisteredTeams");
            HttpGet request = new HttpGet(url);
            //create the request
            CloseableHttpResponse response = httpClient.execute(request);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    JSONArray res = new JSONArray(result);
                    ListView<String> listView = new ListView<>();

                    List<Object> listObj = res.toList();
                    List<String> teams = new LinkedList<>();
                    for (Object obj : listObj)
                        teams.add((String) obj);
                    ObservableList items = listView.getItems();
                    items.addAll(teams);
                    listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (!confirmSpecificTeam(listView.getSelectionModel().getSelectedItem()).equals("fail"))
                                listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());

                        }
                    });

                    Stage stage = new Stage();
                    stage.setTitle("Unregistered teams");
                    VBox vbox = new VBox(listView);
                    Scene scene = new Scene(vbox, 350, 250);
                    stage.setScene(scene);
                    stage.show();
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

    private String confirmSpecificTeam(String teamName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Register team approval");
        alert.setHeaderText("Team approval");
        alert.setContentText("Would you like to approve this team?");
        ButtonType buttonApprove = new ButtonType("Approve");
        ButtonType buttonReject = new ButtonType("Reject");
        alert.getButtonTypes().setAll(buttonApprove, buttonReject);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonApprove){
            return sendResponse(teamName, "approve");
        } else if  (result.get() == buttonReject){
            return sendResponse(teamName, "reject");
        }
        alert.show();
        return "";
    }

    private String sendResponse(String teamName, String confirm) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
            HttpPost request = new HttpPost(MainController.serverURL + "/team/approveTeam");
            JSONObject json = new JSONObject();
            json.put("teamName", teamName);
            json.put("confirm", confirm);

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
                    if (!result.equals("fail")) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Team registration");
                        alert.setContentText("Team was approved successfully");
                        alert.show();
                    }
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
}

