package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.PDS;

public class PDSUpgrade extends Upgrade {

	private AtlasRegion tex;
	
	public PDSUpgrade(GameState gs) {
		super(gs, Specular.prefs.getFloat("PDS Upgrade Grade"), 10);
	}
	
	public PDSUpgrade(float grade, float maxGrade, TextureAtlas ta) {
		super(null, grade, maxGrade);
		tex = ta.findRegion("game1/Point Defense");
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		PDS.setDamage((float) (2 + Math.sqrt(getGrade())));
	}
	
	public AtlasRegion getTexture() {
		return tex;
	}

	@Override
	public String getDescription() {
		return "improve defense capacity";
	}
}