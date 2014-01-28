package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class BulletBurst extends PowerUp {
	private static Texture texture;
	private static int level;
	private boolean levelDown;	// Field for checking if the level should go up or down
	private Player player;
	
	public BulletBurst(float x, float y, GameState gs) {
		super(x, y, gs);
		activeTime = 600;
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/5 Burst.png"));
	}
	
	@Override
	protected void affect(Player player) {
		this.player = player;

		// Level increases or decreases
		if(levelDown)
			if(level > 0)
				level--;
		else
			if(level < 3)
				level++;
		
		// Affects player according to level
		if(level == 0) {
			player.setBulletBurst(3);
		} else if(level == 1) {
			player.setBulletBurst(5);
			player.setShot180(false);
		} else if(level == 2) {
			player.setShot180(true);
			player.setShot90(false);
		} else if(level >= 3) {
			player.setShot180(true);
			player.setShot90(true);
		}
		
		levelDown = false;
	}

	@Override
	public void render(SpriteBatch batch) {
		if(!isActivated() && despawnTime > 0)
			Util.drawCentered(batch, texture, x, y, 0);
	}
	
	@Override
	public boolean update() {
		// If super.update() returns true it means that one of the affecting power-ups active time has run out
		if(super.update()) {
			levelDown = true;
			affect(player);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public float getRadius() {
		return texture.getWidth() / 2;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
	
	@Override
	public void dispose() {
		texture.dispose();
	}
}
