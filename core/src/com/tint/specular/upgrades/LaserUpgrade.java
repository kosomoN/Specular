package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;

public class LaserUpgrade extends Upgrade {

	private AtlasRegion tex;
	
	public LaserUpgrade(GameState gs) {
		super(gs, Specular.prefs.getFloat("Beam Upgrade Grade"), 10);
	}
	
	public LaserUpgrade(float grade, float maxGrade, TextureAtlas ta) {
		super(null, grade, maxGrade);
		tex = ta.findRegion("game1/Laser Powerup");
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		gs.getPlayer().setLaserArc((float) (30 + Math.sqrt(getGrade()) * 3));
	}
	
	public AtlasRegion getTexture() {
		return tex;
	}

	@Override
	public String getDescription() {
		return "wider targeting arc";
	}
}
