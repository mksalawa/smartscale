package pl.edu.agh.smartscale.metrics;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.smartscale.scaling.StatusListener;
import pl.edu.agh.smartscale.scaling.TaskStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

public class MetricsCollector implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MetricsCollector.class);
    private static final int METRIC_COLLECTOR_PORT = 9002;
    private final StatusListener statusListener;

    public MetricsCollector(StatusListener statusListener) {
        this.statusListener = statusListener;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(METRIC_COLLECTOR_PORT)) {
            while (true) {
                Optional<MetricData> data = receiveDataFromMonitoringPlugin(serverSocket);
                data.ifPresent(d -> {
                        TaskStatus taskStatus = TaskStatus.builder()
                            .metricData(d)
                            .timestamp(DateTime.now())
                            .build();
                        logger.info("Collector received data: {} - {}", d, taskStatus.getTimestamp().toString());
                        statusListener.receive(taskStatus);
                    }
                );
                if (!data.isPresent()) {
                    logger.warn("Data from monitoring plugin not received.");
                }
            }
        } catch (IOException e) {
            logger.error("Error while creating server socket.", e);
        }
    }

    private Optional<MetricData> receiveDataFromMonitoringPlugin(ServerSocket serverSocket) {
        try (Socket client = serverSocket.accept(); BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {

            int tasksLeft = Integer.parseInt(in.readLine().split("\\s")[1]);
            int outputsLeft = Integer.parseInt(in.readLine().split("\\s")[1]);
            int tasksProcessed = Integer.parseInt(in.readLine().split("\\s")[1]);
            int allTasks = Integer.parseInt(in.readLine().split("\\s")[1]);
            int stage = Integer.parseInt(in.readLine().split("\\s")[1]);
            int consumers = Integer.parseInt(in.readLine().split("\\s")[1]);

            return Optional.of(MetricData.builder()
                .tasksLeft(tasksLeft)
                .outputsLeft(outputsLeft)
                .tasksProcessed(tasksProcessed)
                .allTasks(allTasks)
                .stage(stage)
                .consumers(consumers)
                .build());
        } catch (IOException e) {
            logger.error("Error while establishing connection.", e);
        }
        return Optional.empty();
    }
}
