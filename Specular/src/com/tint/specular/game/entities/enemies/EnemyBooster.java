package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

public class EnemyBooster extends Enemy {

	//FIELDS
	private static Texture tex;
	
	private float speed;
	private double direction;
	private int boostingDelay;
	
	//CONSTRUCTOR
	public EnemyBooster(float x, float y, GameState gs) {
		super(x, y, gs);
		
		speedUtilization = 1;
		life = 1;
	}

	//RENDER&UPDATE loop
/*________________________________________________________________________________*/
	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, tex, x, y, (float) Math.toDegrees(direction));
	}
	
	@Override
	public boolean update() {
		if(boostingDelay > 30) {
			speed += 0.2;
			
			dx = (float) (Math.cos(direction) * speed);
			dy = (float) (Math.sin(direction) * speed);
			x += dx * speedUtilization;
			y += dy * speedUtilization;
			
			if(speedTimer.getTime() > 0) {
				speedTimer.setTime(speedTimer.getTime() - 10);
			}
			
			boostingDelay++;
		} else if(boostingDelay == 0) {
			direction = Math.atan2(getClosestPlayer().getCenterY() - y, getClosestPlayer().getCenterX() - x);
			speed = 0;
			boostingDelay++;
		} else {
			boostingDelay++;
		}
		
		if(x - 20 < 0) {
			x = 20;
			boostingDelay = 0;
		} else if(x + 20 > gs.getCurrentMap().getWidth()){
			x = gs.getCurrentMap().getWidth() - 20;
			boostingDelay = 0;
		}
		
		if(y - 20 < 0) {
			y = 20;
			boostingDelay = 0;
		} else if(y + 20 > gs.getCurrentMap().getHeight()){
			y = gs.getCurrentMap().getHeight() - 20;
			boostingDelay = 0;
		}
		
		return super.update();
	}
/*________________________________________________________________________________*/
	
	//GETTERS
	@Override
	public float getInnerRadius() {
		return tex.getWidth() / 4;
	}
	
	public float getOuterRadius() {
		return tex.getWidth() / 2;
	}
	
	@Override
	public float getDeltaX() {
		return dx;
	}

	@Override
	public float getDeltaY() {
		return dy;
	}

	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/Enemy Booster.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	public void hit(Player shooter) {
		super.hit(shooter);
		if(life <= 0)
			shooter.addScore(50);
	}
	
	@Override
	public void dispose() {
		tex.dispose();
	}
}
