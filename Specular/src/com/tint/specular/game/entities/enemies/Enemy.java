package com.tint.specular.game.entities.enemies;

import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Entity;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public abstract class Enemy implements Entity {
	
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
	 * @param slowdown - Max value 1, minimum value 0.
	 * If it is higher than 1 or lower than 0, it will be changed to
	 * the nearest accepted value
	 */
	public static void setSlowdown(float slowdown) {
		Enemy.slowdown = slowdown;
	}
	
	public void hit(double damage) {
		life -= damage;
		if(Specular.camera.position.x - Specular.camera.viewportWidth / 2 - 100 < x &&
				Specular.camera.position.x + Specular.camera.viewportWidth / 2 + 100 > x &&
				Specular.camera.position.y - Specular.camera.viewportHeight / 2 - 100 < y &&
				Specular.camera.position.y + Specular.camera.viewportHeight / 2 + 100 > y) {//Check if the enemy is on the screen
			if(gs.particlesEnabled()) {
				if(life == 0)
					gs.getParticleSpawnSystem().spawn(this, 15, true);
				else
					gs.getParticleSpawnSystem().spawn(this, 6, false);
			}
		}
	}
	
	public void addLife(int life) {
		this.life += life;
		if(this.life > MAX_LIFE)
			this.life = MAX_LIFE;
	}
	
	public abstract int getValue();
	
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
