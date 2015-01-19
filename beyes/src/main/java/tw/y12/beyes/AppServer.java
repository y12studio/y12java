package tw.y12.beyes;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Date;
import java.util.EventListener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.component.Container;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.component.LifeCycle.Listener;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

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

	public void startRest() throws Exception {
		// evBus.register(this);
		Server server = new Server(8080);
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/rest");
		webapp.setResourceBase(".");
		// https://github.com/resteasy/Resteasy/blob/master/jaxrs/examples/guice-hello/src/main/webapp/WEB-INF/web.xml

		webapp.setInitParameter("resteasy.guice.modules",
				AppModule.class.getName());
		webapp.addEventListener(new GuiceResteasyBootstrapServletContextListener());
		webapp.addServlet(new ServletHolder(new HttpServletDispatcher()), "/*");
		server.setHandler(webapp);
		server.start();
	}

	public static void main(String[] args) throws Exception {

		// new WebServer().start(eventBus);
		new AppServer().startRest();

		System.out.println("WebServer Start.");

	}

}
