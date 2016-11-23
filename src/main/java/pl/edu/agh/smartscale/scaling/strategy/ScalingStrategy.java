package pl.edu.agh.smartscale.scaling.strategy;

import pl.edu.agh.smartscale.command.Command;
import pl.edu.agh.smartscale.scaling.TaskStatus;

import java.util.Optional;

public interface ScalingStrategy {
    Optional<Command> process(TaskStatus taskStatus);
}
