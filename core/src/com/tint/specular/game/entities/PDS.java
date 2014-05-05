package com.tint.specular.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;

public class PDS implements Entity {

	private static int ammo;
	private static float range;
	private GameState gs;
	private Player player;
	
	public PDS(GameState gs, Player player) {
		this.gs = gs;
		this.player = player;
		range = 200;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if(isActive()) {
			
		}
	}

	@Override
	public boolean update() {
		float dx, dy;
		float distSqrd;
		
		for(Enemy e : gs.getEnemies()) {
			if(e.hasSpawned()) {
				dx = (e.getX() - player.getX());
				dy = (e.getY() - player.getY());
				distSqrd = dx * dx + dy * dy;
				
				if(distSqrd < range * range) {
					e.hit(2);
					gs.addEntity(Laser.obtainLaser(player.getX(), player.getY(), e.getX(), e.getY(), 0, false));;
					ammo--;
				}
			}
		}
		
		return false;
	}
	
	public static void refillAmmo(int ammo) {
		PDS.ammo += ammo;
	}
	
	public static void increaseRange(float range) {
		PDS.range += range;
	}
	
	public static boolean isActive() {
		 return ammo > 0;
	}

	@Override
	public float getX() {
		return player.getX();
	}

	@Override
	public float getY() {
		return player.getY();
	}
	
	@Override
	public void dispose() {
		
	}
}
