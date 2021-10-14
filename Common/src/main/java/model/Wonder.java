package model;

import java.util.List;

public class Wonder {

    private String name;
    private List<List<Trade>> reward;
    private List<List<Trade>> cost;
    private Trade ressourceWonder;
    private int wonderLevel;


    /**
     * ressourceWonder est la ressource gratuite donnée par la merveille
     * La liste des reward contient des listes de Trade pour les récompenses de chaque étape de la merveille
     * La liste des coûts contient contient des listes de Trade pour les coûts de chaque étape de la merveille
     * reward[0] = étape 1
     * cost[0] = coût étape 1
     * Une merveille peut avoir de 2 à 4 étapes, alors les listes auront jusqu'à 4 sous-listes
     */

    public Wonder() {}

    public Wonder(String name, Trade ressourceWonder, List<List<Trade>> reward, List<List<Trade>> cost) {
        this.name = name;
        this.ressourceWonder = ressourceWonder;
        this.reward = reward;
        this.cost = cost;
        wonderLevel = 0;
    }

    public String getName() {
        return name;
    }

    public List<List<Trade>> getReward() {
        return reward;
    }

    public List<List<Trade>> getCost() {
        return cost;
    }

    public Trade getRessourceWonder() {
        return ressourceWonder;
    }

    public int getWonderLevel() {
        return wonderLevel;
    }

    public void setWonderLevel(int wonderLevel) {
        this.wonderLevel = wonderLevel;
    }
}
