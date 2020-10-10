package net.thedanpage.game.world.map.block;

import net.thedanpage.game.Game;
import net.thedanpage.game.world.map.Map;

public class FluidBlock extends Block {
	
	private static final long serialVersionUID = -2119516562195847271L;

	private static final int SPREAD_TIME = 5;
	
	static final int
	SOURCE_DIR_LEFT=0,
	SOURCE_DIR_RIGHT=1,
	SOURCE_DIR_UP=2,
	SOURCE_DIR_DOWN=3,
	SOURCE_DIR_ORIGIN=4;
	
	private int sourceDir;

	FluidBlock(int x, int y, String blockName, int sourceDir) {
		super(x, y, blockName);
		
		this.sourceDir = sourceDir;
	}
	
	@Override
	public void update(Map map) {
		super.update(map);
		
		if (Game.getTicks() % SPREAD_TIME == 0) {
			
			if (map.getBlock(this.getX(), this.getY()-1) == null) {
				map.setBlock(new FluidBlock(this.getX(), this.getY()-1, this.getBlockType(), SOURCE_DIR_UP), this.getX(), this.getY()-1);
			}
			
			
			if (this.sourceDir == SOURCE_DIR_UP && (map.getBlock(this.getX(), this.getY()+1) == null || map.getBlock(this.getX(), this.getY()+1).getBlockType() != "water")) {
				map.setBlock(null, this.getX(), this.getY());
			}
			
		}
	}

}
