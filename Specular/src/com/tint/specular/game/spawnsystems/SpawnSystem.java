package com.tint.specular.game.spawnsystems;

import java.util.Random;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;

public abstract class SpawnSystem {
	protected GameState gs;
	protected Random rand;
	
	public SpawnSystem(GameState gs) {
		this.gs = gs;
		rand = new Random();
	}
	
	public void spawn(int i) {}
	public void spawn(Enemy e) {}
}
