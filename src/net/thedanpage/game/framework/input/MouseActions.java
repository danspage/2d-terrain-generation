package net.thedanpage.game.framework.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.thedanpage.game.GameEngine;

public class MouseActions extends MouseAdapter {
	
	public void mouseClicked(MouseEvent e) {
		GameEngine.getState().onMousePress(e);
	}
	
}
