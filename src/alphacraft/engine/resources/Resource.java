package alphacraft.engine.resources;

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

  public int getGasCost() {
    return gasCost;
  }

  public int getBuildTime() {
    return buildTime;
  }

  public GameElement[] getDependencies() {
    return dependencies;
  }

  // public void setAvaliable(String curentTime) {
  //   avaliable += buildTime + currentTime;
  // }
  //
  // public boolean isActive(int currentTime) {
  //   boolean active = false;
  //   if (currentTime >= avaliable) {
  //     active = true;
  //   }
  //   return active;
  // }
}
