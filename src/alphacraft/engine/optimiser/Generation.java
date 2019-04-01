package alphacraft.engine.optimiser;

import javafx.scene.control.ListView;
import alphacraft.engine.resources.GameElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.text.DecimalFormat;
import java.lang.IllegalStateException;
/**
* Handles the optimals results and generated results
*/
public class Generation {

  private int maxRunTime;
  private HashMap<GameElement, Integer> buildingFor;
  private ListView<String> optimal;
  private ArrayList<GameElement> upgrades;
  private DecimalFormat decimalFormat;
  private ArrayList<GameElement> currentFastest;
  private long buffer = System.currentTimeMillis();
  private int gameSecond;

  /**
  * Creates a nwe generation object which shall be responsible for these operations
  *
  * @param buildingFor what the user is trying to build
  * @param upgrades upgrades user is trying to impletement
  * @param optimal stores the output for the program
  * @param gameSecond how many seconds must pass in the game for an action to take place
  */
  public Generation(HashMap<GameElement, Integer> buildingFor, ArrayList<GameElement> upgrades, ListView<String> optimal, int gameSecond) {
    this.buildingFor = buildingFor;
    this.upgrades = upgrades;
    this.optimal = optimal;
    this.gameSecond = gameSecond;
    this.decimalFormat = new DecimalFormat("00");
  }

  /**
  * Determines the worse possible solution to find maximum time each generation has to find solution.
  */
  public void initialisation() {
    AlphaFind alpha = new AlphaFind(buildingFor, upgrades, gameSecond);
    alpha.findSolution();
    newOptimal(alpha.foundSolution());

  }

  /**
  * Gets the limiting length for which an attempt for solution can run for
  *
  * @return the max run time of a game object
  */
  public int maxRunTime() {
    return this.maxRunTime;
  }

  /**
  * Generates a new game object for use
  *
  * @return new game object
  */
  public Game newGame(){
    return new Game();
  }

  /**
  * Determines if a new optimal has been found and determines if listview is avaliable to be accessed.
  */
  public void newOptimal(ArrayList<GameElement> build) {
    if (build.size() < maxRunTime) {
      maxRunTime = build.size();
    }
    long current = System.currentTimeMillis() * 1000;
    this.maxRunTime = build.size();
    currentFastest = build;
    if (current - buffer  >= 10000000) {
      printOptimal();
      buffer = System.currentTimeMillis() * 1000;
    }
  }

  /**
  * Prints the solution to the list view if it is avaliable
  */
  public void printOptimal() {

    try {
      optimal.getItems().clear();
      HashMap<GameElement, Integer> printMap = new HashMap<GameElement, Integer>();
      int totalSeconds = 0;
      for (GameElement element: currentFastest) {
        int minutes = totalSeconds/60;
        int seconds = totalSeconds % 60;
        if (element != GameElement.WAIT) {
          if (printMap.containsKey(element)) {
            optimal.getItems().add(minutes + ":"+ decimalFormat.format(seconds) +" " + element + ": Quantity " + (printMap.get(element).intValue() + 1));
            printMap.put(element, new Integer(printMap.get(element).intValue() + 1));
          }
          else {
            optimal.getItems().add(minutes + ":"+ decimalFormat.format(seconds) +" " + element + ": Quantity " +  1);
            printMap.put(element, new Integer(1));

          }
        }
        totalSeconds += gameSecond;
      }
    }
    catch (IllegalStateException e) {
      System.out.println("is busy");
    }
  }
}
