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
	private static float freezeTime; // Ms
	private float ticksFrozen; // Only handled in one slowdown
	
	public SlowdownEnemies(float x, float y, GameState gs) {
		super(x, y, gs, 300);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/powerups/Slowdown.png"));
//		freezeTime = Specular.prefs.getFloat("Freeze Time");
	}
	
	@Override
	protected void affect(Player player) {
		Enemy.setSlowdown(0.25f);
		ticksFrozen = 0;
	}

	@Override
	protected void updatePowerup(Player player) {
		if(!hasUpdatedSlowdown) {
			hasUpdatedSlowdown = true;
			
			if(ticksFrozen >= freezeTime / 60f)
				Enemy.setSlowdown(Enemy.getSlowdown() + 0.75f / 300);
			ticksFrozen++;
		}
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
	
	public static void setUpdatedSlowdown(boolean slow) {
		hasUpdatedSlowdown = slow;
	}
	
	public static void setFreezeTime(float freezeTime) { SlowdownEnemies.freezeTime = freezeTime; }
	public static float getFreezeTime() { return freezeTime; }
}
