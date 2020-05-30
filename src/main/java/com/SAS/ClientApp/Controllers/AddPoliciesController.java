package com.SAS.ClientApp.Controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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

public class AddPoliciesController {
    public AnchorPane addPoliciesPane;
    public TextField league_name;
    public TextField season_year;
    public Button add_policies_btn;
    public ToggleGroup ranking;
    public ToggleGroup rounds;
    public ToggleGroup points;

    public void init() {
        add_policies_btn.setOnAction(event -> addPolicies());
    }

    private void addPolicies() {
        //check that all fields are full

        if (!(league_name.getText() == null || league_name.getText() == ""||season_year.getText() == null || season_year.getText() == "")) {
            try {
                Integer.parseInt(season_year.getText());
                CloseableHttpClient httpClient = HttpClients.createDefault();
                try {
                    HttpPost request = new HttpPost(MainController.serverURL + "/league/definePolicies");
                    JSONObject json = new JSONObject();
                    json.put("username", MainController.username);
                    json.put("leagueName", league_name.getText());
                    json.put("seasonName", season_year.getText());
                    if(ranking.getSelectedToggle()==null||rounds.getSelectedToggle()==null||points.getSelectedToggle()==null){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Please choose all the policies");
                        alert.show();
                        throw new Exception();
                    }
                    else {
                        if(ranking.getSelectedToggle().toString().charAt(16)=='1'){
                            json.put("leagueRankPolicy", "2");
                        }
                        else {
                            if (ranking.getSelectedToggle().toString().charAt(16) == '2') {
                                json.put("leagueRankPolicy", "1");
                            }
                        }
                        if(points.getSelectedToggle().toString().charAt(16)=='3'){
                            json.put("pointsPolicy", "3");
                        }
                        else {
                            if (points.getSelectedToggle().toString().charAt(16) == '4') {
                                json.put("pointsPolicy", "2");
                            }
                            else{
                                if (points.getSelectedToggle().toString().charAt(16) == '5') {
                                    json.put("pointsPolicy", "1");
                                }
                            }
                        }
                        if(rounds.getSelectedToggle().toString().charAt(16)=='6'){
                            json.put("gamePolicy", "1");
                        }
                        else {
                            if (rounds.getSelectedToggle().toString().charAt(16) == '7') {
                                json.put("gamePolicy", "2");
                            }
                            else{
                                if (rounds.getSelectedToggle().toString().charAt(16) == '8') {
                                    json.put("gamePolicy", "3");
                                }
                            }
                        }
                    }
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
                                alert.setContentText("Added policies to league in season successfully.");
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
                alert.setContentText("Some or all of the input is incorrect or missing");
                alert.show();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("League name is empty");
            alert.show();
        }
    }
}
