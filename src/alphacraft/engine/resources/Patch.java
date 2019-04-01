package alphacraft.engine.resources;


/**
* Describes the behaviour of any resource patch in starlob 2
*/
public class Patch {

  private double avaliableResource;

  /**
  * creates a new patch
  *
  * @param avaliableResource the maximum amount resource the patch
  */
  public Patch(double avaliableResource) {
    this.avaliableResource = avaliableResource;
  }

  /**
  * Allows for extraction  of resource from a patch
  *
  * @param amount  the amount of mineral that is wished to be extracted from the gas patch
  */
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

  /**
  * checks if a patch has any resource tanding
  *
  * @return true is there are no resources remanding 
  */
  public boolean depleted() {
    if (avaliableResource == 0) {
      return true;
    }
    return false;
  }
}
