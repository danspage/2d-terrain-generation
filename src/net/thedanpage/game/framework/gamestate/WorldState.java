package net.thedanpage.game.framework.gamestate;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import net.thedanpage.game.Game;
import net.thedanpage.game.GameEngine;
import net.thedanpage.game.framework.Util;
import net.thedanpage.game.graphics.Font;
import net.thedanpage.game.graphics.Fonts;
import net.thedanpage.game.graphics.Graphics;
import net.thedanpage.game.world.map.Map;
import net.thedanpage.game.world.map.block.Block;
import net.thedanpage.game.world.map.block.BlockFactory;
import net.thedanpage.game.world.map.block.Blocks;

public class WorldState extends BaseGameState {

	private static Map map = new Map();

	/** Used to show an overlay when a new map is being loaded */
	private static boolean currentlyLoading = false;

	@Override
	public void init() {
		Blocks.loadProperties();
		map.init(map.generateSeed());
	}

	@Override
	public void handleEvents() {
		Game.keyboard.update();
	}

	@Override
	public void update() {
		if (!currentlyLoading)
			map.update();
	}

	@Override
	public void render() {
		map.render();

		if (currentlyLoading) {
			Graphics.applyScreenLightness(0.5f);
			Fonts.drawString("Loading...", "doublelargefont", Game.screen.getWidth() / 2, Game.screen.getHeight() / 2,
					0xffff00, Font.ALIGN_CENTER);
		}
	}

	@Override
	public void onKeyPress(int keyCode) {
		net.thedanpage.game.world.input.KeyPressActions.pressKey(keyCode);
	}

	@Override
	public void onLoad() {
	}

	@Override
	public void onExit() {
	}

	@Override
	public void onMousePress(MouseEvent e) {
		// Destroy the block at the mouse position
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (map.getBlockAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y) != null) {
				map.setBlock(null,
						map.getBlockCoordsAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y).x,
						map.getBlockCoordsAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y).y);
			}
		}
		// Place the selected block at the mouse position
		else if (SwingUtilities.isRightMouseButton(e)) {
			Point blockPos = map.getBlockCoordsAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y);
			if (blockPos.x >= 0 && blockPos.x < Map.MAP_SIZE_CHUNKS * Block.BLOCK_SIZE && blockPos.y >= 0
					&& blockPos.y < Map.MAP_HEIGHT) {
				map.setBlock(
						BlockFactory.createBlock(blockPos.x, blockPos.y,
								Blocks.placeBlockList.get(map.currentPlaceBlockIndex)),
						map.getBlockCoordsAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y).x,
						map.getBlockCoordsAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y).y);
			}
		}
	}

	public static Map getMap() {
		return map;
	}

	public static void loadMap(Map m) {
		currentlyLoading = true;
		new Thread() {
			public void run() {
				map = (Map) Util.deepClone(m);
				currentlyLoading = false;
			}
		}.start();
	}
	
	public static void newMap(String seed) {
		currentlyLoading = true;
		new Thread() {
			public void run() {
				map = new Map();
				
				if (seed.length() <= 7 && seed.matches("^[0-9]*$")) {
					map.init(Integer.parseInt(seed));
				}
				else map.init(Map.generateSeed(seed));
				
				currentlyLoading = false;
			}
		}.start();
	}

}
