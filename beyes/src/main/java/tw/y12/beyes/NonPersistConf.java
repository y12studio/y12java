package tw.y12.beyes;

public class NonPersistConf {
	
	private long walletBalance = 0;

	private boolean autoTest1 = false;

	public boolean isAutoTest1() {
		return autoTest1;
	}

	public void setAutoTest1(boolean autoTest1) {
		this.autoTest1 = autoTest1;
	}



	public long getWalletBalance() {
		return walletBalance;
	}

	public void setWalletBalance(long walletBalance) {
		this.walletBalance = walletBalance;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NonPersistConf [walletBalance=" + walletBalance
				+ ", autoTest1=" + autoTest1 + "]";
	}

}
