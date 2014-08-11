package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.powerups.Ricochet;

public class RicochetUpgrade extends Upgrade {

	private AtlasRegion tex;
	
	public RicochetUpgrade(GameState gs) {
		super(gs, Specular.prefs.getFloat("Ricochet Upgrade Grade"), 10);
	}
	
	public RicochetUpgrade(float grade, float maxGrade, TextureAtlas ta) {
		super(null, grade, maxGrade);
		tex = ta.findRegion("game1/Ricochet");
	}

	@Override
	public void refresh() {
		// Specific for every upgrade
		Ricochet.setAddBounces((int) (1 + Math.floor(2 * (getGrade() / getMaxGrade()))));
	}

	@Override
	public AtlasRegion getTexture() {
		return tex;
	}

	@Override
	public String getDescription() {
		return "Increase the amount of reflections";
	}
}
