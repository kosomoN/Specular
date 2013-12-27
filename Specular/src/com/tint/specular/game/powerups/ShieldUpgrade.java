package com.tint.specular.game.powerups;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class ShieldUpgrade extends PowerUp {
	
	public ShieldUpgrade(float x, float y, GameState gs) {
		super(x, y, gs);
	}

	@Override
	protected void affect(Player p) {
		p.addShield();
	}
}
