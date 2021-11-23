package org.override.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.override.AcademicResultsApplication;

import java.io.IOException;
import java.net.URL;

public class MainController {
    static final String ACTIVE_CLASS = "active";
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Button viewScoreBtn;
    @FXML
    private Button viewProcessBtn;
    @FXML
    private Button viewRankingBtn;

    @FXML
    private void handleShowView(ActionEvent e) {
        viewScoreBtn.getStyleClass().remove(ACTIVE_CLASS);
        viewProcessBtn.getStyleClass().remove(ACTIVE_CLASS);
        viewRankingBtn.getStyleClass().remove(ACTIVE_CLASS);

        Button btn = (Button) e.getSource();
        btn.getStyleClass().add(ACTIVE_CLASS);
        String view = (String) btn.getUserData();
        loadFXML(AcademicResultsApplication.class.getResource(view));
    }

    private void loadFXML(URL url) {
        try {
            FXMLLoader loader = new FXMLLoader(url);
            mainBorderPane.setCenter(loader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
