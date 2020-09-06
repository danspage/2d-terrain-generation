package net.thedanpage.game.framework.input;

import net.thedanpage.game.world.entity.Entity;
import net.thedanpage.game.world.entity.Player;
import net.thedanpage.game.world.map.Map;

/**
 * This class contains functions that are run once when various keys are pressed down.
 * 
 * @author Dan
 *
 */
public class KeyPressActions {
	
	/**
	 * Makes the player jump in non-flying mode.
	 */
	public static void keyUp() {
		// Cycle through all player entities in the world and make them jump
		for (Entity entity : Map.getEntityGroup(Entity.ENTITY_GROUP_PLAYERS)) {
			Player player = (Player) entity;
			if (!player.getFlying() && player.onGround() && player.getVelocityY() == 0) player.jump();
		}
	}
	
	/**
	 * Toggles whether the player is in flying mode or not.
	 */
	public static void keySpace() {
		// Cycle through all player entities in the world and toggle whether they're in flying mode
		for (Entity entity : Map.getEntityGroup(Entity.ENTITY_GROUP_PLAYERS)) {
			Player player = (Player) entity;
			if (player.getFlying()) player.setFlying(false);
			else player.setFlying(true);
		}
	}

}
