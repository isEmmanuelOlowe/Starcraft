package alphacraft.engine.resources;

/*
* Describes an upgrade in StarCraftII
*/
public class Upgrade extends Resource {

  /**
  * creates a new Upgrade Resource
  *
  * @param name the name of the GameElement
  * @param mineralCost the mineral cost of the upgrades
  * @param gasCost the gas cost of an upgrade
  * @param buildTime the time it takes to build
  * @param dependencies the dependences the upgrades
  */
  public Upgrade (GameElement name, int mineralCost, int gasCost, int buildTime, GameElement[] dependencies) {
    super(name, mineralCost, gasCost, buildTime, dependencies);
  }

}
