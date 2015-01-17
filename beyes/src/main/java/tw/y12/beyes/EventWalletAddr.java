package tw.y12.beyes;

public class EventWalletAddr {
	
	private String pubAddr;

	public EventWalletAddr(String pubAddr) {
		this.pubAddr = pubAddr;
	}

	/**
	 * @return the pubAddr
	 */
	public String getPubAddr() {
		return pubAddr;
	}

	/**
	 * @param pubAddr the pubAddr to set
	 */
	public void setPubAddr(String pubAddr) {
		this.pubAddr = pubAddr;
	}

}
