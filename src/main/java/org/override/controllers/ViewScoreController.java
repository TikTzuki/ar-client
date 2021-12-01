package org.override.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.override.AcademicResultsApplication;
import org.override.models.StudentSummary;
import org.override.models.TermResult;
import org.override.models.TermScoreItem;
import org.override.models.TermScoreSummary;
import org.override.utils.FakeData;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

public class ViewScoreController extends Controller implements Initializable {
    @FXML
    GridPane studentSummaryPane;

    @FXML
    VBox vBoxResult;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TermResult rs = FakeData.getTermResult(MainController.currentStudenId);
//        SUMMARY

        setUpStudenSummaryPane(studentSummaryPane, rs.studentSummary);
//        SCORE
        for (TermResult.TermResultItem termResultItem : rs.termResultItems) {
            Accordion accordion = new Accordion();
            accordion.getPanes().add(setUpTermResultItemTitlePane(termResultItem));
            vBoxResult.getChildren().add(accordion);
        }

    }

    private void setUpStudenSummaryPane(GridPane studentSummaryPane, StudentSummary studentSummary) {
        studentSummaryPane.setPadding(new Insets(20));
        studentSummaryPane.setHgap(25);
        studentSummaryPane.setVgap(15);

        Label[] values = Stream.of(
                studentSummary.id,
                studentSummary.name,
                studentSummary.gender,
                studentSummary.placeOfBirth,
                studentSummary.classes,
                studentSummary.subject
        ).map(Label::new).toArray(Label[]::new);

        Label[] labels = Stream.of(
                "Mã sinh viên",
                "Tên sinh viên",
                "Phái",
                "Nơi sinh",
                "Lớp",
                "Ngành"
        ).map(Label::new).toArray(Label[]::new);

        studentSummaryPane.addColumn(0, labels);
        studentSummaryPane.addColumn(1, values);
    }

    private TitledPane setUpTermResultItemTitlePane(TermResult.TermResultItem termResultItem) {
        GridPane termScoreSummaryPane = setUpTermResultPane(termResultItem);
        return new TitledPane(String.format("Học kỳ %s - Năm học %s", termResultItem.term, termResultItem.year), termScoreSummaryPane);
    }

    private GridPane setUpTermResultPane(TermResult.TermResultItem termResultItem) {
        TermScoreSummary termScoreSummary = termResultItem.termScoreSummary;
        GridPane termScoreSummaryPane = new GridPane();
        termScoreSummaryPane.setPadding(new Insets(20));
        termScoreSummaryPane.setHgap(25);
        termScoreSummaryPane.setVgap(15);

//        TABLE
        TableView<TermScoreItem> table = setUpTableView(termResultItem.termScoreItems);
        termScoreSummaryPane.add(table, 0, 0, 2, 1);
//        CONCLUSION

        Label[] termScoreLabels = (Label[]) Stream.of(
                "Điểm trung bình học kỳ hệ 10/100:",
                "Điểm trung bình học kỳ hệ 4:",
                "Điểm trung bình tích lũy:",
                "Điểm trung bình tích lũy (hệ 4):",
                "Số tín chỉ đạt:",
                "Số tín chỉ tích lũy:"
        ).map(Label::new).toArray(Label[]::new);

        Label[] termScoreValues = (Label[]) Stream.of(
                termScoreSummary.avgTermScore.toString(),
                termScoreSummary.avgGPATermScore.toString(),
                termScoreSummary.avgScore.toString(),
                termScoreSummary.avgGPAScore.toString(),
                termScoreSummary.creditsTermCount.toString(),
                termScoreSummary.creditsCount.toString()
        ).map(Label::new).toArray(Label[]::new);

        termScoreSummaryPane.addColumn(0, termScoreLabels);
        termScoreSummaryPane.addColumn(1, termScoreValues);

        return termScoreSummaryPane;
    }

    private TableView<TermScoreItem> setUpTableView(List<TermScoreItem> termScoreItems) {
        TableView<TermScoreItem> table = new TableView<>();
        TableColumn<TermScoreItem, String> subjectIdCol = new TableColumn<>("Mã Môn");
        TableColumn<TermScoreItem, String> subjectNameCol = new TableColumn<>("Tên Môn");
        TableColumn<TermScoreItem, Integer> creditsCountCol = new TableColumn<>("TC");
        TableColumn<TermScoreItem, Double> examPercentCol = new TableColumn<>("% KT");
        TableColumn<TermScoreItem, Double> finalExamPercentCol = new TableColumn<>("% Thi");
        TableColumn<TermScoreItem, Double> examScoreCol = new TableColumn<>("Kiểm tra");
        TableColumn<TermScoreItem, Double> finalExamScoreCol = new TableColumn<>("Thi L1");
        TableColumn<TermScoreItem, Double> termScoreFirstCol = new TableColumn<>("TK1(10)");
        TableColumn<TermScoreItem, Double> termScoreSecondCol = new TableColumn<>("TK(10)");
        TableColumn<TermScoreItem, String> gpaFirstCol = new TableColumn<>("TK1(CH)");
        TableColumn<TermScoreItem, String> gpaSecondCol = new TableColumn<>("TK(CH)");
        TableColumn<TermScoreItem, Double> gpaCol = new TableColumn<>("TK(4)");
        TableColumn<TermScoreItem, String> resultCol = new TableColumn<>("KQ");

        List<TableColumn> columns = List.of(
                subjectIdCol,
                subjectNameCol,
                creditsCountCol,
                examPercentCol,
                finalExamPercentCol,
                examScoreCol,
                finalExamScoreCol,
                termScoreFirstCol,
                termScoreSecondCol,
                gpaFirstCol,
                gpaSecondCol,
                gpaCol,
                resultCol
        );
        String[] porpteries = new String[]{
                "subjectId",
                "subjectName",
                "creditsCount",
                "examPercent",
                "finalExamPercent",
                "examScore",
                "finalExamScore",
                "termScoreFirst",
                "termScoreSecond",
                "gpaFirst",
                "gpaSecond",
                "gpa",
                "result"
        };
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setCellValueFactory(new PropertyValueFactory<>(porpteries[i]));
        }

        table.getColumns().addAll(subjectIdCol, subjectNameCol, creditsCountCol, examPercentCol, finalExamPercentCol, examScoreCol, finalExamScoreCol, termScoreFirstCol, termScoreSecondCol, gpaFirstCol, gpaSecondCol, gpaCol, resultCol);
//        SETUP DATA
        table.getItems().addAll(termScoreItems);
        return table;
    }


}
