package pl.edu.agh.smartscale.command


import com.amazonaws.services.cloudwatch.AmazonCloudWatch
import com.amazonaws.services.cloudwatch.model.MetricDatum
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest

import spock.lang.Specification
import spock.lang.Unroll

class AWSMetricDataEmitterTest extends Specification {

    AWSMetricDataEmitter emitter
    AmazonCloudWatch clientMock

    def setup() {
        clientMock = Mock(AmazonCloudWatch.class)
        emitter = new AWSMetricDataEmitter(clientMock);
    }

    @Unroll
    def "should emit #value on #command"() {
        when:
        emitter.emit(command)

        then:
        1 * clientMock.putMetricData(new PutMetricDataRequest()
                .withNamespace(emitter.namespace)
                .withMetricData(new MetricDatum()
                .withMetricName(emitter.metricName)
                .withValue(value)))

        where:
        command            || value
        Command.SCALE_UP   || 1.0
        Command.SCALE_DOWN || 0.0
    }
}
