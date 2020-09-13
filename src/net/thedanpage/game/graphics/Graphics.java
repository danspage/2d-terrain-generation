package net.thedanpage.game.graphics;

import java.awt.geom.Rectangle2D;

import net.thedanpage.game.Game;
import net.thedanpage.game.world.map.block.Block;

/**
 * A general class for handling and drawing graphics to the screen.
 * 
 * @author Dan
 *
 */
public class Graphics {

	/**
	 * Draws an image to the screen given an array of RGB pixel data.
	 * 
	 * @param x         an x coordinate on the screen to draw the image at
	 * @param y         a y coordinate on the screen to draw the image at
	 * @param imgWidth  the width of the image, in pixels
	 * @param imgHeight the height of the image, in pixels
	 * @param imgPixels an array of pixel data
	 */
	public static void drawImage(int x, int y, int imgWidth, int imgHeight, int[] imgPixels) {
		try {

			for (int i = 0; i < imgWidth; i++) {
				for (int j = 0; j < imgHeight; j++) {
					if (x + i >= Game.screen.getWidth() || x + i < 0)
						break;

					if (imgPixels[i + j * imgWidth] != -1 && (y + j < Game.screen.getHeight() && y + j >= 0))
						Game.screen.setPixel(x + i, y + j, imgPixels[i + j * imgWidth]);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Draws a {@link Texture} object directly to the screen.
	 * 
	 * @param x       an x coordinate on the screen to draw the texture at
	 * @param y       a y coordinate on the screen to draw the texture at
	 * @param texture a texture object
	 */
	public static void drawTexture(int x, int y, Texture texture) {
		drawImage(x, y, texture.getWidth(), texture.getHeight(), texture.getPixels());
	}

	/**
	 * Draws a {@link Texture} object directly to the screen, tiled vertically and
	 * horizontally, and starting from (x,y)
	 * 
	 * @param texture a texture object
	 */
	public static void drawTextureTiled(int x, int y, Texture texture) {
		while (x > 0)
			x -= texture.getWidth();
		while (y > 0)
			y -= texture.getHeight();
		for (int px = x; px < Game.WIDTH; px += texture.getWidth()) {
			for (int py = y; py < Game.HEIGHT; py += texture.getHeight()) {
				drawImage(px, py, texture.getWidth(), texture.getHeight(), texture.getPixels());
			}
		}
	}

	/**
	 * Flips an array of RGB pixel data horizontally.
	 * 
	 * @param width  width of the image in pixels
	 * @param height height of the image in pixels
	 * @param pixels an RGB pixel data array
	 * @return
	 */
	public static int[] flipHorizontal(int width, int height, int[] pixels) {
		int[] newImg = new int[width * height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				newImg[x + y * width] = pixels[((width - 1) - x) + y * width];
			}
		}
		return newImg;
	}

	/**
	 * Flips an array of RGB pixel data vertically.
	 * 
	 * @param width  width of the image in pixels
	 * @param height height of the image in pixels
	 * @param pixels an RGB pixel data array
	 * @return
	 */
	public static int[] flipVertical(int width, int height, int[] pixels) {
		int[] newImg = new int[width * height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				newImg[x + y * width] = pixels[x + ((height - 1) - y) * width];
			}
		}
		return newImg;
	}

	/**
	 * Draws a rectangle to the screen.
	 * 
	 * @param rect           a {@link Rectangle2D}.Double object
	 * @param color          the color of the rectangle
	 * @param useBlockCoords specifies whether the rectangle's coordinates are
	 *                       relative to the screen, or relative to blocks in the
	 *                       world
	 */
	public static void drawRectangle(Rectangle2D.Double rect, int color, boolean useBlockCoords) {
		int rectX, rectY, rectW, rectH;

		// Draw the rectangle based on block coordinates, using the screen offset, and
		// scaling up to block coordinates
		if (useBlockCoords) {
			rectX = (int) (rect.getX() * Block.BLOCK_SIZE);
			rectY = (int) (rect.getY() * Block.BLOCK_SIZE);
			rectW = (int) (rect.getWidth() * Block.BLOCK_SIZE);
			rectH = (int) (rect.getHeight() * Block.BLOCK_SIZE);

			for (int x = rectX; x < rectX + rectW + 1; x++) {
				Game.screen.setPixel(Game.screen.getScreenOffsetX() + x,
						Game.screen.getScreenOffsetY() + Game.screen.getHeight() - (rectY + 1), color);
				Game.screen.setPixel(Game.screen.getScreenOffsetX() + x,
						Game.screen.getScreenOffsetY() + Game.screen.getHeight() - (rectY + rectH + 1), color);
			}
			for (int y = rectY + 1; y < rectY + rectH; y++) {
				Game.screen.setPixel(Game.screen.getScreenOffsetX() + rectX,
						Game.screen.getScreenOffsetY() + Game.screen.getHeight() - (y + 1), color);
				Game.screen.setPixel(Game.screen.getScreenOffsetX() + (rectX + rectW),
						Game.screen.getScreenOffsetY() + Game.screen.getHeight() - (y + 1), color);
			}
		} else {
			// Draw the rectangle normally using screen coordinates
			rectX = (int) rect.getX();
			rectY = (int) rect.getY();
			rectW = (int) rect.getWidth();
			rectH = (int) rect.getHeight();

			for (int x = rectX; x < rectX + rectW + 1; x++) {
				Game.screen.setPixel(x, Game.screen.getHeight() - (rectY + 1), color);
				Game.screen.setPixel(x, Game.screen.getHeight() - (rectY + rectH + 1), color);
			}
			for (int y = rectY + 1; y < rectY + rectH; y++) {
				Game.screen.setPixel(rectX, Game.screen.getHeight() - (y + 1), color);
				Game.screen.setPixel(rectX + rectW, Game.screen.getHeight() - (y + 1), color);
			}
		}
	}

	/**
	 * Draws a filled rectangle to the screen.
	 * 
	 * @param rect           a {@link Rectangle2D}.Double object
	 * @param color          the color of the rectangle
	 * @param useBlockCoords specifies whether the rectangle's coordinates are
	 *                       relative to the screen, or relative to blocks in the
	 *                       world
	 */
	public static void fillRectangle(Rectangle2D.Double rect, int color, boolean useBlockCoords) {
		int rectX, rectY, rectW, rectH;

		// Draw the rectangle based on block coordinates, using the screen offset, and
		// scaling up to block coordinates
		if (useBlockCoords) {
			rectX = (int) (rect.getX() * Block.BLOCK_SIZE);
			rectY = (int) (rect.getY() * Block.BLOCK_SIZE);
			rectW = (int) (rect.getWidth() * Block.BLOCK_SIZE);
			rectH = (int) (rect.getHeight() * Block.BLOCK_SIZE);

			for (int x = rectX; x < rectX + rectW; x++) {
				for (int y = rectY; y < rectY + rectH; y++) {
					Game.screen.setPixel(Game.screen.getScreenOffsetX() + x,
							Game.screen.getScreenOffsetY() + Game.screen.getHeight() - (y + 1), color);
				}
			}
		} else {
			// Draw the rectangle normally using screen coordinates
			rectX = (int) rect.getX();
			rectY = (int) rect.getY();
			rectW = (int) rect.getWidth();
			rectH = (int) rect.getHeight();

			for (int x = rectX; x < rectX + rectW; x++) {
				for (int y = rectY; y < rectY + rectH; y++) {
					Game.screen.setPixel(x, Game.screen.getHeight() - (y + 1), color);
				}
			}
		}
	}
	
	
	/**
	 * Highlights a rectangle on the screen, relative to block coordinates.
	 * 
	 * @param rect           a {@link Rectangle2D}.Double object
	 * @param color          the color of the rectangle
	 * @param useBlockCoords specifies whether the rectangle's coordinates are
	 *                       relative to the screen, or relative to blocks in the
	 *                       world
	 */
	public static void highlightRectangleBlockCoords(Rectangle2D.Double rect) {
		int rectX, rectY, rectW, rectH;

		// Draw the rectangle based on block coordinates, using the screen offset, and
		// scaling up to block coordinates
		rectX = (int) (rect.getX() * Block.BLOCK_SIZE);
		rectY = (int) (rect.getY() * Block.BLOCK_SIZE);
		rectW = (int) (rect.getWidth() * Block.BLOCK_SIZE);
		rectH = (int) (rect.getHeight() * Block.BLOCK_SIZE);

		int r, g, b, rgbInt;
		for (int x = rectX; x < rectX + rectW; x++) {
			for (int y = rectY; y < rectY + rectH; y++) {
				rgbInt = Game.screen.getPixel(Game.screen.getScreenOffsetX() + x, Game.screen.getScreenOffsetY() + Game.screen.getHeight() - (y + 1));
				r = (rgbInt >> 16) & 255;
				g = (rgbInt >> 8) & 255;
				b = rgbInt & 255;
				r *= 1.3;
				g *= 1.3;
				b *= 1.3;
				if (r > 255) r = 255;
				if (g > 255) g = 255;
				if (b > 255) b = 255;
				
				Game.screen.setPixel(Game.screen.getScreenOffsetX() + x,
						Game.screen.getScreenOffsetY() + Game.screen.getHeight() - (y + 1),
						(r << 16) + (g << 8) + b);
			}
		}}

}
