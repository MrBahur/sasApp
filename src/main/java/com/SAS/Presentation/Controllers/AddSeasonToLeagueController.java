package com.SAS.Presentation.Controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class AddSeasonToLeagueController {
    public AnchorPane addSeasonToLeaguePane;
    public TextField league_name;
    public TextField season_year;
    public Button add_season_to_league_btn;

    public void init() {
        add_season_to_league_btn.setOnAction(event -> addSeasonToLeague());
    }

    private void addSeasonToLeague() {
        //check that all fields are full

        if (!(league_name.getText() == null || league_name.getText() == ""||season_year.getText() == null || season_year.getText() == "")) {
            try {
                Integer.parseInt(season_year.getText());
                CloseableHttpClient httpClient = HttpClients.createDefault();
                try {
                    HttpPost request = new HttpPost(MainController.serverURL + "/league/addSeasonToLeague");
                    JSONObject json = new JSONObject();
                    json.put("leagueName", league_name.getText());
                    json.put("seasonName", season_year.getText());

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
                                alert.setContentText("Added season to league successfully.");
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
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Wrong input");
                alert.setContentText("The 'year' field is not integer");
                alert.show();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Some of the fields are empty");
            alert.show();
        }
    }
}
