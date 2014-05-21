package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.Player.AmmoType;

public class LaserPowerup extends PowerUp {

	private static Texture tex;
	
	public LaserPowerup(float x, float y, GameState gs) {
		super(x, y, gs, 800);
	}
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/powerups/Laser.png"));
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
	public void pause() {
		super.pause();
		gs.getPlayer().changeAmmo(AmmoType.BULLET);
	}

	@Override
	public void resume() {
		super.resume();
		gs.getPlayer().changeAmmo(AmmoType.LASER);
	}

	@Override
	public Texture getTexture() {
		return tex;
	}

}
