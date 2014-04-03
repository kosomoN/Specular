package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
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

	private static Animation anim;
	private static Texture tex, warningTex;

	public EnemyStriver(float x, float y, GameState gs) {
		super(x, y, gs, 2);
		
		speed = (float) (Math.random() * 2 + 2);
	}

	@Override
	public void renderEnemy(SpriteBatch batch) {
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
		tex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Striver.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);		
		
		warningTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Striver Warning.png"));
		warningTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture animTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Striver Anim.png"));
		animTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		anim = Util.getAnimation(animTex, 64, 64, 1 / 15f, 0, 0, 3, 3);
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
		return Type.ENEMY_STRIVER;
	}	
	
	@Override
	protected Animation getSpawnAnim() {
		return anim;
	}

	@Override
	protected Texture getWarningTex() {
		return warningTex;
	}

	@Override
	protected float getRotationSpeed() {
		return 90;
	}
}
