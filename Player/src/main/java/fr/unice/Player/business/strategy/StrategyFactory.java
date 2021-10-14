package fr.unice.Player.business.strategy;

public class StrategyFactory {

    public enum DIFFICULTY_LEVEL {
        SCIENTIFIC,
        MILITARY,
        CIVIL,
        RANDOM,
        RESOURCE,
        EASY
    }

    public IStrategy setStrategy(DIFFICULTY_LEVEL difficultyLevel) throws Exception {
        switch (difficultyLevel) {

            case RANDOM:
                return new RandomStrategy();

            case EASY:
                return new EasyStrategy();

            case MILITARY:
                return new MilitaryStrategy();

            case CIVIL:
                return new CivilStrategy();

            case SCIENTIFIC:
                return new ScientificStrategy();

            case RESOURCE:
                return new ResourceStrategy();

            default:
                throw new Exception("Unable to create create Strategy");
        }
    }
}
