package Controllers;

import Main.Main;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

public abstract class MainTest extends ApplicationTest {

    /**
     * This will start the application for the tests
     * @throws Exception
     */
    @Before
    public void setUpClass() throws Exception {
        ApplicationTest.launch(Main.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.show();
    }

    /**
     * This is s teardown functions the will release any unwanted "clicks" at the end of each test
     * @throws TimeoutException
     */
    @After
    public void afterEachTest() throws TimeoutException{
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    /**
     * with this function you return elements from your fxml. good for checking things.
     * @param query the fx:id of the element
     * @param <T>
     * @return
     */
    public <T extends Node> T find(String query){
        return (T) lookup(query).queryAll().iterator().next();
    }
}