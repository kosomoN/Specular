package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class ShieldUpgrade extends PowerUp {
	private static Texture texture;
	
	public ShieldUpgrade(float x, float y, GameState gs) {
		super(x, y, gs, -1);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/powerups/ShieldUpgrade.png"));
	}

	@Override
	protected void affect(Player p) {
		p.addShield();
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
}