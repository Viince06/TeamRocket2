package fr.unice.Server.business.game;

import fr.unice.Server.business.board.Deck;
import model.AbstractPlayer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameTest {

    @Test
    public void GameTest() {
        //Test Existance Referee
        Referee referee = new Referee();
        assertFalse(referee.toString().isBlank());

        //Test Existance Deck
        Deck deck = new Deck();
        deck.initialize();
        assertFalse(deck.toString().isEmpty());

        //Test Existance Joueurs
        LinkedList<AbstractPlayer> playersWithInventories = new LinkedList<>();

        for (int i = 0; i < 4; i++) {
            AbstractPlayer playerMock = Mockito.mock(AbstractPlayer.class);
            playersWithInventories.add(playerMock);
            assertEquals(i + 1, playersWithInventories.size());
        }
    }
}