package org.override;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.override.services.UserService;

import java.net.URL;
import java.util.Objects;


public class AcademicResultsApplication extends Application {

    public static Scene scene;
    private final UserService userService = UserService.getInstance();

    @Override
    public void start(Stage stage) throws Exception {
        while(userService.getCurrentUser() == null)
            userService.requiredLogin();

        URL resource = getClass().getResource("main-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        BorderPane pane = fxmlLoader.load();
        scene = new Scene(pane);

        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/education/alarm-clock.png"))));
        stage.setTitle("Have a good day, sweetie!!!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        AcademicResultsApplication.launch(args);
    }
}