package fr.unice.Player.business.strategy;

import fr.unice.Player.business.utils.FunctionsHelper;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class EasyStrategy extends PlayStrategy {

    /*
        Stratégies :

        #BOT IDIOT : Je parcours la liste des cartes en mains,
                     je prend la première qui est gratuite OU que je peux acheter avec mes ressources ou avec celles des autres joueurs
                     Sinon je sacrifie la première que j'ai en main

        #BOT IDIOT.2 : Sacrifier la première carte, tout le temps.

        #BOT AVANCE :
        1 : Je choisis ma stratégie en regardant la liste des cartes qui sont dans ma main
        2.1 : Il y a une carte qui m'interresse et je souhaite l'acheté
        2.1.1 : J'ai l'argent nécessaire je renvoi un PlayerAction avec ma carte et le choix PAY
        OU
        2.1.2 : J'ai pas les ressources nécessaires, je vérifie chez mes voisins s'ils ont les ressources.
                S'ils les ont, je créé un PlayerAction avec une Liste d'Exchange qui contiendra les échanges dont j'ai besoin
                avec le voisin en question...

        OU
        2.2 : Aucune carte ne m'interresse : Je rajoute une étape à ma merveille ou je sacrifie une carte...
     */

    //BOT IDIOT 2


    /**
     * Appelé par la classe Game, renvoit le choix de la carte à poser ou à défausser
     * pour le moment le but est simplement de poser une carte quelle quelle soit
     */
    @Override
    public PlayerAction chooseAction(Inventory inventory, Inventory inventoryLeft, Inventory inventoryRight) {
        List<Card> cardsInHand = inventory.getCardsInHand();
        for (Card currentCard : cardsInHand) {
            List<Trade> cardCosts = currentCard.getCost();
            if (cardCosts.isEmpty() || inventory.getCardsPlayed().stream().map(Card::getName).anyMatch(s -> s.equals(currentCard.getPreviousCard()))) {
                // Si la carte est gratuite ou si on a la sous évolution
                return PlayerAction.builder().card(currentCard).choice(PlayerAction.Choice.PLAY).build();
            } else if (FunctionsHelper.canEvolveMyWonder(inventory)) {
                return PlayerAction.builder().card(currentCard).choice(PlayerAction.Choice.WONDER).build();
            }

            List<Exchange> exchangeList = isPlayable(cardCosts, inventory, inventoryLeft, inventoryRight);
            if (exchangeList == null) {
                continue;
            } else if (exchangeList.isEmpty()) {
                return PlayerAction.builder()
                        .card(currentCard)
                        .listOfExchange(exchangeList)
                        .build();
            }
        }
        //si aucune carte ne peut être jouée je sacrifie ma 1ere carte
        return super.sacrificeCard(inventory.getCardsInHand().get(0));
    }

    protected List<Exchange> isPlayable(List<Trade> cardCost, Inventory inventory, Inventory left, Inventory right) {
        return super.isPlayable(cardCost, inventory, left, right);
    }

    @Override
    public Resources chooseReward(List<Trade> trades) {
        return trades.get(0).getResource();
    }

    @Override
    public PlayerAction chooseActionFromList(List<PlayerAction> playableAction) {
        return playableAction.get(0);
    }

    @Override
    public String getStrategyName() {
        return "Easy Strategy";
    }

    @Override
    public boolean maximumReached() {
        return false;
    }
}
