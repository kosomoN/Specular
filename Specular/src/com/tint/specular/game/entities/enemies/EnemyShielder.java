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
	
	private static Animation anim;
	private float animFrameTime;
	
	public EnemyShielder(float x, float y, GameState gs) {
		super(x, y, gs, 10);
	}

	@Override
	public void render(SpriteBatch batch) {
		animFrameTime += Gdx.graphics.getDeltaTime();
		
		TextureRegion frame = anim.getKeyFrame(animFrameTime, true);
		batch.draw(frame, x - frame.getRegionWidth() / 2, y - frame.getRegionHeight() / 2, frame.getRegionWidth() / 2, frame.getRegionHeight() / 2, frame.getRegionWidth(), frame.getRegionHeight(), 1, 1, animFrameTime * 70, false);
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
			if(!(e instanceof EnemyShielder)) {
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
	public int getSpawnTime() {
		return 100;
	}
}
