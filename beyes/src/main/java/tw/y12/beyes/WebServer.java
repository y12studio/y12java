package tw.y12.beyes;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class WebServer {

	@SuppressWarnings("serial")
	public static class AppServlet extends HttpServlet {

		private EventBus evBus;

		public AppServlet(EventBus eventBus) {
			this.evBus = eventBus;
		}

		@Override
		protected void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			if (request.getPathInfo().equals("/app")) {
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println("Hello SimpleServlet");
				this.evBus.post(new EventDebug("HTTP GET "
						+ request.getPathInfo() + " " + new Date()));
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

//	@Subscribe
//	public void listenEvDebug(EventDebug ev) {
//		System.out.println("Debug in WebServer " + ev.getMessage());
//	}

	public void start(EventBus evBus) throws Exception {

		evBus.register(this);

		Server server = new Server(8080);

		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);

		handler.addServletWithMapping(new ServletHolder(new AppServlet(evBus)),
				"/*");

		server.start();
		// server.join();
	}

	public static void main(String[] args) throws Exception {
		EventBus eventBus = new EventBus("test");
		new WebServer().start(eventBus);

		System.out.println("WebServer Start.");

	}

}
