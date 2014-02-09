package com.tint.specular.game.entities;

import java.util.Iterator;

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
	
	public static final int MAX_DELTA_SPEED = 1;
	public static final float FRICTION = 0.95f;
	
	public static Animation anim, spawnAnim, deathAnim;
	public static Texture playerTex, playerSpawnTex, playerDeathTex, shieldTexture, barrelTexture;
	public static int radius;
	
	private GameState gs;
	
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
	private int controls;
	
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
		

	public Player(GameState gs, float x, float y, int lives) {
		this.gs = gs;
		centerx = x;
		centery = y;
		soundEffects = !Specular.prefs.getBoolean("SoundsMuted");
		setLife(lives);
		
		sensitivity = Specular.prefs.getFloat("Sensitivity");
		maxSpeedAreaSquared = (Specular.camera.viewportWidth / 8 * sensitivity) * (Specular.camera.viewportWidth / 8 * sensitivity);
	}

	public void shoot(float direction, int offset, int spaces) {
		if(soundEffects)
			soundShoot1.play(0.8f, (float) (1 + Math.random() / 3 - 0.16), 0);
		
		//If the amount of bullet "lines" are even
		if(bulletBurst % 2 == 0) {
			for(int j = 0; j < (spaces - 1) / 2 + 1; j++) {
				if(j == 0) {
					gs.addEntity(new Bullet(centerx, centery, direction + offset / 2, dx, dy, gs));
					gs.addEntity(new Bullet(centerx, centery, direction - offset / 2, dx, dy, gs));
				} else {
					gs.addEntity(new Bullet(centerx, centery, direction + offset / 2 + j * offset, dx, dy, gs));
					gs.addEntity(new Bullet(centerx, centery, direction - offset / 2 - j * offset, dx, dy, gs));
				}
			}
			
		//If the number of bullet "lines" are odd
		} else {
			gs.addEntity(new Bullet(centerx, centery, direction, dx, dy, gs));

			for(int i = 0; i < spaces / 2; i++) {
				gs.addEntity(new Bullet(centerx, centery, direction + (i + 1) * offset, dx, dy, gs));
				gs.addEntity(new Bullet(centerx, centery, direction - (i + 1) * offset, dx, dy, gs));
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
			Util.drawCentered(batch, barrelTexture, getCenterX(), getCenterY(), direction);
			for(int i = 0; i < shields; i++) {
				Util.drawCentered(batch, shieldTexture, getCenterX(), getCenterY(), -animFrameTime * 360 + 360f * i / shields);
			}
			
		}
	}
	
	@Override
	public boolean update() {
		// Taking control away when player is hit and respawning
		if(!dying && !spawning) {
			updateMovement();
			updateShooting();
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
		
		if(controls == 0) {
			changeSpeed(Gdx.input.getAccelerometerX() * 0.1f * 0.6f, Gdx.input.getAccelerometerY() * 0.1f * 0.6f);
		} else if(controls == 2 || controls == 3){
			
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
				//The amount of spaces, i.e. two bullet "lines" have one space between them
				int spaces = bulletBurst - 1;
				int offset = 8;
				
				shoot(direction, offset, spaces);
				
				timeSinceLastFire = 0;
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
    					dying = true;
    					
    					//Hit sound (randomize)
//    					int randomNum = rand.nextInt(3);
//    					if (randomNum == 0) { soundHit1.play(1.0f); } else if (randomNum == 1) { soundHit2.play(1.0f); } else { soundHit3.play(1.0f); }
    					animFrameTime = 0;
    					clearEnemies = true;
    					
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
	public void setFireRate(float fireRate) { this.fireRate = fireRate; }
	public void setBulletBurst(int burst) { bulletBurst = burst; }
	public void setBulletBurstLevel(int level) { bulletBurstLevel = level; }
	public void addBulletBurstLevel(int level) { bulletBurstLevel += level; }
	
	public void setLife(int life) { this.life = life; }
	public void kill() { life = 0; }
	
	public float getCenterX() { return centerx; }
	public float getCenterY() { return centery;	}
	public float getDeltaX() { return dx; }
	public float getDeltaY() { return dy; }
	public float getDirection() { return direction; }
	public float getFireRate() { return fireRate; }
	public float getTimeSinceLastFire() { return timeSinceLastFire; }
	public static float getRadius() { return radius; }
	public int getLife() { return life;	}
	public int getBulletBurst() { return bulletBurst; }
	public int getBulletBurstLevel() { return bulletBurstLevel; }
	public int getControls() { return controls; }
	public int getScore() { return score; }
	public int getShields() { return shields; }

	public boolean hasShield() { return shields > 0; }
	public boolean isHit() { return dying; }
	public boolean isSpawning() { return spawning; }
	public boolean isDead() { return dead; }
	public GameState getGameState() { return gs; }
	
	public void respawn() {
		animFrameTime = 0;
		spawning = true;
		dead = false;
		controls = Specular.prefs.getInteger("Controls");
	}
	
	public static void init() {
		playerTex  = new Texture(Gdx.files.internal("graphics/game/Player.png"));
		playerTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		anim = Util.getAnimation(playerTex, 128, 128, 1 / 16f, 0, 0, 7, 3);
		
		playerSpawnTex  = new Texture(Gdx.files.internal("graphics/game/Player Spawn Anim.png"));
		playerSpawnTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		spawnAnim = Util.getAnimation(playerSpawnTex, 128, 128, 1 / 16f, 0, 0, 4, 3);
		
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
