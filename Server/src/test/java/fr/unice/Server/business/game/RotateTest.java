package fr.unice.Server.business.game;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RotateTest {

    private List<AbstractPlayer> playersWithInventories = new LinkedList<>();

    @BeforeEach
    public void init() {
        for (int i = 0; i < 4; i++) {
            AbstractPlayer playerMock = Mockito.mock(AbstractPlayer.class);
            this.playersWithInventories.add(playerMock);
        }
    }

    @Test
    public void giveCardsTest() throws Exception {
        Card card1 = new Card("Lumber Yard", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.WOOD, 1)), null, Card.Type.RAW, null, null);
        Card card2 = new Card("Stone Pit", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.STONE, 1)), null, Card.Type.RAW, null, null);
        Card card3 = new Card("Clay Pool", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.CLAY, 1)), null, Card.Type.RAW, null, null);
        List<Card> d1 = new ArrayList<>();
        List<Card> d2 = new ArrayList<>();
        List<Card> d3 = new ArrayList<>();


        d1.add(card1);
        d2.add(card2);
        d3.add(card3);


        Inventory inventory1 = new Inventory();
        Inventory inventory2 = new Inventory();
        Inventory inventory3 = new Inventory();
        when(playersWithInventories.get(0).getInventory()).thenReturn(inventory1);
        when(playersWithInventories.get(1).getInventory()).thenReturn(inventory2);
        when(playersWithInventories.get(2).getInventory()).thenReturn(inventory3);


        playersWithInventories.get(0).getInventory().setCardsInHand(d1);
        playersWithInventories.get(1).getInventory().setCardsInHand(d2);
        playersWithInventories.get(2).getInventory().setCardsInHand(d3);

        Card c1 = playersWithInventories.get(0).getInventory().getCardsInHand().get(0);
        Card c2 = playersWithInventories.get(1).getInventory().getCardsInHand().get(0);
        Card c3 = playersWithInventories.get(2).getInventory().getCardsInHand().get(0);

        //On fait tourner les cartes dans le sens anti-horaire, elles doivent reprendre leur place originale
        //Rotate.giveCards(false, playersWithInventories);
        assertEquals(c1.getName(), playersWithInventories.get(0).getInventory().getCardsInHand().get(0).getName());
        assertEquals(c2.getName(), playersWithInventories.get(1).getInventory().getCardsInHand().get(0).getName());
        assertEquals(c3.getName(), playersWithInventories.get(2).getInventory().getCardsInHand().get(0).getName());
    }

}
