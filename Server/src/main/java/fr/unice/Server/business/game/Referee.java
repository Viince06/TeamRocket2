package fr.unice.Server.business.game;

import fr.unice.Server.business.board.CardService;
import fr.unice.Server.business.board.WonderService;
import fr.unice.Server.business.player.PlayerList;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Referee {
    private static final Logger log = LoggerFactory.getLogger(Referee.class);

    private final CardService cardService = new CardService();
    private final WonderService wonderService = new WonderService();

    public Referee() {
    }

    /**
     * Renvoie true si le choix du joueur est valide
     * Prend en paramètre un joueur et l'action qu'il souhaite faire
     * une action = type d'action, carte à jouer, liste d'échanges avec d'autres joueurs
     */
    public boolean isChoicePlayable(PlayerAction choice, Inventory playerInventory, Inventory left, Inventory right) {
        if (PlayerAction.Choice.SACRIFICE.equals(choice.getChoice())) {
            return true;
        }

        Card cardChoosen = choice.getCard();
        List<Exchange> exchangeList = choice.getListOfExchange();

        // Si la carte choisie
        if (!playerInventory.getCardsInHand().contains(cardChoosen)) {
            return false;
        }

        // Est ce que le player à la sous évolution de la carte
        if (choice.getChoice().equals(PlayerAction.Choice.PLAY) && playerInventory.getCardsPlayed().stream().map(Card::getName).anyMatch(s -> s.equals(cardChoosen.getPreviousCard()))) {
            return true;
        }

        // Est ce que le joueur à suffisement d'argent pour tous ses échanges
        if (exchangeList.stream().mapToInt(exchange -> exchange.getTradeToBeDone().getQuantity()).sum() * 2 > playerInventory.getQtyResource(Resources.COINS)) {
            return false;
        }

        // Si notre échange contient une quantité de ressources plus grande que ce que mon voisin possède
        for (Exchange exchange : exchangeList) {
            Trade trade = exchange.getTradeToBeDone();
            if (trade.getQuantity() > (exchange.isLeft() ? left.getQtyResource(trade.getResource()) : right.getQtyResource(trade.getResource()))) {
                return false;
            }
        }

        // Si canPay()
        List<Trade> resourcesFromExchanges = choice.getListOfExchange().stream().map(Exchange::getTradeToBeDone).collect(Collectors.toList());
        if (PlayerAction.Choice.PLAY.equals(choice.getChoice())) {
            return this.cardService.canPay(cardChoosen, playerInventory, resourcesFromExchanges);
        } else if (PlayerAction.Choice.WONDER.equals(choice.getChoice())) {
            return this.wonderService.canPay(playerInventory.getWonder(), playerInventory, resourcesFromExchanges);
        }
        return false;
    }

    public List<AbstractPlayer> getWinner(List<? extends AbstractPlayer> listOfPlayers) {
        Map<AbstractPlayer, Integer> victoryPoints = PointsCalculator.calculVictoryPoints(listOfPlayers);
        List<AbstractPlayer> winnerList = new ArrayList<>();
        int maxVictoryPoints = Collections.max(victoryPoints.values());

        for (Map.Entry<AbstractPlayer, Integer> entry : victoryPoints.entrySet()) {
            int scientificPts = entry.getKey().getInventory().getQtyResource(Resources.MATHEMATICS) + entry.getKey().getInventory().getQtyResource(Resources.PHYSICS) + entry.getKey().getInventory().getQtyResource(Resources.WRITING);
            log.debug("Le joueur {} a {} points de Victoire, {} Or, {} points de Militaire et {} points Scientifique.", entry.getKey().getName(), entry.getValue(), entry.getKey().getInventory().getQtyResource(Resources.COINS), entry.getKey().getInventory().getMilitaryPointsOfPlayer(), scientificPts);

            log.debug("Le joueur {} a {} points de Wood, {} Clay, {} Ore, {} Stone", entry.getKey().getName(), entry.getKey().getInventory().getQtyResource(Resources.WOOD), entry.getKey().getInventory().getQtyResource(Resources.CLAY), entry.getKey().getInventory().getQtyResource(Resources.ORE), entry.getKey().getInventory().getQtyResource(Resources.STONE));
            if (entry.getValue() == maxVictoryPoints) {
                winnerList.add(entry.getKey());
            }
        }
        return winnerList;
    }

    public static AbstractPlayer checkWinnerOfTwo(AbstractPlayer p1, AbstractPlayer p2) {
        if (p1.getInventory().getQtyResource(Resources.MILITARY_POINT) > p2.getInventory().getQtyResource(Resources.MILITARY_POINT)) {
            return p1;
        }
        return p2;
    }

    public static AbstractPlayer checkLooserOfTwo(AbstractPlayer p1, AbstractPlayer p2) {
        if (p1.getInventory().getQtyResource(Resources.MILITARY_POINT) < p2.getInventory().getQtyResource(Resources.MILITARY_POINT)) {
            return p1;
        }
        return p2;
    }

    public static boolean checkEqual(AbstractPlayer p1, AbstractPlayer p2) {
         return p1.getInventory().getQtyResource(Resources.MILITARY_POINT) == p2.getInventory().getQtyResource(Resources.MILITARY_POINT);
    }
}
