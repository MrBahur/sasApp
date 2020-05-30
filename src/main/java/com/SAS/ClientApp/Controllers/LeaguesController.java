package com.SAS.ClientApp.Controllers;

import com.SAS.ClientApp.Controllers.Vista.VistaNavigator;
import com.SAS.ClientApp.Controllers.viewers.LeaguesViewer;
import com.SAS.ClientApp.Controllers.viewers.TeamsViewer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class LeaguesController implements Initializable {
    public Button add_referee_in_league_btn;
    public Button remove_referee_btn;
    public Button add_referee_btn;
    public Button add_league_btn;
    public Button add_season_to_league_btn;
    public Button add_policies_to_league_btn;
    /**
     * Event handler fired when the user requests a new vista.
     *
     * @param event the event that triggered the handler.
     */
    //@FXML private javafx.scene.control.TableView tableView;
    @FXML
    private AnchorPane leaguesPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Label label = new Label("Leagues");
        label.setFont(new Font("Arial Rounded MT Bold", 24));
        label.setPadding(new Insets(0, 0, 10, 160));

        TableView tableView = new TableView<LeaguesViewer>();
        TableColumn<String, String> leagueCol = new TableColumn<>("League");
        leagueCol.setCellValueFactory((new PropertyValueFactory<>("leagueName")));
        leagueCol.setPrefWidth(200);
        leagueCol.setEditable(true);
        tableView.getColumns().addAll(leagueCol);

        List<LeaguesViewer> leagues = getLeaguesFromServer();
        tableView.getItems().addAll(leagues);
//        addPageButtonToTable(tableView);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(30, 0, 0, 110));
        vbox.getChildren().addAll(label, tableView);

        leaguesPane.getChildren().add(vbox);
        add_league_btn.setOnAction((ActionEvent event) -> {
            openAddLeague();
        });
        add_policies_to_league_btn.setOnAction((ActionEvent event) -> {
            addPolicies();
        });
    }

    private void addPolicies() {
        try {
            FXMLLoader loader = new FXMLLoader(VistaNavigator.class.getResource(VistaNavigator.ADDPOLICIES));
            Parent root = loader.load();
            AddPoliciesController policiesController = loader.getController();
            policiesController.init();
            Scene newScene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setMaxHeight(640);
            newStage.setMaxWidth(620);
            newStage.setScene(newScene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openAddLeague() {
        try {
            FXMLLoader loader = new FXMLLoader(VistaNavigator.class.getResource(VistaNavigator.ADDLEAGUE));
            Parent root = loader.load();
            AddLeagueController leagueController = loader.getController();
            leagueController.init();
            Scene newScene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setMaxHeight(640);
            newStage.setMaxWidth(620);
            newStage.setScene(newScene);
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<LeaguesViewer> getLeaguesFromServer() {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpGet request = new HttpGet(MainController.serverURL + "/league/getLeagues");

            //create the request
            request.getRequestLine();
            request.addHeader("Content-Type", "application/json");

            CloseableHttpResponse response = httpClient.execute(request);

            try {

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    JSONArray res = new JSONArray(result);
                    List<LeaguesViewer> list = new LinkedList<>();

                    for (int i = 0; i < res.length(); i++) {
                        list.add(new LeaguesViewer(res.get(i).toString()));
                    }

                    return list;
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

        return null;
    }
}
