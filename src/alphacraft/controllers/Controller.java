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

/**
* Handles the swtiching between views
*/
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

  /**
  * Returns the user to Splash screen
  */
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

  /**
  * Takes user to the display Screen
  *
  * @param build the build the user is tring to make
  * @param upgrades the upgrades the user wants
  * @param gameSecond the seconds which shall pass before game action
  */
  public void changeToDisplay(HashMap<GameElement, Integer> build, ArrayList<GameElement> upgrades , int gameSecond) {
    try {

      FXMLLoader loader = new FXMLLoader(getClass().getResource("display/display.fxml"));
      Parent root = loader.load();
      DisplayController displayController = (DisplayController) loader.getController();
      displayController.buildStage(build, upgrades, gameSecond);
      stage.setTitle("AlphaCraft");
      stage.setScene(new Scene(root));
      stage.show();
    }
    catch (IOException e) {
      System.out.println("FXML file is missing");
    }
  }


}
