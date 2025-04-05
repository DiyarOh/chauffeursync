module com.example.chauffeursync {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chauffeursync to javafx.fxml;
    exports com.example.chauffeursync;
}