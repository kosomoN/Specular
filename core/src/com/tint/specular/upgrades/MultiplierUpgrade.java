package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;

public class MultiplierUpgrade extends Upgrade {

	private Texture tex;
	
	public MultiplierUpgrade(GameState gs, int cost) {
		super(gs, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Multiplier.png"));
	}
	
	public MultiplierUpgrade(int grade, int cost) {
		super(grade, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Multiplier.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every update
	}
	
	public Texture getTexture() {
		return tex;
	}
}
