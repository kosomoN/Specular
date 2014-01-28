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

public class BulletBurst_5 extends PowerUp {
	private static Texture texture;
	private Player player;
	
	public BulletBurst_5(float x, float y, GameState gs) {
		super(x, y, gs);
		activeTime = 600;
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/5 Burst.png"));
	}
	
	@Override
	protected void affect(Player player) {
		this.player = player;
		player.setBulletBurst(5);
	}

	@Override
	public void render(SpriteBatch batch) {
		if(despawnTime > 0)
			Util.drawCentered(batch, texture, x, y, 0);
	}
	
	@Override
	public boolean update() {
		if(super.update())
			player.setBulletBurst(3);
		else
			return false;
		
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
