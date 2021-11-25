package org.override.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import org.override.AcademicResultsApplication;
import org.override.core.models.HyperBody;
import org.override.models.StudentSummary;
import org.override.models.TermResult;
import org.override.utils.FakeData;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RankingController extends Controller implements Initializable {

    @FXML
    TableView<StudentSummary> rankTable;

    @FXML
    CheckBox academicYearCheckBox;

    @FXML
    CheckBox specialityCheckBox;

    @FXML
    CheckBox departmentCheckBox;

    @FXML
    Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<StudentSummary> studentSummaries = List.of(
                FakeData.getTermResult().studentSummary,
                FakeData.getTermResult1().studentSummary
        );
        setUpRankTable(studentSummaries);
    }

    private void setUpRankTable(List<StudentSummary> studentSummaries) {
        TableColumn[] columns = List.of(
                        "Mã sinh viên",
                        "Tên sinh viên",
                        "Phái",
                        "Nơi sinh",
                        "Lớp",
                        "Ngành"
                ).stream().map(TableColumn::new)
                .toArray(TableColumn[]::new);
        String[] properties = new String[]{
                "id",
                "name",
                "gender",
                "placeOfBirth",
                "classes",
                "subject"
        };
        for (int i = 0; i < columns.length; i++) {
            columns[i].setCellValueFactory(new PropertyValueFactory<>(properties[i]));
        }

        TableColumn actionCol = new TableColumn("Detail");
        Callback<TableColumn, TableCell> cellFactory = new Callback<>() {
            @Override
            public TableCell call(TableColumn param) {
                TableCell<StudentSummary, String> cell = new TableCell<>() {
                    final Button btn = new Button("Detail");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(e -> {
                                StudentSummary studentSummary = getTableView().getItems().get(getIndex());
                                System.out.println(studentSummary);

                                FXMLLoader loader = getLoder("main-view.fxml");
                                try {
                                    AcademicResultsApplication.scene.setRoot(loader.load());
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                MainController controller = (MainController) loader.getController();

                                controller.studentIdTextField.setText(studentSummary.id);

                                controller.loadFXML(AcademicResultsApplication.class.getResource("view-score.fxml"));
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
        rankTable.getItems().addAll(studentSummaries);
    }
}
