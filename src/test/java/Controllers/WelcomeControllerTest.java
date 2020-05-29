package Controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class WelcomeControllerTest extends MainTest {

    String userNameTextFieldID = "#txtUsername";
    String passwordTextFieldID = "#txtPassword";
    String btnSignupID = "#btnSignup";
    String btnSignInID = "#btnSignin";
    String btnGuestID = "#btnGuest";
    String loggedUserNameTextID = "#userName";

    @Test
    public void handleLoginSuccess() {
        sleep(5000);
        clickOn(userNameTextFieldID);
        write("a");
        clickOn(passwordTextFieldID);
        write("a");
        clickOn(btnSignInID);
        //im checking the JFXTextField that contains the the user name in the main screen is equal to the logged user. if it is success its must contain the user name
        JFXTextField userName = find(loggedUserNameTextID);
        assertEquals("a", userName.getText());
    }

    @Test
    public void handleLoginFail() {
        sleep(5000);
        clickOn(userNameTextFieldID);
        write("a");
        clickOn(passwordTextFieldID);
        write("b");
        clickOn(btnSignInID);
        //im checking that the button sign in is not nut null. if the log in was successful, then i will be in the main screen. i checking that i stay at welcome screen.
        Button userName = find(btnSignInID);
        assertNotNull(userName);
    }

    @Test
    public void handleGuestLogin() {
        sleep(5000);
        clickOn(btnGuestID);
        JFXTextField userName = find(loggedUserNameTextID);

        assertEquals("Guest", userName.getText());
    }

    @Test
    public void handSignupFail() {
        sleep(5000);
        clickOn(btnSignupID);
        Button signin = find("#submitButton");
        assertNotNull(signin);
    }

    /*
    @Test
    public void handSignupFail() {
        sleep(5000);
        clickOn(btnSignupID);
        clickOn("#userNameField");
        write("test");
        clickOn("#passwordField");
        write("test");
        clickOn("#fullNameField");
        write("test test");
        clickOn("#emailField");
        write("test@test.com");
        clickOn("#occupation");
        ComboBox occupation = find("#occupation");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        //occupation.setValue("FAN");
        Button signin = find(btnSignInID);
        assertNotNull(signin);
    }
     */
}