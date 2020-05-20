package Controllers.wizard;

import Controllers.MainController;
import com.google.gson.Gson;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.controlsfx.control.ListSelectionView;
import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ExternalSystemsController implements Initializable {

    private List<String> externalSystems = new LinkedList<>();

    @FXML
    private ListSelectionView systemsAvailable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        externalSystems.addAll(getExernalSystems());
        for (String system: externalSystems) {
            systemsAvailable.getSourceItems().add(system);
        }
    }

    private List<String> getExernalSystems() {
        String res;
        List<String> systems = new LinkedList();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request;
        CloseableHttpResponse response = null;
        try {
            request = new HttpGet(MainController.serverURL + "/system/external_systems");
            request.getRequestLine();
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                res = EntityUtils.toString(entity, "UTF-8");
                systems.addAll(Arrays.asList(new Gson().fromJson(res, String[].class)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return systems;
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (Exception e) {
                return systems;
            }
            return systems;
        }
    }

    private boolean addExternalSystem(String name){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request;
        CloseableHttpResponse response = null;
        try {
            request = new HttpPost(MainController.serverURL + "/system/add_system");

            JSONObject json = new JSONObject();
            json.put("name", name);

            //create the request
            StringEntity stringEntity = new StringEntity(json.toString());
            request.getRequestLine();
            request.setEntity(stringEntity);
            request.addHeader("Content-Type", "application/json");
            response = httpClient.execute(request);
            //HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

    @FXML
    private void connectSystems(ActionEvent actionEvent) {
        boolean res;
        ObservableList<String> connectSystems = systemsAvailable.getTargetItems();
        for(String system : connectSystems){
           res = addExternalSystem(system);
           if(!res){
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setContentText("Something went wrong.\nPlease try again.");
               alert.show();
               return;
           }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("All systems were added successfully.");
        alert.show();
    }
}
