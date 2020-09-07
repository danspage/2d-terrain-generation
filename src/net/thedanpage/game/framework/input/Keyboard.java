package net.thedanpage.game.framework.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
	 * Used to check if the left or the right arrow key was pressed more recently
	 */
	public boolean leftLast = false;
	
	/**
	 * Literally self explanatory<br>
	 * <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Macaca_sinica_-_01.jpg/220px-Macaca_sinica_-_01.jpg">
	 */
	public boolean upPressed = false;
	
	/**
	 * Updates key variables to tell whether certain keys are pressed or not
	 */
	public void update() {
		up = keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_DOWN];
		left = keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_RIGHT];
		
		if (left) leftLast = true;
		else if (right) leftLast = false;
		
		if (up) upPressed = true;
		else upPressed = false;
	}

	/**
	 * Actions that only run once when a key is pressed down. Key actions in this function will point to {@link KeyPressActions}.
	 * This function also sets key variables to the pressed state.
	 */
	public void keyPressed(KeyEvent event) {
		
		if (!keys[event.getKeyCode()]) {
			if (event.getKeyCode() == KeyEvent.VK_UP) KeyPressActions.keyUp();
			
			if (event.getKeyCode() == KeyEvent.VK_SPACE) KeyPressActions.keySpace();
		}
		
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

}
