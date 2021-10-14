package fr.unice.Server.business.player;

import model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerActionTest {

    @Test
    public void testCard() {
        Card card = new Card("Test", Age.AGE_1, 4, Arrays.asList(new Trade(Resources.ORE, 1)), null, Card.Type.RAW, null, null);
        List<Card> choice = new ArrayList<>();
        choice.add(card);

        PlayerAction action = PlayerAction.builder().card(choice.get(0)).choice(PlayerAction.Choice.PLAY).build();

        assertEquals(card, action.getCard());
        assertEquals(PlayerAction.Choice.PLAY, action.getChoice());
    }
}