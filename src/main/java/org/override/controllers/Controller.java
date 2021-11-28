package org.override.controllers;

import javafx.fxml.FXMLLoader;
import org.override.AcademicResultsApplication;
import org.override.core.configs.Appconfig;

import java.io.IOException;

public class Controller {
    Appconfig APP_CONFIG = Appconfig.getInstance();

    protected FXMLLoader getLoader(String view) {
        return new FXMLLoader(AcademicResultsApplication.class.getResource(view));
    }

    protected <T> T getComp(String view) {
        FXMLLoader loader = getLoader(view);
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
