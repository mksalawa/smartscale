package pl.edu.agh.smartscale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.smartscale.metrics.MetricConverter;
import pl.edu.agh.smartscale.metrics.MetricListener;

public class AppRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    public static void main(String[] args) {

        Thread metricListener = new Thread(new MetricListener(new MetricConverter()));
        metricListener.start();

    }
}
