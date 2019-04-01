package alphacraft.engine.optimiser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import alphacraft.engine.resources.*;
import java.util.Random;

/**
* Simulates a Game of StarCraft II
*/
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
  private static int gameSecond;
  private int currentGameTime = 0;
  private double currentGas = 0;
  private double currentMinerals = 50;
  //since initial command center has these attributes
  private int currentSupply = 10;

  /**
  * Creates a new game
  */
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
  /**
  * Initial Setup of the  Game class
  *
  * @param setUnitGoal the quantity of units that is trying to be built
  * @param setUpgradeGoal the upgrades trying to be built
  * @param setBuildingMaxQuantity the max number of a building which can be present
  * @param buildList the items which are required to be buist
  * @param setGameSecond the amount of time which needs to pass before a action takes place
  */
  public static void setup(HashMap<GameElement, Integer> setUnitGoal, ArrayList<GameElement> setUpgradeGoal, HashMap<GameElement, Integer> setBuildingMaxQuantity, ArrayList<GameElement> buildList, int setGameSecond) {
    gameSecond = setGameSecond;
    cap = new Random();
    unitsGoal = setUnitGoal;
    upgradesGoal = new HashSet<GameElement>(setUpgradeGoal);
    buildingMaxQuantity = setBuildingMaxQuantity;
    dependenciesList = new HashSet<GameElement>(buildList);
    //adds Reactors addon to build list.
    dependenciesList.add(GameElement.SCV);
    dependenciesList.add(GameElement.STARPORT_REACTOR);
    dependenciesList.add(GameElement.FACTORY_REACTOR);
    dependenciesList.add(GameElement.BARRACKS_REACTOR);
    dependenciesList.add(GameElement.ORBITAL_COMMAND);
  }


  /**
  * Builds a resource which has been executed
  *
  * @param resource the resource intended to be built
  */
  private void build(Resource resource) {

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

  /**
  * Ads a build to the list of built items
  *
  * @param resource the building being built
  */
  private void addBuilding(Resource resource) {
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

    if (building.getName() == GameElement.COMMAND_CENTER && validCommand(building)) {

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

  /**
  * Determiens if a object is a valid command center
  *
  * @return true if it is
  */
  private boolean validCommand(Resource building){
    try {
      CommandCenter command = (CommandCenter) building;
      command.init();
      return true;
    }
    catch (ClassCastException e) {
      return false;
    }
  }


  /**
  * Builds a unit that has been executed
  *
  * @param resource the unit being built
  */
  private void addUnit(Resource resource) {
    Unit unit = (Unit) resource;
    if (unit.getName() == GameElement.MARINE ) {
    }
    if (unit.getName() == GameElement.SCV) {
      if (CommandCenter.numberOfWorker() > worker_cap) {
        dependencies.remove(GameElement.SCV);
      }
      CommandCenter.addWorker();
    }
    else if (units.containsKey(unit.getName())) {
      if (resource.getName() == GameElement.MARINE) {
      }
      Integer amount = units.get(unit.getName());
      Integer goal = unitsGoal.get(unit.getName());
      currentSupply += unit.getSupplyCost();
      units.put(unit.getName(), new Integer(amount.intValue() + 1));
      if (amount.equals(goal)) {
        dependencies.remove(unit.getName());
      }
    }
    else {
      units.put(unit.getName(), new Integer(1));
    }
  }

  /**
  * Builds an upgrade that has been executed
  *
  * @param resource the upgrade being built
  */
  public void addUpgrade(Resource resource) {
    dependencies.remove(resource.getName());
    upgrades.add(resource.getName());
  }

  /**
  * Executes an action
  *
  * @param action being executed
  */
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
          if (building.getName() == buildTo && !building.hasAddon() && building.isAvaliable(currentGameTime)) {
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
                CommandCenter.takeWorker(build.getBuildTime(), currentGameTime);
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
    currentGameTime +=  gameSecond;
  }


  /**
  * gets the actions that can be executed to move towards goal
  *
  * @return list of elements which can be built
  */
  public ArrayList<GameElement> getPossibleActions() {
    CommandCenter.resetDemand();
    ArrayList<GameElement> possibleBuild = new ArrayList<GameElement>();
    possibleBuild.add(GameElement.WAIT);
    for (GameElement dependence: dependenciesList) {
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

  /**
  * Ensures buildings dont exceed max quantity
  *
  * @@param possibleBuild the list of items which can be built
  */
  private void enforeMaximums(ArrayList<GameElement> possibleBuild) {
    for (int i = 0; i < possibleBuild.size(); i++) {
      GameElement build = possibleBuild.get(i);
      if (buildingMaxQuantity.containsKey(build) && buildingsQuantity.containsKey(build)) {
        if (buildingMaxQuantity.get(build).equals(buildingsQuantity.get(build))) {
          possibleBuild.remove(i);
        }
      }
      else if (validAddon(build)) {
        GameElement building = GameElement.valueOf(build.toString().split("_")[0]);
        if (buildingMaxQuantity.containsKey(building) && buildingsQuantity.containsKey(building)) {
          if (buildingMaxQuantity.get(building).equals(buildingsQuantity.get(building))) {
            possibleBuild.remove(i);
          }
        }
      }
    }
  }

  /**
  * determines is there a bulding compatible with an addon
  *
  * @param addon the addon being add to being
  */
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


  /**
  * determines it the game player current has all the dependencies required to build an item
  *
  * @param dependsOn the depends being searches
  * @param isBuilding whether or not the building is a resource
  */
  private boolean containsDependencies(GameElement[] dependsOn, boolean isBuilding) {
    boolean valid = true;
    for (int i = 0; i < dependsOn.length; i++) {
      GameElement dependence = dependsOn[i];
      if (actions.contains(dependence)) {
        if (i == 0 && !(isBuilding && dependence == GameElement.COMMAND_CENTER)) {
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

  /**
  * Depends if a building is able to build a unit
  *
  * @param dependence the building that needs to build
  * @param needsLab whether or not the building being built needs a lab
  */
  private boolean avaliableBuiling(GameElement dependence, boolean needsLab) {
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

  /**
  * Gets the minrals which have been generated in teh last game second;
  */
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


  /**
  * Depends if the upgrades goal have been fulfilled
  *
  * @return true if upgrade goals have been fullfilled
  */
  private boolean upgradesEqual() {
    boolean valid = true;
    for(GameElement upgrade: upgradesGoal) {
      if (!upgrades.contains(upgrade)) {
        valid = false;
      }
    }
    return valid;
  }

  /**
  * Depends if the game has build the desired outcome
  *
  * @return true if goals have been built
  */
  public boolean complete() {
    boolean completed = true;
    if (!units.equals(unitsGoal)) {
      completed = false;
    }
    else if (!upgradesEqual()) {
      completed = false;
    }
    return completed;
  }

  /**
  * the length the game has gone on for
  *
  * @return the length of the game so far
  */
  public int gameLength() {
    return actions.size();
  }

  /**
  * Returns list of actions game to to move towards goal
  *
  * @returns log of gamelement actions which we executed every game second
  */
  public ArrayList<GameElement> log() {
    return actions;
  }
}
