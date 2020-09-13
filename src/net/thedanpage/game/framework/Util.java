package net.thedanpage.game.framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.rits.cloning.Cloner;

/**
 * A general utilities class
 * 
 * @author Dan
 *
 */
public class Util {

	private static ClassLoader cl = Util.class.getClassLoader();

	/**
	 * Used to round decimals when the coordinates of the player are displayed on
	 * screen.
	 */
	private static DecimalFormat dfForString = new DecimalFormat("#.0000");

	/** Used to clone objects */
	private static Cloner cloner = new Cloner();

	/**
	 * Initialization for the util functions.
	 */
	public static void init() {
		// Round the decimals up for dfForString.
		dfForString.setRoundingMode(RoundingMode.CEILING);
	}

	public static int getMaxIntInArray(int[] inputArray) {
		int maxValue = inputArray[0];
		for (int i = 1; i < inputArray.length; i++) {
			if (inputArray[i] > maxValue) {
				maxValue = inputArray[i];
			}
		}
		return maxValue;
	}

	public static int getMinIntInArray(int[] inputArray) {
		int minValue = inputArray[0];
		for (int i = 1; i < inputArray.length; i++) {
			if (inputArray[i] < minValue) {
				minValue = inputArray[i];
			}
		}
		return minValue;
	}

	/**
	 * Returns an InputStream relative to the resources folder.
	 * 
	 * @param filepath a path relative to the resources folder
	 * @return the InputStream relative to the resources folder
	 */
	public static InputStream getResourceAsStream(String filepath) {
		return cl.getResourceAsStream("net/thedanpage/game/resources/" + filepath);
	}

	/**
	 * Get a list of files from the resources directory in the format of a string
	 * array. Solution taken from <a href=
	 * "https://stackoverflow.com/questions/28985379/java-how-to-read-folder-and-list-files-in-that-folder-in-jar-environment-instead">Stack
	 * Overflow</a>
	 * 
	 * @param folderpath
	 * @return
	 */
	public static List<String> getFilesInResourceFolder(String folderpath) {
		List<String> filenames = new ArrayList<String>();
		try (
				// Not really sure what this does. I copied it from stack overflow lol
				final InputStream is2 = cl.getResourceAsStream("net/thedanpage/game/resources/" + folderpath);
				final InputStreamReader isr = new InputStreamReader(is2, StandardCharsets.UTF_8);
				final BufferedReader br = new BufferedReader(isr)) {
			// Add each output from "br" to the filenames list
			br.lines().forEach(filenames::add);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filenames;
	}

	/**
	 * Format a double to a string (format: #.####) because it looks nice with
	 * 4-decimal rounding. 3 cases are included because Java is stupid and has
	 * inconsistent double formatting. <333 This is used for formatting the player's
	 * coordinates when they're shown on the screen. {@link #dfForString} is used in
	 * order to do the formatting.
	 * 
	 * @param d A double to be formatted
	 * @return
	 */
	public static String formatDoubleForString(double d) {
		if (d == 0)
			return "0.0000";
		if (d > 0 && d < 1)
			return "0" + dfForString.format(d);

		// The DecimalFormat object is used to easily format doubles to strings.
		return dfForString.format(d);
	}

	/**
	 * Clones objects using kostaskougios'
	 * <a href="https://github.com/kostaskougios/cloning">cloning library</a>
	 */
	public static Object deepClone(Object obj) {
		return cloner.deepClone(obj);
	}

}
