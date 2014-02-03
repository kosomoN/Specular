package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class AddLife extends PowerUp {
	private static Texture texture;
	
	public AddLife(float x, float y, GameState gs) {
		super(x, y, gs);
		activeTime = -1;
	}

	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/Life.png"));
	}
	
	@Override
	protected void affect(Player player) {
		player.addLives(1);
	}

	@Override
	public void render(SpriteBatch batch) {
		if(!isActivated() && despawnTime > 0)
			batch.draw(texture, x, y);
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
