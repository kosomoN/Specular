package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet implements Entity {
	
	private static Texture bulletTex;
	
	private float x, y, dx, dy;
	
	public Bullet(float x, float y, float direction, float initalDx, float initalDy) {
		this.x = x;
		this.y = y;
		this.dx = (float) (Math.cos(Math.toRadians(direction)) * 15);// + initalDx);
		this.dy = (float) (Math.sin(Math.toRadians(direction)) * 15);// + initalDy);
	}

	@Override
	public void update() {
		x += dx;
		y += dy;
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(bulletTex, x - bulletTex.getWidth() / 2, y - bulletTex.getHeight() / 2);
	}
	
	public static void init() {
		bulletTex = new Texture(Gdx.files.internal("graphics/game/Bullet.png"));
	}
}
