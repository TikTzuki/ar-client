package org.override.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import lombok.extern.log4j.Log4j2;
import org.override.AcademicResultsApplication;
import org.override.models.PagingModel;
import org.override.models.StudentModel;
import org.override.services.RankingService;
import org.override.utils.StringResources;
import org.override.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Log4j2
public class RankingController extends Controller implements Initializable {
    StringResources stringResources = StringResources.getInstance();
    RankingService rankingService = RankingService.getInstance();

    @FXML
    TableView<StudentModel> rankTable;

    @FXML
    CheckBox courseCheckBox;

    @FXML
    CheckBox specialityCheckBox;

    @FXML
    CheckBox subjectCheckBox;

    @FXML
    Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpRankTable();
        pushDataRankTable();
    }

    @FXML
    public void handleSubmitRanking(ActionEvent e) {
        pushDataRankTable();
    }

    private void setUpRankTable() {
        TableColumn[] columns = List.of(
                        "Mã sinh viên",
                        "Tên sinh viên",
//                        "Phái",
//                        "Nơi sinh",
                        "Điểm trung bình",
                        "Khóa",
                        "Khoa",
                        "Ngành"
                ).stream().map(TableColumn::new)
                .toArray(TableColumn[]::new);
        String[] properties = new String[]{
                "studentId",
                "name",
                "avgScore",
                "course",
                "subject",
                "speciality"
        };
        for (int i = 0; i < columns.length; i++) {
            columns[i].setCellValueFactory(new PropertyValueFactory<>(properties[i]));
        }

        TableColumn actionCol = new TableColumn("Detail");
        Callback<TableColumn, TableCell> cellFactory = new Callback<>() {
            @Override
            public TableCell call(TableColumn param) {
                TableCell<StudentModel, String> cell = new TableCell<>() {
                    final Button btn = new Button("Detail");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(e -> {
                                StudentModel student = getTableView().getItems().get(getIndex());

                                FXMLLoader loader = getLoader(APP_CONFIG.mainView());
                                try {
                                    AcademicResultsApplication.scene.setRoot(loader.load());
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                MainController controller = (MainController) loader.getController();

                                controller.studentIdTextField.setText(student.studentId);

                                controller.loadFXML(AcademicResultsApplication.class.getResource(APP_CONFIG.scoreView()));
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        actionCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        actionCol.setCellFactory(cellFactory);
        rankTable.getColumns().addAll(columns);
        rankTable.getColumns().add(actionCol);
    }

    private void pushDataRankTable() {
//        if (!courseCheckBox.isSelected() && !specialityCheckBox.isSelected() && !subjectCheckBox.isSelected()) {
//            Utils.showAlert(stringResources.requestFailed(), "", "You must select at least one of them: {course, subject, speciality}");
//            return;
//        }
        MainController.currentTermResult.ifPresent(t -> {
            new Thread() {
                @Override
                public void run() {
                    AcademicResultsApplication.scene.setCursor(Cursor.WAIT);
                    PagingModel<StudentModel> studentPage = rankingService.getRanking(
                            t.studentSummary.id,
                            courseCheckBox.isSelected(),
                            subjectCheckBox.isSelected(),
                            specialityCheckBox.isSelected()
                    );
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            AcademicResultsApplication.scene.setCursor(Cursor.DEFAULT);
                            if (studentPage != null) {
                                rankTable.getItems().clear();
                                rankTable.getItems().addAll(studentPage.getItems());
                                Utils.autoResizeColumns(rankTable, 0);
                                Utils.autoResizeHeight(rankTable);
                            }
                        }
                    });
                }
            }.start();
        });
    }
}
