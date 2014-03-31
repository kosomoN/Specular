package com.tint.specular.game.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.WaveManager.WaveModifier;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.Enemy.EnemyType;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyCircler;
import com.tint.specular.game.entities.enemies.EnemyDasher;
import com.tint.specular.game.entities.enemies.EnemyExploder;
import com.tint.specular.game.entities.enemies.EnemyShielder;
import com.tint.specular.game.entities.enemies.EnemyStriver;
import com.tint.specular.game.entities.enemies.EnemyTanker;
import com.tint.specular.game.entities.enemies.EnemyVirus;
import com.tint.specular.game.entities.enemies.EnemyWanderer;

public class Wave {
	private static final Random rand = new Random();
	
	private long ID;
	private GameState gs;
	private WaveModifier modifier, permanentModifier;
	private int timer, totalLength;
	//This list is used to recalculate the formation
	private List<EnemySpawnFormation> formationList = new ArrayList<EnemySpawnFormation>();
	
	//A sorted list with ascending "spawntime" (time when they should spawn)
	private List<EnemySpawn> specialEnemies = new ArrayList<EnemySpawn>(), baseEnemies = new ArrayList<EnemySpawn>();
	//At which point in the list we are at, e.g. which enemies has already spawned
	private int specialListIndexSpawned, baseListIndexSpawned;

	public Wave(GameState gs, long ID, int totalLengthTicks) {
		this.gs = gs;
		this.ID = ID;
		this.totalLength = totalLengthTicks;
	}
	
	public long getID() {
		return ID;
	}

	public boolean update() {
		do {
			timer++;
			
			if(timer == 1) {
				start();
			}
			
			//Check if the enemy spawned
			if(specialEnemies.size() > specialListIndexSpawned && specialEnemies.get(specialListIndexSpawned).spawn(timer, true)) {
				//Loop until next enemy which has not spawned is found, this is required to be able to spawn many enemies in one tick
				do {
					specialListIndexSpawned++;
					if(specialEnemies.size() <= specialListIndexSpawned)
						break;
				} while(specialEnemies.get(specialListIndexSpawned).spawn(timer, true));
			}
			
			//Check if the enemy spawned
			if(baseEnemies.size() > baseListIndexSpawned && baseEnemies.get(baseListIndexSpawned).spawn(timer, false)) {
				//Loop until next enemy which has not spawned is found, this is required to be able to spawn many enemies in one tick
				do {
					baseListIndexSpawned++;
					if(baseEnemies.size() <= baseListIndexSpawned)
						break;
				} while(baseEnemies.get(baseListIndexSpawned).spawn(timer, false));
			}
			
			//The wave is over when the timer has ran out
			if(timer >= totalLength) {
				end();
				return true;
			}
		} while(gs.getEnemies().size <= 0);
		
		
		return false;
	}

	private void end() {
		if(permanentModifier != null)
			permanentModifier.end(gs);
		if(modifier != null)
			modifier.end(gs);
	}

	private void start() {
		if(permanentModifier != null)
			permanentModifier.start(gs);
		if(modifier != null)
			modifier.start(gs);
	}

