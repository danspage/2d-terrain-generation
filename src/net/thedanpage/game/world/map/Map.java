package net.thedanpage.game.world.map;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.thedanpage.game.Game;
import net.thedanpage.game.GameEngine;
import net.thedanpage.game.framework.Util;
import net.thedanpage.game.graphics.Font;
import net.thedanpage.game.graphics.Fonts;
import net.thedanpage.game.graphics.Graphics;
import net.thedanpage.game.world.entity.Entity;
import net.thedanpage.game.world.entity.Player;
import net.thedanpage.game.world.map.block.Block;
import net.thedanpage.game.world.map.block.BlockFactory;
import net.thedanpage.game.world.map.block.Blocks;

public class Map implements Serializable {
	
	// Required for Serializable
	private static final long serialVersionUID = -5536697908734438630L;

	/**
	 * The maximum height of the map, in blocks
	 */
	public static final int MAP_HEIGHT = 256;

	/**
	 * The maximum number of chunks the map will create
	 */
	public static final int MAP_SIZE_CHUNKS = 200;
	
	/** Used for generating seeds */
	private static final Random random = new Random();

	/**
	 * An ArrayList containing all entities in the world
	 */
	private List<List<Entity>> entities = new ArrayList<List<Entity>>();

	/**
	 * An ArrayList containing all chunks in the world
	 */
	private List<Chunk> chunks = new ArrayList<Chunk>();

	/**
	 * The main player entity
	 */
	private Player player;

	/**
	 * A random number that the world will generate terrain based off of
	 */
	private int seed;

	private int numGeneratedChunks = 0;

	private int chunkRenderMin, chunkRenderMax;

	/** Controls whether the player's hitbox is showing or not */
	private boolean showHitboxes = false;

	/** Used for cycling through which blocks will be placed */
	public int currentPlaceBlockIndex = 0;

	/**
	 * Returns a {@link Chunk} object based on its index in {@link #chunks}
	 * 
	 * @param index
	 * @return
	 */
	public Chunk getChunkAtIndex(int index) {
		return chunks.get(index);
	}

