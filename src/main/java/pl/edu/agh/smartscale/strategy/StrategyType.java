package pl.edu.agh.smartscale.strategy;

public enum StrategyType {
    LINEAR("LINEAR");

    private final String strategyName;

    StrategyType(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getStrategyName() {
        return strategyName;
    }
}
