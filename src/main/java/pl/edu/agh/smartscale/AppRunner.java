package pl.edu.agh.smartscale;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.smartscale.command.AWSCapacityEmitter;
import pl.edu.agh.smartscale.config.Config;
import pl.edu.agh.smartscale.config.ConfigReader;
import pl.edu.agh.smartscale.config.NormalTimerImpl;
import pl.edu.agh.smartscale.config.ParametersNotFoundException;
import pl.edu.agh.smartscale.scaling.StatusListener;
import pl.edu.agh.smartscale.metrics.MetricsCollector;
import pl.edu.agh.smartscale.metrics.StrategyBasedStatusListener;
import pl.edu.agh.smartscale.scaling.strategy.LinearStrategy;
import pl.edu.agh.smartscale.scaling.strategy.ScalingStrategy;
import pl.edu.agh.smartscale.scaling.strategy.StrategyNotFoundException;
import pl.edu.agh.smartscale.scaling.strategy.StrategyType;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;


public class AppRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private static final String AWS_PROPERTIES_FILE = "aws.properties";
    private static final String CONFIG_PROPERTIES_FILE = "config.properties";

    public static void main(String[] args) {

        Optional<BasicAWSCredentials> credentials = getAWSCredentials();
        if (!credentials.isPresent()) {
            logger.error("Error while getting credentials.");
            return;
        }

        AWSCapacityEmitter emitter = new AWSCapacityEmitter("smartscale", new AmazonAutoScalingClient(credentials.get()));
        Config config;
        try {
            config = ConfigReader.readProperties(CONFIG_PROPERTIES_FILE);
        } catch (ParametersNotFoundException e) {
            logger.error(e.getMessage());
            return;
        }
        StatusListener listener;
        try {
            listener = new StrategyBasedStatusListener(createAppropriateStrategy(config.getStrategyType(), config.getTimeLeft()), emitter);
        } catch (StrategyNotFoundException e) {
            logger.error(e.getMessage());
            return;
        }

        Thread metricCollectorThread = new Thread(new MetricsCollector(listener));
        metricCollectorThread.start();
    }

    private static ScalingStrategy createAppropriateStrategy(StrategyType strategyType, Duration timeLeft) throws StrategyNotFoundException {
        if (strategyType == StrategyType.LINEAR) {
            DateTime startDate = DateTime.now();
            return new LinearStrategy(new NormalTimerImpl(startDate, startDate.plusMinutes(timeLeft.toStandardMinutes().getMinutes())));
        } else {
            throw new StrategyNotFoundException("Strategy not found.");
        }
    }

    private static Optional<BasicAWSCredentials> getAWSCredentials() {
        logger.info("Reading credentials from environment variables.");
        String accesskey = System.getenv("AWS_ACCESSKEY");
        String secretkey = System.getenv("AWS_SECRETKEY");
        if (accesskey != null && secretkey != null) {
            return Optional.of(new BasicAWSCredentials(accesskey, secretkey));
        }
        try (InputStream input = ClassLoader.getSystemResourceAsStream(AWS_PROPERTIES_FILE)) {
            logger.info("Reading credentials from file: {}.", AWS_PROPERTIES_FILE);
            Properties props = new Properties();
            props.load(input);
            return Optional.of(new BasicAWSCredentials(props.getProperty("accesskey"), props.getProperty("secretkey")));
        } catch (FileNotFoundException e) {
            logger.error("Properties file not found.", e);
        } catch (Exception e) {
            logger.error("Error while reading properties file.", e);
        }
        logger.error("Could not read credentials.");
        return Optional.empty();
    }
}
