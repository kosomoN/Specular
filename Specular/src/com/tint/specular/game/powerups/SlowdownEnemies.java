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
			e.setSpeedUtilization(0.5f);
			e.setTimer(5);
		}
	}
	
	@Override
	public boolean update() {
		for(Player p : gs.getPlayers()) {
			if(Math.pow(getCenterX() - p.getCenterX(), 2) +	Math.pow(getCenterY() - p.getCenterY(), 2)
					< Math.pow(Player.getRadius() + (getCenterX() - getX()), 2)) {
				affect(gs.getEnemies());
				collected = true;
			}
		}
		
		return collected;
	}
}
