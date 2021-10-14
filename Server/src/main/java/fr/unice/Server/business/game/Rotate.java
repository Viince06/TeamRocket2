package fr.unice.Server.business.game;

import model.AbstractPlayer;
import model.Card;

import java.util.ArrayList;
import java.util.List;

public class Rotate {

    /**
     * Classe qui va transférer les decks de cartes de joueur en joueur
     * Cette classe doit être appelée quand on a fini un tour de jeu, toutes les modifications ont été apportées sur
     * les inventaires et les cartes jouées : alors on passe au joueur suivant
     * Si le booléen sens est appelé avec True, on passe les mains dans le sens horaire,
     * si c'est avec false, les mains sont passées dans l'autre sens
     */

    public static void giveCards(boolean sens, List<? extends AbstractPlayer> players) {
        List<Card> cardOtherPlayer = null;

        List<List<Card>> handsList = new ArrayList<>();
        for (AbstractPlayer player : players) {
            handsList.add(player.getInventory().getCardsInHand());
        }

        for (int i = 0; i < players.size(); i++) {
            if (sens && (i == 0)) {
                cardOtherPlayer = handsList.get(handsList.size() - 1);
            }
            if (sens && (i != 0)) {
                cardOtherPlayer = handsList.get(i - 1);
            }
            if (!sens && (i == (players.size() - 1))) {
                cardOtherPlayer = handsList.get(0);
            }
            if (!sens && (i != (players.size() - 1))) {
                cardOtherPlayer = handsList.get(i + 1);
            }
            players.get(i).getInventory().setCardsInHand(cardOtherPlayer);
        }
    }
}
