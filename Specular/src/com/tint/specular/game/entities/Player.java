package com.tint.specular.game.entities;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.utils.Timer;
import com.tint.specular.utils.Util;

public class Player implements Entity {
	
	//FIELDS
	private static Animation anim;
	private static Texture texture;
	
	private GameState gs;
	private Timer speedTimer, bulletTimer;
	
	private float animFrameTime;
	private float centerx, centery, dx, dy;
	private float timeSinceLastFire, fireRate = 6f;
	
	private int life = 5;
	private int speedBonus = 1;
	private int bulletBurst = 2;
	private int score = 0;
	
	private boolean isDead;
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
		batch.draw(frame, centerx - frame.getRegionWidth() / 2, centery - frame.getRegionHeight() / 2, frame.getRegionWidth() / 2, frame.getRegionHeight() / 2, frame.getRegionWidth(), frame.getRegionHeight(), 1, 1, animFrameTime % 2 > 1 ? 0 : 180, false);
	}
	
	@Override
	public boolean update() {
		//Updating timers and power-ups
		if(!speedTimer.update(10))
			deactivateSpeedBonus();
		
		if(!bulletTimer.update(10))
			setBulletBurst(1);
		
		//Handling key input
		/*if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			dy += 0.8f * getSpeedBonus();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			dy -= 0.8f * getSpeedBonus();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			dx += 0.8f * getSpeedBonus();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			dx -= 0.8f * getSpeedBonus();
		}*/
		
		//Shooting
		timeSinceLastFire += 1;
		
		if(Gdx.input.isTouched()) {
			if(timeSinceLastFire >= fireRate) {
				
				//TODO Uncomment to get random direction
				float direction = (float) (Math.toDegrees(Math.atan2(Gdx.graphics.getHeight() / 2
						- Gdx.input.getY(), Gdx.input.getX() - Gdx.graphics.getWidth() / 2)));
					//+ Math.random() * 20 - 10);
				
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

					for(int i = 0; i < spaces; i++) {
						gs.addEntity(new Bullet(centerx, centery, direction + i * offset, dx, dy, gs, this));
						gs.addEntity(new Bullet(centerx, centery, direction - i * offset, dx, dy, gs, this));
					}
				}
				
				timeSinceLastFire = 0;
			}
		}
		
		//Movement
        dx *= 0.95f;
        dy *= 0.95f;
        
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
        return isDead;
	}
	
	public void updateHitDetection() {
        if(life <= 0) {
        	System.out.println("Score: " + score);
        	isDead = true;
        } else {
        	isDead = false;
        }
        
        if(!isHit) {
	        for(Iterator<Entity> it = gs.getEntities().iterator(); it.hasNext(); ) {
	        	Entity e = it.next();
	        	if(e instanceof Enemy) {
	        		if(!((Enemy) e).isDead()) {
		        		if(Math.pow(centerx - ((Enemy) e).getX(), 2) + Math.pow(centery - ((Enemy) e).getY(), 2)
			    			< Math.pow(getRadius() + ((Enemy) e).getInnerRadius(), 2)) {
		        			
//			        		gs.getParticleSpawnSystem().spawn((Enemy) e, 20, 5);
		        			
		        			it.remove();
		        			life--;
		        			
			        		if(centerx - getRadius() + dx < ((Enemy) e).getX() + ((Enemy) e).getInnerRadius())
			                	dx = -dx ;
			                else if(centerx + getRadius() + dx > ((Enemy) e).getX() - ((Enemy) e).getInnerRadius())
			                	dx = -dx;
			                
			                if(centery - getRadius() + dy < ((Enemy) e).getY() + ((Enemy) e).getInnerRadius())
			                	dy = -dy;
			                else if(centery + getRadius() + dy > ((Enemy) e).getY() - ((Enemy) e).getInnerRadius())
			                	dy = -dy;
			        	}
	        		}
	        	} else if(e instanceof Player) {
	        		if(!e.equals(this)) {
						if(Util.getDistanceSquared(centerx, centery, ((Player) e).getCenterX(), ((Player) e).getCenterY())
								< Math.pow(getRadius() * 2, 2)) {
							life--;
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
	public void activateSpeedBonus() {
		speedBonus = 2;
	}
	
	public void deactivateSpeedBonus() {
		speedBonus = 1;
	}
	
	public void addLives(int livesToAdd) {
		life += livesToAdd;
	}
	
	public void addScore(int score) {
		this.score += score * gs.getScoreMultiplier();
	}
	
	public void changeAcceleration(float dx, float dy) {
		this.dx += dx;
		this.dy += dy;
	}
	
	//SETTERS
	public void setCenterX(float x) { this.centerx = x; }
	public void setCenterY(float y) { this.centery = y;	}
	public void setBulletBurst(int burst) {	bulletBurst = burst; }
	public void setLife(int life) { this.life = life; }
	public void setHit(boolean hit) { isHit = hit; }
	
	public void setTimer(Timer timer, float seconds) {
		if(timer.getTime() <= 0) {
			timer.setTime(seconds < 0 ? 0 : seconds);
		}
	}
	
	//GETTERS
	public float getCenterX() { return centerx; }
	public float getCenterY() { return centery;	}
	public float getDeltaX() { return dx; }
	public float getDeltaY() { return dy; }
	public static float getRadius() { return (anim.getKeyFrame(0).getRegionWidth() - 10) / 2; }
	public int getLife() { return life;	}
	public int getSpeedBonus() { return speedBonus;	}
	public int getScore() { return score; }
	public boolean isDead() { return isDead; }
	public Timer getSpeedTimer() { return speedTimer; }
	public Timer getBulletTimer() { return bulletTimer;	}
	public GameState getGameState() { return gs; }
	
	public static void init() {
		texture  = new Texture(Gdx.files.internal("graphics/game/Player.png"));
		anim = Util.getAnimation(texture, 128, 128, 1 / 16f, 0, 0, 3, 3);
	}
	
	public void reset() {
		isDead = false;
		setLife(1);
		speedTimer = new Timer(0);
		bulletTimer = new Timer(0);
		bulletTimer.setTime(5000);
		deactivateSpeedBonus();
		
		setBulletBurst(3);
	}

	@Override
	public void dispose() {
		texture.dispose();
	}
}
