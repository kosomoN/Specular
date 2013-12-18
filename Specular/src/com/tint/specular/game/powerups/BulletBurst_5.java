package com.tint.specular.game.powerups;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class BulletBurst_5 extends PowerUp {

	public BulletBurst_5(float x, float y, GameState gs) {
		super(x, y, gs);
	}
	
	@Override
	protected void affect(Player player) {
		player.setBulletBurst(5);
	}
}
