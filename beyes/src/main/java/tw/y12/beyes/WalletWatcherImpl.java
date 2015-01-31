package tw.y12.beyes;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.bitcoinj.core.AbstractPeerEventListener;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.BlockChainListener;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.InventoryItem;
import org.bitcoinj.core.InventoryMessage;
import org.bitcoinj.core.Message;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.PeerEventListener;
import org.bitcoinj.core.ScriptException;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.core.VerificationException;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.core.Wallet.BalanceType;
import org.bitcoinj.core.Wallet.SendRequest;
import org.bitcoinj.core.Wallet.SendResult;
import org.bitcoinj.core.WalletEventListener;
import org.bitcoinj.core.AbstractBlockChain.NewBlockType;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptOpCodes;
import org.bitcoinj.store.UnreadableWalletException;
import org.bitcoinj.wallet.DeterministicSeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org590c.ProtoOpReturn;
import org590c.ProtoOpReturn.OpReturn;
import org590c.ProtoOpReturn.OpReturn.Tag;
import org590c.ProtoOpReturn.OpReturn.Type;
import tw.y12.beyes.event.EventDebug;
import tw.y12.beyes.event.EventGetBlock;
import tw.y12.beyes.event.EventInvBlock;
import tw.y12.beyes.event.EventInvTx;
import tw.y12.beyes.event.EventMessage;
import tw.y12.beyes.event.EventTxCoinsReceived;
import tw.y12.beyes.event.EventSendCoin;
import tw.y12.beyes.event.EventNewBestBlock;
import tw.y12.beyes.event.EventWalletAddr;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static org.bitcoinj.script.ScriptOpCodes.OP_RETURN;

public class WalletWatcherImpl implements WalletWatcher {

	protected static final Logger log = LoggerFactory
			.getLogger(WalletWatcherImpl.class);

	private final NetworkParameters params;

	private Wallet wallet;

	private WalletEventListener wevl = new WalletEventListener() {

		public void onKeysAdded(List<ECKey> keys) {
			// TODO Auto-generated method stub

		}

		public void onWalletChanged(Wallet wallet) {
			updateNonPersistBalance();
		}

		public void onTransactionConfidenceChanged(Wallet wallet, Transaction tx) {
			// TODO Auto-generated method stub

		}

		public void onScriptsAdded(Wallet wallet, List<Script> scripts) {
			// TODO Auto-generated method stub

		}

		public void onReorganize(Wallet wallet) {
			// TODO Auto-generated method stub

		}

		public void onCoinsSent(Wallet wallet, Transaction tx,
				Coin prevBalance, Coin newBalance) {

			updateNonPersistBalance();
		}

		public void onCoinsReceived(Wallet wallet, Transaction tx,
				Coin prevBalance, Coin newBalance) {
			eventBus.post(new EventDebug("onCoinsReceived "
					+ tx.getHashAsString()
					+ " : balance from "
					+ prevBalance + " to " + newBalance));
			
			// invoke after org.bitcoinj.core.Wallet - Received a pending transaction
			// Not after org.bitcoinj.core.Wallet - Received tx for xxx BTC
			String txHash = tx.getHashAsString();
			
			EventTxCoinsReceived txev = new EventTxCoinsReceived(txHash, tx.toString());
			
			for(TransactionOutput txout : tx.getOutputs()){
				if(txout.getScriptPubKey().isOpReturn()){
					// isOpReturn = chunks.size() == 2 && chunks.get(0).equalsOpCode(OP_RETURN);
					// Utils.HEX.encode(txout.getScriptPubKey().getProgram())
					// 6a18590c080112120a0e524553542d4a41582d525340544b1000
					// Utils.HEX.encode(txout.getScriptPubKey().getChunks().get(1).data))
					// 590c080112120a0e524553542d4a41582d525340544b1000
					// eventBus.post(new EventDebug("onCoinsReceived Found OP_RETURN:" + Utils.HEX.encode(txout.getScriptPubKey().getChunks().get(1).data)));
					txev.setOpReturnData(txout.getScriptPubKey().getChunks().get(1).data);
				}
			}
			eventBus.post(txev);
			updateNonPersistBalance();
		}
	};

	private org.bitcoinj.core.BlockChainListener bevl = new BlockChainListener() {

		public void reorganize(StoredBlock splitPoint,
				List<StoredBlock> oldBlocks, List<StoredBlock> newBlocks)
				throws VerificationException {

		}

		public void receiveFromBlock(Transaction tx, StoredBlock block,
				NewBlockType blockType, int relativityOffset)
				throws VerificationException {
			
		}

		public boolean notifyTransactionIsInBlock(Sha256Hash txHash,
				StoredBlock block, NewBlockType blockType, int relativityOffset)
				throws VerificationException {
			return false;
		}

		public void notifyNewBestBlock(StoredBlock block)
				throws VerificationException {
			eventBus.post(new EventNewBestBlock(block));
		}

		public boolean isTransactionRelevant(Transaction tx)
				throws ScriptException {
			return false;
		}
	};

