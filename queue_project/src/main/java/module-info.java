module com.jomo.queue_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
	requires javafx.base;
	requires javafx.graphics;

    opens com.jomo.queue_project to javafx.fxml;
    exports com.jomo.queue_project;
}