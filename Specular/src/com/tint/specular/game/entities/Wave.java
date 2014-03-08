package com.tint.specular.game.entities;

import java.util.ArrayList;
import java.util.List;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.Enemy.EnemyType;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyCircler;
import com.tint.specular.game.entities.enemies.EnemyDasher;
import com.tint.specular.game.entities.enemies.EnemyShielder;
import com.tint.specular.game.entities.enemies.EnemyStriver;
import com.tint.specular.game.entities.enemies.EnemySuicider;
import com.tint.specular.game.entities.enemies.EnemyVirus;
import com.tint.specular.game.entities.enemies.EnemyWanderer;

public class Wave {

	//Permanent modifier will always be used for this wave
	private WaveModifier modifier, permanentModifier;
	private int totalDuration;
	private int duration;
	private GameState gs;
	private List<EnemySpawnFormation> enemieFormations = new ArrayList<EnemySpawnFormation>();
	private int maxRepeatTimes, repeatTimes, repeatDelay;
	private float repeatDelayChange = 1;
	private long ID;
	
	public Wave(GameState gs, long ID) {
		this.gs = gs;
		this.ID = ID;
	}

	public boolean update() {
		do {
			duration++;
			if(duration == 1) {
				start();
			}
			if(maxRepeatTimes > repeatTimes && (int) (repeatDelay * (repeatTimes + 1) * repeatDelayChange) == duration) {
				repeatTimes++;
				List<Enemy> newEnemies = new ArrayList<Enemy>();
				for(EnemySpawnFormation esf : enemieFormations) {
					for(EnemySpawn es : esf.getEnemies())
						newEnemies.add(spawnEnemy(gs, es));
				}
				if(modifier != null)
					modifier.affectRepeat(gs, newEnemies);
				if(permanentModifier != null)
					permanentModifier.affectRepeat(gs, newEnemies);
			}
			
			if(totalDuration <= duration) {
				end();
				return true;
			}
		} while(gs.getEnemies().size <= 0);
		return false;
	}
	
	private void start() {
		List<Enemy> newEnemies = new ArrayList<Enemy>();
		for(EnemySpawnFormation esf : enemieFormations) {
			for(EnemySpawn es : esf.getEnemies())
				newEnemies.add(spawnEnemy(gs, es));
		}
		if(modifier != null)
			modifier.affect(gs, newEnemies);
		if(permanentModifier != null)
			permanentModifier.affect(gs, newEnemies);
	}
	
	public void end() {
		if(modifier != null)
			modifier.removeEffect(gs);
		if(permanentModifier != null)
			permanentModifier.removeEffect(gs);
	}
	
	public void reset() {
		duration = 0;
		repeatTimes = 0;
		modifier = null;
	}

	public void setModifer(WaveModifier modifier) {
		this.modifier = modifier;
	}
	
	public Wave setPermanentModifer(WaveModifier permanentModifier) {
		this.permanentModifier = permanentModifier;
		return this;
	}
	
	public static abstract class WaveModifier {
		public abstract void affect(GameState gs, List<Enemy> justSpawnedEnemies);
		public abstract void affectRepeat(GameState gs, List<Enemy> justSpawnedEnemies);
		public abstract void removeEffect(GameState gs);
	}
	
	public class EnemySpawn {
		private EnemyType enemyType;
		private float x, y;
		public EnemySpawn(EnemyType enemyType) { this.enemyType = enemyType; }
		
		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}
			
		public void setX(float x) { this.x = x;}
		public void setY(float y) { this.y = y;}
	}
	
	public class EnemySpawnFormation {
		private List<EnemySpawn> enemies;
		private Formation formation;

		public EnemySpawnFormation(List<EnemySpawn> enemies, Formation formation) {
			this.enemies = enemies;
			this.formation = formation;
		}

		public List<EnemySpawn> getEnemies() {
			if(formation.needsRecalculation)
				formation.setFormation(enemies, gs);
			return enemies;
		}
	}
	
	public void addEnemies(EnemyType[] enemyTypes, int[] amounts, Formation formation) {
		List<EnemySpawn> tempEnemies = new ArrayList<EnemySpawn>();
		for(int i = 0; i < enemyTypes.length; i++) {
			for(int j = 0; j < amounts[i]; j++)
				tempEnemies.add(new EnemySpawn(enemyTypes[i]));
		}
		formation.setFormation(tempEnemies, gs);
		enemieFormations.add(new EnemySpawnFormation(tempEnemies, formation));
	}
	
	public void addEnemies(EnemyType enemyType, int amount, Formation formation) {
		List<EnemySpawn> tempEnemies = new ArrayList<EnemySpawn>();
		for(int j = 0; j < amount; j++)
			tempEnemies.add(new EnemySpawn(enemyType));
		formation.setFormation(tempEnemies, gs);
		enemieFormations.add(new EnemySpawnFormation(tempEnemies, formation));
	}

	public Wave setTotalDuration(int seconds) {
		this.totalDuration = seconds * 60;
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
	
	private static Enemy spawnEnemy(GameState gs, EnemySpawn es) {
		Enemy e = null;
		switch(es.enemyType) {
		case ENEMY_WANDERER:
			e = new EnemyWanderer(es.getX(), es.getY(), gs);
			break;
		case ENEMY_STRIVER:
			e = new EnemyStriver(es.getX(), es.getY(), gs);
			break;
		case ENEMY_BOOSTER:
			e = new EnemyBooster(es.getX(), es.getY(), gs);
			break;
		case ENEMY_DASHER:
			e = new EnemyDasher(es.getX(), es.getY(), gs);
			break;
		case ENEMY_CIRCLER:
			e = new EnemyCircler(es.getX(), es.getY(), gs);
			break;
		case ENEMY_SHIELDER:
			e = new EnemyShielder(es.getX(), es.getY(), gs);
			break;
		case ENEMY_VIRUS:
			e = new EnemyVirus(es.getX(), es.getY(), gs, false);
			break;
		case ENEMY_SUICIDER:
			e = new EnemySuicider(es.getX(), es.getY(), gs);
			break;
		default:
			break;
		}
		
		gs.addEntity(e);
		return e;
	}

	public long getID() {
		return ID;
	}
}
