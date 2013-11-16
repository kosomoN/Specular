package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.utils.Util;

public class Particle implements Entity {
	
	//FIELDS
	private float x, y, dx, dy;
	private int lifetime;
	
	private GameState gs;
	private static Texture texture;
	
	public Particle(float x, float y, float dx, float dy, GameState gs) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.gs = gs;
		
		lifetime = 1000;
	}
	
	@Override
	public boolean update() {
		//Lifetime update
		lifetime -= 10;
	
		//Movement
		x += dx;
		y += dy;
		
		//Check map boundaries and life
		if((x + dx < 0 || x + dx > gs.getCurrentMap().getWidth() || y + dy < 0 || y + dy > gs.getCurrentMap().getHeight()))
			return true;
		else
			return lifetime <= 0;
	}

	@Override
	public void render(SpriteBatch batch) {
		if(texture != null)
			Util.drawCentered(batch, texture, x, y, 0);
	}
	
	public static void init() {
		texture = new  Texture(Gdx.files.internal(""));
	}
	
	//SETTERS
	
	//GETTERS
	public float getX() { return x; }
	public float getY() { return y; }

	@Override
	public void dispose() {
		texture.dispose();
	}

}