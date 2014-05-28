package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;

public class SwarmUpgrade extends Upgrade {

	private Texture tex;
	
	public SwarmUpgrade(GameState gs, int cost) {
		super(gs, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Swarm.png"));
	}
	
	public SwarmUpgrade(int grade, int cost) {
		super(grade, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Swarm.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
	}
	
	public Texture getTexture() {
		return tex;
	}
}
