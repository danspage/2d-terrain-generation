package net.thedanpage.game.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.thedanpage.game.framework.Util;

public class Texture {
	
	private int width;
	private int height;
	private int[] pixels;
	
	public Texture() {
	}
	
	/**
	 * @param	image	a file path relative to the resources/images folder
	 * 
	 * @throws	IOException if the image cannot be found
	 */
	public Texture(String image) {
		try {
			BufferedImage bImg = ImageIO.read(Util.getResourceAsStream("images/" + image));
			int imgWidth = bImg.getWidth();
			int imgHeight = bImg.getHeight();
			
			this.width = imgWidth;
			this.height = imgHeight;
			
			int[] imgPx = new int[imgWidth*imgHeight];
			
			for (int i=0; i<imgWidth; i++) {
				for (int j=0; j<imgHeight; j++) {
					int color = bImg.getRGB(i, j);
	                int r = (color>>16)&0xff; 
	                int g = (color>>8)&0xff; 
	                int b = color&0xff; 
	                imgPx[i + j*imgWidth] = (r<<16) | (g<<8) | b;
	                
	                if (((color>>24)&0xff) != 0) imgPx[i + j*imgWidth] = (r<<16) | (g<<8) | b;
	                else imgPx[i + j*imgWidth] = -1;
				}
			}
			
			this.pixels = imgPx;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets pixel data for the texture.
	 * @param	x		an x coordinate relative to the texture
	 * @param	y		a y coordinate relative to the texture
	 * @param	color	an RBG color in integer format
	 */
	public void setPixel(int x, int y, int color) {
		this.pixels[x + y*this.width] = color;
	}
	
	/**
	 * @return	the width of the texture
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * @return	the height of the texture
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * @return	an array of pixels containing RBG integers
	 */
	public int[] getPixels() {
		return this.pixels;
	}
	
}
