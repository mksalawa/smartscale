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
        switch (command) {
            case SCALE_UP:
                setDesiredCapacity(capacity + 1);
                break;
            case SCALE_DOWN:
                setDesiredCapacity(capacity > 1 ? capacity - 1 : capacity);
                break;
            default:
                logger.error("Command %s not supported.", command);
        }
    }

    private void setDesiredCapacity(int capacity) {
        autoScalingClient.setDesiredCapacity(new SetDesiredCapacityRequest()
            .withAutoScalingGroupName(autoscalingGroupName)
            .withDesiredCapacity(capacity));
    }

}
