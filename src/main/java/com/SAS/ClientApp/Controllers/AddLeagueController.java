package com.SAS.ClientApp.Controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddLeagueController {
    public AnchorPane addLeaguePane;
    public TextField league_name;
    public Button add_league_btn;



    public void init() {
        add_league_btn.setOnAction(event -> addLeague());
    }

    private void addLeague() {
        //check that all fields are full

        if (league_name.getText()==null||league_name.getText()=="") {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing input");
            alert.setContentText("League name is empty");
            alert.show();
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpPost request = new HttpPost(MainController.serverURL + "/league/createLeague");
                JSONObject json = new JSONObject();
                json.put("user", MainController.username);
                json.put("leagueName", league_name.getText());
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
                            alert.setContentText("Your League was created successfully.");
                            alert.show();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Your details are invalid.\nPlease try again.");
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
}
