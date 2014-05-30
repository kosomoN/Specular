package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class MultiplierUpgrade extends Upgrade {

	private Texture tex;
	
	public MultiplierUpgrade(int grade, int maxGrade) {
		super(grade, maxGrade);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/UpgradeIcon.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every update
	}
	
	public Texture getTexture() {
		return tex;
	}
}
