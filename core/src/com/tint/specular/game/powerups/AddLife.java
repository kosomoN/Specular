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

public class AddLife extends PowerUp {
	private static AtlasRegion texture;
	
	public AddLife(float x, float y, GameState gs) {
		super(x, y, gs, -1);
	}

	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/Life");
	}
	
	@Override
	protected void affect(Player player) {
		player.addLives(1);
	}

	@Override
	public AtlasRegion getTexture() {
		return texture;
	}
}
