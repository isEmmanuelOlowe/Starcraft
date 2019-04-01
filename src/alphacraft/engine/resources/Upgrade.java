package alphacraft.engine.resources;

public class Upgrade extends Resource {

  public Upgrade (GameElement name, int mineralCost, int gasCost, int buildTime, GameElement[] dependencies) {
    super(name, mineralCost, gasCost, buildTime, dependencies);
  }

}
