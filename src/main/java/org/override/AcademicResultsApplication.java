package org.override;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.override.core.configs.Appconfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;


public class AcademicResultsApplication extends Application {

    public static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        URL resouce = getClass().getResource("main-view.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(resouce);

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