package net.thedanpage.game.world.entity;

import java.awt.geom.Rectangle2D;

import net.thedanpage.game.Game;
import net.thedanpage.game.graphics.AnimatedTexture;
import net.thedanpage.game.graphics.Graphics;
import net.thedanpage.game.graphics.Texture;
import net.thedanpage.game.graphics.Textures;
import net.thedanpage.game.world.map.Chunk;
import net.thedanpage.game.world.map.Map;
import net.thedanpage.game.world.map.block.Block;

public class Entity {
	
	/** The direction that the entity is facing in */
	protected static final int FACING_LEFT=0, FACING_RIGHT=1;

	/** The entity's texture */
	protected Texture sprite;

	/** The entity's X position in the world */
	private double x = 0;

	/** The entity's Y position in the world */
	private double y = 0;

	/** The entity's X velocity */
	private double velX = 0;

	/** The entity's Y velocity */
	private double velY = 0;

	/** The entity's hitbox width, in blocks */
	private double width = 0;

	/** The entity's hitbox height, in blocks */
	private double height = 0;

	/** The direction that the entity is facing */
	private int facing = FACING_RIGHT;

	/** Whether the entity is on the ground or not */
	private boolean onGround = false;

	/** Constants used for grouping entities and iterating through them */
	public static final int ENTITY_GROUP_EVERYTHING = 0, ENTITY_GROUP_PLAYERS = 1;

