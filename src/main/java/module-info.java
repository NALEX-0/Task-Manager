module media_2024 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.datatype.jsr310;


    opens media_2024 to com.fasterxml.jackson.databind, javafx.fxml;
    exports media_2024;
}
