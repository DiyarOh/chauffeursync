module com.example.chauffeursync {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires com.google.protobuf;

    exports com.chauffeursync;

    opens com.chauffeursync.controllers to javafx.fxml;
}
