package alphacraft.engine.optimiser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import alphacraft.engine.resources.*;
import java.util.Random;

public class Game {

  private static HashMap<GameElement, Integer> unitsGoal;
  private static HashSet<GameElement> upgradesGoal;
  private static HashMap<GameElement, Integer> buildingMaxQuantity = new HashMap<GameElement, Integer>();
  private ArrayList<Building> buildings = new ArrayList<Building>();
  private HashMap<GameElement, Integer> buildingsQuantity = new HashMap<GameElement, Integer>();
  private HashMap<GameElement, Integer> units = new HashMap<GameElement, Integer>();
  private HashSet<GameElement> upgrades = new HashSet<GameElement>();
  private static HashSet<GameElement> dependenciesList;
  private HashSet<GameElement> dependencies;
  private int worker_cap;
  private static  Random cap;
  //list of everything that has been built
  private ArrayList<GameElement> actions = new ArrayList<GameElement>();
  //determines how long it takes each command to execute per second
  private int gameSecond = 1;
  private int currentGameTime = 0;
  private double currentGas = 0;
  private double currentMinerals = 50;
  //since initial command center has these attributes
  private int currentSupply = 10;

  public Game() {
    CommandCenter.reset();
    OrbitalCommand.reset();
    CommandCenter command = new CommandCenter();
    command.init();
    buildings.add(command);
    actions.add(GameElement.COMMAND_CENTER);
    if (buildingMaxQuantity.containsKey(GameElement.COMMAND_CENTER)) {
      worker_cap = buildingMaxQuantity.get(GameElement.COMMAND_CENTER).intValue() * cap.nextInt(30) ;
    }
    else {
      worker_cap = cap.nextInt(30);
    }
    dependencies = new HashSet<GameElement>(dependenciesList);
  }
  public static void setup(HashMap<GameElement, Integer> setUnitGoal, ArrayList<GameElement> setUpgradeGoal, HashMap<GameElement, Integer> setBuildingMaxQuantity, ArrayList<GameElement> buildList) {
    cap = new Random();
    unitsGoal = setUnitGoal;
    upgradesGoal = new HashSet<GameElement>(setUpgradeGoal);
    buildingMaxQuantity = setBuildingMaxQuantity;
    dependenciesList = new HashSet<GameElement>(buildList);
    //adds Reactors addon to build list.
    dependenciesList.add(GameElement.SCV);
    // dependencies.add(GameElement.STARPORT_REACTOR);
    // dependencies.add(GameElement.FACTORY_TECH_LAB);
    // dependencies.add(GameElement.BARRACKS_REACTOR);
  }

  public void build(Resource resource) {

    currentGas -= resource.getGasCost();
    currentMinerals -= resource.getMineralCost();
    if (resource instanceof Building) {
      addBuilding(resource);
    }
    else if (resource instanceof Unit) {
      addUnit(resource);
    }
    else if (resource instanceof Upgrade) {
      addUpgrade(resource);
    }
  }

  public void addBuilding(Resource resource) {
    Building building = (Building) resource;
    buildings.add(building);
    int buildTime = building.getBuildTime();
    building.setAvaliable(buildTime, currentGameTime);
    currentSupply += building.getSupplyProvided();
    if (buildingsQuantity.containsKey(building.getName())) {
      Integer amount = buildingsQuantity.get(building.getName());
      Integer goal = buildingMaxQuantity.get(building.getName());
      if (amount.equals(goal)) {
        dependencies.remove(building.getName());
      }
      buildingsQuantity.put(building.getName(), new Integer(amount.intValue() + 1));
    }
    else {
      buildingsQuantity.put(building.getName(), new Integer(1));
    }

    if (building.getName() == GameElement.COMMAND_CENTER) {
      CommandCenter command = (CommandCenter) building;
      command.init();
    }
    else if (building.getName() == GameElement.REFINERY) {
      CommandCenter.addRefinery();
    }
    else if (resource instanceof OrbitalCommand) {
      for (int i = 0; i < buildings.size(); i++) {
        if (buildings.get(i).getName() == GameElement.COMMAND_CENTER && buildings.get(i).isAvaliable(currentGameTime)) {
          buildings.remove(i);
          OrbitalCommand.init();
          buildings.add(building);
          break;
        }
      }
    }
  }

  public void addUnit(Resource resource) {
    Unit unit = (Unit) resource;
    if (unit.getName() == GameElement.SCV) {
      if (CommandCenter.numberOfWorker() > worker_cap) {
        dependencies.remove(GameElement.SCV);
      }
      CommandCenter.addWorker();
    }
    else if (units.containsKey(unit.getName())) {
      Integer amount = units.get(unit.getName());
      Integer goal = unitsGoal.get(unit.getName());
      currentSupply += unit.getSupplyCost();
      if (amount.equals(goal)) {
        dependencies.remove(unit.getName());
      }
      units.put(unit.getName(), new Integer(amount.intValue() + 1));
    }
    else {
      units.put(unit.getName(), new Integer(1));
    }
  }

  public void addUpgrade(Resource resource) {
    dependencies.remove(resource.getName());
    upgrades.add(resource.getName());
  }

