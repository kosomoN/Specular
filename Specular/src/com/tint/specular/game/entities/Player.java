package com.tint.specular.game.entities;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.Shield;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.input.AnalogStick;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class Player implements Entity {
	
	//FIELDS
	private static final int MAX_DELTA_SPEED = 8;
	private static final float SPEED = 0.125f;
	private static final float FRICTION = 0.95f;
	
	private static Animation anim;
	private static Texture playerTex;
	private static Array<Texture> shieldTextures = new Array<Texture>();
	
	private GameState gs;
	private Array<Shield> shields = new Array<Shield>();
	
	private float animFrameTime;
	private float centerx, centery, dx, dy;
	private float timeSinceLastFire, fireRate = 10f;
	
	private int life = 3, lifeY = 512, targetLifeY = 512;
	
	private int bulletBurst = 3;
	private int score = 0;
	
	private boolean isHit;
	
	//CONSTRUCTOR
	public Player(GameState gs, float x, float y) {
		this.gs = gs;
		centerx = x;
		centery = y;
		reset();
	}

	
	//RENDER&UPDATE loop
/*_____________________________________________________________________*/
	
	@Override
	public void render(SpriteBatch batch) {
		animFrameTime += Gdx.graphics.getDeltaTime();
		TextureRegion frame = anim.getKeyFrame(animFrameTime, true);
		batch.draw(frame, centerx - frame.getRegionWidth() / 2, centery - frame.getRegionHeight() / 2);
	}
	
	@Override
	public boolean update() {
		//Update lifebar
		if(lifeY != targetLifeY) {
			if(lifeY - targetLifeY > 0)
				lifeY -= 2;
			else
				lifeY += 2;
		}
		
		//Moving
		updateMovement();
		
		//Shooting
		updateShooting();
		
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
		
		if(gs.getGameProcessor().getShootStick().isActive()) {
			if(timeSinceLastFire >= fireRate) {
				
				float direction = (float) (Math.toDegrees(Math.atan2(gs.getGameProcessor().getShootStick().getYHead()
					- gs.getGameProcessor().getShootStick().getYBase(), gs.getGameProcessor().getShootStick().getXHead()
					- gs.getGameProcessor().getShootStick().getXBase())));

				//The amount of spaces, i.e. two bullet "lines" have one space between them
				int spaces = bulletBurst - 1;
				
				int offset = 8;
				
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
				
				timeSinceLastFire = 0;
			}
		}
	}
	
	public void updateHitDetection() {
        if(!isHit) {
	        for(Iterator<Enemy> it = gs.getEnemies().iterator(); it.hasNext(); ) {
	        	Enemy e = it.next();
        		if(e.getLife() > 0) {
        			float distX = centerx - e.getX();
        			float distY = centery - e.getY();
	        		if(distX * distX + distY * distY < (getRadius() + e.getInnerRadius()) * (getRadius() + e.getInnerRadius())) {
	        			
	        			gs.getEntities().removeValue(e, true);
	        			it.remove();
	        			
	        			if(getLife() > 0)
	        				addLives(-1);
	        				
	        			//Repel effect after collision
	        			double repelAngle = Math.atan2(e.getY() - centery, e.getX() - centerx);
	        			
	        			setSpeed((float) (Math.cos(repelAngle) * -dx * 0.5f), (float) (Math.sin(repelAngle) * -dy * 0.5f));
		        	}
        		}
	        }
        }
	}
/*_____________________________________________________________________*/

	//POWER-UPS
	public void addShield() {
		if(shields.size < shieldTextures.size)
			shields.add(new Shield(this, shieldTextures.get(shields.size)));
	}
	
	public void addLives(int livesToAdd) {
		life = life + livesToAdd < 3 ? life + livesToAdd : 3;
		
		if(life == 3)
			targetLifeY = 512;
		else if(life == 2)
			targetLifeY = 408;
		else if(life == 1)
			targetLifeY = 265;
		else if(life <= 0)
			targetLifeY = 0;
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
	
	//SETTERS
	public void setCenterX(float x) { this.centerx = x; }
	public void setCenterY(float y) { this.centery = y;	}
	/**
	 * Takes the amount of updates as rate of fire, not any specific "time"
	 * @param fireRate
	 */
	public void setFireRate(float fireRate) { this.fireRate = fireRate; }
	public void setBulletBurst(int burst) {	bulletBurst = burst; }
	public void setLife(int life) { this.life = life; }
	public void setHit(boolean hit) { isHit = hit; }
	public void kill() { life = 0; }
	
	//GETTERS
	public float getCenterX() { return centerx; }
	public float getCenterY() { return centery;	}
	public float getDeltaX() { return dx; }
	public float getDeltaY() { return dy; }
	public static float getRadius() { return (anim.getKeyFrame(0).getRegionWidth() - 10) / 2; }
	public int getLife() { return life;	}
	public int getBulletBurst() { return bulletBurst; }
	public Array<Shield> getShields() { return shields; }
	public int getScore() { return score; }
	public boolean hasShield() { return shields.size > 0; }
	public boolean isDead() { return life <= 0; }
	public GameState getGameState() { return gs; }
	
	public static void init() {
		playerTex  = new Texture(Gdx.files.internal("graphics/game/Player.png"));
		playerTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		anim = Util.getAnimation(playerTex, 64, 64, 1 / 16f, 0, 0, 7, 3);
	}
	
	public void reset() {
		setLife(3);
//		shield = new Shield(this, shieldLayer1);
	}

	@Override
	public void dispose() {
		playerTex.dispose();
	}
}
