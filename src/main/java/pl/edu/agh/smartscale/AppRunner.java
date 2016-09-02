package pl.edu.agh.smartscale;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.smartscale.command.AWSCapacityEmitter;
import pl.edu.agh.smartscale.events.MetricsListener;
import pl.edu.agh.smartscale.metrics.MetricCollector;
import pl.edu.agh.smartscale.metrics.StrategyBasedListener;
import pl.edu.agh.smartscale.strategy.TogglingStrategy;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;


public class AppRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private static final String AWS_PROPERTIES_FILE = "aws.properties";

    public static void main(String[] args) {

        Optional<BasicAWSCredentials> credentials = getAWSCredentials();
        if (!credentials.isPresent()) {
            logger.error("Error while getting credentials.");
            return;
        }

        AWSCapacityEmitter emitter = new AWSCapacityEmitter("smartscale", new AmazonAutoScalingClient(credentials.get()));

        MetricsListener listener = new StrategyBasedListener(new TogglingStrategy(), emitter);

        Thread metricListenerThread = new Thread(new MetricCollector(listener));
        metricListenerThread.start();
    }

    private static Optional<BasicAWSCredentials> getAWSCredentials() {
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
        logger.info("Reading credentials from environment variables.");
        String accesskey = System.getenv("AWS_ACCESSKEY");
        String secretkey = System.getenv("AWS_SECRETKEY");
        if (accesskey != null && secretkey != null) {
            return Optional.of(new BasicAWSCredentials(accesskey, secretkey));
        }
        logger.error("Could not read credentials.");
        return Optional.empty();
    }
}
