package net.thedanpage.game.graphics;

import java.util.HashMap;
import java.util.Map;

/**
 * A font object, used for drawing characters on the screen. Each font's texture
 * file is located in resources/images/fonts/(fontname).png, and assigned a name
 * directly through the {@link Fonts#init()} function. Font characters should be
 * drawn on a grid, with the font's charWidth and charHeight reprenting the
 * dimensions of each character, in pixels. Only completely white pixels will be
 * read as the font texture, so the background of the image can be any other
 * color/s of choice.
 * 
 * @author Dan
 *
 */
public class Font {
	
	static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.,;:?!-_'#\"\\/{}() ";

	// The width and height of each individual character
	private int charWidth, charHeight;
	
	/**
	 * Storage for character textures, referenced by their respective char
	 */
	private Map<Character, int[]> charTextures = new HashMap<Character, int[]>();

	/**
	 * A font used for drawing strings to the game.
	 * 
	 * @param fontName   the file name of the font. For example,
	 *                   <code>resources/images/fonts/myfont.png</code> would become
	 *                   "myfont".
	 * @param charWidth  the width of each character, in pixels
	 * @param charHeight the height of each character, in pixels
	 */
	public Font(String fontName, int charWidth, int charHeight) {
		this.charWidth = charWidth;
		this.charHeight = charHeight;

		// Loads the font's png texture from the fonts folder
		Texture tex = new Texture("fonts/" + fontName + ".png");

		/*
		 * Get charsWide and charsTall by dividing (image width/height)/(char
		 * width/height)
		 */
		int charsWide = tex.getWidth() / charWidth;
		int charsTall = tex.getHeight() / charHeight;

		// Get the raw pixel data from the font image
		int[] fontmap = tex.getPixels();

		// Counts the number of the texture so that we can get its matching character
		int charIndex=0;
		// Iterate through each character
		for (int charY = 0; charY < charsTall; charY++) {
			for (int charX = 0; charX < charsWide; charX++) {
				
				// Temporarily stores the pixel data for each character
				int[] charTexture = new int[charWidth*charHeight];
				
				// Iterate through each pixel of the character on the image
				for (int x = charX; x < (charX * charWidth) + charWidth; x++) {
					for (int y = charY; y < (charY * charHeight) + charHeight; y++) {

						// If the pixel is pure white, set the pixel in charTexture to 1
						if (fontmap[x + y * tex.getWidth()] == 0xffffff)
							charTexture[x % this.charWidth
										+ (y % this.charHeight) * charWidth] = 1;
						// Otherwise, set the pixel to 0
						else
							charTexture[x % this.charWidth
										+ (y % this.charHeight) * charWidth] = 0;
					}
				}
				
				// Transfer charTexture to charTextures
				charTextures.put(CHARS.charAt(charIndex), charTexture);
				
				charIndex ++;

			}
		}
	}

	/** Returns the width of characters in a font */
	public int getCharWidth() {
		return this.charWidth;
	}

	/** Returns the height of characters in a font */
	public int getCharHeight() {
		return this.charHeight;
	}

	/**
	 * Returns an array containing the binary texture data of a character in the
	 * font. If the RGB pixel data for drawing images is desired,
	 * {@link Fonts#charDataToPixelArray} should be used in tandem.
	 */
	public int[] getCharTexture(char c) {
		return this.charTextures.get(c);
	}

}
