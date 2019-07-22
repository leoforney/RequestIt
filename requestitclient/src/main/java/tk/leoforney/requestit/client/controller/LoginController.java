package tk.leoforney.requestit.client.controller;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import tk.leoforney.requestit.client.Application;
import tk.leoforney.requestit.client.service.WebsocketService;

import java.io.IOException;
import java.util.logging.Logger;

public class LoginController {

    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @FXML
    JFXTextField emailField;

    @FXML
    JFXPasswordField passwordField;

    @FXML
    JFXButton submitButton;

    private WebsocketService websocketService;

    private Gson gson;

    public LoginController() {
        websocketService = (WebsocketService) Application.getSpringContext().getAutowireCapableBeanFactory().getBean("websocketService");
        gson = new Gson();
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) throws IOException {
        if (!emailField.getText().equals("") && !passwordField.getText().equals("")) {
            logger.info(emailField.getText() + " - " + passwordField.getText());

            HttpResponse<String> response = Unirest.post("http://localhost:8080/login")
                    .field("email", emailField.getText())
                    .field("password", passwordField.getText())
                    .asString();

            HttpResponse<JsonNode> userResponse = Unirest.get("http://localhost:8080/user")
                    .asJson();

            boolean authenticated = userResponse.getBody().getObject().getBoolean("authenticated");

            logger.info("Response code: " + authenticated);

            if (authenticated) {
                websocketService.start();
            }

        }
    }

}
