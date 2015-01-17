package tw.y12.beyes;

import java.util.Map;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Message;
import org.bitcoinj.core.StoredBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class WalletFirebaseListener {

	protected static final Logger log = LoggerFactory
			.getLogger(WalletFirebaseListener.class);

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

	public WalletFirebaseListener(String fbToken, String rootUrl) {

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
	public void listenWalletAddr(EventWalletAddr ev) {
		String addr = ev.getPubAddr();
		walletRef.child("address").setValue(addr);
	}

	@Subscribe
	public void listenStoreBlock(EventStoreBlock ev) {
		StoredBlock b = ev.getBlock();
		try {
			log.info("EvBus Store Block :" + b.getHeight());
			Map<String, Object> r = Maps.newHashMap();
			r.put("epoch", b.getHeader().getTimeSeconds());
			r.put("height", b.getHeight());
			r.put("date", b.getHeader().getTime().toGMTString());
			Firebase blockRef = blocksRef
					.child(b.getHeader().getHashAsString());
			blockRef.setValue(r);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
