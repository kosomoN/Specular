package com.tint.specular.game.entities;

import static com.tint.specular.game.entities.enemies.Enemy.EnemyType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Wave.WaveModifier;
import com.tint.specular.game.entities.enemies.Enemy.EnemyType;

public class WaveManager {

	private Wave wave1, wave2, wave3;
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
		wave1 = new Wave(gs).setTotalDuration(20).setRepeatTimes(2).setRepeatDelay(5);
		wave1.addEnemies(ENEMY_WANDERER, 10, Formation.EDGES);
		
		wave2 = new Wave(gs).setTotalDuration(20).setRepeatTimes(2).setRepeatDelay(5);
		wave2.addEnemies(ENEMY_NORMAL, 5, Formation.RANDOM);
		wave2.addEnemies(ENEMY_FAST, 5, Formation.EDGES);
		
		wave3 = new Wave(gs).setTotalDuration(20).setRepeatTimes(2).setRepeatDelay(5);
		wave3.addEnemies(new EnemyType[] {ENEMY_FAST, ENEMY_WANDERER, ENEMY_BOOSTER}, new int[] {3, 5, 2}, Formation.EDGES);
		
		Wave wave = new Wave(gs).setTotalDuration(20);
		wave.addEnemies(new EnemyType[] {ENEMY_SHIELDER, ENEMY_WANDERER}, new int[] {1, 20}, Formation.SURROUND_ENEMY);
		waves.add(wave);
		Gdx.app.log("Specular", "Loading Waves Complete");
	}
}
