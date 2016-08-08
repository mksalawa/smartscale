package pl.edu.agh.smartscale.command;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.*;


public class AWSCommandEmitter implements CommandEmitter {

    private final AmazonCloudWatch cloudWatch;
    private final String namespace = "smartscale";
    private final String metricName = "ScaleRequest";

    public AWSCommandEmitter(AmazonCloudWatch cloudWatch) {
        this.cloudWatch = cloudWatch;
        createAlarm();
    }

    @Override
    public void emit(Command command) {
        PutMetricDataRequest putMetricDataRequest = new PutMetricDataRequest().withNamespace(namespace);

        putMetricDataRequest.getMetricData().add(
            new MetricDatum()
                .withMetricName(metricName)
                .withValue(1.0)
        );

        cloudWatch.putMetricData(putMetricDataRequest);
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
