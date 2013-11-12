package com.tint.specular.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.entities.Entity;

public class Particle implements Entity {
	
	private float x, y;
	
	public Particle(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	//SETTERS
	
	//GETTERS
	public float getX() { return x; }
	public float getY() { return y; }

	@Override
	public boolean update() {
		return false;
	}

	@Override
	public void render(SpriteBatch batch) {
		
	}

	@Override
	public void dispose() {
		
	}

}