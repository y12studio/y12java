package tw.y12.beyes;

import org.bitcoinj.store.UnreadableWalletException;

import com.google.inject.Inject;

public class SrvCtrlImpl implements SrvCtrl {
	
	private final WalletWatcher walletWatcher;

	@Inject
	public SrvCtrlImpl(WalletWatcher ww){
		this.walletWatcher = ww;
		try {
			ww.start();
		} catch (UnreadableWalletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void status() {
		// TODO Auto-generated method stub

	}

	public WalletWatcher getWalletWatcher() {
		return walletWatcher;
	}

}
