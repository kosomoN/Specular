package com.tint.specular.game.spawnsystems;

import com.badlogic.gdx.utils.Array;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyFast;
import com.tint.specular.game.entities.enemies.EnemyNormal;
import com.tint.specular.game.entities.enemies.EnemyStupid;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class EnemySpawnSystem extends SpawnSystem {

	private int enemiesSpawned = 0;
	private Array<Enemy> enemyList;
	
	public EnemySpawnSystem(GameState gs, Array<Enemy> enemyList) {
		super(gs);
		this.enemyList = enemyList;
	}

	public void update(int ticks) {
		if(ticks > 5000) { //Different-spawns-depending-on-time-played concept
			
		} if(ticks > 2000) {
			
		} if(ticks > 200) {
			
		}
		
		//Remove this in case you are going to use the concept above
		if(enemyList.size < ticks / 100) {
			int randomId = rand.nextInt(6);
			if(randomId < 3) {
				putIntoGame(new EnemyNormal(0, 0, gs));
			} else if(randomId < 5) {
				putIntoGame(new EnemyFast(0, 0, gs));
			} else {
				putIntoGame(new EnemyBooster(0, 0, gs));
			}
		}
	}
	
	private void putIntoGame(Enemy ent) {
		int x;
		int y;
		while(true) {//I'm a bit skeptical about this loop
			x = rand.nextInt(gs.getCurrentMap().getWidth());
			y = rand.nextInt(gs.getCurrentMap().getHeight());
			if(Specular.camera.position.x - Specular.camera.viewportWidth / 2 - 100 > x ||
					Specular.camera.position.x + Specular.camera.viewportWidth / 2 + 100 < x ||
					Specular.camera.position.y - Specular.camera.viewportHeight / 2 - 100 > y ||
					Specular.camera.position.y + Specular.camera.viewportHeight / 2 + 100 < y) {
				ent.setX(x);
				ent.setY(y);
				gs.addEntity(ent);
				return;
			}
		}
	}
	
	public int getSpawnedEnemies() {
		return enemiesSpawned;
	}
}
