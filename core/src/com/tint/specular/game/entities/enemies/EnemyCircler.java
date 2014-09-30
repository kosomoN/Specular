package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class EnemyCircler extends Enemy {

	private static Animation anim;
	private static AtlasRegion tex, warningTex;
	
	private float offset;
	private double angle;
	
	public EnemyCircler(float x, float y, GameState gs) {
		super(x, y, gs, 1);
		
	    if(Math.random() < 0.5) {
	    	offset = (float) Math.toRadians(60);
	    } else {
            offset = (float) Math.toRadians(-60);
	    }
	    
	    targetSpeed = (float) (Math.random() * 2 + 1);
	}

	@Override
	public void renderEnemy(SpriteBatch batch) {
		Util.drawCentered(batch, tex, x, y, rotation * 100);
	}
	
	@Override
	public void updateMovement() {
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
	}
	
	@Override
	public float getInnerRadius() { return 16; }
	@Override
	public float getOuterRadius() { return 30; }

	public static void init(TextureAtlas ta) {
		tex = ta.findRegion("game1/Enemy Circler");
		
		warningTex = ta.findRegion("game1/Enemy Circler Warning");
		
		AtlasRegion animTex = ta.findRegion("game1/Enemy Circler Anim");
		anim = Util.getAnimation(animTex, 64, 64, 1 / 15f, 0, 0, 3, 1);
	}

	@Override
	public int getValue() {
		return 2;
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public Type getParticleType() {
		return Type.ENEMY_CIRCLER;
	}	

	@Override
	protected Animation getSpawnAnim() {
		return anim;
	}

	@Override
	protected AtlasRegion getWarningTex() {
		return warningTex;
	}

	@Override
	protected float getRotationSpeed() {
		return 100;
	}
	
	@Override
	public Enemy copy() {
		return new EnemyCircler(0, 0, gs);
	}
}
