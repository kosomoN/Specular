package com.tint.specular.game.entities;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.BoardShock;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.input.AnalogStick;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class Player implements Entity {
	
	private static final int MAX_DELTA_SPEED = 8;
	private static final float SPEED = 0.125f;
	private static final float FRICTION = 0.95f;
	
	private static Animation anim, spawnAnim, deathAnim;
	private static Texture playerTex, playerSpawnTex, playerDeathTex, shieldTexture, barrelTexture;
	private static int radius;
	
	private GameState gs;
	
	private float animFrameTime;
	private float centerx, centery, dx, dy;
	private float timeSinceLastFire, fireRate = 10f;
	private float direction;
	
	private int life = 3;
	private int shields;
	private int bulletBurst = 3;
	private int score = 0;
	
	private boolean isHit, spawning, dead;
	private boolean shot90, shot180;
//-----------------SOUND FX-----------------------	
	
	//Shoot sound
	Sound soundShoot1 = Gdx.audio.newSound(Gdx.files.internal("audio/Shoot.wav"));
	
	//Hit sound
	Sound soundHit1 = Gdx.audio.newSound(Gdx.files.internal("audio/Hit1.wav"));			
	Sound soundHit2 = Gdx.audio.newSound(Gdx.files.internal("audio/Hit2.wav"));	
	Sound soundHit3 = Gdx.audio.newSound(Gdx.files.internal("audio/Hit3.wav"));	
			
//---------------SOUND FX END---------------------		
		

	public Player(GameState gs, float x, float y, int lives) {
		this.gs = gs;
		centerx = x;
		centery = y;
		setLife(lives);
	}

	private void shoot(float direction, int offset, int spaces) {
		//If the amount of bullet "lines" are even
		if(bulletBurst % 2 == 0) {
			for(int j = 0; j < (spaces - 1) / 2 + 1; j++) {
				if(j == 0) {
					gs.addEntity(new Bullet(centerx, centery, direction + offset / 2, dx, dy, gs, this));
					gs.addEntity(new Bullet(centerx, centery, direction - offset / 2, dx, dy, gs, this));
				} else {
					gs.addEntity(new Bullet(centerx, centery, direction + offset / 2 + j * offset, dx, dy, gs, this));
					gs.addEntity(new Bullet(centerx, centery, direction - offset / 2 - j * offset, dx, dy, gs, this));
				}
			}
			
		//If the number of bullet "lines" are odd
		} else {
			gs.addEntity(new Bullet(centerx, centery, direction, dx, dy, gs, this));

			for(int i = 0; i < spaces / 2; i++) {
				gs.addEntity(new Bullet(centerx, centery, direction + (i + 1) * offset, dx, dy, gs, this));
				gs.addEntity(new Bullet(centerx, centery, direction - (i + 1) * offset, dx, dy, gs, this));
			}
		}
	}
	

	
	@Override
	public void render(SpriteBatch batch) {

		animFrameTime += Gdx.graphics.getDeltaTime();
		if(spawning) {
			// Spawn animation
			TextureRegion frame = spawnAnim.getKeyFrame(animFrameTime, false);
			batch.draw(frame, centerx - frame.getRegionWidth() / 2, centery - frame.getRegionHeight() / 2);
			if(spawnAnim.isAnimationFinished(animFrameTime)) {
				spawning = false;
				animFrameTime = 0;
			}	
			} else if(isHit) {
			// Death animation
			TextureRegion frame = deathAnim.getKeyFrame(animFrameTime, false);
			batch.draw(frame, centerx - frame.getRegionWidth() / 2, centery - frame.getRegionHeight() / 2);
			if(deathAnim.isAnimationFinished(animFrameTime)) {
				isHit  =  false;
				dead = true;
				animFrameTime = 0;
			}
		} else {
			TextureRegion baseAnimFrame = anim.getKeyFrame(animFrameTime, true);
			Util.drawCentered(batch, barrelTexture, getCenterX(), getCenterY(), direction);
			batch.draw(baseAnimFrame, centerx - baseAnimFrame.getRegionWidth() / 2, centery - baseAnimFrame.getRegionHeight() / 2);
			for(int i = 0; i < shields; i++) {
				Util.drawCentered(batch, shieldTexture, getCenterX(), getCenterY(), -animFrameTime * 360 + 360f * i / shields);
			}

	}
}

	
	@Override
	public boolean update() {
		// Taking control away when player is hit and respawning
		if(!isHit && !spawning) {
			updateMovement();
			updateShooting();
		}
		
		//Movement
        dx *= FRICTION;
        dy *= FRICTION;
        
        if(centerx - getRadius() + dx < 23)
        	dx = -dx * 0.6f;
        else if(centerx + getRadius() + dx > gs.getCurrentMap().getWidth() - 23)
        	dx = -dx * 0.6f;
        
        if(centery - getRadius() + dy < 23)
        	dy = -dy * 0.6f;
        else if(centery + getRadius() + dy > gs.getCurrentMap().getHeight() - 23)
        	dy = -dy * 0.6f;
        
        centerx += dx;
        centery += dy;
        
        return dead && life <= 0;
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
		/*
		changeSpeed(Gdx.input.getAccelerometerX() * 0.1f * 0.6f,
				Gdx.input.getAccelerometerY() * 0.1f * 0.6f);*/
		
		float aFourthOfWidthSqrd = Specular.camera.viewportWidth / 8 * Specular.camera.viewportWidth / 8;
		
		AnalogStick moveStick = gs.getGameProcessor().getMoveStick();;
		float moveDx = moveStick.getXHead() - moveStick.getXBase();
		float moveDy = moveStick.getYHead() - moveStick.getYBase();
		float distBaseToHead = moveDx * moveDx + moveDy * moveDy;
				
		
		if(moveStick.isActive() && distBaseToHead != 0) {
			//calculating the angle with delta x and delta y
			double angle = Math.atan2(moveDy, moveDx);
			
			changeSpeed(
					(float) Math.cos(angle) * MAX_DELTA_SPEED *
					(distBaseToHead >= aFourthOfWidthSqrd ? 1 : distBaseToHead / aFourthOfWidthSqrd) * SPEED, //Change the last number to alter the sensitivity
					
					(float) Math.sin(angle) * MAX_DELTA_SPEED *
					(distBaseToHead >= aFourthOfWidthSqrd ? 1 : distBaseToHead / aFourthOfWidthSqrd) * SPEED
					);
		}
	}
	
	public void updateShooting() {
		timeSinceLastFire += 1;
		direction = (float) (Math.toDegrees(Math.atan2(gs.getGameProcessor().getShootStick().getYHead()
				- gs.getGameProcessor().getShootStick().getYBase(), gs.getGameProcessor().getShootStick().getXHead()
				- gs.getGameProcessor().getShootStick().getXBase())));
		if(gs.getGameProcessor().getShootStick().isActive()) {
			if(timeSinceLastFire >= fireRate) {
				
				float direction = (float) (Math.toDegrees(Math.atan2(gs.getGameProcessor().getShootStick().getYHead()
					- gs.getGameProcessor().getShootStick().getYBase(), gs.getGameProcessor().getShootStick().getXHead()
					- gs.getGameProcessor().getShootStick().getXBase())));

				//The amount of spaces, i.e. two bullet "lines" have one space between them
				int spaces = bulletBurst - 1;
				int offset = 8;
				
				// Straight
				shoot(direction, offset, spaces);
				
				if(shot180) {
					// Backwards
					direction += 180;
					direction = direction % 360;
					
					shoot(direction, offset, 2);
				}
				
				if(shot90) {
					// Right
					direction += 90;
					direction = direction % 360;
					shoot(direction, offset, 2);
					
					// Left
					direction -= 180;
					direction = direction  % 360;
					shoot(direction, offset, 2);
				}
				
				timeSinceLastFire = 0;
				
				//Hit sound (randomize)
				soundShoot1.play(1.0f);
			}
		}
	}
	
	public void updateHitDetection() {
	
	
        boolean clearEnemies = false;
		for(Iterator<Enemy> it = gs.getEnemies().iterator(); it.hasNext(); ) {
        	Enemy e = it.next();
    		if(e.getLife() > 0) {
    			float distX = centerx - e.getX();
    			float distY = centery - e.getY();
        		if(distX * distX + distY * distY < (getRadius() + e.getInnerRadius()) * (getRadius() + e.getInnerRadius())) {
        			
        			//Make the player invincible when the boardshock is activated to avoid wasting and annoying moments
        			if(BoardShock.isActivated()) {
                    	e.hit(e.getLife());
        				gs.getEntities().removeValue(e, true);
        				it.remove();
        				
        				break;
        			} else if(shields > 0) {
	        			//Repel effect after collision with shield
//	        			setSpeed(dx + e.getDx(), dy + e.getDy());
	        			
	        			gs.getEntities().removeValue(e, true);
        				it.remove();
	        			shields--;
        			} else {
    					addLives(-1);
    					isHit = true;
    					
    					//Hit sound (randomize)
//    					int randomNum = rand.nextInt(3);
//    					if (randomNum == 0) { soundHit1.play(1.0f); } else if (randomNum == 1) { soundHit2.play(1.0f); } else { soundHit3.play(1.0f); }
    					animFrameTime = 0;
    					clearEnemies = true;
    					
    					// Particles
    					/*
    					if(life <= 0) {
    						gs.getParticleSpawnSystem().spawn(this, 200, false);
    						gs.getParticleSpawnSystem().spawn(this, 80, true);
    					} else {
    						gs.getParticleSpawnSystem().spawn(this, 100, false);
    					}*/
    					
    					break;
        			}	
        		}	
    		}
        }	
		
        if(clearEnemies) {
        	// Removing all enemies from lists
        	for(Iterator<Enemy> it = gs.getEnemies().iterator(); it.hasNext(); ) {
            	Enemy e = it.next();
            	e.hit(e.getLife());
            	gs.getEntities().removeValue(e, true);
            	it.remove();
        	}
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
	public void setFireRate(float fireRate) {
		this.fireRate = fireRate;
	}
	
	public void setBulletBurst(int burst) {
		bulletBurst = burst;
	}
	
	public void setShot90(boolean shot90) {
		this.shot90 = shot90;
	}
	
	public void setShot180(boolean shot180) {
		this.shot180 = shot180;
	}
	
	public void setLife(int life) { this.life = life; }
	public void setHit(boolean hit) { isHit = hit; }
	public void kill() { life = 0; }
	
	public float getCenterX() { return centerx; }
	public float getCenterY() { return centery;	}
	public float getDeltaX() { return dx; }
	public float getDeltaY() { return dy; }
	public float getFireRate() { return fireRate; }
	public static float getRadius() { return radius; }
	public int getLife() { return life;	}
	public int getBulletBurst() { return bulletBurst; }
	public int getScore() { return score; }
	public int getShields() {
		return shields;
	}

	public boolean hasShield() { return shields > 0; }
	public boolean isHit() { return isHit; }
	public boolean isSpawning() { return spawning; }
	public boolean isDead() { return dead; }
	public GameState getGameState() { return gs; }
	
	public void respawn() {
		animFrameTime = 0;
		spawning = true;
		dead = false;
	}
	
	public static void init() {
		playerTex  = new Texture(Gdx.files.internal("graphics/game/Player.png"));
		playerTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		anim = Util.getAnimation(playerTex, 128, 128, 1 / 16f, 0, 0, 7, 3);
		
		playerSpawnTex  = new Texture(Gdx.files.internal("graphics/game/Player Spawn Anim.png"));
		playerSpawnTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		spawnAnim = Util.getAnimation(playerSpawnTex, 128, 128, 1 / 16f, 0, 0, 3, 3);
		
		playerDeathTex  = new Texture(Gdx.files.internal("graphics/game/Player Death Anim.png"));
		playerDeathTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		deathAnim = Util.getAnimation(playerDeathTex, 128, 128, 1 / 16f, 0, 0, 3, 3);
		
		radius = (anim.getKeyFrame(0).getRegionWidth() - 10) / 2;
		
		shieldTexture = new Texture(Gdx.files.internal("graphics/game/Shield.png"));
		
		barrelTexture = new Texture(Gdx.files.internal("graphics/game/Barrels.png"));
	}
	
	@Override
	public void dispose() {
		playerTex.dispose();
	}
}
