package alphacraft.engine.resources;

/**
* Describes games reources.
*/
public class Resource {

  private GameElement name;
  private int mineralCost;
  private int gasCost;
  private int buildTime;
  private GameElement[] dependencies;

  //private int avaliable;

  /**
  * Creating game resources
  *
  * @param name the name of the game resource
  * @param buildTime the time taken to build game resource
  * @param mineralCost the mineral cost of the game resource
  * @param gasCost the gasCost of the game resource
  * @param dependencies the dependencies of the game resource
  */
  public Resource(GameElement name, int mineralCost, int gasCost, int buildTime, GameElement dependencies[]){
    this.name = name;
    this.buildTime = buildTime;
    this.mineralCost = mineralCost;
    this.gasCost = gasCost;
    this.dependencies = dependencies;
  }

  /**
  * Gets the name of the game resource
  *
  * @return the name of the game resource
  */
  public GameElement getName() {
    return name;
  }

  /**
  * Gets the mineral cost of a game resource
  *
  * @return mineralCost
  */
  public int getMineralCost() {
    return mineralCost;
  }

  /**
  * The gas cost to build this resource
  *
  * @return the gas cost of the resource
  */
  public int getGasCost() {
    return gasCost;
  }

  /**
  * The time take to build this resource
  *
  * @return the time it takes to build the resource
  */
  public int getBuildTime() {
    return buildTime;
  }

  /**
  * Returns the items that are required to be built be for this resource can be built.
  *
  * @return the dependencies of resource
  */
  public GameElement[] getDependencies() {
    return dependencies;
  }
}
