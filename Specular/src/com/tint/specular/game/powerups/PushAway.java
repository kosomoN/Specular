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
		super(x, y, gs, 600);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/powerups/Repulsor.png"));
	}
	
	@Override
	protected void updatePowerup(Player player) {
		float distanceSquared;
		double angle;
		for(Enemy e : gs.getEnemies()) {
			distanceSquared = (e.getX() - player.getX()) * (e.getX() - player.getX()) + (e.getY() - player.getY()) * (e.getY() - player.getY());
			if(PUSHAWAY_RANGE > distanceSquared) {
				angle = Math.atan2(e.getY() - player.getY(), e.getX() - player.getX());
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
