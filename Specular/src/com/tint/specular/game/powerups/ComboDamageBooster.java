package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

public class ComboDamageBooster extends PowerUp {
private static Texture texture;
	
	public ComboDamageBooster(float x, float y, GameState gs) {
		super(x, y, gs);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/DamageBooster.png"));
	}
	
	@Override
	protected void affect(Player player) {
		gs.setDamageBooster(gs.getDamageBooster() * 1.5);
	}

	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, texture, x, y, 0);
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
