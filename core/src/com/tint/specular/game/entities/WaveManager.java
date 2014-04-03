package com.tint.specular.game.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;

public class WaveManager {

	private List<Wave> specialWaves = new ArrayList<Wave>();
	private Random rand = new Random();
	
	public WaveManager(GameState gs) {
		WaveLoader.initWaves(gs, specialWaves);
	}

	public Wave getWave(int waveNumber) {
		Wave wave = specialWaves.get(rand.nextInt(specialWaves.size()));
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
