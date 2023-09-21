package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import main.java.controllers.AnimationTestController;
import main.java.controllers.MainController;

public class RunJavafx extends Application
{

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
		System.out.print("");
//		loader.setController(new MainController());
		
		loader = new FXMLLoader(getClass().getResource("/fxml/animationTest.fxml"));
		System.out.print("");
		loader.setController(new AnimationTestController());
		primaryStage = loader.load();
		primaryStage.show();
	

	}
	public static void main(String[] args)
	{
		launch();
	}
	
}
