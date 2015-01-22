package tw.y12.beyes;

import tw.y12.beyes.event.EventDebug;
import tw.y12.beyes.event.EventTxCoinsReceived;

import com.google.common.eventbus.Subscribe;

public class DebugListener {

	@Subscribe
	public void listenEvDebug(EventDebug ev) {
		System.out.println("Debug in " + this.getClass().getName()
				+ " with msg= " + ev.getMessage());
	}

	@Subscribe
	public void listenEvNewTx(EventTxCoinsReceived ev) {
		System.out.println("Debug in " + ev.toString());
	}

}
