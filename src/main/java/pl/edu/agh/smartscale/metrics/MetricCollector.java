package pl.edu.agh.smartscale.metrics;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.smartscale.events.Event;
import pl.edu.agh.smartscale.events.Topic;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MetricCollector implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MetricCollector.class);
    private static final int METRIC_COLLECTOR_PORT = 9002;
    private final MetricConverter metricConverter;
    private final Topic topic;

    public MetricCollector(MetricConverter metricConverter, Topic topic) {
        this.metricConverter = metricConverter;
        this.topic = topic;
    }

    @Override
    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(METRIC_COLLECTOR_PORT)) {
            while (true) {
                final String data = receiveDataFromMonitoringPlugin(serverSocket);
                if (data != null) {
                    final MetricData metricData = metricConverter.convert(data);
                    if (isValid(metricData)) {
                        topic.sendEvent(Event.builder()
                                .metricData(metricData)
                                .timestamp(new DateTime())
                                .build());
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error while creating server socket.");
        }
    }

    private boolean isValid(MetricData metricData) {
        return isFieldValid(metricData.getTasksLeft()) && isFieldValid(metricData.getOutputsLeft())
                && isFieldValid(metricData.getTasksProcessed()) && isFieldValid(metricData.getAllTasks())
                && isFieldValid(metricData.getStage()) && isFieldValid(metricData.getConsumers());
    }

    private boolean isFieldValid(int dataValue) {
        return dataValue != -1;
    }

    private String receiveDataFromMonitoringPlugin(ServerSocket serverSocket) {
        try (Socket client = serverSocket.accept(); InputStream in = client.getInputStream()) {
            byte[] receivedBytes = new byte[1024];
            //TODO: loop reading data until EOF
            final int len = in.read(receivedBytes);
            return new String(receivedBytes, 0,  len);
        } catch (IOException e) {
            logger.error("Error while establishing connection.");
        }
        return null;
    }
}
