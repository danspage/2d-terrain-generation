package net.thedanpage.game;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.thedanpage.game.framework.Sounds;
import net.thedanpage.game.framework.Util;
import net.thedanpage.game.framework.gamestate.BaseGameState;
import net.thedanpage.game.framework.gamestate.TitleMenuState;
import net.thedanpage.game.framework.gamestate.WorldState;
import net.thedanpage.game.graphics.Fonts;
import net.thedanpage.game.graphics.Textures;
import net.thedanpage.game.world.map.block.Blocks;

/**
 * A game engine class derived from
 * <a href="http://gamedevgeek.com/tutorials/managing-game-states-in-c/">Game
 * Dev Geek</a>
 * 
 * @author Dan
 *
 */
public class GameEngine {

	/** Controls whether the game is running or not */
	private static boolean running;

	/** A list of game states */
	public static enum STATE {
		TITLE_MENU, WORLD
	};

	/** The current state of the game */
	private static STATE currentState;

	/**
	 * A HashMap containing all of the game states. Each function in
	 * {@link GameEngine} that updates a game state points to this.
	 */
	private static Map<STATE, BaseGameState> states = new HashMap<STATE, BaseGameState>();
	
	/** Returns the mouse position on screen, relative to the game window */
	public static Point getMousePos() {
		return new Point(
				(MouseInfo.getPointerInfo().getLocation().x-Game.game.getLocationOnScreen().x)/Game.SCALE,
				(MouseInfo.getPointerInfo().getLocation().y-Game.game.getLocationOnScreen().y)/Game.SCALE
		);
	}

	/**
	 * Runs setup for the game engine
	 */
	public static void init() {
		// Global initialization
		Util.init();
		Blocks.loadProperties();
		Textures.init();
		Sounds.init();
		Fonts.init();
		
		// Add each game state
		states.put(STATE.WORLD, new WorldState());
		states.put(STATE.TITLE_MENU, new TitleMenuState());

		// Initialize each game state
		for (Entry<STATE, BaseGameState> state : states.entrySet()) {
			state.getValue().init();
		}

		changeState(STATE.TITLE_MENU);
	}

	public static void changeState(STATE state) {
		if (currentState != null) states.get(currentState).onExit();
		
		currentState = state;
		
		// We want a new world each time
		if (state == STATE.WORLD) states.get(STATE.WORLD).init();
		
		states.get(currentState).onLoad();
	}

	public static void handleEvents() {
		states.get(currentState).handleEvents();
	}
	
	public static void onKeyPress(int keyCode) {
		states.get(currentState).onKeyPress(keyCode);
	}

	public static void update() {
		states.get(currentState).update();
	}

	public static void render() {
		states.get(currentState).render();
	}

	public static boolean running() {
		return running;
	}

	public static void quit() {
		running = false;
		Game.quit();
	}
	
	public static BaseGameState getState() {
		return states.get(currentState);
	}

}
