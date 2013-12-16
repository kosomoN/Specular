package com.tint.specular.game.powerups;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class BulletBurst_5 extends PowerUp {

	public BulletBurst_5(float x, float y, GameState gs) {
		super(x, y, gs);
	}

	public void affect(Player player) {
		player.setBulletBurst(5);
	}
	
	@Override
	public boolean update() {
//		for(Player p : gs.getPlayers()) {
			if(Math.pow(getCenterX() - gs.getPlayer().getCenterX(), 2) +	Math.pow(getCenterY() - gs.getPlayer().getCenterY(), 2)
					< Math.pow(Player.getRadius() + getRadius(), 2)) {
				affect(gs.getPlayer());
				return true;
			}
//		}
		return false;
	}
}
