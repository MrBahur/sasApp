package Controllers;

import Controllers.Vista.VistaNavigator;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TeamsController {

    //teams
    @FXML private JFXButton teamBtn;


    /**
     * Event handler fired when the user requests a new vista.
     *
     * @param event the event that triggered the handler.
     */
    @FXML
    void nextPane(ActionEvent event) {
        VistaNavigator.loadVista(VistaNavigator.TEAMS);
    }

    public void enterTeamPage(ActionEvent event) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpGet request = new HttpGet(MainController.serverURL + "/team/getTeamPage/Hapoel");
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

}

