package fr.unice.Player.business.utils;

import model.Inventory;
import model.Trade;
import model.Wonder;

import java.util.List;

public class FunctionsHelper {

    public static boolean canEvolveMyWonder(Inventory playerInventory) {
        return FunctionsHelper.canPayAWonder(playerInventory, playerInventory.getWonder());
    }

    public static boolean canPayAWonder(Inventory playerInventory, Wonder wonder) {
        if (wonder.getWonderLevel() >= wonder.getCost().size()) {
            return false;
        }

        //récupère la liste des couts pour le niveau de merveille suivant
        List<Trade> tradeWonder = wonder.getCost().get(wonder.getWonderLevel());

        for (Trade trade : tradeWonder) {
            int totalQtyAvailable = playerInventory.getQtyResource(trade.getResource());
            if (totalQtyAvailable < trade.getQuantity()) {
                return false;
            }
        }
        return true;
    }
}
