package alphacraft.engine.resources;

import java.util.HashMap;
import java.util.ArrayList;

/**
* Describes the behaviour of the Command Center in starcraft II
*/
public class CommandCenter extends Building {

  //so any command center can access are static
  private static ArrayList<MineralPatch> mineralPatches;
  private static ArrayList<GasPatch> gasPatches;
  private static ArrayList<Integer> takenWorkers;
  //the amount of patches that come with each base
  private static final int baseMineralPatches = 8;
  private static final int baseGasPatches = 2;
  //demand for future resources
  private static int gasDemand;
  private static int mineralDemand;
  //number of workers initial
  private static int noWorkers = 6;
  private static double generatedGas;
  private static double generatedMinerals;

  /**
  * Creates a new CommandCenter
  */
  public CommandCenter() {
    super(GameElement.COMMAND_CENTER, 400, 0, 100, 10, new GameElement[]{});
  }

  /**
  * The number of workers which are current avaliable
  *
  * @return the integer number of workers
  */
  public static int numberOfWorker() {
    return noWorkers;
  }


  /**
  * initialisation of CommandCenter with new bases.
  */
  public void init() {
    for (int i = 0; i < baseMineralPatches; i++) {
      mineralPatches.add(new MineralPatch());
    }
    for (int i = 0; i < baseGasPatches; i++) {
      gasPatches.add(new GasPatch());
    }
  }

  /**
  * Resets the Class static variables back to their ground state.
  */
  public static  void reset() {
    MineralPatch.reset();
    GasPatch.reset();
    mineralPatches = new ArrayList<MineralPatch>();
    gasPatches = new ArrayList<GasPatch>();
    takenWorkers = new ArrayList<Integer>();
    gasDemand = 0;
    mineralDemand = 0;
    noWorkers = 6;
    generatedGas = 0;
    generatedMinerals = 0;
  }

  /**
  * reduces demand for resource to ground state.
  */
  public static void resetDemand() {
    gasDemand = 0;
    mineralDemand = 0;
  }

  /**
  * Incrementatlly calculatees the demand for resources
  *
  * @param resource the resource attemptting to be built
  */
  public static void getCosts(Resource resource) {
    gasDemand += resource.getGasCost();
    mineralDemand += resource.getMineralCost();
  }

  /**
  * Returns the minerals gasPatches.
  *
  * @return arraylist of mineralPatches
  */
  public static ArrayList<MineralPatch> getMineralPatches() {
    return mineralPatches;
  }

  /**
  * Calculatues how many workers shall be put to work on gas gasPatches.
  *
  * @param workers the number of avliable
  * @param currentGas the current Gas avaliable to be spent
  * @param currentMinerals the current mineral amount able to be spent
  * @return the number of workers to worker at the gas patche
  */
  private static int getRates(int workers, double currentGas, double currentMinerals) {
    int maxWorker;
    int freeMineral = MineralPatch.avaliablePatch();
    int freeGas = GasPatch.avaliablePatch();
    int maxGas = freeGas * 3;
    int maxMineral = freeMineral * 3;

    if (workers > (maxGas + maxMineral)) {
      maxWorker = maxGas + maxMineral;
    }
    else {
      maxWorker = workers;
    }
    //no workers can be added since no patches are avaliable.
    if (maxGas == 0) {
      return 0;
    }
    //caculates a ratio between the gas and mineral
    double gasRatio = getRatio(currentGas, gasDemand);
    double mineralRatio = getRatio(currentMinerals, mineralDemand);
    double ratio = gasDemand / ((mineralRatio + gasRatio));
    int gasPotential = (int) (maxWorker * ratio);
    if (gasPotential >= maxGas) {
      return maxGas;
    }
    return gasPotential;
  }

  /**
  * calculates a ratio of demand to current.
  *
  * @param current the current rate avaliable
  * @param demand the current demand for resource
  */
  private static double getRatio(double current, double demand) {
    if (current > demand) {
      return current  - demand;
    }
    return 0;
  }

  /**
  * performs calculations necessary for the gathering of resources.
  *
  * @param currentGas the current Gas avaliable for use
  * @param currentMinerals the current minerals which is avaliable for
  */
  public static void delegate(double currentGas, double currentMinerals) {
    generatedMinerals = 0;
    generatedGas = 0;
    int avaliableWorkers = noWorkers - takenWorkers.size();
    int gasWorkers = getRates(avaliableWorkers, currentGas, currentMinerals);
    int mineralWorkers = avaliableWorkers - gasWorkers;

    int timesRan = 0;
    while (mineralWorkers > 0 && timesRan != 2) {
      for (MineralPatch patch: mineralPatches) {
          if (!patch.depleted()) {
            generatedMinerals += patch.extract(timesRan);
            mineralWorkers--;
          }
      }
      timesRan++;
    }

    while (gasWorkers > 0) {
      for (GasPatch patch: gasPatches) {
          if (!patch.depleted() && patch.hasRefinery()) {
            generatedGas += patch.extract();
            gasWorkers--;
          }
      }
    }

  }

  /**
  * The gas that has been generated by workers.
  *
  * @return the double amount of new gas avaliable
  */
  public static double getGas() {
    return generatedGas;
  }

  /**
  * adds a refinery to a gas patch which does not have one.
  */
  public static void addRefinery() {
    boolean found = false;
    int i = 0;
    //loops for gas patch with refinery and adds one
    while(i < gasPatches.size() && found == false) {
      GasPatch patch = gasPatches.get(i);
      if (!patch.hasRefinery()) {
        patch.addRefinery();
        found = true;
      }
      i++;
    }
  }

  /**
  * Gets the newly generated minerals avaliable.
  *
  * @return the minerals which have been ope
  */
  public static double getMinerals() {
    return generatedMinerals;
  }

  /**
  * adds a worker to the total number of workers.
  */
  public static void addWorker() {
    noWorkers++;
  }

  /**
  * temoraryly takes a worker away to build a building.
  *
  * @param buildTime the buildtime of the building
  * @param currentTime the current time in the gas
  */
  public static void takeWorker(int buildTime, int currentTime) {
    takenWorkers.add(new Integer(buildTime + currentTime));
  }

  /**
  * Returns a worker to Command CommandCenter.
  *
  * @param currentTime the current time in the game
  */
  public static void returnWorkers(int currentTime) {
    for (int i = 0; i < takenWorkers.size(); i++) {
      int returnTime = takenWorkers.get(i).intValue();
      if (returnTime >= currentTime) {
        takenWorkers.remove(i);
      }
    }
  }

  /**
  * Checks if there are workers avaliabe.
  *
  * @return true if workers are avaliabe
  */
  public static boolean hasAvaliableWorkers() {
    if (noWorkers == takenWorkers.size()) {
      return false;
    }
    return true;
  }

}
