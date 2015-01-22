package tw.y12.beyes;

import java.util.Map;

import org.bitcoinj.core.StoredBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.y12.beyes.event.EventNewBestBlock;
import tw.y12.beyes.event.EventSendCoin;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

public class BlockActerImpl implements BlockActer {

	protected static final Logger log = LoggerFactory
			.getLogger(BlockActerImpl.class);

	private final AppBaseSrv appBaseSrv;
	private final WalletWatcher walletWatcher;
	private EventBus eventBus;

	@Inject
	public BlockActerImpl(AppBaseSrv appBaseSrv, WalletWatcher walletWatcher) {
		super();
		this.appBaseSrv = appBaseSrv;
		this.walletWatcher = walletWatcher;
		this.eventBus = this.appBaseSrv.getEventBus();
		this.eventBus.register(this);
	}

	@Subscribe
	public void listenNewBestBlock(EventNewBestBlock ev) {
		StoredBlock b = ev.getBlock();
		try {
			log.info("[BlockActer] EvBus Store Block :" + b.getHeight());
			sendTestAddrWithBlockHeightTag(b);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void sendTestAddrWithBlockHeightTag(StoredBlock b) {
		// 72 blocks ~= 12 hrs 
		// 12 blocks ~= 2 hrs
		if (b.getHeight() % 36 == 0) {
			this.eventBus.post(new EventSendCoin(
					"1PDjw7r4MkaP8Q3QYhdeuv1M9Xj1yYPndL", b.getHeight(),
					"BK" + b.getHeight() + "@590c.org", 0));
		}
	}

	public String getResult() {
		return null;
	}

}
