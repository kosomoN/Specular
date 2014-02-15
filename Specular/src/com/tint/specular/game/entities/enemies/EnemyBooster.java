package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class EnemyBooster extends Enemy {

	private static Texture tex;
	
	private float speed;
	private double direction;
	private int boostingDelay;
	
	public EnemyBooster(float x, float y, GameState gs) {
		super(x, y, gs, 3);
	}

	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, tex, x, y, (float) Math.toDegrees(direction) - 90);
	}
	
	@Override
	public boolean update() {
		//Boosting and changing direction
		if(boostingDelay > 30) {
			speed += 0.1;
			
			dx = (float) (Math.cos(direction) * speed);
			dy = (float) (Math.sin(direction) * speed);
			x += dx * slowdown;
			y += dy * slowdown;
			
			boostingDelay++;
		} else if(boostingDelay == 0) {
			if(gs.getPlayer() != null)
				direction = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
			
			speed = 0;
			boostingDelay++;
		} else {
			boostingDelay++;
		}
		
		//Checking so that the enemy will not get outside the map
		if(x - 20 - 18 < 0) {
			x = 20 + 18;
			boostingDelay = 0;
		} else if(x + 20 + 18 > gs.getCurrentMap().getWidth()){
			x = gs.getCurrentMap().getWidth() - 20 - 18;
			boostingDelay = 0;
		}
		
		if(y - 20 - 18 < 0) {
			y = 20 + 18;
			boostingDelay = 0;
		} else if(y + 20 + 18 > gs.getCurrentMap().getHeight()){
			y = gs.getCurrentMap().getHeight() - 20 - 18;
			boostingDelay = 0;
		}
		
		return super.update();
	}

	@Override
	public float getInnerRadius() { return 16; }
	@Override
	public float getOuterRadius() { return 30; }
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Booster.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	public int getValue() {
		return 5;
	}
	
	@Override
	public void dispose() {
		tex.dispose();
	}

	@Override
	public Type getParticleType() {
		return Type.ENEMY_BOOSTER;
	}
}
