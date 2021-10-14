package fr.unice.Player.business.strategy;


import model.Inventory;
import model.PlayerAction;
import model.Resources;
import model.Trade;

import java.util.ArrayList;
import java.util.List;

public interface IStrategy {
    PlayerAction chooseAction(Inventory inventory, Inventory inventoryLeft, Inventory inventoryRight);

    Resources chooseReward(List<Trade> trades);

    PlayerAction chooseActionFromList(List<PlayerAction> playableAction);

    String getStrategyName();

    boolean maximumReached();
}