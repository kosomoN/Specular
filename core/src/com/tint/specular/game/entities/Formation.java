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
	
	RINGS(false) {
		@Override
		public void setFormation(List<EnemySpawn> enemies, GameState gs) {
			EnemySpawn es;
			for(int i = 0; i < enemies.size(); i++) {
				double angle = i / (double) enemies.size() * Math.PI * 2;
				es = enemies.get(i);
				es.setX((float) (x + Math.cos(angle) * radius));
				es.setY((float) (y + Math.sin(angle) * radius));
			}
		}
	},
	
	SIDE(false) {
		@Override
		public void setFormation(List<EnemySpawn> enemies, GameState gs) {
			for(int i = 0; i < enemies.size(); i++) {
				EnemySpawn es = enemies.get(i);
				
				if(side == Sides.LEFT) {
					es.setX(100);
					es.setY(gs.getCurrentMap().getHeight() / (enemies.size() + 1) * (i + 1));
				} else if(side == Sides.RIGHT) {
					es.setX(gs.getCurrentMap().getWidth() - 100);
					es.setY(gs.getCurrentMap().getHeight() / (enemies.size() + 1) * (i + 1));
				} else if(side == Sides.UP) {
					es.setX(gs.getCurrentMap().getWidth() / (enemies.size() + 1) * (i + 1));
					es.setY(100);
				} else if(side == Sides.DOWN) {
					es.setX(gs.getCurrentMap().getWidth() / (enemies.size() + 1) * (i + 1));
					es.setY(gs.getCurrentMap().getHeight() - 100);
				}
			}
		}
	},
	
	CORNERS(false) {
		private static final int UP_LEFT = 0, UP_RIGHT = 1, LOW_RIGHT = 2, LOW_LEFT = 3;
		@Override
		public void setFormation(List<EnemySpawn> enemies, GameState gs) {
			float corner = 0;
			for(EnemySpawn es : enemies) {
				if(corner == UP_LEFT) {	// Upper-left corner
					es.setX(100);
					es.setY(100);
				} else if(corner == UP_RIGHT) {	// Upper-right corner
					es.setX(gs.getCurrentMap().getWidth() - 100);
					es.setY(100);
				} else if(corner == LOW_RIGHT) {	// Lower-right corner
					es.setX(gs.getCurrentMap().getWidth() - 100);
					es.setY(gs.getCurrentMap().getHeight() - 100);
				} else if(corner == LOW_LEFT) {	// Lower-left corner
					es.setX(100);
					es.setY(gs.getCurrentMap().getHeight() - 100);
				}
				
				corner++;
				corner = corner % 4;
			}
		}
	}, 
	
	CENTER(false) {
		@Override
		public void setFormation(List<EnemySpawn> enemies, GameState gs) {
			for(EnemySpawn es: enemies) {
				es.setX(gs.getCurrentMap().getWidth() / 2);
				es.setY(gs.getCurrentMap().getHeight() / 2);
			}
		}
	};
	
	public enum Sides {
		LEFT, RIGHT, UP, DOWN;
	};
	public final boolean needsRecalculation;
	public float radius = 100, x, y;
	public Sides side;
	public abstract void setFormation(List<EnemySpawn> enemies, GameState gs);
	public void setSide(Sides side) { this.side = side; }
	
	public void setRadius(float radius) { this.radius = radius; }
	public void setCenterRingPoint(float x, float y) { this.x = x; this.y = y; }
	
	private Formation(boolean needsRecalculation) {
		this.needsRecalculation = needsRecalculation;
	}
}
