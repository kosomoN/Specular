package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.tint.specular.game.GameState;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class Particle implements Entity, Poolable {
	
	public enum Type {
		PLAYER, ENEMY_NORMAL, ENEMY_FAST, ENEMY_BOOSTER;
	}
	
	//FIELDS
	private float x, y, dx, dy;
	private int lifetime;
	
	private GameState gs;
	private Type type;
	private int textureIndexInArray;
	private static TextureRegion[] textures;
	private static Texture bigBase, smallBase;
	
	public Particle(GameState gs) {
		this.gs = gs;
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
		Util.drawCentered(batch, textures[textureIndexInArray], x, y, 0);
	}
	
	public Type getType() { return type; }
	public float getX() { return x; }
	public float getY() { return y; }

	public static void init() {
		bigBase = new Texture(Gdx.files.internal("graphics/game/Large Particles.png"));
		smallBase = new Texture(Gdx.files.internal("graphics/game/Small Particles.png"));
		
		//The amount of particles * 2 for large and small
		textures = new TextureRegion[Type.values().length * 2];
		
		//Loop through image and create sub-images
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				textures[i * 2 + j] = new TextureRegion(smallBase, j * smallBase.getWidth() / 2, i * smallBase.getWidth() / 2,
																		smallBase.getWidth() / 2, smallBase.getWidth() / 2);
				textures[i * 2 + j + 4] = new TextureRegion(bigBase, j * bigBase.getWidth() / 2, i * bigBase.getWidth() / 2,
						bigBase.getWidth() / 2, bigBase.getWidth() / 2);
			}
		}
	}
	
	@Override
	public void dispose() {
		bigBase.dispose();
		smallBase.dispose();
	}

	/**
	 * This is called to re-use the particle to avoid garbage collection
	 */
	public void reUse(float x, float y, float direction, float initialDx, float initialDy, float radius, boolean large, Type type) {
		this.x = x;
		this.y = y;
		
		float sin = (float) Math.sin(Math.toRadians(direction));
		float cos = (float) Math.cos(Math.toRadians(direction));
		dx = (float) (cos * (Math.random() * 4 + 2));
		dy = (float) (sin * (Math.random() * 4 + 2));
		
		//Adding the speed the enemy had when it died
		dx += initialDx;
		dy += initialDy;
		
		this.textureIndexInArray = type.ordinal() + (large ? 4 : 0); //This is just a variable to make getting the subimage easier
		this.type = type;
		
		lifetime = (int) (250 + Math.random() * 250);
	}
	
	@Override
	public void reset() {
	}
}