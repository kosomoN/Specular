package com.tint.specular.game.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;

public class WaveManager {

	private List<Wave> specialWaves;
	private Random rand = new Random();
	private int lastValidWaveIndex = 0;
	
	public WaveManager(GameState gs) {
		specialWaves = new ArrayList<Wave>();
		WaveLoader.initWaves(gs, specialWaves);
	}

	public Wave getWave(int waveNumber) {
		
		
		if(lastValidWaveIndex + 1 < specialWaves.size()) {
			
			//Check if a new wave is available for this waveNumber and then find the next valid one
			while(specialWaves.get(lastValidWaveIndex + 1).getMinimumWaveSpawn() <= waveNumber) {
				System.out.println(waveNumber + " " + lastValidWaveIndex);
				lastValidWaveIndex++;
				
				//We have reached the end DUN DUN DUUUUUUN
				if(lastValidWaveIndex + 1 >= specialWaves.size())
					break;
			}
		}
		
		Wave wave = specialWaves.get(rand.nextInt(lastValidWaveIndex + 1));
		wave.reset(waveNumber);
		wave.setModifer(getModifier(waveNumber));
		return wave;
	}
	
	private WaveModifier getModifier(int waveNumber) {
		return null;
	}
	
	public static abstract class WaveModifier {
		public void affectSpecial(GameState gs, Enemy justSpawnedEnemy) {}
		public void affectBase(GameState gs, Enemy justSpawnedEnemy) {}
		public void end(GameState gs) {}
		public void start(GameState gs) {}
	}
}
