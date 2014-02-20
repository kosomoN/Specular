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

	private static final float FORCE = 7, RANGE = 300;
	
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
	public boolean update() {
		
		double angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
		
		if(!pushed) {
			if(speed < 4.5f)
				speed += 0.1f;
			else
				speed = 5;
			
			dx = (float) (Math.cos(angle) * 5);
			dy = (float) (Math.sin(angle) * 5);
		} else {
			speed = super.speed;
		}
		x += dx * slowdown;
		y += dy * slowdown;
		
		for(Enemy e : gs.getEnemies()) {
			if(!(e instanceof EnemyShielder)) {
				if(Math.abs(e.getX() - x) < RANGE && Math.abs(e.getY() - y) < RANGE) {
					float xDiff = x - e.getX();
					float yDiff = y - e.getY();
					
					float edx = 0, edy = 0;
					
					if(Math.abs(xDiff) > 64) {
						edx = xDiff / RANGE * FORCE;
					} else if(Math.abs(xDiff) < 56){
						edx = xDiff > 0 ? -1 : 1;
					}
					
					if(Math.abs(yDiff) > 64) {
						edy = yDiff / RANGE * FORCE;
					} else if(Math.abs(xDiff) < 56){
						edy = yDiff > 0 ? -1 : 1;
					}
					
					e.setX(e.getX() + edx + dx * 1.1f);
					e.setY(e.getY() + edy + dy * 1.1f);
				}
			}
			
		}
		
		return super.update();
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
		return Type.ENEMY_BOOSTER;
	}
}
