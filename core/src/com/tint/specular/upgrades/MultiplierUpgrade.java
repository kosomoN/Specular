package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;

public class MultiplierUpgrade extends Upgrade {

	private AtlasRegion tex;
	
	public MultiplierUpgrade(GameState gs) {
		super(gs, Specular.prefs.getInteger("Multiplier Upgrade Grade"), 10);
	}
	
	public MultiplierUpgrade(int grade, int maxGrade, TextureAtlas ta) {
		super(null, grade, maxGrade);
		tex = ta.findRegion("game1/Multiplier");
	}
	
	@Override
	public void refresh() {
		// Specific to every update
		GameState.MULTIPLIER_COOLDOWN_TIME = (int) (360 * (1 + Math.sqrt(getGrade()) / getMaxGrade() * 0.2f));
	}
	
	public AtlasRegion getTexture() {
		return tex;
	}

	@Override
	public String getDescription() {
		return "Decrease multiplier cooldown";
	}
}
