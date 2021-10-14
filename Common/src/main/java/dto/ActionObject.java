package dto;

import model.Age;
import model.Inventory;

import java.util.UUID;

public class ActionObject {
    private UUID gameUuid;
    private Age age;
    private int turn;
    private Inventory playerInventory;
    private Inventory inventoryLeft;
    private Inventory inventoryRight;

    public ActionObject() {
    }

    public ActionObject(Age age, int turn, Inventory playerInventory, Inventory inventoryLeft, Inventory inventoryRight) {
        this.age = age;
        this.turn = turn;
        this.playerInventory = playerInventory;
        this.inventoryLeft = inventoryLeft;
        this.inventoryRight = inventoryRight;
    }

    public ActionObject(UUID gameUuid, Age age, int turn, Inventory playerInventory, Inventory inventoryLeft, Inventory inventoryRight) {
        this.gameUuid = gameUuid;
        this.age = age;
        this.turn = turn;
        this.playerInventory = playerInventory;
        this.inventoryLeft = inventoryLeft;
        this.inventoryRight = inventoryRight;
    }

    public Age getAge() {
        return age;
    }

    public void setAge(Age age) {
        this.age = age;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Inventory getPlayerInventory() {
        return playerInventory;
    }

    public void setPlayerInventory(Inventory playerInventory) {
        this.playerInventory = playerInventory;
    }

    public Inventory getInventoryLeft() {
        return inventoryLeft;
    }

    public void setInventoryLeft(Inventory inventoryLeft) {
        this.inventoryLeft = inventoryLeft;
    }

    public Inventory getInventoryRight() {
        return inventoryRight;
    }

    public void setInventoryRight(Inventory inventoryRight) {
        this.inventoryRight = inventoryRight;
    }
}
