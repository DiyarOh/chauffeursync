module com.example.chauffeursync {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;

    exports com.chauffeursync;

    opens com.chauffeursync.controllers to javafx.fxml;
}
