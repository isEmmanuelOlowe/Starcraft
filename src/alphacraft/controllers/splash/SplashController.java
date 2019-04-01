package alphacraft.controllers.splash;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.HashMap;
import java.util.ArrayList;
import alphacraft.engine.resources.*;
import alphacraft.controllers.Controller;

public class SplashController {

    @FXML
    private VBox error;

    @FXML
    private Text errorMessage;

    @FXML
    private TextField marine;

    @FXML
    private TextField hellion;

    @FXML
    private TextField medivac;

    @FXML
    private TextField viking;

    @FXML
    private TextField marauder;

    @FXML
    private TextField reaper;

    @FXML
    private TextField ghost;

    @FXML
    private TextField siegeTank;

    @FXML
    private TextField thor;

    @FXML
    private TextField raven;

    @FXML
    private TextField banshee;

    @FXML
    private TextField battlecruiser;

    @FXML
    private ComboBox<String> infantryWeapons;

    @FXML
    private ComboBox<String> infantryArmor;

    @FXML
    private ComboBox<String> vehicleWeapons;

    @FXML
    private ComboBox<String> vehicleArmor;

    @FXML
    private ComboBox<String> shipWeapons;

    @FXML
    private ComboBox<String> shipArmor;

    @FXML
    private ComboBox<String> gameSecond;

    @FXML
    private Button buildButton;



    private TextField[] textFields = new TextField[12];
    private GameElement[] fieldName = new GameElement[12];
    private String[] boxName = new String[6];
    private ArrayList<ComboBox<String>> comboBoxes = new ArrayList<ComboBox<String>>();


    private void createComboBoxes() {
      boxName[0] = "INFANTRY_WEAPON";
      comboBoxes.add(infantryWeapons);
      boxName[1] = "INFANTRY_ARMOR";
      comboBoxes.add(infantryArmor);
      boxName[2] = "VEHICLE_WEAPON";
      comboBoxes.add(vehicleWeapons);
      boxName[3] = "VEHICLE_ARMOR";
      comboBoxes.add(vehicleArmor);
      boxName[4] = "SHIP_WEAPON";
      comboBoxes.add(shipWeapons);
      boxName[5] = "SHIP_ARMOR";
      comboBoxes.add(shipArmor);

    }

    private void formatComboBoxes(ArrayList<ComboBox<String>> comboBoxes) {
      for (ComboBox<String> box: comboBoxes) {
        box.getItems().addAll("No Upgrade", "1", "2", "3");
        box.getSelectionModel().select("No Upgrade");
      }
    }
    /**
    * Ennsures only numeric input is allowed into TextFields
    *
    * @param textField the textfield being given these restrictionss
    */
    private void formatTextField(TextField... textFields) {
      for (TextField textField: textFields) {

        textField.textProperty().addListener(new ChangeListener<String>() {
          @Override
          public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
            if (!newValue.matches("\\d*")) {
              textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
          }
        });
      }
    }


    /**
    * Adds all the required textFields in page to array in which they can be
    looped over.
    */
    private void createTextFieldArray() {
      textFields[0] = marine;
      fieldName[0] = GameElement.MARINE;
      textFields[1] = hellion;
      fieldName[1] = GameElement.HELLION;
      textFields[2] = medivac;
      fieldName[2] = GameElement.MEDIVAC;
      textFields[3] = viking;
      fieldName[3] = GameElement.VIKING;
      textFields[4] = marauder;
      fieldName[4] = GameElement.MARAUDER;
      textFields[5] = reaper;
      fieldName[5] = GameElement.REAPER;
      textFields[6] = ghost;
      fieldName[6] = GameElement.GHOST;
      textFields[7] = siegeTank;
      fieldName[7] = GameElement.SIEGE_TANK;
      textFields[8] = thor;
      fieldName[8] = GameElement.THOR;
      textFields[9] = raven;
      fieldName[9] = GameElement.RAVEN;
      textFields[10] = banshee;
      fieldName[10] = GameElement.BANSHEE;
      textFields[11] = battlecruiser;
      fieldName[11] = GameElement.BATTLECRUISER;
    }

    /**
    * Formats fields in program
    */
    public void buildStage(){
      gameSecond.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
      gameSecond.getSelectionModel().select("1");
      createTextFieldArray();
      //ensures only text input is allowed
      formatTextField(textFields);
      createComboBoxes();
      formatComboBoxes(comboBoxes);

    }

    @FXML
    public void buildGame(ActionEvent event) {
      HashMap<GameElement, Integer> buildingFor = new HashMap<GameElement, Integer>();
      int selectedGameSecond = Integer.parseInt(gameSecond.getSelectionModel().getSelectedItem().toString());
      for (int i = 0; i < textFields.length; i++) {
        int value = parseInput(textFields[i]);
        if (value != 0) {
          //adds all the items being built to hashmap
          buildingFor.put(fieldName[i], new Integer(parseInput(textFields[i])));
        }
      }

      if (buildingFor.size() != 0) {
        if (!supplyCapCheck(buildingFor)) {
          error.setVisible(true);
          errorMessage.setText("The Supply cost exceeds game cap of 200");
        }
        else {
          error.setVisible(false);
          ArrayList<GameElement> upgrades = addComboxes();
          Controller controller = new Controller();
          controller.changeToDisplay(buildingFor, upgrades, selectedGameSecond);
        }
      }
      else {
        error.setVisible(true);
        errorMessage.setText("No Quantity for Unit Selected");

      }
    }

    private ArrayList<GameElement> addComboxes() {
      ArrayList<GameElement> upgrades = new ArrayList<GameElement>();
      for (int i = 0; i < comboBoxes.size(); i++) {
        String selection = comboBoxes.get(i).getSelectionModel().getSelectedItem().toString();
        if (!selection.equals("No Upgrade")) {
          upgrades.add(GameElement.valueOf(boxName[i]  + "_" + selection));
        }
      }
      return upgrades;
    }

    private boolean supplyCapCheck(HashMap<GameElement, Integer> buildingFor) {
      final int maxSupply = 200;
      int requiredSupply = 0;
      for (GameElement e: buildingFor.keySet()) {
        Unit unit = (Unit) ResourceHandler.getResource(e);
        requiredSupply = unit.getSupplyCost() * buildingFor.get(e).intValue();
      }
      if (requiredSupply > 200){
        return false;
      }
      else {
        return true;
      }
    }


    private int parseInput(TextField textField) {

        String text = textField.getText();
        if (text.isEmpty()) {
          //default value for empty field
          return 0;
        }
        else {
          return Integer.parseInt(text);
        }
    }

}
