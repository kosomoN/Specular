package com.tint.specular.game.entities.enemies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 * 
 */


public class EnemyExploder extends Enemy {

	private static final int EXPLODE_RANGE_SQUARED = 400 * 400;
	private Random random = new Random();
	private boolean exploded;
	
	// Animation
	private static Animation spawnAnim, anim;
	private static Texture warningTex, explotionTex;
	
	// Movement
	private int timeSinceLastDirChange;
	private int dirChangeRate = 200;
	private double angle, turnRate;
	
	public EnemyExploder(float x, float y, GameState gs) {
		super(x, y, gs, 2);
		speed = 0.5f;
	}

	public static void init() {
		Texture spawnAnimTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Exploder Anim.png"));
		spawnAnim = Util.getAnimation(spawnAnimTex, 128, 128, 1 / 16f, 0, 0, 3, 1);
		
		Texture animTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Exploder.png"));
		animTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		anim = Util.getAnimation(animTex, 128, 128, 1 / 16f, 0, 0, 3, 1);
		
		explotionTex = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Circle.png"));
		explotionTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		warningTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Exploder Warning.png"));
		warningTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	@Override
	protected void renderEnemy(SpriteBatch batch) {
		// Normal animation
		TextureRegion frame = anim.getKeyFrame(rotation, true);
		Util.drawCentered(batch, frame, x, y, rotation * 10 % 360);
	}
	
	@Override
	public boolean update() {
		if(super.update() && !exploded)
			explode();

		return exploded;
	}

	@Override
	public void updateMovement() {
		timeSinceLastDirChange++;
		
		if(timeSinceLastDirChange > dirChangeRate) {
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
	
	private void explode() {
		float distanceSquared;
		double angle;
		
		// Enemies
		for(Enemy e : gs.getEnemies()) {
			if(!e.equals(this)) {
				distanceSquared = (e.getX() - getX()) * (e.getX() - getX()) + (e.getY() - getY()) * (e.getY() - getY());
				if(distanceSquared - 32 * 32 < EXPLODE_RANGE_SQUARED && distanceSquared + 32 * 32 > EXPLODE_RANGE_SQUARED) {
					angle = Math.atan2(e.getY() - getY(), e.getX() - getX());
					
					// Explosion power is how much the enemy is pushed
					float explotionPower = 10 * (1 - distanceSquared / EXPLODE_RANGE_SQUARED);
					float damage = (1f - (distanceSquared / EXPLODE_RANGE_SQUARED));
					
					e.addDx((float) (Math.cos(angle) * explotionPower));
					e.addDy((float) (Math.sin(angle) * explotionPower));
					
					damage = damage < 0 ? 0 : damage;
					e.hit(damage);
				}
			}
		}
		
		// Player
		Player player = gs.getPlayer();
		distanceSquared = (player.getX() - getX()) * (player.getX() - getX()) + (player.getY() - getY()) * (player.getY() - getY());
		angle = Math.atan2(player.getY() - getY(), player.getX() - getX());
		
		if(distanceSquared < EXPLODE_RANGE_SQUARED) {
			player.setSpeed(player.getDeltaX() + (float) (Math.cos(angle) * 20 * (1 - distanceSquared / EXPLODE_RANGE_SQUARED)),
					player.getDeltaY() + (float) (Math.sin(angle) * 20 * (1 - distanceSquared / EXPLODE_RANGE_SQUARED)));
			player.kill();
		} else {
			exploded = true;
		}
	}

	@Override
	public int getValue() {
		return 4;
	}

	@Override
	public Type getParticleType() {
		return Type.ENEMY_WANDERER;
	}
	
	@Override
	public float getInnerRadius() {	return 45; }

	@Override
	public float getOuterRadius() {	return 64; }

	@Override
	protected Animation getSpawnAnim() {
		return spawnAnim;
	}

	@Override
	protected Texture getWarningTex() {
		return warningTex;
	}

	@Override
	protected float getRotationSpeed() {
		return 10;
	}

}
