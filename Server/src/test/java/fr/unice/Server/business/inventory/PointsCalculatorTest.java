package fr.unice.Server.business.inventory;


import fr.unice.Server.business.board.CardService;
import fr.unice.Server.business.game.PointsCalculator;
import model.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PointsCalculatorTest {

    @Test
    public void calculGoldPoints() {
        AbstractPlayer playerMock = Mockito.mock(AbstractPlayer.class);
        Inventory inventory = new Inventory();
        when(playerMock.getInventory()).thenReturn(inventory);

        assertEquals(1, PointsCalculator.calculGoldPoints(playerMock.getInventory()));

        playerMock.getInventory().addQtyResource(Resources.COINS, 3);
        assertEquals(2, PointsCalculator.calculGoldPoints(playerMock.getInventory()));
    }

    @Test
    void calculCivilPoints() throws Exception {
        AbstractPlayer playerMock = Mockito.mock(AbstractPlayer.class);
        Inventory inventory = new Inventory();
        when(playerMock.getInventory()).thenReturn(inventory);

        List<Card> cardsInHand = new ArrayList<>();
        cardsInHand.add(new Card("Baths", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.VICTORY_POINT, 10)), null, Card.Type.CIVIL, null, null));
        cardsInHand.add(new Card("Workshop", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.VICTORY_POINT, 1)), null, Card.Type.CIVIL, null, null));
        playerMock.getInventory().setCardsInHand(cardsInHand);
        playerMock.getInventory().cardPlayed(new Card("Baths", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.VICTORY_POINT, 10)), null, Card.Type.CIVIL, null, null));

        assertEquals(10, PointsCalculator.calculCivilPoints(playerMock.getInventory()));

        Card card = new Card("Workshop", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.VICTORY_POINT, 1)), null, Card.Type.CIVIL, null, null);
        new CardService().pay(card, playerMock);

        assertEquals(11, PointsCalculator.calculCivilPoints(playerMock.getInventory()));
    }

    @Test
    void calculMilitaryPoints() throws Exception {
        AbstractPlayer playerMock = Mockito.mock(AbstractPlayer.class);
        Inventory inventory = new Inventory();
        when(playerMock.getInventory()).thenReturn(inventory);

        List<Card> cardsInHand = new ArrayList<>();
        cardsInHand.add(new Card("Baths", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.MILITARY_POINT, 10)), null, Card.Type.MILITARY, null, null));
        cardsInHand.add(new Card("Workshop", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.MILITARY_POINT, 1)), null, Card.Type.MILITARY, null, null));
        playerMock.getInventory().setCardsInHand(cardsInHand);

        Card card = new Card("Baths", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.MILITARY_POINT, 10)), null, Card.Type.MILITARY, null, null);
        new CardService().pay(card, playerMock);

        assertEquals(10, playerMock.getInventory().getMilitaryPointsOfPlayer());
    }

    @Test
    void calculSciencePoints() throws Exception {
        AbstractPlayer playerMock = Mockito.mock(AbstractPlayer.class);
        Inventory inventory = new Inventory();
        when(playerMock.getInventory()).thenReturn(inventory);

        Card card1 = new Card("Baths", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.PHYSICS, 1)), null, Card.Type.SCIENTIFIC, null, null);
        List<Card> cardsInHand = new ArrayList<>();
        cardsInHand.add(card1);
        inventory.setCardsInHand(cardsInHand);
        new CardService().pay(card1, playerMock);
        assertEquals(1, PointsCalculator.calculSciencePoints(playerMock.getInventory()));

        Card card2 = new Card("Workshop", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.PHYSICS, 1)), null, Card.Type.SCIENTIFIC, null, null);
        cardsInHand = new ArrayList<>();
        cardsInHand.add(card2);
        inventory.setCardsInHand(cardsInHand);
        new CardService().pay(card2, playerMock);
        assertEquals(4, PointsCalculator.calculSciencePoints(playerMock.getInventory()));
    }
}
