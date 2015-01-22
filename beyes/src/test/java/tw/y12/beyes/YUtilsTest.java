package tw.y12.beyes;

import static org.junit.Assert.*;

import java.security.SignatureException;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet2Params;
import org.bitcoinj.params.TestNet3Params;
import org.junit.Test;

import com.google.common.primitives.Chars;

public class YUtilsTest {

	@Test
	public void verifyBitidUri() throws Exception {
		// https://github.com/bitid/bitid-ruby/blob/master/test/test_bitid.rb
		String bitidUri = "bitid://bitid.bitcoin.blue/callback?x=3893a2a881dd4a1e&u=1";
		String sigBase64 = "ID5heI0WOeWoryGhZHaxoOH5vkmmcwDsfc4nDQ5vPcXSWh2jyETDGkSNO5zk4nbESGD6k0tgFxYA3HzlEGOf5Uc=";
		String expectedAddress = "mpsaRD2ugdCY1iFrQdsDYRT4qeZzCnvGHW";

		boolean isValid = YUtils.verifyBitIdUri(TestNet3Params.get(),
				expectedAddress, bitidUri, sigBase64);
		assertTrue(isValid);
	}

	@Test
	public void signBitidMessage() throws Exception {
		// https://github.com/bitid/bitid
		String bitidUri = "bitid://590c.org/callback?x=b679ebdbf1d7b8ff&u=1";
		ECKey key = new ECKey();
		String signatureBase64 = key.signMessage(bitidUri);
		System.out.println("Message signed with "
				+ key.toAddress(MainNetParams.get()) + ": " + signatureBase64);
		// Should verify correctly.
		key.verifyMessage(bitidUri, signatureBase64);
		try {
			key.verifyMessage("Evil attacker says hello!", signatureBase64);
			fail();
		} catch (SignatureException e) {
			// OK.
		}
	}

}
