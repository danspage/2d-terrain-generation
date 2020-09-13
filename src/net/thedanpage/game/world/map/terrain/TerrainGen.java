package net.thedanpage.game.world.map.terrain;

import java.util.Random;

import net.thedanpage.game.world.map.Chunk;
import net.thedanpage.game.world.map.Map;
import net.thedanpage.game.world.map.block.Block;
import net.thedanpage.game.world.map.block.BlockFactory;

public class TerrainGen {
	
	private static Random random = new Random();
	
	public static Block[][] generateChunk(int x0, int seed) {
		
		Block[][] blocks = new Block[Chunk.CHUNK_WIDTH][Map.MAP_HEIGHT];
		
		// Terrain gen
		
		for (int x=0; x<Chunk.CHUNK_WIDTH; x++) {
			// Calculate surface height using simplex noise. This is the basis of the terrain generation.
			int surfaceHeight = (int) ((SimplexNoise.noise((x0+x)/32.0, seed)+3)*10);
			
			int counter = 0;
			for (int y=surfaceHeight; y>=0; y--) {
				
				// Grass
				if (counter == 0) blocks[x][y] = BlockFactory.createBlock(x0+x,y,"grass");
				
				// Dirt
				else if (counter < (surfaceHeight-counter)/20 + 8) blocks[x][y] = BlockFactory.createBlock(x0+x,y,"dirt");
				
				// Stone and minerals
				else {
					if (random.nextInt(100) == 1) blocks[x][y] = BlockFactory.createBlock(x0+x,y,"coal_ore");
					else if (random.nextInt(200) == 1) blocks[x][y] = BlockFactory.createBlock(x0+x,y,"iron_ore");
					else if (y <= 5 && random.nextInt(1000) == 1) blocks[x][y] = BlockFactory.createBlock(x0+x,y,"diamond_ore");
					else blocks[x][y] = BlockFactory.createBlock(x0+x,y,"stone");
				}
				
				counter ++;
			}
			
			for (int y=24; y>=0; y--) {
				if (blocks[x][y] == null) blocks[x][y] = BlockFactory.createBlock(x0+x,y,"water");
			}
		}
		
		// End terrain gen
		
		return blocks;
		
	}

}
