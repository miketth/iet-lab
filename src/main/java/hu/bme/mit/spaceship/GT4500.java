package hu.bme.mit.spaceship;

/**
* A simple spaceship with two proton torpedo stores and four lasers
*/
public class GT4500 implements SpaceShip {

  private TorpedoStore primaryTorpedoStore;
  private TorpedoStore secondaryTorpedoStore;

  private boolean wasPrimaryFiredLast = false;

  public GT4500() {
    this.primaryTorpedoStore = new TorpedoStore(10);
    this.secondaryTorpedoStore = new TorpedoStore(10);
  }

  public boolean fireLaser(FiringMode firingMode) {
    // TODO not implemented yet
    return false;
  }

  private TorpedoStore getOtherTorpedoStore(TorpedoStore store) {
    if (store == primaryTorpedoStore) return secondaryTorpedoStore;
    else if (store == secondaryTorpedoStore) return primaryTorpedoStore;
  
    throw new IllegalArgumentException("torpedo store not found");
  }

  /**
  * Tries to fire the torpedo stores of the ship.
  *
  * @param firingMode how many torpedo bays to fire
  * 	SINGLE: fires only one of the bays.
  * 			- For the first time the primary store is fired.
  * 			- To give some cooling time to the torpedo stores, torpedo stores are fired alternating.
  * 			- But if the store next in line is empty, the ship tries to fire the other store.
  * 			- If the fired store reports a failure, the ship does not try to fire the other one.
  * 	ALL:	tries to fire both of the torpedo stores.
  *
  * @return whether at least one torpedo was fired successfully
  */
  @Override
  public boolean fireTorpedo(FiringMode firingMode) {

    boolean firingSuccess = false;

    switch (firingMode) {
      case SINGLE:
        TorpedoStore store = primaryTorpedoStore;
        if (wasPrimaryFiredLast) {
          store = secondaryTorpedoStore;
        }
        try {
          firingSuccess = store.fire(1);
          wasPrimaryFiredLast = false;
        } catch(IllegalArgumentException e) {
          // although primary was fired last time, but the secondary is empty
          // thus try to fire primary again
          try {
            store = getOtherTorpedoStore(store);
            firingSuccess = store.fire(1);
            wasPrimaryFiredLast = true;
          } catch (IllegalArgumentException ignored) { /* */ }
        }
        break;

      case ALL:
        // try to fire both of the torpedo stores
        if (!primaryTorpedoStore.isEmpty() && !secondaryTorpedoStore.isEmpty()) {
          firingSuccess = primaryTorpedoStore.fire(1) && secondaryTorpedoStore.fire(1);
        }        

        break;
    }

    return firingSuccess;
  }

}
