package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.powerups.FireRateBoost;


public class FirerateUpgrade extends Upgrade {

	private Texture tex;
	
	public FirerateUpgrade(int grade, int maxGrade) {
		super(grade, maxGrade);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/UpgradeIcon.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		FireRateBoost.setBoost((float) ((2 / 3f) / (1 + Math.sqrt(getGrade() / getMaxGrade()))));
	}
	
	public Texture getTexture() {
		return tex;
	}
}
