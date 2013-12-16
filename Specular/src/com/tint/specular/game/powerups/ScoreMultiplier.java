package com.tint.specular.game.powerups;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class ScoreMultiplier extends PowerUp {
	
	public ScoreMultiplier(float x, float y, GameState gs) {
		super(x, y, gs);
	}
	
	public void affect(float multiplier) {
		gs.setScoreMultiplier(multiplier);
	}
	
	@Override
	public boolean update() {
		if(Math.pow(getCenterX() - gs.getPlayer().getCenterX(), 2) + Math.pow(getCenterY() - gs.getPlayer().getCenterY(), 2)
				< Math.pow(Player.getRadius() + getRadius(), 2)) {
			affect(2);
			return true;
		}
		return false;
	}
}
