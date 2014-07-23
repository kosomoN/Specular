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

public class FireRateBoost extends PowerUp {
	private static AtlasRegion texture;
	private static float boost = 2 / 3f;
	public static int stacks;
	
	public FireRateBoost(float x, float y, GameState gs) {
		super(x, y, gs, 800);
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/FireRate");
//		boost = Specular.prefs.getFloat("Firerate Boost");
	}
	
	@Override
	protected void affect(Player player) {
		stacks++;
		player.setFireRate((float) (10 * Math.pow(boost, stacks)));
	}
	
	@Override
	protected void removeEffect(Player player) {
		stacks--;
		player.setFireRate((float) (10 * Math.pow(boost, stacks)));
	}
	
	public static void setBoost(float boost) { FireRateBoost.boost = boost; }
	public static float getBoost() { return boost; }
	
	@Override
	public AtlasRegion getTexture() {
		return texture;
	}
}
