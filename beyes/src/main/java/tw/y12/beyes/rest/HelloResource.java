package tw.y12.beyes.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import tw.y12.beyes.AppBaseSrv;
import tw.y12.beyes.event.EventDebug;

import com.google.inject.Inject;

@Path("hello")
public class HelloResource {
	
	private final Greeter greeter;
	private final AppBaseSrv apsrv;

	@Inject
	public HelloResource(final Greeter greeter, final AppBaseSrv asrv) {
		this.greeter = greeter;
		this.apsrv = asrv;
	}

	@GET
	@Path("{name}")
	public String hello(@PathParam("name") final String name) {
		this.apsrv.getEventBus().post(new EventDebug("appbasesrv="
				+ this.apsrv.toString()
				+ ", Request /rest/hello/" + name));
		return greeter.greet(name);
	}
}
