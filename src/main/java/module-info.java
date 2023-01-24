module tse.fise2.image3.cardmatcher {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;
    requires opencv;

    requires java.desktop;
    requires org.apache.commons.io;
    requires java.datatransfer;

    opens tse.fise2.image3.cardmatcher to javafx.fxml;
    exports tse.fise2.image3.cardmatcher;
    exports tse.fise2.image3.cardmatcher.controller;
    opens tse.fise2.image3.cardmatcher.controller to javafx.fxml;
    exports tse.fise2.image3.cardmatcher.model;
    opens tse.fise2.image3.cardmatcher.model to javafx.fxml;
    opens tse.fise2.image3.cardmatcher.util to javafx.fxml;

}
