package alphacraft.engine.resources;

public class GasPatch extends Patch {
  private static final double maxGas = 2500.0;
  private static final double workerRate = 38.0/60.0;
  private static int avaliable;
  private static int inactive;
  private boolean refinery = false;

  public GasPatch() {
    super(maxGas);
    inactive++;
  }

  public static void reset() {
    avaliable = 0;
    inactive = 0;
  }
  public void addRefinery() {
    avaliable++;
    inactive--;
    refinery = true;
  }

  public static int avaliablePatch () {
    return avaliable;
  }

  public boolean hasRefinery() {
    return refinery;
  }

  public static boolean needRefinery() {
    if (inactive > 0) {
      return true;
    }
    return false;
  }
  public double extract() {
    double quantity = remove(workerRate);
    if (quantity != workerRate) {
      avaliable--;
    }
    return quantity;
  }
}
