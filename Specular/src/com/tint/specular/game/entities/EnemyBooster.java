package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.Util;
import com.tint.specular.game.GameState;

public class EnemyBooster extends Enemy {

	private static Texture tex;
	private double direction;
	private Player player;
	private GameState gs;
	private int boostingDelay;
	private float speed;
	
	public EnemyBooster(float x, float y, Player player, GameState gameState) {
		this.x = x;
		this.y = y;
		this.player = player;
		this.gs = gameState;
	}

	@Override
	public boolean update() {
		if(boostingDelay > 30) {
			speed += 0.1;
			x += Math.cos(direction) * speed;
			y += Math.sin(direction) * speed;
			boostingDelay++;
		} else if(boostingDelay == 0) {
			direction = Math.atan2(player.getY() - y, player.getX() - x);
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
		
		return false;
	}

	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, tex, x, y, (float) Math.toDegrees(direction));
	}
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/Enemy Booster.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
}
