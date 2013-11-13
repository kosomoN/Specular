package com.tint.specular.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Particle implements Entity {
	
	private float x, y;
	private Texture texture;
	private int lifeInMillis = 0;
	
	public Particle(Texture texture, float x, float y) {
		this.x = x;
		this.y = y;
		this.texture = texture;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(texture, x, y);
	}
	
	@Override
	public boolean update() {
		lifeInMillis -= 10;
		return lifeInMillis <= 0;
	}
	
	//SETTERS
	
	//GETTERS
	public float getX() { return x; }
	public float getY() { return y; }

	@Override
	public void dispose() {
		
	}

}