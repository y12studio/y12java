package tw.y12.beyes;

import org.bitcoinj.core.Message;

public class EventMessage {
	
	private final Message message;
	
	public EventMessage(Message m){
		this.message = m;
	}

	public Message getMessage() {
		return message;
	}

}
