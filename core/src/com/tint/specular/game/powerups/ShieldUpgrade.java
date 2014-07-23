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

public class ShieldUpgrade extends PowerUp {
	private static AtlasRegion texture;
	
	public ShieldUpgrade(float x, float y, GameState gs) {
		super(x, y, gs, -1);
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/ShieldUpgrade");
	}

	@Override
	protected void affect(Player p) {
		p.addShield();
	}

	@Override
	public AtlasRegion getTexture() {
		return texture;
	}
}
