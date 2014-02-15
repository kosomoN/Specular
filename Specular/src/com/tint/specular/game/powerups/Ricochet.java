package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class Ricochet extends PowerUp {

	private static Texture texture;
	
	public Ricochet(float x, float y, GameState gs, float activeTime) {
		super(x, y, gs, activeTime);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/powerups/"));
	}

	@Override
	protected void affect(Player p) {
		gs.getCurrentMap().setReflective(true);
	}
	
	@Override
	public boolean update() {
		if(super.update()) {
			if(isActivated())
				gs.getCurrentMap().setReflective(false);
			return true;
		}
		
		return false;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
}
