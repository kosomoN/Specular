package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class BoardshockUpgrade extends Upgrade {

	private Texture tex;
	
	public BoardshockUpgrade(int grade, int maxGrade) {
		super(grade, maxGrade);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/UpgradeIcon.png"));
	}
	
	@Override
	public void refresh() {
		// Specfic to every upgrade
	}
	
	public Texture getTexture() {
		return tex;
	}
}
