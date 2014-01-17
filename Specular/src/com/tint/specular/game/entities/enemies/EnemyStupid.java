package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class EnemyStupid extends Enemy {

	private static Texture texture;
	private float rotation;
	private float dirChangeRateMs = 2000f;
	private float timeSinceLastDirChange;
	private double angle;
	
	public EnemyStupid(float x, float y, GameState gs) {
		super(x, y, gs, 2);
		angle = Math.random() * 360;
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/Enemy Wanderer.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, texture, x, y, rotation);
	}
	
	@Override
	public boolean update() {
		//10 ms is the update rate
		timeSinceLastDirChange += 10;
		if(timeSinceLastDirChange > dirChangeRateMs) {
			angle = Math.random() * 360;
			timeSinceLastDirChange = 0;
		}
		
		dx = (float) (Math.cos(angle) * 2);
		dy = (float) (Math.sin(angle) * 2);
		x += dx * (1 - slowdown);
		y += dy * (1 - slowdown);
		
		//Checking so that the enemy will not get outside the map
		if(x - 20 < 0) {
			x = 20;
		} else if(x + 20 > gs.getCurrentMap().getWidth()){
			x = gs.getCurrentMap().getWidth() - 20;
		}
		
		if(y - 20 < 0) {
			y = 20;
		} else if(y + 20 > gs.getCurrentMap().getHeight()){
			y = gs.getCurrentMap().getHeight() - 20;
		}
		
		return super.update();
	}
	
	@Override
	public int getValue() {
		return 1;
	}

	@Override
	public float getInnerRadius() {
		return texture.getWidth() / 4;
	}

	@Override
	public float getOuterRadius() {
		return texture.getWidth() / 2;
	}
}
