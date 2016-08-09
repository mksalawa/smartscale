package pl.edu.agh.smartscale.metrics;

public interface MetricCollector {
    int getTasksLeft();
    int getOutputsLeft();
    int getTasksProcessed();
    int getAllTasks();
    int getStage();
    int getConsumers();
}
