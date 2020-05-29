package com.SAS.ClientApp.Controllers;

import com.SAS.ClientApp.Controllers.Vista.VistaNavigator;
import com.SAS.ClientApp.Controllers.viewers.TeamsViewer;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class TeamController implements Initializable {

    @FXML private javafx.scene.text.Text teamName;
    @FXML private javafx.scene.text.Text teamOwners;
    @FXML private javafx.scene.text.Text teamCoach;
    @FXML private javafx.scene.text.Text teamManager;
    @FXML private javafx.scene.control.ListView Players;
    @FXML private javafx.scene.layout.AnchorPane teamPage;
    @FXML private JFXButton editBtn;
    @FXML private JFXButton closeBtn;
    private static boolean open;


    @FXML
    void nextPane() {
        VistaNavigator.loadVista(VistaNavigator.TEAM);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (MainController.userRole.equals("team_owner")){
            editBtn.setVisible(true);
            closeBtn.setVisible(true);
        }else if(MainController.userRole.equals("team_manager")){
            editBtn.setVisible(true);
        }
    }

    public void init(JSONObject team){
        teamName.setText(team.get("Name").toString());
        teamManager.setText(team.get("Team Manager").toString());
        teamCoach.setText(team.get("Coach").toString());
        List<Object> playerObj= ((JSONArray)team.get("Players")).toList();
        List<String> players = new LinkedList<>();
        for (Object obj: playerObj)
            players.add((String)obj);
        ObservableList items = Players.getItems();
        items.addAll(players);
        List<Object> ownersObj= ((JSONArray)team.get("Team Owners")).toList();
        String owners = "";
        for (Object obj: ownersObj)
            owners+= ((String)obj) + '\n';
        teamOwners.setText(owners);
        open = team.get("Activity status").toString().equals("true") ? true : false;
        if(open)
            closeBtn.setText("Close team");
        else
            closeBtn.setText("Open team");


    }

    public void edit(ActionEvent actionEvent) {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        try {
//            HttpPost request = new HttpPost(MainController.serverURL + "/users/login");
//
//            JSONObject json = new JSONObject();
//
//
//
//        } finally {
//            response.close();
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    } finally {
//        try {
//            httpClient.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

    public void closeOpenTeam(ActionEvent actionEvent) {
        String result;
        if (!open){  //team is closed
            result = openTeam();
            if (!result.equals("fail")) {
                closeBtn.setText("Close team");
                open = true;
            }
        }else{
            result = closeTeam();
            if(!result.equals("fail")) {
                closeBtn.setText("Open team");
                open = false;
            }
        }
    }

    private String openTeam(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
            HttpPost request = new HttpPost(MainController.serverURL + "/team/openTeam");
            JSONObject json = new JSONObject();
            json.put("teamName", teamName.getText());
            json.put("owner", MainController.username);
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
                    if (result.equals("fail")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Team could not be opened.");
                        alert.show();
                        return result;
                    }else{
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Open team");
                        alert.setContentText("Team was opened successfully");
                        alert.show();
                        return result;
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

    private String closeTeam() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
            HttpPost request = new HttpPost(MainController.serverURL + "/team/closeTeam");
            JSONObject json = new JSONObject();
            json.put("teamName", teamName.getText());
            json.put("owner", MainController.username);
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
                    if (result.equals("fail")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Team could not be closed.");
                        alert.show();
                        return result;
                    } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Close team");
                        alert.setContentText("Team was closed successfully");
                        alert.show();
                        return result;
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

    public void closeTeam(ActionEvent actionEvent) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpPost request = new HttpPost(MainController.serverURL + "/team/closeTeam");

            JSONObject json = new JSONObject();
            json.put("teamName", teamName.getText());
            json.put("owner", "Rami123");

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
                        VistaNavigator.getMainController().setNotificationsButton();
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

