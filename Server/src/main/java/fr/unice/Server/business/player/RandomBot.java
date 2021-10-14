package fr.unice.Server.business.player;

import model.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Random;

public class RandomBot implements IPlayerState {
    private final Random random = new Random();
    private final Player player;

    public RandomBot(Player player) {
        this.player = player;
    }

    /**
     * Appelé par la classe Game, renvoit une carte à poser ou à défausser
     * et il choisi une carte en Random
     */
    @Override
    public PlayerAction chooseAction(Age age, int turn, Inventory inventoryLeft, Inventory inventoryRight) {
        List<Card> cardsInHand = this.player.getInventory().getCardsInHand();

        for (int i = 0; i < cardsInHand.size(); i++) {
            //int cardNumber = random.nextInt(cardsInHand.size() - 1);
            int cardNumber = 0;
            List<Trade> cardCosts = cardsInHand.get(cardNumber).getCost();
            if (cardCosts.isEmpty()) {
                return PlayerAction.builder()
                        .card(cardsInHand.get(cardNumber))
                        .chosenReward(this.chooseReward(cardsInHand.get(cardNumber).getReward()))
                        .build();
            }
        }
        return PlayerAction.builder()
                .card(this.player.getInventory().getCardsInHand().get(0))
                .choice(PlayerAction.Choice.SACRIFICE)
                .build();
    }

    public Resources chooseReward(List<Trade> trades) {
        return trades.get(random.nextInt(trades.size() - 1)).getResource();
    }

    @Override
    public void announceWinner(String winnerName) {
    }

    @Override
    public void connected(SseEmitter emitter) {
        this.player.setPlayerState(new ConnectedPlayer(this.player, emitter));
    }

    @Override
    public void disconnected() {
    }
}
