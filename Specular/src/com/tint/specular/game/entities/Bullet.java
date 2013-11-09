package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.tint.specular.game.GameState;
import com.tint.specular.utils.Util;

public class Bullet implements Entity {
	
	//FIELDS
	private static Texture bulletTex;
	
	private float x, y, dx, dy;
	private boolean isHit;
	private Circle hitbox;
	private GameState gs;
	
	//CONSTRUCTOR
	public Bullet(float x, float y, float direction, float initalDx, float initalDy, GameState gs) {
		this.x = x;
		this.y = y;
		float cos = (float) Math.cos(Math.toRadians(direction));
		float sin = (float) Math.sin(Math.toRadians(direction));
		this.dx = cos * 15;
		this.dy = sin * 15;
		if(Math.abs(dx + initalDx) > 15) {
			dx += initalDx;
		}
		if(Math.abs(dy + initalDy) > 15) {
			dy += initalDy;
		}
		this.gs = gs;
		
		this.x += cos * 32;
		this.y += sin * 32;
		
		hitbox = new Circle(x, y, bulletTex.getWidth() > bulletTex.getHeight() ? bulletTex.getWidth() : bulletTex.getHeight());
	}

	//RENDER&UPDATE loop
/*________________________________________________________________*/
	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, bulletTex, x, y, 0);
	}
	
	@Override
	public boolean update(float delta) {
		x += dx;
		y += dy;
		
		if((x + dx < 0 || x + dx > gs.getMapWidth() || y + dy < 0 || y + dy > gs.getMapHeight() || isHit))
			return true;
		else
			return false;
	}
/*________________________________________________________________*/
	
	public static void init() {
		bulletTex = new Texture(Gdx.files.internal("graphics/game/Bullet.png"));
	}
	
	//SETTERS
	public void hit() {
		isHit = true;
	}
	
	//GETTERS
	public Circle getHitbox() { return hitbox; }
	public float getX() { return x; }
	public float getY() { return y; }
	public float getWidth() { return bulletTex.getWidth(); }
	public float getHeight() { return bulletTex.getHeight(); }

	@Override
	public void dispose() {
		bulletTex.dispose();
	}
}
