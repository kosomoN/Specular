package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class Bullet implements Entity {
	
	private static final float SPEED = 25;

	public static float damage = 1;
	
	private static Texture bulletTex;
	private static int size;
	
	private float x, y, dx, dy;
	private boolean isHit;
	private Circle hitbox;
	private GameState gs;

	private float direction;
	
	public Bullet(float x, float y, float direction, float initialDx, float initialDy, GameState gs) {
		this.x = x;
		this.y = y;
		direction += Math.random() * 6 - 3;
		float cos = (float) Math.cos(Math.toRadians(direction));
		float sin = (float) Math.sin(Math.toRadians(direction));
		this.dx = cos * SPEED;
		this.dy = sin * SPEED;

		//Move the bullet away from the player center
		this.x += cos * 28;
		this.y += sin * 28;
		
		this.gs = gs;
		
		this.direction = direction - 90;
	}

	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, bulletTex, x, y, direction);
	}
	
	@Override
	public boolean update() {
		x += dx;
		y += dy;
		
		if((x + dx < 1 || x + dx > gs.getCurrentMap().getWidth() || y + dy < 0 || y + dy > gs.getCurrentMap().getHeight()) || isHit) {
			if(Specular.camera.position.x - Specular.camera.viewportWidth / 2 - 100 < x &&
					Specular.camera.position.x + Specular.camera.viewportWidth / 2 + 100 > x &&
					Specular.camera.position.y - Specular.camera.viewportHeight / 2 - 100 < y &&
					Specular.camera.position.y + Specular.camera.viewportHeight / 2 + 100 > y) {//Check if the enemy is on the screen
				if(gs.particlesEnabled())
					gs.getParticleSpawnSystem().spawn(Type.BULLET, x, y, 0, 0, 4, false);
			}
			return true;
		}
		return false;
	}
	
	public static void init() {
		bulletTex = new Texture(Gdx.files.internal("graphics/game/Bullet.png"));
		bulletTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		size = bulletTex.getWidth() / 2;
	}
	
	public void hit() {
		isHit = true;
	}
	
	public Circle getHitbox() { return hitbox; }
	public float getX() { return x; }
	public float getY() { return y; }
	public float getWidth() { return size; }
	public float getHeight() { return size; }
	
	@Override
	public void dispose() {
		bulletTex.dispose();
	}
}
