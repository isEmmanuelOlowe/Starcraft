package alphacraft.engine.resources;
public class Unit extends Resource {

  private int supplyCost;

  /**
  * Creating building in game
  *
  * @param name the name of the game unit
  * @param buildTime the time taken to build game unit
  * @param mineralCost the mineral cost of the game unit
  * @param gasCost the gasCost of the game unit
  * @param supplyCost supply spent by building the current game unit
  * @param dependencies the dependencies of the game unit
  */
  public Unit(GameElement name, int mineralCost, int gasCost, int buildTime, int supplyCost, GameElement[] dependencies) {
    super(name, mineralCost, gasCost, buildTime, dependencies);
    this.supplyCost = supplyCost;
  }

  public int getSupplyCost() {
    return supplyCost;
  }
}
