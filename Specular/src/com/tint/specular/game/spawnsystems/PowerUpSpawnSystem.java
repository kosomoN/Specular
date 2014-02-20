package com.tint.specular.game.spawnsystems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.powerups.AddLife;
import com.tint.specular.game.powerups.BoardshockPowerUp;
import com.tint.specular.game.powerups.BulletBurst;
import com.tint.specular.game.powerups.FireRateBoost;
import com.tint.specular.game.powerups.PowerUp;
import com.tint.specular.game.powerups.Ricochet;
import com.tint.specular.game.powerups.ScoreMultiplier;
import com.tint.specular.game.powerups.ShieldUpgrade;
import com.tint.specular.game.powerups.SlowdownEnemies;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class PowerUpSpawnSystem extends SpawnSystem {

	
	public PowerUpSpawnSystem(GameState gs) {
		super(gs);
	}
	
	/**
	 * Spawns a random power-up at a random location
	 */
	public void spawn() {
		float x = rand.nextInt(gs.getCurrentMap().getWidth() - 100) + 50;
		float y = rand.nextInt(gs.getCurrentMap().getHeight() - 100) + 50;
		
		/* First it takes in to account the affecting powerups, then it sets how common a powerup is, and lastly adds a random factor in
		 * Example:
		 * Affecting powerups ~ 1f - player.getLife() / 4f; to get importance in percentage
		 * How common ~ 0.6f; ranging from 0% to 100%
		 * Random factor ~ rand.nextInt(10) / 10f; returning a float casted double
		 */
		Player player = gs.getPlayer();
		float life = 1f - player.getLife() / 4f * 0.6f + rand.nextInt(10) / 10f,
				fireRate = player.getFireRate() / 10f * 0.8f + rand.nextInt(10) / 10f, 
				burst = 1f - player.getBulletBurstLevel() / 3f * 0.8f + rand.nextInt(10) / 10f, 
				score = 0.75f + rand.nextInt(10) / 10f, 
				slow = Enemy.getSlowdown() / 1f * 0.75f + rand.nextInt(10) / 10f, 
				shield = 1f - player.getShields() / 3 * 0.6f + rand.nextInt(10) / 10f, 
				board = 1f - gs.getBoardshockCharge() * 0.85f + rand.nextInt(10) / 10f, 
				ricochet = 0.5f + rand.nextInt(10) / 10f;
		
		// Sorting values in a descending order wwith the highest value on top
		List<Float> importance = new ArrayList<Float>();
		importance.add(life);
		importance.add(fireRate);
		importance.add(burst);
		importance.add(score);
		importance.add(slow);
		importance.add(shield);
		importance.add(board);
		importance.add(ricochet);
		Collections.sort(importance);
		Collections.reverse(importance);
		
		// A random of the 3 highest values (maybe not necessary)
		float powerUp = importance.get(rand.nextInt(3));
		
		if(powerUp == life && player.getLife() < 4)
			gs.addEntity(new AddLife(x, y, gs));
		else if(powerUp == fireRate)
			gs.addEntity(new FireRateBoost(x, y, gs));
		else if(powerUp == burst)
			gs.addEntity(new BulletBurst(x, y, gs));
		else if(powerUp == score)
			gs.addEntity(new ScoreMultiplier(x, y, gs));
		else if(powerUp == slow)
			gs.addEntity(new SlowdownEnemies(x, y, gs));
		else if(powerUp == shield && player.getShields() < 3)
			gs.addEntity(new ShieldUpgrade(x, y, gs));
		else if(powerUp == board)
			gs.addEntity(new BoardshockPowerUp(x, y, gs));
		else if(powerUp == ricochet)
			gs.addEntity(new Ricochet(x, y, gs));
	}
	
	/**
	 * Use to spawn one specific powerup at random location
	 * @param pu - The power-up to spawn
	 */
	public void spawn(PowerUp pu) {
		float x = rand.nextInt(gs.getCurrentMap().getWidth() - 100) + 50, y = rand.nextInt(gs.getCurrentMap().getHeight() - 100) + 50;
		pu.setPosition(x, y);
		
		gs.addEntity(pu);
	}
}
