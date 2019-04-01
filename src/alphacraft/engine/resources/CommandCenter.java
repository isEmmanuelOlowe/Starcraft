package alphacraft.engine.resources;

import java.util.HashMap;
import java.util.ArrayList;

public class CommandCenter extends Building {

  //so any command center can access are static
  private static ArrayList<MineralPatch> mineralPatches;
  private static ArrayList<GasPatch> gasPatches;
  private static ArrayList<Integer> takenWorkers;
  private static final int baseMineralPatches = 8;
  private static final int baseGasPatches = 2;
  private static int gasDemand;
  private static int mineralDemand;
  private static int noWorkers = 6;
  private static double generatedGas;
  private static double generatedMinerals;

  public CommandCenter() {
    super(GameElement.COMMAND_CENTER, 400, 0, 100, 10, new GameElement[]{});
  }

  public static int numberOfWorker() {
    return noWorkers;
  }

  public void init() {
    for (int i = 0; i < baseMineralPatches; i++) {
      mineralPatches.add(new MineralPatch());
    }
    for (int i = 0; i < baseGasPatches; i++) {
      gasPatches.add(new GasPatch());
    }
  }

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

  public static void resetDemand() {
    gasDemand = 0;
    mineralDemand = 0;
  }
  public static void getCosts(Resource resource) {
    gasDemand += resource.getGasCost();
    mineralDemand += resource.getMineralCost();
  }

  public static ArrayList<MineralPatch> getMineralPatches() {
    return mineralPatches;
  }
  public static int getRates(int workers, double currentGas, double currentMinerals) {
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
    if (maxGas == 0) {
      return 0;
    }
    double gasRatio = getRatio(currentGas, gasDemand);
    double mineralRatio = getRatio(currentMinerals, mineralDemand);
    double ratio = gasDemand / ((mineralRatio + gasRatio));
    int gasPotential = (int) (maxWorker * ratio);
    if (gasPotential >= maxGas) {
      return maxGas;
    }
    return gasPotential;
  }

  public static double getRatio(double current, double demand) {
    if (current > demand) {
      return current  - demand;
    }
    return 0;
  }

  public static void delegate(double currentGas, double currentSupply) {
    generatedMinerals = 0;
    generatedGas = 0;
    int avaliableWorkers = noWorkers - takenWorkers.size();
    int gasWorkers = getRates(avaliableWorkers, currentGas, currentSupply);
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

  public static double getGas() {
    return generatedGas;
  }

  public static void addRefinery() {
    boolean found = false;
    int i = 0;
    while(i < gasPatches.size() && found == false) {
      GasPatch patch = gasPatches.get(i);
      if (!patch.hasRefinery()) {
        patch.addRefinery();
        found = true;
      }
      i++;
    }
  }

  public static double getMinerals() {
    return generatedMinerals;
  }
  public static void addWorker() {
    noWorkers++;
  }

  public static void takeWorker(int takenTill) {
    takenWorkers.add(new Integer(takenTill));
  }

  public static void returnWorkers(int currentTime) {
    for (int i = 0; i < takenWorkers.size(); i++) {
      int returnTime = takenWorkers.get(i).intValue();
      if (returnTime >= currentTime) {
        takenWorkers.remove(i);
      }
    }
  }
  public static boolean hasAvaliableWorkers() {
    if (noWorkers == takenWorkers.size()) {
      return false;
    }
    return true;
  }

}
