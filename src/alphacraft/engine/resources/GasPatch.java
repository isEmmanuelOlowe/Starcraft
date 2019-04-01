package alphacraft.engine.resources;

/**
* Describes the behaviour of gas patches in starcraft 2
*/
public class GasPatch extends Patch {
  //maximum gas the patch has avaliable
  private static final double maxGas = 2500.0;
  //rate at which it can be removed
  private static final double workerRate = 38.0/60.0;
  //if there are any gas patches avliable for uses
  private static int avaliable;
  private static int inactive;
  private boolean refinery = false;

  /**
  * Creates a new Gas Patch
  */
  public GasPatch() {
    super(maxGas);
    inactive++;
  }

  /**
  * Resets the Gas Patch globally to initial static
  */
  public static void reset() {
    avaliable = 0;
    inactive = 0;
  }

  /**
  * adds a refinery to the gas patch
  */
  public void addRefinery() {
    avaliable++;
    inactive--;
    refinery = true;
  }


  /**
  * the number of gas patches avaliable for use.
  *
  * @return the number of patches avaliable
  */
  public static int avaliablePatch () {
    return avaliable;
  }

  /**
  * Checks if the gas patch has a refinery
  *
  * @return true if it has a refiney
  */
  public boolean hasRefinery() {
    return refinery;
  }

  /**
  * Wether or not the mineralpatches need another refinery
  */
  public static boolean needRefinery() {
    if (inactive > 0) {
      return true;
    }
    return false;
  }

  /**
  * Removes an amount of gas from a mineral.
  */
  public double extract() {
    double quantity = remove(workerRate);
    if (quantity != workerRate) {
      avaliable--;
    }
    return quantity;
  }
}
