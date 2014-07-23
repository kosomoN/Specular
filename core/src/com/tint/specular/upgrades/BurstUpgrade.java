package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.powerups.BulletBurst;

public class BurstUpgrade extends Upgrade {

	private AtlasRegion tex;
	
	public BurstUpgrade(GameState gs) {
		super(gs, Specular.prefs.getInteger("Burst Upgrade Grade"), 10);
	}
	
	public BurstUpgrade(int grade, int maxGrade, TextureAtlas ta) {
		super(null, grade, maxGrade);
		tex = ta.findRegion("game1/5 Burst");
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		BulletBurst.setMaxActiveTime((float) (800 + Math.sqrt(getGrade()) * 50));
	}
	
	public AtlasRegion getTexture() {
		return tex;
	}
}
