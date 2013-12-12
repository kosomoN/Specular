package com.tint.specular.game.powerups;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class BulletBurst_3 extends PowerUp {

	public BulletBurst_3(float x, float y, GameState gs) {
		super(x, y, gs);
	}
	
	public void affect(Player player) {
		if(player.getBulletBurst() != 5) {
			player.setBulletBurst(3);
		}
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
