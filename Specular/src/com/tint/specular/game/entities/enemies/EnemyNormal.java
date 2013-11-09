package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

public class EnemyNormal extends Enemy {

	//FIELDS
	private static Texture tex;
	private float rotation;
	private float offset;
	
	private Player player;
	private GameState gs;
	
	//CONSTRUCTOR
	public EnemyNormal(float x, float y, Player player, GameState gameState) {
		this.x = x;
		this.y = y;
		this.width = tex.getWidth();
		this.height = tex.getHeight();
		this.player = player;
		this.gs = gameState;
		
		speedUtilization = 1;
		
	    if(Math.random() < 0.5) {
	    	offset = (float) Math.toRadians(60);
	    } else {
            offset = (float) Math.toRadians(-60);
	    }
	}

	//RENDER&UPDATE loop
/*_______________________________________________________________________________*/
	@Override
	public void render(SpriteBatch batch) {
		rotation += Gdx.graphics.getDeltaTime();
		Util.drawCentered(batch, tex, x, y, rotation * 90 % 360);
	}
	
	@Override
	public boolean update(float delta) {
		double angle = Math.atan2(player.getCenterY() - y, player.getCenterX() - x) + offset;
		x += Math.cos(angle) * 2 * speedUtilization;
		y += Math.sin(angle) * 2 * speedUtilization;
		
		
		if(speedTimer > 0)
			speedTimer -= delta;
		else
			setSpeedUtilization(1f);
		
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
		
		
		return super.update(delta);
	}
/*_______________________________________________________________________________*/
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/Enemy Normal.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	public void dispose() {
		tex.dispose();
	}
}
