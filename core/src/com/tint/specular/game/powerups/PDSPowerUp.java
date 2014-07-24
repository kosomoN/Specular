package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.PDS;
import com.tint.specular.game.entities.Player;
import com.tint.specular.states.UpgradeState;

public class PDSPowerUp extends PowerUp{

	private static AtlasRegion tex;
	private static Texture levelTex;
	
	public PDSPowerUp(float x, float y, GameState gs) {
		super(x, y, gs, -1);
	}
	
	public PDSPowerUp() {
		super();
	}
	
	public static void init(TextureAtlas ta) {
		tex = ta.findRegion("game1/Point Defense");
	}
	
	public static void reloadLevelTextures(float grade) {
		levelTex = UpgradeState.getUpgradeLevelTexture(grade);
	}
	
	@Override
	protected void affect(Player player) {
		PDS.refillAmmo(10);
	}

	@Override
	public AtlasRegion getTexture() {
		return tex;
	}

	@Override
	public Texture getLevelTexture() {
		return levelTex;
	}

}
