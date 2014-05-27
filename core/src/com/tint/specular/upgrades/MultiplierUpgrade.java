package com.tint.specular.upgrades;

import com.tint.specular.game.GameState;

public class MultiplierUpgrade extends Upgrade {

	public MultiplierUpgrade(GameState gs, int cost) {
		super(gs, cost);
	}

	@Override
	public void refresh() {
		// Specific to every update
	}
}
