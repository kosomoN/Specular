package com.tint.specular.upgrades;

import com.tint.specular.game.GameState;

public class SwarmUpgrade extends Upgrade {

	public SwarmUpgrade(GameState gs, int cost) {
		super(gs, cost);
	}

	@Override
	public void refresh() {
		// Specific to every upgrade
	}
}
