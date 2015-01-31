package tw.y12.beyes.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import tw.y12.beyes.AppBaseSrv;
import tw.y12.beyes.NonPersistConf;
import tw.y12.beyes.RestActResult;
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
	@Path("/npconf")
	@Produces(MediaType.APPLICATION_JSON)
	public NonPersistConf getNonPersistConf() {
		// /rest/hello/npconf
		this.apsrv.getEventBus().post(
				new EventDebug("appbasesrv=" + this.apsrv.toString()
						+ ", Request /rest/hello/npconf "
						+ this.apsrv.getNonPersistConf().toString()));
		return this.apsrv.getNonPersistConf();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/act/{key}/{value}")
	public RestActResult act(@PathParam("key") final String key,
			@PathParam("value") final String value) {
		RestActResult r = new RestActResult();
		if (key.equals("AutoTest1")) {
			if (value.equals("ON")) {
				this.apsrv.getNonPersistConf().setAutoTest1(true);
			} else if (value.equals("OFF")) {
				this.apsrv.getNonPersistConf().setAutoTest1(false);
			}
		}
		r.setMessage("OK");
		return r;
	}

	@GET
	@Path("/foo/{name}")
	public String hello(@PathParam("name") final String name) {
		this.apsrv.getEventBus().post(
				new EventDebug("appbasesrv=" + this.apsrv.toString()
						+ ", Request /rest/hello/foo/" + name));
		return greeter.greet(name);
	}
}
