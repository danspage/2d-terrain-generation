package net.thedanpage.game.framework.gamestate;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import net.thedanpage.game.Game;
import net.thedanpage.game.GameEngine;
import net.thedanpage.game.GameEngine.STATE;
import net.thedanpage.game.framework.Sounds;
import net.thedanpage.game.graphics.Font;
import net.thedanpage.game.graphics.Fonts;
import net.thedanpage.game.graphics.Graphics;
import net.thedanpage.game.graphics.Textures;

public class TitleMenuState extends BaseGameState {
	
	private static int moveBG = 0;

	@Override
	public void init() {
	}

	@Override
	public void handleEvents() {
	}

	@Override
	public void update() {
		if (Game.getTicks() % 5 == 0) moveBG ++;
	}

	@Override
	public void render() {
		Graphics.drawTextureTiled(moveBG, moveBG, Textures.getTexture("titlebg"));
		
		Fonts.drawString("Press enter to begin.", "doublelargefont", Game.WIDTH/2, Game.HEIGHT/2 - 7, 0xffff00, Font.ALIGN_CENTER);
		Fonts.drawString("(Press ESC to close the game)", "tinyfont", Game.WIDTH/2, Game.HEIGHT/2 + 20, 0xffbb00, Font.ALIGN_CENTER);
	}

	@Override
	public void onKeyPress(int keyCode) {
		if (keyCode == KeyEvent.VK_ENTER) GameEngine.changeState(STATE.WORLD);
		
		else if (keyCode == KeyEvent.VK_ESCAPE) GameEngine.quit();
	}

	@Override
	public void onLoad() {
		Sounds.play("menu_music", 0.4, true);
	}

	@Override
	public void onExit() {
		Sounds.stop("menu_music");
	}
	
	@Override
	public void onMousePress(MouseEvent e) {
	}

	
	
}
