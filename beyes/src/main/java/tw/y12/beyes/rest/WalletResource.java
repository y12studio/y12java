package tw.y12.beyes.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import tw.y12.beyes.AppBaseSrv;
import tw.y12.beyes.event.EventDebug;
import tw.y12.beyes.event.EventSendCoin;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

@Path("wallet")
public class WalletResource {

	private final AppBaseSrv apsrv;
	private EventBus eventBus;

	@Inject
	public WalletResource(final AppBaseSrv asrv) {
		this.apsrv = asrv;
		this.eventBus = asrv.getEventBus();
	}

	@GET
	@Path("/{app}/{addr}/{coin}")
	public String wallet(@PathParam("app") final String app,
			@PathParam("addr") final String addr,
			@PathParam("coin") final String coin) {
		// http://localhost:8080/rest/wallet/send/1Kxxxx/386688
		this.eventBus.post(new EventDebug("Request /rest/wallet/" + app + "/"
				+ addr + "/" + coin));
		if(app.equals("send")){
			this.eventBus.post(new EventSendCoin(addr, Long.valueOf(coin), null, 0));
		}
		return "ok";
	}
}
