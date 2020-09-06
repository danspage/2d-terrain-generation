package net.thedanpage.game.graphics;

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

	// The width and height of each individual character
	private int charWidth, charHeight;

	// The pixel data of each individual character in the font
	private int[][] charTextures;

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

		int charsWide = tex.getWidth() / charWidth;
		int charsTall = tex.getHeight() / charHeight;

		charTextures = new int[charsWide * charsTall][charWidth * charHeight];

		int[] fontmap = tex.getPixels();
		for (int charX = 0; charX < charsWide; charX++) {
			for (int charY = 0; charY < charsTall; charY++) {

				for (int x = charX; x < (charX * charWidth) + charWidth; x++) {
					for (int y = charY; y < (charY * charHeight) + charHeight; y++) {

						if (fontmap[x + y * tex.getWidth()] == 0xffffff)
							charTextures[charX + charY * charsWide][x % this.charWidth
									+ (y % this.charHeight) * charWidth] = 1;
						else
							charTextures[charX + charY * charsWide][x % this.charWidth
									+ (y % this.charHeight) * charWidth] = 0;
					}
				}

			}
		}
	}

	/**
	 * Returns the width of characters in a font
	 * 
	 * @return character width
	 */
	int getCharWidth() {
		return this.charWidth;
	}

	/**
	 * Returns the height of characters in a font
	 * 
	 * @return character height
	 */
	int getCharHeight() {
		return this.charHeight;
	}

	/**
	 * Returns an array containing the binary texture data of each character in the
	 * font. If the RGB pixel data for drawing images is desired,
	 * {@link Fonts#charDataToPixelArray} should be used in tandem.
	 * 
	 * @return
	 */
	int[][] getCharTextures() {
		return this.charTextures;
	}

}
