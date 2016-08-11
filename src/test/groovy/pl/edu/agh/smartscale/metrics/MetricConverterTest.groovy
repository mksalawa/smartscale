package pl.edu.agh.smartscale.metrics

import spock.lang.Specification

class MetricConverterTest extends Specification {

    MetricConverter converter = new MetricConverter()

    def "should create MetricData class with expected values"() {
        given:
        def data = "HyperFlow.nTasksLeft 38 1470847245\n" +
                "HyperFlow.nOutputsLeft 75 1470847245\n" +
                "HyperFlow.nTasksProcessed 105 1470847245\n" +
                "HyperFlow.nTasks 144 1470847245\n" +
                "HyperFlow.stage 2 1470847245\n" +
                "HyperFlow.nConsumers 8 1470847245\n"

        when:
        def metricData = converter.convert(data)

        then:
        metricData.tasksLeft == 38
        metricData.outputsLeft == 75
        metricData.tasksProcessed == 105
        metricData.allTasks == 144
        metricData.stage == 2
        metricData.consumers == 8
    }
}
