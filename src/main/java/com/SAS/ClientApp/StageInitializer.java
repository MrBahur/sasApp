package com.SAS.ClientApp;


import com.SAS.ClientApp.Controllers.Vista.VistaNavigator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.SAS.ClientApp.DeYMCAApplication.*;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    public static Boolean isSplashLoaded = false;

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
    }
}
