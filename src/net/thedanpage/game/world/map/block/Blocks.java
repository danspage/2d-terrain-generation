package net.thedanpage.game.world.map.block;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.thedanpage.game.Game;
import net.thedanpage.game.framework.Util;

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
	 * Block animation timer for cycling block textures. {@link net.thedanpage.game.graphics.Texture#init()}
	 * sets BLOCK_ANIM_TIMER_LENGTH to a multiple of all unique block animation
	 * lengths. This is done so that one timer can be used for every animation,
	 * while allowing them to seamlessly loop.
	 */
	public static int BLOCK_ANIM_TIMER_LENGTH = 0;
	private static int blockAnimTimer = 0;
	private static final int BLOCK_ANIM_TIME = 4;

	/** Loads block properties from <code>resources/blockproperties.json</code> */
	@SuppressWarnings("unchecked")
	public static void loadProperties() {
		try {
			JSONParser parser = new JSONParser();

			Reader reader = new InputStreamReader(Util.getResourceAsStream("blockproperties.json"));

			JSONObject jsonObject = (JSONObject) parser.parse(reader);

			jsonObject.keySet().forEach(key -> {
				String texture = (String) ((JSONObject) jsonObject.get(key)).get("texture");
				boolean isFluid = (Boolean) ((JSONObject) jsonObject.get(key)).get("isFluid");
				blockProperties.put((String) key, new Object[] { texture, isFluid });
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Retrieves a single property given a block name */
	public static Object getProperty(String blockName, String property) {
		if (property == "texture") {
			return blockProperties.get(blockName)[0];
		} else if (property == "isFluid") {
			return blockProperties.get(blockName)[1];
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

}
