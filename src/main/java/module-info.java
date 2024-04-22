module nalu.gami2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javax.mail.api;


    opens nalu.gami2 to javafx.fxml;
    opens entity to javafx.base;
    exports nalu.gami2;
}