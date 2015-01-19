package tw.y12.beyes.event;

import org.bitcoinj.core.StoredBlock;

public class EventStoreBlock {
	
	private StoredBlock block;
	
	

	public EventStoreBlock(StoredBlock block) {
		super();
		this.block = block;
	}

	public StoredBlock getBlock() {
		return block;
	}

	public void setBlock(StoredBlock block) {
		this.block = block;
	}

}
