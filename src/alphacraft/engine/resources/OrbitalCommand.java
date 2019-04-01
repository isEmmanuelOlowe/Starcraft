package alphacraft.engine.resources;

import java.util.ArrayList;

public class OrbitalCommand extends Building {

  private static final double energyRate = 0.5625;
  private static final double maxEnergy = 200.0;
  private static final int muleCost = 50;
  private static final int muleCode = -1;
  private static final int muleLifeSpan = 90;
  private static final GameElement[] depElements = {GameElement.COMMAND_CENTER, GameElement.BARRACKS};
  private static double currentEnergy = 0.0;
  private static int orbitalNo;
  private static ArrayList<Integer> mule;

  public OrbitalCommand() {
    super(GameElement.COMMAND_CENTER, 150, 0, 35, 10, depElements);
  }

  public static void init(){
    orbitalNo++;
  }

  public static void reset() {
    orbitalNo = 0;
    currentEnergy = 0;
    mule = new ArrayList<Integer>();
    currentEnergy = 0;
  }

  public static void disburseMULE(int currentTime) {
    if (muleCost <= currentEnergy) {
      mule.add(new Integer(currentTime + muleLifeSpan));
    }
    for (int i = 0; i < mule.size(); i++) {
      if (currentTime > mule.get(i).intValue()) {
        mule.remove(i);
      }
    }
    currentEnergy += orbitalNo * energyRate;
    if (currentEnergy > maxEnergy) {
      currentEnergy = maxEnergy;
    }
  }

  public static double MULELoad() {
    int noMules = mule.size();
    double load = 0;
    ArrayList<MineralPatch> mineralPatches = CommandCenter.getMineralPatches();
    while (noMules > 0 && MineralPatch.avaliablePatch() > 0){
      for (MineralPatch mineralPatch: mineralPatches) {
        if (!mineralPatch.depleted()) {
          load += mineralPatch.extract(muleCode);
          noMules--;
        }
      }
    }
    return load;
  }
}
