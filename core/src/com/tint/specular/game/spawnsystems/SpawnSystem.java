package com.tint.specular.game.spawnsystems;

import java.util.Random;

import com.tint.specular.game.GameState;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public abstract class SpawnSystem {
	protected GameState gs;
	protected Random rand;
	
	public SpawnSystem(GameState gs) {
		this.gs = gs;
		rand = new Random();
	}
}
