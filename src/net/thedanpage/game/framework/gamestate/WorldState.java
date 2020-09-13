package net.thedanpage.game.framework.gamestate;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import net.thedanpage.game.Game;
import net.thedanpage.game.GameEngine;
import net.thedanpage.game.world.map.Map;
import net.thedanpage.game.world.map.block.Block;
import net.thedanpage.game.world.map.block.BlockFactory;
import net.thedanpage.game.world.map.block.Blocks;

public class WorldState extends BaseGameState {

	@Override
	public void init() {
		Blocks.loadProperties();
		Map.init();
	}

	@Override
	public void handleEvents() {
		Game.keyboard.update();
	}

	@Override
	public void update() {
		Map.update();
	}

	@Override
	public void render() {
		Map.render();
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
			if (Map.getBlockAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y) != null) {
				Map.setBlock(null, Map.getBlockCoordsAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y).x,
						Map.getBlockCoordsAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y).y);
			}
		}
		// Place the selected block at the mouse position
		else if (SwingUtilities.isRightMouseButton(e)) {
			Point blockPos = Map.getBlockCoordsAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y);
			if (blockPos.x >= 0 && blockPos.x < Map.MAP_SIZE_CHUNKS*Block.BLOCK_SIZE && blockPos.y >= 0 && blockPos.y < Map.MAP_HEIGHT) {
				Map.setBlock(BlockFactory.createBlock(blockPos.x, blockPos.y, Map.placeBlockList.get(Map.currentPlaceBlockIndex)), Map.getBlockCoordsAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y).x,
						Map.getBlockCoordsAtScreenPos(GameEngine.getMousePos().x, GameEngine.getMousePos().y).y);
			}
		}
	}

}
