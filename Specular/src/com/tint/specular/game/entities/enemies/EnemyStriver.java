package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class EnemyStriver extends Enemy {

	private static Texture tex;
	private float rotation;
	private float speed;

	public EnemyStriver(float x, float y, GameState gs) {
		super(x, y, gs, 2);
		
		speed = (float) (Math.random() * 2 + 2);
	}

	@Override
	public void render(SpriteBatch batch) {
		rotation += Gdx.graphics.getDeltaTime();
		if(hasSpawned)
			Util.drawCentered(batch, tex, x, y, rotation * 90 % 360);
		else
			Util.drawCentered(batch, tex, x, y, tex.getWidth() * (spawnTimer / 100f), tex.getHeight() * (spawnTimer / 100f), rotation * 90 % 360);
	}
	
	@Override
	public void updateMovement() {
		//Calculating angle of movement based on closest player
		double angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
		
		dx = (float) (Math.cos(angle) * speed);
		dy = (float) (Math.sin(angle) * speed);
		x += dx * slowdown;
		y += dy * slowdown;
	}
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Fast.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	public int getValue() {
		return 3;
	}
	
	@Override
	public void dispose() {
		tex.dispose();
	}

	@Override
	public float getInnerRadius() { return 16; }
	@Override
	public float getOuterRadius() { return 30; }

	@Override
	public Type getParticleType() {
		return Type.ENEMY_FAST;
	}	
	
	@Override
	public int getSpawnTime() {
		return 100;
	}
}
