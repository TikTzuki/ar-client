package org.override.utils;

import lombok.extern.log4j.Log4j2;
import org.override.AcademicResultsApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Log4j2
public class StringResources extends Properties {

    private static final StringResources INSTANCE = new StringResources();

    public static StringResources getInstance() {
        return INSTANCE;
    }

    static {
        String rootPath = AcademicResultsApplication.class.getResource("").getPath();
        String appConfigPath = rootPath + "string-resources.xml";
        try {
            INSTANCE.loadFromXML(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String requestFailed() {
        String REQUEST_FAILED = "request-failed";
        return getProperty(REQUEST_FAILED);
    }

}
