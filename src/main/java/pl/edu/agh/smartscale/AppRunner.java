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

    public static void main(String[] args) {
        Properties props = new Properties();
        BasicAWSCredentials credentials;

        try (InputStream input = new FileInputStream("aws.properties")) {
            props.load(input);
            credentials = new BasicAWSCredentials(props.getProperty("accesskey"), props.getProperty("secretkey"));
        } catch (FileNotFoundException e) {
            logger.error("Properties file not found.", e);
            return;
        } catch (IOException e) {
            logger.error("IO error while reading properties file.", e);
            return;
        }

        String namespace = "smartscale";
        String metricName = "ScaleRequest";
        AmazonCloudWatchClient cloudWatch = new AmazonCloudWatchClient(credentials);

        createAlarm(cloudWatch, namespace, metricName);

        AWSCommandEmitter emitter = new AWSCommandEmitter(cloudWatch, namespace, metricName);

        emitter.emit(Command.SCALE_UP);


    }

    private static void createAlarm(AmazonCloudWatchClient cloudWatch, String namespace, String metricName) {
        PutMetricAlarmRequest alarmRequest = new PutMetricAlarmRequest()
            .withAlarmName("Scale up requested")
            .withMetricName(metricName)
            .withNamespace(namespace)
            .withEvaluationPeriods(1)
            .withPeriod(60)
            .withStatistic(Statistic.Maximum)
            .withThreshold(1.0)
            .withComparisonOperator(ComparisonOperator.GreaterThanOrEqualToThreshold);

        cloudWatch.putMetricAlarm(alarmRequest);
    }
}
