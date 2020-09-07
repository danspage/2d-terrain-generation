package net.thedanpage.game.graphics;

import java.util.HashMap;
import java.util.Map;

/**
 * A class for managing and loading fonts. It preloads all of the game's fonts
 * and stores them in {@link #fonts}. Unlike textures, fonts are not data driven
 * and are loaded and named individually in {@link #loadFonts()}.
 * 
 * 
 * @author Dan
 *
 */
public class Fonts {

	/**
	 * A hashmap used to index each allowed character to a number value
	 */
	private static Map<Character, Integer> charList = new HashMap<Character, Integer>();

	/**
	 * A HashMap containing all of the game's fonts
	 */
	private static Map<String, Font> fonts = new HashMap<String, Font>();

	/**
	 * A list of all allowed characters, in order of how they appear on font
	 * textures (left to right, top to bottom)
	 */
	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.,;:?!-_'#\"\\/{}() ";

	/**
	 * Instantiates {@link #charList} and loads all fonts
	 */
	public static void init() {
		for (int i = 0; i < CHARS.length(); i++)
			charList.put(CHARS.charAt(i), i);

		loadFonts();
	}

	/**
	 * Loads an individual font
	 * 
	 * @param font        the file name of the font. For example,
	 *                    <code>resources/images/fonts/myfont.png</code> would
	 *                    become "myfont".
	 * @param charWidth   the width of each character, in pixels
	 * @param charHeight  the height of each character, in pixels
	 * @param storageName the name that the font will be referenced by when it is
	 *                    accessed
	 */
	private static void loadFont(String font, int charWidth, int charHeight, String storageName) {
		fonts.put(storageName, new Font(font, charWidth, charHeight));
	}

	/**
	 * Loads the game's fonts
	 */
	private static void loadFonts() {
		loadFont("tiny", 3, 5, "tinyfont");
	}

	/**
	 * A character's pixel data is represented in binary numbers, so to draw it, it
	 * needs to be converted to RGB data. This function fills it with a specified
	 * RGB integer and returns valid pixel data.
	 * 
	 * @param charData the character's binary pixel data
	 * @param color    an RGB integer to fill the character with
	 * @return RGB pixel data
	 */
	private static int[] charDataToPixelArray(int[] charData, int color) {
		int[] newCharData = new int[charData.length];

		// Literally just replaces all the 0's with -1's and 1's with the RGB color
		for (int i = 0; i < charData.length; i++) {
			if (charData[i] == 1)
				newCharData[i] = color;
			else
				newCharData[i] = -1;
		}
		return newCharData;

	}

	/**
	 * Draws a character on screen, for use only with the {@link #drawString}
	 * function
	 */
	private static void drawChar(Font font, char c, int x, int y, int color) throws Exception {
		if (!CHARS.contains(new Character(c).toString()))
			throw new Exception("The input string contained an invalid character: " + c);
		Graphics.drawImage(x, y, font.getCharWidth(), font.getCharHeight(),
				charDataToPixelArray(font.getCharTextures()[CHARS.indexOf(c)], color));
	}

	/**
	 * Draws a string onto the screen using a specified font, and a specified color.
	 * 
	 * @param string the string to be drawn
	 * @param font   the reference name of any font
	 * @param x      an x position on the screen to draw at
	 * @param y      a y position on the screen to draw at
	 * @param color  a color to fill the characters with, in RBG integer format
	 */
	public static void drawString(String string, String font, int x, int y, int color) {
		try {
			// Get the font based on the reference nae
			Font f = fonts.get(font);

			// Draw each character of the string, with one pixel of space in between each
			// one
			for (int i = 0; i < string.length(); i++) {
				drawChar(f, string.charAt(i), x + i * (f.getCharWidth() + 1), y, color);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
