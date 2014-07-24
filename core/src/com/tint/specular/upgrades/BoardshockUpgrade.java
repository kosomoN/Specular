package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.BoardShock;
import com.tint.specular.game.GameState;

public class BoardshockUpgrade extends Upgrade {

	private AtlasRegion tex;
	
	public BoardshockUpgrade(GameState gs) {
		super(gs, Specular.prefs.getInteger("Boardshock Upgrade Grade"), 10);
	}
	
	public BoardshockUpgrade(int grade, int maxGrade, TextureAtlas ta) {
		super(null, grade, maxGrade);
		tex = ta.findRegion("game1/BoardShock");
	}
	
	@Override
	public void refresh() {
		// Specfic to every upgrade
		BoardShock.setEfficiency((float) (0.25 - Math.sqrt(getGrade()) / getMaxGrade() * 0.25 / 2));
	}
	
	public AtlasRegion getTexture() {
		return tex;
	}

	@Override
	public String getDescription() {
		return "Increase efficiency against Virus";
	}
}
