package com.tint.specular.upgrades;

import com.tint.specular.game.GameState;

public class BoardshockUpgrade extends Upgrade {

	public BoardshockUpgrade(GameState gs, int cost) {
		super(gs, cost);
	}

	@Override
	public void refresh() {
		// Specfic to every upgrade
	}
}
