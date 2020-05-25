package Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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

        TableColumn<Game, String> gameIdColumn = new TableColumn<>("ID");
        gameIdColumn.setMinWidth(40);
        gameIdColumn.setCellValueFactory(new PropertyValueFactory<>("gameID"));

        TableColumn<Game, String> gameDateColumn = new TableColumn<>("Date");
        gameDateColumn.setMinWidth(60);
        gameDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Game, String> gameYearColumn = new TableColumn<>("Year");
        gameYearColumn.setMinWidth(20);
        gameYearColumn.setCellValueFactory(new PropertyValueFactory<>("seasonYear"));

        TableColumn<Game, String> hostTeamNameColumn = new TableColumn<>("Host Team");
        hostTeamNameColumn.setMinWidth(100);
        hostTeamNameColumn.setCellValueFactory(new PropertyValueFactory<>("hostTeamName"));

        TableColumn<Game, String> guestTeamNameColumn = new TableColumn<>("Guest Team");
        guestTeamNameColumn.setMinWidth(100);
        guestTeamNameColumn.setCellValueFactory(new PropertyValueFactory<>("guestTeamName"));

        TableColumn<Game, String> hostScoreColumn = new TableColumn<>("Host Score");
        hostScoreColumn.setMinWidth(5);
        hostScoreColumn.setCellValueFactory(new PropertyValueFactory<>("hostScore"));

        TableColumn<Game, String> guestScoreColumn = new TableColumn<>("Guest Score");
        guestScoreColumn.setMinWidth(5);
        guestScoreColumn.setCellValueFactory(new PropertyValueFactory<>("guestScore"));

        TableColumn<Game, String> leagueNameColumn = new TableColumn<>("League Name");
        leagueNameColumn.setMinWidth(100);
        leagueNameColumn.setCellValueFactory(new PropertyValueFactory<>("leagueName"));

        TableColumn<Game, String> stadiumNameColumn = new TableColumn<>("Stadium Name");
        stadiumNameColumn.setMinWidth(100);
        stadiumNameColumn.setCellValueFactory(new PropertyValueFactory<>("stadiumName"));

        ObservableList<Game> data = getDataFromServer();

        table.setItems(data);
        table.getColumns().addAll(gameIdColumn, gameDateColumn, gameYearColumn, hostTeamNameColumn, guestTeamNameColumn,
                hostScoreColumn, guestScoreColumn, leagueNameColumn, stadiumNameColumn);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(30, 0, 0, 0));
        vbox.getChildren().addAll(label, table);

        gamePane.getChildren().add(vbox);


    }

    private ObservableList<Game> getDataFromServer() {
        ObservableList<Game> list = FXCollections.observableArrayList();
        list.add(new Game("10000", "20/20/2020", "9", "9", "very long name", "very long name", "very long name", "very long name", "1992"));
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
