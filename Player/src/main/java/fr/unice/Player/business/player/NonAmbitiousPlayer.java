package fr.unice.Player.business.player;

import model.*;

import java.util.List;

public class NonAmbitiousPlayer extends StrategicPlayer {

    public NonAmbitiousPlayer(String name, Inventory inventory) {
        super(name, inventory);
    }

    @Override
    public PlayerAction chooseAction(Age age, int turn, Inventory inventoryLeft, Inventory inventoryRight) {
        return this.strategy.chooseAction(this.inventory, inventoryLeft, inventoryRight);
    }

    @Override
    public Resources chooseReward(List<Trade> trades) {
        return this.strategy.chooseReward(trades);
    }
}
