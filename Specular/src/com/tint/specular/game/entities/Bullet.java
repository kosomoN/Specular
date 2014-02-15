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

	private double direction;
	
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
		this.direction = direction;
	}

	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, bulletTex, x, y, (float) direction - 90);
	}
	
	@Override
	public boolean update() {
		boolean reflect = gs.getCurrentMap().isReflective();
		x += dx;
		y += dy;
		
		if(isHit) {
			createParticles();
			return true;
		} else if(x + dx < 1 || x + dx > gs.getCurrentMap().getWidth()) {
			createParticles();
			if(reflect)
				reflect(Math.toDegrees(Math.atan2(dy, dx)), true);
			else
				return true;
			
		} else if(y + dy < 0 || y + dy > gs.getCurrentMap().getHeight()) {
			createParticles();
			if(reflect)
				reflect(Math.toDegrees(Math.atan2(dy, dx)), false);
			else
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
	
	private void createParticles() {
		if(Specular.camera.position.x - Specular.camera.viewportWidth / 2 - 100 < x &&
				Specular.camera.position.x + Specular.camera.viewportWidth / 2 + 100 > x &&
				Specular.camera.position.y - Specular.camera.viewportHeight / 2 - 100 < y &&
				Specular.camera.position.y + Specular.camera.viewportHeight / 2 + 100 > y) {//Check if the enemy is on the screen
			if(gs.particlesEnabled())
				gs.getParticleSpawnSystem().spawn(Type.BULLET, x, y, 0, 0, 4, false);
		}
	}
	
	/**
	 * Changes direction as the law of reflection in physics
	 * @param inAngle - The incoming angle which the object is moving towards the reflective surface
	 * @param x - If the reflective surface is looked from above on the x-axis or the y-axis
	 */
	private void reflect(double inAngle, boolean x) {
		direction = 180 - inAngle;
		direction += x ? 0 : 180;
		
		if(direction < 0)
			direction += 360;
		else if(direction > 360)
			direction -= 360;
		
		dx = (float) (Math.cos(direction / 180 * Math.PI) * (SPEED - 5));
		dy = (float) (Math.sin(direction / 180 * Math.PI) * (SPEED - 5));
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
