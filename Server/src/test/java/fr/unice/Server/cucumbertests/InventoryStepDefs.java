package fr.unice.Server.cucumbertests;


import fr.unice.Server.business.game.PointsCalculator;
import io.cucumber.java8.En;
import model.AbstractPlayer;
import model.Inventory;
import model.Resources;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InventoryStepDefs implements En {
    Inventory inventory;

    public InventoryStepDefs() {


        Given("a player with name {string} joins the game",
                (String playerName) ->
                {
                    AbstractPlayer playerMock = Mockito.mock(AbstractPlayer.class);
                    inventory = new Inventory();
                });

        When("{string} requests his number of gold he gets {int}",
                (String nom, Integer numberOfGold) -> {
                    assertEquals(numberOfGold.intValue(), inventory.getQtyResource(Resources.COINS));
                });

        When("{string} gets {int} victory points from his gold",
                (String nom, Integer numberOfVictoryPoints) -> {
                    assertEquals(numberOfVictoryPoints.intValue(), PointsCalculator.calculGoldPoints(inventory));
                });
    }
}
