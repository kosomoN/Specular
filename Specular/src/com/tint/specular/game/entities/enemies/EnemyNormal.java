package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

public class EnemyNormal extends Enemy {

	private static Texture tex;
	private float rotation;
	private Player player;
	private float offset;
	private GameState gs;
	
	public EnemyNormal(float x, float y, Player player, GameState gameState) {
		this.x = x;
		this.y = y;
		this.player = player;
		this.gs = gameState;
		
		useOfSpeed = 1;
		
	    if(Math.random() < 0.5) {
	    	offset = (float) Math.toRadians(60);
	    } else {
            offset = (float) Math.toRadians(-60);
	    }
	}

	@Override
	public boolean update() {
		double angle = Math.atan2(player.getY() - y, player.getX() - x) + offset;
		x += Math.cos(angle) * 2 * useOfSpeed;
		y += Math.sin(angle) * 2 * useOfSpeed;
		
		if(x - 20 < 0) {
			offset = -offset;
		} else if(x + 20 > gs.getMapWidth()){
			offset = -offset;
		}
		
		if(y - 20 < 0) {
			offset = -offset;
		} else if(y + 20 > gs.getMapHeight()){
			offset = -offset;
		}
		
		
		return false;
	}

	@Override
	public void render(SpriteBatch batch) {
		rotation += Gdx.graphics.getDeltaTime();
		Util.drawCentered(batch, tex, x, y, rotation * 90 % 360);
	}
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/Enemy Normal.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
}
