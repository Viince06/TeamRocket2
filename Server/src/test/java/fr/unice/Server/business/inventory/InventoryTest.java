package fr.unice.Server.business.inventory;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    public void init() {
        this.inventory = new Inventory();
    }

    @Test
    void checkCardInventory() {
        List<Card> cardsInHand = new ArrayList<>();
        Card toBePlayed = new Card("Test", Age.AGE_1, 4, Arrays.asList(new Trade(Resources.ORE, 1)), null, Card.Type.RAW, null, null);
        cardsInHand.add(toBePlayed);
        inventory.setCardsInHand(cardsInHand);
        inventory.cardPlayed(toBePlayed);

        assertEquals(0, inventory.getCardsInHand().size());
        assertEquals(1, inventory.getCardsPlayed().size());
    }

    @Test
    void getQtyResource() {
        assertEquals(3, inventory.getQtyResource(Resources.COINS));
        inventory.addQtyResource(Resources.COINS, 2);
        assertEquals(5, inventory.getQtyResource(Resources.COINS));
    }
}
