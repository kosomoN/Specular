package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.states.UpgradeState;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class AddLife extends PowerUp {
	private static AtlasRegion texture;
	private static Texture levelTex;
	
	public AddLife(float x, float y, GameState gs) {
		super(x, y, gs, -1);
	}
	
	public AddLife() {
		super();
	}

	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/Life");
	}
	
	public static void reloadLevelTextures(float grade) {
		levelTex = UpgradeState.getUpgradeLevelTexture(grade);
	}
	
	@Override
	protected void affect(Player player) {
		player.addLives(1);
	}

	@Override
	public AtlasRegion getTexture() {
		return texture;
	}

	@Override
	public Texture getLevelTexture() {
		return levelTex;
	}
}
