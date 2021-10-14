package fr.unice.Server.business.board;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class WonderTest {

    private AbstractPlayer player;

    @BeforeEach
    public void init() {
        AbstractPlayer playerMock = Mockito.mock(AbstractPlayer.class);
        this.player = playerMock;
        Inventory inventory = new Inventory();
        when(player.getInventory()).thenReturn(inventory);
    }


    @Test
    public void canPay() {
        Wonder wonder = new Wonder("RHODOS_A", new Trade(Resources.ORE, 1),
                Arrays.asList(Arrays.asList(new Trade(Resources.VICTORY_POINT, 3)),
                        Arrays.asList(new Trade(Resources.MILITARY_POINT, 2)),
                        Arrays.asList(new Trade(Resources.VICTORY_POINT, 7))),
                Arrays.asList(Arrays.asList(new Trade(Resources.WOOD, 2)),
                        Arrays.asList(new Trade(Resources.CLAY, 3)),
                        Arrays.asList(new Trade(Resources.ORE, 4))));

        assertFalse(new WonderService().canPay(wonder, player.getInventory()));  // False, parce que on a pas les ressources nécessaires

        player.getInventory().addQtyResource(Resources.WOOD, 2);
        player.getInventory().addQtyResource(Resources.CLAY, 3);
        player.getInventory().addQtyResource(Resources.ORE, 4);

        assertTrue(new WonderService().canPay(wonder, player.getInventory()));  // True, maintenant on les a

    }

    @Test
    public void pay() throws Exception {
        Wonder wonder = new Wonder("RHODOS_A", new Trade(Resources.ORE, 1),
                Arrays.asList(Arrays.asList(new Trade(Resources.VICTORY_POINT, 3)),
                        Arrays.asList(new Trade(Resources.MILITARY_POINT, 2)),
                        Arrays.asList(new Trade(Resources.VICTORY_POINT, 7))),
                Arrays.asList(Arrays.asList(new Trade(Resources.WOOD, 2)),
                        Arrays.asList(new Trade(Resources.CLAY, 3)),
                        Arrays.asList(new Trade(Resources.ORE, 4))));


        player.getInventory().setWonder(wonder);

        assertEquals(wonder, player.getInventory().getWonder());  // Merveille liée et bien reconnue

        player.getInventory().addQtyResource(Resources.WOOD, 2);
        player.getInventory().addQtyResource(Resources.CLAY, 3);
        player.getInventory().addQtyResource(Resources.ORE, 4);

        assertEquals(2, player.getInventory().getQtyResource(Resources.WOOD));
        assertEquals(3, player.getInventory().getQtyResource(Resources.CLAY)); // Trio de Tests pour vérifier les résources nécessaires
        assertEquals(4 + 1, player.getInventory().getQtyResource(Resources.ORE)); // 4 ajoutées, 1 implémentée avec la merveille

        //De base, wonderLevel = 0
        new WonderService().pay(wonder, player);

        assertEquals(2, player.getInventory().getQtyResource(Resources.WOOD)); // Trio de Tests pour vérifier l'absence des résources
    }

    @Test
    public void giveReward() {
        Wonder wonder = new Wonder("RHODOS_A", new Trade(Resources.ORE, 1),
                Arrays.asList(Arrays.asList(new Trade(Resources.VICTORY_POINT, 3)),
                        Arrays.asList(new Trade(Resources.MILITARY_POINT, 2)),
                        Arrays.asList(new Trade(Resources.VICTORY_POINT, 7))),
                Arrays.asList(Arrays.asList(new Trade(Resources.WOOD, 2)),
                        Arrays.asList(new Trade(Resources.CLAY, 3)),
                        Arrays.asList(new Trade(Resources.ORE, 4))));

        player.getInventory().setWonder(wonder);
        assertEquals(wonder, player.getInventory().getWonder());

        //wonderLevel = 0
        new WonderService().giveReward(wonder, player);
        assertEquals(3, player.getInventory().getQtyResource(Resources.VICTORY_POINT));  // Trio de tests pour vérifier la Reward
    }
}
