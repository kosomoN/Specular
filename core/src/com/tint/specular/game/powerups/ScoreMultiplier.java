package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class ScoreMultiplier extends PowerUp {
	private static AtlasRegion texture;
	
	public ScoreMultiplier(float x, float y, GameState gs) {
		super(x, y, gs, -1);
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/Multiplier");
	}
	
	@Override
	protected void affect(Player player) {
		gs.setScoreMultiplier(gs.getScoreMultiplier() * 2);
	}

	@Override
	public AtlasRegion getTexture() {
		return texture;
	}
}
