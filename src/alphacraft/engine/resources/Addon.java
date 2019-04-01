package alphacraft.engine.resources;

/**
* Describes an addon to a building in game
*/
public class Addon extends Resource {

  //The building it is being added to
  private GameElement addTo;

  /**
  * Creates a new addon.
  *
  * @param name the name of the addon
  * @param mineralCost the mineral cost of the addon
  * @param gasCost the gas cost of the addon
  * @param buildTime how long it take to add the addon
  */
  public Addon(GameElement name, int  mineralCost, int gasCost, int buildTime) {

    super(name, mineralCost, gasCost, buildTime, new GameElement[]{});
  }


  /**
  * Sets the building in which the addon is being added.
  *
  * @param building the building the addon is being added
  */
  public void setBuidlingTo(GameElement building) {

    addTo = building;
  }

  /**
  * the building the addon is being added to.
  *
  * @return the buildin the addon is being added to.
  */
  public GameElement buildTo() {

    return addTo;
  }
}
