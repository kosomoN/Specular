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

public class EnemyWanderer extends Enemy {

	private static Texture texture;
	private float rotation;
	private float dirChangeRateMs = 2000f;
	private float timeSinceLastDirChange;
	private double angle, turnRate;
	
	public EnemyWanderer(float x, float y, GameState gs) {
		super(x, y, gs, 1);
		angle = Math.random() * 360;
		turnRate = 20;//Math.random() * 40 - 20;
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/Enemy Wanderer.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		rotation -= Gdx.graphics.getDeltaTime();
		Util.drawCentered(batch, texture, x, y, rotation * 90 % 360);
	}
	
	@Override
	public boolean update() {
		//10 ms is the update rate
		timeSinceLastDirChange += 10;
		if(timeSinceLastDirChange > dirChangeRateMs) {
			turnRate = Math.random() * 40 - 20;
			timeSinceLastDirChange = 0;
		}
		angle += turnRate / 180 * Math.PI;
		
		dx = (float) (Math.cos(angle / 180 * Math.PI) * 2);
		dy = (float) (Math.sin(angle / 180 * Math.PI) * 2);
		x += dx * (1 - slowdown);
		y += dy * (1 - slowdown);
		
		//Checking so that the enemy will not get outside the map
		// Left edge
		if(x - 20 < 0) {
			x = 20;
			
			angle = Math.random() * 90 - 45;
		}
		// Right edge
		else if(x + 20 > gs.getCurrentMap().getWidth()){
			x = gs.getCurrentMap().getWidth() - 20;
			
			angle = Math.random() * 90 + 135;
		}
		// Upper edge
		if(y - 20 < 0) {
			y = 20;
			
			angle = Math.random() * 90 + 45;
		}
		// Lower edge
		else if(y + 20 > gs.getCurrentMap().getHeight()){
			y = gs.getCurrentMap().getHeight() - 20;
			
			angle = Math.random() * 90 + 225;
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
