package com.tint.specular.game.entities;

import static com.tint.specular.game.entities.enemies.Enemy.EnemyType.*;

import java.util.Collections;
import java.util.Comparator;
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
		
		// Enemy specific waves
		
		// Base Wave
		Wave wave = new Wave(gs, 0, 600, 0);
			wave.addEnemies(ENEMY_WANDERER, 1, Formation.RANDOM, 0, 0);
		specialWaves.add(wave);
		
		// Booster Wave
		wave = new Wave(gs, 1, 600, 15);
			wave.addEnemies(ENEMY_BOOSTER, 4, Formation.CORNERS, 0, 100);
			wave.setPermanentModifer(new WaveModifier() {
				@Override
				public void affectSpecial(GameState gs, Enemy justSpawnedEnemy) {
					justSpawnedEnemy.setLife(2);
				}
			});
		specialWaves.add(wave);
		
		
		
		
		// Dasher Wave
		wave = new Wave(gs, 2, 800, 12);
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
		
		
		
		/* EXPLODER REMOVED UNTIL SOURCE OF LAG IS FOUND
		
		// Exploder Wave
		wave = new Wave(gs, 3, 400);
			wave.addEnemies(ENEMY_EXPLODER, 8, Formation.EDGES, 0, 20);
			wave.setPermanentModifer(new WaveModifier() {
				@Override
				public void affectBase(GameState gs, Enemy justSpawnedEnemy) {
					//Reverse life increase for tanky wanderers to stop them from being too OP
					justSpawnedEnemy.setLife(justSpawnedEnemy.getLife() * 2 / (gs.getGsTicks() / Wave.ENEMY_LIFE_INCREASE));
				}
			});
		specialWaves.add(wave);*/
		
		
		
		
		// Wanderer Wave
		Formation f = Formation.RINGS;
			f.setCenterRingPoint(gs.getCurrentMap().getWidth() / 2, gs.getCurrentMap().getHeight() / 2);
			wave = new Wave(gs, 5, 800, 5);
				wave.addEnemies(ENEMY_WANDERER, 8, f, 10, 1, false);
		
			f.setRadius(400);
				wave.addEnemies(ENEMY_WANDERER, 16, f, 20, 1, false);
		specialWaves.add(wave);
		
		
		
		
		// Tanker Wave
		Formation g = Formation.RINGS;
			g.setCenterRingPoint(gs.getCurrentMap().getWidth() / 2, gs.getCurrentMap().getHeight() / 2);
			wave = new Wave(gs, 6, 800, 30);
				wave.addEnemies(ENEMY_TANKER, 8, f, 10, 1, false);
		specialWaves.add(wave);
				
				
				
		// Circler Wave
		Formation h = Formation.RINGS;
			h.setCenterRingPoint(gs.getCurrentMap().getWidth() / 2, gs.getCurrentMap().getHeight() / 2);
			wave = new Wave(gs, 7, 800, 10);
				wave.addEnemies(ENEMY_CIRCLER, 8, f, 10, 1, false);
			
			h.setRadius(400);
				wave.addEnemies(ENEMY_CIRCLER, 16, f, 20, 1, false);
		specialWaves.add(wave);
			
		
		
		
		// Striver Wave
		wave = new Wave(gs, 8, 600, 3);
			wave.addEnemies(ENEMY_STRIVER, 16, Formation.EDGES, 0, 0);
		specialWaves.add(wave);
		
		
		
		
		// Virus wave
		wave = new Wave(gs, 9, 800, 7);
			wave.addEnemies(new EnemyType[] {ENEMY_VIRUS}, new int[] {7}, Formation.RANDOM, 0, 10);
		specialWaves.add(wave);	
		
		
		
		
		// Worm Wave
		wave = new Wave(gs, 10, 700, 20);
			wave.addEnemies(ENEMY_WORM, 1, Formation.CORNERS, 0, 0);
			wave.setPermanentModifer(new WaveModifier() {
				@Override
				public void affectBase(GameState gs, Enemy justSpawnedEnemy) {
					Enemy e = justSpawnedEnemy.copy();
					e.setX(50 + rand.nextInt(gs.getCurrentMap().getWidth() - 100));
					e.setY(50 + rand.nextInt(gs.getCurrentMap().getHeight() - 100));
					gs.addEntity(e);
				}
			});
			
		specialWaves.add(wave);
		
		
		
		
		// Combinations
		
		
		// Striver and Wanderer
		Formation i = Formation.RINGS;
			i.setCenterRingPoint(gs.getCurrentMap().getWidth() / 2, gs.getCurrentMap().getHeight() / 2);
			wave = new Wave(gs, 11, 800, 25);
				wave.addEnemies(ENEMY_WANDERER, 8, f, 10, 1, false);
			
			i.setRadius(600);
				wave.addEnemies(ENEMY_STRIVER, 12, f, 20, 1, false);
			
			i.setRadius(900);
				wave.addEnemies(ENEMY_WANDERER, 32, f, 30, 1, false);
		specialWaves.add(wave);
		
		
		
		
		// Booster and Wanderer
		wave = new Wave(gs, 12, 800, 20);
			wave.addEnemies(new EnemyType[] {ENEMY_BOOSTER, ENEMY_WANDERER}, new int[] {6, 22}, Formation.EDGES, 0, 0);
		specialWaves.add(wave);		
		
		
		
		
		// Worm and Dasher
		wave = new Wave(gs, 13, 800, 30);
			wave.addEnemies(new EnemyType[] {ENEMY_WORM, ENEMY_DASHER}, new int[] {1, 6}, Formation.RANDOM, 0, 10);
		specialWaves.add(wave);			
		
		
		
		
		/*final EnemyType[] randomEnemies = new EnemyType[]{ENEMY_BOOSTER,ENEMY_CIRCLER,ENEMY_DASHER,ENEMY_STRIVER,ENEMY_WANDERER};
		final EnemyType[] hcEnemies = new EnemyType[]{ENEMY_EXPLODER,ENEMY_TANKER};

		int random = rand.nextInt(randomEnemies.length);
		int randomHc = rand.nextInt(hcEnemies.length);
		
		// Random wave, get lucky or unlucky
		// REASON: UNBALANCABLE
		/*wave = new Wave(gs, 15, 800);
			wave.addEnemies(new EnemyType[] {randomEnemies[random], hcEnemies[randomHc]}, new int[] {8, 2}, Formation.RANDOM, 0, 10);
		specialWaves.add(wave);	*/
	
		
		
		
		// Shielder and Wanderer
		wave = new Wave(gs, 16, 800, 25);
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
		
		
		
		//Sort ascending by "minimum wave"
		Collections.sort(specialWaves, new Comparator<Wave>() {
			@Override
			public int compare(Wave w1, Wave w2) {
				return (w1.getMinimumWaveSpawn() - w2.getMinimumWaveSpawn());
			}
		});
		
		Gdx.app.log("Specular", "Loading Waves Complete");
	}
}
