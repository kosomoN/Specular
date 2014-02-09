package com.tint.specular.game.spawnsystems;

import java.util.Random;

import com.tint.specular.game.GameState;
import com.tint.specular.game.powerups.AddLife;
import com.tint.specular.game.powerups.BoardshockPowerUp;
import com.tint.specular.game.powerups.BulletBurst;
import com.tint.specular.game.powerups.FireRateBoost;
import com.tint.specular.game.powerups.PowerUp;
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
		Random rand = new Random();
		int powerUpID = rand.nextInt(7);
		
		float x = rand.nextInt(gs.getCurrentMap().getWidth() - 100) + 50;
		float y = rand.nextInt(gs.getCurrentMap().getHeight() - 100) + 50;
		
		if(powerUpID == 0)
			gs.addEntity(new AddLife(x, y, gs));
		else if(powerUpID == 1)
			gs.addEntity(new FireRateBoost(x, y, gs));
		else if(powerUpID == 2)
			gs.addEntity(new BulletBurst(x, y, gs));
		else if(powerUpID == 3)
			gs.addEntity(new ScoreMultiplier(x, y, gs));
		else if(powerUpID == 4)
			gs.addEntity(new SlowdownEnemies(x, y, gs));
		else if(powerUpID == 5)
			gs.addEntity(new ShieldUpgrade(x, y, gs));
		else if(powerUpID == 6)
			gs.addEntity(new BoardshockPowerUp(x, y, gs));
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
