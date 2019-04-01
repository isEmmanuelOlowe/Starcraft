package alphacraft.engine.resources;

public class Addon extends Resource {

  private GameElement addTo;

  public Addon(GameElement name, int  mineralCost, int gasCost, int buildTime) {
    super(name, mineralCost, gasCost, buildTime, new GameElement[]{});
  }

  public void setBuidlingTo(GameElement building) {
    addTo = building;
  }

  public GameElement buildTo() {
    return addTo;
  }
}
