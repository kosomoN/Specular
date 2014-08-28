package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.states.UpgradeState;

public class BoardshockPowerUp extends PowerUp {

	private static AtlasRegion texture;
	private static AtlasRegion levelTex;

	public BoardshockPowerUp(float x, float y, GameState gs) {
		super(x, y, gs, -1);
	}

	public BoardshockPowerUp() {
		super();
	}
	
	@Override
	protected void affect(Player p) {
		gs.addBoardshockCharge(1f);
	}

	@Override
	public AtlasRegion getTexture() {
		return texture;
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/BoardShock");
	}
	
	public static void reloadLevelTextures(float grade) {
		levelTex = UpgradeState.getUpgradeLevelTexture(grade);
	}

	@Override
	public AtlasRegion getLevelTexture() {
		return levelTex;
	}
}
