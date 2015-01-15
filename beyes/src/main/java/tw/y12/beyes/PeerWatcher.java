package tw.y12.beyes;

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

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

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

	public PeerWatcher(String fbToken) {
		BriefLogFormatter.init();
		NetworkParameters params = MainNetParams.get();
		peerGroup = new PeerGroup(params, null /* no chain */);
		peerGroup.setUserAgent("PeerMonitor", "1.0");
		peerGroup.addPeerDiscovery(new DnsDiscovery(params));
		peerGroup.addEventListener(pev, Threading.SAME_THREAD);
		eventBus.register(new BlockChainListener(fbToken));
	}

	public void start() {
		peerGroup.startAsync();
		peerGroup.awaitRunning();
	}

	public static void main(String[] args) {

		String fbToken = System.getProperty("fb.token");
		if (fbToken == null) {
			System.out.println("java -Dfb.token=xxx -jar app.jar");
			System.exit(-1);
		}

		PeerWatcher watcher = new PeerWatcher(fbToken);
		watcher.start();
	}

}
