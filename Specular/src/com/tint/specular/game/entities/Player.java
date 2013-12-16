package com.tint.specular.game.entities;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.input.GameInputProcessor;
import com.tint.specular.utils.Util;

public class Player implements Entity {
	
	//FIELDS
	private static final int MAX_DELTA_SPEED = 8;
	
	private static Animation anim;
	private static Texture texture;
	private static Texture lifeBar, lifeTex;
	
	private GameState gs;
	
	private float animFrameTime;
	private float centerx, centery, dx, dy;
	private float timeSinceLastFire, fireRate = 10f;
	
	private int life = 5, lifeY = 512, targetLifeY = 512;
	
	private int bulletBurst = 2;
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
	
	public void renderLifebar(SpriteBatch batch) {
		//Takes minus the screens with and height because the camera is centered
		batch.draw(lifeTex, 30 - Gdx.graphics.getWidth() / 2, 30 - Gdx.graphics.getHeight() / 2, 64, lifeY);
		batch.draw(lifeBar, 30 - Gdx.graphics.getWidth() / 2, 30 - Gdx.graphics.getHeight() / 2);
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
        dx *= 0.98f;
        dy *= 0.98f;
        
        if(centerx - getRadius() + dx < 0)
        	dx = -dx * 0.6f;
        else if(centerx + getRadius() + dx > gs.getCurrentMap().getWidth())
        	dx = -dx * 0.6f;
        
        if(centery - getRadius() + dy < 0)
        	dy = -dy * 0.6f;
        else if(centery + getRadius() + dy > gs.getCurrentMap().getHeight())
        	dy = -dy * 0.6f;
        
        centerx += dx;
        centery += dy;
        
        return lifeY <= 0;
	}
	
	public void updateMovement() {
		/*if(gs.getProcessor().isWDown())
			changeSpeed(0, 0.6f);
		if(gs.getProcessor().isADown())
			changeSpeed(-0.6f, 0);
		if(gs.getProcessor().isSDown())
			changeSpeed(0, -0.6f);
		if(gs.getProcessor().isDDown())
			changeSpeed(0.6f, 0);
		
		changeSpeed(Gdx.input.getAccelerometerX() * 0.1f * 0.6f,
				Gdx.input.getAccelerometerY() * 0.1f * 0.6f);*/
		
		float aFourthOfWidthSqrd = Gdx.graphics.getWidth() / 4 * Gdx.graphics.getWidth() / 4;
		
		float distBaseToHead = Math.abs((((GameInputProcessor) gs.getProcessor()).getMoveStick().getYHead() - ((GameInputProcessor) gs.getProcessor()).getMoveStick().getYBase())
				* (((GameInputProcessor) gs.getProcessor()).getMoveStick().getYHead() - ((GameInputProcessor) gs.getProcessor()).getMoveStick().getYBase())
				+
				(((GameInputProcessor) gs.getProcessor()).getMoveStick().getXHead() - ((GameInputProcessor) gs.getProcessor()).getMoveStick().getXBase())
				* (((GameInputProcessor) gs.getProcessor()).getMoveStick().getXHead() - ((GameInputProcessor) gs.getProcessor()).getMoveStick().getXBase()));
				
		
		if(((GameInputProcessor) gs.getProcessor()).getMoveStick().isActive() && distBaseToHead != 0) {
			//calculating the angle with delta x and delta y
			double angle = Math.atan2(((GameInputProcessor) gs.getProcessor()).getMoveStick().getYHead() 
				- ((GameInputProcessor) gs.getProcessor()).getMoveStick().getYBase(), ((GameInputProcessor) gs.getProcessor()).getMoveStick().getXHead() 
				- ((GameInputProcessor) gs.getProcessor()).getMoveStick().getXBase());
			
			setSpeed(
					(float) Math.cos(angle) * MAX_DELTA_SPEED *
					(distBaseToHead >= aFourthOfWidthSqrd ? 1 : distBaseToHead / aFourthOfWidthSqrd),
					
					(float) Math.sin(angle) * MAX_DELTA_SPEED *
					(distBaseToHead >= aFourthOfWidthSqrd ? 1 : distBaseToHead / aFourthOfWidthSqrd)
					);
		}
	}
	
