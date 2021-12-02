package org.override.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import lombok.extern.log4j.Log4j2;
import org.override.AcademicResultsApplication;
import org.override.models.PagingModel;
import org.override.models.StudentModel;
import org.override.services.RankingService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Log4j2
public class RankingController extends Controller implements Initializable {
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
//        List<StudentSummary> students = List.of(
//                FakeData.getTermResult("123").studentSummary,
//                FakeData.getTermResult("456").studentSummary
//        );
        MainController.currentTermResult.ifPresent(t -> {
            System.out.println("is present");
            PagingModel<StudentModel> studentPage = rankingService.getRanking(
                    t.studentSummary.id,
                    courseCheckBox.isSelected(),
                    specialityCheckBox.isSelected(),
                    subjectCheckBox.isSelected()
            );
            if (studentPage != null) {
                System.out.println(studentPage.getItems().size());
                setUpRankTable(studentPage.getItems());
            }
        });
        System.out.println("set up done");
    }

    private void setUpRankTable(List<StudentModel> students) {
        TableColumn[] columns = List.of(
                        "Mã sinh viên",
//                        "Tên sinh viên",
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
                                System.out.println(student);

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

        actionCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        actionCol.setCellFactory(cellFactory);
        rankTable.getColumns().addAll(columns);
        rankTable.getColumns().add(actionCol);
        rankTable.getItems().addAll(students);
    }
}
