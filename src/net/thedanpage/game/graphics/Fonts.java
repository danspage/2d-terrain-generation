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
	 * A HashMap containing all of the game's fonts
	 */
	private static Map<String, Font> fonts = new HashMap<String, Font>();

	/**
	 * Instantiates {@link #charList} and loads all fonts
	 */
	public static void init() {

		loadFonts();
	}

	/**
	 * Loads an individual font
	 * 
	 * @param fontPath    the file name of the font. For example,
	 *                    <code>resources/images/fonts/myfont.png</code> would
	 *                    become "myfont".
	 * @param charWidth   the width of each character, in pixels
	 * @param charHeight  the height of each character, in pixels
	 * @param storageName the name that the font will be referenced by when it is
	 *                    accessed
	 */
	private static void loadFont(String fontPath, int charWidth, int charHeight, String storageName) {
		fonts.put(storageName, new Font(fontPath, charWidth, charHeight));
	}

	/**
	 * Loads the game's fonts
	 */
	private static void loadFonts() {
		loadFont("tiny", 3, 5, "tinyfont");
		loadFont("large", 5, 7, "largefont");
		loadFont("doublelarge", 10, 14, "doublelargefont");
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
	private static int[] getCharPixels(Font font, char c, int color) throws Exception {
		if (!Font.CHARS.contains(new Character(c).toString()))
			throw new Exception("The input string contained an invalid character: " + c);
		return charDataToPixelArray(font.getCharTexture(c), color);
	}

	/**
	 * Draws a string onto the screen using a specified font, and a specified color.
	 * 
	 * @param string    the string to be drawn
	 * @param font      the reference name of any font
	 * @param x         an x position on the screen to draw at
	 * @param y         a y position on the screen to draw at
	 * @param color     a color to fill the characters with, in RBG integer format
	 * @param alignment the alignment of the text, determined by a Font.ALIGN...
	 *                  constant
	 */
	public static void drawString(String string, String font, int x, int y, int color, int alignment) {
		try {
			// Get the font based on the reference name
			Font f = fonts.get(font);

			Texture stringTex = new Texture((f.getCharWidth() + 1) * string.length() - 1, f.getCharHeight());

			/*
			 * Draw each character to the string texture, with one pixel of space in between
			 * each one
			 */
			for (int i = 0; i < string.length(); i++) {
				stringTex.drawImageToTexture(
						new Texture(getCharPixels(f, string.charAt(i), color), f.getCharWidth(), f.getCharHeight()),
						i * (f.getCharWidth() + 1), 0);
			}

			switch (alignment) {
			
			case Font.ALIGN_LEFT:
				break;
			case Font.ALIGN_CENTER:
				x -= stringTex.getWidth() / 2;
				break;
			case Font.ALIGN_RIGHT:
				x -= stringTex.getWidth();
				
			}

			Graphics.drawImage(x, y, stringTex.getWidth(), stringTex.getHeight(), stringTex.getPixels());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
