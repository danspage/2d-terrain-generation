package net.thedanpage.game.world.map.block;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import net.thedanpage.game.Game;
import net.thedanpage.game.framework.Util;
import net.thedanpage.game.graphics.AnimatedTexture;
import net.thedanpage.game.graphics.Graphics;
import net.thedanpage.game.graphics.Texture;
import net.thedanpage.game.graphics.Textures;
import net.thedanpage.game.world.entity.Entity;
import net.thedanpage.game.world.map.Map;

public class Block implements Comparable<Block>, Serializable {

	private static final long serialVersionUID = 6448312362433846402L;

	/**
	 * The size of a single block in pixels. Default is 8, but it can be set to a
	 * lower number to scale the terrain down (or a higher number if the texture png
	 * size is increased).
	 */
	public static final int BLOCK_SIZE = 8;

	/** The block's X coordinate */
	private int x;

	/** The block's Y coordinate */
	private int y;

	/** Type of block */
	private String blockType = "null";

	/** Block texture name */
	private String texture = "block_null";

	/** Whether the block is a fluid or not */
	private boolean isFluid = false;
	
	/** Whether the block is a light source or not */
	private boolean isLightSource = false;

	/** Whether the block is highlighted or not, used for mouseovers */
	private boolean highlighted = false;

	/** Stores the last calculation of the distanceToEntity() function */
	private double distToLastEntity;

	/**
	 * How much light will be subtracted when it obtains light values from adjacent
	 * blocks.
	 */
	private float subtractLighting = 0.07f;

	Block(int x, int y, String blockName) {
		this.x = x;
		this.y = y;

		this.blockType = blockName;
		this.texture = (String) Blocks.getProperty(blockName, "texture");
		this.isFluid = (Boolean) Blocks.getProperty(blockName, "isFluid");
		this.isLightSource = (Boolean) Blocks.getProperty(blockName, "isLightSource");
	}

	Texture textureTemp;

	/** Returns the block's texture in an RGB integer array */
	public int[] getTexture() {
		textureTemp = Textures.getBlockTexture(this.texture);
		if (textureTemp instanceof AnimatedTexture) {
			return (int[]) Util.deepClone(((AnimatedTexture) textureTemp)
					.getPixels(Blocks.getBlockAnimTime() % ((AnimatedTexture) textureTemp).getNumFrames()));
		}
		return textureTemp.getPixels();
	}

	public String getBlockType() {
		return blockType;
	}

	public boolean isFluid() {
		return isFluid;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public float getSubtractLighting() {
		return subtractLighting;
	}

	public boolean isLightSource() {
		return isLightSource;
	}

	/** Returns the block's hitbox */
	public Rectangle2D.Double getBounds() {
		return new Rectangle2D.Double(this.x, this.y, 1, 1);
	}

	public void update(Map map) {
		highlighted = false;
	}

	/** Draws the block to the screen, accounting for the screen offset */
	public void draw(Map map) {
		Graphics.drawImage(x * BLOCK_SIZE + Game.screen.getScreenOffsetX(),
				Game.screen.getHeight() - y * BLOCK_SIZE - BLOCK_SIZE + Game.screen.getScreenOffsetY(), BLOCK_SIZE,
				BLOCK_SIZE, Graphics.applyLighting(getTexture(), map.getChunkAtBlock(x).getLightLevel(x, y)));
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public void setDistToEntity(Entity e) {
		distToLastEntity = Math.sqrt(Math.exp((getX() + .5) - (e.getX() + e.getWidth() / 2))
				+ Math.exp((getY() + .5) - (e.getY() + e.getWidth() / 2)));
	}

	public double getDistToLastEntity() {
		return distToLastEntity;
	}

	@Override
	public int compareTo(Block b) {
		// We want to scale the distance up so that it's more precise, since compareTo
		// only accepts ints
		return (int) (b.getDistToLastEntity() - this.getDistToLastEntity()) * 10000;
	}

}
