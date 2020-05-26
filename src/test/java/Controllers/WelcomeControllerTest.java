package Controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Button;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class WelcomeControllerTest extends MainTest {

    String userNameTextFieldID = "#txtUsername";
    String passwordTextFieldID = "#txtPassword";
    String btnSignInID = "#btnSignin";
    String loggedUserNameTextID = "#userName";

    @Test
    public void handleRegisterSuccess() {
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
    public void handleRegisterFail() {
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
    }

    @Test
    public void handleLogin() {
    }
}