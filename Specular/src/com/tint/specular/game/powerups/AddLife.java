package com.tint.specular.game.powerups;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class AddLife extends PowerUp {

	public AddLife(float x, float y, GameState gs) {
		super(x, y, gs);
	}

	public void affect(Player player) {
		player.addLives(1);
	}

	@Override
	public boolean update() {
		for(Player p : gs.getPlayers()) {
			if(Math.pow(getCenterX() - p.getCenterX(), 2) +	Math.pow(getCenterY() - p.getCenterY(), 2)
					< Math.pow(Player.getRadius() + (getCenterX() - getX()), 2)) {
				affect(p);
				collected = true;
			}
		}
		
		return collected;
	}
	
	
}
