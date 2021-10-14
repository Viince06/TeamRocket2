package fr.unice.Player.business.strategy;

import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static model.Resources.*;


public class ScientificStrategy extends PlayStrategy {
    private Inventory inventory;
    private Inventory inventoryRight;
    private Inventory inventoryLeft;
    private List<Resources> resourcesNeeded = Arrays.asList(MATHEMATICS, PHYSICS, WRITING);

    public ScientificStrategy() {
        super.setPriority(Collections.singletonList(Card.Type.SCIENTIFIC));
    }

    @Override
    public PlayerAction chooseAction(Inventory inventory, Inventory inventoryLeft, Inventory inventoryRight) {
        this.inventory = inventory;
        this.inventoryLeft = inventoryLeft;
        this.inventoryRight = inventoryRight;


        List<PlayerAction> actions = super.playCard(inventory, inventoryLeft, inventoryRight);

        if (actions.size() == 1) {
            return actions.get(0);
        } else {
            return chooseActionFromList(actions);
        }

    }

    /**
     * Dans les cartes scientifiques il n'y a pas besoin de faire de choix, la fonction sert à la classe Card
     * dans la fonction giveReward
     *
     * @param trades la liste des ressources sur une carte
     * @return La ressource du trade
     */
    @Override
    public Resources chooseReward(List<Trade> trades) {
        if (trades.size() == 1) {
            return trades.get(0).getResource();
        }

        Trade max = trades.get(0);
        int qty = 0;

        for (Trade trade : trades) {
            if ((resourcesNeeded.contains(trade.getResource())) && (trade.getQuantity() > qty)) {
                max = trade;
                qty = trade.getQuantity();
            }
        }
        return max.getResource();
    }

    /**
     * Cherche dans les playerAction la carte qui permettra d'avoir le plus de points scientifique
     * On cherche dans l'inventaire le symbole scientifique dont la quantité est la plus grande et on va essayer
     * de jouer en priorité une carte dans les playableAction qui contient aussi ce symbole.
     *
     * @param playableAction La liste des actions qui peuvent être jouées et qui sont de type scientifique
     * @return une PlayerAction qui sera jouée par le joueur
     */
    @Override
    public PlayerAction chooseActionFromList(List<PlayerAction> playableAction) {
        return chooseActionList(playableAction, resourcesNeeded, inventory);
    }

    public static List<Card.Type> getCardType() {
        return Collections.singletonList(Card.Type.SCIENTIFIC);
    }

    @Override
    public String getStrategyName() {
        return "Scientific Strategy";
    }

    @Override
    public boolean maximumReached() {
        return false;
    }
}
