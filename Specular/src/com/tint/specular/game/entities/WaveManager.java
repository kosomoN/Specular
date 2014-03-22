package com.tint.specular.game.entities;

import static com.tint.specular.game.entities.enemies.Enemy.EnemyType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Wave.WaveModifier;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyWanderer;
import com.tint.specular.game.entities.enemies.Enemy.EnemyType;

public class WaveManager {

	private Wave wave1, wave2, wave3, virusWave;
	private List<Wave> waves = new ArrayList<Wave>();
	private Random rand = new Random();
	
	public WaveManager(GameState gs) {
		initWaves(gs);
	}

	public Wave getWave(int waveNumber) {
		if(waveNumber == 0) {
			wave1.reset();
			return wave1;
		} else if(waveNumber == 1) {
			wave2.reset();
			return wave2;
		} else if(waveNumber == 2) {
			wave3.reset();
			return wave3;
		} else if(waveNumber == 7) {
			virusWave.reset();
			return virusWave;
		} else {
			Wave wave = waves.get(rand.nextInt(waves.size()));
			wave.reset();
			if(waveNumber > 10)
				wave.setModifer(getModifier(waveNumber));
			return wave;
		}
	}
	
	private WaveModifier getModifier(int waveNumber) {
		return null;
	}
	
	public void initWaves(GameState gs) {
		Gdx.app.log("Specular", "Loading Waves");
		wave1 = new Wave(gs, 0).setTotalDuration(15);
		wave1.addEnemies(ENEMY_WANDERER, 16, Formation.EDGES);
		
		wave2 = new Wave(gs, 1).setTotalDuration(15);
		wave2.addEnemies(ENEMY_CIRCLER, 8, Formation.RANDOM);
		wave2.addEnemies(ENEMY_STRIVER, 8, Formation.EDGES);
		
		wave3 = new Wave(gs, 2).setTotalDuration(15);
		wave3.addEnemies(new EnemyType[] {ENEMY_STRIVER, ENEMY_WANDERER, ENEMY_BOOSTER}, new int[] {4, 10, 2}, Formation.EDGES);
		
		Wave wave = new Wave(gs, 3).setTotalDuration(10);
		wave.addEnemies(ENEMY_EXPLODER, 6, Formation.EDGES);
		wave.addEnemies(ENEMY_CIRCLER, 20, Formation.RANDOM);
		waves.add(wave);
		
		wave = new Wave(gs, 4).setTotalDuration(10);
		wave.addEnemies(new EnemyType[] {ENEMY_WANDERER, ENEMY_CIRCLER, ENEMY_STRIVER, ENEMY_BOOSTER}, new int[] {6, 20, 10, 6}, Formation.RANDOM);
		waves.add(wave);
		
		wave = new Wave(gs, 5).setTotalDuration(10);
		wave.addEnemies(new EnemyType[] {ENEMY_WANDERER, ENEMY_CIRCLER, ENEMY_STRIVER, ENEMY_BOOSTER}, new int[] {6, 10, 10, 6}, Formation.RANDOM);
		waves.add(wave);
		
		wave = new Wave(gs, 5).setTotalDuration(10);
		wave.addEnemies(new EnemyType[] {ENEMY_WANDERER, ENEMY_CIRCLER, ENEMY_BOOSTER, ENEMY_DASHER}, new int[] {6, 10, 6, 8}, Formation.RANDOM);
		waves.add(wave);
		
		wave = new Wave(gs, 6).setTotalDuration(5).setPermanentModifer(new WaveModifier() {
			
			@Override
			public void removeEffect(GameState gs) {}
			
			@Override
			public void affectRepeat(GameState gs, List<Enemy> justSpawnedEnemies) {
				for(Enemy e : justSpawnedEnemies) {
					if(e instanceof EnemyWanderer)
						e.addLife(10);
				}
			}
			
			@Override
			public void affect(GameState gs, List<Enemy> justSpawnedEnemies) {
				for(Enemy e : justSpawnedEnemies) {
					if(e instanceof EnemyWanderer)
						e.addLife(10);
				}
			}
		});;
		wave.addEnemies(new EnemyType[] {ENEMY_SHIELDER, ENEMY_WANDERER}, new int[] {1, 20}, Formation.SURROUND_ENEMY);
		wave.addEnemies(ENEMY_DASHER, 3, Formation.EDGES);
		waves.add(wave);
		
		wave = new Wave(gs, 7).setTotalDuration(20).setRepeatDelay(7).setRepeatTimes(1);
		wave.addEnemies(new EnemyType[] {ENEMY_WANDERER, ENEMY_CIRCLER, ENEMY_STRIVER, ENEMY_BOOSTER}, new int[] {2, 10, 5, 3}, Formation.RANDOM);
		
		waves.add(wave);
		
		wave = new Wave(gs, 8).setRepeatTimes(1).setRepeatDelay(5).setTotalDuration(20).setPermanentModifer(new WaveModifier() {
			
			@Override
			public void removeEffect(GameState gs) {}
			
			@Override
			public void affectRepeat(GameState gs, List<Enemy> justSpawnedEnemies) {
				for(Enemy e : justSpawnedEnemies) {
					e.addLife(-e.getLife() + 1);
				}
			}
			
			@Override
			public void affect(GameState gs, List<Enemy> justSpawnedEnemies) {
				for(Enemy e : justSpawnedEnemies) {
					e.addLife(-e.getLife() + 1);
				}
			}
		});
		wave.addEnemies(ENEMY_STRIVER, 50, Formation.EDGES);
	
		waves.add(wave);
		
		virusWave = new Wave(gs, 1000).setTotalDuration(0);
		virusWave.addEnemies(ENEMY_VIRUS, 9, Formation.RANDOM);
		
		Gdx.app.log("Specular", "Loading Waves Complete");
	}
}
