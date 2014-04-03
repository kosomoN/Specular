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
	
	public FireRateBoost(float x, float y, GameState gs) {
		super(x, y, gs, 800);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/powerups/FireRate.png"));
	}
	
	@Override
	protected void affect(Player player) {
		player.setFireRate(player.getFireRate() * 4 / 6f);
	}
	
	@Override
	protected void removeEffect(Player player) {
		player.setFireRate(10f);
	}
	
	@Override
	public Texture getTexture() {
		return texture;
	}
}
