package com.tint.specular.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class Shield {
	
	private static final float ROTATION_SPEED = 3;
	private static Player player;
	private static Texture texture;
	private static float rotation;
	
	public static void init(Player player) {
		Shield.player = player;
		texture = new Texture(Gdx.files.internal("graphics/game/Shield.png"));
	}
	
	public static void render(SpriteBatch batch) {
		rotation -= ROTATION_SPEED;
		Util.drawCentered(batch, texture, player.getCenterX(), player.getCenterY(), rotation);
	}
}
