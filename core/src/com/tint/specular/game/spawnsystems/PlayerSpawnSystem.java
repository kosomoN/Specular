package com.tint.specular.game.spawnsystems;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class PlayerSpawnSystem extends SpawnSystem {
	
	public PlayerSpawnSystem(GameState gs) {
		super(gs);
	}
	
	public void spawn(int lives, boolean respawn) {
		int x = gs.getCurrentMap().getWidth() / 2;
		int y = gs.getCurrentMap().getHeight() / 2;
		
		if(respawn) {
			gs.getPlayer().setCenterPosition(x, y);
			gs.getPlayer().respawn();
		} else {
			gs.addEntity(new Player(gs, x, y, lives));
			gs.getPlayer().respawn();
		}
	}
}
