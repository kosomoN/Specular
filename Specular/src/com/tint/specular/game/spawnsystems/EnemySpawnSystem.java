package com.tint.specular.game.spawnsystems;

import java.util.Random;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyFast;
import com.tint.specular.game.entities.enemies.EnemyNormal;
import com.tint.specular.utils.Util;

public class EnemySpawnSystem {
	
	private GameState gs;
	private int enemiesSpawned = 0;
	
	public EnemySpawnSystem(GameState gs) {
		this.gs = gs;
	}

	public void spawn(int wave) {
		
		Random rand = new Random();
		int enemyID = 0;
		int enemiesAdded = 0;
		int x;
		int y;
		
		do {
			x = rand.nextInt(gs.getCurrentMap().getWidth());
			y = rand.nextInt(gs.getCurrentMap().getHeight());
			
			if(Util.getDistance(x, gs.getPlayer().getCenterX(),
					y, gs.getPlayer().getCenterY()) < 1000) {
				continue;
			}
			
			enemyID = rand.nextInt(6);
			
			if(enemyID < 3)
				gs.addEntity(new EnemyNormal(x, y, gs.getPlayer(), gs));
			else if(enemyID < 5)
				gs.addEntity(new EnemyFast(x, y, gs.getPlayer(), gs));
			else if(enemyID == 5)
				gs.addEntity(new EnemyBooster(x, y, gs.getPlayer(), gs));
			
			enemiesAdded++;
			enemiesSpawned++;
		} while(enemiesAdded <= wave);
	}
	
	public int getSpawnedEnemies() {
		return enemiesSpawned;
	}
}
