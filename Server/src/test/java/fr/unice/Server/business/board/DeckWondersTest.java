package fr.unice.Server.business.board;

import model.AbstractPlayer;
import model.Inventory;
import model.Wonder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DeckWondersTest {


    private List<AbstractPlayer> playersWithInventories = new LinkedList<>();
    private Inventory inventory;

    @BeforeEach
    public void init() {
        for (int i = 0; i < 4; i++) {
            AbstractPlayer playerMock = Mockito.mock(AbstractPlayer.class);
            this.playersWithInventories.add(playerMock);
        }
    }

    @Test
    //Teste si la merveille prise est bien celle qui est donnée au joueur, pour toutes les joueurs de la partie
    public void wondersDistribution() {
        DeckWonders deckWonders = new DeckWonders();
        deckWonders.initialize();

        Deque<Wonder> wonders = new ArrayDeque<>(deckWonders.getWonders());
        for (AbstractPlayer playerWithInventory : playersWithInventories) {
            Wonder wonder = wonders.pop();
            this.inventory = new Inventory();
            when(playerWithInventory.getInventory()).thenReturn(inventory);
            playerWithInventory.getInventory().setWonder(wonder);
            assertEquals(wonder, playerWithInventory.getInventory().getWonder());
        }
    }
}

