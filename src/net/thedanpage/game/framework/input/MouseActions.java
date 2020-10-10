package net.thedanpage.game.framework.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import net.thedanpage.game.GameEngine;

public class MouseActions implements MouseListener {
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		GameEngine.getState().onMousePress(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
}
