package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;

public class BurstUpgrade extends Upgrade {

	private Texture tex;
	
	public BurstUpgrade(GameState gs, int cost) {
		super(gs, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Burst.png"));
	}
	
	public BurstUpgrade(int grade, int cost) {
		super(grade, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Burst.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
	}
	
	public Texture getTexture() {
		return tex;
	}
}
