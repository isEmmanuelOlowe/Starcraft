package alphacraft;

import alphacraft.controllers.splash.SplashController;
import alphacraft.controllers.Controller;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class AlphaCraft extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Controller controller = new Controller();
    controller.setStage(primaryStage);
    controller.changeToSplash();
  }

}
