package pl.edu.agh.smartscale.metrics;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.smartscale.command.Command;
import pl.edu.agh.smartscale.command.CommandEmitter;
import pl.edu.agh.smartscale.scaling.StatusListener;
import pl.edu.agh.smartscale.scaling.TaskStatus;
import pl.edu.agh.smartscale.scaling.strategy.ScalingStrategy;

import java.util.Optional;

@AllArgsConstructor
public class StrategyBasedStatusListener implements StatusListener {
    private static final Logger logger = LoggerFactory.getLogger(StrategyBasedStatusListener.class);

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
