package com.tint.specular.game.entities;

import java.util.List;
import java.util.Random;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Wave.EnemySpawn;

public enum Formation {
	
	EDGES {
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
					es.x = gs.getCurrentMap().getWidth() / (top + 1) * (i / 4 + 1);
					es.y = gs.getCurrentMap().getHeight() - 100;
				} else if(side == 2) {//Bottom
					es.x = gs.getCurrentMap().getWidth() / (bottom + 1) * (i / 4 + 1);
					es.y = 100;
				} else if(side == 1){//Right
					es.x = gs.getCurrentMap().getWidth() - 100;
					es.y = gs.getCurrentMap().getHeight() / (right + 1) * (i / 4 + 1);
				} else {//Left
					es.x = 100;
					es.y = gs.getCurrentMap().getHeight() / (left + 1) * (i / 4 + 1);
				}
			}
		}
	}, RANDOM {
		Random rand = new Random();
		@Override
		public void setFormation(List<EnemySpawn> enemies, GameState gs) {
			for(EnemySpawn es : enemies) {
				es.x = rand.nextInt(gs.getCurrentMap().getWidth());
				es.y = rand.nextInt(gs.getCurrentMap().getHeight());
			}
				
		}
	}, 
	/**
	 * Surrounds the first enemy int the list at a random position, other around it
	 */
	SURROUND_ENEMY {
		Random rand = new Random();
		@Override
		public void setFormation(List<EnemySpawn> enemies, GameState gs) {
			float x = rand.nextInt(gs.getCurrentMap().getWidth());
			float y = rand.nextInt(gs.getCurrentMap().getHeight());
			enemies.get(0).x = x;
			enemies.get(0).y = y;
			
			EnemySpawn es;
			for(int i = 1; i < enemies.size(); i++) {
				double angle = i / (enemies.size() - 1d) * Math.PI * 2;
				es = enemies.get(i);
				es.x = (float) (x + Math.cos(angle) * rand.nextInt(50) + 25);
				es.y = (float) (y + Math.sin(angle) * rand.nextInt(50) + 25);
			}
		}
	};
	
	public abstract void setFormation(List<EnemySpawn> enemies, GameState gs);
}
