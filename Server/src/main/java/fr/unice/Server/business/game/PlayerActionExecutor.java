package fr.unice.Server.business.game;

import fr.unice.Server.business.board.CardService;
import fr.unice.Server.business.board.WonderService;
import fr.unice.Server.business.player.Player;
import fr.unice.Server.business.player.PlayerList;
import model.Exchange;
import model.PlayerAction;
import model.Resources;
import model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerActionExecutor {

    private static final Logger logger = LoggerFactory.getLogger(PlayerActionExecutor.class);

    private final PlayerList players;
    private final CardService cardService = new CardService();
    private final WonderService wonderService = new WonderService();

    public PlayerActionExecutor(PlayerList players) {
        this.players = players;
    }

    public void executePlayerActions(Map<UUID, PlayerAction> currentPlayerActions) throws Exception {
        for (Map.Entry<UUID, PlayerAction> entry : currentPlayerActions.entrySet()) {
            Player player = players.getByUUID(entry.getKey());
            PlayerAction playerAction = entry.getValue();
            this.cardExchange(player, playerAction);
            this.playerChoice(player, playerAction);
        }
    }

    /**
     * Permet l'échange entre les joueurs ainsi que d'appliquer le choix d'action du joueur
     */
    private void cardExchange(Player player, PlayerAction playerAction) {
        for (Exchange exchange : playerAction.getListOfExchange()) {
            Trade trade = exchange.getTradeToBeDone();
            boolean isLeft = exchange.isLeft();
            Player playerTarget;
            if (isLeft) {
                playerTarget = players.leftPlayerOf(player);
            } else {
                playerTarget = players.rightPlayerOf(player);
            }
            int cost = (trade.getQuantity() * 2);

            player.getInventory().addQtyResource(Resources.COINS, (-1 * cost));
            playerTarget.getInventory().addQtyResource(Resources.COINS, cost);

            logger.info("Echange de ressource effectué entre '{}' et '{}' ({} {} contre {} golds)", player.getName(), playerTarget.getName(), trade.getQuantity(), trade.getResource(), cost);
        }
    }

    /**
     * Choice of playing a card, sacrificing a card or choosing wonder
     */
    private void playerChoice(Player player, PlayerAction currentAction) throws Exception {
        switch (currentAction.getChoice()) {
            case PLAY:
                List<Trade> resourcesFromExchanges = currentAction.getListOfExchange().stream().map(Exchange::getTradeToBeDone).collect(Collectors.toList());
                cardService.pay(currentAction.getCard(), player, resourcesFromExchanges);
                break;

            case SACRIFICE:
                player.getInventory().setCardSacrificed(currentAction.getCard());
                player.getInventory().addQtyResource(Resources.COINS, 3);
                break;

            case WONDER:
                player.getInventory().setCardUsedForWonder(currentAction.getCard());
                wonderService.pay(player.getInventory().getWonder(), player);
                break;

            default:
                throw new Exception("Error Occurred");
        }
    }
}
