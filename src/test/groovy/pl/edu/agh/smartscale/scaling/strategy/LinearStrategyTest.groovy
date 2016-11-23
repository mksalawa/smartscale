package pl.edu.agh.smartscale.scaling.strategy

import org.joda.time.DateTime
import pl.edu.agh.smartscale.command.SetCapacityCommand
import pl.edu.agh.smartscale.config.NormalTimerImpl
import pl.edu.agh.smartscale.scaling.TaskStatus
import pl.edu.agh.smartscale.metrics.MetricData
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static org.joda.time.Duration.standardMinutes

class LinearStrategyTest extends Specification {

    final evaluationFrequency = standardMinutes(5)
    final timer = Mock(NormalTimerImpl.class)
    @Subject
    final strategy = new LinearStrategy(timer, evaluationFrequency)
    final baseTimestamp = new DateTime(2016, 01, 01, 10, 00, 00)

    @Unroll
    def "should return #command when needing 20 min and having #timeLeft.standardMinutes min (consumers: #consumers)"() {
        given:
        final prevStatus = taskStatus(
                timestamp: baseTimestamp,
                allTasks: 10,
                tasksLeft: 5,
                tasksProcessed: 5,
                consumers: consumers)
        final currentStatus = taskStatus(
                timestamp: (baseTimestamp + evaluationFrequency).plusSeconds(1),
                allTasks: 10,
                tasksLeft: 4,
                tasksProcessed: 6,
                consumers: consumers)
        timer.getTimeLeft() >> timeLeft

        when:
        final cmdBefore = strategy.process(prevStatus)
        final cmdAfter = strategy.process(currentStatus)

        then:
        cmdBefore == Optional.empty()
        cmdAfter == command

        where:
        // expected needed time: 20min
        timeLeft            || consumers || command
        standardMinutes(20) || 1         || Optional.empty()

        standardMinutes(10) || 1         || Optional.of(new SetCapacityCommand(2))
        standardMinutes(15) || 1         || Optional.of(new SetCapacityCommand(2))
        standardMinutes(19) || 1         || Optional.of(new SetCapacityCommand(2))

        standardMinutes(35) || 2         || Optional.empty()
        standardMinutes(40) || 2         || Optional.of(new SetCapacityCommand(1))
        standardMinutes(45) || 2         || Optional.of(new SetCapacityCommand(1))

        standardMinutes(10) || 2         || Optional.of(new SetCapacityCommand(4))
        standardMinutes(15) || 2         || Optional.of(new SetCapacityCommand(3))
    }

    private static TaskStatus taskStatus(Map args) {
        return TaskStatus.builder()
                .timestamp(args.timestamp as DateTime)
                .metricData(MetricData.builder()
                    .allTasks(args.allTasks as Integer)
                    .tasksLeft(args.tasksLeft as Integer)
                    .tasksProcessed(args.tasksProcessed as Integer)
                    .consumers(args.consumers as Integer)
                    .build())
                .build()
    }
}
