package pl.edu.agh.smartscale.command;

import com.amazonaws.services.autoscaling.AmazonAutoScalingClient;
import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsRequest;
import com.amazonaws.services.autoscaling.model.SetDesiredCapacityRequest;
import com.amazonaws.services.autoscaling.model.UpdateAutoScalingGroupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AWSCapacityEmitter implements CommandEmitter {

    private static final Logger logger = LoggerFactory.getLogger(AWSCapacityEmitter.class);
    private AmazonAutoScalingClient autoScalingClient;
    private final String autoscalingGroupName;

    public AWSCapacityEmitter(String autoscalingGroupName, AmazonAutoScalingClient autoScalingClient) {
        this.autoscalingGroupName = autoscalingGroupName;
        this.autoScalingClient = autoScalingClient;
    }

    @Override
    public void emit(Command command) {
        DescribeAutoScalingGroupsRequest groupsRequest = new DescribeAutoScalingGroupsRequest().withAutoScalingGroupNames(autoscalingGroupName);
        AutoScalingGroup autoScalingGroup = autoScalingClient.describeAutoScalingGroups(groupsRequest).getAutoScalingGroups().get(0);

        int capacity = autoScalingGroup.getDesiredCapacity();
        int desiredCapacity = command.getDesiredConsumers();
        if (desiredCapacity != capacity) {
            setDesiredCapacity(desiredCapacity);
        } else {
            logger.info("Skipping emitting current capacity: {}", desiredCapacity);
        }
    }

    private void setDesiredCapacity(int capacity) {
        logger.info("Emitting desired capacity: {}", capacity);
        autoScalingClient.setDesiredCapacity(new SetDesiredCapacityRequest()
            .withAutoScalingGroupName(autoscalingGroupName)
            .withDesiredCapacity(capacity));
    }

    public void emitMaxInstances(int maxInstances) {
        logger.info("Emitting max number of instances: {}", maxInstances);
        autoScalingClient.updateAutoScalingGroup(new UpdateAutoScalingGroupRequest()
            .withAutoScalingGroupName(autoscalingGroupName)
            .withMaxSize(maxInstances));
    }
}
