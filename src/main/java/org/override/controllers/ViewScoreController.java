package org.override.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.override.models.StudentSummary;
import org.override.models.TermResult;
import org.override.models.TermScoreItem;
import org.override.models.TermScoreSummary;

import java.net.URL;
import java.util.*;

public class ViewScoreController implements Initializable {

    @FXML
    VBox vBoxResult;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TermResult rs = fakeData();
        for (TermResult.TermResultItem termResultItem : rs.termResultItems) {
            Accordion accordion = new Accordion();
            accordion.getPanes().add(setUpTermResultItemTitlePane(termResultItem));
            vBoxResult.getChildren().add(accordion);
        }
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
        String[] termScoreLabels = new String[]{
                "Điểm trung bình học kỳ hệ 10/100:",
                "Điểm trung bình học kỳ hệ 4:",
                "Điểm trung bình tích lũy:",
                "Điểm trung bình tích lũy (hệ 4):",
                "Số tín chỉ đạt:",
                "Số tín chỉ tích lũy:"
        };
        String[] termScoreValue = new String[]{
                termScoreSummary.avgTermScore.toString(),
                termScoreSummary.avgGPATermScore.toString(),
                termScoreSummary.avgScore.toString(),
                termScoreSummary.avgGPAScore.toString(),
                termScoreSummary.creditsTermCount.toString(),
                termScoreSummary.creditsCount.toString()
        };
        for (String s : termScoreLabels) {
            termScoreSummaryPane.addColumn(0, new Label(s));
        }
        for (String s : termScoreValue) {
            termScoreSummaryPane.addColumn(1, new Label(s));
        }

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

    private TermResult fakeData() {
        return new TermResult(
                new StudentSummary("123", "name", "gender", "123 Ng", "2020-10-30", "DTC", "CNTT"),
                List.of(
                        new TermResult.TermResultItem(
                                1,
                                "2021-2022",
                                List.of(new TermScoreItem("1", "môn 1", 1, 1D, 1D, 1D, 1D, 1D, 1D, "a", "b", 4D, "Đ")),
                                new TermScoreSummary(3D, 4D, 3D, 4D, 20, 120)
                        )
                )
        );
    }
}
