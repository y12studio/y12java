package tw.y12.beyes.bak;

import java.util.Map;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Message;
import org.bitcoinj.core.StoredBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.y12.beyes.event.EventGetBlock;
import tw.y12.beyes.event.EventInvBlock;
import tw.y12.beyes.event.EventInvTx;
import tw.y12.beyes.event.EventMessage;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Firebase.AuthResultHandler;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.eventbus.Subscribe;

@Deprecated
public class PeerFirebaseListener {

	protected static final Logger log = LoggerFactory
			.getLogger(PeerFirebaseListener.class);

	private Multiset<String> mset = HashMultiset.create();

	private boolean _debug = false;

	private Firebase fbRef;

	private ValueEventListener vel = new ValueEventListener() {
		public void onDataChange(DataSnapshot snapshot) {
			System.out.println("Data Snapshot : " + snapshot.getValue());
			// setData(snapshot);
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

	private ChildEventListener blocksCev = new ChildEventListener() {

		public void onCancelled(FirebaseError arg0) {
			// TODO Auto-generated method stub
		}

		public void onChildAdded(DataSnapshot snapshot, String prevkey) {
			Firebase last = summaryRef.child("lastest");
			last.child("hash").setValue(snapshot.getKey());
			last.child("value").setValue(snapshot.getValue());
		}

		public void onChildChanged(DataSnapshot arg0, String arg1) {
			// TODO Auto-generated method stub

		}

		public void onChildMoved(DataSnapshot arg0, String arg1) {
			// TODO Auto-generated method stub

		}

		public void onChildRemoved(DataSnapshot arg0) {
			// TODO Auto-generated method stub

		}

	};

	private ValueEventListener blocksVel = new ValueEventListener() {
		public void onDataChange(DataSnapshot snapshot) {
			summaryRef.child("count").setValue(snapshot.getChildrenCount());
		}

		public void onCancelled(FirebaseError error) {
			System.out.println(error.getMessage());
		}
	};

	private Firebase blocksRef;

	private Firebase summaryRef;

	private Firebase walletRef;

	public PeerFirebaseListener(String fbToken, String rootUrl) {
		// curl https://crackling-heat-6124.firebaseio.com/beyes/t150115.json
		// curl
		// https://crackling-heat-6124.firebaseio.com/beyes/t150116/blocks.json
		// curl
		// https://crackling-heat-6124.firebaseio.com/beyes/t150116/summary.json

		fbRef = new Firebase(rootUrl);
		fbRef.authWithCustomToken(fbToken, handler);

		// fbRef.addValueEventListener(vel);

		blocksRef = fbRef.child("blocks");
		summaryRef = fbRef.child("summary");
		walletRef = fbRef.child("wallet");

		blocksRef.addChildEventListener(blocksCev);
		blocksRef.addValueEventListener(blocksVel);
	}

	@Subscribe
	public void listenInvBlock(EventInvBlock ev) {
		String hash = ev.getHash();
		log.info("EvBus inv blcok :" + hash);
	}

	@Subscribe
	public void listenInvTx(EventInvTx ev) {
		String hash = ev.getHash();

		System.out.println("EvBus inv tx :" + hash);

		int tcount = mset.count("org.bitcoinj.core.Transaction");
		if (tcount == 1 || tcount % 20 == 0) {
			log.info(mset.toString());
		}
	}

	@Subscribe
	public void listenGetBlock(EventGetBlock ev) {
		Block b = ev.getBlock();
		try {
			log.info("EvBus Get Block :" + b.getHashAsString());
			Map<String, Object> r = Maps.newHashMap();
			r.put("epoch", b.getTimeSeconds());
			r.put("txsize", b.getTransactions().size());
			r.put("date", b.getTime().toGMTString());
			Firebase blockRef = blocksRef.child(b.getHashAsString());
			blockRef.setValue(r);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	@Subscribe
	public void listenMessage(EventMessage ev) {
		Message m = ev.getMessage();
		String cname = m.getClass().getCanonicalName();
		mset.add(cname);
		System.out.println("EvBus Message :" + cname);
	}

}
