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
		super(x, y);
		this.player = player;
		
		speedUtilization = 1;
		life = 2;
	}

	//RENDER&UPDATE loop
/*_______________________________________________________________________*/	
	@Override
	public void render(SpriteBatch batch) {
		rotation += Gdx.graphics.getDeltaTime();
		Util.drawCentered(batch, tex, x, y, rotation * 90 % 360);
	}
	
	@Override
	public boolean update() {
		double angle = Math.atan2(player.getCenterY() - y, player.getCenterX() - x);
		x += Math.cos(angle) * 3 * speedUtilization;
		y += Math.sin(angle) * 3 * speedUtilization;
		
		if(speedTimer > 0) {
			speedTimer -= 10;
		} else {
			setSpeedUtilization(1f);
		}
		
		return super.update();
	}
/*_______________________________________________________________________*/
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/Enemy Fast.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	public void hit() {
		super.hit();
		if(life <= 0)
			player.getGameState().addPoints(20);
	}

	@Override
	public void dispose() {
		tex.dispose();
	}
}
