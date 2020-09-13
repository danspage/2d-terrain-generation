package net.thedanpage.game.world.map.block;

import java.awt.geom.Rectangle2D;

import net.thedanpage.game.Game;
import net.thedanpage.game.framework.Util;
import net.thedanpage.game.graphics.AnimatedTexture;
import net.thedanpage.game.graphics.Graphics;
import net.thedanpage.game.graphics.Texture;
import net.thedanpage.game.graphics.Textures;

public class Block {

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

	/** Whether the block is a fluid or not. (currently unused) */
	private boolean isFluid = false;

	/** Whether the block is highlighted or not, used for mouseovers */
	private boolean highlighted = false;
	
	Block(int x, int y, String blockName) {
		this.x = x;
		this.y = y;

		this.blockType = blockName;
		this.texture = (String) Blocks.getProperty(blockName, "texture");
		this.isFluid = (Boolean) Blocks.getProperty(blockName, "isFluid");
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
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
	public boolean isHighlighted() {
		return highlighted;
	}

	/** Returns the block's hitbox */
	public Rectangle2D.Double getBounds() {
		return new Rectangle2D.Double(this.x * BLOCK_SIZE, this.y * BLOCK_SIZE, 1, 1);
	}

	public void update() {
		this.highlighted = false;
	}

	/** Draws the block to the screen, accounting for the screen offset */
	public void draw() {
		Graphics.drawImage(this.x*BLOCK_SIZE + Game.screen.getScreenOffsetX(),
				Game.screen.getHeight() - this.y*BLOCK_SIZE - BLOCK_SIZE + Game.screen.getScreenOffsetY(), BLOCK_SIZE,
				BLOCK_SIZE, this.getTexture());
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

}
