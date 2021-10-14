package model;

public interface IPlayer {
    PlayerAction chooseAction(Age age, int turn, Inventory inventoryLeft, Inventory inventoryRight);
}
