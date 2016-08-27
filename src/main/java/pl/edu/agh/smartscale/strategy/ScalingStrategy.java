package pl.edu.agh.smartscale.strategy;

import pl.edu.agh.smartscale.command.Command;
import pl.edu.agh.smartscale.events.TaskStatus;

import java.util.Optional;

public interface ScalingStrategy {
    Optional<Command> process(TaskStatus taskStatus);
}
