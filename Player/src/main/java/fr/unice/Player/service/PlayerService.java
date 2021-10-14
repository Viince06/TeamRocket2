package fr.unice.Player.service;

import fr.unice.Player.business.strategy.EasyStrategy;
import fr.unice.Player.business.strategy.IStrategy;
import model.Inventory;
import model.PlayerAction;
import model.Resources;
import model.Trade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private IStrategy playerStrategy = new EasyStrategy();

    public PlayerAction chooseAction(Inventory inventory, Inventory inventoryLeft, Inventory inventoryRight) throws Exception {
        return playerStrategy.chooseAction(inventory, inventoryLeft, inventoryRight);
    }

    public void showWinner(String winnerName) {
        System.out.println(winnerName + " is the winner");
    }

    public Resources chooseReward(List<Trade> Trades){
        return playerStrategy.chooseReward(Trades);
    }
}
