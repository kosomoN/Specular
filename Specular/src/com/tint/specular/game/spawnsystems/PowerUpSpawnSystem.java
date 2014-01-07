package com.tint.specular.game.spawnsystems;

import java.util.Random;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.powerups.*;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class PowerUpSpawnSystem extends SpawnSystem {

	
	public PowerUpSpawnSystem(GameState gs) {
		super(gs);
	}
	
	public void spawn(Enemy e) {
		Random rand = new Random();
		int powerUpID = rand.nextInt(7);
		
		float x = e.getX(), y = e.getY();
		
		//Checking so that no powerup spawns "outside" the map
		if(x > gs.getCurrentMap().getWidth() - 32)
			x = gs.getCurrentMap().getWidth() - 32;
		else if(x < 32)
			x = 32;
		
		if(y > gs.getCurrentMap().getHeight() - 32)
			y = gs.getCurrentMap().getHeight() - 32;
		else if(y < 32)
			y = 32;
		
		if(powerUpID == 0)
			gs.addEntity(new AddLife(x, y, gs));
		else if(powerUpID == 1)
			gs.addEntity(new BulletBurst_3(x, y, gs));
		else if(powerUpID == 2)
			gs.addEntity(new BulletBurst_5(x, y, gs));
		else if(powerUpID == 3)
			gs.addEntity(new ScoreMultiplier(x, y, gs));
		else if(powerUpID == 4)
			gs.addEntity(new SlowdownEnemies(x, y, gs));
		else if(powerUpID == 5)
			gs.addEntity(new FireRateBoost(x, y, gs));
		else if(powerUpID == 6)
			gs.addEntity(new ComboDamageBooster(x, y, gs));
	}
}
