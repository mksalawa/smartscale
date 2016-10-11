package pl.edu.agh.smartscale.command;

import com.amazonaws.services.autoscaling.AmazonAutoScalingClient;
import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsRequest;
import com.amazonaws.services.autoscaling.model.SetDesiredCapacityRequest;
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

        setDesiredCapacity(command.getDesiredConsumers());
        logger.info("Emitted desired capacity: {}", capacity);
    }

    private void setDesiredCapacity(int capacity) {
        logger.info("Emitting desired capacity: {}", capacity);
        autoScalingClient.setDesiredCapacity(new SetDesiredCapacityRequest()
            .withAutoScalingGroupName(autoscalingGroupName)
            .withDesiredCapacity(capacity));
    }

}
