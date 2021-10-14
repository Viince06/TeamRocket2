package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

public class PlayerAction {

    private Choice choice;
    private Card card;
    private List<Exchange> listOfExchange;
    private Resources chosenReward;

    public PlayerAction() {
    }

    public PlayerAction(Choice choice, Card card, List<Exchange> listOfExchange, Resources chosenReward) {
        this.choice = choice;
        this.card = card;
        this.listOfExchange = listOfExchange;
        this.chosenReward = chosenReward;
    }

    public static PlayerActionBuilder builder() {
        return new PlayerActionBuilder();
    }

    public enum Choice {
        PLAY,
        SACRIFICE,
        WONDER
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public List<Exchange> getListOfExchange() {
        return listOfExchange;
    }

    public void setListOfExchange(List<Exchange> listOfExchange) {
        this.listOfExchange = listOfExchange;
    }

    public Resources getChosenReward() {
        return chosenReward;
    }

    public void setChosenReward(Resources chosenReward) {
        this.chosenReward = chosenReward;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Choice : ").append(this.choice).append(" ").append(this.card.getName());
        if (!this.listOfExchange.isEmpty()) {
            sb.append(" | Exchange :");
            for (Exchange exchange : this.listOfExchange) {
                sb.append(exchange.getTradeToBeDone().getQuantity())
                        .append(" ")
                        .append(exchange.getTradeToBeDone().getResource())
                        .append(" - ")
                        .append(exchange.isLeft() ? "Left player" : "Right player");
            }
        }
        return sb.toString();
    }

    @JsonIgnoreProperties
    public static class PlayerActionBuilder {
        private Choice choice = Choice.PLAY;
        private Card card;
        private List<Exchange> listOfExchange = new ArrayList<>();
        private Resources chosenReward;

        PlayerActionBuilder() {
        }

        public PlayerActionBuilder choice(Choice choice) {
            this.choice = choice;
            return this;
        }

        public PlayerActionBuilder card(Card card) {
            this.card = card;
            return this;
        }

        public PlayerActionBuilder listOfExchange(List<Exchange> listOfExchange) {
            this.listOfExchange = listOfExchange;
            return this;
        }

        public PlayerActionBuilder chosenReward(Resources chosenReward) {
            this.chosenReward = chosenReward;
            return this;
        }

        public PlayerAction build() {
            if (chosenReward == null && choice == Choice.PLAY) {
                chosenReward = card.getReward().get(0).getResource();
            }
            return new PlayerAction(choice, card, listOfExchange, chosenReward);
        }

        public String toString() {
            return "PlayerAction.PlayerActionBuilder(choice=" + this.choice + ", card=" + this.card + ", listOfExchange=" + this.listOfExchange + ", chosenReward=" + this.chosenReward + ")";
        }
    }
}
