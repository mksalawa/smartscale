package pl.edu.agh.smartscale.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.smartscale.scaling.strategy.StrategyType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigReader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);

    private static final String TIME_KEY = "TIME";
    private static final String STRATEGY_KEY = "STRATEGY";
    private static final String GROUP_NAME_KEY = "GROUP_NAME";
    private static final String MAX_INSTANCES_KEY = "MAX_INSTANCES";
    private static final ImmutableList<String> configKeys =
        ImmutableList.of(TIME_KEY, STRATEGY_KEY, GROUP_NAME_KEY, MAX_INSTANCES_KEY);
    private static final ImmutableMap<String, StrategyType> strategiesMap = ImmutableMap.of("LINEAR", StrategyType.LINEAR);

    public static Config readProperties(String configFileName) throws ParametersNotFoundException {
        logger.info("Reading properties from environment variables...");

        Map<String, String> configValues = new HashMap<>();
        for (String key : configKeys) {
            String envVal = System.getenv(key);
            if (envVal != null) {
                configValues.put(key, envVal);
            }
        }

        if (configValues.size() < configKeys.size()) {
            try (InputStream input = ClassLoader.getSystemResourceAsStream(configFileName)) {
                logger.info("Reading {} missing properties from file: {}.", configKeys.size() - configValues.size(), configFileName);
                Properties props = new Properties();
                props.load(input);
                for (String key : configKeys) {
                    if (!configValues.containsKey(key)) {
                        String value = props.getProperty(key);
                        if (value == null) {
                            throw new ParametersNotFoundException("Parameter " + key + " not found.");
                        }
                        configValues.put(key, value);
                    }
                }
            } catch (FileNotFoundException e) {
                logger.error("Properties file not found.", e);
            } catch (IOException e) {
                logger.error("Error while reading properties file.", e);
            }
        }
        return new Config(parseTimeLeft(configValues.get(TIME_KEY)), getStrategyType(configValues.get(STRATEGY_KEY)),
            configValues.get(GROUP_NAME_KEY), Integer.valueOf(configValues.get(MAX_INSTANCES_KEY)));
    }

    private static StrategyType getStrategyType(String strategy) {
        return strategiesMap.get(strategy.toUpperCase());
    }

    private static Duration parseTimeLeft(String time) {
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendHours()
                .appendLiteral(":")
                .appendMinutes()
                .toFormatter();
        return formatter.parsePeriod(time).toStandardDuration();
    }
}
