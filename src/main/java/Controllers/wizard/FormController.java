package Controllers.wizard;
import Controllers.MainController;
import Controllers.alerts.AlertHelper;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormController {

    private String fullName;
    private String userName;
    private String password;
    private String email;

    public static boolean isAdminCreated = false;


    @FXML private JFXTextField nameField;
    @FXML private JFXTextField emailField;
    @FXML private JFXTextField userNameField;
    @FXML private JFXPasswordField passwordField;
    @FXML private Button submitButton;
    @FXML private RequiredFieldValidator nameValidator;
    @FXML private RequiredFieldValidator userNameValidator;
    @FXML private RequiredFieldValidator passwordValidator;
    @FXML private RequiredFieldValidator emailValidator;

    @FXML
    public void handleSubmitButtonAction(ActionEvent event) {
        Window owner = submitButton.getScene().getWindow();
        fullName = nameField.getText() ;
        userName = userNameField.getText();
        password = passwordField.getText();
        email = emailField.getText();
        if(formValidator(owner)){
            addAdminToDB(fullName, userName, password, email);
            AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Registration Successful!",
                    "Welcome " + nameField.getText());
            isAdminCreated = true;
        }
    }

    private boolean addAdminToDB(String fullName, String userName, String password, String email) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request;
        CloseableHttpResponse response = null;
        try {
            request = new HttpPost(MainController.serverURL + "/system/add_system_admin");

            JSONObject json = new JSONObject();
            json.put("fullName", fullName);
            json.put("userName", userName);
            json.put("password", password);
            json.put("email", email);

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

    private boolean formValidator(Window owner) {
        if (fullName.isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your full name name");
            return false;
        }
        if (userName.isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a user name");
            return false;
        }
        if (password.isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return false;
        }
        if (email.isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email");
            return false;
        }

        if (!validateEmail(email)) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Your email is not valid. \nPlease try again.");
            return false;
        }

        return true;
    }


    private boolean validateEmail(String email) {
        String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
