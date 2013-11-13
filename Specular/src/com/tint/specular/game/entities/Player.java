package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
	
	private int life = 2;
	private int speedBonus = 1;
	private int bulletBurst = 2;
	
	private boolean isDead;
	
	//CONSTRUCTOR
	public Player(GameState gs) {
		this.gs = gs;
		speedTimer = new Timer();
		bulletTimer = new Timer();
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
		if(speedTimer.getTime() <= 0) {
			deactivateSpeedBonus();
		} else {
			speedTimer.update(10);
		}
		
		if(bulletTimer.getTime() <= 0) {
			setBulletBurst(1);
		} else {
			bulletTimer.update(10);
		}
		
		//Handling key input
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			dy += 0.8f * speedBonus;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			dy -= 0.8f * speedBonus;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			dx += 0.8f * speedBonus;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			dx -= 0.8f * speedBonus;
		}
		
		//TODO debugging stuff
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			System.gc();
		}
		
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

					for(int i = 0; i < spaces; i++) {
						gs.addEntity(new Bullet(centerx, centery, direction + i * offset, dx, dy, gs));
						gs.addEntity(new Bullet(centerx, centery, direction - i * offset, dx, dy, gs));
					}
				}
				
				timeSinceLastFire = 0;
			}
		}
		
		//Movement
        dx *= 0.95f;
        dy *= 0.95f;
        
        if(centerx - 31 + dx < 0)
        	dx = -dx * 0.6f;
        else if(centerx + 31 + dx > gs.getCurrentMap().getWidth())
        	dx = -dx * 0.6f;
        
        if(centery - 31 + dy < 0)
        	dy = -dy * 0.6f;
        else if(centery + 31 + dy > gs.getCurrentMap().getHeight())
        	dy = -dy * 0.6f;
        
        centerx += dx;
        centery += dy;
        
        return isDead;
	}
	
	public void updateHitDetection() {
		//Hit detection
        for(Enemy e : gs.getEnemies()) {
        	if(Math.pow(centerx - e.getX(), 2) + Math.pow(centery - e.getY(), 2)
        			< Math.pow(getRadius() + e.getWidth(), 2)) {
        		life--;
        		e.hit();
        	}
        }
        
        if(life <= 0)
        	isDead = true;
        else
        	isDead = false;
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
	
	//SETTERS
	public void setCenterX(float x) { this.centerx = x; }
	public void setCenterY(float y) { this.centery = y;	}
	public void setBulletBurst(int burst) {	bulletBurst = burst; }
	public void setLife(int life) { this.life = life; }
	
	public void setTimer(Timer timer, float seconds) {
		if(timer.getTime() <= 0) {
			timer.setTime(seconds < 0 ? 0 : seconds);
		}
	}
	
	//GETTERS
	public float getCenterX() { return centerx; }
	public float getCenterY() { return centery;	}
	public float getRadius() { return anim.getKeyFrame(0).getRegionWidth() / 2; }
	public int getLife() { return life;	}
	public int getSpeedBonus() { return speedBonus;	}
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
		speedTimer = new Timer();
		bulletTimer = new Timer();
		deactivateSpeedBonus();
		setBulletBurst(1);
		
		init();
	}

	@Override
	public void dispose() {
		texture.dispose();
	}
}
