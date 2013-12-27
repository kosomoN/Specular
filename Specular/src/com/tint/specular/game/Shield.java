package com.tint.specular.game;

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
	
	private Player player;
	private Texture texture;
	private float durability = 100;
	
	public Shield(Player player, Texture texture) {
		this.player = player;
		this.texture = texture;
	}
	
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, texture, player.getCenterX(), player.getCenterY(), 0);
	}
	
	public boolean update() {
		return durability <= 0;
	}
	
	public void decreaseDurability(float decrease) {
		durability -= decrease;
	}
	
	public void increaseDurability(float increase) {
		durability += increase;
	}
	
	public float getRadius() {
		return texture.getWidth();
	}
	
	public float getDurability() {
		return durability;
	}
}
