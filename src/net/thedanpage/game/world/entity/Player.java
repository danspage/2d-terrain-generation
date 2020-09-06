package net.thedanpage.game.world.entity;

import net.thedanpage.game.Game;
import net.thedanpage.game.framework.Constants;
import net.thedanpage.game.framework.Sounds;
import net.thedanpage.game.framework.Util;
import net.thedanpage.game.graphics.AnimatedTexture;
import net.thedanpage.game.graphics.Fonts;
import net.thedanpage.game.graphics.Graphics;
import net.thedanpage.game.world.map.block.Block;

public class Player extends Entity {
	
	/** How much velocity will be added to the player upon jumping */
	private static final double JUMP_STRENGTH = 0.5;
	
	/** The player's walking velocity */
	private static final double WALK_SPEED = 0.25;
	
	/** The player's flying velocity */
	private static final double FLY_SPEED = 0.4;
	
	/** An animation counter, used for the animated texture */
	private int animationCounter = 0;
	
	/** The amount of time the player has been walking for */
	private int walkingTime = 0;
	
	/** Controls whether the player is flying or not */
	private boolean flying = false;

	public Player() {
		super("player");
	}
	
	/** Makes the player jump, and plays the jumping sound */
	public void jump() {
		this.setVelocityY(JUMP_STRENGTH);
		Sounds.play("jump");
	}
	
	@Override
	public AnimatedTexture getTexture() {
		return (AnimatedTexture)this.sprite;
	}
	
	@Override
	public void update() {
		// Movement based on keys and whether the player is flying
		if (this.flying) {
			if (Game.keyboard.left) this.setVelocityX(-FLY_SPEED);
			if (Game.keyboard.right) this.setVelocityX(FLY_SPEED);
			if (Game.keyboard.up) this.setVelocityY(FLY_SPEED*2/3);
			if (Game.keyboard.down) this.setVelocityY(-FLY_SPEED*2/3);
			if (!(Game.keyboard.up || Game.keyboard.down)) this.setVelocityY(0);
		}
		else {
			if (Game.keyboard.left) this.setVelocityX(-WALK_SPEED);
			if (Game.keyboard.right) this.setVelocityX(WALK_SPEED);
			this.setVelocityY(this.getVelocityY() - Constants.GRAVITY*Game.getDelta());
		}
		
		// Set X velocity to zero when none of the left or right keys are pressed
		if (!(Game.keyboard.left || Game.keyboard.right)) this.setVelocityX(0);
		
		// Walking time
		if ((Game.keyboard.left || Game.keyboard.right) &&
				((this.getVelocityX() != 0.0 && this.onGround()) || this.flying)) {
			walkingTime ++;
		} else {
			walkingTime = 0;
		}
		
		// Facing direction
		if (this.getVelocityX() < 0) this.setFacing(Constants.FACING_LEFT);
		else if (this.getVelocityX() > 0) this.setFacing(Constants.FACING_RIGHT);
		
		// Footstep sound
		if (!this.flying && this.walkingTime != 0 && this.walkingTime % 19 == 0) {
			Sounds.play("footsteps", 0.13);
		}
		
		// Update onGround
		this.updateOnGround();
		
		// Update position
		this.updatePosition();
		
		// Collision
		this.doCollision();
		
		
		
		// Walking animation
		if (Game.keyboard.left || Game.keyboard.right) {
			animationCounter --;
			if (animationCounter < 0) animationCounter = 12*4;
		}
		
		if (!(this.onGround() || this.flying))
			this.animationCounter = 13*4;
		else if (!(Game.keyboard.left || Game.keyboard.right)) {
			this.animationCounter = 0;
		}
	}
	
	/** Draw the player on the screen */
	@Override
	public void draw() {
		try {
			if (this.getFacing() == Constants.FACING_LEFT)
				Graphics.drawImage((int)(this.getX()*Block.BLOCK_SIZE)+Game.screen.getScreenOffsetX(), Game.screen.getHeight()-(int)(this.getY()*Block.BLOCK_SIZE)-this.getTexture().getHeight(animationCounter/4)+Game.screen.getScreenOffsetY(), this.getTexture().getWidth(animationCounter/4), this.getTexture().getHeight(animationCounter/4), this.getDrawPixels(animationCounter/4, false, false));
			else
				Graphics.drawImage((int)(this.getX()*Block.BLOCK_SIZE)+Game.screen.getScreenOffsetX(), Game.screen.getHeight()-(int)(this.getY()*Block.BLOCK_SIZE)-this.getTexture().getHeight(animationCounter/4)+Game.screen.getScreenOffsetY(), this.getTexture().getWidth(animationCounter/4), this.getTexture().getHeight(animationCounter/4), this.getDrawPixels(animationCounter/4, true, false));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Fonts.drawString("Player X: " + Util.formatDoubleForString(this.getX()), "tinyfont", 2, 29, 0xffff00);
		Fonts.drawString("Player Y: " + Util.formatDoubleForString(this.getY()), "tinyfont", 2, 35, 0xffff00);
	}

	public boolean getFlying() {
		return flying;
	}

	public void setFlying(boolean flying) {
		this.flying = flying;
	}

}
