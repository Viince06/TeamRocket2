package fr.unice.Server.business.board;

import model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WonderService {

    /**
     * Détermine si un joueur a les ressources nécessaire pour payer l'étape de merveille suivante,
     *
     * @param inventory l'inventaire du joueur
     * @return true or false
     */
    public boolean canPay(Wonder wonder, Inventory inventory) {
        return canPay(wonder, inventory, new ArrayList<>());
    }

    public boolean canPay(Wonder wonder, Inventory inventory, List<Trade> resourcesFromExchanges) {
        if (resourcesFromExchanges == null) {
            resourcesFromExchanges = new ArrayList<>();
        }
        if (wonder.getWonderLevel() < wonder.getCost().size()) {
            List<Trade> tradeWonder = wonder.getCost().get(wonder.getWonderLevel()); //récupère la liste des couts pour le niveau de merveille suivant
            for (Trade trade : tradeWonder) {
                int totalQtyAvailable = inventory.getQtyResource(trade.getResource()) + resourcesFromExchanges.stream().filter(t -> trade.getResource().equals(t.getResource())).mapToInt(Trade::getQuantity).sum();
                if (totalQtyAvailable < trade.getQuantity()) {
                    return false;
                }
            }
            return true;

        }
        return false;
    }

    /**
     * Si le joueur a le nombre de ressources et d'argent nécessaire pour acheter une étape de merveille,
     * décompte l'argent nécessaire à payer la merveille, (et pas les ressources car elles sont permanente)
     *
     * @param player le joueur qui veut acheter l'étape
     * @throws Exception
     */
    public void pay(Wonder wonder, AbstractPlayer player) throws Exception {
        pay(wonder, player, new ArrayList<>());
    }

    public void pay(Wonder wonder, AbstractPlayer player, List<Trade> resourcesFromExchanges) throws Exception {
        if (!canPay(wonder, player.getInventory(), resourcesFromExchanges)) {
            //log.error("Le joueur {} n'a pas les ressources nécessaires pour acheter l'étape de merveille {}", player.getPlayer().getName(), this.name);
            throw new Exception(String.format("Le joueur %s n'a pas les ressources nécessaires pour acheter l'étape de merveille '%s'", player.getName(), wonder.getName()));
        }

        List<Trade> tradeWonder = wonder.getCost().get(wonder.getWonderLevel());

        for (Trade trade : tradeWonder) {
            if (trade.getResource() == Resources.COINS) {
                player.getInventory().addQtyResource(trade.getResource(), -trade.getQuantity());
            }
            //log.debug("le joueur a payé {} de {}", trade.getQuantity(), trade.getResource().name());
        }
        giveReward(wonder, player);
    }

    /**
     * Ajoute la ou les récompenses à l'inventaire du joueur, il n'y a pas besoin de choisir parmi les récompenses.
     */
    public void giveReward(Wonder wonder, AbstractPlayer player) {
        List<Trade> rewardWonder = wonder.getReward().get(wonder.getWonderLevel());

        for (Trade trade : rewardWonder) {
            player.getInventory().addQtyResource(trade.getResource(), trade.getQuantity());
        }
        wonder.setWonderLevel(wonder.getWonderLevel() + 1);
    }
}
