package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;


public class PushAway extends PowerUp {
	private static final float PUSHAWAY_RANGE = 500 * 500;
	private static Texture texture;
	
	public PushAway(float x, float y, GameState gs) {
		super(x, y, gs, 800);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/powerups/Repulsor.png"));
	}
	
	@Override
	protected void updatePowerup(Player player) {
		float distanceSquared;
		double angle;
		float playerX = player.getX(), playerY = player.getY(), deltaX = 0, deltaY = 0;
		for(Enemy e : gs.getEnemies()) {
			deltaX = e.getX() - playerX;
			deltaY = e.getY() - playerY;
			distanceSquared = deltaX * deltaX + deltaY * deltaY;
			if(PUSHAWAY_RANGE > distanceSquared) {
				angle = Math.atan2(deltaY, deltaX);
				e.setX((float) (e.getX() + Math.cos(angle) * 20 * (1 - distanceSquared / PUSHAWAY_RANGE)));
				e.setY((float) (e.getY() + Math.sin(angle) * 20 * (1 - distanceSquared / PUSHAWAY_RANGE)));
			}
		}
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
}
