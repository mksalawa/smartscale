package pl.edu.agh.smartscale.events;

import lombok.Builder;
import lombok.Value;
import org.joda.time.DateTime;
import pl.edu.agh.smartscale.metrics.MetricData;

@Value
@Builder
public class Event {
    MetricData metricData;
    DateTime timestamp;
}
