module com.example.notebook {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.notebook to javafx.fxml;
    exports com.example.notebook;
}