package tw.y12.beyes;

import org.bitcoinj.store.UnreadableWalletException;

public interface WalletWatcher {
	
	public void start() throws UnreadableWalletException;

}
