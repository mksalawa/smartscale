package pl.edu.agh.smartscale.command;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.ComparisonOperator;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricAlarmRequest;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.Statistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AWSCommandEmitter implements CommandEmitter {

    private static final Logger logger = LoggerFactory.getLogger(AWSCommandEmitter.class);

    private final AmazonCloudWatch cloudWatch;
    public final String namespace = "smartscale";
    public final String metricName = "ScaleRequest";

    public AWSCommandEmitter(AmazonCloudWatch cloudWatch) {
        this.cloudWatch = cloudWatch;
        createAlarm();
    }

    @Override
    public void emit(Command command) {
        switch (command) {
            case SCALE_UP:
                emitScaleUp();
                break;
            case SCALE_DOWN:
                emitScaleDown();
                break;
            default:
                logger.error("Command %s not supported.", command);
        }
    }

    private void emitScaleUp() {
        cloudWatch.putMetricData(createPutMetricDataRequest(metricName, namespace, 1.0));
    }

    private void emitScaleDown() {
        cloudWatch.putMetricData(createPutMetricDataRequest(metricName, namespace, 0));
    }

    private PutMetricDataRequest createPutMetricDataRequest(String name, String ns, double value) {
        return new PutMetricDataRequest()
            .withNamespace(ns)
            .withMetricData(new MetricDatum()
                .withMetricName(name)
                .withValue(value));
    }

    private void createAlarm() {
        PutMetricAlarmRequest alarmRequest = new PutMetricAlarmRequest()
            .withAlarmName("Scale request")
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
