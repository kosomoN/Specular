package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

public class EnemyFast extends Enemy {

	//FIELDS
	private Player player;
	private static Texture tex;
	private float rotation;

	//CONSTRUCTOR
	public EnemyFast(float x, float y, Player player) {
		this.x = x;
		this.y = y;
		this.player = player;
		
		speedUtilization = 1;
	}

	//RENDER&UPDATE loop
/*_______________________________________________________________________*/	
	@Override
	public void render(SpriteBatch batch) {
		rotation += Gdx.graphics.getDeltaTime();
		Util.drawCentered(batch, tex, x, y, rotation * 90 % 360);
	}
	
	@Override
	public boolean update(float delta) {
		double angle = Math.atan2(player.getCenterY() - y, player.getCenterX() - x);
		x += Math.cos(angle) * 3 * speedUtilization;
		y += Math.sin(angle) * 3 * speedUtilization;
		
		if(speedTimer > 0) {
			speedTimer -= delta;
		} else {
			setSpeedUtilization(1f);
		}
		
		return super.update(delta);
	}
/*_______________________________________________________________________*/
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/Enemy Fast.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	public void dispose() {
		tex.dispose();
	}
}
