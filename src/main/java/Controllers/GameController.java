package Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public AnchorPane gamePane;
    public TextField guest_team_name;
    public TextField guest_team_score;
    public TextField host_team_name;
    public TextField host_team_score;
    public TableView<Event> events_table;
    public Button add_event_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init(String guestTeamName, String hostTeamName, String guestScore, String hostScore, String gameID) {
        this.guest_team_name.setText(guestTeamName);
        this.guest_team_score.setText(guestScore);
        this.host_team_name.setText(hostTeamName);
        this.host_team_score.setText(hostScore);

        events_table.setEditable(true);

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
