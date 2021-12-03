package org.override.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.override.AcademicResultsApplication;
import org.override.models.TermResult;
import org.override.services.TermResultService;
import org.override.utils.FakeData;
import org.override.utils.StringResources;
import org.override.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController extends Controller implements Initializable {
    TermResultService termResultService = TermResultService.getInstance();
    StringResources stringResources = StringResources.getInstance();
    static final String ACTIVE_CLASS = "active";

    public static Optional<TermResult> currentTermResult = Optional.empty();

    @FXML
    public TextField studentIdTextField;
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

    public void loadFXML(URL url) {
        try {
            FXMLLoader loader = new FXMLLoader(url);
            mainBorderPane.setCenter(loader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void loadTermResult(ActionEvent e) {
        if (studentIdTextField.getText() == null || studentIdTextField.getText().isEmpty()) {
            Utils.showAlert(stringResources.invalidInput(), "", "You must fill the input");
            return;
        }
        currentTermResult = Optional.ofNullable(termResultService.getTermResult(studentIdTextField.getText()));
        loadFXML(AcademicResultsApplication.class.getResource(APP_CONFIG.scoreView()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
