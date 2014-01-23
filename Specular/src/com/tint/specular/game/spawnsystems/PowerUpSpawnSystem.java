package com.tint.specular.game.spawnsystems;

import java.util.Random;

import com.tint.specular.game.GameState;
import com.tint.specular.game.powerups.AddLife;
import com.tint.specular.game.powerups.BulletBurst_5;
import com.tint.specular.game.powerups.FireRateBoost;
import com.tint.specular.game.powerups.ScoreMultiplier;
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
	
	public void spawn() {
		Random rand = new Random();
		int powerUpID = rand.nextInt(5);
		
		float x = rand.nextInt(gs.getCurrentMap().getWidth() - 64) + 32, y = rand.nextInt(gs.getCurrentMap().getHeight() - 64) + 32;
		
		if(powerUpID == 0)
			gs.addEntity(new AddLife(x, y, gs));
		else if(powerUpID == 1)
			gs.addEntity(new FireRateBoost(x, y, gs));
		else if(powerUpID == 2)
			gs.addEntity(new BulletBurst_5(x, y, gs));
		else if(powerUpID == 3)
			gs.addEntity(new ScoreMultiplier(x, y, gs));
		else if(powerUpID == 4)
			gs.addEntity(new SlowdownEnemies(x, y, gs));
	}
}
