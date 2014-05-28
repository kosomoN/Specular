package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;

public class BeamUpgrade extends Upgrade {

	private Texture tex;
	
	public BeamUpgrade(GameState gs, int cost) {
		super(gs, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Beam.png"));
	}
	
	public BeamUpgrade(int grade, int cost) {
		super(grade, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Beam.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
	}
	
	public Texture getTexture() {
		return tex;
	}
}
