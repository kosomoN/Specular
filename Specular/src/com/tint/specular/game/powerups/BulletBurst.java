package com.tint.specular.game.powerups;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class BulletBurst extends PowerUp {
	private static Texture texture;
	private static List<Player> playersFired = new ArrayList<Player>();
	private static int timesFiredSamePlayer;
	private Player player;
	private float timeSinceLastFire, fireRate;
	
	public BulletBurst(float x, float y, GameState gs) {
		super(x, y, gs);
		activeTime = 600;
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/5 Burst.png"));
	}
	
	@Override
	protected void affect(Player player) {
		this.player = player;
		player.addBulletBurstLevel(1);
		timeSinceLastFire = player.getTimeSinceLastFire();
		fireRate = player.getFireRate();
	}

	@Override
	public void render(SpriteBatch batch) {
		if(!isActivated() && despawnTime > 0)
			batch.draw(texture, x, y);
	}
	
	@Override
	public boolean update() {
		timeSinceLastFire++;
		
		if(super.update()) {
			player.addBulletBurstLevel(-1);
			if(player.getBulletBurstLevel() == 0) {
				player.setBulletBurst(3);
			}
			return true;
		}
		
		if(isActivated() && gs.getGameProcessor().getShootStick().isActive()) {
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
						player.shoot(direction, offset, spaces - 2);
						
						direction -= 180;
						direction = direction % 360;
						player.shoot(direction, offset, spaces - 2);
					}
					
					if(level > 2) {
						direction -= 90;
						direction = direction % 360;
						player.shoot(direction, offset, spaces - 2);
					}
					
					timeSinceLastFire = 0;
				}
			}
		}
		
		return false;
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
	
	@Override
	public void dispose() {
		texture.dispose();
	}
}
