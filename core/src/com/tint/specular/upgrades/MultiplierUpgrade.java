package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.powerups.ScoreMultiplier;

public class MultiplierUpgrade extends Upgrade {

	private AtlasRegion tex;
	
	public MultiplierUpgrade(GameState gs) {
		super(gs, Specular.prefs.getFloat("Multiplier Upgrade Grade"), 10);
	}
	
	public MultiplierUpgrade(float grade, float maxGrade, TextureAtlas ta) {
		super(null, grade, maxGrade);
		tex = ta.findRegion("game1/Multiplier");
	}
	
	@Override
	public void refresh() {
		// Specific to every update
		ScoreMultiplier.MULTIPLIER = 1.5f + (getGrade() / getMaxGrade() * 0.5f);
	}
	
	public AtlasRegion getTexture() {
		return tex;
	}

	@Override
	public String getDescription() {
		return "improve multiplier duration";
	}
}