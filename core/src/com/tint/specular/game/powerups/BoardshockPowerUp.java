package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class BoardshockPowerUp extends PowerUp {

	private static AtlasRegion texture;

	public BoardshockPowerUp(float x, float y, GameState gs) {
		super(x, y, gs, -1);
	}

	@Override
	protected void affect(Player p) {
		gs.addBoardshockCharge(1f);
	}

	@Override
	public AtlasRegion getTexture() {
		return texture;
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/BoardShock");
	}
	
}
