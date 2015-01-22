package tw.y12.beyes;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Bytes;

public class YUtils {

	public static final byte B59 = (byte) 0x59;
	public static final byte B0c = (byte) 0x0c;
	public static final byte[] Prefix590c = { B59, B0c };

	public static final BaseEncoding HEX = BaseEncoding.base16().lowerCase();

	public static final byte[] wrap590c(byte[] target) {
		return Bytes.concat(Prefix590c, checkNotNull(target));
	}

	public static final byte[] unwrap590c(byte[] target) {
		checkArgument(target != null && target.length > 2 && target[0] == B59
				&& target[1] == B0c);
		return Arrays.copyOfRange(target, 2, target.length);
	}

	/**
	 * @param params
	 * @param address
	 * @param bitidUri
	 * @param sigBase64
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyBitIdUri(NetworkParameters params,
			String address, String bitidUri, String sigBase64) throws Exception {
		ECKey key = ECKey.signedMessageToKey(bitidUri, sigBase64);
		Address gotAddress = key.toAddress(params);
		Address expectedAddress = new Address(params, address);
		return expectedAddress.equals(gotAddress);
	}

}
