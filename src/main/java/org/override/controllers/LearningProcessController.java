package org.override.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.override.AcademicResultsApplication;
import org.override.models.CreditModel;
import org.override.models.LearningProcessModel;
import org.override.models.TermResult;
import org.override.services.LearningProcessService;
import org.override.services.RankingService;
import org.override.utils.Utils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LearningProcessController implements Initializable {
    RankingService rankingService = RankingService.getInstance();
    LearningProcessService learningProcessService = LearningProcessService.getInstance();


    final String SERIES_TEMPLATE = "HK %s - %s";
    final String AVG_GPA_SCORE = "Trung bình GPA tích lũy";
    final String AVG_GPA_TERM_SCORE = "Trung bình GPA học kỳ";
    final String AVG_SCORE = "Trung bình tích lũy";
    final String AVG_TERM_SCORE = "Trung bình học kỳ";

    @FXML
    private Text percentProcessText;

    @FXML
    private TableView creditsTable;

    @FXML
    private LineChart<String, Number> learningProcessLC;

    XYChart.Series gpaScoreSeries;
    XYChart.Series gpaTermScoreSeries;
    XYChart.Series scoreSeries;
    XYChart.Series termScoreSeries;

    @FXML
    CheckBox avgGPATermScoreCheckBox;

    @FXML
    CheckBox avgGPAScoreCheckBox;

    @FXML
    CheckBox avgTermScoreCheckBox;

    @FXML
    CheckBox avgScoreCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            MainController.currentTermResult.ifPresent(
                    termResult -> {
                        setUpLineChart(learningProcessLC, termResult);
                        new Thread() {
                            @Override
                            public void run() {
                                AcademicResultsApplication.scene.setCursor(Cursor.WAIT);
                                LearningProcessModel learningProcess = learningProcessService.getProcess(
                                        termResult.studentSummary.id, false, true
                                );
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        AcademicResultsApplication.scene.setCursor(Cursor.DEFAULT);
                                        if (learningProcess != null) {
                                            percentProcessText.setText(
                                                    "%.2f %% - %s".formatted(learningProcess.learningProcessPercent, learningProcess.process)
                                            );
                                            setUpCreditsTable(creditsTable, learningProcess.credits);
                                        }
                                    }
                                });
                            }
                        }.start();
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpCreditsTable(TableView table, List<CreditModel> credits) {
        TableColumn[] columns = List.of(
                        "Mã môn học",
                        "Tên",
                        "Tín chỉ"
                ).stream().map(TableColumn::new)
                .toArray(TableColumn[]::new);
        String[] properties = new String[]{
                "subjectId",
                "subjectName",
                "creditsCount"
        };
        for (int i = 0; i < columns.length; i++) {
            columns[i].setCellValueFactory(new PropertyValueFactory<>(properties[i]));
        }

        table.getColumns().addAll(columns);
        table.getItems().addAll(credits);
        Utils.autoResizeTableColumns(table);
    }

    @FXML
    public void toggleLine(ActionEvent e) {
//        CheckBox cb = (CheckBox) e.getSource();
//        switch (cb.getId()) {
//            case "avgGPAScoreCheckBox" -> System.out.println("2");
//            case "avgGPATermScoreCheckBox" -> System.out.println("1");
//        }
//        System.out.println(
//                cb.isSelected()
//        );
    }

    public void setUpLineChart(LineChart<String, Number> lineChart, TermResult termResult) {


        gpaScoreSeries = new XYChart.Series<>();
        gpaScoreSeries.setName(AVG_GPA_SCORE);
        gpaScoreSeries.getData().addAll(
                termResult.termResultItems.stream()
                        .filter(
                                i -> i.termScoreSummary != null
                        )
                        .map(i -> new XYChart.Data<>(
                                String.format(SERIES_TEMPLATE, i.term, i.year), i.termScoreSummary.avgGPAScore)
                        ).toArray(XYChart.Data[]::new)
        );

        gpaTermScoreSeries = new XYChart.Series<>();
        gpaTermScoreSeries.setName(AVG_GPA_TERM_SCORE);
        gpaTermScoreSeries.getData().addAll(
                termResult.termResultItems.stream()
                        .filter(
                                i -> i.termScoreSummary != null
                        )
                        .map(i -> new XYChart.Data<>(
                                String.format(SERIES_TEMPLATE, i.term, i.year), i.termScoreSummary.avgGPATermScore)
                        ).toArray(XYChart.Data[]::new)
        );

        scoreSeries = new XYChart.Series<>();
        scoreSeries.setName(AVG_SCORE);
        scoreSeries.getData().addAll(
                termResult.termResultItems.stream()
                        .filter(
                                i -> i.termScoreSummary != null
                        )
                        .map(i -> new XYChart.Data<>(
                                String.format(SERIES_TEMPLATE, i.term, i.year), i.termScoreSummary.avgScore
                        )).toArray(XYChart.Data[]::new)
        );

        termScoreSeries = new XYChart.Series<>();
        termScoreSeries.setName(AVG_TERM_SCORE);
        termScoreSeries.getData().addAll(
                termResult.termResultItems.stream()
                        .filter(
                                i -> i.termScoreSummary != null
                        )
                        .map(i -> new XYChart.Data<>(
                                String.format(SERIES_TEMPLATE, i.term, i.year), i.termScoreSummary.avgTermScore
                        )).toArray(XYChart.Data[]::new)
        );


        lineChart.getData().addAll(
                gpaScoreSeries,
                gpaTermScoreSeries,
                scoreSeries,
                termScoreSeries
        );
    }


}
