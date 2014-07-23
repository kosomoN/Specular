package com.tint.specular.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class Bullet implements Entity, Poolable {
	
	private static final float SPEED = 35;

	public static float damage = 1;
	
	private static AtlasRegion bulletTex;
	private static int size;

	public static int maxBounces = 0;
	
	private static Pool<Bullet> bulletPool;
	
	public static int bulletsFired, bulletsMissed;
	
	private static boolean twist;
	
	private int bounces = 0;
	private float x, y, dx, dy;
	private boolean isHit;

	
	private GameState gs;

	private float direction, turning;
	
	private Bullet(GameState gs) {
		this.gs = gs;
	}

	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, bulletTex, x, y, direction - 90);
	}
	
	@Override
	public boolean update() {
		// Turning
		if(isTwisting())
			calculateDelta();
		// Moving
		x += dx;
		y += dy;
		
		if(isHit) {
			createParticles();
			return true;
		}
		
		if(x + dx - 18 < 0 || x + dx + 18 > gs.getCurrentMap().getHeight()) {
			createParticles();
			if(bounces >= maxBounces) {
				bulletsMissed++;
				return true;
			}
			bounces++;
			
			direction = 180 - direction;
			calculateDelta();
		}
		
		if(y + dy - 18 < 0 || y + dy + 18 > gs.getCurrentMap().getHeight()) {
			createParticles();
			if(bounces >= maxBounces) {
				bulletsMissed++;
				return true;
			}
			bounces++;
			
			direction = 360 - direction;
			calculateDelta();
		}
		return false;
	}
	
	private void calculateDelta() {
		turning *= 0.9f;
		direction += turning;
		dx = (float) Math.cos(Math.toRadians(direction)) * SPEED;
		dy = (float) Math.sin(Math.toRadians(direction)) * SPEED;
	}

	public static void init(final GameState gs) {
		bulletTex = gs.getTextureAtlas().findRegion("game1/Bullet");
		
		size = bulletTex.getRegionWidth() / 2;
		
		bulletPool = new Pool<Bullet>() {
			@Override
			protected Bullet newObject() {
				return new Bullet(gs);
			}
		};
	}
	
	public void hit() {
		isHit = true;
	}
	
	private void createParticles() {
		gs.getParticleSpawnSystem().spawn(Type.BULLET, x, y, 0, 0, 3, false);
	}
	
	public float getX() { return x; }
	public float getY() { return y; }
	public float getWidth() { return size; }
	public float getHeight() { return size; }
	public static void setTwist(boolean twist) { Bullet.twist = twist; }
	public static void setDamage(float damage) { Bullet.damage = damage;}
	public static boolean isTwisting() { return twist; }
	@Override
	public void dispose() {
	}
	
	/**
	 * This is called to re-use the particle to avoid garbage collection
	 */
	private Bullet reUse(float x, float y, float direction, float turning, float initialDx, float initialDy) {
		bulletsFired++;
		
		this.x = x;
		this.y = y;
		direction += Math.random() - 0.5f;
		this.turning = turning;
		float cos = (float) Math.cos(Math.toRadians(direction));
		float sin = (float) Math.sin(Math.toRadians(direction));
		this.dx = cos * SPEED;
		this.dy = sin * SPEED;

		//Move the bullet away from the player center
		this.x += cos * 28;
		this.y += sin * 28;
		this.direction = direction;
		
		bounces = 0;
		isHit = false;
		
		return this;
	}
	
	@Override
	public void reset() {}
	
	public static Bullet obtainBullet(float x, float y, float direction, float turning, float initialDx, float initialDy) {
		return bulletPool.obtain().reUse(x, y, direction, turning, initialDx, initialDy);
	}
	
	public static void free(Bullet bullet) {
		bulletPool.free(bullet);
	}
}
