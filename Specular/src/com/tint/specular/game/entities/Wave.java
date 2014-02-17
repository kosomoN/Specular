package com.tint.specular.game.entities;

import java.util.ArrayList;
import java.util.List;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy.EnemyType;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyDasher;
import com.tint.specular.game.entities.enemies.EnemyFast;
import com.tint.specular.game.entities.enemies.EnemyNormal;
import com.tint.specular.game.entities.enemies.EnemyShielder;
import com.tint.specular.game.entities.enemies.EnemyVirus;
import com.tint.specular.game.entities.enemies.EnemyWanderer;

public class Wave {

	private WaveModifier modifier;
	private int totalDuration;
	private int duration;
	private GameState gs;
	private List<EnemySpawn> enemies = new ArrayList<EnemySpawn>();
	private int maxRepeatTimes, repeatTimes, repeatDelay;
	private float repeatDelayChange = 1;
	
	public Wave(GameState gs) {
		this.gs = gs;
	}

	public boolean update() {
		duration++;
		
		if(maxRepeatTimes > repeatTimes && (int) (repeatDelay * (repeatTimes + 1) * repeatDelayChange) == duration) {
			repeatTimes++;
			for(EnemySpawn es : enemies) {
				spawnEnemy(gs, es);
			}
		}
		
		if(totalDuration == duration) {
			end();
			return true;
		}
		return false;
	}
	
	public void start() {
		if(modifier != null)
			modifier.affect(gs);
		for(EnemySpawn es : enemies) {
			spawnEnemy(gs, es);
		}
	}
	
	public void end() {
		if(modifier != null)
			modifier.affect(gs);
	}
	
	public void reset() {
		duration = 0;
		repeatTimes = 0;
		modifier = null;
	}

	public void setModifer(WaveModifier modifier) {
		this.modifier = modifier;
	}
	
	public abstract class WaveModifier {
		public abstract void affect(GameState gs);
		public abstract void removeEffect(GameState gs);
	}
	
	public class EnemySpawn {
		private EnemyType enemyType;
		public float x, y;
		public EnemySpawn(EnemyType enemyType) { this.enemyType = enemyType; }
	}
	
	public void addEnemies(EnemyType[] enemyTypes, int[] amounts, Formation formation) {
		List<EnemySpawn> tempEnemies = new ArrayList<Wave.EnemySpawn>();
		for(int i = 0; i < enemyTypes.length; i++) {
			for(int j = 0; j < amounts[i]; j++)
				tempEnemies.add(new EnemySpawn(enemyTypes[i]));
		}
		formation.setFormation(tempEnemies, gs);
		enemies.addAll(tempEnemies);
	}
	
	public void addEnemies(EnemyType enemyType, int amount, Formation formation) {
		List<EnemySpawn> tempEnemies = new ArrayList<Wave.EnemySpawn>();
		for(int j = 0; j < amount; j++)
			tempEnemies.add(new EnemySpawn(enemyType));
		formation.setFormation(tempEnemies, gs);
		enemies.addAll(tempEnemies);
	}

	public Wave setTotalDuration(int seconds) {
		this.totalDuration = seconds * 60;
		return this;
	}

	public Wave setEnemies(List<EnemySpawn> enemies) {
		this.enemies = enemies;
		return this;
	}

	public Wave setRepeatTimes(int x) {
		this.maxRepeatTimes = x;
		return this;
	}

	public Wave setRepeatDelay(int seconds) {
		this.repeatDelay = seconds * 60;
		return this;
	}
	
	public Wave setRepeatDelayChange(float change) {
		this.repeatDelayChange = change;
		return this;
	}
	
	private static void spawnEnemy(GameState gs, EnemySpawn es) {
		switch(es.enemyType) {
		case ENEMY_WANDERER:
			gs.addEntity(new EnemyWanderer(es.x, es.y, gs));
			break;
		case ENEMY_FAST:
			gs.addEntity(new EnemyFast(es.x, es.y, gs));
			break;
		case ENEMY_BOOSTER:
			gs.addEntity(new EnemyBooster(es.x, es.y, gs));
			break;
		case ENEMY_DASHER:
			gs.addEntity(new EnemyDasher(es.x, es.y, gs));
			break;
		case ENEMY_NORMAL:
			gs.addEntity(new EnemyNormal(es.x, es.y, gs));
			break;
		case ENEMY_SHIELDER:
			gs.addEntity(new EnemyShielder(es.x, es.y, gs));
			break;
		case ENEMY_VIRUS:
			gs.addEntity(new EnemyVirus(es.x, es.y, gs));
			break;
		}
	}
}
