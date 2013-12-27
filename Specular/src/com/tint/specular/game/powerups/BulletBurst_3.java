package com.tint.specular.game.powerups;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class BulletBurst_3 extends PowerUp {

	public BulletBurst_3(float x, float y, GameState gs) {
		super(x, y, gs);
	}
	
	@Override
	protected void affect(Player player) {
		if(player.getBulletBurst() != 5) {
			player.setBulletBurst(3);
		}
	}
}
