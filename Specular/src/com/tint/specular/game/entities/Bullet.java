package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.tint.specular.game.GameState;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class Bullet implements Entity {
	
	public static float damage = 1;
	
	private static Texture bulletTex;
	private static int size;
	
	private float x, y, dx, dy;
	private boolean isHit;
	private Circle hitbox;
	private GameState gs;
	private Player shooter;
	
	public Bullet(float x, float y, float direction, float initialDx, float initialDy, GameState gs, Player shooter) {
		this.x = x;
		this.y = y;
		direction += Math.random() * 10 - 5;
		float cos = (float) Math.cos(Math.toRadians(direction));
		float sin = (float) Math.sin(Math.toRadians(direction));
		this.dx = cos * 15;
		this.dy = sin * 15;

		//Move the bullet away from the player center
		this.x += cos * 28;
		this.y += sin * 28;
		
		this.gs = gs;
		this.shooter = shooter;
	}

	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, bulletTex, x, y, 0);
	}
	
	@Override
	public boolean update() {
		x += dx;
		y += dy;
		
		return ((x + dx < 0 || x + dx > gs.getCurrentMap().getWidth() || y + dy < 0 || y + dy > gs.getCurrentMap().getHeight()) || isHit);
	}
	
	public static void init() {
		bulletTex = new Texture(Gdx.files.internal("graphics/game/Bullet.png"));
		bulletTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		size = bulletTex.getWidth() / 2;
	}
	
	public void hit() {
		isHit = true;
	}
	
	public Player getShooter() { return shooter; }
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
