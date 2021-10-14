package fr.unice.Server.business.game;

import model.*;

import java.util.*;

public class PointsCalculator {

    private PointsCalculator() {
    }

    /**
     * Compare les points militaire des joueurs, ils gagnent toujours 1 point de victoire par victoire
     * Attribue des points militaire à chaque joueur qui en a dépassé un autre
     * L'autre joueur perd un point militaire
     * On peut avoir un total négatif de points militaire
     * Rend dictionnaire <joueur, point de victoire militaire>
     */
    public static Map<AbstractPlayer, Integer> calculMilitaryPoints(List<? extends AbstractPlayer> players, Age age) {
        int ptsGagne = 1;
        if (age.getAge() == 2) {
            ptsGagne = 3;
        }
        if (age.getAge() == 3) {
            ptsGagne = 5;
        }
        Map<AbstractPlayer, Integer> hashSetPoints = new HashMap<>();
        AbstractPlayer winner;
        AbstractPlayer looser;

        for (AbstractPlayer player : players) {
            hashSetPoints.put(player, 0);
        }

        for (int i = 0; i < players.size(); i++) {

            if (i == (players.size() - 1)) {
                if (!Referee.checkEqual(players.get(0), players.get(i))) {
                    winner = Referee.checkWinnerOfTwo(players.get(0), players.get(i));
                    looser = Referee.checkLooserOfTwo(players.get(0), players.get(i));
                    hashSetPoints.put(winner, hashSetPoints.get(winner) + ptsGagne);
                    hashSetPoints.put(looser, hashSetPoints.get(looser) - 1);
                }
            } else {
                if (!Referee.checkEqual(players.get(i), players.get(i + 1))) {
                    winner = Referee.checkWinnerOfTwo(players.get(i), players.get(i + 1));
                    looser = Referee.checkLooserOfTwo(players.get(i), players.get(i + 1));
                    hashSetPoints.put(winner, hashSetPoints.get(winner) + ptsGagne);
                    hashSetPoints.put(looser, hashSetPoints.get(looser) - 1);
                }
            }
        }
        return hashSetPoints;
    }

    /**
     * Renvoie la liste des points de victoire pour chaque joueur
     */
    public static Map<AbstractPlayer, Integer> calculVictoryPoints(List<? extends AbstractPlayer> players) {
        Map<AbstractPlayer, Integer> vP = new HashMap<>();

        for (AbstractPlayer player : players) {
            Inventory inventory = player.getInventory();
            int pts = calculCivilPoints(inventory)
                    + calculGoldPoints(inventory)
                    + inventory.getQtyResource(Resources.MILITARY_VICTORY_POINT)
                    + calculSciencePoints(inventory);

            vP.put(player, pts);
        }

        return vP;
    }

    /**
     * Calcule les points de victoire avec les symboles scientifique
     * Récupère les points de chaque type (math, physique, écriture) et calcule les points obtenus grâce à eyx
     */
    public static int calculSciencePoints(Inventory inventory) {

        EnumMap<Resources, Integer> qtyByScientificResource = new EnumMap<>(Resources.class);
        for (Resources r : Arrays.asList(Resources.MATHEMATICS, Resources.PHYSICS, Resources.WRITING)) {
            qtyByScientificResource.put(r, 0);
        }

        // On récupère la quantité des ressources de chaque carte Scientifique
        for (Card card : inventory.getCardsPlayed()) {
            Trade cardReward = card.getChosenReward();
            // Si la récompense de la carte est de type scientifique, on ajoute la quantité de ces resources dans la Map
            if (cardReward.getResource().getType() == Resources.ResourceType.SCIENTIFIC_SYMBOLS) {
                qtyByScientificResource.put(cardReward.getResource(), qtyByScientificResource.get(cardReward.getResource()) + cardReward.getQuantity());
            }
        }

        int res = 0;
        // Symboles identiques : qty²
        for (int value : qtyByScientificResource.values()) {
            res += Math.pow(value, 2);
        }

        // Lot de 3 symboles différents
        res += qtyByScientificResource.values().stream().min(Integer::compareTo).get() * 7;
        return res;
    }

    public static int calculCivilPoints(Inventory inventory) { //calcul pts de victoire carte bleues
        int res = 0;
        List<Card> cards = inventory.getCardsPlayed();
        for (Card card : cards) {
            if (card.getChosenReward().getResource() == Resources.VICTORY_POINT) {
                res += card.getChosenReward().getQuantity();
            }
        }
        return res;
    }

    public static int calculGoldPoints(Inventory inventory) {//calcul pts de victoire de l'argent
        return inventory.getQtyResource(Resources.COINS) / 3;
    }
}
