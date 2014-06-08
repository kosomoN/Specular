package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class BulletBurst extends PowerUp {
	private static Texture texture;
	private static float maxActiveTime = 800;
	
	public BulletBurst(float x, float y, GameState gs) {
		super(x, y, gs, maxActiveTime);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/powerups/5 Burst.png"));
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
	public float getRadius() {
		return texture.getWidth() / 2;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
}
