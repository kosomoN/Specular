package com.tint.specular.game.entities.enemies;

import static com.tint.specular.game.entities.enemies.EnemyVirus.Behavior.POINTLESS;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class EnemyVirus extends Enemy {

	public enum Behavior {
		POINTLESS, FOLLOW;
	}

	private static final float BASE_GROWTH_RATE = 0.0025f;
	private static int spawnedVirusAmount;
	
	private static Animation anim;
	private static AtlasRegion tex, warningTex;
	private float size = 1;
	private float growthRate = (float) (BASE_GROWTH_RATE + BASE_GROWTH_RATE * Math.random());
	private Behavior behavior;
	private double angle;
	
	public EnemyVirus(float x, float y, GameState gs, boolean spawnedFromOtherVirus) {
		super(x, y, gs, 1);
		spawnedVirusAmount++;
		
		double rand = Math.random();
		if(rand > 0.333) {
			behavior = POINTLESS;
		} else {
			behavior = POINTLESS;
		}
		
		angle = Math.random() * Math.PI * 2;
		dx = (float) (Math.cos(angle) * 3);
		dy = (float) (Math.sin(angle) * 3);
		
		if(spawnedFromOtherVirus)
			hasSpawned = true;
		
		targetSpeed = 3;
	}

	@Override
	public void renderEnemy(SpriteBatch batch) {
		Util.drawCentered(batch, tex, (float) x, y, tex.getRegionWidth() * size, tex.getRegionHeight() * size, rotation * 80 % 360);
	}
	
	@Override
	public void updateMovement() {
		switch(behavior) {
		case POINTLESS:
			if(x - 20 - 18 < 0) {
				angle = Math.PI - angle;
				x = 20 + 20;
			}
			 
			if(x + 20 + 18 > gs.getCurrentMap().getHeight()) {
				angle = Math.PI - angle;
				x = gs.getCurrentMap().getHeight() - 20 - 20;
			}
			
			if(y - 20 - 18 < 0) {
				angle = Math.PI * 2 - angle;
				y = 20 + 20;
			}
			
			if(y + 20 + 18 > gs.getCurrentMap().getHeight()){
				angle = Math.PI * 2 - angle;
				y = gs.getCurrentMap().getHeight() - 20 - 20;
			}
			
			dx = (float) (Math.cos(angle) * speed);
			dy = (float) (Math.sin(angle) * speed);

			break;
		case FOLLOW:
			double angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
			dx = (float) (Math.cos(angle) * speed);
			dy = (float) (Math.sin(angle) * speed);
			break;
		}

		
		x += dx * slowdown;
		y += dy * slowdown;
		
		size += growthRate * 10 / spawnedVirusAmount;
		if(size >= 1.5f) {
			size = 1;
			gs.addEntity(new EnemyVirus(x, y, gs, true));
		}
	}

	@Override
	public int getValue() {
		return 1;
	}

	@Override
	public float getInnerRadius() {
		return 16 * size;
	}

	@Override
	public float getOuterRadius() {
		return 30 * size;
	}
	
	public static void init(TextureAtlas ta) {
		tex = ta.findRegion("game1/Enemy Virus");
		
		warningTex = ta.findRegion("game1/Enemy Striver Warning");
		
		AtlasRegion animTex = ta.findRegion("game1/Enemy Striver Anim");
		anim = Util.getAnimation(animTex, 64, 64, 1 / 15f, 0, 0, 3, 1);
	}
	
	public static void resetSpawnedAmount() {
		spawnedVirusAmount = 0;
	}

	@Override
	public Type getParticleType() {
		return Type.ENEMY_VIRUS;
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
		return 80;
	}
	
	@Override
	public Enemy copy() {
		return new EnemyVirus(0, 0, gs, false);
	}
}
