package alphacraft.engine.resources;
/**
* Describes the behaviour of Mineral Patches in starcraft II
*/
public class MineralPatch extends Patch {

  private static final double maxMineral = 1500.0;
  private static final double workerRate1 = 41.0 / 60.0;
  private static final double workerRate3 = 20.0 / 60.0;
  private static final double muleRate = 170.0 /60.0 ;
  private static int avaliable = 0;

  /**
  * Creates a new Mineral Patch
  */
  public MineralPatch() {
    super(maxMineral);
    avaliable++;
  }

  /**
  * resets static avaliable quantity to base state
  */
  public static void reset () {
    avaliable = 0;
  }

  /**
  * the number of patches
  *
  * @return the number of patches avaliable
  */
  public static int avaliablePatch () {
    return avaliable;
  }

  public double extract(int worker) {
    double quantity;
    double workerRate;
    if (worker == -1){
      workerRate = muleRate;
    }
    else if (worker < 2) {
      workerRate = workerRate1;
    }
    else {
      workerRate = workerRate3;
    }
    quantity = remove(workerRate);

    if (quantity != workerRate) {
      avaliable--;
    }
    return quantity;
  }
}
