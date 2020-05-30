package com.SAS.Presentation;


import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.SAS.Presentation.DeYMCAApplication.*;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    public static Boolean isSplashLoaded = false;

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
    }
}
