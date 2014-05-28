package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;


public class FirerateUpgrade extends Upgrade {

	private Texture tex;
	
	public FirerateUpgrade(GameState gs, int cost) {
		super(gs, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Firerate.png"));
	}
	
	public FirerateUpgrade(int grade, int cost) {
		super(grade, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Firerate.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
	}
	
	public Texture getTexture() {
		return tex;
	}
}
