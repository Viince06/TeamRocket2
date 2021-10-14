package fr.unice.Server.business.game;

import model.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;

public class RefereeTest {

    private AbstractPlayer player;
    private Referee referee;
    private Inventory emptyInventory;


    @BeforeEach
    public void init() {
        this.referee = new Referee();
        this.emptyInventory = new Inventory();

        AbstractPlayer playerMock = Mockito.mock(AbstractPlayer.class);
        this.player = playerMock;
        Inventory inventory = new Inventory();
        when(player.getInventory()).thenReturn(inventory);
    }

    @Test
    public void isChoicePlayableTest1() {
        PlayerAction.Choice choice = PlayerAction.Choice.PLAY;
        Card card = new Card("TestCard", Age.AGE_1, 1, Arrays.asList(new Trade(Resources.WOOD, 10)), null, Card.Type.RAW, null, null);
        player.getInventory().setCardsInHand(Collections.singletonList(card));
        PlayerAction playerAction = PlayerAction.builder().card(card).choice(choice).build();
        assertTrue(referee.isChoicePlayable(playerAction, player.getInventory(), emptyInventory, emptyInventory));

    }

    @Test
    public void isChoicePlayableTest2() {
        PlayerAction.Choice choice = PlayerAction.Choice.SACRIFICE;
        Card card = new Card("TestCard", Age.AGE_1, 1, Arrays.asList(new Trade(Resources.WOOD, 10)), null, Card.Type.RAW, null, null);
        player.getInventory().setCardsInHand(Collections.singletonList(card));
        PlayerAction playerAction = PlayerAction.builder().card(card).choice(choice).build();
        assertTrue(referee.isChoicePlayable(playerAction, player.getInventory(), emptyInventory, emptyInventory));
    }


    @Test
    public void isChoicePlayableTest3() {
        List<Trade> cost = new ArrayList<>();
        cost.add(new Trade(Resources.WOOD, 1));

        Card card = new Card("TestCard", Age.AGE_1, 1, Arrays.asList(new Trade(Resources.WOOD, 10)), cost, Card.Type.RAW, null, null);
        List<Exchange> listOfExchange = new ArrayList<>();
        player.getInventory().setCardsInHand(Collections.singletonList(card));
        PlayerAction playerAction = PlayerAction.builder().card(card).listOfExchange(listOfExchange).build();
        assertFalse(referee.isChoicePlayable(playerAction, player.getInventory(), emptyInventory, emptyInventory));

    }

    @Test
    public void isChoicePlayableTest4() {
        List<Trade> cost = new ArrayList<>();
        cost.add(new Trade(Resources.WOOD, 1));
        Card card = new Card("TestCard", Age.AGE_1, 1, Arrays.asList(new Trade(Resources.WOOD, 10)), cost, Card.Type.RAW, null, null);
        List<Exchange> listOfExchange = new ArrayList<>();
        listOfExchange.add(new Exchange(true, new Trade(Resources.WOOD, 1)));
        player.getInventory().setCardsInHand(Collections.singletonList(card));
        PlayerAction playerAction = PlayerAction.builder().card(card).listOfExchange(listOfExchange).build();
        emptyInventory.addQtyResource(Resources.WOOD, 1);
        assertTrue(referee.isChoicePlayable(playerAction, player.getInventory(), emptyInventory, emptyInventory));
    }
}