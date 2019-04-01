package alphacraft.engine.resources;

/**
* Correlates GameElement to corresponding class.
*/
public class ResourceHandler {

  public static Resource getResource(GameElement name) {
    /*
    *http://java-performance.info/string-switch-implementation/
    * better performance from switch statements.
    */
    Resource resource;
    GameElement[] dependencies = null;
    switch (name) {
      case WAIT:
        dependencies = new GameElement[]{};
        resource = null;
        break;
      /**
      * SELECTION BLOCK FOR BUILDINGS
      */
      case ARMORY:
        dependencies = new GameElement[]{GameElement.FACTORY};
        resource = new Building(GameElement.ARMORY, 150, 100, 65, 0, dependencies);
        break;
      case BARRACKS:
        dependencies = new GameElement[]{GameElement.SUPPLY_DEPOT};
        resource = new Building(GameElement.BARRACKS, 150, 0, 65, 0, dependencies);
        break;
      case COMMAND_CENTER:
        resource = new CommandCenter();
        break;
      case ENGINEERING_BAY:
        //no dependencies since there will always be a command center since you start with one.
        dependencies = new GameElement[]{};
        resource = new Building(GameElement.ENGINEERING_BAY, 125, 0, 35, 0, dependencies);
        break;
      case FACTORY:
        dependencies = new GameElement[] {GameElement.BARRACKS};
        resource = new Building(GameElement.FACTORY, 150, 100, 60, 0, dependencies);
        break;
      case FUSION_CORE:
        dependencies = new GameElement[] {GameElement.STARPORT};
        resource = new Building(GameElement.FUSION_CORE, 150, 150, 65, 0, dependencies);
        break;
      case GHOST_ACADEMY:
        dependencies = new GameElement[] {GameElement.BARRACKS};
        resource = new Building(GameElement.GHOST_ACADEMY, 150, 100, 40, 0, dependencies);
        break;
      case ORBITAL_COMMAND:
        //return here
        resource = new OrbitalCommand();
        break;
      case REFINERY:
        dependencies = new GameElement[]{};
        resource = new Building(GameElement.REFINERY, 75, 0, 30, 0, dependencies);
        break;
      case STARPORT:
        dependencies = new GameElement[]{GameElement.FACTORY};
        resource = new Building(GameElement.STARPORT, 150, 100, 50, 0, dependencies);
        break;
      case SUPPLY_DEPOT:
        dependencies = new GameElement[]{};
        resource = new Building(GameElement.SUPPLY_DEPOT, 100, 0, 30, 8, dependencies);
        break;

      /*
      * SELECTION BLOCK FOR ADDONS
      * NO break since they all return same thing.
      */
      case FACTORY_TECH_LAB:
      case STARPORT_TECH_LAB:
      case BARRACKS_TECH_LAB:
        resource = new Addon(GameElement.TECH_LAB, 50, 25, 25);
        break;
      case FACTORY_REACTOR:
      case STARPORT_REACTOR:
      case BARRACKS_REACTOR:
        resource = new Addon(GameElement.REACTOR, 50, 50, 50);
        break;


      /*
      * SELECTION BLOCK FOR UNTIS
      */
      case BANSHEE:
        dependencies = new GameElement[]{GameElement.STARPORT, GameElement.STARPORT_TECH_LAB};
        resource = new Unit(GameElement.BANSHEE, 50, 0, 17, 1, dependencies);
        break;
      case BATTLECRUISER:
        dependencies = new GameElement[] {GameElement.STARPORT, GameElement.STARPORT_TECH_LAB, GameElement.FUSION_CORE};
        resource = new Unit(GameElement.BATTLECRUISER, 400, 300, 90, 6, dependencies);
        break;
      case GHOST:
        dependencies = new GameElement[] {GameElement.BARRACKS, GameElement.BARRACKS_TECH_LAB, GameElement.GHOST_ACADEMY};
        resource = new Unit(GameElement.GHOST, 200, 100, 40, 2, dependencies);
        break;
      case HELLION:
        dependencies = new GameElement[] {GameElement.FACTORY};
        resource = new Unit(GameElement.HELLION, 100, 0, 30, 1, dependencies);
        break;
      case MARAUDER:
        dependencies = new GameElement[] {GameElement.BARRACKS, GameElement.BARRACKS_TECH_LAB};
        resource = new Unit(GameElement.MARAUDER, 100, 25, 30, 2, dependencies);
        break;
      case MARINE:
        dependencies = new GameElement[] {GameElement.BARRACKS};
        resource = new Unit(GameElement.MARINE, 50, 0 , 25, 1, dependencies);
        break;
      case MEDIVAC:
        dependencies = new GameElement[] {GameElement.STARPORT};
        resource = new Unit(GameElement.MEDIVAC, 100, 100, 42, 2, dependencies);
        break;
      case RAVEN:
        dependencies = new GameElement[] {GameElement.STARPORT, GameElement.STARPORT_TECH_LAB};
        resource = new Unit(GameElement.RAVEN, 100, 200, 60, 6, dependencies);
        break;
      case REAPER:
        dependencies = new GameElement[] {GameElement.BARRACKS, GameElement.BARRACKS_TECH_LAB};
        resource = new Unit(GameElement.REAPER, 50, 50, 45, 1, dependencies);
        break;
      case SCV:
        dependencies = new GameElement[]{GameElement.COMMAND_CENTER};
        resource = new Unit(GameElement.SCV, 50, 0, 17, 1, dependencies);
        break;
      case SIEGE_TANK:
        dependencies = new GameElement[] {GameElement.FACTORY, GameElement.FACTORY_TECH_LAB};
        resource = new Unit(GameElement.SIEGE_TANK, 150, 125, 45, 3, dependencies);
        break;
      case THOR:
        dependencies = new GameElement[] {GameElement.FACTORY, GameElement.FACTORY_TECH_LAB, GameElement.ARMORY};
        resource = new Unit(GameElement.THOR, 300, 200, 60, 6, dependencies);
        break;
      case VIKING:
        dependencies = new GameElement[] {GameElement.STARPORT};
        resource = new Unit(GameElement.VIKING, 150, 75, 42, 2, dependencies);
        break;

      /*
      * SELECTION BLOCK: for Infantry upgrades
      */
      case INFANTRY_WEAPON_1:
        dependencies = new GameElement[]{GameElement.ENGINEERING_BAY};
        resource = new Upgrade(GameElement.INFANTRY_WEAPON_1, 100, 100, 160, dependencies);
        break;
      case INFANTRY_WEAPON_2:
        dependencies = new GameElement[]{GameElement.ENGINEERING_BAY, GameElement.INFANTRY_WEAPON_1, GameElement.ARMORY};
        resource = new Upgrade(GameElement.INFANTRY_WEAPON_2, 175, 175, 190, dependencies);
        break;
      case INFANTRY_WEAPON_3:
        //Finding Armory here is redunant as amory must already exist to have created Weapon 2
        dependencies = new GameElement[]{GameElement.ENGINEERING_BAY, GameElement.INFANTRY_WEAPON_2};
        resource = new Upgrade(GameElement.INFANTRY_WEAPON_3, 250, 250, 220, dependencies);
        break;
      case INFANTRY_ARMOR_1:
        dependencies = new GameElement[]{GameElement.ENGINEERING_BAY};
        resource = new Upgrade(GameElement.INFANTRY_ARMOR_1, 100, 100, 160, dependencies);
        break;
      case INFANTRY_ARMOR_2:
        dependencies = new GameElement[]{GameElement.ENGINEERING_BAY, GameElement.INFANTRY_ARMOR_1, GameElement.ARMORY};
        resource = new Upgrade(GameElement.INFANTRY_ARMOR_2, 175, 175, 190, dependencies);
        break;
      case INFANTRY_ARMOR_3:
        //Finding Armory here is redunant as amory must already exist to have created Weapon 2
        dependencies = new GameElement[]{GameElement.ENGINEERING_BAY, GameElement.INFANTRY_ARMOR_2};
        resource = new Upgrade(GameElement.INFANTRY_ARMOR_3, 250, 250, 220, dependencies);
        break;

      /*
      * SELECTION BLOCK: for Vehicle upgrade
      */
      case VEHICLE_WEAPON_1:
        dependencies = new GameElement[]{GameElement.ARMORY};
        resource = new Upgrade(GameElement.VEHICLE_WEAPON_1, 100, 100, 160, dependencies);
        break;
      case VEHICLE_WEAPON_2:
        dependencies = new GameElement[]{GameElement.ARMORY, GameElement.VEHICLE_WEAPON_1};
        resource = new Upgrade(GameElement.VEHICLE_WEAPON_2, 175, 175, 190, dependencies);
        break;
      case VEHICLE_WEAPON_3:
        dependencies = new GameElement[]{GameElement.ARMORY, GameElement.VEHICLE_WEAPON_2};
        resource = new Upgrade(GameElement.VEHICLE_WEAPON_3, 250, 250, 220, dependencies);
        break;
      case VEHICLE_ARMOR_1:
        dependencies = new GameElement[]{GameElement.ARMORY};
        resource = new Upgrade(GameElement.VEHICLE_ARMOR_1, 100, 100, 160, dependencies);
        break;
      case VEHICLE_ARMOR_2:
        dependencies = new GameElement[]{GameElement.ARMORY, GameElement.VEHICLE_ARMOR_1};
        resource = new Upgrade(GameElement.VEHICLE_ARMOR_2, 175, 175, 190, dependencies);
        break;
      case VEHICLE_ARMOR_3:
        dependencies = new GameElement[]{GameElement.ARMORY, GameElement.VEHICLE_ARMOR_2};
        resource = new Upgrade(GameElement.VEHICLE_ARMOR_3, 250, 250, 220, dependencies);
        break;

      /*
      * SELECTION BLOCK: for SHIP upgrade
      */
      case SHIP_WEAPON_1:
        dependencies = new GameElement[]{GameElement.ARMORY};
        resource = new Upgrade(GameElement.SHIP_WEAPON_1, 100, 100, 160, dependencies);
        break;
      case SHIP_WEAPON_2:
        dependencies = new GameElement[]{GameElement.ARMORY, GameElement.SHIP_WEAPON_1};
        resource = new Upgrade(GameElement.SHIP_WEAPON_2, 175, 175, 190, dependencies);
        break;
      case SHIP_WEAPON_3:
        dependencies = new GameElement[]{GameElement.ARMORY, GameElement.SHIP_WEAPON_2};
        resource = new Upgrade(GameElement.SHIP_WEAPON_3, 250, 250, 220, dependencies);
        break;
      case SHIP_ARMOR_1:
        dependencies = new GameElement[]{GameElement.ARMORY};
        resource = new Upgrade(GameElement.SHIP_ARMOR_1, 100, 100, 160, dependencies);
        break;
      case SHIP_ARMOR_2:
        dependencies = new GameElement[]{GameElement.ARMORY, GameElement.SHIP_ARMOR_1};
        resource = new Upgrade(GameElement.SHIP_ARMOR_2, 175, 175, 190, dependencies);
        break;
      case SHIP_ARMOR_3:
        dependencies = new GameElement[]{GameElement.ARMORY, GameElement.SHIP_ARMOR_2};
        resource = new Upgrade(GameElement.SHIP_ARMOR_3, 250, 250, 220, dependencies);
        break;
      default:
        resource = new Resource(GameElement.WAIT, 0, 0, 0, null);
    }

    return resource;
  }
}
