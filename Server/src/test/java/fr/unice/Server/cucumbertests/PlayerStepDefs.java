package fr.unice.Server.cucumbertests;


import io.cucumber.java8.En;
import model.AbstractPlayer;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PlayerStepDefs implements En {
    private AbstractPlayer player;

    public PlayerStepDefs() {

        Given("a player with name {string}",
                (String playerName) ->
                {
                    AbstractPlayer playerMock = Mockito.mock(AbstractPlayer.class);
                    this.player = playerMock;
                });

        When("{string} requests his name",
                (String nom) -> {
                    when(player.getName()).thenReturn("Toto");
                    assertEquals(nom, player.getName());
                });
    }
}