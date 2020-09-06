package net.thedanpage.game.framework;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * A class for managing and loading sounds. It preloads all of the game's sound objects and plays them based
 * on their names. The sound names and filepaths are located in resources/sounds.json
 * 
 * @author Dan
 *
 */
public class Sounds {
	
	/**
	 * A hash map containing the game's sounds. It's convenient to reference all of the sounds based on a string,
	 * as opposed to their full file name.
	 */
	private static Map<String, Sound> sounds = new HashMap<String, Sound>();
	
	/**
	 * Load the game's sounds. The sound names and filepaths are located in resources/sounds.json
	 */
	@SuppressWarnings("unchecked")
	public static void init() {
		try {
			JSONParser parser = new JSONParser();

			// Load the resources/sounds.json file
			Reader reader = new InputStreamReader(Util.getResourceAsStream("sounds.json"));

			// The JSON data will be loader into this object
			JSONObject jsonObject;

			// Parse the JSON data from the file
			jsonObject = (JSONObject) parser.parse(reader);

			jsonObject.keySet().forEach(key -> {
				
				// The data loaded from each individual json key
				JSONObject json = ((JSONObject)jsonObject.get(key));
				
				// Load if there is a single sound
				if (json.containsKey("sound")) {
					String path = (String) json.get("sound");
					sounds.put((String) key, new Sound(path));
				}
				
				// Load if there are multiple sounds
				else if (json.containsKey("sounds")) {
					JSONArray jsonArray = (JSONArray) json.get("sounds");
					String[] paths = new String[jsonArray.size()];
					
					for (int i=0; i<jsonArray.size(); i++) {
						paths[i] = jsonArray.get(i).toString();
					}
					
					sounds.put((String) key, new Sound(paths));
				}
				
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play a sound referenced by its name, at the original volume
	 * 
	 * @param sound The name that the sound is referenced by
	 */
	public static void play(String sound) {
		try {
			if (sounds.containsKey(sound)) {
				sounds.get(sound).play();
			} else {
				throw new Exception("The sound '" + sound + "' does not exist.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Play a sound referenced by its name, at a specified volume
	 * 
	 * @param sound The name that the sound is referenced by
	 * @param volume Any double from 0 to 1, with 1 being normal volume
	 */
	public static void play(String sound, double volume) {
		try {
			if (sounds.containsKey(sound)) {
				sounds.get(sound).play(volume);
			} else {
				throw new Exception("The sound '" + sound + "' does not exist.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
