package pl.edu.agh.smartscale.metrics;


import lombok.Builder;

@Builder
public class CustomMetricCollector implements MetricCollector {

    private final int tasksLeft;
    private final int outputsLeft;
    private final int tasksProcessed;
    private final int allTasks;
    private final int stage;
    private final int consumers;

    @Override
    public int getTasksLeft() {
        return tasksLeft;
    }

    @Override
    public int getOutputsLeft() {
        return outputsLeft;
    }

    @Override
    public int getTasksProcessed() {
        return tasksProcessed;
    }

    @Override
    public int getAllTasks() {
        return allTasks;
    }

    @Override
    public int getStage() {
        return stage;
    }

    @Override
    public int getConsumers() {
        return consumers;
    }
}
