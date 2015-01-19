package tw.y12.beyes.event;

public class EventInvTx {
	
	private final String hash;
	
	public EventInvTx(String hash){
		this.hash = hash;
	}

	public String getHash() {
		return hash;
	}
}
