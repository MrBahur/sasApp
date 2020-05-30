package Controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.input.MouseButton;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddPoliciesControllerTest extends MainTest {
    String userNameTextFieldID = "#txtUsername";
    String passwordTextFieldID = "#txtPassword";
    String btnSignInID = "#btnSignin";
    String btnLeagues = "#leagues";
    String btnAddPolicies = "#add_policies_to_league_btn";
    String leagueName = "#league_name";
    String year = "#season_year";
    String f1 = "#f1";
    String f3 = "#f3";
    String f6 = "#f6";
    String start = "#add_policies_btn";

    @Test
    public void handleAddPolicies() {
        sleep(5000);
        clickOn(userNameTextFieldID);
        write("yaa");
        clickOn(passwordTextFieldID);
        write("123");
        clickOn(btnSignInID);
        clickOn(btnLeagues);
        clickOn(btnAddPolicies);
        clickOn(leagueName);
        write("Ligat Haal");
        clickOn(year);
        write("2020");
        clickOn(f1);
        clickOn(f3);
        clickOn(f6);
        clickOn(start);
        assertNull(start);

    }

    @Test
    public void handleFailAddPolicies() {
        sleep(5000);
        clickOn(userNameTextFieldID);
        write("yaa");
        clickOn(passwordTextFieldID);
        write("123");
        clickOn(btnSignInID);
        clickOn(btnLeagues);
        clickOn(btnAddPolicies);
        clickOn(leagueName);
        write("fail");
        clickOn(year);
        write("fail");
        clickOn(f1);
        clickOn(f3);
        clickOn(f6);
        clickOn(start);
        assertNotNull(start);

    }
}