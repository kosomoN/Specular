package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class ScoreMultiplier extends PowerUp {
	private static Texture texture;
	
	public ScoreMultiplier(float x, float y, GameState gs) {
		super(x, y, gs);
		activeTime = -1;
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/Multiplier.png"));
	}
	
	@Override
	protected void affect(Player player) {
		gs.setScoreMultiplier(gs.getScoreMultiplier() + 1);
	}

	@Override
	public void render(SpriteBatch batch) {
		if(!isActivated() && despawnTime > 0)
			Util.drawCentered(batch, texture, x, y, 0);
	}
	
	@Override
	public boolean update() {
		return super.update() == true ? true : despawnTime <= 0;
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
