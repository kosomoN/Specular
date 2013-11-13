package com.tint.specular.game.spawnsystems;

import java.util.Random;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.powerups.PowerUp;

public class PowerUpSpawnSystem {

	private GameState gs;
	
	public PowerUpSpawnSystem(GameState gs) {
		this.gs = gs;
	}
	
	public void spawn(Enemy e) {
		Random rand = new Random();
		int powerUpID = rand.nextInt(PowerUp.Type.values().length);
		gs.addEntity(new PowerUp(PowerUp.Type.values()[powerUpID], e.getX(), e.getY(), gs));
	}
}
