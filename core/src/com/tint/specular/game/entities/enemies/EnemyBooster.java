package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class EnemyBooster extends Enemy {

	private static final double MAX_TURNANGLE = Math.PI / 256; // 0.012 degrees (per tick)
	private static Animation anim;
	private static Texture tex, warningTex;
	
	private double direction;
	private int boostingDelay;
	public boolean passed;
	
	public EnemyBooster(float x, float y, GameState gs) {
		super(x, y, gs, 3);
	}

	@Override
	public void renderEnemy(SpriteBatch batch) {
		Util.drawCentered(batch, tex, x, y, (float) Math.toDegrees(direction) - 90);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if(hasSpawned)
			renderEnemy(batch);
		else {
			//Show warning image
			rotation = (float) Math.toDegrees(Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x));
			if(spawnTimer < 2f * 60) {
				batch.setColor(1, 1, 1, ((float) Math.cos(spawnTimer / 60f * Math.PI * 2 + Math.PI) + 1) / 2);
				Util.drawCentered(batch, getWarningTex(), x, y, rotation - 90);
				batch.setColor(1, 1, 1, 1);
			} else {
				TextureRegion tr = getSpawnAnim().getKeyFrame(spawnTimer / 60f - 2);
				Util.drawCentered(batch, tr, x, y, rotation - 90);
			}
		}
	}
	
	@Override
	public void updateMovement() {
		// Boosting and changing direction
		
		/* It turns by the half of the angle created when the current direction and target direction are as angle legs
		 * If the angle is 1 Pi rad, which means the player is in the opposite direction of the travelling direction, it's not turning
		 */
		if(boostingDelay > 60) {
			double targetDir = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
			double turn = 0;
			double diffDir = targetDir - direction;
			
			// In case of smaller angles
			if(diffDir >= Math.PI)
				turn = diffDir - 2 * Math.PI;
			else if(diffDir <= -Math.PI)
				turn = diffDir + 2 * Math.PI;
			else if(diffDir < 0 || diffDir > 0)
				turn = diffDir;
			
			// There is a limit of turning angle
			direction += turn < -MAX_TURNANGLE ? -MAX_TURNANGLE : turn > MAX_TURNANGLE ? MAX_TURNANGLE : turn;
		}
		
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
			passed = false;
			boostingDelay++;
			dx = 0;
			dy = 0;
		} else {
			boostingDelay++;
		}
		
		// Checking so that the enemy will not get outside the map
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
	}
	
	@Override
	public float getInnerRadius() { return 16; }
	@Override
	public float getOuterRadius() { return 30; }
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Booster.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		warningTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Booster Warning.png"));
		warningTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture animTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Booster Anim.png"));
		animTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		anim = Util.getAnimation(animTex, 64, 64, 1 / 15f, 0, 0, 3, 3);
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
	
	@Override
	protected Animation getSpawnAnim() {
		return anim;
	}

	@Override
	protected Texture getWarningTex() {
		return warningTex;
	}

	@Override
	protected float getRotationSpeed() {
		return 0;
	}
	
	@Override
	public Enemy copy() {
		return new EnemyBooster(0, 0, gs);
	}
}
