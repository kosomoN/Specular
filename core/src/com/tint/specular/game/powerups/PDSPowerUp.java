package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.PDS;
import com.tint.specular.game.entities.Player;

public class PDSPowerUp extends PowerUp{

	private static AtlasRegion tex;
	
	public PDSPowerUp(float x, float y, GameState gs) {
		super(x, y, gs, -1);
	}
	
	public static void init(TextureAtlas ta) {
		tex = ta.findRegion("game1/Point Defense");
	}
	
	@Override
	protected void affect(Player player) {
		PDS.refillAmmo(10);
	}

	@Override
	public AtlasRegion getTexture() {
		return tex;
	}

}
