package com.tint.specular.game.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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
		BULLET, ENEMY_CIRCLER, ENEMY_WANDERER, ENEMY_STRIVER, ENEMY_VIRUS, ENEMY_BOOSTER, ENEMY_DASHER, ENEMY_SHIELDER, ENEMY_TANKER, ENEMY_EXPLODER;
	}
	
	//FIELDS
	private float x, y, dx, dy;
	private int lifetime;
	
	private GameState gs;
	private Type type;
	private int textureIndexInArray;
	private float rotation;
	private boolean grow;
	
	private static final float ROTATION_SPEED = 6;
	private static TextureRegion[] textures;
	private static AtlasRegion bigBase, smallBase;
	private static Random rand = new Random();
	
	public Particle(GameState gs) {
		this.gs = gs;
	}
	
	@Override
	public boolean update() {
		rotation += ROTATION_SPEED;
		
		//Lifetime update
		lifetime -= 1;
	
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
		float alpha = Math.min((lifetime / 25f) * (lifetime / 25f), 1);
		batch.setColor(1, 1, 1, alpha);
		Util.drawCentered(batch, textures[textureIndexInArray], x, y, (grow ? 1.5f - alpha : alpha) * textures[textureIndexInArray].getRegionWidth(), (grow ? 1.5f - alpha : alpha) * textures[textureIndexInArray].getRegionHeight(), rotation); 
		batch.setColor(1, 1, 1, 1);
	}
	
	public Type getType() { return type; }
	public float getX() { return x; }
	public float getY() { return y; }

	public static void init(TextureAtlas ta) {
		bigBase = ta.findRegion("game1/Large Particles");
		smallBase = ta.findRegion("game1/Small Particles");
		
		//The amount of particles * 2 for large and small
		textures = new TextureRegion[Type.values().length * 2];
		
		//Loop through image and create sub-images
		for(int i = 0; i < Type.values().length; i++) {
			textures[i] = new TextureRegion(smallBase, i % 4 * 32, i / 4 * 32, 32, 32);
			textures[i + Type.values().length] = new TextureRegion(bigBase, i % 4 * 64, i / 4 * 64, 64, 64);
		}
	}
	
	@Override
	public void dispose() {
	}

	/**
	 * This is called to re-use the particle to avoid garbage collection
	 */
	public void reUse(float x, float y, float direction, float initialDx, float initialDy, boolean large, Type type) {
		this.x = x;
		this.y = y;
		
		float sin = (float) Math.sin(Math.toRadians(direction));
		float cos = (float) Math.cos(Math.toRadians(direction));
		dx = (float) (cos * (Math.random() * 4));
		dy = (float) (sin * (Math.random() * 4));
		
		//Adding the speed the enemy had when it died
		dx += initialDx;
		dy += initialDy;
		
		this.textureIndexInArray = type.ordinal() + (large ? Type.values().length : 0); //This is just a variable to make getting the subimage easier
		this.type = type;
		
		lifetime = 30 + rand.nextInt(20);
		
		rotation = rand.nextInt(360);
		
		grow = rand.nextBoolean();
	}
	
	@Override
	public void reset() {
	}
	
	public float getLifetimePercent() {
		return lifetime / 40f;
	}
}