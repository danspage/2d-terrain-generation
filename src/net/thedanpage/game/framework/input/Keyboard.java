package net.thedanpage.game.framework.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.thedanpage.game.GameEngine;

/**
 * The game's key listener class, used to process {@link KeyEvent} inputs.
 * 
 * @author Dan
 *
 */
public class Keyboard implements KeyListener {
	
	// Size of 65535 to potentially allow for all key events
	private boolean keys[] = new boolean[65535];

	
	public boolean up, down, left, right;
	
	/**
	 * Updates key variables to tell whether certain keys are pressed or not
	 */
	public void update() {
		if (keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W]) up = true;
		else up = false;
		if (keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S]) down = true;
		else down = false;
		if (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A]) left = true;
		else left = false;
		if (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) right = true;
		else right = false;
	}

	/**
	 * Actions that only run once when a key is pressed down. Key actions in this function will point to {@link KeyPressActions}.
	 * This function also sets key variables to the pressed state.
	 */
	public void keyPressed(KeyEvent event) {
		
		GameEngine.onKeyPress(event.getKeyCode());
		
		keys[event.getKeyCode()] = true;
	}

	/**
	 * Sets key variables to the unpressed state
	 */
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	// Unused, but has to be included as an implementation of KeyListener
	public void keyTyped(KeyEvent e) {
	}
	
	/**
	 * Releases all keys. This is triggered when the window loses focus.
	 */
	public void releaseAll() {
		for (int i=0; i<keys.length; i++) {
			keys[i] = false;
		}
	}

}
