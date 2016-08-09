package pl.edu.agh.smartscale.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MetricListener implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MetricListener.class);
    private static final int METRIC_COLLECTOR_PORT = 9002;
    private final MetricConverter metricConverter;

    public MetricListener(MetricConverter metricConverter) {
        this.metricConverter = metricConverter;
    }

    @Override
    public void run() {

        try {
            ServerSocket serverSocket = new ServerSocket(METRIC_COLLECTOR_PORT);
            while (true) {
                final String data = receiveDataFromMonitoringPlugin(serverSocket);
                if (data != null) {
                    metricConverter.convert(data);
                }
            }
        } catch (IOException e) {
            logger.error("Error while creating server socket.");
        }

    }

    private String receiveDataFromMonitoringPlugin(ServerSocket serverSocket) {
        try {
            Socket client = serverSocket.accept();
            InputStream in = client.getInputStream();
            byte[] receivedBytes = new byte[1024];
            final int len = in.read(receivedBytes);
            return new String(receivedBytes, 0,  len);
        } catch (IOException e) {
            logger.error("Error while establishing connection.");
        }
        return null;
    }
}
