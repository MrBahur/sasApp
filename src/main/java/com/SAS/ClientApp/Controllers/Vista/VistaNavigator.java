package com.SAS.ClientApp.Controllers.Vista;

import com.SAS.ClientApp.Controllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Utility class for controlling navigation between vistas.
 * <p>
 * All methods on the navigator are static to facilitate
 * simple access from anywhere in the application.
 */
public class VistaNavigator {

    /**
     * Convenience constants for fxml layouts managed by the navigator.
     */
    public static final String PERSONAL_TEAM_OWNER = "/fxml/personalAreaTeamOwner.fxml";
    public static final String PERSONAL_REPRESENTATIVE = "/fxml/personalAreaRepresentative.fxml";
    public static final String PERSONAL_GENERAL = "/fxml/personalAreaGeneral.fxml";
    public static final String LEAGUES = "/fxml/leagues.fxml";
    public static final String TEAMS = "/fxml/teams.fxml";
    public static final String PLAYERS = "/fxml/players.fxml";
    public static final String ANIMATION = "/fxml/animation.fxml";
    public static final String WIZARDVIEW = "/fxml/wizard/wizardView.fxml";
    public static final String WELCOME = "/fxml/welcome.fxml";
    public static final String MAIN = "/fxml/main.fxml";
    public static final String GAMES = "/fxml/games.fxml";
    public static final String GAME = "/fxml/game.fxml";
    public static final String VISTA_1 = "/fxml/vista1.fxml";
    public static final String VISTA_2 = "/fxml/vista2.fxml";
    public static final String TEAM = "/fxml/team.fxml";
    public static final String REGISTER = "/fxml/register.fxml";
    public static final String OPENTEAM = "/fxml/openTeam.fxml";



    /**
     * The main application layout controller.
     */
    private static MainController mainController;

    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param mainController the main application layout controller.
     */
    public static void setMainController(MainController mainController) {
        VistaNavigator.mainController = mainController;
    }

    public static MainController getMainController() {
        return mainController;
    }

    /**
     * Loads the vista specified by the fxml file into the
     * vistaHolder pane of the main application layout.
     * <p>
     * Previously loaded vista for the same fxml file are not cached.
     * The fxml is loaded anew and a new vista node hierarchy generated
     * every time this method is invoked.
     * <p>
     * A more sophisticated load function could potentially add some
     * enhancements or optimizations, for example:
     * cache FXMLLoaders
     * cache loaded vista nodes, so they can be recalled or reused
     * allow a user to specify vista node reuse or new creation
     * allow back and forward history like a browser
     *
     * @param fxml the fxml file to be loaded.
     */
    public static void loadVista(String fxml) {
        try {
            mainController.setVista(FXMLLoader.load(VistaNavigator.class.getResource(fxml)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadVista(Pane pane) {
        try {
            mainController.setVista(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
