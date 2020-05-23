package Controllers;

import Controllers.Vista.VistaNavigator;
import Controllers.viewers.TeamsViewer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class TeamsController implements Initializable {

    //@FXML private javafx.scene.control.TableView tableView;
    @FXML private javafx.scene.layout.AnchorPane teamsPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final Label label = new Label("Teams");
        label.setFont(new Font("Arial", 24));
        label.setPadding(new Insets(0, 0, 10, 160));

        TableView tableView = new TableView<TeamsViewer>();
        TableColumn<String, String> teamCol = new TableColumn<>("Team");
        teamCol.setCellValueFactory((new PropertyValueFactory<>("teamName")));
        teamCol.setPrefWidth(200);
        teamCol.setEditable(true);
        tableView.getColumns().addAll(teamCol);

        List<TeamsViewer> teams = getTeamsFromServer();
        tableView.getItems().addAll(teams);
        addPageButtonToTable(tableView);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(30, 0, 0, 110));
        vbox.getChildren().addAll(label, tableView);

        teamsPane.getChildren().add(vbox);
    }

    private List<TeamsViewer> getTeamsFromServer() {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpGet request = new HttpGet(MainController.serverURL + "/team/getTeams");

            //create the request
            request.getRequestLine();
            request.addHeader("Content-Type", "application/json");

            CloseableHttpResponse response = httpClient.execute(request);

            try {

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    JSONArray res = new JSONArray(result);
                    List<TeamsViewer> list = new LinkedList<>();

                    for (int i = 0; i < res.length(); i++) {
                        list.add(new TeamsViewer(res.get(i).toString()));
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

    /**
     * The function adds enter team page button to table view
     * @param tableView
     */
    private void addPageButtonToTable(TableView<String> tableView) {
        TableColumn<String, Void> colBtn = new TableColumn("Page");
        colBtn.setPrefWidth(200);

        Callback<TableColumn<String, Void>, TableCell<String, Void>> cellFactory = new Callback<TableColumn<String, Void>, TableCell<String, Void>>() {
            @Override
            public TableCell<String, Void> call(final TableColumn<String, Void> param) {
                final TableCell<String, Void> cell = new TableCell<String, Void>() {

                    private final Button btn = new Button("Enter Team Page");
                    {
                        btn.setMaxWidth(150);
                        btn.setStyle("-fx-font-size:14px");
                        btn.setOnAction((ActionEvent event) -> {
                            Object team = getTableView().getItems().get(getIndex());
                            enterTeamPage(((TeamsViewer)team).getTeamName());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);
        tableView.getColumns().add(colBtn);
    }

    private void enterTeamPage(String teamName) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            String url = String.format(MainController.serverURL + "/team/getTeamPage/%s", teamName);
            HttpGet request = new HttpGet(url);
            //create the request
            CloseableHttpResponse response = httpClient.execute(request);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String retSrc = EntityUtils.toString(entity);
                    // parsing JSON
                    JSONObject teamJSON = new JSONObject(retSrc);

                    FXMLLoader loader = new FXMLLoader(VistaNavigator.class.getResource(VistaNavigator.TEAM));
                    Parent root = (Parent) loader.load();

                    TeamController tc = loader.getController();
                    tc.init(teamJSON);
                    Scene newScene = new Scene(root);
                    Stage newStage = new Stage();
                    newStage.setMaxHeight(640);
                    newStage.setMaxWidth(620);
                    newStage.setScene(newScene);
                    newStage.show();
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



    /**
     * Event handler fired when the user requests a new vista.
     *
     * @param event the event that triggered the handler.
     */
    @FXML
    void nextPane(ActionEvent event) {
        VistaNavigator.loadVista(VistaNavigator.TEAMS);
    }

}
