package tw.y12.beyes.bak;

import java.util.Set;

import org.bitcoinj.core.AbstractPeerEventListener;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InventoryItem;
import org.bitcoinj.core.InventoryMessage;
import org.bitcoinj.core.Message;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.PeerEventListener;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.utils.Threading;

import tw.y12.beyes.event.EventGetBlock;
import tw.y12.beyes.event.EventInvBlock;
import tw.y12.beyes.event.EventInvTx;
import tw.y12.beyes.event.EventMessage;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Deprecated
public class PeerWatcher {

	private EventBus eventBus = new EventBus("peerwatcher");

	private PeerGroup peerGroup;

	private PeerEventListener pev = new AbstractPeerEventListener() {

		private Set<String> bset = Sets.newHashSet();

		@Override
		public void onPeerConnected(Peer peer, int peerCount) {
			System.out.println(peer);
		}

		@Override
		public Message onPreMessageReceived(Peer peer, Message m) {
			eventBus.post(new EventMessage(m));
			try {
				if (m instanceof InventoryMessage) {
					InventoryMessage inv = (InventoryMessage) m;
					for (InventoryItem invt : inv.getItems()) {
						if (invt.type == InventoryItem.Type.Block) {
							String id = invt.hash.toString();
							eventBus.post(new EventInvBlock(id));
							if (!bset.contains(id)) {
								peer.getBlock(invt.hash);
							}
							bset.add(id);
						}
					}
				}
				if (m instanceof Block) {
					Block b = (Block) m;
					eventBus.post(new EventGetBlock(b));
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return super.onPreMessageReceived(peer, m);
		}

		private void printTxOutput(Transaction t) {

			long totalOut = 0;
			for (TransactionOutput out : t.getOutputs()) {
				totalOut += out.getValue().value;
			}
			System.out.println(t.getHashAsString() + ":"
					+ Coin.valueOf(totalOut).toFriendlyString());
		}

		public void onTransaction(Peer peer, Transaction t) {
			String tid = t.getHashAsString();
			eventBus.post(new EventInvTx(tid));
		}
	};

	public PeerWatcher(String fbToken,String fbUrl) {
		BriefLogFormatter.init();
		NetworkParameters params = MainNetParams.get();
		peerGroup = new PeerGroup(params, null /* no chain */);
		peerGroup.setUserAgent("PeerMonitor", "1.0");
		peerGroup.addPeerDiscovery(new DnsDiscovery(params));
		peerGroup.addEventListener(pev, Threading.SAME_THREAD);
		eventBus.register(new PeerFirebaseListener(fbToken,fbUrl));
	}

	public void start() {
		peerGroup.startAsync();
		peerGroup.awaitRunning();
	}

	public static void main(String[] args) {

		Config conf = ConfigFactory.load();
		String confFbToken = conf.getString("firebase_token");
		String confFbUrl = conf.getString("firebase_url");
		System.out.println(confFbToken);
		System.out.println(confFbUrl);
		PeerWatcher watcher = new PeerWatcher(confFbToken, confFbUrl);
		watcher.start();
	}

}
