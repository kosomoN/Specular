package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class FireRateBoost extends PowerUp {
	private static Texture texture;
	private static float boost = 2 / 3f;
	public static int stacks;
	
	public FireRateBoost(float x, float y, GameState gs) {
		super(x, y, gs, 800);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/powerups/FireRate.png"));
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
	public Texture getTexture() {
		return texture;
	}
}
