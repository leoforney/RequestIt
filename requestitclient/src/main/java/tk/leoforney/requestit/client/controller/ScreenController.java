package tk.leoforney.requestit.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Logger;

public class ScreenController {
    private HashMap<String, Pane> screenMap = new HashMap<>();
    private Scene main;

    private static final Logger logger = Logger.getLogger(ScreenController.class.getName());

    private static ScreenController instance;

    public static ScreenController getInstance(Scene main) {
        if (instance == null && main != null) {
            instance = new ScreenController(main);
        }
        return instance;
    }

    public static ScreenController getInstance(Stage mainStage) {
        if (instance == null && mainStage != null) {
            instance = new ScreenController(mainStage);
        }
        return instance;
    }

    private ScreenController(Scene main) {
        this.main = main;
    }

    private ScreenController(Stage mainStage) {
        main = new Scene(new VBox(), 800, 600);
        mainStage.setScene(main);
        mainStage.show();
    }

    public void addScreen(String name, Pane pane){
         screenMap.put(name, pane);
    }

    public void addScreen(String name, String fxmlName) {
        try {
            URL discoveredFxml = new ClassPathResource("fxml/" + fxmlName).getURL();
            logger.info(discoveredFxml.toString());
            Pane pane = FXMLLoader.load(discoveredFxml);
            addScreen(name, pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addScreen(String name) {
        addScreen(name, name + ".fxml");
    }

    public void removeScreen(String name){
        screenMap.remove(name);
    }

    public void activate(String name){
        main.setRoot(screenMap.get(name));
    }
}