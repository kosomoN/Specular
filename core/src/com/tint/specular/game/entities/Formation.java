package com.tint.specular.game.entities;

import java.util.List;
import java.util.Random;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Wave.EnemySpawn;

public enum Formation {
	
	EDGES(false) {
		@Override
		public void setFormation(List<EnemySpawn> enemies, GameState gs) {
			int side;
			int top = enemies.size() / 4, bottom = enemies.size() / 4, left = enemies.size() / 4, right = enemies.size() / 4;
			
			switch(enemies.size() % 4) {//If the enemies are not evenly distributed
			case 3:
				bottom++;
			case 2:
				right++;
			case 1:
				top++;
				break;
			}
			
			for(int i = 0; i < enemies.size(); i++) {
				side = i % 4;
				EnemySpawn es = enemies.get(i);
				//Top
				if(side == 0) {
					es.setX(gs.getCurrentMap().getWidth() / (top + 1) * (i / 4 + 1));
					es.setY(gs.getCurrentMap().getHeight() - 100);
				} else if(side == 2) {//Bottom
					es.setX(gs.getCurrentMap().getWidth() / (bottom + 1) * (i / 4 + 1));
					es.setY(100);
				} else if(side == 1){//Right
					es.setX(gs.getCurrentMap().getWidth() - 100);
					es.setY(gs.getCurrentMap().getHeight() / (right + 1) * (i / 4 + 1));
				} else {//Left
					es.setX(100);
					es.setY(gs.getCurrentMap().getHeight() / (left + 1) * (i / 4 + 1));
				}
			}
		}
	}, RANDOM(true) {
		Random rand = new Random();
		@Override
		public void setFormation(List<EnemySpawn> enemies, GameState gs) {
			for(EnemySpawn es : enemies) {
				es.setX(rand.nextInt(gs.getCurrentMap().getWidth() - 200) + 100);
				es.setY(rand.nextInt(gs.getCurrentMap().getHeight() - 200) + 100);
			}
				
		}
	}, 
	/**
	 * Surrounds the first enemy in the list at a random position, others around it
	 */
	SURROUND_ENEMY(true) {
		Random rand = new Random();
		@Override
		public void setFormation(List<EnemySpawn> enemies, GameState gs) {
			float x = rand.nextInt(gs.getCurrentMap().getWidth() - 200) + 100;
			float y = rand.nextInt(gs.getCurrentMap().getHeight() - 200) + 100;
			enemies.get(0).setX(x);
			enemies.get(0).setY(y);
			
			EnemySpawn es;
			for(int i = 1; i < enemies.size(); i++) {
				double angle = i / (enemies.size() - 1d) * Math.PI * 2;
				es = enemies.get(i);
				es.setX((float) (x + Math.cos(angle) * 100));
				es.setY((float) (y + Math.sin(angle) * 100));
			}
		}
	},
	
	RINGS(true) {
		@Override
		public void setFormation(List<EnemySpawn> enemies, GameState gs) {
			float x = gs.getCurrentMap().getWidth() / 2;
			float y = gs.getCurrentMap().getHeight() / 2;
			EnemySpawn es;
			for(int i = 0; i < enemies.size(); i++) {
				double angle = i / (double) enemies.size() * Math.PI * 2;
				es = enemies.get(i);
				es.setX((float) (x + Math.cos(angle) * radius));
				es.setY((float) (y + Math.sin(angle) * radius));
			}
		}
	};
	
	public final boolean needsRecalculation;
	public float radius = 100;
	public abstract void setFormation(List<EnemySpawn> enemies, GameState gs);
	public void setRadius(float radius) { this.radius = radius; }
	
	private Formation(boolean needsRecalculation) {
		this.needsRecalculation = needsRecalculation;
	}
}