	public Entity(String sprite) {
		this.sprite = Textures.getTexture(sprite);

		if (this.sprite instanceof AnimatedTexture) {
			// Divide by block size to convert from pixels to blocks
			this.width = ((AnimatedTexture) this.sprite).getWidth(0) / Block.BLOCK_SIZE;
			this.height = ((AnimatedTexture) this.sprite).getHeight(0) / Block.BLOCK_SIZE;
		} else {
			this.width = this.sprite.getWidth();
			this.height = this.sprite.getHeight();
		}
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getVelocityX() {
		return this.velX;
	}

	public double getVelocityY() {
		return this.velY;
	}

	public double getWidth() {
		return this.width;
	}

	public double getHeight() {
		return this.height;
	}

	public int getFacing() {
		return this.facing;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setVelocityX(double velX) {
		this.velX = velX;
	}

	public void setVelocityY(double velY) {
		this.velY = velY;
	}

	public void setFacing(int facing) {
		this.facing = facing;
	}

	public Texture getTexture() {
		return this.sprite;
	}

	public boolean onGround() {
		return this.onGround;
	}

	protected void updateOnGround() {
		if (this.getY() == 0)
			this.onGround = true;
		else
			this.onGround = false;
	}

	public void update() {
	}

	/**
	 * Returns the entity's current pixels so they can be drawn to the screen
	 * 
	 * @param flipHorizontal Optionally flip the texture horizontally
	 * @param flipVertical Optionally flip the texture vertically
	 * @return an array of RGB pixel data
	 */
	protected int[] getDrawPixels(boolean flipHorizontal, boolean flipVertical) {
		int[] pixels;

		if (this.sprite instanceof AnimatedTexture)
			pixels = ((AnimatedTexture) (this.sprite)).getPixels(0);
		else
			pixels = this.sprite.getPixels();

		if (flipHorizontal)
			pixels = Graphics.flipHorizontal(this.getTexture().getWidth(), this.getTexture().getHeight(), pixels);
		if (flipVertical)
			pixels = Graphics.flipVertical(this.getTexture().getWidth(), this.getTexture().getHeight(), pixels);

		return pixels;
	}

	/**
	 * Returns the entity's current pixels so they can be drawn to the screen
	 * 
	 * @param frame desired frame of the animated texture
	 * @param flipHorizontal Optionally flip the texture horizontally
	 * @param flipVertical Optionally flip the texture vertically
	 * @return an array of RGB pixel data
	 */
	protected int[] getDrawPixels(int frame, boolean flipHorizontal, boolean flipVertical) throws Exception {
		int[] pixels;

		if (this.sprite instanceof AnimatedTexture) {
			pixels = ((AnimatedTexture) this.sprite).getPixels(frame);

			if (flipHorizontal)
				pixels = Graphics.flipHorizontal(((AnimatedTexture) this.getTexture()).getWidth(frame),
						((AnimatedTexture) this.getTexture()).getHeight(frame), pixels);
			if (flipVertical)
				pixels = Graphics.flipVertical(((AnimatedTexture) this.getTexture()).getWidth(frame),
						((AnimatedTexture) this.getTexture()).getHeight(frame), pixels);
		} else
			throw new Exception("This entity does not have an animated sprite");

		return pixels;
	}

	/**
	 * Returns the entity's hitbox
	 * @return {@link Rectangle2D.Double}
	 */
	public Rectangle2D.Double getBounds() {
		return new Rectangle2D.Double(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}

	/**
	 * Draws the entity to the screen, accounting for its facing direction
	 * 
	 * @throws Exception
	 */
	public void draw() throws Exception {
		try {
			if (this.facing == FACING_LEFT)
				Graphics.drawImage((int) (this.getX() * Block.BLOCK_SIZE) - this.getTexture().getWidth() / 2,
						Game.screen.getHeight() - (int) (this.getY() * Block.BLOCK_SIZE)
								- this.getTexture().getHeight(),
						this.getTexture().getWidth(), this.getTexture().getHeight(), this.getDrawPixels(false, false));
			else
				Graphics.drawImage((int) (this.getX() * Block.BLOCK_SIZE) - this.getTexture().getWidth() / 2,
						Game.screen.getHeight() - (int) (this.getY() * Block.BLOCK_SIZE)
								- this.getTexture().getHeight(),
						this.getTexture().getWidth(), this.getTexture().getHeight(), this.getDrawPixels(true, false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Draws the entity to the screen, depending on its facing direction.
	 * 
	 * @param frame desired frame of the animated texture
	 * 
	 * @throws Exception
	 */
	public void draw(int frame) throws Exception {
		try {
			try {
				if (!(this.sprite instanceof AnimatedTexture))
					throw new Exception("This entity does not have an animated sprite");

				if (this.facing == FACING_LEFT)
					Graphics.drawImage((int) (this.getX()),
							Game.screen.getHeight() - (int) (this.getY() * Block.BLOCK_SIZE)
									- this.getTexture().getHeight(),
							this.getTexture().getWidth(), this.getTexture().getHeight(),
							this.getDrawPixels(frame, false, false));
				else
					Graphics.drawImage((int) (this.getX()),
							Game.screen.getHeight() - (int) (this.getY() * Block.BLOCK_SIZE)
									- this.getTexture().getHeight(),
							this.getTexture().getWidth(), this.getTexture().getHeight(),
							this.getDrawPixels(frame, true, false));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Graphics.drawRectangle(this.getBounds(), 0xffff00, true);
	}

	/** Processes collision of the entity with the world bounds */
	protected void doCollision() {

		// Bottom world bound
		if (this.getY() < 0) {
			this.setY(0);
			this.setVelocityY(0);
		}
		
		// Left world bound
		if (this.getX() < 0) {
			this.setX(0);
			this.setVelocityX(0);
		}
		
		// Right world bound
		if (this.getX() + this.getWidth()/Block.BLOCK_SIZE/2 > Map.MAP_SIZE_CHUNKS*Chunk.CHUNK_WIDTH - 2) {
			this.setX(Map.MAP_SIZE_CHUNKS*Chunk.CHUNK_WIDTH - this.getWidth()/Block.BLOCK_SIZE/2 - 2);
			this.setVelocityX(0);
		}

	}

	/** Updates the entity's position based on its velocity and the delta time */
	protected void updatePosition() {
		double newVelX = this.getVelocityX();
		double newVelY = this.getVelocityY();

		this.setX(this.getX() + newVelX * Game.getDelta());
		this.setY(this.getY() + newVelY * Game.getDelta());

		this.setVelocityX(newVelX);
		this.setVelocityY(newVelY);
	}

}
