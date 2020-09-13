package net.thedanpage.game.framework.gamestate;

import java.awt.event.MouseEvent;

import net.thedanpage.game.GameEngine;

/**
 * A game state class, derived from
 * <a href="http://gamedevgeek.com/tutorials/managing-game-states-in-c/">Game
 * Dev Geek</a>
 * 
 * @author Dan
 *
 */
public abstract class BaseGameState {

	public abstract void init();
	
	public abstract void handleEvents();
	
	public abstract void update();
	
	public abstract void render();
	
	public abstract void onKeyPress(int keyCode);
	
	public abstract void onMousePress(MouseEvent e);
	
	public abstract void onLoad();
	
	public abstract void onExit();
	
	public void changeState(GameEngine.STATE state) {
		GameEngine.changeState(state);
	}

}
