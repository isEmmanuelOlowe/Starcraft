package alphacraft.controllers.display;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import alphacraft.engine.resources.GameElement;
import alphacraft.controllers.Controller;
import alphacraft.engine.optimiser.Optimiser;
import java.util.ArrayList;
import java.util.HashMap;
public class DisplayController {

    @FXML
    private VBox error;

    @FXML
    private Text errorMessage;

    @FXML
    private ListView<String> buildingFor;

    @FXML
    private ListView<String> optimalBuild;

    @FXML
    private Button backButton;

    private Optimiser thread;

    @FXML
    public void stopBuilding(ActionEvent event) {
      thread.interrupt();
      Controller controller = new Controller();
      controller.changeToSplash();
      controller.changeToSplash();
    }

    public void buildStage(HashMap<GameElement, Integer> build, ArrayList<GameElement> upgrades) {

      for (GameElement key: build.keySet()) {
        buildingFor.getItems().add(key.toString() + ": " + build.get(key).toString());
      }

      for (GameElement upgrade: upgrades) {
        buildingFor.getItems().add(upgrade.toString());
      }
      thread = new Optimiser(build, upgrades, optimalBuild);
      thread.start();
    }
}
