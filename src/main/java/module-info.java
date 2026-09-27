module it.volta.calcolatore {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.volta.calcolatore to javafx.fxml;
    exports it.volta.calcolatore;
}