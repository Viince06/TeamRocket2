package fr.unice.Player.business.player;

import fr.unice.Player.business.strategy.IStrategy;
import fr.unice.Player.business.strategy.StrategyFactory;
import model.AbstractPlayer;
import model.Inventory;
import model.Resources;
import model.Trade;

import java.util.List;

public abstract class StrategicPlayer extends AbstractPlayer {

    public StrategicPlayer(String name, Inventory inventory) {
        super(name, inventory);
    }

    protected IStrategy strategy;
    protected final StrategyFactory strategyFactory = new StrategyFactory();

    public IStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(StrategyFactory.DIFFICULTY_LEVEL difficultyLevel) throws Exception {
        this.strategy = strategyFactory.setStrategy(difficultyLevel);
    }

    public abstract Resources chooseReward(List<Trade> trades);
}
