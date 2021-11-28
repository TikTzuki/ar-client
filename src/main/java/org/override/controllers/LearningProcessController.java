package org.override.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainController.currentTermResult.ifPresent(
                termResult -> setUpLineChart(learningProcessLC, termResult)
        );
    }

    private void setUpLineChart(LineChart<String, Number> lineChart, TermResult termResult) {


        XYChart.Series gpaScoreSeries = new XYChart.Series<>();
        gpaScoreSeries.setName(AVG_GPA_SCORE);
        gpaScoreSeries.getData().addAll(
                termResult.termResultItems.stream()
                        .map(i -> new XYChart.Data<>(
                                String.format(SERIES_TEMPLATE, i.term, i.year), i.termScoreSummary.avgGPAScore)
                        ).toArray(XYChart.Data[]::new)
        );

        XYChart.Series gpaTermScoreSeries = new XYChart.Series<>();
        gpaTermScoreSeries.setName(AVG_GPA_TERM_SCORE);
        gpaTermScoreSeries.getData().addAll(
                termResult.termResultItems.stream()
                        .map(i -> new XYChart.Data<>(
                                String.format(SERIES_TEMPLATE, i.term, i.year), i.termScoreSummary.avgGPATermScore
                        )).toArray(XYChart.Data[]::new)
        );

        XYChart.Series scoreSeries = new XYChart.Series<>();
        scoreSeries.setName(AVG_SCORE);
        scoreSeries.getData().addAll(
                termResult.termResultItems.stream()
                        .map(i -> new XYChart.Data<>(
                                String.format(SERIES_TEMPLATE, i.term, i.year), i.termScoreSummary.avgScore
                        )).toArray(XYChart.Data[]::new)
        );

        XYChart.Series termScoreSeries = new XYChart.Series<>();
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
