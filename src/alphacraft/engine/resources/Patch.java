package alphacraft.engine.resources;

public class Patch {

  private double avaliableResource;

  public Patch(double avaliableResource) {
    this.avaliableResource = avaliableResource;
  }

  //the amount you are trying to take out.
  protected double remove(double amount) {
    double quantity;
    if (amount <= avaliableResource) {
      avaliableResource -= amount;
      quantity = amount;
    }
    else {
      quantity = avaliableResource;
      avaliableResource = 0;
    }
    return quantity;
  }

  public boolean depleted() {
    if (avaliableResource == 0) {
      return true;
    }
    return false;
  }
}
