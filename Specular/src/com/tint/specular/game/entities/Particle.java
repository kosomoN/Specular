package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.utils.Util;

public class Particle implements Entity {
	
	//FIELDS
	private float x, y, dx, dy;
	private int lifetime;
	
	private GameState gs;
	private Type type;
	private static TextureRegion texture;
	private static Texture bigBase, smallBase;
	
	public Particle(float x, float y, float direction, float initialDx, float initialDy, float radius, boolean large, Type type, GameState gs) {
		this.x = x;
		this.y = y;
		float sin = (float) Math.sin(Math.toRadians(direction));
		float cos = (float) Math.cos(Math.toRadians(direction));
		dx = cos * 10;
		dy = sin * 10;
		
		//float minimumOffset = 15 - (dx < dy ? dx : dy);
		
		if(dx + initialDx > 15)
			dx += initialDx;
		//else
			//dx += minimumOffset;
		
		if(dy + initialDy > 15)
			dy += initialDy;
		//else
		//	dy += minimumOffset;
		
		this.x += cos * radius;
		this.y += sin * radius;
		
		this.gs = gs;
		
		lifetime = 500;
		
		
		switch(type) {
		case PLAYER :
			texture = new TextureRegion(large ? bigBase : smallBase, 0, 0, large ? bigBase.getWidth() : smallBase.getWidth(),
					large ? bigBase.getHeight() : smallBase.getHeight());
			break;
			
		case ENEMY_NORMAL :
			texture = new TextureRegion(large ? bigBase : smallBase, large ? bigBase.getWidth() / 2 : smallBase.getWidth() / 2,
					0, large ? bigBase.getWidth() : smallBase.getWidth(), large ? bigBase.getHeight() : smallBase.getHeight());
			break;
			
		case ENEMY_FAST :
			texture = new TextureRegion(large ? bigBase : smallBase, 0, large ? bigBase.getHeight() / 2 : smallBase.getHeight() / 2,
					large ? bigBase.getWidth() : smallBase.getWidth(), large ? bigBase.getHeight() : smallBase.getHeight());
			break;
			
		case ENEMY_BOOSTER :
			texture = new TextureRegion(large ? bigBase : smallBase, large ? bigBase.getWidth() / 2 : smallBase.getWidth() / 2,
					large ? bigBase.getHeight() / 2 : smallBase.getHeight() / 2, large ? bigBase.getWidth() : smallBase.getWidth(),
					large ? bigBase.getHeight() : smallBase.getHeight());
			break;
		}
	}
	
	public enum Type {
		PLAYER, ENEMY_NORMAL, ENEMY_FAST, ENEMY_BOOSTER;
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
	
	//SETTERS
	
	//GETTERS
	public Type getType() { return type; }
	public float getX() { return x; }
	public float getY() { return y; }

	public static void init() {
		bigBase = new Texture(Gdx.files.internal("graphics/game/Large Particles.png"));
		smallBase = new Texture(Gdx.files.internal("graphics/game/Small Particles.png"));
	}
	
	@Override
	public void dispose() {
		
	}

}