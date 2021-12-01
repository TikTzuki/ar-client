package org.override.core.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;
import org.override.AcademicResultsApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Log4j2
public class Appconfig extends Properties {
    private static final Appconfig INSTANCE = new Appconfig();

    public static Appconfig getInstance() {
        return INSTANCE;
    }

    static {
        String rootPath = AcademicResultsApplication.class.getResource("").getPath();
        String appConfigPath = rootPath + "configurations.xml";
        try {
            INSTANCE.loadFromXML(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getPort() {
        return Integer.valueOf(getProperty("port"));
    }

    public String getHost() {
        return getProperty("host");
    }

    public String mainView() {
        return getProperty("main-view");
    }

    public String scoreView() {
        return getProperty("score-view");
    }

    public String rankingView() {
        return getProperty("ranking-view");
    }
}
