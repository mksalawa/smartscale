package pl.edu.agh.smartscale.config;

import org.joda.time.Duration;

public class ConfigurationHelperImpl implements ConfigurationHelper {

    @Override
    public Duration getTimeLeft() {
        // TODO: implement
        return Duration.standardHours(2);
    }
}
