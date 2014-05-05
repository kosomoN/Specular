package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.PDS;
import com.tint.specular.game.entities.Player;

public class PDSPowerUp extends PowerUp{

	private static Texture tex;
	
	public PDSPowerUp(float x, float y, GameState gs) {
		super(x, y, gs, -1);
	}
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/powerups/Point Defense.png"));
	}
	
	@Override
	protected void affect(Player player) {
		PDS.refillAmmo(10);
	}

	@Override
	public Texture getTexture() {
		return tex;
	}

}
