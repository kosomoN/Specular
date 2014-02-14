package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

public class EnemyShielder extends Enemy {

	private static final float FORCE = 7, RANGE = 300;
	
	private static Texture texture;
	private float rotation;
	
	public EnemyShielder(float x, float y, GameState gs) {
		super(x, y, gs, 10);
	}

	@Override
	public void render(SpriteBatch batch) {
		rotation -= Gdx.graphics.getDeltaTime() * 90;
		Util.drawCentered(batch, texture, x, y, rotation);
	}
	
	@Override
	public boolean update() {
		
		double angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
		
		dx = (float) (Math.cos(angle) * 5);
		dy = (float) (Math.sin(angle) * 5);
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
					e.setY(e.getY() + edy + dx * 1.1f);
				}
			}
			
		}
		
		return super.update();
	}

	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/Enemy Shielder.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
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
