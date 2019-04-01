package alphacraft.controllers;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import alphacraft.controllers.display.DisplayController;
import alphacraft.controllers.splash.SplashController;
import java.io.IOException;
import alphacraft.engine.resources.GameElement;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

  public static Stage stage;

/**
* Sets the main stage to the primary stage
*
* @param primaryStage the primary JavaFX stage
*/
  public void setStage(Stage primaryStage){
    stage = primaryStage;
  }

  public void changeToSplash() {
    try {

      FXMLLoader loader = new FXMLLoader(getClass().getResource("splash/splash.fxml"));
      Parent root = loader.load();
      SplashController splashController = (SplashController) loader.getController();
      splashController.buildStage();
      stage.setTitle("AlphaCraft");
      stage.setScene(new Scene(root));
      stage.show();
    }
    catch (IOException e) {
      System.out.println("FXML file is missing");
    }
  }

  public void changeToDisplay(HashMap<GameElement, Integer> build, ArrayList<GameElement> upgrades , int gameSecond) {
    try {

      FXMLLoader loader = new FXMLLoader(getClass().getResource("display/Display.fxml"));
      Parent root = loader.load();
      DisplayController displayController = (DisplayController) loader.getController();
      displayController.buildStage(build, upgrades, gameSecond);
      //splashController.buildStage();
      stage.setTitle("AlphaCraft");
      stage.setScene(new Scene(root));
      stage.show();
    }
    catch (IOException e) {
      System.out.println("FXML file is missing");
    }
  }


}
