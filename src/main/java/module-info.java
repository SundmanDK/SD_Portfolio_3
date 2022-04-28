module com.example.sd_portfolio_3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.sd_portfolio_3 to javafx.fxml;
    exports com.example.sd_portfolio_3;
}