package net.thedanpage.game.world.map;

import net.thedanpage.game.Game;
import net.thedanpage.game.graphics.AnimatedTexture;
import net.thedanpage.game.graphics.Graphics;
import net.thedanpage.game.graphics.Textures;

public class Sky {

	private static int sunFloatAnimationState = 0;
	private static int sunTextureState = 1;

	/**
	 * Fill the screen with a sky blue color
	 */
	private static void fillSky() {
		Game.screen.fillBG(0x00bfff);
	}

	/**
	 * Draw the sun with an offset on the screen, in different Y positions based on
	 * its floating animation state. The screen offsets are divided so that the sun
	 * moves more slowly than the world going by, creating the effect of it being
	 * off in the distance.
	 */
	private static void drawSun() {
		if (sunFloatAnimationState == 0 || sunFloatAnimationState == 2) {
			Graphics.drawImage(Game.screen.getScreenOffsetX() / 10 + 180,
					Game.screen.getScreenOffsetY() / 6 + Game.screen.getHeight() - 120, 25, 25,
					((AnimatedTexture) Textures.getTexture("sun")).getPixels(sunTextureState));
		} else if (sunFloatAnimationState == 1) {
			Graphics.drawImage(Game.screen.getScreenOffsetX() / 10 + 180,
					Game.screen.getScreenOffsetY() / 6 + Game.screen.getHeight() - 121, 25, 25,
					((AnimatedTexture) Textures.getTexture("sun")).getPixels(sunTextureState));
		} else {
			Graphics.drawImage(Game.screen.getScreenOffsetX() / 10 + 180,
					Game.screen.getScreenOffsetY() / 6 + Game.screen.getHeight() - 119, 25, 25,
					((AnimatedTexture) Textures.getTexture("sun")).getPixels(sunTextureState));
		}
	}

	public static void update() {
		// Progress the sun floating state every 20 game updates
		if (Game.getTicks() % 20 == 0) {
			sunFloatAnimationState++;
			if (sunFloatAnimationState > 3)
				sunFloatAnimationState = 0;
		}

		// Progress the sun texture state every 6 game updates
		if (Game.getTicks() % 6 == 0) {
			sunTextureState++;
			if (sunTextureState > 3)
				sunTextureState = 0;
		}
	}

	public static void render() {
		fillSky();
		drawSun();
	}

}
