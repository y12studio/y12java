package tw.y12.beyes;

import org.bitcoinj.store.UnreadableWalletException;

import tw.y12.beyes.event.EventDebug;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class AppBaseSrvImpl implements AppBaseSrv {

	private final EventBus eventBus;
	private final Config conf;

	public AppBaseSrvImpl() throws UnreadableWalletException {
		this.eventBus = new EventBus("AppEventBus");
		this.eventBus.register(this);
		conf = ConfigFactory.load();

		String fbToken = conf.getString("firebase_token");
		String fbUrl = conf.getString("firebase_url");

		WalletFirebaseListener wfl = new WalletFirebaseListener(fbToken, fbUrl);
		this.eventBus.register(wfl);
	}

	public EventBus getEventBus() {
		return this.eventBus;
	}

	@Subscribe
	public void listenEvDebug(EventDebug ev) {
		System.out.println("Debug in " + this.getClass().getName()
				+ " with msg= " + ev.getMessage());
	}

	public Config getConf() {
		return conf;
	}

}