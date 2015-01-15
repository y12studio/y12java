package tw.y12.beyes;

import java.util.Map;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Message;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Firebase.AuthResultHandler;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.eventbus.Subscribe;

public class BlockChainListener {

	private Multiset<String> mset = HashMultiset.create();

	private boolean _debug = false;

	private Firebase fbRef;

	private ValueEventListener vel = new ValueEventListener() {
		public void onDataChange(DataSnapshot snapshot) {
			System.out.println("Data Snapshot : " + snapshot.getValue());

		}

		public void onCancelled(FirebaseError error) {
			System.out.println(error.getMessage());
		}
	};

	private AuthResultHandler handler = new AuthResultHandler() {

		public void onAuthenticationError(FirebaseError error) {
			System.out.println(error);
		}

		public void onAuthenticated(AuthData authData) {
			System.out.println("Authenticated:" + authData.getProvider());
		}
	};

	public BlockChainListener(String fbToken) {
		// curl https://crackling-heat-6124.firebaseio.com/beyes/t150115.json
		
		fbRef = new Firebase(
				"https://crackling-heat-6124.firebaseio.com/beyes/t150115");
		fbRef.addValueEventListener(vel);
		fbRef.authWithCustomToken(fbToken, handler);
	}

	@Subscribe
	public void listenInvBlock(EventInvBlock ev) {
		String hash = ev.getHash();
		System.out.println("EvBus get inv blcok :" + hash);
	}

	@Subscribe
	public void listenInvTx(EventInvTx ev) {
		String hash = ev.getHash();
		if (_debug) {
			System.out.println("EvBus get inv tx :" + hash);
		}

		int tcount = mset.count("org.bitcoinj.core.Transaction");
		if (tcount == 1 || tcount % 20 == 0) {
			System.out.println(mset);
		}
	}

	@Subscribe
	public void listenGetBlock(EventGetBlock ev) {
		Block b = ev.getBlock();
		System.out.println("EvBus get block :" + b.getHashAsString());
		Map<String, Object> r = Maps.newHashMap();
		r.put("epoch", b.getTimeSeconds());
		r.put("txsize", b.getTransactions().size());
		Firebase blockRef = fbRef.child(b.getHashAsString());
		blockRef.setValue(r);
	}

	@Subscribe
	public void listenMessage(EventMessage ev) {
		Message m = ev.getMessage();
		mset.add(m.getClass().getCanonicalName());
	}

}
