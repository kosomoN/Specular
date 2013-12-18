package com.tint.specular.game.powerups;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;

public class SlowdownEnemies extends PowerUp {

	public SlowdownEnemies(float x, float y, GameState gs) {
		super(x, y, gs);
	}
	
	@Override
	protected void affect(Player p) {
		for(Enemy e : gs.getEnemies()) {
			e.setSlowdown(0.5f);
		}
	}
}
