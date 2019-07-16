package tk.leoforney.requestit.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import tk.leoforney.requestit.client.controller.ScreenController;
import tk.leoforney.requestit.client.service.WebsocketService;

@SpringBootApplication
public class Application extends javafx.application.Application {

    private ConfigurableApplicationContext springContext;

    WebsocketService websocketService;

    public static void main(String[] args) {
        launch(args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

        };
    }

    @Override
    public void start(Stage stage) throws Exception {
        websocketService = (WebsocketService) springContext.getAutowireCapableBeanFactory().getBean("websocketService");
        stage.setTitle("RequestIt");
        stage.getIcons().add(new Image(new ClassPathResource("web_hi_res_512.png").getURL().toString()));

        ScreenController controller = ScreenController.getInstance(stage);
        controller.addScreen("login");

        controller.activate("login");
    }

    @Override
    public void init() throws Exception {
        springContext = new SpringApplicationBuilder(Application.class)
                .web(WebApplicationType.NONE)
                .run();

    }

    @Override
    public void stop() {
        springContext.stop();
    }
}
