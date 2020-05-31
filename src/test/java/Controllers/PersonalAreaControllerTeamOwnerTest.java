package Controllers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PersonalAreaControllerTeamOwnerTest extends MainTest{

    String editbtn = "#editbtn";
    String tabActions = "#tabActions";
    String teamNameTextID = "#newTeamName";
    String btnRegisterApply = "#registerbtn";
    String btnRegister = "#registerbtnOpen";


    @Test
    void registerTeamSuccess() {
        sleep(5000);
        clickOn(tabActions);
        clickOn(btnRegisterApply);
        clickOn(teamNameTextID);
        write("new team");
        clickOn(btnRegister);
        assertNull(tabActions); //since we are in a new scene
    }

    @Test
    void registerTeamFail() {
        sleep(5000);
        clickOn(tabActions);
        clickOn(btnRegisterApply);
        clickOn(teamNameTextID);
        write("new team");
        clickOn(btnRegister);
        assertNotNull(tabActions);
    }
}