package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class LifeUpgrade extends Upgrade {

	private AtlasRegion tex;

	public LifeUpgrade(GameState gs) {
		super(gs, Specular.prefs.getInteger("Life Upgrade Grade"), 10);
	}
	
	public LifeUpgrade(int grade, int maxGrade, TextureAtlas ta) {
		super(null, grade, maxGrade);
		tex = ta.findRegion("game1/Life");
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		if(getGrade() == getMaxGrade())
			Player.setStartingLife(4);
	}
	
	public AtlasRegion getTexture() {
		return tex;
	}
}
