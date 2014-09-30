package com.tint.specular.game.entities.enemies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.game.Camera;
import com.tint.specular.game.GameState;
import com.tint.specular.game.ShockWaveRenderer;
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
	private boolean exploded, explosionDone;
	
	// Animation and graphics
	private static Animation spawnAnim, anim;
	private static AtlasRegion warningTex, explosionWarningTex;
	private static Texture explosionTex1;
	private static AtlasRegion explosionWarningTex2;
	private int shockWaveTime;
	
	private Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("audio/fx/Explosion.ogg"));	
	
	// Movement
	private int timeSinceLastDirChange;
	private int dirChangeRate = 200;
	private double angle, turnRate;
	
	public EnemyExploder(float x, float y, GameState gs) {
		super(x, y, gs, 2);
		targetSpeed = 0.5f;
		rotation = 0;
		shockWaveTime = 0;
	}

	public static void init(TextureAtlas ta) {
		AtlasRegion spawnAnimTex = ta.findRegion("game1/Enemy Exploder Anim");
		spawnAnim = Util.getAnimation(spawnAnimTex, 128, 128, 1 / 16f, 0, 0, 3, 1);
		
		AtlasRegion animTex = ta.findRegion("game1/Enemy Exploder");
		anim = Util.getAnimation(animTex, 128, 128, 1 / 16f, 0, 0, 3, 1);
		
		explosionTex1 = ta.findRegion("ExploderEffectAlternate").getTexture();
		
		explosionWarningTex2 = ta.findRegion("ExploderEffectInner");
		
		explosionWarningTex = ta.findRegion("ExploderEffectWarning");
		
		warningTex = ta.findRegion("game1/Enemy Exploder Warning");
	}
	
	@Override
	protected void renderEnemy(SpriteBatch batch) {
		if(!exploded) {
			batch.setColor(1, 1, 1, 0.3f * (rotation - 8 / 15f - 2) < 0 ? 0 : 0.3f * (rotation - 8 / 15f - 2) > 1 ? 1 : 0.3f * (rotation - 8 / 15f - 2));
			
			// Explosion Warning
			Util.drawCentered(batch, explosionWarningTex, x, y, rotation * 1.2f);
			Util.drawCentered(batch, explosionWarningTex2, x, y,  rotation * -1.2f);
			
			batch.setColor(1, 1, 1, 1);
			// Normal animation
			TextureRegion frame = anim.getKeyFrame(rotation, true);
			Util.drawCentered(batch, frame, x, y, rotation * 10 % 360);
		} else {
			shockWaveTime++;
			
			// Explosion
			if(shockWaveTime < 10) {
				batch.setColor(1, 1, 1, 1);
				Texture origTex = ShockWaveRenderer.getShockwaveTexture();
				ShockWaveRenderer.setShockwaveTexture(explosionTex1);
				ShockWaveRenderer.renderShockwave(batch, x, y, shockWaveTime / 10f, false);
				ShockWaveRenderer.setShockwaveTexture(origTex);
			} else {
				explosionDone = true;
			}
		}
	}
	
	@Override
	public boolean update() {
		if(super.update() && !exploded)
			explode();
		return explosionDone;
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
		
		if(gs.isSoundEnabled())
			explosionSound.play();
		
		// Enemies
		for(Enemy e : gs.getEnemies()) {
			if(!e.equals(this)) {
				distanceSquared = (e.getX() - getX()) * (e.getX() - getX()) + (e.getY() - getY()) * (e.getY() - getY());
				if(distanceSquared < EXPLODE_RANGE_SQUARED) {
					angle = Math.atan2(e.getY() - getY(), e.getX() - getX());
					
					// Explosion power is how much the enemy is pushed
					float explosionPower = 10 * (1 - distanceSquared / EXPLODE_RANGE_SQUARED);
					float damage = 20 * (1f - (distanceSquared / EXPLODE_RANGE_SQUARED));
					
					e.addDx((float) (Math.cos(angle) * explosionPower));
					e.addDy((float) (Math.sin(angle) * explosionPower));
					
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
			Array<Enemy> list = new Array<Enemy>();
			list.add(this);
			player.kill(list);
		}
		Camera.shake(0.9f, 0.03f);
		exploded = true;
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
	protected AtlasRegion getWarningTex() {
		return warningTex;
	}

	@Override
	protected float getRotationSpeed() {
		return 10;
	}

	@Override
	public Enemy copy() {
		return new EnemyExploder(0, 0, gs);
	}
}
