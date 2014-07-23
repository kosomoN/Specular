package com.tint.specular.game.entities.enemies;

import java.util.Random;

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

public class EnemyWanderer extends Enemy {

	private static Animation anim;
	private static AtlasRegion tex, warningTex;
	private static Random random = new Random();
	private float dirChangeRateMs = 2000f;
	private float timeSinceLastDirChange;
	private double angle, turnRate;
	
	public EnemyWanderer(float x, float y, GameState gs) {
		super(x, y, gs, 1);
		angle = random.nextInt(360);
		turnRate = random.nextInt(40) - 20;
		
		speed = (float) (2 + Math.random());
	}
	
	public static void init(TextureAtlas ta) {
		tex = ta.findRegion("game1/Enemy Wanderer");
		
		warningTex = ta.findRegion("game1/Enemy Wanderer Warning");
		
		AtlasRegion animTex = ta.findRegion("game1/Enemy Wanderer Anim");
		anim = Util.getAnimation(animTex, 64, 64, 1 / 15f, 0, 0, 3, 1);
	}
	
	@Override
	public void renderEnemy(SpriteBatch batch) {
		Util.drawCentered(batch, tex, x, y, rotation * 70 % 360);
	}

	@Override
	public void updateMovement() {
		//10 ms is the update rate
		timeSinceLastDirChange += 10;
		if(timeSinceLastDirChange > dirChangeRateMs) {
			turnRate = random.nextInt(40) - 20;
			timeSinceLastDirChange = 0;
		}
		angle += turnRate / 180 * Math.PI;
		
		dx = (float) (Math.cos(angle / 180 * Math.PI) * speed);
		dy = (float) (Math.sin(angle / 180 * Math.PI) * speed);
		x += dx * slowdown;
		y += dy * slowdown;
		
		//Checking so that the enemy will not get outside the map
		// Left edge. 20 for width and 18 for map border
		if(x - 20 - 18 < 0) {
			x = 20 + 18;
			
			angle = random.nextInt(90) - 45;
		}
		// Right edge
		else if(x + 20 + 18 > gs.getCurrentMap().getWidth()){
			x = gs.getCurrentMap().getWidth() - 20 - 18;
			
			angle = random.nextInt(90) + 135;
		}
		// Upper edge
		if(y - 20 - 18 < 0) {
			y = 20 + 18;
			
			angle = random.nextInt(90) + 45;
		}
		// Lower edge
		else if(y + 20 + 18 > gs.getCurrentMap().getHeight()){
			y = gs.getCurrentMap().getHeight() - 20 - 18;
			
			angle = random.nextInt(90) + 225;
		}
	}
	
	@Override
	public int getValue() {
		return 1;
	}

	@Override
	public float getInnerRadius() {
		return 16;
	}

	@Override
	public float getOuterRadius() {
		return 30;
	}
	
	@Override
	public Type getParticleType() {
		return Type.ENEMY_WANDERER;
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
		return 70;
	}
	
	@Override
	public Enemy copy() {
		return new EnemyWanderer(0, 0, gs);
	}
}
