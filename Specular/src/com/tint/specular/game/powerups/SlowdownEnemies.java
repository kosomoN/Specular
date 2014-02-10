package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class SlowdownEnemies extends PowerUp {
	private static Texture texture;
	private static boolean hasUpdatedSlowdown = false;
	
	public SlowdownEnemies(float x, float y, GameState gs) {
		super(x, y, gs, 300);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/Slowdown.png"));
	}
	
	@Override
	protected void affect(Player player) {
		Enemy.setSlowdown(0.25f);
	}

	@Override
	public boolean update() {
		if(isActivated() && !hasUpdatedSlowdown) {
			hasUpdatedSlowdown = true;
			Enemy.setSlowdown(Enemy.getSlowdown() + 0.75f / 300);
		}
		
		return super.update();
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
	
	public static void setUpdatedSlowdown(boolean slow) {
		hasUpdatedSlowdown = slow;
	}
}
