package com.tint.specular.game.powerups;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class ComboDamageBooster extends PowerUp {

	public ComboDamageBooster(float x, float y, GameState gs) {
		super(x, y, gs);
	}

	@Override
	protected void affect(Player p) {
		gs.setDamageBooster(1.5f);
	}
}
