package com.tint.specular.game.entities;

import static com.tint.specular.game.entities.enemies.Enemy.EnemyType.*;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.WaveManager.WaveModifier;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.Enemy.EnemyType;
import com.tint.specular.game.entities.enemies.EnemyWanderer;

public class WaveLoader {

	public static void initWaves(GameState gs, List<Wave> specialWaves) {
		Gdx.app.log("Specular", "Loading Waves");
		
		final Random rand = new Random();
		
		/* Enemy specific waves */
		
		// Booster Wave
		Wave wave = new Wave(gs, 0, 600);
		wave.addEnemies(ENEMY_BOOSTER, 4, Formation.CORNERS, 0, 100);
		wave.setPermanentModifer(new WaveModifier() {
			@Override
			public void affectSpecial(GameState gs, Enemy justSpawnedEnemy) {
				justSpawnedEnemy.setLife(2);
			}
		});
		specialWaves.add(wave);
		
		// Dasher Wave
		wave = new Wave(gs, 1, 800);
			int side = rand.nextInt(4);
			if(side == 0)
				Formation.SIDE.setSide(Formation.Sides.LEFT);
			else if(side == 1)
				Formation.SIDE.setSide(Formation.Sides.RIGHT);
			else if(side == 2)
				Formation.SIDE.setSide(Formation.Sides.UP);
			else
				Formation.SIDE.setSide(Formation.Sides.DOWN);
			
			wave.addEnemies(ENEMY_DASHER, 4, Formation.SIDE, 0, 2);
		specialWaves.add(wave);
		
		// Exploder Wave
		wave = new Wave(gs, 2, 400);
			wave.addEnemies(ENEMY_EXPLODER, 8, Formation.EDGES, 0, 20);
			wave.setPermanentModifer(new WaveModifier() {
				@Override
				public void affectBase(GameState gs, Enemy justSpawnedEnemy) {
					justSpawnedEnemy.setLife(justSpawnedEnemy.getLife() * 2);
				}
			});
		specialWaves.add(wave);
		
		// Shielder Wave
		wave = new Wave(gs, 3, 600);
			wave.addEnemies(ENEMY_SHIELDER, 1, Formation.RANDOM, 0, 50);
			wave.setPermanentModifer(new WaveModifier() {
				@Override
				public void affectBase(GameState gs, Enemy justSpawnedEnemy) {
					Enemy e = justSpawnedEnemy;
					e.setX(50 + rand.nextInt(gs.getCurrentMap().getWidth() - 100));
					e.setY(50 + rand.nextInt(gs.getCurrentMap().getHeight() - 100));
					gs.addEntity(e);
				}
			});
		specialWaves.add(wave);
		
		// Wanderer Wave
		wave = new Wave(gs, 4, 400);
			wave.addEnemies(ENEMY_WANDERER, 10, Formation.RANDOM, 0, 5);
		specialWaves.add(wave);
		
		// Tanker Wave
		wave = new Wave(gs, 5, 600);
			wave.addEnemies(ENEMY_TANKER, 3, Formation.RANDOM, 0, 100);
		specialWaves.add(wave);
		
		// Circler Wave
		Formation f = Formation.RINGS;
			f.setCenterRingPoint(gs.getCurrentMap().getWidth() / 2, gs.getCurrentMap().getHeight() / 2);
			wave = new Wave(gs, 6, 800);
				wave.addEnemies(ENEMY_CIRCLER, 8, f, 10, 1, false);
			
			f.setRadius(400);
				wave.addEnemies(ENEMY_CIRCLER, 16, f, 20, 1, false);
			
			f.setRadius(700);
				wave.addEnemies(ENEMY_CIRCLER, 32, f, 30, 1, false);
		specialWaves.add(wave);
			
		// Striver Wave
		wave = new Wave(gs, 7, 600);
			wave.addEnemies(ENEMY_STRIVER, 20, Formation.EDGES, 0, 0);
		specialWaves.add(wave);
		
		// Worm Wave
		wave = new Wave(gs, 8, 700);
			wave.addEnemies(ENEMY_WORM, 1, Formation.CORNERS, 0, 0);
			wave.setPermanentModifer(new WaveModifier() {
				@Override
				public void affectBase(GameState gs, Enemy justSpawnedEnemy) {
					Enemy e = justSpawnedEnemy;
					e.setX(50 + rand.nextInt(gs.getCurrentMap().getWidth() - 100));
					e.setY(50 + rand.nextInt(gs.getCurrentMap().getHeight() - 100));
					gs.addEntity(e);
				}
			});
			
		specialWaves.add(wave);
		
		/* Combinations */
		
		// Worm and Dasher, should change
		wave = new Wave(gs, 9, 800);
			wave.addEnemies(new EnemyType[] {ENEMY_WORM, ENEMY_DASHER}, new int[] {1, 5}, Formation.RANDOM, 0, 10);
		specialWaves.add(wave);
		
		// Shielder and wanderer, idea pretty clear
		wave = new Wave(gs, 10, 800);
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
		
		Gdx.app.log("Specular", "Loading Waves Complete");
	}
}
