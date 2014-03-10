package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
		ENEMY_BOOSTER, ENEMY_DASHER, ENEMY_STRIVER, ENEMY_CIRCLER, ENEMY_SHIELDER, ENEMY_VIRUS, ENEMY_WANDERER, ENEMY_SUICIDER, ENEMY_TANKER;
	}
	
	protected float x, y, dx, dy;
	protected float direction;
	protected static float slowdown = 1;
	
	protected int life;
	protected boolean isHit, hasSpawned;
	protected int spawnTimer;
	
	protected GameState gs;
	
	public Enemy(float x, float y, GameState gs, int life) {
		this.x = x;
		this.y = y;
		this.gs = gs;
		this.life = life;
	}
	
	@Override
	public boolean update() {
		if(hasSpawned)
			updateMovement();
		else {
			spawnTimer++;
			//16 frames, 1 / 15s per frame and 3 seconds for the warning. Multiply with 60 to get ticks
			if(spawnTimer >= (1 / 15f * 16 + 2) * 60)
				hasSpawned = true;
		}
		return life <= 0;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if(hasSpawned)
			renderEnemy(batch);
		else {
			
			//Show warning image
			if(spawnTimer < 2f * 60) {
				batch.setColor(1, 1, 1, ((float) Math.cos(spawnTimer / 60f * Math.PI * 2 + Math.PI) + 1) / 2);
				batch.draw(getWarningTex(), x - getWarningTex().getWidth() / 2, y - getWarningTex().getHeight() / 2);
				batch.setColor(1, 1, 1, 1);
			} else {
				TextureRegion tr = getSpawnAnim().getKeyFrame(spawnTimer / 60f - 2);
				batch.draw(tr, x - tr.getRegionWidth() / 2, y - tr.getRegionHeight() / 2);
			}
		}
	}

	protected abstract void renderEnemy(SpriteBatch batch);
	
	protected abstract Animation getSpawnAnim();
	protected abstract Texture getWarningTex();
	
	public abstract void updateMovement();

	/**
	 * 
	 * @param slowdown - 1 means normal speed, 0 means no speed.
	 */
	public static void setSlowdown(float slowdown) {
		Enemy.slowdown = slowdown;
	}
	
	public void hit(int damage) {
		life -= damage;
		
		if(life <= 0)
			gs.getParticleSpawnSystem().spawn(getParticleType(), x, y, dx * slowdown, dy * slowdown, 15, true);
		else
			gs.getParticleSpawnSystem().spawn(getParticleType(), x, y, dx * slowdown, dy * slowdown, 6, false);
	}
	
	public void addLife(int life) { this.life += life; }
	public void kill() { hit(life); }
	
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
		this.x = x < 38 ? 38 : (x > gs.getCurrentMap().getWidth() - 38 ? gs.getCurrentMap().getWidth() - 38 : x);
	}
	
	public void setY(float y) {
		this.y = y < 38 ? 38 : (y > gs.getCurrentMap().getHeight() - 38 ? gs.getCurrentMap().getHeight() - 38 : y);
	}
	
	public void addDx(float dx) {
		this.dx += dx;
	}
	
	public void addDy(float dy) {
		this.dy += dy;
	}
	
	public boolean hasSpawned() {
		return hasSpawned;
	}
}
