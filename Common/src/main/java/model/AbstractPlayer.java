package model;

public abstract class AbstractPlayer {

    protected String name;
    protected Inventory inventory;

    public AbstractPlayer(String name, Inventory inventory) {
        this.name = name;
        this.inventory = inventory;
    }

    public String getName() {
        return name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void resetInventory() {
        this.inventory = new Inventory();
    }

    public abstract PlayerAction chooseAction(Age age, int turn, Inventory inventoryLeft, Inventory inventoryRight) throws Exception;
}
