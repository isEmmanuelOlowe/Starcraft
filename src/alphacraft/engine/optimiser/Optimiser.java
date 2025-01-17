package alphacraft.engine.optimiser;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import javafx.scene.control.ListView;
import alphacraft.engine.resources.GameElement;
/**
* Allows for multiple thread and for problem to run concurrently with GUI
*/
public class Optimiser extends Thread {

  private HashMap<GameElement, Integer> buildingFor;
  private ArrayList<GameElement> upgrades;
  private boolean run = true;
  private ListView<String> optimal;
  private int gameSecond;
  /**
  * Variables which the Thread shall be able to manipulate
  *
  * @param buildingFor what the user is build for
  * @param upgrades the upgrades the user desires
  * @param optimal where the optimal results shall be outputted to
  * @param gameSecond how many seonds must pass before an action can take place
  */
  public Optimiser(HashMap<GameElement, Integer> buildingFor, ArrayList<GameElement> upgrades, ListView<String> optimal, int gameSecond) {
    this.buildingFor = buildingFor;
    this.upgrades = upgrades;
    this.optimal = optimal;
    this.gameSecond = gameSecond;

  }

  /**
  * Finds the optimal build paths
  */
  public void run() {

    Generation generation = new Generation(buildingFor, upgrades, optimal, gameSecond);
    Random randNum = new Random();
    generation.initialisation();

    //runs until thread is interrupted
    while (run) {
        Game game = generation.newGame();
        while(game.complete() == false && game.gameLength() < generation.maxRunTime()) {
            game.getMaterials();
            ArrayList<GameElement> actions = game.getPossibleActions();
            int randomChoice = randNum.nextInt(actions.size());
            game.execute(actions.get(randomChoice));
          }
          if (game.complete()) {
            generation.newOptimal(game.log());
          }
        }
  }

  /**
  * Stops the Optimiser thread from running
  */
  public void interrupt() {
    run = false;
    super.interrupt();
  }
}
