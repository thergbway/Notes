package webServer;

import com.googlecode.jsonrpc4j.JsonRpcServer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;

public class WebServiceServlet extends HttpServlet {
    public static final String WEB_SERVICE_SERVLET_NAME = "WebServiceServletName";

    private JsonRpcServer jsonRpcServer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        WebServerMethodInvoker methodInvoker =
                ((WebServerMethodInvoker) getServletContext().getAttribute(WEB_SERVICE_SERVLET_NAME));
        WebService webService = new WebService(methodInvoker);

        jsonRpcServer = new JsonRpcServer(webService, webService.getClass());
        jsonRpcServer.setAllowLessParams(true);
        jsonRpcServer.setAllowExtraParams(true);
        jsonRpcServer.setExceptionLogLevel(Level.INFO);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        jsonRpcServer.handle(req, resp);
    }
}