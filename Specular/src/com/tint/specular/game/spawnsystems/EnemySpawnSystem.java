package com.tint.specular.game.spawnsystems;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyFast;
import com.tint.specular.game.entities.enemies.EnemyNormal;
import com.tint.specular.game.entities.enemies.EnemyWorm;
import com.tint.specular.utils.Util;

public class EnemySpawnSystem extends SpawnSystem {
	
	private int enemiesSpawned = 0;
	
	public EnemySpawnSystem(GameState gs) {
		super(gs);
	}

	public void spawn(int wave) {
		int enemyID = 0;
		int enemiesAdded = 0;
		int x;
		int y;
		
		outer :
		do {
			x = rand.nextInt(gs.getCurrentMap().getWidth());
			y = rand.nextInt(gs.getCurrentMap().getHeight());
			
			for(Player p : gs.getPlayers()) {
				if(Util.getDistanceSquared(x, y, p.getCenterX(), p.getCenterY()) < Math.pow(1000, 2)) {
					continue outer;
				}
			}
			
			enemyID = rand.nextInt(6);
			
			if(enemyID < 3)
				gs.addEntity(new EnemyNormal(x, y, gs));
			else if(enemyID < 5)
				gs.addEntity(new EnemyFast(x, y, gs));
			else if(enemyID == 5)
				gs.addEntity(new EnemyBooster(x, y, gs));
			
			enemiesAdded++;
			enemiesSpawned++;
		} while(enemiesAdded <= wave);
	}
	
	public void spawnBoss() {
		boolean bossAdded = false;
		doLoop :
		do {
			int x = rand.nextInt(gs.getCurrentMap().getWidth());
			int y = rand.nextInt(gs.getCurrentMap().getHeight());
			
			for(Player p : gs.getPlayers()) {
				if(Util.getDistanceSquared(x, y, p.getCenterX(), p.getCenterY())
						< Math.pow(1000, 2)) {
					continue doLoop;
				}
			}
			
			gs.addEntity(new EnemyWorm(x, y, gs));
			bossAdded = true;
		} while(!bossAdded);
	}
	
	public int getSpawnedEnemies() {
		return enemiesSpawned;
	}
}
