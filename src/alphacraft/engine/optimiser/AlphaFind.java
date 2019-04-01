package alphacraft.engine.optimiser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import alphacraft.engine.resources.*;
//remove when done debugging
import java.util.Scanner;

public class AlphaFind {

  //from intial command center
  private int supplyCost = -10;
  private int gasCost = 0;
  private int mineralCost = 0;
  private Game game;
  private HashSet<GameElement> build = new HashSet<GameElement>();
  private ArrayList<GameElement> buildList = new ArrayList<GameElement>();
  private HashMap<GameElement, Integer> maxItem = new HashMap<GameElement, Integer>();

  public AlphaFind(HashMap<GameElement, Integer> buildingFor, ArrayList<GameElement> upgrades, int gameSecond) {
    getDependencies(buildingFor);
    addUpgradeDependence(upgrades);
    buildList.addAll(build);
    setupMaxList();
    getSupply();
    getBase();
    Game.setup(buildingFor, upgrades, maxItem, buildList, gameSecond);
  }

  private void getBase() {
    int refinery =  (gasCost / 1500) + 1;
    for (int i = 0; i < refinery; i++) {
      buildList.add(GameElement.REFINERY);
    }
    gasCost -= 1500 * 8;
    mineralCost -= 2500 * 2;
    while (gasCost >= 0 || mineralCost >= 0) {
      gasCost -= 1500 * 8;
      mineralCost -= 2500 * 2;
      buildList.add(GameElement.COMMAND_CENTER);
      incrementMax(GameElement.COMMAND_CENTER);
    }
  }

  private void setupMaxList() {
    int permanentUpgrade = 1;
    HashMap<GameElement, Integer> maxItem = new HashMap<GameElement, Integer>();
    maxItem.put(GameElement.GHOST_ACADEMY, new Integer(permanentUpgrade));
    maxItem.put(GameElement.FUSION_CORE, new Integer(permanentUpgrade));
    for (GameElement element: GameElement.values()) {
      Resource resource = ResourceHandler.getResource(element);
      if (resource instanceof Building) {
        if (!buildList.contains(element)) {
          maxItem.put(element, new Integer(0));
        }
      }
    }
  }

  private void incrementMax(GameElement element) {
    if (maxItem.containsKey(element)) {
      int currentValue = maxItem.get(element);
      maxItem.put(element, new Integer(currentValue + 1));

    }
    else {
      maxItem.put(element, new Integer(1));
    }
  }

  public void findSolution() {
    game = new Game();
    while(!game.complete()) {
      game.getMaterials();
      ArrayList<GameElement> choice = game.getPossibleActions();
      for (int i = 0; i < buildList.size(); i++) {
        GameElement build = buildList.get(i);
        if (choice.contains(build)) {
          game.execute(build);
          buildList.remove(i);
          break;
        }
        else {
          game.execute(GameElement.WAIT);
        }
      }
    }
  }

  private void getSupply() {
    int supplyDepotSupplyProvided = 8;
    while (supplyCost > 0) {
      buildList.add(GameElement.SUPPLY_DEPOT);
      supplyCost -= supplyDepotSupplyProvided;
      incrementMax(GameElement.SUPPLY_DEPOT);
    }
  }
  private void getDependencies(HashMap<GameElement, Integer> buildingFor) {

    for (GameElement element: buildingFor.keySet()) {
      Unit unit = (Unit) ResourceHandler.getResource(element);
      int quantity = buildingFor.get(element).intValue();
      supplyCost += unit.getSupplyCost() * quantity;
      gasCost += unit.getGasCost() * quantity;
      mineralCost += unit.getMineralCost() * quantity;
      GameElement[] dependencies = unit.getDependencies();
      for (int i = 0; i < quantity; i++) {
        buildList.add(element);
      }
      addBuildDependence(dependencies);
    }
  }

  private void addUpgradeDependence(ArrayList<GameElement> upgrades) {
    for (GameElement upgrade: upgrades) {
      addDependence(upgrade);
    }
  }

  private void addBuildDependence(GameElement[] items) {
    for (GameElement item: items) {
      addDependence(item);
    }
  }

  private void addDependence(GameElement item) {
    incrementMax(item);
    if (!build.contains(item)) {
      Resource resource = ResourceHandler.getResource(item);
      gasCost += resource.getGasCost();
      mineralCost += resource.getMineralCost();
      if (resource instanceof Building) {
        Building build = (Building) resource;
        supplyCost -= build.getSupplyProvided();
      }
      build.add(item);
      addBuildDependence(resource.getDependencies());
    }
  }

  public ArrayList<GameElement> foundSolution() {
    return game.log();
  }
}
