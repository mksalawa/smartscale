package pl.edu.agh.smartscale.config;

import com.google.common.collect.ImmutableMap;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.smartscale.scaling.strategy.StrategyType;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static final ImmutableMap<String, StrategyType> strategiesMap = ImmutableMap.of("LINEAR", StrategyType.LINEAR);

    public static Config readProperties(String configFileName) throws ParametersNotFoundException {
        logger.info("Reading credentials from environment variables.");
        String time = System.getenv("TIME");
        String strategy = System.getenv("STRATEGY");
        if (time != null && strategy != null) {
            return new Config(getTimeLeft(time), getStrategyType(strategy));
        }
        try (InputStream input = ClassLoader.getSystemResourceAsStream(configFileName)) {
            logger.info("Reading properties from file: {}.", configFileName);
            Properties props = new Properties();
            props.load(input);
            time = props.getProperty("time");
            strategy = props.getProperty("strategy");
            if (time != null && strategy != null) {
                return new Config(getTimeLeft(time), getStrategyType(strategy));
            }
        } catch (FileNotFoundException e) {
            logger.error("Properties file not found.", e);
        } catch (Exception e) {
            logger.error("Error while reading properties file.", e);
        }
        throw new ParametersNotFoundException("Parameters not found.");
    }

    private static StrategyType getStrategyType(String strategy) {
        return strategiesMap.get(strategy);
    }

    private static Duration getTimeLeft(String time) {
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendHours()
                .appendLiteral(":")
                .appendMinutes()
                .toFormatter();
        return formatter.parsePeriod(time).toStandardDuration();
    }
}
