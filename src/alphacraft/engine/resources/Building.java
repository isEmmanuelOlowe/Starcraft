package alphacraft.engine.resources;

public class Building extends Resource {

  private int avaliable = 0;
  private int reactorAvaliable = 0;
  private int supplyProvided;
  private boolean hasAddon = false;
  private GameElement addon;
  /**
  * Creating building in game
  *
  * @param name the name of the game building
  * @param buildTime the time taken to build game building
  * @param mineralCost the mineral cost of the game building
  * @param gasCost the gasCost of the game building
  * @param supplyProvided supply provided by building the current game building
  * @param dependencies the dependencies of the game building
  */
  public Building(GameElement name, int mineralCost, int gasCost, int buildTime, int supplyProvided, GameElement dependencies[]) {
    super(name, mineralCost, gasCost, buildTime, dependencies);
    this.supplyProvided = supplyProvided;
  }

  public boolean hasAddon() {
    return hasAddon;
  }

  public boolean hasTechLab() {
    if (GameElement.TECH_LAB == addon) {
      return true;
    }
    return false;
  }
  public void addon(GameElement addon) {
    String[] type = addon.toString().split("_");
    if (type.length == 2) {
      this.addon = GameElement.REACTOR;
    }
    else {
      this.addon = GameElement.TECH_LAB;
    }
    this.hasAddon = true;
  }
  public int getSupplyProvided() {
    return supplyProvided;
  }

  public void setAvaliable(int buildTime, int currentTime) {
    if (GameElement.REACTOR == addon && avaliable > currentTime) {
      reactorAvaliable = buildTime + currentTime;
    }
    else {
      avaliable = buildTime + currentTime;
    }

  }

  public boolean isAvaliable(int currentTime) {
    if (avaliable <= currentTime) {
      if (reactorAvaliable < currentTime) {
        return true;
      }
    }
    return false;
  }
}
