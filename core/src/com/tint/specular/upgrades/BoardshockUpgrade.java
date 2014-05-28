package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;

public class BoardshockUpgrade extends Upgrade {

	private Texture tex;
	
	public BoardshockUpgrade(GameState gs, int cost) {
		super(gs, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Boardshock.png"));
	}
	
	public BoardshockUpgrade(int grade, int cost) {
		super(grade, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Boardshock.png"));
	}
	
	@Override
	public void refresh() {
		// Specfic to every upgrade
	}
	
	public Texture getTexture() {
		return tex;
	}
}
