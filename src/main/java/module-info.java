module media_2024 {
    requires javafx.controls;
    requires javafx.fxml;

    opens media_2024 to javafx.fxml;
    exports media_2024;
}