  public void execute(GameElement action) {
    //buildings are built
    if (action != GameElement.WAIT) {
      Resource build = ResourceHandler.getResource(action);
      if (build instanceof Building) {
        build(build);
      }
      else if (validAddon(action)) {
        Addon addon = (Addon) ResourceHandler.getResource(action);
        GameElement buildTo = GameElement.valueOf(action.toString().split("_")[0]);
        for (Building building: buildings) {
          if (building.getName() == buildTo && !building.hasAddon()) {
            building.addon(action);
            building.setAvaliable(addon.getBuildTime(), currentGameTime);
            build(build);
            break;
          }
        }
      }
      //upgrads and units are built
      else {
        for (Building building: buildings) {

          GameElement[] dependsOn = build.getDependencies();
          if (building.getName() == dependsOn[0] && building.isAvaliable(currentGameTime)) {
            if (dependsOn.length > 1 && validAddon(dependsOn[1])) {
              if (building.hasTechLab()) {
                building.setAvaliable(build.getBuildTime(), currentGameTime);
              }
            }
            else {
              building.setAvaliable(build.getBuildTime(), currentGameTime);
            }
            build(build);
            break;
            }
        }
      }
    }
    actions.add(action);
    currentGameTime += 1 * gameSecond;
  }


  //consider using breaks to save time
  public ArrayList<GameElement> getPossibleActions() {
    CommandCenter.resetDemand();
    ArrayList<GameElement> possibleBuild = new ArrayList<GameElement>();
    possibleBuild.add(GameElement.WAIT);
    for (GameElement dependence: dependencies) {
      System.out.println(dependence);
      Resource resource = ResourceHandler.getResource(dependence);
      CommandCenter.getCosts(resource);
      boolean isBuilding = (resource instanceof Building)? true: false;
      boolean possible = true;
      if (resource.getGasCost() > currentGas) {
        possible = false;
      }
      else if (resource.getMineralCost() > currentMinerals) {
        possible = false;
      }
      //no workers avaliable to build the building
      else if (isBuilding && !CommandCenter.hasAvaliableWorkers()) {
        possible = false;
      }
      else if (!containsDependencies(resource.getDependencies(), isBuilding)) {
        if (dependence == GameElement.SCV) {
        }
        possible = false;
      }
      else if (resource instanceof Unit) {
        Unit unit = (Unit) resource;
        if (unit.getSupplyCost() > currentSupply) {
          possible = false;
        }
      }
      else if (dependence == GameElement.REFINERY && !GasPatch.needRefinery()) {
        possible = false;
      }
      else if (validAddon(dependence)) {
        possible = attachAvaliable(dependence);
      }
      else if (dependence == GameElement.SCV) {
        if (CommandCenter.numberOfWorker() >= worker_cap) {
          possible = false;
        }
      }

      if (possible == true) {
        possibleBuild.add(dependence);
      }
    }
    enforeMaximums(possibleBuild);
    return possibleBuild;
  }

  public void enforeMaximums(ArrayList<GameElement> possibleBuild) {
    for (int i = 0; i < possibleBuild.size(); i++) {
      GameElement build = possibleBuild.get(i);
      if (buildingMaxQuantity.containsKey(build) && buildingsQuantity.containsKey(build)) {
        if (buildingMaxQuantity.get(build).equals(buildingsQuantity.get(build))) {
          possibleBuild.remove(i);
        }
      }
    }
  }
  public boolean attachAvaliable(GameElement addon) {
    String add = addon.toString();
    String[] components = add.split("_");
    boolean avaliable = false;
    //in the event every building has an addon
    int addons = 0;
    GameElement attachTo = GameElement.valueOf(components[0]);
    for (Building building: buildings) {
      if (building.hasAddon()) {
        addons++;
      }
      if (building.getName() == attachTo) {
        if (building.isAvaliable(currentGameTime)) {
          avaliable = true;
        }
      }
    }
    //every building has an addon
    //removes it from dependencies
    try {
      if (buildingMaxQuantity.get(attachTo).intValue() == addons) {
        dependencies.remove(addon);
      }
    }
    catch (NullPointerException e) {
      dependencies.remove(addon);
    }
    return avaliable;
  }
  public boolean validAddon(GameElement addon) {
    boolean valid = false;
    String add = addon.toString();
    String[] components = add.split("_");
    if (components.length > 1 && (components[1].equals("REACTOR") || components[1].equals("TECH"))) {
      valid = true;
    }
    return valid;
  }


  //consider using breaks to save time
  public boolean containsDependencies(GameElement[] dependsOn, boolean isBuilding) {
    boolean valid = true;
    for (int i = 0; i < dependsOn.length; i++) {
      GameElement dependence = dependsOn[i];
      if (actions.contains(dependence)) {
        if (i == 0 && !isBuilding) {
          //checks that a building is avliable to build it
          boolean needLab = false;
          if (dependsOn.length > 1) {
            if (validAddon(dependsOn[i + 1])) {
              needLab = true;
              i++;
            }
          }
          valid = avaliableBuiling(dependence, needLab);
        }
      }
      else {
        valid = false;
      }
    }
    return valid;
  }

  public boolean avaliableBuiling(GameElement dependence, boolean needsLab) {
    boolean avaliable = false;
    for (Building building: buildings) {
      if (building.getName() == dependence) {
        if (building.isAvaliable(currentGameTime)) {
          if (needsLab) {
            if (building.hasTechLab()) {
              avaliable = true;
            }
          }
          else {
            avaliable = true;
          }
        }
      }
    }
    return avaliable;
  }

  public void getMaterials() {
      //keep in mind gameSecond
      for (int i = 0; i < gameSecond; i++) {
        CommandCenter.returnWorkers(currentGameTime);
        CommandCenter.delegate(currentGas, currentSupply);
        currentGas += CommandCenter.getGas();
        currentMinerals += CommandCenter.getMinerals();
        OrbitalCommand.disburseMULE(currentGameTime);
        currentMinerals += OrbitalCommand.MULELoad();
      }
  }

  public boolean complete() {
    boolean completed = true;
    if (!upgrades.equals(upgradesGoal)) {
      completed = false;
    }
    else if (!units.equals(unitsGoal)) {
      completed = false;
    }
    return completed;
  }

  public int gameLength() {
    return currentGameTime;
  }

  public ArrayList<GameElement> log() {
    return actions;
  }
}
