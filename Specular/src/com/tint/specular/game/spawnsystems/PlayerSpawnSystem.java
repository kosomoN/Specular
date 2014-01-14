package com.tint.specular.game.spawnsystems;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.utils.Util;

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
		int x, y;
		boolean playersSpawned = false;
		
		outer :
		do {
			x = (int) (rand.nextInt(gs.getCurrentMap().getWidth() - (int) (Player.getRadius() * 2)) + Player.getRadius());
			y = (int) (rand.nextInt(gs.getCurrentMap().getHeight() - (int) (Player.getRadius() * 2)) + Player.getRadius());
			
			for(Entity ent : gs.getEntities()) {
				if(ent instanceof Enemy) {
					if(Util.getDistanceSquared(x, y, ((Enemy) ent).getX(), ((Enemy) ent).getY()) < 500 * 500) {
						continue outer;
					}
				} else if(ent instanceof Player) {
					if(Util.getDistanceSquared(x, y, ((Player) ent).getCenterX(),
							((Player) ent).getCenterY()) < Player.getRadius() * Player.getRadius() * 4) {
						continue outer;
					}
				}
			}
			
			if(respawn)
				gs.getPlayer().setCenterPosition(x, y);
			else
				gs.addEntity(new Player(gs, x, y, lives));
			
			playersSpawned = true;
		} while(!playersSpawned);
	}
}
