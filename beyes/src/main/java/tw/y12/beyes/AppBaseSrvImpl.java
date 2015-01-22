package tw.y12.beyes;

import org.bitcoinj.store.UnreadableWalletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.y12.beyes.event.EventDebug;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class AppBaseSrvImpl implements AppBaseSrv {
	
	protected static final Logger log = LoggerFactory
			.getLogger(AppBaseSrvImpl.class);

	private final EventBus eventBus;
	private final Config conf;

	public AppBaseSrvImpl() throws UnreadableWalletException {
		this.eventBus = new EventBus("AppEventBus");
		this.eventBus.register(new DebugListener());
		conf = ConfigFactory.load();

		String fbToken = conf.getString("firebase_token");
		String fbUrl = conf.getString("firebase_url");

		FirebaseListener wfl = new FirebaseListener(fbToken, fbUrl);
		this.eventBus.register(wfl);
	}

	public EventBus getEventBus() {
		return this.eventBus;
	}

	public Config getConf() {
		return conf;
	}

}
