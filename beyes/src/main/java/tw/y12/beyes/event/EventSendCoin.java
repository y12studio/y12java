package tw.y12.beyes.event;

public class EventSendCoin {
	
	private String tagMsg;
	private String address;
	private long value;
	private int tagInt32;
	
	
	public EventSendCoin( String addr, long value, String tagMsg, int tagInt32) {
		super();
		this.setAddress(addr);
		this.setTagMsg(tagMsg);
		this.setValue(value);
		this.setTagInt32(tagInt32);
	}


	public String getTagMsg() {
		return tagMsg;
	}


	public void setTagMsg(String tagMsg) {
		this.tagMsg = tagMsg;
	}


	public long getValue() {
		return value;
	}


	public void setValue(long value) {
		this.value = value;
	}


	public int getTagInt32() {
		return tagInt32;
	}


	public void setTagInt32(int tagInt32) {
		this.tagInt32 = tagInt32;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventSendCoin ["
				+ (tagMsg != null ? "tagMsg=" + tagMsg + ", " : "")
				+ (address != null ? "address=" + address + ", " : "")
				+ "value=" + value + ", tagInt32=" + tagInt32 + "]";
	}
	
	

}
