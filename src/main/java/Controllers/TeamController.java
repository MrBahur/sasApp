package Controllers;

import Controllers.Vista.VistaNavigator;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class TeamController implements Initializable {

    @FXML private javafx.scene.text.Text teamName;
    @FXML private javafx.scene.text.Text teamOwners;
    @FXML private javafx.scene.text.Text teamCoach;
    @FXML private javafx.scene.text.Text teamManager;
    @FXML private javafx.scene.control.ListView Players;
    @FXML private javafx.scene.layout.AnchorPane teamPage;
    @FXML private JFXButton editBtn;


    @FXML
    void nextPane() {
        VistaNavigator.loadVista(VistaNavigator.TEAM);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void init(JSONObject team){
        teamName.setText(team.get("Name").toString());
        teamManager.setText(team.get("Team Manager").toString());
        teamCoach.setText(team.get("Coach").toString());
        List<Object> playerObj= ((JSONArray)team.get("Players")).toList();
        List<String> players = new LinkedList<>();
        for (Object obj: playerObj)
            players.add((String)obj);
        ObservableList items = Players.getItems();
        items.addAll(players);
        List<Object> ownersObj= ((JSONArray)team.get("Team Owners")).toList();
        String owners = "";
        for (Object obj: ownersObj)
            owners+= ((String)obj) + '\n';
        teamOwners.setText(owners);

    }

    public void edit(ActionEvent actionEvent) {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        try {
//            HttpPost request = new HttpPost(MainController.serverURL + "/users/login");
//
//            JSONObject json = new JSONObject();
//
//
//
//        } finally {
//            response.close();
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    } finally {
//        try {
//            httpClient.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
}
