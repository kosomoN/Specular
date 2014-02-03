package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class FireRateBoost extends PowerUp {
	private static Texture texture;
	private Player player;
	
	public FireRateBoost(float x, float y, GameState gs) {
		super(x, y, gs);
		activeTime = 600;
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/FireRate.png"));
	}
	
	@Override
	protected void affect(Player player) {
		this.player = player;
		player.setFireRate(player.getFireRate() * 3 / 4f); // 50% boost
	}

	@Override
	public void render(SpriteBatch batch) {
		if(!isActivated() && despawnTime > 0)
			batch.draw(texture, x, y);
	}

	@Override
	public boolean update() {
		if(super.update())
			player.setFireRate(10f);
		else
			return despawnTime <= 0;
		
		return true;
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
