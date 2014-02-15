package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class BoardshockPowerUp extends PowerUp {

	private static Texture texture;

	public BoardshockPowerUp(float x, float y, GameState gs) {
		super(x, y, gs, -1);
	}

	@Override
	protected void affect(Player p) {
		gs.addBoardshockCharge(0.5f);
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/powerups/BoardShock.png"));
	}
	
}
