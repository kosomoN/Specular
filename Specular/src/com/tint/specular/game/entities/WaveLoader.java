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
		Wave wave = new Wave(gs, 0, 800);
		wave.addEnemies(new EnemyType[] {ENEMY_SHIELDER, ENEMY_WANDERER}, new int[] {1, 20}, Formation.SURROUND_ENEMY, 0, 1);
		wave.addEnemies(ENEMY_DASHER, 4, Formation.EDGES, 0, 20);
		wave.setPermanentModifer(new WaveModifier() {
			@Override
			public void affectSpecial(GameState gs, Enemy justSpawnedEnemy) {
				if(justSpawnedEnemy instanceof EnemyWanderer) {
					justSpawnedEnemy.setLife(10);
				}
			}
		});
		specialWaves.add(wave);
		
		Wave wave2 = new Wave(gs, 8, 800);
		wave2.addEnemies(ENEMY_EXPLODER, 8, Formation.RANDOM, 0, 20);
		specialWaves.add(wave2);
		
		Gdx.app.log("Specular", "Loading Waves Complete");
	}
}
