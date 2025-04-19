module org.example.studentai {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.studentai to javafx.fxml;
    opens StudentuInformacija to javafx.base; // Svarbu, kad JavaFX galėtų pasiekti Student klasę

    exports org.example.studentai;
}
