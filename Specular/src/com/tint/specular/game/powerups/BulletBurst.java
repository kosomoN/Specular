package com.tint.specular.game.powerups;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.Player.AmmoType;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class BulletBurst extends PowerUp {
	private static Texture texture;
	private static List<Player> playersFired = new ArrayList<Player>();
	private static int timesFiredSamePlayer;
	private float timeSinceLastFire, fireRate;
	
	public BulletBurst(float x, float y, GameState gs) {
		super(x, y, gs, 800);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/powerups/5 Burst.png"));
	}
	
	@Override
	protected void affect(Player player) {
		player.addBulletBurstLevel(1);
		timeSinceLastFire = player.getTimeSinceLastFire();
		fireRate = player.getFireRate();
	}

	@Override
	public boolean update() {
		timeSinceLastFire++;
		
		Player player = gs.getPlayer();
		if(super.update()) {
			player.addBulletBurstLevel(-1);
			if(player.getBulletBurstLevel() <= 0) {
				player.setBulletBurst(3);
				player.setBulletBurstLevel(0);
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected void updatePowerup(Player player) {
		if(player.getAmmoType() != AmmoType.LASER && gs.getGameProcessor().getShootStick().isActive()) {
			if(playersFired.contains(player))
				timesFiredSamePlayer++;

			if(timesFiredSamePlayer < player.getBulletBurstLevel()) {
				if(timeSinceLastFire >= fireRate) {
					fireRate = player.getFireRate();
					playersFired.add(player);
					
					float direction = player.getDirection();
					int offset = 8;
					int spaces;
					int level = player.getBulletBurstLevel();
					
					if(level > 0)
						player.setBulletBurst(5);
					
					spaces = player.getBulletBurst() - 1;
					if(level > 1) {
						direction += 90;
						direction = direction % 360;
						player.shootBullet(direction, offset, spaces - 2);
						
						direction -= 180;
						direction = direction % 360;
						player.shootBullet(direction, offset, spaces - 2);
					}
					
					if(level > 2) {
						direction -= 90;
						direction = direction % 360;
						player.shootBullet(direction, offset, spaces - 2);
					}
					
					timeSinceLastFire = 0;
				}
			}
		}
	}
	
	public static void updateBulletBursts() {
		playersFired.clear();
		timesFiredSamePlayer = 0;
	}

	@Override
	public float getRadius() {
		return texture.getWidth() / 2;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
}
