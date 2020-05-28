package com.SAS.ClientApp.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public AnchorPane gamePane;
    public TextField guest_team_name;
    public TextField guest_team_score;
    public TextField host_team_name;
    public TextField host_team_score;
    public TableView<Event> events_table;
    public ChoiceBox<String> goal_choice_box;
    public TextField goal_player_name;
    public Button goal_btn;
    public Button injury_btn;
    public TextField injury_player_name;
    public TextArea injury_description;
    public Button offence_btn;
    public TextField offence_player_committed;
    public TextField offence_player_against;
    public TextArea offence_description;
    public Button offside_btn;
    public ChoiceBox<String> offside_choice_box;
    public TextField offside_player_name;
    public Button sub_btn;
    public TextField sub_player_in;
    public TextField sub_player_out;
    public Button card_btn;
    public ChoiceBox<String> card_choice_box;
    public TextField card_referee;
    public TextField card_player;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init(String guestTeamName, String hostTeamName, String guestScore, String hostScore, String gameID, LocalDate gameDate) {
        this.guest_team_name.setText(guestTeamName);
        this.guest_team_name.setEditable(false);
        this.guest_team_score.setText(guestScore);
        this.guest_team_score.setEditable(false);
        this.host_team_name.setText(hostTeamName);
        this.host_team_name.setEditable(false);
        this.host_team_score.setText(hostScore);
        this.host_team_score.setEditable(false);
        this.events_table.setEditable(true);

        this.goal_btn.setOnAction((event -> addEventToGame(gameID, gameDate, "goal")));
        this.injury_btn.setOnAction(event -> addEventToGame(gameID, gameDate, "injury"));
        this.offence_btn.setOnAction(event -> addEventToGame(gameID, gameDate, "offence"));
        this.offside_btn.setOnAction(event -> addEventToGame(gameID, gameDate, "offside"));
        this.sub_btn.setOnAction(event -> addEventToGame(gameID, gameDate, "sub"));
        this.card_btn.setOnAction(event -> addEventToGame(gameID, gameDate, "card"));

        this.goal_choice_box.getItems().add("Host");
        this.goal_choice_box.getItems().add("Guest");

        this.offside_choice_box.getItems().add("Host");
        this.offside_choice_box.getItems().add("Guest");

        this.card_choice_box.getItems().add("Yellow");
        this.card_choice_box.getItems().add("Red");

        TableColumn<Event, String> gameMinuteColumn = new TableColumn<>("game minute");
        gameMinuteColumn.setCellValueFactory(new PropertyValueFactory<>("gameMinute"));
        gameMinuteColumn.setSortType(TableColumn.SortType.ASCENDING);

        TableColumn<Event, String> typeColumn = new TableColumn<>("event type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("eventType"));

        TableColumn<Event, String> descriptionColumn = new TableColumn<>("description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        ObservableList<Event> data = getEventsFromServer(gameID);
        events_table.setItems(data);

        events_table.getColumns().addAll(gameMinuteColumn, typeColumn, descriptionColumn);
        events_table.getSortOrder().add(gameMinuteColumn);

    }

    private void addEventToGame(String gameID, LocalDate gameDate, String type) {
        if (canAlterGame(gameID, gameDate)) {
            switch (type) {
                case "goal":
                    String goalTeamName = (goal_choice_box.getValue().equals("Host")) ? host_team_name.getText() : guest_team_name.getText();
                    String goalPlayerName = goal_player_name.getText();
                    break;
                case "injury":
                    String injuryPlayerName = injury_player_name.getText();
                    String injuryDesc = injury_description.getText();
                    break;
                case "offence":
                    String offenceDesc = offence_description.getText();
                    String offencePlayerComm = offence_player_committed.getText();
                    String offencePlayerAgainst = offence_player_against.getText();
                    break;
                case "offside":
                    String offsidePlayerName = offside_player_name.getText();
                    String offsideTeamName = (offside_choice_box.getValue().equals("Host")) ? host_team_name.getText() : guest_team_name.getText();
                    break;
                case "sub":
                    String subPlayerIn = sub_player_in.getText();
                    String subPlayerOut = sub_player_out.getText();
                    break;
                case "ticket":
                    String ticketType = card_choice_box.getValue();
                    String playerAgainst = card_player.getText();
                    String refereePulled = card_referee.getText();
                    break;
                default:
                    break;
            }
        } else {
            System.out.println("can't alter event");
        }
    }

    private boolean canAlterGame(String gameID, LocalDate gameDate) {
        //test if the game can be edited (5 hours after it end) and after it starts.
        LocalTime gameHour = LocalTime.of(20, 0);
        LocalDateTime gameTime = LocalDateTime.of(gameDate, gameHour);
        if (LocalDateTime.now().isBefore(gameTime.plusHours(6).plusMinutes(30)) && LocalDateTime.now().isAfter(gameTime)) {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            //check if the referee is in the game
            try {
                HttpGet request = new HttpGet(MainController.serverURL + String.format("/games/referee/%s", gameID));
                request.getRequestLine();
                request.addHeader("Content-Type", "application/json");
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    JSONObject res = new JSONObject(result);
                    JSONArray refs = res.getJSONArray("refs");
                    boolean canEdit = false;
                    for (int i = 0; !canEdit && i < refs.length(); i++) {
                        String loggedInID = MainController.userID;
                        canEdit = loggedInID.equals(refs.getInt(i) + "");
                    }
                    if (canEdit) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    private ObservableList<Event> getEventsFromServer(String gameID) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ObservableList<Event> list = FXCollections.observableArrayList();
        try {
            HttpGet request = new HttpGet(MainController.serverURL + String.format("/games/events/%s", gameID));
            request.getRequestLine();
            request.addHeader("Content-Type", "application/json");
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject res = new JSONObject(result);
                //tickets
                JSONArray tickets = res.getJSONArray("tickets");
                for (int i = 0; i < tickets.length(); i++) {
                    JSONObject event = tickets.getJSONObject(i);
                    Event ticket = new Event(event.getInt("game_minute") + "", getDescriptionForTicket(event), "ticket", event);
                    list.add(ticket);
                }
                //injuries
                JSONArray injuries = res.getJSONArray("injuries");
                for (int i = 0; i < injuries.length(); i++) {
                    JSONObject event = injuries.getJSONObject(i);
                    Event injury = new Event(event.getInt("game_minute") + "", getDescriptionForInjury(event), "injury", event);
                    list.add(injury);
                }
                //subs
                JSONArray subs = res.getJSONArray("subs");
                for (int i = 0; i < subs.length(); i++) {
                    JSONObject event = subs.getJSONObject(i);
                    Event sub = new Event(event.getInt("game_minute") + "", getDescriptionForSub(event), "subs", event);
                    list.add(sub);
                }
                //offences
                JSONArray offences = res.getJSONArray("offences");
                for (int i = 0; i < offences.length(); i++) {
                    JSONObject event = offences.getJSONObject(i);
                    Event offence = new Event(event.getInt("game_minute") + "", getDescriptionForOffence(event), "offence", event);
                    list.add(offence);
                }
                //goals
                JSONArray goals = res.getJSONArray("goals");
                for (int i = 0; i < goals.length(); i++) {
                    JSONObject event = goals.getJSONObject(i);
                    Event goal = new Event(event.getInt("game_minute") + "", getDescriptionForGoal(event), "goal", event);
                    list.add(goal);
                }
                //offsides
                JSONArray offsides = res.getJSONArray("offsides");
                for (int i = 0; i < offsides.length(); i++) {
                    JSONObject event = offsides.getJSONObject(i);
                    Event offside = new Event(event.getInt("game_minute") + "", getDescriptionForOffside(event), "offside", event);
                    list.add(offside);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return list;
    }

    private String getDescriptionForOffside(JSONObject event) {
        return String.format("Team in favor: %s Player committed: %s", event.getString("team_in_favor"), event.getString("player_committed"));
    }

    private String getDescriptionForGoal(JSONObject event) {
        return String.format("Scoring team: %S Scorer: %s ", event.getString("team_name"), event.getString("player"));
    }

    private String getDescriptionForOffence(JSONObject event) {
        return String.format("Player against: %s Player committed: %s %s", event.getString("player_against"), event.getString("player_committed"), event.getString("description"));
    }

    private String getDescriptionForSub(JSONObject event) {
        return String.format("Player in: %s Player out %s", event.getString("player_in"), event.getString("player_out"));
    }

    private String getDescriptionForInjury(JSONObject event) {
        return String.format("Player injured: %s %s", event.getString("player"), event.getString("description"));
    }

    private String getDescriptionForTicket(JSONObject event) {
        return String.format("color: %s Referee pulled: %s Player against: %s", event.getString("type"), event.getString("referee_pulled"), event.getString("player_against"));
    }

    public static class Event {
        private final SimpleStringProperty gameMinute;
        private final SimpleStringProperty description;
        private final SimpleStringProperty eventType;
        private final JSONObject event;

        public Event(String gameMinute, String description, String eventType, JSONObject event) {
            this.gameMinute = new SimpleStringProperty(gameMinute);
            this.description = new SimpleStringProperty(description);
            this.eventType = new SimpleStringProperty(eventType);
            this.event = event;
        }

        public String getEventType() {
            return eventType.get();
        }

        public void setEventType(String eventType) {
            this.eventType.set(eventType);
        }

        public String getGameMinute() {
            return gameMinute.get();
        }

        public void setGameMinute(String gameMinute) {
            this.gameMinute.set(gameMinute);
        }

        public String getDescription() {
            return description.get();
        }

        public void setDescription(String description) {
            this.description.set(description);
        }
    }
}
