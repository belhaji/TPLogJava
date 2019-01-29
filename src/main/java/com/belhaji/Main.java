package com.belhaji;

import com.belhaji.controller.MainUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
  public static void main(String[] args)
  {
    launch(Main.class, args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource(MainUIController.FILENAME));
    Parent parent = loader.load();
    MainUIController controller = loader.getController();
    Scene scene = new Scene(parent);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
