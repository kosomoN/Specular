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

public class EnemyNormal extends Enemy {

	private static Texture tex;
	private float rotation;
	private float offset;
	private double angle;
	private float speed;
	
	public EnemyNormal(float x, float y, GameState gs) {
		super(x, y, gs, 1);
		
	    if(Math.random() < 0.5) {
	    	offset = (float) Math.toRadians(60);
	    } else {
            offset = (float) Math.toRadians(-60);
	    }
	    
	    speed = (float) (Math.random() * 2 + 1);
	}

	@Override
	public void render(SpriteBatch batch) {
		rotation += Gdx.graphics.getDeltaTime();
		Util.drawCentered(batch, tex, x, y, rotation * 90 % 360);
	}
	
	@Override
	public boolean update() {
		if(gs.getPlayer() != null)
			angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x) + offset;
		
		dx = (float) (Math.cos(angle) * speed);
		dy = (float) (Math.sin(angle) * speed);
		x += dx * slowdown;
		y += dy * slowdown;
		
		//Checking map boundaries. 20 for width and 18 for map border
		if(x - 20 - 18 < 0) {
			offset = -offset;
		} else if(x + 20 + 18 > gs.getCurrentMap().getHeight()){
			offset = -offset;
		}
		
		if(y - 20 - 18 < 0) {
			offset = -offset;
		} else if(y + 20 + 18 > gs.getCurrentMap().getHeight()){
			offset = -offset;
		}
		
		
		return super.update();
	}
	
	@Override
	public float getInnerRadius() { return 16; }
	@Override
	public float getOuterRadius() { return 30; }

	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/Enemy Normal.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	public int getValue() {
		return 2;
	}
	
	@Override
	public void dispose() {
		tex.dispose();
	}

	@Override
	public Type getParticleType() {
		return Type.ENEMY_NORMAL;
	}
}
