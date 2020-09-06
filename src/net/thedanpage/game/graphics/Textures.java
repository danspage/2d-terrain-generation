package net.thedanpage.game.graphics;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.thedanpage.game.framework.Util;
import net.thedanpage.game.world.map.block.Blocks;

/**
 * A class for managing and loading textures. It preloads all of the game's
 * textures and stores them in {@link #textures} and {@link #blockTextures}.
 * 
 * @author Dan
 *
 */
public class Textures {

	/**
	 * A HashMap containing all non-block texture names and their respective RGB
	 * pixel data
	 */
	private static Map<String, Texture> textures = new HashMap<String, Texture>();

	/**
	 * A HashMap containing all block texture names and their respective RGB pixel
	 * data
	 */
	private static Map<String, Texture> blockTextures = new HashMap<String, Texture>();

	@SuppressWarnings("unchecked")
	public static void init() {

		// Load json files containing texture paths

		JSONParser parser = new JSONParser();

		// Load regular textures
		try {
			Reader reader = new InputStreamReader(Util.getResourceAsStream("textures.json"));
			JSONObject jsonObject = (JSONObject) parser.parse(reader);

			jsonObject.keySet().forEach(key -> {

				Object valObj = jsonObject.get(key);

				if (valObj instanceof String) {
					// If the json value is a string, add it to blockTextures as a texture
					textures.put((String) key, new Texture((String) valObj + ".png"));
				} else if (valObj instanceof JSONArray) {
					// If the json value is a JSONArray, iterate it and add its values to
					// blockTextures as a texture
					JSONArray valArr = (JSONArray) valObj;

					List<String> texturesList = new ArrayList<String>();

					Iterator<String> i = valArr.iterator();
					while (i.hasNext()) {
						texturesList.add(i.next() + ".png");
					}

					try {
						textures.put((String) key, new AnimatedTexture(texturesList.stream().toArray(String[]::new)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Load block textures
		try {
			Reader reader = new InputStreamReader(Util.getResourceAsStream("blocktextures.json"));

			JSONObject jsonObject = (JSONObject) parser.parse(reader);

			jsonObject.keySet().forEach(key -> {

				Object valObj = jsonObject.get(key);

				if (valObj instanceof String) {
					// If the json value is a string, add it to blockTextures as a texture
					blockTextures.put((String) key, new Texture((String) valObj + ".png"));
				} else if (valObj instanceof JSONArray) {
					// If the json value is a JSONArray, iterate it and add its values to
					// blockTextures as a texture
					JSONArray valArr = (JSONArray) valObj;

					List<String> texturesList = new ArrayList<String>();

					Iterator<String> i = valArr.iterator();
					while (i.hasNext()) {
						texturesList.add(i.next() + ".png");
					}

					try {
						blockTextures.put((String) key,
								new AnimatedTexture(texturesList.stream().toArray(String[]::new)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Set blockAnimTimer to a multiple of all the different block animation lengths
		List<Integer> animTextureFramesList = new ArrayList<Integer>();
		blockTextures.keySet().forEach(key -> {
			if (blockTextures.get(key) instanceof AnimatedTexture) {
				if (!animTextureFramesList.contains(((AnimatedTexture) blockTextures.get(key)).frames.length))
					animTextureFramesList.add(((AnimatedTexture) blockTextures.get(key)).frames.length);
			}
		});
		for (int i = 0; i < animTextureFramesList.size(); i++) {
			if (i == 0)
				Blocks.BLOCK_ANIM_TIMER_LENGTH = animTextureFramesList.get(i);
			else
				Blocks.BLOCK_ANIM_TIMER_LENGTH *= animTextureFramesList.get(i);
		}

	}

	/** Returns a texture based on its reference name */
	public static Texture getTexture(String texture) {
		return textures.get(texture);
	}

	/** Returns a block texture based on its reference name */
	public static Texture getBlockTexture(String block) {
		return blockTextures.get(block);
	}

}
