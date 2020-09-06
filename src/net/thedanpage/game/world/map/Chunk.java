package net.thedanpage.game.world.map;

import net.thedanpage.game.Game;
import net.thedanpage.game.world.map.block.Block;

/**
 * A rectangle of blocks, with a width of {@link #CHUNK_WIDTH} and a height of
 * {@link Map#MAP_HEIGHT}. The map is split up into an array of chunks which are
 * procedurally generated as they come onto the screen, and only updated when
 * they are on the screen. This is a much more efficient way to do updates and
 * rending rather than interacting with every block every game update.
 * 
 * @author Dan
 */
public class Chunk {

	/** The width of the chunk, in blocks */
	public static final int CHUNK_WIDTH = 8;

	/** A 2D array containing every block in the chunk */
	private Block[][] blocks = new Block[CHUNK_WIDTH][Map.MAP_HEIGHT];

	/** The leftmost X coordinate of the chunk */
	private int x;

	/** Whether the chunk's blocks have been generated or not */
	private boolean generated = false;

	public Chunk(int x0) {
		this.x = x0;
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
	public Block getBlock(int worldX, int worldY) {
		return blocks[worldX % CHUNK_WIDTH][worldY];
	}

	/** Updates all blocks in the chunk */
	public void update() {
		for (Block[] i : blocks) {
			for (Block block : i) {
				if (block != null)
					block.update();
			}
		}
	}

	/** Renders all blocks in the chunk */
	public void render() {
		int minRenderHeight = (Game.screen.getScreenOffsetY()) / Block.BLOCK_SIZE;
		int maxRenderHeight = (Game.screen.getScreenOffsetY() + Game.screen.getHeight()) / Block.BLOCK_SIZE + 1;

		for (Block[] column : blocks) {
			for (int y = minRenderHeight; y < maxRenderHeight; y++) {
				if (y >= 0 && y < column.length && column[y] != null)
					column[y].draw();
			}
		}
	}

}
