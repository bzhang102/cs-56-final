module com.example.cs56final {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cs56final to javafx.fxml;
    exports com.example.cs56final;
}