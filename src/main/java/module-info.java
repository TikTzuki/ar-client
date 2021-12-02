module org.override {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires static lombok;
    requires java.security.jgss;
    requires java.security.sasl;
    requires com.google.gson;
    requires org.apache.commons.codec;
    requires org.apache.commons.lang3;

    opens org.override to javafx.fxml;
    exports org.override;

    exports org.override.models;
    opens org.override.models to com.google.gson;

    exports org.override.controllers;
    opens org.override.controllers to javafx.fxml;

    exports org.override.core.models;

}