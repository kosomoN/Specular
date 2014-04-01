package com.tint.specular.game.entities;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.WaveManager.WaveModifier;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.Enemy.EnemyType;
import com.tint.specular.game.entities.enemies.EnemyWanderer;

import static com.tint.specular.game.entities.enemies.Enemy.EnemyType.*;


public class WaveLoader {

	public static void initWaves(GameState gs, List<Wave> specialWaves) {
		Gdx.app.log("Specular", "Loading Waves");
		
		//shieldwave, I am shielder, I am pain, I am darkness
		Wave wave = new Wave(gs, 0, 800);
		wave.addEnemies(new EnemyType[] {ENEMY_SHIELDER, ENEMY_WANDERER}, new int[] {1, 20}, Formation.SURROUND_ENEMY, 0, 1);
		wave.setPermanentModifer(new WaveModifier() {
			@Override
			public void affectSpecial(GameState gs, Enemy justSpawnedEnemy) {
				if(justSpawnedEnemy instanceof EnemyWanderer) {
					justSpawnedEnemy.setLife(10);
				}
			}
		});
		specialWaves.add(wave);
		
		wave = new Wave(gs, 1, 400);
			wave.addEnemies(ENEMY_WANDERER, 30, Formation.RANDOM, 0, 5);
		specialWaves.add(wave);
		
		wave = new Wave(gs, 2, 600);
			wave.addEnemies(ENEMY_BOOSTER, 10, Formation.EDGES, 0, 50);
		specialWaves.add(wave);
		
		wave = new Wave(gs, 3, 600);
			wave.addEnemies(ENEMY_EXPLODER, 10, Formation.RANDOM, 0, 60);
		specialWaves.add(wave);
		
		wave = new Wave(gs, 4, 400);
			wave.addEnemies(ENEMY_DASHER, 6, Formation.RANDOM, 0, 20);
		specialWaves.add(wave);
			
		Gdx.app.log("Specular", "Loading Waves Complete");
	}
}