	public void updateShooting() {
		timeSinceLastFire += 1;
		
		if(((GameInputProcessor) gs.getProcessor()).getShootStick().isActive()) {
			if(timeSinceLastFire >= fireRate) {
				
				float direction = (float) (Math.toDegrees(Math.atan2(((GameInputProcessor) gs.getProcessor()).getShootStick().getYHead()
					- ((GameInputProcessor) gs.getProcessor()).getShootStick().getYBase(), ((GameInputProcessor) gs.getProcessor()).getShootStick().getXHead()
					- ((GameInputProcessor) gs.getProcessor()).getShootStick().getXBase())));

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
	        for(Iterator<Entity> it = gs.getEntities().iterator(); it.hasNext(); ) {
	        	Entity e = it.next();
	        	if(e instanceof Enemy) {
	        		if(((Enemy) e).getLife() > 0) {
		        		if(Math.pow(centerx - ((Enemy) e).getX(), 2) + Math.pow(centery - ((Enemy) e).getY(), 2)
			    			< Math.pow(getRadius() + ((Enemy) e).getInnerRadius(), 2)) {
		        			
//			        		gs.getParticleSpawnSystem().spawn((Enemy) e, 20, 5);
		        			
		        			it.remove();
		        			
		        			if(getLife() > 0)
		        				addLives(-1);
		        			
			        		if(centerx - getRadius() + dx < ((Enemy) e).getX() + ((Enemy) e).getInnerRadius())
			                	dx = -dx * 0.5f;
			                else if(centerx + getRadius() + dx > ((Enemy) e).getX() - ((Enemy) e).getInnerRadius())
			                	dx = -dx * 0.5f;
			                
			                if(centery - getRadius() + dy < ((Enemy) e).getY() + ((Enemy) e).getInnerRadius())
			                	dy = -dy * 0.5f;
			                else if(centery + getRadius() + dy > ((Enemy) e).getY() - ((Enemy) e).getInnerRadius())
			                	dy = -dy * 0.5f;
			        	}
	        		}
	        	} else if(e instanceof Player) {
	        		if(!e.equals(this)) {
						if(Util.getDistanceSquared(centerx, centery, ((Player) e).getCenterX(), ((Player) e).getCenterY())
								< Math.pow(getRadius() * 2, 2)) {
							addLives(-1);
							((Player) e).addLives(-1);
							
							if(centerx - getRadius() + dx < ((Player) e).getCenterX() + Player.getRadius())
					        	dx = -dx * 0.6f;
					        else if(centerx + getRadius() + dx > ((Player) e).getCenterX() - Player.getRadius())
					        	dx = -dx * 0.6f;
					        
					        if(centery - getRadius() + dy < ((Player) e).getCenterY() + Player.getRadius())
					        	dy = -dy * 0.6f;
					        else if(centery + getRadius() + dy > ((Player) e).getCenterY() - Player.getRadius())
					        	dy = -dy * 0.6f;
						}
	        		}
	        	}
	        }
        }
	}
/*_____________________________________________________________________*/

	//POWER-UPS
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
	public void setBulletBurst(int burst) {	bulletBurst = burst; }
	public void setHit(boolean hit) { isHit = hit; }
	
	//GETTERS
	public float getCenterX() { return centerx; }
	public float getCenterY() { return centery;	}
	public float getDeltaX() { return dx; }
	public float getDeltaY() { return dy; }
	public static float getRadius() { return (anim.getKeyFrame(0).getRegionWidth() - 10) / 2; }
	public int getLife() { return life;	}
	public int getBulletBurst() { return bulletBurst; }
	public int getScore() { return score; }
	public boolean isDead() { return lifeY <= 0; }
	public GameState getGameState() { return gs; }
	
	public static void init() {
		lifeBar = new Texture(Gdx.files.internal("graphics/game/Lifebar.png"));
		lifeTex = new Texture(Gdx.files.internal("graphics/game/Life.png"));
		texture  = new Texture(Gdx.files.internal("graphics/game/Player.png"));
		anim = Util.getAnimation(texture, 64, 64, 1 / 16f, 0, 0, 7, 3);
	}
	
	public void reset() {
		addLives(3);
	}

	@Override
	public void dispose() {
		texture.dispose();
		lifeBar.dispose();
		lifeTex.dispose();
	}
}
