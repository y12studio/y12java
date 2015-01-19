package org590c;

import java.io.File;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.core.Wallet.SendRequest;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;

public class WalletTest {

	public static void main(String[] args) throws AddressFormatException {
		// TODO Auto-generated method stub
		NetworkParameters params = MainNetParams.get();

		WalletAppKit kit = new WalletAppKit(params, new File("build"),
				"testonly");

		String wif = "xxxxxx";
		String toAddr = "yyyyyy";
		long value = 19999;

		DumpedPrivateKey dprivkey = new DumpedPrivateKey(params, wif);

		kit.setBlockingStartup(true);
		kit.startAsync();
		kit.awaitRunning();
		kit.wallet().importKey(dprivkey.getKey());

		System.out.println("Balance=" + kit.wallet().getBalance().getValue());

		SendRequest request = Wallet.SendRequest.to(
				new Address(params, toAddr), Coin.valueOf(value));
		try {
			kit.wallet().sendCoins(request);
		} catch (InsufficientMoneyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