	public void reset(int waveNumber) {
		specialListIndexSpawned = 0;
		baseListIndexSpawned = 0;
		timer = 0;
		
		for(EnemySpawnFormation esf : formationList)
			esf.reset();
		
		//Initialize basewave
		baseEnemies = new ArrayList<EnemySpawn>();
		EnemyType et = null;
		for(int i = 0; i < 10 + waveNumber; i++) {
			int random = rand.nextInt(3);
			if(random < 1)
				et = EnemyType.ENEMY_CIRCLER;
			else if(random < 2)
				et = EnemyType.ENEMY_STRIVER;
			else if(random < 3)
				et = EnemyType.ENEMY_WANDERER;
			
			baseEnemies.add(new EnemySpawn(et, 3));
		}
		
		Formation.RANDOM.setFormation(baseEnemies, gs);
	}
	/**
	 * @param enemyTypes
	 * @param amounts
	 * @param formation
	 * @param spawnTime The tick they will spawn at, 0 for the beginning of the wave
	 * @param spawndelay The delay that will added between enemy spawns
	 */
	public void addEnemies(EnemyType[] enemyTypes, int[] amounts, Formation formation, int spawnTime, float spawndelayTicks) {
		List<EnemySpawn> tempEnemies = new ArrayList<EnemySpawn>();
		for(int i = 0; i < enemyTypes.length; i++) {
			for(int j = 0; j < amounts[i]; j++)
				tempEnemies.add(new EnemySpawn(enemyTypes[i], spawnTime));
		}

		formation.setFormation(tempEnemies, gs);
		formationList.add(new EnemySpawnFormation(tempEnemies, formation));
		
		Collections.shuffle(tempEnemies);
		
		for(int i = 0; i < tempEnemies.size(); i++) {
			tempEnemies.get(i).spawnTime += spawndelayTicks * i;
		}
		
		specialEnemies.addAll(tempEnemies);
	}

	public void addEnemies(EnemyType enemyType, int amount, Formation formation, int spawnTime, float spawndelayTicks) {
		List<EnemySpawn> tempEnemies = new ArrayList<EnemySpawn>();
		for(int j = 0; j < amount; j++)
			tempEnemies.add(new EnemySpawn(enemyType, spawnTime));
		
		Collections.shuffle(tempEnemies);
		
		for(int i = 0; i < tempEnemies.size(); i++) {
			tempEnemies.get(i).spawnTime += spawndelayTicks * i;
		}
		
		formation.setFormation(tempEnemies, gs);
		formationList.add(new EnemySpawnFormation(tempEnemies, formation));
		
		specialEnemies.addAll(tempEnemies);
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
		case ENEMY_TANKER:
			e = new EnemyTanker(es.getX(), es.getY(), gs);
			break;
		case ENEMY_EXPLODER:
			e = new EnemyExploder(es.getX(), es.getY(), gs);
			break;
		}
		
		gs.addEntity(e);
		return e;
	}
	
	public class EnemySpawn implements Comparable<EnemySpawn> {
		private EnemyType enemyType;
		private float x, y;
		private int spawnTime;
		
		public EnemySpawn(EnemyType enemyType, int spawnTime) {
			this.enemyType = enemyType;
			this.spawnTime = spawnTime;
		}

		public float getX() {
			return x;
		}
		
		public boolean spawn(int timer, boolean special) {
			if(spawnTime <= timer) {
				Enemy e = spawnEnemy(gs, this);
				if(permanentModifier != null) {
					if(special) 
						permanentModifier.affectSpecial(gs, e);
					else
						permanentModifier.affectBase(gs, e);
				}
				
				if(modifier != null) {
					if(special) 
						modifier.affectSpecial(gs, e);
					else
						modifier.affectBase(gs, e);
				}
				return true;
			}
			return false;
		}

		public void setX(float x) {
			this.x = x;
		}
		
		public float getY() {
			return y;
		}
		
		public void setY(float y) {
			this.y = y;
		}

		@Override
		public int compareTo(EnemySpawn es) {
			return this.spawnTime - es.spawnTime;
		}
	}
	
	public class EnemySpawnFormation {
		private List<EnemySpawn> enemies;
		private Formation formation;

		public EnemySpawnFormation(List<EnemySpawn> enemies, Formation formation) {
			this.enemies = new ArrayList<Wave.EnemySpawn>(enemies);
			this.formation = formation;
		}

		public void reset() {
			formation.setFormation(enemies, gs);
		}
	}

	public void setModifer(WaveModifier modifier) {
		this.modifier = modifier;
	}
	
	public void setPermanentModifer(WaveModifier permanentModifier) {
		this.permanentModifier = permanentModifier;
	}
}
