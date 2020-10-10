package net.thedanpage.game.world.map.block;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.thedanpage.game.Game;
import net.thedanpage.game.framework.Util;
import net.thedanpage.game.framework.gamestate.WorldState;
import net.thedanpage.game.world.entity.Entity;

/**
 * A class for managing and loading blocks and their properties. Block
 * properties are located in <code>resources/blockproperties.json</code>
 * 
 * @author Dan
 *
 */
public class Blocks {

	/** A Hashmap containing a list of blocks and their properties */
	public static Map<String, Object[]> blockProperties = new HashMap<String, Object[]>();

	/**
	 * A hashmap containing a list of translucencies for each block group, defined
	 * in blocktranslucencies.json
	 */
	public static Map<String, Float> blockTranslucencies = new HashMap<String, Float>();

	/**
	 * Block animation timer for cycling block textures.
	 * {@link net.thedanpage.game.graphics.Texture#init()} sets
	 * BLOCK_ANIM_TIMER_LENGTH to a multiple of all unique block animation lengths.
	 * This is done so that one timer can be used for every animation, while
	 * allowing them to seamlessly loop.
	 */
	public static int BLOCK_ANIM_TIMER_LENGTH = 0;
	private static int blockAnimTimer = 0;
	private static final int BLOCK_ANIM_TIME = 4;

	private static JSONParser parser = new JSONParser();

	/** A list containing all blocks that can be placed */
	public static List<String> placeBlockList = new ArrayList<String>();

	/** Loads block properties from <code>resources/blockproperties.json</code> */
	@SuppressWarnings("unchecked")
	public static void loadProperties() {
		// Load translucency groups
		try {

			Reader reader = new InputStreamReader(Util.getResourceAsStream("blocktranslucencies.json"));

			JSONObject jsonObject = (JSONObject) parser.parse(reader);

			jsonObject.keySet().forEach(key -> {
				blockTranslucencies.put((String) key, ((Number) jsonObject.get(key)).floatValue());
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Load normal properties
		try {

			Reader reader = new InputStreamReader(Util.getResourceAsStream("blockproperties.json"));

			JSONObject jsonObject = (JSONObject) parser.parse(reader);

			jsonObject.keySet().forEach(key -> {
				String texture = (String) ((JSONObject) jsonObject.get(key)).get("texture");
				boolean isFluid = (Boolean) ((JSONObject) jsonObject.get(key)).get("isFluid");
				boolean isLightSource = (Boolean) ((JSONObject) jsonObject.get(key)).get("isLightSource");
				String translucencyGroup = (String) ((JSONObject) jsonObject.get(key)).get("translucencyGroup");
				blockProperties.put((String) key,
						new Object[] { texture, isFluid, blockTranslucencies.get(translucencyGroup), isLightSource });
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Initialize the list of blocks that can be placed
		for (Entry<String, Object[]> entry : blockProperties.entrySet()) {
			placeBlockList.add(entry.getKey());
		}
		placeBlockList.remove(placeBlockList.indexOf("null"));
	}

	/** Retrieves a single property given a block name */
	public static Object getProperty(String blockName, String property) {
		if (property == "texture") {
			return blockProperties.get(blockName)[0];
		} else if (property == "isFluid") {
			return blockProperties.get(blockName)[1];
		} else if (property == "translucency") {
			return blockProperties.get(blockName)[2];
		} else if (property == "isLightSource") {
			return blockProperties.get(blockName)[3];
		}
		return null;
	}

	/** Updates the block helper class */
	public static void update() {
		if (Game.getTicks() % BLOCK_ANIM_TIME == 0)
			blockAnimTimer++;
		if (blockAnimTimer >= BLOCK_ANIM_TIMER_LENGTH)
			blockAnimTimer = 0;
	}

	/** Returns the current value of the block animation timer */
	public static int getBlockAnimTime() {
		return blockAnimTimer;
	}

	public static void sortByDistanceToPlayer(ArrayList<Block> blocks) {
		for (int b = 0; b < blocks.size(); b++) {
			blocks.get(b).setDistToEntity(WorldState.getMap().getEntityGroup(Entity.ENTITY_GROUP_PLAYERS).get(0));
		}
		Collections.sort(blocks);
	}

}
