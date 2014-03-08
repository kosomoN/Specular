package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.Player.AmmoType;

public class Laser extends PowerUp {

	private static Texture tex;
	
	public Laser(float x, float y, GameState gs) {
		super(x, y, gs, 800);
	}
	
	public static void init() {
		tex = new Texture(Gdx.files.internal(""));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	protected void affect(Player player) {
		player.changeAmmo(AmmoType.LASER);
	}

	@Override
	protected void removeEffect(Player player) {
		player.changeAmmo(AmmoType.BULLET);
	}

	@Override
	public Texture getTexture() {
		return tex;
	}

}
