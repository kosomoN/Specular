package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class FireRateBoost extends PowerUp {
private static Texture texture;
	
	public FireRateBoost(float x, float y, GameState gs) {
		super(x, y, gs);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/FireRate.png"));
	}
	
	@Override
	protected void affect(Player player) {
		player.setFireRate(5f);
	}

	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, texture, x, y, 0);
	}

	@Override
	public float getRadius() {
		return texture.getWidth() / 2;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
	
	@Override
	public void dispose() {
		texture.dispose();
	}
}