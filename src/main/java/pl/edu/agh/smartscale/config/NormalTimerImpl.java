package pl.edu.agh.smartscale.config;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class NormalTimerImpl implements Timer {

    private final DateTime startDate;
    private final DateTime prefferedEndDate;

    public NormalTimerImpl( DateTime startDate, DateTime prefferedEndDate) {
        this.startDate = startDate;
        this.prefferedEndDate = prefferedEndDate;
    }

    @Override
    public Duration getTimeLeft() {
        return new Duration(DateTime.now(), prefferedEndDate);
    }

    public Duration getTotalTimeForExecution() {
        return new Duration(startDate, prefferedEndDate);
    }
}