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
	private double direction;
	private Player player;
	private GameState gs;
	private int boostingDelay;
	private float speed;
	
	//CONSTRUCTOR
	public EnemyBooster(float x, float y, Player player, GameState gameState) {
		this.x = x;
		this.y = y;
		this.player = player;
		this.gs = gameState;
		
		speedUtilization = 1;
	}

	//RENDER&UPDATE loop
/*________________________________________________________________________________*/
	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, tex, x, y, (float) Math.toDegrees(direction));
	}
	
	@Override
	public boolean update(float delta) {
		if(boostingDelay > 30) {
			speed += 0.2;
			
			x += Math.cos(direction) * speed * speedUtilization;
			y += Math.sin(direction) * speed * speedUtilization;
			
			if(speedTimer > 0) {
				speedTimer -= delta;
			} else {
				setSpeedUtilization(1f);
			}
			
			boostingDelay++;
		} else if(boostingDelay == 0) {
			direction = Math.atan2(player.getCenterY() - y, player.getCenterX() - x);
			speed = 0;
			boostingDelay++;
		} else {
			boostingDelay++;
		}
		
		if(x - 20 < 0) {
			x = 20;
			boostingDelay = 0;
		} else if(x + 20 > gs.getMapWidth()){
			x = gs.getMapWidth() - 20;
			boostingDelay = 0;
		}
		
		if(y - 20 < 0) {
			y = 20;
			boostingDelay = 0;
		} else if(y + 20 > gs.getMapHeight()){
			y = gs.getMapHeight() - 20;
			boostingDelay = 0;
		}
		
		return super.update(delta);
	}
/*________________________________________________________________________________*/
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/Enemy Booster.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	public void dispose() {
		tex.dispose();
	}
}
