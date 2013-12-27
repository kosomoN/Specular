package com.tint.specular.game.powerups;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class AddLife extends PowerUp {

	public AddLife(float x, float y, GameState gs) {
		super(x, y, gs);
	}

	@Override
	protected void affect(Player player) {
		player.addLives(1);
	}
}
