package net.thedanpage.game.world.map.block;

public class BlockFactory {
	
	public static Block createBlock(int x, int y, String blockName) {
		if ((boolean) Blocks.getProperty(blockName, "isFluid") == true) {
			return new FluidBlock(x,y,blockName,FluidBlock.SOURCE_DIR_ORIGIN);
		}
		else {
			return new Block(x,y,blockName);
		}
	}

}
