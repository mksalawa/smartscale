package pl.edu.agh.smartscale.scaling;

import lombok.Builder;
import lombok.Value;
import org.joda.time.DateTime;
import pl.edu.agh.smartscale.metrics.MetricData;

@Value
@Builder
public class TaskStatus {
    MetricData metricData;
    DateTime timestamp;
}
