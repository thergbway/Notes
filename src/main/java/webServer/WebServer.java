package webServer;

import loggingUtils.Loggers;
import serverConfiguration.ServerConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.component.AbstractLifeCycle;

public class WebServer {
    private WebServerMethodInvoker methodInvoker;
    private Server server = new Server(((Integer) ServerConfiguration.getProperty("port")));

    public WebServer(WebServerMethodInvoker methodInvoker) {
        this.methodInvoker = methodInvoker;
    }

    private Integer port;

    public Integer getPort() {
        if (!server.isStarted())
            throw new IllegalStateException("Server is not started. Current state is " + server.getState());
        return server.getURI().getPort();
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void start() throws Exception {
        if (server.getState() != AbstractLifeCycle.STOPPED)
            throw new IllegalStateException("Server state is not stopped! It`s current state is " + server.getState());
        ServletHandler handler = new ServletHandler();
        Object entry = ServerConfiguration.getProperty("entry");
        handler.addServletWithMapping(WebServiceServlet.class, ((String) entry));
        server.setHandler(handler);
        server.start();

        handler.getServletContext().setAttribute(WebServiceServlet.WEB_SERVICE_SERVLET_NAME, methodInvoker);

        Loggers.WEB_SERVER.info("Web server started at port " + port);
        Loggers.WEB_SERVER.info("JsonRpc Api is opened under the name " + entry);
    }

    public void stop() throws Exception {
        server.stop();
        server.join();

        Loggers.WEB_SERVER.info("Web server stopped");
    }
}