package tw.y12.beyes;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Bytes;

public class Utils {
	
	public static final byte B59 = (byte) 0x59;
	public static final byte B0c = (byte) 0x0c;
	public static final byte[] Prefix590c = { B59, B0c };
	
	public static final BaseEncoding HEX = BaseEncoding.base16().lowerCase();
	
	public static final byte[] wrap590c(byte[] target){
		return  Bytes.concat(Prefix590c, checkNotNull(target));
	}
	
	public static final byte[] unwrap590c(byte[] target){
		checkArgument(target !=null && target.length>2 && target[0]==B59 && target[1] == B0c);
		return Arrays.copyOfRange(target, 2, target.length);
	}

}
