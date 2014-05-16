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
		
		/* Enemy specific waves */
		
		// Booster Wave
		Wave wave = new Wave(gs, 0, 600);
		wave.addEnemies(ENEMY_BOOSTER, 8, Formation.EDGES, 0, 50);
		wave.setPermanentModifer(new WaveModifier() {
			@Override
			public void affectSpecial(GameState gs, Enemy justSpawnedEnemy) {
				justSpawnedEnemy.setLife(2);
			}
		});
		specialWaves.add(wave);
		
		// Dasher Wave
		wave = new Wave(gs, 1, 800);
			Formation.SIDE.setSide(Formation.Sides.LEFT);
			wave.addEnemies(ENEMY_DASHER, 6, Formation.SIDE, 0, 2);
		specialWaves.add(wave);
		
		// Exploder Wave
		wave = new Wave(gs, 2, 400);
			wave.addEnemies(ENEMY_EXPLODER, 8, Formation.CORNERS, 0, 20);
			wave.setPermanentModifer(new WaveModifier() {
				@Override
				public void affectBase(GameState gs, Enemy justSpawnedEnemy) {
					justSpawnedEnemy.setLife(justSpawnedEnemy.getLife() * 2);
				}
			});
		specialWaves.add(wave);
		
		// Shielder Wave, I am shielder, I am pain, I am darkness
		wave = new Wave(gs, 3, 800);
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
		
		// Wanderer Wave
		wave = new Wave(gs, 4, 400);
			wave.addEnemies(ENEMY_WANDERER, 10, Formation.RANDOM, 0, 5);
		specialWaves.add(wave);
		
		// Tanker Wave
		wave = new Wave(gs, 5, 600);
			wave.addEnemies(ENEMY_TANKER, 5, Formation.RANDOM, 0, 60);
		specialWaves.add(wave);
		
		// Circler Wave
		Formation f = Formation.RINGS;
			wave = new Wave(gs, 6, 800);
				wave.addEnemies(ENEMY_CIRCLER, 8, f, 10, 1, false);
			
			f.setRadius(400);
				wave.addEnemies(ENEMY_CIRCLER, 16, f, 20, 1, false);
			
			f.setRadius(700);
				wave.addEnemies(ENEMY_CIRCLER, 32, f, 30, 1, false);
			specialWaves.add(wave);
			
			wave = new Wave(gs, 7, 800);
				wave.addEnemies(ENEMY_TANKER, 5, Formation.RANDOM, 0, 20);
		specialWaves.add(wave);
		
		// The others
		wave = new Wave(gs, 8, 800);
		wave.addEnemies(new EnemyType[] {ENEMY_WORM, ENEMY_DASHER}, new int[] {1, 5}, Formation.RANDOM, 0, 10);
		specialWaves.add(wave);
		
		Gdx.app.log("Specular", "Loading Waves Complete");
	}
}
