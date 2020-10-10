package net.thedanpage.game.world.physics.collision;

import java.util.ArrayList;

import net.thedanpage.game.world.entity.Entity;
import net.thedanpage.game.world.map.Map;
import net.thedanpage.game.world.map.block.Block;
import net.thedanpage.game.world.map.block.Blocks;

public class CollideBlocks {

	private static ArrayList<Block> relevantBlocks;

	public static void collideBlocks(Map map, Entity entity) {

		relevantBlocks = new ArrayList<Block>();

		for (int x = (int) entity.getX(); x <= (int) (entity.getX() + entity.getWidth()); x++) {
			for (int y = (int) entity.getY(); y <= (int) (entity.getY() + entity.getHeight()); y++) {

				if (map.getBlock(x, y) != null)
					relevantBlocks.add(map.getBlock(x, y));

			}
		}

		Blocks.sortByDistanceToPlayer(relevantBlocks);

		for (int i=0; i<relevantBlocks.size(); i++) {
			collideBlock(map, entity, relevantBlocks.get(i));
		}
	}

	private static void collideBlock(Map map, Entity entity, Block block) {

		if (block != null && !block.isFluid() && entity.getBounds().intersects(block.getBounds())) {

			double player_bottom = entity.getY() + entity.getHeight();
			double tiles_bottom = block.getY() + 1;
			double player_right = entity.getX() + entity.getWidth();
			double tiles_right = block.getX() + 1;

			double b_collision = tiles_bottom - entity.getY();
			double t_collision = player_bottom - block.getY();
			double l_collision = player_right - block.getX();
			double r_collision = tiles_right - entity.getX();

			if (t_collision < b_collision && t_collision < l_collision && t_collision < r_collision) {
				// Top collision (of player)
				entity.setY(block.getY() - entity.getHeight());
				entity.setVelocityY(0);
			}
			if (b_collision < t_collision && b_collision < l_collision && b_collision < r_collision) {
				// Bottom collision (of player)
				entity.setY(block.getY() + 1);
				entity.setVelocityY(0);
			}
			if (l_collision < r_collision && l_collision < t_collision && l_collision < b_collision) {
				// Right collision (of player)
				entity.setX(block.getX() - entity.getWidth());
				entity.setVelocityX(0);
			}
			if (r_collision < l_collision && r_collision < t_collision && r_collision < b_collision) {
				// Left collision (of player)
				entity.setX(block.getX() + 1);
				entity.setVelocityX(0);
			}

		}

		entity.updatePrevPos();
	}

}
