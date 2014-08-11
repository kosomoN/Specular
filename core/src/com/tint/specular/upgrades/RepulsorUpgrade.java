package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.powerups.Repulsor;

public class RepulsorUpgrade extends Upgrade {

	private AtlasRegion tex;
	
	public RepulsorUpgrade(GameState gs) {
		super(gs, Specular.prefs.getFloat("Repulsor Upgrade Grade"), 10);
	}
	
	public RepulsorUpgrade(float grade, float maxGrade, TextureAtlas ta) {
		super(null, grade, maxGrade);
		tex = ta.findRegion("game1/Repulsor");
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		Repulsor.setMaxActiveTime((float) (800 + Math.sqrt(getGrade() * 100)));
	}
	
	public AtlasRegion getTexture() {
		return tex;
	}

	@Override
	public String getDescription() {
		return "Increase duration";
	}
}
