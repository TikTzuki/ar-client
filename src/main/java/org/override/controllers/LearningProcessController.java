package org.override.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.ResourceBundle;

public class LearningProcessController implements Initializable {
    @FXML
    private LineChart<String, Number> learningProcessLC;

    @FXML
    private GridPane gridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpLineChart(learningProcessLC);

//        final NumberAxis xAxis = new NumberAxis();
//        final NumberAxis yAxis = new NumberAxis();
//        xAxis.setLabel("Number of Month");
//        learningProcessLC = new LineChart<>(xAxis, yAxis);
//        setUpLineChart(learningProcessLC);
    }

    private LineChart<String, Number> setUpLineChart(LineChart<String, Number> lineChart) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
//        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data
        series.getData().add(new XYChart.Data("a", 23));
        series.getData().add(new XYChart.Data("b", 14));
        series.getData().add(new XYChart.Data("c", 15));
        series.getData().add(new XYChart.Data("d", 24));

        lineChart.getData().add(series);

        return lineChart;
    }
}
