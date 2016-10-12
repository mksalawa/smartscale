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

    def "should emit proper capacity on command"() {
        given:
        def request = new SetDesiredCapacityRequest()
                .withAutoScalingGroupName(autoscalingGroupName)
                .withDesiredCapacity(baseCapacity)

        when:
        emitter.emit(new SetCapacityCommand(baseCapacity))

        then:
        1 * clientMock.describeAutoScalingGroups(autoScalingGroupsRequest) >> resultMock
        1 * resultMock.getAutoScalingGroups() >> this.autoScalingGroups
        1 * clientMock.setDesiredCapacity(request)
    }
}
