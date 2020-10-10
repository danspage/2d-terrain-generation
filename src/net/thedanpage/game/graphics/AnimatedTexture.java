package net.thedanpage.game.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.thedanpage.game.framework.Util;

/**
 * An animated game texture, extended from the {@link Texture} class.
 * 
 * @author Dan
 *
 */
public class AnimatedTexture extends Texture {
	
	private static final long serialVersionUID = -5582175552004790148L;

	/**
	 * An array containing pixel data for each frame. The first dimension specifies
	 * the frame number, while the second dimension contains the actual RGB data.
	 */
	protected int[][] frames;

	/**
	 * The width of each frame's texture
	 */
	protected int[] frameWidth;

	/**
	 * The height of each frame's texture
	 */
	protected int[] frameHeight;

	/**
	 * Creates an AnimatedTexture object from an array of strings.
	 * 
	 * @param images a string array consisting of image paths, relative to the
	 *               resources/images folder
	 * @throws IOException if any of the images cannot be found
	 */
	public AnimatedTexture(String[] images) throws Exception {
		super();

		this.frames = new int[images.length][];
		this.frameWidth = new int[images.length];
		this.frameHeight = new int[images.length];

		for (int img = 0; img < images.length; img++) {
			try {

				// Read the image file
				BufferedImage bImg = ImageIO.read(Util.getResourceAsStream("images/" + images[img]));
				int imgWidth = bImg.getWidth();
				int imgHeight = bImg.getHeight();

				// Temporary array for writing pixels to
				int[] imgPx = new int[imgWidth * imgHeight];

				// Read the image pixel by pixel, and transfer it to "imgPx"
				for (int i = 0; i < imgWidth; i++) {
					for (int j = 0; j < imgHeight; j++) {
						int color = bImg.getRGB(i, j);
						int r = (color >> 16) & 0xff;
						int g = (color >> 8) & 0xff;
						int b = color & 0xff;
						imgPx[i + j * imgWidth] = (r << 16) | (g << 8) | b;

						/*
						 * If the pixel is not completely transparent, store its RGB integer value in
						 * "imgPx" (note that any alpha value will be removed)
						 */
						if (((color >> 24) & 0xff) != 0)
							imgPx[i + j * imgWidth] = (r << 16) | (g << 8) | b;
						// But if it is, set that pixel in "imgPx" to -1
						else
							imgPx[i + j * imgWidth] = -1;
					}
				}

				// Transfer the current pixel array to "frames"
				this.frames[img] = imgPx;

				// Transfer the current image width and height to frameWidth and frameHeight
				this.frameWidth[img] = imgWidth;
				this.frameHeight[img] = imgHeight;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * This is an animated texture, you can't get data without specifying a frame!
	 * you're silly
	 */
	@Override
	public int getWidth() {
		return 0;
	}
	@Override
	public int getHeight() {
		return 0;
	}
	@Override
	public int[] getPixels() {
		return null;
	}

	/**
	 * Returns the width of an animated texture for a specific frame.
	 * 
	 * @param frame The frame to be accessed
	 * @return The width of the frame
	 */
	public int getWidth(int frame) {
		if (frame >= 0 && frame < this.frameWidth.length)
			return frameWidth[frame];
		return 0;
	}

	/**
	 * Returns the height of an animated texture for a specific frame.
	 * 
	 * @param frame The frame to be accessed
	 * @return The height of the frame
	 */
	public int getHeight(int frame) {
		if (frame >= 0 && frame < this.frameHeight.length)
			return frameHeight[frame];
		return 0;
	}

	/**
	 * Returns the pixel data for a specific frame.
	 * 
	 * @param frame The frame to be accessed
	 * @return The frame's RGB data
	 */
	public int[] getPixels(int frame) {
		if (frame >= 0 && frame < this.frames.length)
			return frames[frame];
		return null;
	}

	/**
	 * Returns the number of frames for an animated texture.
	 * 
	 * @return Number of frames
	 */
	public int getNumFrames() {
		return this.frames.length;
	}

}
