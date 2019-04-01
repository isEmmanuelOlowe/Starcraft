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
import javafx.scene.image.Image;
public class AlphaCraft extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Controller controller = new Controller();
    // primaryStage.getIcons().add(new Image(AlphaCraft.class.getResourceAsStream("/controllers/splash/img/Buildings/Command_Center.jpg")));
    // primaryStage.getIcons().add(new Image("/controllers/splash/img/Buildings/Command_Center.jpg"));
    primaryStage.getIcons().add(new Image("http://1.bp.blogspot.com/-ZRDN2sugdrg/UXa1qeFfjYI/AAAAAAAAVjo/1m10L-jCIgo/s1600/starcraft_2_render7E0.png"));
    controller.setStage(primaryStage);
    controller.changeToSplash();
  }

}
