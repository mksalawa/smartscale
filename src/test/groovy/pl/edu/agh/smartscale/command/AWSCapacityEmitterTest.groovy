package pl.edu.agh.smartscale.command

import com.amazonaws.services.autoscaling.AmazonAutoScalingClient
import com.amazonaws.services.autoscaling.model.AutoScalingGroup
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsRequest
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsResult
import com.amazonaws.services.autoscaling.model.SetDesiredCapacityRequest
import spock.lang.Specification

class AWSCapacityEmitterTest extends Specification {

    String autoscalingGroupName = "smartscale"
    AWSCapacityEmitter emitter
    AmazonAutoScalingClient clientMock
    DescribeAutoScalingGroupsResult resultMock
    DescribeAutoScalingGroupsRequest autoScalingGroupsRequest =
            new DescribeAutoScalingGroupsRequest().withAutoScalingGroupNames(autoscalingGroupName)
    List<AutoScalingGroup> autoScalingGroups = new ArrayList<AutoScalingGroup>()
    int baseCapacity = 3

    def setup() {
        clientMock = Mock(AmazonAutoScalingClient.class)
        resultMock = Mock(DescribeAutoScalingGroupsResult.class)
        emitter = new AWSCapacityEmitter(autoscalingGroupName, clientMock);

        autoScalingGroups.add(new AutoScalingGroup()
                .withAutoScalingGroupName(autoscalingGroupName)
                .withDesiredCapacity(baseCapacity))
    }

    def "should add instance to the scaling group on scale up"() {
        given:
        def request = new SetDesiredCapacityRequest()
                .withAutoScalingGroupName(autoscalingGroupName)
                .withDesiredCapacity(baseCapacity + 1)

        when:
        emitter.emit(Command.SCALE_UP)

        then:
        1 * clientMock.describeAutoScalingGroups(autoScalingGroupsRequest) >> resultMock
        1 * resultMock.getAutoScalingGroups() >> this.autoScalingGroups
        1 * clientMock.setDesiredCapacity(request)
    }

    def "should remove instance from the scaling group on scale down"() {
        given:
        def request = new SetDesiredCapacityRequest()
                .withAutoScalingGroupName(autoscalingGroupName)
                .withDesiredCapacity(baseCapacity - 1)

        when:
        emitter.emit(Command.SCALE_DOWN)

        then:
        1 * clientMock.describeAutoScalingGroups(autoScalingGroupsRequest) >> resultMock
        1 * resultMock.getAutoScalingGroups() >> this.autoScalingGroups
        1 * clientMock.setDesiredCapacity(request)
    }

    // TODO: add corner case tests
}
