package springApp;

import com.SAS.Presentation.Controllers.MainController;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping()
@RestController
public class ClientApiController {

    private MainController mainController;

    public ClientApiController(){
        this.mainController = new MainController();
    }

    @PostMapping(value ="/getNotification")
    public String  getNotification(@RequestBody String details) {
        JSONObject json = new JSONObject(details);
        String message = json.get("message").toString();
        mainController.addNotification(message);
        return "sent";
    }

}
