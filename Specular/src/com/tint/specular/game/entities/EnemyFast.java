package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.Util;

public class EnemyFast extends Enemy {

	private static Texture tex;
	private float rotation;
	private Player player;
	
	public EnemyFast(float x, float y, Player player) {
		this.x = x;
		this.y = y;
		this.player = player;
	}

	@Override
	public boolean update() {
		double angle = Math.atan2(player.getY() - y, player.getX() - x);
		x += Math.cos(angle) * 3;
		y += Math.sin(angle) * 3;
		return false;
	}

	@Override
	public void render(SpriteBatch batch) {
		rotation += Gdx.graphics.getDeltaTime();
		Util.drawCentered(batch, tex, x, y, rotation * 90 % 360);
	}
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/Enemy Fast.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
}
