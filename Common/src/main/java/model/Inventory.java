package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class Inventory {

    private List<Card> cardsInHand = new ArrayList<>();
    private List<Card> cardsPlayed = new ArrayList<>();
    private List<Card> cardsSacrificed = new ArrayList<>();
    private List<Card> cardsUsedForWonder = new ArrayList<>();
    private Wonder wonder;

    private final EnumMap<Resources, Integer> resources = new EnumMap<>(Resources.class);

    public Inventory() {
        for (Resources r : Resources.values()) {
            this.resources.put(r, 0);
        }

        this.resources.put(Resources.COINS, 3);
    }

    public Map<Resources, Integer> getResources() {
        return resources;
    }

    public void setCardsInHand(List<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public void setWonder(Wonder wonder) {
        this.wonder = wonder;
        Trade ress = wonder.getRessourceWonder();
        addQtyResource(ress.getResource(), ress.getQuantity());
    }

    public Wonder getWonder() {
        return wonder;
    }

    public void cardPlayed(Card cardToPlay) {
        Optional<Card> card = cardsInHand.stream().filter(c -> c.getName().equals(cardToPlay.getName())).findFirst();
        if (card.isEmpty()) {
            throw new IllegalArgumentException("Card \"" + cardToPlay.getName() + "\" not in hand");
        }

        cardsInHand.remove(card.get());
        cardsPlayed.add(card.get());
    }

    public List<Card> getCardsInHand() {
        return cardsInHand;
    }

    public List<Card> getCardsPlayed() {
        return cardsPlayed;
    }

    public List<Card> getCardsSacrificed() {
        return cardsSacrificed;
    }

    public void setCardSacrificed(Card cardsSacrificed) {
        cardsInHand.remove(cardsSacrificed);
        cardsPlayed.remove(cardsSacrificed);
        this.cardsSacrificed.add(cardsSacrificed);
    }

    public List<Card> getCardsUsedForWonder() {
        return cardsUsedForWonder;
    }

    public void setCardUsedForWonder(Card cardUsed) {
        cardsInHand.remove(cardUsed);
        this.cardsUsedForWonder.add(cardUsed);
    }

    public int getQtyResource(Resources resource) {
        return this.getResources().get(resource);
    }

    /**
     * @param qtyAdded positive value for addition and negative for substraction
     */
    public void addQtyResource(Resources resource, int qtyAdded) {
        this.resources.put(resource, this.resources.get(resource) + qtyAdded);
    }

    @JsonIgnore
    public int getMilitaryPointsOfPlayer() { //récupère les points militaire d'un joueur
        List<Card> cards = getCardsPlayed();
        int res = 0;
        for (Card card : cards) {
            if (card.getChosenReward().getResource() == Resources.MILITARY_POINT) {
                res += card.getChosenReward().getQuantity();
            }
        }
        return res;
    }

    public void setCardsPlayed(List<Card> cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
    }

    public void setCardsSacrificed(List<Card> cardsSacrificed) {
        this.cardsSacrificed = cardsSacrificed;
    }

    public void setCardsUsedForWonder(List<Card> cardsUsedForWonder) {
        this.cardsUsedForWonder = cardsUsedForWonder;
    }
}
