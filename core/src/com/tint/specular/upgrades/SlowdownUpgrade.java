package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.powerups.SlowdownEnemies;

public class SlowdownUpgrade extends Upgrade {

	private AtlasRegion tex;
	
	public SlowdownUpgrade(GameState gs) {
		super(gs, Specular.prefs.getInteger("Slowdown Upgrade Grade"), 10);
	}
	
	public SlowdownUpgrade(int grade, int maxGrade, TextureAtlas ta) {
		super(null, grade, maxGrade);
		tex = ta.findRegion("game1/Slowdown");
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		SlowdownEnemies.setFreezeTime((float) (Math.sqrt(getGrade()) * 500));
	}
	
	public AtlasRegion getTexture() {
		return tex;
	}

	@Override
	public String getDescription() {
		return "Increase time of total freeze";
	}
}
