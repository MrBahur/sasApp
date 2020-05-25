package Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public AnchorPane gamePane;
    public TextField guest_team_name;
    public TextField guest_team_score;
    public TextField host_team_name;
    public TextField host_team_score;
    public TableView<Event> events_table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init(String guestTeamName, String hostTeamName, String guestScore, String hostScore, String gameID) {
        this.guest_team_name.setText(guestTeamName);
        this.guest_team_score.setText(guestScore);
        this.host_team_name.setText(hostTeamName);
        this.host_team_score.setText(hostScore);
    }

    public static class Event{

    }
}