	private PeerEventListener pevl = new AbstractPeerEventListener() {
		private Set<String> bset = Sets.newHashSet();

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

		@Override
		public void onTransaction(Peer peer, Transaction t) {
			String tid = t.getHashAsString();
			eventBus.post(new EventInvTx(tid));
		}

	};

	private AppBaseSrv apsrv;

	private EventBus eventBus;

	@Inject
	public WalletWatcherImpl(AppBaseSrv apsrv) {
		this.apsrv = apsrv;
		this.eventBus = apsrv.getEventBus();
		params = MainNetParams.get();
		eventBus.register(this);
	}

	public static DeterministicSeed buildRandomSeedAndBackup(
			NetworkParameters params) {
		Wallet wallet = new Wallet(params);
		DeterministicSeed dseed = wallet.getKeyChainSeed();
		System.out.println("mnemonicCode: "
				+ Joiner.on(" ").join(dseed.getMnemonicCode()));
		System.out.println("creation time: " + dseed.getCreationTimeSeconds());
		return dseed;
	}

	public void startKitFromSeed(String mCodes, long creationTime, String dir,
			String name) throws UnreadableWalletException {
		File vWalletFile = new File(dir, name + ".wallet");
		File chainFile = new File(dir, name + ".spvchain");
		WalletAppKit kit = null;

		if (!vWalletFile.exists()) {
			// redownload chain file to restore from seed.
			if (chainFile.exists()) {
				chainFile.delete();
			}

			String passphrase = "";
			DeterministicSeed seed = new DeterministicSeed(mCodes, null,
					passphrase, creationTime);
			kit = new WalletAppKit(params, new File(dir), name);
			kit.restoreWalletFromSeed(seed);
		} else {
			kit = new WalletAppKit(params, new File(dir), name);
		}

		kit.setBlockingStartup(true);
		kit.startAsync();
		kit.awaitRunning();
		kit.chain().addListener(bevl);

		this.wallet = checkNotNull(kit.wallet());
		String pubAddr = wallet.currentReceiveAddress().toString();
		this.eventBus.post(new EventWalletAddr(pubAddr));
		wallet.addEventListener(wevl);

		System.out.println("Balance=" + wallet.getBalance().getValue());
		System.out.println("Balance(Estimated)="
				+ wallet.getBalance(BalanceType.ESTIMATED));
		this.updateNonPersistBalance();
	}
	
	private void updateNonPersistBalance(){		
		apsrv.getNonPersistConf().setWalletBalance(wallet.getBalance().getValue());
	}

	private void sendOpReturnMessage(byte[] cargo, String toAddrStr, Coin value) {
		try {
			Address toAddr = new Address(params, toAddrStr);
			Transaction tx = new Transaction(params);
			Script script = new ScriptBuilder().op(ScriptOpCodes.OP_RETURN)
					.data(cargo).build();
			tx.addOutput(value, toAddr);
			tx.addOutput(Coin.ZERO, script);
			SendRequest request = Wallet.SendRequest.forTx(tx);
			// request.fee = Coin.valueOf(1059);
			// Debug completeTx only
			// wallet.completeTx(request);
			//
			SendResult r = wallet.sendCoins(request);
			log.info("coins sent. transaction hash: " + r.tx.getHashAsString());
		} catch (InsufficientMoneyException e) {
			e.printStackTrace();
		} catch (AddressFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Subscribe
	public void listenEvSendCoin(EventSendCoin ev) {
		log.info("SendEvent : " + ev.toString());

		EventSendCoin x = checkNotNull(ev);
		
		String tagMsg = x.getTagMsg();
		
		if(tagMsg == null){
			tagMsg = "BK" + wallet.getLastBlockSeenHeight() + "@590c.org";
		}

		Tag tag = Tag.newBuilder().setTagInt32(ev.getTagInt32())
				.setTagString(x.getTagMsg()).build();
		OpReturn opr = ProtoOpReturn.OpReturn.newBuilder().setType(Type.TAG)
				.setTag(tag).build();

		sendOpReturnMessage(YUtils.wrap590c(opr.toByteArray()), x.getAddress(),
				Coin.valueOf(x.getValue()));
	}

	@Subscribe
	public void listenEvDebug(EventDebug ev) {
		System.out.println("[Debug in Wallet] Wallet Balance = " + wallet.getBalance());
	}

	public void start() throws UnreadableWalletException {
		Config conf = this.apsrv.getConf();
		String mCodes = conf.getString("wallet_mcodes");
		long creationTime = conf.getLong("wallet_ctime");
		String wFilename = conf.getString("wallet_filename");
		String wFiledir = conf.getString("wallet_filedir");
		startKitFromSeed(mCodes, creationTime, wFiledir, wFilename);
	}

}
