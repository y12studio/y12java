package tw.y12.beyes.event;

import com.google.protobuf.InvalidProtocolBufferException;

import org590c.ProtoOpReturn;
import org590c.ProtoOpReturn.OpReturn;
import tw.y12.beyes.Utils;

public class EventTxCoinsReceived {
	
	private String hash;
	private String detail;
	private byte[] opReturnData;

	/**
	 * @param hash
	 * @param detail
	 */
	public EventTxCoinsReceived(String hash, String detail) {
		this.hash = hash;
		this.detail = detail;
	}
	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}
	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	
	public byte[] getOpReturnData() {
		return opReturnData;
	}
	
	public void setOpReturnData(byte[] opReturnData) {
		this.opReturnData = opReturnData;
	}
	
	public OpReturn getProtocolBuffer(){
		OpReturn r = null;
		try {
			r = ProtoOpReturn.OpReturn.parseFrom(Utils.unwrap590c(this.opReturnData));
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return r;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventTxCoinsReceived ["
				+ (hash != null ? "hash=" + hash + ", " : "")
				+ (detail != null ? "detail=" + detail + ", " : "")
				+ (opReturnData != null ? "opReturn=" + Utils.HEX.encode(opReturnData)
						: "") + "]";
	}

}
