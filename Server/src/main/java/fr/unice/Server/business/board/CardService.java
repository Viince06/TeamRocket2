package fr.unice.Server.business.board;

import model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component
public class CardService {

    public boolean canPay(Card card, Inventory inventory) {

        return canPay(card, inventory, new ArrayList<>());
    }

    public boolean canPay(Card card, Inventory inventory, List<Trade> resourcesFromExchanges) {
        if (resourcesFromExchanges == null) {
            resourcesFromExchanges = new ArrayList<>();
        }

        for (Trade trade : card.getCost()) {
            int totalQtyAvailable = inventory.getQtyResource(trade.getResource()) + resourcesFromExchanges.stream().filter(t -> trade.getResource().equals(t.getResource())).mapToInt(Trade::getQuantity).sum();
            if (totalQtyAvailable < trade.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    public void pay(Card card, AbstractPlayer player) throws Exception {
        pay(card, player, new ArrayList<>());
    }

    public void pay(Card card, AbstractPlayer player, List<Trade> resourcesFromExchanges) throws Exception {
        pay(card, player, resourcesFromExchanges, card.getReward().get(0).getResource());
    }

    public void pay(Card card, AbstractPlayer player, List<Trade> resourcesFromExchanges, Resources chosenReward) throws Exception {

        // Est-ce que la sous évolution du batiment est présente dans l'inventaire ? Alors on paye
        if (card.getPreviousCard() == null || player.getInventory().getCardsPlayed().stream().filter(c -> c.getName().equals(card.getPreviousCard())).findFirst().isEmpty()) {
            if (!canPay(card, player.getInventory(), resourcesFromExchanges)) {
                //log.error("Le joueur {} n'a pas les ressources nécessaires pour acheter la carte {}", player.getPlayer().getName(), this.name);
                throw new Exception(String.format("Le joueur %s n'a pas les ressources nécessaires pour acheter la carte '%s'", player.getName(), card.getName()));
            }

            for (Trade trade : card.getCost()) { //on n'enlève de l'inventaire que l'argent, le total des ressources ne doit pas diminuer
                if (trade.getResource() == Resources.COINS) {
                    player.getInventory().addQtyResource(trade.getResource(), -trade.getQuantity());
                }
            }

        }
        player.getInventory().cardPlayed(card);
        giveReward(card, player, chosenReward);
    }

    public void giveReward(Card card, AbstractPlayer player) throws Exception {
        giveReward(card, player, null);
    }

    private void giveReward(Card card, AbstractPlayer player, Resources chosenResource) throws Exception {
        List<Trade> rewards = card.getReward();
        if (rewards.size() == 1 || chosenResource == null) {
            player.getInventory().addQtyResource(rewards.get(0).getResource(), rewards.get(0).getQuantity());
            card.setChosenReward(rewards.get(0));
        } else if (rewards.size() >= 1) {
            Optional<Trade> chosenTrade = rewards.stream().filter(trade -> trade.getResource() == chosenResource).findFirst();
            if (chosenTrade.isEmpty()) {
                //log.error("La récompense choisie par le joueur ne fait pas parti des récompenses de la carte");
                throw new Exception("La récompense choisie par le joueur ne fait pas parti des récompenses de la carte");
            }
            player.getInventory().addQtyResource(chosenResource, chosenTrade.get().getQuantity());
            card.setChosenReward(chosenTrade.get());
        }
    }
}