	/**
	 * Returns the chunk that a certain X coordinate is inside of
	 * 
	 * @param blockX an X coordinate
	 * @return the {@link Chunk} object
	 */
	public Chunk getChunkAtBlock(int blockX) {
		try {
			int chunk = blockX / Chunk.CHUNK_WIDTH;
			if (chunk >= 0 && chunk < chunks.size())
				return chunks.get(chunk);
			else
				throw new Exception("The chunk at index " + chunk + " and block x=" + blockX + " does not exist!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Adds an entity to the world
	 * 
	 * @param group  a group that the entity will be put in
	 * @param entity the entity that will be created
	 */
	public void addEntity(int group, Entity entity) {
		entities.get(Entity.ENTITY_GROUP_EVERYTHING).add(entity);

		if (group != Entity.ENTITY_GROUP_EVERYTHING)
			entities.get(group).add(entity);
	}

	/**
	 * Get a list containing all entites that are tagged in a specified group.
	 * 
	 * @param group The ID of the group. Refer to the world.entity.Entity
	 *              "ENTITY_GROUP_..." constants.
	 * @return a List containing entities
	 */
	public List<Entity> getEntityGroup(int group) {
		return entities.get(group);
	}

	/** Returns a random seven-digit number */
	public int generateSeed() {
		// Generate a random 7 digit seed
		int randSeed = random.nextInt(10000000);
		while (randSeed < 1000000)
			randSeed *= 10;
		return randSeed;
	}
	
	public static int generateSeed(String string) {
		// Convert the string to hashcode
		int strSeed = string.hashCode();
		
		// Jumble the numbers
		strSeed = (int) (strSeed * 1591.6932896);
		
		// Trim the number to be 7 digits long
		strSeed = Integer.parseInt(Integer.toString(strSeed).substring(0, 7));
		
		return strSeed;
	}
	
	public void init(int seed) {
		
		while (seed < 1000000)
			seed *= 10;
		
		this.seed = seed;

		// Instantiate the entity groups
		entities.add(Entity.ENTITY_GROUP_EVERYTHING, new ArrayList<Entity>());
		entities.add(Entity.ENTITY_GROUP_PLAYERS, new ArrayList<Entity>());

		// Instantiate the player at the center of the world
		player = new Player();
		player.setPosition(MAP_SIZE_CHUNKS*Chunk.CHUNK_WIDTH/2, 100);
		addEntity(Entity.ENTITY_GROUP_PLAYERS, player);

		// Instantiate the chunks. They are NOT generated upon creation
		for (int i = 0; i < MAP_SIZE_CHUNKS; i++)
			chunks.add(i, new Chunk(i * Chunk.CHUNK_WIDTH));

		// Make sure hitboxes are not showing
		setShowingHitboxes(false);

		currentPlaceBlockIndex = 0;
	}

	public void update() {
		// Find the furthest left and furthest right chunks that are visible on the
		// screen
		chunkRenderMin = (-Game.screen.getScreenOffsetX()) / Block.BLOCK_SIZE / Chunk.CHUNK_WIDTH;
		chunkRenderMax = (-Game.screen.getScreenOffsetX() + Game.screen.getWidth()) / Block.BLOCK_SIZE
				/ Chunk.CHUNK_WIDTH + 3;

		// Update and generate chunks that are visible on the screen
		for (int x = chunkRenderMin; x < chunkRenderMax; x++) {
			if (x >= 0 && x < chunks.size() && chunks.get(x) != null) {
				if (!chunks.get(x).isGenerated())
					chunks.get(x).generate(seed);
				chunks.get(x).update(this);
			}
		}

		// Update all entities
		for (Entity entity : getEntityGroup(Entity.ENTITY_GROUP_EVERYTHING)) {
			entity.update(this);
		}

		// Update the blocks helper class
		Blocks.update();

		// Update the sky
		Sky.update();

		// Highlight the block at the mouse
		try {
			if (getBlockAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y) != null)
				getBlockAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y).setHighlighted(true);
		} catch (NullPointerException e) {
		}
	}

	public void render() {

		// Render the sky
		Sky.render();

		// Set the screen offset based on the player's position
		Game.screen.setScreenOffsetX(-(int) (player.getX() * Block.BLOCK_SIZE - Game.screen.getWidth() / 2));
		Game.screen.setScreenOffsetY((int) (player.getY() * Block.BLOCK_SIZE - Game.screen.getHeight() / 6));
		if (Game.screen.getScreenOffsetY() < 0)
			Game.screen.setScreenOffsetY(0);

		// Find the furthest left and furthest right chunks that are visible on the
		// screen
		chunkRenderMin = (-Game.screen.getScreenOffsetX()) / Block.BLOCK_SIZE / Chunk.CHUNK_WIDTH;
		chunkRenderMax = (-Game.screen.getScreenOffsetX() + Game.screen.getWidth()) / Block.BLOCK_SIZE
				/ Chunk.CHUNK_WIDTH + 1;

		// Render chunks that are visible on the screen
		for (int x = chunkRenderMin; x < chunkRenderMax; x++) {
			if (x >= 0 && x < chunks.size() && chunks.get(x) != null) {
				chunks.get(x).render(this);
			}
		}

		// Render entities
		for (Entity entity : getEntityGroup(Entity.ENTITY_GROUP_EVERYTHING)) {
			try {
				entity.draw(showHitboxes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Show various information on the top left of the screen
		Fonts.drawString("Ups:" + Game.getCurrentUps() + " Fps:" + Game.getCurrentFps(), "tinyfont", 2, 2, 0xffff00,
				Font.ALIGN_LEFT);
		Fonts.drawString("Seed:" + seed, "tinyfont", 2, 8, 0xffff00, Font.ALIGN_LEFT);

		numGeneratedChunks = 0;
		for (int i = 0; i < chunks.size(); i++)
			if (chunks.get(i).isGenerated())
				numGeneratedChunks++;
		Fonts.drawString("Generated chunks:" + numGeneratedChunks, "tinyfont", 2, 14, 0xffff00, Font.ALIGN_LEFT);

		Fonts.drawString("Player X: " + Util.formatDoubleForString(player.getX()), "tinyfont", 2, 29, 0xffff00,
				Font.ALIGN_LEFT);
		Fonts.drawString("Player Y: " + Util.formatDoubleForString(player.getY()), "tinyfont", 2, 35, 0xffff00,
				Font.ALIGN_LEFT);

		Fonts.drawString("Space: Toggle flying", "tinyfont", 2, 48, 0xffff00, Font.ALIGN_LEFT);
		Fonts.drawString("Movement:WASD/Arrows", "tinyfont", 2, 54, 0xffff00, Font.ALIGN_LEFT);
		Fonts.drawString("H: Toggle hitboxes", "tinyfont", 2, 60, 0xffff00, Font.ALIGN_LEFT);
		Fonts.drawString("Esc:Exit to menu", "tinyfont", 2, 66, 0xffff00, Font.ALIGN_LEFT);

		Fonts.drawString("Cycle block: U/I", "tinyfont", 2, 79, 0xffff00, Font.ALIGN_LEFT);

		// Show the selected block on the top left menu
		Fonts.drawString("Current block:", "tinyfont", 2, 88, 0xffff00, Font.ALIGN_LEFT);
		Graphics.drawImage(59, 86, Block.BLOCK_SIZE, Block.BLOCK_SIZE,
				BlockFactory.createBlock(0, 0, Blocks.placeBlockList.get(currentPlaceBlockIndex)).getTexture());

		Fonts.drawString("Left click: Remove", "tinyfont", 2, 95, 0xffff00, Font.ALIGN_LEFT);
		Fonts.drawString("Right click: Place", "tinyfont", 2, 101, 0xffff00, Font.ALIGN_LEFT);
		
		Fonts.drawString("F5: Save world", "tinyfont", 2, 114, 0xffff00, Font.ALIGN_LEFT);
		Fonts.drawString("F6: Load world", "tinyfont", 2, 120, 0xffff00, Font.ALIGN_LEFT);
		Fonts.drawString("F7: New world", "tinyfont", 2, 126, 0xffff00, Font.ALIGN_LEFT);

	}

	public boolean isShowingHitboxes() {
		return showHitboxes;
	}

	public void setShowingHitboxes(boolean showHitboxes) {
		this.showHitboxes = showHitboxes;
	}

	/** Returns the block at a specific position on the screen */
	public Block getBlockAtScreenPos(int screenX, int screenY) {
		// Inverse of x coordinate from Block.draw
		int blockX = (int) ((screenX - Game.screen.getScreenOffsetX()) / Block.BLOCK_SIZE);
		// Inverse of y coordinate from Block.draw
		int blockY = (int) ((-screenY + Game.screen.getHeight() + Game.screen.getScreenOffsetY()) / Block.BLOCK_SIZE);

		if (blockX >= 0 && blockX < MAP_SIZE_CHUNKS * Chunk.CHUNK_WIDTH && blockY >= 0 && blockY < Map.MAP_HEIGHT) {
			return getChunkAtBlock(blockX).getBlock(blockX, blockY);
		}
		return null;
	}

	/** Returns the block at a specific position on the screen */
	public Point getBlockCoordsAtScreenPos(int screenX, int screenY) {
		// Inverse of x coordinate from Block.draw
		int blockX = (int) ((screenX - Game.screen.getScreenOffsetX()) / Block.BLOCK_SIZE);
		// Inverse of y coordinate from Block.draw
		int blockY = (int) ((-screenY + Game.screen.getHeight() + Game.screen.getScreenOffsetY()) / Block.BLOCK_SIZE);

		if (blockX >= 0 && blockX < MAP_SIZE_CHUNKS * Chunk.CHUNK_WIDTH && blockY >= 0 && blockY < Map.MAP_HEIGHT) {
			return new Point(blockX, blockY);
		}
		return null;
	}

	/** Gets the block at a specified coordinate */
	public Block getBlock(int x, int y) {
		if (x >= 0 && x < MAP_SIZE_CHUNKS * Chunk.CHUNK_WIDTH && y >= 0 && y < MAP_HEIGHT)
			return getChunkAtBlock(x).getBlock(x, y);
		return null;
	}

	/** Sets the block at a specified coordinate */
	public void setBlock(Block block, int x, int y) {
		if (x >= 0 && x < MAP_SIZE_CHUNKS * Chunk.CHUNK_WIDTH && y >= 0 && y < MAP_HEIGHT)
			getChunkAtBlock(x).setBlock(block, x, y);
	}

	public void cyclePlaceBlockIndexBack() {
		currentPlaceBlockIndex--;
		if (currentPlaceBlockIndex < 0)
			currentPlaceBlockIndex = Blocks.placeBlockList.size() - 1;
	}

	public void cyclePlaceBlockIndexForward() {
		currentPlaceBlockIndex++;
		if (currentPlaceBlockIndex >= Blocks.placeBlockList.size())
			currentPlaceBlockIndex = 0;
	}

	public float getLightLevel(int x, int y) {
		return getChunkAtBlock(x).getLightLevel(x, y);
	}

}