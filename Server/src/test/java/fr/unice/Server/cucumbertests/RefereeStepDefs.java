package fr.unice.Server.cucumbertests;


import fr.unice.Server.business.game.Referee;
import io.cucumber.java8.En;
import model.*;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class RefereeStepDefs implements En {
    Referee referee = new Referee();
    Inventory playerInventory;
    PlayerAction playerAction;
    PlayerAction.Choice choice;
    Card card;
    List<AbstractPlayer> listOfPlayers;
    Inventory emptyInventory = new Inventory();


    public RefereeStepDefs() {
        Given("a player with his inventory and name {string}",
                (String playerName) ->
                {
                    AbstractPlayer player = Mockito.mock(AbstractPlayer.class);
                    Inventory inven=new Inventory();
                    when(player.getInventory()).thenReturn(inven);
                    playerInventory = player.getInventory();
                });


        When("The player plays a card {string} with cost set to null",
                (String nameOfCard) -> {
                    choice = PlayerAction.Choice.PLAY;
                    card = new Card(nameOfCard, Age.AGE_1, 1, Arrays.asList(new Trade(Resources.WOOD, 10)), null, Card.Type.RAW, null, null);
                    playerInventory.setCardsInHand(Collections.singletonList(card));
                    playerAction = PlayerAction.builder().card(card).choice(choice).build();

                });

        Then("The move is a valid one as cost is {string}", (String cost) -> {
            assertTrue(referee.isChoicePlayable(playerAction, playerInventory, emptyInventory, emptyInventory));
        });


        When("The player sacrifices a card {string}",
                (String nameOfCard) -> {
                    choice = PlayerAction.Choice.SACRIFICE;
                    card = new Card(nameOfCard, Age.AGE_1, 1, Arrays.asList(new Trade(Resources.WOOD, 10)), null, Card.Type.RAW, null, null);
                    playerInventory.setCardsInHand(Collections.singletonList(card));
                    playerAction = PlayerAction.builder().card(card).choice(choice).build();
                });

        Then("The move is a valid and cost is {string}", (String cost) -> {
            assertTrue(referee.isChoicePlayable(playerAction, playerInventory, emptyInventory, emptyInventory));
        });


        When("The player play a card {string} with resource Wood of quantity 1 is needed and he has no Wood resource",
                (String nameOfCard) -> {
                    choice = PlayerAction.Choice.PLAY;
                    List<Trade> cost = new ArrayList<>();
                    cost.add(new Trade(Resources.WOOD, 1));

                    card = new Card(nameOfCard, Age.AGE_1, 1, Arrays.asList(new Trade(Resources.WOOD, 10)), cost, Card.Type.RAW, null, null);
                    playerInventory.setCardsInHand(Collections.singletonList(card));
                    playerAction = PlayerAction.builder().card(card).choice(choice).build();
                });

        Then("The move is invalid as cost is {int}", (Integer cost) -> {
            assertFalse(referee.isChoicePlayable(playerAction, playerInventory, emptyInventory, emptyInventory));
        });


        When("The player play a card {string} with Wood 1 is needed but he has no Wood resource",
                (String nameOfCard) -> {
                    choice = PlayerAction.Choice.PLAY;
                    List<Trade> cost = new ArrayList<>();
                    cost.add(new Trade(Resources.WOOD, 1));
                    card = new Card(nameOfCard, Age.AGE_1, 1, Arrays.asList(new Trade(Resources.WOOD, 10)), cost, Card.Type.RAW, null, null);
                    List<Exchange> listOfExchange = new ArrayList<>();
                    listOfExchange.add(new Exchange(true, new Trade(Resources.WOOD, 1)));
                    emptyInventory.addQtyResource(Resources.WOOD, 1);
                    playerInventory.setCardsInHand(Collections.singletonList(card));
                    playerAction = PlayerAction.builder().card(card).listOfExchange(listOfExchange).build();
                });

        Then("He proposes a Trade with the player on the left for 1 Wood resource, the move is then valid as he has {int} Wood", (Integer traded) -> {
            assertTrue(referee.isChoicePlayable(playerAction, playerInventory, emptyInventory, emptyInventory));
        });

    }
}