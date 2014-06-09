package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.powerups.BulletBurst;

public class BurstUpgrade extends Upgrade {

	private Texture tex;
	
	public BurstUpgrade(GameState gs) {
		super(gs, Specular.prefs.getInteger("Burst Upgrade Grade"), 10);
	}
	
	public BurstUpgrade(int grade, int maxGrade) {
		super(null, grade, maxGrade);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/UpgradeIcon.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		BulletBurst.setMaxActiveTime((float) (800 + Math.sqrt(getGrade()) * 50));
	}
	
	public Texture getTexture() {
		return tex;
	}
}
