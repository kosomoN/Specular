package com.tint.specular.game.entities;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy.EnemyType;


public class WaveLoader {

	public static void initWaves(GameState gs, List<Wave> specialWaves) {
		Gdx.app.log("Specular", "Loading Waves");
		Wave wave = new Wave(gs, 5, 800);
		wave.addEnemies(EnemyType.ENEMY_EXPLODER, 10, Formation.RANDOM, 0, 20);
		specialWaves.add(wave);
		Gdx.app.log("Specular", "Loading Waves Complete");
	}
}
