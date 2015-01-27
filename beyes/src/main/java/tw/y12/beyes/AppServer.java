package tw.y12.beyes;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import com.google.common.collect.Lists;
import com.typesafe.config.Config;

public class AppServer {

	@SuppressWarnings("serial")
	public static class AppServlet extends HttpServlet {

		@Override
		protected void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			if (request.getPathInfo().equals("/app")) {
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println("Hello SimpleServlet");
				// this.evBus.post(new EventDebug("HTTP GET "+
				// request.getPathInfo() + " " + new Date()));

			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

	public void start() throws Exception {

		// evBus.register(this);
		Server server = new Server(8080);
		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);
		handler.addServletWithMapping(new ServletHolder(new AppServlet()), "/*");
		server.start();
		// server.join();
	}

	public void startRest(Config conf) throws Exception {
		// evBus.register(this);
		Server server = new Server(8080);

		WebAppContext appRoot = new WebAppContext();
		appRoot.setContextPath("/");
		String webRoot = conf.getString("web_root");

		appRoot.setResourceBase(webRoot);
		String[] wfiles = { "index.html" };
		appRoot.setWelcomeFiles(wfiles);
		appRoot.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");

		WebAppContext appRest = new WebAppContext();
		appRest.setContextPath("/rest");

		appRest.setResourceBase(".");

		// https://github.com/resteasy/Resteasy/blob/master/jaxrs/examples/guice-hello/src/main/webapp/WEB-INF/web.xml

		appRest.setInitParameter("resteasy.guice.modules",
				AppModule.class.getName());
		appRest.addEventListener(new GuiceResteasyBootstrapServletContextListener());
		appRest.addServlet(new ServletHolder(new HttpServletDispatcher()), "/*");

		HandlerCollection handlers = new HandlerCollection();
		handlers.setHandlers(new Handler[] { appRest, appRoot });
		StatisticsHandler stats = new StatisticsHandler();
		stats.setHandler(handlers);
		server.setHandler(stats);
		server.start();
	}

	public static void main(String[] args) throws Exception {

		// new WebServer().start(eventBus);
		// new AppServer().startRest();

		// System.out.println("WebServer Start.");

	}

}
