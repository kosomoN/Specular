package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.powerups.PowerUp;
import com.tint.specular.utils.Util;

public class Player implements Entity {

	private static Animation anim;
	private float animFrameTime;
	private float x, y, dx, dy;
	private GameState gs;
	private float timeSinceLastFire, fireRate = 6f;
	private int life = 3;
	private int speedBonus = 1;
	private int bulletBurst = 2;
	
	public Player(GameState gs) {
		this.gs = gs;
	}

	@Override
	public boolean update() {
		
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
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			System.gc();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.B)) {
			for(PowerUp pow : gs.getPowerUps()) {
				if(pow.getType() == PowerUp.Type.BULLETBURST) {
					//pow.affect(this, gs.getEnemies());
				}
			}
		}
		
		timeSinceLastFire += 1;
		
		//Shooting
		if(Gdx.input.isTouched()) {
			if(timeSinceLastFire >= fireRate) {
				
				//Uncomment to get random direction
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
							gs.addEntity(new Bullet(x, y, direction + offset / 2, dx, dy, gs));
							gs.addEntity(new Bullet(x, y, direction - offset / 2, dx, dy, gs));
						} else {
							gs.addEntity(new Bullet(x, y, direction + offset / 2 + j * offset, dx, dy, gs));
							gs.addEntity(new Bullet(x, y, direction - offset / 2 - j * offset, dx, dy, gs));
						}
					}
					
				//If the number of bullet "lines" are odd
				} else {
					gs.addEntity(new Bullet(x, y, direction, dx, dy, gs));

					for(int i = 0; i < spaces; i++) {
						gs.addEntity(new Bullet(x, y, direction + i * offset, dx, dy, gs));
						gs.addEntity(new Bullet(x, y, direction - i * offset, dx, dy, gs));
					}
				}
				
				timeSinceLastFire = 0;
			}
		}
		
        dx *= 0.95f;
        dy *= 0.95f;
        
        if(x - 31 + dx < 0)
        	dx = -dx * 0.6f;
        else if(x + 31 + dx > gs.getMapWidth())
        	dx = -dx * 0.6f;
        
        if(y - 31 + dy < 0)
        	dy = -dy * 0.6f;
        else if(y + 31 + dy > gs.getMapHeight())
        	dy = -dy * 0.6f;
        
        x += dx;
        y += dy;
        
        return false;
	}

	@Override
	public void render(SpriteBatch batch) {
		animFrameTime += Gdx.graphics.getDeltaTime();
		TextureRegion frame = anim.getKeyFrame(animFrameTime, true);
		batch.draw(frame, x - frame.getRegionWidth() / 2, y - frame.getRegionHeight() / 2, frame.getRegionWidth() / 2, frame.getRegionHeight() / 2, frame.getRegionWidth(), frame.getRegionHeight(), 1, 1, animFrameTime % 2 > 1 ? 0 : 180, false);
	}

	public void activateSpeedBonus() {
		speedBonus = 2;
	}
	
	public void deactivateSpeedBonus() {
		speedBonus = 1;
	}
	
	//Setters
	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void setBulletBurst(int burst) {
		bulletBurst = burst;
	}
	
	public void addLives(int livesToAdd) {
		life += livesToAdd;
	}
	
	//Getters
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public int getLife() {
		return life;
	}
	
	public int getSpeedBonus() {
		return speedBonus;
	}
	
	public static void init() {
		Texture texture  = new Texture(Gdx.files.internal("graphics/game/Player.png"));
		anim = Util.getAnimation(texture, 128, 128, 1 / 16f, 0, 0, 3, 3);
	}
}
