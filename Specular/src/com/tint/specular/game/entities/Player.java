package com.tint.specular.game.entities;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.BoardShock;
import com.tint.specular.game.Camera;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyExploder;
import com.tint.specular.input.AnalogStick;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class Player implements Entity {
	
	public static final int MAX_DELTA_SPEED = 1;
	public static final float FRICTION = 0.95f;
	
	public static Animation anim, spawnAnim, deathAnim;
	public static Texture playerTex, playerSpawnTex, playerDeathTex, shieldTexture, barrelTexture;
	public static int radius;
	
	private GameState gs;
	private AmmoType ammo = AmmoType.BULLET;
	
	private float animFrameTime;
	private float centerx, centery, dx, dy;
	private float timeSinceLastFire, fireRate = 10f;
	private float sensitivity = 1f;
	private float direction;
	private float maxSpeedAreaSquared;
	
	private int life = 3;
	private int shields;
	private int bulletBurst = 3;
	private int bulletBurstLevel;
	private int score = 0;
	
	private boolean tilt;
	
	private boolean dying, spawning, dead;
	private boolean soundEffects;
//-----------------SOUND FX-----------------------	
	
	//Shoot sound
	Sound soundShoot1 = Gdx.audio.newSound(Gdx.files.internal("audio/Shoot.wav"));
	
	//Hit sound
	Sound soundHit1 = Gdx.audio.newSound(Gdx.files.internal("audio/Hit1.wav"));			
	Sound soundHit2 = Gdx.audio.newSound(Gdx.files.internal("audio/Hit2.wav"));	
	Sound soundHit3 = Gdx.audio.newSound(Gdx.files.internal("audio/Hit3.wav"));
			
//---------------SOUND FX END---------------------		

	public enum AmmoType {
		BULLET, LASER
	}

	public Player(GameState gs, float x, float y, int lives) {
		this.gs = gs;
		centerx = x;
		centery = y;
		soundEffects = !Specular.prefs.getBoolean("SoundsMuted");
		setLife(lives);
		
		sensitivity = Specular.prefs.getFloat("Sensitivity");
		maxSpeedAreaSquared = (Specular.camera.viewportWidth / 8 * sensitivity) * (Specular.camera.viewportWidth / 8 * sensitivity);
	}

	public void shootBullet(float direction, int offset, int spaces) {
		//If the amount of bullet "lines" are even
		if(bulletBurst % 2 == 0) {
			for(int j = 0; j < (spaces - 1) / 2 + 1; j++) {
				if(j == 0) {
					gs.addEntity(Bullet.obtainBullet(centerx, centery, direction + offset / 2, dx, dy));
					gs.addEntity(Bullet.obtainBullet(centerx, centery, direction - offset / 2, dx, dy));
				} else {
					gs.addEntity(Bullet.obtainBullet(centerx, centery, direction + offset / 2 + j * offset, dx, dy));
					gs.addEntity(Bullet.obtainBullet(centerx, centery, direction - offset / 2 - j * offset, dx, dy));
				}
			}
			
		//If the number of bullet "lines" are odd
		} else {
			gs.addEntity(Bullet.obtainBullet(centerx, centery, direction, dx, dy));

			for(int i = 0; i < spaces / 2; i++) {
				gs.addEntity(Bullet.obtainBullet(centerx, centery, direction + (i + 1) * offset, dx, dy));
				gs.addEntity(Bullet.obtainBullet(centerx, centery, direction - (i + 1) * offset, dx, dy));
			}
		}
	}
	
	private void shootLaser(float direction) {
		float dist1 = Integer.MAX_VALUE, dist2 = Integer.MAX_VALUE, dist3 = Integer.MAX_VALUE;
		Enemy e1 = null, e2 = null, e3 = null;
		for(Enemy e : gs.getEnemies()) {
			if(!e.hasSpawned())
				continue;
			
			float distance = Util.getDistanceSquared(centerx, centery, e.getX(), e.getY());
			
			if(dist3 > distance) {
				double angleToEnemy = Math.toDegrees(Math.atan2(e.getY() - centery, e.getX() - centerx));
				double deltaAngle = (angleToEnemy - direction + 360) % 360;
				if(deltaAngle > 180) 
					deltaAngle = 360 - deltaAngle;
				
				if(deltaAngle < 30) {
					if(dist1 > distance) {
						dist3 = dist2;
						dist2 = dist1;
						dist1 = distance;
						
						e3 = e2;
						e2 = e1;
						e1 = e;
					} else if(dist2 > distance) {
						dist3 = dist2;
						dist2 = distance;
						
						e3 = e2;
						e2 = e;
					} else if(dist1 > distance) {
						dist3 = distance;
						e3 = e;
					}
				}
			}
		}
		
		if(e1 != null) {
			e1.hit(1);
			Camera.shake(0.1f, 0.05f);
			gs.enemyHit(e1);
			gs.addEntity(Laser.obtainLaser(centerx, centery, e1.getX(), e1.getY(), 0));
		} else {
			shootLaserInDir(direction, 0);
		}
		
		if(e2 != null) {
			e2.hit(1);
			Camera.shake(0.1f, 0.05f);
			gs.enemyHit(e2);
			gs.addEntity(Laser.obtainLaser(centerx, centery, e2.getX(), e2.getY(), -1));
		} else {
			shootLaserInDir(direction - 8, -1);
		}
		
		if(e3 != null) {
			e3.hit(1);
			Camera.shake(0.1f, 0.05f);
			gs.enemyHit(e3);
			gs.addEntity(Laser.obtainLaser(centerx, centery, e3.getX(), e3.getY(), 1));
		} else {
			shootLaserInDir(direction + 8, 1);
		}
	}
	
	private void shootLaserInDir(float dir, int barrelIndex) {
		float sin = (float) Math.sin(Math.toRadians(dir));
		float cos = (float) Math.cos(Math.toRadians(dir));
		float x2 = 0, y2 = 0;
		if(cos < 0) {
			x2 = 0;
			y2 = centery + sin * centerx / -cos;
		} else {
			x2 = gs.getCurrentMap().getWidth();
			y2 = centery + sin * (gs.getCurrentMap().getWidth() - centerx) / cos;
		}
		
		if(y2 < 0) {
			y2 = 0;
			x2 = centerx + cos * centery / -sin;
		} else if(y2 > gs.getCurrentMap().getHeight()) {
			y2 = gs.getCurrentMap().getHeight();
			x2 = centerx + cos * (gs.getCurrentMap().getHeight() - centery) / sin;
		}
		
		gs.addEntity(Laser.obtainLaser(centerx, centery, x2, y2, barrelIndex));
	}
	
	@Override
	public void render(SpriteBatch batch) {
		
		if(spawning) {
			// Spawn animation
			TextureRegion frame = spawnAnim.getKeyFrame(animFrameTime, false);
			batch.draw(frame, centerx - frame.getRegionWidth() / 2, centery - frame.getRegionHeight() / 2);
			if(spawnAnim.isAnimationFinished(animFrameTime)) {
				spawning = false;
				animFrameTime = 0;
			}	
		} else if(dying) {
			// Death animation
			TextureRegion frame = deathAnim.getKeyFrame(animFrameTime, false);
			batch.draw(frame, centerx - frame.getRegionWidth() / 2, centery - frame.getRegionHeight() / 2);
			if(deathAnim.isAnimationFinished(animFrameTime)) {
				dying  =  false;
				dead = true;
				animFrameTime = 0;
			}
		} else {
			TextureRegion baseAnimFrame = anim.getKeyFrame(animFrameTime, true);
			batch.draw(baseAnimFrame, centerx - baseAnimFrame.getRegionWidth() / 2, centery - baseAnimFrame.getRegionHeight() / 2);
			Util.drawCentered(batch, barrelTexture, getX(), getY(), direction);
			for(int i = 0; i < shields; i++) {
				Util.drawCentered(batch, shieldTexture, getX(), getY(), -animFrameTime * 360 + 360f * i / shields);
			}
			
		}
	}
	
	@Override
	public boolean update() {
		// Taking control away when player is hit and respawning
		animFrameTime += 1 / 60f;
		
		if(!dying && !spawning) {
			updateMovement();
			updateShooting();
		} else if(spawning) {
			float distanceSquared;
			double angle;
			for(Enemy e : gs.getEnemies()) {
				distanceSquared = (e.getX() - getX()) * (e.getX() - getX()) + (e.getY() - getY()) * (e.getY() - getY());
				if(250000 > distanceSquared) {
					angle = Math.atan2(e.getY() - getY(), e.getX() - getX());
					e.setX((float) (e.getX() + Math.cos(angle) * 20 * (1 - distanceSquared / 250000)));
					e.setY((float) (e.getY() + Math.sin(angle) * 20 * (1 - distanceSquared / 250000)));
				}
			}
		}
		
		//Movement
        dx *= FRICTION;
        dy *= FRICTION;
        
        if(centerx - getRadius() + dx < 8)
        	dx = -dx * 0.6f;
        else if(centerx + getRadius() + dx > gs.getCurrentMap().getWidth() - 8)
        	dx = -dx * 0.6f;
        
        if(centery - getRadius() + dy < 8)
        	dy = -dy * 0.6f;
        else if(centery + getRadius() + dy > gs.getCurrentMap().getHeight() - 8)
        	dy = -dy * 0.6f;
        
        centerx += dx;
        centery += dy;
        
        return life <= 0;
	}
	
	public void updateMovement() {
		if(gs.getGameProcessor().isWDown())
			changeSpeed(0, 0.6f);
		if(gs.getGameProcessor().isADown())
			changeSpeed(-0.6f, 0);
		if(gs.getGameProcessor().isSDown())
			changeSpeed(0, -0.6f);
		if(gs.getGameProcessor().isDDown())
			changeSpeed(0.6f, 0);
		
		if(tilt) {
			changeSpeed(Gdx.input.getAccelerometerX() * 0.1f * 0.6f, Gdx.input.getAccelerometerY() * 0.1f * 0.6f);
		} else {
			
			AnalogStick moveStick = gs.getGameProcessor().getMoveStick();
			float moveDx = moveStick.getXHead() - moveStick.getXBase();
			float moveDy = moveStick.getYHead() - moveStick.getYBase();
			float distBaseToHead = moveDx * moveDx + moveDy * moveDy;
			
			if(moveStick.isActive() && distBaseToHead != 0) {
				//calculating the angle with delta x and delta y
				double angle = Math.atan2(moveDy, moveDx);
				changeSpeed(
						(float) Math.cos(angle) * MAX_DELTA_SPEED *
						(distBaseToHead >= maxSpeedAreaSquared ? 1 : distBaseToHead / maxSpeedAreaSquared),
						
						(float) Math.sin(angle) * MAX_DELTA_SPEED *
						(distBaseToHead >= maxSpeedAreaSquared ? 1 : distBaseToHead / maxSpeedAreaSquared)
						);
			}
		}
	}
	
	public void updateShooting() {
		timeSinceLastFire += 1;
		AnalogStick shootStick = gs.getGameProcessor().getShootStick();
		direction = (float) (Math.toDegrees(Math.atan2(shootStick.getYHead() - shootStick.getYBase(), shootStick.getXHead() - shootStick.getXBase())));
		if(shootStick.isActive()) {
			if(timeSinceLastFire >= fireRate) {
				switch(ammo) {
				case BULLET:
					int spaces = bulletBurst - 1;
					int offset = 8;
					
					shootBullet(direction, offset, spaces);
					if(soundEffects)
						soundShoot1.play(0.7f, (float) (1 + Math.random() / 3 - 0.16), 0);
					break;
				case LASER:
					shootLaser(direction);
					break;
				}
				//The amount of spaces, i.e. two bullet "lines" have one space between them
				
				timeSinceLastFire = 0;
			}
		}
	}
	
	public void updateHitDetection() {
        boolean clearEnemies = false;
		for(Iterator<Enemy> it = gs.getEnemies().iterator(); it.hasNext(); ) {
        	Enemy e = it.next();
    		if(!(e instanceof EnemyExploder) && e.hasSpawned() && e.getLife() > 0) {
    			float distX = centerx - e.getX();
    			float distY = centery - e.getY();
        		if(distX * distX + distY * distY < (getRadius() + e.getInnerRadius()) * (getRadius() + e.getInnerRadius())) {
                	e.hit(e.getLife());
    				gs.getEntities().removeValue(e, true);
    				it.remove();
    				
        			//Make the player invincible when the boardshock is activated to avoid wasting and annoying moments
        			if(BoardShock.isActivated()) {
        				break;
        			} else if(shields > 0) {
	        			//Repel effect after collision with shield
//	        			setSpeed(dx + e.getDx(), dy + e.getDy());
	        			
	        			shields--;
        			} else {
        				Specular.nativeAndroid.sendAnalytics("Death", String.valueOf(gs.getCurrentWave().getID()), e.getClass().getSimpleName(), null);
        				
    					addLives(-1);
    					dying = true;
    					
    					//Hit sound (randomize)
//    					int randomNum = rand.nextInt(3);
//    					if (randomNum == 0) { soundHit1.play(1.0f); } else if (randomNum == 1) { soundHit2.play(1.0f); } else { soundHit3.play(1.0f); }
    					animFrameTime = 0;
    					clearEnemies = true;
    					
    					gs.getParticleSpawnSystem().spawn(Type.BULLET, getX(), getY(), getDeltaX(), getDeltaY(), 20, false);
    					
    					break;
        			}	
        		}	
    		}
        }	
		
        if(clearEnemies) {
        	gs.clearEnemies();
        }
	}
	
	public void addShield() {
		if(shields < 3)
			shields++;
	}
	
	public void addLives(int livesToAdd) {
		life = life + livesToAdd < 4 ? life + livesToAdd : 4;
	}
	
	public void addScore(int score) {
		this.score += score * gs.getScoreMultiplier();
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void changeSpeed(float dx, float dy) {
		this.dx += dx;
		this.dy += dy;
	}
	
	public void setSpeed(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setCenterPosition(float x, float y) {
		centerx = x;
		centery = y;
	}
	
	/**
	 * Takes the amount of updates as rate of fire, not any specific "time"
	 * @param fireRate
	 */
	public void setFireRate(float fireRate) { this.fireRate = fireRate; }
	public void setBulletBurst(int burst) { bulletBurst = burst; }
	public void setBulletBurstLevel(int level) { bulletBurstLevel = level; }
	public void addBulletBurstLevel(int level) { bulletBurstLevel += level; }
	
	public void changeAmmo(AmmoType ammo) { this.ammo = ammo; }
	
	public void setLife(int life) { this.life = life; }
	public void kill() {
		addLives(-1);
		dying = true;
		
		//Hit sound (randomize)
//		int randomNum = rand.nextInt(3);
//		if (randomNum == 0) { soundHit1.play(1.0f); } else if (randomNum == 1) { soundHit2.play(1.0f); } else { soundHit3.play(1.0f); }
		animFrameTime = 0;
		gs.clearEnemies();
		
		System.out.println("Kill");
	}
	
	
	@Override public float getX() { return centerx; }
	@Override public float getY() { return centery;	}
	public float getDeltaX() { return dx; }
	public float getDeltaY() { return dy; }
	public float getDirection() { return direction; }
	public float getFireRate() { return fireRate; }
	public float getTimeSinceLastFire() { return timeSinceLastFire; }
	public static float getRadius() { return radius; }
	public int getLife() { return life;	}
	public int getBulletBurst() { return bulletBurst; }
	public int getBulletBurstLevel() { return bulletBurstLevel; }
	public int getScore() { return score; }
	public int getShields() { return shields; }

	public boolean hasShield() { return shields > 0; }
	public boolean isDying() { return dying; }
	public boolean isSpawning() { return spawning; }
	public boolean isDead() { return dead; }
	public GameState getGameState() { return gs; }
	public AmmoType getAmmoType() { return ammo; }
	
	public void respawn() {
		animFrameTime = 0;
		spawning = true;
		dead = false;
		tilt = Specular.prefs.getBoolean("Tilt");
	}
	
	public static void init() {
		playerTex  = new Texture(Gdx.files.internal("graphics/game/Player.png"));
		anim = Util.getAnimation(playerTex, 128, 128, 1 / 16f, 0, 0, 7, 3);
		
		playerSpawnTex  = new Texture(Gdx.files.internal("graphics/game/Player Spawn Anim.png"));
		spawnAnim = Util.getAnimation(playerSpawnTex, 128, 128, 1 / 16f, 0, 0, 4, 3);
		
		playerDeathTex  = new Texture(Gdx.files.internal("graphics/game/Player Death Anim.png"));
		deathAnim = Util.getAnimation(playerDeathTex, 128, 128, 1 / 16f, 0, 0, 3, 3);
		
		radius = (anim.getKeyFrame(0).getRegionWidth() - 10) / 2;
		
		shieldTexture = new Texture(Gdx.files.internal("graphics/game/Shield.png"));
		
		barrelTexture = new Texture(Gdx.files.internal("graphics/game/Barrels.png"));
	}
	
	@Override
	public void dispose() {
		playerTex.dispose();
	}
	
	public float getBarrelPosX(int barrelIndex) {
		return (float) (centerx + Math.cos(Math.toRadians(direction + barrelIndex * 8)) * 60);
	}
	
	public float getBarrelPosY(int barrelIndex) {
		return (float) (centery + Math.sin(Math.toRadians(direction + barrelIndex * 8)) * 60);
	}
}
