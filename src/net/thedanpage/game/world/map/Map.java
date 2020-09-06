package net.thedanpage.game.world.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.thedanpage.game.Game;
import net.thedanpage.game.graphics.Fonts;
import net.thedanpage.game.world.entity.Entity;
import net.thedanpage.game.world.entity.Player;
import net.thedanpage.game.world.map.block.Block;
import net.thedanpage.game.world.map.block.Blocks;

public class Map {

	/**
	 * The maximum height of the map, in blocks
	 */
	public static final int MAP_HEIGHT = 256;

	/**
	 * The maximum number of chunks the map will create
	 */
	public static final int MAP_SIZE_CHUNKS = 100;

	/**
	 * An ArrayList containing all entities in the world
	 */
	private static List<List<Entity>> entities = new ArrayList<List<Entity>>();

	/**
	 * An ArrayList containing all chunks in the world
	 */
	private static List<Chunk> chunks = new ArrayList<Chunk>();

	/**
	 * The main player entity
	 */
	private static Player player;

	/**
	 * A random number that the world will generate terrain based off of
	 */
	private static int seed;

	private static int numGeneratedChunks = 0;

	private static int chunkRenderMin, chunkRenderMax;

	/**
	 * Returns a {@link Chunk} object based on its index in {@link #chunks}
	 * 
	 * @param index
	 * @return
	 */
	public static Chunk getChunkAtIndex(int index) {
		return chunks.get(index);
	}

	/**
	 * Returns the chunk that a certain X coordinate is inside of
	 * 
	 * @param blockX an X coordinate
	 * @return the {@link Chunk} object
	 */
	public static Chunk getChunkAtBlock(int blockX) {
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
	public static void addEntity(int group, Entity entity) {
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
	public static List<Entity> getEntityGroup(int group) {
		return entities.get(group);
	}

	public static void init() {

		// Generate a random 7 digit seed
		seed = new Random().nextInt(10000000);
		while (seed < 1000000)
			seed *= 10;

		// Instantiate the entity groups
		entities.add(Entity.ENTITY_GROUP_EVERYTHING, new ArrayList<Entity>());
		entities.add(Entity.ENTITY_GROUP_PLAYERS, new ArrayList<Entity>());

		// Instantiate the player
		player = new Player();
		player.setPosition(25, 10);
		addEntity(Entity.ENTITY_GROUP_PLAYERS, player);

		// Instantiate the chunks. They are NOT generated upon creation
		for (int i = 0; i < MAP_SIZE_CHUNKS; i++)
			chunks.add(i, new Chunk(i * Chunk.CHUNK_WIDTH));
	}

	public static void update() {
		// Update the chunks that are visible on screen
		for (int x = chunkRenderMin; x < chunkRenderMax; x++) {
			if (x >= 0 && x < chunks.size() && chunks.get(x) != null) {
				if (!chunks.get(x).isGenerated())
					chunks.get(x).generate(seed);
				chunks.get(x).update();
			}
		}

		// Update all entities
		for (Entity entity : getEntityGroup(Entity.ENTITY_GROUP_EVERYTHING)) {
			entity.update();
		}

		// Update the blocks helper class
		Blocks.update();

		// Update the sky
		Sky.update();
	}

	public static void render() {

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

		// Render and generate chunks that are visible on the screen
		for (int x = chunkRenderMin; x < chunkRenderMax; x++) {
			if (x >= 0 && x < chunks.size() && chunks.get(x) != null) {
				if (!chunks.get(x).isGenerated())
					chunks.get(x).generate(seed);
				chunks.get(x).render();
			}
		}

		// Render entities
		for (Entity entity : getEntityGroup(Entity.ENTITY_GROUP_EVERYTHING)) {
			try {
				entity.draw();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Show various information on the top left of the screen
		Fonts.drawString("Ups:" + Game.getCurrentUps() + " Fps:" + Game.getCurrentFps(), "tinyfont", 2, 2, 0xffff00);
		Fonts.drawString("Space: Toggle flying", "tinyfont", 2, 8, 0xffff00);
		Fonts.drawString("Seed:" + seed, "tinyfont", 2, 14, 0xffff00);

		numGeneratedChunks = 0;
		for (int i = 0; i < chunks.size(); i++)
			if (chunks.get(i).isGenerated())
				numGeneratedChunks++;
		Fonts.drawString("Generated chunks:" + numGeneratedChunks, "tinyfont", 2, 20, 0xffff00);

		Fonts.drawString("Movement:WASD", "tinyfont", 2, 44, 0xffff00);

	}

}
