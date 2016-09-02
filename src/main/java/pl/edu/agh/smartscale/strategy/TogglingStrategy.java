package pl.edu.agh.smartscale.strategy;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.smartscale.command.Command;
import pl.edu.agh.smartscale.events.TaskStatus;

import java.util.Optional;

public class TogglingStrategy implements ScalingStrategy {

    private static final Logger logger = LoggerFactory.getLogger(TogglingStrategy.class);

    private DateTime lastEmitted;
    private Command lastCommand;

    public TogglingStrategy() {
        this.lastEmitted = DateTime.now().minusMinutes(4);
        this.lastCommand = Command.SCALE_DOWN;
    }

    @Override
    public Optional<Command> process(TaskStatus taskStatus) {
        if (lastEmitted.plusMinutes(5).isBeforeNow()) {
            lastCommand = (lastCommand == Command.SCALE_UP ? Command.SCALE_DOWN : Command.SCALE_UP);
            lastEmitted = DateTime.now();
            logger.info("Strategy returns: {} - {}", lastCommand, lastEmitted);
            return Optional.of(lastCommand);
        }
        logger.info("Strategy returns: EMPTY");
        return Optional.empty();
    }
}
