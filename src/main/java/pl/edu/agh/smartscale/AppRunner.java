package pl.edu.agh.smartscale;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.ComparisonOperator;
import com.amazonaws.services.cloudwatch.model.PutMetricAlarmRequest;
import com.amazonaws.services.cloudwatch.model.Statistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.smartscale.command.AWSCommandEmitter;
import pl.edu.agh.smartscale.command.Command;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);
    private static final String AWS_PROPERTIES_FILE = "aws.properties";

    public static void main(String[] args) {
        BasicAWSCredentials credentials = getAWSCredentials();
        if (credentials == null) {
            logger.error("Error while getting credentials.");
            return;
        }

        AmazonCloudWatchClient cloudWatch = new AmazonCloudWatchClient(credentials);
        AWSCommandEmitter emitter = new AWSCommandEmitter(cloudWatch);

        emitter.emit(Command.SCALE_UP);
    }

    private static BasicAWSCredentials getAWSCredentials() {
        try (InputStream input = new FileInputStream(AWS_PROPERTIES_FILE)) {
            Properties props = new Properties();
            props.load(input);
            return new BasicAWSCredentials(props.getProperty("accesskey"), props.getProperty("secretkey"));
        } catch (FileNotFoundException e) {
            logger.error("Properties file not found.", e);
            return null;
        } catch (IOException e) {
            logger.error("IO error while reading properties file.", e);
            return null;
        }
    }
}
