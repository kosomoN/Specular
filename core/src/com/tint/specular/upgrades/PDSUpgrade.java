package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;

public class PDSUpgrade extends Upgrade {

	private Texture tex;
	
	public PDSUpgrade(GameState gs, int cost) {
		super(gs, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/PDS.png"));
	}
	
	public PDSUpgrade(int grade, int cost) {
		super(grade, cost);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/PDS.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
	}
	
	public Texture getTexture() {
		return tex;
	}
}