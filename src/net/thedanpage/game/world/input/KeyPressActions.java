package net.thedanpage.game.world.input;

import java.awt.event.KeyEvent;

import net.thedanpage.game.GameEngine;
import net.thedanpage.game.GameEngine.STATE;
import net.thedanpage.game.world.entity.Entity;
import net.thedanpage.game.world.entity.Player;
import net.thedanpage.game.world.map.Map;

/**
 * This class contains functions that are run once when various keys are pressed
 * down.
 * 
 * @author Dan
 *
 */
public class KeyPressActions {

	public static void pressKey(int keyCode) {

		// Makes the player jump in non-flying mode
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			// Cycle through all player entities in the world and make them jump
			for (Entity entity : Map.getEntityGroup(Entity.ENTITY_GROUP_PLAYERS)) {
				Player player = (Player) entity;
				if (!player.getFlying() && player.onGround() && player.getVelocityY() == 0)
					player.jump();
			}
		}

		// Toggles whether the player is in flying mode or not
		else if (keyCode == KeyEvent.VK_SPACE) {
			// Cycle through all player entities in the world and toggle whether they're in
			// flying mode
			for (Entity entity : Map.getEntityGroup(Entity.ENTITY_GROUP_PLAYERS)) {
				Player player = (Player) entity;
				if (player.getFlying())
					player.setFlying(false);
				else
					player.setFlying(true);
			}
		}
		
		// Toggles whether hitboxes are showing or not
		else if (keyCode == KeyEvent.VK_H) {
			if (Map.isShowingHitboxes()) Map.setShowingHitboxes(false);
			else Map.setShowingHitboxes(true);
		}
		
		// Quits the game if the escape key is pressed
		else if (keyCode == KeyEvent.VK_ESCAPE) {
			GameEngine.changeState(STATE.TITLE_MENU);
		}
		
		// Cycle the selected block
		else if (keyCode == KeyEvent.VK_U) {
			Map.cyclePlaceBlockIndexBack();
		}
		else if (keyCode == KeyEvent.VK_I) {
			Map.cyclePlaceBlockIndexForward();
		}

	}

}
