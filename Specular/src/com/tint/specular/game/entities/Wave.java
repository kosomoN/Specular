package com.tint.specular.game.entities;

import java.util.ArrayList;
import java.util.List;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy.EnemyType;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyDasher;
import com.tint.specular.game.entities.enemies.EnemyStriver;
import com.tint.specular.game.entities.enemies.EnemyCircler;
import com.tint.specular.game.entities.enemies.EnemyShielder;
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
				for(EnemySpawnFormation esf : enemieFormations) {
					for(EnemySpawn es : esf.getEnemies())
						spawnEnemy(gs, es);
				}
				if(modifier != null)
					modifier.affectRepeat(gs);
				if(permanentModifier != null)
					permanentModifier.affectRepeat(gs);
			}
			
			if(totalDuration <= duration) {
				end();
				return true;
			}
		} while(gs.getEnemies().size <= 0);
		return false;
	}
	
	private void start() {
		for(EnemySpawnFormation esf : enemieFormations) {
			for(EnemySpawn es : esf.getEnemies())
				spawnEnemy(gs, es);
		}
		if(modifier != null)
			modifier.affect(gs);
		if(permanentModifier != null)
			permanentModifier.affect(gs);
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
		public abstract void affect(GameState gs);
		public abstract void affectRepeat(GameState gs);
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
	
	private static void spawnEnemy(GameState gs, EnemySpawn es) {
		switch(es.enemyType) {
		case ENEMY_WANDERER:
			gs.addEntity(new EnemyWanderer(es.getX(), es.getY(), gs));
			break;
		case ENEMY_STRIVER:
			gs.addEntity(new EnemyStriver(es.getX(), es.getY(), gs));
			break;
		case ENEMY_BOOSTER:
			gs.addEntity(new EnemyBooster(es.getX(), es.getY(), gs));
			break;
		case ENEMY_DASHER:
			gs.addEntity(new EnemyDasher(es.getX(), es.getY(), gs));
			break;
		case ENEMY_CIRCLER:
			gs.addEntity(new EnemyCircler(es.getX(), es.getY(), gs));
			break;
		case ENEMY_SHIELDER:
			gs.addEntity(new EnemyShielder(es.getX(), es.getY(), gs));
			break;
		case ENEMY_VIRUS:
			gs.addEntity(new EnemyVirus(es.getX(), es.getY(), gs));
			break;
		}
	}

	public long getID() {
		return ID;
	}
}
