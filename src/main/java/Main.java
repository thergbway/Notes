import loggingUtils.Loggers;
import loggingUtils.LoggingUtils;
import webServer.WebServer;
import webServer.WebServerMethodInvokerImpl;

public class Main {
    public static void main(String[] args) throws Exception {
        LoggingUtils.initLoggingSystem();
        Loggers.MAIN_CYCLE.info("Initializing application");
        Loggers.MAIN_CYCLE.info("Press Ctrl+C to stop the server");

        final WebServer webServer = new WebServer(new WebServerMethodInvokerImpl());
        webServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                webServer.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }
}