package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;

public class PDS implements Entity {

	private static int ammo;
	private static float range;
	private static float damage = 2;
	private Sound pdsSound = Gdx.audio.newSound(Gdx.files.internal("audio/fx/Laser.ogg"));
	private GameState gs;
	private Player player;
	
	public PDS(GameState gs, Player player) {
		this.gs = gs;
		this.player = player;
		range = 200;
		damage = Specular.prefs.getFloat("PDS Damage");
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
					if(gs.isSoundEnabled())
						pdsSound.play();
					
					e.hit(damage);
					gs.addEntity(Laser.obtainLaser(player.getX(), player.getY(), e.getX(), e.getY(), 0, false));;
					ammo--;
				}
			}
		}
		
		return false;
	}
	
	public static void refillAmmo(int ammo0, int ammo1) {
		PDS.ammo += ammo0;
		if(ammo1 == 1) {
			PDS.ammo = 0;
		}
	}
	
	public static void increaseRange(float range) {
		PDS.range += range;
	}
	
	public static boolean isActive() {
		 return ammo > 0;
	}
	
	public static void setDamage(float damage) { PDS.damage = damage; }
	public static float getDamage() { return damage; }

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
