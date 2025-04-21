module org.example.studentai {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.studentai to javafx.fxml;
    opens StudentuInformacija to javafx.base;
    opens Lankomumas to javafx.base;

    exports org.example.studentai;
}
