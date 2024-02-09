module application.ai {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens application.ai to javafx.fxml, javafx.base;
    exports application.ai;
}