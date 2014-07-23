package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class BulletBurst extends PowerUp {
	private static AtlasRegion texture;
	private static float maxActiveTime = 800;
	
	public BulletBurst(float x, float y, GameState gs) {
		super(x, y, gs, maxActiveTime);
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/5 Burst");
//		maxActiveTime = Specular.prefs.getFloat("Burst Max Time");
	}
	
	@Override
	protected void affect(Player player) {
		player.addBulletBurstLevel(1);
	}
	
	@Override
	protected void removeEffect(Player player) {
		player.addBulletBurstLevel(-1);
	}

	@Override
	public boolean update() {
		return super.update();
	}
	
	public static void setMaxActiveTime(float maxActiveTime) { BulletBurst.maxActiveTime = maxActiveTime; }
	public static float getMaxActiveTime() { return maxActiveTime; }

	@Override
	public AtlasRegion getTexture() {
		return texture;
	}
}
