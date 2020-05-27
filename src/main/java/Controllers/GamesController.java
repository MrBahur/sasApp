package Controllers;

import Controllers.Vista.VistaNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GamesController implements Initializable {

    @FXML
    public AnchorPane gamePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableView<Game> table = new TableView<>();
        final Label label = new Label("Games");
        label.setFont(new Font("Arial", 24));
        label.setPadding(new Insets(0, 0, 0, 0));

        table.setEditable(true);

        TableColumn<Game, String> gameDateColumn = new TableColumn<>("Date");
        gameDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Game, String> gameYearColumn = new TableColumn<>("Season");
        gameYearColumn.setCellValueFactory(new PropertyValueFactory<>("seasonYear"));

        TableColumn<Game, String> hostTeamNameColumn = new TableColumn<>("Host");
        hostTeamNameColumn.setCellValueFactory(new PropertyValueFactory<>("hostTeamName"));

        TableColumn<Game, String> guestTeamNameColumn = new TableColumn<>("Guest");
        guestTeamNameColumn.setCellValueFactory(new PropertyValueFactory<>("guestTeamName"));

        TableColumn<Game, String> hostScoreColumn = new TableColumn<>("Host Score");
        hostScoreColumn.setCellValueFactory(new PropertyValueFactory<>("hostScore"));

        TableColumn<Game, String> guestScoreColumn = new TableColumn<>("Guest Score");
        guestScoreColumn.setCellValueFactory(new PropertyValueFactory<>("guestScore"));

        TableColumn<Game, String> leagueNameColumn = new TableColumn<>("League");
        leagueNameColumn.setCellValueFactory(new PropertyValueFactory<>("leagueName"));

        TableColumn<Game, String> stadiumNameColumn = new TableColumn<>("Stadium");
        stadiumNameColumn.setCellValueFactory(new PropertyValueFactory<>("stadiumName"));

        ObservableList<Game> data = getDataFromServer();

        table.setItems(data);
        table.setRowFactory(tv -> {
            TableRow<Game> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !(row.isEmpty())) {
                    Game rowData = row.getItem();
                    openGame(rowData);
                }
            });
            return row;
        });
        table.getColumns().addAll(gameDateColumn, gameYearColumn, hostTeamNameColumn, guestTeamNameColumn,
                hostScoreColumn, guestScoreColumn, leagueNameColumn, stadiumNameColumn);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(30, 0, 0, 0));
        vbox.getChildren().addAll(label, table);

        gamePane.getChildren().add(vbox);


    }

    private void openGame(Game rowData) {
        try {
            FXMLLoader loader = new FXMLLoader(VistaNavigator.class.getResource(VistaNavigator.GAME));
            Parent root = loader.load();
            GameController gameController = loader.getController();

            gameController.init(rowData.getGuestTeamName(), rowData.getHostTeamName(), rowData.getGuestScore(), rowData.getHostScore(), rowData.getGameID());
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

    private ObservableList<Game> getDataFromServer() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ObservableList<Game> list = FXCollections.observableArrayList();
        try {
            HttpGet request = new HttpGet(MainController.serverURL + "/games/get");
            //create the request
            request.getRequestLine();
            request.addHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject res = new JSONObject(result);
                JSONArray array = res.getJSONArray("games");
                for (int i = 0; i < array.length(); i++) {
                    list.add(new Game(array.getJSONObject(i)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //list.add(new Game("gameID", "date", "hostScore", "guestScore", "LeagueName", "hostName", "guestName", "stadiumName", "year"));//for test, remove later
        return list;
    }

    public static class Game {
        private final SimpleStringProperty gameID;
        private final SimpleStringProperty date;
        private final SimpleStringProperty seasonYear;
        private final SimpleStringProperty hostTeamName;
        private final SimpleStringProperty guestTeamName;
        private final SimpleStringProperty hostScore;
        private final SimpleStringProperty guestScore;
        private final SimpleStringProperty leagueName;
        private final SimpleStringProperty stadiumName;

        public Game(String gameID, String date, String hostScore, String guestScore, String leagueName,
                    String hostTeamName, String guestTeamName, String stadiumName, String seasonYear) {
            this.gameID = new SimpleStringProperty(gameID);
            this.date = new SimpleStringProperty(date);
            this.seasonYear = new SimpleStringProperty(seasonYear);
            this.hostTeamName = new SimpleStringProperty(hostTeamName);
            this.guestTeamName = new SimpleStringProperty(guestTeamName);
            this.hostScore = new SimpleStringProperty(hostScore);
            this.guestScore = new SimpleStringProperty(guestScore);
            this.leagueName = new SimpleStringProperty(leagueName);
            this.stadiumName = new SimpleStringProperty(stadiumName);
        }

        public Game(JSONObject jsonGameObject) {
            this.gameID = new SimpleStringProperty(Integer.toString(jsonGameObject.getInt("game_id")));
            this.date = new SimpleStringProperty(jsonGameObject.getString("date"));
            this.seasonYear = new SimpleStringProperty(Integer.toString(jsonGameObject.getInt("season_year")));
            this.hostTeamName = new SimpleStringProperty(jsonGameObject.getString("host_team_name"));
            this.guestTeamName = new SimpleStringProperty(jsonGameObject.getString("guest_team_name"));
            this.hostScore = new SimpleStringProperty(Integer.toString(jsonGameObject.getInt("host_score")));
            this.guestScore = new SimpleStringProperty(Integer.toString(jsonGameObject.getInt("guest_score")));
            this.leagueName = new SimpleStringProperty(jsonGameObject.getString("league_name"));
            this.stadiumName = new SimpleStringProperty(jsonGameObject.getString("stadium_name"));
        }

        public String getGameID() {
            return gameID.get();
        }

        public void setGameID(String gameID) {
            this.gameID.set(gameID);
        }

        public String getDate() {
            return date.get();
        }

        public void setDate(String date) {
            this.date.set(date);
        }

        public String getHostScore() {
            return hostScore.get();
        }

        public void setHostScore(String hostScore) {
            this.hostScore.set(hostScore);
        }

        public String getGuestScore() {
            return guestScore.get();
        }


        public void setGuestScore(String guestScore) {
            this.guestScore.set(guestScore);
        }

        public String getLeagueName() {
            return leagueName.get();
        }


        public void setLeagueName(String leagueName) {
            this.leagueName.set(leagueName);
        }

        public String getHostTeamName() {
            return hostTeamName.get();
        }


        public void setHostTeamName(String hostTeamName) {
            this.hostTeamName.set(hostTeamName);
        }

        public String getGuestTeamName() {
            return guestTeamName.get();
        }


        public void setGuestTeamName(String guestTeamName) {
            this.guestTeamName.set(guestTeamName);
        }

        public String getStadiumName() {
            return stadiumName.get();
        }

        public void setStadiumName(String stadiumName) {
            this.stadiumName.set(stadiumName);
        }

        public String getSeasonYear() {
            return seasonYear.get();
        }

        public void setSeasonYear(String seasonYear) {
            this.seasonYear.set(seasonYear);
        }
    }
}
