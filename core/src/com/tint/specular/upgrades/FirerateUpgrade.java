package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.powerups.FireRateBoost;

public class FirerateUpgrade extends Upgrade {

	private AtlasRegion tex;
	
	public FirerateUpgrade(GameState gs) {
		super(gs, Specular.prefs.getInteger("Firerate Upgrade Grade"), 10);
	}
	
	public FirerateUpgrade(int grade, int maxGrade, TextureAtlas ta) {
		super(null, grade, maxGrade);
		tex = ta.findRegion("game1/FireRate");
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		FireRateBoost.setBoost((float) ((2 / 3f) / (1 + Math.sqrt(getGrade() / getMaxGrade()))));
	}
	
	public AtlasRegion getTexture() {
		return tex;
	}

	@Override
	public String getDescription() {
		return "Increase effect";
	}
}
