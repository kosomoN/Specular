package com.tint.specular.game.powerups;

import com.badlogic.gdx.utils.Array;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;

public class SlowdownEnemies extends PowerUp {

	public SlowdownEnemies(float x, float y, GameState gs) {
		super(x, y, gs);
	}

	public void affect(Array<Enemy> enemies) {
		for(Enemy e : enemies) {
			e.setSlowdown(0.5f);
		}
	}
	
	@Override
	public boolean update() {
		for(Player p : gs.getPlayers()) {
			if(Math.pow(getCenterX() - p.getCenterX(), 2) +	Math.pow(getCenterY() - p.getCenterY(), 2)
					< Math.pow(Player.getRadius() + getRadius(), 2)) {
				affect(gs.getEnemies());
				return true;
			}
		}
		return false;
	}
}
