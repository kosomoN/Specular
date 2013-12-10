package com.tint.specular.game.powerups;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class BulletBurst_5 extends PowerUp {

	public BulletBurst_5(float x, float y, GameState gs) {
		super(x, y, gs);
	}

	public void affect(Player player) {
		player.setBulletBurst(5);
		player.setTimer(player.getBulletTimer(), 5000);
	}
	
	@Override
	public boolean update() {
		for(Player p : gs.getPlayers()) {
			if(Math.pow(getCenterX() - p.getCenterX(), 2) +	Math.pow(getCenterY() - p.getCenterY(), 2)
					< Math.pow(Player.getRadius() + (getCenterX() - getX()), 2)) {
				affect(p);
				return true;
			}
		}

		return false;
	}
}
