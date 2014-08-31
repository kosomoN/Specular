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
		
		// Wanderer Wave
		Formation f = Formation.RINGS;
			f.setCenterRingPoint(gs.getCurrentMap().getWidth() / 2, gs.getCurrentMap().getHeight() / 2);
			wave = new Wave(gs, 4, 800);
				wave.addEnemies(ENEMY_WANDERER, 8, f, 10, 1, false);
		
			f.setRadius(400);
				wave.addEnemies(ENEMY_WANDERER, 16, f, 20, 1, false);
		
			f.setRadius(700);
				wave.addEnemies(ENEMY_WANDERER, 32, f, 30, 1, false);
		specialWaves.add(wave);
		
		// Tanker Wave
		Formation g = Formation.RINGS;
			g.setCenterRingPoint(gs.getCurrentMap().getWidth() / 2, gs.getCurrentMap().getHeight() / 2);
			wave = new Wave(gs, 5, 800);
				wave.addEnemies(ENEMY_TANKER, 8, f, 10, 1, false);
		
		// Circler Wave
		Formation h = Formation.RINGS;
			h.setCenterRingPoint(gs.getCurrentMap().getWidth() / 2, gs.getCurrentMap().getHeight() / 2);
			wave = new Wave(gs, 7, 800);
				wave.addEnemies(ENEMY_CIRCLER, 8, f, 10, 1, false);
			
			h.setRadius(400);
				wave.addEnemies(ENEMY_CIRCLER, 16, f, 20, 1, false);
			
			h.setRadius(700);
				wave.addEnemies(ENEMY_CIRCLER, 32, f, 30, 1, false);
		specialWaves.add(wave);
			
		// Striver Wave
		wave = new Wave(gs, 8, 600);
			wave.addEnemies(ENEMY_STRIVER, 20, Formation.EDGES, 0, 0);
		specialWaves.add(wave);
		
		// Virus wave
		wave = new Wave(gs, 9, 800);
		wave.addEnemies(new EnemyType[] {ENEMY_VIRUS}, new int[] {7}, Formation.RANDOM, 0, 10);
		specialWaves.add(wave);	
		
		// Worm Wave
		wave = new Wave(gs, 10, 700);
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
		final EnemyType[] randomEnemies = new EnemyType[]{ENEMY_BOOSTER,ENEMY_CIRCLER,ENEMY_DASHER,ENEMY_STRIVER,ENEMY_WANDERER};
		final EnemyType[] hcEnemies = new EnemyType[]{ENEMY_EXPLODER,ENEMY_TANKER};

		int random, randomHc;
		random = rand.nextInt(randomEnemies.length);
		randomHc = rand.nextInt(hcEnemies.length);
		
		// Striver and Wanderer
		Formation i = Formation.RINGS;
			i.setCenterRingPoint(gs.getCurrentMap().getWidth() / 2, gs.getCurrentMap().getHeight() / 2);
			wave = new Wave(gs, 11, 800);
				wave.addEnemies(ENEMY_WANDERER, 8, f, 10, 1, false);
			
			i.setRadius(600);
				wave.addEnemies(ENEMY_STRIVER, 12, f, 20, 1, false);
			
			i.setRadius(900);
				wave.addEnemies(ENEMY_WANDERER, 32, f, 30, 1, false);
		specialWaves.add(wave);
		
		// Booster and Wanderer
		wave = new Wave(gs, 12, 800);
			wave.addEnemies(new EnemyType[] {ENEMY_BOOSTER, ENEMY_WANDERER}, new int[] {6, 22}, Formation.EDGES, 0, 0);
		specialWaves.add(wave);		
		
		// Worm and Dasher
		wave = new Wave(gs, 13, 800);
			wave.addEnemies(new EnemyType[] {ENEMY_WORM, ENEMY_DASHER}, new int[] {1, 6}, Formation.RANDOM, 0, 10);
		specialWaves.add(wave);			
		
		// Tanker and Dasher	
			wave = new Wave(gs, 14, 800);
			wave.addEnemies(new EnemyType[] {ENEMY_TANKER, ENEMY_DASHER}, new int[] {6, 8}, Formation.EDGES, 0, 10);
		specialWaves.add(wave);	
		
		// Random wave, get lucky or unlucky
		wave = new Wave(gs, 15, 800);
		wave.addEnemies(new EnemyType[] {randomEnemies[random], hcEnemies[randomHc]}, new int[] {8, 2}, Formation.RANDOM, 0, 10);
		specialWaves.add(wave);	
	
		// Shielder and Wanderer
		wave = new Wave(gs, 16, 800);
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
