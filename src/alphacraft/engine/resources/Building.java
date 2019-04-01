package alphacraft.engine.resources;

/**
* Describes the behaviours of a building in Starcraft II.
*/
public class Building extends Resource {

  private int avaliable = 0;
  private int reactorAvaliable = 0;
  private int supplyProvided;
  private boolean hasAddon = false;
  private GameElement addon;

  /**
  * Creating building in game.
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

  /**
  * Wether or not this building has an addon.
  *
  * @return true if the building has an addon
  */
  public boolean hasAddon() {

    return hasAddon;
  }

  /**
  * Wether or not this building has a Tech Lab.
  *
  * @return true if the building has a Tech TECH_LAB
  */
  public boolean hasTechLab() {
    if (GameElement.TECH_LAB == addon) {
      return true;
    }
    return false;
  }

  /**
  * Adds an addon to a Building.
  *
  * @param addon the addon beign added to this building
  */
  public void addon(GameElement addon) {

    String[] type = addon.toString().split("_");
    //checks the extension to see which kind of addon is being added
    if (type.length == 2) {
      this.addon = GameElement.REACTOR;
    }
    else {
      this.addon = GameElement.TECH_LAB;
    }
    this.hasAddon = true;
  }

  /**
  * Gets the supply Provided by the build.
  *
  * @return the supply the building adds to the the players total
  */
  public int getSupplyProvided() {

    return supplyProvided;
  }

  /**
  * Sets the next time the building will be avaliable for used.
  *
  * @param buildTime the time taken to building resource
  * @param currentTime the current time in the game
  */
  public void setAvaliable(int buildTime, int currentTime) {

    //in the event that this building possess a REACTOR
    if (GameElement.REACTOR == addon && avaliable > currentTime) {
      reactorAvaliable = buildTime + currentTime;
    }
    else {
      avaliable = buildTime + currentTime;
    }

  }

  /**
  * Wether or not the building is avaliable for use.
  *
  * @param currentTime the currentTime in the game.
  * @return if avaliable for use.
  */
  public boolean isAvaliable(int currentTime) {

    if (avaliable <= currentTime) {
      if (reactorAvaliable < currentTime) {
        return true;
      }
    }
    return false;
  }
}
