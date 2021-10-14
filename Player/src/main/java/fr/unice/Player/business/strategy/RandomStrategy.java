package fr.unice.Player.business.strategy;

import fr.unice.Player.business.utils.FunctionsHelper;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomStrategy extends PlayStrategy {
    private final Random random = new Random();

    /**
     * Appelé par la classe Game, renvoit une carte à poser ou à défausser
     * et il choisi une carte en Random
     */
    @Override
    public PlayerAction chooseAction(Inventory inventory, Inventory inventoryLeft, Inventory inventoryRight) {
        List<Card> cardsInHand = inventory.getCardsInHand();

        for (int i = 0; i < cardsInHand.size(); i++) {
            int cardNumber = random.nextInt(cardsInHand.size() - 1);
            List<Trade> cardCosts = cardsInHand.get(cardNumber).getCost();
            if (cardCosts.isEmpty()) {
                return PlayerAction.builder()
                        .card(cardsInHand.get(cardNumber))
                        .chosenReward(this.chooseReward(cardsInHand.get(cardNumber).getReward()))
                        .build();
            } else if (FunctionsHelper.canEvolveMyWonder(inventory)) {
                return PlayerAction.builder()
                        .card(cardsInHand.get(i))
                        .choice(PlayerAction.Choice.WONDER)
                        .build();
            }
        }
        return super.sacrificeCard(inventory.getCardsInHand().get(0));
    }

    @Override
    public Resources chooseReward(List<Trade> trades) {
        return trades.get(random.nextInt(trades.size() - 1)).getResource();
    }

    @Override
    public PlayerAction chooseActionFromList(List<PlayerAction> playableAction) {
        return playableAction.get(random.nextInt(playableAction.size() - 1));
    }

    @Override
    public String getStrategyName() {
        return "Random Strategy";
    }

    @Override
    public boolean maximumReached() {
        return false;
    }
}
