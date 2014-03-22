package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

public class EnemyShielder extends Enemy {

	private static final float FORCE = 7, RANGE = 300 * 300;
	
	private static Animation anim, spawnAnim;
	private static Texture warningTex;
	
	public EnemyShielder(float x, float y, GameState gs) {
		super(x, y, gs, 10);
	}

	@Override
	public void renderEnemy(SpriteBatch batch) {
		TextureRegion frame = anim.getKeyFrame(rotation, true);
		Util.drawCentered(batch, frame, x, y, rotation * 70);
	}

	@Override
	public void updateMovement() {
		double angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
		
		dx = (float) (Math.cos(angle) * 6);
		dy = (float) (Math.sin(angle) * 6);
		x += dx * slowdown;
		y += dy * slowdown;
		
		float distanceSquared;
		for(Enemy e : gs.getEnemies()) {
			if(!(e instanceof EnemyShielder) && !(e instanceof EnemyDasher)) {
				distanceSquared = (e.getX() - getX()) * (e.getX() - getX()) + (e.getY() - getY()) * (e.getY() - getY());
				if(RANGE > distanceSquared) {
					//If the enemy is too close to the shielder it should be pushed away
					angle = Math.atan2(getY() - e.getY(), getX() - e.getX());
					e.setX((float) (e.getX() + Math.cos(angle) * FORCE * ((distanceSquared + 15000) / RANGE)) + dx);
					e.setY((float) (e.getY() + Math.sin(angle) * FORCE * ((distanceSquared + 15000) / RANGE)) + dy);
				}
			}
		}
	}
	
	public static void init() {
		Texture texture = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Shielder.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		anim = Util.getAnimation(texture, 128, 128, 1 / 15f, 0, 0, 3, 1);		
		
		warningTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Shielder Warning.png"));
		warningTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture animTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Shielder Anim.png"));
		animTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		spawnAnim = Util.getAnimation(animTex, 128, 128, 1 / 15f, 0, 0, 3, 3);
	}

	@Override
	public int getValue() {
		return 10;
	}

	@Override
	public float getInnerRadius() { return 32; }
	@Override
	public float getOuterRadius() { return 60; }

	@Override
	public Type getParticleType() {
		return Type.ENEMY_SHIELDER;
	}	
	
	@Override
	protected Animation getSpawnAnim() {
		return spawnAnim;
	}

	@Override
	protected Texture getWarningTex() {
		return warningTex;
	}

	@Override
	protected float getRotationSpeed() {
		return 70;
	}
}
