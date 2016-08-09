package pl.edu.agh.smartscale.metrics;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MetricData {
    int tasksLeft;
    int outputsLeft;
    int tasksProcessed;
    int allTasks;
    int stage;
    int consumers;
}
