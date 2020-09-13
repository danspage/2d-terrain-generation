package net.thedanpage.game.graphics;

import net.thedanpage.game.GameEngine;

/**
 * A class with functions used to draw to the game's canvas
 * 
 * @author Dan
 *
 */
public class Screen {

	private int width, height;

	/** The screen's RGB pixel data */
	public int pixels[];

	/** The X offset of the screen, used for relative rendering of objects */
	private int screenOffsetX = 0;
	/** The Y offset of the screen, used for relative rendering of objects */
	private int screenOffsetY = 0;

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}

	/** Clears the screen's pixels by setting them all to black */
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}

	public void render() {
		GameEngine.render();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/** Sets a pixel's color using an RGB integer */
	public void setPixel(int x, int y, int color) {
		if (x < 0 || x >= width)
			return;
		if (y < 0 || y >= height)
			return;
		pixels[x + y * width] = color;
	}
	
	public int getPixel(int x, int y) {
		if (x < 0 || x >= width)
			return -1;
		if (y < 0 || y >= height)
			return -1;
		return pixels[x + y * width];
	}

	/** Fills the background with a color, using an RGB integer */
	public void fillBG(int color) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}

	public int getScreenOffsetX() {
		return this.screenOffsetX;
	}

	public void setScreenOffsetX(int screenOffset) {
		this.screenOffsetX = screenOffset;
	}

	public int getScreenOffsetY() {
		return screenOffsetY;
	}

	public void setScreenOffsetY(int screenOffsetY) {
		this.screenOffsetY = screenOffsetY;
	}

}
