package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.states.UpgradeState;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class SlowdownEnemies extends PowerUp {
	private static AtlasRegion texture;
	private static Texture levelTex;
	private static boolean hasUpdatedSlowdown = false;
	private static float freezeTime; // Ms
	private float ticksFrozen; // Only handled in one slowdown
	
	public SlowdownEnemies(float x, float y, GameState gs) {
		super(x, y, gs, 300);
	}
	
	public SlowdownEnemies() {
		super();
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/Slowdown");
//		freezeTime = Specular.prefs.getFloat("Freeze Time");
	}
	
	public static void reloadLevelTextures(float grade) {
		levelTex = UpgradeState.getUpgradeLevelTexture(grade);
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
	public AtlasRegion getTexture() {
		return texture;
	}
	
	public static void setUpdatedSlowdown(boolean slow) {
		hasUpdatedSlowdown = slow;
	}
	
	public static void setFreezeTime(float freezeTime) { SlowdownEnemies.freezeTime = freezeTime; }
	public static float getFreezeTime() { return freezeTime; }

	@Override
	public Texture getLevelTexture() {
		return levelTex;
	}
}
