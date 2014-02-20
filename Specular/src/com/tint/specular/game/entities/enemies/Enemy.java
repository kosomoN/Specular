package com.tint.specular.game.entities.enemies;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Particle.Type;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public abstract class Enemy implements Entity {
	
	public enum EnemyType {
		ENEMY_BOOSTER, ENEMY_DASHER, ENEMY_STRIVER, ENEMY_CIRCLER, ENEMY_SHIELDER, ENEMY_VIRUS, ENEMY_WANDERER;
	}

	protected float x, y, dx, dy;
	protected float direction;
	protected static float slowdown = 1;
	
	protected int life;
	protected static int MAX_LIFE;
	protected boolean isHit;
	
	protected GameState gs;
	
	public Enemy(float x, float y, GameState gs, int life) {
		this.x = x;
		this.y = y;
		this.gs = gs;
		this.life = life;
		MAX_LIFE = life;
	}
	
	@Override
	public boolean update() {
		return life <= 0;
	}

	/**
	 * 
	 * @param slowdown - 1 means normal speed, 0 means no speed.
	 */
	public static void setSlowdown(float slowdown) {
		Enemy.slowdown = slowdown;
	}
	
	public void hit(double damage) {
		life -= damage;
		
		if(life == 0)
			gs.getParticleSpawnSystem().spawn(getParticleType(), x, y, dx, dy, 15, true);
		else
			gs.getParticleSpawnSystem().spawn(getParticleType(), x, y, dx, dy, 6, false);
	}
	
	public void addLife(int life) {
		this.life += life;
		if(this.life > MAX_LIFE)
			this.life = MAX_LIFE;
	}
	
	public abstract int getValue();
	public abstract Type getParticleType();
	
	public float getX() { return x;	}
	public float getY() { return y; }
	public float getDx() { return dx; }
	public float getDy() { return dy; }
	public static float getSlowdown() { return slowdown; }

	public int getLife() { return life; }
	
	public abstract float getInnerRadius();
	public abstract float getOuterRadius();
	
	@Override
	public void dispose() {}

	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
}
