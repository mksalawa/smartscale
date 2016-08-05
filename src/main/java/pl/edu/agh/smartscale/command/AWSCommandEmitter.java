package pl.edu.agh.smartscale.command;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;


public class AWSCommandEmitter implements CommandEmitter {

    private final AmazonCloudWatch cloudWatch;
    private final String namespace;
    private final String metricName;

    public AWSCommandEmitter(AmazonCloudWatch cloudWatch, String namespace, String metricName) {
        this.cloudWatch = cloudWatch;
        this.namespace = namespace;
        this.metricName = metricName;
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

}
