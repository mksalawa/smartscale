package pl.edu.agh.smartscale.strategy;

import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.smartscale.command.Command;
import pl.edu.agh.smartscale.command.SetCapacityCommand;
import pl.edu.agh.smartscale.config.Timer;
import pl.edu.agh.smartscale.events.TaskStatus;
import pl.edu.agh.smartscale.metrics.MetricData;

import java.util.Optional;

public class LinearStrategy implements ScalingStrategy {

    private static final Logger logger = LoggerFactory.getLogger(LinearStrategy.class);

    private Duration EVALUATION_FREQUENCY;
    private Optional<TaskStatus> previousHistoricalStatus = Optional.empty();
    private Timer timer;

    public LinearStrategy(Timer timer, Duration evaluationFrequency) {
        this.timer = timer;
        this.EVALUATION_FREQUENCY = evaluationFrequency;
    }

    public LinearStrategy(Timer timer) {
        this(timer, Duration.standardMinutes(5));
    }

    @Override
    public Optional<Command> process(TaskStatus taskStatus) {

        Optional<Command> command = Optional.empty();
        if (!previousHistoricalStatus.isPresent()) {
            previousHistoricalStatus = Optional.of(taskStatus);
        } else if (isNewHistoricalStatus(taskStatus)) {
            command = evaluateCurrentState(previousHistoricalStatus.get(), taskStatus);
            previousHistoricalStatus = Optional.of(taskStatus);
        }

        logger.info("Strategy returns: {}", command.map(Object::toString).orElse("EMPTY"));
        return command;
    }

    /**
     * Analyzes the {@code currentState} in comparison to {@code previousState} by computing the speed of task
     * processing and the expected end time (assuming the linear processing time).
     * Returns the optional command to be executed.
     *
     * @param previousStatus
     * @param currentStatus
     * @return Optional command to be executed.
     */
    private Optional<Command> evaluateCurrentState(TaskStatus previousStatus, TaskStatus currentStatus) {

        Minutes timeDelta = Minutes.minutesBetween(previousStatus.getTimestamp(), currentStatus.getTimestamp());

        MetricData currentMetricData = currentStatus.getMetricData();
        MetricData prevMetricData = previousStatus.getMetricData();
        int currentConsumers = currentMetricData.getConsumers();

        // current processing speed
        int tasksProcessedDelta = currentMetricData.getTasksProcessed() - prevMetricData.getTasksProcessed();
        double currentSpeed = tasksProcessedDelta / (double) timeDelta.getMinutes();

        // desired speed to finish all tasks in time left
        Minutes timeLeft = timer.getTimeLeft().toStandardMinutes();
        double desiredSpeed = currentMetricData.getTasksLeft() / (double) timeLeft.getMinutes();

        int desiredConsumers = (int) Math.ceil(currentConsumers * desiredSpeed / currentSpeed);

        logger.info("Current consumers: {}, Desired consumers: {}", currentConsumers, desiredConsumers);
        if (desiredConsumers != currentConsumers) {
            return Optional.of(new SetCapacityCommand(desiredConsumers));
        }
        return Optional.empty();
    }

    private boolean isNewHistoricalStatus(TaskStatus taskStatus) {
        return previousHistoricalStatus.map(last ->
            Seconds.secondsBetween(last.getTimestamp(), taskStatus.getTimestamp()).isGreaterThan(EVALUATION_FREQUENCY.toStandardSeconds()))
            .orElse(false);
    }
}
