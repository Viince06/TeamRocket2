package fr.unice.Server.business.board;

import model.AbstractPlayer;
import model.Card;
import model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DeckTest {

    private List<AbstractPlayer> playersWithInventories = new LinkedList<>();
    private Inventory inventory;
    private Deque<Card> cards;

    @BeforeEach
    public void init() {
        Deck deck = new Deck();
        deck.initialize();

        for (int i = 0; i < 4; i++) {
            AbstractPlayer playerMock = Mockito.mock(AbstractPlayer.class);
            this.playersWithInventories.add(playerMock);
        }
        cards = new ArrayDeque<>(deck.getCards());
    }

    @Test
    public void CardDistribution() {

        for (AbstractPlayer player : playersWithInventories) {
            Card card = cards.pop();
            this.inventory = new Inventory();
            when(player.getInventory()).thenReturn(inventory);
            player.getInventory().getCardsInHand().add(card);
            assertEquals(card.getName(), player.getInventory().getCardsInHand().get(0).getName());
        }
    }
}


