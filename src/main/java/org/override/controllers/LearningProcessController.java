package org.override.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import org.override.models.TermResult;
import org.override.utils.FakeData;

import java.net.URL;
import java.util.ResourceBundle;

public class LearningProcessController implements Initializable {
    final String SERIES_TEMPLATE = "HK %s - %s";
    final String AVG_GPA_SCORE = "Trung bình GPA tích lũy";
    final String AVG_GPA_TERM_SCORE = "Trung bình GPA học kỳ";
    final String AVG_SCORE = "Trung bình tích lũy";
    final String AVG_TERM_SCORE = "Trung bình học kỳ";

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
        TermResult termResult = FakeData.getTermResult(MainController.currentStudenId);
        setUpLineChart(learningProcessLC, termResult);
    }

    @FXML
    public void toggleLine(ActionEvent e) {
        CheckBox cb = (CheckBox) e.getSource();
        switch (cb.getId()) {
            case "avgGPAScoreCheckBox" -> System.out.println("2");
            case "avgGPATermScoreCheckBox" -> System.out.println("1");
        }
//        System.out.println(
//                cb.isSelected()
//        );
        System.out.println(
                cb == avgGPAScoreCheckBox
        );
    }

    public void setUpLineChart(LineChart<String, Number> lineChart, TermResult termResult) {


        gpaScoreSeries = new XYChart.Series<>();
        gpaScoreSeries.setName(AVG_GPA_SCORE);
        gpaScoreSeries.getData().addAll(
                termResult.termResultItems.stream()
                        .map(i -> new XYChart.Data<>(
                                String.format(SERIES_TEMPLATE, i.term, i.year), i.termScoreSummary.avgGPAScore)
                        ).toArray(XYChart.Data[]::new)
        );

        gpaTermScoreSeries = new XYChart.Series<>();
        gpaTermScoreSeries.setName(AVG_GPA_TERM_SCORE);
        gpaTermScoreSeries.getData().addAll(
                termResult.termResultItems.stream()
                        .map(i -> new XYChart.Data<>(
                                String.format(SERIES_TEMPLATE, i.term, i.year), i.termScoreSummary.avgGPATermScore
                        )).toArray(XYChart.Data[]::new)
        );

        scoreSeries = new XYChart.Series<>();
        scoreSeries.setName(AVG_SCORE);
        scoreSeries.getData().addAll(
                termResult.termResultItems.stream()
                        .map(i -> new XYChart.Data<>(
                                String.format(SERIES_TEMPLATE, i.term, i.year), i.termScoreSummary.avgScore
                        )).toArray(XYChart.Data[]::new)
        );

        termScoreSeries = new XYChart.Series<>();
        termScoreSeries.setName(AVG_TERM_SCORE);
        termScoreSeries.getData().addAll(
                termResult.termResultItems.stream()
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
