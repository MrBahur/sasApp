package com.SAS.ClientApp.Controllers.wizard;

import com.SAS.ClientApp.Controllers.MainController;
import com.SAS.ClientApp.Controllers.Vista.VistaNavigator;
import com.SAS.ClientApp.Controllers.alerts.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.SAS.ClientApp.Controllers.wizard.FormController.isAdminCreated;


public class WizardController implements Initializable {

    private static FXMLLoader loader;
    private static Pane mainPane = null;
    private static int wizardPage = 1;


    @FXML private AnchorPane vistaHolder;
    @FXML private Button exitBtn;
    @FXML private Button nextBtn;
    @FXML private Button prevBtn;
    @FXML private Button finishBtn;



    /**
     * Replaces the vista displayed in the vista holder with a new vista.
     *
     * @param node the vista node to be swapped in.
     */
    public void setVista(Node node) {
        vistaHolder.getChildren().setAll(node);
    }

    private void changePage(){
        loader = new FXMLLoader();
        mainPane = null;
        try {
            String path = "/fxml/wizard/wizard" + wizardPage + ".fxml";
            mainPane = (Pane) loader.load(getClass().getResourceAsStream(path));
        } catch (IOException e) {
        }
        vistaHolder.getChildren().setAll(mainPane);
    }

    public void exitWizard(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void nextPage(ActionEvent actionEvent) {
        prevBtn.setDisable(false);
        if(wizardPage == 3 && isAdminCreated == false){
            Window owner = nextBtn.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please add a user admin to the system.");
            return;
        }
        wizardPage++;
        if(wizardPage == 4 ){
            exitBtn.setVisible(false);
            prevBtn.setVisible(false);
            nextBtn.setVisible(false);
            finishBtn.setVisible(true);
        }
        changePage();

    }

    public void previousPage(ActionEvent actionEvent) {
        wizardPage--;
        if(wizardPage == 1){
            prevBtn.setDisable(true);
        }
        changePage();
    }

    //public List<String> get

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prevBtn.setDisable(true);
        finishBtn.setVisible(false);
        changePage();


    }

    public void finishWizard(ActionEvent actionEvent) {
        activateSystem();
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        Pane mainPane = null;
        try {
            mainPane = (Pane) loader.load(getClass().getResourceAsStream(VistaNavigator.WELCOME));
        } catch (IOException e) {
        }
        stage.close();
        Scene scene = new Scene(mainPane);
        stage.setTitle("DeYMCA");
        stage.setScene(scene);
        stage.show();
    }

    private void activateSystem() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request;
        CloseableHttpResponse response = null;
        try {
            request = new HttpPost(MainController.serverURL + "/system/activate_system");

            JSONObject json = new JSONObject();
            json.put("system_status", "1");

            //create the request
            StringEntity stringEntity = new StringEntity(json.toString());
            request.getRequestLine();
            request.setEntity(stringEntity);
            request.addHeader("Content-Type", "application/json");
            response = httpClient.execute(request);
            //HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return;
            }
        } catch (Exception e) {
            return;
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (Exception e) {
                return;
            }
            return;
        }
    }
}
