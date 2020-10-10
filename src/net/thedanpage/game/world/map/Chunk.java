package net.thedanpage.game.world.map;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import net.thedanpage.game.Game;
import net.thedanpage.game.graphics.Graphics;
import net.thedanpage.game.world.map.block.Block;
import net.thedanpage.game.world.map.block.Blocks;
import net.thedanpage.game.world.map.terrain.TerrainGen;

/**
 * A rectangle of blocks, with a width of {@link #CHUNK_WIDTH} and a height of
 * {@link Map#MAP_HEIGHT}. The map is split up into an array of chunks which are
 * procedurally generated as they come onto the screen, and only updated when
 * they are on the screen. This is a much more efficient way to do updates and
 * rending rather than interacting with every block every game update.
 * 
 * @author Dan
 */
public class Chunk implements Serializable {

	private static final long serialVersionUID = 6482501415662803473L;

	/** The width of the chunk, in blocks */
	public static final int CHUNK_WIDTH = 8;

	/** A 2D array containing every block in the chunk */
	private Block[][] blocks = new Block[CHUNK_WIDTH][Map.MAP_HEIGHT];

	/** A 2D array containing light levels for the chunk */
	private float[][] lightLevels = new float[CHUNK_WIDTH][Map.MAP_HEIGHT];

	/** The leftmost X coordinate of the chunk */
	private int x;

	/** Whether the chunk's blocks have been generated or not */
	private boolean generated = false;

	public Chunk(int x0) {
		this.x = x0;

		for (int x = 0; x < lightLevels.length; x++) {
			for (int y = 0; y < lightLevels[0].length; y++) {
				lightLevels[x][y] = 0;
			}
		}
	}

	public int getX() {
		return this.x;
	}

	public Block[][] getBlocks() {
		return this.blocks;
	}

	/** Generate the terrain */
	public void generate(int seed) {
		this.blocks = TerrainGen.generateChunk(this.x, seed);
		this.generated = true;
	}

	public boolean isGenerated() {
		return this.generated;
	}

	/**
	 * Returns a block at a given coordinate in the world, without having to go
	 * through chunks
	 */
	public Block getBlock(int x, int y) {
		return blocks[x % CHUNK_WIDTH][y];
	}

	/** Sets the block at a specified coordinate */
	public void setBlock(Block block, int x, int y) {
		blocks[x % CHUNK_WIDTH][y] = block;
	}

	/** Updates all blocks in the chunk */
	public void update(Map map) {
		for (Block[] i : blocks) {
			for (Block block : i) {
				if (block != null)
					block.update(map);
			}
		}

		updateLighting(map);
	}

	/** Renders all blocks in the chunk */
	public void render(Map map) {
		int minRenderHeight = (Game.screen.getScreenOffsetY()) / Block.BLOCK_SIZE;
		int maxRenderHeight = (Game.screen.getScreenOffsetY() + Game.screen.getHeight()) / Block.BLOCK_SIZE + 1;

		for (int x = 0; x < blocks.length; x++) {
			for (int y = minRenderHeight; y < maxRenderHeight; y++) {
				if (y >= 0 && y < blocks[x].length && blocks[x][y] != null)
					blocks[x][y].draw(map);
				if (blocks[x][y] != null && blocks[x][y].isHighlighted()) {
					Graphics.highlightRectangleBlockCoords(new Rectangle2D.Double(this.x + x, y, 1, 1));
				}
			}
		}
	}

	public float getLightLevel(int blockX, int blockY) {
		return lightLevels[blockX % CHUNK_WIDTH][blockY];
	}

	private void updateLighting(Map map, int xInChunk, int yInChunk) {
		// If the block is a light source, set it to max brightness and return
		if (map.getBlock(x + xInChunk, yInChunk) != null && map.getBlock(x + xInChunk, yInChunk).isLightSource()) {
			lightLevels[xInChunk][yInChunk] = 1;
			return;
		}

		float oldLight = lightLevels[xInChunk][yInChunk];

		// Set light level to 1 if there are no blocks above
		skyCheck: for (int checkY = yInChunk + 1; checkY < Map.MAP_HEIGHT; checkY++) {
			if (map.getBlock(x + xInChunk, checkY) != null) {
				break skyCheck;
			}
			if (checkY == Map.MAP_HEIGHT - 1) {
				lightLevels[xInChunk][yInChunk] = 1;
				return;
			}
		}

		// If there are blocks above:

		if (x + xInChunk - 1 >= 0)
			lightLevels[xInChunk][yInChunk] = Math.max(map.getLightLevel(x + xInChunk - 1, yInChunk),
					lightLevels[xInChunk][yInChunk]);
		if (x + xInChunk + 1 < Map.MAP_SIZE_CHUNKS * CHUNK_WIDTH)
			lightLevels[xInChunk][yInChunk] = Math.max(map.getLightLevel(x + xInChunk + 1, yInChunk),
					lightLevels[xInChunk][yInChunk]);
		if (yInChunk - 1 >= 0)
			lightLevels[xInChunk][yInChunk] = Math.max(map.getLightLevel(x + xInChunk, yInChunk - 1),
					lightLevels[xInChunk][yInChunk]);
		if (yInChunk + 1 < Map.MAP_HEIGHT)
			lightLevels[xInChunk][yInChunk] = Math.max(map.getLightLevel(x + xInChunk, yInChunk + 1),
					lightLevels[xInChunk][yInChunk]);

		if (map.getBlock(x + xInChunk, yInChunk) != null)
			lightLevels[xInChunk][yInChunk] -= map.getBlock(x + xInChunk, yInChunk).getSubtractLighting();
		else
			lightLevels[xInChunk][yInChunk] -= Blocks.blockTranslucencies.get("air");

		if (lightLevels[xInChunk][yInChunk] < 0)
			lightLevels[xInChunk][yInChunk] = 0;
		if (lightLevels[xInChunk][yInChunk] > 1)
			lightLevels[xInChunk][yInChunk] = 1;

		// If the light level changed, update adjacent blocks
		if (lightLevels[xInChunk][yInChunk] != oldLight) {
			if (x + xInChunk - 1 >= 0)
				map.getChunkAtBlock(x + xInChunk + 1).updateLighting(map, (x + xInChunk - 1) % CHUNK_WIDTH, yInChunk);
			if (x + xInChunk + 1 < Map.MAP_SIZE_CHUNKS * CHUNK_WIDTH)
				map.getChunkAtBlock(x + xInChunk + 1).updateLighting(map, (x + xInChunk + 1) % CHUNK_WIDTH, yInChunk);
			if (yInChunk - 1 >= 0)
				map.getChunkAtBlock(x + xInChunk).updateLighting(map, (x + xInChunk) % CHUNK_WIDTH, yInChunk - 1);
			if (yInChunk + 1 < Map.MAP_HEIGHT)
				map.getChunkAtBlock(x + xInChunk).updateLighting(map, (x + xInChunk) % CHUNK_WIDTH, yInChunk + 1);
		}
	}

	private void updateLighting(Map map) {
		for (int blockX = 0; blockX < CHUNK_WIDTH; blockX++) {
			for (int blockY = 0; blockY < Map.MAP_HEIGHT; blockY++) {

				updateLighting(map, blockX, blockY);

			}
		}
	}

}
