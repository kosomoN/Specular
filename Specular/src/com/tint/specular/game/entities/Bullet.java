package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;

public class Bullet implements Entity {
	
	private static Texture bulletTex;
	
	private float x, y, dx, dy;
	private GameState gs;
	
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
	}

	@Override
	public boolean update() {
		x += dx;
		y += dy;
        
       	return (x + dx < 0 || x + dx > gs.getMapWidth() || y + dy < 0 || y + dy > gs.getMapHeight());
        
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(bulletTex, x - bulletTex.getWidth() / 2, y - bulletTex.getHeight() / 2);
	}
	
	public static void init() {
		bulletTex = new Texture(Gdx.files.internal("graphics/game/Bullet.png"));
	}
}
