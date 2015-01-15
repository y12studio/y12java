package tw.y12.beyes;

import org.bitcoinj.core.Block;

public class EventGetBlock {
	
	private final Block block;
	
	public EventGetBlock(Block b){
		this.block = b;
	}

	public Block getBlock() {
		return block;
	}

}
