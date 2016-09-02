package pl.edu.agh.smartscale.metrics;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.smartscale.command.Command;
import pl.edu.agh.smartscale.command.CommandEmitter;
import pl.edu.agh.smartscale.events.MetricsListener;
import pl.edu.agh.smartscale.events.TaskStatus;
import pl.edu.agh.smartscale.strategy.ScalingStrategy;

import java.util.Optional;

@AllArgsConstructor
public class StrategyBasedListener implements MetricsListener {
    private static final Logger logger = LoggerFactory.getLogger(StrategyBasedListener.class);

    private ScalingStrategy scalingStrategy;
    private CommandEmitter emitter;

    @Override
    public void receive(TaskStatus taskStatus) {
        logger.info("Listener received status: {}", taskStatus);
        Optional<Command> command = scalingStrategy.process(taskStatus);
        command.ifPresent(c -> {
            logger.info("Listener delegating command {} to emitter.", c);
            emitter.emit(c);
        });
    }
}
