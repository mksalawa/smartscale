package pl.edu.agh.smartscale.events;

public interface MetricsListener {
    void receive(TaskStatus taskStatus);
}
