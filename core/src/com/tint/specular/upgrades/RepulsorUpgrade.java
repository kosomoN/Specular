package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.powerups.BulletBurst;

public class RepulsorUpgrade extends Upgrade {

	private Texture tex;
	
	public RepulsorUpgrade(int grade, int maxGrade) {
		super(grade, maxGrade);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/UpgradeIcon.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		BulletBurst.setMaxActiveTime((float) (800 + Math.sqrt(getGrade() * 100)));
	}
	
	public Texture getTexture() {
		return tex;
	}
}
