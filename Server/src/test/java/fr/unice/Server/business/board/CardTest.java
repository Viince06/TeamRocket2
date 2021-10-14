package fr.unice.Server.business.board;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CardTest {

    private AbstractPlayer player;

    @BeforeEach
    public void init() {
        AbstractPlayer playerMock = Mockito.mock(AbstractPlayer.class);
        this.player = playerMock;
        Inventory inventory = new Inventory();
        when(player.getInventory()).thenReturn(inventory);
    }

    // POUR MIEUX SE REPERER, ON FAIT TOUJOURS DANS L'ORDRE : Brown / Blue / Grey / Green / Red

    //Brown Cards

    @Test
    public void giveBrownReward() throws Exception {
        Card card = new Card("TestCard", Age.AGE_1, 4, Arrays.asList(new Trade(Resources.ORE, 1)), null, Card.Type.RAW, null, null);
        assertEquals(0, this.player.getInventory().getResources().get(Resources.ORE));
        new CardService().giveReward(card, this.player);
        assertEquals(1, this.player.getInventory().getResources().get(Resources.ORE));

    }

    //Blue Cards
    @Test
    public void giveBlueReward() throws Exception {
        Card card = new Card("TestCard", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.VICTORY_POINT, 1)), null, Card.Type.CIVIL, null, null);
        assertEquals(0, this.player.getInventory().getResources().get(Resources.VICTORY_POINT));
        new CardService().giveReward(card, this.player);
        assertEquals(1, this.player.getInventory().getResources().get(Resources.VICTORY_POINT));
    }

    //Grey Cards
    @Test
    public void giveGreyReward() throws Exception {
        Card card = new Card("TestCard", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.GLASS, 1)), null, Card.Type.HANDMADE, null, null);
        assertEquals(0, this.player.getInventory().getResources().get(Resources.GLASS));
        new CardService().giveReward(card, this.player);
        assertEquals(1, this.player.getInventory().getResources().get(Resources.GLASS));
    }

    //Green Cards
    @Test
    public void giveGreenReward() throws Exception {
        Card card = new Card("TestCard", Age.AGE_1, 3, Arrays.asList(new Trade(Resources.PHYSICS, 1)), null, Card.Type.SCIENTIFIC, null, null);
        assertEquals(0, this.player.getInventory().getResources().get(Resources.PHYSICS));
        new CardService().giveReward(card, this.player);
        assertEquals(1, this.player.getInventory().getResources().get(Resources.PHYSICS));
    }

    //Red Cards
    @Test
    public void giveRedReward() throws Exception {
        Card card = new Card("TestCard", Age.AGE_2, 3, Arrays.asList(new Trade(Resources.MILITARY_POINT, 2)), null, Card.Type.MILITARY, null, null);
        assertEquals(0, this.player.getInventory().getResources().get(Resources.MILITARY_POINT));
        new CardService().giveReward(card, this.player);
        assertEquals(2, this.player.getInventory().getResources().get(Resources.MILITARY_POINT));
    }

    @Test
    public void canPay() {
        Card firstCard = new Card("TestCard", Age.AGE_1, 6,
                Arrays.asList(
                        new Trade(Resources.WOOD, 1),
                        new Trade(Resources.CLAY, 3)),
                Arrays.asList(
                        new Trade(Resources.COINS, 1)), Card.Type.RAW, null, null);
        assertTrue(new CardService().canPay(firstCard, player.getInventory())); //True, parce que on a 3 coins

        Card secondCard = new Card("TestCard", Age.AGE_1, 6,
                Arrays.asList(
                        new Trade(Resources.WOOD, 1),
                        new Trade(Resources.CLAY, 3)),
                Arrays.asList(
                        new Trade(Resources.COINS, 4)), Card.Type.RAW, null, null);
        assertFalse(new CardService().canPay(secondCard, player.getInventory())); //False, parce que on a 3 coins, il en faut 4

        this.player.getInventory().addQtyResource(Resources.CLAY, 1);
        this.player.getInventory().addQtyResource(Resources.GLASS, 3);
        Card thirdCard = new Card("test", Age.AGE_1, 6,
                Arrays.asList(
                        new Trade(Resources.WOOD, 1),
                        new Trade(Resources.CLAY, 3)),
                Arrays.asList(
                        new Trade(Resources.GLASS, 4)), Card.Type.RAW, null, null);
        assertFalse(new CardService().canPay(thirdCard, player.getInventory()));

        Card fourthCard = new Card("test", Age.AGE_1, 6,
                Arrays.asList(
                        new Trade(Resources.WOOD, 1),
                        new Trade(Resources.CLAY, 3)),
                Arrays.asList(
                        new Trade(Resources.GLASS, 2),
                        new Trade(Resources.CLAY, 1),
                        new Trade(Resources.COINS, 3)), Card.Type.RAW, null, null);
        assertTrue(new CardService().canPay(fourthCard, player.getInventory()));
    }
}