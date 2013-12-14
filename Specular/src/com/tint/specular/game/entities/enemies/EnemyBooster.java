package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

public class EnemyBooster extends Enemy {

	private static Texture tex;
	
	private float speed;
	private double direction;
	private int boostingDelay;
	
	public EnemyBooster(float x, float y, GameState gs) {
		super(x, y, gs);
		
		slowdown = 0;
		life = 1;
	}

	//RENDER&UPDATE loop
/*________________________________________________________________________________*/
	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, tex, x, y, (float) Math.toDegrees(direction) - 90);
	}
	
	@Override
	public boolean update() {
		//Boosting and changing direction
		if(boostingDelay > 30) {
			speed += 0.1;
			
			dx = (float) (Math.cos(direction) * speed);
			dy = (float) (Math.sin(direction) * speed);
			x += dx * (1 - slowdown);
			y += dy * (1 - slowdown);
			
			boostingDelay++;
		} else if(boostingDelay == 0) {
			direction = Math.atan2(getClosestPlayer().getCenterY() - y, getClosestPlayer().getCenterX() - x);
			speed = 0;
			boostingDelay++;
		} else {
			boostingDelay++;
		}
		
		//Checking so that the enemy will not get outside the map
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
	public float getInnerRadius() { return tex.getWidth() / 4; }
	@Override
	public float getOuterRadius() { return tex.getWidth() / 2; }
	
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
