package org.override.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import org.override.AcademicResultsApplication;

import java.io.IOException;

public class Controller {
    protected FXMLLoader getLoder(String view) {
        return new FXMLLoader(AcademicResultsApplication.class.getResource(view));
    }

    protected <T> T getComp(String view) {
        FXMLLoader loader = getLoder(view);
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected <T> T getController(String view) {
        FXMLLoader loader = new FXMLLoader(AcademicResultsApplication.class.getResource(view));
        try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return loader.getController();
    }
}
