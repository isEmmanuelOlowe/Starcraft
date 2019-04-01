package alphacraft.engine.optimiser;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import javafx.scene.control.ListView;
import alphacraft.engine.resources.GameElement;

public class Optimiser extends Thread {

  private HashMap<GameElement, Integer> buildingFor;
  private ArrayList<GameElement> upgrades;
  private boolean run = true;
  private ListView<String> optimal;
  /**
  * Variables which the Thread shall be able to manipulate
  */
  public Optimiser(HashMap<GameElement, Integer> buildingFor, ArrayList<GameElement> upgrades, ListView<String> optimal) {
    this.buildingFor = buildingFor;
    this.upgrades = upgrades;
    this.optimal = optimal;

  }

  /**
  * Finds the optimal build paths
  */
  public void run() {

    Generation generation = new Generation(buildingFor, upgrades, optimal);
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
          generation.newOptimal(game.log());
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
