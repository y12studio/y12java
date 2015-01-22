package tw.y12.beyes.event;

import org.bitcoinj.core.StoredBlock;

public class EventNewBestBlock {
	
	private StoredBlock block;
	
	

	public EventNewBestBlock(StoredBlock block) {
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
