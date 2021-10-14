package model;

import java.util.ArrayList;
import java.util.List;

public class Card {

    private String name;
    private Age age;
    private int nbPlayersMin;
    private List<Trade> reward;
    private List<Trade> cost;
    private Type cardType;
    private Trade chosenReward;
    private String previousCard;
    private List<String> nextCard;

    public enum Type {
        RAW,            //marron
        HANDMADE,       //gris
        CIVIL,          //bleue
        SCIENTIFIC,     //vert
        TRADE,          //jaune
        MILITARY,       //rouge
        GUILD           //violet
    }

    public Card() {}

    public Card(String name, Age age, int nbPlayersMin, List<Trade> reward, List<Trade> cost, Type type, String previousCard, List<String> nextCard) {
        this.name = name;
        this.age = age;
        this.nbPlayersMin = nbPlayersMin;
        this.reward = reward == null ? new ArrayList<>() : reward;
        this.cost = cost == null ? new ArrayList<>() : cost;
        this.cardType = type;
        this.chosenReward = this.reward.get(0);
        this.previousCard = previousCard;
        this.nextCard = nextCard;
    }

    public String getName() {
        return name;
    }

    public Age getAge() {
        return age;
    }

    public int getNbPlayersMin() {
        return nbPlayersMin;
    }

    public List<Trade> getReward() {
        return reward;
    }

    public List<Trade> getCost() {
        return cost;
    }

    public Type getCardType() {
        return cardType;
    }

    public Trade getChosenReward() {
        return chosenReward;
    }

    public void setChosenReward(Trade chosenReward) {
        this.chosenReward = chosenReward;
    }

    public String getPreviousCard() {
        return previousCard;
    }

    public List<String> getNextCard() {
        return nextCard;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Card && ((Card) o).name.equals(this.name);
    }
}
