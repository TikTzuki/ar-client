package org.override.utils;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class Utils {

    public static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showAlertInPlatform(String title, String header, String content) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                showAlert(title, header, content);
            }
        });
    }

    public static double autoResizeColumns(TableView<?> table, double preWith) {
        //Set the right policy
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        for (TableColumn column : table.getColumns()) {
            //Minimal width = columnheader
            Text t = new Text(column.getText());
            double max = t.getLayoutBounds().getWidth();
            for (int i = 0; i < table.getItems().size(); i++) {
                //cell must not be empty
                if (column.getCellData(i) != null) {
                    t = new Text(column.getCellData(i).toString());
                    double calcwidth = t.getLayoutBounds().getWidth();
                    //remember new max-width
                    if (calcwidth > max) {
                        max = calcwidth;
                    }
                }
            }
            //set the new max-widht with some extra space
            preWith += (max + 10.0);
            column.setPrefWidth(max + 10.0d);
        }
        return preWith;
    }

    public static void autoResizeTableColumns(TableView<?> table) {
        double preWith = 0;
        preWith = autoResizeColumns(table, preWith);
        table.setPrefWidth(preWith + 2);
    }

    public static void autoResizeHeight(TableView<?> table) {
        table.setFixedCellSize(28);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(2)));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
    }
}
