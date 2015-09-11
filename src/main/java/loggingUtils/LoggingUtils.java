package loggingUtils;

import org.apache.log4j.xml.DOMConfigurator;

public class LoggingUtils {
    public static void initLoggingSystem() {
        DOMConfigurator.configure(LoggingUtils.class.getResource("/cfg/log4j.xml"));
    }

    private LoggingUtils() {
    }
}