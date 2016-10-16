package pl.edu.agh.smartscale.config;

import lombok.Value;
import org.joda.time.Duration;
import pl.edu.agh.smartscale.strategy.StrategyType;

@Value
public class Config {
    Duration timeLeft;
    StrategyType strategyType;
}
