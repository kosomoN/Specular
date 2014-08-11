package com.tint.specular.game.spawnsystems;

import java.util.Random;

import com.badlogic.gdx.utils.Pool;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.UpgradeOrb;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class OrbSpawnSystem extends SpawnSystem {

	private Pool<UpgradeOrb> orbPool;
	
	public OrbSpawnSystem(final GameState gs) {
		super(gs);
		rand = new Random();
		orbPool = new Pool<UpgradeOrb>(0, 10) {
			@Override
			protected UpgradeOrb newObject() {
				return new UpgradeOrb(gs);
			}
		};
	}

	public void spawn(float x, float y, float dx, float dy, int amount) {
		UpgradeOrb orb;
		float dir = 360f / amount;
		int offset;
		
		for(int i = 0; i < amount; i++) {
			offset = rand.nextInt(50) - 25;
					
			orb = orbPool.obtain();
			orb.reUse(x, y, dir + offset, dx, dy);
			
			gs.addEntity(orb);
		}
	}
	
	public Pool<UpgradeOrb> getPool() {
		return orbPool;
	}
}
