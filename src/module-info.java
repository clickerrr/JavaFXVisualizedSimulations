module PathfindingVisualization {
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.controls;
	opens main.java to javafx.graphics, javafx.fxml, javafx.controls;
	opens main.java.controllers to javafx.graphics, javafx.fxml, javafx.controls;
	opens classes to javafx.graphics, javafx.fxml, javafx.controls;
}