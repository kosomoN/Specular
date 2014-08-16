package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.powerups.Swarm;

public class SwarmUpgrade extends Upgrade {

	private AtlasRegion tex;
	
	public SwarmUpgrade(GameState gs) {
		super(gs, Specular.prefs.getFloat("Swarm Upgrade Grade"), 10);
	}
	
	public SwarmUpgrade(float grade, float maxGrade, TextureAtlas ta) {
		super(null, grade, maxGrade);
		tex = ta.findRegion("game1/Swarm");
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		Swarm.setEffect((float) (3 * (1 + Math.sqrt(getGrade() / getMaxGrade()))));
	}
	
	public AtlasRegion getTexture() {
		return tex;
	}

	@Override
	public String getDescription() {
		return "improve firerate";
	}
}
