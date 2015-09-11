package loggingUtils;

import org.apache.log4j.Logger;

public class Loggers {
    public static final Logger ROOT = Logger.getRootLogger();
    public static final Logger MAIN_CYCLE = Logger.getLogger("MainCycle");
    public static final Logger WEB_SERVER = Logger.getLogger("webServer");
    public static final Logger DB = Logger.getLogger("DataAccessObject");

    private Loggers() {
    }
}
